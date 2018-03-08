package power.ejb.basedata;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.KeyValue;
import power.ejb.comm.TreeNode;
import power.ejb.hr.form.Parameter;

@Remote 
public interface BaseDataManager {
	
	public PageObject findUnitList(String queryKey,String enterpriseCode,final int... rowStartIdxAndCount);
	 
	
	/**
	 * 取得子部门
	 */
	public List<TreeNode> getDeptsByPid(Long pid,String needBanzuCheck);
	/**
	 * 由部门查询人员(级连查询)
	 * @param deptId 部门编码
	 * @param notInWorkerCodes 格式为 : '1440','0689'
	 * @return List<TreeNode>
	 */
	public PageObject getWorkersByDeptId(Long deptId,Long dept, String flag, String queryKey,
			String notInWorkerCodes, final int... rowStartIdxAndCount);
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
	 * 取得参数列表
	 * @return List<Parameter> 参数列表
	 */
	public List<Parameter> getParamsList();
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
	
	/**
	 * 获得部门标识+父部门标识
	 * add by fyyang 090317 for createWorkticketNo
	 * @param deptCode 部门编码
	 * @return
	 */
	public String getDeptAndPdeptIdentifier(String deptCode);
	/**
	 * 获得专业标识
	 * @param specialityCode 专业编码
	 * @return
	 */
	public String getSpecialityShortName(String specialityCode);
	/**
	 * 获得部门标识
	 * @param operatorMan 操作人工号
	 * @return
	 */
	public String getDeptIdentifierByOpCode(String operatorMan);
	/**
	 * 取得
	 * @param enterpriseCode 企业编码
	 * @param type 类型
	 * @return List<KeyValue>
	 */
	public List<KeyValue> getBpBasicDataByType(String enterpriseCode,String type);
	/**
	 * 判断该部门属于哪类部门 
	 * add by fyyang 090414
	 * @param deptCode 部门编码
	 * @return String  "FD"---发电分公司 "JX"----检修分公司  "SY"---实业分公司
	 */
	public String checkDeptType(String deptCode);
	
	/**
	 * 获得该部门下的所有员工（不级联）
	 * add by fyyang 090908
	 * @param deptId
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getWorkersOnlyInDeptId(Long deptId, 
			 final int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 091219 按查询条件查询所有指标
	 * @param argFuzzy
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getAllItemToCommon(String argFuzzy,String dataTimeType,String itemType,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 091219 获得指标树findOperateItemTreeForSelect
	 * @param itemCode
	 * @param enterpriseCode
	 * @param searchKey
	 * @return
	 */
	public List<TreeNode> findItemTreeToCommon(String itemCode,String dataTimeType,String itemType,
			String enterpriseCode,String searchKey);
	
	
	/**
	 * add by liuyi 091219 按查询条件查询所有经营指标
	 * @param argFuzzy
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getOperateItemForSelect(String argFuzzy,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 091219 获得经营指标树
	 * @param itemCode
	 * @param enterpriseCode
	 * @param searchKey
	 * @return
	 */
	public List<TreeNode> findOperateItemTreeForSelect(String itemCode,
			String enterpriseCode,String searchKey);
	
	/**
	 * 删除工作流信息
	 * add by fyyang 090727
	 * @param workflowNo 工作流实例号
	 */
	public void deleteWf(Long workflowNo);
	/**
	 * 根据部门Id查询一级部门id，编码，名称
	 * @param depid
	 * @return List
	 * add by kzhang20100917
	 */
	public List getFirstLeverDeptByDeptId(String depid);
}
