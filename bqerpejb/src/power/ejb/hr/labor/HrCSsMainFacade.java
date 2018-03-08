package power.ejb.hr.labor;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrCSsMain.
 * 
 * @see power.ejb.hr.labor.HrCSsMain
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCSsMainFacade implements HrCSsMainFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;


	public HrCSsMain save(HrCSsMain entity) {
		LogUtil.log("saving HrCSsMain instance", Level.INFO, null);
		try {
			entity.setMainId(bll.getMaxId("HR_C_SS_MAIN", "main_id"));
			entity.setImportDate(new Date());
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(String ids)
	{
		String sql=
			"update HR_C_SS_MAIN t\n" +
			"set t.is_use='N'\n" + 
			"where t.main_id in ("+ids+")";
		bll.exeNativeSQL(sql);
	}


	public HrCSsMain update(HrCSsMain entity) {
		LogUtil.log("updating HrCSsMain instance", Level.INFO, null);
		try {
			entity.setImportDate(new Date());
			HrCSsMain result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCSsMain findById(Long id) {
		LogUtil.log("finding HrCSsMain instance with id: " + id, Level.INFO,
				null);
		try {
			HrCSsMain instance = entityManager.find(HrCSsMain.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public HrCSsMain findSsMainInfo(String strYear,String yearType,String insuranceType,String enterpriseCode)
	{
		String sql=
			"select *\n" +
			"  from HR_C_SS_MAIN t\n" + 
			" where t.is_use = 'Y'\n" + 
			"   and t.main_year = '"+strYear+"'\n" + 
			"   and t.year_type = '"+yearType+"'\n" + 
			"   and t.insurance_type = '"+insuranceType+"'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'";

		List<HrCSsMain> list=bll.queryByNativeSQL(sql, HrCSsMain.class);
		if(list!=null&&list.size()>0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}
	
	

}