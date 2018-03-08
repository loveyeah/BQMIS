/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.VisitRegisterInfo;

/**
 * 来访人员登记Remote
 * 
 * @author daichunlin
 * 
 */
@Remote
public interface VisitRegisterFacadeRemote {

	/**
	 * 来访人员登记
	 * 
	 * @param rowStartIdxAndCount
	 *            页面参数
	 * @return PageObject 检索结果
	 * @throws SQLException 
	 */
	public PageObject getVisitInfoList(String strEnterpriseCode,int... rowStartIdxAndCount) throws SQLException;
	
}