package power.web.manage.contract.action;

import java.io.File; //import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.apache.struts2.components.File;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJBalaApprove;
import power.ejb.manage.contract.business.ConJBalance;
import power.ejb.manage.contract.business.ConJBalanceFacadeRemote;
import power.ejb.manage.contract.business.ConJBalinvioce;
import power.ejb.manage.contract.business.ConJBalinvioceFacadeRemote;
import power.ejb.manage.contract.business.ConJConDoc;
import power.ejb.manage.contract.business.ConJConDocFacadeRemote;
import power.ejb.manage.contract.business.ConJContractInfo;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.business.ContractApprove;
import power.ejb.manage.contract.business.ContractMaterial;
import power.ejb.manage.contract.form.BalinvioceForm;
import power.ejb.manage.contract.form.ConBalanceForm;
import power.ejb.manage.contract.form.ConBalanceFullForm;
import power.ejb.manage.contract.form.ConDocForm;
import power.ejb.manage.contract.form.ContractForm;
import power.ejb.manage.contract.form.ContractFullInfo;
import power.ejb.system.SysCRolesFacadeRemote;
import power.ejb.system.SysCUl;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

public class ConBalanceAction extends AbstractAction {

	/**
	 * slTang
	 */
	private static final long serialVersionUID = 1L;
	private String fuzzy;
	private String startdate;
	private String enddate;
	private Long conId;
	private String type;
	private ConJBalance bal;
	private Long balanceId;
	private ConJConDoc doc;
	private Long docid;
	private String filePath;
	private ConJBalinvioce balrevoice;
	private ConJContractInfoFacadeRemote conremote;
	private ConJBalanceFacadeRemote balremote;
	private ConJConDocFacadeRemote docremote;
	private SysCRolesFacadeRemote roleremote;
	private ConJBalinvioceFacadeRemote revremote;
	private ContractApprove appremote;
	private ContractMaterial cmremote;
	private Long conTypeId;
	private File docFile;
	// 附件id
	private Long conDocId;

