/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;

/**
 * 接待审批查询Remote
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
@Remote
public interface ReceptionQueryInfoFacadeRemote {
	/**
	 * 通过查询条件获得相应的接待信息数据
	 * 
	 * @param strStartDate
	 * @param strEndDate
	 * @param strdeptCode
	 * @param strWorkerCode
	 * @param strIsOver
	 * @param strDcmStatus
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 * @throws SQLException 
	 */
	public PageObject getReceptionQueryInfo(String strStartDate,
			    String strEndDate,String strdeptCode,
			    String strWorkerCode,String strIsOver, 
			    String strDcmStatus, String enterpriseCode,
	            final int ...rowStartIdxAndCount) throws SQLException;
}