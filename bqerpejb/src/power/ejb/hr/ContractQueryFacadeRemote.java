/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 合同台帐查询Remote
 * 
 * @author jincong
 * @version 1.0
 */
@Remote
public interface ContractQueryFacadeRemote {

	/**
	 * 员工合同查询
	 * 到期合同查询
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param deptCode 部门编码
	 * @param contractTerm 合同有效期
	 * @param contractType 合同形式
	 * @param duetoTime 合同到期月份
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	public PageObject getContractQueryEmployee(String startDate, String endDate,
			String deptCode, String contractTerm, String contractType,
			String duetoTime, String enterpriseCode, final int... rowStartIdxAndCount);
	
	/**
	 * 续签合同查询
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param deptCode 部门编码
	 * @param contractTerm 合同有效期
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	public PageObject getContractQueryContinue(String startDate, String endDate,
			String deptCode, String contractTerm, String enterpriseCode,
			final int... rowStartIdxAndCount);
	
	/**
	 * 合同变更查询
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param deptBeforeCode 变更前部门编码
	 * @param deptAfterCode 变更后部门编码
	 * @param contractTerm 合同有效期
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	public PageObject getContractQueryChange(String startDate, String endDate,
			String deptBeforeCode, String deptAfterCode, String contractTerm,
			String enterpriseCode, final int... rowStartIdxAndCount);
	
	/**
	 * 合同终止查询
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param deptCode 部门编码
	 * @param contractTerm 合同有效期
	 * @param stopType 终止类别
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	public PageObject getContractQueryStop(String startDate, String endDate,
			String deptCode, String contractTerm, String stopType,
			String enterpriseCode, final int... rowStartIdxAndCount);
	
	/**
	 * 未签合同查询
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param deptCode 部门编码
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	public PageObject getContractQueryNotsign(String startDate, String endDate,
			String deptCode, String enterpriseCode, final int... rowStartIdxAndCount);
	
	/**
	 * 查找合同有效期数据
	 * 
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	public PageObject findContractTerm(String enterpriseCode,
			final int... rowStartIdxAndCount);
}
