package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdJMeetfileFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJMeetfileFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJMeetfile entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJMeetfile entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJMeetfile entity) throws SQLException;

	/**
	 * Delete a persistent AdJMeetfile entity.
	 * 
	 * @param entity
	 *            AdJMeetfile entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJMeetfile entity);

	/**
	 * Persist a previously saved AdJMeetfile entity and return it or a copy of
	 * it to the sender. A copy of the AdJMeetfile entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJMeetfile entity to update
	 * @return AdJMeetfile the persisted AdJMeetfile entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJMeetfile update(AdJMeetfile entity) throws SQLException, DataChangeException;

	public AdJMeetfile findById(Long id);

	/**
	 * Find all AdJMeetfile entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJMeetfile property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJMeetfile> found by query
	 */
	public List<AdJMeetfile> findByProperty(String propertyName, Object value);

	public List<AdJMeetfile> findByMeetId(Object meetId);

	public List<AdJMeetfile> findByFileType(Object fileType);

	public List<AdJMeetfile> findByFileKind(Object fileKind);

	public List<AdJMeetfile> findByFileName(Object fileName);

	public List<AdJMeetfile> findByIsUse(Object isUse);

	public List<AdJMeetfile> findByUpdateUser(Object updateUser);

	/**
	 * Find all AdJMeetfile entities.
	 * 
	 * @return List<AdJMeetfile> all AdJMeetfile entities
	 */
	public List<AdJMeetfile> findAll();
	
	/**
	 * find by id 且 is_use为"Y"
	 * @param id 附件ID
	 * @return AdJMeetfile 附件信息
	 */
	public AdJMeetfile myFindById(Long id);
	/**
	 * find by 会务id 且 is_use为"Y"
	 * @param meetIdid 会务ID
	 * @return List<AdJMeetfile> 会务信息List
	 */
	public PageObject myFindByMeetId(String meetId, final int...rowStartIdxAndCount);
	/**
	 * 得到会务附件表里的最大流水号（用于插入数据）
	 * @return Long id 会务附件的最大流水号
	 */
	public Long myGetMaxId();
}