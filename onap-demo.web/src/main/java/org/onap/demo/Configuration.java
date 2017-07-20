package org.onap.demo;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

	private static Map<String, String> map = new HashMap<>();
	
	static {
		map.put("robot-ip", "23.253.125.166");
		map.put("aai-ip", "172.99.115.238");
		map.put("appc-ip", "67.192.246.90");
		map.put("dcae-ip", "67.192.246.40");
		map.put("dcae-ip", "67.192.246.203");
		map.put("message-router-ip", "104.130.159.144");
		map.put("mso-ip", "67.192.246.187");
		map.put("policy-ip", "172.99.115.234");
		map.put("portal-ip", "104.239.241.48");
		map.put("sdc-ip", "172.99.115.127");
		map.put("sdnc-ip", "67.192.246.228");
		map.put("vid-ip", "23.253.125.165");
		map.put("cdap0-ip", "23.253.85.68");
		map.put("cdap1-ip", "174.143.130.203");
		map.put("cdap2-ip", "104.239.140.22");
		map.put("coll-ip", "162.242.235.75");
		map.put("pstg-ip", "104.239.240.7");
		
	}
	
	public static String get(String key) {
		return map.get(key);
	}
	
	public static void put(String key, String value) {
		map.put(key, value);
	}
	
	
}
