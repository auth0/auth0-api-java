package com.auth0.client.mgmt.filter;

/**
 * Class used to filter the results received when calling an endpoint that supports Pagination.
 * <p>
 * This class is not thread-safe.
 *
 * @see BaseFilter
 */
public class PageFilter extends BaseFilter {
    /**
     * Filter by page
     *
     * @param pageNumber    the page number to retrieve.
     * @param amountPerPage the amount of items per page to retrieve.
     * @return this filter instance
     */
    public PageFilter withPage(int pageNumber, int amountPerPage) {
        parameters.put("page", pageNumber);
        parameters.put("per_page", amountPerPage);
        return this;
    }

    /**
     * Include the query summary
     *
     * @param includeTotals whether to include or not the query summary.
     * @return this filter instance
     */
    public PageFilter withTotals(boolean includeTotals) {
        parameters.put("include_totals", includeTotals);
        return this;
    }

    /**
     * Include the {@code from} parameter to specify where to start the page selection. Only applicable for endpoints that
     * support checkpoint pagination.
     * <p>
     * <strong>Note: If this or the {@linkplain PageFilter#withTake(int)} is specified, any offset paging parameters set
     * via {@linkplain PageFilter#withPage(int, int)} or {@linkplain PageFilter#withTotals(boolean)} will be disregarded
     * by the API.</strong>
     * </p>
     * @param from the ID from which to start selection. This can be obtained from the {@code next} field returned from
     *             a checkpoint-paginated result.
     * @return this filter instance.
     */
    public PageFilter withFrom(String from) {
        parameters.put("from", from);
        return this;
    }

    /**
     * Include the {@code take} parameter to specify the amount of results to return per page. Only applicable for endpoints that
     * support checkpoint pagination.
     * <p>
     * <strong>Note: If this or the {@linkplain PageFilter#withFrom(String)} is specified, any offset paging parameters set
     * via {@linkplain PageFilter#withPage(int, int)} or {@linkplain PageFilter#withTotals(boolean)} will be disregarded
     * by the API.</strong>
     * </p>
     * @param take the amount of entries to retrieve per page.
     * @return this filter instance.
     */
    public PageFilter withTake(int take) {
        parameters.put("take", take);
        return this;
    }

}
