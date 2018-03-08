/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.sql.SQLException;
import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;

/**
 * 基础表维护Remote
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
@Remote
public interface BaseTableInfoFacadeRemote {
	/**
	 * 获得基础表维护信息
	 * 
	 * @param tableName
	 *            基础表名称
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 * @throws SQLException
	 */
	public PageObject getBaseTableRecordList(String tableName,
			String enterpriseCode, final int... rowStartIdxAndCount)
			throws SQLException;
}