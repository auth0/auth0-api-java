package com.auth0.json.mgmt.organizations;

import com.auth0.json.JsonTest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EnabledConnectionsPageTest extends JsonTest<EnabledConnectionsPage> {

    private static String jsonWithoutTotals = "[\n" +
        "    {\n" +
        "        \"connection_id\": \"con_1\",\n" +
        "        \"assign_membership_on_login\": false,\n" +
        "        \"connection\": {\n" +
        "            \"name\": \"google-oauth2\",\n" +
        "            \"strategy\": \"google-oauth2\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"connection_id\": \"con_2\",\n" +
        "        \"assign_membership_on_login\": true,\n" +
        "        \"connection\": {\n" +
        "            \"name\": \"Username-Password-Authentication\",\n" +
        "            \"strategy\": \"auth0\"\n" +
        "        }\n" +
        "    }\n" +
        "]";

    private static String jsonWithTotals = "{\n" +
        "    \"enabled_connections\": [\n" +
        "        {\n" +
        "            \"connection_id\": \"con_1\",\n" +
        "            \"assign_membership_on_login\": false,\n" +
        "            \"connection\": {\n" +
        "                \"name\": \"google-oauth2\",\n" +
        "                \"strategy\": \"google-oauth2\"\n" +
        "            }\n" +
        "        },\n" +
        "        {\n" +
        "            \"connection_id\": \"con_2\",\n" +
        "            \"assign_membership_on_login\": true,\n" +
        "            \"connection\": {\n" +
        "                \"name\": \"Username-Password-Authentication\",\n" +
        "                \"strategy\": \"auth0\"\n" +
        "            }\n" +
        "        }\n" +
        "    ],\n" +
        "    \"start\": 0,\n" +
        "    \"limit\": 2,\n" +
        "    \"total\": 2\n" +
        "}";

    @Test
    public void shouldDeserializeWithoutTotals() throws Exception {
        EnabledConnectionsPage page = fromJSON(jsonWithoutTotals, EnabledConnectionsPage.class);

        assertThat(page, is(notNullValue()));
        assertThat(page.getStart(), is(nullValue()));
        assertThat(page.getLength(), is(nullValue()));
        assertThat(page.getTotal(), is(nullValue()));
        assertThat(page.getLimit(), is(nullValue()));
        assertThat(page.getItems(), is(notNullValue()));
        assertThat(page.getItems().size(), is(2));
    }

    @Test
    public void shouldDeserializeWithTotals() throws Exception {
        EnabledConnectionsPage page = fromJSON(jsonWithTotals, EnabledConnectionsPage.class);

        assertThat(page, is(notNullValue()));
        assertThat(page, is(notNullValue()));
        assertThat(page.getStart(), is(0));
        assertThat(page.getTotal(), is(2));
        assertThat(page.getLimit(), is(2));
        assertThat(page.getItems(), is(notNullValue()));
        assertThat(page.getItems().size(), is(2));
    }
}
