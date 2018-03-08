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
public interface AdCTimeWorkAllFacadeRemote {
	/**
	 * 子画面全部定期工作查询
	 * @param fuzzy 查询条件
	 * @param strWorkType 工作类别
	 * @param rowStartIdxAndCount 页面控制
	 * @return PageObject
	 * @author daichunlin
	 */
	public PageObject getAllRegularQuery(String strEnterpriseCode,String fuzzy, String strWorkType,
			final int... rowStartIdxAndCount);

}