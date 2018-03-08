package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;

/**
 * Remote interface for HrJBorrowcontractFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJBorrowcontractFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJBorrowcontract entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJBorrowcontract entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJBorrowcontract entity);
	/**
	 * 
	 * @param entity
	 */
	public long save2(HrJBorrowcontract entity);
	/**
	 * Delete a persistent HrJBorrowcontract entity.
	 * 
	 * @param entity
	 *            HrJBorrowcontract entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJBorrowcontract entity);

	/**
	 * Persist a previously saved HrJBorrowcontract entity and return it or a
	 * copy of it to the sender. A copy of the HrJBorrowcontract entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJBorrowcontract entity to update
	 * @return HrJBorrowcontract the persisted HrJBorrowcontract entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJBorrowcontract update(HrJBorrowcontract entity);
	/**
	 * 
	 * @param entity
	 * @throws DataChangeException
	 * @throws SQLException
	 */
	public HrJBorrowcontract update2(HrJBorrowcontract entity)throws DataChangeException,SQLException;
	public HrJBorrowcontract findById(Long id);

	/**
	 * Find all HrJBorrowcontract entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJBorrowcontract property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJBorrowcontract> found by query
	 */
	public List<HrJBorrowcontract> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all HrJBorrowcontract entities.
	 * 
	 * @return List<HrJBorrowcontract> all HrJBorrowcontract entities
	 */
	public List<HrJBorrowcontract> findAll();
	/**
	 * 
	 * @param entity
	 */
	public void logicDelete(String strBorrowContractId,String user_id,String strOldUpdateTime)throws DataChangeException,SQLException;
	/**
	 * 
	 * @param strBorrowContractId
	 * @param user_id
	 * @param strOldUpdateTime
	 * @throws DataChangeException
	 * @throws SQLException
	 */
	
	public void repoet(String strBorrowContractId,String user_id,String strOldUpdateTime)
    throws  DataChangeException ,SQLException;
}