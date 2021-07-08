package com.github.prgrms.products;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class Product {

    private final Long seq;

    private String name;

    private String details;

    private int reviewCount;

    private final LocalDateTime createAt;

    public Product(String name, String details) {
        this(null, name, details, 0, null);
    }

    public Product(Long seq, String name, String details, int reviewCount, LocalDateTime createAt) {
        checkArgument(isNotEmpty(name), "name must be provided");
        checkArgument(
                name.length() >= 1 && name.length() <= 50,
                "name length must be between 1 and 50 characters"
        );
        checkArgument(
                isEmpty(details) || details.length() <= 1000,
                "details length must be less than 1000 characters"
        );

        this.seq = seq;
        this.name = name;
        this.details = details;
        this.reviewCount = reviewCount;
        this.createAt = defaultIfNull(createAt, now());
    }

    public void setName(String name) {
        checkArgument(isNotEmpty(name), "name must be provided");
        checkArgument(
                name.length() >= 1 && name.length() <= 50,
                "name length must be between 1 and 50 characters"
        );

        this.name = name;
    }

    public void setDetails(String details) {
        checkArgument(
                isEmpty(details) || details.length() <= 1000,
                "details length must be less than 1000 characters"
        );

        this.details = details;
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getDetails() {
        return ofNullable(details);
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(seq, product.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("name", name)
                .append("details", details)
                .append("reviewCount", reviewCount)
                .append("createAt", createAt)
                .toString();
    }

    static public class Builder {
        private Long seq;
        private String name;
        private String details;
        private int reviewCount;
        private LocalDateTime createAt;

        public Builder() {/*empty*/}

        public Builder(Product product) {
            this.seq = product.seq;
            this.name = product.name;
            this.details = product.details;
            this.reviewCount = product.reviewCount;
            this.createAt = product.createAt;
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder details(String details) {
            this.details = details;
            return this;
        }

        public Builder reviewCount(int reviewCount) {
            this.reviewCount = reviewCount;
            return this;
        }

        public Builder createAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        public Product build() {
            return new Product(
                    seq,
                    name,
                    details,
                    reviewCount,
                    createAt
            );
        }
    }

}