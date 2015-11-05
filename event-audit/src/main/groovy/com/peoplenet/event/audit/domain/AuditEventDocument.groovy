package com.peoplenet.event.audit.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import groovy.transform.CompileStatic

@CompileStatic
@JsonPropertyOrder(alphabetic = true)
class AuditEventDocument {

    Metadata metadata = new Metadata()
    Map source = [:]

    // Needed for JSON marshaling
    AuditEventDocument() {}

    AuditEventDocument(String indexName, String typeName, String documentId, Map source) {
        this.source.putAll(source)
        metadata.indexName = indexName
        metadata.typeName = typeName
        metadata.documentId = documentId
        metadata.uri = metadata.getUri()
    }

    AuditEventDocument(String indexName, String typeName, Map source) {
        this.source.putAll(source)
        metadata.indexName = indexName
        metadata.typeName = typeName
        metadata.uri = metadata.getUri()
    }

    @JsonPropertyOrder(alphabetic = true)
    static class Metadata {

        String indexName
        String typeName
        String documentId
        long version = 0l
        long timestamp = 0l
        String uri
        Object[] sortValues

        @JsonProperty
        String getUri() {
            if (!this.uri) {
                this.uri = '/auditEvents/' + documentId
            }
            return uri
        }
    }

    @Override
    String toString() {
        return metadata.uri ?: '/auditEvents/?'
    }
}