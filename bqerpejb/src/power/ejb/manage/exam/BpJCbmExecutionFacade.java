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
import power.ejb.manage.exam.form.BpJCbmExecutionForm;
import power.ejb.manage.exam.form.BpJCbmYearExecutionForm;

/**
 * Facade for entity BpJCbmExecution.
 * 
 * @see power.ejb.manage.exam.BpJCbmExecution
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJCbmExecutionFacade implements BpJCbmExecutionFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved BpJCbmExecution entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJCbmExecution entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpJCbmExecution entity) {
		LogUtil.log("saving BpJCbmExecution instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpJCbmExecution entity.
	 * 
	 * @param entity
	 *            BpJCbmExecution entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJCbmExecution entity) {
		LogUtil.log("deleting BpJCbmExecution instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJCbmExecution.class, entity
					.getExecutionId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpJCbmExecution entity and return it or a copy
	 * of it to the sender. A copy of the BpJCbmExecution entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJCbmExecution entity to update
	 * @return BpJCbmExecution the persisted BpJCbmExecution entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJCbmExecution update(BpJCbmExecution entity) {
		LogUtil.log("updating BpJCbmExecution instance", Level.INFO, null);
		try {
			BpJCbmExecution result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJCbmExecution findById(Long id) {
		LogUtil.log("finding BpJCbmExecution instance with id: " + id,
				Level.INFO, null);
		try {
			BpJCbmExecution instance = entityManager.find(
					BpJCbmExecution.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpJCbmExecution entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJCbmExecution property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJCbmExecution> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpJCbmExecution> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpJCbmExecution instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpJCbmExecution model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getExecutionValueList(String type, String datetime,
			String enterpriseCode) {
		PageObject obj = new PageObject();
//		String getTarget = ("plan").equals(type) ? "branch_plan_value"
//				: "real_value"; 
		String getTarget = "real_value";
		String sql; 
			sql = "SELECT (SELECT x.execution_id\n"
					+ "          FROM bp_j_cbm_execution x\n"
					+ "         WHERE x.item_id = a.item_id\n"
					+ "           AND x.belong_block = '1'\n"
					+ "           AND x.year_month = '"
					+ datetime
					+ "'\n"
					+ "           AND rownum = 1) xexecutionid,\n"
					+ "       (SELECT y.execution_id\n"
					+ "          FROM bp_j_cbm_execution y\n"
					+ "         WHERE y.item_id = a.item_id\n"
					+ "           AND y.belong_block = '2'\n"
					+ "           AND y.year_month = '"
					+ datetime
					+ "'\n"
					+ "           AND rownum = 1) yexecutionid,\n"
					+ "       (SELECT z.execution_id\n"
					+ "          FROM bp_j_cbm_execution z\n"
					+ "         WHERE z.item_id = a.item_id\n"
					+ "           AND z.belong_block = '3'\n"
					+ "           AND z.year_month = '"
					+ datetime
					+ "'\n"
					+ "           AND rownum = 1) zexecutionid,\n"
					+ "       a.item_id,\n"
					+ "       a.item_code,\n"
					+ "       a.item_name,\n"
					+ "       (SELECT b.unit_name\n"
					+ "          FROM bp_c_measure_unit b\n"
					+ "         WHERE b.unit_id = a.unit_id) unitname,\n"
					+ "       (SELECT x."
					+ getTarget
					+ "\n"
					+ "          FROM bp_j_cbm_execution x\n"
					+ "         WHERE x.item_id = a.item_id\n"
					+ "           AND x.belong_block = '1'\n"
					+ "           AND x.year_month = '"
					+ datetime
					+ "'\n"
					+ "           AND rownum = 1) branchplanvalue1,\n"
					+ "       (SELECT y."
					+ getTarget
					+ "\n"
					+ "          FROM bp_j_cbm_execution y\n"
					+ "         WHERE y.item_id = a.item_id\n"
					+ "           AND y.belong_block = '2'\n"
					+ "           AND y.year_month = '"
					+ datetime
					+ "'\n"
					+ "           AND rownum = 1) branchplanvalue2,\n"
					+ "       (SELECT z."
					+ getTarget
					+ "\n"
					+ "          FROM bp_j_cbm_execution z\n"
					+ "         WHERE z.item_id = a.item_id\n"
					+ "           AND z.belong_block = '3'\n"
					+ "           AND z.year_month = '"
					+ datetime
					+ "'\n"
					+ "           AND rownum = 1) branchplanvalue3\n"
					+ "  FROM /*bp_c_cbm_relation t,*/ bp_c_cbm_item a\n"
					+ " WHERE\n"
					+ "/* t.topic_id = 1\n"
					+ "   AND t.is_use = 'Y'\n"
					+ "   AND t.enterprise_code = 'hfdc'\n"
					+ "   AND a.item_id = t.item_id*/\n"
					+ "  a.is_use = 'Y' and a.is_item='Y'\n"
