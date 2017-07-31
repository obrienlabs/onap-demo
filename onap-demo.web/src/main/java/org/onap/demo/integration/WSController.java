package org.onap.demo.integration;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

//@Controller
public class WSController {

    //@MessageMapping("/ws")
    //@SendTo("/topic/content")
    public WSContent greeting(WSMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new WSContent("Hello, " + message.getName() + "!");
    }

}
