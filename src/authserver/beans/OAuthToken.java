package authserver.beans;

/**
 * Bean class containing OAuth Token (access token,auth code) info.
 * @author Venu Karna
 *
 */
public class OAuthToken {
	
	private String token;
	private String tokenSecret;
	private int tokenType;
	private long issued_timestamp;
	private long modTimestamp;
	private String email;
	private OAuthClientInfo clientInfo;
	private long expiresIn;
	private String refreshToken;
	private int isDeleted;
	private boolean expired;
	private boolean revoked;
	
	public OAuthToken(){
		
	}
	
	public OAuthToken(String token,String tokenSecret,int tokenType){
		this.token = token;
		this.tokenSecret = tokenSecret;
		this.tokenType = tokenType;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTokenSecret() {
		return tokenSecret;
	}
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
	public int getTokenType() {
		return tokenType;
	}
	public void setTokenType(int tokenType) {
		this.tokenType = tokenType;
	}

	public long getIssued_timestamp() {
		return issued_timestamp;
	}

	public void setIssued_timestamp(long issued_timestamp) {
		this.issued_timestamp = issued_timestamp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public OAuthClientInfo getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(OAuthClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public long getModTimestamp() {
		return modTimestamp;
	}

	public void setModTimestamp(long modTimestamp) {
		this.modTimestamp = modTimestamp;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	

}
