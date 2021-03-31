package com.auth0.json.mgmt.organizations;

import com.auth0.json.JsonTest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class OrganizationsPageTest extends JsonTest<OrganizationsPage> {
    private static final String jsonWithoutTotals = "[\n" +
        "    {\n" +
        "        \"id\": \"org_1\",\n" +
        "        \"name\": \"org-1\",\n" +
        "        \"display_name\": \"org 1\",\n" +
        "        \"branding\": {\n" +
        "            \"logo_url\": \"https://some-url.com/\",\n" +
        "            \"colors\": {\n" +
        "                \"primary\": \"#FF0000\",\n" +
        "                \"page_background\": \"#FF0000\"\n" +
        "            }\n" +
        "        },\n" +
        "        \"metadata\": {\n" +
        "            \"key1\": \"val1\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"id\": \"org_2\",\n" +
        "        \"name\": \"org-2\",\n" +
        "        \"display_name\": \"org 2\"\n" +
        "    }\n" +
        "]";

    private static final String jsonWithTotals = "{\n" +
        "    \"organizations\": [\n" +
        "        {\n" +
        "            \"id\": \"org_2\",\n" +
        "            \"name\": \"org-1\",\n" +
        "            \"display_name\": \"org 1\",\n" +
        "            \"branding\": {\n" +
        "                \"logo_url\": \"https://some-url.com/\",\n" +
        "                \"colors\": {\n" +
        "                    \"primary\": \"#FF0000\",\n" +
        "                    \"page_background\": \"#FF0000\"\n" +
        "                }\n" +
        "            },\n" +
        "            \"metadata\": {\n" +
        "                \"key1\": \"val1\"\n" +
        "            }\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": \"org_2\",\n" +
        "            \"name\": \"org-2\",\n" +
        "            \"display_name\": \"org 2\"\n" +
        "        }\n" +
        "    ],\n" +
        "    \"start\": 0,\n" +
        "    \"limit\": 20,\n" +
        "    \"total\": 2\n" +
        "}";

    @Test
    public void shouldDeserializeWithoutTotals() throws Exception {
        OrganizationsPage page = fromJSON(jsonWithoutTotals, OrganizationsPage.class);

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
        OrganizationsPage page = fromJSON(jsonWithTotals, OrganizationsPage.class);

        assertThat(page, is(notNullValue()));
        assertThat(page.getStart(), is(0));
        assertThat(page.getTotal(), is(2));
        assertThat(page.getLimit(), is(20));
        assertThat(page.getItems(), is(notNullValue()));
        assertThat(page.getItems().size(), is(2));
    }
}
