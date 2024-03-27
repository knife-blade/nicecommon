package com.suchtool.nicecommon.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据库公共实体类
 */
@Data
public class CommonEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String createId;

    /**
     * 创建人名字
     */
    @TableField(fill = FieldFill.INSERT)
    private String createName;

    /**
     * 更新人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateId;

    /**
     * 更新人名字
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;

    /**
     * 删除标记。0：未删除；其他：已删除
     */
    @TableLogic(delval = "id")
    private Long deleteFlag;
}