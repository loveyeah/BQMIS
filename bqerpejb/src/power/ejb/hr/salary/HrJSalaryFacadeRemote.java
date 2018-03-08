package power.ejb.hr.salary;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.salary.form.SalaryAmountForm;

/**
 * 薪酬主表
 * @author liuyi 090928
 */
@Remote
public interface HrJSalaryFacadeRemote {
	/**
	 * 新增一条薪酬主表数据
	 */
	public HrJSalary save(HrJSalary entity);

	/**
	 *删除一条薪酬主表数据
	 */
	public void delete(HrJSalary entity);

	/**
	 * 更新一条薪酬主表数据
	 */
	public HrJSalary update(HrJSalary entity);

	public HrJSalary findById(Long id);

	
	
	public List<HrJSalary> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 通过部门id,月份判断数据库中是否已存在数据
	 * @param deptId
	 * @param month
	 * @return true:存在，false:不存在
	 */
	public boolean judgetSalaryExist(Long deptId,String month,String enterpriseCode);
	
	/**
	 * 通过部门id查找在该部门下的所有在册人员
	 * @param deptId
	 * @param emterpriseCode
	 * @return
	 */
	public List<HrJEmpInfo> findEmpByDeptId(Long deptId,String enterpriseCode);
	
	/**
	 * 保存薪酬数据
	 * @param method add：增加，update:修改
	 * @param arrlist
	 */
	public void saveSalaryAmountRec(String method,List<SalaryAmountForm> arrlist);
	
	/**
	 * 查询工资列表
	 * add by fyyang 090928
	 * @param strMonth 2009-09
	 * @param deptId
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject findSalayByDept(String strMonth,Long deptId,String enterpriseCode);
}