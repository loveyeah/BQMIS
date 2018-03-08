package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;
import power.ear.comm.DataChangeException;

/**
 * Facade for entity AdJUserMenu.
 * 
 * @see power.ejb.administration.AdJUserMenu
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJUserMenuFacade implements AdJUserMenuFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved AdJUserMenu entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJUserMenu entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJUserMenu entity) {
		LogUtil.log("saving AdJUserMenu instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent AdJUserMenu entity.
	 * 
	 * @param entity
	 *            AdJUserMenu entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJUserMenu entity) {
		LogUtil.log("deleting AdJUserMenu instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJUserMenu.class, entity
					.getMId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AdJUserMenu entity and return it or a copy of
	 * it to the sender. A copy of the AdJUserMenu entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJUserMenu entity to update
	 * @return AdJUserMenu the persisted AdJUserMenu entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJUserMenu update(AdJUserMenu entity) {
		LogUtil.log("updating AdJUserMenu instance", Level.INFO, null);
		try {
			AdJUserMenu result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AdJUserMenu findById(Long id) {
		LogUtil.log("finding AdJUserMenu instance with id: " + id, Level.INFO,
				null);
		try {
			AdJUserMenu instance = entityManager.find(AdJUserMenu.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJUserMenu entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJUserMenu property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJUserMenu> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJUserMenu> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJUserMenu instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJUserMenu model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJUserMenu entities.
	 * 
	 * @return List<AdJUserMenu> all AdJUserMenu entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJUserMenu> findAll() {
		LogUtil.log("finding all AdJUserMenu instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJUserMenu model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 退回/审核处理
	 * @throws SQLException
	 * @throws DataChangeException 
	 */
	public void backUpdate(AdJUserMenu entity, String oldUpdateTime) throws SQLException, DataChangeException{
		// log信息
		String logInfo = "";
		// 订餐状态0or2
		String menuInfo = entity.getMenuInfo();
		if("1".equals(menuInfo)){
			logInfo = "退回";
		}
		if("3".equals(menuInfo)){
			logInfo = "审核";
		}
		LogUtil.log("EJB:" + logInfo + "处理开始。", Level.INFO, null);
		try {
			// 排他：
			// 通过id取得上次更新时间
			AdJUserMenu oldBeen = findById(entity.getMId());
			String strOldTime = oldBeen.getUpdateTime().toString();
			// 比较更新时间是否一致
			if(!strOldTime.substring(0,19).equals(oldUpdateTime)){
				throw new DataChangeException(null);
			}
			
			// 更新
			update(entity);
			LogUtil.log("EJB:" + logInfo + "处理开始。", Level.INFO, null);

		} catch (DataChangeException e) {
			LogUtil.log("EJB:" + logInfo + "处理开始。", Level.INFO, e);
			throw e;
		} catch(RuntimeException sqlE){
			LogUtil.log("EJB:接待审批查询失败。", Level.SEVERE, sqlE);
			throw new SQLException();
		}
	}
}