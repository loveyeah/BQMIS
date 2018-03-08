/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;


/**
 * 定期工作登记
 * 
 * @author daichunlin
 */
@Remote
public interface RegularWorkRegisterFacadeRemote {
	/**
	 * 定期工作登记取得
	 * @param strWorkType 工作类别
	 * @param rowStartIdxAndCount  分页
	 * @return PageObject
	 */
	public PageObject getRegisterList(String strEnterpriseCode,String strWorkType, int... rowStartIdxAndCount);
	/**
	 * 全部定期工作
	 * @param strWorkType 工作类别
	 * @return PageObject
	 */
	public PageObject getAllRegularWorkList(String strEnterpriseCode,String strWorkType);
	/**
	 * 通过ID查询定期工作
	 * @param id 序号
	 * @return PageObject
	 */
	public PageObject findById(Long id);

}