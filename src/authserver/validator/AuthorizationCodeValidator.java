package authserver.validator;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.utils.OAuthUtils;

import authserver.beans.OAuthToken;
import authserver.common.validators.OAuthValidatorImpl;
import authserver.utils.AuthorizationServerUtils;

import com.sun.jersey.api.core.HttpRequestContext;

/**
 * 
 * @author Venu Karna
 *
 */
public class AuthorizationCodeValidator extends OAuthValidatorImpl<HttpRequestContext> {

    public AuthorizationCodeValidator() {
        requiredParams.add(OAuth.OAUTH_GRANT_TYPE);
        requiredParams.add(OAuth.OAUTH_CLIENT_ID);
        requiredParams.add(OAuth.OAUTH_CODE);
        //requiredParams.add(OAuth.OAUTH_REDIRECT_URI); if not received in token request get from client registry. 
        requiredParams.add(OAuth.OAUTH_CLIENT_SECRET);
    }
    
    @Override
    public void performAllValidations(HttpRequestContext request)
    		throws OAuthProblemException {
    	validate(getFormParam(request, OAuth.OAUTH_CODE));
    }


	private void validate(String code) throws OAuthProblemException{
		OAuthToken token;
		//token = AuthorizationServerUtils.getInstance().getOAuthToken(code,AuthorizationServerUtils.getInstance().getCachedOAuthToken().getClientInfo().getClientId());
		token = AuthorizationServerUtils.getInstance().getOAuthTokenByTokenId(code);
        if(token == null)
        	throw OAuthUtils.handleOAuthProblemException("invalid code");
        AuthorizationServerUtils.getInstance().validateToken(token, false);
		AuthorizationServerUtils.getInstance().getCachedOAuthToken().setEmail(token.getEmail());
	}


}
