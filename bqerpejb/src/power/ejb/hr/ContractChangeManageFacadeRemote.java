/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 合同变更管理Remote
 * 
 * @author jincong
 * @version 1.0
 */
@Remote
public interface ContractChangeManageFacadeRemote {

	/**
	 * 根据人员Id获得变更合同的信息
	 * 
	 * @param empId
	 *            人员Id
	 * @param workContractId
	 * 			  劳动合同签订ID
	 * @param enterpriseCode
	 *            企业编码
	 * @return PageObject
	 */
	public PageObject getContractChangeInfo(String empId, String workContractId,
			String enterpriseCode);

	/**
	 * 根据人员Id获得登记合同的信息
	 * 
	 * @param empId
	 *            人员Id
	 * @param enterpriseCode
	 *            企业编码
	 * @return PageObject
	 */
	public PageObject getContractInfoForChange(String empId,
			String enterpriseCode);
	
	/**
	 * 查找合同有效期数据
	 * 
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject findContractTerm(String enterpriseCode,
			final int... rowStartIdxAndCount);
	
	/**
	 * 根据人员Id取得部门和岗位
	 * 
	 * @param empId 人员Id
	 * @param enterpriseCode 企业编码
	 * @return PageObject
	 */
	public PageObject getBaseInfoForChange(String empId, String enterpriseCode);
}
