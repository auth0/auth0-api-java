package com.auth0.json.mgmt.organizations;

import com.auth0.json.mgmt.Page;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = OrganizationsPageDeserializer.class)
public class OrganizationsPage extends Page<Organization> {
    public OrganizationsPage(List<Organization> items) {
        super(items);
    }

    public OrganizationsPage(Integer start, Integer length, Integer total, Integer limit, List<Organization> items) {
        super(start, length, total, limit, items);
    }
}
