package power.ejb.hr.labor;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 医疗保险明细信息
 * 
 * @author fyyang 20100605
 */
@Remote
public interface HrJSsHealthFacadeRemote {

	/**
	 * 保存医疗保险明细信息
	 * 
	 * @param entity
	 * @return
	 */
	public HrJSsHealth save(HrJSsHealth entity);

	/**
	 * 删除医疗保险明细信息
	 * 
	 * @param ids
	 */
	public void delete(String ids);

	/**
	 * 修改医疗保险明细信息
	 * 
	 * @param entity
	 * @return
	 */
	public HrJSsHealth update(HrJSsHealth entity);

	/**
	 * 查找医疗保险明细信息
	 * 
	 * @param id
	 * @return
	 */
	public HrJSsHealth findById(Long id);

	/**
	 * 保存或修改医疗保险明细信息
	 * 
	 * @param mainModel
	 * @param healthList
	 */
	public void saveOrUpdateHealthInfo(HrCSsMain mainModel,
			List<HrJSsHealth> healthList);

	/**
	 * 查询医疗保险明细信息列表
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
	
	public PageObject findHealthList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode, final int... rowStartIdxAndCount);

	/**
	 * 全部删除医疗保险信息
	 * 
	 * @param strYear
	 * @param yearType
	 * @param insuranceType
	 * @param deptName
	 * @param workName
	 * @param enterpriseCode
	 * @return
	 */
	public void delAllHealthList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode);
}