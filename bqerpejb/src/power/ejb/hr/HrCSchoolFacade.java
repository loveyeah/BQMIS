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
 * Facade for entity HrCSchool.
 *
 * @see power.ejb.hr.HrCSchool
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCSchoolFacade implements HrCSchoolFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved HrCSchool entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCSchool entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCSchool entity) {
		LogUtil.log("saving HrCSchool instance", Level.INFO, null);
		try {
			// 设置ID
			entity.setSchoolCodeId(bll.getMaxId("HR_C_SCHOOL", "SCHOOL_CODE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCSchool entity.
	 *
	 * @param entity
	 *            HrCSchool entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCSchool entity) {
		LogUtil.log("deleting HrCSchool instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCSchool.class, entity
					.getSchoolCodeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCSchool entity and return it or a copy of it
	 * to the sender. A copy of the HrCSchool entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCSchool entity to update
	 * @return HrCSchool the persisted HrCSchool entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCSchool update(HrCSchool entity) {
		LogUtil.log("updating HrCSchool instance", Level.INFO, null);
		try {
			HrCSchool result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCSchool findById(Long id) {
		LogUtil.log("finding HrCSchool instance with id: " + id, Level.INFO,
				null);
		try {
			HrCSchool instance = entityManager.find(HrCSchool.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCSchool entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCSchool property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCSchool> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCSchool> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCSchool instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCSchool model where model."
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
	 * Find all HrCSchool entities.
	 *
	 * @return List<HrCSchool> all HrCSchool entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCSchool> findAll() {
		LogUtil.log("finding all HrCSchool instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCSchool model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 查找所有学校
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllSchools(String enterpriseCode){
		try{
			String sql = "SELECT L.SCHOOL_CODE_ID ,L.SCHOOL_NAME  FROM HR_C_SCHOOL L \n" +
					" WHERE L.IS_USE = 'Y' AND L.ENTERPRISE_CODE = '" + enterpriseCode +"'";
			LogUtil.log("所有学校id和名称开始。SQL=" + sql, Level.INFO, null);
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
			LogUtil.log("查找所有学校id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有学校id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}

	public Long getSchoolCodeIdByName(String name, String enterpriseCode) {
		Long schoolCodeId = null;
		String sql = 
			"select a.school_code_id\n" +
			"  from hr_c_school a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.school_name = '"+name+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			schoolCodeId = Long.parseLong(obj.toString());
		return schoolCodeId;
	}
}