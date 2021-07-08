package com.github.prgrms.reviews;

import com.github.prgrms.errors.NotFoundException;
import com.github.prgrms.security.JwtAuthentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.github.prgrms.utils.ApiUtils.ApiResult;

import java.util.Optional;

import static com.github.prgrms.utils.ApiUtils.success;


@RestController
@RequestMapping("api/orders")
public class ReviewRestController {
    // TODO review 메소드 구현이 필요합니다. (완료)
    private final ReviewService reviewService;

    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{id}/review")
    public ApiResult<ReviewDto> review(@AuthenticationPrincipal JwtAuthentication authentication, @PathVariable Long id,
                                       @RequestBody ReviewRequest request) {
        Long seq = reviewService.newReview(authentication.id, id, request.getContent());
        Optional<Review> result = reviewService.findById(seq);

        return success(result.map(ReviewDto::new).orElseThrow(() -> new NotFoundException("Could not found review for " + seq)));
    }
}