package power.ejb.hr.salary;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * @author liuyi 20100208
 */
@Remote
public interface HrJSalaryWageFacadeRemote {
	
	/**
	 * 计算工资 月奖 大奖
	 * @param flag 1：工资, 2：月奖, 3：大奖 
	 * @param deptId 部门
	 * @param yearMonth  月份
	 * @param enterpriseCode
	 */
	void calculateSalary(String flag,String deptId,String yearMonth,String enterpriseCode) throws Exception;
	
	/**
	 * 查询工资 奖金 大奖 
	 * @param deptId
	 * @param yearMonth
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	PageObject getBasicPrimiumAndAward(String deptId,String yearMonth,String enterpriseCode,int...rowStartIdxAndCount);
	//add by ypan 20100730
	PageObject getSalary(String startDate,String endDate,int...rowStartIdxAndCount);
	
	
	void saveBasicPrimiumAndAward(List<HrJSalaryWage> updateList);
	/**
	 * 判断历史工资不可修改 add by sychen 20100805
	 * @param deptId
	 * @param enterpriseCode
	 * @return
	 */
	public String checkOldSalaryModify(String deptId,String enterpriseCode);
	
	
	/**根据月份查询个人月度收入情况 add by wpzhu 
	 * @param month
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getSalaryByMonth(String flag,Long empId,String month ,String enterpriseCode,final int...rowStartIdxAndCount);
	
	public List getWageDetail(String id,String month,int... rowStartIdxAndCount);
}