/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 来访人员查询Remote
 * @author chaihao
 * 
 */
@Remote
public interface VisitorQueryFacadeRemote {

	/**
	 * 来访人员查询
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param startDate 起始时间
	 * @param endDate 截止时间
	 * @param depCode 部门编码
	 * @param rowStartIdxAndCount 数据检索附加参数
	 * @return PageObject 检索结果
	 */
	public PageObject findVisitor(String strEnterpriseCode, String strStartDate, String strEndDate,
			String strDepCode, int... rowStartIdxAndCount) throws SQLException;
}