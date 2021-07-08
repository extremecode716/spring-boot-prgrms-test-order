package com.github.prgrms.users;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class LoginResult {

    private final String token;

    private final UserDto user;

    public LoginResult(String token, User user) {
        this.token = token;
        this.user = new UserDto(user);
    }

    public String getToken() {
        return token;
    }

    public UserDto getUser() {
        return user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("token", token)
                .append("user", user)
                .toString();
    }

}