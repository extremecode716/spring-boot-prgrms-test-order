package com.github.prgrms.security;

import static com.google.common.base.Preconditions.checkNotNull;

public class JwtAuthentication {

    public final Long id;

    public final String name;

    JwtAuthentication(Long id, String name) {
        checkNotNull(id, "id must be provided");
        checkNotNull(name, "name must be provided");

        this.id = id;
        this.name = name;
    }

}