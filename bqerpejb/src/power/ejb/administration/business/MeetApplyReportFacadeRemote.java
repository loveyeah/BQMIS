/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 会务申请上报
 * @author huangweijie
 */
@Remote
public interface MeetApplyReportFacadeRemote {

	/**
	 * query 会务申请上报查询
	 * @param workerCode 登陆的员工号
	 * @param argEnterpriseCode 企业编码
	 * @return PageObject
	 * @throws SQLException 数据库异常
	 */
	public PageObject getMeetApplyReportList(String workerCode, String argEnterpriseCode,
			final int ...rowStartIdxAndCount) throws SQLException;
}

