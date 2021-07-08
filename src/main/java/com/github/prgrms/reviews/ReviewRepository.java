package com.github.prgrms.reviews;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {

    Optional<Review> findById(long id);

    List<Review> findAll();

    Long save(Review review);
}
