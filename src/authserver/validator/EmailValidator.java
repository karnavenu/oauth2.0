package authserver.validator;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.utils.OAuthUtils;

import authserver.OAuthASConstants;
import authserver.common.validators.OAuthValidatorImpl;
import authserver.utils.AuthorizationServerUtils;

import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.core.util.Base64;

/**
 * 
 * @author Venu Karna
 *
 */
public class EmailValidator extends OAuthValidatorImpl<HttpRequestContext> {

    public EmailValidator() {
        requiredParams.add(OAuthASConstants.AUTHENTICATION_IDENTIFIER);
    }
    
    @Override
    public void performAllValidations(HttpRequestContext request)
    		throws OAuthProblemException {
        validate(getFormParam(request, OAuthASConstants.AUTHENTICATION_IDENTIFIER));
    }

	private void validate(String email) throws OAuthProblemException{
		try {
			email = new String(Base64.decode(email.getBytes()));
			AuthorizationServerUtils.getInstance().getCachedOAuthToken().setEmail(email);

		} catch (Exception e) {
			OAuthProblemException ope = OAuthUtils.handleOAuthProblemException("unauthenticated user.");
			ope.setRedirectUri(AuthorizationServerUtils.getInstance().getCachedOAuthToken().getClientInfo().getRedirectUri());
			throw ope;
		}
	}


}
