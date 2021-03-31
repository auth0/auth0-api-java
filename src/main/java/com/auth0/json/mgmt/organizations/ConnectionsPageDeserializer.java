package com.auth0.json.mgmt.organizations;

import com.auth0.json.mgmt.PageDeserializer;

import java.util.List;

public class ConnectionsPageDeserializer  extends PageDeserializer<EnabledConnectionsPage, EnabledConnection> {
    protected ConnectionsPageDeserializer() {
        super(EnabledConnection.class, "enabled_connections");
    }

    @Override
    protected EnabledConnectionsPage createPage(List<EnabledConnection> items) {
        return new EnabledConnectionsPage(items);
    }

    @Override
    protected EnabledConnectionsPage createPage(Integer start, Integer length, Integer total, Integer limit, List<EnabledConnection> items) {
        return new EnabledConnectionsPage(start, length, total, limit, items);
    }
}
