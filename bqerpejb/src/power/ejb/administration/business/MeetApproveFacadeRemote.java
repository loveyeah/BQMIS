/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.business;

import java.io.Serializable;
import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 会务审批查询
 * MeetApproveFacadeRemote interface.
 * 
 * @author chenshoujiang
 */
@Remote
public interface MeetApproveFacadeRemote {
	/**
	 * query 会务审批查询
	 * @param startDate,endDate..
	 * @return PageObject
	 */
	public PageObject getMeetApproveInfo(String strStartDate,String strEndDate,String strDepCode,String strPerson,
				String strOverSpend,String strDcmStatus,String enterpriseCode,final int ...rowStartIdxAndCount)throws SQLException;
	/**
	 * query 会务申请查询帐票
	 * @param strApplyNo
	 * @return PageObject
     * @author daichunlin
	 */
	public PageObject getMeetApproveInfo(String strApplyNo,String enterpriseCode,final int ...rowStartIdxAndCount)throws SQLException;
}
