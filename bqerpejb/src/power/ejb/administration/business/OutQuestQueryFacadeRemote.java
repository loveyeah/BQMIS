/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 签报申请查询Remote
 * 
 * @author jincong
 * @version 1.0
 */
@Remote
public interface OutQuestQueryFacadeRemote {

	/**
	 * 查询签报申请信息
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param deptCode
	 *            部门编码
	 * @param workerCode
	 *            人员编码
	 * @param reportStatus
	 *            单据状态
	 * @param enterpriseCode
	 * 			  企业代码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 签报申请信息
	 * @throws SQLException 
	 */
	public PageObject findOutQueryApply(String startDate, String endDate,
			String deptCode, String workerCode, String reportStatus,
			String enterpriseCode, final int... rowStartIdxAndCount) throws SQLException;

	/**
	 * 查询签报申请附件信息
	 * 
	 * @param applyId
	 *            申请单号
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 签报申请附件信息
	 * @throws SQLException 
	 */
	public PageObject findOutQueryFile(String applyId,
			final int... rowStartIdxAndCount) throws SQLException;
	
	/**
	 * 根据ID查询签报申请附件信息
	 * 
	 * @param id
	 * @return 签报申请附件信息
	 * @throws SQLException 
	 */
	public PageObject findOutQueryFileById(String id) throws SQLException;
}
