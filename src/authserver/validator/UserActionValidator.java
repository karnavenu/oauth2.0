package authserver.validator;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.utils.OAuthUtils;

import authserver.OAuthASConstants;
import authserver.common.validators.OAuthValidatorImpl;

import com.sun.jersey.api.core.HttpRequestContext;

/**
 * 
 * @author Venu Karna
 *
 */
public class UserActionValidator extends OAuthValidatorImpl<HttpRequestContext> {

    public UserActionValidator() {
        requiredParams.add(OAuthASConstants.OAUTH_USER_ACTION);
    }
    
    @Override
    public void performAllValidations(HttpRequestContext request)
    		throws OAuthProblemException {
    	 validate(getFormParam(request, OAuthASConstants.OAUTH_USER_ACTION));
         
    }

	private void validate(String action) throws OAuthProblemException{
		if(action == null || action.trim().equals(OAuthASConstants.ACTION_DENIED)){
        	throw OAuthUtils.handleOAuthProblemException("access denied by the user.");
        }else if(!action.trim().equals(OAuthASConstants.ACTION_APPROVED)){
        	throw OAuthUtils.handleOAuthProblemException("invalid action performed, expected approve/deny");
        }
	}


}
