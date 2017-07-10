package org.onap.demo.integration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.SslConfigurator;

//import com.obrienlabs.gps.business.entity.Record;
//import com.obrienlabs.gps.business.entity.User;

public class JAXRSClient {

	private static final String HTTP_PORT = "http://biometric.elasticbeanstalk.com/rest/read";
    
	public void run(boolean isSSL) {
		Client client = null;
		//Long id = 1L;
		WebTarget latestTarget = null;
		WebTarget rootTarget = null;
		client = ClientBuilder.newClient();
		rootTarget = client.target(HTTP_PORT);
		latestTarget = rootTarget.path("geohashcount");
		// https://obrienlabs.elasticbeanstalk.com/rest/read/geohashcount/f241b3
		try {
			WebTarget finalTarget = null;
			String prefix = "f241brnekx3";
			String record = null;
			int id=0;
				try { Thread.sleep(1); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
				finalTarget = latestTarget.path(prefix + String.valueOf('0'));
				long before = System.currentTimeMillis();
				record = finalTarget.request().get(String.class);//Long.class);//Record.class);
				long after = System.currentTimeMillis();
				System.out.println(new StringBuffer(prefix + String.valueOf('0')).append(":").append(record)
						.append(" : ").append(String.valueOf(after - before)).toString());
		} catch (Exception e)  {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}
	
	public static void main(String[] args) {
		JAXRSClient client = new JAXRSClient();
		client.run(false);
	}

}
