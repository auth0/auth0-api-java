package com.auth0.json.mgmt.organizations;

import com.auth0.json.JsonTest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RolesPageTest extends JsonTest<RolesPage> {

    private static String jsonWithoutTotals = "[\n" +
        "    {\n" +
        "        \"id\": \"rol_1\",\n" +
        "        \"name\": \"role name\",\n" +
        "        \"description\": \"role description\"\n" +
        "    }\n" +
        "]";

    private static String jsonWithTotals = "{\n" +
        "    \"roles\": [\n" +
        "        {\n" +
        "            \"id\": \"rol_1\",\n" +
        "            \"name\": \"role name\",\n" +
        "            \"description\": \"role description\"\n" +
        "        }\n" +
        "    ],\n" +
        "    \"start\": 0,\n" +
        "    \"limit\": 50,\n" +
        "    \"total\": 1\n" +
        "}";

    @Test
    public void shouldDeserializeWithoutTotals() throws Exception {
        RolesPage page = fromJSON(jsonWithoutTotals, RolesPage.class);

        assertThat(page, is(notNullValue()));
        assertThat(page.getStart(), is(nullValue()));
        assertThat(page.getLength(), is(nullValue()));
        assertThat(page.getTotal(), is(nullValue()));
        assertThat(page.getLimit(), is(nullValue()));
        assertThat(page.getItems(), is(notNullValue()));
        assertThat(page.getItems().size(), is(1));
    }

    @Test
    public void shouldDeserializeWithTotals() throws Exception {
        RolesPage page = fromJSON(jsonWithTotals, RolesPage.class);

        assertThat(page, is(notNullValue()));
        assertThat(page, is(notNullValue()));
        assertThat(page.getStart(), is(0));
        assertThat(page.getTotal(), is(1));
        assertThat(page.getLimit(), is(50));
        assertThat(page.getItems(), is(notNullValue()));
        assertThat(page.getItems().size(), is(1));
    }
}
