package authserver.validator;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.utils.OAuthUtils;
import org.apache.amber.oauth2.ext.dynamicreg.common.OAuthRegistration;

import authserver.common.validators.OAuthValidatorImpl;

import com.sun.jersey.api.core.HttpRequestContext;

/**
 * 
 * @author Venu Karna
 *
 */
public class ServerRegistrationValidator extends OAuthValidatorImpl<HttpRequestContext> {

    public ServerRegistrationValidator() {
    	requiredParams.add(OAuthRegistration.Request.CLIENT_NAME);
    	requiredParams.add(OAuthRegistration.Request.REDIRECT_URL);
    	requiredParams.add(OAuthRegistration.Request.TYPE);
    }

	@Override
	public void validateContentType(HttpRequestContext request)
			throws OAuthProblemException {
		String contentType = request.getMediaType().toString();
        final String expectedContentType = OAuth.ContentType.JSON;
        if (!OAuthUtils.hasContentType(contentType, expectedContentType)) {
            throw OAuthUtils.handleBadContentTypeException(expectedContentType);
        }
	}
}
