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

public class JAXRSClient {
 
	public String run(boolean isSSL, String ip, String port, String path, String authUser, String authPass, String appId) {
		String record = null;
		String prefix = null;
		Client client = null;
		WebTarget rootTarget = null;
		if(isSSL) {
			SslConfigurator sslConfig = SslConfigurator.newInstance();
			/*SslConfigurator sslConfig = SslConfigurator.newInstance()
					.trustStoreFile("/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/security/cacerts")
					.trustStorePassword("changeit")
					.keyStoreFile("/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/security/cacerts")
					.keyPassword("changeit");*/
			SSLContext sslContext = sslConfig.createSSLContext();
			// fix java.security.cert.CertificateException: No subject alternative names present
			HostnameVerifier verifier = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession sslSession) {
					return true; // TODO: security breach
				}};

			client = ClientBuilder.newBuilder().sslContext(sslContext).hostnameVerifier(verifier).build();
			client.register(new Authenticator(authUser, authPass));
			prefix = "https://";
		} else {
			client = ClientBuilder.newClient();
			prefix = "http://";
		}
		rootTarget = client.target(prefix + ip + ":" + port + "/" + path);
		try {
				record = rootTarget.request()
						.header("X-FromAppId", appId).header("Accept", "application/json")
						.get(String.class);
				System.out.println(record);
			} catch (Exception e)  {
				e.printStackTrace();
			} finally {
				client.close();
			}
		return record;
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
