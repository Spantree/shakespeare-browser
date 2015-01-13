package net.spantree.shakespeare

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress

import javax.annotation.PostConstruct

/**
 * Created by cedric on 1/11/15.
 */
class ElasticsearchService {
    Client client

    @PostConstruct
    void init() {
        client = new TransportClient()
            .addTransportAddress(new InetSocketTransportAddress("slave1", 9300))
            .addTransportAddress(new InetSocketTransportAddress("slave2", 9300))
    }
}
