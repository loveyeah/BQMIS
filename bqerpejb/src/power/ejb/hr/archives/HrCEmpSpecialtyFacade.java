package power.ejb.hr.archives;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrCEmpSpecialty.
 * 
 * @see power.ejb.hr.archives.HrCEmpSpecialty
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCEmpSpecialtyFacade implements HrCEmpSpecialtyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public HrCEmpSpecialty save(HrCEmpSpecialty entity) {
		LogUtil.log("saving HrCEmpSpecialty instance", Level.INFO, null);
		try {
			entity.setSpecialtyId(bll.getMaxId("hr_c_emp_Specialty", "specialty_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids)
	{
		String sql=
			"update hr_c_emp_Specialty t\n" +
			"   set t.is_use = 'N'\n" + 
			" where t.specialty_id in ("+ids+")";
       bll.exeNativeSQL(sql);
	}

	public HrCEmpSpecialty update(HrCEmpSpecialty entity) {
		LogUtil.log("updating HrCEmpSpecialty instance", Level.INFO, null);
		try {
			HrCEmpSpecialty result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCEmpSpecialty findById(Long id) {
		LogUtil.log("finding HrCEmpSpecialty instance with id: " + id,
				Level.INFO, null);
		try {
			HrCEmpSpecialty instance = entityManager.find(
					HrCEmpSpecialty.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findSepecialtyList(String empId,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql = "select a.specialty_id,\n" +
			"       a.emp_id,\n" + 
			"       i.new_emp_code,\n" + 
			"       i.chs_name,\n" + 
			"       a.specialty_name,\n" + 
			"       a.specialty_level,\n" + 
			"       a.award_unit,\n" + 
			"       to_char(a.award_date, 'yyyy-MM-dd'),\n" + 
			"       a.memo\n" + 
			"  from hr_c_emp_Specialty a, hr_j_emp_info i\n" + 
			" where a.emp_id = i.emp_id\n" + 
			"   and a.emp_id = "+empId+"\n" + 
			"   and i.is_use = 'Y'\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and i.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'";
		
		String sqlCount = "select count(1) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}
	
	@SuppressWarnings("unchecked")
	public void importEmpSpeFilesInfo(List<HrCEmpSpecialty> empList) {
		
		for(HrCEmpSpecialty emp : empList){
			emp = this.save(emp);
			entityManager.flush();
//			String sql = "select a.* from hr_c_emp_Specialty a where a.specialty_id='"+emp.getSpecialtyId()+"'";
//			List<HrCEmpSpecialty> selectList = bll.queryByNativeSQL(sql, HrCEmpSpecialty.class);
//			if(selectList != null && selectList.size() > 0){
//				HrCEmpSpecialty updated = selectList.get(0);
//				updated.setEmpId(emp.getEmpId());
//				updated.setAwardDate(emp.getAwardDate());
//				updated.setAwardUnit(emp.getAwardUnit());
//				updated.setMemo(emp.getMemo());
//				updated.setSpecialtyLevel(emp.getSpecialtyLevel());
//				updated.setSpecialtyName(emp.getSpecialtyName());
//				entityManager.merge(updated);
//			}else{
//				Long nextId = bll.getMaxId("hr_c_emp_Specialty", "specialty_id");
//				emp.setSpecialtyId(nextId++);
//				entityManager.persist(emp);
//			}
		}
	}
}