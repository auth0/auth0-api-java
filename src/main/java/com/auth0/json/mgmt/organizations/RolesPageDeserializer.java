package com.auth0.json.mgmt.organizations;

import com.auth0.json.mgmt.PageDeserializer;

import java.util.List;

public class RolesPageDeserializer extends PageDeserializer<RolesPage, Role> {

    protected RolesPageDeserializer() {
        super(Role.class, "roles");
    }

    @Override
    protected RolesPage createPage(List<Role> items) {
        return new RolesPage(items);
    }

    @Override
    protected RolesPage createPage(Integer start, Integer length, Integer total, Integer limit, List<Role> items) {
        return new RolesPage(start, length, total, limit, items);
    }
}
