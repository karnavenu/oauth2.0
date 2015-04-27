package authserver;

import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import authserver.beans.OAuthToken;
import authserver.generators.CustomValueGenerator;


/**
 * @author Venu Karna
 *
 */
public class CustomOAuthIssuerImpl extends OAuthIssuerImpl {
	
	CustomValueGenerator vg = null;

	public CustomOAuthIssuerImpl(CustomValueGenerator vg) {
		super(vg);
		this.vg = vg;
	}
	
	public OAuthToken authorizationCode(String rawToken) throws OAuthSystemException {
		OAuthToken token =  vg.generateToken(rawToken);
		token.setTokenType(OAuthASConstants.OAUTH_TOKEN_TYPE_REQUEST);
		token.setExpiresIn(OAuthASConstants.AUTHORIZATION_TOKEN_AGE);
		return token;
	}
	
	public OAuthToken refreshToken(String rawToken) throws OAuthSystemException {
		OAuthToken token =  vg.generateToken(rawToken);
		token.setTokenType(OAuthASConstants.OAUTH_TOKEN_TYPE_REFRESH);
		token.setExpiresIn(OAuthASConstants.REFRESH_TOKEN_AGE);
		return token;
	}
	
	public OAuthToken accessToken(String rawToken) throws OAuthSystemException {
		OAuthToken token =  vg.generateToken(rawToken);
		token.setTokenType(OAuthASConstants.OAUTH_TOKEN_TYPE_ACCESS);
		token.setExpiresIn(OAuthASConstants.ACCESS_TOKEN_AGE);//we can instead give the actual expiry timestamp if required.
		return token;
	}
	
	public OAuthToken clientToken(String rawToken) throws OAuthSystemException {
		OAuthToken token =  vg.generateToken(rawToken);
		token.setTokenType(OAuthASConstants.OAUTH_TOKEN_TYPE_CLIENT);
		token.setExpiresIn(OAuthASConstants.CLIENT_TOKEN_AGE);
		return token;
	}

}
