package com.imooc.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.utils.PagedGridResult;

public interface IBaseService{

    /**
     * 设置表格的回显数据
     * @param pageLists
     * @return
     */
    default  PagedGridResult setterPagedGrid(Page<?> pageLists){
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setRows(pageLists.getRecords());
        pagedGridResult.setPage(pageLists.getCurrent());
        pagedGridResult.setTotal(pageLists.getPages());
        pagedGridResult.setRecords(pageLists.getTotal());
        return pagedGridResult;
    }
}
