package power.ejb.equ.change;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.change.form.ChangeModel;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquJEquchang.
 * 
 * @see power.ejb.equ.change.EquJEquchang
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJEquchangFacade implements EquJEquchangFacadeRemote {
	@PersistenceContext
	private EntityManager entityManager;
	WorkflowService service;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public EquJEquchangFacade() {
		bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("NativeSqlHelper");
		service = new WorkflowServiceImpl();
	}
	
	public int save(EquJEquchang entity) {
		 if(!this.CheckChangeNoSame(entity.getEnterpriseCode(), entity.getEquChangeNo()))
		 {
			 if(entity.getEquChangeId()==null)
				{
					entity.setEquChangeId(bll.getMaxId("EQU_J_EQUCHANG", "equ_change_id"));
					entity.setIsUse("Y");
					entity.setEquChangeNo(entity.getEquChangeNo());
					entity.setWfState("0");
					
				
				}
				entityManager.persist(entity);
				return Integer.parseInt(entity.getEquChangeId().toString());
				
		 }
		else
		{
			return -1;
		}
	
	}
	
	
	public String createChangeNo()
	{
		String mymonth="";
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat("yyyy-MM-dd" + " " + "hh:mm:ss"); 
		mymonth = tempDate.format(new java.util.Date());
		mymonth= mymonth.substring(0, 4)+mymonth.substring(5, 7);
		String sql=
			"select case\n" +
			"                                    when max(t.equ_change_no) is null then\n" + 
			"                                     '001'\n" + 
			"                                    else\n" + 
			"                                     substr(max(t.equ_change_no)+1, 7, 3)\n" + 
			"                                  end\n" + 
			"                             from  EQU_J_EQUCHANG t\n" + 
			"                             where t.is_use='Y'\n" + 
			"                             and substr(t.equ_change_no, 0, 6)='"+mymonth+"'";

	   String no= bll.getSingal(sql).toString();
	   no=mymonth+no;
	   return no;

	}


	public void delete(Long changeId) {
		
		EquJEquchang entity=this.findById(changeId);
		entity.setIsUse("N");
		this.update(entity);
	}

	
	public EquJEquchang update(EquJEquchang entity) {
		LogUtil.log("updating EquJEquchang instance", Level.INFO, null);
		try {
			EquJEquchang result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJEquchang findById(Long id) {
		LogUtil.log("finding EquJEquchang instance with id: " + id, Level.INFO,
				null);
		try {
			EquJEquchang instance = entityManager.find(EquJEquchang.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	public boolean CheckChangeNoSame(String enterpriseCode,String equChangeNo,Long... changeId) 
	{ 
		boolean isSame = false;
		String sql =
			"select count(*) from EQU_J_EQUCHANG t\n" +
			"where t.equ_change_no='"+equChangeNo+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";
	    if(changeId !=null&& changeId.length>0){
	    	sql += "  and t.equ_change_id <> " + changeId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	

	
	@SuppressWarnings("unchecked")
	public String getEquChangeDeptName(String deptCode) {
		String deptName = "";
		String sql = "select  getdeptname('" + deptCode + "')\n "+ 
		             "    from   hr_c_dept a\n  " + 
		             "    where   a.dept_code ='"+ deptCode + "'" +
		             "    and a.is_use='Y'";
		Object obj = bll.getSingal(sql);
		if (obj != null) {
			deptName = obj.toString();
			return deptName;

		}
		return "";

	}
	
	@SuppressWarnings("unchecked")
	public PageObject findChangeList(String workerCode,String changeNo,String changeTitle,String flag,String entryIds,
			String enterpriseCode,final int... rowStartIdxAndCount) throws ParseException
	{
		try {
			PageObject result = new PageObject(); 
			String deptName="";
			String sql="select t.equ_change_id,\n" +
				"       t.equ_change_no,\n" + 
				"       t.assetnum,\n" + 
				"       t.equ_old_code,\n" + 
				"       t.equ_new_code,\n" + 
				"       t.equ_name,\n" + 
				"       t.special_code,\n" + 
				"       t.change_title,\n" + 
				"       t.source_code,\n" + 
				"       t.change_reason,\n" + 
				"       t.change_type,\n" + 
				"       to_char(t.change_plan_date, 'yyyy-mm-dd HH24:mi:ss') change_plan_date,\n" + 
				"       t.front_thing,\n" + 
				"       t.back_thing,\n" + 
				"       t.apply_man,\n" + 
				"       to_char(t.apply_date, 'yyyy-mm-dd HH24:mi:ss') apply_date,\n" + 
				"       t.work_flow_no,\n" + 
				"       t.wf_state,\n" + 
				"       t.enterprise_code,\n" + 
				"       t.is_use,\n" + 
				"       a.source_name,\n" + 
				"       b.speciality_name,\n" + 
				"       t.dept_code,\n" + 
				"       t.annex\n" + 
				"  from EQU_J_EQUCHANG t, EQU_C_CHANGESOURCE a, RUN_C_SPECIALS b\n";
			if(flag!=null && !flag.equals("")&&flag.equals("back"))
				{
				sql+=",(SELECT e.equ_change_id, ',' || h.dept_code || ',' deptcode\n" +
				"         FROM equ_j_equchang e, hr_c_dept h\n" + 
				"        WHERE instr(',' || e.dept_code || ',', ',' || h.dept_code || ',') <> 0) c\n";
				}

				sql+="  where  t.source_code=a.source_code(+) " +
				"  and t.special_code=b.speciality_code(+) \n"+
				"  and t.enterprise_code = '"+enterpriseCode+"'\n" + 
				"  and t.is_use='Y'";
			
//			String sqlCount="select count(*) from EQU_J_EQUCHANG t\n" +
//				"where t.is_use='Y'"+
//				"and t.enterprise_code = '"+enterpriseCode+"'\n";

			if(changeNo!=null && !changeNo.equals("")){
				sql+="  and t.equ_change_no like '%"+changeNo+"%'\n";
//				sqlCount+="  and t.equ_change_no like '%"+changeNo+"%'\n";
			}
			
			if(changeTitle!=null && !changeTitle.equals("")){
				sql+="  and t.change_title like '%"+changeTitle+"%'\n" ;
//				sqlCount+="  and t.change_title like '%"+changeTitle+"%'\n" ;
			}
			
			if(changeNo!=null && !changeNo.equals("")){
				sql+="  and t.equ_change_no like '%"+changeNo+"%'\n";
//				sqlCount+="  and t.equ_change_no like '%"+changeNo+"%'\n";
			}
			
			if(flag!=null && !flag.equals("")){
				if(flag.equals("register")){
					sql+="  and t.wf_state in (0,3)\n";
//					sqlCount+="  and t.wf_state in (0,3)\n";
				
				}
				if(flag.equals("approve")){
					if (entryIds != null) {
						String inEntriyId = bll.subStr(entryIds, ",", 500,"t.work_flow_no");
						sql+="  and t.wf_state not in (3)\n";
						sql += " and " + inEntriyId;
//						sqlCount+="  and t.wf_state not in (3)\n";
//						sqlCount += " and " + inEntriyId;
					} else{
					    sql+="  and t.wf_state not in (3)\n";
						sql += " and t.work_flow_no =null";
//						sqlCount+="  and t.wf_state not in (3)\n";
//					    sqlCount += " and t.work_flow_no =null";
					}
				}
				if(flag.equals("back")){
					String condition=
//						"and (SELECT COUNT(*)\n" +
//						"          FROM (SELECT sys_connect_by_path(dept_code, ',') deptcodes\n" + 
//						"                  FROM hr_c_dept\n" + 
//						"                 WHERE dept_code = '"+workerCode+"'\n" + 
//						"                 START WITH pdept_id = -1\n" + 
//						"                CONNECT BY PRIOR dept_id = pdept_id\n" + 
//						"                       AND is_use = 'Y') a,\n" + 
//						"               (SELECT ',' || h.dept_code || ',' deptcode\n" + 
//						"                  FROM equ_j_equchang e,\n" + 
//						"                       hr_c_dept      h\n" + 
//						"                 WHERE instr(',' || e.dept_code || ',', ',' || h.dept_code || ',') <> 0\n" + 
//						"                   AND e.equ_change_id = t.equ_change_id) b\n" + 
//						"         WHERE instr(deptcodes || ',', deptcode) <> 0) > 0  order by t.change_plan_date desc";

						"and t.equ_change_id = c.equ_change_id\n" +
						"   and instr((SELECT sys_connect_by_path(dept_code, ',') deptcodes\n" + 
						"               FROM hr_c_dept\n" + 
						"              WHERE dept_code = '"+workerCode+"'\n" + 
						"              START WITH pdept_id = -1\n" + 
						"             CONNECT BY PRIOR dept_id = pdept_id\n" + 
						"                    AND is_use = 'Y')||',',\n" + 
						"             c.deptcode) <> 0\n" + 
						" order by t.change_plan_date desc";

					sql+="  and t.wf_state in (4) and t.equ_change_no like '%"+changeNo+"%'";
//					sqlCount+="  and t.wf_state in (4) and t.equ_change_no like '%"+changeNo+"%'";
					sql+= condition;
//					sqlCount += condition;
				}
			}
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		String	sqlCount="select count(*) from ("+sql+")";
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			List arrlist = new ArrayList();
			Iterator it=list.iterator();
			while(it.hasNext())
			{
				ChangeModel model=new ChangeModel();
				EquJEquchang equmodel=new EquJEquchang();
				Object[] data=(Object[])it.next();
				equmodel.setEquChangeId(Long.parseLong(data[0].toString()));
				equmodel.setEquChangeNo(data[1].toString());
				if(data[2]!=null)
				{
				 equmodel.setAssetnum(data[2].toString());
				}
				if(data[3]!=null)
				{
					equmodel.setEquOldCode(data[3].toString());
				}
				if(data[4]!=null)
				{
					equmodel.setEquNewCode(data[4].toString());
				}
				if(data[5]!=null)
				{
					equmodel.setEquName(data[5].toString());
				}
				if(data[6]!=null)
				{
					equmodel.setSpecialCode(data[6].toString());
				}
				if(data[7]!=null)
				{
					equmodel.setChangeTitle(data[7].toString());
				}
				if(data[8]!=null)
				{
					equmodel.setSourceCode(data[8].toString());
				}
				if(data[9]!=null)
				{
					equmodel.setChangeReason(data[9].toString());
				}
				if(data[10]!=null)
				{
					
					equmodel.setChangeType(data[10].toString());
					if(equmodel.getChangeType().equals("1"))
					{
						equmodel.setChangeType("安装");
					}
					else if(equmodel.getChangeType().equals("2"))
					{
						equmodel.setChangeType("改装");
					}
					else if(equmodel.getChangeType().equals("3"))
					{
						equmodel.setChangeType("拆除");
					}
					else if(equmodel.getChangeType().equals("4"))
					{
						equmodel.setChangeType("改造");
					}
					else
					{
						
					}
				}
				if(data[11]!=null)
				{
					model.setChangePlanDate(data[11].toString());
				}
				if(data[12]!=null)
				{
					equmodel.setFrontThing(data[12].toString());
				}
				if(data[13]!=null)
				{
					equmodel.setBackThing(data[13].toString());
				}
				
				if(data[14]!=null)
				{
					equmodel.setApplyMan(data[14].toString());
				}
				if(data[15]!=null)
				{
					model.setApplyDate(data[15].toString());
				}
				if(data[16]!=null)
				{
					equmodel.setWorkFlowNo(data[16].toString());
				}
				if(data[17]!=null)
				{
					equmodel.setWfState(data[17].toString());
				}
				if(data[20]!=null)
				{
				model.setSourceName(data[20].toString());
				}
				if(data[21]!=null)
				{
				model.setSpecialityName(data[21].toString());
				}
				if (data[22] != null) {
					String deptCode=data[22].toString();
					String deptCodes[]=deptCode.split(",");
					for(int i=0;i<deptCodes.length;i++)
					{
						deptName+=this.getEquChangeDeptName(deptCodes[i])+",";
				    }
					deptName=deptName.substring(0,deptName.lastIndexOf(","));
					equmodel.setDeptCode(data[22].toString());
					model.setDeptName(deptName);
					deptName="";
					
				}
				if (data[23] != null) {
					equmodel.setAnnex(data[23].toString());
				}
				model.setChange(equmodel);
				arrlist.add(model);
			}
			
			result.setList(arrlist);
			result.setTotalCount(totalCount);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public int getMaxNo(String maxNo,String enterpriseCode){
		int maxId=1;
		String sql="select max(t.equ_change_no)\n" +
			"  from EQU_J_EQUCHANG t\n" + 
			" where t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.equ_change_no like '%"+maxNo+"%'";

		List list=bll.queryByNativeSQL(sql);
		if (list!=null&&list.size()>0&&list.get(0)!=null) {
			String maxCode=list.get(0).toString();
			maxCode=maxCode.substring(maxNo.length()+1,maxNo.length()+3);
			maxId=Integer.parseInt(maxCode)+1;
		}
		return maxId;

	}
	
	public void reportEquChange(Long equChangeId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String nextRolePs, String workflowType) {
		EquJEquchang model = findById(equChangeId);
		Long entryId;
		if (model.getWorkFlowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, equChangeId.toString());
			model.setWorkFlowNo(entryId.toString());
		} else {
			entryId = Long.parseLong(model.getWorkFlowNo());
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		model.setWfState("1");
		entityManager.merge(model);

	}
	
	@SuppressWarnings("unchecked")
	public EquJEquchang approveEquChange(String equChangeId,String workflowType,String workerCode,
			String actionId,String eventIdentify, String approveText, String nextRoles, String enterpriseCode) {

		EquJEquchang entity = findById(Long.parseLong(equChangeId));
		try {
			if (eventIdentify.equals("TH")) {
				entity.setWfState("3");
			} else {
				if (entity.getWfState().equals("1")) {

					entity.setWfState("2");
				} 
				else if (entity.getWfState().equals("2")) {
					entity.setWfState("4");
				} 
			}
			WorkflowService service = new WorkflowServiceImpl();
			service.doAction(Long.parseLong(entity.getWorkFlowNo()), workerCode, Long
					.parseLong(actionId), approveText, null, nextRoles, "");
			entityManager.merge(entity);
		} catch (Exception e) {

		}
		return entity;
	}

	public String getMajorNo(String special) {
		String specialName = bll.getSingal("select t.short_name from run_c_specials t where t.speciality_code = '"+special+"' ").toString();
		String sql = String
		.format("select fun_spellcode('%s') from dual", specialName);
		return bll.getSingal(sql).toString();
	}
	
	@SuppressWarnings("unchecked")
	public String getEquSpecialName(String specialCode) {
		String specialName = "";
		String sql = "select  getspecialname('" + specialCode + "')\n "+ 
		             "    from   EQU_J_EQUCHANG a\n  " + 
		             "    where   a.special_code ='"+ specialCode + "'" +
		             "    and a.is_use='Y'";
		Object obj = bll.getSingal(sql);
		if (obj != null) {
			specialName = obj.toString();
			return specialName;
		}
		return "";

	}
	
	@SuppressWarnings("unchecked")
	public String getEquChangeSourceName(String sourceCode,String enterpriseCode) {
		String specialName = "";
		String sql = "select t.source_name\n" +
			"  from EQU_C_CHANGESOURCE t\n" + 
			" where t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '" + enterpriseCode + "'\n" + 
			"   and t.source_code = '" + sourceCode + "'";

		Object obj = bll.getSingal(sql);
		if (obj != null) {
			specialName = obj.toString();
			return specialName;
		}
		return "";

	}

	public void saveEquJEquchangBackfill(EquJEquchangBackfill entity) {
		Long id = Long.parseLong(bll.getMaxId("EQU_J_EQUCHANG_BACKFILL", "BACKFILL_ID").toString());
		entity.setBackfillId(id);
		entityManager.persist(entity);
	}

	public EquJEquchangBackfill findEquJEquchangBackfill(String changeNo) throws ParseException {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String sql = 
				"SELECT t.backfill_id,\n" +
				"       t.equ_change_no,\n" + 
				"GETDEPTNAME(t.dept_code),\n" +
				"     GETWORKERNAME(t.backfill_by),\n" +
				"       t.exe_date,\n" + 
				"       t.exe_situation,\n" + 
				"       t.is_backfill\n" + 
				"  FROM equ_j_equchang_backfill t\n" + 
				" WHERE t.equ_change_no = '"+changeNo+"'";
			List<Object[]> result = bll.queryByNativeSQL(sql);
			EquJEquchangBackfill model =null;
			if(result.size()>0){
				model= new EquJEquchangBackfill();
				Object[] o  = result.get(0);
				if(o[0]!=null){
					model.setBackfillId(Long.parseLong(o[0].toString()));
				}
				if(o[1]!=null){
					model.setEquChangeNo(o[1].toString());
				}if(o[2]!=null){
					model.setDeptCode(o[2].toString());
				}if(o[3]!=null){
					model.setBackfillBy(o[3].toString());
				}if(o[4]!=null){
					model.setExeDate(format.parse(o[4].toString()));
				}if(o[5]!=null){
					model.setExeSituation(o[5].toString());
				}if(o[6]!=null){
					model.setIsBackfill(o[6].toString());
				}
			}
			
			return model;
	}
	/**
	 * 根据设备编码查询异动信息
	 * @param equCode
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 * add by kzhang 20100921
	 */
	public PageObject findChangeListByEquCode(String equCode,
			String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject(); 
			String deptName="";
			String sql="select t.equ_change_id,\n" +
				"       t.equ_change_no,\n" + 
				"       t.assetnum,\n" + 
				"       t.equ_old_code,\n" + 
				"       t.equ_new_code,\n" + 
				"       t.equ_name,\n" + 
				"       t.special_code,\n" + 
				"       t.change_title,\n" + 
				"       t.source_code,\n" + 
				"       t.change_reason,\n" + 
				"       t.change_type,\n" + 
				"       to_char(t.change_plan_date, 'yyyy-mm-dd HH24:mi:ss') change_plan_date,\n" + 
				"       t.front_thing,\n" + 
				"       t.back_thing,\n" + 
				"       t.apply_man,\n" + 
				"       to_char(t.apply_date, 'yyyy-mm-dd HH24:mi:ss') apply_date,\n" + 
				"       t.work_flow_no,\n" + 
				"       t.wf_state,\n" + 
				"       t.enterprise_code,\n" + 
				"       t.is_use,\n" + 
				"       a.source_name,\n" + 
				"       b.speciality_name,\n" + 
				"       t.dept_code\n" + 
				"  from EQU_J_EQUCHANG t, EQU_C_CHANGESOURCE a, RUN_C_SPECIALS b\n"+
				"  where  t.source_code=a.source_code(+) " +
				"  and t.special_code=b.speciality_code(+) \n"+
				"  and t.enterprise_code = '"+enterpriseCode+"'\n" + 
				"  and t.is_use='Y'\n" +
				"  and t.equ_new_code='"+equCode+"'";
			String sqlCount="select count(1) from ("+sql+")";
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public String getApplyManName(String applyMan) {
		String sql=
			"select getworkername('"+applyMan+"') from dual";
		return bll.getSingal(sql).toString();
}

	
}