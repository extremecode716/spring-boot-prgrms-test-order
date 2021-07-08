package com.github.prgrms.products;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.github.prgrms.utils.DateTimeUtils.dateTimeOf;
import static java.util.Optional.ofNullable;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Product> findById(long id) {
        List<Product> results = jdbcTemplate.query(
                "SELECT * FROM products WHERE seq=?",
                mapper,
                id
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM products ORDER BY seq DESC",
                mapper
        );
    }

    @Override
    public void addReviewCount(Long id, int cnt) {
        jdbcTemplate.update("UPDATE products SET review_count = ? WHERE seq = ?", cnt, id);
    }

    static RowMapper<Product> mapper = (rs, rowNum) ->
            new Product.Builder()
                    .seq(rs.getLong("seq"))
                    .name(rs.getString("name"))
                    .details(rs.getString("details"))
                    .reviewCount(rs.getInt("review_count"))
                    .createAt(dateTimeOf(rs.getTimestamp("create_at")))
                    .build();

}