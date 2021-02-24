package com.zhangweiwhim.es.model;

/**
 * Description: sprinboot-es
 * Created by zhangwei on 2021/2/23 17:10
 */
public class PageModel {

    private int pageNo; //页码
    private int pageSize; //每页显示行数
    private int totalPage; //总页数
    private int totalRow; //总行数


    public PageModel() {
    }

    public PageModel(int pageNo, int pageSize, int totalPage, int totalRow) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalRow = totalRow;
    }

    public PageModel(int pageNo, int pageSize, int totalRow) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalPage = getTotalPage(totalRow, pageSize);
        this.totalRow = totalRow;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }


    /**
     * 获取总页数
     *
     * @param totalRow
     * @param pageSize
     * @return
     */
    public int getTotalPage(int totalRow, int pageSize) {
        if (totalRow % pageSize == 0) {
            return totalRow / pageSize;
        }
        return totalRow / pageSize + 1;
    }

}
