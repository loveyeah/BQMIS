/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
/**
 * 物料盘点调整interface
 * @author huangweijie
 */
@Remote 
public interface BookCheck {
    /**
     * 查询盘点单号
     * @param bookId 盘点单号ID
     * @param rowStartIdxAndCount 翻页参数
     * @return PageObject 盘点单明细
     */
    public PageObject findBookDetails(String bookNo, String enterpriseCode, final int... rowStartIdxAndCount);
}