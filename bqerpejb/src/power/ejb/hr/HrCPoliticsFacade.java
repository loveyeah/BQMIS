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
 * Facade for entity HrCPolitics.
 *
 * @see power.ejb.hr.HrCPolitics
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCPoliticsFacade implements HrCPoliticsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved HrCPolitics entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCPolitics entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCPolitics entity) {
		LogUtil.log("saving HrCPolitics instance", Level.INFO, null);
		try {
			// 设置ID
			entity.setPoliticsId(bll.getMaxId("HR_C_POLITICS", "POLITICS_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCPolitics entity.
	 *
	 * @param entity
	 *            HrCPolitics entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCPolitics entity) {
		LogUtil.log("deleting HrCPolitics instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCPolitics.class, entity
					.getPoliticsId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCPolitics entity and return it or a copy of
	 * it to the sender. A copy of the HrCPolitics entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCPolitics entity to update
	 * @return HrCPolitics the persisted HrCPolitics entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCPolitics update(HrCPolitics entity) {
		LogUtil.log("updating HrCPolitics instance", Level.INFO, null);
		try {
			HrCPolitics result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCPolitics findById(Long id) {
		LogUtil.log("finding HrCPolitics instance with id: " + id, Level.INFO,
				null);
		try {
			HrCPolitics instance = entityManager.find(HrCPolitics.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCPolitics entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCPolitics property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCPolitics> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCPolitics> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCPolitics instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCPolitics model where model."
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
	 * Find all HrCPolitics entities.
	 *
	 * @return List<HrCPolitics> all HrCPolitics entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCPolitics> findAll() {
		LogUtil.log("finding all HrCPolitics instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCPolitics model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 查找所有政治面貌
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllPolitics(String enterpriseCode){
		try{
			String sql = "SELECT P.POLITICS_ID,P.POLITICS_NAME FROM HR_C_POLITICS P \n" +
					" WHERE P.IS_USE = 'Y' AND P.ENTERPRISE_CODE = '" + enterpriseCode +"'";
			LogUtil.log("所有政治面貌id和名称开始。SQL=" + sql, Level.INFO, null);
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
			LogUtil.log("查找所有政治面貌id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有政治面貌id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}
}