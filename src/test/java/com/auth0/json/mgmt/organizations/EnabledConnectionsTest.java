package com.auth0.json.mgmt.organizations;

import com.auth0.json.JsonMatcher;
import com.auth0.json.JsonTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EnabledConnectionsTest extends JsonTest<EnabledConnection> {

    @Test
    public void shouldSerialize() throws Exception {
        Connection connection = new Connection();
        connection.setName("con_1");
        connection.setStrategy("strategy");

        EnabledConnection enabledConnection = new EnabledConnection();
        enabledConnection.setAssignMembershipOnLogin(true);
        enabledConnection.setConnection(connection);

        String serialized = toJSON(enabledConnection);
        assertThat(serialized, is(notNullValue()));
        assertThat(serialized, JsonMatcher.hasEntry("assign_membership_on_login", true));
        assertThat(serialized, JsonMatcher.hasEntry("connection", is(notNullValue())));

        Map<String, String> connectionMap = new HashMap<>();
        connectionMap.put("name", "con_1");
        connectionMap.put("strategy", "strategy");
        assertThat(serialized, JsonMatcher.hasEntry("connection", connectionMap));
    }

    @Test
    public void shouldDeserialize() throws Exception {
        String json = "{\n" +
            "   \"connection_id\": \"con_id\",\n" +
            "   \"assign_membership_on_login\": false,\n" +
            "   \"connection\": {\n" +
            "       \"name\": \"google-oauth2\",\n" +
            "       \"strategy\": \"google-oauth2\"\n" +
            "   }\n" +
            " }";

        EnabledConnection enabledConnection = fromJSON(json, EnabledConnection.class);
        assertThat(enabledConnection, is(notNullValue()));
        assertThat(enabledConnection.getConnectionId(), is("con_id"));
        assertThat(enabledConnection.isAssignMembershipOnLogin(), is(false));
        assertThat(enabledConnection.getConnection(), is(notNullValue()));
        assertThat(enabledConnection.getConnection().getName(), is("google-oauth2"));
        assertThat(enabledConnection.getConnection().getStrategy(), is("google-oauth2"));
    }
}
