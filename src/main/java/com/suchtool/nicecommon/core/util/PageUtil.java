package com.suchtool.nicecommon.core.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suchtool.nicecommon.core.entity.PageBO;
import com.suchtool.nicecommon.core.entity.PageVO;
import com.suchtool.nicetool.util.base.BeanUtil;

import java.util.List;

public class PageUtil {
    public static <T> Page<T> toPage(PageBO pageBO) {
        Page<T> page = new Page<>();
        page.setSize(pageBO.getPageSize());
        page.setCurrent(pageBO.getCurrentPage());
        return page;
    }

    public static <T> PageVO<T> toPageVO(Page<T> page) {
        PageVO<T> pageVO = new PageVO<T>();
        pageVO.setCurrentPage(page.getCurrent());
        pageVO.setPageSize(page.getSize());
        pageVO.setTotalSize(page.getTotal());

        pageVO.setDataList(page.getRecords());

        return pageVO;
    }

    public static <T, R> PageVO<R> toPageVO(Page<T> page, Class<R> rClass) {
        PageVO<R> pageVO = new PageVO<R>();
        pageVO.setCurrentPage(page.getCurrent());
        pageVO.setPageSize(page.getSize());
        pageVO.setTotalSize(page.getTotal());

        page.getRecords();

        List<R> copy = BeanUtil.copy(page.getRecords(), rClass);

        pageVO.setDataList(copy);

        return pageVO;
    }
}
