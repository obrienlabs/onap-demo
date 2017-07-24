package org.onap.demo;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

	private static Map<String, Map<String, String>> map = new HashMap<>();
	
	static {
		map.put("dfw",  new HashMap<>());
		map.put("iad",  new HashMap<>());	
		Map<String, String> dMap = map.get("dfw");
		Map<String, String> iMap = map.get("iad");
		dMap.put("robot-ip", "23.253.125.166");
		dMap.put("aai-ip", "172.99.115.238");
		dMap.put("appc-ip", "67.192.246.90");
		dMap.put("dcae-ip", "67.192.246.40");
		dMap.put("message-router-ip", "104.130.159.144");
		dMap.put("mso-ip", "67.192.246.187");
		dMap.put("policy-ip", "172.99.115.234");
		dMap.put("portal-ip", "104.239.241.48");
		dMap.put("sdc-ip", "172.99.115.127");
		dMap.put("sdnc-ip", "67.192.246.228");
		dMap.put("vid-ip", "23.253.125.165");
		dMap.put("cdap0-ip", "23.253.85.68");
		dMap.put("cdap1-ip", "174.143.130.203");
		dMap.put("cdap2-ip", "104.239.140.22");
		dMap.put("coll-ip", "162.242.235.75");
		dMap.put("pstg-ip", "104.239.240.7");	
		
		iMap.put("robot-ip", "104.130.13.93");
		iMap.put("aai-ip", "146.20.65.216");
		iMap.put("appc-ip", "162.242.219.48");
		iMap.put("dcae-ip", "172.99.67.156");
		iMap.put("message-router-ip", "162.209.124.37");
		iMap.put("mso-ip", "162.242.218.100");
		iMap.put("policy-ip", "104.239.234.15");
		iMap.put("portal-ip", "104.130.31.44");
		iMap.put("sdc-ip", "104.239.234.11");
		iMap.put("sdnc-ip", "162.242.218.219");
		iMap.put("vid-ip", "104.130.169.38");
		iMap.put("cdap0-ip", "104.239.175.196");
		iMap.put("cdap1-ip", "104.130.239.149");
		iMap.put("cdap2-ip", "172.99.68.155");
		iMap.put("coll-ip", "162.242.235.75");
		iMap.put("pstg-ip", "104.239.168.49");
	}
	
	public static String get(String region, String key) {
		return map.get(region).get(key);
	}
	
	public static void put(String region, String key, String value) {
		map.get(region).put(key, value);
	}
	
	
}
