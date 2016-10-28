package com.gpxparser.json;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/28/2016
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.gpxparser")
@JsonPropertyOrder({
        "foo",
        "bar",
        "baz"
})
public class Example {

    @JsonProperty("foo")
    private String foo;
    @JsonProperty("bar")
    private Integer bar;
    @JsonProperty("baz")
    private Boolean baz;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The foo
     */
    @JsonProperty("foo")
    public String getFoo() {
        return foo;
    }

    /**
     * @param foo The foo
     */
    @JsonProperty("foo")
    public void setFoo(String foo) {
        this.foo = foo;
    }

    /**
     * @return The bar
     */
    @JsonProperty("bar")
    public Integer getBar() {
        return bar;
    }

    /**
     * @param bar The bar
     */
    @JsonProperty("bar")
    public void setBar(Integer bar) {
        this.bar = bar;
    }

    /**
     * @return The baz
     */
    @JsonProperty("baz")
    public Boolean getBaz() {
        return baz;
    }

    /**
     * @param baz The baz
     */
    @JsonProperty("baz")
    public void setBaz(Boolean baz) {
        this.baz = baz;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Example{" +
                "foo='" + foo + '\'' +
                ", bar=" + bar +
                ", baz=" + baz +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
