/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.business;

import java.io.Serializable;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * DutyManSearchFacade 值班人员查询..
 * 
 * @author zhaomingjian
 */
@Remote
public interface DutyManSearchFacadeRemote extends Serializable {
	  
	/**
     * @param strStartDate 起始时间
     * @param strEndDate 截止时间
     * @param  strWorkTypeCode 工作类型码
     * @param  strSubWorkTypeCode 子工作类型码
     * @param  strDutyTypeCode 值类型码
     * @return  PageObject
     */
	public PageObject getOnDutyManInfo(String startDate,String endDate,String workTypeCode,String subWorkTypeCode,String valueTypeCode,String strEnterPriseCode,final int ...rowStartIdxAndCount);
}
