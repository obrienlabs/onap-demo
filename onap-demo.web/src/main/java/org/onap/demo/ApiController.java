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
    		content = client.run(false, Configuration.get(Configuration.DC, "coll-ip"), "3904", "events/unauthenticated.SEC_MEASUREMENT_OUTPUT/group3/sub1?timeout=9000", null, null, null);
    	   break;
    	case "tca":
    		content = client.run(false, Configuration.get(Configuration.DC, "coll-ip"), "3904", "events/unauthenticated.TCA_EVENT_OUTPUT/group3/sub1?timeout=9000", null, null, null);
    	   break;
    	case "customer-read":
    		// will get a 4000 on customers/customer but not customers at demo init state
    		content = client.run(true, Configuration.get(Configuration.DC, "aai-ip"), "8443", "aai/v8/business/customers/customer", "AAI", "AAI", "AAI");
    		//content = client.run(true, Configuration.get(Configuration.DC, "aai-ip"), "8443", "aai/v8/service-design-and-creation/models", "AAI", "AAI", "AAI");
    		break;
    	case "init-config": // 1 demo.sh or rest
    		ExternalProcessEndpoint ep_ic = new ExternalProcessEndpoint();
    		content = verifyScript(ep_ic.runExternal("demo.sh","init"));
    		break;
    	case "service-deploy-read": // 2 vid
    		// check endpoints
    		content = "service-deploy-read-return";
    		break;
    	case "service-creation-read": // 2 vid
    		// check endpoints
    		
    		// https://{{aai_ip}}:8443/aai/v8/business/customers/customer/Demonstration/service-subscriptions/service-subscription/vFW/service-instances/
    		// {  	    "service-instance": [    	                         {    	                             "service-instance-id": "9f801217-8cbd-4419-af06-640801422563",    	                             "service-instance-name": "DemoInstance",
    		break;
    	case "vnf-creation-read": // 3 vid
    		// check endpoints
    		ExternalProcessEndpoint ep_vcr = new ExternalProcessEndpoint();
    		content = ep_vcr.runExternalAbsolute("/Users/michaelobrien/wse_onap/onap/extract.sh");
    		System.out.println(content);
    		// https://{{aai_ip}}:8443/aai/v8/network/generic-vnfs
    		//{    	    "generic-vnf": [    	                    {
    	    //                    "vnf-id": "6e81b7e3-3a93-4b6f-8790-be65580a12ce",
    	    //                    "vnf-name": "DemoVNF",
    	    //                    "vnf-type": "fwservice/vsp 1",
    	    //                    "service-id": "0a3bb60f-2cf0-4982-a819-853cb088e916",

    		
    		break;
    	case "vfm-preload": // 4 demo.sh
    		ExternalProcessEndpoint ep_mp = new ExternalProcessEndpoint();
    		content = verifyScript(ep_mp.runExternal("demo.sh","preload", "DemoVNF", "DemoModule"));
    		// verify http://{{sdnc_ip}}:8282/restconf/config/VNF-API:preload-vnfs
    		// network on 
            //"vnf-parameter-name": "ecomp_private_net_id",
           // "vnf-parameter-value": "oam_ecomp_d037"
    		// ips 0= 1= 2=
            //"vnf-parameter-name": "vfw_private_ip_2",
            //"vnf-parameter-value": "10.1.0.11"


    		
    		break;
    	case "vfm-creation-read": // 5 vid or rest
    		// check endpoints
    		break;	
    	case "closed-loop": // 6 demo.sh or rest
    		ExternalProcessEndpoint ep_cl = new ExternalProcessEndpoint();
    		content = verifyScript(ep_cl.runExternal("demo.sh","appc", "DemoModule"));
    		break;	
    	case "ping": // ping -c 1
    		break;
    	case "dc-read":
    		break;
    	case "dc-switch":
    		content = Configuration.switchDC();
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