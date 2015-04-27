package authserver.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.utils.OAuthUtils;

import authserver.common.validators.OAuthValidator;
import authserver.validator.OAuthCodeValidator;
import authserver.validator.EmailValidator;
import authserver.validator.OAuthClientValidator;
import authserver.validator.RedirectUriValidator;
import authserver.validator.UserActionValidator;
import authserver.validator.OAuthValidatorEnum;

import com.sun.jersey.api.core.HttpRequestContext;

/**
 * Custom class for authorization request endpoint.
 * 
 * @author Venu Karna
 * 
 */
public class OAuthAuthzRequestImpl extends OAuthRequestImpl {

	protected Map<String, Class<? extends OAuthValidator<HttpRequestContext>>> validators = new HashMap<String, Class<? extends OAuthValidator<HttpRequestContext>>>();

	protected List<Class<? extends OAuthValidator<HttpRequestContext>>> validatorObjs = new ArrayList<Class<? extends OAuthValidator<HttpRequestContext>>>();

	public OAuthAuthzRequestImpl(HttpRequestContext request)
			throws OAuthSystemException, OAuthProblemException {
		super(request);
		initActianceOAuthValdators();
	}

	@Override
	protected OAuthValidator<HttpRequestContext> initValidator()
			throws OAuthProblemException, OAuthSystemException {
		return new OAuthCodeValidator();
	}

	public void initActianceOAuthValdators() throws OAuthProblemException,
			OAuthSystemException {
		/**
		 * keep rest of the validators after @class ActOAuthClientValidator
		 * since its sets client info which are used by later validators.
		 */
		validators.put(OAuthValidatorEnum.CLIENT_VALIDATOR.toString(),
				OAuthClientValidator.class);
		validators.put(OAuthValidatorEnum.REDIRECT_URI_VALIDATOR.toString(),
				RedirectUriValidator.class);
		validators.put(OAuthValidatorEnum.USER_ACTION_VALIDATOR.toString(),
				UserActionValidator.class);
		validators.put(OAuthValidatorEnum.EMAIL_ID_VALIDATOR.toString(),
				EmailValidator.class);

		for (String validator : validators.keySet()) {
			Class<? extends OAuthValidator<HttpRequestContext>> clazz = validators
					.get(validator);
			if (clazz == null) {
				throw OAuthUtils
						.handleOAuthProblemException("invalid validator");
			}
			validatorObjs.add(clazz);
		}
		executeActValidators();
	}

	public void executeActValidators() throws OAuthSystemException,
			OAuthProblemException {
		for (Class<? extends OAuthValidator<HttpRequestContext>> clazz : validatorObjs) {
			OAuthValidator<HttpRequestContext> obj = OAuthUtils
					.instantiateClass(clazz);
			obj.performAllValidations(request);
		}
	}

	public String getState() {
		return getFormParam(OAuth.OAUTH_STATE);
	}

	public String getResponseType() {
		return getFormParam(OAuth.OAUTH_RESPONSE_TYPE);
	}

}
