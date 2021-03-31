package com.auth0.json.mgmt.organizations;

import com.auth0.json.JsonMatcher;
import com.auth0.json.JsonTest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RolesTest extends JsonTest<Role> {

    @Test
    public void shouldSerialize() throws Exception {
        Role role = new Role();
        role.setName("role-name");
        role.setDescription("role description");

        String serialized = toJSON(role);
        assertThat(serialized, is(notNullValue()));
        assertThat(serialized, JsonMatcher.hasEntry("name", "role-name"));
        assertThat(serialized, JsonMatcher.hasEntry("description", "role description"));
    }

    @Test
    public void shouldDeserialize() throws Exception {
        String json = "{\n" +
            "   \"id\": \"rol_1\",\n" +
            "   \"name\": \"role name\",\n" +
            "   \"description\": \"role description\"\n" +
            "}";

        Role role = fromJSON(json, Role.class);
        assertThat(role, is(notNullValue()));
        assertThat(role.getId(), is("rol_1"));
        assertThat(role.getName(), is("role name"));
        assertThat(role.getDescription(), is("role description"));
    }
}
