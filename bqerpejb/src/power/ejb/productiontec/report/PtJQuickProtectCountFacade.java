package power.ejb.productiontec.report;

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
import power.ejb.productiontec.report.form.PtJProtectCheckupForm;
import power.ejb.productiontec.report.form.QuickProtectCountForm;

/**
 * 快速保护（线路高频保护及元件差动保护）统计报表
 * @author liuyi 091016
 */
@Stateless
public class PtJQuickProtectCountFacade implements
		PtJQuickProtectCountFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;


	public void save(PtJQuickProtectCount entity) {
		LogUtil.log("saving PtJQuickProtectCount instance", Level.INFO, null);
		try {
			entity.setChekupId(bll.getMaxId("PT_J_QUICK_PROTECT_COUNT", "CHEKUP_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(PtJQuickProtectCount entity) {
		LogUtil.log("deleting PtJQuickProtectCount instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJQuickProtectCount.class,
					entity.getChekupId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PtJQuickProtectCount update(PtJQuickProtectCount entity) {
		LogUtil.log("updating PtJQuickProtectCount instance", Level.INFO, null);
		try {
			PtJQuickProtectCount result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJQuickProtectCount findById(Long id) {
		LogUtil.log("finding PtJQuickProtectCount instance with id: " + id,
				Level.INFO, null);
		try {
			PtJQuickProtectCount instance = entityManager.find(
					PtJQuickProtectCount.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtJQuickProtectCount> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJQuickProtectCount instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJQuickProtectCount model where model."
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
	public List<PtJQuickProtectCount> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJQuickProtectCount instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from PtJQuickProtectCount model";
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


	public PageObject findAllByMonth(String month, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.chekup_id, \n"
			+ "a.str_month, \n"
			+ "a.protect_equ, \n"
			+ "a.protect_device, \n"
			+ "a.PROTECT_TYPE, \n"
			+ "a.EXIT_REASON, \n"			
			+ "a.approve_by, \n"
			+ "a.check_by, \n"
			+ "a.entry_by, \n"
			+ "a.protect_equ as equname, \n"
			+ "a.protect_device as devicename, \n"
			+ "a.PROTECT_TYPE as protecttype, \n"
			+ "getworkername(a.approve_by), \n"
			+ "getworkername(a.check_by), \n"
			+ "getworkername(a.entry_by), \n"
			+ "to_char(a.EXIT_DATE,'yyyy-MM-dd'), \n"
			+ "to_char(a.RESUME_DATE,'yyyy-MM-dd'), \n"
			+ "to_char(a.entry_date,'yyyy-MM-dd') \n"
			+ "from PT_J_QUICK_PROTECT_COUNT a \n"
			+ "where a.enterprise_code='" + enterpriseCode + "' \n";
		if(month != null && !month.equals(""))
			sql += "and a.str_month='" + month + "' \n";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<QuickProtectCountForm> arrlist = new ArrayList<QuickProtectCountForm>();
		if(list != null && list.size() > 0)
		{
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				QuickProtectCountForm form = new QuickProtectCountForm();
				Object[] da = (Object[])it.next();
				if(da[0] != null)
					form.setChekupId(Long.parseLong(da[0].toString()));
				if(da[1] != null)
					form.setStrMonth(da[1].toString());
				if(da[2] != null)
					form.setProtectEqu(da[2].toString());
				if(da[3] != null)
					form.setProtectDevice(da[3].toString());
				if(da[4] != null)
					form.setProtectType(da[4].toString());
				if(da[5] != null)
					form.setExitReason(da[5].toString());
//				if(da[6] != null)
//					form.setHasProblem(da[6].toString());
				if(da[6] != null)
					form.setApproveBy(da[6].toString());
				if(da[7] != null)
					form.setCheckBy(da[7].toString());
				if(da[8] != null)
					form.setEntryBy(da[8].toString());
				if(da[9] != null)
					form.setProtectEquName(da[9].toString());
				if(da[10] != null)
					form.setProtectDeviceName(da[10].toString());
				if(da[11] != null)
					form.setProtectTypeName(da[11].toString());
				if(da[12] != null)
					form.setApproveName(da[12].toString());
				else
					form.setApproveName("");
				if(da[13] != null)
					form.setCheckName(da[13].toString());
				else
					form.setCheckName("");
				if(da[14] != null)
					form.setEntryName(da[14].toString());
				else 
					form.setEntryName("");
				if(da[15] != null)
					form.setExitDateString(da[15].toString());
				if(da[16] != null)
					form.setResumeDateString(da[16].toString());				
				if(da[17] != null)
					form.setEntryDateString(da[17].toString());
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}


	public void saveMod(List<PtJQuickProtectCount> addList,
			List<PtJQuickProtectCount> updateList, String ids) {
		if(addList != null && addList.size() > 0)
		{
			for(PtJQuickProtectCount entity : addList)
			{
				this.save(entity);
				entityManager.flush();
			}
		}
		if(updateList != null && updateList.size() > 0)
		{
			for(PtJQuickProtectCount entity : updateList)
			{
				this.update(entity);
			}
		}
		if(ids != null && ids.length() > 0)
		{
			String sql = "delete from PT_J_QUICK_PROTECT_COUNT a where a.CHEKUP_ID in (" + ids + ") \n";
			bll.exeNativeSQL(sql);
		}
		
	}

}