package power.ejb.administration;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJRoomPrice.
 * 
 * @see power.ejb.administration.AdJRoomPrice
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJRoomPriceFacade implements AdJRoomPriceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved AdJRoomPrice entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJRoomPrice entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJRoomPrice entity) {
		LogUtil.log("saving AdJRoomPrice instance", Level.INFO, null);
		try {
			entity.setId(bll.getMaxId("AD_J_ROOM_PRICE", "ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent AdJRoomPrice entity.
	 * 
	 * @param entity
	 *            AdJRoomPrice entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJRoomPrice entity) {
		LogUtil.log("deleting AdJRoomPrice instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJRoomPrice.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AdJRoomPrice entity and return it or a copy of
	 * it to the sender. A copy of the AdJRoomPrice entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJRoomPrice entity to update
	 * @return AdJRoomPrice the persisted AdJRoomPrice entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJRoomPrice update(AdJRoomPrice entity) {
		LogUtil.log("updating AdJRoomPrice instance", Level.INFO, null);
		try {
			AdJRoomPrice result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AdJRoomPrice findById(Long id) {
		LogUtil.log("finding AdJRoomPrice instance with id: " + id, Level.INFO,
				null);
		try {
			AdJRoomPrice instance = entityManager.find(AdJRoomPrice.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJRoomPrice entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJRoomPrice property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJRoomPrice> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJRoomPrice> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJRoomPrice instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJRoomPrice model where model."
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
	 * Find all AdJRoomPrice entities.
	 * 
	 * @return List<AdJRoomPrice> all AdJRoomPrice entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJRoomPrice> findAll() {
		LogUtil.log("finding all AdJRoomPrice instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJRoomPrice model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 从房间类别编码查询宾馆房间价格维护
	 *
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return AdJRoomPrice 宾馆房间价格维护
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByRoomTypeCode(String roomTypeCode, final int... rowStartIdxAndCount)
	throws ParseException {
		LogUtil.log("查询宾馆房间价格维护实例", Level.INFO, null);
		AdJRoomPrice adjRoomPrice = new AdJRoomPrice();
		PageObject pobj = new PageObject();
		String sql = "Select ID," +
				"PRICE " +
				"FROM AD_J_ROOM_PRICE " +
				"WHERE IS_USE = ? " +
				"AND ROOM_TYPE_CODE = ?";
		Object[] params = new Object[2];
		params[0] = "Y";
		params[1] = roomTypeCode;
		List<AdJRoomPrice> list = bll.queryByNativeSQL(sql, params);
		List<AdJRoomPrice> lstAdJ = new ArrayList<AdJRoomPrice>();
		if (list != null) {
			Iterator it = list.iterator();
			while (it.hasNext()) { 
				Object[] data = (Object[]) it.next();
				// 序号
				if (data[0] != null) {
					adjRoomPrice.setId(Long.parseLong(data[0].toString()));
				}
				// 价格
				if (data[1] != null) {
					adjRoomPrice.setPrice(Double.parseDouble(data[1].toString()));
				}
				lstAdJ.add(adjRoomPrice);
			}
		}
		if(lstAdJ.size()>0)
		{
			// 符合条件的物资详细单
			pobj.setList(lstAdJ);
			// 符合条件的物资详细单的总数 
			pobj.setTotalCount(Long.parseLong(lstAdJ.size() + ""));
		}	
		return pobj;
		
	}
}