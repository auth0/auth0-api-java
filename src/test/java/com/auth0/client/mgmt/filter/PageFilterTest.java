package com.auth0.client.mgmt.filter;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class PageFilterTest {

    private PageFilter filter;

    @Before
    public void setUp() {
        filter = new PageFilter();
    }

    @Test
    public void shouldFilterByPage() {
        PageFilter instance = filter.withPage(5, 10);

        assertThat(filter, is(instance));
        assertThat(filter.getAsMap(), is(notNullValue()));
        assertThat(filter.getAsMap(), Matchers.hasEntry("per_page", 10));
        assertThat(filter.getAsMap(), Matchers.hasEntry("page", 5));
    }

    @Test
    public void shouldIncludeTotals() {
        PageFilter instance = filter.withTotals(true);

        assertThat(filter, is(instance));
        assertThat(filter.getAsMap(), is(notNullValue()));
        assertThat(filter.getAsMap(), Matchers.hasEntry("include_totals", true));
    }

    @Test
    public void shouldIncludeCheckpointParams() {
        PageFilter instance = filter.withFrom("abc123").withTake(2);

        assertThat(filter.getAsMap(), is(notNullValue()));
        assertThat(filter.getAsMap(), Matchers.hasEntry("from", "abc123"));
        assertThat(filter.getAsMap(), Matchers.hasEntry("take", 2));
    }
}
