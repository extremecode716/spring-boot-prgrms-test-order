package com.github.prgrms.orders;

import com.github.prgrms.configures.web.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.github.prgrms.utils.DateTimeUtils.dateTimeOf;
import static com.github.prgrms.utils.DateTimeUtils.timestampOf;
import static java.util.Optional.ofNullable;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Optional<Orders> findById(long id) {
        List<Orders> results = jdbcTemplate.query(
                "SELECT * FROM orders WHERE seq=?",
                mapper,
                id
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public List<Orders> findAll(Pageable pageable) {

        List<Orders> orders = jdbcTemplate.query(
                "SELECT * FROM orders ORDER BY seq DESC LIMIT " + pageable.getSize() + " OFFSET " + pageable.getOffset(),
                mapper
        );

        return orders;
    }

    @Override
    public void updateStateToAccepted(Long seq) {
        jdbcTemplate.update("UPDATE orders SET state = ? WHERE seq = ?", StateType.ACCEPTED.toString(), seq);
    }

    @Override
    public void updateStateToRejected(Long seq, String rejectMsg) {
        jdbcTemplate.update("UPDATE orders SET state = ?,reject_msg = ?, rejected_at = ? WHERE seq = ?", StateType.REJECTED.toString(), rejectMsg, timestampOf(LocalDateTime.now()), seq);
    }

    @Override
    public void updateStateToShipping(Long seq) {
        jdbcTemplate.update("UPDATE orders SET state = ? WHERE seq = ?", StateType.SHIPPING.toString(), seq);
    }

    @Override
    public void updateStateToComplete(Long seq) {
        jdbcTemplate.update("UPDATE orders SET state = ?, completed_at = ? WHERE seq = ?", StateType.COMPLETED.toString(), timestampOf(LocalDateTime.now()), seq);
    }


    @Override
    public void addReview(Long orderSeq, Long reviewSeq) {
        jdbcTemplate.update("UPDATE orders SET review_seq = ? WHERE seq = ?", reviewSeq, orderSeq);
    }

    static RowMapper<Orders> mapper = (rs, rowNum) ->
            new Orders.Builder()
                    .seq(rs.getLong("seq"))
                    .userSeq(rs.getLong("user_seq"))
                    .productSeq(rs.getLong("product_seq"))
                    .reviewSeq(rs.getLong("review_seq"))
                    .state(rs.getString("state"))
                    .requestMsg(rs.getString("request_msg"))
                    .rejectMsg(rs.getString("reject_msg"))
                    .completedAt(dateTimeOf(rs.getTimestamp("completed_at")))
                    .rejectedAt(dateTimeOf(rs.getTimestamp("rejected_at")))
                    .createAt(dateTimeOf(rs.getTimestamp("create_at")))
                    .build();
}
