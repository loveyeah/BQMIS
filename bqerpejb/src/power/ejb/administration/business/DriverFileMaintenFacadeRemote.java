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
public interface DriverFileMaintenFacadeRemote {

	/**
	 * 按属性查找司机档案
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param strDepCode 部门编码
	 * @param strWorkerCode 人员编码
	 * @param strLicence 驾照类型
	 * @param rowStartIdxAndCount 检索数据附加参数
	 * @return 结果
	 */
	public PageObject findDriverFile(String strEnterpriseCode, String strDepCode, String strWorkerCode,
			String strLicence, int... rowStartIdxAndCount) throws SQLException;
	
	/**
	 * 按人员编码查询人员信息
	 * 
	 * params strWorkerCode 人员编码
	 * return PageObject 结果
	 */
	public PageObject findByWorkCode(String strWorkerCode) throws SQLException;
}
