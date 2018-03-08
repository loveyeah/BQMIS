/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 劳务派遣查询Remote
 * 
 * @author lichensheng
 * @version 1.0
 */
@Remote
public interface LabourSendSearchFacadeRemote {
	/**
	 * 查询劳务派遣信息
	 * @param strStartDate 起始日期
	 * @param strEndDate 结束日期
	 * @param strCooperateUnit 协助单位
	 * @param strDcmStatus 单据状态
	 * @param enterpriseCode 企业编码
	 * @return PageObject
	 * @throws SQLException
	 */
	public PageObject ejbGetLabourBy(String strStartDate, String strEndDate,
			String strCooperateUnit, String strDcmStatus,String enterpriseCode,String transferType,
			final int... rowStartIdxAndCount) throws SQLException;
	/**
	 * 查询人员一览信息
	 * @param lngBorrowcontractid 劳务派遣合同ID
	 * @param enterpriseCode 企业编码
	 * @return PageObject
	 * @throws SQLException
	 */
	public PageObject ejbGetEmpBy(Long lngBorrowcontractid,String enterpriseCode) throws SQLException;
	
}