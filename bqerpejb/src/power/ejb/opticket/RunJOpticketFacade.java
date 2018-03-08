package power.ejb.opticket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.opticket.form.OpInfoForm;
import power.ejb.opticket.form.OpticketInfo;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.form.WorkticketInfo;

import com.opensymphony.workflow.service.WorkflowService;

@Stateless
public class RunJOpticketFacade implements RunJOpticketFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "RunCOpticketTaskFacade")
	protected RunCOpticketTaskFacadeRemote taskRemote;
	@EJB(beanName = "RunJOpticketstepFacade")
	protected RunJOpticketstepFacadeRemote stepRemote;
	@EJB(beanName = "RunJWorkticketsFacade")
	protected RunJWorkticketsFacadeRemote wRemote;
	private BussiStatusEnum bussiStatus;

	public RunJOpticket save(String enterpriseChar, RunJOpticket entity) {
		try {
			if (entity.getOperateTaskId() == null) {
				throw new RuntimeException("操作任务为空");
			}
			RunCOpticketTask m = taskRemote.findById(entity.getOperateTaskId());
			if (m != null) {
				entity.setOpticketType(m.getOperateTaskCode());
			}
			String opticketCode = createOpticketCode(entity);
			entity.setOpticketCode(opticketCode);
			entity.setOperateTaskId(entity.getOperateTaskId());
			entity.setIsUse("Y");
			entity.setOpticketStatus("0");
			entity.setCreateDate(new java.util.Date());
			entityManager.persist(entity);
			StringBuffer sqls = new StringBuffer();
			sqls.append("begin\n");
			String CreatePassed=entity.getCreateBy();
			if(entity.getIsStandar().equals("N") && entity.getOpticketType().startsWith("00")){
				CreatePassed="3000007";
			}
			String sqlStep = "insert into run_j_opticketstep(operate_step_id,opticket_code,operate_step_name,display_no,is_main,run_add_flag,memo)\n"
					+ "select (select nvl(max(operate_step_id), 0) from run_j_opticketstep)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ opticketCode
					+ "',\n"
					+ "       t.operate_step_name,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.is_main,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_C_OPTICKETSTEP t\n"
					+ " where t.OPERATE_TASK_ID="
					+ entity.getOperateTaskId()
					+ " and t.is_use='Y';\n";
			String sqlDanger = "insert into RUN_J_OP_MEASURES(DANGER_ID,opticket_code,DANGER_NAME,display_no,Measure_Content,run_add_flag,memo)\n"
					+ "select (select nvl(max(DANGER_ID), 0) from RUN_J_OP_MEASURES)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ opticketCode
					+ "',\n"
					+ "       t.DANGER_NAME,\n"
					+ "       row_number() over(order by t.display_no), t.measure_content,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_C_OP_MEASURES t\n"
					+ " where t.OPERATE_TASK_ID='0' and t.is_use='Y' and t.modify_by='"+CreatePassed+"';\n";
			String sqlFin = "insert into RUN_J_OP_FINWORK(finish_work_id,opticket_code,finish_work_name,display_no,check_status,run_add_flag,memo)\n"
					+ "select (select nvl(max(finish_work_id), 0) from RUN_J_OP_FINWORK)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ opticketCode
					+ "',\n"
					+ "       t.finish_work_name,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.check_status,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_C_OP_FINWORK t\n"
					+ " where t.OPERATE_TASK_ID='0' and t.is_use='Y';\n";
			String sqlBef = "insert into RUN_J_OP_STEPCHECK(STEP_CHECK_ID,opticket_code,STEP_CHECK_NAME,display_no,check_status,run_add_flag,memo)\n"
					+ "select (select nvl(max(STEP_CHECK_ID), 0) from RUN_J_OP_STEPCHECK)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ opticketCode
					+ "',\n"
					+ "       t.STEP_CHECK_NAME,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.check_status,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_C_OP_STEPCHECK t\n"
					+ " where t.OPERATE_TASK_ID='0' and t.is_use='Y';\n";
			if (entity.getOpticketType().substring(0, 2).equals("00")) {
				sqls.append(sqlFin);
				sqls.append(sqlBef);
			}
			if (entity.getOpticketType().length() > 2) {
				sqls.append(sqlStep);
				sqls.append(sqlDanger);
			} else {
				sqls.append(sqlDanger);
			}
			sqls.append("commit;\nend;");
			bll.exeNativeSQL(sqls.toString());
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunJOpticket saveByStandTicketNo(String enterpriseChar,
			RunJOpticket entity, String standTicketNo) {
		try {
			//String opticketCode = createOpticketCode(entity) + "B";
			String opticketCode = createOpticketCode(entity);
			entity.setOpticketCode(opticketCode);
			entity.setOperateTaskId(entity.getOperateTaskId());
			entity.setIsUse("Y");
			entity.setOpticketStatus("0");
			entity.setCreateDate(new java.util.Date());
			entityManager.persist(entity);
			StringBuffer sqls = new StringBuffer();
			sqls.append("begin\n");
			String sqlStep = "insert into run_j_opticketstep(operate_step_id,opticket_code,operate_step_name,display_no,is_main,run_add_flag,memo)\n"
					+ "select (select nvl(max(operate_step_id), 0) from run_j_opticketstep)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ opticketCode
					+ "',\n"
					+ "       t.operate_step_name,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.is_main,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_J_OPTICKETSTEP t\n"
					+ " where t.OPTICKET_CODE='" + standTicketNo + "';\n";
			String sqlDanger = "insert into RUN_J_OP_MEASURES(DANGER_ID,opticket_code,DANGER_NAME,display_no,Measure_Content,run_add_flag,memo)\n"
					+ "select (select nvl(max(DANGER_ID), 0) from RUN_J_OP_MEASURES)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ opticketCode
					+ "',\n"
					+ "       t.DANGER_NAME,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.Measure_Content,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_J_OP_MEASURES t\n"
					+ " where t.OPTICKET_CODE='" + standTicketNo + "';\n";
			String sqlFin = "insert into RUN_J_OP_FINWORK(finish_work_id,opticket_code,finish_work_name,display_no,check_status,run_add_flag,memo)\n"
				+ "select (select nvl(max(finish_work_id), 0) from RUN_J_OP_FINWORK)+ row_number() over(order by t.display_no),\n"
				+ "       '"
				+ opticketCode
				+ "',\n"
				+ "       t.finish_work_name,\n"
				+ "       row_number() over(order by t.display_no),\n"
				+ "       t.check_status,\n"
				+ "       'N',\n"
				+ "       t.memo\n"
				+ "  from RUN_C_OP_FINWORK t\n"
				+ " where t.OPERATE_TASK_ID='0' and t.is_use='Y';\n";
		String sqlBef = "insert into RUN_J_OP_STEPCHECK(STEP_CHECK_ID,opticket_code,STEP_CHECK_NAME,display_no,check_status,run_add_flag,memo)\n"
				+ "select (select nvl(max(STEP_CHECK_ID), 0) from RUN_J_OP_STEPCHECK)+ row_number() over(order by t.display_no),\n"
				+ "       '"
				+ opticketCode
				+ "',\n"
				+ "       t.STEP_CHECK_NAME,\n"
				+ "       row_number() over(order by t.display_no),\n"
				+ "       t.check_status,\n"
				+ "       'N',\n"
				+ "       t.memo\n"
				+ "  from RUN_C_OP_STEPCHECK t\n"
				+ " where t.OPERATE_TASK_ID='0' and t.is_use='Y';\n";
			sqls.append(sqlStep);
			sqls.append(sqlDanger);
			if (entity.getOpticketType().substring(0, 2).equals("00")) {
				sqls.append(sqlFin);
				sqls.append(sqlBef);
			}
			sqls.append("commit;\nend;");
			bll.exeNativeSQL(sqls.toString());
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}

	}

	public RunJOpticket saveByendTicketNo(String enterpriseChar,
			RunJOpticket entity, String endTicketNo) {
		try {
			String opticketCode = createOpticketCode(entity);
			entity.setOpticketCode(opticketCode);
			entity.setOperateTaskId(entity.getOperateTaskId());
			entity.setIsUse("Y");
			entity.setOpticketStatus("0");
			entity.setCreateDate(new java.util.Date());
			entityManager.persist(entity);
			StringBuffer sqls = new StringBuffer();
			sqls.append("begin\n");
			String sqlStep = "insert into run_j_opticketstep(operate_step_id,opticket_code,operate_step_name,display_no,is_main,run_add_flag,memo)\n"
					+ "select (select nvl(max(operate_step_id), 0) from run_j_opticketstep)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ opticketCode
					+ "',\n"
					+ "       t.operate_step_name,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.is_main,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_J_OPTICKETSTEP t\n"
					+ " where t.OPTICKET_CODE='" + endTicketNo + "';\n";
			String sqlDanger = "insert into RUN_J_OP_MEASURES(DANGER_ID,opticket_code,DANGER_NAME,display_no,Measure_Content,run_add_flag,memo)\n"
					+ "select (select nvl(max(DANGER_ID), 0) from RUN_J_OP_MEASURES)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ opticketCode
					+ "',\n"
					+ "       t.DANGER_NAME,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.Measure_Content,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_J_OP_MEASURES t\n"
					+ " where t.OPTICKET_CODE='" + endTicketNo + "';\n";
			String sqlFin = "insert into RUN_J_OP_FINWORK(finish_work_id,opticket_code,finish_work_name,display_no,check_status,run_add_flag,memo)\n"
					+ "select (select nvl(max(finish_work_id), 0) from RUN_J_OP_FINWORK)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ opticketCode
					+ "',\n"
					+ "       t.finish_work_name,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.check_status,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_J_OP_FINWORK t\n"
					+ " where t.OPTICKET_CODE='" + endTicketNo + "';\n";
			String sqlBef = "insert into RUN_J_OP_STEPCHECK(STEP_CHECK_ID,opticket_code,STEP_CHECK_NAME,display_no,check_status,run_add_flag,memo)\n"
					+ "select (select nvl(max(STEP_CHECK_ID), 0) from RUN_J_OP_STEPCHECK)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ opticketCode
					+ "',\n"
					+ "       t.STEP_CHECK_NAME,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.check_status,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_J_OP_STEPCHECK t\n"
					+ " where t.OPTICKET_CODE='" + endTicketNo + "';\n";
			sqls.append(sqlStep);
			sqls.append(sqlDanger);
			if (entity.getOpticketType().substring(0, 2).equals("00")) {
				sqls.append(sqlFin);
				sqls.append(sqlBef);
			}
			sqls.append("commit;\nend;");
			bll.exeNativeSQL(sqls.toString());
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	private String createOpticketCode(RunJOpticket entity) {
		// String sql = "select replace(('"
		// + enterpriseChar
		// + "' || to_char(sysdate, 'yyyyMM') ||\n"
		// + " nvl(to_char(substr(max(t.opticket_code), 8) + 1, '0000'),\n"
		// + " '0001')),\n" + " ' ',\n"
		// + " '')\n" + " from run_j_opticket t\n"
		// + " where t.opticket_code like '" + enterpriseChar
		// + "' || to_char(sysdate, 'yyyyMM') || '%'";
		BaseDataManager baseData = (BaseDataManager) Ejb3Factory.getInstance()
				.getFacadeRemote("BaseDataManagerImpl");
		String typeIder = entity.getOpticketType().substring(0, 2).equals("01") ? "热"
				: "电";
		String spacialShortName = baseData.getSpecialityShortName(entity
				.getSpecialityCode());
		String dept = baseData.getDeptIdentifierByOpCode(entity
				.getOperatorMan());
		String sql = "select replace('"
				+ dept
				+ "' || '"
				+ spacialShortName
				+ "' || substr(to_char(sysdate,'yyyyMM'), length(to_char(sysdate,'yyyyMM')) - 3,\n"
				+ "                              length(to_char(sysdate,'yyyyMM'))) || '"
				+ typeIder
				+ "' || nvl(to_char(max(substr(t.opticket_code,\n"
				// + " length(t.Opticket_Code) - 3,\n"
				// + " length(t.opticket_code))) + 1,\n"
				+ "                              10,\n"
				+ "                              4))+1,\n"
				+ "           '0000'),\n"
				+ "       '0001'),' ','')\n"
				+ "  from run_j_opticket t\n"
				+ " where t.opticket_code like '"
				+ dept
				+ "' || '"
				+ spacialShortName
				+ "' || "
				+ " substr(to_char(sysdate,'yyyyMM'), length(to_char(sysdate,'yyyyMM')) - 3,\n"
				+ "                              length(to_char(sysdate,'yyyyMM')))"
				+ " || '%'";
		Object o = bll.getSingal(sql);
		if (o != null) {
			return o.toString().trim() + "B";
		} else
			return "00000000";
	}

	public RunJOpticket update(RunJOpticket entity) {
		try {
			RunJOpticket result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String opticketCode) {
		try {
			RunJOpticket m = this.findById(opticketCode);
			m.setIsUse("N");
			this.update(m);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJOpticket findById(String opticketCode) {
		try {
			RunJOpticket instance = entityManager.find(RunJOpticket.class,
					opticketCode);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public OpInfoForm findByCode(String opticketCode) {
		try {
			OpInfoForm entity = new OpInfoForm();
			RunJOpticket instance = entityManager.find(RunJOpticket.class,
					opticketCode);
			if (instance.getIsStandar().equals("N")) {
				String sql = "select getworkername(t.operatormans),t.work_ticket_no  from run_j_opticket t where t.opticket_code='"
						+ opticketCode + "'";
				List list = bll.queryByNativeSQL(sql);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						Object[] data = (Object[]) list.get(i);
						if (data[0] != null) {
							entity.setOperatorName(data[0].toString());
						}
						if (data[1] != null) {
							entity.setWorkTicketNo(data[1].toString());
						}
					}

				}
				if (instance.getPlanEndTime() != null) {
					entity.setPlanEndTime(instance.getPlanEndTime().toString());
				}
				if (instance.getPlanStartTime() != null)
					entity.setPlanStartTime(instance.getPlanStartTime()
							.toString());
			}
			entity.setAppendixAddr(instance.getAppendixAddr());
			entity.setIsSingle(instance.getIsSingle());
			entity.setMemo(instance.getMemo());
			entity.setOperateTaskId(instance.getOperateTaskId());
			entity.setOperateTaskName(instance.getOperateTaskName());
			entity.setOperatorMan(instance.getOperatorMan());
			entity.setOpticketCode(opticketCode);
			entity.setOpticketStatus(instance.getOpticketStatus());
			entity.setOpticketType(instance.getOpticketType());
			entity.setSpecialityCode(instance.getSpecialityCode());
			entity.setOpticketName(instance.getOpticketName());
			entity.setApplyNo(instance.getApplyNo());
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject getOpticketReportList(String workerCode,
			String enterpriseCode, String date, String date2, String opType,
			String specialCode, String opticketStatus, String isStandar,
			String optaskName, final int... rowStartIdxAndCount) {
		String strWhere = "";
		long total = 0;
		Object ob = new Object();
		if (date != null && !"".equals(date)) {
			strWhere += "  and t.create_date >=to_date('" + date
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (date2 != null && !date2.equals("")) {
			strWhere += "  and t.create_date <=to_date('" + date2
					+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("-1"))
				opType = "";
			strWhere += " and t.opticket_type like '" + opType + "%'\n";
		}
		if (specialCode != null && !"".equals(specialCode)) {
			strWhere += " and t.speciality_code='" + specialCode + "'\n";
		}
		if (isStandar != null && !"".equals(isStandar)) {
			strWhere += " and t.is_standar='" + isStandar + "'\n";
		}
		strWhere += " and (t.opticket_status='"
				+ bussiStatus.REGIST_STATUS.getValue()
				+ "' or t.opticket_status='"
				+ bussiStatus.BACK_APPROVE_STATUS.getValue() + "')\n";
		if (!"999999".equals(workerCode)) {
			strWhere += " and t.create_by='" + workerCode + "'";
		}
		if(optaskName!=null && !"".equals(optaskName)){
			strWhere += " and t.opticket_name like ?";
		}
		String sqlCount = "select count(1)  from Run_j_Opticket t\n"
				+ " where t.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and t.is_use = 'Y'\n";
		sqlCount += strWhere;
		if (optaskName != null && !optaskName.equals("")) {
			ob = bll.getSingal(sqlCount,
					new Object[] { "%" + optaskName + "%" });
		} else {
			ob = bll.getSingal(sqlCount);
		}
		if (ob != null) {
			try {
				total = Long.parseLong(ob.toString());
				PageObject result = new PageObject();
				String sql = "select\n"
						+ "    t.opticket_code,\n"
						+ "    t.opticket_name,\n"
						+ "    t.operate_task_name,\n"
						+ "    to_char(t.plan_start_time, 'yyyy-MM-dd HH24:MI:SS') psdate,\n"
						+ "    to_char(t.plan_end_time, 'yyyy-MM-dd HH24:MI:SS') pedate,\n"
						+ "    getworkername(t.operatormans) operatorName,\n"
						+ "    getworkername(t.protectormans) protectorName,\n"
						+ "    getworkername(t.class_leader) classLeaderName,\n"
						+ "    t.memo,\n"
						+ "    t.is_standar,\n"
						+ "    t.opticket_type,\n"
						+ "    t.operate_task_id,\n"
						+ "    t.opticket_status,\n"
						+ "    t.appendix_addr,\n"
						+ "    to_char(t.start_time, 'yyyy-MM-dd') sdate,\n"
						+ "    to_char(t.end_time, 'yyyy-MM-dd') edate,\n"
						+ "    t.speciality_code,\n"
						+ "    getworkername(t.create_by) createrName,\n"
						+ "    to_char(t.create_date,'yyyy-MM-dd') cdate,\n"
						+ "    t.enterprise_code,\n"
						+ "    t.work_flow_no,\n"
						+ "    t.is_use,\n"
						+ "    getworkername(t.charge_by) chargeName,\n"
						+ "    t.is_single,\n"
						+ "    getspecialname(t.speciality_code) specialityName\n"
						+ "  from Run_j_Opticket t\n"
						+ " where t.enterprise_code = '" + enterpriseCode
						+ "'\n" + "   and t.is_use = 'Y'\n";
				sql += strWhere;
				List list;
				if (optaskName != null && !optaskName.equals("")) {
					list = bll.queryByNativeSQL(sql, new Object[] { "%"
							+ optaskName + "%" }, rowStartIdxAndCount);
				} else {
					list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				}
				List<OpticketInfo> arrlist = new ArrayList<OpticketInfo>();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					RunJOpticket model = new RunJOpticket();
					OpticketInfo info = new OpticketInfo();
					Object[] data = (Object[]) it.next();
					model.setOpticketCode(data[0].toString());
					if (data[1] != null)
						model.setOpticketName(data[1].toString());
					if (data[2] != null)
						model.setOperateTaskName(data[2].toString());
					if (data[3] != null)
						info.setPlanStartDate(data[3].toString());
					if (data[4] != null)
						info.setPlanEndDate(data[4].toString());
					if (data[5] != null)
						info.setOperatorName(data[5].toString());
					if (data[6] != null)
						info.setProtectorName(data[6].toString());
					if (data[7] != null)
						info.setClassLeader(data[7].toString());
					if (data[8] != null)
						model.setMemo(data[8].toString());
					if (data[9] != null)
						model.setIsStandar(data[9].toString());
					if (data[10] != null)
						model.setOpticketType(data[10].toString());
					if (data[11] != null)
						model.setOperateTaskId(Long.parseLong(data[11]
								.toString()));
					if (data[12] != null)
						model.setOpticketStatus(data[12].toString());
					if (data[13] != null)
						model.setAppendixAddr(data[13].toString());
					if (data[14] != null)
						info.setStartTime(data[14].toString());
					if (data[15] != null)
						info.setEndTime(data[15].toString());
					if (data[16] != null)
						model.setSpecialityCode(data[16].toString());
					if (data[17] != null)
						info.setCreaterName(data[17].toString());
					if (data[18] != null)
						info.setCreateDate(data[18].toString());
					if (data[19] != null)
						model.setEnterpriseCode(data[19].toString());
					if (data[20] != null)
						model
								.setWorkFlowNo(Long.parseLong(data[20]
										.toString()));
					if (data[21] != null)
						model.setIsUse(data[21].toString());
					if (data[22] != null)
						info.setChargeName(data[22].toString());
					if (data[23] != null)
						model.setIsSingle(data[23].toString());
					if (data[24] != null)
						info.setSpecialityName(data[24].toString());
					info.setOpticket(model);
					arrlist.add(info);
				}
				if (arrlist.size() > 0) {
					result.setList(arrlist);
					result.setTotalCount(total);
				}
				return result;
			} catch (RuntimeException e) {
				e.printStackTrace();
				return null;
			}
		} else
			return null;
	}

	public PageObject getOpticketApproveList(String workerCode,
			String enterpriseCode, String date, String date2, String opType,
			String specialCode, String opticketStatus, String isStandar,
			String optaskName,String optaskNo, final int... rowStartIdxAndCount) {
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService
				.getAvailableWorkflow(new String[] { "bqOpTicketDQ",
						"bqOpTicketRL", "bqStandOpTicket" }, workerCode);
		if (entryIds == null || entryIds.trim().equals("")) {
			return null;
		}
		String strWhere = "";
		long total = 0;
		Object ob = new Object();
		if (date != null && !"".equals(date)) {
			strWhere += "  and t.create_date >=to_date('" + date
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (date2 != null && !date2.equals("")) {
			strWhere += "  and t.create_date <=to_date('" + date2
					+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("-1"))
				opType = "";
			strWhere += " and t.opticket_type like '" + opType + "%'\n";
		}
		if (specialCode != null && !"".equals(specialCode)) {
			strWhere += " and t.speciality_code='" + specialCode + "'\n";
		}
		if (isStandar != null && !"".equals(isStandar)) {
			strWhere += " and t.is_standar='" + isStandar + "'\n";
		}
		if (optaskName != null && !optaskName.equals("")) {
			strWhere += " and t.opticket_name like ?\n";
		}
		if(optaskNo != null && !optaskNo.equals("")) {
			strWhere += " and t.opticket_code like ?\n";
		}
		strWhere += " and (t.opticket_status!='"
				+ bussiStatus.REGIST_STATUS.getValue()
				+ "' and t.opticket_status!='"
				+ bussiStatus.BACK_APPROVE_STATUS.getValue()
				+ "' and t.opticket_status!='"
				+ bussiStatus.UNDIF_STATUS.getValue()
				+ "' and t.opticket_status!='"
				+ bussiStatus.END_APPROVE_STATUS.getValue()
				+ "' and t.opticket_status!='"
				+ bussiStatus.ENGINEER_APPROVE_STATUS.getValue()
				+ "' and t.opticket_status!='"
				+ bussiStatus.INVALID_APPROVE_STATUS.getValue() + "')\n";
		String _entryIds = bll.subStr(entryIds, ",", 1, "t.Work_Flow_No");
		// strWhere += " and t.Work_Flow_No in (" + entryIds + ")\n";
		strWhere += " and " + _entryIds + "\n";
		String sqlCount = "select count(1)  from Run_j_Opticket t\n"
				+ " where t.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and t.is_use = 'Y'\n";
		sqlCount += strWhere;
		if (optaskName != null && !optaskName.equals("") && optaskNo!=null && !optaskNo.equals("")) {
			ob = bll.getSingal(sqlCount,
					new Object[] { "%" + optaskName + "%" ,"%"+optaskNo+"%"});
		}else if(optaskName != null && !optaskName.equals("")){
			ob =bll.getSingal(sqlCount, 
					new Object[] {"%"+optaskName+"%"});
		}else if(optaskNo!=null && !optaskNo.equals("")){
			ob =bll.getSingal(sqlCount, 
					new Object[] {"%"+optaskNo+"%"});
		}else {
			ob = bll.getSingal(sqlCount);
		}
		if (ob != null) {
			total = Long.parseLong(ob.toString());
			try {
				PageObject result = new PageObject();

				String sql = "select\n"
						+ "    t.opticket_code,\n"
						+ "    t.opticket_name,\n"
						+ "    t.operate_task_name,\n"
						+ "    to_char(t.plan_start_time, 'yyyy-MM-dd HH24:MI:SS') psdate,\n"
						+ "    to_char(t.plan_end_time, 'yyyy-MM-dd HH24:MI:SS') pedate,\n"
						+ "    getworkername(t.operatormans) operatorName,\n"
						+ "    getworkername(t.protectormans) protectorName,\n"
						+ "    getworkername(t.class_leader) classLeaderName,\n"
						+ "    t.memo,\n"
						+ "    t.is_standar,\n"
						+ "    t.opticket_type,\n"
						+ "    t.operate_task_id,\n"
						+ "    t.opticket_status,\n"
						+ "    t.appendix_addr,\n"
						+ "    to_char(t.start_time, 'yyyy-MM-dd') sdate,\n"
						+ "    to_char(t.end_time, 'yyyy-MM-dd') edate,\n"
						+ "    t.speciality_code,\n"
						+ "    getworkername(t.create_by) createrName,\n"
						+ "    to_char(t.create_date,'yyyy-MM-dd') cdate,\n"
						+ "    t.enterprise_code,\n"
						+ "    t.work_flow_no,\n"
						+ "    t.is_use,\n"
						+ "    getworkername(t.charge_by) chargeName,\n"
						+ "    t.is_single,\n"
						+ "    getspecialname(t.speciality_code) specialityName\n"
						+ "  from Run_j_Opticket t\n"
						+ " where t.enterprise_code = '" + enterpriseCode
						+ "'\n" + "   and t.is_use = 'Y'\n";
				sql += strWhere;
				List list;
				if (optaskName !=null && !optaskName.equals("") && optaskNo !=null && !optaskNo.equals("")){
					list = bll.queryByNativeSQL(sql, new Object[] { "%"
					       + optaskName + "%","%" + optaskNo + "%"}, rowStartIdxAndCount);
				}else if (optaskNo !=null && !optaskNo.equals("")){
					list = bll.queryByNativeSQL(sql, new Object[] { "%"
							+ optaskNo + "%"}, rowStartIdxAndCount);
				}else if (optaskName != null && !optaskName.equals("")) {
					list = bll.queryByNativeSQL(sql, new Object[] { "%"
							+ optaskName + "%" }, rowStartIdxAndCount);
				}else {
					list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				}
				List<OpticketInfo> arrlist = new ArrayList<OpticketInfo>();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					RunJOpticket model = new RunJOpticket();
					OpticketInfo info = new OpticketInfo();
					Object[] data = (Object[]) it.next();
					model.setOpticketCode(data[0].toString());
					if (data[1] != null)
						model.setOpticketName(data[1].toString());
					if (data[2] != null)
						model.setOperateTaskName(data[2].toString());
					if (data[3] != null)
						info.setPlanStartDate(data[3].toString());
					if (data[4] != null)
						info.setPlanEndDate(data[4].toString());
					if (data[5] != null)
						info.setOperatorName(data[5].toString());
					if (data[6] != null)
						info.setProtectorName(data[6].toString());
					if (data[7] != null)
						info.setClassLeader(data[7].toString());
					if (data[8] != null)
						model.setMemo(data[8].toString());
					if (data[9] != null)
						model.setIsStandar(data[9].toString());
					if (data[10] != null)
						model.setOpticketType(data[10].toString());
					if (data[11] != null)
						model.setOperateTaskId(Long.parseLong(data[11]
								.toString()));
					if (data[12] != null)
						model.setOpticketStatus(data[12].toString());
					if (data[13] != null)
						model.setAppendixAddr(data[13].toString());
					if (data[14] != null)
						info.setStartTime(data[14].toString());
					if (data[15] != null)
						info.setEndTime(data[15].toString());
					if (data[16] != null)
						model.setSpecialityCode(data[16].toString());
					if (data[17] != null)
						info.setCreaterName(data[17].toString());
					if (data[18] != null)
						info.setCreateDate(data[18].toString());
					if (data[19] != null)
						model.setEnterpriseCode(data[19].toString());
					if (data[20] != null)
						model
								.setWorkFlowNo(Long.parseLong(data[20]
										.toString()));
					if (data[21] != null)
						model.setIsUse(data[21].toString());
					if (data[22] != null)
						info.setChargeName(data[22].toString());
					if (data[23] != null)
						model.setIsSingle(data[23].toString());
					if (data[24] != null)
						info.setSpecialityName(data[24].toString());
					info.setOpticket(model);
					arrlist.add(info);
				}
				if (arrlist.size() > 0) {
					result.setList(arrlist);
					result.setTotalCount(total);
				}
				return result;
			} catch (RuntimeException e) {
				LogUtil.log("find all failed", Level.SEVERE, e);
				throw e;
			}
		} else
			return null;
	}

	public PageObject getOpticketList(String enterpriseCode, String newOrOld,
			String date, String date2, String opType, String specialCode,
			String opticketStatus, String isStandar, String optaskName,
			String createBy, String optaskNo,final int... rowStartIdxAndCount) {
		String strWhere = "";
		long total = 0;
		Object ob = new Object();
		if (date != null && !"".equals(date)) {
			strWhere += "  and t.create_date >=to_date('" + date
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (date2 != null && !date2.equals("")) {
			strWhere += "  and t.create_date <=to_date('" + date2
					+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (opType != null && !"".equals(opType)) {
			strWhere += " and t.opticket_type like '" + opType + "%'\n";
		}
		if (opticketStatus != null && !"".equals(opticketStatus)) {
			//update by sychen 20100805
			if("'10'".equals(opticketStatus)){
				strWhere += " and t.opticket_status  not in ('5','Z')\n";	
			}
			else {

				strWhere += " and t.opticket_status in (" + opticketStatus + ")\n";
			}
//			strWhere += " and t.opticket_status in (" + opticketStatus + ")\n";
			//update by sychen 20100805 end
		}
		if (specialCode != null && !"".equals(specialCode)) {
			strWhere += " and t.speciality_code='" + specialCode + "'\n";
		}
		// 如果为标准票查询加入新票/老票区别
		if (isStandar != null && !"".equals(isStandar)) {
			strWhere += " and t.is_standar='" + isStandar + "'\n";
			if ("old".equals(newOrOld)) {
				strWhere += " and t.opticket_code like '125MW%'\n";
			} else if ("new".equals(newOrOld)) {
				strWhere += " and t.opticket_code not like '125MW%'\n";
			}
		}
		if (optaskName != null && !optaskName.equals("")) {
			strWhere += " and t.opticket_name like ?\n";
		}
		if (createBy != null && !createBy.equals("")) {
			strWhere += " and t.create_by ='" + createBy + "' \n";
		}
		if(optaskNo !=null && !optaskNo.equals("")){
			strWhere +="and t.opticket_code like ?\n";
		}
		String sqlCount = "select count(1)  from Run_j_Opticket t\n"
				+ " where t.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and t.is_use = 'Y'\n";
		sqlCount += strWhere;
		if (optaskName != null && !optaskName.equals("") && optaskNo!=null && !optaskNo.equals("")) {
			ob = bll.getSingal(sqlCount,
					new Object[] { "%" + optaskName + "%" ,"%"+optaskNo+"%"});
		}else if(optaskName != null && !optaskName.equals("")){
			ob =bll.getSingal(sqlCount, 
					new Object[] {"%"+optaskName+"%"});
		}else if(optaskNo!=null && !optaskNo.equals("")){
			ob =bll.getSingal(sqlCount, 
					new Object[] {"%"+optaskNo+"%"});
		}else {
			ob = bll.getSingal(sqlCount);
		}
		if (ob != null) {
			try {
				PageObject result = new PageObject();
				total = Long.parseLong(ob.toString());
				String sql = "select\n"
						+ "    t.opticket_code,\n"
						+ "    t.opticket_name,\n"
						+ "    t.operate_task_name,\n"
						+ "    to_char(t.plan_start_time, 'yyyy-MM-dd HH24:MI:SS') psdate,\n"
						+ "    to_char(t.plan_end_time, 'yyyy-MM-dd HH24:MI:SS') pedate,\n"
						+ "    getworkername(t.operatormans) operatorName,\n"
						+ "    getworkername(t.protectormans) protectorName,\n"
						+ "    getworkername(t.class_leader) classLeaderName,\n"
						+ "    t.memo,\n"
						+ "    t.is_standar,\n"
						+ "    t.opticket_type,\n"
						+ "    t.operate_task_id,\n"
						+ "    t.opticket_status,\n"
						+ "    t.appendix_addr,\n"
						+ "    to_char(t.start_time, 'yyyy-MM-dd HH24:MI:SS') sdate,\n"
						+ "    to_char(t.end_time, 'yyyy-MM-dd HH24:MI:SS') edate,\n"
						+ "    t.speciality_code,\n"
						+ "    getworkername(t.create_by) createrName,\n"
						+ "    to_char(t.create_date,'yyyy-MM-dd') cdate,\n"
						+ "    t.enterprise_code,\n"
						+ "    t.work_flow_no,\n"
						+ "    t.is_use,\n"
						+ "    getworkername(t.charge_by) chargeName,\n"
						+ "    t.is_single,\n"
						+ "    getspecialname(t.speciality_code) specialityName\n"
						+ "  from Run_j_Opticket t\n"
						+ " where t.enterprise_code = '" + enterpriseCode
						+ "'\n" + "   and t.is_use = 'Y'\n";

				sql += strWhere;
				List list;
				if (optaskName !=null && !optaskName.equals("") && optaskNo !=null && !optaskNo.equals("")){
					list = bll.queryByNativeSQL(sql, new Object[] { "%"
					       + optaskName + "%","%" + optaskNo + "%"}, rowStartIdxAndCount);
				}else if (optaskNo !=null && !optaskNo.equals("")){
					list = bll.queryByNativeSQL(sql, new Object[] { "%"
							+ optaskNo + "%"}, rowStartIdxAndCount);
				}else if (optaskName != null && !optaskName.equals("")) {
					list = bll.queryByNativeSQL(sql, new Object[] { "%"
							+ optaskName + "%" }, rowStartIdxAndCount);
				}else {
					list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				}
				List<OpticketInfo> arrlist = new ArrayList<OpticketInfo>();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					RunJOpticket model = new RunJOpticket();
					OpticketInfo info = new OpticketInfo();
					Object[] data = (Object[]) it.next();
					model.setOpticketCode(data[0].toString());
					if (data[1] != null)
						model.setOpticketName(data[1].toString());
					if (data[2] != null)
						model.setOperateTaskName(data[2].toString());
					if (data[3] != null)
						info.setPlanStartDate(data[3].toString());
					if (data[4] != null)
						info.setPlanEndDate(data[4].toString());
					if (data[5] != null)
						info.setOperatorName(data[5].toString());
					if (data[6] != null)
						info.setProtectorName(data[6].toString());
					if (data[7] != null)
						info.setClassLeader(data[7].toString());
					if (data[8] != null)
						model.setMemo(data[8].toString());
					if (data[9] != null)
						model.setIsStandar(data[9].toString());
					if (data[10] != null)
						model.setOpticketType(data[10].toString());
					if (data[11] != null)
						model.setOperateTaskId(Long.parseLong(data[11]
								.toString()));
					if (data[12] != null)
						model.setOpticketStatus(data[12].toString());
					if (data[13] != null)
						model.setAppendixAddr(data[13].toString());
					if (data[14] != null)
						info.setStartTime(data[14].toString());
					if (data[15] != null)
						info.setEndTime(data[15].toString());
					if (data[16] != null)
						model.setSpecialityCode(data[16].toString());
					if (data[17] != null)
						info.setCreaterName(data[17].toString());
					if (data[18] != null)
						info.setCreateDate(data[18].toString());
					if (data[19] != null)
						model.setEnterpriseCode(data[19].toString());
					if (data[20] != null)
						model
								.setWorkFlowNo(Long.parseLong(data[20]
										.toString()));
					if (data[21] != null)
						model.setIsUse(data[21].toString());
					if (data[22] != null)
						info.setChargeName(data[22].toString());
					if (data[23] != null)
						model.setIsSingle(data[23].toString());
					if (data[24] != null)
						info.setSpecialityName(data[24].toString());
					info.setOpticket(model);
					arrlist.add(info);
				}
				if (arrlist.size() > 0) {
					result.setList(arrlist);
					result.setTotalCount(total);
				}
				return result;
			} catch (RuntimeException e) {
				LogUtil.log("find all failed", Level.SEVERE, e);
				throw e;
			}
		} else
			return null;

	}

	public PageObject getStantdOpticktetList(String enterpriseCode,
			String newOrOld, String opticketType, String specialityCode,
			String opStatus, String taskNameKey,
			final int... rowStartIdxAndCount) {
		String sqlWhere = "";
		long total = 0;
		Object ob = new Object();
		if (opticketType != null) {
			sqlWhere += "   and t.opticket_type like '" + opticketType + "%'\n";
		}
		if (specialityCode != null) {
			sqlWhere += "   and t.speciality_code = '" + specialityCode + "'\n";
		}
		if (opStatus != null) {
			sqlWhere += "   and t.opticket_status = '" + opStatus + "'\n";
		}
		if (taskNameKey != null && !taskNameKey.trim().equals("")) {
			taskNameKey = taskNameKey.replace("'", "");
			sqlWhere += "   and t.opticket_name like '%" + taskNameKey + "%'\n";
		}
		if ("old".equals(newOrOld)) {
			sqlWhere += " and t.opticket_code like '125MW%'\n";
		} else if ("new".equals(newOrOld)) {
			sqlWhere += " and t.opticket_code not like '125MW%'\n";
		}

		String sqlCount = "select count(1) from run_j_opticket t"
				+ " where t.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and t.is_standar = 'Y'\n" + "   and t.is_use = 'Y'";
		sqlCount += sqlWhere;
		ob = bll.getSingal(sqlCount);
		if (ob != null) {
			try {
				total = Long.parseLong(ob.toString());
				PageObject pg = new PageObject();
				String sql = "select t.*" + "  from run_j_opticket t\n"
						+ " where t.enterprise_code = '" + enterpriseCode
						+ "'\n" + "   and t.is_standar = 'Y'\n"
						+ "   and t.is_use = 'Y'";
				sql += sqlWhere;
				List<RunJOpticket> list = bll.queryByNativeSQL(sql,
						RunJOpticket.class, rowStartIdxAndCount);
				pg.setList(list);
				pg.setTotalCount(total);
				return pg;
			} catch (RuntimeException e) {
				LogUtil.log("find all failed", Level.SEVERE, e);
				throw e;
			}
		} else
			return null;
	}

	public String findByWorktickectNo(String worktickectNo) {
		String returnStr = "";
		String sql = "select t.opticket_code from run_j_opticket t where t.work_ticket_no=? and t.is_use='Y'";
		List list = bll.queryByNativeSQL(sql, new Object[] { worktickectNo });
		Iterator it = list.iterator();
		while (it.hasNext()) {
			returnStr += it.next().toString() + ",";
		}
		if (!returnStr.equals("")) {
			returnStr = returnStr.substring(0, returnStr.length() - 1);
		}
		return returnStr;
	}

	public PageObject getWarOpticketList(String enterpriseCode, String date,
			String date2, String opType, String specialCode,
			final int... rowStartIdxAndCount) {
		List<WorkticketInfo> wlist = wRemote.workticketEndWarn(enterpriseCode,
				"%", "%", "%");
		String workicketCodes = "";

		for (int k = 0; k < wlist.size(); k++) {
			workicketCodes += wlist.get(k).getModel().getWorkticketNo();
			workicketCodes += ",";
		}
		if (workicketCodes.length() > 1) {
			workicketCodes = workicketCodes.substring(0, workicketCodes
					.length() - 1);
		}
		String strWhere = "";
		long total = 0;
		Object ob = new Object();
		if (date != null && !"".equals(date)) {
			strWhere += "  and t.create_date >=to_date('" + date
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (date2 != null && !date2.equals("")) {
			strWhere += "  and t.create_date <=to_date('" + date2
					+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (opType != null && !"".equals(opType)) {
			strWhere += " and t.opticket_type like '" + opType + "%'\n";
		}

		if (specialCode != null && !"".equals(specialCode)) {
			strWhere += " and t.speciality_code='" + specialCode + "'\n";
		}
		strWhere += " and t.Work_Ticket_No in (" + workicketCodes + ")";
		String sqlCount = "select count(1)  from Run_j_Opticket t\n"
				+ " where t.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and t.is_use = 'Y'\n";
		sqlCount += strWhere;
		ob = bll.getSingal(sqlCount);
		if (ob != null) {
			try {
				PageObject result = new PageObject();
				total = Long.parseLong(ob.toString());
				String sql = "select\n"
						+ "    t.opticket_code,\n"
						+ "    t.opticket_name,\n"
						+ "    t.operate_task_name,\n"
						+ "    to_char(t.plan_start_time, 'yyyy-MM-dd HH24:MI:SS') psdate,\n"
						+ "    to_char(t.plan_end_time, 'yyyy-MM-dd HH24:MI:SS') pedate,\n"
						+ "    getworkername(t.operatormans) operatorName,\n"
						+ "    getworkername(t.protectormans) protectorName,\n"
						+ "    getworkername(t.class_leader) classLeaderName,\n"
						+ "    t.memo,\n"
						+ "    t.is_standar,\n"
						+ "    t.opticket_type,\n"
						+ "    t.operate_task_id,\n"
						+ "    t.opticket_status,\n"
						+ "    t.appendix_addr,\n"
						+ "    to_char(t.start_time, 'yyyy-MM-dd HH24:MI:SS') sdate,\n"
						+ "    to_char(t.end_time, 'yyyy-MM-dd HH24:MI:SS') edate,\n"
						+ "    t.speciality_code,\n"
						+ "    getworkername(t.create_by) createrName,\n"
						+ "    to_char(t.create_date,'yyyy-MM-dd') cdate,\n"
						+ "    t.enterprise_code,\n"
						+ "    t.work_flow_no,\n"
						+ "    t.is_use,\n"
						+ "    getworkername(t.charge_by) chargeName,\n"
						+ "    t.is_single,\n"
						+ "    getspecialname(t.speciality_code) specialityName\n"
						+ "  from Run_j_Opticket t\n"
						+ " where t.enterprise_code = '" + enterpriseCode
						+ "'\n" + "   and t.is_use = 'Y'\n";

				sql += strWhere;
				List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				List<OpticketInfo> arrlist = new ArrayList<OpticketInfo>();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					RunJOpticket model = new RunJOpticket();
					OpticketInfo info = new OpticketInfo();
					Object[] data = (Object[]) it.next();
					model.setOpticketCode(data[0].toString());
					if (data[1] != null)
						model.setOpticketName(data[1].toString());
					if (data[2] != null)
						model.setOperateTaskName(data[2].toString());
					if (data[3] != null)
						info.setPlanStartDate(data[3].toString());
					if (data[4] != null)
						info.setPlanEndDate(data[4].toString());
					if (data[5] != null)
						info.setOperatorName(data[5].toString());
					if (data[6] != null)
						info.setProtectorName(data[6].toString());
					if (data[7] != null)
						info.setClassLeader(data[7].toString());
					if (data[8] != null)
						model.setMemo(data[8].toString());
					if (data[9] != null)
						model.setIsStandar(data[9].toString());
					if (data[10] != null)
						model.setOpticketType(data[10].toString());
					if (data[11] != null)
						model.setOperateTaskId(Long.parseLong(data[11]
								.toString()));
					if (data[12] != null)
						model.setOpticketStatus(data[12].toString());
					if (data[13] != null)
						model.setAppendixAddr(data[13].toString());
					if (data[14] != null)
						info.setStartTime(data[14].toString());
					if (data[15] != null)
						info.setEndTime(data[15].toString());
					if (data[16] != null)
						model.setSpecialityCode(data[16].toString());
					if (data[17] != null)
						info.setCreaterName(data[17].toString());
					if (data[18] != null)
						info.setCreateDate(data[18].toString());
					if (data[19] != null)
						model.setEnterpriseCode(data[19].toString());
					if (data[20] != null)
						model
								.setWorkFlowNo(Long.parseLong(data[20]
										.toString()));
					if (data[21] != null)
						model.setIsUse(data[21].toString());
					if (data[22] != null)
						info.setChargeName(data[22].toString());
					if (data[23] != null)
						model.setIsSingle(data[23].toString());
					if (data[24] != null)
						info.setSpecialityName(data[24].toString());
					info.setOpticket(model);
					arrlist.add(info);
				}
				if (arrlist.size() > 0) {
					result.setList(arrlist);
					result.setTotalCount(total);
				}
				return result;
			} catch (RuntimeException e) {
				LogUtil.log("find all failed", Level.SEVERE, e);
				throw e;
			}
		} else
			return null;
	}

	public String createAnddelOptickect(String optickect,
			String enterpriseChar, RunJOpticket entity) {
		String optickectNo = "";
		optickectNo = this.createOpticketCode(entity);
		entity.setOpticketCode(optickectNo);
		try {
			if (entity.getOperateTaskId() == null) {
				throw new RuntimeException("操作任务为空");
			}
			entity.setOpticketStatus("0");
			entity.setCreateDate(new java.util.Date());
			entityManager.persist(entity);
			StringBuffer sqls = new StringBuffer();
			sqls.append("begin\n");
			String sqlStep = "insert into run_j_opticketstep(operate_step_id,opticket_code,operate_step_name,display_no,is_main,run_add_flag,memo)\n"
					+ "select (select nvl(max(operate_step_id), 0) from run_j_opticketstep)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ optickectNo
					+ "',\n"
					+ "       t.operate_step_name,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.is_main,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_J_OPTICKETSTEP t\n"
					+ " where t.OPTICKET_CODE='" + optickect + "';\n";
			String sqlDanger = "insert into RUN_J_OP_MEASURES(DANGER_ID,opticket_code,DANGER_NAME,display_no,Measure_Content,run_add_flag,memo)\n"
					+ "select (select nvl(max(DANGER_ID), 0) from RUN_J_OP_MEASURES)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ optickectNo
					+ "',\n"
					+ "       t.DANGER_NAME,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.Measure_Content,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_J_OP_MEASURES t\n"
					+ " where t.OPTICKET_CODE='" + optickect + "';\n";
			String sqlFin = "insert into RUN_J_OP_FINWORK(finish_work_id,opticket_code,finish_work_name,display_no,check_status,run_add_flag,memo)\n"
					+ "select (select nvl(max(finish_work_id), 0) from RUN_J_OP_FINWORK)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ optickectNo
					+ "',\n"
					+ "       t.finish_work_name,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.check_status,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_J_OP_FINWORK t\n"
					+ " where t.OPTICKET_CODE='" + optickect + "';\n";
			String sqlBef = "insert into RUN_J_OP_STEPCHECK(STEP_CHECK_ID,opticket_code,STEP_CHECK_NAME,display_no,check_status,run_add_flag,memo)\n"
					+ "select (select nvl(max(STEP_CHECK_ID), 0) from RUN_J_OP_STEPCHECK)+ row_number() over(order by t.display_no),\n"
					+ "       '"
					+ optickectNo
					+ "',\n"
					+ "       t.STEP_CHECK_NAME,\n"
					+ "       row_number() over(order by t.display_no),\n"
					+ "       t.check_status,\n"
					+ "       'N',\n"
					+ "       t.memo\n"
					+ "  from RUN_J_OP_STEPCHECK t\n"
					+ " where t.OPTICKET_CODE='" + optickect + "';\n";
			sqls.append(sqlStep);
			sqls.append(sqlDanger);
			if (entity.getOpticketType().substring(0, 2).equals("00")) {
				sqls.append(sqlFin);
				sqls.append(sqlBef);
			}
			sqls.append("commit;\nend;");
			bll.exeNativeSQL(sqls.toString());
			this.delete(optickect);
		} catch (RuntimeException re) {
			throw re;
		}
		return optickectNo;
	}
}