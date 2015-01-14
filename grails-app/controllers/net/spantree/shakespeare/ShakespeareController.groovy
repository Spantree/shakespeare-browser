package net.spantree.shakespeare

import grails.converters.JSON
import io.searchbox.core.Search
import org.elasticsearch.index.query.FilterBuilders
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.aggregations.AggregationBuilder
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms
import org.elasticsearch.search.aggregations.bucket.terms.UnmappedTerms
import org.elasticsearch.search.sort.SortBuilders

/**
 * Created by cedric on 1/11/15.
 */
class ShakespeareController {
    def elasticsearchService
    def serverIdService
    def indexName = "shakespeare"

    def listPlays() {
        def client = elasticsearchService.jestClient
        def q = [
            query: [
                match_all: [:]
            ],
            aggs: [
                play_name: [terms: ["field": "play_name"]]
            ],
            size: 0
        ] as JSON

        def search = new Search.Builder(q.toString())
            .addIndex(indexName)
            .build()

        def resp = client.execute(search)

        def buckets = resp.jsonMap.aggregations.play_name.buckets

        [
            buckets: buckets,
            serverId: serverIdService.serverId
        ]
    }

    def listLines() {
        def playName = params.playName
        def searchText = params.searchText

        def client = elasticsearchService.jestClient

        def query = [match_all: [:]]

        if(searchText) {
            query = [
                multi_match: [
                    query: searchText,
                    fields: ["text_entry", "speaker"]
                ]
            ]
        }

        def q = [
            query: [
                filtered: [
                    query: query,
                    filter: [
                        term: [play_name: playName]
                    ]
                ]
            ],
            highlight: [
                fields: [
                    "speaker": [:],
                    "text_entry": [:]
                ]
            ],
            size: 100
        ] as JSON

        def search = new Search.Builder(q.toString())
            .addIndex(indexName)
            .build()

        def resp = client.execute(search)

        def lines = resp.jsonMap.hits.hits.collect { hit ->
            def highlight = hit.highlight
            def source = hit._source
            [
                speaker: highlight?.speaker?.first() ?: source['speaker'],
                text: highlight?.text_entry?.first() ?: source['text_entry'],
            ]
        }

        [
            playName: playName,
            searchText: searchText,
            count: resp.total,
            lines: lines,
            serverId: serverIdService.serverId
        ]
    }
}
