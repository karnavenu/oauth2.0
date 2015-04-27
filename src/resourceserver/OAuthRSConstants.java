package resourceserver;

/**
 * TODO change oauthserverregistraionrequest to custom.
 * TODO token generation logic and decryption logic.
 * TODO handling scopes
 * TODO handling reidrect uri incase if its not in auth code request.
 * TODO convert authorize app api to POST.
 * 
 * Constants specific to Resource Server.
 * @author Venu Karna
 *
 */
public class OAuthRSConstants {

	public static final String VALIDATE_ACCESSTOKEN_ENDPOINT = "/oauth/as/v1/validatetoken?access_token=";
	public static final String AUTHORIZE_DIALOG_URI = "/oauth/authdialog";
	public static final String LOGIN_URI = "/oauth/authorize";
	public static final String TOKENBYEMAILID_ENDPOINT = "/oauth/as/v1/tokenbyemailid?emailId=";
    public static final String OAUTH_LOGIN_FAILED = "Login Failed.";
}
