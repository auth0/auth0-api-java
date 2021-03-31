package com.auth0.json.mgmt.organizations;

import com.auth0.json.mgmt.PageDeserializer;
import com.auth0.json.mgmt.logevents.LogEvent;

import java.util.List;

public class OrganizationsPageDeserializer extends PageDeserializer<OrganizationsPage, Organization> {

    protected OrganizationsPageDeserializer() {
        super(Organization.class, "organizations");
    }

    @Override
    protected OrganizationsPage createPage(List<Organization> items) {
        return new OrganizationsPage(items);
    }

    @Override
    protected OrganizationsPage createPage(Integer start, Integer length, Integer total, Integer limit, List<Organization> items) {
        return new OrganizationsPage(start, length, total, limit, items);
    }
}
