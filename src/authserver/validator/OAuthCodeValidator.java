package authserver.validator;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.error.OAuthError;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;

import authserver.common.validators.OAuthValidatorImpl;

import com.sun.jersey.api.core.HttpRequestContext;

/**
 * 
 * @author Venu Karna
 *
 */
public class OAuthCodeValidator extends OAuthValidatorImpl<HttpRequestContext> {

    public OAuthCodeValidator() {
        requiredParams.add(OAuth.OAUTH_RESPONSE_TYPE);
        requiredParams.add(OAuth.OAUTH_CLIENT_ID);
    }

    @Override
    public void validateMethod(HttpRequestContext request) throws OAuthProblemException {
        String method = request.getMethod();
        if (!OAuth.HttpMethod.GET.equals(method) && !OAuth.HttpMethod.POST.equals(method)) {
            throw OAuthProblemException.error(OAuthError.CodeResponse.INVALID_REQUEST)
                .description("Method not correct.");
        }
    }

    @Override
    public void validateContentType(HttpRequestContext request) throws OAuthProblemException {
    }

}
