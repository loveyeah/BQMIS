/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.util.zip.DataFormatException;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJReception;

/**
 * 接待申请上报.
 * 
 * @author liugonglei
 */
@Remote
public interface ReceptionApplyFacadeRemote {
    /**
     * 检索接待申请一览
     * @param strWorkCode
     * @return
     * @throws SQLException 
     */
	public PageObject findReceptApply(String strWorkCode,String strEnterPriseCode, int start, int limit) throws SQLException;
	/**
	 * 更新来宾接待审批单
	 * @param entity
	 * @param lngUpdateTime
	 * @throws SQLException
	 * @throws DataFormatException
	 * @throws DataChangeException 
	 */
    public AdJReception updateReceptApply(AdJReception entity,Long lngUpdateTime)throws SQLException, DataChangeException;
    /**
     * 取得接待申请id
     * @return
     * @throws SQLException
     */
    public Long getReceptionId() throws SQLException;
}
