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
    public @ResponseBody Api sayHello(@RequestParam(value="name", required=true, defaultValue="undefined") String name) {
    	String message = null;
    	// external call
    	message = getState(name);
    	Api api = new Api(
    			counter.incrementAndGet(), //String.format(template, name));
    			message);
    	return api;
    } 
    
    private String getState(String id) {
    	String content = null;
    	JAXRSClient client = new JAXRSClient();
    	//String sec = client.run(false, "http://" + Configuration.get("coll-ip") + ":3904/events/unauthenticated.SEC_MEASUREMENT_OUTPUT/group3/sub1?timeout=9000","","");
    	
    	
    	switch(id) {
    	case "sec_mo":
    		content = client.run(false, Configuration.get("iad", "coll-ip"), "3904", "events/unauthenticated.SEC_MEASUREMENT_OUTPUT/group3/sub1?timeout=9000", null, null, null);
    	   break;
    	case "customer":
    		// will get a 4000 on customers/customer but not customers at demo init state
    		content = client.run(true, Configuration.get("iad", "aai-ip"), "8443", "aai/v8/business/customers/customer", "AAI", "AAI", "AAI");
    		break;
    	}
    	
    	return content;
    }

}