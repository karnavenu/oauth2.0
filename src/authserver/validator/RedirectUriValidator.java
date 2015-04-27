package authserver.validator;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;

import authserver.common.validators.OAuthValidatorImpl;
import authserver.utils.AuthorizationServerUtils;

import com.sun.jersey.api.core.HttpRequestContext;

/**
 * 
 * @author Venu Karna
 *
 */
public class RedirectUriValidator extends OAuthValidatorImpl<HttpRequestContext> {

    public RedirectUriValidator() {
        requiredParams.add(OAuth.OAUTH_REDIRECT_URI);
    }

    @Override
    public void performAllValidations(HttpRequestContext request)
    		throws OAuthProblemException {
    	validate(getFormParam(request,OAuth.OAUTH_REDIRECT_URI));
            
    }
    
    /**
     * 
     * @param redirectUri if null then the redirectUri is taken from client registry.
     * @throws OAuthProblemException
     */
    private void validate(String redirectUri) throws OAuthProblemException{
    	if(redirectUri != null){
            AuthorizationServerUtils.getInstance().validateDomain(redirectUri, 
                    AuthorizationServerUtils.getInstance().getCachedOAuthToken().getClientInfo().getRedirectUri());
    	}
    }
    


}
