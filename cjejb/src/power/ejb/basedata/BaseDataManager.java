package power.ejb.basedata;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.system.SysCUl;

@Remote 
public interface BaseDataManager {
	/**
	 * 取得管理员列表
	 * @param enterprisecode 企业编码
	 * @return List<SysCUl>
	 */
	public List<SysCUl> getAdminWorker(String enterprisecode);
	/**
	 * 判断是否管理员
	 * @param enterpriseCode 企业编码
	 * @param workerCode 工号  
	 * @return boolean
	 */
	public boolean checkWorkerIsAdmin(String workerCode,String enterpriseCode);
	/**
	 * 取得子部门
	 */
	public List<TreeNode> getDeptsByPid(String pid);
	/**
	 * 由部门查询人员(级连查询)
	 * @param deptId 部门编码
	 * @param notInWorkerCodes 格式为 : '1440','0689'
	 * @return List<TreeNode>
	 */
	public PageObject getWorkersByDeptId(String deptCode, String queryKey,String notInWorkerCodes,
			final int... rowStartIdxAndCount);
	/**
	 * 获取本币币种
	 * @return parameterValue String
	 */
	public String getOriCurrency();
	/**
	 * 获取物料领用方式 
	 * @return parameterValue String
	 * 1：按照谁计划谁领用方式,2：按照先到先领方式
	 */
	public String getIssueType();
	/**
	 * 根据专业类型与专业编码取得子专业
	 * @param specialType 专业类型
	 * @param enterpriseCode 企业编码
	 * @param specialCode 专业编码
	 * @return List<TreeNode>
	 */
	public List<TreeNode> getSpecialsByPCode(String enterpriseCode,String specialType,String specialCode);
	/**
	 * 更新参数值
	 * @param  parameterValue 参数值
	 * @param  parameterNo 参数编码
	 */
	public void updateParamValue(String parameterNo,String parameterValue);
	/**
	 * 获取员工的基本信息
	 * @param String workerCode 工号 
	 * @return Employee
	 */
	public Employee getEmployeeInfo(String workerCode);
	
	/**
	 * 验证工号密码
	 * add by fyyang 090212
	 * @param enterpriseCode 企业编码
	 * @param workerCode 工号
	 * @param password 密码
	 * @return
	 */
	public boolean checkUserRight(String enterpriseCode,String workerCode,String password);
	/**
	 * 判断是否点检员
	 * @param workerCode 工号
	 * @return boolean
	 */
	public boolean checkIsDianJianYuan(String workerCode);
}
