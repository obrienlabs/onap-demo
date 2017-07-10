package org.onap.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

//@RestController
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index() {
    	return "index.html";
    }

    
}
