
package OadrSample;

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import org.himalay.commandline.CLTBase
import org.himalay.commandline.CLTBaseQuiet
import org.himalay.commandline.Configure
import org.himalay.commandline.Option
import groovy.cli.commons.OptionAccessor
import org.slf4j.Logger

import org.slf4j.LoggerFactory;


public class VenSimulator extends CLTBaseQuiet{
	public static final Logger logger = LoggerFactory.getLogger(VenSimulator.class);
	
	public static VenSimulator INSTANCE = null;
	
	@Option(required = true, description='https://novadre.nebland.com:4433/oadrapi/ven/krishna')
	String url ;//
	@Option(required = true)
	String venId ;//= "krishnaVen";
	@Option(required = true, description='example: config\\certs\\Nova.keyStore.jks')
	String keyStorePath = "config\\certs\\Nova.keyStore.jks";
	@Option(required = true, description='KeyStore password')
	String keyStorePassword;
	@Option(required = true, description='Print SSL debugging output', numberOfOptions=0)
	boolean debugSSL;
	
	@Override
	protected void realMain(OptionAccessor options) {
		SampleVen sampleVen = new SampleVen(url, venId, keyStorePath, keyStorePassword);
		sampleVen.testRegister();
	}
	
    public static void main(String[] args) {
		debugSsl();
		INSTANCE = new VenSimulator();
		
		CLTBase._main(INSTANCE, args)
		
	}
    
	
	
	private static void debugSsl() {
		System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.conn", "DEBUG");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.client", "DEBUG");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.client", "DEBUG");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");
	}

}
