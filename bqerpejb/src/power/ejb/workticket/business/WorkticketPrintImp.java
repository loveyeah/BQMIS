package power.ejb.workticket.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.workticket.form.DhMeasureInfoForPrint;
import power.ejb.workticket.form.SecurityMeasureCardModel;
import power.ejb.workticket.form.SecurityMeasureForPrint;
import power.ejb.workticket.form.WorkticketBaseForPrint;
import power.ejb.workticket.form.WorkticketCountForm;
import power.ejb.workticket.form.WorkticketDangerPointForPrintHF;
import power.ejb.workticket.form.WorkticketHisForPrint;
import power.ejb.workticket.form.WorkticketPrintModel;

@Stateless
public class WorkticketPrintImp implements WorkticketPrint {
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	protected RunJWorkticketMapFacadeRemote mapRemote;
	protected RunJWorkticketContentFacadeRemote contentRemote;

	public WorkticketPrintImp() {
		mapRemote = (RunJWorkticketMapFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("RunJWorkticketMapFacade");
		contentRemote=(RunJWorkticketContentFacadeRemote)Ejb3Factory.getInstance()
		.getFacadeRemote("RunJWorkticketContentFacade");
	}

	public WorkticketPrintModel getWorkticketPrintInfo(String workticketNo) {
		WorkticketPrintModel model = new WorkticketPrintModel();
		model.setBase(this.getWorkticketBaseInfo(workticketNo));
		model.setHistory(this.findApproveHisList(workticketNo,model.getBase().getWorkticketTypeCode()));
		model.setSafety(this.findSecurityList(workticketNo,model.getBase().getWorkticketTypeCode()));
		model
				.setDangerHF(this
						.getWorkticketDangerPointForPrintHF(workticketNo));
		model.setMeasure(this.getMeasureInfo(workticketNo));
		return model;
	}

	@SuppressWarnings("unchecked")
	private WorkticketBaseForPrint getWorkticketBaseInfo(String workticketNo) {

		String sql = "select t.workticket_no,\n"
				+ "       nvl(GETWORKERNAME(t.charge_by),t.charge_by) chargeby,\n"
				+ "       nvl(GETDEPTNAME(t.charge_dept),t.charge_dept) chargedept,\n"
				+ "       t.member_count,\n"
				+ "       t.members,\n"
				+ "       nvl(GETWTSOURCENAME(t.source_id),t.source_id) sourcename,\n"
				+ "       t.condition_name,\n"
				+ "       nvl(GETWTFIRELEVELNAME(t.firelevel_id),t.firelevel_id) firelevelname,\n"
				+ "       t.workticket_content,\n"
				+ "       nvl(GETSPECIALNAME(t.repair_specail_code),t.repair_specail_code) resepcialname,\n"
				+ "       nvl(GETBlockBYCODE(t.equ_attribute_code),t.equ_attribute_code) equname,\n"
				+ "       nvl(GETWORKERNAME(t.watcher),t.watcher) watchername,\n"
				+ "        t.is_emergency  emergency,\n"
				+ "       to_char(t.plan_start_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "        to_char(t.plan_end_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "        nvl(GETWORKERNAME(t.entry_by),t.entry_by) enterby,\n"
				+ "        to_char(t.entry_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "        to_char(t.actual_start_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "        to_char(t.actual_end_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "        to_char(t.approved_finish_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "        t.ref_workticket_no,\n"
				+ "        nvl(GETWORKERNAME(t.fireticket_execute_by),t.fireticket_execute_by) fireexeman,\n"
				+ "        nvl(GETWORKERNAME(t.fireticket_fireman),t.fireticket_fireman) fireman,\n"
				+ "        t.workticket_memo,\n"
				+ "        t.danger_type,\n"
				+ "        t.danger_condition,\n"
				+ "        GETWTTYPEAME(t.workticket_type_code),\n"
				+ "        t.workticket_type_code, \n"
				+
				// add 取得总挂牌数
				"(select count(*) from run_j_workticket_safety t\n"
				+ " where t.workticket_no='"
				+ workticketNo
				+ "'\n"
				+ " and t.markcard_code is not null\n"
				+ " and t.markcard_code<>' '\n"
				+ " and t.is_use='Y') cardnum, \n"
				+
				// add end
				"        t.location_name,\n" + "        t.auto_device_name\n"
				+ "        from run_j_worktickets t\n"
				+ "        where t.workticket_no='" + workticketNo + "'\n"
				+ "        and t.is_use='Y'";
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		WorkticketBaseForPrint base = new WorkticketBaseForPrint();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");// modify By ywliu 09/05/18
		while (it.hasNext()) {

			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				base.setWorkticketNo(data[0].toString());
			}
			if (data[1] != null) {
				base.setChargeBy(data[1].toString());
			}
			if (data[2] != null) {
				base.setChargeDept(data[2].toString());
			}
			if (data[3] != null) {
				base.setMemberCount(data[3].toString());
			}
			if (data[4] != null) {
				base.setMembers(data[4].toString());
			}
			if (data[5] != null) {
				base.setSourceName(data[5].toString());
			}
			if (data[6] != null) {
				base.setWorkCondition(data[6].toString());
			}
			if (data[7] != null) {
				base.setFireLevel(data[7].toString());
			}
			if (data[8] != null) {
				base.setWorkticketContent(data[8].toString());
			}
			if (data[9] != null) {
				base.setRepairSpecail(data[9].toString());
			}
			if (data[10] != null) {
				base.setEquAttributeName(data[10].toString());
			}
			if (data[11] != null) {
				base.setWatcher(data[11].toString());
			}
			if (data[12] != null) {
				if (data[12].toString().equals("Y")) {
					base.setIsEmergency("紧急");
				} else {
					base.setIsEmergency("普通");
				}
			}
			if (data[13] != null) {
				base.setPlanStartDate(data[13].toString());

				try {
					Date planSDate = df.parse(base.getPlanStartDate());
					base.setPlanStartDate(sf.format(planSDate));
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
			if (data[14] != null) {
				base.setPlanEndDate(data[14].toString());
				try {

					base.setPlanEndDate(sf.format(df.parse(base
							.getPlanEndDate())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (data[15] != null) {
				base.setEntryBy(data[15].toString());
			}
			if (data[16] != null) {
				base.setEntryDate(data[16].toString());
				try {

					base.setEntryDate(sf.format(df.parse(base.getEntryDate())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (data[17] != null) {
				base.setActualStartDate(data[17].toString());
				try {

					base.setActualStartDate(sf.format(df.parse(base
							.getActualStartDate())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (data[18] != null) {
				base.setActualEndDate(data[18].toString());
				try {

					base.setActualEndDate(sf.format(df.parse(base
							.getActualEndDate())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (data[19] != null) {
				base.setApprovedFinishDate(data[19].toString());
				try {

					base.setApprovedFinishDate(sf.format(df.parse(base
							.getApprovedFinishDate())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (data[20] != null) {
				base.setRefMainticketNo(data[20].toString());
			}
			if (data[21] != null) {
				base.setFireExeMan(data[21].toString());
			}
			if (data[22] != null) {
				base.setFireWatcherMan(data[22].toString());
			}
			if (data[23] != null) {
				base.setWorkticketMemo(data[23].toString());
			}
			if (data[24] != null) {
				base.setDangerType(data[24].toString());
			}
			if (data[25] != null) {
				base.setDangerContent(data[25].toString());
			}
			if (data[26] != null) {
				base.setWorkticketType(data[26].toString());
			}
			if (data[27] != null) {
				base.setWorkticketTypeCode(data[27].toString());
				if (data[27].toString().equals("1")) {
					RunJWorkticketMap mapmodel = mapRemote
							.findByWorkticketNo(workticketNo);
					if (mapmodel != null) {
						if (mapmodel.getWorkticketMap() != null) {
							base.setWorkticketMap(mapmodel.getWorkticketMap());
						}
					}
				}
			}
			if (data[28] != null) {
				base.setMarkCardNum(data[28].toString());
			}
			if (data[29] != null) {
				base.setLocationName(data[29].toString());
			}
			if (data[30] != null) {
				base.setAutoDeviceName(data[30].toString());
			}

		}

		//add by fyyang 090311
//		base.setWorkticketContent(contentRemote.getWorkticketContent(workticketNo));
		base.setEquAttributeName(this.getEquName(workticketNo));
		return base;
	}

	//modify by fyyang 090317
	@SuppressWarnings("unchecked")
	public List<WorkticketHisForPrint> findApproveHisList(String workticketNo,String workticketTypeCode) {
		List hisList = new ArrayList();
		// start by fangjihu
		/*
		 * String sql= "select t.id,
		 * t.workticket_no,nvl(GETWORKERNAME(t.approve_by),t.approve_by)
		 * approveman,t.approve_date,\n" + " t.approve_text,\n" + " (case when
		 * t.change_status is null then t.approve_status else\n" + " ( case
		 * t.change_status when '1' then 'delay' when '2' then 'changecharge'
		 * else '' end )\n" + " end ) status,\n" + " (case when t.change_status
		 * is null then
		 * nvl(GETWTSTATUSNAME(t.approve_status),t.approve_status)\n" + "
		 * else\n" + " ( case t.change_status when '1' then '延期' when '2' then
		 * '变更工作负责人' else '' end )\n" + " end) statusname,\n" + "
		 * nvl(GETWORKERNAME(t.old_charge_by),t.old_charge_by),\n" + "
		 * nvl(GETWORKERNAME(t.new_charge_by),t.new_charge_by),\n" + "
		 * t.old_approved_finish_date,\n" + " t.new_approved_finish_date\n" + "
		 * from run_j_worktickethis t\n" + " where
		 * t.workticket_no='"+workticketNo+"'\n" + " and t.approve_date >=(
		 * select m.approve_date from run_j_worktickethis m\n" + " where m.id in
		 * (select max(n.id) from\n" + " run_j_worktickethis n where
		 * n.approve_status='3' and n.workticket_no='"+workticketNo+"' ) )\n" +
		 * "\n" + " order by id";
		 */

		String sql = "select t.id, t.workticket_no,nvl(GETWORKERNAME(t.approve_by),t.approve_by) approveman,to_char(t.approve_date,'yyyy-mm-dd hh24:mi:ss')approve_date,\n"
				+ "            t.approve_text,\n"
				+ "            (case when t.change_status is null then t.approve_status else\n"
				//modify 090223
				+ "            ( case t.change_status when '1' then 'delay' when '2' then 'changecharge'  when '4' then 'permit'  when '5' then 'return' when '6' then 'back' when '7' then 'safetyexe'  else '' end  )\n"
				+ "             end ) status,\n"
				+ "            (case when t.change_status is null then  nvl(GETWTSTATUSNAME(t.approve_status),t.approve_status)\n"
				+ "              else\n"
				+ "               ( case t.change_status when '1' then '延期' when '2' then '变更工作负责人' when '4' then '许可工作回填'  when '5' then '交回' when '6'  then '恢复' when '7' then '安措办理回填'  else '' end  )\n"
				+ "               end)    statusname,\n"
				+ "            nvl(GETWORKERNAME(t.old_charge_by),t.old_charge_by),\n"
				+ "            nvl(GETWORKERNAME(t.new_charge_by),t.new_charge_by),\n"
				+ "           to_char(t.old_approved_finish_date,'yyyy-mm-dd hh24:mi:ss') old_approved_finish_date,\n"
				+ "           to_char(t.new_approved_finish_date,'yyyy-mm-dd hh24:mi:ss') new_approved_finish_date,\n"
            //add by fyyang  090223
				+"nvl(GETWORKERNAME(t.fire_by),t.fire_by),\n" +
				"         nvl(GETWORKERNAME(t.duty_charge_by),t.duty_charge_by),\n" + 
				"         t.total_line,\n" + 
				"         t.nobackout_line,\n" + 
				"         t.nobackout_num \n"
           //----------------------------------------
				+ "     from run_j_worktickethis t\n"
				+ "     where  t.workticket_no='"
				+ workticketNo
				+ "'\n"
				+ "     and t.approve_date >=(  select m.approve_date   from run_j_worktickethis m\n"
				+ "                            where m.id in (select max(n.id)  from\n"
				+ "                            run_j_worktickethis n  \n"
				+
				// " where n.approve_status='3' \n" +
				// modify by fyyang 090108
				"where n.approve_status=decode((select w.is_emergency from run_j_worktickets w where w.workticket_no='"
				+ workticketNo
				+ "'),'N','3', decode('"+workticketTypeCode+"','4','7','4'))"
				+ "   and n.workticket_no='"
				+ workticketNo + "' ) )\n" + "\n" + "     order by id";

		// end by fangjihu
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		while (it.hasNext()) {
			WorkticketHisForPrint model = new WorkticketHisForPrint();
			Object[] data = (Object[]) it.next();
			if (data[1] != null) {
				model.setWorkticketNo(data[1].toString());
			}
			if (data[2] != null) {
				model.setApproveMan(data[2].toString());
			}
			if (data[3] != null) {
				// model.setApproveDate(data[3].toString());
				try {
					model.setApproveDate(sf
							.format(df.parse(data[3].toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
			if (data[4] != null) {
				model.setApproveText(data[4].toString());
			}
			if (data[5] != null) {
				model.setApproveStatusId(data[5].toString());
			}
			if (data[6] != null) {
				model.setApproveStatusName(data[6].toString());
			}
			if (data[7] != null) {
				model.setOldChargeBy(data[7].toString());
			}
			if (data[8] != null) {
				model.setNewChargeBy(data[8].toString());
			}
			if (data[9] != null) {
				try {
					model.setOldApprovedFinishDate(sf.format(df.parse(data[9]
							.toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// model.setOldApprovedFinishDate(data[9].toString());
			}
			if (data[10] != null) {
				// model.setNewApprovedFinishDate(data[10].toString());
				try {
					model.setNewApprovedFinishDate(sf.format(df.parse(data[10]
							.toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(data[11]!=null)
			{
				model.setFireBy(data[11].toString());
			}
			if(data[12]!=null)
			{
				model.setDutyChargeBy(data[12].toString());
			}
			if(data[13]!=null)
			{
				model.setTotalLine(data[13].toString());
			}
			if(data[14]!=null)
			{
				model.setNobackoutLine(data[14].toString());
			}
			if(data[15]!=null)
			{
				model.setNobackoutNum(data[15].toString());
			}
			if(model.getTotalLine()!=null&&model.getNobackoutLine()!=null)
			{
				Long line=(Long.parseLong(model.getTotalLine())-Long.parseLong(model.getNobackoutLine()));
				model.setBackoutLine(line.toString());
			}
			hisList.add(model);
		}
		return hisList;
	}

	@SuppressWarnings("unchecked")
	private List<SecurityMeasureForPrint> findSecurityList(String workticketNo,String workticketTypeCode) {
		List safetyList = new ArrayList();
//		String sql = "select t.id,t.workticket_no,t.safety_code,GETWTSAFETYDESC(t.safety_code),t.safety_content,t.safety_exe_content,\n"
//				+ "SPELLWTMARKCARD(t.workticket_no,t.safety_code),GETWTMARKCARDCOUNT(t.workticket_no,t.safety_code)\n"
//				+ " from RUN_J_WORKTICK_SFATY_CONTENT t\n"
//				+ " where t.workticket_no='"
//				+ workticketNo
//				+ "'\n"
//				+ " and t.is_use='Y'";
		String sql=
			"select a.id,\n" +
			"       a.workticket_no,\n" + 
			"       a.safety_code,\n" + 
			"       GETWTSAFETYDESC(a.safety_code),\n" + 
			"       a.front_keyword || a.equ_name || " +
			"decode(a.attribute_code,'temp','','(' ||\n" + 
			"       a.attribute_code || ')' )|| a.back_keyword ||\n" + 
			"       a.flag_desc safety_content,\n" + 
			"       decode('"+workticketTypeCode+"',\n" + 
			"              '1',\n" + 
			"              decode(a.safety_execute_code, null, '', 'NON', '', '已'||\n" + 
			"              a.front_keyword || a.equ_name || " +
			"  decode(a.attribute_code,'temp','','(' ||\n" + 
			"              a.attribute_code || ')' )|| a.back_keyword ||\n" + 
			"              a.flag_desc),\n" + 
			"              '2',\n" + 
			"              decode(a.safety_execute_code, null, '', 'NON', '', '已' ||\n" + 
			"              a.front_keyword || a.equ_name || " +
			"decode(a.attribute_code,'temp','','(' ||\n" + 
			"              a.attribute_code || ')') || a.back_keyword ||\n" + 
			"              a.flag_desc),\n" + 
			"              decode(a.safety_execute_code, null, '', 'NON', '', 'REP','检修自理','√')) safety_exe_content,\n" + 
			"       SPELLWTMARKCARD(a.workticket_no, a.safety_code),\n" + 
			"       GETWTMARKCARDCOUNT(a.workticket_no, a.safety_code),\n" + 
			"       nvl(GETWORKERNAME(a.safety_execute_by), a.safety_execute_by)\n" + 
			"  FROM run_j_workticket_safety a\n" + 
			" WHERE \n" + 
			"    a.is_use='Y' \n"+
			"   and a.workticket_no = '"+workticketNo+"'" +
			"order by a.safety_code , a.operation_order ASC";

		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			SecurityMeasureForPrint model = new SecurityMeasureForPrint();
			if (data[1] != null) {
				model.setWorkticketNo(data[1].toString());
			}
			if (data[2] != null) {
				model.setSafetyCode(data[2].toString());
			}
			if (data[3] != null) {
				model.setSafetyDesc(data[3].toString());
			}
			if (data[4] != null) {
				model.setSafetyContent(data[4].toString());
			}
			if (data[5] != null) {
				model.setSafetyExeContent(data[5].toString());
			}
			if (data[6] != null) {
				model.setMarkCard(data[6].toString());
			}
			if (data[7] != null) {
				model.setCardCount(data[7].toString());
			}
			if(data[8]!=null)
			{
				model.setSafetyExeBy(data[8].toString());
			}
			safetyList.add(model);
		}

		return safetyList;
	}

	public SecurityMeasureCardModel getSecurityMeasureCardInfo(
			String workticketNo) {
		SecurityMeasureCardModel model = new SecurityMeasureCardModel();
		model.setBase(this.getWorkticketBaseInfo(workticketNo));
		model.setSafety(this.findSecurityList(workticketNo,model.getBase().getWorkticketTypeCode()));
		return model;
	}

	@SuppressWarnings("unchecked")
	private WorkticketDangerPointForPrintHF getWorkticketDangerPointForPrintHF(
			String workticketNo) {
		String strsql = "select ";

		for (int i = 1; i < 12; i++) {
			strsql += "(SELECT COUNT(1)\n"
					+ "          FROM run_j_workticket_fire_content t\n"
					+ "         WHERE t.firecontent_id = " + i + "\n"
					+ "           AND t.workticket_no = '" + workticketNo
					+ "') Count" + i + ",";
		}
		strsql += "(SELECT COUNT(1)\n"
				+ "          FROM run_j_workticket_fire_content t\n"
				+ "         WHERE t.firecontent_id = 12\n"
				+ "           AND t.workticket_no = '" + workticketNo
				+ "') Count12";

		strsql += " from dual";

		List list = bll.queryByNativeSQL(strsql);
		Iterator it = list.iterator();
		WorkticketDangerPointForPrintHF model = new WorkticketDangerPointForPrintHF();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			if (data[0].toString().equals("1")) {
				model.setDangerType01("√");
			}
			if (data[1].toString().equals("1")) {
				model.setDangerType02("√");
			}
			if (data[2].toString().equals("1")) {
				model.setDangerType03("√");
			}
			if (data[3].toString().equals("1")) {
				model.setDangerType04("√");
			}
			if (data[4].toString().equals("1")) {
				model.setDangerType05("√");
			}
			if (data[5].toString().equals("1")) {
				model.setDangerType06("√");
			}
			if (data[6].toString().equals("1")) {
				model.setDangerType07("√");
			}
			if (data[7].toString().equals("1")) {
				model.setDangerType08("√");
			}
			if (data[8].toString().equals("1")) {
				model.setDangerType09("√");
			}
			if (data[9].toString().equals("1")) {
				model.setDangerType10("√");
			}
			if (data[10].toString().equals("1")) {
				model.setDangerType11("√");
			}
			if (data[11].toString().equals("1")) {
				model.setDangerType12("√");
			}
		}

		return model;
	}
	
	/**
	 * 一级动火票测量信息
	 * @param workticketNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DhMeasureInfoForPrint getMeasureInfo(String workticketNo)
	{
		DhMeasureInfoForPrint measureModel=new DhMeasureInfoForPrint();
		String sql="select t.measure_data_id,\n" +
		"       t.workticket_no,\n" + 
		"       t.measure_data,\n" + 
		"       nvl(GETWORKERNAME(t.measure_man),t.measure_man) measure_man,\n" + 
		"       t.measure_date,\n" + 
		"       t.enterprise_code,\n" + 
		"       t.is_use\n" + 
		"  from RUN_J_WORKTICKET_MEASURE_DATA t\n" + 
		" where t.workticket_no = '"+workticketNo+"'\n" + 
		" and t.is_use='Y'";
		List<RunJWorkticketMeasureData> list=bll.queryByNativeSQL(sql, RunJWorkticketMeasureData.class);
		measureModel.setMesureData(list);
		
		String sqlInfo=
			"select * from RUN_J_WORKTICKET_MEASURE t\n" +
			"where t.workticket_no='"+workticketNo+"'";
		List<RunJWorkticketMeasure> infoList=bll.queryByNativeSQL(sqlInfo, RunJWorkticketMeasure.class);
		if(infoList!=null&&infoList.size()>0)
		{
			RunJWorkticketMeasure model=infoList.get(0);
			measureModel.setCombustibleGas(model.getCombustibleGas());
			measureModel.setMeasureLocation(model.getMeasureLocation());
			measureModel.setUseTool(model.getUseTool());
		}
		return measureModel;
		
	}
	
	@SuppressWarnings("unchecked")
	public String getEquName(String workticketNo)
	{
//		String sql=
//			"select t.id,t.equ_name\n" +
//			"from run_j_workticket_content t\n" + 
//			"where t.workticket_no='"+workticketNo+"'\n" + 
//			"and t.is_use='Y'";
//动火票设备从主表主设备上取     modify lwqi 090523
		
		String sql=
			"select t.main_equ_name\n" +
			"from run_j_worktickets t\n" + 
			"where t.workticket_no='"+workticketNo+"'\n" + 
			"and t.is_use='Y'";
//      List list=bll.queryByNativeSQL(sql);
//      Iterator it = list.iterator();
      String equName= "";
      if(bll.getSingal(sql) != null)
      {
      equName += bll.getSingal(sql).toString();
      }
//      while(it.hasNext())
//      {
//    	  Object[] data = (Object[]) it.next();
//    	  if(data[1]!=null)
//    	  {
//    		  if(equName.equals(""))
//    		  {
//    			  equName=data[1].toString();
//    		  }
//    		  else
//    		  {
//    			  equName=equName+","+data[1].toString();
//    		  }
//    	  }
//      }
      
		return equName;
	}
	/**
	 * 取得工作票部门合格率统计
	 */
	public List<WorkticketCountForm> getRateDataOfDept(String stop,String enterpriseCode,
			String date, String depts) {
		String sql=
			"select t.workticket_type_code,\n" +
			"       (select r.workticket_type_name\n" + 
			"          from run_c_workticket_type r\n" + 
			"         where r.workticket_type_code = t.workticket_type_code) workticket_type_name,\n" + 
			"       (select min(substr(t1.workticket_no, 8, 7))\n" + 
			"          from run_j_worktickets t1\n" + 
			"         where t1.is_standard = 'N'\n" + 
			"           and t1.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t1.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t1.is_use = 'Y'\n" + 
			"           and t1.workticket_type_code = t.workticket_type_code\n" + 
			"           and t1.enterprise_code = '"+enterpriseCode+"') startNo,\n" + 
			"       (select max(substr(t1.workticket_no, 8, 7))\n" + 
			"          from run_j_worktickets t1\n" + 
			"         where t1.is_standard = 'N'\n" + 
			"           and t1.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t1.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t1.is_use = 'Y'\n" + 
			"           and t1.workticket_type_code = t.workticket_type_code\n" + 
			"           and t1.enterprise_code = '"+enterpriseCode+"') endNo,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t1\n" + 
			"         where t1.is_standard = 'N'\n" + 
			"           and t1.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t1.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t1.is_use = 'Y'\n" + 
			"           and t1.workticket_type_code = t.workticket_type_code\n" + 
			"           and t1.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and t1.workticket_staus_id in (8, 14)) count1,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t2\n" + 
			"         where t2.is_standard = 'N'\n" + 
			"           and t2.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t2.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t2.is_use = 'Y'\n" + 
			"           and t2.workticket_type_code = t.workticket_type_code\n" + 
			"           and t2.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and t2.workticket_staus_id = 8) count2,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t3\n" + 
			"         where t3.is_standard = 'N'\n" + 
			"           and t3.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t3.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t3.is_use = 'Y'\n" + 
			"           and t3.workticket_type_code = t.workticket_type_code\n" + 
			"           and t3.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and t3.workticket_staus_id = 14) count3,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t4\n" + 
			"         where t4.is_standard = 'N'\n" + 
			"           and t4.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t4.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t4.is_use = 'Y'\n" + 
			"           and t4.workticket_type_code = t.workticket_type_code\n" + 
			"           and t4.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and substr(t4.workticket_no, 15, 1) = 'B'" +
			"			and t4.workticket_staus_id <> 14) count4,\n" + // modify by ywliu 20091231 去掉作废状态, 14
			"       (select count(*)\n" + 
			"          from run_j_worktickets t4\n" + 
			"         where t4.is_standard = 'N'\n" + 
			"           and t4.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t4.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t4.is_use = 'Y'\n" + 
			"           and t4.workticket_type_code = t.workticket_type_code\n" + 
			"           and t4.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and substr(t4.workticket_no, 15, 1) = 'B'\n" + 
			"           and t4.workticket_staus_id = 8) count5\n" + 
			"  from run_j_worktickets t\n" + 
			" where t.is_standard = 'N'\n" + 
			"   and t.charge_dept like '"+depts+"%'\n" + 
			"   and to_char(t.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n";
		if(stop != null && stop.equals("stop"))
			sql+=" and t.charge_dept in (select d.dept_code from hr_c_dept d, " +
					"hr_c_dept_type dt where d.dept_type_id = dt.dept_type_id and " +
					"d.dept_type_id=5 and d.is_use='Y' and d.dept_status in ('1','3')) \n"; //update by sychen 20100902
//		"d.dept_type_id=5 and d.is_use='U' and d.dept_status in ('1','3')) \n";
		else
			sql += " and t.charge_dept in (select d.dept_code from hr_c_dept d, " +
			"hr_c_dept_type dt where d.dept_type_id = dt.dept_type_id and " +
			"d.dept_type_id=5 and d.is_use='Y' and d.dept_status in ('0','3')) \n"; //update by sychen 20100902
//		"d.dept_type_id=5 and d.is_use='U' and d.dept_status in ('0','3')) \n";
		sql +=	" group by t.workticket_type_code";
		
		
		 List list=bll.queryByNativeSQL(sql);
	     Iterator it = list.iterator();
	     List<WorkticketCountForm> arrayList=new ArrayList();
	     while(it.hasNext())
	     {
	    	 Object[] o = (Object[]) it.next();
	    	 WorkticketCountForm model=new WorkticketCountForm();
	    	 if(o[0] != null)
	    	 {
	    		 model.setWorkticketTypeCode(o[0].toString());
	    	 }
	    	 if(o[1] != null)
	    	 {
	    		 model.setWorkticketTypeName(o[1].toString());
	    	 }
	    	 if(o[2] != null)
	    	 {
	    		 model.setStartWorkticketNo(o[2].toString());
	    	 }
	    	 if(o[3] != null)
	    	 {
	    		 model.setEndWorkticketNo(o[3].toString());
	    	 }
	    	 if(o[4] != null)
	    	 {
	    		 model.setTotlaNum(Long.parseLong(o[4].toString()));
	    	 }
	    	 if(o[5] != null)
	    	 {
	    		 model.setWorkticketQualifiedNum(Long.parseLong(o[5].toString()));
	    	 }
	    	 if(o[6] != null)
	    	 {
	    		 model.setInvalidNum(Long.parseLong(o[6].toString()));
	    	 }
	    	 if(o[7] != null)
	    	 {
	    		 model.setUsedStanderworkticketNum(Long.parseLong(o[7].toString()));
	    	 }
	    	 if(o[8] != null)
	    	 {
	    		 model.setUsedStaworkQualifiedNum(Long.parseLong(o[8].toString()));
	    	 }
	    	 arrayList.add(model);
	     }
		return arrayList;
	}
	/**
	 * 工作票各种状态统计一览表
	 */
	public List<WorkticketCountForm> getStatusDataOfDept(String stop,String enterpriseCode,
			String date, String depts) {
		String sql=
			"select t.workticket_type_code,\n" +
			"       (select r.workticket_type_name\n" + 
			"          from run_c_workticket_type r\n" + 
			"         where r.workticket_type_code = t.workticket_type_code) workticket_type_name,\n" + 
			"       (select min(substr(t1.workticket_no, 8, 7))\n" + 
			"          from run_j_worktickets t1\n" + 
			"         where t1.is_standard = 'N'\n" + 
			"           and t1.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t1.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t1.is_use = 'Y'\n" + 
			"           and t1.workticket_type_code = t.workticket_type_code\n" + 
			"           and t1.enterprise_code = '"+enterpriseCode+"') startNo,\n" + 
			"       (select max(substr(t1.workticket_no, 8, 7))\n" + 
			"          from run_j_worktickets t1\n" + 
			"         where t1.is_standard = 'N'\n" + 
			"           and t1.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t1.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t1.is_use = 'Y'\n" + 
			"           and t1.workticket_type_code = t.workticket_type_code\n" + 
			"           and t1.enterprise_code = '"+enterpriseCode+"') endNo,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t1\n" + 
			"         where t1.is_standard = 'N'\n" + 
			"           and t1.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t1.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t1.is_use = 'Y'\n" + 
			"           and t1.workticket_type_code = t.workticket_type_code\n" + 
			"           and t1.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and t1.workticket_staus_id = 2) count1,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t2\n" + 
			"         where t2.is_standard = 'N'\n" + 
			"           and t2.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t2.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t2.is_use = 'Y'\n" + 
			"           and t2.workticket_type_code = t.workticket_type_code\n" + 
			"           and t2.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and t2.workticket_staus_id = 3) count2,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t3\n" + 
			"         where t3.is_standard = 'N'\n" + 
			"           and t3.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t3.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t3.is_use = 'Y'\n" + 
			"           and t3.workticket_type_code = t.workticket_type_code\n" + 
			"           and t3.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and t3.workticket_staus_id in (4, 24)) count3,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t4\n" + 
			"         where t4.is_standard = 'N'\n" + 
			"           and t4.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t4.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t4.is_use = 'Y'\n" + 
			"           and t4.workticket_type_code = t.workticket_type_code\n" + 
			"           and t4.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and ((t4.workticket_staus_id = 5 and t4.workticket_type_code <> 8)\n" + 
			"            or (t4.workticket_staus_id = 18 and t4.workticket_type_code = 8))) count4,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t4\n" + 
			"         where t4.is_standard = 'N'\n" + 
			"           and t4.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t4.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t4.is_use = 'Y'\n" + 
			"           and t4.workticket_type_code = t.workticket_type_code\n" + 
			"           and t4.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and t4.workticket_staus_id in (7, 27)) count5,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t4\n" + 
			"         where t4.is_standard = 'N'\n" + 
			"           and t4.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t4.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t4.is_use = 'Y'\n" + 
			"           and t4.workticket_type_code = t.workticket_type_code\n" + 
			"           and t4.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and t4.workticket_staus_id = 8) count6,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t4\n" + 
			"         where t4.is_standard = 'N'\n" + 
			"           and t4.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t4.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t4.is_use = 'Y'\n" + 
			"           and t4.workticket_type_code = t.workticket_type_code\n" + 
			"           and t4.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and t4.workticket_staus_id = 14) count7,\n" + 
			"       (select count(*)\n" + 
			"          from run_j_worktickets t4\n" + 
			"         where t4.is_standard = 'N'\n" + 
			"           and t4.charge_dept like '"+depts+"%'\n" + 
			"           and to_char(t4.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"           and t4.is_use = 'Y'\n" + 
			"           and t4.workticket_type_code = t.workticket_type_code\n" + 
			"           and t4.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and ((t4.workticket_staus_id in (2, 3, 4, 24, 5, 7, 27, 8) and\n" + // modify by ywliu 20091231 去掉作废状态, 14
			"               t4.workticket_type_code <> 8) or\n" + 
			"               (t4.workticket_staus_id in (2, 3, 4, 24, 18, 7, 27, 8) and\n" + // modify by ywliu 20091231 去掉作废状态, 14
			"               t4.workticket_type_code = 8))) totalcount\n" + 
			"  from run_j_worktickets t\n" + 
			" where t.is_standard = 'N'\n" + 
			"   and t.charge_dept like '"+depts+"%'\n" + 
			"   and to_char(t.entry_date, 'yyyy-mm') = '"+date+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" ;
		if(stop != null && stop.equals("stop"))
			sql+=" and t.charge_dept in (select d.dept_code from hr_c_dept d, " +
					"hr_c_dept_type dt where d.dept_type_id = dt.dept_type_id and " +
					"d.dept_type_id=5 and d.is_use='Y' and d.dept_status in ('1','3')) \n"; //update by sychen 20100902
//		"d.dept_type_id=5 and d.is_use='U' and d.dept_status in ('1','3')) \n";
		else
			sql += " and t.charge_dept in (select d.dept_code from hr_c_dept d start with d.dept_id = 2 or d.dept_id =195 " +
			" or d.dept_id = 194 or d.dept_id=197 " +
			" connect  by prior d.dept_id=d.pdept_id) \n";
		sql +=	" group by t.workticket_type_code";
		
		 List list=bll.queryByNativeSQL(sql);
	     Iterator it = list.iterator();
	     List<WorkticketCountForm> arrayList=new ArrayList();
	     while(it.hasNext())
	     {
	    	 Object[] o = (Object[]) it.next();
	    	 WorkticketCountForm model=new WorkticketCountForm();
	    	 if(o[0] != null)
	    	 {
	    		 model.setWorkticketTypeCode(o[0].toString());
	    	 }
	    	 if(o[1] != null)
	    	 {
	    		 model.setWorkticketTypeName(o[1].toString());
	    	 }
	    	 if(o[2] != null)
	    	 {
	    		 model.setStartWorkticketNo(o[2].toString());
	    	 }
	    	 if(o[3] != null)
	    	 {
	    		 model.setEndWorkticketNo(o[3].toString());
	    	 }
	    	 if(o[4] != null)
	    	 {
	    		 model.setNotIssuedNum(Long.parseLong(o[4].toString()));
	    	 }
	    	 if(o[5] != null)
	    	 {
	    		 model.setIssuedNum(Long.parseLong(o[5].toString()));
	    	 }
	    	 if(o[6] != null)
	    	 {
	    		 model.setReceivedNum(Long.parseLong(o[6].toString()));
	    	 }
	    	 if(o[7] != null)
	    	 {
	    		 model.setApprovaledNum(Long.parseLong(o[7].toString()));
	    	 }
	    	 if(o[8] != null)
	    	 {
	    		 model.setLicensedNum(Long.parseLong(o[8].toString()));
	    	 }
	    	 if(o[9] != null)
	    	 {
	    		 model.setAchievedNum(Long.parseLong(o[9].toString()));
	    	 }
	    	 if(o[10] != null)
	    	 {
	    		 model.setInvalidNum(Long.parseLong(o[10].toString()));
	    	 }
	    	 if(o[11] != null)
	    	 {
	    		 model.setTotlaNum(Long.parseLong(o[11].toString()));
	    	 }
	    	 arrayList.add(model);
	     }
		return arrayList;
	}
	
}
