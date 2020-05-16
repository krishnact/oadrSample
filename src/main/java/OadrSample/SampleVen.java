package OadrSample;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.xml.bind.JAXBException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avob.openadr.client.http.OadrHttpClient;
import com.avob.openadr.client.http.oadr20b.OadrHttpClient20b;
import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.security.exception.OadrSecurityException;

public class SampleVen {

	Logger logger = LoggerFactory.getLogger(SampleVen.class);
	String url   ;        
	String venId   ;      
	String keyStorePath  ;
	String keyStorePassword ;
	HttpClientContext defaultLocalContext;

	
	public SampleVen(String url, String venId, String keyStorePath, String keyStorePassword)throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, JAXBException, OadrSecurityException, URISyntaxException, Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, IOException {
		super();
		this.url = url;
		this.venId = venId;
		this.keyStorePath = keyStorePath;
		this.keyStorePassword = keyStorePassword;
		logger.info("Ven created");
	}

//	public SampleVen() 
//		
//
//		//queryRegister();
//		testRegister();//
//	}
	
	void testRegister()  throws JAXBException, OadrSecurityException, URISyntaxException, Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		OadrCreatePartyRegistrationType  payload = new OadrCreatePartyRegistrationType();
		
		Object retVal = null;
		payload.setRequestID("" +System.currentTimeMillis()%1000);
		payload.setVenID(venId);
		OadrHttpVenClient20b venCl = getOADRVenHttpClient();
		retVal = venCl.oadrCreatePartyRegistration(payload);
		
		logger.info(retVal.toString());
	}
	
	void queryRegister()  throws JAXBException, OadrSecurityException, URISyntaxException, Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {

		OadrQueryRegistrationType payload = new OadrQueryRegistrationType();
		Object retVal = null;
		
		payload.setRequestID("" +System.currentTimeMillis()%1000);
		//payload.setSchemaVersion("2.0b");
		OadrHttpVenClient20b venCl = getOADRVenHttpClient();
		retVal = venCl.oadrQueryRegistrationType(payload);
		
        System.out.println(retVal);
	}

	
	private OadrHttpVenClient20b getOADRVenHttpClient() throws JAXBException, OadrSecurityException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		defaultLocalContext = new HttpClientContext();
		CloseableHttpClient httpClient1 = SSLHelper.getHttpsClient(keyStorePath, keyStorePassword);
		URI uri = new java.net.URI(url);
		OadrHttpClient cl = new OadrHttpClient((HttpClient) httpClient1, uri, defaultLocalContext);
		OadrHttpClient20b client = new OadrHttpClient20b(cl);
		OadrHttpVenClient20b venCl = new OadrHttpVenClient20b(client);
		return venCl;
	}
	
}
