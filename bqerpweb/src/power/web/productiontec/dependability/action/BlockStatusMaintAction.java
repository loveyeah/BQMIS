package power.web.productiontec.dependability.action;

import java.util.List;

import power.ejb.comm.TreeNode;
import power.ejb.productiontec.dependabilityAnalysis.JsJKkxJzztwh;
import power.ejb.productiontec.dependabilityAnalysis.JsJKkxJzztwhFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BlockStatusMaintAction extends AbstractAction {
	public JsJKkxJzztwhFacadeRemote remote;
	public JsJKkxJzztwh model;

	public BlockStatusMaintAction() {
		remote = (JsJKkxJzztwhFacadeRemote) factory
				.getFacadeRemote("JsJKkxJzztwhFacade");
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
		JsJKkxJzztwh model = remote.findById(Long.parseLong(nodeid));
		write(JSONUtil.serialize(model));
	}

	public void saveNode() throws JSONException {
		model.setEnterpriseCode(employee.getEnterpriseCode());
		JsJKkxJzztwh baseInfo = remote.save(model);
		write("{success:true,baseInfo:" + JSONUtil.serialize(baseInfo) + "}");
	}

	public void updateNode() {
		model.setEnterpriseCode(employee.getEnterpriseCode());
		remote.update(model);
		write("{success:true}");

	}

	public JsJKkxJzztwh getModel() {
		return model;
	}

	public void setModel(JsJKkxJzztwh model) {
		this.model = model;
	}
}
