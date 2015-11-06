package com.peoplenet.event.audit.service

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.Canonical
import groovy.transform.CompileStatic

/**
 * Created by dhreines on 11/5/15.
 */
@CompileStatic
@Canonical
class Bucket {

    String key
    BucketValue value

    @JsonProperty("doc_count")
    Long docCount

}
