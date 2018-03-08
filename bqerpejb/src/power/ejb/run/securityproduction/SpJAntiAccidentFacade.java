package power.ejb.run.securityproduction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.run.securityproduction.form.SpJAntiAccidentForm;

/**
 * @author liuyi 090917
 */
@Stateless
public class SpJAntiAccidentFacade implements SpJAntiAccidentFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "SpJAntiAccidentDetailsFacade")
	protected SpJAntiAccidentDetailsFacadeRemote remote;

	public String save(SpJAntiAccident entity, String measureCode) {
		try {
			entity.setMeasureCode(this.createCode(measureCode));
			entityManager.persist(entity);
			return entity.getMeasureCode();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public void delete(SpJAntiAccident entity) {
		LogUtil.log("deleting SpJAntiAccident instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SpJAntiAccident.class, entity
					.getMeasureCode());
			entityManager.remove(entity);
			List<SpJAntiAccidentDetails> list = remote.findByCode(entity
					.getMeasureCode());
			if (list != null) {
				Iterator it = list.iterator();
				StringBuilder sb=new StringBuilder();
				while(it.hasNext()){
					SpJAntiAccidentDetails m=(SpJAntiAccidentDetails)it.next();
					sb.append(m.getDetailsId());
					sb.append(",");
				}
				if(sb.length()>0){
					String str=sb.toString();
					remote.delete(str.substring(0,str.length()-1));
				}
				
			}
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJAntiAccident update(SpJAntiAccident entity) {
		LogUtil.log("updating SpJAntiAccident instance", Level.INFO, null);
		try {
			SpJAntiAccident result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJAntiAccident findById(String id) {
		LogUtil.log("finding SpJAntiAccident instance with id: " + id,
				Level.INFO, null);
		try {
			SpJAntiAccident instance = entityManager.find(
					SpJAntiAccident.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 生成25项反事故措施编码
	 * 
	 * @param measureCode
	 *            25项反措施编码 如果measureCode为0，生成的编码为01-25
	 *            如果measureCode为01-25，生成的编码为0101-2599
	 *             如果measureCode为0101-2599，生成的编码为010101-259999
	 * @return String
	 */
	private String createCode(String measureCode) {
		String mCode = "";
		if ("0".equals(measureCode)) {
			// 生成的编码为01-25
			String sql = "select trim(decode(to_char(max(t.measure_code) + 1, '00'),\n"
					+ "              null,\n"
					+ "              '01',\n"
					+ "              to_char(max(t.measure_code) + 1, '00')))\n"
					+ "  from SP_J_ANTI_ACCIDENT t\n"
					+ " where length(t.measure_code) = 2";
			mCode = bll.getSingal(sql).toString();
		} else if(measureCode.length() == 2){
			// 生成的编码为0101-2599
			String sql = "select trim(decode(to_char(max(t.measure_code) + 1, '0000'),\n"
					+ "              null,\n"
					+ "              '"
					+ measureCode
					+ "' || '01',\n"
					+ "              to_char(max(t.measure_code) + 1, '0000')))\n"
					+ "  from SP_J_ANTI_ACCIDENT t\n"
					+ " where substr(t.measure_code, 0, 2) = '"
					+ measureCode
					+ "' and length(t.measure_code)=4";
			mCode = bll.getSingal(sql).toString();
		}else
		{
			// 生成的编码为010101-259999
			String sql = "select trim(decode(to_char(max(t.measure_code) + 1, '000000'),\n"
					+ "              null,\n"
					+ "              '"
					+ measureCode
					+ "' || '01',\n"
					+ "              to_char(max(t.measure_code) + 1, '000000')))\n"
					+ "  from SP_J_ANTI_ACCIDENT t\n"
					+ " where substr(t.measure_code, 0, 4) = '"
					+ measureCode
					+ "' and length(t.measure_code)=6";
			mCode = bll.getSingal(sql).toString();
		}
		return mCode;
	}

	@SuppressWarnings("unchecked")
	public void saveModified(SpJAntiAccident entity, String opFlag,
			List addList, List updateList, String deleteCode) {
		// opFlag为a父编码，新增操作，u：修改操作，d：删除操作；如果删除操作，addList，updateList，deleteList为null
		String mCode = "";
		if (opFlag.indexOf("a") != -1) {
			String measureCode = opFlag.substring(1, opFlag.length());
			mCode = this.save(entity, measureCode);
			remote.saveModified(addList, updateList, deleteCode, mCode);
		} else if (opFlag.equals("u")) {
			SpJAntiAccident model = this.update(entity);
			mCode = model.getMeasureCode();
			remote.saveModified(addList, updateList, deleteCode, mCode);
		} else {
			this.delete(entity);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SpJAntiAccidentForm> findByParentCode(String check,String parentCode,String fdManager) {
		String sql = "";	
		if(check == null)
		{
			if (parentCode.equals("0"))
			{
				sql = "select a.measure_code, \n"
					+ "a.measure_name, \n"
					+ "a.special_code, \n"
					+ "getspecialname(a.special_code), \n"
					+ "a.fd_duty_leader, \n"
					+ "getworkername(a.fd_duty_leader), \n"
					+ "a.fd_manager, \n"
					+ "getworkername(a.fd_manager), \n"
					+ "a.fd_technology_by, \n"
					+ "getworkername(a.fd_technology_by), \n"
					+ "a.fd_supervise_by, \n"
					+ "getworkername(a.fd_supervise_by), \n"
					+ "a.dt_duty_leader, \n"
					+ "getworkername(a.dt_duty_leader), \n"
					+ "a.dt_manager, \n"
					+ "getworkername(a.dt_manager), \n"
					+ "a.dt_technology_by, \n"
					+ "getworkername(a.dt_technology_by), \n"
					+ "a.dt_supervise_by, \n"
					+ "getworkername(a.dt_supervise_by), \n"
					+ "a.entry_by, \n"
					+ "getworkername(a.entry_by), \n"
					+ "a.entry_dept, \n"
					+ "getdeptname(a.entry_dept), \n"
					+ "to_char(a.entry_date,'yyyy-MM-dd'), \n"
					+ "a.memo \n"
					+ "from sp_j_anti_accident a \n"
					+ "where a.is_use='Y' \n"
					+ "and length(a.measure_code)=2 \n"
					+ "order by a.measure_code \n";
			}
			else if(parentCode.length() == 2)
			{
				sql = "select a.measure_code, \n"
					+ "a.measure_name, \n"
					+ "a.special_code, \n"
					+ "getspecialname(a.special_code), \n"
					+ "a.fd_duty_leader, \n"
					+ "getworkername(a.fd_duty_leader), \n"
					+ "a.fd_manager, \n"
					+ "getworkername(a.fd_manager), \n"
					+ "a.fd_technology_by, \n"
					+ "getworkername(a.fd_technology_by), \n"
					+ "a.fd_supervise_by, \n"
					+ "getworkername(a.fd_supervise_by), \n"
					+ "a.dt_duty_leader, \n"
					+ "getworkername(a.dt_duty_leader), \n"
					+ "a.dt_manager, \n"
					+ "getworkername(a.dt_manager), \n"
					+ "a.dt_technology_by, \n"
					+ "getworkername(a.dt_technology_by), \n"
					+ "a.dt_supervise_by, \n"
					+ "getworkername(a.dt_supervise_by), \n"
					+ "a.entry_by, \n"
					+ "getworkername(a.entry_by), \n"
					+ "a.entry_dept, \n"
					+ "getdeptname(a.entry_dept), \n"
					+ "to_char(a.entry_date,'yyyy-MM-dd'), \n"
					+ "a.memo \n"
					+ "from sp_j_anti_accident a \n"
					+ "where a.is_use='Y' \n"
					+ "and length(a.measure_code)=4 \n"
					+ "and substr(a.measure_code,0,2)='"+ parentCode + "'\n"
					+ "order by a.measure_code \n";
			}
//			else if(parentCode.length() == 4)
//			{
//				sql = "select a.measure_code, \n"
//					+ "a.measure_name, \n"
//					+ "a.special_code, \n"
//					+ "getspecialname(a.special_code), \n"
//					+ "a.fd_duty_leader, \n"
//					+ "getworkername(a.fd_duty_leader), \n"
//					+ "a.fd_manager, \n"
//					+ "getworkername(a.fd_manager), \n"
//					+ "a.fd_technology_by, \n"
//					+ "getworkername(a.fd_technology_by), \n"
//					+ "a.fd_supervise_by, \n"
//					+ "getworkername(a.fd_supervise_by), \n"
//					+ "a.dt_duty_leader, \n"
//					+ "(select b.emp_name from sp_j_corp_empinfo b where b.is_use='Y' and a.dt_duty_leader=b.emp_id(+)), \n"
//					+ "a.dt_manager, \n"
//					+ "(select b.emp_name from sp_j_corp_empinfo b where b.is_use='Y' and a.dt_manager=b.emp_id(+)), \n"
//					+ "a.dt_technology_by, \n"
//					+ "(select b.emp_name from sp_j_corp_empinfo b where b.is_use='Y' and a.dt_technology_by=b.emp_id(+)), \n"
//					+ "a.dt_supervise_by, \n"
//					+ "(select b.emp_name from sp_j_corp_empinfo b where b.is_use='Y' and a.dt_supervise_by=b.emp_id(+)), \n"
//					+ "a.entry_by, \n"
//					+ "getworkername(a.entry_by), \n"
//					+ "a.entry_dept, \n"
//					+ "getdeptname(a.entry_dept), \n"
//					+ "to_char(a.entry_date,'yyyy-MM-dd'), \n"
//					+ "a.memo \n"
//					+ "from sp_j_anti_accident a \n"
//					+ "where a.is_use='Y' \n"
//					+ "and length(a.measure_code)=6 \n"
//					+ "and substr(a.measure_code,0,4)='"+ parentCode + "'\n"
//					+ "order by a.measure_code \n";
//			}
		}
		else if(check != null && check.equals("1"))
		{
			if (parentCode.equals("0"))
			{
				sql = "select a.measure_code, \n"
					+ "a.measure_name, \n"
					+ "a.special_code, \n"
					+ "getspecialname(a.special_code), \n"
					+ "a.fd_duty_leader, \n"
					+ "getworkername(a.fd_duty_leader), \n"
					+ "a.fd_manager, \n"
					+ "getworkername(a.fd_manager), \n"
					+ "a.fd_technology_by, \n"
					+ "getworkername(a.fd_technology_by), \n"
					+ "a.fd_supervise_by, \n"
					+ "getworkername(a.fd_supervise_by), \n"
					+ "a.dt_duty_leader, \n"
					+ "getworkername(a.dt_duty_leader), \n"
					+ "a.dt_manager, \n"
					+ "getworkername(a.dt_manager), \n"
					+ "a.dt_technology_by, \n"
					+ "getworkername(a.dt_technology_by), \n"
					+ "a.dt_supervise_by, \n"
					+ "getworkername(a.dt_supervise_by), \n"
					+ "a.entry_by, \n"
					+ "getworkername(a.entry_by), \n"
					+ "a.entry_dept, \n"
					+ "getdeptname(a.entry_dept), \n"
					+ "to_char(a.entry_date,'yyyy-MM-dd'), \n"
					+ "a.memo \n"
					+ "from sp_j_anti_accident a \n"
					+ "where a.is_use='Y' \n"
					+ "and a.measure_code like '" + parentCode + "%' \n"
					+ "and length(a.measure_code) = 2 \n"
					+ "and (select count(*) \n"
					+ "     from SP_J_ANTI_ACCIDENT t \n"
					+ "     where t.measure_code like a.measure_code || '%' \n"
					+ "     and t.FD_MANAGER = '" + fdManager + "' \n"
					+ "     and length(t.measure_code) = 6) <> 0"
					+ "order by a.measure_code \n";
			}
			else if(parentCode.length() == 2)
			{
				sql = "select a.measure_code, \n"
					+ "a.measure_name, \n"
					+ "a.special_code, \n"
					+ "getspecialname(a.special_code), \n"
					+ "a.fd_duty_leader, \n"
					+ "getworkername(a.fd_duty_leader), \n"
					+ "a.fd_manager, \n"
					+ "getworkername(a.fd_manager), \n"
					+ "a.fd_technology_by, \n"
					+ "getworkername(a.fd_technology_by), \n"
					+ "a.fd_supervise_by, \n"
					+ "getworkername(a.fd_supervise_by), \n"
					+ "a.dt_duty_leader, \n"
					+ "getworkername(a.dt_duty_leader), \n"
					+ "a.dt_manager, \n"
					+ "getworkername(a.dt_manager), \n"
					+ "a.dt_technology_by, \n"
					+ "getworkername(a.dt_technology_by), \n"
					+ "a.dt_supervise_by, \n"
					+ "getworkername(a.dt_supervise_by), \n"
					+ "a.entry_by, \n"
					+ "getworkername(a.entry_by), \n"
					+ "a.entry_dept, \n"
					+ "getdeptname(a.entry_dept), \n"
					+ "to_char(a.entry_date,'yyyy-MM-dd'), \n"
					+ "a.memo \n"
					+ "from sp_j_anti_accident a \n"
					+ "where a.is_use='Y' \n"
					+ "and a.measure_code like '" + parentCode + "%' \n"
					+ "and length(a.measure_code) = 4 \n"
					+ "and (select count(*) \n"
					+ "     from SP_J_ANTI_ACCIDENT t \n"
					+ "     where t.measure_code like a.measure_code || '%' \n"
					+ "     and t.FD_MANAGER = '" + fdManager + "' \n"
					+ "     and length(t.measure_code) = 6) <> 0"
					+ "order by a.measure_code \n";
			}
			else if(parentCode.length() == 4)
			{
				sql = "select a.measure_code, \n"
					+ "a.measure_name, \n"
					+ "a.special_code, \n"
					+ "getspecialname(a.special_code), \n"
					+ "a.fd_duty_leader, \n"
					+ "getworkername(a.fd_duty_leader), \n"
					+ "a.fd_manager, \n"
					+ "getworkername(a.fd_manager), \n"
					+ "a.fd_technology_by, \n"
					+ "getworkername(a.fd_technology_by), \n"
					+ "a.fd_supervise_by, \n"
					+ "getworkername(a.fd_supervise_by), \n"
					+ "a.dt_duty_leader, \n"
					+ "(select b.emp_name from sp_j_corp_empinfo b where b.is_use='Y' and a.dt_duty_leader=b.emp_id(+)), \n"
					+ "a.dt_manager, \n"
					+ "(select b.emp_name from sp_j_corp_empinfo b where b.is_use='Y' and a.dt_manager=b.emp_id(+)), \n"
					+ "a.dt_technology_by, \n"
					+ "(select b.emp_name from sp_j_corp_empinfo b where b.is_use='Y' and a.dt_technology_by=b.emp_id(+)), \n"
					+ "a.dt_supervise_by, \n"
					+ "(select b.emp_name from sp_j_corp_empinfo b where b.is_use='Y' and a.dt_supervise_by=b.emp_id(+)), \n"
					+ "a.entry_by, \n"
					+ "getworkername(a.entry_by), \n"
					+ "a.entry_dept, \n"
					+ "getdeptname(a.entry_dept), \n"
					+ "to_char(a.entry_date,'yyyy-MM-dd'), \n"
					+ "a.memo \n"
					+ "from sp_j_anti_accident a \n"
					+ "where a.is_use='Y' \n"
					+ "and a.measure_code like '" + parentCode + "%' \n"
					+ "and length(a.measure_code) = 6 \n"
					+ "and (select count(*) \n"
					+ "     from SP_J_ANTI_ACCIDENT t \n"
					+ "     where t.measure_code like a.measure_code || '%' \n"
					+ "     and t.FD_MANAGER = '" + fdManager + "' \n"
					+ "     and length(t.measure_code) = 6) <> 0"
					+ "order by a.measure_code \n";
			}
		}
		//by ghzhou 2009/10/22
		List list = null;
		if(!"".equals(sql))
		{
			list = bll.queryByNativeSQL(sql);
		}
		List<SpJAntiAccidentForm> arr = new ArrayList<SpJAntiAccidentForm>();
		if (list != null) {
			Iterator it = list.iterator();			
			while (it.hasNext()) {
				Object[] ob = (Object[]) it.next();
				SpJAntiAccidentForm form = new SpJAntiAccidentForm();
				if (ob[0] != null) {
					form.setMeasureCode(ob[0].toString());
				}
				if (ob[1] != null) {
					form.setMeasureName(ob[1].toString());
				}
				if (ob[2] != null) {
					form.setSpecialCode(ob[2].toString());
				}
				if (ob[3] != null) {
					form.setSpecialName(ob[3].toString());
				}
				if (ob[4] != null) {
					form.setFdDutyLeader(ob[4].toString());
				}
				if (ob[5] != null) {
					form.setFdDutyLeaderName(ob[5].toString());
				}
				if (ob[6] != null) {
					form.setFdManager(ob[6].toString());
				}
				if (ob[7] != null) {
					form.setFdManagerName(ob[7].toString());
				}
				if (ob[8] != null) {
					form.setFdTechnologyBy(ob[8].toString());
				}
				if (ob[9] != null) {
					form.setFdTechnologyName(ob[9].toString());
				}
				if (ob[10] != null) {
					form.setFdSuperviseBy(ob[10].toString());
				}
				if (ob[11] != null) {
					form.setFdSuperviseName(ob[11].toString());
				}
				if(ob[12] != null)
					form.setDtDutyLeader(ob[12].toString());
				if(ob[13] != null)
					form.setDtDutyLeaderName(ob[13].toString());	
			    if(ob[14] != null)
					form.setDtManager(ob[14].toString());	
				if(ob[15] != null)
					form.setDtManagerName(ob[15].toString());
				if(ob[16] != null)
					form.setDtTechnologyBy(ob[16].toString());
				if(ob[17] != null)
					form.setDtTechnologyName(ob[17].toString());
				if(ob[18] != null)
					form.setDtSuperviseBy(ob[18].toString());
				if(ob[19] != null)
					form.setDtSuperviseName(ob[19].toString());
				if(ob[20] != null)
					form.setEntryBy(ob[20].toString());
				if(ob[21] != null)
					form.setEntryName(ob[21].toString());
				if(ob[22] != null)
					form.setEntryDept(ob[22].toString());
				if(ob[23] != null)
					form.setEntryDeptName(ob[23].toString());
				if(ob[24] != null)
					form.setEntryDateString(ob[24].toString());
				if(ob[25] != null)
					form.setMemo(ob[25].toString());
				arr.add(form);
			}		
		} 
		return arr;
	}
	
	/*
	 * add by fyyang 090920
	 * 查询所有的反措信息
	 */
	@SuppressWarnings("unchecked")
	public List<SpJAntiAccidentForm> findAllAntiAccidentByParent(String deptCode,String enterpriseCode)
	{
		String sql=
			"select a.measure_code,\n" +
			"         a.measure_name,\n" + 
			"         a.special_code,\n" + 
			"         getspecialname(a.special_code),\n" + 
			"         a.fd_duty_leader,\n" + 
			"         getworkername(a.fd_duty_leader),\n" + 
			"         a.fd_manager,\n" + 
			"         getworkername(a.fd_manager),\n" + 
			"         a.fd_technology_by,\n" + 
			"         getworkername(a.fd_technology_by),\n" + 
			"         a.fd_supervise_by,\n" + 
			"         getworkername(a.fd_supervise_by),\n" + 
			"         a.dt_duty_leader,\n" + 
			"         getworkername(a.dt_duty_leader),\n" + 
			"         a.dt_manager,\n" + 
			"         getworkername(a.dt_manager),\n" + 
			"         a.dt_technology_by,\n" + 
			"         getworkername(a.dt_technology_by),\n" + 
			"         a.dt_supervise_by,\n" + 
			"         getworkername(a.dt_supervise_by),\n" + 
			"         a.entry_by,\n" + 
			"         getworkername(a.entry_by),\n" + 
			"         a.entry_dept,\n" + 
			"         getdeptname(a.entry_dept),\n" + 
			"         to_char(a.entry_date,'yyyy-MM-dd'),\n" + 
			"         a.memo\n" + 
			"         from sp_j_anti_accident a\n" + 
			"         where a.is_use='Y'\n" + 
			"         and a.entry_dept='"+deptCode+"'\n" + 
			"         order by a.measure_code asc";
		List list = bll.queryByNativeSQL(sql);
		List<SpJAntiAccidentForm> arr = new ArrayList<SpJAntiAccidentForm>();
		if (list != null) {
			Iterator it = list.iterator();			
			while (it.hasNext()) {
				Object[] ob = (Object[]) it.next();
				SpJAntiAccidentForm form = new SpJAntiAccidentForm();
				if (ob[0] != null) {
					form.setMeasureCode(ob[0].toString());
				}
				if (ob[1] != null) {
					form.setMeasureName(ob[1].toString());
				}
				if (ob[2] != null) {
					form.setSpecialCode(ob[2].toString());
				}
				if (ob[3] != null) {
					form.setSpecialName(ob[3].toString());
				}
				if (ob[4] != null) {
					form.setFdDutyLeader(ob[4].toString());
				}
				if (ob[5] != null) {
					form.setFdDutyLeaderName(ob[5].toString());
				}
				if (ob[6] != null) {
					form.setFdManager(ob[6].toString());
				}
				if (ob[7] != null) {
					form.setFdManagerName(ob[7].toString());
				}
				if (ob[8] != null) {
					form.setFdTechnologyBy(ob[8].toString());
				}
				if (ob[9] != null) {
					form.setFdTechnologyName(ob[9].toString());
				}
				if (ob[10] != null) {
					form.setFdSuperviseBy(ob[10].toString());
				}
				if (ob[11] != null) {
					form.setFdSuperviseName(ob[11].toString());
				}
				if(ob[12] != null)
					form.setDtDutyLeader(ob[12].toString());
				if(ob[13] != null)
					form.setDtDutyLeaderName(ob[13].toString());	
			    if(ob[14] != null)
					form.setDtManager(ob[14].toString());	
				if(ob[15] != null)
					form.setDtManagerName(ob[15].toString());
				if(ob[16] != null)
					form.setDtTechnologyBy(ob[16].toString());
				if(ob[17] != null)
					form.setDtTechnologyName(ob[17].toString());
				if(ob[18] != null)
					form.setDtSuperviseBy(ob[18].toString());
				if(ob[19] != null)
				if(ob[20] != null)
					form.setEntryBy(ob[20].toString());
				if(ob[21] != null)
					form.setEntryName(ob[21].toString());
				if(ob[22] != null)
					form.setEntryDept(ob[22].toString());
				if(ob[23] != null)
					form.setEntryDeptName(ob[23].toString());
				if(ob[24] != null)
					form.setEntryDateString(ob[24].toString());
				if(ob[25] != null)
					form.setMemo(ob[25].toString());
				arr.add(form);
			}		
		} 
        return arr;
	}
	//根据父编码获得反措列表
//	public List<SpJAntiAccident> findByCode(String pCode){
//		String sql = 
//			"select b.p_measure_name,a.measure_name,getworkername(a.fd_duty_leader) fd_duty_leader_name,getworkername(a.fd_manager) fd_manager_name,\n" +
//			"getworkername(a.fd_technology_by) fd_technology_by_name,getworkername(a.fd_supervise_by) fd_supervise_by_name from\n" + 
//			"(select aa.measure_code,aa.measure_name,aa.fd_duty_leader,aa.fd_manager,aa.fd_technology_by,aa.fd_supervise_by\n" + 
//			"from sp_j_anti_accident aa where length(aa.measure_code) > 4)a,\n" + 
//			"(select aa.measure_code,aa.measure_name p_measure_name\n" + 
//			"from sp_j_anti_accident aa where length(aa.measure_code) =4)b where substr(a.measure_code,0,4) = b.measure_code and substr(a.measure_code,0,4)='" + 
//			pCode +"'";
//
//		return bll.queryByNativeSQL(sql);
//	}
	
	public  String getLeaderName(String workCode,String mesureCode)
	{
		String leaderName="";
		String sql="select  getworkername('"+workCode+"')\n " +
				"   from   SP_J_ANTI_ACCIDENT a\n  " +
				"   where   a.measure_code ='"+mesureCode+"'";
//		System.out.println("the sql"+sql);
		Object obj=bll.getSingal(sql);
		if(obj!=null)
		{
			leaderName=obj.toString();
			return leaderName;
			
		}
		return "";
		
	}
	public  String getTechLeaderName(String workCode,String mesureCode)
	{
		String techlogyerName="";
		String sql="select  getworkername('"+workCode+"')\n " +
				"   from   SP_J_ANTI_ACCIDENT a\n  " +
				"   where   a.measure_code ='"+mesureCode+"'";
	
		Object obj=bll.getSingal(sql);
		if(obj!=null)
		{
			techlogyerName=obj.toString();
			return techlogyerName;
			
		}
		return "";
		
	}
	public List<SpJAntiAccidentForm> findByCode(String pCode)
	{
		String mesureCode="";
		String leaderName="";
		String techlogyerName="";
		String sql=
			"select a.measure_code,b.p_measure_name,a.measure_name,getworkername(a.fd_duty_leader) fd_duty_leader_name,getworkername(a.fd_manager) fd_manager_name,\n" +
			"GETTECNOLOGYNAME(a.fd_technology_by) fd_technology_by_name,getworkername(a.fd_supervise_by) fd_supervise_by_name," +
			"a.fd_duty_leader,a.fd_manager,a.fd_technology_by,a.fd_supervise_by ,a.fd_technology_by   from\n" + 
			"(select aa.measure_code,aa.measure_name,aa.fd_duty_leader,aa.fd_manager,aa.fd_technology_by,aa.fd_supervise_by\n" + 
			"from sp_j_anti_accident aa where length(aa.measure_code) > 4)a,\n" + 
			"(select aa.measure_code,aa.measure_name p_measure_name\n" + 
			"from sp_j_anti_accident aa where length(aa.measure_code) =4)b where substr(a.measure_code,0,4) = b.measure_code and substr(a.measure_code,0,2)=" + "\n"
			+ "'" + pCode + "' order by a.measure_code ";
		List list = bll.queryByNativeSQL(sql);
		List<SpJAntiAccidentForm> arr = new ArrayList<SpJAntiAccidentForm>();
		if (list != null) {
			Iterator it = list.iterator();			
			while (it.hasNext()) {
				Object[] ob = (Object[]) it.next();
				SpJAntiAccidentForm form = new SpJAntiAccidentForm();
				if (ob[0] != null) {
					form.setMeasureCode(ob[0].toString());
					mesureCode=ob[0].toString();
				}
				if (ob[1] != null) {
					form.setPMeasureName(ob[1].toString());
				}
				if (ob[2] != null) {
					form.setMeasureName(ob[2].toString());
				}
				/*if (ob[3] != null) {
					form.setFdDutyLeaderName(ob[3].toString());
				}*/
				if (ob[4] != null) {
					form.setFdManagerName(ob[4].toString());
				}
				/*if (ob[5] != null) {
					form.setFdTechnologyName(ob[5].toString());
				}*/
				if (ob[6] != null) {
					form.setFdSuperviseName(ob[6].toString());
				}
				
				
				
				if (ob[7] != null) {
					String leader=ob[7].toString();
					String dutyLeader[]=leader.split(",");
					for(int i=0;i<dutyLeader.length;i++)
					{
						leaderName+=this.getLeaderName(dutyLeader[i],mesureCode)+",";
				    }
					if(!"".equals(leaderName))
					{
					leaderName=leaderName.substring(0,leaderName.lastIndexOf(","));
					}
					form.setFdDutyLeader(ob[7].toString());
					form.setFdDutyLeaderName(leaderName);
					
					leaderName="";
					
				}
				if (ob[8] != null) {
					form.setFdManager(ob[8].toString());
				}
				if (ob[9] != null) {
					form.setFdTechnologyBy(ob[9].toString());
				}
				if (ob[10] != null) {
					form.setFdSuperviseBy(ob[10].toString());
				}
				if (ob[11] != null) {
					String techlogyer=ob[11].toString();
					String techlogy[]=techlogyer.split(",");
					for(int i=0;i<techlogy.length;i++)
					{
						techlogyerName+=this.getTechLeaderName(techlogy[i],mesureCode)+",";
				    }
					if(!"".equals(techlogyerName))
					{
					techlogyerName=techlogyerName.substring(0,techlogyerName.lastIndexOf(","));
					}
					form.setFdTechnologyBy(ob[11].toString());
					form.setFdTechnologyName(techlogyerName);
					
					techlogyerName="";
					
				}
				arr.add(form);
			}		
		} 
        return arr;
	}
}