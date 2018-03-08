package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.form.Dept;

/**
 * 部门管理
 *
 * @author wzhyan
 */
@Remote
public interface HrCDeptFacadeRemote {
	/**
	 * 新增部门
	 * @param entity 部门实体
	 * @throws CodeRepeatException 
	 */
	public void save(HrCDept entity) throws CodeRepeatException;

	/**
	 * 删除部门
	 * @param entity
	 */
	public void delete(HrCDept entity);
	
	/**
	 * 根据人员查部门名称	
	 */
	public String getDeptNameByEmpCode(String empCode);

	/**
	 * 修改部门信息
	 * @param entity
	 * @return HrCDept
	 */
	public HrCDept update(HrCDept entity);
	/**
	 * 部门ID查找
	 * @param id 部门流水号
	 * @return HrCDept
	 */
	public HrCDept findById(Long id); 
	/**
	 * Find all HrCDept entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCDept property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCDept> found by query
	 */
//	public List<HrCDept> findByProperty(String propertyName, Object value,
//			int... rowStartIdxAndCount);

//	public List<HrCDept> findByProperties(String strWhere, Object[] o,
//			final int... rowStartIdxAndCount);

	/**
	 * 根据父部门Id查找所有子部门列表
	 * 
	 * @param Object
	 *            pdeptId 父节点Id
	 * @praam int... rowStartIdxAndCount 分页
	 */
	public List<HrCDept> findByPdeptId(Object pdeptId,
			int... rowStartIdxAndCount);



	public List<HrCDept> getDeptByPId(Long pid);

	/*
	 * 取缺陷要检修部门
	 */
	public List<HrCDept> getFailDeptById();
	public List<HrCDept> getFailDeptById1();
	public List<HrCDept> getFailDeptById2();
	
	/**
	 * 取缺陷要检修部门老和新老共用
	 * add by bjxu 091219
	 * @return
	 */
	public List<HrCDept> getFailDeptByIdStop();
	
	public HrCDept getDeptInfoByempCode(String empCode);
	
	/**
	 * 通过部门编码获取部门信息
	 * add by fyyang 090306
	 * @param deptCode
	 * @return
	 */
	public HrCDept getDeptInfoByDeptCode(String deptCode);
	
//	/**
//	 * 由部门查询人员(级连查询)
//	 * @param deptId 部门编码
//	 * @param notInWorkerCodes 格式为 : '1440','0689'
//	 * @return List<Employee>
//	 */
//	public PageObject getWorkersByDeptId(Long deptId,String queryKey,String notInWorkerCodes,final int... rowStartIdxAndCount);
	

	
//	/**
//	 * 由部门查询人员(级连查询)
//	 * @param deptId 部门编码
//	 * @param notInWorkerCodes 格式为 : '1440','0689'
//	 * @return List<Employee>
//	 */
//	public PageObject getWorkersByDeptId(Long deptId,String queryKey,String notInWorkerCodes,final int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 090915
	 * copy by fyyang 090929
	 * 查询该公司的部门信息
	 * @param enterpriseCode
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getDeptInfo(String enterpriseCode) throws SQLException;
	
	/**
	 * add by fyyang 090929
	 * @param strIsBanzu
	 * @param enterpriseCode
	 * @return
	 * @throws SQLException
	 */
	public PageObject getBanzuInfo(String strIsBanzu,String enterpriseCode) throws SQLException;
	
/**
 * add by fyyang 090929
 * @param ids
 * @param enterpriseCode
 * @param workCode
 * @throws Exception
 */
	public void updateDeptBanzu(String ids ,String enterpriseCode,String workCode) throws Exception;
	
	/**
	 * add by fyyang 090929
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject findAllBanzuDepts(String enterpriseCode);
	
	/**
	 * add by liuyi 090921
	 * @param pdeptId 父部门id
	 * @param needCheck 是否检查，若yes则将班组不显示
	 * @return
	 */
	public List<HrCDept> getListByParentId(Long pdeptId,String needCheck);
	//  查询部门并过滤掉老部门
	public List<HrCDept> getByParentId(Long pdeptId,String needCheck,String flag);//add by wpzhu 20100707
	
	/**
	 * add by liuyi 090921
	 * 查找具体部门下的班组
	 * @param deptId
	 * @return
	 */
	public List getBanzuByDept(Long deptId);
	
	/**
	 * add by liuyi 20100610
	 * 通过部门名称获得该部门的部门id
	 * @param deptName
	 * @return
	 */
	Long getDeptIdByDeptName(String deptName);
	
	/**
	 * 根据部门编码或Id查询一级部门信息
	 * @param deptCodeOrIdOrWorkerCode 部门编码或id或员工编号
	 * @param flag 1--部门编码 2---部门id 3---员工编号
	 * @param enterpriseCode 企业编码
	 * @return HrCDept
	 * add by kzhang 20100928
	 */
	public HrCDept getFirstLevelDept(String deptCodeOrIdOrWorkerCode,String flag,String enterpriseCode);
	
}