package com.auth0.client.mgmt;

import com.auth0.json.mgmt.userblocks.UserBlocks;
import com.auth0.net.Request;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;

import static com.auth0.client.MockServer.MGMT_USER_BLOCKS;
import static com.auth0.client.RecordedRequestMatcher.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class UserBlocksEntityTest extends BaseMgmtEntityTest {

    @Test
    public void shouldThrowOnGetUserBlocksByIdentifierWithNullId() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'identifier' cannot be null!");
        api.userBlocks().getUserBlocksByIdentifier(null);
    }

    @Test
    public void shouldGetUserBlocksByIdentifier() throws Exception {
        Request<UserBlocks> request = api.userBlocks().getUserBlocksByIdentifier("username");
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_USER_BLOCKS, 200);
        UserBlocks response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/user-blocks"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("identifier", "username"));

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldThrowOnGetUserBlocksWithNullId() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'user id' cannot be null!");
        api.userBlocks().getUserBlocks(null);
    }

    @Test
    public void shouldGetUserBlocks() throws Exception {
        Request<UserBlocks> request = api.userBlocks().getUserBlocks("1");
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_USER_BLOCKS, 200);
        UserBlocks response = request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("GET", "/api/v2/user-blocks/1"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldThrowOnDeleteUserBlocksByIdentifierWithNullId() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'identifier' cannot be null!");
        api.userBlocks().deleteUserBlocksByIdentifier(null);
    }

    @Test
    public void shouldDeleteUserBlocksByIdentifier() throws Exception {
        Request request = api.userBlocks().deleteUserBlocksByIdentifier("username");
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_USER_BLOCKS, 200);
        request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("DELETE", "/api/v2/user-blocks"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
        assertThat(recordedRequest, hasQueryParameter("identifier", "username"));
    }

    @Test
    public void shouldThrowOnDeleteUserBlocksWithNullId() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'user id' cannot be null!");
        api.userBlocks().deleteUserBlocks(null);
    }

    @Test
    public void shouldDeleteUserBlocks() throws Exception {
        Request request = api.userBlocks().deleteUserBlocks("1");
        assertThat(request, is(notNullValue()));

        server.jsonResponse(MGMT_USER_BLOCKS, 200);
        request.execute();
        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest, hasMethodAndPath("DELETE", "/api/v2/user-blocks/1"));
        assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
        assertThat(recordedRequest, hasHeader("Authorization", "Bearer apiToken"));
    }
}
