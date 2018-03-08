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
public interface CarFileMaintenFacadeRemote {

	/**
	 * 按指定属性查找车辆档案
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param strStartDate 开始时间
	 * @param strEndDate 截止时间
	 * @param strDepCode 部门编码
	 * @param rowStartIdxAndCount 检索数据库附加参数
	 * @return 结果
	 * @throws SQLException
	 */
	public PageObject findCarFile(String strEnterpriseCode, String strStartDate, String strEndDate,
			String strDepCode, int... rowStartIdxAndCount) throws SQLException;
}
