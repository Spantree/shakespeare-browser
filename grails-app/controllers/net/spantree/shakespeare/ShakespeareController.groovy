package net.spantree.shakespeare

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
        def client = elasticsearchService.client
        def resp = client.prepareSearch(indexName)
            .addAggregation(
                AggregationBuilders.terms("play").field("play_name")
            )
            .execute().actionGet()
        StringTerms terms = resp.aggregations.get("play")
        println terms.buckets.size()
        [
            buckets: terms.buckets,
            serverId: serverIdService.serverId
        ]
    }

    def listLines() {
        def playName = params.playName
        def searchText = params.searchText

        def client = elasticsearchService.client
        def search = client.prepareSearch(indexName)
            .setPostFilter(FilterBuilders.termFilter("play_name", playName))
            .addSort(SortBuilders.fieldSort("line_id"))
            .setSize(100)
            .addHighlightedField("text_entry")
            .addHighlightedField("speaker")

        if(searchText) {
            search.setQuery(
                QueryBuilders.multiMatchQuery(searchText, "text_entry", "speaker")
            )
        }
        def resp = search.execute().actionGet()

        def lines = resp.hits.hits.collect { hit ->
            def highlight = hit.highlightFields()
            def source = hit.sourceAsMap()
            [
                speaker: highlight['speaker']?.fragments()?.first()?.string() ?: source['speaker'],
                text: highlight['text_entry']?.fragments()?.first()?.string() ?: source['text_entry'],
            ]
        }

        [
            playName: playName,
            searchText: searchText,
            count: resp.hits.totalHits,
            lines: lines,
            serverId: serverIdService.serverId
        ]
    }
}
