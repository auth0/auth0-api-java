package com.auth0.json.mgmt.organizations;

import com.auth0.json.mgmt.PageDeserializer;

import java.util.List;

/**
 * Parses a paged response into its {@linkplain OrganizationsPage} representation.
 */
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

    @Override
    protected OrganizationsPage createPage(Integer start, Integer length, Integer total, Integer limit, String next, List<Organization> items) {
        return new OrganizationsPage(start, length, total, limit, next, items);
    }
}
