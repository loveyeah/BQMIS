package power.ejb.run.securityproduction.safesupervise;

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

/**
 * Facade for entity SpJWeldingRepair.
 * 
 * @see power.ejb.run.securityproduction.safesupervise.SpJWeldingRepair
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJWeldingRepairFacade implements SpJWeldingRepairFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(SpJWeldingRepair entity) {
		LogUtil.log("保存电动工器具和电焊机监测记录开始", Level.INFO, null);
		try {
			entity
					.setRepairId(bll.getMaxId("SP_J_WELDING_REPAIR",
							"REPAIR_ID"));
			entityManager.persist(entity);
			LogUtil.log("保存成功", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("保存失败", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(SpJWeldingRepair entity) {
		LogUtil.log("deleting SpJWeldingRepair instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SpJWeldingRepair.class, entity
					.getRepairId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJWeldingRepair update(SpJWeldingRepair entity) {
		LogUtil.log("updating SpJWeldingRepair instance", Level.INFO, null);
		try {
			SpJWeldingRepair result = entityManager.merge(entity);
			LogUtil.log("修改成功", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("修改失败", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJWeldingRepair findById(Long id) {
		LogUtil.log("finding SpJWeldingRepair instance with id: " + id,
				Level.INFO, null);
		try {
			SpJWeldingRepair instance = entityManager.find(
					SpJWeldingRepair.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<SpJWeldingRepair> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding SpJWeldingRepair instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SpJWeldingRepair model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<SpJWeldingRepair> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all SpJWeldingRepair instances", Level.INFO, null);
		try {
			final String queryString = "select model from SpJWeldingRepair model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getWelding(String beginTime, String endTime,
			String toolCode, String toolType, String enterprise,
			String isMaint, String fillBy, int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		String sql = "select t.repair_id," + "       l.tool_code,"
				+ "       l.tool_type," + "       l.tool_model,"
				+ "       getdeptname(t.belong_dep),\n"
				+ "       to_char(l.factory_date, 'yyyy-MM-dd'),\n"
				+ "       t.insulation,\n" + "       t.resistance,\n"
				+ "       t.outside_check,\n" + "       t.repair_result,\n"
				+ "       getworkername(t.repair_by),\n"
				+ "       to_char(t.repair_begin, 'yyyy-MM-dd'),\n"
				+ "       to_char(t.repair_end, 'yyyy-MM-dd'),\n"
				+ "       to_char(t.next_time, 'yyyy-MM-dd'),\n"
				+ "       t.memo,t.tool_id,l.tool_name,t.repair_by,t.belong_dep\n"
				+ "  from SP_J_WELDING_REPAIR t, SP_C_TOOLS l\n"
				+ " where t.tool_id = l.tool_id\n" + "   and t.is_use = 'Y'\n"
				+ "   and l.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterprise + "'\n" + "   and l.enterprise_code = '"
				+ enterprise + "'";
		if (beginTime != null && !beginTime.equals(""))
			sql += "and to_char(t.repair_begin,'yyyy-mm-dd') >= '" + beginTime
					+ "' ";
		if (endTime != null && !endTime.equals(""))
			sql += "and to_char(t.repair_end,'yyyy-mm-dd') <= '" + endTime
					+ "' ";
		if (toolCode != null && !toolCode.equals(""))
			sql += "and l.tool_code like '%" + toolCode + "%' ";
		if (toolType != null && !toolType.equals(""))
			sql += "and l.tool_type='" + toolType + "' ";
		if (isMaint != null && isMaint.equals("1"))
			sql += " and t.fill_by='" + fillBy + "'  ";

		// String sqlCount = sql
		// .replaceAll("select.*from", "select count(*) from");
		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += "order by t.repair_id";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setList(list);
		result.setTotalCount(count);
		return result;

	}

	/**
	 * 删除 电动工器具和电焊机检测记录 多条删除
	 * 
	 * @param id
	 *            删除id
	 * @return
	 */
	public String deleteWeldinglist(String ids) {
		String sql = "update SP_J_WELDING_REPAIR a set a.is_use='N' where a.repair_id in ("
				+ ids + ")";
		try {
			bll.exeNativeSQL(sql);
			return "数据删除成功！";
		} catch (Exception e) {
			e.printStackTrace();
			return "数据删除失败！";
		}
	}

}