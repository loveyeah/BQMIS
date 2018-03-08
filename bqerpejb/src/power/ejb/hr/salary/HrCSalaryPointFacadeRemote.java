package power.ejb.hr.salary;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCSalaryPointFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCSalaryPointFacadeRemote {

	public void save(HrCSalaryPoint entity);

	/**
	 * 删除一条或多条记录
	 * 
	 * add by drdu 090929
	 * @param ids
	 */
	public void deleteMulti(String ids);

	public HrCSalaryPoint update(HrCSalaryPoint entity);

	public HrCSalaryPoint findById(Long id);

	public PageObject findAll(String sDate,String enterprisecode,int... rowStartIdxAndCount);
}