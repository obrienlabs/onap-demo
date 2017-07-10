package org.onap.demo;

import org.onap.demo.integration.JAXRSClient;

public class Api {

    private final long id;
    private final String content;

    public Api(long id, String content) {
        this.id = id;
        this.content = content;
        
		JAXRSClient client = new JAXRSClient();
		client.run(false);
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }


}
