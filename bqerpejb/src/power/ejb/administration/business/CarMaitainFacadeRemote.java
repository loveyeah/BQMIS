/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 车辆维修申请单
 * 
 * @author daichunlin
 */
@Remote
public interface CarMaitainFacadeRemote {
	/**
	 * query 车辆维修申请单查询
	 * @param strApplyNo
	 * @return PageObject
     * @author daichunlin
	 */
	public PageObject getCarMaitainInfo(String enterpriseCode,String strApplyNo,final int ...rowStartIdxAndCount)throws SQLException;
	/**
	 * query 车辆维修申请单明细查询
	 * @param strApplyNo
	 * @return PageObject
     * @author daichunlin
	 */
	public PageObject getCarMaitainDetailInfo(String enterpriseCode,String strApplyNo,final int ...rowStartIdxAndCount)throws SQLException;
}