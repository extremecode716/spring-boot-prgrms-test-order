package com.github.prgrms.configures.web;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;

public class SimplePageRequest implements Pageable {

    private final long offset;

    private final int size;

    public SimplePageRequest() {
        this(0, 5);
    }

    public SimplePageRequest(long offset, int size) {
        checkArgument(offset >= 0, "offset must be greater or equals to zero");
        checkArgument(size >= 1, "size must be greater than zero");

        this.offset = offset;
        this.size = size;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("offset", offset)
                .append("size", size)
                .toString();
    }

}