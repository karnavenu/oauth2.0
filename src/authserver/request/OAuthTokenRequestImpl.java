package authserver.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.amber.oauth2.common.utils.OAuthUtils;

import authserver.common.validators.OAuthValidator;
import authserver.validator.AuthorizationCodeValidator;
import authserver.validator.OAuthClientValidator;
import authserver.validator.RedirectUriValidator;
import authserver.validator.OAuthValidatorEnum;

import com.sun.jersey.api.core.HttpRequestContext;

/**
 * Custom class for oauth token generation endpoint.
 * @author Venu Karna
 *
 */
public class OAuthTokenRequestImpl extends OAuthRequestImpl {

	protected Map<String, Class<? extends OAuthValidator<HttpRequestContext>>> validators =
	        new HashMap<String, Class<? extends OAuthValidator<HttpRequestContext>>>();
	
	protected List<Class<? extends OAuthValidator<HttpRequestContext>>> validatorObjs = 
			new ArrayList<Class<? extends OAuthValidator<HttpRequestContext>>>();
	
	public OAuthTokenRequestImpl(HttpRequestContext request)
			throws OAuthSystemException, OAuthProblemException {
		super(request);
		initActValidators();
		executeActValidators();
	}
	
	
	@Override
	protected OAuthValidator<HttpRequestContext> initValidator() throws OAuthProblemException, OAuthSystemException {
		
		/*Currently not implemented.
		 * validators.put(GrantType.PASSWORD.toString(), PasswordValidator.class);
		validators.put(GrantType.CLIENT_CREDENTIALS.toString(), ClientCredentialValidator.class);
		validators.put(GrantType.REFRESH_TOKEN.toString(), RefreshTokenValidator.class);*/
        validators.put(GrantType.AUTHORIZATION_CODE.toString(), AuthorizationCodeValidator.class);
        validateContentType();
        String requestTypeValue = getFormParam(OAuth.OAUTH_GRANT_TYPE);
        if (OAuthUtils.isEmpty(requestTypeValue)) {
            throw OAuthUtils.handleOAuthProblemException("Missing grant_type parameter value");
        }
        Class<? extends OAuthValidator<HttpRequestContext>> clazz = validators.get(requestTypeValue);
        if (clazz == null) {
            throw OAuthUtils.handleOAuthProblemException("Invalid grant_type parameter value");
        }
        return OAuthUtils.instantiateClass(clazz);
	}
	
	public void initActValidators() throws OAuthProblemException{
		validators.put(OAuthValidatorEnum.AUTHORIZATION_CODE_VALIDATOR.toString(), AuthorizationCodeValidator.class);
		/**
		 * keep rest of the validators after @class ActOAuthClientValidator since its sets client info which are used
		 * by later validators.
		 */
		validators.put(OAuthValidatorEnum.CLIENT_VALIDATOR.toString(), OAuthClientValidator.class);
		validators.put(OAuthValidatorEnum.REDIRECT_URI_VALIDATOR.toString(), RedirectUriValidator.class);
		
		for(String validator:validators.keySet()){
			Class<? extends OAuthValidator<HttpRequestContext>> clazz = validators.get(validator);
	        if (clazz == null) {
	            throw OAuthUtils.handleOAuthProblemException("invalid validator");
	        }
	        validatorObjs.add(clazz);
		}
	}
	
	public void executeActValidators() throws OAuthSystemException, OAuthProblemException{
		for (Class<? extends OAuthValidator<HttpRequestContext>> clazz : validatorObjs){
			OAuthValidator<HttpRequestContext> obj = OAuthUtils.instantiateClass(clazz);
			obj.performAllValidations(request);
		}
	}
	
	
    public String getCode() {
        return getFormParam(OAuth.OAUTH_CODE);
    }

}
