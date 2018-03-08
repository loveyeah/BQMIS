package power.web.manage.contract.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.manage.contract.ConCConTypeFacadeRemote;
import power.ejb.manage.contract.business.ConJConDoc;
import power.ejb.manage.contract.business.ConJConDocFacadeRemote;
import power.ejb.manage.contract.business.ConJContractInfo;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.business.ContractApprove;
import power.ejb.manage.contract.business.ContractMaterial;
import power.ejb.manage.contract.form.ConDocForm;
import power.ejb.manage.contract.form.ContractFullInfo;
import power.ejb.manage.contract.form.MaterialDetailsForm;
import power.ejb.manage.plan.BpJPlanJobDepMain;
import power.ejb.manage.project.PrjCTypeFacadeRemote;
import power.ejb.resource.SysCCurrency;
import power.ejb.resource.SysCCurrencyFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

@SuppressWarnings("serial")
public class ConMeetSignAction extends AbstractAction {
	private ConJContractInfoFacadeRemote remote;
	private ConCConTypeFacadeRemote contyperemote;
	private ConJConDocFacadeRemote docremote;
	private HrJEmpInfoFacadeRemote eremote;
	private ContractApprove appremote;
	private ContractMaterial cmremote;
	private PrjCTypeFacadeRemote prjtyperemote;
	private String fuzzy;
	private String startdate;
	private String enddate;
	private ConJContractInfo con;
	private String method;
	private Long conid;
	private String type;
	private ConJConDoc doc;
	private Long docid;
	private String deptId;
	private File conFile;
	private File fjdocFile;

	// 乙方负责人
	private String secondcharge;
	// 附件id
	private Long conDocId;

	// 合同类型
	// private Long conTypeId;
	// private String filePath;
	public ConMeetSignAction() {
		remote = (ConJContractInfoFacadeRemote) factory
				.getFacadeRemote("ConJContractInfoFacade");
		eremote = (HrJEmpInfoFacadeRemote) factory
				.getFacadeRemote("HrJEmpInfoFacade");
		contyperemote = (ConCConTypeFacadeRemote) factory
				.getFacadeRemote("ConCConTypeFacade");
		docremote = (ConJConDocFacadeRemote) factory
				.getFacadeRemote("ConJConDocFacade");
		appremote = (ContractApprove) factory
				.getFacadeRemote("ContractApproveImp");
		cmremote = (ContractMaterial) factory
				.getFacadeRemote("ContractMaterialImp");
		prjtyperemote = (PrjCTypeFacadeRemote) factory
				.getFacadeRemote("PrjCTypeFacade");
	}

	// 获取session值
	public void getSessionInfo() throws JSONException {
		Object[] o = new Object[4];
		o[0] = employee.getWorkerCode();
		o[1] = employee.getWorkerName();
		if (employee.getDeptCode() != null) {
			o[2] = employee.getDeptCode();
		}
		if (employee.getDeptName() != null) {
			o[3] = employee.getDeptName();
		}
		write("{success:true,data:" + JSONUtil.serialize(o) + "}");
	}

