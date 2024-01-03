package com.knife.example.common.core.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PageRequest {
    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private Integer currentPage = 0;

    /**
     * 每页条数
     */
    @ApiModelProperty("每页条数")
    private Integer pageSize = 10;

    /**
     * 创建开始时间
     */
    @ApiModelProperty("创建开始时间")
    private LocalDateTime createTimeStart;

    /**
     * 创建结束时间
     */
    @ApiModelProperty("创建结束时间")
    private LocalDateTime createTimeEnd;
}
