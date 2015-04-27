package resourceserver.services.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.amber.oauth2.common.error.OAuthError;
import org.apache.amber.oauth2.common.message.types.ParameterStyle;
import org.apache.amber.oauth2.rs.request.OAuthAccessResourceRequest;

import resourceserver.exceptions.OAuthRSSystemException;
import resourceserver.services.filter.OAuthRestFilterUtils.FETCH_BY;
import authserver.beans.OAuthToken;

import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

/**
 * REST Filter for the the secure REST APIs exposed. Validates the access token
 * in the request for every access to the secured API.
 * 
 * @author Venu Karna
 * 
 */
public class OAuthRestFilter implements ContainerRequestFilter {

	@Context
	HttpServletRequest httpServletRequest;

	public OAuthRestFilter() {
	}

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		try {

			String accessToken = request.getHeaderValue("authorization");
			OAuthToken token = validateAccessTokenForUser(accessToken,
					FETCH_BY.TOKEN_ID);

			if (token != null) {
				InBoundHeaders header = new InBoundHeaders();
				header.putSingle("authorization", token.getToken());
				header.putSingle("emailID", token.getEmail());
				request.setHeaders(header);
			}

		} catch (OAuthRSSystemException oAuthException) {
			throw oAuthException;
		} catch (Exception e) {
			throw new OAuthRSSystemException("Invalid access token.");
		}
		return request;
	}

	public OAuthToken validateAccessTokenForUser(String id, FETCH_BY fetchBy)
			throws Exception {
		OAuthToken token = null;
		OAuthRestFilterUtils instance = OAuthRestFilterUtils.getInstance();
		if (instance.isInternalAuthServer(httpServletRequest.getServerName())) {
			token = instance.fetchOAuthTokenFromInternalAuthServer(id, fetchBy);
			if (token == null || token.isExpired() || token.isRevoked()) {
				token.setExpired(true);
				throw new OAuthRSSystemException(
						HttpServletResponse.SC_UNAUTHORIZED,
						OAuthError.ResourceResponse.INVALID_TOKEN);
			}
		} else {
			instance.checkAccessTokenFromAuthServer(fetchBy, id);
		}
		return token;
	}

}
