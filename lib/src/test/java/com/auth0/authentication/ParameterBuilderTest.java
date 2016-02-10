/*
 * ParameterBuilderTest.java
 *
 * Copyright (c) 2016 Auth0 (http://auth0.com)
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

package com.auth0.authentication;


import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ParameterBuilderTest {

    public static final String CLIENT_ID = "CLIENT ID";
    public static final String GRANT_TYPE = "password";
    public static final String CONNECTION = "AD";
    public static final String DEVICE = "ANDROID TEST DEVICE";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private ParameterBuilder builder;

    @Before
    public void setUp() throws Exception {
        this.builder = ParameterBuilder.newBuilder();
    }

    @Test
    public void shouldInstantiateWithNoArguments() throws Exception {
        assertThat(new ParameterBuilder(), is(notNullValue()));
        assertThat(ParameterBuilder.newBuilder(), is(notNullValue()));
    }

    @Test
    public void shouldInstantiateWithArguments() throws Exception {
        assertThat(new ParameterBuilder(new HashMap<String, Object>()), is(notNullValue()));
        assertThat(ParameterBuilder.newBuilder(new HashMap<String, Object>()), is(notNullValue()));
    }

    @Test
    public void shouldFailToInstantiateWithNullParameters() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(equalToIgnoringCase("Must provide non-null parameters"));
        new ParameterBuilder(null);
    }

    @Test
    public void shouldFailToInstantiateWithNullParametersInFactoryMethod() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(equalToIgnoringCase("Must provide non-null parameters"));
        ParameterBuilder.newBuilder(null);
    }

    @Test
    public void shouldAddArbitraryEntry() throws Exception {
        assertThat(builder.set("key", "value").asDictionary(), hasEntry("key", "value"));
    }

    @Test
    public void shouldAddAllFromDictionary() throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("key", "value");
        assertThat(builder.addAll(parameters).asDictionary(), hasEntry("key", "value"));
    }

    @Test
    public void shouldDoNothingWhenAddingNullParameters() throws Exception {
        builder.set("key", "value");
        assertThat(builder.addAll(null).asDictionary(), hasEntry("key", "value"));
    }

    @Test
    public void shouldProvideADictionaryCopy() throws Exception {
        Map<String, Object> parameters = builder.set("key", "value").asDictionary();
        parameters.put("key2", "value2");
        assertThat(builder.asDictionary(), not(hasEntry("key2", "value2")));
    }

    private static Matcher<Map<? extends String, ?>> hasEntry(String key, Object value) {
        return Matchers.hasEntry(key, value);
    }
}
