package org.fuc.viewmodels;

import org.fuc.entities.Account;

public class RequestVm {
    private Long id;

    private Account owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }
}