	public ConBalanceAction() {
		conremote = (ConJContractInfoFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("ConJContractInfoFacade");
		balremote = (ConJBalanceFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("ConJBalanceFacade");
		docremote = (ConJConDocFacadeRemote) factory
				.getFacadeRemote("ConJConDocFacade");
		roleremote = (SysCRolesFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("SysCRolesFacade");
		revremote = (ConJBalinvioceFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("ConJBalinvioceFacade");
		appremote = (ContractApprove) factory
				.getFacadeRemote("ContractApproveImp");
		cmremote = (ContractMaterial) factory
				.getFacadeRemote("ContractMaterialImp");
	}

	// 查找执行中的合同列表
	public void findAppConList() throws JSONException {
		// 合同类别
		Long conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj = conremote.findContractInfos(conTypeId, employee
				.getEnterpriseCode(), startdate, enddate, fuzzy, "2", null,
				null, start, limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"
				+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}

	// 查找合同结算申请列表
	public void findBalanceList() throws JSONException {
		List<ConBalanceForm> list = balremote.findBalanceListByConId(conId,
				employee.getEnterpriseCode());
		String str = "{balanceTotal:" + list.size() + ",balanceRoot:"
				+ JSONUtil.serialize(list) + "}";
		write(str);
	}

	// 查找合同详细信息
	public void findConFullInfo() throws JSONException {
		ContractFullInfo info = conremote.getConFullInfoById(conId);
		String str = JSONUtil.serialize(info);
		write("{success:true,data:" + str + "}");
	}

	// 获取附件
	public void findDocList() throws JSONException {
		PageObject obj = docremote.findConDocList(employee.getEnterpriseCode(),
				balanceId, type, conDocId);
		List<ConDocForm> list = obj.getList();
		write(JSONUtil.serialize(list));
	}

	// 获取合同结算申请信息
	public void findBalanceInfo() throws JSONException {
		ConBalanceFullForm balinfo = balremote
				.findBalanceByBalanceId(balanceId);
		String str = JSONUtil.serialize(balinfo);

		write("{success:true,data:" + str + "}");
	}

	public void findContract() throws JSONException {
		ContractForm form = balremote.findContractByConId(conId);
		String str = JSONUtil.serialize(form);
		write(str);
	}

	// 增加合同结算申请
	public void addBalance() throws JSONException, CodeRepeatException {
		if (conId != null) {
			ContractFullInfo con = conremote.getConFullInfoById(conId);
			bal.setConId(conId);
			bal.setApplicatDate(new Date());
			bal.setEntryDate(new Date());
			bal.setEntryBy(employee.getWorkerCode());
			bal.setEnterpriseCode(employee.getEnterpriseCode());
			bal.setItemId(con.getItemId());
			bal.setCliendId(con.getCliendId());
			ConJBalance conBalance = balremote.save(bal);
			String balId = conBalance.getBalanceId().toString();
			write("{success:true,balId:" + JSONUtil.serialize(balId)
					+ ",balaFlag:'" + conBalance.getBalaFlag() + "'}");
		}
	}

	// 修改合同结算申请
	public void updateBalance() throws JSONException, CodeRepeatException {
		if (bal.getBalanceId() != null) {
			ConJBalance model = balremote.findById(bal.getBalanceId());
			if (model.getBalaFlag().equals("0")) {
				model.setApplicatDate(new Date());
				model.setEntryBy(employee.getWorkerCode());
				model.setEntryDate(new Date());
				model.setBalaBatch(bal.getBalaBatch());
				model.setBalaCause(bal.getBalaCause());
				model.setApplicatPrice(bal.getApplicatPrice());
				model.setPaymentId(bal.getPaymentId());
				model.setOperateBy(bal.getOperateBy());
				model.setBalaMethod(bal.getBalaMethod());
				model.setChequeNo(bal.getChequeNo());
				model.setPassPrice(bal.getPassPrice());
				model.setBalancePrice(bal.getBalancePrice());
				model.setBalanceBy(bal.getBalanceBy());
				ConJBalance conBalance = balremote.update(model);
				String balId = conBalance.getBalanceId().toString();
				write("{success:true,balId:" + JSONUtil.serialize(balId)
						+ ",balaFlag:'" + conBalance.getBalaFlag() + "'}");
			}
		}
	}

	// 删除
	public void deleteBal() throws CodeRepeatException {
		ConJBalance model = balremote.findById(bal.getBalanceId());
		balremote.delete(model);
	}

	// 上报
	public void reportBalance() {
		// bal.setPassPrice(bal.getApplicatPrice());
		// bal.setWorkflowStatus(1l);
		// bal.setPassDate(new Date());
		// balremote.update(bal);
		appremote.conBalanceReport(bal, employee.getWorkerCode());
	}

	// 查询审批列表
	public void findBalanceApproveList() throws JSONException {
		// 合同类别
		Long conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = "";
		if (conTypeId == 2) {
			// modify by fyyang 090729 增加prjConQuality质保金流程
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "prjConBalance" }, employee.getWorkerCode());
		} else {

			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bubConBalance" }, employee.getWorkerCode());
		}
		PageObject obj = balremote.findConBalanceApproveList(conTypeId,
				startdate, enddate, employee.getEnterpriseCode(), entryIds,
				type, start, limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"
				+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}

	// 审批
	public void approveBalance() {
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String identify = request.getParameter("eventIdentify");
		ConJBalaApprove hismodel = new ConJBalaApprove();
		ConJBalance model = new ConJBalance();
		if (entryId != null && actionId != null) {
			try {
				model = balremote.findById(bal.getBalanceId());
				hismodel.setBalanceId(model.getBalanceId());
				hismodel.setApproveBy(employee.getWorkerCode());
				hismodel.setApproveDate(new Date());
				hismodel.setApproveOpinion(approveText);
				hismodel.setEnterpriseCode(employee.getEnterpriseCode());
				hismodel.setIsUse("Y");
				if ("TGCW".equals(identify)) {
					model.setBalancePrice(model.getBalancePrice());
					model.setBalaDate(new Date());
					hismodel.setApproveAmount(model.getBalancePrice());
				} else {
					model.setPassPrice(model.getPassPrice());
					model.setPassDate(new Date());
					hismodel.setApproveAmount(model.getPassPrice());
				}
				if ("TH".equals(identify)) {
					ConJContractInfo conmodel = conremote.findById(model
							.getConId());
					model.setWorkflowStatus(3L);
					model.setBalaFlag("3");
					conmodel.setAppliedAmount(conmodel.getAppliedAmount()
							- model.getPassPrice());
					conremote.update(conmodel, null, null);
				} else if (actionId.equals("67")) {
					model.setBalaFlag("2");
				} else if ("TGEND".equals(identify)) {
					model.setWorkflowStatus(2L);
					model.setBalaFlag("2");
				}

				// -----------add by ltong 20100608 短信通知----------------
				PostMessage postMsg = new PostMessage();
				String thisRoleString = nextRoles;
				ConJContractInfo conmodel = conremote
						.findById(model.getConId());
				if (!actionId.equals("72") && !actionId.equals("42")
						&& !actionId.equals("73") && !actionId.equals("52")
						&& !actionId.equals("62")) {
					if (nextRoles == null || "".equals(nextRoles)) {
						thisRoleString = postMsg.getNextSetpRoles(entryId,
								actionId);
					}
				}
				// -----------add end-------------------------------------

				appremote.prjConBalanceApprove(Long.parseLong(entryId),
						workerCode, Long.parseLong(actionId), approveText,
						nextRoles, model, hismodel);

				// -----------add by ltong 20100608 短信通知----------------
				if (!actionId.equals("72") && !actionId.equals("42")
						&& !actionId.equals("52") && !actionId.equals("62")) {
					if (thisRoleString != null && !thisRoleString.equals("")) {
						String msg = "项目合同结算等待您的审批，请您及时处理。合同名称："
								+ conmodel.getContractName() + "。";
						postMsg.sendMsg(thisRoleString, msg);
					}
				}
				// -----------add end-------------------------------------

				write("{success:true,data:'审批成功!'}");
			} catch (Exception e) {
				write("{success:false,errorMsg:'" + e.getMessage() + "'}");
			}
		}
	}

	// 增加付款附件
	public void addBalDoc() throws Exception {
		if (docFile != null) {
			// if (!(filePath.equals(""))) {
			java.io.FileInputStream fis = new java.io.FileInputStream(docFile);
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
			doc.setKeyId(balanceId);
			doc.setDocType(type);
			docremote.save(doc);
			write("{success:true,data:'数据保存成功！'}");
		}
		// }
	}

	// 修改付款附件
	public void updateBalDoc() throws Exception {
		ConJConDoc cmodel = docremote.findById(doc.getConDocId());
		if (docFile != null) {
			java.io.FileInputStream fis = new java.io.FileInputStream(docFile);
			int index = filePath.lastIndexOf(".");
			int ssdex = filePath.lastIndexOf("\\");
			byte[] data = new byte[(int) fis.available()];
			fis.read(data);
			if (index > 0) {
				cmodel.setOriFileExt(filePath.substring(index + 1));
				cmodel.setOriFileName(filePath.substring(ssdex + 1, index));
				// doc.setDocName(filePath.substring(ssdex+1, index));
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

	// // 增加票据
	// public void addBalInvoice() throws Exception {
	// String str = request.getParameter("str");
	// String bid=request.getParameter("balanceId");
	// Object obj = JSONUtil.deserialize(str);
	// List<Map> list = (List<Map>) obj;
	// for (Map data : list) {
	// String invoiceNo = ((Map) ((Map) data)).get("invoiceNo").toString();
	// String invoiceId = ((Map) ((Map) data)).get("invoiceId").toString();
	// String invoiceName = ((Map) ((Map) data)).get("invoiceName")
	// .toString();
	// String invoicePrice = ((Map) ((Map) data)).get("invoicePrice")
	// .toString();
	// String drawerBy = ((Map) ((Map) data)).get("drawerBy").toString();
	// ConJBalinvioce model=new ConJBalinvioce();
	// model.setInvoiceNo(Long.parseLong(invoiceNo));
	// // model.setInvoiceCode("");
	// model.setBalanceId(Long.valueOf(bid));
	// model.setEnterpriseCode(employee.getEnterpriseCode());
	// model.setDrawerBy(drawerBy);
	// model.setInvoiceDate(new Date());
	// model.setInvoiceName(invoiceName);
	// model.setInvoiceId(Long.valueOf(invoiceId));
	// model.setInvoicePrice(Double.valueOf(invoicePrice));
	// model.setOperateBy(employee.getWorkerCode());
	// model.setDrawerBy(drawerBy);
	// revremote.save(model);
	// }
	// }

	// 修改票据
	@SuppressWarnings("unchecked")
	public void updateBalInvoice() throws Exception {

		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<ConJBalinvioce> addList = new ArrayList<ConJBalinvioce>();
			List<ConJBalinvioce> updateList = new ArrayList<ConJBalinvioce>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				String balanceId = null;
				String invoiceCode = null;
				String invoiceId = null;
				String invoiceDate = null;
				String invoiceName = null;
				String invoicePrice = null;
				String drawerBy = null;
				String operateBy = null;
				String memo = null;
				String invoiceNo = null;

				if (data.get("invoiceNo") != null) {
					invoiceNo = data.get("invoiceNo").toString();
				}

				if (data.get("balanceId") != null) {
					balanceId = data.get("balanceId").toString();
				}
				if (data.get("invoiceCode") != null) {
					invoiceCode = data.get("invoiceCode").toString();
				}
				if (data.get("invoiceId") != null) {
					invoiceId = data.get("invoiceId").toString();
				}
				if (data.get("invoiceDate") != null) {
					invoiceDate = data.get("invoiceDate").toString();
				}
				if (data.get("invoiceName") != null) {
					invoiceName = data.get("invoiceName").toString();
				}
				if (data.get("invoicePrice") != null) {
					invoicePrice = data.get("invoicePrice").toString();
				}
				if (data.get("drawerBy") != null) {
					drawerBy = data.get("drawerBy").toString();
				}
				if (data.get("operateBy") != null) {
					operateBy = data.get("operateBy").toString();
				}
				if (data.get("memo") != null) {
					memo = data.get("memo").toString();
				}

				ConJBalinvioce model = new ConJBalinvioce();

				// 增加
				if (invoiceNo == null) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");

					if (invoiceId != null && !invoiceId.equals("")) {
						model.setInvoiceId(invoiceId);
					}
					if (balanceId != null && !balanceId.equals("")) {
						model.setBalanceId(Long.parseLong(balanceId));
					}

					if (invoiceDate != null && !invoiceDate.equals("")) {
						model.setInvoiceDate(sdf.parse(invoiceDate));
					}
					model.setInvoiceCode(invoiceCode);
					model.setInvoiceName(invoiceName);

					if (invoicePrice != null && !invoicePrice.equals("")) {
						model.setInvoicePrice(Double.parseDouble(invoicePrice));
					}
					model.setDrawerBy(drawerBy);
					model.setOperateBy(operateBy);
					model.setMemo(memo);

					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);
				} else {
					model = revremote.findById(Long.parseLong(invoiceNo));

					if (invoiceId != null && !invoiceId.equals("")) {
						model.setInvoiceId(invoiceId);
					}
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					if (invoiceDate != null && !invoiceDate.equals("")) {
						model.setInvoiceDate(sdf.parse(invoiceDate));
					}
					model.setInvoiceCode(invoiceCode);
					model.setInvoiceName(invoiceName);

					if (invoicePrice != null && !invoicePrice.equals("")) {
						model.setInvoicePrice(Double.parseDouble(invoicePrice));
					}
					model.setDrawerBy(drawerBy);
					model.setOperateBy(operateBy);
					model.setMemo(memo);

					updateList.add(model);
				}
			}

			if (addList.size() > 0)

				revremote.save(addList);

			if (updateList.size() > 0)

				revremote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))

				revremote.delete(deleteIds);

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}

		// String str = request.getParameter("str");
		// Object obj = JSONUtil.deserialize(str);
		// List<Map> list = (List<Map>) obj;
		// for (Map data : list) {
		// String invoiceNo = ((Map) ((Map) data)).get("invoiceNo").toString();
		// String invoiceId = ((Map) ((Map) data)).get("invoiceId").toString();
		// String invoiceName = ((Map) ((Map) data)).get("invoiceName")
		// .toString();
		// String invoicePrice = ((Map) ((Map) data)).get("invoicePrice")
		// .toString();
		// String drawerBy = ((Map) ((Map) data)).get("drawerBy").toString();
		// ConJBalinvioce model = revremote
		// .findById(Long.valueOf(invoiceNo));
		// model.setDrawerBy(drawerBy);
		// model.setInvoiceDate(new Date());
		// model.setInvoiceName(invoiceName);
		// model.setInvoiceId(Long.valueOf(invoiceId));
		// model.setInvoicePrice(Double.valueOf(invoicePrice));
		// model.setOperateBy(employee.getWorkerCode());
		// model.setDrawerBy(drawerBy);
		// revremote.update(model);
		// }
		//
		// // model.setDrawerBy(balrevoice.getDrawerBy());
		// // model.setInvoiceDate(balrevoice.getInvoiceDate());
		// // model.setInvoiceCode(balrevoice.getInvoiceCode());
		// // model.setInvoiceName(balrevoice.getInvoiceName());
		// // model.setInvoiceId(balrevoice.getInvoiceId());
		// // model.setInvoicePrice(balrevoice.getInvoicePrice());
		// // model.setOperateBy(balrevoice.getOperateBy());
		// // model.setMemo(balrevoice.getMemo());

	}

	// // 删除票据
	// public void delBalInvoice() {
	// String delId=request.getParameter("delId");
	// String[] ids=delId.split(",");
	// for(String id:ids){
	// ConJBalinvioce model = revremote.findById(Long.parseLong(id));
	// revremote.delete(model);
	// }
	// }

	// 查看票据列表
	public void findBalInvoice() throws Exception {
		List<BalinvioceForm> list = revremote
				.findByBalanceId("hfdc", balanceId);
		String str = JSONUtil.serialize(list);
		write(str);
	}

	public void deleteBalDoc() {
		if (docid != null) {
			ConJConDoc model = docremote.findById(docid);
			model.setIsUse("N");
			docremote.update(model);
			write("{success:true,data:'数据删除成功！'}");
		}
	}

	public void getOperateBy() throws Exception {
		List<SysCUl> list = roleremote.findUsersByRole(1l);
		write(JSONUtil.serialize(list));
	}

	// 合同实际付款明细查询 add by drdu @20090106
	public void findConPayDetailsList() throws JSONException {
		// add 合同类型
		Long conTypeId = null;
		if (request.getParameter("conTypeId") != null) {
			conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		}
		String conNo = request.getParameter("conNo");
		String conName = request.getParameter("conName");
		String clientName = request.getParameter("clientName");
		String operaterBy = request.getParameter("operaterBy");
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject pg = balremote.findConPayDetailsList(conTypeId, employee
				.getEnterpriseCode(), startdate, enddate, conNo, conName,
				clientName, operaterBy, start, limit);
		String str = "{total:" + pg.getTotalCount() + ",root:"
				+ JSONUtil.serialize(pg.getList()) + "}";
		write(str);
	}

	// 合同付款执行情况查询 add by drdu @20090108
	public void findConPaymentList() throws JSONException {
		// 合同类别
		Long conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		// startdate = request.getParameter("startdate");
		// enddate = request.getParameter("enddate");
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		String conNo = request.getParameter("conNo");
		String conName = request.getParameter("conName");
		String clientName = request.getParameter("clientName");
		String operaterBy = request.getParameter("operaterBy");
		PageObject pg = balremote.findConPaymentList(conTypeId, employee
				.getEnterpriseCode(), startdate, enddate, conNo, conName,
				clientName, operaterBy, start, limit);
		String str = "{total:" + pg.getTotalCount() + ",root:"
				+ JSONUtil.serialize(pg.getList()) + "}";
		write(str);
	}

	public void updateMaterialById() {
		String purNo = request.getParameter("purNo");
		String conId = request.getParameter("conId");
		String method = request.getParameter("method");
		cmremote.updateContractMaterialByConId(purNo, conId, employee
				.getEnterpriseCode(), method);
	}

	// 项目合同结算上报 及质保金上报公用
	public void prjreportBalance() {
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String balanceId = request.getParameter("balanceId");
		String passPrice = request.getParameter("passPrice");
		// add by fyyang 090729 获取流程编码
		String workflowType = request.getParameter("workflowType");
		appremote.prjConBalanceReport(balanceId, employee.getWorkerCode(),
				approveText, nextRoles, passPrice, workflowType);
		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		ConJBalance model = balremote.findById(Long.parseLong(balanceId));
		ConJContractInfo conmodel = conremote.findById(model.getConId());
		if (nextRoles == null || nextRoles.equals("")) {
			nextRoles = postMsg.getFistStepRoles("prjConBalance", "24", null,
					null);
		}
		if (nextRoles != null && !nextRoles.equals("")) {
			String msg = "项目合同结算申请已上报等待您的审批，请您及时处理。合同名称："
					+ conmodel.getContractName() + "。";
			postMsg.sendMsgByWorker(nextRoles, msg);
		}
		// -----------add end--------------------------------------
		write("{success:true,msg:'上报成功！'}");
	}

	public String getFuzzy() {
		return fuzzy;
	}

	public void setFuzzy(String fuzzy) {
		this.fuzzy = fuzzy;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getConId() {
		return conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ConJBalance getBal() {
		return bal;
	}

	public void setBal(ConJBalance bal) {
		this.bal = bal;
	}

	public Long getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(Long balanceId) {
		this.balanceId = balanceId;
	}

	public ConJConDoc getDoc() {
		return doc;
	}

	public void setDoc(ConJConDoc doc) {
		this.doc = doc;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getDocid() {
		return docid;
	}

	public void setDocid(Long docid) {
		this.docid = docid;
	}

	public ConJBalinvioce getBalrevoice() {
		return balrevoice;
	}

	public void setBalrevoice(ConJBalinvioce balrevoice) {
		this.balrevoice = balrevoice;
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

	public Long getConTypeId() {
		return conTypeId;
	}

	public void setConTypeId(Long conTypeId) {
		this.conTypeId = conTypeId;
	}

	public Long getConDocId() {
		return conDocId;
	}

	public void setConDocId(Long conDocId) {
		this.conDocId = conDocId;
	}

	public File getDocFile() {
		return docFile;
	}

	public void setDocFile(File docFile) {
		this.docFile = docFile;
	}
}
