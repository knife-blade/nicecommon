package com.suchtool.nicecommon.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PageBO {
    @ApiModelProperty("当前页序号")
    private Long currentPageIndex = 0L;

    @ApiModelProperty("每页个数")
    private Long pageSize = 10L;

    @ApiModelProperty("创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty("创建时间结束")
    private LocalDateTime createTimeEnd;
}
