package com.github.prgrms.reviews;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static com.github.prgrms.utils.DateTimeUtils.dateTimeOf;
import static com.github.prgrms.utils.DateTimeUtils.timestampOf;
import static java.util.Optional.ofNullable;

@Repository
public class JdbcReviewRepository implements ReviewRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Review> findById(long id) {
        List<Review> results = jdbcTemplate.query(
                "SELECT * FROM reviews WHERE seq=?",
                mapper,
                id
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public List<Review> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM reviews ORDER BY seq DESC",
                mapper
        );
    }

    @Override
    public Long save(Review review) {

        String insertSql = "INSERT INTO reviews(seq,user_seq,product_seq,content,create_at) values (null,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        String id_column = "seq";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql, new String[]{id_column});
            ps.setLong(1, review.getUserSeq());
            ps.setLong(2, review.getProductSeq());
            ps.setString(3, review.getContent());
            ps.setTimestamp(4, timestampOf(review.getCreateAt()));
            return ps;
        }, keyHolder);

        Long id = (Long) keyHolder.getKeys().get(id_column);

        return id;
    }

    static RowMapper<Review> mapper = (rs, rowNum) ->
            new Review.Builder()
                    .seq(rs.getLong("seq"))
                    .userSeq(rs.getLong("user_seq"))
                    .productSeq(rs.getLong("product_seq"))
                    .createAt(dateTimeOf(rs.getTimestamp("create_at")))
                    .content(rs.getString("content"))
                    .build();
}
