package com.auth0.json.mgmt.organizations;

import com.auth0.json.mgmt.Page;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = RolesPageDeserializer.class)
public class RolesPage extends Page<Role> {
    public RolesPage(List<Role> items) {
        super(items);
    }

    public RolesPage(Integer start, Integer length, Integer total, Integer limit, List<Role> items) {
        super(start, length, total, limit, items);
    }
}
