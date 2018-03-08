package power.ejb.monthaward;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import power.ejb.hr.salary.form.BasisSalaryForm;

/**
 * Facade for entity HrCMonthAward.
 * 
 * @see power.ejb.administration.HrCMonthAward
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCMonthAwardFacade implements HrCMonthAwardFacadeRemote {
	// property constants
	public static final String MONTH_AWARD = "monthAward";
	public static final String MEMO = "memo";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	public PageObject findMonAward(String Time,String enterpriseCode,final int... rowStartIdxAndCount) throws ParseException
	{
		PageObject pg = new PageObject();
		String sql =
			"select a.month_award_id," +
			"a.month_award," +
			"to_char(a.effect_start_time,'yyyy-MM')," +
			"to_char(a.effect_end_time,'yyyy-MM')," +
			"a.memo," +
			"a.is_use,\n" +
			"decode(a.effect_start_time,\n" + 
			"              (select max(b.effect_start_time)\n" + 
			"                 from HR_C_MONTH_AWARD b\n" + 
			"                where b.is_use = 'Y'\n" + 
			"                  and b.enterprise_code = '"+enterpriseCode+"'),\n" + 
			"              '1',\n" + 
			"              '0') status\n"+
			"from HR_C_MONTH_AWARD  a\n" + 
			"where a.is_use='Y'\n" + 
			"and a.enterprise_code='"+enterpriseCode+"'";

		
		String whereStr = "";
		if (Time != null && Time.length() > 0) {
			whereStr +=" and to_char(a.effect_start_time,'yyyy-MM') <= '"+Time+"'";
			whereStr +=" and to_char(a.effect_end_time,'yyyy-MM') >= '"+Time+"'";
		}
		sql += whereStr;
		String sqlCount = "select count(1) from ("+sql+")";
		sql += " order by a.effect_start_time";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<monthAwardForm> arrlist = new ArrayList();
		while (it.hasNext()) {
			monthAwardForm form = new monthAwardForm();
			Object[] data = (Object[]) it.next();
			if(data[0] != null)
				form.setMonthAwardId((Long.parseLong(data[0].toString())));
			if(data[1] != null)
				form.setMonthAward((Double.parseDouble(data[1].toString())));
			if(data[2] != null)
				{
				//DateFormat sf = new SimpleDateFormat("yyyy-MM");
				form.setEffectStartTime(data[2].toString());
				}
			if(data[3] != null)
			{  //DateFormat sf = new SimpleDateFormat("yyyy-MM");
				form.setEffectEndTime((data[3].toString()));
			}
			if(data[4] != null)
				form.setMemo(data[4].toString());
			if(data[5] != null)
				form.setIsUse(data[5].toString());
			if(data[6] != null)
				form.setStatus(data[6].toString());
			
			arrlist.add(form);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}
	public void deleteMonAward(String ids)
	{
		String sql="update HR_C_MONTH_AWARD a\n" +
		"   set a.is_use = 'N'\n" + 
		" where a.month_award_id in ("+ids+")";

        bll.exeNativeSQL(sql);
	}
	public void deleteMonStandDays(String ids)
	{
		String sql="update  HR_C_MONTH_STANDARDDAYS  s\n" +
		"   set s.is_use = 'N'\n" + 
		" where  s.standarddays_id in ("+ids+")";

        bll.exeNativeSQL(sql);
		
	}
	public PageObject findMonAwardDays(String equTime,String enterpriseCode,final int... rowStartIdxAndCount) throws ParseException
	{
		PageObject pg = new PageObject();
		String sql =

			"select s.standarddays_id," +
			"s.standarddays," +
			"to_char(s.effect_start_time,'yyyy-MM')," +
			"to_char(s.effect_end_time,'yyyy-MM')," +
			"s.memo," +
			"s.is_use," +
			"decode(s.effect_start_time,\n" + 
			"              (select max(b.effect_start_time)\n" + 
			"                 from HR_C_MONTH_STANDARDDAYS b\n" + 
			"                where b.is_use = 'Y'\n" + 
			"                  and b.enterprise_code = '"+enterpriseCode+"'),\n" + 
			"              '1',\n" + 
			"              '0') status\n" +
			"from  HR_C_MONTH_STANDARDDAYS  s\n" + 
			"where s.is_use='Y'\n" + 
			"and s.enterprise_code='"+enterpriseCode+"'";


		
		String whereStr = "";
		if (equTime != null && equTime.length() > 0) {
			whereStr +=" and to_char(s.effect_start_time,'yyyy-MM') <= '"+equTime+"'";
			whereStr +=" and to_char( s.effect_end_time,'yyyy-MM') >= '"+equTime+"'";
		}
		sql += whereStr;
		String sqlCount = "select count(1) from ("+sql+")";
		sql += " order by s.effect_start_time";
		System.out.println("the sql is"+sql);
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<monthStandDaysFrom> arrlist = new ArrayList();
		while (it.hasNext()) {
			monthStandDaysFrom form = new monthStandDaysFrom();
			Object[] data = (Object[]) it.next();
			if(data[0] != null)
				form.setStandarddaysId((Long.parseLong(data[0].toString())));
			if(data[1] != null)
				form.setStandarddays((Double.parseDouble(data[1].toString())));
			if(data[2] != null)
				{
				//DateFormat sf = new SimpleDateFormat("yyyy-MM");
				form.setEffectStartTime(data[2].toString());
				}
			if(data[3] != null)
			{ // DateFormat sf = new SimpleDateFormat("yyyy-MM");
				form.setEffectEndTime((data[3].toString()));
			}
			if(data[4] != null)
				form.setMemo(data[4].toString());
			if(data[5] != null)
				form.setIsUse(data[5].toString());
			if(data[6] != null)
				form.setStatus(data[6].toString());
			
			arrlist.add(form);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
		
		
		
	}
		
		
	


	/**
	 * Perform an initial save of a previously unsaved HrCMonthAward entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCMonthAward entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCMonthAward entity) {
		try {
			System.out.println("the methos is"+entity);
			entity.setMonthAwardId(bll.getMaxId("HR_C_MONTH_AWARD",
					"month_award_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void saveStandar(HrCMonthStandarddays entity) {
		try {
			System.out.println("the methos is"+entity);
			entity.setStandarddaysId(bll.getMaxId("HR_C_MONTH_STANDARDDAYS",
					"standarddays_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCMonthAward entity.
	 * 
	 * @param entity
	 *            HrCMonthAward entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCMonthAward entity) {
		LogUtil.log("deleting HrCMonthAward instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCMonthAward.class, entity
					.getMonthAwardId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCMonthAward entity and return it or a copy
	 * of it to the sender. A copy of the HrCMonthAward entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCMonthAward entity to update
	 * @return HrCMonthAward the persisted HrCMonthAward entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCMonthAward update(HrCMonthAward entity) {
		LogUtil.log("updating HrCMonthAward instance", Level.INFO, null);
		try {
			HrCMonthAward result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	public HrCMonthStandarddays updateAwardDays(HrCMonthStandarddays entity) {
		LogUtil.log("updating HrCMonthStandarddays instance", Level.INFO, null);
		try {
			HrCMonthStandarddays result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCMonthAward findById(Long id) {
		LogUtil.log("finding HrCMonthAward instance with id: " + id,
				Level.INFO, null);
		try {
			HrCMonthAward instance = entityManager
					.find(HrCMonthAward.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	public HrCMonthStandarddays findByDaysId(Long id) {
		LogUtil.log("finding HrCMonthStandarddays instance with id: " + id,
				Level.INFO, null);
		try {
			HrCMonthStandarddays instance = entityManager
					.find(HrCMonthStandarddays.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	

}