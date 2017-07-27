package org.onap.demo;

import java.util.concurrent.atomic.AtomicLong;

import org.onap.demo.integration.JAXRSClient;
import org.onap.demo.sbi.ExternalProcessEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class ApiController {

    private final AtomicLong counter = new AtomicLong();
	private static final CharSequence pass = "PASS";
	private static final CharSequence fail = "FAIL";
    
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Api process(@RequestParam(value="action", required=true, defaultValue="undefined") String action) {
    	String message = null;
    	// external call
    	message = externalAPICall(action);
    	Api api = new Api(counter.incrementAndGet(), message);
    	return api;
    } 
    
    private String externalAPICall(String action) {
    	String content = null;
    	JAXRSClient client = new JAXRSClient();

    	
    	// todo
    	// get https://{{aai_ip}}:8443/aai/v8/business/customers/customer/Demonstration/service-subscriptions/service-subscription/vFW/service-instances/
    	// {"service-instance": [ { "service-instance-id": "cd2eb659-2463-461b-9c3b-3bf03619c167",
    	switch(action) {
    	case "sec":
    		content = client.run(false, Configuration.get("iad", "coll-ip"), "3904", "events/unauthenticated.SEC_MEASUREMENT_OUTPUT/group3/sub1?timeout=9000", null, null, null);
    	   break;
    	case "tca":
    		content = client.run(false, Configuration.get("iad", "coll-ip"), "3904", "events/unauthenticated.TCA_EVENT_OUTPUT/group3/sub1?timeout=9000", null, null, null);
    	   break;
    	case "customer-read":
    		// will get a 4000 on customers/customer but not customers at demo init state
    		content = client.run(true, Configuration.get("iad", "aai-ip"), "8443", "aai/v8/business/customers/customer", "AAI", "AAI", "AAI");
    		break;
    	case "initial-config": // 1 demo.sh or rest
    		ExternalProcessEndpoint ep_ic = new ExternalProcessEndpoint();
    		content = verifyScript(ep_ic.runExternal("demo.sh","init", "op1", "op2"));
    		break;
    	case "service-deploy-read": // 2 vid
    		// check endpoints
    		break;
    	case "service-creation-read": // 2 vid
    		// check endpoints
    		break;
    	case "vnf-creation-read": // 3 vid
    		// check endpoints
    		
    		break;
    	case "vfm-preload": // 4 demo.sh
    		ExternalProcessEndpoint ep_mp = new ExternalProcessEndpoint();
    		content = verifyScript(ep_mp.runExternal("demo.sh","preload", "DemoVNF", "DemoModule"));
    		break;
    	case "vfm-creation-read": // 5 vid or rest
    		// check endpoints
    		break;	
    	case "closed-loop": // 6 demo.sh or rest
    		ExternalProcessEndpoint ep_cl = new ExternalProcessEndpoint();
    		content = verifyScript(ep_cl.runExternal("demo.sh","appc", "DemoModule", "op2"));
    		break;	
    	}
    	
    	return content;
    }
    
    private String verifyScript(String content) {
    	// assume failure
    	String passed = "fail";
		// check for FAIL
		if(content.contains(pass)) {
			System.out.println("passed");
			passed = "pass";
		}
		if(content.contains(fail)) {
			System.out.println("failed");
			passed = "fail";
		}
		return passed;
    }

}