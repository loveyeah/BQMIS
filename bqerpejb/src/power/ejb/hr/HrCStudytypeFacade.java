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
 * Facade for entity HrCStudytype.
 *
 * @see power.ejb.hr.HrCStudytype
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCStudytypeFacade implements HrCStudytypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved HrCStudytype entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCStudytype entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCStudytype entity) {
		LogUtil.log("saving HrCStudytype instance", Level.INFO, null);
		try {
			// 设置ID
			entity.setStudyTypeCodeId(bll.getMaxId("HR_C_STUDYTYPE", "STUDY_TYPE_CODE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCStudytype entity.
	 *
	 * @param entity
	 *            HrCStudytype entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCStudytype entity) {
		LogUtil.log("deleting HrCStudytype instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCStudytype.class, entity
					.getStudyTypeCodeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCStudytype entity and return it or a copy of
	 * it to the sender. A copy of the HrCStudytype entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCStudytype entity to update
	 * @return HrCStudytype the persisted HrCStudytype entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCStudytype update(HrCStudytype entity) {
		LogUtil.log("updating HrCStudytype instance", Level.INFO, null);
		try {
			HrCStudytype result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCStudytype findById(Long id) {
		LogUtil.log("finding HrCStudytype instance with id: " + id, Level.INFO,
				null);
		try {
			HrCStudytype instance = entityManager.find(HrCStudytype.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCStudytype entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCStudytype property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCStudytype> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCStudytype> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCStudytype instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCStudytype model where model."
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
	 * Find all HrCStudytype entities.
	 *
	 * @return List<HrCStudytype> all HrCStudytype entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCStudytype> findAll() {
		LogUtil.log("finding all HrCStudytype instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCStudytype model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 查找所有学习类别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllStudytypes(String enterpriseCode){
		try{
			String sql = "SELECT L.STUDY_TYPE_CODE_ID ,L.STUDY_TYPE FROM HR_C_STUDYTYPE L \n" +
					" WHERE L.IS_USE = 'Y' AND L.ENTERPRISE_CODE = '" + enterpriseCode +"'";
			LogUtil.log("所有学习类别id和名称开始。SQL=" + sql, Level.INFO, null);
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
			LogUtil.log("查找所有学习类别id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有学习类别id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}

	public Long getStudytypeIdByName(String name, String enterpriseCode) {
		Long id = null;
		String sql = 
			"select a.study_type_code_id\n" +
			"  from hr_c_studytype a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.study_type='"+name+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			id = Long.parseLong(obj.toString());

		return id;
	}
}