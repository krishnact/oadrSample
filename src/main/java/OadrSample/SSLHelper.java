package OadrSample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class SSLHelper {

	public static SSLConnectionSocketFactory getSSLSockConnF(KeyStore cks, String password, TrustManager[] tm)
			throws Exception {
		String[] protocols = new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" };
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(createSslCustomContext(cks, password, tm),
				protocols, // Allowed protocols
				null, new MyHostnameVerifier()// SSLConnectionSocketFactory.getDefaultHostnameVerifier()
		);

		return csf;
	}

	public static SSLContext createSslCustomContext(KeyStore ks, String password, TrustManager[] tms) throws Exception {
		if (tms == null) {
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(ks);
			tms = tmf.getTrustManagers();

		}
		KeyManager[] kms = getKeyManagers(ks, password);

		SSLContext sslContext = SSLContext.getInstance("TLS");
		// sslContext.init(null, tmf.getTrustManagers(), null);
		sslContext.init(kms, tms, null);
		return sslContext;

	}

	public static KeyManager[] getKeyManagers(KeyStore ks, String password) throws Exception {
		KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmfactory.init(ks, password.toCharArray());
		return kmfactory.getKeyManagers();
	}
	
	public static KeyStore getKeyStore(String filePath, String keyStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
	    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		char[] pwdArray = keyStorePassword.toCharArray();
		ks.load(new FileInputStream(filePath), pwdArray);
		return ks;
	}
	
	public static CloseableHttpClient getHttpsClient(String keyStorePath, String keyStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{
		
		KeyStore ks = SSLHelper.getKeyStore(keyStorePath, keyStorePassword);
		HttpClientBuilder clientbuilder = HttpClients.custom();
		try {
			clientbuilder = clientbuilder.disableContentCompression().
					setSSLSocketFactory(SSLHelper.getSSLSockConnF(ks, keyStorePassword,  getGullibleTrustManagers()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
		//Building the CloseableHttpClient
		CloseableHttpClient httpclient = clientbuilder.build();
		return httpclient;
	}
	
    public static class MyHostnameVerifier implements javax.net.ssl.HostnameVerifier
    {

        @Override
        public boolean verify(String hostname, SSLSession session) {
                return true;
        }

}

	
	public static TrustManager[] getGullibleTrustManagers() {
	    return new  TrustManager[] {new GullibleTrustManager()};
	}
	
	
	public static class GullibleTrustManager implements X509TrustManager{
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		public void checkClientTrusted(
			java.security.cert.X509Certificate[] certs, String authType) {
		}
		public void checkServerTrusted(
			java.security.cert.X509Certificate[] certs, String authType) {
		}
	}
}