//					+ "  and a.if_branch_item = 'Y'\n"//add by drdu 091201 Y---是分公司指标
					+ " AND a.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ " ORDER BY /*t.display_no,\n"
					+ "          t.relation_id*/ a.display_no,\n"
					+ "          a.item_id"; 
			List list = bll.queryByNativeSQL(sql);
			List arraylist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				BpJCbmExecutionForm model = new BpJCbmExecutionForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					model.setExecutionid1(data[0].toString());
				if (data[1] != null)
					model.setExecutionid2(data[1].toString());
				if (data[2] != null)
					model.setExecutionid3(data[2].toString());
				if (data[3] != null)
					model.setItemid(data[3].toString());
				if (data[4] != null)
					model.setItemcode(data[4].toString());
				if (data[5] != null)
					model.setItemname(data[5].toString());
				if (data[6] != null)
					model.setUnitname(data[6].toString());
				if (data[7] != null)
					model.setValue1(data[7].toString());
				if (data[8] != null)
					model.setValue2(data[8].toString());
				if (data[9] != null)
					model.setValue3(data[9].toString());
				arraylist.add(model);
			}
			obj.setList(arraylist); 
		return obj;
	}
	/**
	 * 实际发生值,分公司月计划
	 */
	public boolean saveExecutionValueList(String type, String datetime,
			List<BpJCbmExecutionForm> postlist, String enterpriseCode) {
		String sql = "begin\n";
		Long id = bll.getMaxId("bp_j_cbm_execution", "EXECUTION_ID");
		for (BpJCbmExecutionForm model : postlist) {
			if(model.getValue1() == null || model.getValue1().trim().equals(""))
				model.setValue1(null);
			if(model.getValue2() == null || model.getValue2().trim().equals(""))
				model.setValue1(null);
			if(model.getValue3() == null || model.getValue3().trim().equals(""))
				model.setValue1(null);
			if (("plan").equals(type)) {
				if (model.getExecutionid1() == null) {
					sql +=  "INSERT INTO bp_j_cbm_execution\n" +
						"  (EXECUTION_ID,\n" + 
						"   ITEM_ID,\n" + 
						"   BELONG_BLOCK,\n" + 
						"   YEAR_MONTH,\n" + 
						"   BRANCH_PLAN_VALUE,\n" + 
						"   IF_RELEASE,\n" + 
						"   IS_USE,\n" + 
						"   ENTERPRISE_CODE)\n" + 
						"VALUES\n" + 
						"  (" + id + ", "+ model.getItemid() +", '1', '" + datetime + "', " + model.getValue1() + ", 'N', 'Y', '" + enterpriseCode + "');";
                        id++; 
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.branch_plan_value = "
							+ model.getValue1() + "\n"
							+ " WHERE t.execution_id = "
							+ model.getExecutionid1() + ";";
					
				}
				if (model.getExecutionid2() == null) {
					sql +=  "INSERT INTO bp_j_cbm_execution\n" +
					"  (EXECUTION_ID,\n" + 
					"   ITEM_ID,\n" + 
					"   BELONG_BLOCK,\n" + 
					"   YEAR_MONTH,\n" + 
					"   BRANCH_PLAN_VALUE,\n" + 
					"   IF_RELEASE,\n" + 
					"   IS_USE,\n" + 
					"   ENTERPRISE_CODE)\n" + 
					"VALUES\n" + 
					"  (" + id + ", "+ model.getItemid() +", '2', '" + datetime + "', " + model.getValue2() + ", 'N', 'Y', '" + enterpriseCode + "');";
                    id++;
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.branch_plan_value = "
							+ model.getValue2() + "\n"
							+ " WHERE t.execution_id = "
							+ model.getExecutionid2() + ";";
				}
				if (model.getExecutionid3() == null) {
					sql +=  "INSERT INTO bp_j_cbm_execution\n" +
					"  (EXECUTION_ID,\n" + 
					"   ITEM_ID,\n" + 
					"   BELONG_BLOCK,\n" + 
					"   YEAR_MONTH,\n" + 
					"   BRANCH_PLAN_VALUE,\n" + 
					"   IF_RELEASE,\n" + 
					"   IS_USE,\n" + 
					"   ENTERPRISE_CODE)\n" + 
					"VALUES\n" + 
					"  (" + id + ", "+ model.getItemid() +", '3', '" + datetime + "', " + model.getValue3() + ", 'N', 'Y', '" + enterpriseCode + "');";
                    id++;
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.branch_plan_value = "
							+ model.getValue3() + "\n"
							+ " WHERE t.execution_id = "
							+ model.getExecutionid3() + ";";
				}
			} else if (("real").equals(type)) {
				if (model.getExecutionid1() == null) { 
					sql += "INSERT INTO bp_j_cbm_execution\n" +
						"  (EXECUTION_ID,\n" + 
						"   ITEM_ID,\n" + 
						"   BELONG_BLOCK,\n" + 
						"   YEAR_MONTH,\n" + 
						"   real_value,\n" + 
						"   IF_RELEASE,\n" + 
						"   IS_USE,\n" + 
						"   ENTERPRISE_CODE)\n" + 
						"VALUES\n" + 
						"  (" + id + ", " + model.getItemid() + ", '1', '" + datetime + "', "+model.getValue1()+", 'Y', 'Y', '" + enterpriseCode + "');";
                        id++;
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.real_value = " + model.getValue1()
							+ "\n" + " WHERE t.execution_id = "
							+ model.getExecutionid1() + ";";
				}
				if (model.getExecutionid2() == null) { 
					sql += "INSERT INTO bp_j_cbm_execution\n" +
					"  (EXECUTION_ID,\n" + 
					"   ITEM_ID,\n" + 
					"   BELONG_BLOCK,\n" + 
					"   YEAR_MONTH,\n" + 
					"   real_value,\n" + 
					"   IF_RELEASE,\n" + 
					"   IS_USE,\n" + 
					"   ENTERPRISE_CODE)\n" + 
					"VALUES\n" + 
					"  (" + id + ", " + model.getItemid() + ", '2', '" + datetime + "', "+model.getValue2()+", 'Y', 'Y', '" + enterpriseCode + "');";
                    
					id++;
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.real_value = " + model.getValue2()
							+ "\n" + " WHERE t.execution_id = "
							+ model.getExecutionid2() + ";";
				}
				if (model.getExecutionid3() == null) { 
					sql += "INSERT INTO bp_j_cbm_execution\n" +
					"  (EXECUTION_ID,\n" + 
					"   ITEM_ID,\n" + 
					"   BELONG_BLOCK,\n" + 
					"   YEAR_MONTH,\n" + 
					"   real_value,\n" + 
					"   IF_RELEASE,\n" + 
					"   IS_USE,\n" + 
					"   ENTERPRISE_CODE)\n" + 
					"VALUES\n" + 
					"  (" + id + ", " + model.getItemid() + ", '3', '" + datetime + "', "+model.getValue3()+", 'Y', 'Y', '" + enterpriseCode + "');";
                    
					id++;
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.real_value = " + model.getValue3()
							+ "\n" + " WHERE t.execution_id = "
							+ model.getExecutionid3() + ";";
				}
			} else {
				sql += "\n";
			}
		}
		sql += "commit;\n";
		sql += "end;"; 
		try {
			bll.exeNativeSQL(sql);
			return true;
		} catch (Exception e) {
			System.out.println(sql);
			return false;
		}
	}

	/**
	 * Find all BpJCbmExecution entities.
	 * 
	 * @return List<BpJCbmExecution> all BpJCbmExecution entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpJCbmExecution> findAll() {
		LogUtil.log("finding all BpJCbmExecution instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpJCbmExecution model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getYearExecutionValueListByItemId(String itemId,
			String datetime, String enterpriseCode) {
		PageObject obj = new PageObject();
		String sql = "SELECT (select a.execution_id\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = '"
				+ itemId
				+ "'\n"
				+ "           and a.belong_block = 1\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) executionid1,\n"
				+ "       (select a.execution_id\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = '"
				+ itemId
				+ "'\n"
				+ "           and a.belong_block = 2\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) executionid2,\n"
				+ "       (select a.execution_id\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = '"
				+ itemId
				+ "'\n"
				+ "           and a.belong_block = 3\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) executionid3,\n"
				+ "       (select a.item_id\n"
				+ "          from bp_c_cbm_item a\n"
				+ "         where a.item_id = '"
				+ itemId
				+ "'\n"
				+ "           and rownum = 1) itemid,\n"
				+ "       (select a.item_code\n"
				+ "          from bp_c_cbm_item a\n"
				+ "         where a.item_id = '"
				+ itemId
				+ "'\n"
				+ "           and rownum = 1) itemcode,\n"
				+ "       (select a.item_name\n"
				+ "          from bp_c_cbm_item a\n"
				+ "         where a.item_id = '"
				+ itemId
				+ "'\n"
				+ "           and rownum = 1) itemname,\n"
				+ "       getunitname((select u.unit_id\n"
				+ "                     from bp_c_cbm_item u\n"
				+ "                    where u.item_id = '"
				+ itemId
				+ "'\n"
				+ "                      and rownum = 1)) unitname,\n"
				+ "       (select a.year_budget_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = '"
				+ itemId
				+ "'\n"
				+ "           and a.belong_block = 1\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) value1,\n"
				+ "       (select a.year_budget_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = '"
				+ itemId
				+ "'\n"
				+ "           and a.belong_block = 2\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) value2,\n"
				+ "       (select a.year_budget_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = '"
				+ itemId
				+ "'\n"
				+ "           and a.belong_block = 3\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) value3,\n"
				+ "       t.year\n"
				+ "  FROM (select '"
				+ datetime
				+ "' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-01' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-02' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-03' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-04' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-05' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-06' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-07' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-08' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-09' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-10' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-11' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '" + datetime + "-12' as year from dual) t";
		List list = bll.queryByNativeSQL(sql);
		List arraylist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			BpJCbmExecutionForm model = new BpJCbmExecutionForm();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				model.setExecutionid1(data[0].toString());
			if (data[1] != null)
				model.setExecutionid2(data[1].toString());
			if (data[2] != null)
				model.setExecutionid3(data[2].toString());
			if (data[3] != null)
				model.setItemid(data[3].toString());
			if (data[4] != null)
				model.setItemcode(data[4].toString());
			if (data[5] != null)
				model.setItemname(data[5].toString());
			if (data[6] != null)
				model.setUnitname(data[6].toString());
			if (data[7] != null)
				model.setValue1(data[7].toString());
			if (data[8] != null)
				model.setValue2(data[8].toString());
			if (data[9] != null)
				model.setValue3(data[9].toString());
			if (data[10] != null)
				model.setDateTime(data[10].toString());
			if (arraylist.size() == 0) {
				model.setItemname(model.getItemname() + "(全年)");
			} else {
				model.setItemname(arraylist.size() + "月");
			}
			arraylist.add(model);

		}
		obj.setList(arraylist);
		obj.setTotalCount((long) arraylist.size());
		return obj;
	}

	public boolean saveYearExecutionValueList(String type, String datetime,
			List<BpJCbmExecutionForm> postlist, String enterpriseCode) {
		String sql = "begin\n";
		Long id = bll.getMaxId("bp_j_cbm_execution", "EXECUTION_ID");
		for (BpJCbmExecutionForm model : postlist) {
			if(model.getValue1() == null || model.getValue1().trim().equals(""))
				model.setValue1("null");
			if(model.getValue2() == null || model.getValue2().trim().equals(""))
				model.setValue2("null");
			if(model.getValue3() == null || model.getValue3().trim().equals(""))
				model.setValue3("null");
			if (("plan").equals(type)) {
				if (model.getExecutionid1() == null) {
					id++;
					sql += "INSERT INTO bp_j_cbm_execution t\n" + "VALUES\n"
							+ "  (" + id + ",\n" + model.getItemid() + ",\n"
							+ "   '1',\n" + "   '" + model.getDateTime() + "',\n" + "   "
							+ model.getValue1() + ",\n" + "   null,\n"
							+ "   null,\n" + "   null,\n" + "   'Y',\n"
							+ "   '" + enterpriseCode + "');";
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.year_budget_value = "
							+ model.getValue1() + "\n"
							+ " WHERE t.execution_id = "
							+ model.getExecutionid1() + ";";
				}
				if (model.getExecutionid2() == null) {
					id++;
					sql += "INSERT INTO bp_j_cbm_execution t\n" + "VALUES\n"
							+ "  (" + id + ",\n" + model.getItemid() + ",\n"
							+ "   '2',\n" + "   '" + model.getDateTime() + "',\n" + "   "
							+ model.getValue2() + ",\n" + "   null,\n"
							+ "   null,\n" + "   null,\n" + "   'Y',\n"
							+ "   '" + enterpriseCode + "');";
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.year_budget_value = "
							+ model.getValue2() + "\n"
							+ " WHERE t.execution_id = "
							+ model.getExecutionid2() + ";";
				}
				if (model.getExecutionid3() == null) {
					id++;
					sql += "INSERT INTO bp_j_cbm_execution t\n" + "VALUES\n"
							+ "  (" + id + ",\n" + model.getItemid() + ",\n"
							+ "   '3',\n" + "   '" + model.getDateTime() + "',\n" + "   "
							+ model.getValue3() + ",\n" + "   null,\n"
							+ "   null,\n" + "   null,\n" + "   'Y',\n"
							+ "   '" + enterpriseCode + "');";
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.year_budget_value = "
							+ model.getValue3() + "\n"
							+ " WHERE t.execution_id = "
							+ model.getExecutionid3() + ";";
				}
			} else if (("real").equals(type)) {
				if (model.getExecutionid1() == null) {
					id++;
					sql += "INSERT INTO bp_j_cbm_execution t\n" + "VALUES\n"
							+ "  (" + id + ",\n" + model.getItemid() + ",\n"
							+ "   '1',\n" + "   '" + model.getDateTime() + "',\n"
							+ "   null,\n" + ",\n" + "   null,\n" + "   "
							+ model.getValue1() + "   null,\n" + "   'Y',\n"
							+ "   '" + enterpriseCode + "');";
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.real_value = " + model.getValue1()
							+ "\n" + " WHERE t.execution_id = "
							+ model.getExecutionid1() + ";";
				}
				if (model.getExecutionid2() == null) {
					id++;
					sql += "INSERT INTO bp_j_cbm_execution t\n" + "VALUES\n"
							+ "  (" + id + ",\n" + model.getItemid() + ",\n"
							+ "   '2',\n" + "   '" + model.getDateTime() + "',\n"
							+ "   null,\n" + ",\n" + "   null,\n"
							+ model.getValue2() + "   null,\n" + "   'Y',\n"
							+ "   '" + enterpriseCode + "');";
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.real_value = " + model.getValue2()
							+ "\n" + " WHERE t.execution_id = "
							+ model.getExecutionid2() + ";";
				}
				if (model.getExecutionid3() == null) {
					id++;
					sql += "INSERT INTO bp_j_cbm_execution t\n" + "VALUES\n"
							+ "  (" + id + ",\n" + model.getItemid() + ",\n"
							+ "   '3',\n" + "   '" + model.getDateTime() + "',\n"
							+ "   null,\n" + ",\n" + "   null,\n"
							+ model.getValue3() + "   null,\n" + "   'Y',\n"
							+ "   '" + enterpriseCode + "');";
				} else {
					sql += "UPDATE bp_j_cbm_execution t\n"
							+ "   SET t.real_value = " + model.getValue3()
							+ "\n" + " WHERE t.execution_id = "
							+ model.getExecutionid3() + ";";
				}
			} else {
				sql += "\n";
			}
		}
		sql += "commit;\n";
		sql += "end;"; 
		try {
			bll.exeNativeSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getYearExecutionValueList(String datetime,
			String enterpriseCode) {
		PageObject obj = new PageObject();
		String sql = "SELECT (select a.execution_id\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 1\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) executionid1,\n"
				+ "       (select a.execution_id\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 2\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) executionid2,\n"
				+ "       (select a.execution_id\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 3\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) executionid3,\n"
				+ "       (select a.item_id\n"
				+ "          from bp_c_cbm_item a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and rownum = 1) itemid,\n"
				+ "       (select a.item_code\n"
				+ "          from bp_c_cbm_item a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and rownum = 1) itemcode,\n"
				+ "       (select a.item_name\n"
				+ "          from bp_c_cbm_item a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and rownum = 1) itemname,\n"
				+ "       getunitname((select u.unit_id\n"
				+ "                     from bp_c_cbm_item u\n"
				+ "                    where u.item_id = h.item_id\n"
				+ "                      and rownum = 1)) unitname,\n"
				+ "       (select a.year_budget_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 1\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) planvalue1,\n"
				+ "       (select a.real_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 1\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) realvalue1,\n"
				+ "       (select a.real_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 1\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) - (select a.year_budget_value\n"
				+ "                                from bp_j_cbm_execution a\n"
				+ "                               where a.item_id = h.item_id\n"
				+ "                                 and a.belong_block = 1\n"
				+ "                                 and a.year_month = t.year\n"
				+ "                                 and rownum = 1) flawvalue1,\n"
				+ "       (select a.year_budget_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 2\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) planvalue2,\n"
				+ "       (select a.real_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 2\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) realvalue2,\n"
				+ "       (select a.real_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 2\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) - (select a.year_budget_value\n"
				+ "                                from bp_j_cbm_execution a\n"
				+ "                               where a.item_id = h.item_id\n"
				+ "                                 and a.belong_block = 2\n"
				+ "                                 and a.year_month = t.year\n"
				+ "                                 and rownum = 1) flawvalue2,\n"
				+ "       (select a.year_budget_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 3\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) planvalue3,\n"
				+ "       (select a.real_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 3\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) realvalue3,\n"
				+ "       (select a.real_value\n"
				+ "          from bp_j_cbm_execution a\n"
				+ "         where a.item_id = h.item_id\n"
				+ "           and a.belong_block = 3\n"
				+ "           and a.year_month = t.year\n"
				+ "           and rownum = 1) - (select a.year_budget_value\n"
				+ "                                from bp_j_cbm_execution a\n"
				+ "                               where a.item_id = h.item_id\n"
				+ "                                 and a.belong_block = 3\n"
				+ "                                 and a.year_month = t.year\n"
				+ "                                 and rownum = 1) flawvalue3,\n"
				+ "       t.year\n" + "  FROM (select '"
				+ datetime
				+ "' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-01' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-02' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-03' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-04' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-05' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-06' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-07' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-08' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-09' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-10' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-11' as year\n"
				+ "          from dual\n"
				+ "        union all\n"
				+ "        select '"
				+ datetime
				+ "-12' as year from dual) t,\n"
				+ "       bp_c_cbm_item h\n"
				+ " where h.is_use = 'Y'\n"
				+ "   AND h.enterprise_code = 'hfdc'\n"
				+ " order by h.item_id, t.year";

		List list = bll.queryByNativeSQL(sql);
		List arraylist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			BpJCbmYearExecutionForm model = new BpJCbmYearExecutionForm();
			Object[] data = (Object[]) it.next();
			double realValue1 = 0.00;
			double realValue2 = 0.00;
			double realValue3 = 0.00;
			if (data[0] != null)
				model.setExecutionid1(data[0].toString());
			if (data[1] != null)
				model.setExecutionid2(data[1].toString());
			if (data[2] != null)
				model.setExecutionid3(data[2].toString());
			if (data[3] != null)
				model.setItemid(data[3].toString());
			if (data[4] != null)
				model.setItemcode(data[4].toString());
			if (data[5] != null)
				model.setItemname(data[5].toString());
			if (data[6] != null)
				model.setUnitname(data[6].toString());
			if (data[7] != null)
				model.setPlanValue1(data[7].toString());
			if (data[8] != null) {
				model.setRealValue1(data[8].toString());
				realValue1 += Double.valueOf(model.getRealValue1());
			}
			if (data[9] != null)
				model.setFlawValue1(data[9].toString());
			if (data[10] != null)
				model.setPlanValue2(data[10].toString());
			if (data[11] != null) {
				model.setRealValue2(data[11].toString());
				realValue2 += Double.valueOf(model.getRealValue2());
			}
			if (data[12] != null)
				model.setFlawValue2(data[12].toString());
			if (data[13] != null)
				model.setPlanValue3(data[13].toString());
			if (data[14] != null) {
				model.setRealValue3(data[14].toString());
				realValue3 += Double.valueOf(model.getRealValue3());
			}
			if (data[15] != null)
				model.setFlawValue3(data[15].toString());
			if (data[16] != null)
				model.setDateTime(data[16].toString());
			if (arraylist.size() % 13 == 0) {
				model.setItemname(model.getItemname() + "(全年)");
			} else {
				model.setItemname(arraylist.size() % 13 + "月");
			}
			arraylist.add(model);

		}
		obj.setList(arraylist);
		obj.setTotalCount((long) arraylist.size());
		return obj;
	}

	public boolean issueExecutionTable(String enterpriseCode, String dateTime) {
		try {
			String sql = "UPDATE bp_j_cbm_execution t\n"
					+ "   SET t.if_release = 'Y'\n" + " WHERE t.year_month = '"
					+ dateTime + "'\n" + "   AND t.enterprise_code = '"
					+ enterpriseCode + "'";
			bll.exeNativeSQL(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean ifAllowSaveExecutionTable(String enterpriseCode,
			String dateTime) {
		try {
			String sql = "SELECT COUNT(1)\n" + "  FROM bp_j_cbm_execution t\n"
					+ " WHERE t.year_month = '" + dateTime + "'\n"
					+ "   AND t.enterprise_code = '" + enterpriseCode + "' and t.if_release = 'Y'";
			if (("0").equals(bll.getSingal(sql).toString()))
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

}