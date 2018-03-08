package power.ejb.hr;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJNewemployeeFacade.
 * 
 * @author drdu 20100618
 */
@Remote
public interface HrJNewemployeeFacadeRemote {

	/**
	 * 增加一条新进人员登记信息
	 * @param entity
	 * @return
	 */
	public HrJNewemployee save(HrJNewemployee entity);

	/**
	 * 删除一条或多条新进人员登记信息
	 * @param ids
	 */
	public void deleteMult(String ids);

	/**
	 * 修改一条新进人员登记信息
	 * @param entity
	 * @return
	 */
	public HrJNewemployee update(HrJNewemployee entity);

	/**
	 * 根据ID查找一条新进人员登记详细信息
	 * @param id
	 * @return
	 */
	public HrJNewemployee findById(Long id);
	
	/**
	 * 根据条件查询新进人员登记列表
	 * @param enterpriseCode
	 * @param year
	 * @param advicenoteNo
	 * @param dept
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findNewEmployeeList(String enterpriseCode,String year,String advicenoteNo,String dept,final int... rowStartIdxAndCount);
}