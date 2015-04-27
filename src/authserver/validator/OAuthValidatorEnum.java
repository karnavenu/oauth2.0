package authserver.validator;

/**
 * 
 * @author Venu Karna
 *
 */
public enum OAuthValidatorEnum {
    // NONE("none"),
    AUTHORIZATION_CODE_VALIDATOR("authorization_code_validator"),
    REDIRECT_URI_VALIDATOR("redirect_uri_validator"),
    CLIENT_VALIDATOR("client_validator"),
    EMAIL_ID_VALIDATOR("email_id_validator"),
    USER_ACTION_VALIDATOR("user_action_validator");

    private String validatorType;

    OAuthValidatorEnum(String validatorType) {
        this.validatorType = validatorType;
    }

    @Override
    public String toString() {
        return validatorType;
    }
}
