package power.ejb.manage.exam;

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
import power.ejb.manage.exam.form.BpJCbmExecutionDepForm;

/**
 * Facade for entity BpJCbmAwardDetail.
 * 
 * @see power.ejb.manage.exam.BpJCbmAwardDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJCbmAwardDetailFacade implements BpJCbmAwardDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved BpJCbmAwardDetail entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJCbmAwardDetail entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpJCbmAwardDetail entity) {
		try {
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpJCbmAwardDetail entity.
	 * 
	 * @param entity
	 *            BpJCbmAwardDetail entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJCbmAwardDetail entity) {
		LogUtil.log("deleting BpJCbmAwardDetail instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJCbmAwardDetail.class, entity
					.getDeclareDetailId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpJCbmAwardDetail entity and return it or a
	 * copy of it to the sender. A copy of the BpJCbmAwardDetail entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJCbmAwardDetail entity to update
	 * @return BpJCbmAwardDetail the persisted BpJCbmAwardDetail entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public boolean update(BpJCbmAwardDetail entity) {
		try {
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJCbmAwardDetail findById(Long id) {
		LogUtil.log("finding BpJCbmAwardDetail instance with id: " + id,
				Level.INFO, null);
		try {
			BpJCbmAwardDetail instance = entityManager.find(
					BpJCbmAwardDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpJCbmAwardDetail entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJCbmAwardDetail property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJCbmAwardDetail> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpJCbmAwardDetail> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpJCbmAwardDetail instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpJCbmAwardDetail model where model."
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
	 * Find all BpJCbmAwardDetail entities.
	 * 
	 * @return List<BpJCbmAwardDetail> all BpJCbmAwardDetail entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpJCbmAwardDetail> findAll() {
		LogUtil
				.log("finding all BpJCbmAwardDetail instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from BpJCbmAwardDetail model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getDeptCash(String datetime, String enterpriseCode) {
		PageObject obj = new PageObject();
		String sql = "SELECT t.*,\n"
				+ "       to_number(realvalue) / to_number(planvalue) pervalue\n"
				+ "  FROM (SELECT (SELECT a.item_code\n"
				+ "                  FROM bp_c_cbm_item a\n"
				+ "                 WHERE a.item_id = m.item_id\n"
				+ "                   AND a.enterprise_code = n.enterprise_code\n"
				+ "                   AND rownum = 1) itemcode,\n"
				+ "               (SELECT a.item_name\n"
				+ "                  FROM bp_c_cbm_item a\n"
				+ "                 WHERE a.item_id = m.item_id\n"
				+ "                   AND a.enterprise_code = n.enterprise_code\n"
				+ "                   AND rownum = 1) itemname,\n"
				+ "               (SELECT b.unit_name\n"
				+ "                  FROM bp_c_measure_unit b\n"
				+ "                 WHERE b.unit_id =\n"
				+ "                       (SELECT a.unit_id\n"
				+ "                          FROM bp_c_cbm_item a\n"
				+ "                         WHERE a.item_id = m.item_id\n"
				+ "                           AND a.enterprise_code = n.enterprise_code\n"
				+ "                           AND rownum = 1)\n"
				+ "                   AND b.enterprise_code = n.enterprise_code\n"
				+ "                   AND rownum = 1) unitname,\n"
				+ "               (SELECT SUM(c.branch_plan_value)\n"
				+ "                  FROM bp_j_cbm_execution c\n"
				+ "                 WHERE c.item_id = m.item_id\n"
				+ "                   AND c.year_month = n.MONTH\n"
				+ "                   AND c.belong_block IN ('1', '2')\n"
				+ "                   AND c.enterprise_code = n.enterprise_code\n"
				+ "                   AND rownum = 1) planvalue,\n"
				+ "               (SELECT SUM(c.real_value)\n"
				+ "                  FROM bp_j_cbm_execution c\n"
				+ "                 WHERE c.item_id = m.item_id\n"
				+ "                   AND c.year_month = n.MONTH\n"
				+ "                   AND c.belong_block IN ('1', '2')\n"
				+ "                   AND c.enterprise_code = n.enterprise_code\n"
				+ "                   AND rownum = 1) realvalue,\n"
				+ "               m.dep_code,\n"
				+ "               (SELECT de.dept_name\n"
				+ "                  FROM hr_c_dept de\n"
				+ "                 WHERE de.dept_code = m.dep_code\n"
				+ "                   AND de.enterprise_code = n.enterprise_code\n"
				+ "                   AND rownum = 1) deptname,\n"
				+ "               m.affiliated_level,\n"
				+ "               decode(m.affiliated_level, 1, '牵头部门', 2, '生产面责任部门', 3, '非生产面责任部门', 4, '生产面一级责任部门', 5, '生产面二级责任部门', 6, '非生产面一级责任部门', 7, '非生产面二级责任部门', '错误值') affiliated_name,\n"
				+ "               m.affiliated_value,\n"
				+ "               (SELECT ad.cash_bonus\n"
				+ "                  FROM bp_j_cbm_award_detail ad\n"
				+ "                 WHERE ad.declare_id = n.declare_id\n"
				+ "                   AND ad.affiliated_id = m.affiliated_id\n"
				+ "                   AND ad.enterprise_code = n.enterprise_code\n"
				+ "                   AND rownum = 1) cashvalue,\n"
				+ "               (SELECT ad.memo\n"
				+ "                  FROM bp_j_cbm_award_detail ad\n"
				+ "                 WHERE ad.declare_id = n.declare_id\n"
				+ "                   AND ad.affiliated_id = m.affiliated_id\n"
				+ "                   AND ad.enterprise_code = n.enterprise_code\n"
				+ "                   AND rownum = 1) memo,\n"
				+ "               (SELECT ad.DECLARE_DETAIL_ID\n"
				+ "                  FROM bp_j_cbm_award_detail ad\n"
				+ "                 WHERE ad.declare_id = n.declare_id\n"
				+ "                   AND ad.affiliated_id = m.affiliated_id\n"
				+ "                   AND ad.enterprise_code = n.enterprise_code\n"
				+ "                   AND rownum = 1) id,\n"
				+ "               n.declare_id,\n"
				+ "               m.affiliated_id\n"
				+ "          FROM bp_c_cbm_affiliated m,\n"
				+ "               bp_j_cbm_award_main n\n"
				+ "         WHERE n.MONTH = '" + datetime + "'\n"
				+ "           AND n.enterprise_code = '" + enterpriseCode
				+ "') t\n" + " ORDER BY t.itemcode,\n" + "          dep_code";

		List list = bll.queryByNativeSQL(sql);
		List arraylist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			BpJCbmExecutionDepForm model = new BpJCbmExecutionDepForm();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				model.setItemcode(data[0].toString());
			if (data[1] != null)
				model.setItemname(data[1].toString());
			if (data[2] != null)
				model.setUnitname(data[2].toString());
			if (data[3] != null)
				model.setPlanvalue(data[3].toString());
			if (data[4] != null)
				model.setRealvalue(data[4].toString());
			if (data[5] != null)
				model.setDeptcode(data[5].toString());
			if (data[6] != null)
				model.setDeptname(data[6].toString());
			if (data[7] != null)
				model.setAfflevel(data[7].toString());
			if (data[8] != null)
				model.setAffname(data[8].toString());
			if (data[9] != null)
				model.setAffvalue(data[9].toString());
			if (data[10] != null)
				model.setCashvalue(data[10].toString());
			if (data[11] != null)
				model.setMemo(data[11].toString());
			if (data[12] != null)
				model.setAffjid(data[12].toString());
			if (data[13] != null)
				model.setDid(data[13].toString());
			if (data[14] != null)
				model.setAffid(data[14].toString());
			if (data[15] != null)
				model.setPervalue(data[15].toString());
			arraylist.add(model);
		}
		obj.setList(list);
		return obj;
	}

	public boolean saveDeptCash(List<BpJCbmAwardDetail> addlist,
			List<BpJCbmAwardDetail> updatelist, String enterpriseCode) {
		try {
			Long id = bll
					.getMaxId("BP_J_CBM_AWARD_DETAIL", "DECLARE_DETAIL_ID");
			for (BpJCbmAwardDetail model : addlist) {
				model.setIsUse("Y");
				model.setEnterpriseCode("hfdc");
				model.setAffiliatedId(id);
				this.save(model);
				id++;
			}
			for (BpJCbmAwardDetail model : updatelist) {
				model.setIsUse("Y");
				model.setEnterpriseCode("hfdc");
				this.update(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}