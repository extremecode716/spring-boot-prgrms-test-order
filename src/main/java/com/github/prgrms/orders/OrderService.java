package com.github.prgrms.orders;

import com.github.prgrms.configures.web.Pageable;
import com.github.prgrms.errors.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<Orders> findAll(Pageable pageable) {
        checkNotNull(pageable, "pageable must be provided");

        return orderRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Orders> findById(Long orderSeq) {
        checkNotNull(orderSeq, "orderSeq must be provided");

        return orderRepository.findById(orderSeq);
    }

    @Transactional
    public Boolean accept(Long orderSeq) {
        checkNotNull(orderSeq, "orderSeq must be provided");

        Orders orders = orderRepository.findById(orderSeq).orElseThrow(() -> new NotFoundException("Could not found order for " + orderSeq));

        if (!orders.getState().equals(StateType.REQUESTED.toString())) {
            return false;
        }

        orderRepository.updateStateToAccepted(orders.getSeq());

        return true;
    }

    @Transactional
    public Boolean reject(Long orderSeq, String rejectMsg) {
        checkNotNull(orderSeq, "orderSeq must be provided");
        checkArgument(isNotEmpty(rejectMsg), "rejectMsg must be provided");

        Orders orders = orderRepository.findById(orderSeq).orElseThrow(() -> new NotFoundException("Could not found order for " + orderSeq));

        if (!orders.getState().equals(StateType.REQUESTED.toString())) {
            return false;
        }

        orderRepository.updateStateToRejected(orders.getSeq(), rejectMsg);

        return true;
    }

    @Transactional
    public Boolean shipping(Long orderSeq) {
        checkNotNull(orderSeq, "orderSeq must be provided");

        Orders orders = orderRepository.findById(orderSeq).orElseThrow(() -> new NotFoundException("Could not found order for " + orderSeq));

        if (!orders.getState().equals(StateType.ACCEPTED.toString())) {
            return false;
        }

        orderRepository.updateStateToShipping(orders.getSeq());

        return true;
    }

    @Transactional
    public Boolean complete(Long orderSeq) {
        checkNotNull(orderSeq, "orderSeq must be provided");

        Orders orders = orderRepository.findById(orderSeq).orElseThrow(() -> new NotFoundException("Could not found order for " + orderSeq));

        if (!orders.getState().equals(StateType.SHIPPING.toString())) {
            return false;
        }

        orderRepository.updateStateToComplete(orders.getSeq());

        return true;
    }

    @Transactional
    public void addReviewSeq(Long orderSeq, Long reviewSeq) {
        orderRepository.addReview(orderSeq, reviewSeq);
    }

}
