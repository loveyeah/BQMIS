/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJReception;

/**
 * 接待费用管理接口.
 * 
 * @author wangyun
 */
@Remote
public interface ReceptionChargeFacadeRemote {
	/**
	 * 接待费用管理一览
	 * 
	 * @param argEnterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            分页
	 * @throws SQLException
	 */
	public PageObject getReceptionCharge(String argEnterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException;

	/**
	 * 来宾接待审批单更新
	 * 
	 * @param entity
	 *            AdJReception
	 * @param strUpdateTime
	 *            修改时间
	 * @throws DataChangeException
	 * @throws SQLException
	 */
	public void update(AdJReception entity, String strUpdateTime)
			throws DataChangeException, SQLException;
}
