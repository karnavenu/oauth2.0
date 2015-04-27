package resourceserver.services.filter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.codehaus.jettison.json.JSONObject;

import resourceserver.OAuthRSConstants;
import resourceserver.exceptions.OAuthRSSystemException;
import resourceserver.utils.ResourceServerUtils;
import authserver.beans.OAuthToken;
import authserver.utils.AuthorizationServerUtils;

public class OAuthRestFilterUtils {

	private static OAuthRestFilterUtils _obj = null;

	private OAuthRestFilterUtils() {
	}

	public static OAuthRestFilterUtils getInstance() {
		if (_obj == null) {
			_obj = new OAuthRestFilterUtils();
		}
		return _obj;
	}

	public enum FETCH_BY {
		EMAIL_ID, TOKEN_ID;
	}

	public boolean isInternalAuthServer(String requestServerName)
			throws Exception {
		String authServer = null;
		if (authServer == null || authServer.trim().equals("")) {
			return true;
		}
		URL authServerUrl = new URL(authServer);
		authServer = authServerUrl.getHost();
		if (authServer != null && authServer.equals(requestServerName)) {
			return true;
		} else {

			return false;
		}
	}

	public OAuthToken fetchOAuthTokenFromInternalAuthServer(String id,
			FETCH_BY fetchBy) throws OAuthProblemException {
		OAuthToken token = null;
		switch (fetchBy) {
		case EMAIL_ID:
			token = AuthorizationServerUtils.getInstance()
					.getOAuthTokenByEmailId(id);
			break;
		case TOKEN_ID:
			token = AuthorizationServerUtils.getInstance()
					.getOAuthTokenByTokenId(id);
			break;
		}
		if (token != null) {
			AuthorizationServerUtils.getInstance().validateToken(token, true);
		}

		return token;
	}

	public String readAuthServerResponse(InputStream io) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(io));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		String s = sb.toString();
		JSONObject json = new JSONObject(s);
		int responseStatus = Integer.valueOf(json.getString("responseStatus"));
		if (responseStatus == HttpServletResponse.SC_NOT_FOUND) {
			throw new OAuthRSSystemException("invalid access token");
		} else if (responseStatus == HttpServletResponse.SC_UNAUTHORIZED) {
			throw new OAuthRSSystemException("access token expired.");
		} else if (responseStatus == HttpServletResponse.SC_OK) {
			String body = json.get("body").toString();// gets the token info.
			return body;
		}
		return null;
	}

	public String checkAccessTokenFromAuthServer(FETCH_BY fetchBy, String id)
			throws Exception {
		OAuthRestFilterUtils.getInstance().trustCertificates();

		String authServer = null;
		if (authServer == null)
			throw new OAuthRSSystemException("authorization server not found.");

		HttpsURLConnection connection = null;

		switch (fetchBy) {
		case EMAIL_ID:
			connection = (HttpsURLConnection) new URL(authServer
					+ OAuthRSConstants.TOKENBYEMAILID_ENDPOINT + id)
					.openConnection();
			break;

		case TOKEN_ID:
			connection = (HttpsURLConnection) new URL(authServer
					+ OAuthRSConstants.VALIDATE_ACCESSTOKEN_ENDPOINT + id)
					.openConnection();
			break;
		}

		if (connection.getResponseCode() != 200) {
			throw new OAuthRSSystemException(
					"error connecting authorization server.");
		}

		String jsonString = OAuthRestFilterUtils.getInstance()
				.readAuthServerResponse(connection.getInputStream());

		connection.disconnect();

		return jsonString;
	}

	public void trustCertificates() {
		X509TrustManager tm = new X509TrustManager() {

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		};

		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				// logger.info("URL Host: " + urlHostName+ " vs. " +
				// session.getPeerHost());
				return true;
			}
		};

		try {
			trustAllHttpsCertificates();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpsURLConnection.setDefaultHostnameVerifier(hv);

		// Use the trust manager to get an SSLSocketFactory
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			sslContext.init(null, new TrustManager[] { tm }, null);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		SSLSocketFactory socketFactory = sslContext.getSocketFactory();

		HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);

	}

	private static void trustAllHttpsCertificates() throws Exception {
		// Create a trust manager that does not validate certificate chains:
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
				.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
	}

	public static class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}
}
