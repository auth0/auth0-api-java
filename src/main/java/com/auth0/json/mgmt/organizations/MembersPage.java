package com.auth0.json.mgmt.organizations;

import com.auth0.json.mgmt.Page;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = MembersPageDeserializer.class)
public class MembersPage extends Page<Member> {
    public MembersPage(List<Member> items) {
        super(items);
    }

    public MembersPage(Integer start, Integer length, Integer total, Integer limit, List<Member> items) {
        super(start, length, total, limit, items);
    }
}
