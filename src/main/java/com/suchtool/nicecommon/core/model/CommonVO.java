package com.suchtool.nicecommon.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据库公共实体类
 */
@Data
public class CommonVO {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人ID")
    private String createId;

    @ApiModelProperty("创建人名字")
    private String createName;

    @ApiModelProperty("更新人ID")
    private String updateId;

    @ApiModelProperty("更新人名字")
    private String updateName;
}