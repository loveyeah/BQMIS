package power.ejb.hr.salary;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.form.SalaryPersonalForm;

/**
 * Remote interface for HrCSalaryPersonalFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCSalaryPersonalFacadeRemote {

	/**
	 * 增加一条薪酬个人维护记录
	 * @param entity
	 * 
	 */
	public void save(HrCSalaryPersonal entity);

	public void delete(HrCSalaryPersonal entity);
	public  HrCSalaryPersonal  findPersonalRec(Long empId,Long deptId);
	public List<HrCSalaryPersonal> findPersonalById(Long empId);

	/**
	 * 修改一条薪酬个人维护记录
	 * @param entity
	 * @return
	 */
	public HrCSalaryPersonal update(HrCSalaryPersonal entity);

	/**
	 * 根据ID查找一条薪酬个人维护记录的详细
	 * @param id
	 * @return
	 */
	public HrCSalaryPersonal findById(Long id);
	
	/**
	 * 员工调整方法
	 * @param enterpriseCode
	 */
	public void empChangeRecord(String enterpriseCode);
	/**
	 * 运龄调整方法
	 * @param ids
	 */
	public void runAgeChangeRecord(String ids);
	/**
	 * 根据条件查找所有薪酬个人维护列表记录
	 * @param fuzzy
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	//modify by ypan 20100728
	public PageObject findSalaryPersonalList(String deptID,String fuzzy,String enterpriseCode,final int...rowStartIdxAndCount);
	
	/**
	 * 取岗位类别
	 * @param empCode
	 * @return
	 */
	public SalaryPersonalForm getRunStationList(String empCode);
	
	/**批量导入个人薪酬维护
	 * @param addOrUpdateList
	 */
	public  void insertSaralyPersonal(List<HrCSalaryPersonal> addOrUpdateList);//add by wpzhu 增加批量导入薪酬个人维护
	
	/**通过岗位名称找到岗位ID
	 * @param stationName
	 * @return
	 */
	public  Long  getIdByStationName(String stationName,String enterpriseCode);
	
	/**通过部门名称找到部门ID
	 * @param deptName
	 * @return
	 */
	public Long  getIdBydeptName(String  deptName,String enterpriseCode);
	
	

	
}