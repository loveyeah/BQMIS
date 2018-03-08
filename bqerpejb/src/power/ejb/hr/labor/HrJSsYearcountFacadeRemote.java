package power.ejb.hr.labor;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 企业年金明细信息
 * 
 * @author fyyang 20100605
 */
@Remote
public interface HrJSsYearcountFacadeRemote {

	/**
	 * 保存企业年金信息
	 * 
	 * @param entity
	 * @return
	 */
	public HrJSsYearcount save(HrJSsYearcount entity);

	/**
	 * 删除企业年金信息
	 * 
	 * @param ids
	 */
	public void delete(String ids);

	/**
	 * 修改企业年金信息
	 * 
	 * @param entity
	 * @return
	 */
	public HrJSsYearcount update(HrJSsYearcount entity);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public HrJSsYearcount findById(Long id);

	/**
	 * 保存或修改企业年金信息
	 * 
	 * @param mainModel
	 * @param yearCountList
	 */
	public void saveOrUpdateYearcountInfo(HrCSsMain mainModel,
			List<HrJSsYearcount> yearCountList);

	/**
	 * 查询企业年金信息
	 * 
	 * @param strYear
	 * @param yearType
	 * @param insuranceType
	 * @param deptName
	 * @param workName
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findYearcountList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode, final int... rowStartIdxAndCount);

	/**
	 * 全部删除企业年金信息
	 * 
	 * @param strYear
	 * @param yearType
	 * @param insuranceType
	 * @param deptName
	 * @param workName
	 * @param enterpriseCode
	 * @return
	 */
	public void delAllYearcountList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode);
}