package com.opensymphony.workflow.action;

import com.opensymphony.db.DBHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import power.ear.comm.Employee;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.engineassistant.ext.WorkflowStepControl;
import com.opensymphony.engineassistant.ext.WorkflowStepControlImpl;
import com.opensymphony.engineassistant.po.Group;
import com.opensymphony.engineassistant.po.Process;
import com.opensymphony.engineassistant.po.WfJHistoryoperation;
import com.opensymphony.engineassistant.po.WorkflowActivity;
import com.opensymphony.engineassistant.util.FileNotFoundException;
import com.opensymphony.engineassistant.util.RightControl;
import com.opensymphony.engineassistant.util.RightControlImpl;
import com.opensymphony.engineassistant.util.RunFlowFileInfo;
import com.opensymphony.engineassistant.util.RunFlowFileInfoJdbcImpl;
import com.opensymphony.engineassistant.util.WorkflowFileControll;
import com.opensymphony.engineassistant.util.WorkflowFileControllImpl;
import com.opensymphony.engineassistant.util.WorkflowHistoryControll;
import com.opensymphony.engineassistant.util.WorkflowHistoryControllImpl;
import com.opensymphony.engineassistant.util.WorkflowUtil;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

/**
 * 流程维护
 * 
 * @author wzhyan
 */
public class FlowFileManager extends HttpServlet {
	private static final long serialVersionUID = -39137263662689248L;
	static Logger log = Logger.getLogger(FlowFileManager.class);
	protected WorkflowFileControll dao;
	protected RightControl rightControl;
	protected WorkflowStepControl stepControl;
	private String baseClassDir;
	private String baseWebDir;

