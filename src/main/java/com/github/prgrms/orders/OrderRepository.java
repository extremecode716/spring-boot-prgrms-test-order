package com.github.prgrms.orders;

import com.github.prgrms.configures.web.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Orders> findAll(Pageable pageable);

    Optional<Orders> findById(long id);

    void updateStateToAccepted(Long seq);

    void updateStateToRejected(Long seq, String rejectMsg);

    void updateStateToShipping(Long seq);

    void updateStateToComplete(Long seq);

    void addReview(Long orderSeq, Long reviewSeq);
}
