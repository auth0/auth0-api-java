package com.auth0.client.mgmt;

import com.auth0.client.MockServer;
import com.auth0.client.mgmt.filter.PageFilter;
import com.auth0.json.mgmt.organizations.*;
import com.auth0.net.Request;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;

import java.util.*;

import static com.auth0.client.MockServer.*;
import static com.auth0.client.RecordedRequestMatcher.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrganizationEntityTest extends BaseMgmtEntityTest {

    //  Organizations entity

    @Test
    public void shouldListOrgsWithoutFilter() throws Exception {
        Request<OrganizationsPage> request = api.organizations().list(null);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATIONS_LIST, 200);
        OrganizationsPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(2));
    }

    @Test
    public void shouldListOrgsWithPage() throws Exception {
        PageFilter filter = new PageFilter().withPage(2, 30);
        Request<OrganizationsPage> request = api.organizations().list(filter);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATIONS_PAGED_LIST, 200);
        OrganizationsPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("page", "2"));
        assertThat(recordedRequest, hasQueryParameter("per_page", "30"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(2));
    }

    @Test
    public void shouldListOrgsWithTotals() throws Exception {
        PageFilter filter = new PageFilter().withTotals(true);
        Request<OrganizationsPage> request = api.organizations().list(filter);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATIONS_PAGED_LIST, 200);
        OrganizationsPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("include_totals", "true"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(2));
    }

    @Test
    public void shouldThrowOnGetOrgWithNullId() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().get(null);
    }

    @Test
    public void shouldGetOrganization() throws Exception {
        Request<Organization> request = api.organizations().get("org_123");
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATION, 200);
        Organization response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations/org_123"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldThrowOnGetOrgWithNullName() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization name");
        api.organizations().getByName(null);
    }

    @Test
    public void shouldGetOrganizationByName() throws Exception {
        Request<Organization> request = api.organizations().getByName("org-1");
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATION, 200);
        Organization response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations/name/org-1"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldThrowOnCreateOrgWithNullOrg() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization");
        api.organizations().create(null);
    }

    @Test
    public void shouldCreateOrganization() throws Exception {
        Organization orgToCreate = new Organization("test-org");
        orgToCreate.setDisplayName("display name");

        Color color = new Color();
        color.setPageBackground("#FF0000");
        color.setPrimary("#FF0000");
        Branding branding = new Branding();
        branding.setColors(color);
        branding.setLogoUrl("https://some-uri.com");
        orgToCreate.setBranding(branding);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key1", "val1");
        orgToCreate.setMetadata(metadata);

        Request<Organization> request = api.organizations().create(orgToCreate);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATION, 200);
        Organization response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("POST", "/api/v2/organizations"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        /*
{
    name: "acme",
    display_name: "Acme Inc",
    branding: {
      logo_uri: 'http://some-url.com/',
      color: {
        primary: '#FF0000', // HEX Color
        page_background: '#FF0000'  //HEX Color
      }
    },
    metadata: {
      'foo': 'bar'
    }
}
         */
        Map<String, Object> body = bodyFromRequest(recordedRequest);
        assertThat(body.size(), is(4));
        assertThat(body, hasEntry("name", "test-org"));
        assertThat(body, hasEntry("display_name", "display name"));
        Map<String, Object> brandingMap = new HashMap<>();
        brandingMap.put("logo_uri", "https://some-url.com");
        Map<String, String> colorMap = new HashMap<>();
        colorMap.put("primary", "#FF0000");
        colorMap.put("page_background", "#FF0000");
        brandingMap.put("color", colorMap);

        Object branding1 = body.get("branding");

        // TODO - assert branding and metadata
//        assertThat(body, hasEntry("branding", is(brandingMap)));
//        assertThat(body, hasEntry("branding", is(orgToCreate.getBranding())));
//        assertThat(body, hasEntry("branding", brandingMap));
//        assertThat(body, hasEntry("metadata", orgToCreate.getMetadata()));

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldThrowOnUpdateOrgWithNullOrgId() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().update(null, new Organization());
    }

    @Test
    public void shouldThrowOnUpdateOrgWithNullOrg() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization");
        api.organizations().update("org_123", null);
    }

    @Test
    public void shouldUpdateOrganization() throws Exception {
        Organization orgToUpdate = new Organization("test-org");
        orgToUpdate.setDisplayName("display name");

        Color color = new Color();
        color.setPageBackground("#FF0000");
        color.setPrimary("#FF0000");
        Branding branding = new Branding();
        branding.setColors(color);
        branding.setLogoUrl("https://some-uri.com");
        orgToUpdate.setBranding(branding);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key1", "val1");
        orgToUpdate.setMetadata(metadata);

        Request<Organization> request = api.organizations().update("org_abc", orgToUpdate);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATION, 200);
        Organization response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("PATCH", "/api/v2/organizations/org_abc"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        /*
{
    name: "acme",
    display_name: "Acme Inc",
    branding: {
      logo_uri: 'http://some-url.com/',
      color: {
        primary: '#FF0000', // HEX Color
        page_background: '#FF0000'  //HEX Color
      }
    },
    metadata: {
      'foo': 'bar'
    }
}
         */
        Map<String, Object> body = bodyFromRequest(recordedRequest);
        assertThat(body.size(), is(4));
        assertThat(body, hasEntry("name", "test-org"));
        assertThat(body, hasEntry("display_name", "display name"));
        Map<String, Object> brandingMap = new HashMap<>();
        brandingMap.put("logo_uri", "https://some-url.com");
        Map<String, String> colorMap = new HashMap<>();
        colorMap.put("primary", "#FF0000");
        colorMap.put("page_background", "#FF0000");
        brandingMap.put("color", colorMap);

        Object branding1 = body.get("branding");

        // TODO - assert branding and metadata
//        assertThat(body, hasEntry("branding", is(brandingMap)));
//        assertThat(body, hasEntry("branding", is(orgToCreate.getBranding())));
//        assertThat(body, hasEntry("branding", brandingMap));
//        assertThat(body, hasEntry("metadata", orgToCreate.getMetadata()));

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldThrowOnDeleteOrgWithNullId() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().delete(null);
    }

    @Test
    public void shouldDeleteOrganization() throws Exception {
        Request request = api.organizations().delete("org_abc");
        assertThat(request, is(notNullValue()));

        server.emptyResponse(204);
        request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("DELETE", "/api/v2/organizations/org_abc"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
    }

    // Organization members entity

    @Test
    public void shouldThrowOnGetMembersWithNullId() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().getMembers(null, null);
    }

    @Test
    public void shouldListOrgMembersWithoutFilter() throws Exception {
        Request<MembersPage> request = api.organizations().getMembers("org_abc", null);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATION_MEMBERS_LIST, 200);
        MembersPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations/org_abc/members"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(3));
    }

    @Test
    public void shouldListOrgMembersWithPage() throws Exception {
        PageFilter filter = new PageFilter().withPage(0, 20);
        Request<MembersPage> request = api.organizations().getMembers("org_abc", filter);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATION_MEMBERS_LIST, 200);
        MembersPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations/org_abc/members"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("page", "0"));
        assertThat(recordedRequest, hasQueryParameter("per_page", "20"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(3));
    }

    @Test
    public void shouldListOrgMembersWithTotals() throws Exception {
        PageFilter filter = new PageFilter().withTotals(true);
        Request<MembersPage> request = api.organizations().getMembers("org_abc", filter);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATION_MEMBERS_PAGED_LIST, 200);
        MembersPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations/org_abc/members"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("include_totals", "true"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(3));
    }

    @Test
    public void shouldThrowOnAddMembersWhenOrgIdNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().addMembers(null, new Members(Collections.singletonList("user1")));
    }

    @Test
    public void shouldThrowOnAddMembersWhenMembersIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("members");
        api.organizations().addMembers("org_abc", null);
    }

    @Test
    public void shouldAddMembersToOrganization() throws Exception {
        List<String> membersList = Arrays.asList("user1", "user2");
        Members members = new Members(membersList);
        Request request = api.organizations().addMembers("org_abc", members);
        assertThat(request, is(notNullValue()));

        server.emptyResponse(204);
        request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("POST", "/api/v2/organizations/org_abc/members"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        Map<String, Object> body = bodyFromRequest(recordedRequest);
        assertThat(body, hasEntry("members", membersList));
    }

    @Test
    public void shouldThrowOnDeleteMembersWhenOrgIdNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().deleteMembers(null, new Members(Collections.singletonList("user1")));
    }

    @Test
    public void shouldThrowOnDeleteMembersWhenMembersIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("members");
        api.organizations().deleteMembers("org_abc", null);
    }

    @Test
    public void shouldDeleteMembersFromOrganization() throws Exception {
        List<String> membersList = Collections.singletonList("user1");
        Members members = new Members(membersList);
        Request request = api.organizations().deleteMembers("org_abc", members);
        assertThat(request, is(notNullValue()));

        server.emptyResponse(204);
        request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("DELETE", "/api/v2/organizations/org_abc/members"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        Map<String, Object> body = bodyFromRequest(recordedRequest);
        assertThat(body, hasEntry("members", membersList));
    }

    // Organization connections

    @Test
    public void shouldThrowOnGetConnectionsWhenOrgIdNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().deleteMembers(null, null);
    }

    @Test
    public void shouldListOrganizationConnectionsWithoutFilter() throws Exception {
        Request<EnabledConnectionsPage> request = api.organizations().getConnections("org_abc", null);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATION_CONNECTIONS_LIST, 200);
        EnabledConnectionsPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations/org_abc/enabled_connections"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(2));
    }

    @Test
    public void shouldGetOrganizationConnectionsWithPage() throws Exception {
        PageFilter filter = new PageFilter().withPage(2, 30);
        Request<EnabledConnectionsPage> request = api.organizations().getConnections("org_abc", filter);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATION_CONNECTIONS_LIST, 200);
        EnabledConnectionsPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations/org_abc/enabled_connections"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("page", "2"));
        assertThat(recordedRequest, hasQueryParameter("per_page", "30"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(2));
    }

    @Test
    public void shouldGetOrganizationConnectionsWithTotals() throws Exception {
        PageFilter filter = new PageFilter().withTotals(true);
        Request<EnabledConnectionsPage> request = api.organizations().getConnections("org_abc", filter);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MockServer.ORGANIZATION_CONNECTIONS_PAGED_LIST, 200);
        EnabledConnectionsPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations/org_abc/enabled_connections"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("include_totals", "true"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(2));
    }

    @Test
    public void shouldThrowOnAddConnectionWhenOrgIdNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().addConnection(null, new EnabledConnection());
    }

    @Test
    public void shouldThrowOnAddConnectionWhenConnectionIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("connection");
        api.organizations().addConnection("org_abc", null);
    }

    @Test
    public void shouldAddConnection() throws Exception {
        EnabledConnection connection = new EnabledConnection();
        connection.setAssignMembershipOnLogin(false);
        connection.setConnectionId("con_123");

        Request<EnabledConnection> request = api.organizations().addConnection("org_abc", connection);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(ORGANIZATION_CONNECTION, 201);
        request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("POST", "/api/v2/organizations/org_abc/enabled_connections"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        Map<String, Object> body = bodyFromRequest(recordedRequest);
        assertThat(body, hasEntry("connection_id", "con_123"));
        assertThat(body, hasEntry("assign_membership_on_login", false));
    }

    @Test
    public void shouldThrowOnDeleteConnectionWhenOrgIdNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().deleteConnection(null, "con_123");
    }

    @Test
    public void shouldThrowOnDeleteConnectionWhenConnectionIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("connection");
        api.organizations().deleteConnection("org_abc", null);
    }

    @Test
    public void shouldDeleteConnection() throws Exception {
        Request request = api.organizations().deleteConnection("org_abc", "con_123");
        assertThat(request, is(notNullValue()));

        server.emptyResponse(204);
        request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("DELETE", "/api/v2/organizations/org_abc/enabled_connections/con_123"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
    }

    @Test
    public void shouldThrowOnUpdateConnectionWhenOrgIdNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().updateConnection(null, "con_123", new EnabledConnection());
    }

    @Test
    public void shouldThrowOnUpdateConnectionWhenUserIdIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("connection");
        api.organizations().updateConnection("org_abc", null, new EnabledConnection());
    }

    @Test
    public void shouldThrowOnUpdateConnectionWhenConnectionIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("connection");
        api.organizations().updateConnection("org_abc", "con_123", null);
    }

    @Test
    public void shouldUpdateOrgConnection() throws Exception {
        EnabledConnection connection = new EnabledConnection();
        connection.setAssignMembershipOnLogin(true);

        Request<EnabledConnection> request = api.organizations().updateConnection("org_abc", "con_123", connection);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(ORGANIZATION_CONNECTION, 201);
        request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("PATCH", "/api/v2/organizations/org_abc/enabled_connections/con_123"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        Map<String, Object> body = bodyFromRequest(recordedRequest);
        assertThat(body, hasEntry("assign_membership_on_login", true));
    }

    // Organization roles

    @Test
    public void shouldThrowOnGetOrgRolesWhenOrgIdIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().getRoles(null, "user_123",null);
    }

    @Test
    public void shouldThrowOnGetOrgRolesWhenUserIdIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("user ID");
        api.organizations().getRoles("org_abc", null, null);
    }

    @Test
    public void shouldGetOrgRolesWithoutPaging() throws Exception {
        Request<RolesPage> request = api.organizations().getRoles("org_abc", "user_123", null);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(ORGANIZATION_MEMBER_ROLES_LIST, 200);
        RolesPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations/org_abc/members/user_123/roles"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(1));
    }

    @Test
    public void shouldGetOrgRolesWithPaging() throws Exception {
        Request<RolesPage> request = api.organizations().getRoles("org_abc", "user_123", new PageFilter().withPage(0, 20));
        assertThat(request, is(notNullValue()));

        server.jsonResponse(ORGANIZATION_MEMBER_ROLES_LIST, 200);
        RolesPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations/org_abc/members/user_123/roles"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("page", "0"));
        assertThat(recordedRequest, hasQueryParameter("per_page", "20"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(1));
    }

    @Test
    public void shouldGetOrgRolesWithIncludeTotals() throws Exception {
        Request<RolesPage> request = api.organizations().getRoles("org_abc", "user_123", new PageFilter().withTotals(true));
        assertThat(request, is(notNullValue()));

        server.jsonResponse(ORGANIZATION_MEMBER_ROLES_PAGED_LIST, 200);
        RolesPage response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/organizations/org_abc/members/user_123/roles"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("include_totals", "true"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getItems(), hasSize(1));
    }

    @Test
    public void shouldThrowOnAddOrgRolesWhenOrgIdIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().addRoles(null, "user_123", new Roles(Collections.singletonList("role_id")));
    }

    @Test
    public void shouldThrowOnAddOrgRolesWhenUserIdIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("user ID");
        api.organizations().addRoles("org_abc", null, new Roles(Collections.singletonList("role_id")));
    }

    @Test
    public void shouldThrowOnAddOrgRolesWhenRolesIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("roles");
        api.organizations().addRoles("org_abc", "user_123", null);
    }

    @Test
    public void shouldAddOrgRoles() throws Exception {
        List<String> rolesList = Arrays.asList("role_1", "role_2");
        Request request = api.organizations().addRoles("org_abc", "user_123", new Roles(rolesList));
        assertThat(request, is(notNullValue()));

        server.emptyResponse(204);
        request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("POST", "/api/v2/organizations/org_abc/members/user_123/roles"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        Map<String, Object> body = bodyFromRequest(recordedRequest);
        assertThat(body, hasEntry("roles", rolesList));
    }

    @Test
    public void shouldThrowOnDeleteOrgRolesWhenOrgIdIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("organization ID");
        api.organizations().deleteRoles(null, "user_123", new Roles(Collections.singletonList("role_id")));
    }

    @Test
    public void shouldThrowDeleteOrgRolesWhenUserIdIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("user ID");
        api.organizations().deleteRoles("org_abc", null, new Roles(Collections.singletonList("role_id")));
    }

    @Test
    public void shouldThrowOnDeleteOrgRolesWhenRolesIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("roles");
        api.organizations().deleteRoles("org_abc", "user_123", null);
    }

    @Test
    public void shouldDeleteOrgRoles() throws Exception {
        List<String> rolesList = Arrays.asList("role_1", "role_2");
        Request request = api.organizations().deleteRoles("org_abc", "user_123", new Roles(rolesList));
        assertThat(request, is(notNullValue()));

        server.emptyResponse(204);
        request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("DELETE", "/api/v2/organizations/org_abc/members/user_123/roles"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        Map<String, Object> body = bodyFromRequest(recordedRequest);
        assertThat(body, hasEntry("roles", rolesList));
    }
}
