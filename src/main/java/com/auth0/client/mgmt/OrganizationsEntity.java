package com.auth0.client.mgmt;

import com.auth0.client.mgmt.filter.PageFilter;
import com.auth0.json.mgmt.organizations.*;
import com.auth0.net.CustomRequest;
import com.auth0.net.Request;
import com.auth0.net.VoidRequest;
import com.auth0.utils.Asserts;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Map;

public class OrganizationsEntity extends BaseManagementEntity {

    private final static String ORGS_PATH = "api/v2/organizations";
    private final static String AUTHORIZATION_HEADER = "Authorization";

    OrganizationsEntity(OkHttpClient client, HttpUrl baseUrl, String apiToken) {
        super(client, baseUrl, apiToken);
    }

    // Organizations Entity

    public Request<OrganizationsPage> list(PageFilter filter) {
        HttpUrl.Builder builder = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH);

        if (filter != null) {
            for (Map.Entry<String, Object> e : filter.getAsMap().entrySet()) {
                builder.addQueryParameter(e.getKey(), String.valueOf(e.getValue()));
            }
        }

        String url = builder.build().toString();
        CustomRequest<OrganizationsPage> request = new CustomRequest<>(client, url, "GET", new TypeReference<OrganizationsPage>() {
        });

        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return request;
    }

    public Request<Organization> get(String orgId) {
        Asserts.assertNotNull(orgId, "organization ID");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .build()
            .toString();

        CustomRequest<Organization> request = new CustomRequest<>(client, url, "GET", new TypeReference<Organization>() {
        });

        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return request;
    }

    public Request<Organization> getByName(String orgName) {
        Asserts.assertNotNull(orgName, "organization name");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment("name")
            .addPathSegment(orgName)
            .build()
            .toString();

        CustomRequest<Organization> request = new CustomRequest<>(client, url, "GET", new TypeReference<Organization>() {
        });

        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return request;
    }

    public Request<Organization> create(Organization organization) {
        Asserts.assertNotNull(organization, "organization");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .build()
            .toString();

        CustomRequest<Organization> request = new CustomRequest<>(client, url, "POST", new TypeReference<Organization>() {
        });

        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        request.setBody(organization);
        return request;
    }

    public Request<Organization> update(String orgId, Organization organization) {
        Asserts.assertNotNull(orgId, "organization ID");
        Asserts.assertNotNull(organization, "organization");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .build()
            .toString();

        CustomRequest<Organization> request = new CustomRequest<>(client, url, "PATCH", new TypeReference<Organization>() {
        });

        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        request.setBody(organization);
        return request;
    }

    public Request delete(String organizationId) {
        Asserts.assertNotNull(organizationId, "organization ID");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(organizationId)
            .build()
            .toString();

        VoidRequest voidRequest = new VoidRequest(client, url, "DELETE");
        voidRequest.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return voidRequest;
    }

    // Organization members

    public Request<MembersPage> getMembers(String orgId, PageFilter filter) {
        Asserts.assertNotNull(orgId, "organization ID");

        HttpUrl.Builder builder = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .addPathSegment("members");

        if (filter != null) {
            for (Map.Entry<String, Object> e : filter.getAsMap().entrySet()) {
                builder.addQueryParameter(e.getKey(), String.valueOf(e.getValue()));
            }
        }

        String url = builder.build().toString();
        CustomRequest<MembersPage> request = new CustomRequest<>(client, url, "GET", new TypeReference<MembersPage>() {
        });
        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return request;
    }

    public Request addMembers(String orgId, Members members) {
        Asserts.assertNotNull(orgId, "organization ID");
        Asserts.assertNotNull(members, "members");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .addPathSegment("members")
            .build()
            .toString();

        VoidRequest request = new VoidRequest(client, url, "POST");
        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        request.setBody(members);
        return request;
    }

    public Request deleteMembers(String orgId, Members members) {
        Asserts.assertNotNull(orgId, "organization ID");
        Asserts.assertNotNull(members, "members");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .addPathSegment("members")
            .build()
            .toString();

        VoidRequest request = new VoidRequest(client, url, "DELETE");
        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        request.setBody(members);
        return request;
    }

    // Organization connections

    public Request<EnabledConnectionsPage> getConnections(String orgId, PageFilter filter) {
        Asserts.assertNotNull(orgId, "organization ID");

        HttpUrl.Builder builder = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .addPathSegment("enabled_connections");

        if (filter != null) {
            for (Map.Entry<String, Object> e : filter.getAsMap().entrySet()) {
                builder.addQueryParameter(e.getKey(), String.valueOf(e.getValue()));
            }
        }

        String url = builder.build().toString();
        CustomRequest<EnabledConnectionsPage> request = new CustomRequest<>(client, url, "GET", new TypeReference<EnabledConnectionsPage>() {
        });
        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return request;
    }

    public Request<EnabledConnection> addConnection(String  orgId, EnabledConnection connection) {
        Asserts.assertNotNull(orgId, "organization ID");
        Asserts.assertNotNull(connection, "connection");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .addPathSegment("enabled_connections")
            .build()
            .toString();

        // TODO confirm that no response is set
        CustomRequest<EnabledConnection> request = new CustomRequest<>(client, url, "POST", new TypeReference<EnabledConnection>() {
        });
        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        request.setBody(connection);
        return request;
    }

    public Request deleteConnection(String  orgId, String connectionId) {
        Asserts.assertNotNull(orgId, "organization ID");
        Asserts.assertNotNull(connectionId, "connection ID");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .addPathSegment("enabled_connections")
            .addPathSegment(connectionId)
            .build()
            .toString();

        VoidRequest voidRequest = new VoidRequest(client, url, "DELETE");
        voidRequest.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return voidRequest;
    }

    //  TODO - should it just take the EnabledConnection and use the ID from there? What do other update APIs do?
    //   -- seems they take it explicitly (e.g., update user)
    public Request<EnabledConnection> updateConnection(String orgId, String connectionId, EnabledConnection connection) {
        Asserts.assertNotNull(orgId, "organization ID");
        Asserts.assertNotNull(connectionId, "connection ID");
        Asserts.assertNotNull(connection, "connection");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .addPathSegment("enabled_connections")
            .addPathSegment(connectionId)
            .build()
            .toString();

        CustomRequest<EnabledConnection> request = new CustomRequest<>(client, url, "PATCH", new TypeReference<EnabledConnection>() {
        });
        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        request.setBody(connection);
        return request;
    }

    // Org roles

    public Request<RolesPage> getRoles(String orgId, String userId, PageFilter filter) {
        Asserts.assertNotNull(orgId, "organization ID");
        Asserts.assertNotNull(userId, "user ID");

        HttpUrl.Builder builder = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .addPathSegment("members")
            .addPathSegment(userId)
            .addPathSegment("roles");

        if (filter != null) {
            for (Map.Entry<String, Object> e : filter.getAsMap().entrySet()) {
                builder.addQueryParameter(e.getKey(), String.valueOf(e.getValue()));
            }
        }

        String url = builder.build().toString();
        CustomRequest<RolesPage> request = new CustomRequest<>(client, url, "GET", new TypeReference<RolesPage>() {
        });
        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return request;
    }

    public Request addRoles(String orgId, String userId, Roles roles) {
        Asserts.assertNotNull(orgId, "organization ID");
        Asserts.assertNotNull(userId, "user ID");
        Asserts.assertNotNull(roles, "roles");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .addPathSegment("members")
            .addPathSegment(userId)
            .addPathSegment("roles")
            .build()
            .toString();

        VoidRequest request = new VoidRequest(client, url, "POST");
        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        request.setBody(roles);
        return request;
    }

    public Request deleteRoles(String orgId, String userId, Roles roles) {
        Asserts.assertNotNull(orgId, "organization ID");
        Asserts.assertNotNull(userId, "user ID");
        Asserts.assertNotNull(roles, "roles");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ORGS_PATH)
            .addPathSegment(orgId)
            .addPathSegment("members")
            .addPathSegment(userId)
            .addPathSegment("roles")
            .build()
            .toString();

        VoidRequest request = new VoidRequest(client, url, "DELETE");
        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        request.setBody(roles);
        return request;
    }
}
