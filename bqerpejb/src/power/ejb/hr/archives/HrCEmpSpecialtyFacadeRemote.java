package power.ejb.hr.archives;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCEmpSpecialtyFacade.
 * 
 * @author drdu 20100610
 */
@Remote
public interface HrCEmpSpecialtyFacadeRemote {

	/**
	 * 增加一条人员特长记录
	 * @param entity
	 * @return
	 */
	public HrCEmpSpecialty save(HrCEmpSpecialty entity);

	/**
	 * 删除一条或多条人员特长记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改人员特长记录
	 * @param entity
	 * @return
	 */
	public HrCEmpSpecialty update(HrCEmpSpecialty entity);

	/**
	 * 根据ID查找一条人员特长记录详细
	 * @param id
	 * @return
	 */
	public HrCEmpSpecialty findById(Long id);

	/**
	 * 根据人员ID，企业编码查询列表
	 * @param empId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findSepecialtyList(String empId,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * 导入功能
	 * @param empList
	 */
	public void importEmpSpeFilesInfo(List<HrCEmpSpecialty> empList);
	
}