package org.onap.demo;

import java.util.concurrent.atomic.AtomicLong;

import org.onap.demo.integration.JAXRSClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class ApiController {

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Api sayHello(@RequestParam(value="name", required=false, defaultValue="undefined") String name) {
    	String message = null;
    	// external call
    	message = getState("sdc");
    	Api api = new Api(
    			counter.incrementAndGet(), //String.format(template, name));
    			message);
    	return api;
    }
    
    private String getState(String id) {
    	JAXRSClient client = new JAXRSClient();
    	//String content = client.run(false,"http://67.192.246.187:8080/asdc/properties/encrypt/ecomp-dev/", "", "aa3871669d893c7fb8abbcda31b88b4f");
    	String sec = client.run(false, "http://" + Configuration.get("coll-ip") + ":3904/events/unauthenticated.SEC_MEASUREMENT_OUTPUT/group3/sub1?timeout=9000","","");
    	// https://{{aai_ip}}:8443/aai/v8/service-design-and-creation/services
    	String content = client.aaiCustomer();//.run(true, "172.99.115.238", "8443", "aai/v8/business/customers");
    	return content;
    }

}