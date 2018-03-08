package power.ejb.productiontec.dependabilityAnalysis;

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
import power.ejb.productiontec.dependabilityAnalysis.form.PtKkxJSjlrForm;

/**
 * Facade for entity PtKkxJSjlr.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.PtKkxJSjlr
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtKkxJSjlrFacade implements PtKkxJSjlrFacadeRemote {
	// property constants
	public static final String BLOCK_CODE = "blockCode";
	public static final String JZZT_ID = "jzztId";
	public static final String KEEP_TIME = "keepTime";
	public static final String REDUCE_EXERT = "reduceExert";
	public static final String STOP_TIMES = "stopTimes";
	public static final String SUCCESS_TIMES = "successTimes";
	public static final String FAILURE_TIMES = "failureTimes";
	public static final String REPAIR_MANDAYS = "repairMandays";
	public static final String REPAIR_COST = "repairCost";
	public static final String STOP_REASON = "stopReason";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtKkxJSjlr entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtKkxJSjlr entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtKkxJSjlr entity) {
		try {
			
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtKkxJSjlr entity.
	 * 
	 * @param entity
	 *            PtKkxJSjlr entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtKkxJSjlr entity) {
		LogUtil.log("deleting PtKkxJSjlr instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtKkxJSjlr.class, entity
					.getSjlrId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtKkxJSjlr entity and return it or a copy of
	 * it to the sender. A copy of the PtKkxJSjlr entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtKkxJSjlr entity to update
	 * @return PtKkxJSjlr the persisted PtKkxJSjlr entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtKkxJSjlr update(PtKkxJSjlr entity) {
		try {
			PtKkxJSjlr result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtKkxJSjlr findById(Long id) {
		LogUtil.log("finding PtKkxJSjlr instance with id: " + id, Level.INFO,
				null);
		try {
			PtKkxJSjlr instance = entityManager.find(PtKkxJSjlr.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtKkxJSjlr entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtKkxJSjlr property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtKkxJSjlr> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtKkxJSjlr> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PtKkxJSjlr instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtKkxJSjlr model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PtKkxJSjlr> findByBlockCode(Object blockCode) {
		return findByProperty(BLOCK_CODE, blockCode);
	}

	public List<PtKkxJSjlr> findByJzztId(Object jzztId) {
		return findByProperty(JZZT_ID, jzztId);
	}

	public List<PtKkxJSjlr> findByKeepTime(Object keepTime) {
		return findByProperty(KEEP_TIME, keepTime);
	}

	public List<PtKkxJSjlr> findByReduceExert(Object reduceExert) {
		return findByProperty(REDUCE_EXERT, reduceExert);
	}

	public List<PtKkxJSjlr> findByStopTimes(Object stopTimes) {
		return findByProperty(STOP_TIMES, stopTimes);
	}

	public List<PtKkxJSjlr> findBySuccessTimes(Object successTimes) {
		return findByProperty(SUCCESS_TIMES, successTimes);
	}

	public List<PtKkxJSjlr> findByFailureTimes(Object failureTimes) {
		return findByProperty(FAILURE_TIMES, failureTimes);
	}

	public List<PtKkxJSjlr> findByRepairMandays(Object repairMandays) {
		return findByProperty(REPAIR_MANDAYS, repairMandays);
	}

	public List<PtKkxJSjlr> findByRepairCost(Object repairCost) {
		return findByProperty(REPAIR_COST, repairCost);
	}

	public List<PtKkxJSjlr> findByStopReason(Object stopReason) {
		return findByProperty(STOP_REASON, stopReason);
	}

	public List<PtKkxJSjlr> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}
	public void delete(String ids) {
		String sqlString = "delete from  PT_KKX_J_SJLR  s "
				+ " where s.SJLR_ID in (" + ids + ")";
		bll.exeNativeSQL(sqlString);
	}
	/**
	 * Find all PtKkxJSjlr entities.
	 * 
	 * @return List<PtKkxJSjlr> all PtKkxJSjlr entities
	 */
	
	/**
	 * modify by drdu 090728
	 */
	@SuppressWarnings("unchecked") 
	public PageObject findAll(String enterpriseCode,int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();
			String sql =  "select a.sjlr_id,\n" +
				"       a.block_code,\n" + 
				"       a.jzzt_id,\n" + 
				"       b.jzzt_code,\n" + 
				"       b.jzzt_name,\n" + 
				"       to_char(a.start_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
				"       to_char(a.end_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
				"       a.keep_time,\n" + 
				"       a.reduce_exert,\n" + 
				"       a.stop_times,\n" + 
				"       a.success_times,\n" + 
				"       a.failure_times,\n" + 
				"       a.repair_mandays,\n" + 
				"       a.repair_cost,\n" + 
				"       a.stop_reason\n" + 
				"  from PT_KKX_J_SJLR a, JS_J_KKX_JZZTWH b\n" + 
				" where a.jzzt_id = b.jzzt_id(+)\n" + 
				"   and a.enterprise_code = '"+enterpriseCode+"'";

			String sqlCount =  "select count(1)\n" +
				"  from PT_KKX_J_SJLR a, JS_J_KKX_JZZTWH b\n" + 
				" where a.jzzt_id = b.jzzt_id(+)\n" + 
				"   and a.enterprise_code = '"+enterpriseCode+"'";

			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			
			if (list != null && list.size() > 0) {
				while (it.hasNext()) {
					PtKkxJSjlrForm form = new PtKkxJSjlrForm();
					Object[] data = (Object[]) it.next();
					form.setSjlrId(Long.parseLong(data[0].toString()));
					if(data[1] != null)
						form.setBlockCode(data[1].toString());
					if(data[2] != null)
						form.setJzztId(Long.parseLong(data[2].toString()));
					if(data[3] != null)
						form.setJzztCode(data[3].toString());
					if(data[4] != null)
						form.setJzztName(data[4].toString());
					if(data[5] != null)
						form.setStartDate(data[5].toString());
					if(data[6] != null)
						form.setEndDate(data[6].toString());
					if(data[7] != null)
						form.setKeepTime(Double.parseDouble(data[7].toString()));
					if(data[8] != null)
						form.setReduceExert(Double.parseDouble(data[8].toString()));
					if(data[9] != null)
						form.setStopTimes(Long.parseLong(data[9].toString()));
					if(data[10] != null)
						form.setSuccessTimes(Long.parseLong(data[10].toString()));
					if(data[11] != null)
						form.setFailureTimes(Long.parseLong(data[11].toString()));
					if(data[12] != null)
						form.setRepairMandays(Double.parseDouble(data[12].toString()));
					if(data[13] != null)
						form.setRepairCost(Double.parseDouble(data[13].toString()));
					if(data[14] != null)
						form.setStopReason(data[14].toString());
					arrlist.add(form);
				}
			}
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
//			final String queryString = "select * from PT_KKX_J_SJLR t where t.enterprise_code='"+enterpriseCode+"'";
//			String sql="select count(1) from PT_KKX_J_SJLR t where t.enterprise_code='"+enterpriseCode+"'";
//			Long count = Long.parseLong(bll.getSingal(sql).toString());
//			List<PtKkxJSjlr> list = bll.queryByNativeSQL(queryString, PtKkxJSjlr.class,rowStartIdxAndCount);
//			pg.setList(list);
//			pg.setTotalCount(count);
//			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void save(List<PtKkxJSjlr> addList, List<PtKkxJSjlr> updateList,
			String deleteId) {
		if(addList.size() > 0){
			Long id = bll.getMaxId("PT_KKX_J_SJLR ", "SJLR_ID");
			for(PtKkxJSjlr entity : addList){
				entity.setSjlrId(id++);
				this.save(entity);
			}}
		if(updateList.size() > 0){
			for (PtKkxJSjlr entity : updateList) {
				this.update(entity);
			}
		}
		if (deleteId.length() > 0) {
			this.delete(deleteId);
		}
		}
}