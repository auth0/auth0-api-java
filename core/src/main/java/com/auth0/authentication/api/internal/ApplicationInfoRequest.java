/*
 * ApplicationInfoRequest.java
 *
 * Copyright (c) 2015 Auth0 (http://auth0.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.auth0.authentication.api.internal;

import com.auth0.Application;
import com.auth0.Auth0Exception;
import com.auth0.authentication.api.APIException;
import com.auth0.authentication.api.ParameterizableRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.Map;

class ApplicationInfoRequest extends BaseRequest<Application> implements Callback {

    public ApplicationInfoRequest(OkHttpClient client, HttpUrl url, ObjectMapper mapper) {
        super(url, client, mapper.reader(Application.class), mapper.reader(new TypeReference<Map<String, Object>>() {}), null);
    }

    @Override
    protected Request doBuildRequest(Request.Builder builder) {
        final Request request = builder.build();
        return request;
    }

    @Override
    public void onResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            APIException exception = parseUnsuccessfulResponse(response);
            postOnFailure(exception);
            return;
        }
        try {
            String json = response.body().string();
            Application app = parseJSONP(json);
            postOnSuccess(app);
        } catch (Exception e) {
            postOnFailure(new Auth0Exception("Failed to parse response to request to " + url, e));
        }
    }

    @Override
    public ParameterizableRequest<Application> addParameters(Map<String, Object> parameters) {
        return this;
    }

    @Override
    public ParameterizableRequest<Application> addHeader(String name, String value) {
        return this;
    }

    @Override
    public Application execute() throws Auth0Exception {
        Request request = doBuildRequest(newBuilder());

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new Auth0Exception("Failed to execute request to " + url, e);
        }

        if (!response.isSuccessful()) {
            throw parseUnsuccessfulResponse(response);
        }

        try {
            String json = response.body().string();
            return parseJSONP(json);
        } catch (Exception e) {
            throw new Auth0Exception("Failed to parse response to request to " + url, e);
        }
    }

    private Application parseJSONP(String json) throws Exception {
        if (json.length() < 16) {
            throw new JSONException("Invalid App Info JSONP");
        }
        json = json.substring(16); // replaces tokenizer.skipPast("Auth0.setClient(") because official (not android's) org.json does not have the method
        JSONTokener tokenizer = new JSONTokener(json);
        if (!tokenizer.more()) {
            throw tokenizer.syntaxError("Invalid App Info JSONP");
        }
        Object nextValue = tokenizer.nextValue();
        if (!(nextValue instanceof JSONObject)) {
            tokenizer.back();
            throw tokenizer.syntaxError("Invalid JSON value of App Info");
        }
        JSONObject jsonObject = (JSONObject) nextValue;
        return getReader().readValue(jsonObject.toString());
    }
}
