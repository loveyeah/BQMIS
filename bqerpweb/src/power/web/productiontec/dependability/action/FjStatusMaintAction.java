package power.web.productiontec.dependability.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.comm.TreeNode;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxCFjstate;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxCFjstateFacadeRemote;
import power.web.comm.AbstractAction;

public class FjStatusMaintAction extends AbstractAction{
	public PtKkxCFjstateFacadeRemote remote;
	public PtKkxCFjstate model;
	public FjStatusMaintAction()
	{
		remote=(PtKkxCFjstateFacadeRemote)factory.getFacadeRemote("PtKkxCFjstateFacade");
	}
	public void findNodeByPNode() throws JSONException {
		String nodeString = request.getParameter("pid");
		List<TreeNode> list = remote.findStatTreeList(nodeString, employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(list));
	}
	public void deleteNode() {
		Long nodeid = model.getJzztId();
		remote.delete(remote.findById(nodeid));
		write("{success:true}");
	}
	public void findNodeInfo() throws JSONException {
		String nodeid = request.getParameter("nodeid");
		PtKkxCFjstate model = remote.findById(Long.parseLong(nodeid));
		write(JSONUtil.serialize(model));
	}

	public void saveNode() throws JSONException {
		model.setEnterpriseCode(employee.getEnterpriseCode());
		PtKkxCFjstate baseInfo = remote.save(model);
		write("{success:true,baseInfo:" + JSONUtil.serialize(baseInfo) + "}");
	}

	public void updateNode() {
		model.setEnterpriseCode(employee.getEnterpriseCode());
		remote.update(model);
		write("{success:true}");

	}
	public PtKkxCFjstate getModel() {
		return model;
	}
	public void setModel(PtKkxCFjstate model) {
		this.model = model;
	}


}
