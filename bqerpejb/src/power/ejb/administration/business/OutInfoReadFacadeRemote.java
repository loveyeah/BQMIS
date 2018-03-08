/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJEmpInfo;

/**
 * 签报信息查阅Remote
 * 
 * @author jincong
 * @version 1.0
 */
@Remote
public interface OutInfoReadFacadeRemote {

	/**
	 * 查找签报信息
	 * 
	 * @param status
	 *            阅读状态
	 * @param applyId
	 *            申请单号
	 * @param readMan
	 *            抄送人员
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 签报信息
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findOutInfo(String status, String applyId,
			String readMan, String enterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException;

	/**
	 * 查找签报信息
	 * 
	 * @param applyId
	 *            申请单号
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 签报信息
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findOutInfo(String applyId, String enterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException;

	/**
	 * 根据人员编码查找人员信息
	 * 
	 * @param workerCode 人员编码
	 * @param enterpriseCode 企业编码
	 * @return 人员信息
	 * @throws SQLException
	 */
	public HrJEmpInfo findNameByCode(String workerCode, String enterpriseCode) throws SQLException;

}
