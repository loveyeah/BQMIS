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
 * Facade for entity HrCSpecialty.
 *
 * @see power.ejb.hr.HrCSpecialty
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCSpecialtyFacade implements HrCSpecialtyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved HrCSpecialty entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCSpecialty entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCSpecialty entity) {
		LogUtil.log("saving HrCSpecialty instance", Level.INFO, null);
		try {
			// 设置ID
			entity.setSpecialtyCodeId(bll.getMaxId("HR_C_SPECIALTY", "SPECIALTY_CODE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCSpecialty entity.
	 *
	 * @param entity
	 *            HrCSpecialty entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCSpecialty entity) {
		LogUtil.log("deleting HrCSpecialty instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCSpecialty.class, entity
					.getSpecialtyCodeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCSpecialty entity and return it or a copy of
	 * it to the sender. A copy of the HrCSpecialty entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCSpecialty entity to update
	 * @return HrCSpecialty the persisted HrCSpecialty entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCSpecialty update(HrCSpecialty entity) {
		LogUtil.log("updating HrCSpecialty instance", Level.INFO, null);
		try {
			HrCSpecialty result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCSpecialty findById(Long id) {
		LogUtil.log("finding HrCSpecialty instance with id: " + id, Level.INFO,
				null);
		try {
			HrCSpecialty instance = entityManager.find(HrCSpecialty.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCSpecialty entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCSpecialty property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCSpecialty> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCSpecialty> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCSpecialty instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCSpecialty model where model."
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
	 * Find all HrCSpecialty entities.
	 *
	 * @return List<HrCSpecialty> all HrCSpecialty entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCSpecialty> findAll() {
		LogUtil.log("finding all HrCSpecialty instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCSpecialty model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 查找所有学习专业
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllSpecialtys(String enterpriseCode){
		try{
			String sql = "SELECT L.SPECIALTY_CODE_ID ,L.SPECIALTY_NAME \n" +
					" FROM HR_C_SPECIALTY L \n" +
					" WHERE L.IS_USE = 'Y' AND L.ENTERPRISE_CODE = '" + enterpriseCode +"'";
			LogUtil.log("所有学习专业id和名称开始。SQL=" + sql, Level.INFO, null);
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
			LogUtil.log("查找所有学习专业id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有学习专业id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}

	public Long getSpecialyIdByName(String name, String enterpriseCode) {
		Long specialyId = null;
		String sql = 
			"select a.specialty_code_id\n" +
			"  from hr_c_specialty a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.specialty_name = '"+name+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			specialyId = Long.parseLong(obj.toString());

		return specialyId;
	}
}