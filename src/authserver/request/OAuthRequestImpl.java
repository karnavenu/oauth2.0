package authserver.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.utils.OAuthUtils;

import authserver.common.validators.OAuthValidator;

import com.sun.jersey.api.core.HttpRequestContext;

/**
 * Abstract class for oauth endpoint requests.
 * @author Venu Karna
 *
 */
public abstract class OAuthRequestImpl {


    protected HttpRequestContext request;
    protected OAuthValidator<HttpRequestContext> validator;
    protected Map<String, Class<? extends OAuthValidator<HttpRequestContext>>> validators =
        new HashMap<String, Class<? extends OAuthValidator<HttpRequestContext>>>();
    
    

    public OAuthRequestImpl(HttpRequestContext request) throws OAuthSystemException, OAuthProblemException {
        this.request = request;
        validate();
    }

    public OAuthRequestImpl() {
    }

    protected void validate() throws OAuthSystemException, OAuthProblemException {
        try {
            validator = initValidator();
            validator.validateMethod(request);
            validator.validateContentType(request);
            validator.validateRequiredParameters(request);
            validator.performAllValidations(request);
        } catch (OAuthProblemException e) {
            try {
                String redirectUri = request.getFormParameters().get(OAuth.OAUTH_REDIRECT_URI).get(0);
                if (!OAuthUtils.isEmpty(redirectUri)) {
                    e.setRedirectUri(redirectUri);
                }
            } catch (Exception ex) {}

            throw e;
        }

    }

    protected abstract OAuthValidator<HttpRequestContext> initValidator() throws OAuthProblemException,
        OAuthSystemException;

    public String getFormParam(String name){
        return request.getFormParameters().get(name) == null?null:request.getFormParameters().get(name).get(0);
    }

    public String getClientId() {
        return getFormParam(OAuth.OAUTH_CLIENT_ID);
    }

    public String getRedirectURI() {
        return getFormParam(OAuth.OAUTH_REDIRECT_URI);
    }

    public String getClientSecret() {
        return getFormParam(OAuth.OAUTH_CLIENT_SECRET);
    }

    public Set<String> getScopes() {
        String scopes = getFormParam(OAuth.OAUTH_SCOPE);
        return OAuthUtils.decodeScopes(scopes);
    }
    
    public void validateContentType() throws OAuthProblemException{
    	String contentType = request.getMediaType().toString();
        final String expectedContentType = OAuth.ContentType.URL_ENCODED;
        if (!OAuthUtils.hasContentType(contentType, expectedContentType)) {
            throw OAuthUtils.handleBadContentTypeException(expectedContentType);
        }
    }

}
