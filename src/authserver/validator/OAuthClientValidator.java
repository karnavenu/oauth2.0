package authserver.validator;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.utils.OAuthUtils;

import authserver.OAuthASConstants;
import authserver.common.validators.OAuthValidatorImpl;
import authserver.utils.AuthorizationServerUtils;

import com.sun.jersey.api.core.HttpRequestContext;

/**
 * 
 * @author Venu Karna
 *
 */
public class OAuthClientValidator extends OAuthValidatorImpl<HttpRequestContext> {

    public OAuthClientValidator() {
        requiredParams.add(OAuth.OAUTH_CLIENT_ID);
        //requiredParams.add(OAuth.OAUTH_CLIENT_SECRET);
    }
    
    
    @Override
    public void performAllValidations(HttpRequestContext request)
    		throws OAuthProblemException {
    	validate(getFormParam(request, OAuth.OAUTH_CLIENT_ID));
    }


	private void validate(String clientId) throws OAuthProblemException{
		try {
			clientInfo = AuthorizationServerUtils.getInstance().getClientInfo(clientId);
			if(clientInfo.isEnabled() == OAuthASConstants.APPLICATION_DISABLED){
				throw OAuthUtils.handleOAuthProblemException("application/client revoked/disabled.");
			}//also need to check for expiry time TODO
			AuthorizationServerUtils.getInstance().getCachedOAuthToken().setClientInfo(clientInfo);
		} catch (Exception e) {
			throw OAuthUtils.handleOAuthProblemException("invalid client id");
		} 
	}


}
