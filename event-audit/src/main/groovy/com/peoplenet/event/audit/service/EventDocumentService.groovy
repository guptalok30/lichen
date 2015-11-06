package com.peoplenet.event.audit.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import com.squareup.okhttp.Response

import javax.inject.Named

/**
 * Created by agupta on 11/5/15.
 */
@Named
class EventDocumentService {
    ObjectMapper objectMapper = new ObjectMapper()

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8")
    OkHttpClient client = new OkHttpClient()

    String save(final String indexName, final String typeName, final Map documentSource) {

        //add the eventServiceNodeKey
        StringJoiner joiner = new StringJoiner(":")
        joiner.add(documentSource.get('service')).add(documentSource.get('eventType'))
        if (documentSource.get('parentService') != null && documentSource.get('parentEventType') != null) {
            joiner.add(documentSource.get('parentService')).add(documentSource.get('parentEventType'))
        }
        String eventServiceNodeKey = joiner.toString()

        documentSource.put('eventServiceNodeKey', eventServiceNodeKey)

        String json = objectMapper.writeValueAsString(documentSource)
        RequestBody body = RequestBody.create(JSON, json)

        Request request = new Request.Builder()
                .url('http://52.91.216.148:9200/' + indexName + '/' + typeName)
                .post(body)
                .build()

        Response response = client.newCall(request).execute()
        //HashMap<String, Object> responseMap;
        //responseMap = new ObjectMapper().readValue(response.body().string(), new TypeReference<HashMap<String,Object>>() {});
        return response.body().string()
    }

}
