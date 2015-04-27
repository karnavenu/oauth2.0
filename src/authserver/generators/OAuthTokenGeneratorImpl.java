package authserver.generators;

import java.io.IOException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.commons.lang.RandomStringUtils;

import authserver.beans.OAuthToken;

import com.sun.jersey.core.util.Base64;

/**
 * TODO need to improve this class to generate proper encrypted token and secret
 * keys, for now generating random alphanumeric values.
 * 
 * @author Venu Karna
 * 
 */
public class OAuthTokenGeneratorImpl implements CustomValueGenerator {

	private static final String CRYPTOGRAPHY_ALGO_DES = "DES";

	private static Cipher cipher = null;
	private static DESKeySpec keySpec = null;
	private static SecretKeyFactory keyFactory = null;

	@Override
	public String generateValue() throws OAuthSystemException {
		return null;
	}

	@Override
	public String generateValue(String plainKey) throws OAuthSystemException {
		return null;
	}

	@Override
	public OAuthToken generateToken(String rawtoken) {
		String randomtoken = RandomStringUtils.randomAlphanumeric(32) + "v1";
		String randomtokensecret = RandomStringUtils.randomAlphanumeric(32)
				+ "v1";

		OAuthToken token = new OAuthToken();
		token.setToken(randomtoken);
		token.setTokenSecret(randomtokensecret);
		token.setIssued_timestamp(System.currentTimeMillis());
		return token;

	}

	public static String encrypt(String inputString, String commonKey)
			throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException {

		String encryptedValue = "";
		SecretKey key = getSecretKey(commonKey);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] inputBytes = inputString.getBytes();
		byte[] outputBytes = cipher.doFinal(inputBytes);
		encryptedValue = new String(Base64.encode(outputBytes));
		return encryptedValue;
	}

	public static String decrypt(String encryptedString, String commonKey)
			throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, IOException {
		String decryptedValue = "";
		encryptedString = encryptedString.replace(' ', '+');
		SecretKey key = getSecretKey(commonKey);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] recoveredBytes = cipher.doFinal(Base64.decode(encryptedString
				.getBytes()));
		decryptedValue = new String(recoveredBytes);
		return decryptedValue;
	}

	private static SecretKey getSecretKey(String secretPassword) {
		SecretKey key = null;
		try {
			cipher = Cipher.getInstance(CRYPTOGRAPHY_ALGO_DES);
			keySpec = new DESKeySpec(secretPassword.getBytes("UTF8"));
			keyFactory = SecretKeyFactory.getInstance(CRYPTOGRAPHY_ALGO_DES);
			key = keyFactory.generateSecret(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}
}
