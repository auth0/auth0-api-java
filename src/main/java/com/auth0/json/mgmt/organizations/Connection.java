package com.auth0.json.mgmt.organizations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the Connection object of an {@linkplain EnabledConnection}. Related to the {@linkplain com.auth0.client.mgmt.OrganizationsEntity}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Connection {

    @JsonProperty("name")
    private String  name;
    @JsonProperty("strategy")
    private String strategy;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrategy() {
        return this.strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
