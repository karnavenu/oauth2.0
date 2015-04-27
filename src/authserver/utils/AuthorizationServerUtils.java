package authserver.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.utils.OAuthUtils;
import org.apache.amber.oauth2.ext.dynamicreg.server.request.OAuthServerRegistrationRequest;

import authserver.OAuthASConstants;
import authserver.beans.OAuthClientInfo;
import authserver.beans.OAuthToken;

/**
 * Utils class specific to OAuth Authorization Server.
 * 
 * @author Venu Karna
 * 
 */
public class AuthorizationServerUtils {
	private static AuthorizationServerUtils _obj = null;
	private OAuthToken tokenCache = null;

	private AuthorizationServerUtils() {

	}

	public static AuthorizationServerUtils getInstance() {
		if (_obj == null) {
			synchronized (AuthorizationServerUtils.class) {
				if (_obj == null) {
					_obj = new AuthorizationServerUtils();
				}
			}
		}
		return _obj;
	}

	public OAuthToken getOAuthToken(String tokenId, String clientId)
			throws OAuthSystemException {
		OAuthToken token = null;

		return token;
	}

	public OAuthToken getOAuthTokenByTokenId(String tokenId) {
		OAuthToken token = null;

		return token;
	}

	public OAuthToken getOAuthTokenByEmailId(String emailId)
			throws OAuthProblemException {
		if (emailId == null || emailId.trim().equals(""))
			return null;
		OAuthToken token = null;
		return token;
	}

	public void registerClient(OAuthServerRegistrationRequest request,
			OAuthToken token) throws Exception {

	}

	public void updateClientRegistration(OAuthServerRegistrationRequest request)
			throws Exception {

	}

	public OAuthClientInfo getClientInfo(String clientId) {

		OAuthClientInfo clientInfo = null;

		return clientInfo;
	}

	public void insertOAuthToken(OAuthToken token) throws OAuthSystemException {

	}

	public void validateDomain(String redirectUri1, String redirectUri2)
			throws OAuthProblemException {
		try {
			URL url1 = new URL(redirectUri1);
			URL url2 = new URL(redirectUri2);
			if (!url1.getHost().equalsIgnoreCase(url2.getHost()))
				throw OAuthUtils
						.handleOAuthProblemException("invalid redirect url");

		} catch (MalformedURLException e) {
			throw OAuthUtils
					.handleOAuthProblemException("invalid redirect url");
		}
	}

	public OAuthToken getCachedOAuthToken() {
		if (tokenCache == null) {
			tokenCache = new OAuthToken();
		}
		return tokenCache;
	}

	public void resetCachedOAuthToken() {
		if (tokenCache != null) {
			tokenCache = null;
		}
	}

	public OAuthToken validateToken(OAuthToken token, boolean setBean)
			throws OAuthProblemException {
		if (token == null) {
			throw OAuthUtils
					.handleOAuthProblemException("invalid token, cannot be null");
		} else if (token.getIsDeleted() == OAuthASConstants.OAUTH_TOKEN_DELETED_TRUE) {
			if (setBean)
				token.setIsDeleted(OAuthASConstants.OAUTH_TOKEN_DELETED_TRUE);
			else
				throw OAuthUtils
						.handleOAuthProblemException("token access revoked");
		} else if (token.getTokenType() == OAuthASConstants.OAUTH_TOKEN_TYPE_REQUEST) {
			long currentTime = System.currentTimeMillis();
			long authCodeIssueTime = token.getIssued_timestamp();
			long deltaTimeInSecs = (currentTime - authCodeIssueTime) / 1000;
			if (deltaTimeInSecs > OAuthASConstants.AUTHORIZATION_TOKEN_AGE) {
				if (setBean)
					token.setExpired(true);
				else
					throw OAuthUtils
							.handleOAuthProblemException("authorization code expired");
			}
		} else if (token.getTokenType() == OAuthASConstants.OAUTH_TOKEN_TYPE_ACCESS
				&& token.getExpiresIn() != OAuthASConstants.ACCESS_TOKEN_AGE_PERPETUAL) {
			long currentTime = System.currentTimeMillis();
			long issuedTime = token.getIssued_timestamp();
			long deltaTimeInSecs = (currentTime - issuedTime) / 1000;
			if (deltaTimeInSecs > OAuthASConstants.ACCESS_TOKEN_AGE) {
				if (setBean)
					token.setExpired(true);
				else
					throw OAuthUtils
							.handleOAuthProblemException("access token expired");
			}
		}
		return token;
	}

	public boolean validateUrlForDomain(String redirectUrl)
			throws OAuthProblemException {

		try {
			new URL(redirectUrl);
		} catch (MalformedURLException e) {
			throw OAuthUtils.handleOAuthProblemException(e.getMessage());
		}
		return true;
	}

}
