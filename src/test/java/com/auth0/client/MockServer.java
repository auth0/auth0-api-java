package com.auth0.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MockServer {

    public static final String AUTH_USER_INFO = "src/test/resources/auth/user_info.json";
    public static final String AUTH_RESET_PASSWORD = "src/test/resources/auth/reset_password.json";
    public static final String AUTH_SIGN_UP = "src/test/resources/auth/sign_up.json";
    public static final String AUTH_SIGN_UP_USERNAME = "src/test/resources/auth/sign_up_username.json";
    public static final String AUTH_TOKENS = "src/test/resources/auth/tokens.json";
    public static final String AUTH_ERROR_WITH_ERROR_DESCRIPTION = "src/test/resources/auth/error_with_error_description.json";
    public static final String AUTH_ERROR_WITH_ERROR = "src/test/resources/auth/error_with_error.json";
    public static final String AUTH_ERROR_WITH_DESCRIPTION = "src/test/resources/auth/error_with_description.json";
    public static final String AUTH_ERROR_WITH_DESCRIPTION_AND_EXTRA_PROPERTIES = "src/test/resources/auth/error_with_description_and_extra_properties.json";
    public static final String AUTH_ERROR_PLAINTEXT = "src/test/resources/auth/error_plaintext.json";
    public static final String MGMT_ERROR_WITH_MESSAGE = "src/test/resources/mgmt/error_with_message.json";
    public static final String MGMT_CLIENT_GRANTS_LIST = "src/test/resources/mgmt/client_grants_list.json";
    public static final String MGMT_CLIENT_GRANTS_PAGED_LIST = "src/test/resources/mgmt/client_grants_paged_list.json";
    public static final String MGMT_CLIENT_GRANT = "src/test/resources/mgmt/client_grant.json";
    public static final String MGMT_CLIENTS_LIST = "src/test/resources/mgmt/clients_list.json";
    public static final String MGMT_CLIENTS_PAGED_LIST = "src/test/resources/mgmt/clients_paged_list.json";
    public static final String MGMT_CLIENT = "src/test/resources/mgmt/client.json";
    public static final String MGMT_CONNECTIONS_LIST = "src/test/resources/mgmt/connections_list.json";
    public static final String MGMT_CONNECTIONS_PAGED_LIST = "src/test/resources/mgmt/connections_paged_list.json";
    public static final String MGMT_CONNECTION = "src/test/resources/mgmt/connection.json";
    public static final String MGMT_DEVICE_CREDENTIALS_LIST = "src/test/resources/mgmt/device_credentials_list.json";
    public static final String MGMT_DEVICE_CREDENTIALS = "src/test/resources/mgmt/device_credentials.json";
    public static final String MGMT_GRANTS_LIST = "src/test/resources/mgmt/grants_list.json";
    public static final String MGMT_GRANTS_PAGED_LIST = "src/test/resources/mgmt/grants_paged_list.json";
    public static final String MGMT_LOG_EVENTS_LIST = "src/test/resources/mgmt/event_logs_list.json";
    public static final String MGMT_LOG_EVENTS_PAGED_LIST = "src/test/resources/mgmt/event_logs_paged_list.json";
    public static final String MGMT_LOG_EVENT = "src/test/resources/mgmt/event_log.json";
    public static final String MGMT_LOG_STREAM = "src/test/resources/mgmt/log_stream.json";
    public static final String MGMT_LOG_STREAMS_LIST = "src/test/resources/mgmt/log_streams_list.json";
    public static final String MGMT_RESOURCE_SERVERS_LIST = "src/test/resources/mgmt/resource_servers_list.json";
    public static final String MGMT_RESOURCE_SERVERS_PAGED_LIST = "src/test/resources/mgmt/resource_servers_paged_list.json";
    public static final String MGMT_RESOURCE_SERVER = "src/test/resources/mgmt/resource_server.json";
    public static final String MGMT_ROLE = "src/test/resources/mgmt/role.json";
    public static final String MGMT_ROLES_LIST = "src/test/resources/mgmt/roles_list.json";
    public static final String MGMT_ROLES_PAGED_LIST = "src/test/resources/mgmt/roles_paged_list.json";
    public static final String MGMT_ROLE_PERMISSIONS_LIST = "src/test/resources/mgmt/role_permissions_list.json";
    public static final String MGMT_ROLE_PERMISSIONS_PAGED_LIST = "src/test/resources/mgmt/role_permissions_paged_list.json";
    public static final String MGMT_ROLE_USERS_LIST = "src/test/resources/mgmt/role_users_list.json";
    public static final String MGMT_ROLE_USERS_PAGED_LIST = "src/test/resources/mgmt/role_users_paged_list.json";
    public static final String MGMT_ROLE_USERS_CHECKPOINT_PAGED_LIST = "src/test/resources/mgmt/role_users_checkpoint_paged_list.json";
    public static final String MGMT_RULES_LIST = "src/test/resources/mgmt/rules_list.json";
    public static final String MGMT_RULES_CONFIGS_LIST = "src/test/resources/mgmt/rules_configs_list.json";
    public static final String MGMT_RULES_PAGED_LIST = "src/test/resources/mgmt/rules_paged_list.json";
    public static final String MGMT_RULE = "src/test/resources/mgmt/rule.json";
    public static final String MGMT_USER_BLOCKS = "src/test/resources/mgmt/user_blocks.json";
    public static final String MGMT_BLACKLISTED_TOKENS_LIST = "src/test/resources/mgmt/blacklisted_tokens_list.json";
    public static final String MGMT_EMAIL_PROVIDER = "src/test/resources/mgmt/email_provider.json";
    public static final String MGMT_EMAIL_TEMPLATE = "src/test/resources/mgmt/email_template.json";
    public static final String MGMT_USERS_LIST = "src/test/resources/mgmt/users_list.json";
    public static final String MGMT_USERS_PAGED_LIST = "src/test/resources/mgmt/users_paged_list.json";
    public static final String MGMT_USER_PERMISSIONS_PAGED_LIST = "src/test/resources/mgmt/user_permissions_paged_list.json";
    public static final String MGMT_USER_ROLES_PAGED_LIST = "src/test/resources/mgmt/user_roles_paged_list.json";
    public static final String MGMT_USER = "src/test/resources/mgmt/user.json";
    public static final String MGMT_RECOVERY_CODE = "src/test/resources/mgmt/recovery_code.json";
    public static final String MGMT_IDENTITIES_LIST = "src/test/resources/mgmt/identities_list.json";
    public static final String MGMT_GUARDIAN_ENROLLMENT = "src/test/resources/mgmt/guardian_enrollment.json";
    public static final String MGMT_GUARDIAN_ENROLLMENTS_LIST = "src/test/resources/mgmt/guardian_enrollments_list.json";
    public static final String MGMT_GUARDIAN_ENROLLMENT_TICKET = "src/test/resources/mgmt/guardian_enrollment_ticket.json";
    public static final String MGMT_GUARDIAN_FACTOR = "src/test/resources/mgmt/guardian_factor.json";
    public static final String MGMT_GUARDIAN_FACTORS_LIST = "src/test/resources/mgmt/guardian_factors_list.json";
    public static final String MGMT_GUARDIAN_TEMPLATES = "src/test/resources/mgmt/guardian_templates.json";
    public static final String MGMT_GUARDIAN_SNS_FACTOR_PROVIDER_EMPTY = "src/test/resources/mgmt/guardian_sns_factor_provider_empty.json";
    public static final String MGMT_GUARDIAN_SNS_FACTOR_PROVIDER = "src/test/resources/mgmt/guardian_sns_factor_provider.json";
    public static final String MGMT_GUARDIAN_TWILIO_FACTOR_PROVIDER_EMPTY = "src/test/resources/mgmt/guardian_twilio_factor_provider_empty.json";
    public static final String MGMT_GUARDIAN_TWILIO_FACTOR_PROVIDER_WITH_FROM = "src/test/resources/mgmt/guardian_twilio_factor_provider_with_from.json";
    public static final String MGMT_GUARDIAN_TWILIO_FACTOR_PROVIDER_WITH_MSSID = "src/test/resources/mgmt/guardian_twilio_factor_provider_with_messaging_service_sid.json";
    public static final String MGMT_TENANT = "src/test/resources/mgmt/tenant.json";
    public static final String MGMT_ACTIVE_USERS_COUNT = "src/test/resources/mgmt/active_users_count.json";
    public static final String MGMT_DAILY_STATS_LIST = "src/test/resources/mgmt/daily_stats_list.json";
    public static final String MGMT_PASSWORD_CHANGE_TICKET = "src/test/resources/mgmt/password_change_ticket.json";
    public static final String MGMT_EMAIL_VERIFICATION_TICKET = "src/test/resources/mgmt/email_verification_ticket.json";
    public static final String MGMT_EMPTY_LIST = "src/test/resources/mgmt/empty_list.json";
    public static final String MGMT_JOB = "src/test/resources/mgmt/job.json";
    public static final String MGMT_JOB_POST_VERIFICATION_EMAIL = "src/test/resources/mgmt/job_post_verification_email.json";
    public static final String MGMT_JOB_POST_USERS_EXPORTS = "src/test/resources/mgmt/job_post_users_exports.json";
    public static final String MGMT_JOB_POST_USERS_IMPORTS = "src/test/resources/mgmt/job_post_users_imports.json";
    public static final String MGMT_JOB_POST_USERS_IMPORTS_INPUT = "src/test/resources/mgmt/job_post_users_imports_input.json";
    public static final String MULTIPART_SAMPLE = "src/test/resources/mgmt/multipart_sample.json";
    public static final String PASSWORDLESS_EMAIL_RESPONSE = "src/test/resources/auth/passwordless_email.json";
    public static final String PASSWORDLESS_SMS_RESPONSE = "src/test/resources/auth/passwordless_sms.json";
    public static final String ORGANIZATION = "src/test/resources/mgmt/organization.json";
    public static final String ORGANIZATIONS_LIST = "src/test/resources/mgmt/organizations_list.json";
    public static final String ORGANIZATIONS_PAGED_LIST = "src/test/resources/mgmt/organizations_paged_list.json";
    public static final String ORGANIZATIONS_CHECKPOINT_PAGED_LIST = "src/test/resources/mgmt/organizations_checkpoint_paged_list.json";
    public static final String ORGANIZATION_MEMBERS_LIST = "src/test/resources/mgmt/organization_members_list.json";
    public static final String ORGANIZATION_MEMBERS_PAGED_LIST = "src/test/resources/mgmt/organization_members_paged_list.json";
    public static final String ORGANIZATION_MEMBERS_CHECKPOINT_PAGED_LIST = "src/test/resources/mgmt/organization_members_checkpoint_paged_list.json";
    public static final String ORGANIZATION_CONNECTIONS_LIST = "src/test/resources/mgmt/organization_connections_list.json";
    public static final String ORGANIZATION_CONNECTIONS_PAGED_LIST = "src/test/resources/mgmt/organization_connections_paged_list.json";
    public static final String ORGANIZATION_CONNECTION = "src/test/resources/mgmt/organization_connection.json";
    public static final String ORGANIZATION_MEMBER_ROLES_LIST = "src/test/resources/mgmt/organization_member_roles_list.json";
    public static final String ORGANIZATION_MEMBER_ROLES_PAGED_LIST = "src/test/resources/mgmt/organization_member_roles_paged_list.json";
    public static final String INVITATION = "src/test/resources/mgmt/invitation.json";
    public static final String INVITATIONS_LIST = "src/test/resources/mgmt/invitations_list.json";
    public static final String INVITATIONS_PAGED_LIST = "src/test/resources/mgmt/invitations_paged_list.json";

    private final MockWebServer server;

    public MockServer() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    public void stop() throws IOException {
        server.shutdown();
    }

    public String getBaseUrl() {
        return server.url("/").toString();
    }

    public RecordedRequest takeRequest() throws InterruptedException {
        return server.takeRequest();
    }

    private String readTextFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public void jsonResponse(String path, int statusCode) throws IOException {
        MockResponse response = new MockResponse()
                .setResponseCode(statusCode)
                .addHeader("Content-Type", "application/json")
                .setBody(readTextFile(path));
        server.enqueue(response);
    }

    public void rateLimitReachedResponse(long limit, long remaining, long reset) {
        MockResponse response = new MockResponse().setResponseCode(429);
        if (limit != -1) {
            response.addHeader("X-RateLimit-Limit", String.valueOf(limit));
        }
        if (remaining != -1) {
            response.addHeader("X-RateLimit-Remaining", String.valueOf(remaining));
        }
        if (reset != -1) {
            response.addHeader("X-RateLimit-Reset", String.valueOf(reset));
        }
        server.enqueue(response);
    }

    public void textResponse(String path, int statusCode) throws IOException {
        MockResponse response = new MockResponse()
                .setResponseCode(statusCode)
                .addHeader("Content-Type", "text/plain")
                .setBody(readTextFile(path));
        server.enqueue(response);
    }

    public void emptyResponse(int statusCode) {
        MockResponse response = new MockResponse()
                .setResponseCode(statusCode);
        server.enqueue(response);
    }

    public static Map<String, Object> bodyFromRequest(RecordedRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
        try (Buffer body = request.getBody()) {
            return mapper.readValue(body.inputStream(), mapType);
        }
    }

    public static String readFromRequest(RecordedRequest request) {
        try (Buffer body = request.getBody()) {
            return body.readUtf8();
        }
    }
}
