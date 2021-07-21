package com.auth0.json.mgmt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Class that represents a paginated list of objects.
 *
 * @param <T> the type of the object this page contains.
 */
@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Page<T> {

    @JsonProperty("start")
    private Integer start;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("limit")
    private Integer limit;
    @JsonProperty("next")
    private String next;
    private final List<T> items;

    public Page(List<T> items) {
        this.items = items;
    }

    public Page(Integer start, Integer length, Integer total, Integer limit, List<T> items) {
        this(start, length, total, limit, null, items);
    }

    public Page(Integer start, Integer length, Integer total, Integer limit, String next, List<T> items) {
        this.start = start;
        this.length = length;
        this.total = total;
        this.limit = limit;
        this.next = next;
        this.items = items;
    }

    /**
     * Getter for the position of the item this page starts from.
     *
     * @return the start value.
     */
    @JsonProperty("start")
    public Integer getStart() {
        return start;
    }

    /**
     * Getter for the amount of items per page.
     *
     * @return the length value.
     */
    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    /**
     * Getter for the total amount of items.
     *
     * @return the total value.
     */
    @JsonProperty("total")
    public Integer getTotal() {
        return total;
    }

    /**
     * Getter for the items amount limit.
     *
     * @return the limit value.
     */
    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    /**
     * Getter for the next value, if using checkpoint pagination, which can be used to fetch the next page of results.
     * @return the next value.
     */
    @JsonProperty("next")
    public String getNext() {
        return next;
    }

    /**
     * Getter for the list of items.
     *
     * @return the list of items.
     */
    public List<T> getItems() {
        return items;
    }
}
