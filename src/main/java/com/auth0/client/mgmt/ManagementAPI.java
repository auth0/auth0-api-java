package com.auth0.client.mgmt;

import com.auth0.client.HttpOptions;
import com.auth0.client.ProxyOptions;
import com.auth0.net.Telemetry;
import com.auth0.net.TelemetryInterceptor;
import com.auth0.utils.Asserts;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

import java.io.IOException;

/**
 * Class that provides an implementation of the Management API methods defined in https://auth0.com/docs/api/management/v2.
 * To begin create an instance of {@link #ManagementAPI(String, String)} using the tenant domain and API token.
 * <p>
 * This class is not entirely thread-safe:
 * A new immutable {@link OkHttpClient} instance is being created with each instantiation, not sharing the thread pool
 * with any prior existing client instance.
 */
@SuppressWarnings("WeakerAccess")
public class ManagementAPI {

    private final HttpUrl baseUrl;
    private String apiToken;
    private final OkHttpClient client;
    private final TelemetryInterceptor telemetry;
    private final HttpLoggingInterceptor logging;

    /**
     * Create an instance with the given tenant's domain and API token.
     * In addition, accepts an {@link HttpOptions} that will be used to configure the networking client.
     * See the Management API section in the readme or visit https://auth0.com/docs/api/management/v2/tokens
     * to learn how to obtain a token.
     *
     * @param domain   the tenant's domain.
     * @param apiToken the token to authenticate the calls with.
     * @param options  configuration options for this client instance.
     * @see #ManagementAPI(String, String)
     */
    public ManagementAPI(String domain, String apiToken, HttpOptions options) {
        Asserts.assertNotNull(domain, "domain");
        Asserts.assertNotNull(apiToken, "api token");

        this.baseUrl = createBaseUrl(domain);
        if (baseUrl == null) {
            throw new IllegalArgumentException("The domain had an invalid format and couldn't be parsed as an URL.");
        }
        this.apiToken = apiToken;

        telemetry = new TelemetryInterceptor();
        logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.NONE);
        client = buildNetworkingClient(options);
    }

    /**
     * Create an instance with the given tenant's domain and API token.
     * See the Management API section in the readme or visit https://auth0.com/docs/api/management/v2/tokens
     * to learn how to obtain a token.
     *
     * @param domain   the tenant's domain.
     * @param apiToken the token to authenticate the calls with.
     */
    public ManagementAPI(String domain, String apiToken) {
        this(domain, apiToken, new HttpOptions());
    }

    /**
     * Given a set of options, it creates a new instance of the {@link OkHttpClient}
     * configuring them according to their availability.
     *
     * @param options the options to set to the client.
     * @return a new networking client instance configured as requested.
     */
    private OkHttpClient buildNetworkingClient(HttpOptions options) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        final ProxyOptions proxyOptions = options.getProxyOptions();
        if (proxyOptions != null) {
            //Set proxy
            clientBuilder.proxy(proxyOptions.getProxy());
            //Set authentication, if present
            final String proxyAuth = proxyOptions.getBasicAuthentication();
            if (proxyAuth != null) {
                clientBuilder.proxyAuthenticator(new Authenticator() {

                    private static final String PROXY_AUTHORIZATION_HEADER = "Proxy-Authorization";

                    @Override
                    public okhttp3.Request authenticate(Route route, Response response) throws IOException {
                        if (response.request().header(PROXY_AUTHORIZATION_HEADER) != null) {
                            return null;
                        }
                        return response.request().newBuilder()
                                .header(PROXY_AUTHORIZATION_HEADER, proxyAuth)
                                .build();
                    }
                });
            }
        }
        return clientBuilder
                .addInterceptor(logging)
                .addInterceptor(telemetry)
                .build();
    }

    /**
     * Update the API token to use on new calls. This is useful when the token is about to expire or already has.
     * Please note you'll need to obtain the corresponding entity again for this to apply. e.g. call {@link #clients()} again.
     * See the Management API section in the readme or visit https://auth0.com/docs/api/management/v2/tokens to learn how to obtain a token.
     *
     * @param apiToken the token to authenticate the calls with.
     */
    public void setApiToken(String apiToken) {
        Asserts.assertNotNull(apiToken, "api token");
        this.apiToken = apiToken;
    }

    /**
     * Avoid sending Telemetry data in every request to the Auth0 servers.
     */
    public void doNotSendTelemetry() {
        telemetry.setEnabled(false);
    }

    /**
     * Setter for the Telemetry to send in every request to Auth0.
     *
     * @param telemetry to send in every request to Auth0
     */
    public void setTelemetry(Telemetry telemetry) {
        this.telemetry.setTelemetry(telemetry);
    }

    /**
     * Whether to enable or not the current HTTP Logger for every Request, Response and other sensitive information.
     *
     * @param enabled whether to enable the HTTP logger or not.
     */
    public void setLoggingEnabled(boolean enabled) {
        logging.setLevel(enabled ? Level.BODY : Level.NONE);
    }

    //Visible for testing
    OkHttpClient getClient() {
        return client;
    }

    //Visible for testing
    HttpUrl getBaseUrl() {
        return baseUrl;
    }

    private HttpUrl createBaseUrl(String domain) {
        String url = domain;
        if (!domain.startsWith("https://") && !domain.startsWith("http://")) {
            url = "https://" + domain;
        }
        return HttpUrl.parse(url);
    }

    /**
     * Getter for the Client Grants entity.
     *
     * @return the Client Grants entity.
     */
    public ClientGrantsEntity clientGrants() {
        return new ClientGrantsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Applications entity.
     *
     * @return the Applications entity.
     */
    public ClientsEntity clients() {
        return new ClientsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Connections entity.
     *
     * @return the Connections entity.
     */
    public ConnectionsEntity connections() {
        return new ConnectionsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Device Credentials entity.
     *
     * @return the Device Credentials entity.
     */
    public DeviceCredentialsEntity deviceCredentials() {
        return new DeviceCredentialsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Grants entity.
     *
     * @return the Grants entity.
     */
    public GrantsEntity grants() {
        return new GrantsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Log Events entity.
     *
     * @return the Log Events entity.
     */
    public LogEventsEntity logEvents() {
        return new LogEventsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Log Streams entity.
     *
     * @return the Log Streams entity.
     */
    public LogStreamsEntity logStreams() {
        return new LogStreamsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Rules entity.
     *
     * @return the Rules entity.
     */
    public RulesEntity rules() {
        return new RulesEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Rules Configs entity.
     *
     * @return the Rules Configs entity.
     */
    public RulesConfigsEntity rulesConfigs() {
        return new RulesConfigsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the User Blocks entity.
     *
     * @return the User Blocks entity.
     */
    public UserBlocksEntity userBlocks() {
        return new UserBlocksEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Users entity.
     *
     * @return the Users entity.
     */
    public UsersEntity users() {
        return new UsersEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Blacklists entity.
     *
     * @return the Blacklists entity.
     */
    public BlacklistsEntity blacklists() {
        return new BlacklistsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Email Templates entity.
     *
     * @return the Email Templates entity.
     */
    public EmailTemplatesEntity emailTemplates() {
        return new EmailTemplatesEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Email Provider entity.
     *
     * @return the Email Provider entity.
     */
    public EmailProviderEntity emailProvider() {
        return new EmailProviderEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Guardian entity.
     *
     * @return the Guardian entity.
     */
    public GuardianEntity guardian() {
        return new GuardianEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Stats entity.
     *
     * @return the Stats entity.
     */
    public StatsEntity stats() {
        return new StatsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Tenants entity.
     *
     * @return the Tenants entity.
     */
    public TenantsEntity tenants() {
        return new TenantsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Tickets entity.
     *
     * @return the Tickets entity.
     */
    public TicketsEntity tickets() {
        return new TicketsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Resource Servers entity.
     *
     * @return the Resource Servers entity.
     */
    public ResourceServerEntity resourceServers() {
        return new ResourceServerEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Jobs entity.
     *
     * @return the Jobs entity.
     */
    public JobsEntity jobs() {
        return new JobsEntity(client, baseUrl, apiToken);
    }

    /**
     * Getter for the Roles entity.
     *
     * @return the Roles entity.
     */
    public RolesEntity roles() {
        return new RolesEntity(client, baseUrl, apiToken);
    }

    public OrganizationsEntity organizations() {
        return new OrganizationsEntity(client, baseUrl, apiToken);
    }
}
