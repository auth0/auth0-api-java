package com.auth0.json.mgmt.organizations;

import com.auth0.json.mgmt.Page;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = ConnectionsPageDeserializer.class)
public class EnabledConnectionsPage extends Page<EnabledConnection> {

    public EnabledConnectionsPage(List<EnabledConnection> items) {
        super(items);
    }

    public EnabledConnectionsPage(Integer start, Integer length, Integer total, Integer limit, List<EnabledConnection> items) {
        super(start, length, total, limit, items);
    }
}
