package power.ejb.opticket;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.opticket.bussiness.RunJOpFinwork;
import power.ejb.opticket.bussiness.RunJOpFinworkFacadeRemote;
import power.ejb.opticket.bussiness.RunJOpMeasures;
import power.ejb.opticket.bussiness.RunJOpMeasuresFacadeRemote;
import power.ejb.opticket.bussiness.RunJOpStepcheck;
import power.ejb.opticket.bussiness.RunJOpStepcheckFacadeRemote;
import power.ejb.opticket.form.CheckBaseForPrint;
import power.ejb.opticket.form.DangerousBaseForPrint;
import power.ejb.opticket.form.OpticketBaseForPrint;
import power.ejb.opticket.form.OpticketPrintModel;
import power.ejb.opticket.form.WorkBaseForPrint;

import com.opensymphony.engineassistant.po.WorkflowEvent;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

@Stateless
public class OpticketApproveImpl implements OpticketApprove {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service = new WorkflowServiceImpl();
	@EJB(beanName = "RunJOpticketFacade")
	protected RunJOpticketFacadeRemote remote;
	@EJB(beanName = "RunJOpticketstepFacade")
	protected RunJOpticketstepFacadeRemote stepremote;
	@EJB(beanName = "RunJOpFinworkFacade")
	protected RunJOpFinworkFacadeRemote workremote;
	@EJB(beanName = "RunJOpMeasuresFacade")
	protected RunJOpMeasuresFacadeRemote dangerremote;
	@EJB(beanName = "RunJOpStepcheckFacade")
	protected RunJOpStepcheckFacadeRemote checkremote;
	private BussiStatusEnum bussiStatusEnum;

	public void reportTo(String opticketCode, String flowCode,
			String workerCode, Long actionId, String eventIdentify,
			String approveText,String appointNextRoleOrNextPeople) {
		RunJOpticket entity = remote.findById(opticketCode);
		if (eventIdentify.equals("JHCZ")) {
			entity.setIsSingle("N");
		} else if (eventIdentify.equals("SB")) {
			entity.setIsSingle("N");
		} else {
			entity.setIsSingle("Y");
		}
		WorkflowService service = new WorkflowServiceImpl();
		long entryId = service.doInitialize(flowCode, workerCode, opticketCode);
		service.doAction(entryId, workerCode, actionId, approveText, null,appointNextRoleOrNextPeople,"");
		entity.setWorkFlowNo(entryId);
		entity.setCreateBy(workerCode);
		entity.setOpticketStatus(bussiStatusEnum.REPORT_STATUS.getValue());
		remote.update(entity);
	}

	/**
	 * 获得审批方式列表
	 */
	public List<WorkflowEvent> findActionList(String opticketCode) {
		RunJOpticket entity = remote.findById(opticketCode);
		if (entity != null) {
			List<WorkflowEvent> actionList = service.getActions(entity
					.getWorkFlowNo());
			return actionList;
		}
		return null;
	}

