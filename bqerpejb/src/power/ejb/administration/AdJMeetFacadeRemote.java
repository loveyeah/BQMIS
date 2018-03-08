package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;

/**
 * Remote interface for AdJMeetFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJMeetFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJMeet entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJMeet entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJMeet entity) throws SQLException;

	/**
	 * Delete a persistent AdJMeet entity.
	 * 
	 * @param entity
	 *            AdJMeet entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJMeet entity);

	/**
	 * Persist a previously saved AdJMeet entity and return it or a copy of it
	 * to the sender. A copy of the AdJMeet entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJMeet entity to update
	 * @return AdJMeet the persisted AdJMeet entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJMeet update(AdJMeet entity) throws SQLException, DataChangeException;

	public AdJMeet findById(Long id);

	/**
	 * Find all AdJMeet entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJMeet property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJMeet> found by query
	 */
	public List<AdJMeet> findByProperty(String propertyName, Object value);

	public List<AdJMeet> findByMeetId(Object meetId);

	public List<AdJMeet> findByApplyMan(Object applyMan);

	public List<AdJMeet> findByMeetName(Object meetName);

	public List<AdJMeet> findByMeetPlace(Object meetPlace);

	public List<AdJMeet> findByRoomNeed(Object roomNeed);

	public List<AdJMeet> findByCigName(Object cigName);

	public List<AdJMeet> findByCigPrice(Object cigPrice);

	public List<AdJMeet> findByCigNum(Object cigNum);

	public List<AdJMeet> findByWineName(Object wineName);

	public List<AdJMeet> findByWinePrice(Object winePrice);

	public List<AdJMeet> findByWineNum(Object wineNum);

	public List<AdJMeet> findByTfNum(Object tfNum);

	public List<AdJMeet> findByTfThing(Object tfThing);

	public List<AdJMeet> findByDjNum(Object djNum);

	public List<AdJMeet> findByDjThing(Object djThing);

	public List<AdJMeet> findByBjNum(Object bjNum);

	public List<AdJMeet> findByBjThing(Object bjThing);

	public List<AdJMeet> findByDinnerNum(Object dinnerNum);

	public List<AdJMeet> findByDinnerBz(Object dinnerBz);

	public List<AdJMeet> findByBudpayInall(Object budpayInall);

	public List<AdJMeet> findByRealpayInall(Object realpayInall);

	public List<AdJMeet> findByMeetOther(Object meetOther);

	public List<AdJMeet> findByDcmStatus(Object dcmStatus);

	public List<AdJMeet> findByWorkFlowNo(Object workFlowNo);

	public List<AdJMeet> findByIsUse(Object isUse);

	public List<AdJMeet> findByUpdateUser(Object updateUser);

	/**
	 * Find all AdJMeet entities.
	 * 
	 * @return List<AdJMeet> all AdJMeet entities
	 */
	public List<AdJMeet> findAll();
	
	/**
	 * 根据会务单id查找记录
	 * @param meetId 会务单id
	 * @return AdJMeet 实体
	 */
	public AdJMeet myFindById(String meetId);
	/**
	 * 根据会务单id查找记录的最后更新时间
	 * @param meetId 会务单meetId
	 * @return AdJMeet 会务单的最后更新时间
	 */
	@SuppressWarnings("unchecked")
	public String myFindTimeById(String meetId);
	
	/**
	 * 得到会务表里的最大流水号（用于插入数据）
	 * @return Long id 会务单的最大流水号
	 */
	public Long myGetMaxId();
}