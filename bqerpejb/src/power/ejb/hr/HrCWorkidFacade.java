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
 * Facade for entity HrCWorkid.
 *
 * @see power.ejb.hr.HrCWorkid
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCWorkidFacade implements HrCWorkidFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved HrCWorkid entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCWorkid entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCWorkid entity) {
		LogUtil.log("saving HrCWorkid instance", Level.INFO, null);
		try {
			// 设置ID
			entity.setWorkId(bll.getMaxId("HR_C_WORKID", "WORK_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCWorkid entity.
	 *
	 * @param entity
	 *            HrCWorkid entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCWorkid entity) {
		LogUtil.log("deleting HrCWorkid instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCWorkid.class, entity
					.getWorkId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCWorkid entity and return it or a copy of it
	 * to the sender. A copy of the HrCWorkid entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCWorkid entity to update
	 * @return HrCWorkid the persisted HrCWorkid entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCWorkid update(HrCWorkid entity) {
		LogUtil.log("updating HrCWorkid instance", Level.INFO, null);
		try {
			HrCWorkid result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCWorkid findById(Long id) {
		LogUtil.log("finding HrCWorkid instance with id: " + id, Level.INFO,
				null);
		try {
			HrCWorkid instance = entityManager.find(HrCWorkid.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCWorkid entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCWorkid property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCWorkid> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCWorkid> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCWorkid instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCWorkid model where model."
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
	 * Find all HrCWorkid entities.
	 *
	 * @return List<HrCWorkid> all HrCWorkid entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCWorkid> findAll() {
		LogUtil.log("finding all HrCWorkid instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCWorkid model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 查找所有员工身份
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllWorkIds(String enterpriseCode){
		try{
			String sql = "SELECT W.WORK_ID,W.WORK_NAME FROM HR_C_WORKID W \n" +
					" WHERE W.IS_USE = 'Y'  AND W.ENTERPRISE_CODE = '" + enterpriseCode +"'";
			LogUtil.log("所有员工身份id和名称开始。SQL=" + sql, Level.INFO, null);
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
			LogUtil.log("查找所有员工身份id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有员工身份id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}

	public Long getWorkIdByWorkName(String name) {
		Long workId = null;
		String sql = "select t.work_id from hr_c_workid t where t.is_use='Y' and t.work_name='"+name+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			workId = Long.parseLong(obj.toString());
		return workId;
	}
}