	public void watcherSign(String opticketCode, String workerCode,
			Long actionId, String approveText, String eventIdentify,
			String appointNextRoleOrNextPeople) {
		RunJOpticket entity = remote.findById(opticketCode);
		String status = bussiStatusEnum.UNDIF_STATUS.getValue();
		if (eventIdentify.equals("TY") || eventIdentify.equals("DRCZ")
				|| eventIdentify.equals("JHCZ")) {
			status = bussiStatusEnum.WARCH_APPROVE_STATUS.getValue();
		} else if (eventIdentify.equals("ZF")) {
			status = bussiStatusEnum.INVALID_APPROVE_STATUS.getValue();
		} else if (eventIdentify.equals("TH")) {
			status = bussiStatusEnum.BACK_APPROVE_STATUS.getValue();
		} else {
			status = bussiStatusEnum.UNDIF_STATUS.getValue();
		}
		entity.setOpticketStatus(status);
		if("N".equals(entity.getIsSingle()) || "00".equals(entity.getOpticketType())){
			List <RunJOpticketstep> steps=stepremote.findByOperateCode(opticketCode);
			if(steps!=null){
				for(int k=0;k<steps.size();k++){
					RunJOpticketstep step=steps.get(k);
					step.setProMan(workerCode);
					stepremote.update(step);
				}
			}
		}
		entity.setProtectorMan(workerCode);
		// }
		remote.update(entity);
		this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
				approveText,appointNextRoleOrNextPeople);
	}

	/**
	 * 执行
	 * 
	 * @param entryId
	 *            实例编号
	 * @param workerCode
	 *            审批人
	 * @param actionId
	 *            动作
	 * @param approveText
	 *            意见
	 */
	private void changeWfInfo(Long entryId, String workerCode, Long actionId,
			String approveText,String appointNextRoleOrNextPeople) {
		System.out.println("指定："+appointNextRoleOrNextPeople);
		service.doAction(entryId, workerCode, actionId, approveText, null,appointNextRoleOrNextPeople,null);
	}

	public void chargSign(String opticketCode, String workerCode,
			Long actionId, String approveText, String eventIdentify,
			String appointNextRoleOrNextPeople) {
		RunJOpticket entity = remote.findById(opticketCode);
		String status = bussiStatusEnum.UNDIF_STATUS.getValue();
		if (eventIdentify.equals("TY") || eventIdentify.equals("SQZZPZ")) {
			status = bussiStatusEnum.CHARG_APPROVE_STATUS.getValue();
		} else if (eventIdentify.equals("ZF")) {
			status = bussiStatusEnum.INVALID_APPROVE_STATUS.getValue();
		} else if (eventIdentify.equals("TH")) {
			status = bussiStatusEnum.BACK_APPROVE_STATUS.getValue();
		} else {
			status = bussiStatusEnum.UNDIF_STATUS.getValue();
		}
		entity.setOpticketStatus(status);
		entity.setChargeBy(workerCode);
		remote.update(entity);
		this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
				approveText,appointNextRoleOrNextPeople);
	}

	public void dutySign(String opticketCode, String workerCode, Long actionId,
			String approveText, String eventIdentify,
			String appointNextRoleOrNextPeople) {
		RunJOpticket entity = remote.findById(opticketCode);
		String status = bussiStatusEnum.UNDIF_STATUS.getValue();
		if (eventIdentify.equals("TY")) {
			status = bussiStatusEnum.CLASS_LEAD_APPROVE_STATUS.getValue();
		} else if (eventIdentify.equals("ZF")) {
			status = bussiStatusEnum.INVALID_APPROVE_STATUS.getValue();
		} else if (eventIdentify.equals("TH")) {
			status = bussiStatusEnum.BACK_APPROVE_STATUS.getValue();
		} else {
			status = bussiStatusEnum.UNDIF_STATUS.getValue();
		}
		entity.setOpticketStatus(status);
		entity.setClassLeader(workerCode);
		remote.update(entity);
		this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
				approveText,appointNextRoleOrNextPeople);
	}

	public void endOpticket(String opticketCode, String workerCode,
			Long actionId, String approveText, String eventIdentify,
			String planStartTime, String planEndTime, String completeTime,
			String aftTime) {
		RunJOpticket entity = remote.findById(opticketCode);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (eventIdentify.equals("ZJ")) {
			try {
				entity.setOpticketStatus(bussiStatusEnum.END_APPROVE_STATUS
						.getValue());
				if (planStartTime != null && planStartTime.trim().length() > 0) {
					entity.setStartTime(format.parse(planStartTime));
				}
				if (planEndTime != null && planEndTime.trim().length() > 0) {
					entity.setEndTime(format.parse(planEndTime));
				}
				if (completeTime != null && completeTime.trim().length() > 0) {
					entity.setPlanEndTime(format.parse(completeTime));
				}
				if (aftTime != null && aftTime.trim().length() > 0) {
					entity.setPlanStartTime(format.parse(aftTime));
				}
				if (approveText != null && approveText.trim().length() > 0) {
					entity.setMemo(approveText);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			entity.setOpticketStatus(bussiStatusEnum.INVALID_APPROVE_STATUS
					.getValue());
		}
		remote.update(entity);
		service.doAction(entity.getWorkFlowNo(), workerCode, actionId, approveText, null);
	}

	public void headSign(String opticketCode, String workerCode, Long actionId,
			String approveText, String eventIdentify,
			String appointNextRoleOrNextPeople) {
		RunJOpticket entity = remote.findById(opticketCode);
		String status = bussiStatusEnum.UNDIF_STATUS.getValue();
		if (eventIdentify.equals("TY")) {
			entity.setOpticketStatus(bussiStatusEnum.HEAD_APPROVE_STATUS
					.getValue());
		} else if (eventIdentify.equals("TH")) {
			entity.setOpticketStatus(bussiStatusEnum.BACK_APPROVE_STATUS
					.getValue());
		} else {
			entity.setOpticketStatus(bussiStatusEnum.UNDIF_STATUS.getValue());
		}
		entity.setProtectorMan(workerCode);
		remote.update(entity);
		this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
				approveText,appointNextRoleOrNextPeople);
	}

	public void safeDeptSign(String opticketCode, String workerCode,
			Long actionId, String approveText, String eventIdentify,
			String appointNextRoleOrNextPeople) {
		RunJOpticket entity = remote.findById(opticketCode);
		String status = bussiStatusEnum.UNDIF_STATUS.getValue();
		if (eventIdentify.equals("TY")) {
			entity.setOpticketStatus(bussiStatusEnum.SAFE_APPROVE_STATUS
					.getValue());
		} else if (eventIdentify.equals("TH")) {
			entity.setOpticketStatus(bussiStatusEnum.BACK_APPROVE_STATUS
					.getValue());
		} else {
			entity.setOpticketStatus(bussiStatusEnum.UNDIF_STATUS.getValue());
		}
		entity.setProtectorMan(workerCode);
		remote.update(entity);
		this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
				approveText,appointNextRoleOrNextPeople);
	}

	public void engineerSign(String opticketCode, String workerCode,
			Long actionId, String approveText) {
		RunJOpticket entity = remote.findById(opticketCode);
		entity.setOpticketStatus(bussiStatusEnum.ENGINEER_APPROVE_STATUS
				.getValue());
		entity.setChargeBy(workerCode);
		remote.update(entity);
		service.doAction(entity.getWorkFlowNo(), workerCode, actionId, approveText, null);
	}

	public OpticketPrintModel getOpticketData(String opticketCode) {
		String[] names = this.getNames(opticketCode);
		OpticketPrintModel model = new OpticketPrintModel();
		String sql = "select\n" + "  t.opticket_code,\n"
				+ "  t.opticket_name,\n" + "  t.operate_task_name,\n"
				+ "  getspecialname(t.speciality_code),\n"
				+ "  to_char(t.start_time,'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "  to_char(t.end_time,'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "  t.memo,\n" + "  getworkername(t.operatormans),\n"
				+ "  getworkername(t.protectormans),\n"
				+ "  getworkername(t.class_leader),\n"
				+ "  getworkername(t.charge_by),\n" + "  t.is_single\n"
				+ "  from run_j_opticket t\n" + " where t.opticket_code = '"
				+ opticketCode + "'\n" + "   and t.is_use = 'Y'";
		Object[] ob = (Object[]) bll.getSingal(sql);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		if (ob != null) {
			OpticketBaseForPrint obp = new OpticketBaseForPrint();
			obp.setOpticketCode(ob[0].toString());
			if (ob[1] != null) {
				obp.setOpticketName(ob[1].toString());
				obp.setOperateTaskName(ob[1].toString());
			}
			// if (ob[2] != null)
			// obp.setOperateTaskName(ob[2].toString());
			if (ob[3] != null)
				obp.setSpecialityName(ob[3].toString());
			if (ob[4] != null) {
				try {
					obp.setStartTime(sf.format(df.parse(ob[4].toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (ob[5] != null) {
				try {
					obp.setEndTime(sf.format(df.parse(ob[5].toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (ob[6] != null)
				obp.setMemo(ob[6].toString());
			if (names[0] != null)
				obp.setOperatorName(names[0]);
			if (names[1] != null)
				obp.setProtectorName(names[1]);
			// if (ob[7] != null)
			// obp.setOperatorName(ob[7].toString());
			// if (ob[8] != null)
			// obp.setProtectorName(ob[8].toString());
			if (ob[9] != null)
				obp.setClassLeaderName(ob[9].toString());
			if (ob[10] != null)
				obp.setChargeName(ob[10].toString());
			if (ob[11] != null) {
				obp.setIsSingle(ob[11].toString());
			}

			model.setModel(obp);
			List<RunJOpticketstep> list = stepremote
					.findByOperateCode(opticketCode);
			model.setList(list);
		} else
			model = null;
		return model;
	}

	public CheckBaseForPrint getBefCheckStepData(String opticketCode) {
		String[] names = this.getNames(opticketCode);
		CheckBaseForPrint model = new CheckBaseForPrint();
		String sql = "select\n" + "  t.opticket_code,\n"
				+ "  t.opticket_name,\n" + "  t.operate_task_name,\n"
				+ "  getspecialname(t.speciality_code),\n"
				+ "  to_char(t.start_time,'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "  to_char(t.plan_end_time,'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "  t.memo,\n" + "  getworkername(t.operatormans),\n"
				+ "  getworkername(t.protectormans),\n"
				+ "  getworkername(t.class_leader),\n"
				+ "  getworkername(t.charge_by),\n" + "  t.is_single\n"
				+ "  from run_j_opticket t\n" + " where t.opticket_code = '"
				+ opticketCode + "'\n" + "   and t.is_use = 'Y'";
		Object[] ob = (Object[]) bll.getSingal(sql);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		if (ob != null) {
			OpticketBaseForPrint obp = new OpticketBaseForPrint();
			obp.setOpticketCode(ob[0].toString());
			if (ob[1] != null) {
				obp.setOpticketName(ob[1].toString());
				obp.setOperateTaskName(ob[1].toString());
			}

			// if (ob[2] != null)
			// obp.setOperateTaskName(ob[2].toString());
			if (ob[3] != null)
				obp.setSpecialityName(ob[3].toString());
			if (ob[4] != null) {
				try {
					obp.setStartTime(sf.format(df.parse(ob[4].toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (ob[5] != null) {
				try {
					obp.setEndTime(sf.format(df.parse(ob[5].toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (ob[6] != null)
				obp.setMemo(ob[6].toString());
			if (names[0] != null)
				obp.setOperatorName(names[0]);
			if (names[1] != null)
				obp.setProtectorName(names[1]);
			// if (ob[7] != null)
			// obp.setOperatorName(ob[7].toString());
			// if (ob[8] != null)
			// obp.setProtectorName(ob[8].toString());
			if (ob[9] != null)
				obp.setClassLeaderName(ob[9].toString());
			if (ob[10] != null)
				obp.setChargeName(ob[10].toString());
			if (ob[11] != null) {
				if (ob[11].toString().equals("Y")) {
					obp.setIsSingle("单人操作");
				} else if (ob[11].toString().equals("N")) {
					obp.setIsSingle("监护操作");
				} else {
					obp.setIsSingle(ob[11].toString());
				}
			}
			model.setModel(obp);
			List<RunJOpStepcheck> list = checkremote
					.findByOpticketCode(opticketCode);
			model.setList(list);
		} else
			model = null;
		return model;
	}

	public WorkBaseForPrint getBefWorkData(String opticketCode) {
		String[] names = this.getNames(opticketCode);
		WorkBaseForPrint model = new WorkBaseForPrint();
		String sql = "select\n" + "  t.opticket_code,\n"
				+ "  t.opticket_name,\n" + "  t.operate_task_name,\n"
				+ "  getspecialname(t.speciality_code),\n"
				+ "  to_char(t.start_time,'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "  to_char(t.plan_end_time,'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "  t.memo,\n" + "  getworkername(t.operatormans),\n"
				+ "  getworkername(t.protectormans),\n"
				+ "  getworkername(t.class_leader),\n"
				+ "  getworkername(t.charge_by),\n" + "  t.is_single\n"
				+ "  from run_j_opticket t\n" + " where t.opticket_code = '"
				+ opticketCode + "'\n" + "   and t.is_use = 'Y'";
		Object[] ob = (Object[]) bll.getSingal(sql);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		if (ob != null) {
			OpticketBaseForPrint obp = new OpticketBaseForPrint();
			if (ob[0] != null)
				obp.setOpticketCode(ob[0].toString());
			if (ob[1] != null) {
				obp.setOpticketName(ob[1].toString());
				obp.setOperateTaskName(ob[1].toString());
			}

			// if (ob[2] != null)
			// obp.setOperateTaskName(ob[2].toString());
			if (ob[3] != null)
				obp.setSpecialityName(ob[3].toString());
			if (ob[4] != null) {
				try {
					obp.setStartTime(sf.format(df.parse(ob[4].toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (ob[5] != null) {
				try {
					obp.setEndTime(sf.format(df.parse(ob[5].toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (ob[6] != null)
				obp.setMemo(ob[6].toString());
			if (names[0] != null)
				obp.setOperatorName(names[0]);
			if (names[1] != null)
				obp.setProtectorName(names[1]);
			// if (ob[7] != null)
			// obp.setOperatorName(ob[7].toString());
			// if (ob[8] != null)
			// obp.setProtectorName(ob[8].toString());
			if (ob[9] != null)
				obp.setClassLeaderName(ob[9].toString());
			if (ob[10] != null)
				obp.setChargeName(ob[10].toString());
			if (ob[11] != null) {
				if (ob[11].toString().equals("Y")) {
					obp.setIsSingle("单人操作");
				} else if (ob[11].toString().equals("N")) {
					obp.setIsSingle("监护操作");
				} else {
					obp.setIsSingle(ob[11].toString());
				}
			}
			model.setModel(obp);
			List<RunJOpStepcheck> temp = checkremote
					.findByOpticketCode(opticketCode);
			if (temp != null) {
				List<RunJOpFinwork> list = new ArrayList<RunJOpFinwork>();
				for (RunJOpStepcheck t : temp) {
					RunJOpFinwork f = new RunJOpFinwork();
					f.setFinishWorkId(t.getStepCheckId());
					f.setOpticketCode(t.getOpticketCode());
					f.setFinishWorkName(t.getStepCheckName());
					f.setRunAddFlag(t.getRunAddFlag());
					f.setCheckStatus(t.getCheckStatus());
					f.setCheckMan(t.getCheckMan());
					f.setDisplayNo(t.getDisplayNo());
					f.setMemo(t.getMemo());
					list.add(f);
				}
				model.setList(list);
			}
			// List<RunJOpFinwork> list = workremote
			// .findByOpticketCode(opticketCode);
			// model.setList(list);
		} else
			model = null;
		return model;
	}

	public WorkBaseForPrint getAftWorkData(String opticketCode) {
		String[] names = this.getNames(opticketCode);
		WorkBaseForPrint model = new WorkBaseForPrint();
		String sql = "select\n" + "  t.opticket_code,\n"
				+ "  t.opticket_name,\n" + "  t.operate_task_name,\n"
				+ "  getspecialname(t.speciality_code),\n"
				+ "  to_char(t.plan_start_time,'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "  to_char(t.plan_end_time,'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "  t.memo,\n" + "  getworkername(t.operatormans),\n"
				+ "  getworkername(t.protectormans),\n"
				+ "  getworkername(t.class_leader),\n"
				+ "  getworkername(t.charge_by),\n" + "  t.is_single\n"
				+ "  from run_j_opticket t\n" + " where t.opticket_code = '"
				+ opticketCode + "'\n" + "   and t.is_use = 'Y'";
		Object[] ob = (Object[]) bll.getSingal(sql);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		if (ob != null) {
			OpticketBaseForPrint obp = new OpticketBaseForPrint();
			if (ob[0] != null)
				obp.setOpticketCode(ob[0].toString());
			if (ob[1] != null) {
				obp.setOpticketName(ob[1].toString());
				obp.setOperateTaskName(ob[1].toString());
			}

			// if (ob[2] != null)
			// obp.setOperateTaskName(ob[2].toString());
			if (ob[3] != null)
				obp.setSpecialityName(ob[3].toString());
			if (ob[4] != null) {
				try {
					obp.setStartTime(sf.format(df.parse(ob[4].toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (ob[5] != null) {
				try {
					obp.setEndTime(sf.format(df.parse(ob[5].toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (ob[6] != null)
				obp.setMemo(ob[6].toString());
			if (names[0] != null)
				obp.setOperatorName(names[0]);
			if (names[1] != null)
				obp.setProtectorName(names[1]);
			// if (ob[7] != null)
			// obp.setOperatorName(ob[7].toString());
			// if (ob[8] != null)
			// obp.setProtectorName(ob[8].toString());
			if (ob[9] != null)
				obp.setClassLeaderName(ob[9].toString());
			if (ob[10] != null)
				obp.setChargeName(ob[10].toString());
			if (ob[11] != null) {
				if (ob[11].toString().equals("Y")) {
					obp.setIsSingle("单人操作");
				} else if (ob[11].toString().equals("N")) {
					obp.setIsSingle("监护操作");
				} else {
					obp.setIsSingle(ob[11].toString());
				}
			}
			model.setModel(obp);
			List<RunJOpFinwork> list = workremote
					.findByOpticketCode(opticketCode);
			model.setList(list);
		} else
			model = null;
		return model;
	}

	public DangerousBaseForPrint getDangeroursData(String opticketCode) {
		String[] names = this.getNames(opticketCode);
		DangerousBaseForPrint model = new DangerousBaseForPrint();
		String sql = "select\n" + "  t.opticket_code,\n"
				+ "  t.opticket_name,\n" + "  t.operate_task_name,\n"
				+ "  getspecialname(t.speciality_code),\n"
				+ "  to_char(t.start_time,'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "  to_char(t.plan_end_time,'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "  t.memo,\n" + "  getworkername(t.operatormans),\n"
				+ "  getworkername(t.protectormans),\n"
				+ "  getworkername(t.class_leader),\n"
				+ "  getworkername(t.charge_by),\n" + "  t.is_single\n"
				+ "  from run_j_opticket t\n" + " where t.opticket_code = '"
				+ opticketCode + "'\n" + "   and t.is_use = 'Y'";
		Object[] ob = (Object[]) bll.getSingal(sql);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		if (ob != null) {
			OpticketBaseForPrint obp = new OpticketBaseForPrint();
			if (ob[0] != null)
				obp.setOpticketCode(ob[0].toString());
			if (ob[1] != null) {
				obp.setOpticketName(ob[1].toString());
				obp.setOperateTaskName(ob[1].toString());
			}

			// if (ob[2] != null)
			// obp.setOperateTaskName(ob[2].toString());
			if (ob[3] != null)
				obp.setSpecialityName(ob[3].toString());
			if (ob[4] != null) {
				try {
					obp.setStartTime(sf.format(df.parse(ob[4].toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (ob[5] != null) {
				try {
					obp.setEndTime(sf.format(df.parse(ob[5].toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (ob[6] != null)
				obp.setMemo(ob[6].toString());
			if (names[0] != null)
				obp.setOperatorName(names[0]);
			if (names[1] != null)
				obp.setProtectorName(names[1]);
			// if (ob[7] != null)
			// obp.setOperatorName(ob[7].toString());
			// if (ob[8] != null)
			// obp.setProtectorName(ob[8].toString());
			if (ob[9] != null)
				obp.setClassLeaderName(ob[9].toString());
			if (ob[10] != null)
				obp.setChargeName(ob[10].toString());
			if (ob[11] != null) {
				obp.setIsSingle(ob[11].toString());
			}

			model.setModel(obp);
			List<RunJOpMeasures> list = dangerremote
					.findByOpticketCode(opticketCode);
			model.setList(list);
		} else
			model = null;
		return model;
	}

	private String[] getNames(String opticketCode) {
		List<RunJOpticketstep> steps = stepremote
				.findByOperateCode(opticketCode);
		String wCodes = "";
		String pCodes = "";
		String[] names = new String[2];
		if (steps != null) {
			for (RunJOpticketstep step : steps) {
				if (step.getExecMan() != null)
					if (wCodes.indexOf(step.getExecMan()) == -1) {
						wCodes += step.getExecMan();
						wCodes += ",";
					}
				if (step.getProMan() != null)
					if (pCodes.indexOf(step.getProMan()) == -1) {
						pCodes += step.getProMan();
						pCodes += ",";
					}
			}
			RunJOpticket op = remote.findById(opticketCode);
			if (op != null) {
				if (op.getOperatorMan() != null)
					if (wCodes.indexOf(op.getOperatorMan()) == -1) {
						wCodes += op.getOperatorMan();
						wCodes += ",";
					}
				if (op.getProtectorMan() != null)
					if (pCodes.indexOf(op.getProtectorMan()) == -1) {
						pCodes += op.getProtectorMan();
						pCodes += ",";
					}
			}
			if (wCodes.length() > 0)
				wCodes = wCodes.substring(0, wCodes.length() - 1);
			if (pCodes.length() > 0)
				pCodes = pCodes.substring(0, pCodes.length() - 1);
			String[] code_1 = wCodes.split(",");
			String[] code_2 = pCodes.split(",");
			String name_1 = "";
			String name_2 = "";
			if (code_1.length > 0)
				for (String s : code_1) {
					if (!"".equals(s)) {
						name_1 += bll.getSingal(
								"select getworkername('" + s + "')"
										+ " from dual").toString();
						name_1 += "、";
					}
				}
			if (code_2.length > 0)
				for (String s : code_2) {
					if (!"".equals(s)) {
						name_2 += bll.getSingal(
								"select getworkername('" + s + "')"
										+ " from dual").toString();
						name_2 += "、";
					}
				}
			if (name_1.length() > 0) {
				names[0] = name_1.substring(0, name_1.length() - 1);
			}
			if (name_2.length() > 0) {
				names[1] = name_2.substring(0, name_2.length() - 1);
			}
		}
		return names;
	}
}
