package com.github.prgrms.users;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.github.prgrms.utils.DateTimeUtils.dateTimeOf;
import static java.util.Optional.ofNullable;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                "UPDATE users SET passwd=?,login_count=?,last_login_at=? WHERE seq=?",
                user.getPassword(),
                user.getLoginCount(),
                user.getLastLoginAt().orElse(null),
                user.getSeq()
        );
    }

    @Override
    public Optional<User> findById(long id) {
        List<User> results = jdbcTemplate.query(
                "SELECT * FROM users WHERE seq=?",
                mapper,
                id
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        List<User> results = jdbcTemplate.query(
                "SELECT * FROM users WHERE email=?",
                mapper,
                email.getAddress()
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    static RowMapper<User> mapper = (rs, rowNum) ->
            new User.Builder()
                    .seq(rs.getLong("seq"))
                    .name(rs.getString("name"))
                    .email(new Email(rs.getString("email")))
                    .password(rs.getString("passwd"))
                    .loginCount(rs.getInt("login_count"))
                    .lastLoginAt(dateTimeOf(rs.getTimestamp("last_login_at")))
                    .createAt(dateTimeOf(rs.getTimestamp("create_at")))
                    .build();

}