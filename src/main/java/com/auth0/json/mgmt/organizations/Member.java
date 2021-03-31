package com.auth0.json.mgmt.organizations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the member of an organization. Related to the {@linkplain com.auth0.client.mgmt.OrganizationsEntity}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Member {

    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("email")
    private String email;
    @JsonProperty("picture")
    private String picture;
    @JsonProperty("name")
    private String name;

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
