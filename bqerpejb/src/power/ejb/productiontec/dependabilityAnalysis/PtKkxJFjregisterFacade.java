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
import power.ejb.productiontec.dependabilityAnalysis.form.PtKkxJFjregisterForm;
/**
 * 辅机数据录入
 * @author liuyi 091015
 */
@Stateless
public class PtKkxJFjregisterFacade implements PtKkxJFjregisterFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * 新增一条辅机数据录入书数据
	 */
	public void save(PtKkxJFjregister entity) {
		LogUtil.log("saving PtKkxJFjregister instance", Level.INFO, null);
		try {
			entity.setFjId(bll.getMaxId("PT_KKX_J_FJREGISTER", "FJ_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条辅机数据录入书数据
	 */
	public void delete(PtKkxJFjregister entity) {
		LogUtil.log("deleting PtKkxJFjregister instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtKkxJFjregister.class, entity
					.getFjId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条辅机数据录入书数据
	 */
	public PtKkxJFjregister update(PtKkxJFjregister entity) {
		LogUtil.log("updating PtKkxJFjregister instance", Level.INFO, null);
		try {
			PtKkxJFjregister result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtKkxJFjregister findById(Long id) {
		LogUtil.log("finding PtKkxJFjregister instance with id: " + id,
				Level.INFO, null);
		try {
			PtKkxJFjregister instance = entityManager.find(
					PtKkxJFjregister.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtKkxJFjregister> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtKkxJFjregister instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtKkxJFjregister model where model."
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
	public List<PtKkxJFjregister> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtKkxJFjregister instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtKkxJFjregister model";
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

	public PageObject getRecordList(String month, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.fj_id, \n"
			+ "a.str_month, \n"
			+ "a.fj_code, \n"
			+ "a.jzzt_id, \n"
			+ "a.keep_time, \n"
			+ "a.standby_num, \n"
			+ "a.repair_mandays, \n"
			+ "a.repair_cost, \n"
			+ "a.event_code, \n"
			+ "a.event_reason, \n"
			+ "a.event_other_reason, \n"
			+ "a.entry_by, \n"
			+ "b.fj_name, \n"
			+ "c.jzzt_name, \n"
			+ "to_char(a.start_date,'yyyy-MM-dd hh24:mi:ss'), \n"
			+ "to_char(a.end_date,'yyyy-MM-dd hh24:mi:ss'), \n"
			+ "to_char(a.entry_date,'yyyy-MM-dd'), \n"
			+ "getworkername(a.entry_by) \n"
			+ "from PT_KKX_J_FJREGISTER a,PT_KKX_C_FJCODE b,PT_KKX_C_FJSTATE c \n"
			+ "where a.fj_code=b.fj_code(+) \n"
			+ "and a.jzzt_id=c.jzzt_id(+) \n"			
			+ "and a.enterprise_code='" + enterpriseCode + "' \n";
		if(month != null && !month.equals(""))
			sql +=  "and a.str_month='" + month + "' \n";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<PtKkxJFjregisterForm> arrlist = new ArrayList<PtKkxJFjregisterForm>();
		if(list != null && list.size() > 0)
		{
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				PtKkxJFjregisterForm form = new PtKkxJFjregisterForm();
				Object[] da = (Object[])it.next();
				if(da[0] != null)
					form.setFjId(Long.parseLong(da[0].toString()));
				if(da[1] != null)
					form.setStrMonth(da[1].toString());
				if(da[2] != null)
					form.setFjCode(da[2].toString());
				if(da[3] != null)
					form.setJzztId(Long.parseLong(da[3].toString()));
				if(da[4] != null)
					form.setKeepTime(Double.parseDouble(da[4].toString()));
				if(da[5] != null)
					form.setStandbyNum(Long.parseLong(da[5].toString()));
				if(da[6] != null)
					form.setRepairMandays(Double.parseDouble(da[6].toString()));
				if(da[7] != null)
					form.setRepairCost(Double.parseDouble(da[7].toString()));
				if(da[8] != null)
					form.setEventCode(da[8].toString());
				if(da[9] != null)
					form.setEventReason(da[9].toString());
				if(da[10] != null)
					form.setEventOtherReason(da[10].toString());
				if(da[11] != null)
					form.setEntryBy(da[11].toString());
				if(da[12] != null)
					form.setFjName(da[12].toString());
				if(da[13] != null)
					form.setJzztName(da[13].toString());
				if(da[14] != null)
					form.setStartDateString(da[14].toString());
				if(da[15] != null)
					form.setEndDateString(da[15].toString());
				if(da[16] != null)
					form.setEntryDateString(da[16].toString());
				if(da[17] != null)
					form.setEntryName(da[17].toString());
				arrlist.add(form);
			}
		}
		pg.setList(arrlist);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setTotalCount(totalCount);
		return pg;
	}

	public void modifyRec(List<PtKkxJFjregister> addList,
			List<PtKkxJFjregister> updateList, String ids) {
		
		if(addList != null && addList.size() > 0)
		{
			for(PtKkxJFjregister temp : addList)
			{
				this.save(temp);
				entityManager.flush();
			}
		}
		
		if(updateList != null && updateList.size() > 0)
		{
			for(PtKkxJFjregister temp : updateList)
			{
				this.update(temp);
			}
		}
		
		if(ids != null && ids.length() > 0)
		{
			String sql = "delete from PT_KKX_J_FJREGISTER a \n"
				+ "where a.FJ_ID in (" + ids + ") \n";
			bll.exeNativeSQL(sql);
		}
	}

}