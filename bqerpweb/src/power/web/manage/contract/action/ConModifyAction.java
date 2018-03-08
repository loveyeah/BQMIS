package power.web.manage.contract.action;

import java.io.File;

import java.io.IOException;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJConDoc;
import power.ejb.manage.contract.business.ConJConDocFacadeRemote;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.business.ConJFileOpinion;
import power.ejb.manage.contract.business.ConJFileOpinionFacadeRemote;
import power.ejb.manage.contract.business.ConJModify;
import power.ejb.manage.contract.business.ConJModifyFacadeRemote;
import power.ejb.manage.contract.business.ContractApprove;
import power.ejb.manage.contract.business.ContractMaterial;
import power.ejb.manage.contract.form.ConModifyForm;
import power.ejb.manage.contract.form.MaterialDetailsForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ConModifyAction extends AbstractAction {
	private ConJModifyFacadeRemote remote;
	private ContractApprove appremote;
	private ContractMaterial cmremote;
	private ConJFileOpinionFacadeRemote bll;
	private ConJModify apply;
	private String fuzzy;
	private int start;
	private int limit;
	private String startDate;
	private String endDate;
	private File conFile;
	private Long conTypeId;

	public ConModifyAction() {
		remote = (ConJModifyFacadeRemote) factory
				.getFacadeRemote("ConJModifyFacade");
		appremote = (ContractApprove) factory
				.getFacadeRemote("ContractApproveImp");
		bll = (ConJFileOpinionFacadeRemote) factory
				.getFacadeRemote("ConJFileOpinionFacade");
		cmremote = (ContractMaterial) factory
				.getFacadeRemote("ContractMaterialImp");
	}

	public void findConModifyList() throws JSONException {
		// Long conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		String workCode = employee.getWorkerCode();
		if (workCode.equals("999999")) {
			workCode = "";
		}
		PageObject obj = remote.findConModifyList(workCode, conTypeId, employee
				.getEnterpriseCode(), startDate, endDate, fuzzy, start, limit);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	public void addModifyBase() throws IOException {
		String conId = request.getParameter("conId");
		apply.setConId(Long.parseLong(conId));
		apply.setEnterpriseCode(employee.getEnterpriseCode());
		apply.setEntryDate(new Date());
		apply.setEntryBy(employee.getWorkerCode());
		apply = remote.save(apply);
		String path = "";
		if (conFile != null) {
			String filePath = request.getParameter("filePath");
			path = this.saveFile(apply.getConModifyId(), conFile, filePath);
		}
		write("{success:true,msg:'增加成功！',id:" + apply.getConModifyId()
				+ ",conModifyNo:'" + apply.getConModifyNo() + "',path:'" + path
				+ "'}");
	}

	private String saveFile(Long modifyId, File conFile, String filePath)
			throws IOException {
		ConJConDocFacadeRemote docRemote = (ConJConDocFacadeRemote) factory
				.getFacadeRemote("ConJConDocFacade");
		int index = filePath.lastIndexOf(".");
		int ssdex = filePath.lastIndexOf("\\");
		java.io.FileInputStream fis = new java.io.FileInputStream(conFile);
		byte[] data = new byte[(int) fis.available()];
		fis.read(data);
		boolean check = true;
		ConJConDoc model = docRemote.findConDocModel(employee
				.getEnterpriseCode(), modifyId, "MCON");
		if (model != null) {
			check = false;
		} else {
			check = true;
			model = new ConJConDoc();
		}
		model.setKeyId(modifyId);
		model.setDocContent(data);
		model.setDocType("MCON");
		model.setOriFileExt(filePath.substring(index + 1));
		model.setOriFileName(filePath.substring(ssdex + 1, index));
		model.setDocName(filePath.substring(ssdex + 1, index));

		model.setLastModifiedBy(employee.getWorkerCode());
		model.setLastModifiedDate(new Date());
		model.setEnterpriseCode(employee.getEnterpriseCode());
		if (check) {
			docRemote.save(model);
		} else {
			docRemote.update(model);
		}
		return model.getOriFileName();
	}

	public String showFile() throws IOException {
		String docId = request.getParameter("docId");
		ConJConDocFacadeRemote docRemote = (ConJConDocFacadeRemote) factory
				.getFacadeRemote("ConJConDocFacade");
		ConJConDoc model = docRemote.findById(Long.parseLong(docId));
		if (model != null) {
			byte[] data = model.getDocContent();
			// response.setContentType("txt/doc");
			// OutputStream outs = response.getOutputStream();
			// for (int i = 0; i < data.length; i++) {
			// outs.write(data[i]);//输出到页面
			// }
			// }
			super.writeDoc(data);
		}
		return "success";
	}

	public void updateModifyBase() throws NumberFormatException, IOException {
		String modifyId = request.getParameter("modifyId");
		ConJModify model = remote.findById(Long.parseLong(modifyId));
		model.setConomodifyName(apply.getConomodifyName());
		model.setConomodifyType(apply.getConomodifyType());
		model.setEntryBy(employee.getWorkerCode());
		model.setEntryDate(new Date());
		model.setModiyActAmount(apply.getModiyActAmount());
		model.setOperateBy(apply.getOperateBy());
		model.setOperateDepCode(apply.getOperateDepCode());
		model.setOperateLeadBy(apply.getOperateLeadBy());
		ConJModify result = remote.update(model);
		String path = "";
		if (conFile != null) {
			String filePath = request.getParameter("filePath");
			path = this.saveFile(Long.parseLong(modifyId), conFile, filePath);
		}
		write("{success:true,msg:'修改成功！',id:" + result.getConModifyId()
				+ ",conModifyNo:'" + result.getConModifyNo() + "',path:'"
				+ path + "'}");
	}

	// 申请归档，修改fileStatus
	public void applyArchive() {
		String modifyId = request.getParameter("conId");
		ConJModify model = remote.findById(Long.parseLong(modifyId));
		model.setFileStatus("PRE");
		remote.update(model);
	}

	// 查询退回意见

	public void findBackOpinion() throws JSONException {
		String modifyId = request.getParameter("modifyId");

		List<ConJFileOpinion> list = bll.findByProperty("keyId", (Object) Long
				.parseLong(modifyId));
		String str = JSONUtil.serialize(list);
		write(str);
	}

	public void findModifyBaseInfo() throws JSONException {
		String modifyId = request.getParameter("modifyId");
		ConModifyForm apply = remote.findConModifyModel(Long
				.parseLong(modifyId));
		String str = JSONUtil.serialize(apply);
		write("{success: true,data:" + str + "}");
	}

	public void deleteModifyBase() {
		String modifyId = request.getParameter("id");
		ConJModify model = remote.findById(Long.parseLong(modifyId));
		remote.delete(model);
		write("{success:true,msg:'删除成功！'}");
	}

	public void findConListForSelect() throws JSONException {
		Long conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		ConJContractInfoFacadeRemote conRemote = (ConJContractInfoFacadeRemote) factory
				.getFacadeRemote("ConJContractInfoFacade");
		PageObject obj = conRemote.findContractSelect(conTypeId, employee
				.getEnterpriseCode(), fuzzy, "2", start, limit);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	// 查询合同变更审批列表
	public void findConModifyApproveList() throws JSONException {
		String status = request.getParameter("status");
		// 合同类别 conTypeId
		Long conTypeId = 0l;
		if (request.getParameter("conTypeId") != null) {
			conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		}
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = "";
		if (conTypeId == 2) {
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "prjConModify" }, employee.getWorkerCode());
		} else {
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqCGContract" }, employee.getWorkerCode());
		}
		PageObject obj = remote.findConModifyApproveList(conTypeId, employee
				.getEnterpriseCode(), startDate, endDate, status, entryIds,
				start, limit);
		if (obj != null) {
			String str = JSONUtil.serialize(obj);
			write(str);
		} else {
			write("{totalCount : 0,list:[]}");
		}
	}

	// 合同变更审批上报
	public void conModApproveReport() {
		String conModId = request.getParameter("conModId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		if (conModId != null && !"".equals(conModId)) {
			appremote.conModifyReport(Long.parseLong(conModId), employee
					.getWorkerCode(), approveText, nextRoles);
			write("{success:true,msg:'上报成功！'}");
		}
	}

	// 合同变更审批
	public void contractModifyApprove() {
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String conModId = request.getParameter("conModId");
		String approveDepts = request.getParameter("approveDepts");
		String stepId = request.getParameter("stepId");
		if (entryId != null && actionId != null) {
			try {
				appremote.conModApprove(Long.parseLong(entryId), workerCode,
						Long.parseLong(actionId), approveText, nextRoles, Long
								.parseLong(conModId),approveDepts,stepId);
				write("{success:true,data:'审批成功！'}");
			} catch (Exception e) {
				write("{success:false,errorMsg:'" + e.getMessage() + "'}");
			}
		}
	}

	public void findMaterialsByConId() throws JSONException {
		String str = "";
		Long conId = Long.parseLong(request.getParameter("conId"));
		List<MaterialDetailsForm> list = cmremote.findAllMaterialsByConId(
				conId, employee.getEnterpriseCode());
		if (list != null) {
			str += "{list:" + JSONUtil.serialize(list) + "}";
		} else {
			str += "{list:[]}";
		}
		write(str);
	}

	// 项目合同变更审批上报
	public void prjConModApproveReport() {
		String conModId = request.getParameter("conModId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		if (conModId != null && !"".equals(conModId)) {
			appremote.prjConModifyReport(Long.parseLong(conModId), employee
					.getWorkerCode(), approveText, nextRoles);
			write("{success:true,msg:'上报成功！'}");
		}
	}

	// 项目合同变更审批
	public void prjContractModifyApprove() {
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String conModId = request.getParameter("conModId");
		if (entryId != null && actionId != null) {
			try {
				appremote.prjConModApprove(Long.parseLong(entryId), workerCode,
						Long.parseLong(actionId), approveText, nextRoles, Long
								.parseLong(conModId));
				write("{success:true,data:'审批成功！'}");
			} catch (Exception e) {
				write("{success:false,errorMsg:'" + e.getMessage() + "'}");
			}
		}
	}

	// 取变更id
	public void findconModifyIds() throws JSONException {
		String conid = request.getParameter("conid");
		List<ConJModify> list = remote.getconModifyId(conid);
		String str = "[";
		int i = 0;
		if (list.size() > 0) {
			for (ConJModify model : list) {
				i++;
				str += "\"" + model.getConModifyId() + "\"";
				if (i < list.size()) {
					str += ",";
				} else {
					str += "]";
				}
			}
		} else{
			str += "]";
		}
		write(str);
	}

	public String getFuzzy() {
		return fuzzy;
	}

	public void setFuzzy(String fuzzy) {
		this.fuzzy = fuzzy;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public ConJModify getApply() {
		return apply;
	}

	public void setApply(ConJModify apply) {
		this.apply = apply;
	}

	public File getConFile() {
		return conFile;
	}

	public void setConFile(File conFile) {
		this.conFile = conFile;
	}

	public Long getConTypeId() {
		return conTypeId;
	}

	public void setConTypeId(Long conTypeId) {
		this.conTypeId = conTypeId;
	}

}
