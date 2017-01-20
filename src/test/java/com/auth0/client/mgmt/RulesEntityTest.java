package com.auth0.client.mgmt;

import com.auth0.client.mgmt.filter.RulesFilter;
import com.auth0.json.mgmt.Rule;
import com.auth0.net.Request;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.auth0.client.MockServer.*;
import static com.auth0.client.RecordedRequestMatcher.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class RulesEntityTest extends BaseMgmtEntityTest {

    @Test
    public void shouldListRules() throws Exception {
        Request<List<Rule>> request = api.rules().listRules(null);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_RULES_LIST, 200);
        List<Rule> response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/rules"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(2));
    }

    @Test
    public void shouldListRulesWithEnabled() throws Exception {
        RulesFilter filter = new RulesFilter().withEnabled(true);
        Request<List<Rule>> request = api.rules().listRules(filter);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_RULES_LIST, 200);
        List<Rule> response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/rules"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("enabled", "true"));

        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(2));
    }

    @Test
    public void shouldListRulesWithFields() throws Exception {
        RulesFilter filter = new RulesFilter().withFields("some,random,fields", true);
        Request<List<Rule>> request = api.rules().listRules(filter);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_RULES_LIST, 200);
        List<Rule> response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/rules"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("fields", "some,random,fields"));
        assertThat(recordedRequest, hasQueryParameter("include_fields", "true"));

        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(2));
    }

    @Test
    public void shouldReturnEmptyRules() throws Exception {
        Request<List<Rule>> request = api.rules().listRules(null);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_EMPTY_LIST, 200);
        List<Rule> response = request.execute();

        assertThat(response, is(notNullValue()));
        assertThat(response, is(emptyCollectionOf(Rule.class)));
    }

    @Test
    public void shouldThrowOnGetRuleWithNullId() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'rule id' cannot be null!");
        api.rules().getRule(null, null);
    }

    @Test
    public void shouldGetRule() throws Exception {
        Request<Rule> request = api.rules().getRule("1", null);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_RULE, 200);
        Rule response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/rules/1"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldGetRuleWithFields() throws Exception {
        RulesFilter filter = new RulesFilter().withFields("some,random,fields", true);
        Request<Rule> request = api.rules().getRule("1", filter);
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_RULE, 200);
        Rule response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/rules/1"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("fields", "some,random,fields"));
        assertThat(recordedRequest, hasQueryParameter("include_fields", "true"));

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldThrowOnCreateRuleWithNullData() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'rule' cannot be null!");
        api.rules().createRule(null);
    }

    @Test
    public void shouldCreateRule() throws Exception {
        Request<Rule> request = api.rules().createRule(new Rule("my-rule", "function(){}"));
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_RULE, 200);
        Rule response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("POST", "/api/v2/rules"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        Map<String, Object> body = bodyFromRequest(recordedRequest);
        assertThat(body.size(), is(2));
        assertThat(body, hasEntry("name", (Object) "my-rule"));
        assertThat(body, hasEntry("script", (Object) "function(){}"));

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldThrowOnDeleteRuleWithNullId() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'rule id' cannot be null!");
        api.rules().deleteRule(null);
    }

    @Test
    public void shouldDeleteRule() throws Exception {
        Request request = api.rules().deleteRule("1");
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_RULE, 200);
        request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("DELETE", "/api/v2/rules/1"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
    }

    @Test
    public void shouldThrowOnUpdateRuleWithNullId() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'rule id' cannot be null!");
        api.rules().updateRule(null, new Rule("my-rule", "function(){}"));
    }

    @Test
    public void shouldThrowOnUpdateRuleWithNullData() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'rule' cannot be null!");
        api.rules().updateRule("1", null);
    }

    @Test
    public void shouldUpdateRule() throws Exception {
        Request<Rule> request = api.rules().updateRule("1", new Rule("my-rule", "function(){}"));
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_CONNECTION, 200);
        Rule response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("PATCH", "/api/v2/rules/1"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        Map<String, Object> body = bodyFromRequest(recordedRequest);
        assertThat(body.size(), is(2));
        assertThat(body, hasEntry("name", (Object) "my-rule"));
        assertThat(body, hasEntry("script", (Object) "function(){}"));

        assertThat(response, is(notNullValue()));
    }
}
