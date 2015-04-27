package authserver;

/**
 * Constants specific to Authorization server only.
 * @author Venu Karna
 *
 */
public class OAuthASConstants {

	public static final int APPLICATION_ACCESS_TYPE_ONLINE = 1;
	public static final int APPLICATION_ACCESS_TYPE_OFFLINE = 2;
	
	public static final int APPLICATION_ENABLED = 1;
	public static final int APPLICATION_DISABLED = 0;
	
	public static final int APPLICATION_TYPE_WEB_APPLICATION = 1;
	public static final int APPLICATION_TYPE_NATIVE_APPLICATION = 2;
	
	public static final int OAUTH_TOKEN_TYPE_REQUEST = 1;
	public static final int OAUTH_TOKEN_TYPE_ACCESS = 2;
	public static final int OAUTH_TOKEN_TYPE_REFRESH = 3;
	public static final int OAUTH_TOKEN_TYPE_CLIENT = 4;
	
	public static final int OAUTH_TOKEN_DELETED_TRUE = 1;
	public static final int OAUTH_TOKEN_DELETED_FALSE = 0;
	
	public static final int SCOPE_ID_DEFAULT = 1;
	
	
	public static final long ACCESS_TOKEN_AGE = 3153600000L;//100 years - 604800 Seconds-7 days
	public static final long ACCESS_TOKEN_AGE_PERPETUAL = -1;//never expires

	public static final long AUTHORIZATION_TOKEN_AGE = 600;//Seconds - 10mins
	
	public static final long REFRESH_TOKEN_AGE = 31536000;//Seconds - 1 year
	public static final long REFRESH_TOKEN_AGE_PERPETUAL = -1;//never expires
	
	public static final long CLIENT_TOKEN_AGE = 31536000;//Seconds - 1 year
	public static final long CLIENT_TOKEN_AGE_PERPETUAL = -1;//never expires
	
	public static final String ACTION_APPROVED = "1";
	public static final String ACTION_DENIED = "0";
	
	public static final String OAUTH_USER_ACTION = "action";
	public static final String AUTHENTICATION_IDENTIFIER = "id";
}
