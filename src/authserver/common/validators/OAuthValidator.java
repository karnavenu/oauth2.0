package authserver.common.validators;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;

import com.sun.jersey.api.core.HttpRequestContext;

/**
 * Interface for the oauth and custom validators.
 * @author Venu Karna
 *
 * @param <T>
 */
public interface OAuthValidator<T extends HttpRequestContext> {

    public void validateMethod(T request) throws OAuthProblemException;

    public void validateContentType(T request) throws OAuthProblemException;

    public void validateRequiredParameters(T request) throws OAuthProblemException;

    public void validateOptionalParameters(T request) throws OAuthProblemException;

    public void validateNotAllowedParameters(T request) throws OAuthProblemException;

    public void performAllValidations(T request) throws OAuthProblemException;
    
}
