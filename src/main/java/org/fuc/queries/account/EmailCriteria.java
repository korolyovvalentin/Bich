package org.fuc.queries.account;

import org.fuc.core.Criteria;

public class EmailCriteria extends Criteria {
    private String email;

    public EmailCriteria(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
