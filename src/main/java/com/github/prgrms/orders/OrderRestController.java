package com.github.prgrms.orders;

import com.github.prgrms.configures.web.Pageable;
import com.github.prgrms.errors.NotFoundException;
import com.github.prgrms.reviews.ReviewDto;
import com.github.prgrms.reviews.ReviewService;
import com.github.prgrms.security.JwtAuthentication;
import com.github.prgrms.utils.ApiUtils.ApiResult;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.github.prgrms.utils.ApiUtils.success;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api/orders")
public class OrderRestController {
    // TODO findAll, findById, accept, reject, shipping, complete 메소드 구현이 필요합니다. (완료)
    private final OrderService orderService;
    private final ReviewService reviewService;

    public OrderRestController(OrderService orderService, ReviewService reviewService) {
        this.orderService = orderService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public ApiResult<List<OrderDto>> findAll(Pageable pageable) {
        List<OrderDto> list = orderService.findAll(pageable).stream().map(OrderDto::new).collect(toList());

        list.stream().forEach(dto -> {
            if (dto.getReview().getSeq() > 0) {
                dto.setReview(reviewService.findById(dto.getReview().getSeq()).map(ReviewDto::new).get());
            } else {
                dto.setReview(null);
            }
        });

        return success(list);
    }

    @GetMapping("/{id}")
    public ApiResult<OrderDto> findById(@PathVariable Long id) {
        OrderDto dto = orderService.findById(id).map(OrderDto::new).orElseThrow(() -> new NotFoundException("Could not found product for " + id));

        if (dto.getReview().getSeq() > 0) {
            dto.setReview(reviewService.findById(dto.getReview().getSeq()).map(ReviewDto::new).get());
        } else {
            dto.setReview(null);
        }

        return success(dto);
    }

    @PatchMapping("/{id}/accept")
    public ApiResult<Boolean> accept(@PathVariable Long id) {
        return success(orderService.accept(id));
    }

    @PatchMapping("/{id}/reject")
    public ApiResult<Boolean> reject(@AuthenticationPrincipal JwtAuthentication authentication, @PathVariable Long id, @Valid @RequestBody(required = false) OrderRejectRequest request) {
        checkArgument(request != null, "Could not found reject request");

        return success(orderService.reject(id, request.getMessage()));
    }

    @PatchMapping("/{id}/shipping")
    public ApiResult<Boolean> shipping(@PathVariable Long id) {
        return success(orderService.shipping(id));
    }

    @PatchMapping("/{id}/complete")
    public ApiResult<Boolean> complete(@PathVariable Long id) {
        return success(orderService.complete(id));
    }
}
