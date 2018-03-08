package power.ejb.workticket.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.workticket.RunCWorkticketDanger;
import power.ejb.workticket.form.WorkticketHisForPrint;
import power.ejb.workticket.form.WorkticketInfo;
import power.ejb.workticket.form.WorkticketSafetyBeakOutModel;

@Stateless
public class RunJWorkticketsFacade implements RunJWorkticketsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "RunJWorkticketsFacade")
	private RunJWorkticketsFacadeRemote remote;
	@EJB(beanName = "RunJWorktickethisFacade")
	private RunJWorktickethisFacadeRemote hisRemote;
	
	private BaseDataManager baseDeptRemote;

	// --------------fyyang------
	public RunJWorktickets save(RunJWorktickets entity) {
		LogUtil.log("saving RunJWorktickets instance", Level.INFO, null);
		try {
			entity.setIsUse("Y");
			entity.setEntryDate(new java.util.Date());
			entity.setWorkticketStausId(1l);
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String workticketNo) {
		RunJWorktickets entity = this.findById(workticketNo);
		entity.setIsUse("N");
		this.update(entity);
	}

	public RunJWorktickets update(RunJWorktickets entity) {
		LogUtil.log("updating RunJWorktickets instance", Level.INFO, null);
		try {
			entity.setEntryDate(new java.util.Date());
			RunJWorktickets result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJWorktickets findById(String id) {
		LogUtil.log("finding RunJWorktickets instance with id: " + id,
				Level.INFO, null);
		try {
			RunJWorktickets instance = entityManager.find(
					RunJWorktickets.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 生成工作票号
	 * modify by fyyang 090317
	 * @param enterpriseCode
	 * @param workticketType
	 * @param deptCode
	 * @param fireLevelId
	 * @return
	 */
	public String createWorkticketNo(String enterpriseCode,String workticketType, String deptCode,Long fireLevelId) {
		baseDeptRemote=(BaseDataManager) Ejb3Factory.getInstance()
		.getFacadeRemote("BaseDataManagerImpl");
		String mymonth = "";
		//父部门标识加部门标识
		String deptIndentifier=baseDeptRemote.getDeptAndPdeptIdentifier(deptCode);
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyyy-MM-dd" + " " + "hh:mm:ss");
		mymonth = tempDate.format(new java.util.Date());
		mymonth = mymonth.substring(2, 4) + mymonth.substring(5, 7);
	     if(workticketType.equals("1")) workticketType="电气Ⅰ";
	     else if(workticketType.equals("2"))  workticketType="电气Ⅱ";
	     else if(workticketType.equals("3"))  workticketType="热机Ⅰ";
	     else if(workticketType.equals("4")) { if(fireLevelId==1) workticketType="动火Ⅰ"; if(fireLevelId==2) workticketType="动火Ⅱ";   }
	     else if(workticketType.equals("5"))  workticketType="热控Ⅰ";
	     else if(workticketType.equals("7"))  workticketType="热机Ⅱ";
	     else if(workticketType.equals("8"))  workticketType="热控Ⅱ";
		String no = deptIndentifier + workticketType + mymonth;
	     String sql=
	    	 "select '"+no+"' ||\n" +
	    	 "       (select Trim(case\n" + 
	    	 "                      when max(w.workticket_no) is null then\n" + 
	    	 "                       '001'\n" + 
	    	 "                      else\n" + 
	    	 "                       to_char(to_number(substr(max(substr(w.workticket_no,0,14)), 12, 3)+1),\n" + 
	    	 "                               '000')\n" + 
	    	 "                    end)\n" + 
	    	 "\n" + 
	    	 "          from run_j_worktickets w\n" + 
	    	 "         where w.workticket_no like\n" + 
	    	 "               Trim('"+no+"%'))\n" + 
	    	 "  from dual";
		no = bll.getSingal(sql).toString().trim();
		return no;
	}
	
	/**
	 * 生成工作票号
	 * modify by fyyang 090317
	 * @param enterpriseCode
	 * @param workticketType
	 * @param deptCode
	 * @param fireLevelId
	 * @return
	 */
	public String createStandardWorkticketNo(String enterpriseCode,String workticketType,Long fireLevelId) {
		baseDeptRemote=(BaseDataManager) Ejb3Factory.getInstance()
		.getFacadeRemote("BaseDataManagerImpl");
		String mymonth = "";
		//父部门标识加部门标识
		String deptIndentifier="【标准】";
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyyy-MM-dd" + " " + "hh:mm:ss");
		mymonth = tempDate.format(new java.util.Date());
		mymonth = mymonth.substring(2, 4) + mymonth.substring(5, 7);
	     if(workticketType.equals("1")) workticketType="电气Ⅰ";
	     else if(workticketType.equals("2"))  workticketType="电气Ⅱ";
	     else if(workticketType.equals("3"))  workticketType="热机Ⅰ";
	     else if(workticketType.equals("4")) { if(fireLevelId==1) workticketType="动火Ⅰ"; if(fireLevelId==2) workticketType="动火Ⅱ";   }
	     else if(workticketType.equals("5"))  workticketType="热控Ⅰ";
	     else if(workticketType.equals("7"))  workticketType="热机Ⅱ";
	     else if(workticketType.equals("8"))  workticketType="热控Ⅱ";
		String no = deptIndentifier + workticketType + mymonth;
	     String sql=
	    	 "select '"+no+"' ||\n" +
	    	 "       (select Trim(case\n" + 
	    	 "                      when max(w.workticket_no) is null then\n" + 
	    	 "                       '0001'\n" + 
	    	 "                      else\n" + 
	    	 "                       to_char(max(to_number(substr(w.workticket_no, 12, 8)))+1,\n" + 
	    	 "                               '0000')\n" + 
	    	 "                    end)\n" + 
	    	 "\n" + 
	    	 "          from run_j_worktickets w\n" + 
	    	 "         where w.workticket_no like\n" + 
	    	 "               Trim('"+no+"%'))\n" +
	    	 "  from dual"; 
		no = bll.getSingal(sql).toString().trim();
		return no;
	}

	//回写主表工作内容 modify by fyyang 090311 不需回写
//	public void updateWorkticketContent(String workticketNo) {
//		String sql = "update RUN_J_WORKTICKETS t\n"
//				+ "set t.workticket_content=GetWorkContent('" + workticketNo
//				+ "')\n" + "where t.workticket_no='" + workticketNo
//				+ "' and t.is_use='Y'";
//		bll.exeNativeSQL(sql);
//
//	}

	//modify by fyyang 090316 安措内容表已不使用
//	public void copySafetyForWorkticket(String enterpriseCode,
//			String workticketTypeCode, String workticketNo) {
//		String sql = "";
//		if (workticketTypeCode.equals("4")) {
//			// 动火票分级别
//			// modify by fyyang 090224
//			RunJWorktickets model = this.findById(workticketNo);
//			if (model.getFirelevelId() == 2) {
//				workticketTypeCode = "6";
//			} else {
//				workticketTypeCode = "4";
//			}
//			// sql = "insert into
//			// RUN_J_WORKTICK_SFATY_CONTENT(id,workticket_no,safety_code,line,enterprise_code,is_use,safety_content)\n"
//			// + " SELECT (SELECT nvl(MAX(id), 0)\n"
//			// + " FROM RUN_J_WORKTICK_SFATY_CONTENT) + row_number() over(order
//			// by b.order_by),\n"
//			// + " '"
//			// + workticketNo
//			// + "',\n"
//			// + " b.safety_code,row_number() over(order by b.order_by),\n"
//			// + " '"
//			// + enterpriseCode
//			// + "','Y',''\n"
//			// + " FROM run_c_worktick_safety b\n"
//			// + " WHERE b.workticket_type_code='"
//			// + workticketTypeCode
//			// + "'\n"
//			// + " and b.enterprise_code='"
//			// + enterpriseCode
//			// + "'\n"
//			// + " and b.is_use='Y'";
//		}
//
//		sql = "insert into RUN_J_WORKTICK_SFATY_CONTENT(id,workticket_no,safety_code,line,enterprise_code,is_use)\n"
//				+ "                       SELECT (SELECT nvl(MAX(id), 0)\n"
//				+ "                                 FROM RUN_J_WORKTICK_SFATY_CONTENT) + row_number() over(order by b.order_by),\n"
//				+ "                              '"
//				+ workticketNo
//				+ "',\n"
//				+ "                              b.safety_code,row_number() over(order by b.order_by),\n"
//				+ "                              '"
//				+ enterpriseCode
//				+ "','Y'\n"
//				+ "                         FROM run_c_worktick_safety b\n"
//				+ "                        WHERE b.workticket_type_code='"
//				+ workticketTypeCode
//				+ "'\n"
//				+ "                        and b.enterprise_code='"
//				+ enterpriseCode
//				+ "'\n"
//				+ "                        and b.is_use='Y'";
//
//		bll.exeNativeSQL(sql);
//	}

	public void copyMainTicketContent(String enterpriseCode,
			String mainTicketNo, String fireTicketNo, String createMan,
			String createDate) {
//		String sql = "insert into  run_j_workticket_content m(m.id,m.workticket_no,m.worktype_id,m.worktype_name,m.flag_id,m.line,m.front_key_id,m.front_key_desc,m.location_id,m.location_name,\n"
//				+ "                                         m.attribute_code,m.equ_name,m.back_key_id,m.back_key_desc,m.create_by,m.create_date,m.enterprise_code,m.is_use)\n"
//				+ "                          select (select nvl(max(id) , 0) from run_j_workticket_content)+rownum,\n"
//				+ "                                 '"
//				+ fireTicketNo
//				+ "',\n"
//				+ "                                 n.worktype_id,n.worktype_name,n.flag_id,n.line,n.front_key_id,n.front_key_desc,n.location_id,n.location_name,n.attribute_code,n.equ_name,\n"
//				+ "                                 n.back_key_id,n.back_key_desc,\n"
//				+ "                                 '"
//				+ createMan
//				+ "',\n"
//				+ "                                 sysdate,\n"
//				+ "                                 '"
//				+ enterpriseCode
//				+ "','Y'\n"
//				+ "                            from run_j_workticket_content n\n"
//				+ "                           where n.workticket_no = '"
//				+ mainTicketNo
//				+ "' and n.is_use='Y' and n.enterprise_code='"
//				+ enterpriseCode + "'";
//
//		bll.exeNativeSQL(sql);
		//modify by fyyang 090523 工作内容表已不用
		String sql=
			"update run_j_worktickets t\n" +
			"set t.workticket_content=\n" + 
			"(\n" + 
			" select tt.workticket_content  from  run_j_worktickets tt where tt.workticket_no='"+mainTicketNo+"'\n" + 
			"\n" + 
			")\n" + 
			"where t.workticket_no='"+fireTicketNo+"'";
		bll.exeNativeSQL(sql);
	}

	public void copyWorkticketSafetyDetail(String oldWorkticketNo,
			String newWorkTicketNo, String createMan, String createDate) {
		String sql = "insert into RUN_J_WORKTICKET_SAFETY t(t.id,t.workticket_no,t.safety_code,t.attribute_code,t.equ_name,t.markcard_code,\n"
				+ "                                      t.namecard_content,t.front_key_id,t.front_keyword,t.back_key_id,t.back_keyword,\n"
				+ "                                      t.flag_id,t.operation_order,t.is_return,t.is_use,t.create_by,t.create_date,t.enterprise_code,t.flag_desc)\n"
				+ "\n"
				+ "                 select (select nvl(max(id), 0) from RUN_J_WORKTICKET_SAFETY) +\n"
				+ "                        row_number() over(order by operation_order),\n"
				+ "                        '"
				+ newWorkTicketNo
				+ "',\n"
				+ "                        m.safety_code,m.attribute_code,m.equ_name,m.markcard_code,m.namecard_content,\n"
				+ "                        m.front_key_id,m.front_keyword,m.back_key_id,m.back_keyword,\n"
				+ "                        m.flag_id,m.operation_order,m.is_return,m.is_use,\n"
				+ "                        '"
				+ createMan
				+ "',\n"
				+ "                        sysdate,\n"
				+ "                        m.enterprise_code,m.flag_desc\n"
				+ "                   from RUN_J_WORKTICKET_SAFETY m\n"
				+ "                  where m.workticket_no='"
				+ oldWorkticketNo
				+ "' \n"
				//add by fyyang 090310 只copy非运行补充安措可检修安措
				+"  and (getwtsafetytype(m.safety_code)  in ('N','repair')  or getwtsafetytype(m.safety_code)  is null)";
				
		bll.exeNativeSQL(sql);

	}

	//modify by fyyang 090311 安措不回写
//	public void copySafetyContentByOld(String oldWorkticketNo,
//			String newWorkticketNo) {
//		String sql = "UPDATE run_j_worktick_sfaty_content b\n"
//				+ "                   SET b.line           = (SELECT a.line\n"
//				+ "                                             FROM run_j_worktick_sfaty_content a\n"
//				+ "                                            WHERE a.workticket_no = '"
//				+ oldWorkticketNo
//				+ "'\n"
//				+ "                                              AND a.safety_code = b.safety_code),\n"
//				+ "                       b.safety_content = (SELECT c.safety_content\n"
//				+ "                                             FROM run_j_worktick_sfaty_content c\n"
//				+ "                                            WHERE c.workticket_no = '"
//				+ oldWorkticketNo
//				+ "'\n"
//				//add by fyyang 090310 只copy非运行补充安措可检修安措
//				+ " and (getwtsafetytype(c.safety_code)  in ('N','repair')  or getwtsafetytype(c.safety_code)  is null)  \n"
//				+ "                                              AND c.safety_code = b.safety_code)\n"
//				+ "                 WHERE b.workticket_no = '"
//				+ newWorkticketNo + "'";
//
//		bll.exeNativeSQL(sql);
//
//	}

	public void updateBlockAndConditionByOld(String oldWorkticketNo,
			String newWorkticketNo) {
		String sql = "update run_j_worktickets t\n"
				+ "                  set t.equ_attribute_code    = (select equ_attribute_code\n"
				+ "                                                   from RUN_J_WORKTICKETS\n"
				+ "                                                  where workticket_no = '"
				+ oldWorkticketNo
				+ "'),\n"
				+ "                      t.permission_dept = (select permission_dept\n"
				+ "                                                   from RUN_J_WORKTICKETS\n"
				+ "                                                  where workticket_no = '"
				+ oldWorkticketNo
				+ "'),\n"
				+ "                      t.condition_name        = (select nvl(condition_name,'')\n"
				+ "                                                   from RUN_J_WORKTICKETS\n"
				+ "                                                  where workticket_no = '"
				+ oldWorkticketNo + "')\n"
				+ "                where t.workticket_no= '" + newWorkticketNo
				+ "'";
		bll.exeNativeSQL(sql);

	}

	//modify by fyyang 090311 安措内容不回写更新
//	public void updateSafetyContent(String workticketNo, String safetyCode) {
//		String sql = "update  RUN_J_WORKTICK_SFATY_CONTENT a\n"
//				+ " set a.safety_content=GETSECURITYMEASURE('" + workticketNo
//				+ "','" + safetyCode + "')\n" + " where a.workticket_no='"
//				+ workticketNo + "' and a.safety_code='" + safetyCode
//				+ "' and a.is_use='Y'";
//		bll.exeNativeSQL(sql);
//	}

	public void updateWorkMemers(String workticketNo) {
		String sql = "update run_j_worktickets t\n"
				+ "set t.members=getworkticketmembers('"
				+ workticketNo
				+ "'),\n"
				+ "t.member_count=(select count(*)+1 from RUN_J_WORKTICKET_ACTORS tt where tt.workticket_no='"
				+ workticketNo + "' and tt.is_use='Y')\n"
				+ "where t.workticket_no='" + workticketNo
				+ "' and t.is_use='Y'";
		bll.exeNativeSQL(sql);

	}

	@SuppressWarnings("unchecked")
	public List<WorkticketInfo> workticketEndWarn(String enterpriseCode,
			String workicketTypeCode, String runSpecialCode,
			String repairSpecialCode) {
		String sql = "select * from\n"
				+ "(\n"
				+ "select  tt.workticket_no,tt.workticket_content,tt.equ,tt.statusname,tt.chargename,\n"
				+ "tt.dept,"
				+ "to_char(tt.plan_start_date, 'yyyy-MM-dd hh24:mi:ss') plan_start_date,"
				+ "to_char(tt.plan_end_date, 'yyyy-MM-dd hh24:mi:ss') plan_end_date,"
				+ "to_char(tt.approved_finish_date, 'yyyy-MM-dd hh24:mi:ss') approved_finish_date,"
				+ "to_char(tt.delaydate, 'yyyy-MM-dd hh24:mi:ss')  delaydate,\n"
				+ "(case\n"
				+ "when (tt.mydate-sysdate)*24*60*60<0\n"
				+ "then '1'\n"
				+ "else\n"
				+ "(case when  (tt.mydate-sysdate)*24<4 then '2' else (case when tt.mydate-sysdate<1 then '3' else '0' end )  end)\n"
				+ "end) mystatus\n"
				+ "\n"
				+ "from\n"
				+ "(\n"
				+ " select t.workticket_no,t.workticket_content,\n"
				+ " GETBlockBYCODE(t.equ_attribute_code) equ,\n"
				+ " GETWTSTATUSNAME(t.workticket_staus_id) statusname,\n"
				+ " GETWORKERNAME(t.charge_by) chargename,\n"
				+ " GETDEPTNAME(t.charge_dept) dept,\n"
				+ " t.plan_start_date,t.plan_end_date,\n"
				+ " t.approved_finish_date,\n"
				+ "  (\n"
				+ " select m.new_approved_finish_date from run_j_worktickethis m\n"
				+ " where m.change_status='1' and m.workticket_no=t.workticket_no and rownum=1\n"
				+ " ) delaydate,\n"
				+ " (case\n"
				+ " when\n"
				+ " (\n"
				+ " select m.new_approved_finish_date from run_j_worktickethis m\n"
				+ " where m.change_status='1' and m.workticket_no=t.workticket_no  and rownum=1\n"
				+ " ) is  null\n"
				+ " then\n"
				+ " ( case when t.approved_finish_date is  null then t.plan_end_date else t.approved_finish_date end )\n"
				+ "  else\n"
				+ "  (\n"
				+ " select m.new_approved_finish_date from run_j_worktickethis m\n"
				+ " where m.change_status='1' and m.workticket_no=t.workticket_no\n"
				+ " )\n" + " end) mydate\n" + " from run_j_worktickets t\n"
				+ " where t.workticket_staus_id=7\n" + " and t.is_use='Y'\n"
				+ " and t.workticket_type_code like '" + workicketTypeCode
				+ "'\n" + " and t.permission_dept like '" + runSpecialCode
				+ "'\n" + " and t.repair_specail_code like '"
				+ repairSpecialCode + "'\n" + "  and  t.enterprise_code='"
				+ enterpriseCode + "' \n" + " ) tt\n" + " ) ttt\n"
				+ " where ttt.mystatus<>'0'";
		List list = bll.queryByNativeSQL(sql);
		List arraylist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			WorkticketInfo model = new WorkticketInfo();
			RunJWorktickets basemodel = new RunJWorktickets();
			if (data[0] != null) {
				basemodel.setWorkticketNo(data[0].toString());
			}
			if (data[1] != null) {
				basemodel.setWorkticketContent(data[1].toString());
			}
			if (data[2] != null) {
				model.setBlockName(data[2].toString());
			}
			if (data[3] != null) {
				// model.setSourceName("");
			}
			if (data[4] != null) {
				model.setChargeByName(data[4].toString());
			}
			if (data[5] != null) {
				model.setDeptName(data[5].toString());
			}
			if (data[6] != null) {
				model.setPlanStartDate(data[6].toString());
			} else {
				model.setPlanStartDate("");
			}
			if (data[7] != null) {
				model.setPlanEndDate(data[7].toString());
			} else {
				model.setPlanEndDate("");
			}
			if (data[8] != null) {
				model.setApproveEndDate(data[8].toString());
			} else {
				model.setApproveEndDate("");
			}
			if (data[9] != null) {
				model.setDelayToDate(data[9].toString());
			} else {
				model.setDelayToDate("");
			}
			if (data[10] != null) {
				model.setStatusName(data[10].toString());
			}

			model.setModel(basemodel);
			arraylist.add(model);
		}

		return arraylist;

	}

	@SuppressWarnings("unchecked")
	public void copyDanger(String workticketNo, String dangerId, String workCode) {
		RunJWorkticketDangerFacadeRemote dangerRemote = (RunJWorkticketDangerFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("RunJWorkticketDangerFacade");
		String sql = "select * from run_c_workticket_danger t\n"
				+ "where t.p_danger_id=" + dangerId + "  and t.is_use='Y'";

		List<RunCWorkticketDanger> list = bll.queryByNativeSQL(sql,
				RunCWorkticketDanger.class);

		for (int i = 0; i < list.size(); i++) {

			RunCWorkticketDanger model = list.get(i);
			RunJWorkticketDanger entity = new RunJWorkticketDanger();
			entity.setDangerName(model.getDangerName());
			entity.setEnterpriseCode(model.getEnterpriseCode());
			entity.setModifyBy(workCode);
			entity.setWorkticketNo(workticketNo);
			entity.setIsRunadd("N");
			entity.setPDangerId(0l);
			try {
				entity = dangerRemote.save(entity);
			} catch (CodeRepeatException e) {
				e.printStackTrace();
			}
			entityManager.flush();
			if (entity.getDangerContentId() != null) {
				String insertSql = "insert into run_j_workticket_danger d\n"
						+ "  (d.danger_content_id,\n"
						+ "   d.workticket_no,\n"
						+ "   d.p_danger_id,\n"
						+ "   d.danger_name,\n"
						+ "   d.order_by,\n"
						+ "   d.modify_by,\n"
						+ "   d.modify_date,\n"
						+ "   d.enterprise_code,\n"
						+ "   d.is_use,d.is_runadd)\n"
						+ "  select (select nvl(max(t.danger_content_id), 0)\n"
						+ "            from run_j_workticket_danger t) + row_number() over(order by a.order_by),\n"
						+ "         '" + workticketNo + "',\n" + "         "
						+ entity.getDangerContentId() + ",\n"
						+ "         a.danger_name,\n"
						+ "         a.order_by,\n" + "         '" + workCode
						+ "',\n" + "         sysdate,\n"
						+ "         a.enterprise_code,\n"
						+ "         'Y','N'\n"
						+ "    from run_c_workticket_danger a\n"
						+ "   where a.p_danger_id = " + model.getDangerId();

				bll.exeNativeSQL(insertSql);
				entityManager.flush();
			}

		}

	}

	public void updateDanger(String workticketNo, String dangerId,
			String workCode) {
		if (!dangerId.equals("")) {
			String sql = "update run_j_workticket_danger a\n"
					+ "set a.is_use='N'\n" + "where a.workticket_no='"
					+ workticketNo + "'\n" + "and a.is_use='Y'";
			bll.exeNativeSQL(sql);
			this.copyDanger(workticketNo, dangerId, workCode);

		}

	}

	/**
	 * 由标准票生成时copy危险点
	 */
	@SuppressWarnings("unchecked")
	public void copyDangerByOldTicket(String oldWorkticketNo,
			String newWorkticketNo, String workCode) {
		RunJWorkticketDangerFacadeRemote dangerRemote = (RunJWorkticketDangerFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("RunJWorkticketDangerFacade");
		String sql = "select * from run_j_workticket_danger  t\n"
				+ "where t.workticket_no='" + oldWorkticketNo + "'\n"
				+ "and t.is_use='Y'\n" + "and t.p_danger_id=0";
		List<RunJWorkticketDanger> list = bll.queryByNativeSQL(sql,
				RunJWorkticketDanger.class);
		for (int i = 0; i < list.size(); i++) {

			RunJWorkticketDanger model = list.get(i);
			String dangerId = model.getDangerContentId().toString();
			model.setDangerContentId(null);
			model.setWorkticketNo(newWorkticketNo);
			model.setIsRunadd("N");
			model.setModifyBy(workCode);
			// model.setPDangerId(0l);
			try {
				model = dangerRemote.save(model);
			} catch (CodeRepeatException e) {
				e.printStackTrace();
			}
			entityManager.flush();
			if (model != null) {
				String insertSql = "insert into run_j_workticket_danger d\n"
						+ "          (d.danger_content_id,\n"
						+ "           d.workticket_no,\n"
						+ "           d.p_danger_id,\n"
						+ "           d.danger_name,\n"
						+ "           d.order_by,\n"
						+ "           d.modify_by,\n"
						+ "           d.modify_date,\n"
						+ "           d.enterprise_code,\n"
						+ "           d.is_use,d.is_runadd)\n"
						+ "          select (select nvl(max(t.danger_content_id), 0)\n"
						+ "                    from run_j_workticket_danger t) + row_number() over(order by a.order_by),\n"
						+ "                 '" + newWorkticketNo + "',\n"
						+ "                 '" + model.getDangerContentId()
						+ "',\n" + "                 a.danger_name,\n"
						+ "                 a.order_by,\n"
						+ "                 '" + workCode + "',\n"
						+ "                 sysdate,\n"
						+ "                 a.enterprise_code,\n"
						+ "                 'Y','N'\n"
						+ "          from run_j_workticket_danger a\n"
						+ "          where a.p_danger_id =" + dangerId;

				bll.exeNativeSQL(insertSql);
				entityManager.flush();
			}
		}

	}

	// -------------------------------------华丽的分割线---------------jyan---------------------------
	//查询已终结的工作票列表 090417 用于由终结票生成新的标准票
	public PageObject getEndWorkticketList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String equAttributeCode,String workticketContent,String workticketNo,
			int... rowStartIdxAndCount)
	{
		//modify by fyyang 090513  增加工作内容查询条件
		return this.getWorkticketListByContent(enterpriseCode, sdate, edate, workticketTypeCode, "8", null, null, equAttributeCode, null, null, "N", workticketContent, workticketNo,null, rowStartIdxAndCount);
		
//		return getWorktickets(enterpriseCode, sdate, edate, workticketTypeCode,
//				"8", null, null, equAttributeCode, null, null,
//				"N", null,rowStartIdxAndCount);
	}
	public PageObject getWorkticketMainList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String equAttributeCode,String repairSpecailCode,String deptId,String workticketContent,String workticketNo,
			int... rowStartIdxAndCount) {
		
		// 此方法即工作票查询方法（用于工作票查询页面）
//		return getWorktickets(enterpriseCode, sdate, edate, workticketTypeCode,
//				workticketStausId, null, null, equAttributeCode, null, null,
//				"N",null, rowStartIdxAndCount);
		//modify by fyyang 090423 增加按检修专业、所属班组、工作内容查询
		String sqlWhere="";
		if(deptId!=null&&!deptId.equals(""))
		{
			sqlWhere+=
				"  and  t.charge_dept=(\n" +
				"select a.dept_code from hr_c_dept a\n" + 
				"where a.dept_id="+deptId+"  and a.is_use='Y' and rownum=1) \n";  //update by sychen 20100902
//			"where a.dept_id="+deptId+"  and a.is_use='U' and rownum=1) \n"; 
		}
	 
		
		return	this.getWorkticketListByContent(enterpriseCode, sdate, edate, workticketTypeCode, workticketStausId, null, repairSpecailCode, equAttributeCode, null, null, "N",workticketContent,workticketNo, sqlWhere, rowStartIdxAndCount);
		
		
	}

	/**
	 * 此方法用于由标准票生成页面的查询列表 add by fyyang 090122
	 * modify by fyyang 20100506 增加动火级别查询条件
	 */
	public PageObject getStandListForSelect(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String mainEquName,String newOrOld,String workticketContent,String worktickNo,String fireLevel, int... rowStartIdxAndCount) {
		// 此方法用于由标准票生成页面的查询列表
		String sqlWhere="  ";
		if(mainEquName!=null&&!mainEquName.equals(""))
		{
		 sqlWhere = " and t.Main_Equ_Name like '%"
			+ mainEquName + "%' \n";
		}
		if("new".equals(newOrOld))
		{
			sqlWhere += "  and t.workticket_no not like '125MW%'\n";
			
		}
		else if("old".equals(newOrOld))
		{
			 sqlWhere += " and  t.workticket_no like '125MW%'\n";
			
		}
		if(worktickNo!=null&&!worktickNo.equals(""))
		{
			 sqlWhere +="  and t.workticket_no  like '%"+worktickNo+"%' \n";
		}
		if(workticketTypeCode!=null&&workticketTypeCode.equals("4"))
		{
			 sqlWhere +=" and t.firelevel_id="+fireLevel+" \n";
		}
		
	
		return	this.getWorkticketListByContent(enterpriseCode, sdate, edate, workticketTypeCode, 
				"4", null, null, null, 
				null, null, "Y",workticketContent,null, sqlWhere, rowStartIdxAndCount);
//		return getWorktickets(enterpriseCode, sdate, edate, workticketTypeCode,
//				"4", null, null, null, null, null, "Y",mainEquName,
//				rowStartIdxAndCount);
	}

	// add by fyyang 090122  modify by fyyang 090513 增加工作内容及填写人查询条件
	public PageObject queryStandTicketList(String enterpriseCode,String newOrOld, String sdate,
			String edate, String workticketTypeCode, String workticketStausId,
			String equAttributeCode,String repairSpecialCode,String entryBy,String workticketContent,String workticketNo, int... rowStartIdxAndCount) {
		// 此方法即标准工作票查询方法（用于标准工作票查询页面）
//		return getWorktickets(enterpriseCode, sdate, edate, workticketTypeCode,
//				workticketStausId, null, repairSpecialCode, equAttributeCode, newOrOld, null,
//				"Y",null, rowStartIdxAndCount);
		String sqlWhere="";
		
		if("new".equals(newOrOld))
		{
			sqlWhere += "  and t.workticket_no not like '125MW%'\n";
			
		}
		else if("old".equals(newOrOld))
		{
			 sqlWhere += " and  t.workticket_no like '125MW%'\n";
			
		}
		
		if(entryBy!=null&&!entryBy.equals(""))
		{
			 sqlWhere +="  and (t.entry_by  like '%"+entryBy+"%' or GETWORKERNAME(t.entry_by)  like '%"+entryBy+"%') \n";
		}
		if(workticketNo!=null&&!workticketNo.equals(""))
		{
			 sqlWhere +="  and t.workticket_no  like '%"+workticketNo+"%' \n";
		}
		return this.getWorkticketListByContent(enterpriseCode, sdate, edate, workticketTypeCode, 
				workticketStausId, null, repairSpecialCode, equAttributeCode, null,
				null, "Y", workticketContent, null,sqlWhere, rowStartIdxAndCount);
	}

	public PageObject getWorkticketRelMainList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String equAttributeCode,
			int... rowStartIdxAndCount) {
		// modify by fyyang 081230 工作票关联主票
		String sqlWhere = "  and  t.workticket_type_code not like '4'";
		return this.getWorkticketListByWhere(enterpriseCode, sdate, edate,
				workticketTypeCode, workticketStausId, null, null,
				equAttributeCode, null, null, "N", sqlWhere,
				rowStartIdxAndCount);

	}

	public PageObject getWorkticketHisTicketList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String equAttributeCode,
			String workticketNo,String isStandard,String firelevel,String workticketContent,int... rowStartIdxAndCount) {
		// modify by fyyang 081230 已终结票选择列表
		String sqlWhere = "";
		if(workticketNo!=null&&!workticketNo.equals(""))
		{
		sqlWhere ="  and  t.workticket_no not like '" + workticketNo
				+ "'";
		}
		if(workticketContent!=null&&!workticketContent.equals("")){
			sqlWhere ="  and  t.workticket_content  like '%" + workticketContent
			+ "%'";
		}
		//add by  fyyang 090729 增加动火级别查询条件
		if(workticketTypeCode.equals("4"))
		{
			if(firelevel!=null&&!firelevel.equals(""))
			{
				sqlWhere+="  and  t.firelevel_id="+firelevel+"  \n";
			}
		}
		return this.getWorkticketListByWhere(enterpriseCode, sdate, edate,
				workticketTypeCode, workticketStausId, null, null,
				equAttributeCode, null, null, isStandard, sqlWhere,
				rowStartIdxAndCount);
	}

	// 工作票审批查询列表
	public PageObject getWorkticketApproveList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String equAttributeCode, 
			String entryIds,String repairSpecailCode,String deptId,String workticketContent,String wticketNo,
			int... rowStartIdxAndCount) {
		String sqlWhere="";
		if(deptId!=null&&!deptId.equals(""))
		{
			sqlWhere+=
				"  and  t.charge_dept=(\n" +
				"select a.dept_code from hr_c_dept a\n" + 
				"where a.dept_id="+deptId+"  and a.is_use='Y' and rownum=1) \n";//update by sychen 20100902
//			"where a.dept_id="+deptId+"  and a.is_use='U' and rownum=1) \n";
		}
		
		
		return getApproveWorktickets(enterpriseCode, sdate, edate,
				workticketTypeCode, workticketStausId, null, repairSpecailCode,
				equAttributeCode, null, null, entryIds, "N",workticketContent,wticketNo,sqlWhere,
				rowStartIdxAndCount);
	}

	/**
	 * 标准票审批查询列表
	 * 
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param workticketTypeCode
	 * @param workticketStausId
	 * @param equAttributeCode
	 * @param entryIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getStandardTicketApproveList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String equAttributeCode, String entryIds,String repairSpecialCode,
			int... rowStartIdxAndCount) {
		return getApproveWorktickets(enterpriseCode, sdate, edate,
				workticketTypeCode, workticketStausId, null, repairSpecialCode,
				equAttributeCode, null, null, entryIds, "Y",null,null,null,
				rowStartIdxAndCount);
	}

	//modify by fyyang  090310 上报列表中只查登录的人填写的工作票
	public PageObject getWorkticketReportList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String equAttributeCode,
			String isStandard,String workerCode, int... rowStartIdxAndCount) {
		String sqlWhere="  and t.entry_by='"+workerCode+"'   order by t.entry_date desc";
		return this.getWorkticketListByWhere(enterpriseCode, sdate, edate, workticketTypeCode,
				workticketStausId, null, null, equAttributeCode,
				null, null, isStandard, sqlWhere, rowStartIdxAndCount);
//		return getWorktickets(enterpriseCode, sdate, edate, workticketTypeCode,
//				workticketStausId, null, null, equAttributeCode, null, null,
//				isStandard, rowStartIdxAndCount);
	}
	
	
	//增加了根据工作内容查询  用于工作票查询页面
	private PageObject getWorkticketListByContent(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String permissionDept,
			String repairSpecailCode, String equAttributeCode,
			String chargeDept, String fuzzy, String isStandard,String workticketContent,String workticketNo,
			String sqlWhere, final int... rowStartIdxAndCount) {
		try {
			PageObject result = null;

			String sql = "select distinct t.workticket_no, \n"
					+ "       t.workticket_content, \n"
					+ "       t.workticket_staus_id, \n"
					+ "       decode(t.is_standard,'N',GETWTSTATUSNAME(t.workticket_staus_id),'Y',GETSTANDSTATUSNAME(t.workticket_staus_id)) workticketstatusname, \n"
					+ "       charge_by, \n"
					+ "       getworkername(t.charge_by) chargebyname, \n"
					+ "       t.charge_dept, \n"
					+ "       getdeptname(t.charge_dept) deptname, \n"
					+ "       to_char(t.plan_start_date, 'yyyy-MM-dd hh24:mi:ss') planstartdate, \n"
					+ "       to_char(t.plan_end_date, 'yyyy-MM-dd hh24:mi:ss') planenddate, \n"
					+ "       to_char(t.approved_finish_date, 'yyyy-MM-dd hh24:mi:ss') approveenddate, \n"
					+ "       t.equ_attribute_code, \n"
					+ "       GETBlockBYCODE(t.equ_attribute_code) blockname, \n"
					+ "       is_emergency, \n"
					+ "       decode(t.is_emergency, 'Y', '紧急', 'N', '普通') is_emergencytext, \n"
					+ "       t.workticket_type_code, \n"
					+ "       t.SOURCE_ID, \n"
					+ "		  getwtsourcename(t.SOURCE_ID) sourcename,work_flow_no,firelevel_id,\n"
				    + "       getworkername(t.entry_by),to_char(t.entry_date, 'yyyy-MM-dd hh24:mi:ss'),t.is_standard,t.is_createby_stand, \n"
				    +"        nvl(getspecialname(t.repair_specail_code),t.repair_specail_code),t.main_equ_code,t.main_equ_name\n"
					+ "  from run_j_worktickets t  where t.is_use='Y'  and t.enterprise_code='" + enterpriseCode + "' \n";
					
					
				
			     
			String sqlCount = "select count(1) \n"
					+ "  from run_j_worktickets t   where  t.is_use='Y'  and t.enterprise_code='" + enterpriseCode + "' \n";
			if(workticketContent!=null&&!workticketContent.equals(""))
			{
				sql+="  and t.workticket_content like '%"+workticketContent.trim()+"%'";
				sqlCount+="  and t.workticket_content like '%"+workticketContent.trim()+"%'";
			}
			if(workticketNo!=null&&!workticketNo.equals("")){
				sql+="  and t.workticket_no like '%"+workticketNo.trim()+"%'";
				sqlCount+="  and t.workticket_no like '%"+workticketNo.trim()+"%'";
			}
		
			
			      
			
			
			
			if (sdate != null && !sdate.equals("")) {
				String strWhere = "  and t.plan_start_date >=to_date('" + sdate
						+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (edate != null && !edate.equals("")) {
				String strWhere = "  and t.plan_end_date <=to_date('" + edate
						+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (workticketTypeCode != null && !workticketTypeCode.equals("")) {
				String strWhere = "  and to_char(t.workticket_type_code) like '"
						+ workticketTypeCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (workticketStausId != null && !workticketStausId.equals("")) {
				String strWhere="";
				if(workticketStausId.equals("5") || workticketStausId.equals("18")){
					strWhere="  and to_char(t.workticket_staus_id)  in (5,18)\n";
				}
				//动火票终结为28
				else if(workticketStausId.equals("8") || workticketStausId.equals("28")){
					strWhere="  and to_char(t.workticket_staus_id)  in (8,28)\n";
				}
				else{
					strWhere = "  and to_char(t.workticket_staus_id)  in ("
						+ workticketStausId + ")\n";
				}
				
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (permissionDept != null && !permissionDept.equals("")) {
				String strWhere = "   and t.PERMISSION_DEPT like '"
						+ permissionDept + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (repairSpecailCode != null && !repairSpecailCode.equals("")) {
				String strWhere = "  and t.REPAIR_SPECAIL_CODE like '"
						+ repairSpecailCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (equAttributeCode != null && !equAttributeCode.equals("")) {
				String strWhere = "   and t.EQU_ATTRIBUTE_CODE like '"
						+ equAttributeCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (chargeDept != null && !chargeDept.equals("")) {
				String strWhere = "   and t.CHARGE_DEPT like '" + chargeDept
						+ "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (fuzzy != null && !fuzzy.equals("")) {
				String strWhere = " and t.charge_by||getworkername(t.charge_by) like '%"
						+ fuzzy + "%'";
				sql += strWhere;
				sqlCount += strWhere;
			}
			// add by fyyang 090110 是否标准票
			if (isStandard != null && !isStandard.equals("")) {
				String strWhere = "";
				if (isStandard.equals("N")) {
					strWhere = "   and (t.is_standard='" + isStandard
							+ "'  or  t.is_standard is null)\n";
				} else {
					strWhere = "  and t.is_standard='" + isStandard + "'";
				}
				sql += strWhere;
				sqlCount += strWhere;
			}
			// add by fyyang
			if (sqlWhere != null && !sqlWhere.equals("")) {
				sql += sqlWhere;
				sqlCount += sqlWhere;
			}
			if(sqlWhere!=null&&sqlWhere.indexOf("order by")==-1)
			{
			sql += " order by t.workticket_no";
			}   
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			if (list != null && list.size() > 0) {
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					RunJWorktickets model = new RunJWorktickets();
					WorkticketInfo omodel = new WorkticketInfo();
					Object[] data = (Object[]) it.next();
					model.setWorkticketNo(data[0].toString());
					if (data[1] != null)
						model.setWorkticketContent(data[1].toString());
					if (data[2] != null)
						model.setWorkticketStausId(Long.parseLong(data[2]
								.toString()));
					if (data[3] != null)
						omodel.setStatusName(data[3].toString());
					if (data[4] != null)
						model.setChargeBy(data[4].toString());
					if (data[5] != null)
						omodel.setChargeByName(data[5].toString());
					if (data[6] != null)
						model.setChargeDept(data[6].toString());
					if (data[7] != null)
						omodel.setDeptName(data[7].toString());
					if (data[8] != null)
						omodel.setPlanStartDate(data[8].toString());
					if (data[9] != null)
						omodel.setPlanEndDate(data[9].toString());
					if (data[10] != null)
						omodel.setApproveEndDate(data[10].toString());
					if (data[11] != null)
						model.setEquAttributeCode(data[11].toString());
					if (data[12] != null)
						omodel.setBlockName(data[12].toString());
					if (data[13] != null)
						model.setIsEmergency(data[13].toString());
					if (data[14] != null)
						omodel.setIsEmergencyText(data[14].toString());
					if (data[15] != null)
						model.setWorkticketTypeCode(data[15].toString());
					if (data[16] != null)
						model.setSourceId(Long.parseLong(data[16].toString()));
					if (data[17] != null)
						omodel.setSourceName(data[17].toString());
					if (data[18] != null)
						model
								.setWorkFlowNo(Long.parseLong(data[18]
										.toString()));
					if(data[19]!=null)
					{
						model.setFirelevelId(Long.parseLong(data[19].toString()));
					}
					if(data[20]!=null)
					{
						model.setEntryBy(data[20].toString());
					}
					if(data[21]!=null)
					{
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							model.setEntryDate(df.parse(data[21].toString()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if(data[22]!=null)
					{
						model.setIsStandard(data[22].toString());
					}
					if(data[23]!=null)
					{
						model.setIsCreatebyStand(data[23].toString());
					}
					if(data[24]!=null)
					{
						omodel.setRepairSpecailName(data[24].toString());
					}
					if(data[25]!=null)
					{
						model.setMainEquCode(data[25].toString());
					}
					if(data[26]!=null)
					{
						model.setMainEquName(data[26].toString());
					}
					omodel.setModel(model);
					arrlist.add(omodel);
				}
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount)
						.toString());
				result.setList(arrlist);
				result.setTotalCount(totalCount);
			}
			return result;
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * add by fyyang 081230 用到此方法的地方：关联主票查询，由现有票生成,紧急票补签列表,安措拆除列表,工作票上报列表
	 * 
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param workticketTypeCode
	 * @param workticketStausId
	 * @param permissionDept
	 * @param repairSpecailCode
	 * @param equAttributeCode
	 * @param chargeDept
	 * @param fuzzy
	 * @param sqlWhere
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PageObject getWorkticketListByWhere(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String permissionDept,
			String repairSpecailCode, String equAttributeCode,
			String chargeDept, String fuzzy, String isStandard,
			String sqlWhere, final int... rowStartIdxAndCount) {
		try {
			PageObject result = null;

			String sql = "select t.workticket_no, \n"
					+ "       t.workticket_content, \n"
					+ "       t.workticket_staus_id, \n"
					+ "       decode(t.is_standard,'N',GETWTSTATUSNAME(t.workticket_staus_id),'Y',GETSTANDSTATUSNAME(t.workticket_staus_id)) workticketstatusname, \n"
					+ "       charge_by, \n"
					+ "       getworkername(t.charge_by) chargebyname, \n"
					+ "       t.charge_dept, \n"
					+ "       getdeptname(t.charge_dept) deptname, \n"
					+ "       to_char(t.plan_start_date, 'yyyy-MM-dd hh24:mi:ss') planstartdate, \n"
					+ "       to_char(t.plan_end_date, 'yyyy-MM-dd hh24:mi:ss') planenddate, \n"
					+ "       to_char(t.approved_finish_date, 'yyyy-MM-dd hh24:mi:ss') approveenddate, \n"
					+ "       t.equ_attribute_code, \n"
					+ "       GETBlockBYCODE(t.equ_attribute_code) blockname, \n"
					+ "       is_emergency, \n"
					+ "       decode(t.is_emergency, 'Y', '紧急', 'N', '普通') is_emergencytext, \n"
					+ "       t.workticket_type_code, \n"
					+ "       t.SOURCE_ID, \n"
					+ "		  getwtsourcename(t.SOURCE_ID) sourcename,work_flow_no,firelevel_id,\n"
				    + "       getworkername(t.entry_by),to_char(t.entry_date, 'yyyy-MM-dd hh24:mi:ss'),t.is_standard,t.is_createby_stand, \n"
				    +"        nvl(getspecialname(t.repair_specail_code),t.repair_specail_code),t.main_equ_code,t.main_equ_name\n"
					+ "  from run_j_worktickets t where t.is_use='Y' \n"
					+ "   and enterprise_code='" + enterpriseCode + "' \n";
			String sqlCount = "select count(1) \n"
					+ "  from run_j_worktickets t where t.is_use='Y' \n"
					+ "   and enterprise_code='" + enterpriseCode + "' \n";
			if (sdate != null && !sdate.equals("")) {
				String strWhere = "  and t.plan_start_date >=to_date('" + sdate
						+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (edate != null && !edate.equals("")) {
				String strWhere = "  and t.plan_end_date <=to_date('" + edate
						+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (workticketTypeCode != null && !workticketTypeCode.equals("")) {
				String strWhere = "  and to_char(t.workticket_type_code) like '"
						+ workticketTypeCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (workticketStausId != null && !workticketStausId.equals("")) {
				String strWhere = "  and to_char(t.workticket_staus_id)  in ("
						+ workticketStausId + ")\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (permissionDept != null && !permissionDept.equals("")) {
				String strWhere = "   and t.PERMISSION_DEPT like '"
						+ permissionDept + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (repairSpecailCode != null && !repairSpecailCode.equals("")) {
				String strWhere = "  and t.REPAIR_SPECAIL_CODE like '"
						+ repairSpecailCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (equAttributeCode != null && !equAttributeCode.equals("")) {
				String strWhere = "   and t.EQU_ATTRIBUTE_CODE like '"
						+ equAttributeCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (chargeDept != null && !chargeDept.equals("")) {
				String strWhere = "   and t.CHARGE_DEPT like '" + chargeDept
						+ "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (fuzzy != null && !fuzzy.equals("")) {
				String strWhere = " and t.charge_by||getworkername(t.charge_by) like '%"
						+ fuzzy + "%'";
				sql += strWhere;
				sqlCount += strWhere;
			}
			// add by fyyang 090110 是否标准票
			if (isStandard != null && !isStandard.equals("")) {
				String strWhere = "";
				if (isStandard.equals("N")) {
					strWhere = "   and (t.is_standard='" + isStandard
							+ "'  or  t.is_standard is null)\n";
				} else {
					strWhere = "  and t.is_standard='" + isStandard + "'";
				}
				sql += strWhere;
				sqlCount += strWhere;
			}
			// add by fyyang
			if (sqlWhere != null && !sqlWhere.equals("")) {
				sql += sqlWhere;
				sqlCount += sqlWhere;
			}
			if(sqlWhere!=null&&sqlWhere.indexOf("order by")==-1)
			{
			sql += " order by t.workticket_no";
			} 
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			if (list != null && list.size() > 0) {
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					RunJWorktickets model = new RunJWorktickets();
					WorkticketInfo omodel = new WorkticketInfo();
					Object[] data = (Object[]) it.next();
					model.setWorkticketNo(data[0].toString());
					if (data[1] != null)
						model.setWorkticketContent(data[1].toString());
					if (data[2] != null)
						model.setWorkticketStausId(Long.parseLong(data[2]
								.toString()));
					if (data[3] != null)
						omodel.setStatusName(data[3].toString());
					if (data[4] != null)
						model.setChargeBy(data[4].toString());
					if (data[5] != null)
						omodel.setChargeByName(data[5].toString());
					if (data[6] != null)
						model.setChargeDept(data[6].toString());
					if (data[7] != null)
						omodel.setDeptName(data[7].toString());
					if (data[8] != null)
						omodel.setPlanStartDate(data[8].toString());
					if (data[9] != null)
						omodel.setPlanEndDate(data[9].toString());
					if (data[10] != null)
						omodel.setApproveEndDate(data[10].toString());
					if (data[11] != null)
						model.setEquAttributeCode(data[11].toString());
					if (data[12] != null)
						omodel.setBlockName(data[12].toString());
					if (data[13] != null)
						model.setIsEmergency(data[13].toString());
					if (data[14] != null)
						omodel.setIsEmergencyText(data[14].toString());
					if (data[15] != null)
						model.setWorkticketTypeCode(data[15].toString());
					if (data[16] != null)
						model.setSourceId(Long.parseLong(data[16].toString()));
					if (data[17] != null)
						omodel.setSourceName(data[17].toString());
					if (data[18] != null)
						model
								.setWorkFlowNo(Long.parseLong(data[18]
										.toString()));
					if(data[19]!=null)
					{
						model.setFirelevelId(Long.parseLong(data[19].toString()));
					}
					if(data[20]!=null)
					{
						model.setEntryBy(data[20].toString());
					}
					if(data[21]!=null)
					{
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							model.setEntryDate(df.parse(data[21].toString()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if(data[22]!=null)
					{
						model.setIsStandard(data[22].toString());
					}
					if(data[23]!=null)
					{
						model.setIsCreatebyStand(data[23].toString());
					}
					if(data[24]!=null)
					{
						omodel.setRepairSpecailName(data[24].toString());
					}
					if(data[25]!=null)
					{
						model.setMainEquCode(data[25].toString());
					}
					if(data[26]!=null)
					{
						model.setMainEquName(data[26].toString());
					}
					omodel.setModel(model);
					arrlist.add(omodel);
				}
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount)
						.toString());
				result.setList(arrlist);
				result.setTotalCount(totalCount);
			}
			return result;
		} catch (RuntimeException e) {
			throw e;
		}
	}

	// 工作票及标准票的审批查询列表
	private PageObject getApproveWorktickets(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String permissionDept,
			String repairSpecailCode, String equAttributeCode,
			String chargeDept, String fuzzy, String entryIds,
			String isStandard,// add by fyyang
			String workticketContent,
			String wticketNo,
			String sqlWhere,
			final int... rowStartIdxAndCount) {
		try {
			if (entryIds == null || "".equals(entryIds)) {
				return null;
			}
			PageObject result = null;
			String sql = "select distinct  t.workticket_no, \n"
					+ "       t.workticket_content, \n"
					+ "       t.workticket_staus_id, \n";
			if (isStandard.equals("Y")) {
				sql = sql
						+ "       GETSTANDSTATUSNAME(t.workticket_staus_id) workticketstatusname, \n";
			} else {
				sql = sql
						+ "       GETWTSTATUSNAME(t.workticket_staus_id) workticketstatusname, \n";
			}

			sql = sql
					+ "       charge_by, \n"
					+ "       getworkername(t.charge_by) chargebyname, \n"
					+ "       t.charge_dept, \n"
					+ "       getdeptname(t.charge_dept) deptname, \n" 
					+ "       to_char(t.plan_start_date, 'yyyy-MM-dd hh24:mi:ss') planstartdate, \n"
					+ "       to_char(t.plan_end_date, 'yyyy-MM-dd hh24:mi:ss') planenddate, \n"
					+ "       to_char(t.approved_finish_date, 'yyyy-MM-dd hh24:mi:ss') approveenddate, \n"
					+ "       t.equ_attribute_code, \n"
					+ "       GETBlockBYCODE(t.equ_attribute_code) blockname, \n"
					+ "       is_emergency, \n"
					+ "       decode(t.is_emergency, 'Y', '紧急', 'N', '普通') is_emergencytext, \n"
					+ "       t.workticket_type_code, \n"
					+ "       t.SOURCE_ID, \n"
					+ "		  getwtsourcename(t.SOURCE_ID) sourcename,work_flow_no,firelevel_id,nvl(getspecialname(t.repair_specail_code),t.repair_specail_code),getworkername(t.entry_by)\n"
					+ "  from run_j_worktickets t  where  t.is_use='Y' and t.enterprise_code='" + enterpriseCode + "' \n";
		
//			if(workticketContent!=null&&!workticketContent.equals(""))
//			{
//				sql=sql+",run_j_workticket_content tt  where  t.is_use='Y'  and t.workticket_no=tt.workticket_no and tt.equ_name||decode(tt.attribute_code,'temp','','（' || tt.attribute_code || '）')||tt.back_key_desc||tt.worktype_name||tt.flag_desc like '%"+workticketContent.trim()+"%' \n";
//			}
			
					
					
			String sqlCount = "select count(1) \n"
					+ "  from run_j_worktickets t where  t.is_use='Y' and t.enterprise_code='" + enterpriseCode + "' \n";
			if(workticketContent!=null&&!workticketContent.equals(""))
			{
				sql+="  and t.workticket_content like '%"+workticketContent.trim()+"%'";
				sqlCount+="  and t.workticket_content like '%"+workticketContent.trim()+"%'";
			}
			if(wticketNo!=null&&!wticketNo.equals(""))
			{
				sql+="  and t.workticket_no like '%"+wticketNo.trim()+"%'";
				sqlCount+="  and t.workticket_no like '%"+wticketNo.trim()+"%'";
			}
			
			sqlCount=sqlCount+ "   and t.enterprise_code='" + enterpriseCode + "' \n";
					
			if (sdate != null && !sdate.equals("")) {
				String strWhere = "  and t.plan_start_date >=to_date('" + sdate
						+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (edate != null && !edate.equals("")) {
				String strWhere = "  and t.plan_end_date <=to_date('" + edate
						+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (workticketTypeCode != null && !workticketTypeCode.equals("")) { 
				String strWhere = "  and to_char(t.workticket_type_code) like '" 
						+ workticketTypeCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (workticketStausId != null && !workticketStausId.equals("")) { 
				String strWhere = "  and to_char(t.workticket_staus_id)  in ("
						+ workticketStausId + ")\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (permissionDept != null && !permissionDept.equals("")) {
				String strWhere = "   and t.PERMISSION_DEPT like '"
						+ permissionDept + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (repairSpecailCode != null && !repairSpecailCode.equals("")) {
				String strWhere = "  and t.REPAIR_SPECAIL_CODE like '"
						+ repairSpecailCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (equAttributeCode != null && !equAttributeCode.equals("")) {
				String strWhere = "   and t.EQU_ATTRIBUTE_CODE like '"
						+ equAttributeCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (chargeDept != null && !chargeDept.equals("")) {
				String strWhere = "   and t.CHARGE_DEPT like '" + chargeDept
						+ "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (fuzzy != null && !fuzzy.equals("")) {
				String strWhere = " and t.charge_by||getworkername(t.charge_by) like '%"
						+ fuzzy + "%'";
				sql += strWhere;
				sqlCount += strWhere;
			}

			// add by fyyang 090110 是否标准票
			if (isStandard != null && !isStandard.equals("")) {
				String strWhere = "";
				if (isStandard.equals("N")) {
					strWhere = "   and (t.is_standard='" + isStandard
							+ "'  or  t.is_standard is null)\n";
				} else {
					strWhere = "  and t.is_standard='" + isStandard + "'";
				}
				sql += strWhere;
				sqlCount += strWhere;
			}  
//			String strWhere = " and work_flow_no in (" + entryIds + ") ";
            String strWhere =" and "+ bll.subStr(entryIds, ",", 100, "work_flow_no"); 
			sql += strWhere;
			sqlCount += strWhere;
            
			if(sqlWhere!=null&&!sqlWhere.equals(""))
			{
				sql += sqlWhere;
				sqlCount += sqlWhere;
			}
			
			// System.out.println(sql);
			sql += " order by t.workticket_no";

			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			if (list != null && list.size() > 0) {
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					RunJWorktickets model = new RunJWorktickets();
					WorkticketInfo omodel = new WorkticketInfo();
					Object[] data = (Object[]) it.next();
					model.setWorkticketNo(data[0].toString());
					if (data[1] != null)
						model.setWorkticketContent(data[1].toString());
					if (data[2] != null)
						model.setWorkticketStausId(Long.parseLong(data[2]
								.toString()));
					if (data[3] != null)
						omodel.setStatusName(data[3].toString());
					if (data[4] != null)
						model.setChargeBy(data[4].toString());
					if (data[5] != null)
						omodel.setChargeByName(data[5].toString());
					if (data[6] != null)
						model.setChargeDept(data[6].toString());
					if (data[7] != null)
						omodel.setDeptName(data[7].toString());
					if (data[8] != null)
						omodel.setPlanStartDate(data[8].toString());
					if (data[9] != null)
						omodel.setPlanEndDate(data[9].toString());
					if (data[10] != null)
						omodel.setApproveEndDate(data[10].toString());
					if (data[11] != null)
						model.setEquAttributeCode(data[11].toString());
					if (data[12] != null)
						omodel.setBlockName(data[12].toString());
					if (data[13] != null)
						model.setIsEmergency(data[13].toString());
					if (data[14] != null)
						omodel.setIsEmergencyText(data[14].toString());
					if (data[15] != null)
						model.setWorkticketTypeCode(data[15].toString());
					if (data[16] != null)
						model.setSourceId(Long.parseLong(data[16].toString()));
					if (data[17] != null)
						omodel.setSourceName(data[17].toString());
					if (data[18] != null)
						model
								.setWorkFlowNo(Long.parseLong(data[18]
										.toString()));
					if(data[19]!=null)
					{
						model.setFirelevelId(Long.parseLong(data[19].toString()));
					}
					if(data[20]!=null)
					{
						omodel.setRepairSpecailName(data[20].toString());
					}
					if(data[21]!=null)
					{
						model.setEntryBy(data[21].toString());
					}
					omodel.setModel(model);
					arrlist.add(omodel);
				}
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount)
						.toString());
				result.setList(arrlist);
				result.setTotalCount(totalCount);
			}
			return result;
		} catch (RuntimeException e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	private PageObject getWorktickets(String enterpriseCode, String sdate,
			String edate, String workticketTypeCode, String workticketStausId,
			String permissionDept, String repairSpecailCode,
			String equAttributeCode, String chargeDept, String fuzzy,
			String isStandard,String mainEquName, final int... rowStartIdxAndCount) {
		try {
			PageObject result = null;
			String sql = "select t.workticket_no, \n"
					+ "       t.workticket_content, \n"
					+ "       t.workticket_staus_id, \n";
			// modify by fyyang
			if (isStandard.equals("Y")) {
				sql = sql
						+ "       GETSTANDSTATUSNAME(t.workticket_staus_id) workticketstatusname, \n";
			} else {
				sql = sql
						+ "       GETWTSTATUSNAME(t.workticket_staus_id) workticketstatusname, \n";
			}
			sql = sql
					+ "       charge_by, \n"
					+ "       getworkername(t.charge_by) chargebyname, \n"
					+ "       t.charge_dept, \n"
					+ "       getdeptname(t.charge_dept) deptname, \n"
					// modified by zhouxu 12/15 start
					// + " to_char(t.plan_start_date, 'yyyy-MM-dd hh24:mi')
					// planstartdate, \n"
					// + " to_char(t.plan_end_date, 'yyyy-MM-dd hh24:mi')
					// planenddate, \n"
					// + " to_char(t.approved_finish_date, 'yyyy-MM-dd hh24:mi')
					// approveenddate, \n"
					+ "       to_char(t.plan_start_date, 'yyyy-MM-dd hh24:mi:ss') planstartdate, \n"
					+ "       to_char(t.plan_end_date, 'yyyy-MM-dd hh24:mi:ss') planenddate, \n"
					+ "       to_char(t.approved_finish_date, 'yyyy-MM-dd hh24:mi:ss') approveenddate, \n"
					// modified by zhouxu 12/15 end
					+ "       t.equ_attribute_code, \n"
					+ "       GETBlockBYCODE(t.equ_attribute_code) blockname, \n"
					+ "       is_emergency, \n"
					+ "       decode(t.is_emergency, 'Y', '紧急', 'N', '普通') is_emergencytext, \n"
					+ "       t.workticket_type_code, \n"
					+ "       t.SOURCE_ID, \n"
					+ "		  getwtsourcename(t.SOURCE_ID) sourcename,work_flow_no,t.firelevel_id,nvl(getspecialname(t.repair_specail_code),t.repair_specail_code),getworkername(t.entry_by),t.main_equ_code,t.main_equ_name\n"
					+ "  from run_j_worktickets t where t.is_use='Y' \n"
					+ "   and enterprise_code='" + enterpriseCode + "' \n";
			String sqlCount = "select count(1) \n"
					+ "  from run_j_worktickets t where t.is_use='Y' \n"
					+ "   and enterprise_code='" + enterpriseCode + "' \n";
			if (sdate != null && !sdate.equals("")) {
				String strWhere = "  and t.plan_start_date >=to_date('" + sdate
						+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (edate != null && !edate.equals("")) {
				String strWhere = "  and t.plan_end_date <=to_date('" + edate
						+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (workticketTypeCode != null && !workticketTypeCode.equals("")) {
				// modified by jyuan 12/13 start
				// String strWhere = " and to_char(t.workticket_type_id) like '"
				String strWhere = "  and to_char(t.workticket_type_code) like '"
						// modified by jyuan 12/13 end
						+ workticketTypeCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (workticketStausId != null && !workticketStausId.equals("")) {
				// String strWhere = " and to_char(t.workticket_staus_id) like
				// '"
				// modify by fyyang 081230
				String strWhere = "  and to_char(t.workticket_staus_id)  in ("
						+ workticketStausId + ")\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (permissionDept != null && !permissionDept.equals("")) {
				String strWhere = "   and t.PERMISSION_DEPT like '"
						+ permissionDept + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (repairSpecailCode != null && !repairSpecailCode.equals("")) {
				String strWhere = "  and t.REPAIR_SPECAIL_CODE like '"
						+ repairSpecailCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (equAttributeCode != null && !equAttributeCode.equals("")) {
				String strWhere = "   and t.EQU_ATTRIBUTE_CODE like '"
						+ equAttributeCode + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			//如果为标准票查询,chargeDept重用为按老系统数据,还是按新系统数据查询
			if("Y".equals(isStandard))
			{
				if("new".equals(chargeDept))
				{
					String strWhere = "  and t.workticket_no not like '125MW%'\n";
					sql += strWhere;
					sqlCount += strWhere; 
				}
				else if("old".equals(chargeDept))
				{
					String strWhere = " and  t.workticket_no like '125MW%'\n";
					sql += strWhere;
					sqlCount += strWhere; 
				}
			}else
			{
				if (chargeDept != null && !chargeDept.equals("")) {
					String strWhere = "   and t.CHARGE_DEPT like '" + chargeDept
							+ "'\n";
					sql += strWhere;
					sqlCount += strWhere;
				}
			}
			if (fuzzy != null && !fuzzy.equals("")) {
				String strWhere = " and t.charge_by||getworkername(t.charge_by) like '%"
						+ fuzzy + "%'";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if(mainEquName !=null && !mainEquName.trim().equals(""))
			{
				String strWhere = " and t.Main_Equ_Name like '%"
					+ mainEquName + "%'";
				sql += strWhere;
				sqlCount += strWhere;
			}
			// add by fyyang 090110 是否标准票
			if (isStandard != null && !isStandard.equals("")) {
				String strWhere = "";
				if (isStandard.equals("N")) {
					strWhere = "   and (t.is_standard='" + isStandard
							+ "'  or  t.is_standard is null)\n";
				} else {
					strWhere = "  and t.is_standard='" + isStandard + "'";
				}
				sql += strWhere;
				sqlCount += strWhere;
			}
			sql += " order by t.workticket_no";  
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			if (list != null && list.size() > 0) {
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					RunJWorktickets model = new RunJWorktickets();
					WorkticketInfo omodel = new WorkticketInfo();
					Object[] data = (Object[]) it.next();
					model.setWorkticketNo(data[0].toString());
					if (data[1] != null)
						model.setWorkticketContent(data[1].toString());
					if (data[2] != null)
						model.setWorkticketStausId(Long.parseLong(data[2]
								.toString()));
					if (data[3] != null)
						omodel.setStatusName(data[3].toString());
					if (data[4] != null)
						model.setChargeBy(data[4].toString());
					if (data[5] != null)
						omodel.setChargeByName(data[5].toString());
					if (data[6] != null)
						model.setChargeDept(data[6].toString());
					if (data[7] != null)
						omodel.setDeptName(data[7].toString());
					if (data[8] != null)
						omodel.setPlanStartDate(data[8].toString());
					if (data[9] != null)
						omodel.setPlanEndDate(data[9].toString());
					if (data[10] != null)
						omodel.setApproveEndDate(data[10].toString());
					if (data[11] != null)
						model.setEquAttributeCode(data[11].toString());
					if (data[12] != null)
						omodel.setBlockName(data[12].toString());
					if (data[13] != null)
						model.setIsEmergency(data[13].toString());
					if (data[14] != null)
						omodel.setIsEmergencyText(data[14].toString());
					if (data[15] != null)
						model.setWorkticketTypeCode(data[15].toString());
					if (data[16] != null)
						model.setSourceId(Long.parseLong(data[16].toString()));
					if (data[17] != null)
						omodel.setSourceName(data[17].toString());
					if (data[18] != null)
						model
								.setWorkFlowNo(Long.parseLong(data[18]
										.toString()));
					if (data[19] != null) {
						// add 090218
						model.setFirelevelId(Long
								.parseLong(data[19].toString()));
					}
					if(data[20]!=null)
					{
						omodel.setRepairSpecailName(data[20].toString());
					}
					if(data[21]!=null)
					{
						model.setEntryBy(data[21].toString());
					}
					if(data[22] != null)
					{
						model.setMainEquCode(data[22].toString());
					}
					if(data[23] != null)
					{
						model.setMainEquName(data[23].toString());
					}
					omodel.setModel(model);
					arrlist.add(omodel);
				}
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount)
						.toString());
				result.setList(arrlist);
				result.setTotalCount(totalCount);
			}
			return result;
		} catch (RuntimeException e) {
			throw e;
		}
	}

	//
	@SuppressWarnings("unchecked")
	public WorkticketInfo queryWorkticket(String enterpriseCode,
			String workticketNo) {
		try {
			WorkticketInfo model = new WorkticketInfo();
			String sql = "select t.* from run_j_worktickets t \n"
					+ "	   where workticket_no='" + workticketNo
					+ "'	 and rownum=1 \n" + "		 and t.is_use='Y' \n"
					+ "      and enterprise_code='" + enterpriseCode + "'";
			String sqlname = "select getworkername(t.charge_by) chargebyname, \n"
					+ "              getworkername(t.WATCHER) releasebyname, \n"
					+ "              getworkername(t.FIRETICKET_EXECUTE_BY) receivebyname,\n"
					+ "              GETDEPTNAME(t.charge_dept) chargedeptname, \n"
					+ "              GETDANGERTYPENAME(t.danger_type), \n"
					+ "              GETwttypeAME(t.workticket_type_code), \n"
					+ "              getspecialname(t.repair_specail_code), \n"
					+ "              getspecialname(t.permission_dept), \n"
					+ "              GETBlockBYCODE(t.equ_attribute_code), \n"
					+ "              GETWTSOURCENAME(t.source_id) \n"
					+ "         from run_j_worktickets t \n"
					+ "		   where workticket_no='"
					+ workticketNo
					+ "'	     and rownum=1 \n"
					+ "         and t.is_use='Y' \n"
					+ "          and enterprise_code='" + enterpriseCode + "'";
			List<RunJWorktickets> list = bll.queryByNativeSQL(sql,
					RunJWorktickets.class);
			if (list != null) {
				if (list.size() > 0) {
					RunJWorktickets baseModel = list.get(0);
					model.setModel(baseModel);
					if (baseModel.getPlanEndDate() != null) {
						model.setPlanEndDate(baseModel.getPlanEndDate()
								.toString());
					}
					if (baseModel.getPlanStartDate() != null) {
						model.setPlanStartDate(baseModel.getPlanStartDate()
								.toString());
					}
					if (baseModel.getApprovedFinishDate() != null) {
						model.setApproveEndDate(baseModel
								.getApprovedFinishDate().toString());
					}
				}
			}

			// model.setModelRunJWorktickets(list.get(0));
			List alist = bll.queryByNativeSQL(sqlname);
			if (alist.size() > 0) {
				Object[] obj = (Object[]) alist.get(0);
				if (obj[0] != null) {
					model.setChargeByName(obj[0].toString());
				}
				if (obj[1] != null) {
					model.setWatcherName(obj[1].toString());
				}
				if (obj[2] != null) {
					model.setFireTickerExeByName(obj[2].toString());
				}
				if (obj[3] != null) {
					model.setDeptName(obj[3].toString());
				}
				if (obj[4] != null) {
					model.setDangerTypeName(obj[4].toString());
				}
				if(obj[5]!=null)
				{
					model.setWorkticketTypeName(obj[5].toString());
				}
				if(obj[6]!=null)
				{
					model.setRepairSpecailName(obj[6].toString());
				}
				if(obj[7]!=null)
				{
					model.setRecieveSpecailName(obj[7].toString());
				}
				if(obj[8]!=null)
				{
					model.setBlockName(obj[8].toString());
				}
				if(obj[9]!=null)
				{
					model.setSourceName(obj[9].toString());
				}

			}
			return model;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}

	}

	// =====================================分割线========drdu==================================

	@SuppressWarnings("unchecked")
	/**
	 * 变更工作票负责人 根据企业编码，开始时间，结束时间，工作票类型，运行专业，检修专业，工作负责人部门查询工作票信息
	 */
	public PageObject queryWorkticketetHis(String enterpriseCode, String sdate,
			String edate, String workticketTypeCode, String permissionDept,
			String repairSpecailCode, String chargeDept,
			final int... rowStartIdxAndCount) {
		return this.getWorktickets(enterpriseCode, sdate, edate,
				workticketTypeCode, "7", permissionDept, repairSpecailCode,
				null, chargeDept, null, "N",null, rowStartIdxAndCount);
	}

	/**
	 * 工作票交回、恢复查询 根据企业编码，开始时间，结束时间，工作票类型，运行专业，检修专业，工作负责人部门查询工作票信息 add by
	 * LiuYingwen
	 */

	public PageObject queryWorkticketet(String enterpriseCode, String sdate,
			String edate, String workticketTypeCode, String permissionDept,
			String repairSpecailCode, String chargeDept,
			final int... rowStartIdxAndCount) {
		return this.getWorktickets(enterpriseCode, sdate, edate,
				workticketTypeCode, "7,15,16", permissionDept, repairSpecailCode,
				null, chargeDept, null, "N",null, rowStartIdxAndCount);
	}

	/**
	 * 延期办理列表
	 * 
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param workticketTypeCode
	 * @param permissionDept
	 * @param chargeDept
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject queryWorkticketForDelayList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String permissionDept, String chargeDept,
			final int... rowStartIdxAndCount) {

		// (String enterpriseCode, String sdate,
		// String edate, String workticketTypeCode, String workticketStausId,
		// String permissionDept, String repairSpecailCode,
		// String equAttributeCode, String chargeDept, String fuzzy,String
		// entryIds ,
		// final int... rowStartIdxAndCount)

		return this.getWorktickets(enterpriseCode, sdate, edate,
				workticketTypeCode, "7", permissionDept, null, null,
				chargeDept, null, "N",null, rowStartIdxAndCount);
		// return this.getWorktickets(enterpriseCode, sdate, edate,
		// workticketTypeCode
		// , "7"
		// , permissionDept
		// , null
		// , null
		// , chargeDept, null
		// ,null, rowStartIdxAndCount);
	}

	/**
	 * 安措拆除查询列表 modify by fyyang 090109
	 * 
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param workticketTypeCode
	 * @param workticketStausId
	 *            t.workticket_staus_id = '8'\n" +
	 * @param breakOutStatusId
	 * @param permissionDept
	 * @param sourceName
	 * @param repairSpecailCode
	 * @param equAttributeCode
	 * @param fuzzy
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findSecurityMeasureForBreakOutList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String permissionDept,
			String repairSpecailCode, String equAttributeCode, String fuzzy,
			String strSafetyExeStatusId, final int... rowStartIdxAndCount) {
		// return this.getWorktickets(enterpriseCode,
		// sdate,
		// edate,
		// workticketTypeCode,
		// workticketStausId,
		// permissionDept,
		// repairSpecailCode,
		// equAttributeCode,
		// null, fuzzy, rowStartIdxAndCount);
		// 没有安措记录的工作票不显示
		String sqlWhere = "   and (select count(*) from run_j_workticket_safety s\n"
				+ "where s.workticket_no=t.workticket_no\n"
				+ "and s.is_use='Y')>0 \n";

		if (!strSafetyExeStatusId.equals("")) {
			if (strSafetyExeStatusId.equals("3")) {
				// 未拆除状态3或者null
				sqlWhere = sqlWhere + "  and (save_exe_status_id="
						+ strSafetyExeStatusId
						+ " or  save_exe_status_id is null)";
			} else {
				sqlWhere = sqlWhere + "  and  save_exe_status_id="
						+ strSafetyExeStatusId;
			}
		}
		return this.getWorkticketListByWhere(enterpriseCode, sdate, edate,
				workticketTypeCode, workticketStausId, permissionDept,
				repairSpecailCode, equAttributeCode, null, fuzzy, "N",
				sqlWhere, rowStartIdxAndCount);
	}

	/**
	 * 安措拆除详细列表
	 * 
	 * @param enterpriseCode
	 * @param workticketNO
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WorkticketSafetyBeakOutModel> findSecurityMeasureForBreakOutByNo(
			String enterpriseCode, String workticketNO) {
		try {

			String sql = "select a.id,\n"
					+ "       b.safety_desc,\n"
					+ "       '(' || row_number() over(ORDER BY a.operation_order ASC, b.safety_id ASC) || ') '\n"
					+ "       || a.front_keyword || a.equ_name || '(' || a.attribute_code || ')'\n"
					+ "       || decode(a.markcard_code, NULL, '', '，编号:(' || a.markcard_code || ')')\n"
					+ "       || a.back_keyword || f.flag_name || chr(13) || chr(10) content,\n"
					+ "       decode(safety_execute_code,\n"
					+ "              'NON',\n"
					+ "              '未处理',\n"
					+ "              'REP',\n"
					+ "              '检修自理',\n"
					+ "              'EXE',\n"
					+ "              '已执行',\n"
					+ "              'CLA',\n"
					+ "              '已拆除',\n"
					+ "              '未处理') safety_execute_code,\n"
					+ "       getworkername(a.safety_execute_by) safety_execute_by_name,\n"
					+ "       to_char(safety_execute_date, 'yyyy-MM-dd hh24:mi') safety_execute_date,\n"
					+ "       getworkername(a.safety_breakout_by) safety_breakout_by_name,\n"
					+ "       to_char(safety_breakout_date, 'yyyy-MM-dd hh24:mi') safety_breakout_date,\n"
					+ "       a.safety_execute_code \n"
					+ "  from run_j_workticket_safety a,\n"
					+ "       run_c_worktick_safety   b,\n"
					+ "       run_c_workticket_flag   f\n"
					+ " where a.safety_code = b.safety_code\n"
					+ "   and a.workticket_no = '" + workticketNO + "'\n"
					+ "   and a.flag_id = f.flag_id\n"
					+ "   and a.is_use = b.is_use\n"
					+ "   and b.is_use = f.is_use\n"
					+ "   and a.is_use = 'Y'\n"
					+ "   and a.enterprise_code = b.enterprise_code\n"
					+ "   and b.enterprise_code = f.enterprise_code\n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
					+ " order by a.operation_order asc, b.safety_id asc";

			List<WorkticketSafetyBeakOutModel> list = bll.queryByNativeSQL(sql);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				WorkticketSafetyBeakOutModel omodel = new WorkticketSafetyBeakOutModel();
				Object[] data = (Object[]) it.next();
				omodel.setId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					omodel.setSafetyName(data[1].toString());
				if (data[2] != null)
					omodel.setSafetyContent(data[2].toString());
				if (data[3] != null)
					omodel.setExeDesc(data[3].toString());
				if (data[4] != null)
					omodel.setExeMan(data[4].toString());
				if (data[5] != null)
					omodel.setExeDate(data[5].toString());
				if (data[6] != null)
					omodel.setBreakOutMan(data[6].toString());
				if (data[7] != null)
					omodel.setBeakOutDate(data[7].toString());
				if (data[8] != null)
					omodel.setIsBreakOut(data[8].toString());

				arrlist.add(omodel);
			}
			return arrlist;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 紧急票补签列表
	 * 
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param workticketTypeCode
	 * @param permissionDept
	 * @param chargeDept
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject queryaddForUrgentTicketList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String permissionDept, String chargeDept,
			final int... rowStartIdxAndCount) {
		// return this.getWorktickets(enterpriseCode, sdate, edate,
		// workticketTypeCode,
		// "8",
		// permissionDept,
		// null,
		// null,
		// chargeDept,
		// null,rowStartIdxAndCount);
		// modify by fyyang 090108
		String sqlWhere = "  and  t.is_emergency='Y' ";
		return this.getWorkticketListByWhere(enterpriseCode, sdate, edate,
				workticketTypeCode, "8", permissionDept, null, null,
				chargeDept, null, "N", sqlWhere, rowStartIdxAndCount);
	}

	/**
	 * 工作票交回 指定工作负责人 15----交回 16----恢复 add by LiuYingwen
	 */
	public void workticketDealBack(RunJWorktickethis entity, String chargeDept) {
		// 修改工作票主表的状态
		RunJWorktickets model = this.findById(entity.getWorkticketNo());
		if ("7".equals(entity.getApproveStatus())
				|| "16".equals(entity.getApproveStatus())) {
			// 改变工作票状态
			entity.setApproveStatus("15");
			hisRemote.save(entity);
			model.setWorkticketStausId(15l);
		} else if ("15".equals(entity.getApproveStatus())) {
			entity.setApproveStatus("16");
			hisRemote.save(entity);
			// 修改工作票主表的工作负责人
			model.setWorkticketStausId(16l);
		}
		remote.update(model);
	}

	/**
	 * 变更工作负责人 1----延期 2----变更工作负责人
	 */
	public void changeWorkticketChargeBy(RunJWorktickethis entity,
			String chargeDept) {

		// 增加变更信息
		entity.setChangeStatus("2");
		hisRemote.save(entity);
		// 修改工作票主表的工作负责人
		RunJWorktickets model = new RunJWorktickets();
		model.setWorkticketNo(entity.getWorkticketNo());
		model = remote.findById(model.getWorkticketNo());
		model.setChargeBy(entity.getNewChargeBy());
		model.setChargeDept(chargeDept);
		remote.update(model);
	}

	/**
	 * 取得工作票变更工作负责人名称 modify by fyyang 090108 增加获得变更原因
	 * 
	 * @param workticketNo
	 * @return String[]
	 */
	public String[] getChangeChargeByName(String workticketNo) {
		String[] result = null;
		String sql = "select getworkername(h.old_charge_by),getworkername(h.new_charge_by),h.reason\n"
				+ "  from run_j_worktickethis h\n"
				+ " where h.workticket_no = ?\n"
				+ "   and h.change_status = '2'";
		List list = bll.queryByNativeSQL(sql, new Object[] { workticketNo });
		if (list != null && list.size() > 0) {
			Object[] r = (Object[]) list.get(0);
			result = new String[3];
			result[0] = r[0] == null ? "" : r[0].toString();
			result[1] = r[1] == null ? "" : r[1].toString();
			result[2] = r[2] == null ? "" : r[2].toString();
		}
		return result;
	}

	/**
	 * 工作票延期
	 */

	public void changeWorktickettime(RunJWorktickethis entity) {
		remote = (RunJWorkticketsFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("RunJWorkticketsFacade");
		hisRemote = (RunJWorktickethisFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("RunJWorktickethisFacade");
		/**
		 * 增加变更信息
		 */
		entity.setChangeStatus("1");
		hisRemote.save(entity);

		/**
		 * 修改工作票主表的批准结束时间
		 */

		RunJWorktickets model = new RunJWorktickets();
		model.setWorkticketNo(entity.getWorkticketNo());
		model = remote.findById(model.getWorkticketNo());
		model.setApprovedFinishDate(entity.getNewApprovedFinishDate());
		remote.update(model);
	}

	/**
	 * 取得延期后的信息 add by fyyang 090108
	 * 
	 * @param workticketNo
	 * @return WorkticketHisForPrint
	 */
	@SuppressWarnings("unchecked")
	public WorkticketHisForPrint getWorktickeDelayInfo(String workticketNo) {
		String sql = "select a.workticket_no,\n"
				+ "to_char(a.old_approved_finish_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "to_char(a.new_approved_finish_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "a.reason,\n"
				+ "to_char(a.approve_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "GETWORKERNAME(a.approve_by)\n"
				+ "from run_j_worktickethis a\n" + "where a.workticket_no='"
				+ workticketNo + "'\n" + "and a.change_status='1'";
		List list = bll.queryByNativeSQL(sql);
		if (list != null) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				WorkticketHisForPrint model = new WorkticketHisForPrint();
				if (data[0] != null) {
					model.setWorkticketNo(data[0].toString());
				}
				if (data[1] != null) {
					model.setOldApprovedFinishDate(data[1].toString());
				}
				if (data[2] != null) {
					model.setNewApprovedFinishDate(data[2].toString());
				}
				if (data[3] != null) {
					model.setApproveText(data[3].toString());
				}
				if (data[4] != null) {
					model.setApproveDate(data[4].toString());
				}
				if (data[5] != null) {
					model.setApproveMan(data[5].toString());
				}
				return model;

			}
			return null;
		}
		return null;
	}

	/**
	 * 拆除安措 modify by fyyang 090109
	 */
	@SuppressWarnings("unchecked")
	public void breakOutSecurityMeasure(String workticketNo, HashMap map,
			String reason, String workerCode) {
		// if(map!=null && map.size()>0)
		// {
		// StringBuffer sb = new StringBuffer();
		// sb.append("begin \n");
		// String _templateSql = "update RUN_J_WORKTICKET_SAFETY t\n"
		// + " set t.safety_breakout_by = '" + workerCode
		// + "',\n"
		// + " t.safety_execute_code = '%s',\n"
		// + " t.safety_breakout_date = sysdate\n"
		// + " where t.id = %s;";
		// Iterator it = map.entrySet().iterator();
		// int _totalCount = 0;
		// int _breakOutCount = 0;
		// while (it.hasNext()) {
		// _totalCount ++;
		// Map.Entry entry = (Map.Entry) it.next();
		// Object key = entry.getKey();
		// Object value = entry.getValue();
		//				
		// if(value !=null && "CLA".equalsIgnoreCase(value.toString()))
		// {
		// _breakOutCount ++;
		// }
		// sb.append(String.format(_templateSql,new
		// Object[]{value.toString(),key.toString()}));
		// }
		// //全部拆除 1 , 部分拆除 2 ,未拆除 3
		// int num = 3;
		// if(_breakOutCount!=0 && _totalCount > _breakOutCount)
		// {
		// num = 2;
		// }
		// else if(_totalCount == _breakOutCount)
		// {
		// num =1;
		// }
		// String sql = "update run_j_worktickets t set t.save_exe_status_id =
		// "+num+" where t.workticket_no='"+workticketNo+"';";
		// sb.append(sql);
		// if(num!=1)
		// {
		// sql = "insert into run_j_worktickethis\n" +
		// " (id, workticket_no, reason, approve_by, approve_date,
		// change_status)\n" +
		// "values\n" +
		// " ((select nvl(max(id), 0) + 1 from run_j_worktickethis),\n" +
		// " '%s',\n" +
		// " '%s',\n" +
		// " '%s',\n" +
		// " sysdate,\n" +
		// " '3');";
		// sb.append(String.format(sql, new
		// Object[]{workticketNo,reason,workerCode}));
		// }
		// sb.append("commit;\n");
		// sb.append("end;\n");
		//	        
		//			
		// //System.out.println(sb.toString());
		// bll.exeNativeSQL(sb.toString());
		// }

		if (map != null && map.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("begin \n");
			String _templateSql = "update RUN_J_WORKTICKET_SAFETY t\n"
					+ "           set t.safety_breakout_by   = '" + workerCode
					+ "',\n"
					+ "               t.safety_execute_code  = '%s',\n"
					+ "               t.safety_breakout_date = sysdate\n"
					+ "         where t.id = %s;";
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				sb.append(String.format(_templateSql, new Object[] {
						value.toString(), key.toString() }));
			}
			sb.append("commit;\n");
			sb.append("end;\n");
			bll.exeNativeSQL(sb.toString());
			entityManager.flush();

			StringBuffer sqlsb = new StringBuffer();
			sqlsb.append("begin \n");
			String mainSql = "update run_j_worktickets w\n"
					+ "set w.save_exe_status_id=\n" + "(\n" + "select case\n"
					+ "         when (select count(*)\n"
					+ "                 from run_j_workticket_safety t\n"
					+ "                where t.workticket_no = '"
					+ workticketNo
					+ "'\n"
					+ "                  and t.is_use = 'Y'\n"
					+ "                  and t.safety_execute_code <> 'CLA') = 0 then\n"
					+ "          1\n"
					+ "         else\n"
					+ "          (case\n"
					+ "         when (select count(*)\n"
					+ "                 from run_j_workticket_safety t\n"
					+ "                where t.workticket_no = '"
					+ workticketNo
					+ "'\n"
					+ "                  and t.is_use = 'Y'\n"
					+ "                  and t.safety_execute_code = 'CLA') = 0 then\n"
					+ "          3\n"
					+ "         else\n"
					+ "          2\n"
					+ "       end) end\n"
					+ "  from dual\n"
					+ "  )\n"
					+ "  where w.workticket_no='"
					+ workticketNo
					+ "'\n"
					+ "  and w.is_use='Y';";
			sqlsb.append(mainSql);
			String sql = "insert into run_j_worktickethis\n"
					+ "  (id, workticket_no, reason, approve_by, approve_date, change_status)\n"
					+ "  select (select nvl(max(id), 0) + 1 from run_j_worktickethis),\n"
					+ "   '"
					+ workticketNo
					+ "',\n"
					+ "   '"
					+ reason
					+ "',\n"
					+ "   '"
					+ workerCode
					+ "',\n"
					+ "   sysdate,\n"
					+ "   '3'\n"
					+ "   from dual\n"
					+ "where 1<>(\n"
					+ "select case\n"
					+ "         when (select count(*)\n"
					+ "                 from run_j_workticket_safety t\n"
					+ "                where t.workticket_no = '"
					+ workticketNo
					+ "'\n"
					+ "                  and t.is_use = 'Y'\n"
					+ "                  and t.safety_execute_code <> 'CLA') = 0 then\n"
					+ "          1\n"
					+ "         else\n"
					+ "          (case\n"
					+ "         when (select count(*)\n"
					+ "                 from run_j_workticket_safety t\n"
					+ "                where t.workticket_no = '"
					+ workticketNo
					+ "'\n"
					+ "                  and t.is_use = 'Y'\n"
					+ "                  and t.safety_execute_code = 'CLA') = 0 then\n"
					+ "          3\n"
					+ "         else\n"
					+ "          2\n"
					+ "       end) end\n" + "  from dual\n" + "  );";

			sqlsb.append(sql);
			sqlsb.append("commit;\n");
			sqlsb.append("end;\n");
			bll.exeNativeSQL(sqlsb.toString());
		}
	}

	/**
	 * 安措未拆除原因列表 modify by fyyang 090109
	 * 
	 * @param workticketNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RunJWorktickethis> findWorkticketHisList(String workticketNo) {
		List hisList = new ArrayList();
		String sql = "select h.id,h.workticket_no,h.reason,\n"
				+ "nvl(GETWORKERNAME(h.approve_by),h.approve_by) approve_by,\n"
				+ "to_char(h.approve_date,'yyyy-mm-dd hh24:mi:ss')  approve_date\n"
				+ "from run_j_worktickethis h\n" + "where h.workticket_no = '"
				+ workticketNo + "'\n" + "and h.change_status = '3'";
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			RunJWorktickethis model = new RunJWorktickethis();
			if (data[1] != null) {
				model.setWorkticketNo(data[1].toString());
			}
			if (data[2] != null) {
				model.setReason(data[2].toString());
			}
			if (data[3] != null) {
				model.setApproveBy(data[3].toString());
			}
			if (data[4] != null) {
				try {
					model.setApproveDate(df.parse(data[4].toString()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			hisList.add(model);
		}
		return hisList;

	}

	/**
	 * 紧急工作票补签
	 * 
	 * @param workticketNo
	 * @param approveBy
	 * @param approveDate
	 * @param approveStatus
	 * @param approveDept
	 * @param fireticketFireman
	 */
	public void addForEmergencyTicket(String workticketNo, String approveBy,
			Date approveDate, String approveStatus, String approveDept,
			String... fireticketFireman) {
		
		RunJWorktickethis his = new RunJWorktickethis();
		RunJWorktickets worktickets = new RunJWorktickets();
		// 增加记录（工作票审批及变更表）
		his.setWorkticketNo(workticketNo);
		his.setApproveBy(approveBy);
		his.setApproveDate(approveDate);
		his.setApproveStatus(approveStatus);
		hisRemote.save(his);

		worktickets = remote.findById(his.getWorkticketNo());
		String no = worktickets.getWorkticketTypeCode();
			//workticketNo.substring(1, 2);
		if (no.equals("4")) {
			// 修改记录（工作票主表）
			// 签发人
			// modify by fyyang 090213 签发等字段已经被删掉
			// worktickets.setReceiveBy(approveBy);
			// worktickets.setReceiveDept(approveDept);
			// worktickets.setReceiveDate(approveDate);
			// 动火票消防监护人
			if (fireticketFireman != null) {
				worktickets.setFireticketFireman(fireticketFireman[0]);
			}
			// worktickets.setReceiveDate(approveDate);
			remote.update(worktickets);
		} else {
			// 修改记录（工作票主表）
			// worktickets.setReceiveBy(approveBy);
			// worktickets.setReceiveDept(approveDept);
			// worktickets.setReceiveDate(approveDate);
			remote.update(worktickets);
		}
	}
	public void createnewDeloldWorkTickect(RunJWorktickets entity,String oldWorktickectNo){
		entity.setWorkticketStausId(1l);
		entityManager.persist(entity);
		this.delete(oldWorktickectNo);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strdate=df.format(entity.getEntryDate());
		String safeSql=
			"update run_j_workticket_safety a\n" +
			"   set a.workticket_no        = '"+entity.getWorkticketNo()+"',\n" + 
			"       a.namecard_print_flag  = null,\n" + 
			"       a.safety_execute_code  = null,\n" + 
			"       a.safety_execute_by    = null,\n" + 
			"       a.safety_execute_date  = null,\n" + 
			"       a.safety_breakout_by   = null,\n" + 
			"       a.safety_breakout_date = null,\n" + 
			"       a.create_by            = '"+entity.getEntryBy()+"',\n" + 
			"       a.create_date          = to_date('"+strdate+"','yyyy-mm-dd hh24:mi:ss')\n" + 
			" where a.workticket_no = '"+oldWorktickectNo+"'  and a.is_use='Y';";
//		String conSql=
//			"update run_j_workticket_content set workticket_no='"+entity.getWorkticketNo()+"' where workticket_no='"+oldWorktickectNo+"' and is_use='Y';";
		String denSql=
			"update run_j_workticket_danger set workticket_no='"+entity.getWorkticketNo()+"' where workticket_no='"+oldWorktickectNo+"' and is_use='Y';";
		StringBuffer sqls = new StringBuffer();
		sqls.append("begin\n");
		sqls.append(safeSql);
//		sqls.append(conSql);
		sqls.append(denSql);
		sqls.append("commit;\nend;");
		bll.exeNativeSQL(sqls.toString());
	}
	
	/**
	 * add by liuyi 091116
	 * 生成工作票号
	 * 
	 * @param enterpriseCode
	 *            工作票类型
	 * @param enterpriseChar
	 *            企业一位标识
	 * @param workticketType
	 *            工作票类型
	 * @param specialCode
	 *            专业编码
	 * @return
	 */
	public String createWorkticketNo(String enterpriseCode,
			String enterpriseChar, String workticketType, String specialCode) {
		String mymonth = "";
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyyy-MM-dd" + " " + "hh:mm:ss");
		mymonth = tempDate.format(new java.util.Date());
		mymonth = mymonth.substring(0, 4) + mymonth.substring(5, 7);
		String no = enterpriseChar + workticketType + mymonth; 
		String sql = "select '"
				+ no
				+ "' ||(select nvl((select t.speciality_char\n"
				+ "                                   from run_c_specials t\n"
				+ "                                  where t.speciality_code = '"
				+ specialCode
				+ "'  and t.enterprise_code='"
				+ enterpriseCode
				+ "' and t.is_use='Y' ),\n"
				+ "                                 substr('"
				+ specialCode
				+ "', 0, 1))\n"
				+ "                        from dual)||\n"
				+ "       (select Trim(case\n"
				+ "                 when max(w.workticket_no) is null then\n"
				+ "                  '001'\n"
				+ "                 else\n"
				+ "                  to_char(to_number(substr(max(w.workticket_no), 10, 3) + 1),\n"
				+ "                          '000')\n"
				+ "               end)\n"
				+ "\n"
				+ "          from run_j_worktickets w\n"
				+ "         where w.workticket_no like Trim('"
				+ no
				+ "'||(select nvl((select t.speciality_char\n"
				+ "                                   from run_c_specials t\n"
				+ "                                  where t.speciality_code = '"
				+ specialCode + "'  and t.enterprise_code='" + enterpriseCode
				+ "' and t.is_use='Y' ),\n"
				+ "                                 substr('" + specialCode
				+ "', 0, 1))\n" + "                        from dual) ||'%')\n"
				+ "                        )\n" + "\n" + "  from dual";

		no = bll.getSingal(sql).toString().trim();
		return no;
	} 
	
	 /** add by liuyi 091116
     * 根据工作票号和企业编码查询一条工作票记录
     * 
     * @param enterpriseCode
     * @param workticketNo
     * @return
     */
	public List<WorkticketInfo> getWorkticketByWorkticketNo(String workticketNo,String enterpriseCode){
		String sql = 
			"select t.workticket_no,\n" +
			"       t.apply_no,\n" + 
			"       t.wo_code,\n" + 
			"       t.charge_by,\n" + 
			"       t.charge_dept,\n" + 
			"       t.workticket_type_code,\n" + 
			"       t.source_id,\n" + 
			"       t.member_count,\n" + 
			"       t.members,\n" + 
			"       t.equ_attribute_code,\n" + 
			"       t.condition_name,\n" + 
			"       t.repair_specail_code,\n" + 
			"       t.permission_dept,\n" + 
			"       t.failure_code,\n" + 
			"       t.location_name,\n" + 
			"       t.workticket_content,\n" + 
			"       t.workticket_memo,\n" + 
			"       getworkername(t.charge_by) chargebyname,\n" + 
			"       getdeptname(t.charge_dept) deptname,\n" + 
			"       getwttypeame(t.workticket_type_code) workticketTypeName,\n" + 
			"       getwtsourcename(t.source_id) sourcename,\n" + 
			"       getblockbycode(t.equ_attribute_code) blockname,\n" + 
			"       to_char(t.plan_start_date,'yyyy-MM-dd hh24:mi:ss') planstartdate,\n" + 
			"       to_char(t.plan_end_date,'yyyy-MM-dd hh24:mi:ss') planenddate,\n" + 
			"       getspecialname(t.repair_specail_code) repairSpecail,\n"+
			"       (select r.speciality_name from run_c_unitprofession r where r.speciality_code = t.permission_dept and r.is_use='Y') receiveSpecail"+
			"  from run_j_worktickets t\n" + 
			" where t.workticket_no ='"+workticketNo+"'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'";
		List list = bll.queryByNativeSQL(sql);
		List arraylist = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			Object[] data = (Object[]) it.next();
			WorkticketInfo model = new WorkticketInfo();
			RunJWorktickets basemodel = new RunJWorktickets();
			if(data[0] != null)
				basemodel.setWorkticketNo(data[0].toString());
			if(data[1] != null)
				basemodel.setApplyNo(data[1].toString());
			if(data[2] != null)
				basemodel.setWoCode(data[2].toString());
			if(data[3] != null)
				basemodel.setChargeBy(data[3].toString());
			if(data[4] != null)
				basemodel.setChargeDept(data[4].toString());
			if(data[5] != null)
				basemodel.setWorkticketTypeCode(data[5].toString());
			if(data[6] != null)
				basemodel.setSourceId(Long.parseLong(data[6].toString()));
			if(data[7] != null)
				basemodel.setMemberCount(Long.parseLong(data[7].toString()));
			if(data[8] != null)
				basemodel.setMembers(data[8].toString());
			if(data[9] != null)
				basemodel.setEquAttributeCode(data[9].toString());
			if(data[10] != null)
				basemodel.setConditionName(data[10].toString());
			if(data[11] != null)
				basemodel.setRepairSpecailCode(data[11].toString());
			if(data[12] != null)
				basemodel.setPermissionDept(data[12].toString());
			if(data[13] != null)
				basemodel.setFailureCode(data[13].toString());
			if(data[14] != null)
				basemodel.setLocationName(data[14].toString());
			if(data[15] != null)
				basemodel.setWorkticketContent(data[15].toString());
			if(data[16] != null)
				basemodel.setWorkticketMemo(data[16].toString());
			if(data[17] != null)
				model.setChargeByName(data[17].toString());
			if(data[18] != null)
				model.setDeptName(data[18].toString());
			if(data[19] != null)
				model.setWorkticketTypeName(data[19].toString());
			if(data[20] != null)
				model.setSourceName(data[20].toString());
			if(data[21] != null)
				model.setBlockName(data[21].toString());
			if(data[22] != null)
				model.setPlanStartDate(data[22].toString());
			if(data[23] != null)
				model.setPlanEndDate(data[23].toString());
			if(data[24] != null)
				model.setRepairSpecail(data[24].toString());
			if(data[25] != null)
				model.setReceiveSpecail(data[25].toString());
			model.setModel(basemodel);
			arraylist.add(model);
		}
		return arraylist;
	}
}