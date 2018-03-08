/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * @author chaihao
 * 
 */
@Remote
public interface CarRepairQueryFacadeRemote {

	/**
	 * 获取车牌号
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @return 结果
	 * @throws SQLException
	 */
	public PageObject getCarNo(String strEnterpriseCode) throws SQLException;

	/**
	 * 车辆维修查询
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param strStartDate 起始日期
	 * @param strEndDate 截止日期
	 * @param strManager 经办人
	 * @param strCarNo 车牌号
	 * @param strDcmStatus 单据状态
	 * @return 结果
	 * @throws SQLException
	 */
	public PageObject getCarRepair(String strEnterpriseCode, String strStartDate, String strEndDate,
			String strManager, String strCarNo, String strDcmStatus)
			throws SQLException;
	
	/**
	 * 车辆维修明细查询
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param strWhId 车辆维护序号
	 * @param strProCode 项目编码
	 * @param rowStartIdxAndCount 检索数据库附加参数
	 * @return 结果
	 * @throws SQLException
	 */
	public PageObject getCarRepairList(String strEnterpriseCode, String strWhId, String strProCode,int... rowStartIdxAndCount)
			throws SQLException;
}
