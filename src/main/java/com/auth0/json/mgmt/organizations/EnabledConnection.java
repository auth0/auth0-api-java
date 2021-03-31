package com.auth0.json.mgmt.organizations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the enabled connection object for an organization. Related to the {@linkplain com.auth0.client.mgmt.OrganizationsEntity}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnabledConnection {

    @JsonProperty("connection")
    private Connection connection;
    @JsonProperty("assign_membership_on_login")
    private boolean assignMembershipOnLogin;
    @JsonProperty("connection_id")
    private String connectionId;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isAssignMembershipOnLogin() {
        return assignMembershipOnLogin;
    }

    public void setAssignMembershipOnLogin(boolean assignMembershipOnLogin) {
        this.assignMembershipOnLogin = assignMembershipOnLogin;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }
}
