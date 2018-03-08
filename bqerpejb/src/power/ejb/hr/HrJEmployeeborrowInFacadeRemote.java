package power.ejb.hr;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.DataChangeException;

/**
 * Remote interface for HrJEmployeeborrowInFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJEmployeeborrowInFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJEmployeeborrowIn
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrJEmployeeborrowIn entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJEmployeeborrowIn entity) throws SQLException;

	/**
	 * 批处理中增加一组数据
	 * @param empIds
	 * @param entity
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String batchAddData(String empIds,HrJEmployeeborrowIn entity )throws SQLException;
	
	/**
	 *  批处理更新员工借调登记
	 * @param borrowInArray
	 * @param endDate
	 * @param workCode
	 * @throws DataChangeException 
	 * @throws DataFormatException 
	 * @throws SQLException 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void batchUpdateData(String borrowInArray,String  endDate,String workCode) throws DataFormatException, DataChangeException, SQLException;
	/**
	 * Delete a persistent HrJEmployeeborrowIn entity.
	 * 
	 * @param entity
	 *            HrJEmployeeborrowIn entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJEmployeeborrowIn entity)throws SQLException, DataChangeException;

	/**
	 * 上报员工借调登记
	 * @param entity
	 * @throws SQLException
	 * @throws DataChangeException
	 */
	public void report(HrJEmployeeborrowIn entity)throws SQLException, DataChangeException;
	/**
	 * Persist a previously saved HrJEmployeeborrowIn entity and return it or a
	 * copy of it to the sender. A copy of the HrJEmployeeborrowIn entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJEmployeeborrowIn entity to update
	 * @return HrJEmployeeborrowIn the persisted HrJEmployeeborrowIn entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public void update(HrJEmployeeborrowIn entity) throws SQLException, DataChangeException;

	public HrJEmployeeborrowIn findById(Long id);
	
	/**
	 * check所选员工是否已借调出
	 * @param empId
	 * @param enterpriseCode
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkIfBackByEmpId(String empId,String enterpriseCode) throws SQLException;

	/**
	 * Find all HrJEmployeeborrowIn entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJEmployeeborrowIn property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJEmployeeborrowIn> found by query
	 */
	public List<HrJEmployeeborrowIn> findByProperty(String propertyName,
			Object value);

	public List<HrJEmployeeborrowIn> findByEmpId(Object empId);

	public List<HrJEmployeeborrowIn> findByIfBack(Object ifBack);

	public List<HrJEmployeeborrowIn> findByMemo(Object memo);

	public List<HrJEmployeeborrowIn> findByBorrowDeptId(Object borrowDeptId);

	public List<HrJEmployeeborrowIn> findByEnterpriseCode(Object enterpriseCode);

	public List<HrJEmployeeborrowIn> findByIsUse(Object isUse);

	public List<HrJEmployeeborrowIn> findByLastModifiedBy(Object lastModifiedBy);

	public List<HrJEmployeeborrowIn> findByDcmStatus(Object dcmStatus);

	/**
	 * Find all HrJEmployeeborrowIn entities.
	 * 
	 * @return List<HrJEmployeeborrowIn> all HrJEmployeeborrowIn entities
	 */
	public List<HrJEmployeeborrowIn> findAll();
}