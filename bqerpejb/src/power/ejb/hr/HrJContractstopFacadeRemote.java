package power.ejb.hr;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJContractstopFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJContractstopFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJContractstop entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJContractstop entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJContractstop entity);
	public  HrJContractstop findContractstop(Long workcontractId);//add by wpzhu 
	public  Long  findContractID(String workcontractCode);
	/**
	 * Delete a persistent HrJContractstop entity.
	 * 
	 * @param entity
	 *            HrJContractstop entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJContractstop entity);

	/**
	 * Persist a previously saved HrJContractstop entity and return it or a copy
	 * of it to the sender. A copy of the HrJContractstop entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJContractstop entity to update
	 * @return HrJContractstop the persisted HrJContractstop entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJContractstop update(HrJContractstop entity);

	public HrJContractstop findById(Long id);

	/**
	 * Find all HrJContractstop entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJContractstop property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJContractstop> found by query
	 */
	public List<HrJContractstop> findByProperty(String propertyName,
			Object value);

	public List<HrJContractstop> findByWorkcontractid(Object workcontractid);

	public List<HrJContractstop> findByContractStopType(Object contractStopType);

	public List<HrJContractstop> findByReleaseReason(Object releaseReason);

	public List<HrJContractstop> findByContractTermCode(Object contractTermCode);

	public List<HrJContractstop> findByDossierDirection(Object dossierDirection);

	public List<HrJContractstop> findBySocietyInsuranceDirection(
			Object societyInsuranceDirection);

	public List<HrJContractstop> findByMemo(Object memo);

	public List<HrJContractstop> findByInsertby(Object insertby);

	public List<HrJContractstop> findByEnterpriseCode(Object enterpriseCode);

	public List<HrJContractstop> findByIsUse(Object isUse);

	public List<HrJContractstop> findByLastModifiedBy(Object lastModifiedBy);

	public List<HrJContractstop> findByEmpId(Object empId);

	public List<HrJContractstop> findByDeptId(Object deptId);

	public List<HrJContractstop> findByStationId(Object stationId);

	/**
	 * Find all HrJContractstop entities.
	 * 
	 * @return List<HrJContractstop> all HrJContractstop entities
	 */
	public List<HrJContractstop> findAll();
	
	/**
	 * 按合同Id和是否使用查找
	 */
	public List<HrJContractstop> findByIdIsUse(Long ContractId);
	public void contractEnd(HrJContractstop entity1,HrJWorkcontract entity2)throws DataChangeException;
	public PageObject searchEmpContract(Long empId);
}