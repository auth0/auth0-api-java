package com.auth0.json.mgmt.organizations;

import com.auth0.json.JsonMatcher;
import com.auth0.json.JsonTest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MembersTest extends JsonTest<Member> {

    @Test
    public void shouldSerialize() throws Exception {
        Member member = new Member();
        member.setEmail("email@domain.com");
        member.setName("name");
        member.setPicture("https://myprofilepic.com");

        String serialized = toJSON(member);
        assertThat(serialized, is(notNullValue()));
        assertThat(serialized, JsonMatcher.hasEntry("email", "email@domain.com"));
        assertThat(serialized, JsonMatcher.hasEntry("name", "name"));
        assertThat(serialized, JsonMatcher.hasEntry("picture", "https://myprofilepic.com"));
    }

    @Test
    public void shouldDeserialize() throws Exception {
        String memberJson = "{\n" +
            "   \"user_id\": \"user_123\",\n" +
            "   \"email\": \"fred@domain.com\",\n" +
            "   \"picture\": \"https://profilepic.com/mypic.png\",\n" +
            "   \"name\": \"fred\"\n" +
            "}";

        Member member = fromJSON(memberJson, Member.class);
        assertThat(member, is(notNullValue()));
        assertThat(member.getUserId(), is("user_123"));
        assertThat(member.getEmail(), is("fred@domain.com"));
        assertThat(member.getPicture(), is("https://profilepic.com/mypic.png"));
        assertThat(member.getName(), is("fred"));

    }
}
