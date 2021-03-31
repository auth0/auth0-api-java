package com.auth0.json.mgmt.organizations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the branding object of an {@linkplain Organization}. Related to the {@linkplain com.auth0.client.mgmt.OrganizationsEntity}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Branding {

    @JsonProperty("logo_url")
    private String logoUrl;
    @JsonProperty("colors")
    private Color colors;

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Color getColors() {
        return colors;
    }

    public void setColors(Color colors) {
        this.colors = colors;
    }
}
