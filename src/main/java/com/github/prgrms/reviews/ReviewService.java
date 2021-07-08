package com.github.prgrms.reviews;

import com.github.prgrms.errors.NotFoundException;
import com.github.prgrms.orders.Orders;
import com.github.prgrms.orders.OrderService;
import com.github.prgrms.orders.StateType;
import com.github.prgrms.products.ProductService;
import com.github.prgrms.users.User;
import com.github.prgrms.users.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;

    public ReviewService(ReviewRepository reviewRepository, OrderService orderService, ProductService productService, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
    }

    @Transactional
    public Long newReview(Long userSeq, Long orderSeq, String content) {
        checkNotNull(userSeq, "userSeq must be provided");
        checkNotNull(orderSeq, "orderSeq must be provided");

        User user = userService.findById(userSeq).orElseThrow(() -> new NotFoundException("Could not found user for " + userSeq));
        Orders orders = orderService.findById(orderSeq).orElseThrow(() -> new NotFoundException("Could not found order for " + orderSeq));

        checkArgument(orders.getReviewSeq() == 0, "Could not write review for order %d because have already written", orderSeq);
        checkArgument(orders.getState().equals(StateType.COMPLETED.toString()), "Could not write review for order %d because state(%s) is not allowed", orderSeq, orders.getState().toString());

        Review saveReview = new Review(user.getSeq(), orders.getProductSeq(), content);

        Long seq = reviewRepository.save(saveReview);
        productService.addReviewCount(orders.getProductSeq());
        orderService.addReviewSeq(orders.getSeq(), seq);

        return seq;
    }

    @Transactional(readOnly = true)
    public Optional<Review> findById(Long reviewId) {
        checkNotNull(reviewId, "reviewId must be provided");

        return reviewRepository.findById(reviewId);
    }

    @Transactional(readOnly = true)
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

}