	public void init() throws ServletException {
		dao = new WorkflowFileControllImpl();
		rightControl = new RightControlImpl();
		stepControl = new WorkflowStepControlImpl();
		baseClassDir = FlowFileManager.class.getResource("/").toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		baseWebDir = "http://" + request.getServerName() + ":"
				+ request.getServerPort() + "/" + request.getContextPath()
				+ "/";
		String action = request.getParameter("action");
		String xml = request.getParameter("xml");
		String flowType = request.getParameter("flowType");
		HttpSession session = request.getSession();
		// String workerCode = (String)session.getAttribute("workerCode");

		// 取得当前审批列表

		if ("doAction".equals(action)) {
			try {
				String nrs = request.getParameter("nrs");
				String nps = request.getParameter("nps");
				String entryId = request.getParameter("entryId");
				String actionId = request.getParameter("actionId");
				String opinion = request.getParameter("opinion");
				String jsonArgs = request.getParameter("jsonArgs");
				String workerCode = request.getParameter("workerCode");
				if (entryId == null || actionId == null || workerCode == null) {
					this.writeHtml(response, "[]");
					return;
					// throw new RuntimeException("实例编号,执行动作编号,审批人员工号均不能为空!");
				}
				Map args = null;
				if (jsonArgs != null && !"".equals(jsonArgs)) {
					args = (Map) JSONUtil.deserialize(jsonArgs);
				}
				WorkflowService service = new WorkflowServiceImpl();
				service.doAction(Long.parseLong(entryId), workerCode, Long
						.parseLong(actionId), opinion, args, nrs, nps);
			} catch (Exception exc) {
				throw new RuntimeException(exc.getMessage());
			}
		} else if ("getFirstStep".equals(action)) {
			try {
				String flowCode = request.getParameter("flowCode");
				String jsonArgs = request.getParameter("jsonArgs");
				if (flowCode == null || "".equals(flowCode)) {
					log.error("取流程第一步的时候没有传入实例号");
					return;
				}
				Map args = null;
				if (jsonArgs != null && !"".equals(jsonArgs)) {
					try {
						args = (Map) JSONUtil.deserialize(jsonArgs);
					} catch (Exception exc) {
						log.error("取流程第一步的时候,json参数格式不正确");
					}
				}
				WorkflowService service = new WorkflowServiceImpl();
				WorkflowActivity step = service.findFirstStep(flowCode, args);
				this.writeHtml(response, JSONUtil.serialize(step));
			} catch (Exception exc) {
				throw new RuntimeException(exc.getMessage());
			}

		} else if ("getNextSteps".equals(action)) {
			RunFlowFileInfo wfControl = new RunFlowFileInfoJdbcImpl(null);
			String entryId = request.getParameter("entryId");
			String actionId = request.getParameter("actionId");
			if (entryId == null || actionId == null) {
				this.writeHtml(response, "[]");
				log.error("取得下一步信息时,实例号没有传入");
				return;
			}
			List<WorkflowActivity> steps = wfControl.getNextSteps(Long
					.parseLong(entryId), Long.parseLong(actionId));
			if (steps != null) {
				try {
					this.writeHtml(response, JSONUtil.serialize(steps));
				} catch (JSONException e) {
					e.printStackTrace();
					this.writeHtml(response, "[]");
				}
			} else {
				this.writeHtml(response, "[]");
			}
		} else if ("getCurrentSteps".equals(action)) {
			try {
				String entryId = request.getParameter("entryId");
				String workerCode = request.getParameter("workerCode");
				// 此处与标准测试的有差别
				if (workerCode == null || "".equals(workerCode.trim())) {
					Employee employee = (Employee) request.getSession()
							.getAttribute("employee");
					workerCode = employee.getWorkerCode();
				}
				String jsonArgs = request.getParameter("jsonArgs");
				if (entryId == null || workerCode == null) {
					this.writeHtml(response, "[]");
					return;
				}
				Map args = null;
				if (jsonArgs != null && !"".equals(jsonArgs)) {
					args = (Map) JSONUtil.deserialize(jsonArgs);
				}
				WorkflowService service = new WorkflowServiceImpl();
				List<WorkflowActivity> steps = service.findCurrentSteps(Long
						.parseLong(entryId), workerCode, args);
				if (steps != null && steps.size() > 0) {
					// System.out.println(JSONUtil.serialize(steps));
					this.writeHtml(response, JSONUtil.serialize(steps));
				} else {
					this.writeHtml(response, "[]");
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				this.writeHtml(response, "[]");
			}
		}
		// 当前实例状态信息
		else if ("getCurrentStepsInfo".equals(action)) {
			try {
				String entryId = request.getParameter("entryId");
				if (entryId == null || entryId.equals("")) {
					log.error("没有传入实体号!");
					return;
				}
				WorkflowService service = new WorkflowServiceImpl();
				String workerCode = request.getParameter("workerCode");
				List<WorkflowActivity> list = service.getCurrentStepsInfo(Long
						.parseLong(entryId), workerCode);
				if (list != null) {
					this.writeHtml(response, JSONUtil.serialize(list));
				} else {
					this.writeHtml(response, "[]");
				}
			} catch (Exception exc) {
				log.error(exc.getMessage());
				this.writeHtml(response, "[]");
			}

		}
		// 载入运行文件
		else if ("loadrunxml".equals(action)) {
			String strXml = "";
			try {
				strXml = dao.loadFlowRunFile(flowType);
			} catch (Exception e) {
				log.debug(e.getMessage());
				// throw new
				// RuntimeException("没有找到编码为：["+flowType+"]的运行xml文件!");
			}
			this.writeXml(response, strXml);
		}
		// 加载流程定义文件
		else if ("load".equals(action)) {
			String strXml = "";
			try {
				strXml = dao.loadFlowFile(flowType);
			} catch (Exception e) {
				log.debug(e.getMessage());
				// throw new
				// RuntimeException("没有找到编码为：["+flowType+"]的定义xml文件!");
			}
			this.writeHtml(response, strXml);

		}
		// 加载最新版本流程定义文件
		else if ("loadCurrentVersion".equals(action)) {
			String flowCode = request.getParameter("flowCode");
			if (flowCode != null) {
				String workflowType = dao.getWorkflowType(flowCode);
				if (workflowType != null) {
					String strXml = "";
					try {
						strXml = dao.loadFlowFile(workflowType);
					} catch (Exception e) {
						log.debug(e.getMessage());
						// throw new
						// RuntimeException("没有找到编码为：["+flowType+"]的定义xml文件!");
					}
					this.writeHtml(response, strXml);
				}
			}
		}
		// ////////////查询状态///////////
		else if ("loadByEntryId".equals(action)) {
			String entryId = request.getParameter("entryId");
			String strXml = "";
			try {
				strXml = dao.loadFlowFileByEntryId(Long.parseLong(entryId));
			} catch (FileNotFoundException e) {
				throw new RuntimeException("没有找到编码为：[" + flowType
						+ "]的定义xml文件!");
			}
			this.writeHtml(response, strXml);
		} else if ("getEntryInfo".equals(action)) {
			try {
				String entryId = request.getParameter("entryId");
				String[] info = dao.loadEntryRunInfo(Long.parseLong(entryId));
				this.writeHtml(response, "{passStepIds:'" + info[0]
						+ "',currentStepId:'" + info[1] + "'}");
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		// ////////////////////////////
		// 修改流程文件
		else if ("update".equals(action)) {
			dao.updateFlowFile("999999", baseClassDir, xml);
		}
		// 增加流程文件
		else if (("add").equals(action)) {
			dao.addFlowFile("999999", baseClassDir, xml);
		}
		// 取得工作流列表
		else if ("workflowlist".equals(action)) {
			List<Process> list = dao.getWorkflowList();
			String str = "[]";
			if (list != null && list.size() > 0) {
				try {
					str = JSONUtil.serialize(list);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			this.writeHtml(response, str);
		} else if ("getApproveList".equals(action)) {
			String entryId = request.getParameter("entryId");
			WorkflowHistoryControll hisControl = new WorkflowHistoryControllImpl();
			List<WfJHistoryoperation> list = hisControl.getApproveOpinion(Long
					.parseLong(entryId));
			String str = "";
			try {
				str = JSONUtil.serialize(list);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			this.writeHtml(response, str);
		}

		else if ("waitGroup".equals(action)) {
			Long stepId = Long.parseLong(request.getParameter("stepId"));
			List<Group> list = rightControl.getAvailableGroup(flowType, stepId);
			String str = "[]";
			if (list != null || list.size() > 0) {
				try {
					str = JSONUtil.serialize(list);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			this.writeHtml(response, str);
		} else if ("alreadyGroup".equals(action)) {
			Long stepId = Long.parseLong(request.getParameter("stepId"));
			List<Group> list = rightControl.getGroups(flowType, stepId);
			String str = "[]";
			if (list != null && list.size() > 0) {
				try {
					str = JSONUtil.serialize(list);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			this.writeHtml(response, str);
		} else if ("getEntryStepGroup".equals(action)) {
			Long entryId = Long.parseLong(request.getParameter("entryId"));
			Long stepId = Long.parseLong(request.getParameter("stepId"));
			List<Group> list = rightControl.getGroups(entryId, stepId);
			String str = "[]";
			if (list != null && list.size() > 0) {
				try {
					str = JSONUtil.serialize(list);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			this.writeHtml(response, str);
		} else if ("getFirstNextStepGroups".equals(action)) {
			String flowCode = request.getParameter("flowCode");
			String actionIdStr = request.getParameter("actionId");
			String deptId = request.getParameter("deptId");
			String sepeciality = request.getParameter("sepeciality");
			String authorizeDeptId =request.getParameter("authorizeDeptId");//add by sychen 20100726
			
			if (this.isNullOrEmptyStr(flowCode)
					|| this.isNullOrEmptyStr(actionIdStr)
					) {
				log.debug("流程初始化时取得下一步状态列表,时流程编码,执行动作均不能为空");
				this.writeHtml(response, "[]");
			} else {

				List<Map<String, Object>> result;
				Long actionId = Long.parseLong(actionIdStr);
				NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
						.getInstance().getFacadeRemote("NativeSqlHelper");
				List<Group> list = null;
				String sql = "select distinct f.role_id, f.role_name, f.accredit_section\n"
						+ "  from wf_c_type a, wf_c_step b, wf_c_event c, wf_c_step d,wf_j_rrs e,sys_c_roles f\n"
						+ " where a.flow_type = b.flow_type\n"
						+ "   and a.flow_type = c.flow_type\n"
						+ "   and a.flow_type = d.flow_type\n"
						+ "   and a.flow_type = e.flow_type\n"
						+ "   and b.step_id = c.from_step_id\n"
						+ "   and c.to_step_id = d.step_id\n"
						+ "   and d.step_id=e.step_id\n"
						+ "   and e.role_id = f.role_id\n"
						+ "   and f.is_use='Y'\n"
						+ "   and a.is_use = 'Y'\n"
						+ "   and a.flow_code = ?\n"
						+ "   and b.step_type = ?\n"
						+ "   and c.event_id = ?\n";
				if (deptId != null && !"".equals(deptId)) {
					sql += "and ( f.accredit_section = '' or   f.accredit_section is null or\n"
							+ " instr(  (select  sys_connect_by_path(dept_id,',')||','   path\n"
							+ "     from hr_c_dept\n"
							+ "     where  dept_id=?\n"
							+ "     start   with   pdept_id=-1\n"
							+ "     connect   by   prior   dept_id=pdept_id and is_use='Y'),','||f.accredit_section||',')<>0)"; //update by sychen 20100902
//					+ "     connect   by   prior   dept_id=pdept_id and is_use='U'),','||f.accredit_section||',')<>0)"; 
					result = DBHelper.query(sql,
							new Object[] { flowCode,
									WorkflowUtil.WORKFLOW_START_STEP, actionId,
									deptId });
				}
				//add by sychen 20100726
				else if (authorizeDeptId != null && !"".equals(authorizeDeptId)) {
					sql += "and ( f.accredit_section = '' or   f.accredit_section is null  or \n"
							+ "GETFirstLevelBYID(f.accredit_section) =\n" +
                               " GETFirstLevelBYID(?))";

					result = DBHelper.query(sql,
							new Object[] { flowCode,
									WorkflowUtil.WORKFLOW_START_STEP, actionId,
									authorizeDeptId });
				}
				//add by sychen 20100726 end
				else {
					result = DBHelper.query(sql, new Object[] { flowCode,
							WorkflowUtil.WORKFLOW_START_STEP, actionId });
				}
			
				
				if(sepeciality != null && !"".equals(sepeciality))
				{
					sql += " and f.role_name like  '%"+ sepeciality +"%'\n";
					result = DBHelper.query(sql, new Object[] { flowCode,
							WorkflowUtil.WORKFLOW_START_STEP, actionId});
				}
				if (result != null && result.size() > 0) {
					list = new ArrayList<Group>();
					for (int i = 0; i < result.size(); i++) {
						Group g = new Group();
						Map m = result.get(i);
						g.setGroupId(((BigDecimal) m.get("ROLE_ID"))
								.longValue());
						g.setGroupName(m.get("ROLE_NAME").toString());
						list.add(g);
					}
				}
				// List<Group> list =
				// rightControl.getFirstNextStepGroups(flowCode, actionId);
				String str = "[]";
				if (list != null && list.size() > 0) {
					try {
						str = JSONUtil.serialize(list);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				this.writeHtml(response, str);
			}
		} else if ("grantGroup".equals(action)) {
			Long stepId = Long.parseLong(request.getParameter("stepId"));
			String roleIds = request.getParameter("roleIds");
			rightControl.grantGroups(flowType, stepId, roleIds);
		} else if ("revokeGroup".equals(action)) {
			Long stepId = Long.parseLong(request.getParameter("stepId"));
			String roleIds = request.getParameter("roleIds");
			rightControl.revokeGroups(flowType, stepId, roleIds);
		} else if ("savewfbaseinfo".equals(action)) {
			Process process = new Process();
			process.setWorkflowType(request.getParameter("workflowType"));
			process.setName(request.getParameter("name"));
			process.setDesc(request.getParameter("desc"));
			process.setFlowListUrl(request.getParameter("flowListUrl"));
			process.setIsRunning(request.getParameter("isRunning"));
			process.setIsShowInMyJob(request.getParameter("isShowInMyJob"));
			process.setTableName(request.getParameter("tableName"));
			process.setTableKeyColName(request.getParameter("tableKeyColName"));
			process.setWfIdColName(request.getParameter("wfIdColName"));
			process.setWfStatusColName(request.getParameter("wfStatusColName"));
			String displayNo = request.getParameter("displayNo");
			if (displayNo != null && !"".equals(displayNo)) {
				process.setDisplayNo(Long.parseLong(displayNo));
			}
			dao.saveWorkflowBaseInfo(process);
		} else if ("addOrUpdateStepInfo".equals(action)) {
			Long stepId = Long.parseLong(request.getParameter("stepId"));
			Long timeLimit = Long.parseLong(request.getParameter("timeLimit"));
			stepControl.addOrUpdate(flowType, stepId, timeLimit);
		}

		else if ("getStepTimeLimit".equals(action)) {
			Long stepId = Long.parseLong(request.getParameter("stepId"));
			Object o = stepControl.getStepTimeLimit(flowType, stepId);
			this.writeHtml(response, o == null ? "" : o.toString());
		} else if ("mangerDelete".equals(action)) {
			/**
			 * 缺陷: "failure" 工作票: "workticket" 操作票: "opticket" 工作申请单:
			 * "workapply"
			 */
			String busiType = request.getParameter("busiType");
			String busiNo = request.getParameter("busiNo");
			String entryId = request.getParameter("entryId");
			if ("failure".equals(busiType)) {
				this.delFailure(busiNo, entryId);
			} else if ("workticket".equals(busiType)) {
				this.delWorkticket(busiNo, entryId);
			} else if ("opticket".equals(busiType)) {
				this.delOpticket(busiNo, entryId);
			} else if ("workapply".equals(busiType)) {
				this.delWorkapply(busiNo, entryId);
			} else if ("power".equals(busiType)) {
				this.delPower(busiNo, entryId);
			} else if ("protect".equals(busiType)) {
				this.delProtectioninoutApply(busiNo, entryId);
			}else if("equRepair".equals(busiType))
			{
				//add by fyyang 20100324
				this.delEquRepair(busiNo, entryId);
			}
		}
	}


	private void delFailure(String busiNo, String entryId) {
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
				.getInstance().getFacadeRemote("NativeSqlHelper");
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		sb
				.append("update EQU_J_FAILURES t set t.ISUSE='N',t.wo_status=4 where t.FAILURE_CODE='"
						+ busiNo + "';");//modified by ghzhou 2010-6-28删除的缺陷状态自动改为已消缺
		sb
				.append("update equ_j_failure_history  t set t.isuse='N' where t.failure_code='"
						+ busiNo + "';");
		if (entryId != null && !"".equals(entryId.trim())) {
			sb.append(getWfSql(Long.parseLong(entryId)));
		}
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
	}

	private void delWorkticket(String busiNo, String entryId) {
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
				.getInstance().getFacadeRemote("NativeSqlHelper");
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		sb
				.append("update run_j_worktickets t set t.is_use='N' where t.workticket_no='"
						+ busiNo + "';");
		if (entryId != null && !"".equals(entryId.trim())) {
			sb.append(getWfSql(Long.parseLong(entryId)));
		}
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
	}

	private void delOpticket(String busiNo, String entryId) {
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
				.getInstance().getFacadeRemote("NativeSqlHelper");
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		sb
				.append("update run_j_opticket t set t.is_use='N' where t.opticket_code='"
						+ busiNo + "';");
		if (entryId != null && !"".equals(entryId.trim())) {
			sb.append(getWfSql(Long.parseLong(entryId)));
		}
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
	}

	// writed by ghzhou 删除停送电联系单
	private void delPower(String busiNo, String entryId) {
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
				.getInstance().getFacadeRemote("NativeSqlHelper");
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		sb
				.append("update run_c_power_notice t set t.is_use='N' where t.notice_no='"
						+ busiNo + "';");
		if (entryId != null && !"".equals(entryId.trim())) {
			sb.append(getWfSql(Long.parseLong(entryId)));
		}
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
	}

	// writed by lwqi 删除保护投退
	private void delProtectioninoutApply(String busiNo, String entryId) {
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
				.getInstance().getFacadeRemote("NativeSqlHelper");
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		sb
				.append("update run_j_protectinoutapply t set t.is_use='N' where t.protect_no='"
						+ busiNo + "';");
		if (entryId != null && !"".equals(entryId.trim())) {
			sb.append(getWfSql(Long.parseLong(entryId)));
		}
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
	}

	private void delWorkapply(String busiNo, String entryId) {
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
				.getInstance().getFacadeRemote("NativeSqlHelper");
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		sb
				.append("update RUN_J_WORK_APPLY t set t.is_use='N' where t.apply_no='"
						+ busiNo + "';");

		String sql = "select ltrim(sys_connect_by_path(id, ','), ',') delay_ids,\n"
				+ "       ltrim(sys_connect_by_path(work_flow_no, ','), ',') entry_ids\n"
				+ "  from (select a.apply_no, a.work_flow_no, a.id, rownum numid\n"
				+ "          from run_j_work_apply_delay a\n"
				+ "         where a.apply_no = '"
				+ busiNo
				+ "' and a.is_use='Y')\n"
				+ " WHERE connect_by_isleaf = 1\n"
				+ " start with numid = 1\n"
				+ "connect by numid - 1 = prior numid\n"
				+ "       and apply_no = prior apply_no";
		List list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			String delayIds = "";
			String delayEntryIds = "";
			Object[] o = (Object[]) list.get(0);
			if (o[0] != null) {
				delayIds = o[0].toString();
				sb
						.append("update run_j_work_apply_delay b set b.is_use = 'N' where b.id in ("
								+ delayIds + ");");
			}
			if (o[1] != null) {
				delayEntryIds = o[1].toString();
				if (delayEntryIds.charAt(delayEntryIds.length() - 1) == ',') {
					delayEntryIds = delayEntryIds.substring(0, delayEntryIds
							.length() - 1);
				}
				sb
						.append("delete from wf_c_currentstep a where a.entry_id in ("
								+ delayEntryIds
								+ ");\n"
								+ "delete from wf_j_rrs_cr b where b.entry_id in ("
								+ delayEntryIds
								+ ");\n"
								+ "delete from wf_j_rrs_cp c where c.entry_id in ("
								+ delayEntryIds + ");\n");
			}
		}

		if (entryId != null && !"".equals(entryId.trim())) {
			sb.append(getWfSql(Long.parseLong(entryId)));
		}
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
	}
	
	
	
	private void delEquRepair(String id,String entryId)
	{
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance().getFacadeRemote("NativeSqlHelper");  
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		sb.append("update wo_j_workorder t set t.is_use='N'   where t.id="+id+";");
		if(entryId != null && !"".equals(entryId.trim()))
		{
			sb.append(getWfSql(Long.parseLong(entryId)));
		}
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
	}

	private String getWfSql(Long workflowNo) {
		String wfSql = "delete from wf_c_currentstep a where a.entry_id="
				+ workflowNo
				+ ";\n"
				+ "delete from wf_j_rrs_cr b where b.entry_id="
				+ workflowNo
				+ ";\n"
				+ "delete from wf_j_rrs_cp c where c.entry_id="
				+ workflowNo
				+ ";\n"
				+ "insert into wf_j_historyoperation(id,entry_id,step_id,step_name,action_id,action_name,caller,caller_name,opinion,opinion_time)\n"
				+ "values((select nvl(max(id),0)+1 from wf_j_historyoperation),"
				+ workflowNo
				+ ",-1,' ',-1,' ','999999','管理员','管理员删除',sysdate);";

		return wfSql;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	private boolean isInAvailableActions(int[] actions, int action) {
		for (int i : actions) {
			if (action == i)
				return true;
		}
		return false;
	}

	private void writeHtml(HttpServletResponse response, String str)
			throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		PrintWriter out = response.getWriter();
		out.write(str);
		out.close();
	}

	private void writeXml(HttpServletResponse response, String str)
			throws IOException {
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		PrintWriter out = response.getWriter();
		out.write(str);
		out.close();
	}

	private boolean isNullOrEmptyStr(String str) {
		if (str == null || "".equals(str))
			return true;
		return false;
	}

}
