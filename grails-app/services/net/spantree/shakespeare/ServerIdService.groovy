package net.spantree.shakespeare

import grails.transaction.Transactional
import org.apache.commons.lang.RandomStringUtils

import javax.annotation.PostConstruct

@Transactional
class ServerIdService {
    String serverId

    @PostConstruct
    void init() {
        serverId = RandomStringUtils.randomAlphabetic(8)
    }
}
