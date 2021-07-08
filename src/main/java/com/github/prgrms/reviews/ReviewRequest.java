package com.github.prgrms.reviews;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class ReviewRequest {

    @NotBlank(message = "content must be provided")
    @Length(max = 1000, message = "content length must be less than 1000 characters")
    private String content;

    protected ReviewRequest() {/*empty*/}

    public ReviewRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("content", content)
                .toString();
    }

}
