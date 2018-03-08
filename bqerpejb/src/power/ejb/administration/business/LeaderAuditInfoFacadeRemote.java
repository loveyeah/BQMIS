/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;
/**
 * 值长审核查询Remote
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
@Remote
public interface LeaderAuditInfoFacadeRemote {
	/**
	 * 通过查询条件获得相应的值长审核信息数据
	 * 
	 * @param strMenuDate
	 * @param strMenuType
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 * @throws SQLException
	 */
	public PageObject getLeaderAuditInfo(String strMenuDate,String strMenuType,
			String enterpriseCode, final int ...rowStartIdxAndCount) throws SQLException;
}