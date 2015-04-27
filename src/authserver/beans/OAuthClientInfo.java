package authserver.beans;

import org.apache.amber.oauth2.common.domain.client.ClientInfo;

/**
 * Bean class containing OAuth Client Info.
 * @author Venu Karna
 *
 */
public class OAuthClientInfo implements ClientInfo {
	
	protected long clientUniquetId;
	protected String name;
    protected String clientId;
    protected String clientSecret;
    protected String redirectUri;
    protected String clientUri;
    protected String description;
    protected String iconUri;
    protected Long issuedTimestamp;
    protected Long expiryTimestamp;
    protected Long modTimestamp;
    protected int enabled;
    protected int accessType;
    protected int scopeId;
    
    protected String shareResponseURL;
    protected String urlHttpMethod;
    protected String shareResponseUsername;
    protected String shareResponsePassword;
    

	public long getClientUniquetId() {
		return clientUniquetId;
	}

	public void setClientUniquetId(long clientUniquetId) {
		this.clientUniquetId = clientUniquetId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public void setClientUri(String clientUri) {
		this.clientUri = clientUri;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public void setIssuedAt(Long issuedTimestamp) {
		this.issuedTimestamp = issuedTimestamp;
	}

	public void setExpiresIn(Long expiryTimestamp) {
		this.expiryTimestamp = expiryTimestamp;
	}

	@Override
	public String getClientId() {
		return clientId;
	}

	@Override
	public String getClientSecret() {
		return clientSecret;
	}

	@Override
	public String getClientUri() {
		return clientUri;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Long getExpiresIn() {
		return expiryTimestamp;
	}

	@Override
	public String getIconUri() {
		return iconUri;
	}

	@Override
	public Long getIssuedAt() {
		return issuedTimestamp;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getRedirectUri() {
		return redirectUri;
	}

	public Long getModTimestamp() {
		return modTimestamp;
	}

	public void setModTimestamp(Long modTimestamp) {
		this.modTimestamp = modTimestamp;
	}

	public int isEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public int getAccessType() {
		return accessType;
	}

	public void setAccessType(int accessType) {
		this.accessType = accessType;
	}

	public int getScopeId() {
		return scopeId;
	}

	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}
	
	public String getShareResponseURL() {
		return shareResponseURL;
	}

	public void setShareResponseURL(String shareResponseURL) {
		this.shareResponseURL = shareResponseURL;
	}	

	public String getShareResponseUsername() {
		return shareResponseUsername;
	}

	public void setShareResponseUsername(String shareResponseUsername) {
		this.shareResponseUsername = shareResponseUsername;
	}

	public String getShareResponsePassword() {
		return shareResponsePassword;
	}

	public void setShareResponsePassword(String shareResponsePassword) {
		this.shareResponsePassword = shareResponsePassword;
	}
	
	public String getUrlHttpMethod() {
		return urlHttpMethod;
	}

	public void setUrlHttpMethod(String urlHttpMethod) {
		this.urlHttpMethod = urlHttpMethod;
	}

	
}
