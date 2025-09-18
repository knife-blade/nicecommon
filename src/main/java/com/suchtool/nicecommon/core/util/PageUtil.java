package com.suchtool.nicecommon.core.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.suchtool.nicecommon.core.model.PageBO;
import com.suchtool.nicecommon.core.model.PageVO;
import com.suchtool.nicetool.util.base.BeanUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class PageUtil {
    public static <T> Page<T> toPageVO(PageBO pageBO) {
        Page<T> page = new Page<>();
        page.setSize(pageBO.getPageSize());
        page.setCurrent(pageBO.getCurrentPageIndex());
        return page;
    }

    public static <T> PageVO<T> toPageVO(Page<T> page) {
        PageVO<T> pageVO = new PageVO<T>();
        pageVO.setCurrentPageIndex(page.getCurrent());
        pageVO.setPageSize(page.getSize());
        pageVO.setTotalSize(page.getTotal());

        pageVO.setDataList(page.getRecords());

        return pageVO;
    }

    public static <T, R> PageVO<R> toPageVO(Page<T> page, Class<R> rClass) {
        PageVO<R> pageVO = new PageVO<R>();
        pageVO.setCurrentPageIndex(page.getCurrent());
        pageVO.setPageSize(page.getSize());
        pageVO.setTotalSize(page.getTotal());

        page.getRecords();

        List<R> copy = BeanUtil.copy(page.getRecords(), rClass);

        pageVO.setDataList(copy);

        return pageVO;
    }

    public static <T, R> PageVO<R> toPageVODeepCopyByJson(Page<T> page, TypeReference<List<R>> typeReference) {
        PageVO<R> pageVO = new PageVO<R>();
        pageVO.setCurrentPageIndex(page.getCurrent());
        pageVO.setPageSize(page.getSize());
        pageVO.setTotalSize(page.getTotal());

        page.getRecords();

        List<R> copy = BeanUtil.deepCopyByJson(page.getRecords(), typeReference);

        pageVO.setDataList(copy);

        return pageVO;
    }

    public static <T> PageVO<T> toPageVO(List<T> list, PageBO pageBO, Long startPageIndex) {
        Long tmpStartPageIndex = startPageIndex != null
                ? startPageIndex
                : 1;

        PageVO<T> pageVO = new PageVO<>();
        pageVO.setPageSize(pageBO.getPageSize());
        pageVO.setCurrentPageIndex(pageBO.getCurrentPageIndex());

        long allSize = 0;
        List<T> listResult = null;

        if (!CollectionUtils.isEmpty(list)) {
            int startIndex = (int) ((pageBO.getCurrentPageIndex() - tmpStartPageIndex)
                    * pageBO.getPageSize());
            allSize = list.size();
            long dataSize = allSize - startIndex;
            listResult = list.subList(startIndex, (int)(dataSize - startIndex));
        }

        pageVO.setTotalSize(allSize);
        pageVO.setDataList(listResult);

        return pageVO;
    }
}
