package com.anubis.li.searchengine.core.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
public class SearchPage<T> implements Serializable {

    private static final long serialVersionUID = -5395997221963176643L;

    private List<T> list;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private int total;
    private String queryString;

    public static <T> SearchPage<T> newPage(int pageNumber, int pageSize) {
        return new SearchPage<>(pageNumber, pageSize);
    }

    public SearchPage(int pageNumber, int pageSize) {
        if(pageNumber<1){
            pageNumber = 1;
        }
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * Constructor.
     *
     * @param list       the list of paginate result
     * @param pageNumber the page number
     * @param pageSize   the page size
     * @param totalRow   the total row of paginate
     */
    public SearchPage(List<T> list, int pageNumber, int pageSize, int totalRow) {
        int totalPage = (int) (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            totalPage++;
        }
        this.list = list;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.total = totalRow;
    }

    /**
     * @param totalRow 要设置的 totalRow
     */
    public void setTotalRow(int totalRow) {
        this.total = totalRow;
        int totalPage = (int) (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            totalPage++;
        }
        this.totalPage = totalPage;
    }

    /**
     * @param list 要设置的 list
     */
    public void setList(List<T> list) {
        this.list = list;
    }

    public SearchPage() {

    }

    /**
     * Return list of this page.
     */
    public List<T> getList() {
        return list;
    }

    /**
     * Return page number.
     */
    public int getPageNumber() {
        return pageNumber == 0 ? 1 : pageNumber;
    }

    /**
     * Return page size.
     */
    public int getPageSize() {
        return pageSize == 0 ? 10 : pageSize;
    }

    /**
     * Return total page.
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * Return total row.
     */
    public int getTotal() {
        return total;
    }
    public String  getQueryString() {
        return queryString;
    }
    public void setQueryString(String  queryString) { this.queryString = queryString; }

    @JsonIgnore
    public int getPageNum() {
        return (pageNumber - 1) * pageSize;
    }


    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("pageNumber : ").append(pageNumber);
        msg.append("\npageSize : ").append(pageSize);
        msg.append("\ntotalPage : ").append(totalPage);
        msg.append("\ntotalRow : ").append(total);
        return msg.toString();
    }
}

