/*-
 * ============LICENSE_START=======================================================
 * org.openecomp.aai
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.demo.oss;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.SslConfigurator;

public class OSSRestClient {
	private static final String IP = "23.253.125.166";
	private static final String HTTP_PORT = "88";
	private static final String HTTPS_PORT = "443";

	
	
	
	
	public void run(boolean isSSL) {
		Client client = null;
		WebTarget latestTarget = null;
		WebTarget rootTarget = null;

		if(isSSL) {
			// sudo keytool -import -alias nutridat_server -file /Users/michaelobrien/Dropbox/Nutridat/nutridat_domain_cert/20150119_nutridat_server_cer.cer -keystore /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/security/tomcat8
			SslConfigurator sslConfig = SslConfigurator.newInstance()
					.trustStoreFile("/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/security/tomcat8")
					.trustStorePassword("changeit")
					.keyStoreFile("/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/security/tomcat8")
					.keyPassword("changeit");
			SSLContext sslContext = sslConfig.createSSLContext();
			// fix java.security.cert.CertificateException: No subject alternative names present
			HostnameVerifier verifier = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession sslSession) {
					return true; // TODO: security breach
				}
			};

			client = ClientBuilder.newBuilder().sslContext(sslContext).hostnameVerifier(verifier).build();
			rootTarget = client.target("https://" + IP + ":" + HTTPS_PORT);
		} else {
			client = ClientBuilder.newClient();
			client.register(new Authenticator("test","test"));
			// only for 88 server with basic auth
			rootTarget = client.target("http://" + IP + ":" + HTTP_PORT);
		}
		latestTarget = rootTarget.path("/");
		try {
			WebTarget finalTarget = null;
			String record = null;
				try { Thread.sleep(1); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
				finalTarget = latestTarget;
				long before = System.currentTimeMillis();
				record = finalTarget.request().get(String.class);
				long after = System.currentTimeMillis();
				System.out.println(new StringBuffer(record)
						.append(" : ").append(String.valueOf(after - before)).toString());
		} catch (Exception e)  {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}

	private void aaiCustomer() {
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
		WebTarget rootTarget = client.target("https://172.99.115.238:8443");
		WebTarget latestTarget = rootTarget.path("aai/v8/business/customers");
		try {
			System.out.println(latestTarget.request().header("X-FromAppId", "AAI").header("Accept", "application/json").get(String.class));
		} catch (Exception e)  {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}
	// {"customer":[{"global-customer-id":"Demonstration","subscriber-name":"Demonstration","subscriber-type":"INFRA","resource-version":"1499992251","service-subscriptions":{"service-subscription":[{"service-type":"vFW","resource-version":"1499992251","relationship-list":{"relationship":[{"related-to":"tenant","related-link":"https://172.99.115.238:8443/aai/v8/cloud-infrastructure/cloud-regions/cloud-region/Rackspace/DFW/tenants/tenant/1035021/","relationship-data":[{"relationship-key":"cloud-region.cloud-owner","relationship-value":"Rackspace"},{"relationship-key":"cloud-region.cloud-region-id","relationship-value":"DFW"},{"relationship-key":"tenant.tenant-id","relationship-value":"1035021"}],"related-to-property":[{"property-key":"tenant.tenant-name","property-value":"1035021"}]}]}},{"service-type":"vLB","resource-version":"1499992251","relationship-list":{"relationship":[{"related-to":"tenant","related-link":"https://172.99.115.238:8443/aai/v8/cloud-infrastructure/cloud-regions/cloud-region/Rackspace/DFW/tenants/tenant/1035021/","relationship-data":[{"relationship-key":"cloud-region.cloud-owner","relationship-value":"Rackspace"},{"relationship-key":"cloud-region.cloud-region-id","relationship-value":"DFW"},{"relationship-key":"tenant.tenant-id","relationship-value":"1035021"}],"related-to-property":[{"property-key":"tenant.tenant-name","property-value":"1035021"}]}]}}]}}]}
	
	private void robotHealthCheck() {
		Client client = ClientBuilder.newClient();
		client.register(new Authenticator("test","test"));
		// only for 88 server with basic auth
		WebTarget rootTarget = client.target("http://" + IP + ":" + HTTP_PORT);
		WebTarget latestTarget = rootTarget.path("/");
		try {
			System.out.println(latestTarget.request().get(String.class));
		} catch (Exception e)  {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}

	public static void main(String[] args) {
		OSSRestClient client = new OSSRestClient();
		//client.robotHealthCheck();
		client.aaiCustomer();
		//client.run(false);
	}
}
