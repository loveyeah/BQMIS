package power.web.equ.workbill.action;

import java.text.ParseException;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.workbill.EquJOtma;
import power.ejb.equ.workbill.EquJOtmaFacadeRemote;
import power.ejb.resource.InvJIssueDetails;
import power.ejb.resource.InvJIssueDetailsFacadeRemote;
import power.ejb.resource.InvJIssueHead;
import power.ejb.resource.InvJIssueHeadFacadeRemote;
import power.web.comm.AbstractAction;

public class RelateMaterielAction extends AbstractAction {
	private InvJIssueHeadFacadeRemote headRemote;
	private InvJIssueDetailsFacadeRemote headDRemote;
	private EquJOtmaFacadeRemote maremote;
	private InvJIssueHead head;
	// private InvJIssueDetails Details;
	InvJIssueHeadFacadeRemote iRemote;
	private Long materialId;
	private Double appliedCount;

	public RelateMaterielAction() {
		maremote = (EquJOtmaFacadeRemote) factory
				.getFacadeRemote("EquJOtmaFacade");
		headRemote = (InvJIssueHeadFacadeRemote) factory
				.getFacadeRemote("InvJIssueHeadFacade");
		headDRemote = (InvJIssueDetailsFacadeRemote) factory
				.getFacadeRemote("InvJIssueDetailsFacade");
		iRemote = (InvJIssueHeadFacadeRemote) factory
				.getFacadeRemote("InvJIssueHeadFacade");
	}

	// 根据工单编号查找所有的相关物资
	public void relateMaterielList() throws JSONException {
		String woCode = request.getParameter("woCode");
		PageObject list = maremote.relateMaterielList(woCode);
		if (list != null) {
			String Str = JSONUtil.serialize(list.getList());
//			System.out.print(Str);
			write("{ root:" + Str + "}");
		} else {
			write("{root:[]}");
		}
	}

	// 删除相关物资
	public void deleteMateriel() throws JSONException {
		Long materialId = Long.parseLong(request.getParameter("materialId"));
		String matCodes = request.getParameter("matCode");
		String[] matCodesnew = matCodes.split(",");

		for (String matCode : matCodesnew) {
			InvJIssueHead entity_2 = iRemote.findByMatCode(matCode, employee
					.getEnterpriseCode());
			int count = headDRemote.getMcount(entity_2.getIssueHeadId());
			if (count >1 ) {
				InvJIssueDetails model = headDRemote.getDetails(entity_2.getIssueHeadId(), materialId);
				model.setIsUse("N");
				headDRemote.update(model);
			} else {
				entity_2.setIsUse("N");
				maremote.delMateriel(matCodes, employee.getEnterpriseCode());
				InvJIssueDetails model = headDRemote.getDetails(entity_2.getIssueHeadId(), materialId);
				model.setIsUse("N");
				headDRemote.update(model);
			}
			iRemote.update(entity_2);
		}
	}

	// 领料单填写
	public void addIssueMateriel() throws ParseException {
		String woCode = request.getParameter("woCode");
		// 新增领料单数据
		// String materialId = request.getParameter("planAmount");
		// String planAmount = request.getParameter("planAmount");

		head.setEnterpriseCode(employee.getEnterpriseCode());
		head.setLastModifiedBy(employee.getWorkerCode());
		// head.setWoNo(woCode);
		EquJOtma entity = new EquJOtma();
		entity.setEnterprisecode(employee.getEnterpriseCode());

		InvJIssueHead model = headRemote.addIssueHead(head);

		entity.setMatCode(model.getIssueNo());
		entity.setWoCode(woCode);
		maremote.save(entity);

		InvJIssueDetails Details = new InvJIssueDetails();
		Details.setApprovedCount(0D);
		Details.setActIssuedCount(0D);
		Details.setMaterialId(materialId);
		Details.setAppliedCount(appliedCount);
		Details.setIssueHeadId(model.getIssueHeadId());
		Details.setIsUse("Y");
		Details.setEnterpriseCode(employee.getEnterpriseCode());
		Details.setLastModifiedBy(employee.getWorkerCode());
		headDRemote.save(Details);
		write("{success : true,msg :'操作成功'}");
	}

	// 领料单选择
	public void chooseIssueMateriel() {
		String woCode = request.getParameter("woCode");
		String issueNo = request.getParameter("issueNo");
		EquJOtma entity = new EquJOtma();
		entity.setWoCode(woCode);
		entity.setMatCode(issueNo);
		entity.setEnterprisecode(employee.getEnterpriseCode());
		maremote.save(entity);
		write("{success : true,msg :'操作成功'}");
	}

	// 撤销关联
	public void CancelIssueMateriel() {
		String matCodes = request.getParameter("matCode");
		maremote.delMateriel(matCodes, employee.getEnterpriseCode());
	}

	public InvJIssueHead getHead() {
		return head;
	}

	public void setHead(InvJIssueHead head) {
		this.head = head;
	}

	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public Double getAppliedCount() {
		return appliedCount;
	}

	public void setAppliedCount(Double appliedCount) {
		this.appliedCount = appliedCount;
	}

	// public InvJIssueDetails getDetails() {
	// return Details;
	// }
	//
	// public void setDetails(InvJIssueDetails details) {
	// Details = details;
	// }
}