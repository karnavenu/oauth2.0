package authserver.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.types.TokenType;
import org.apache.amber.oauth2.ext.dynamicreg.server.request.JSONHttpServletRequestWrapper;
import org.apache.amber.oauth2.ext.dynamicreg.server.request.OAuthServerRegistrationRequest;
import org.apache.amber.oauth2.ext.dynamicreg.server.response.OAuthServerRegistrationResponse;

import authserver.CustomOAuthIssuerImpl;
import authserver.OAuthASConstants;
import authserver.beans.OAuthToken;
import authserver.generators.OAuthTokenGeneratorImpl;
import authserver.request.OAuthAuthzRequestImpl;
import authserver.request.OAuthTokenRequestImpl;
import authserver.utils.AuthorizationServerUtils;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;

/**
 * REST Services specific to authorization server includes 1.OAuth Authorization
 * Endpoint. 2.OAuth Token Endpoint. 3.OAuth Client Registration Enpoint.
 * 4.Endpoint For Access Token Validation For Resource Server.
 * 
 * @author Venu Karna
 * 
 */
@Path("/v1")
public class OAuthASRESTService {

	public OAuthASRESTService() {

	}

	@POST
	@Produces({ "application/x-www-form-urlencoded" })
	@Path("authorize")
	public Response authorizeApp(@Context HttpServletRequest request,
			@Context HttpContext httpContext) throws URISyntaxException,
			OAuthSystemException {
		OAuthAuthzRequestImpl oauthRequest = null;
		CustomOAuthIssuerImpl oauthIssuerImpl = new CustomOAuthIssuerImpl(
				new OAuthTokenGeneratorImpl());

		try {
			oauthRequest = new OAuthAuthzRequestImpl(httpContext.getRequest());

			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
					.authorizationResponse(request,
							HttpServletResponse.SC_FOUND);

			// generate token
			OAuthToken authToken = oauthIssuerImpl
					.authorizationCode(AuthorizationServerUtils.getInstance()
							.getCachedOAuthToken().getEmail());
			authToken.setClientInfo(AuthorizationServerUtils.getInstance()
					.getCachedOAuthToken().getClientInfo());
			authToken.setEmail(AuthorizationServerUtils.getInstance()
					.getCachedOAuthToken().getEmail());

			// persist token
			AuthorizationServerUtils.getInstance().insertOAuthToken(authToken);

			builder.setCode(authToken.getToken());
			if (oauthRequest.getState() != null)
				builder.setParam(OAuth.OAUTH_STATE, oauthRequest.getState());
			// get the redirect uri from client registry if not sent in the
			// request.
			String redirectUri = oauthRequest.getRedirectURI() == null ? AuthorizationServerUtils
					.getInstance().getCachedOAuthToken().getClientInfo()
					.getRedirectUri()
					: oauthRequest.getRedirectURI();

			final OAuthResponse response = builder.location(redirectUri)
					.buildQueryMessage();
			URI url = new URI(response.getLocationUri());

			return Response.status(response.getResponseStatus()).location(url)
					.build();

		} catch (OAuthProblemException e) {
			final Response.ResponseBuilder responseBuilder = Response
					.status(HttpServletResponse.SC_FOUND);
			String redirectUri = e.getRedirectUri() == null ? AuthorizationServerUtils
					.getInstance().getCachedOAuthToken().getClientInfo()
					.getRedirectUri()
					: e.getRedirectUri();

			if (org.apache.amber.oauth2.common.utils.OAuthUtils
					.isEmpty(redirectUri)) {
				throw new WebApplicationException(
						responseBuilder
								.entity("OAuth callback url needs to be provided by client.")
								.type(MediaType.APPLICATION_JSON).build());

			}
			final OAuthResponse response = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_FOUND).error(e)
					.location(redirectUri).buildQueryMessage();
			final URI location = new URI(response.getLocationUri());
			return responseBuilder.location(location).build();
		} catch (OAuthSystemException ose) {
			final Response.ResponseBuilder responseBuilder = Response
					.status(HttpServletResponse.SC_FOUND);

			String redirectUri = oauthRequest.getRedirectURI();

			if (org.apache.amber.oauth2.common.utils.OAuthUtils
					.isEmpty(redirectUri)) {
				throw new WebApplicationException(
						responseBuilder
								.entity("OAuth callback url needs to be provided by client.")
								.type(MediaType.APPLICATION_JSON).build());

			}
			final OAuthResponse response = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_FOUND)
					.setError("Authorization failed")
					.setErrorDescription(ose.getMessage())
					.location(redirectUri).buildQueryMessage();
			final URI location = new URI(response.getLocationUri());
			return responseBuilder.location(location).build();
		}
	}

	@POST
	@Produces({ "application/json" })
	@Path("accesstoken")
	public String accessToken(@Context HttpServletRequest request,
			@Context HttpContext httpContext) throws IOException,
			URISyntaxException {
		OAuthTokenRequestImpl oauthRequest = null;
		CustomOAuthIssuerImpl oauthIssuerImpl = new CustomOAuthIssuerImpl(
				new OAuthTokenGeneratorImpl());

		try {
			oauthRequest = new OAuthTokenRequestImpl(httpContext.getRequest());// handle
																				// exception
																				// in
																				// case
																				// of
																				// params
																				// in
																				// url
																				// TODO

			String rawAccessToken = oauthRequest.getClientId()
					+ System.currentTimeMillis() + "#"
					+ OAuthASConstants.OAUTH_TOKEN_TYPE_ACCESS;
			OAuthToken accessToken = oauthIssuerImpl
					.accessToken(rawAccessToken);

			String rawRefreshToken = oauthRequest.getClientId()
					+ System.currentTimeMillis() + "#"
					+ OAuthASConstants.OAUTH_TOKEN_TYPE_REFRESH;
			OAuthToken refreshToken = oauthIssuerImpl
					.refreshToken(rawRefreshToken);// idea is to have same
													// secret for both access
													// and refresh tokens.

			accessToken.setRefreshToken(refreshToken.getToken());
			accessToken.setClientInfo(AuthorizationServerUtils.getInstance()
					.getCachedOAuthToken().getClientInfo());
			accessToken.setEmail(AuthorizationServerUtils.getInstance()
					.getCachedOAuthToken().getEmail());

			AuthorizationServerUtils.getInstance()
					.insertOAuthToken(accessToken);
			AuthorizationServerUtils.getInstance().resetCachedOAuthToken();// clearing
																			// the
																			// temporarily
																			// cached
																			// token.

			OAuthResponse response = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessToken.getToken())
					.setExpiresIn(String.valueOf(accessToken.getExpiresIn()))
					.setRefreshToken(accessToken.getRefreshToken())
					.setTokenType(TokenType.BEARER.toString()).setScope("")// handle
																			// scope
																			// TODO
					.buildJSONMessage();

			return response.getBody();
		} catch (OAuthProblemException ex) {
			OAuthResponse r = null;
			try {
				r = OAuthResponse.errorResponse(401).error(ex)
						.buildJSONMessage();
			} catch (OAuthSystemException e) {
			}

			return r.getBody();
		} catch (OAuthSystemException e) {
			OAuthResponse r = null;
			try {
				r = OAuthResponse.errorResponse(401).buildJSONMessage();
			} catch (OAuthSystemException ose) {
			}
			return r.getBody();
		}

	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("register")
	public Response register(@Context HttpServletRequest request)
			throws OAuthSystemException {

		OAuthServerRegistrationRequest oauthRequest = null;

		try {
			oauthRequest = new OAuthServerRegistrationRequest(
					new JSONHttpServletRequestWrapper(request));
			AuthorizationServerUtils.getInstance().validateUrlForDomain(
					oauthRequest.getRedirectURI());

			String rawClientToken = oauthRequest.getClientName();
			CustomOAuthIssuerImpl oauthIssuerImpl = new CustomOAuthIssuerImpl(
					new OAuthTokenGeneratorImpl());
			OAuthToken token = oauthIssuerImpl.clientToken(rawClientToken);

			AuthorizationServerUtils.getInstance().registerClient(oauthRequest,
					token);

			DateFormat _fullDateFormat = DateFormat.getDateTimeInstance(
					DateFormat.MEDIUM, DateFormat.MEDIUM);

			String issuedAt = _fullDateFormat.format(token
					.getIssued_timestamp());

			OAuthResponse response = OAuthServerRegistrationResponse
					.status(HttpServletResponse.SC_OK)
					.setClientId(token.getToken())
					.setClientSecret(token.getTokenSecret())
					.setIssuedAt(issuedAt)
					.setExpiresIn(String.valueOf(token.getExpiresIn()))
					.buildJSONMessage();

			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();

		} catch (OAuthProblemException e) {
			OAuthResponse response = OAuthServerRegistrationResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
					.setError("Registration Failed:")
					.setErrorDescription(e.getMessage()).buildJSONMessage();
			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();
		} catch (Exception exc) {
			OAuthResponse response = OAuthServerRegistrationResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
					.setError("Registration Failed:")
					.setErrorDescription(exc.getMessage()).buildJSONMessage();
			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();
		}

	}

	@GET
	@Produces("application/json")
	@Path("validatetoken")
	public OAuthResponse validateAccessToken(
			@QueryParam("access_token") String access_token) {
		OAuthResponse response = null;
		OAuthToken token = null;
		try {
			token = AuthorizationServerUtils.getInstance()
					.getOAuthTokenByTokenId(access_token);
			AuthorizationServerUtils.getInstance().validateToken(token, false);
		} catch (OAuthProblemException oe) {
			try {
				response = OAuthASResponse.tokenResponse(
						HttpServletResponse.SC_UNAUTHORIZED).buildJSONMessage();
			} catch (OAuthSystemException e) {
			}
			return response;
		} catch (Exception e1) {
			try {
				response = OAuthASResponse.tokenResponse(
						HttpServletResponse.SC_NOT_FOUND).buildJSONMessage();
			} catch (OAuthSystemException e) {
			}
			return response;
		}

		try {
			response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(token.getToken())
					.setExpiresIn(String.valueOf(token.getExpiresIn()))
					.setRefreshToken(token.getRefreshToken())
					.buildJSONMessage();
			return response;
		} catch (OAuthSystemException e) {
		}
		return response;
	}

}
