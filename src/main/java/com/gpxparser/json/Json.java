package com.gpxparser.json;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gpxparser.controller.RestWSController;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * This class is necessary for simple raw data printing into request get call
 * @see RestWSController#simpleJsonExample()
 * http://www.jsonschema2pojo.org/ this tool has been used to generate Jackson 2 java pojos from raw Json data
 */
public class Json {
    private final String value;

    public Json(String value) {
        this.value = value;
    }

    @JsonValue
    @JsonRawValue
    public String value() {
        return value;
    }
}
