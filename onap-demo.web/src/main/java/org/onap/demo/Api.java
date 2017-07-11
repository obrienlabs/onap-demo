package org.onap.demo;

import org.onap.demo.integration.JAXRSClient;

public class Api {

    private long id;
    private String content;

    public Api(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
    	JAXRSClient client = new JAXRSClient();
		content = client.run(false);
        return content;
    }


}
