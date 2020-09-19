package com.daishuai.mybatis.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class PageSearchDto {

    private Integer pageNo;

    private Integer pageSize;

    public Pageable pageable() {
        return PageRequest.of(pageNo, pageSize);
    }
}
