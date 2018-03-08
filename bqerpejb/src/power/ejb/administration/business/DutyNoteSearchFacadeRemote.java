/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface 值班记事查询EJB
 * 
 * @author 赵明建
 */
@Remote
public interface DutyNoteSearchFacadeRemote {
    /**
     * 返回记事查询结果
     * @Param startDate  起始时间 
     * @Param endDate  结束时间
     * @Param workTypeCode 工作类型编码
     * @Param subWorkTypeCode 子工作类型编码
     * @Param  valueTypeCode 值类型编码
     * @Param  rowStartIdxAndCount 起始行及最大行限制
     * @return PageObject
     */
    public PageObject getOnDutyListInfo(String startDate,String endDate,String workTypeCode,String subWorkTypeCode,String valueTypeCode,String strEnterPriseCode,final int ...rowStartIdxAndCount);
}
