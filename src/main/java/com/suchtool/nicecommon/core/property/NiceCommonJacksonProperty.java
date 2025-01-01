package com.suchtool.nicecommon.core.property;

import lombok.Data;

@Data
public class NiceCommonJacksonProperty {
    /**
     * 启用配置
     */
    private Boolean enableConfig = true;

    /**
     * 启用避免报错功能
     */
    private Boolean enableAvoidFail = true;
}
