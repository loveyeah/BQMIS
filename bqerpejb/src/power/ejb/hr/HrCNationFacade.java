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
 * Facade for entity HrCNation.
 *
 * @see power.ejb.hr.HrCNation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCNationFacade implements HrCNationFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved HrCNation entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCNation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCNation entity) {
		LogUtil.log("saving HrCNation instance", Level.INFO, null);
		try {
			// 设置ID
			entity.setNationCodeId(bll.getMaxId("HR_C_NATION", "NATION_CODE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCNation entity.
	 *
	 * @param entity
	 *            HrCNation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCNation entity) {
		LogUtil.log("deleting HrCNation instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCNation.class, entity
					.getNationCodeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCNation entity and return it or a copy of it
	 * to the sender. A copy of the HrCNation entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCNation entity to update
	 * @return HrCNation the persisted HrCNation entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCNation update(HrCNation entity) {
		LogUtil.log("updating HrCNation instance", Level.INFO, null);
		try {
			HrCNation result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCNation findById(Long id) {
		LogUtil.log("finding HrCNation instance with id: " + id, Level.INFO,
				null);
		try {
			HrCNation instance = entityManager.find(HrCNation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCNation entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCNation property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCNation> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCNation> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCNation instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCNation model where model."
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
	 * Find all HrCNation entities.
	 *
	 * @return List<HrCNation> all HrCNation entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCNation> findAll() {
		LogUtil.log("finding all HrCNation instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCNation model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 查找所有民族
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllNations(String enterpriseCode){
		try{
			String sql = "SELECT N.NATION_CODE_ID,N.NATION_NAME FROM HR_C_NATION N \n" +
					" WHERE N.IS_USE = 'Y' AND N.ENTERPRISE_CODE = '" + enterpriseCode +"'";
			LogUtil.log("所有民族编码id和名称开始。SQL=" + sql, Level.INFO, null);
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
			LogUtil.log("查找所有民族编码id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有民族编码id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}

	public Long findNationIdByName(String nationName, String enterpriseCode) {
		Long nationId = null;
		String sql = "select a.nation_code_id from HR_C_NATION a where a.nation_name='"
				+ nationName
				+ "' and a.is_use='Y' and a.enterprise_code='"
				+ enterpriseCode + "' ";
		Object obj = bll.getSingal(sql);
		if(obj != null){
			nationId = Long.parseLong(obj.toString());
		}
		return nationId;
	}
}