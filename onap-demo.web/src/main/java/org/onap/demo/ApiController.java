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

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Api process(@RequestParam(value="name", required=true, defaultValue="undefined") String name) {
    	String message = null;
    	// external call
    	message = externalAPICall(name);
    	Api api = new Api(
    			counter.incrementAndGet(), //String.format(template, name));
    			message);
    	return api;
    } 
    
    private String externalAPICall(String id) {
    	String content = null;
    	JAXRSClient client = new JAXRSClient();

    	switch(id) {
    	case "sec_mo":
    		content = client.run(false, Configuration.get("iad", "coll-ip"), "3904", "events/unauthenticated.SEC_MEASUREMENT_OUTPUT/group3/sub1?timeout=9000", null, null, null);
    	   break;
    	case "customer":
    		// will get a 4000 on customers/customer but not customers at demo init state
    		content = client.run(true, Configuration.get("iad", "aai-ip"), "8443", "aai/v8/business/customers/customer", "AAI", "AAI", "AAI");
    		break;
    	case "initial-config":
    		ExternalProcessEndpoint ep_ic = new ExternalProcessEndpoint();
    		content = ep_ic.runExternal("openstack_port_list.sh","network", "list");
    		break;
    	case "service-creation":
    		ExternalProcessEndpoint ep_sc = new ExternalProcessEndpoint();
    		content = ep_sc.runExternal("openstack_port_list.sh","network", "list");
    		break;
    	case "vnf-creation":
    		ExternalProcessEndpoint ep_vc = new ExternalProcessEndpoint();
    		content = ep_vc.runExternal("openstack_port_list.sh","network", "list");
    		break;
    	case "vfm-preload":
    		ExternalProcessEndpoint ep_mp = new ExternalProcessEndpoint();
    		content = ep_mp.runExternal("openstack_port_list.sh","network", "list");
    		break;
    	case "vfm-creation":
    		ExternalProcessEndpoint ep_mc = new ExternalProcessEndpoint();
    		content = ep_mc.runExternal("openstack_port_list.sh","network", "list");
    		break;	
    	case "closed-loop":
    		ExternalProcessEndpoint ep_cl = new ExternalProcessEndpoint();
    		content = ep_cl.runExternal("openstack_port_list.sh","network", "list");
    		break;	
    		
    	}
    	
    	return content;
    }

}