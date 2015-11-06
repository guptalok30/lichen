package com.peoplenet.event.audit.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.peoplenet.common.PeopleNetObjectMapperBuilder
import com.peoplenet.event.audit.domain.EventServiceNodeAggregate
import com.peoplenet.event.audit.domain.EventTraceAggregate
import com.squareup.okhttp.*
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

/**
 * Created by dhreines on 11/5/15.
 */
@Component
class AggregateService {

    private ObjectMapper mapper = new PeopleNetObjectMapperBuilder().build()
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8")

    OkHttpClient client = new OkHttpClient()

    EventTraceAggregate getAggregateTrace() {

        String body = """{
            "aggs" : {
                "aggregation" : {
                    "terms" : {
                        "field" : "eventServiceNodeKey"
                    },
                    "aggs": {
                        "value": {
                            "avg": {
                                "field": "duration"
                            }
                        }
                    }
                }
            }
        }"""
        RequestBody requestBody = RequestBody.create(JSON, body)
        Request request = new Request.Builder().url("http://52.91.216.148:9200/lichen/event_audit/_search?search_type=count").method("POST", requestBody).build()
        Response response = client.newCall(request).execute()
        String resultString = response.body().string()
        println resultString

        AggregationResult result = mapper.readValue(resultString, AggregationResult)

        List<EventServiceNodeAggregate> aggregates = []
        result?.aggregations?.aggregation?.buckets.each { bucket ->
            EventServiceNodeAggregate aggregate = new EventServiceNodeAggregate()
            aggregate.with {

                List<String> tokens = bucket.key.tokenize(":")
                println("tokens $tokens")
                if (tokens.size() == 4) {
                    serviceName = tokens[0]
                    eventType = tokens[1]
                    parentServiceName = tokens[2]
                    parentEventType = tokens[3]
                }
                // originating consumer, no parent consumed event
                if (tokens.size() == 3) {
                    serviceName = tokens[0]
                    eventType = tokens[1]
                    parentServiceName = tokens[2]
                }
                avgDuration = bucket?.value.value
                count = bucket.docCount
            }
            aggregates.add(aggregate)
        }
        buildEventTraceAggregate(aggregates)
    }

    private EventTraceAggregate buildEventTraceAggregate(List<EventServiceNodeAggregate> aggregates) {

        EventTraceAggregate traceAggregate = new EventTraceAggregate()
        MultiValueMap<String, EventServiceNodeAggregate> childrenByParent = new LinkedMultiValueMap()
        aggregates.each {
            if (it.parentEventType != null) {
                String parentKey = "${it.parentServiceName}:${it.parentEventType}"

                println "adding parentKey ${parentKey}"
                childrenByParent.add(parentKey, it)
            } else {
                traceAggregate.origin = it
            }
        }

        aggregates.each {
            String key = "${it.serviceName}:${it.eventType}"
            List children = childrenByParent.get(key)
            println "key:${key} found children ${children}"
            if (children != null) {
                it.children.addAll(children)
            }
        }
        println traceAggregate
        traceAggregate
    }

//    private EventTraceAggregate mockData() {
//        EventTraceAggregate aggregate = new EventTraceAggregate()
//        EventServiceNodeAggregate node = new EventServiceNodeAggregate()
//        node.serviceName = 'abc'
//        node.eventType = 'FAULT'
//        node.count = 10
//        node.avgDuration = 5
//        aggregate.origin = node
//
//        EventServiceNodeAggregate node1 = new EventServiceNodeAggregate()
//        node1.serviceName = 'def'
//        node1.eventType = 'FAULT_DEC'
//        node1.count = 15
//        node1.avgDuration = 5
//        node.children.add(node1)
//
//        EventServiceNodeAggregate node2 = new EventServiceNodeAggregate()
//        node2.serviceName = 'xyz'
//        node2.eventType = 'FAULT_GUIDANCE'
//        node2.count = 20
//        node2.avgDuration = 5
//        node1.children.add(node2)
//
//        EventServiceNodeAggregate node3 = new EventServiceNodeAggregate()
//        node3.serviceName = 'ghi'
//        node3.eventType = 'FAULT_DEC'
//        node3.count = 20
//        node3.avgDuration = 5
//        node.children.add(node3)
//        aggregate
//    }

}
