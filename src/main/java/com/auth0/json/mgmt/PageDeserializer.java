package com.auth0.json.mgmt;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.List;

/**
 * Parses a given paged response into their page pojo representation.
 * <p>
 * This class is thread-safe.
 *
 * @param <T> the class that represents a page of U.
 * @param <U> the class that represents the items in the page.
 */
public abstract class PageDeserializer<T, U> extends StdDeserializer<T> {

    private final String itemsPropertyName;
    private final Class<U> uClazz;
    private static final ObjectMapper mapper = new ObjectMapper();

    protected PageDeserializer(Class<U> clazz, String arrayName) {
        super(Object.class);
        this.uClazz = clazz;
        this.itemsPropertyName = arrayName;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (node.isArray()) {
            return createPage(getArrayElements((ArrayNode) node));
        }

        Integer start = getIntegerValue(node.get("start"));
        Integer length = getIntegerValue(node.get("length"));
        Integer total = getIntegerValue(node.get("total"));
        Integer limit = getIntegerValue(node.get("limit"));

        // "next" field is sent when using checkpoint pagination on APIs that support it. It will be null when
        // end of pages reached, or if offset paging is used
        String next = getStringValue(node.get("next"));
        ArrayNode array = (ArrayNode) node.get(itemsPropertyName);

        return createPage(start, length, total, limit, next, getArrayElements(array));
    }

    protected abstract T createPage(List<U> items);

    protected abstract T createPage(Integer start, Integer length, Integer total, Integer limit, List<U> items);

    /**
     * Creates a new page result.<br/>
     * <strong>By default, this method delegates to {@linkplain PageDeserializer#createPage(Integer, Integer, Integer, Integer, List)},
     * to preserve backwards-compatibility for implementors. If an API supports checkpoint pagination, the specific
     * deserializer implementation must override this method and construct the Page to populate the "next" field</strong>.
     */
    protected T createPage(Integer start, Integer length, Integer total, Integer limit, String next, List<U> items) {
        return createPage(start, length, total, limit, items);
    }

    private Integer getIntegerValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        } else {
            return node.intValue();
        }
    }

    private String getStringValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        } else {
            return node.textValue();
        }
    }

    private List<U> getArrayElements(ArrayNode array) throws IOException {
        CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, uClazz);
        return mapper.readerFor(type).readValue(array);
    }
}
