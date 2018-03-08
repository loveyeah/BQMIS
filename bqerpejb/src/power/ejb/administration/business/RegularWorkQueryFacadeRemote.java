/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 定期工作查询Remote
 * 
 * @author chaihao
 * 
 */
@Remote
public interface RegularWorkQueryFacadeRemote {

	/**
	 * 定期工作查询
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param startDate 起始时间
	 * @param endDate 截止时间
	 * @param subWorkType 子类别编码
	 * @param flag 标志
	 * @param workType 工作类别编码
	 * @param rowStartIdxAndCount 检索数据附加参数
	 * @return PageObject 检索结果
	 */
	public PageObject findRegularWork(String strEnterpriseCode, String strStartDate, String strEndDate,
			String strSubWorkType, String strFlag, String strWorkType,
			int... rowStartIdxAndCount) throws SQLException;
}
