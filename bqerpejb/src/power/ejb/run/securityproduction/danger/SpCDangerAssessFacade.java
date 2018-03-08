package power.ejb.run.securityproduction.danger;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.Employee;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;


@Stateless
public class SpCDangerAssessFacade implements SpCDangerAssessFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	
	


	public SpCDangerAssess save(SpCDangerAssess entity) {
		LogUtil.log("saving SpCDangerAssess instance", Level.INFO, null);
		try {
			
			entity.setAssessId(bll.getMaxId("SP_C_DANGER_ASSESS", "assess_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;
	}

	
	public void delete(String ids) {
		String sql=
			"update SP_C_DANGER_ASSESS t\n" +
			" set t.Is_Use='N' where t.Assess_Id in ("+ids+")";
		bll.exeNativeSQL(sql);
	}

	
	public SpCDangerAssess update(SpCDangerAssess entity) {
		LogUtil.log("updating SpCDangerAssess instance", Level.INFO, null);
		try {
			SpCDangerAssess result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpCDangerAssess findById(Long id) {
		LogUtil.log("finding SpCDangerAssess instance with id: " + id,
				Level.INFO, null);
		try {
			SpCDangerAssess instance = entityManager.find(
					SpCDangerAssess.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	


	
	@SuppressWarnings("unchecked")
	public List<SpCDangerAssess> findAll(final int... rowStartIdxAndCount) {
		String sql = "select * from SP_C_DANGER_ASSESS";
		return bll.queryByNativeSQL(sql, SpCDangerAssess.class, rowStartIdxAndCount);
	}
	

	@SuppressWarnings("unchecked")
	public List<SpCDangerAssess> findByKeyword(String name,String sort,String enterprise_code,final int... rowStartIdxAndCount) {
		String queryString ="";
		if("".equals(sort)||sort==null){
			queryString = "select t.assess_id,t.file_name,t.assess_sort,t.annex,GETWORKERNAME(t.entry_by) as entry_by,t.entry_date,t.is_use,t.enterprise_code from SP_C_DANGER_ASSESS t where t.File_Name like '%"+name+"%' and t.Is_Use='Y' and t.Enterprise_Code='"+enterprise_code+"' order by assess_id desc";
		}else{
			queryString = "select t.assess_id,t.file_name,t.assess_sort,t.annex,GETWORKERNAME(t.entry_by) as entry_by,t.entry_date,t.is_use,t.enterprise_code from SP_C_DANGER_ASSESS t where t.File_Name like '%"+name+"%' and t.Assess_Sort = '"+sort+"' and Is_Use='Y' and t.Enterprise_Code='"+enterprise_code+"'  order by assess_id desc";
		}
		return bll.queryByNativeSQL(queryString, SpCDangerAssess.class, rowStartIdxAndCount);
	}
	
	
	public int getCount(String name,String sort,String enterprise_code){
		String queryString ="";
		if("".equals(sort)||sort==null){
			queryString = "select * from SP_C_DANGER_ASSESS where File_Name like '%"+name+"%' and Is_Use='Y' and Enterprise_Code='"+enterprise_code+"' ";
		}else{
			queryString = "select * from SP_C_DANGER_ASSESS where File_Name like '%"+name+"%' and Assess_Sort = '"+sort+"' and Is_Use='Y' and Enterprise_Code='"+enterprise_code+"' ";
		}
		return bll.queryByNativeSQL(queryString, SpCDangerAssess.class).size();
	}

}