package net.spantree.shakespeare

import io.searchbox.client.JestClient
import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig

import javax.annotation.PostConstruct

/**
 * Created by cedric on 1/11/15.
 */
class ElasticsearchService {
    def jestClientFactory

    @PostConstruct
    void init() {
        jestClientFactory = new JestClientFactory(
            httpClientConfig: new HttpClientConfig.Builder(System.properties.getProperty('ES_ROOT') ?: "http://slave1:9200")
                .multiThreaded(true)
                .build()
        )
    }

    JestClient getJestClient() {
        jestClientFactory.getObject()
    }
}
