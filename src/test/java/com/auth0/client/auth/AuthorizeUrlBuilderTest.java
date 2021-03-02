package com.auth0.client.auth;

import okhttp3.HttpUrl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URLEncoder;

import static com.auth0.client.UrlMatcher.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class AuthorizeUrlBuilderTest {

    private static final HttpUrl DOMAIN = HttpUrl.parse("https://domain.auth0.com");
    private static final String CLIENT_ID = "clientId";
    private static final String REDIRECT_URI = "https://domain.auth0.com/callback";

    @SuppressWarnings("deprecation")
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldThrowWhenBaseUrlIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'base url' cannot be null!");
        AuthorizeUrlBuilder.newInstance(null, CLIENT_ID, REDIRECT_URI);
    }

    @Test
    public void shouldThrowWhenRedirectUriIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'redirect uri' cannot be null!");
        AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, null);
    }

    @Test
    public void shouldThrowWhenClientIdIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'client id' cannot be null!");
        AuthorizeUrlBuilder.newInstance(DOMAIN, null, REDIRECT_URI);
    }

    @Test
    public void shouldGetNewInstance() {
        AuthorizeUrlBuilder instance = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI);
        assertThat(instance, is(notNullValue()));
    }

    @Test
    public void shouldBuildValidAuthorizeUrlWithHttp() {
        HttpUrl httpBaseUrl = HttpUrl.parse("http://domain.auth0.com");
        String url = AuthorizeUrlBuilder.newInstance(httpBaseUrl, CLIENT_ID, REDIRECT_URI).build();
        assertThat(url, isUrl("http", "domain.auth0.com", "/authorize"));
    }

    @Test
    public void shouldBuildValidAuthorizeUrlWithHttps() {
        HttpUrl httpsBaseUrl = HttpUrl.parse("https://domain.auth0.com");
        String url = AuthorizeUrlBuilder.newInstance(httpsBaseUrl, CLIENT_ID, REDIRECT_URI).build();
        assertThat(url, isUrl("https", "domain.auth0.com", "/authorize"));
    }

    @Test
    public void shouldAddResponseTypeCode() {
        String url = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI).build();
        assertThat(url, hasQueryParameter("response_type", "code"));
    }

    @Test
    public void shouldAddClientId() {
        String url = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI).build();
        assertThat(url, hasQueryParameter("client_id", CLIENT_ID));

    }

    @Test
    public void shouldAddRedirectUri() {
        String url = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI).build();
        assertThat(url, hasQueryParameter("redirect_uri", REDIRECT_URI));
    }

    @Test
    public void shouldNotEncodeTwiceTheRedirectUri() throws Exception {
        String encodedUrl = URLEncoder.encode("https://www.google.com/?src=her&q=ans", "UTF-8");
        String url = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, encodedUrl).build();
        assertThat(url, encodedQueryContains("redirect_uri=" + encodedUrl));
    }

    @Test
    public void shouldSetConnection() {
        String url = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withConnection("my-connection")
                .build();
        assertThat(url, hasQueryParameter("connection", "my-connection"));
    }

    @Test
    public void shouldThrowWhenConnectionIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'connection' cannot be null!");
        AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withConnection(null);
    }

    @Test
    public void shouldSetAudience() {
        String url = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withAudience("https://myapi.domain.com/users")
                .build();
        assertThat(url, hasQueryParameter("audience", "https://myapi.domain.com/users"));
    }

    @Test
    public void shouldThrowWhenAudienceIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'audience' cannot be null!");
        AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withAudience(null);
    }

    @Test
    public void shouldSetState() {
        String url = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withState("1234567890")
                .build();
        assertThat(url, hasQueryParameter("state", "1234567890"));
    }

    @Test
    public void shouldThrowWhenStateIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'state' cannot be null!");
        AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withState(null);
    }

    @Test
    public void shouldSetScope() {
        String url = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withScope("profile email contacts")
                .build();
        assertThat(url, hasQueryParameter("scope", "profile email contacts"));
    }

    @Test
    public void shouldThrowWhenScopeIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'scope' cannot be null!");
        AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withScope(null);
    }

    @Test
    public void shouldSetResponseType() {
        String url = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withResponseType("token id_token")
                .build();
        assertThat(url, hasQueryParameter("response_type", "token id_token"));
    }

    @Test
    public void shouldThrowWhenResponseTypeIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'response type' cannot be null!");
        AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withResponseType(null);
    }

    @Test
    public void shouldSetCustomParameter() {
        String url = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withParameter("name", "value")
                .build();
        assertThat(url, hasQueryParameter("name", "value"));
    }

    @Test
    public void shouldThrowWhenCustomParameterNameIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'name' cannot be null!");
        AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withParameter(null, "value");
    }

    @Test
    public void shouldThrowWhenCustomParameterValueIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'value' cannot be null!");
        AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
                .withParameter("name", null);
    }

    @Test
    public void shouldAddOrganizationParameter() {
        String authUrl = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
            .withOrganization("org_abc")
            .build();
        assertThat(authUrl, hasQueryParameter("organization", "org_abc"));
    }

    @Test
    public void shouldThrowWhenOrganizationIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'organization' cannot be null!");
        String authUrl = AuthorizeUrlBuilder.newInstance(DOMAIN, CLIENT_ID, REDIRECT_URI)
            .withOrganization(null)
            .build();
        assertThat(authUrl, hasQueryParameter("organization", "org_abc"));
    }
}
