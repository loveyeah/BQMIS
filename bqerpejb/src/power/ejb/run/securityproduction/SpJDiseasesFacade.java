package power.ejb.run.securityproduction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.run.securityproduction.form.EmployeeInfo;
import power.ejb.run.securityproduction.form.SpDiseasesInfo;

/**
 * Facade for entity SpJDiseases.
 * 
 * @see power.ejb.run.securityproduction.SpJDiseases
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJDiseasesFacade implements SpJDiseasesFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public SpJDiseases save(SpJDiseases entity) {
		LogUtil.log("saving SpJDiseases instance", Level.INFO, null);
		try {
			entity.setMedicalId(Long.parseLong(this.createMedicaCode()));
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
		String sql = "delete sp_j_diseases b where b.medical_id in(" + ids + ")";
		bll.exeNativeSQL(sql);
	}
	
	public SpJDiseases update(SpJDiseases entity) {
		LogUtil.log("updating SpJDiseases instance", Level.INFO, null);
		try {
			SpJDiseases result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJDiseases findById(Long id) {
		LogUtil.log("finding SpJDiseases instance with id: " + id, Level.INFO,
				null);
		try {
			SpJDiseases instance = entityManager.find(SpJDiseases.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 取得人员详细信息
	 * @param workerCode 工号
	 * @return EmployeeInfo
	 */
	@SuppressWarnings("unchecked")
	public  EmployeeInfo getEmpInfoDetail(String workerCode)
	{
		EmployeeInfo result = null;
		String sql = "select a.emp_code,\n"
				+ "       decode(a.sex,'M','男','W','女')sex,\n"
				+ "       n.dept_name,\n"
				+ "       d.station_name,\n"
				+ "       f.emp_type_name,\n"
				+ "       a.social_insurance_id,\n"
				+ "       a.curriculum_vitae,\n"
				+ "       getworkername(a.emp_code),\n"
				+ "        n.dept_code\n"
				+ "  from hr_j_emp_info a, hr_c_station d, hr_c_emp_type f, hr_c_dept n\n"
				+ " where a.station_id = d.station_id(+)\n"
				+ "   and a.emp_type_id = f.emp_type_id(+)\n"
				+ "   and a.dept_id = n.dept_id(+)\n"
				+ "   and a.emp_code = '"+workerCode+"'\n" 
				+ "   and d.is_use(+) = 'Y'\n" //update by sychen 20100902
//				+ "   and d.is_use(+) = 'U'\n" 
				+ "   and f.is_use(+) = 'Y'\n"  //update by sychen 20100902
//				+ "   and f.is_use(+) = 'U'\n" 
				+ "   and n.is_use(+) = 'Y'"; //update by sychen 20100902
//				+ "   and n.is_use(+) = 'U'"; 
		    List list = bll.queryByNativeSQL(sql);
		    if(list !=null && list.size()>0)
		    {
		    	result = new EmployeeInfo();
		    	Object[] r = (Object[])list.get(0);
		    	if(r[0] !=null)
		    	{
		    		result.setEmpCode(r[0].toString());
		    		
		    	}
		    	if(r[1] !=null)
		    	{
		    		result.setSex(r[1].toString());
		    		
		    	}
		    	if(r[2] !=null)
		    	{
		    		result.setDeptName(r[2].toString());
		    		
		    	}
		    	if(r[3] !=null)
		    	{
		    		result.setWorkStationName(r[3].toString());
		    		
		    	}
		    	if(r[4] != null)
		    	{
		    		result.setEmpType(r[4].toString());
		    	}
		    	if(r[5] !=null)
		    	{
		    		result.setSocialInsuranceId(r[5].toString());
		    		
		    	}
		    	if(r[6] !=null)
		    	{
		    		result.setCurriculumVitae(r[6].toString());	    		
		    	}
		    	if(r[7] != null)
		    	{
		    		result.setEmpName(r[7].toString());
		    	}
		    	if(r[8] != null)
		    	{
		    		result.setDeptCode(r[8].toString());
		    	}
		    	
		    }
		return result; 
	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String workName, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();

		String sql = "select a.medical_id,\n" 
				+ "       a.worker_code,\n"
				+ "       getworkername(a.worker_code),\n"
				+ "       a.contact_harm,\n" 
				+ "       a.contact_year,\n"
				+ "       a.check_date,\n" 
				+ "       a.hospital,\n"
				+ "       a.content,\n"
				+ "       a.check_result,\n"
				+ "       a.memo\n" 
				+ "  from sp_j_diseases a\n"
				+ " where a.enterprise_code = '" + enterpriseCode + "'";

		String sqlCount = "select count(1) from sp_j_diseases a\n"
				+ "where a.enterprise_code = '" + enterpriseCode + "'";
		String strWhere = "";
		if (workName != null && workName.length() > 0) {
			strWhere += " and getworkername(a.worker_code) like '%" + workName
					+ "%'";
		}
		sql += strWhere;
		sql = sql + " order by a.medical_id";
		sqlCount += strWhere;
		sqlCount = sqlCount + " order by a.medical_id";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while (it.hasNext()) {
				SpDiseasesInfo info = new SpDiseasesInfo();
				Object []data = (Object[])it.next();
				info.setMedicalId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					info.setWorkerCode(data[1].toString());
				if(data[2] != null)
					info.setWorkerName(data[2].toString());
				if(data[3] != null)
					info.setContactHarm(data[3].toString());
				if(data[4] != null)
					info.setContactYear(Long.parseLong(data[4].toString()));
				if(data[5] != null)
					info.setCheckDate(data[5].toString());
				if(data[6] != null)
					info.setHospital(data[6].toString());
				if(data[7] != null)
					info.setContent(data[7].toString());
				if(data[8] != null)
					info.setCheckResult(data[8].toString());
				if(data[9] != null)
					info.setMemo(data[9].toString());
				arrlist.add(info);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

	@SuppressWarnings("unused")
	private String createMedicaCode() {
		String mymonth = "";
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyyy-MM-dd" + " " + "hh:mm:ss");
		mymonth = tempDate.format(new java.util.Date());
		mymonth = mymonth.substring(0, 4) + mymonth.substring(5, 7);
		String no = mymonth;
		String sql = "select '"
				+ no
				+ "' ||\n"
				+ "       (select Trim(case\n"
				+ "                 when max(t.medical_id) is null then\n"
				+ "                  '001'\n"
				+ "                 else\n"
				+ "                  to_char(to_number(substr(max(t.medical_id), 8, 3) + 1),\n"
				+ "                          '000')\n"
				+ "               end)\n"
				+ "          from sp_j_diseases t\n"
				+ "         where  substr(t.medical_id, 0, 6) = '" + no + "')\n"
				+ "  from dual";
		no = bll.getSingal(sql).toString().trim();
		return no;
	}
}