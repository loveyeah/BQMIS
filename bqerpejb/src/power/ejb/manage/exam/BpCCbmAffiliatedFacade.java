package power.ejb.manage.exam;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.exam.form.BpCCbmAffiliatedForm;

/**
 * Facade for entity BpCCbmAffiliated.
 * 
 * @see power.ejb.manage.exam.BpCCbmAffiliated
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCCbmAffiliatedFacade implements BpCCbmAffiliatedFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected static NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved BpCCbmAffiliated entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCCbmAffiliated entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(BpCCbmAffiliated entity){
		try {
			boolean flg = true;
			entity.setAffiliatedId(bll.getMaxId("bp_c_cbm_affiliated",
					"affiliated_id"));
			entity.setIsUse("Y");
			// try {
			if (judgeByAffiliatedLevel(entity.getItemId(), entity
					.getAffiliatedLevel(), entity.getAffiliatedId())){
				entityManager.persist(entity);
				flg = true;
			}else {
				flg = false;
			}
			// }
			return flg;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	private boolean judgeByAffiliatedLevel(Long itemId, String AffiliatedLevel,
			Long affiliatedId) {
		boolean flg = true;
		if (AffiliatedLevel != null && AffiliatedLevel.equals("1")) {
			flg = true;
		} else {
			String sql = "select count(1) from bp_c_cbm_affiliated t where t.item_id=' "
					+ itemId + "'";
			sql += " and t.affiliated_level ='" + AffiliatedLevel + "'";
			if (affiliatedId != null) {
				sql += " and t.affiliated_id !='" + affiliatedId + "'";
			}
			Long count = Long.parseLong(bll.getSingal(sql).toString());
			if (count > 0) {
				flg = false;
			} else {
				flg = true;
			}
		}
		return flg;
	}

	public boolean findByFourProperty(BpCCbmAffiliated entity) {

		// 判断增加的指标名称，指标所属部门，挂靠级别，挂靠标准四个形成的主体是否在数据库中已经存在
		long id = entity.getItemId();
		String dept = entity.getDepCode();
		String level = entity.getAffiliatedLevel();
		Double value = entity.getAffiliatedValue();
		final String queryString = "select t.affiliated_id from BP_C_CBM_AFFILIATED t\n"
				+ "where t.item_id="
				+ id
				+ "\n"
				+ "and t.dep_code="
				+ dept
				+ "\n"
				+ "and t.affiliated_level="
				+ level
				+ "\n"
				+ "and t.affiliated_value=" + value + "and t.is_use='Y'";
		Object obj = bll.getSingal(queryString);

		if (obj != null)
			return true;
		else
			return false;
	}

	/**
	 * Delete a persistent BpCCbmAffiliated entity.
	 * 
	 * @param entity
	 *            BpCCbmAffiliated entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(BpCCbmAffiliated entity) {
		try {
			entity = entityManager.getReference(BpCCbmAffiliated.class, entity
					.getAffiliatedId());
			entityManager.remove(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean delete(String ids) {
		try {
			String sql = "UPDATE bp_c_cbm_affiliated t\n"
					+ "   SET t.is_use = 'N'\n" + " WHERE t.affiliated_id IN ("
					+ ids + ")";
			bll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpCCbmAffiliated entity and return it or a
	 * copy of it to the sender. A copy of the BpCCbmAffiliated entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCCbmAffiliated entity to update
	 * @return BpCCbmAffiliated the persisted BpCCbmAffiliated entity instance,
	 *         may not be the same
	 * @throws CodeRepeatException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public boolean update(BpCCbmAffiliated entity) {
		boolean flag = true;
		try {
			if(judgeByAffiliatedLevel(entity.getItemId(), entity.getAffiliatedLevel(), entity.getAffiliatedId())){
				entityManager.merge(entity);
				flag = true;
			}else{
				flag = false;
			}
			return flag;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	// public boolean namePermitted(Long id, String deptcode) {
	// try {
	// String sql = "SELECT COUNT(1)\n" + " FROM bp_c_cbm_affiliated t\n"
	// + " WHERE t.item_id = '" + id + "'\n"
	// + " AND t.dep_code <> " + deptcode + "";
	// Long count = Long.parseLong(bll.getSingal(sql).toString());
	// if (count > 0)
	// return true;
	// else
	// return false;
	// } catch (RuntimeException re) {
	// LogUtil.log("update failed", Level.SEVERE, re);
	// throw re;
	// }
	// }

	public BpCCbmAffiliated findById(Long id) {
		// LogUtil.log("finding BpCCbmAffiliated instance with id: " + id,
		// Level.INFO, null);
		try {
			BpCCbmAffiliated instance = entityManager.find(
					BpCCbmAffiliated.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCCbmAffiliated entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCCbmAffiliated property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCCbmAffiliated> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpCCbmAffiliated> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCCbmAffiliated instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCCbmAffiliated model where model."
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
	 * Find all BpCCbmAffiliated entities.
	 * 
	 * @return List<BpCCbmAffiliated> all BpCCbmAffiliated entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpCCbmAffiliated> findAll() {
		LogUtil.log("finding all BpCCbmAffiliated instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpCCbmAffiliated model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findStandardList(String enterpriseCode,
			int... rowStartIdxAndCount) throws Exception {
		try {
			PageObject obj = new PageObject();
			String sql = "SELECT t.affiliated_id,\n"
					+ "       t.item_id,\n"
					+ "       (SELECT a.item_name\n"
					+ "          FROM bp_c_cbm_item a\n"
					+ "         WHERE a.item_id = t.item_id) itemname,\n"
					+ "       t.dep_code,\n"
					+ "       (SELECT b.dept_name\n"
					+ "          FROM hr_c_dept b\n"
					+ "         WHERE b.dept_code = t.dep_code) deptname,\n"
					+ "       t.affiliated_level,\n"
					+ "       t.affiliated_value,\n"
					// +" (select m.if_branch_item from BP_C_CBM_ITEM m where
					// m.item_id = t.item_id and m.is_use =
					// 'Y')if_branch_item,\n"
					+ "       t.view_no\n" + "  FROM bp_c_cbm_affiliated t\n"
					+ " WHERE t.is_use = 'Y'\n"
					+ "   AND t.enterprise_code = '" + enterpriseCode + "'\n"
					+ " order by t.view_no";
			String sqlcount = "SELECT COUNT(1)\n"
					+ "  FROM bp_c_cbm_affiliated t\n"
					+ " WHERE t.is_use = 'Y'\n"
					+ "   AND t.enterprise_code = '" + enterpriseCode + "'";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Long count = Long.parseLong(bll.getSingal(sqlcount).toString());
			List arraylist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				BpCCbmAffiliatedForm fmodel = new BpCCbmAffiliatedForm();
				BpCCbmAffiliated model = new BpCCbmAffiliated();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					model.setAffiliatedId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setItemId(Long.parseLong(data[1].toString()));
				if (data[2] != null)
					fmodel.setItemname(data[2].toString());
				if (data[3] != null) {
					model.setDepCode(data[3].toString());
					String[] dArray = model.getDepCode().split(",");
					String deptName = "";
					for (int i = 0; i < dArray.length; i++) {
						String dsql = "SELECT b.dept_name\n"
								+ "          FROM hr_c_dept b\n"
								+ "         WHERE b.dept_code ='" + dArray[i]
								+ "'";
						String temp = "";
						if (bll.getSingal(dsql) != null) {
							temp = bll.getSingal(dsql).toString();
						}
						if (i < dArray.length - 1)
							deptName += temp + ",";
						else
							deptName += temp;
					}
					fmodel.setDeptname(deptName);
				}

				// if (data[4] != null)
				// fmodel.setDeptname(data[4].toString());
				if (data[5] != null)
					model.setAffiliatedLevel(data[5].toString());
				if (data[6] != null)
					if (!("").equals(data[6].toString()))
						model.setAffiliatedValue(Double.parseDouble(data[6]
								.toString()));
				// if(data[7] != null)
				// fmodel.setIfBranchItem(data[7].toString());
				if (data[7] != null)
					model.setViewNo(Long.parseLong(data[7].toString()));

				fmodel.setAfInfo(model);
				arraylist.add(fmodel);
			}
			obj.setList(arraylist);
			obj.setTotalCount(count);
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}

	public Object findStandardInfo(String aid) {
		String sql = "SELECT t.affiliated_id,\n" + "       t.item_id,\n"
				+ "       (SELECT a.item_code\n"
				+ "          FROM bp_c_cbm_item a\n"
				+ "         WHERE a.item_id = t.item_id) itemcode,\n"
				+ "       (SELECT a.item_name\n"
				+ "          FROM bp_c_cbm_item a\n"
				+ "         WHERE a.item_id = t.item_id) itemname,\n"
				+ "       t.dep_code,\n" + "       (SELECT b.dept_name\n"
				+ "          FROM hr_c_dept b\n"
				+ "         WHERE b.dept_code = t.dep_code) deptname,\n"
				+ "       t.affiliated_level,\n"
				+ "       t.affiliated_value,\n" + "       t.view_no\n"
				+ "  FROM bp_c_cbm_affiliated t\n"
				+ " WHERE t.affiliated_id = '" + aid + "'";
		return bll.getSingal(sql);
	}
}