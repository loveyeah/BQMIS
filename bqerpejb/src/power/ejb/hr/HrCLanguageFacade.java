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

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * Facade for entity HrCLanguage.
 *
 * @see power.ejb.hr.HrCLanguage
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCLanguageFacade implements HrCLanguageFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved HrCLanguage entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCLanguage entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCLanguage entity) {
		LogUtil.log("saving HrCLanguage instance", Level.INFO, null);
		try {
			// 设置ID
			entity.setLanguageCodeId(bll.getMaxId("HR_C_LANGUAGE", "LANGUAGE_CODE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCLanguage entity.
	 *
	 * @param entity
	 *            HrCLanguage entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCLanguage entity) {
		LogUtil.log("deleting HrCLanguage instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCLanguage.class, entity
					.getLanguageCodeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCLanguage entity and return it or a copy of
	 * it to the sender. A copy of the HrCLanguage entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCLanguage entity to update
	 * @return HrCLanguage the persisted HrCLanguage entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCLanguage update(HrCLanguage entity) {
		LogUtil.log("updating HrCLanguage instance", Level.INFO, null);
		try {
			HrCLanguage result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCLanguage findById(Long id) {
		LogUtil.log("finding HrCLanguage instance with id: " + id, Level.INFO,
				null);
		try {
			HrCLanguage instance = entityManager.find(HrCLanguage.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCLanguage entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCLanguage property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCLanguage> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCLanguage> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCLanguage instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCLanguage model where model."
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
	 * Find all HrCLanguage entities.
	 *
	 * @return List<HrCLanguage> all HrCLanguage entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCLanguage> findAll() {
		LogUtil.log("finding all HrCLanguage instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCLanguage model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 查找所有语种
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllLanguages(String enterpriseCode){
		try{
			String sql = "SELECT L.LANGUAGE_CODE_ID,L.LANGUAGE_NAME \n" +
					" FROM HR_C_LANGUAGE L \n" +
					" WHERE L.IS_USE = 'Y' AND L.ENTERPRISE_CODE = '" + enterpriseCode +"'";
			LogUtil.log("所有语种id和名称开始。SQL=" + sql, Level.INFO, null);
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
			LogUtil.log("查找所有语种id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有语种id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}

}