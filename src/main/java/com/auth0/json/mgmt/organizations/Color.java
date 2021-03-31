package com.auth0.json.mgmt.organizations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the color object of a {@linkplain Branding}. Related to the {@linkplain com.auth0.client.mgmt.OrganizationsEntity}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Color {

    @JsonProperty("primary")
    private String primary;
    @JsonProperty("page_background")
    private String pageBackground;

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getPageBackground() {
        return pageBackground;
    }

    public void setPageBackground(String pageBackground) {
        this.pageBackground = pageBackground;
    }
}
