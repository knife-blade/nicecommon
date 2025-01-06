package com.suchtool.nicecommon.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {
    @ApiModelProperty("当前页序号")
    private Long currentPageIndex = 0L;

    @ApiModelProperty("每页个数")
    private Long pageSize = 10L;

    @ApiModelProperty("总个数")
    private Long totalSize = 0L;

    private List<T> dataList;
}