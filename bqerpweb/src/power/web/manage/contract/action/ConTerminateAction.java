package power.web.manage.contract.action;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJConDoc;
import power.ejb.manage.contract.business.ConJConDocFacadeRemote;
import power.ejb.manage.contract.business.ConJContractInfo;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.business.ConJModifyFacadeRemote;
import power.ejb.manage.contract.form.ConDocForm;
import power.ejb.manage.contract.form.ConModifyForm;
import power.ejb.manage.contract.form.ContractFullInfo;
import power.web.comm.AbstractAction;

public class ConTerminateAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConJContractInfoFacadeRemote conRemote;
	private ConJConDocFacadeRemote docRemote;
	private ConJModifyFacadeRemote modifyRemote;
	private ConJContractInfo con;
	private String fuzzy;
	private int start;
	private int limit;
	private Long conid;
	private String type;
	private ConJConDoc doc;
	private Long docid;
	private String conttreesNo;
	private String contractName;
	private Long cliendId;
	// 合同类型
	private Long conTypeId;
	private String terminateMome;
	// 附件id
	private Long conDocId;

	public ConTerminateAction() {
		conRemote = (ConJContractInfoFacadeRemote) factory
				.getFacadeRemote("ConJContractInfoFacade");
		docRemote = (ConJConDocFacadeRemote) factory
				.getFacadeRemote("ConJConDocFacade");
		modifyRemote = (ConJModifyFacadeRemote) factory
				.getFacadeRemote("ConJModifyFacade");
	}

	// 获取session值
	public void getSession() throws JSONException {
		Object[] o = new Object[2];
		o[0] = employee.getWorkerCode();
		o[1] = employee.getWorkerName();
		write("{success:true,data:" + JSONUtil.serialize(o) + "}");
	}

	// 合同验收终止列表
	public void findContractTerminateList() throws JSONException {
		String enterpriseCode = employee.getEnterpriseCode();
		PageObject obj = conRemote.findContractTerminateList(conTypeId,
				enterpriseCode, fuzzy, start, limit);
		if (obj != null) {
			String str = JSONUtil.serialize(obj);
			write(str);
		}else {
			write("{list : [],totalCount : 0}");
		}
	}

	// 取一条合同记录的详细信息
	public void findConModel() throws JSONException {
		ContractFullInfo info = conRemote.getConFullInfoById(conid);
		write("{success:true,data:" + JSONUtil.serialize(info) + "}");
		String str = JSONUtil.serialize(info);
		write(str);
	}

	// 取一条合同变更记录的详细信息
	public void findConModifyModel() throws JSONException {
		String modifyId = request.getParameter("modifyId");
		ConModifyForm apply = modifyRemote.findConModifyModel(Long
				.parseLong(modifyId));
		String str = JSONUtil.serialize(apply);
		write("{success: true,data:" + str + "}");
	}

	// 取合同附件、凭据记录信息
	public void findTerminateDocList() throws JSONException {
		PageObject obj = docRemote.findConDocList(employee.getEnterpriseCode(),
				conid, type, conDocId);
		List<ConDocForm> list = obj.getList();
		write(JSONUtil.serialize(list));
	}

	// 验收终止FORM
	public void conTerminate() throws CodeRepeatException {
		String mome = request.getParameter("terminateMome");
		ConJContractInfo model = conRemote.findById(conid);
		model.setTerminateDate(new Date());
		model.setTerminateMome(mome);
		model.setTerminateBy(employee.getWorkerCode());
		model.setEnterpriseCode(employee.getEnterpriseCode());
		conRemote.saveConTerminate(model);

		write("{success:true}");
	}

	public ConJContractInfo getCon() {
		return con;
	}

	public void setCon(ConJContractInfo con) {
		this.con = con;
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

	public String getConttreesNo() {
		return conttreesNo;
	}

	public void setConttreesNo(String conttreesNo) {
		this.conttreesNo = conttreesNo;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Long getCliendId() {
		return cliendId;
	}

	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}

	public String getTerminateMome() {
		return terminateMome;
	}

	public void setTerminateMome(String terminateMome) {
		this.terminateMome = terminateMome;
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
}
