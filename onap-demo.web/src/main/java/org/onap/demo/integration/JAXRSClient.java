package org.onap.demo.integration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.SslConfigurator;
import org.onap.demo.oss.Authenticator;

//import com.obrienlabs.gps.business.entity.Record;
//import com.obrienlabs.gps.business.entity.User;

public class JAXRSClient {

	private static final String AAI_CUSTOMERS = "https://{{aai_ip}}:8443/aai/v8/business/customers";
    
	public String run(boolean isSSL, String url, String port, String path) {
		String record = null;
		Client client = null;
		WebTarget latestTarget = null;
		WebTarget rootTarget = null;
		
		if(isSSL) {
			SslConfigurator sslConfig = SslConfigurator.newInstance();
			SSLContext sslContext = sslConfig.createSSLContext();
			// fix java.security.cert.CertificateException: No subject alternative names present
			HostnameVerifier verifier = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession sslSession) {
					return true; // TODO: security breach
				}
			};

			client = ClientBuilder.newBuilder().sslContext(sslContext).hostnameVerifier(verifier).build();
			client.register(new Authenticator("AAI","AAI"));
		} else {
			client = ClientBuilder.newClient();
		}
		rootTarget = client.target(url);
		latestTarget = rootTarget.path(path);

		try {
			WebTarget finalTarget = null;
				try { Thread.sleep(1); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
				finalTarget = latestTarget;
				long before = System.currentTimeMillis();
				record = finalTarget.request()
						.header("X-FromAppId", "AAI").header("Accept", "application/json")
						.get(String.class);
				long after = System.currentTimeMillis();
				System.out.println(record);
			} catch (Exception e)  {
				e.printStackTrace();
			} finally {
				client.close();
			}
		return record;
	}
	
	public String aaiCustomer() {
		String json = null;
		SslConfigurator sslConfig = SslConfigurator.newInstance()
				.trustStoreFile("/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/security/cacerts")//tomcat8")
				.trustStorePassword("changeit")
				.keyStoreFile("/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/security/cacerts")//tomcat8")
				.keyPassword("changeit");
		SSLContext sslContext = sslConfig.createSSLContext();
		// fix java.security.cert.CertificateException: No subject alternative names present
		HostnameVerifier verifier = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession sslSession) {
				return true; // TODO: security breach
			}
		};

		// without specifying the keystore above make sure you import the certificate as below
		//sudo keytool -import -trustcacerts -alias aai -file /config/certs/aai/aaiapisimpledemoopenecomporg.cer -keystore $JAVA_HOME/jre/lib/security/cacerts
		/*javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
				new javax.net.ssl.HostnameVerifier(){

				    public boolean verify(String hostname,
				            javax.net.ssl.SSLSession sslSession) {
				        return hostname.equals("172.99.115.238");
				    }
				});*/
		Client client = ClientBuilder.newBuilder().sslContext(sslContext).hostnameVerifier(verifier).build();
		//Client client = ClientBuilder.newClient();
		client.register(new Authenticator("AAI","AAI"));
		// only for 88 server with basic auth
		WebTarget rootTarget = client.target("https://146.20.65.216:8443");
		WebTarget latestTarget = rootTarget.path("aai/v8/business/customers/customer");
		try {
			json = (latestTarget.request().header("X-FromAppId", "AAI").header("Accept", "application/json").get(String.class));
		} catch (Exception e)  {
			e.printStackTrace();
		} finally {
			client.close();
		}
		System.out.println(json);
		return json;
	}
	
	//public static void main(String[] args) {
		//JAXRSClient client = new JAXRSClient();
		//client.run(false, "http://67.192.246.187:8080/asdc/properties/encrypt/ecomp-dev/", "", "aa3871669d893c7fb8abbcda31b88b4f");
		// https://developer.openstack.org/api-guide/quick-start/api-quick-start.html
		/*try {
		ProcessBuilder processBuilder = new ProcessBuilder("/Users/michaelobrien/wse_onap/onap/openstack_port_list.sh","network", "list");
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		InputStreamReader isr = new InputStreamReader(process.getInputStream());
		BufferedReader buff = new BufferedReader (isr);
		String line;
		while((line = buff.readLine()) != null) {
		    System.out.print(line);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	//}
		

}