	// 查询合同申请会签列表
	public void queryMeetConlist() throws JSONException {
		String status = "report";
		int start = 0;
		int limit = 99999999;
		Long conTypeId = null;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (request.getParameter("conTypeId") != null) {
			conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		}
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		String workCode = employee.getWorkerCode();
		if (workCode.equals("999999")) {
			workCode = "";
		}
		PageObject obj = remote.findReportContractList(workCode, conTypeId,
				employee.getEnterpriseCode(), startdate, enddate, fuzzy, null,
				null, status, null, start, limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"
				+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}

	// 查询合同会签审批列表
	public void queryApproveConlist() throws JSONException {
		String status = request.getParameter("status");
		// add bjxu -conTypeId
		Long conTypeId = 0l;
		if (request.getParameter("conTypeId") != null) {
			conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		}
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = "";
		if (conTypeId == 2) {
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqProContract" }, employee.getWorkerCode());
		} else {
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqCGContract" }, employee.getWorkerCode());
		}

		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj = remote.findReportContractList("", conTypeId, employee
				.getEnterpriseCode(), startdate, enddate, null, null, null,
				status, entryIds, start, limit);
		if (obj != null) {
			String str = "{total:" + obj.getTotalCount() + ",root:"
					+ JSONUtil.serialize(obj.getList()) + "}";
			write(str);
		} else {
			write("{total : 0,root :[]}");
		}
	}

	// 合同委托审批列表 add by drdu 091109
	public void findDelegationList() throws JSONException {
		String status = request.getParameter("status");

		Long conTypeId = 0l;
		if (request.getParameter("conTypeId") != null) {
			conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		}
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = "";
		 if (conTypeId == 2) {
		 entryIds = workflowService.getAvailableWorkflow(
		 new String[] { "bqConDelegationManage" }, employee.getWorkerCode());
		 } else {
		 entryIds = workflowService.getAvailableWorkflow(
		 new String[] { "bqContractDelegation" }, employee.getWorkerCode());
		 }
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj = remote.findDelegationList("", conTypeId, employee
				.getEnterpriseCode(), startdate, enddate, null, null, null,
				status, entryIds, start, limit);
		if (obj != null) {
			String str = "{total:" + obj.getTotalCount() + ",root:"
					+ JSONUtil.serialize(obj.getList()) + "}";
			write(str);
		} else {
			write("{total : 0,root :[]}");
		}
	}

	// 查询执行中合同列表
	public void getExecConList() throws JSONException {
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj = remote.findExecContractList(employee
				.getEnterpriseCode(), startdate, enddate, fuzzy, null, start,
				limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"
				+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}

	// 查询满足条件的合同列表
	public void getAllConList() throws JSONException {
		// add bjxu -conTypeId
		Long conTypeId = null;
		if (request.getParameter("conTypeId") != null) {
			conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		}
		String conNo = request.getParameter("conNo");
		String conName = request.getParameter("conName");
		String clientName = request.getParameter("clientName");
		String operaterBy = request.getParameter("operaterBy");
		Long status = null;
		if ((request.getParameter("status") != null)
				&& (!"".equals(request.getParameter("status")))) {
			status = Long.parseLong(request.getParameter("status"));
		}
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj = remote.findAllContractList(conTypeId, employee
				.getEnterpriseCode(), startdate, enddate, conNo, conName,
				clientName, operaterBy, status, start, limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"
				+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}

	// 根据部门查员工列表
	@SuppressWarnings("unchecked")
	public void getEmpByDept() throws JSONException {
		List<HrJEmpInfo> list;
		// list=eremote.findByDeptId(Long.parseLong(deptId));
		list = eremote.getListByDeptCode(employee.getEnterpriseCode(), deptId);
		write(JSONUtil.serialize(list));
	}

	// 增加修改合同信息
	@SuppressWarnings( { "unchecked", "unchecked" })
	public void addMeetConInfo() throws JSONException, IOException,
			CodeRepeatException {
		if ("add".equals(method)) {
			con.setSignStartDate(con.getEntryDate());
			con.setIsUse("Y");
			con.setExecFlag(0L);
			con.setFileStatus("DRF");
			con.setWorkflowStatus(0L);
			con.setEnterpriseCode(employee.getEnterpriseCode());
			if (con.getConTypeId() != null) {
				String markCode = null;
				if (con.getConTypeId().equals(2l)) {
					markCode = prjtyperemote.findById(con.getPrjtypeId())
							.getMarkCode();
				} else {
					markCode = con.getConCode() + "-" + con.getConYear() + "-";
				}
				ConJConDoc dmodel = new ConJConDoc();
				ConJContractInfo model = new ConJContractInfo();
				// 没有上传文件
				if (conFile != null) {
					String filePath = request.getParameter("filePath");
					java.io.FileInputStream fis = new java.io.FileInputStream(
							conFile);
					int index = filePath.lastIndexOf(".");
					int ssdex = filePath.lastIndexOf("\\");
					byte[] data = new byte[(int) fis.available()];
					fis.read(data);
					if (index > 0) {
						dmodel.setOriFileExt(filePath.substring(index + 1));
						dmodel.setOriFileName(filePath.substring(ssdex + 1,
								index));
						dmodel.setDocName(filePath.substring(ssdex + 1, index));
					}
					dmodel.setDocType("CON");
					dmodel.setDocContent(data);
					dmodel.setEnterpriseCode(employee.getEnterpriseCode());
					dmodel.setIsUse("Y");
					dmodel.setLastModifiedBy(employee.getWorkerCode());
					dmodel.setLastModifiedDate(new Date());
					model = remote.save(markCode, con, dmodel, secondcharge);
				} else {
					model = remote.save(markCode, con, null, secondcharge);
				}
				write("{success:true,data:" + JSONUtil.serialize(model)
						+ ",conttreesNo:'" + model.getConttreesNo()
						+ "',path:'" + dmodel.getOriFileName() + "'}");
			}
		} else if (method.equals("update")) {
			ConJContractInfo model = remote.findById(con.getConId());
			model.setContractName(con.getContractName());
			model.setConTypeId(con.getConTypeId());
			model.setCliendId(con.getCliendId());
			model.setItemId(con.getItemId());
			model.setIsInstant(con.getIsInstant());
			model.setIsSign(con.getIsSign());
			model.setIsSum(con.getIsSum());
			model.setConYear(con.getConYear());
			model.setOperateBy(con.getOperateBy());
			model.setOperateDepCode(con.getOperateDepCode());
			model.setOperateLeadBy(con.getOperateLeadBy());
			model.setCurrencyType(con.getCurrencyType());
			model.setStartDate(con.getStartDate());
			model.setEndDate(con.getEndDate());
			model.setConAbstract(con.getConAbstract());
			model.setEntryDate(con.getEntryDate());
			model.setActAmount(con.getActAmount());
			// derek 2009.05.20
			model.setProjectId(con.getProjectId());
			model.setThirdClientId(con.getThirdClientId());
			model.setWarrantyPeriod(con.getWarrantyPeriod());
			// bjxu 200091012
			model.setOperateTel(con.getOperateTel());
			model.setOperateAdvice(con.getOperateAdvice());
			model.setOperateDate(con.getOperateDate());
			model.setJyjhAdvice(con.getJyjhAdvice());
			model.setJyjhDate(con.getJyjhDate());
			model.setProsy_by(con.getProsy_by());
			model.setProsyStartDate(con.getProsyStartDate());
			model.setProsyEndDate(con.getProsyEndDate());
			model.setPrjtypeId(con.getPrjtypeId());
			// add by bjxu 20091116
			model.setConCode(con.getConCode());
			model.setProsy_by(con.getProsy_by());
			model.setIfSecrity(con.getIfSecrity());
			ConJConDoc dmodel = new ConJConDoc();
			if (conFile != null) {
				String filePath = request.getParameter("filePath");
				PageObject obj = docremote.findConDocList(employee
						.getEnterpriseCode(), con.getConId(), "CON", conDocId);
				if (obj.getList().size() > 0) {
					List list = obj.getList();
					ConDocForm formmodel = new ConDocForm();
					formmodel = (ConDocForm) list.get(0);
					dmodel = docremote.findById(formmodel.getConDocId());
					java.io.FileInputStream fis = new java.io.FileInputStream(
							conFile);
					int index = filePath.lastIndexOf(".");
					int ssdex = filePath.lastIndexOf("\\");
					byte[] data = new byte[(int) fis.available()];
					fis.read(data);
					if (index > 0) {
						dmodel.setOriFileExt(filePath.substring(index + 1));
						dmodel.setOriFileName(filePath.substring(ssdex + 1,
								index));
						dmodel.setDocName(filePath.substring(ssdex + 1, index));
					}
					dmodel.setDocContent(data);
					dmodel.setLastModifiedBy(employee.getWorkerCode());
					dmodel.setLastModifiedDate(new Date());
					remote.update(model, dmodel, secondcharge);
				} else {
					java.io.FileInputStream fis = new java.io.FileInputStream(
							conFile);
					int index = filePath.lastIndexOf(".");
					int ssdex = filePath.lastIndexOf("\\");
					byte[] data = new byte[(int) fis.available()];
					fis.read(data);
					if (index > 0) {
						dmodel.setOriFileExt(filePath.substring(index + 1));
						dmodel.setOriFileName(filePath.substring(ssdex + 1,
								index));
						dmodel.setDocName(filePath.substring(ssdex + 1, index));
					}
					dmodel.setDocContent(data);
					dmodel.setLastModifiedBy(employee.getWorkerCode());
					dmodel.setLastModifiedDate(new Date());
					dmodel.setKeyId(con.getConId());
					dmodel.setDocType("CON");
					dmodel.setEnterpriseCode(employee.getEnterpriseCode());
					dmodel.setIsUse("Y");
					remote.update(model, dmodel, secondcharge);
				}
			} else {
				remote.update(model, null, secondcharge);
			}
			System.out.println("the ifsecrity"+con.getIfSecrity());
			write("{success:true,data:" + JSONUtil.serialize(model) + ",path:'"
					+ dmodel.getOriFileName() + "'}");
		} else {
			String id = request.getParameter("id");
			String itemCode = request.getParameter("itemCode");
			String conttreesNo = request.getParameter("conttreesNo");
			if (id != null) {
				ConJContractInfo model = remote.findById(Long.parseLong(id));
				if (model.getConTypeId() == 2) {
					model.setItemId(itemCode);
					model.setConttreesNo(conttreesNo);
					ConJConDoc dmodel = new ConJConDoc();
					if (conFile != null) {
						String filePath = request.getParameter("filePath");
						PageObject obj = docremote.findConDocList(employee
								.getEnterpriseCode(), Long.parseLong(id),
								"CON", conDocId);
						if (obj.getList().size() > 0) {
							List list = obj.getList();
							ConDocForm formmodel = new ConDocForm();
							formmodel = (ConDocForm) list.get(0);
							dmodel = docremote
									.findById(formmodel.getConDocId());
							java.io.FileInputStream fis = new java.io.FileInputStream(
									conFile);
							int index = filePath.lastIndexOf(".");
							int ssdex = filePath.lastIndexOf("\\");
							byte[] data = new byte[(int) fis.available()];
							fis.read(data);
							if (index > 0) {
								dmodel.setOriFileExt(filePath
										.substring(index + 1));
								dmodel.setOriFileName(filePath.substring(
										ssdex + 1, index));
								dmodel.setDocName(filePath.substring(ssdex + 1,
										index));
							}
							dmodel.setDocContent(data);
							dmodel.setLastModifiedBy(employee.getWorkerCode());
							dmodel.setLastModifiedDate(new Date());
							remote.update(model, dmodel, secondcharge);
						} else {
							java.io.FileInputStream fis = new java.io.FileInputStream(
									conFile);
							int index = filePath.lastIndexOf(".");
							int ssdex = filePath.lastIndexOf("\\");
							byte[] data = new byte[(int) fis.available()];
							fis.read(data);
							if (index > 0) {
								dmodel.setOriFileExt(filePath
										.substring(index + 1));
								dmodel.setOriFileName(filePath.substring(
										ssdex + 1, index));
								dmodel.setDocName(filePath.substring(ssdex + 1,
										index));
							}
							dmodel.setDocContent(data);
							dmodel.setLastModifiedBy(employee.getWorkerCode());
							dmodel.setLastModifiedDate(new Date());
							dmodel.setKeyId(Long.parseLong(id));
							dmodel.setDocType("CON");
							dmodel.setEnterpriseCode(employee
									.getEnterpriseCode());
							dmodel.setIsUse("Y");
							remote.update(model, dmodel, secondcharge);
						}
					} else {
						remote.update(model, null, secondcharge);
					}
					write("{success:true,data:" + JSONUtil.serialize(model)
							+ ",path:'" + dmodel.getOriFileName() + "'}");

				} else {
					model.setItemId(itemCode);
					remote.update(model, null, null);
				}
			}
		}
	}

	// 删除合同信息
	public void deleteMeetConInfo() throws CodeRepeatException {
		if (conid != null) {
			ConJContractInfo model = remote.findById(conid);
			remote.delete(model);
			write("{success:true,data:'数据删除成功！'}");
		} else {
			write("{success:true,data:'操作失败！'}");
		}

	}

	// 获取一条记录详细信息
	public void findMeetConModel() throws JSONException {
		ContractFullInfo conmodel = remote.getConFullInfoById(conid);
		write("{success:true,data:" + JSONUtil.serialize(conmodel) + "}");
		 System.out.println(JSONUtil.serialize(conmodel));
	}

	@SuppressWarnings("unchecked")
	public void showConFile() throws IOException, JSONException {
		if (type.equals("CON") || type.equals("MCON") || type.equals("CONATT")
				|| type.equals("CONEVI") || type.equals("MCONATT")
				|| type.equals("MCONEVI") || type.equals("CONPAY")
				|| type.equals("SHENQING")) {
			PageObject obj = docremote.findConDocList(employee
					.getEnterpriseCode(), conid, type, conDocId);
			List<ConDocForm> list = obj.getList();
			if (list.size() > 0) {
				ConDocForm fmodel = list.get(0);
				if (fmodel != null) {
					ConJConDoc model = docremote.findById(fmodel.getConDocId());
					/* 合同文档文件类型 */
					String sFormart = model.getOriFileExt();
					/* 合同文档文件名称 */
					String filename = new String(model.getDocName().getBytes(
							"GBK"), "ISO8859_1");
					try {
						// response.reset();
						response.setHeader("Content-Disposition",
								"attachment; filename=" + filename + "."
										+ sFormart);
						if (sFormart.equals("txt")) {
							sFormart = "text/plain;charset=utf-8";
						} else if (sFormart.equals("doc")) {
							sFormart = "application/vnd.msword;charset=utf-8";
						} else if (sFormart.equals("xls")) {
							sFormart = "application/vnd.ms-excel;charset=utf-8";
						}
						byte[] data = model.getDocContent();
						response.setContentType(sFormart);
						OutputStream outs = response.getOutputStream();
						for (int i = 0; i < data.length; i++) {
							outs.write(data[i]);
						}
						outs.write(data);
						outs.flush();
						outs.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			ConJConDoc model = docremote.findById(docid);
			if (model != null) {
				byte[] data = model.getDocContent();
				super.writeDoc(data);
			}
		}
	}

	// 获取合同附件/凭据列表
	@SuppressWarnings("unchecked")
	public void findDocList() throws JSONException {
		PageObject obj = docremote.findConDocList(employee.getEnterpriseCode(),
				conid, type, conDocId);
		List<ConDocForm> list = obj.getList();
		write(JSONUtil.serialize(list));
	}

	// 增加合同附件/凭据
	public void addConDoc() throws IOException {

		if (fjdocFile != null) {
			String filePath = request.getParameter("filePath");
			java.io.FileInputStream fis = new java.io.FileInputStream(fjdocFile);
			int index = filePath.lastIndexOf(".");
			int ssdex = filePath.lastIndexOf("\\");
			byte[] data = new byte[(int) fis.available()];
			fis.read(data);
			if (index > 0) {
				doc.setOriFileExt(filePath.substring(index + 1));
				doc.setOriFileName(filePath.substring(ssdex + 1, index));
				// doc.setDocName(filePath.substring(ssdex+1, index));
			}
			doc.setLastModifiedBy(employee.getWorkerCode());
			doc.setLastModifiedDate(new Date());
			doc.setDocContent(data);
			doc.setIsUse("Y");
			doc.setEnterpriseCode(employee.getEnterpriseCode());
			docremote.save(doc);
			write("{success:true,data:'数据保存成功！'}");
		}

	}

	// 修改合同附件/凭据
	public void updateConDoc() throws IOException {
		String filePath = request.getParameter("filePath");
		ConJConDoc cmodel = docremote.findById(doc.getConDocId());
		if (fjdocFile != null) {
			java.io.FileInputStream fis = new java.io.FileInputStream(fjdocFile);
			int index = filePath.lastIndexOf(".");
			int ssdex = filePath.lastIndexOf("\\");
			byte[] data = new byte[(int) fis.available()];
			fis.read(data);
			if (index > 0) {
				cmodel.setOriFileExt(filePath.substring(index + 1));
				cmodel.setOriFileName(filePath.substring(ssdex + 1, index));
				// cmodel.setDocName(filePath.substring(ssdex+1, index));
			}
			cmodel.setDocContent(data);

		}
		cmodel.setDocMemo(doc.getDocMemo());
		cmodel.setDocName(doc.getDocName());
		cmodel.setLastModifiedBy(employee.getWorkerCode());
		cmodel.setLastModifiedDate(new Date());
		docremote.update(cmodel);
		write("{success:true,data:'数据保存成功！'}");
	}

	// 删除合同附件/凭据
	public void deleteConDoc() {
		if (docid != null) {
			ConJConDoc model = docremote.findById(docid);
			model.setIsUse("N");
			docremote.update(model);
			write("{success:true,data:'数据删除成功！'}");
		}
	}

	// 采购合同会签上报
	public void contractReport() throws CodeRepeatException {
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		appremote.contractReport(conid, employee.getWorkerCode(), approveText,
				nextRoles);
		//----------add by fyyang 20100608--短信通知------------
		ConJContractInfo model = remote.findById(conid);
		PostMessage postMsg=new PostMessage();
		if(nextRoles==null||nextRoles.equals(""))
		{
			nextRoles=postMsg.getFistStepRoles("bqCGContract", "221", null, null);
		}
		if(nextRoles!=null&&!nextRoles.equals(""))
		{
			String msg=model.getConttreesNo()+"("+model.getContractName()+"）合同等待您的审批，请您及时处理。";
			postMsg.sendMsg(nextRoles, msg);
		}
		//---------add end------------------------------------------
		write("{success:true,msg:'上报成功！'}");
	}

	// 采购合同会签审批
	public void contractApprove() {
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String approveDepts = request.getParameter("approveDepts");
		String stepId = request.getParameter("stepId");
		if (entryId != null && actionId != null) {
			try {
				//----------add by fyyang 20100608--短信通知------------
				PostMessage postMsg=new PostMessage();
				String thisRoles=nextRoles;
				if(!actionId.equals("393"))
				{
				if(nextRoles==null||nextRoles.equals(""))
				{
					thisRoles=postMsg.getNextSetpRoles(entryId, actionId);
				}
				}
				//-------------------------------------------------------------
				appremote.contractApprove(Long.parseLong(entryId), workerCode,
						Long.parseLong(actionId), approveText, nextRoles,
						conid, approveDepts, stepId);
//				
				//----------add by fyyang 20100608--短信通知------------
				ConJContractInfo model = remote.findById(conid);
				if(actionId.equals("393"))
				{
					thisRoles=postMsg.getFistStepRoles("bqContractDelegation", "24", null, null);
					if(thisRoles!=null&&!thisRoles.equals(""))
					{
						String msg=model.getConttreesNo()+"("+model.getContractName()+"）合同等待您的审批，请您及时处理。";
						postMsg.sendMsg(thisRoles, msg);
					}
				}
				else
				{
				if(model.getWorkflowStatus()!=3)
				{
				if(thisRoles!=null&&!thisRoles.equals(""))
				{
					String msg=model.getConttreesNo()+"("+model.getContractName()+"）合同等待您的审批，请您及时处理。";
					postMsg.sendMsg(thisRoles, msg);
				}
				}
				}
				//---------add end------------------------------------------
				write("{success:true,data:'审批成功！'}");
			} catch (Exception e) {
				write("{success:false,errorMsg:'" + e.getMessage() + "'}");
			}
		}
	}

	// 合同综合统计 add by drdu @20090107
	public void finConIntegrateList() throws JSONException {
		// add 合同类别
		Long conTypeId = null;
		if (request.getParameter("conTypeId") != null) {
			conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		}
		String conNo = request.getParameter("conNo");
		String conName = request.getParameter("conName");
		String clientName = request.getParameter("clientName");
		String operaterBy = request.getParameter("operaterBy");
		String status = "";
		String fileStatus = "";
		if ((request.getParameter("status") != null)
				&& (!"".equals(request.getParameter("status")))) {
			status = request.getParameter("status");
		}
		if ((request.getParameter("fileStatus") != null)
				&& (!"".equals(request.getParameter("fileStatus")))) {
			fileStatus = request.getParameter("fileStatus");
		}

		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject pg = remote.findConIntegrateList(conTypeId, employee
				.getEnterpriseCode(), startdate, enddate, conNo, conName,
				clientName, operaterBy, status, fileStatus, start, limit);
		String str = "{total:" + pg.getTotalCount() + ",root:"
				+ JSONUtil.serialize(pg.getList()) + "}";
		write(str);
	}

	// /*
	// * 取得所有的供应商
	// */
	// public void getConClientList() {
	// String str = "";
	// ConJClientInfoFacadeRemote clientBll = (ConJClientInfoFacadeRemote)
	// factory
	// .getFacadeRemote("resource/ConJClientInfoFacade");
	// List<ConJClientInfo> list = clientBll.findByProperty("isUse", "Y");
	// if (list != null) {
	// try {
	// str += "{list:" + JSONUtil.serialize(list) + "}";
	// } catch (JSONException e) {
	// str = "{msg:'操作异常!'}";
	// e.printStackTrace();
	// }
	// } else {
	// str = "{list:[]}";
	// }
	// write(str);
	// }

	/**
	 * 取得所有的币别 add by drdu 2009/05/05
	 */
	public void getConCurrencyList() {
		String str = "";
		SysCCurrencyFacadeRemote sysremote = (SysCCurrencyFacadeRemote) factory
				.getFacadeRemote("SysCCurrencyFacade");
		List<SysCCurrency> list = sysremote.findByProperty("isUse", "Y");
		if (list != null) {
			try {
				str += "{list:" + JSONUtil.serialize(list) + "}";
			} catch (JSONException e) {
				str = "{msg:'操作异常!'}";
				e.printStackTrace();
			}
		} else {
			str = "{list:[]}";
		}
		write(str);
	}

	/**
	 * 合同审批上传修改合同文本信息
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void modifyMeetConInfo() throws IOException {

		if (conFile != null) {
			String filePath = request.getParameter("filePath");
			PageObject obj = docremote.findConDocList(employee
					.getEnterpriseCode(), conid, "CON", conDocId);
			Iterator it = obj.getList().iterator();
			if (it.hasNext()) {
				ConDocForm formmodel = (ConDocForm) it.next();
				ConJConDoc model = docremote.findById(formmodel.getConDocId());
				java.io.FileInputStream fis = new java.io.FileInputStream(
						conFile);
				int index = filePath.lastIndexOf(".");
				int ssdex = filePath.lastIndexOf("\\");
				byte[] data = new byte[(int) fis.available()];
				fis.read(data);
				if (index > 0) {
					model.setOriFileExt(filePath.substring(index + 1));
					model.setOriFileName(filePath.substring(ssdex + 1, index));
					// doc.setDocName(filePath.substring(ssdex+1, index));
				}
				model.setDocContent(data);
				model.setLastModifiedBy(employee.getWorkerCode());
				model.setLastModifiedDate(new Date());
				docremote.update(model);
				write("{success:true,data:'数据保存成功！'}");

			}

		}
	}

	/**
	 * 合同相关物资明细信息 add by dswang 2009/5/23
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	public void findAllMaterialByContractNo() throws IOException, JSONException {
		String str = "";
		String contractNo = request.getParameter("contractNo");
		List<MaterialDetailsForm> list = cmremote.findAllMaterialsByContractNo(
				contractNo, employee.getEnterpriseCode());
		if (list != null) {
			str += "{list:" + JSONUtil.serialize(list) + "}";
		} else {
			str += "{list:[]}";
		}
		write(str);
	}

	/**
	 * 对一合同进行增加或者删除采购单关联 add by dswang 2009/5/26
	 */
	public void updateMaterial() {
		String purNo = request.getParameter("purNo");
		String contractNo = request.getParameter("contractNo");
		String method = request.getParameter("method");
		cmremote.updateContractMaterial(purNo, contractNo, employee
				.getEnterpriseCode(), method);
	}

	// 项目合合同会签上报
	public void prjContractReport() throws CodeRepeatException {//modify by wpzhu 20100607
		// 取得审批部门
//		try {
//			BpJPlanJobDepMain obj = mRemote.reportTo(prjNo, workflowType,
//					employee.getWorkerCode(), actionId, approveText, nextRolePs,
//					nextRoles,employee.getEnterpriseCode());
//			
//			write("{success:true,msg:'操作成功！',obj:" + JSONUtil.serialize(obj)
//					+ "}");
//		} catch (Exception e) {
//			write("{failure:true,msg:'" + e.getMessage() + "'}");
//		}
		// String approveDept = request.getParameter("approveDept");
		 String workflowType = request.getParameter("workflowType");
		 String nextRolesPs = request.getParameter("nextRolePs");
		 String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		
	
		appremote.prjContractReport(conid, employee.getWorkerCode(),actionId,secondcharge, approveText, nextRolesPs,
				nextRoles,employee.getEnterpriseCode());

		//---------add by fyyang 20100608----------------
		ConJContractInfo model=remote.findById(conid);
		PostMessage postMsg=new PostMessage();
		if(nextRoles!=null&&!nextRoles.equals(""))
		{
			String msg=model.getConttreesNo()+"("+model.getContractName()+"）合同等待您的审批，请您及时处理。";
			postMsg.sendMsg(nextRoles, msg);
		}
		if(nextRolesPs!=null&&!nextRolesPs.equals(""))
		{
			String msg=model.getConttreesNo()+"("+model.getContractName()+"）合同等待您的审批，请您及时处理。";
			postMsg.sendMsgByWorker(nextRolesPs, msg);
		}
		//---------add end------------------------------------------
		write("{success:true,msg:'上报成功！'}");
	}

	// 项目合同审批 add bjxu
	public void prjContractApprove() {
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String approveDepts = request.getParameter("approveDepts");
		if (entryId != null && actionId != null) {
			try {
				//----------add by ywliu 20100609--短信通知------------
				PostMessage postMsg = new PostMessage();
				String thisRoles = nextRoles;
				if (!"183".equals(actionId)) {
					if (nextRoles == null || nextRoles.equals("")) {
						thisRoles = postMsg.getNextSetpRoles(entryId, actionId);
					}
				}
				//-------------------------------------------------------------
				appremote.prjContractApprove(Long.parseLong(entryId),
						workerCode, Long.parseLong(actionId), approveText,
						nextRoles, conid, approveDepts);
				//----------add by ywliu 20100609--短信通知------------
				ConJContractInfo model = remote.findById(conid);
				if (!"183".equals(actionId) && model.getWorkflowStatus() != 3) {
					if (thisRoles != null && !thisRoles.equals("")) {
						String msg = model.getConttreesNo() + "("
								+ model.getContractName()
								+ "）合同等待您的审批，请您及时处理。";
						postMsg.sendMsg(thisRoles, msg);
					}
				} else if("183".equals(actionId)) {
					thisRoles=postMsg.getFistStepRoles("bqConDelegationManage", "24", null, null);
					if(thisRoles!=null&&!thisRoles.equals(""))
					{
						String msg=model.getConttreesNo()+"("+model.getContractName()+"）合同等待您的审批，请您及时处理。";
						postMsg.sendMsg(thisRoles, msg);
					}
				}
				//---------add end------------------------------------------
				write("{success:true,data:'审批成功！'}");
			} catch (Exception e) {
				write("{success:false,errorMsg:'" + e.getMessage() + "'}");
			} 
		}
	}

	/**
	 * 项目合同委托审批 add by drdu 091110
	 */
	public void proConDelegationApprove() {
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String prosyBy = request.getParameter("prosyBy");
		String sDate = request.getParameter("sDate");
		String eDate = request.getParameter("eDate");
		if (entryId != null && actionId != null) {
			try {
				appremote.delegationApproveSign(Long.parseLong(entryId),
						workerCode, Long.parseLong(actionId), approveText,
						nextRoles, conid, prosyBy, sDate, eDate);
				//----------add by ywliu 20100608--短信通知------------
				if (Long.parseLong(actionId) == 57l || Long.parseLong(actionId) == 45l) {
					ConJContractInfo model = remote.findById(conid);
					PostMessage postMsg=new PostMessage();
					String msg = model.getConttreesNo()+"("+model.getContractName()+")"+"合同等待您的审批，请您及时处理。";
					if(Long.parseLong(actionId) == 45l) {
						postMsg.sendMsgByWorker(prosyBy, msg);
					} else if(Long.parseLong(actionId) == 57l) {
						postMsg.sendMsgByWorker(model.getOperateBy(), msg);
					}
				}
				//----------end------------ 
				write("{success:true,msg:'审批成功！'}");
			} catch (Exception e) {
				write("{success:false,errorMsg:'" + e.getMessage() + "'}");
			}
		}
	}

	/**
	 * 采购合同委托审批 add by drdu 091119
	 */
	public void purConDelegationApprove() {
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String prosyBy = request.getParameter("prosyBy");
		String sDate = request.getParameter("sDate");
		String eDate = request.getParameter("eDate");
		if (entryId != null && actionId != null) {
			try {
				appremote.conDelegationApproveSign(Long.parseLong(entryId),
						workerCode, Long.parseLong(actionId), approveText,
						nextRoles, conid, prosyBy, sDate, eDate);
				//----------add by ywliu 20100608--短信通知------------
				if (Long.parseLong(actionId) == 56l || Long.parseLong(actionId) == 45l) {
					ConJContractInfo model = remote.findById(conid);
					PostMessage postMsg=new PostMessage();
					String msg = model.getConttreesNo()+"("+model.getContractName()+")"+"合同等待您的审批，请您及时处理。";
					if(Long.parseLong(actionId) == 45l) {
						postMsg.sendMsgByWorker(prosyBy, msg);
					} else if(Long.parseLong(actionId) == 56l) {
						postMsg.sendMsgByWorker(model.getOperateBy(), msg);
					}
				}
				//----------end------------ 

				write("{success:true,msg:'审批成功！'}");
			} catch (Exception e) {
				write("{success:false,errorMsg:'" + e.getMessage() + "'}");
			}
		}
	}

	// /保存质保期限 add by fyyang 090729
	public void saveWarrantyPeriod() throws CodeRepeatException {
		String contractId = request.getParameter("myContractId");
		String warrantyPeriod = request.getParameter("warrantyPeriod");
		ConJContractInfo model = remote.findById(Long.parseLong(contractId));
		model.setWarrantyPeriod(Long.parseLong(warrantyPeriod));
		remote.update(model, null, null);
		write("{success:true,msg:'保存成功！'}");

	}

	public String getFuzzy() {
		return fuzzy;
	}

	public void setFuzzy(String fuzzy) {
		this.fuzzy = fuzzy;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public ConJContractInfo getCon() {
		return con;
	}

	public void setCon(ConJContractInfo con) {
		this.con = con;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Long getConid() {
		return conid;
	}

	public void setConid(Long conid) {
		this.conid = conid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ConJConDoc getDoc() {
		return doc;
	}

	public void setDoc(ConJConDoc doc) {
		this.doc = doc;
	}

	public Long getDocid() {
		return docid;
	}

	public void setDocid(Long docid) {
		this.docid = docid;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public File getFjdocFile() {
		return fjdocFile;
	}

	public void setFjdocFile(File fjdocFile) {
		this.fjdocFile = fjdocFile;
	}

	public File getConFile() {
		return conFile;
	}

	public void setConFile(File conFile) {
		this.conFile = conFile;
	}

	// public Long getConTypeId() {
	// return conTypeId;
	// }
	//
	// public void setConTypeId(Long conTypeId) {
	// this.conTypeId = conTypeId;
	// }

	public String getSecondcharge() {
		return secondcharge;
	}

	public void setSecondcharge(String secondcharge) {
		this.secondcharge = secondcharge;
	}

	public Long getConDocId() {
		return conDocId;
	}

	public void setConDocId(Long conDocId) {
		this.conDocId = conDocId;
	}
}
