package com.auth0.json.mgmt.organizations;

import com.auth0.json.JsonTest;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MembersPageTest extends JsonTest<MembersPage> {
    private static final String jsonWithoutTotals = "[\n" +
        "  {\n" +
        "    \"user_id\": \"auth0|605a1f57cbeb2c0070fdf123\",\n" +
        "    \"email\": \"dave@domain.com\",\n" +
        "    \"picture\": \"https://domain.com/img.png\",\n" +
        "    \"name\": \"dave\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"user_id\": \"auth0|605a0fc1bef67f006851a123\",\n" +
        "    \"email\": \"eric@domain.com\",\n" +
        "    \"picture\": \"https://domain.com/img.png\",\n" +
        "    \"name\": \"eric\"\n" +
        "  }\n" +
        "]\n";

    private static final String jsonWithTotals = "{\n" +
        "  \"members\": [\n" +
        "    {\n" +
        "      \"user_id\": \"auth0|605a1f57cbeb2c0070fdf123\",\n" +
        "      \"email\": \"dave@domain.com\",\n" +
        "      \"picture\": \"https://domain.com/img.png\",\n" +
        "      \"name\": \"dave\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"user_id\": \"auth0|605a0fc1bef67f006851a123\",\n" +
        "      \"email\": \"eric@domain.com\",\n" +
        "      \"picture\": \"https://domain.com/img.png\",\n" +
        "      \"name\": \"eric\"\n" +
        "    }\n" +
        "  ],\n" +
        "  \"start\": 0,\n" +
        "  \"limit\": 20,\n" +
        "  \"total\": 2\n" +
        "}\n";

    private static final String jsonWithCheckpointPageResponse = "{\n" +
        "  \"members\": [\n" +
        "    {\n" +
        "      \"user_id\": \"auth0|605a1f57cbeb2c0070fdf123\",\n" +
        "      \"email\": \"dave@domain.com\",\n" +
        "      \"picture\": \"https://domain.com/img.png\",\n" +
        "      \"name\": \"dave\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"user_id\": \"auth0|605a0fc1bef67f006851a123\",\n" +
        "      \"email\": \"eric@domain.com\",\n" +
        "      \"picture\": \"https://domain.com/img.png\",\n" +
        "      \"name\": \"eric\"\n" +
        "    }\n" +
        "  ],\n" +
        "  \"next\": \"MjAyMS0wMy0yOSAxNjo1MDo09s44NDYxODcrMDAsb3JnX2Y0VXZUbG1iSWd2005zTGw\"\n" +
        "}\n";

    @Test
    public void shouldDeserializeWithoutTotals() throws Exception {
        MembersPage page = fromJSON(jsonWithoutTotals, MembersPage.class);

        assertThat(page, is(notNullValue()));
        assertThat(page.getStart(), is(nullValue()));
        assertThat(page.getLength(), is(nullValue()));
        assertThat(page.getTotal(), is(nullValue()));
        assertThat(page.getLimit(), is(nullValue()));
        assertThat(page.getNext(), is(nullValue()));
        assertThat(page.getItems(), is(notNullValue()));
        assertThat(page.getItems().size(), is(2));
    }

    @Test
    public void shouldDeserializeWithTotals() throws Exception {
        MembersPage page = fromJSON(jsonWithTotals, MembersPage.class);

        assertThat(page, is(notNullValue()));
        assertThat(page.getStart(), is(0));
        assertThat(page.getTotal(), is(2));
        assertThat(page.getLimit(), is(20));
        assertThat(page.getNext(), is(nullValue()));
        assertThat(page.getItems(), is(notNullValue()));
        assertThat(page.getItems().size(), is(2));
    }

    @Test
    public void shouldDeserializeWithNextItemPointer() throws Exception {
        MembersPage page = fromJSON(jsonWithCheckpointPageResponse, MembersPage.class);

        assertThat(page, is(notNullValue()));

        assertThat(page.getStart(), is(nullValue()));
        assertThat(page.getTotal(), is(nullValue()));
        assertThat(page.getLimit(), is(nullValue()));
        assertThat(page.getNext(), is("MjAyMS0wMy0yOSAxNjo1MDo09s44NDYxODcrMDAsb3JnX2Y0VXZUbG1iSWd2005zTGw"));
        assertThat(page.getItems(), is(notNullValue()));
        assertThat(page.getItems().size(), is(2));
    }

    @Test
    public void shouldBeCreatedWithoutNextField() {
        MembersPage page = new MembersPageDeserializer().createPage(0, 5, 20, 50, new ArrayList<>());

        assertThat(page.getStart(), is(0));
        assertThat(page.getLength(), is(5));
        assertThat(page.getTotal(), is(20));
        assertThat(page.getLimit(), is(50));
        assertThat(page.getItems(), is(notNullValue()));
    }
}

