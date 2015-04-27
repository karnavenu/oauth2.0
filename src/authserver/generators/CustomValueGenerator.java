package authserver.generators;

import org.apache.amber.oauth2.as.issuer.ValueGenerator;

import authserver.beans.OAuthToken;
/**
 * @author Venu Karna
 *
 */
public interface CustomValueGenerator extends ValueGenerator {

	public OAuthToken generateToken(String rawtoken);
}
