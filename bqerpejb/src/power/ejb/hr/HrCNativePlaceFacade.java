package power.ejb.hr;

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
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * Facade for entity HrCNativePlace.
 *
 * @see power.ejb.hr.HrCNativePlace
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCNativePlaceFacade implements HrCNativePlaceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved HrCNativePlace entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCNativePlace entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCNativePlace entity) {
		LogUtil.log("saving HrCNativePlace instance", Level.INFO, null);
		try {
			// 设置ID
			entity.setNativePlaceId(bll.getMaxId("HR_C_NATIVE_PLACE", "NATIVE_PLACE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCNativePlace entity.
	 *
	 * @param entity
	 *            HrCNativePlace entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCNativePlace entity) {
		LogUtil.log("deleting HrCNativePlace instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCNativePlace.class, entity
					.getNativePlaceId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCNativePlace entity and return it or a copy
	 * of it to the sender. A copy of the HrCNativePlace entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 *
	 * @param entity
	 *            HrCNativePlace entity to update
	 * @return HrCNativePlace the persisted HrCNativePlace entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCNativePlace update(HrCNativePlace entity) {
		LogUtil.log("updating HrCNativePlace instance", Level.INFO, null);
		try {
			HrCNativePlace result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCNativePlace findById(Long id) {
		LogUtil.log("finding HrCNativePlace instance with id: " + id,
				Level.INFO, null);
		try {
			HrCNativePlace instance = entityManager.find(HrCNativePlace.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCNativePlace entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCNativePlace property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCNativePlace> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCNativePlace> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCNativePlace instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCNativePlace model where model."
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
	 * Find all HrCNativePlace entities.
	 *
	 * @return List<HrCNativePlace> all HrCNativePlace entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCNativePlace> findAll() {
		LogUtil.log("finding all HrCNativePlace instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCNativePlace model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 籍贯检索
	 * @param enterpriceCode 企业编码
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllNativePlace(String enterpriceCode){
		try{
			String sql = "SELECT P.NATIVE_PLACE_ID ,P.NATIVE_PLACE_NAME FROM HR_C_NATIVE_PLACE P \n" +
				"WHERE P.IS_USE = 'Y'  AND P.ENTERPRISE_CODE = '" + enterpriceCode + "'";
			LogUtil.log("所有籍贯编码id和名称开始。SQL=" + sql, Level.INFO, null);
			List list = bll.queryByNativeSQL(sql);
			List<DrpCommBeanInfo> arraylist = new ArrayList<DrpCommBeanInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DrpCommBeanInfo model = new DrpCommBeanInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setText(data[1].toString());
				}
				arraylist.add(model);
			}
			PageObject result = new PageObject();
			result.setList(arraylist);
			LogUtil.log("所有籍贯编码id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有籍贯编码id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}

	public Long getNativePlaceIdByName(String name) {
		Long nativePlaceId = null;
		String sql = "select a.native_place_id from hr_c_native_place a where a.is_use='Y' and a.native_place_name='"+name+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			nativePlaceId = Long.parseLong(obj.toString());
		return nativePlaceId;
	}
}