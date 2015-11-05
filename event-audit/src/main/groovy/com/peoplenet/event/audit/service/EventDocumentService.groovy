package com.peoplenet.event.audit.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.okhttp.*

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
