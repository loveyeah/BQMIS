package power.ejb.manage.plan.trainplan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import org.omg.RTCORBA.minPriority;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;

@Stateless
public class TrainPlanSignManagerImpl implements TrainPlanSignManager {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	protected TrainPlanManager planMain;
	WorkflowService service;

	public TrainPlanSignManagerImpl() {
		service = new WorkflowServiceImpl();
		planMain = (TrainPlanManager) Ejb3Factory.getInstance()
				.getFacadeRemote("TrainPlanManagerImpl");
	}

	public void TrainPlanReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType) {
		BpJTrainingMain model = planMain.findById(mainId);
		Long entryId;
		if (model.getWorkflowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, mainId
					.toString());
			model.setWorkflowNo(entryId);
		} else {
			entryId = model.getWorkflowNo();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null, "",
				nextRoles);
		model.setWorkflowStatus(1l);
		model.setReportBy(workerCode);
		model.setReportTime(new Date());
		planMain.update(model);

	}

	public void TrainPlanGatherReport(Long gatherId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType) {
		BpJTrainingGather model = planMain.findByGatherId(gatherId);
		Long entryId;
		if (model.getWorkflowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, gatherId
					.toString());
			model.setWorkflowNo(entryId);
		} else {
			entryId = model.getWorkflowNo();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		model.setWorkflowStatus(1l);
		planMain.update(model);
	}

	public void TrainPlanApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles) {
		// modified by liuyi 20100427
		// BpJTrainingMain model = planMain.findById(mainId);
		// service.doAction(entryId, workerCode, actionId, approveText, null,
		// nextRoles, "");
		// if (actionId == 42l) {
		// model.setWorkflowStatus(3l);
		// } else if (actionId == 43l) {
		// model.setWorkflowStatus(2l);
		// }
		// planMain.update(model);

		BpJTrainingMain model = planMain.findById(mainId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if (model.getTrainingMonth() != null) {
			String month = sdf.format(model.getTrainingMonth());

			String sqlMain = "select t.* from bp_j_training_main t where to_char(t.training_month,'yyyy-mm')='"
					+ month
					+ "' and t.training_dep='"
					+ model.getTrainingDep()
					+ "'   and t.workflow_status=" + model.getWorkflowStatus();
			List<BpJTrainingMain> mainList = bll.queryByNativeSQL(sqlMain,
					BpJTrainingMain.class);
			for (BpJTrainingMain main : mainList) {
				Long mainEntryId = main.getWorkflowNo();
				service.doAction(mainEntryId, workerCode, actionId,
						approveText, null, nextRoles, "");
			}
			// service.doAction(entryId, workerCode, actionId, approveText,
			// null,
			// nextRoles, "");
			Long workflowStatus = null;
			// 0,'未上报',1,'审批中',2,'审批已通过',3,'审批退回 4部门培训员已汇总
			if (actionId == 54l) {
				workflowStatus = 4l; //
			} else if (actionId == 52l) {
				workflowStatus = 3l;
			} else if (actionId == 43l) {
				workflowStatus = 2l;
			} else if (actionId == 45l) {
				workflowStatus = 3l;
			}
			String sql = "update BP_J_TRAINING_MAIN a set a.workflow_status="
					+ workflowStatus
					+ " where to_char(a.training_month,'yyyy-mm')='" + month
					+ "'\n" + "and a.training_dep='" + model.getTrainingDep()
					+ "'   and a.workflow_status=" + model.getWorkflowStatus();
			bll.exeNativeSQL(sql);
		}

	}

	public void TrainPlanGatherApprove(Long gatherId, Long actionId,
			Long entryId, String workerCode, String approveText,
			String nextRoles) {
		BpJTrainingGather model = planMain.findByGatherId(gatherId);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 52l || actionId == 42l) {
			model.setWorkflowStatus(3l);
		} else if (actionId == 53l) {
			model.setWorkflowStatus(2l);
		}
		planMain.update(model);
	}

	public void TrainPlanBackReport(Long mainId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType) {
		BpJTrainingMain model = planMain.findById(mainId);
		Long entryId;
		if (model.getBackfillWorkflowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, mainId
					.toString());
			model.setBackfillWorkflowNo(entryId);
		} else {
			entryId = model.getBackfillWorkflowNo();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null, "",
				nextRoles);
		// modified by liuyi 20100504 多条记录公用一个工作流
		// model.setBackfillWorkflowStatus(1l);
		// model.setBackfillBy(workerCode);
		// model.setBackfillDate(new Date());
		// planMain.update(model);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentString = sdf.format(new Date());
		SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
		String sql = "update BP_J_TRAINING_MAIN a set a.backfill_workflow_no='"
				+ entryId + "',a.backfill_workflow_status='1',a.backfill_by='"
				+ workerCode
				+ "'\n"
				+ ",a.backfill_date=to_date('"
				+ currentString
				+ "','yyyy-mm-dd')\n"
				+ "where  to_char(a.training_month,'yyyy-mm')='"
				+ sdfMonth.format(model.getTrainingMonth())
				+ "' and a.training_dep='"
				+ model.getTrainingDep()
				+ "'"
				+ " and a.report_By='"
				+ model.getReportBy()
				+ "'"
				// add by sychen 20100518
				+ " and (a.backfill_workflow_status=0 or a.backfill_workflow_status=3)";

		bll.exeNativeSQL(sql);

	}

	@SuppressWarnings("unchecked")
	public void TrainPlanBackApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles) {
		// BpJTrainingMain model = planMain.findById(mainId);
		// service.doAction(entryId, workerCode, actionId, approveText, null,
		// nextRoles, "");
		// // modified by liuyi 20100505 多条数据公用一个工作流及状态
		// Long backfillWorkflowStatus = 3l;
		// // if (actionId == 42l) {
		// // model.setBackfillWorkflowStatus(3l);
		// // } else if (actionId == 43l) {
		// // model.setBackfillWorkflowStatus(2l);
		// // }
		// // planMain.update(model);
		//		
		// //update by sychen 20100518
		// if (actionId == 54l) {
		// backfillWorkflowStatus = 4l;
		// } else if (actionId == 52l) {
		// backfillWorkflowStatus = 3l;
		// }else if (actionId == 43l) {
		// backfillWorkflowStatus = 2l;
		// }else if (actionId == 45l) {
		// backfillWorkflowStatus = 3l;
		// }
		//		
		// // if (actionId == 42l) {
		// // ;
		// // } else if (actionId == 43l) {
		// // backfillWorkflowStatus = 2l;
		// // }
		// SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
		// SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
		// String sql =
		// "update bp_j_training_main a\n" +
		// " set a.backfill_workflow_status = "+backfillWorkflowStatus+"\n" +
		// //" a.backfill_by = '"+workerCode+"',\n" +
		// //" a.backfill_date = to_date('"+sdfDay.format(new Date())+"',
		// 'yyyy-mm-dd')\n" +
		// " where a.training_dep = '"+model.getTrainingDep()+"'\n" +
		// " and to_char(a.training_month, 'yyyy-mm') =
		// '"+sdfMonth.format(model.getTrainingMonth())+"'";
		// bll.exeNativeSQL(sql);

		// update by ltong 20100603 多条一起审批
		BpJTrainingMain model = planMain.findById(mainId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if (model.getTrainingMonth() != null) {
			String month = sdf.format(model.getTrainingMonth());

			String sqlMain = "select t.* from bp_j_training_main t where to_char(t.training_month,'yyyy-mm')='"
					+ month
					+ "' and t.training_dep='"
					+ model.getTrainingDep()
					+ "'   and t.backfill_workflow_status="
					+ model.getBackfillWorkflowStatus();
			List<BpJTrainingMain> mainList = bll.queryByNativeSQL(sqlMain,
					BpJTrainingMain.class);
			for (BpJTrainingMain main : mainList) {
				Long mainEntryId = main.getBackfillWorkflowNo();
				service.doAction(mainEntryId, workerCode, actionId,
						approveText, null, nextRoles, "");
			}
			Long backfillWorkflowStatus = 3l;
			// 0,'未上报',1,'审批中',2,'审批已通过',3,'审批退回 4部门培训员已汇总
			if (actionId == 54l) {
				backfillWorkflowStatus = 4l;
			} else if (actionId == 52l) {
				backfillWorkflowStatus = 3l;
			} else if (actionId == 43l) {
				backfillWorkflowStatus = 2l;
			} else if (actionId == 45l) {
				backfillWorkflowStatus = 3l;
			}
			String sql = "update BP_J_TRAINING_MAIN a set a.backfill_workflow_status="
					+ backfillWorkflowStatus
					+ " where to_char(a.training_month,'yyyy-mm')='"
					+ month
					+ "'\n"
					+ "and a.training_dep='"
					+ model.getTrainingDep()
					+ "'   and a.backfill_workflow_status="
					+ model.getBackfillWorkflowStatus();
			bll.exeNativeSQL(sql);
		}

	}

	public void TrainPlanBackGatherReport(Long approveId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType) {

		BpJTrainingSumApproval model = entityManager.find(
				BpJTrainingSumApproval.class, approveId);

		Long entryId;
		if (model.getWorkflowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, approveId
					.toString());
			model.setWorkflowNo(entryId);
		} else {
			entryId = model.getWorkflowNo();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		model.setWorkflowStatus(1L);
		entityManager.merge(model);
	}

	public void trainPlanBackGatherApprove(Long approvalId, Long actionId,
			Long entryId, String workerCode, String approveText,
			String nextRoles) {
		BpJTrainingSumApproval model = entityManager.find(
				BpJTrainingSumApproval.class, approvalId);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 52l || actionId == 42l) {
			model.setWorkflowStatus(3l);
		} else if (actionId == 53l) {
			model.setWorkflowStatus(2l);
		}
		entityManager.merge(model);
	}
}
