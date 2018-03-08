package power.web.run.repair.action;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.comm.TreeNode;
import power.ejb.run.repair.RunCRepairProject;
import power.ejb.run.repair.RunCRepairProjectFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class RunRepairProjectAction extends AbstractAction {

	protected RunCRepairProjectFacadeRemote remote;
	private RunCRepairProject repair;

	public RunRepairProjectAction() {
		remote = (RunCRepairProjectFacadeRemote) factory
				.getFacadeRemote("RunCRepairProjectFacade");
	}

	@SuppressWarnings("unchecked")
	public void findRepairTreeList() throws JSONException {
		String method = "";
		Object myobj = request.getParameter("method");
		if (myobj != null) {
			method = myobj.toString();
		}
		// update by ltong 选择多项时树带checkbox
		String fRepairProjectId = request.getParameter("id");
		List<TreeNode> arrayList = new ArrayList();
		List<RunCRepairProject> list = remote.findRepairTreeList(
				fRepairProjectId, employee.getEnterpriseCode());
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				RunCRepairProject repair = list.get(i);
				TreeNode model = new TreeNode();
				boolean isLeaf = !remote.IfHasChild(
						repair.getRepairProjectId(), employee
								.getEnterpriseCode());
				if (method.equals("many")) {
					Object o = remote.findWorkerNameObject(repair
							.getWorkingCharge());
					JSONStr.append("{id:'"
							+ repair.getRepairProjectId().toString()
							+ "',text:'" + repair.getRepairProjectName()
							+ "',workingChargeCode:'"
							+ repair.getWorkingCharge() + "',workingCharge:'"
							+ o + "',workingMenbers:'"
							+ repair.getWorkingMenbers() + "',workingTime:'"
							+ repair.getWorkingTime() + "',openType:'"
							+ repair.getAcceptanceLevel() + "',leaf:" + isLeaf
							+ ",checked:false},");

				}
				else if (method.equals("single")) {
					Object o = remote.findWorkerNameObject(repair
							.getWorkingCharge());
					JSONStr.append("{id:'"
							+ repair.getRepairProjectId().toString()
							+ "',text:'" + repair.getRepairProjectName()
							+ "',workingChargeCode:'"
							+ repair.getWorkingCharge() + "',workingCharge:'"
							+ o + "',workingMenbers:'"
							+ repair.getWorkingMenbers() + "',workingTime:'"
							+ repair.getWorkingTime() + "',openType:'"
							+ repair.getAcceptanceLevel() + "',leaf:" + isLeaf
							+ ",checked:false},");

				}else {
					model.setId(repair.getRepairProjectId().toString());
					model.setText(repair.getRepairProjectName());
					model.setLeaf(isLeaf);
					model.setOpenType(repair.getAcceptanceLevel());
					arrayList.add(model);
				}
			}
		}
		if (JSONStr.length() > 1) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]");
		if (method.equals("many")) {
			write(JSONStr.toString());
		}
		else if (method.equals("single")) {
			write(JSONStr.toString());
		} else {
			write(JSONUtil.serialize(arrayList));
		}
	}

	public void getRepairTreeInfo() throws JSONException {
		String code = "";
		code = request.getParameter("id");
		Object o = remote.findRepairProjectInfo(Long.parseLong(code));
		write(JSONUtil.serialize(o));
	}

	public void addAndUpdateRepairRecord() {
		String methode = request.getParameter("method");
		if (methode.equals("add")) {
			repair.setEnterpriseCode(employee.getEnterpriseCode());
			remote.save(repair);
			write("{success:true,msg:'增加成功！'}");
		} else {
			RunCRepairProject model = remote.findById(repair
					.getRepairProjectId());
			model.setRepairProjectName(repair.getRepairProjectName());
			model.setAcceptanceFirst(repair.getAcceptanceFirst());
			model.setAcceptanceSecond(repair.getAcceptanceSecond());
			model.setAcceptanceThird(repair.getAcceptanceThird());
			model.setAcceptanceLevel(repair.getAcceptanceLevel());
			model.setWorkingCharge(repair.getWorkingCharge());
			model.setWorkingMenbers(repair.getWorkingMenbers());
			model.setWorkingTime(repair.getWorkingTime());

			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		}
	}

	public void deleteRepairMaintRecord() {
		RunCRepairProject model = new RunCRepairProject();
		String code = "";
		code = request.getParameter("id");
		if (!remote.IfHasChild(Long.parseLong(code), employee
				.getEnterpriseCode())) {
			model = remote.findByCode(code, employee.getEnterpriseCode());
			model.setIsUse("N");
			remote.update(model);
			write("{success:true,msg:'删除成功！'}");
		} else {
			write("{success:false,msg:'该节点有子节点，不能删除！'}");
		}
	}

	public RunCRepairProject getRepair() {
		return repair;
	}

	public void setRepair(RunCRepairProject repair) {
		this.repair = repair;
	}

}
