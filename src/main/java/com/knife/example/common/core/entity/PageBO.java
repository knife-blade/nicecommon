package com.knife.example.common.core.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PageBO {
    @ApiModelProperty("当前页序号")
    private Long currentPage = 0L;

    @ApiModelProperty("每页个数")
    private Long pageSize = 10L;

    @ApiModelProperty("创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty("创建时间结束")
    private LocalDateTime createTimeEnd;
}
