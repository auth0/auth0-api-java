package com.auth0.client.auth;

import okhttp3.HttpUrl;

import java.util.HashMap;
import java.util.Map;

import static com.auth0.utils.Asserts.assertNotNull;

/**
 * Class that provides the methods to generate a valid Auth0 Authorize Url. It's based on the https://auth0.com/docs/api/authentication#social docs.
 * <p>
 * This class is not thread-safe:
 * It makes use of {@link HashMap} for storing the parameters. Make sure to not call the builder methods
 * from a different or un-synchronized thread.
 */
@SuppressWarnings("WeakerAccess")
public class AuthorizeUrlBuilder {

    private final HttpUrl.Builder builder;
    private final Map<String, String> parameters;

    /**
     * Creates an instance of the {@link AuthorizeUrlBuilder} using the given domain and base parameters.
     *
     * @param baseUrl     the base url constructed from a valid domain.
     * @param clientId    the application's client_id value to set
     * @param redirectUri the redirect_uri value to set. Must be already URL Encoded and must be white-listed in your Auth0's dashboard.
     * @return a new instance of the {@link AuthorizeUrlBuilder} to configure.
     */
    static AuthorizeUrlBuilder newInstance(HttpUrl baseUrl, String clientId, String redirectUri) {
        return new AuthorizeUrlBuilder(baseUrl, clientId, redirectUri);
    }

    private AuthorizeUrlBuilder(HttpUrl url, String clientId, String redirectUri) {
        assertNotNull(url, "base url");
        assertNotNull(clientId, "client id");
        assertNotNull(redirectUri, "redirect uri");

        parameters = new HashMap<>();
        builder = url.newBuilder()
                .addPathSegment("authorize")
                .addEncodedQueryParameter("redirect_uri", redirectUri)
                .addQueryParameter("client_id", clientId);
        withParameter("response_type", "code");
    }

    /**
     * Sets the connection value.
     *
     * @param connection connection to set
     * @return the builder instance
     */
    public AuthorizeUrlBuilder withConnection(String connection) {
        assertNotNull(connection, "connection");
        parameters.put("connection", connection);
        return this;
    }

    /**
     * Sets the audience value.
     *
     * @param audience audience to set
     * @return the builder instance
     */
    public AuthorizeUrlBuilder withAudience(String audience) {
        assertNotNull(audience, "audience");
        parameters.put("audience", audience);
        return this;
    }

    /**
     * Sets the state value.
     *
     * @param state state to set
     * @return the builder instance
     */
    public AuthorizeUrlBuilder withState(String state) {
        assertNotNull(state, "state");
        parameters.put("state", state);
        return this;
    }

    /**
     * Sets the scope value.
     *
     * @param scope scope to set
     * @return the builder instance
     */
    public AuthorizeUrlBuilder withScope(String scope) {
        assertNotNull(scope, "scope");
        parameters.put("scope", scope);
        return this;
    }

    /**
     * Sets the response type value.
     *
     * @param responseType response type to set
     * @return the builder instance
     */
    public AuthorizeUrlBuilder withResponseType(String responseType) {
        assertNotNull(responseType, "response type");
        parameters.put("response_type", responseType);
        return this;
    }

    public AuthorizeUrlBuilder withOrganization(String organization) {
        assertNotNull(organization, "organization");
        parameters.put("organization", organization);
        return this;
    }

    /**
     * Sets an additional parameter.
     *
     * @param name  name of the parameter
     * @param value value of the parameter to set
     * @return the builder instance
     */
    public AuthorizeUrlBuilder withParameter(String name, String value) {
        assertNotNull(name, "name");
        assertNotNull(value, "value");
        parameters.put(name, value);
        return this;
    }

    /**
     * Creates a string representation of the URL with the configured parameters.
     *
     * @return the string URL
     */
    public String build() {
        for (Map.Entry<String, String> p : parameters.entrySet()) {
            builder.addQueryParameter(p.getKey(), p.getValue());
        }
        return builder.build().toString();
    }
}
