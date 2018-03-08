package power.ejb.hr.labor;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 养老失业保险信息
 * 
 * @author fyyang 20100605
 */
@Remote
public interface HrJSsOldageFacadeRemote {

	/**
	 * 增加养老失业保险信息
	 * 
	 * @param entity
	 * @return
	 */
	public HrJSsOldage save(HrJSsOldage entity);

	/**
	 * 根据条件查询社保管路主表有没有此条记录
	 * 
	 * @param strYear
	 * @param yearType
	 * @param insuranceType
	 * @return
	 */
	public Long findMainID(String strYear, String yearType, String insuranceType);

	/**
	 * 删除养老失业保险信息
	 * 
	 * @param entity
	 */
	public void delete(String ids);

	/**
	 * 修改养老失业保险信息
	 * 
	 * @param entity
	 * @return
	 */
	public HrJSsOldage update(HrJSsOldage entity);

	/**
	 * 查找养老失业保险信息
	 * 
	 * @param id
	 * @return
	 */
	public HrJSsOldage findById(Long id);

	/**
	 * 增加或修改养老失业保险信息
	 * 
	 * @param mainModel
	 * @param oldageList
	 */
	public void saveOrUpdateOldageInfo(HrCSsMain mainModel,
			List<HrJSsOldage> oldageList);

	
	/**
	 * 查找养老失业保险信息列表
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
	public PageObject findOldageList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode, final int... rowStartIdxAndCount);

	/**
	 * 全部删除养老失业保险信息列表
	 * 
	 * @param strYear
	 * @param yearType
	 * @param insuranceType
	 * @param deptName
	 * @param workName
	 * @param enterpriseCode
	 * @return
	 */
	public void delAllOldageList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode);

}