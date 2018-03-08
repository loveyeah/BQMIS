package power.web.manage.budget.action;

import java.util.List;

import javax.ejb.EJB;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.manage.budget.CbmCFinanceItem;
import power.ejb.manage.budget.CbmCFinanceItemFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class FinanceItemAction extends AbstractAction{
	CbmCFinanceItemFacadeRemote remote;
	public CbmCFinanceItem model;
	// 父指标编码
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * 构造函数
	 * */
	public FinanceItemAction() {
		remote = (CbmCFinanceItemFacadeRemote)factory
						.getFacadeRemote("CbmCFinanceItemFacade");
	}
	/*指标树*/
	public void findFinanceTree(){
		String pid = request.getParameter("pid");
		try {
			List<TreeNode> list = remote.findFinanceItemTreeList(pid, employee
					.getEnterpriseCode());
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			exc.printStackTrace();
			write("[]");
		}
	}
	/*获得节点信息*/
	public void findNodeInfo() throws JSONException {
		String nodeid = request.getParameter("nodeid");
		CbmCFinanceItem model = remote.findById(Long.parseLong(nodeid));
		write(JSONUtil.serialize(model));
	}
	/*保存*/
	public void saveNode() throws JSONException {
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setIsUse("Y");
		CbmCFinanceItem baseInfo = remote.save(model);
		if(baseInfo == null)
		{
			write("{success:true,msg:'该财务科目编码已经存在！'}");
		}
		else 
		write("{success:true,baseInfo:" + JSONUtil.serialize(baseInfo) + "}");
	}
	/*更新*/
	public void updateNode() {
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setIsUse("Y");
		CbmCFinanceItem baseInfo = remote.update(model);
		if(baseInfo == null)
		{
			write("{success:true,msg:'该财务科目编码已经存在！'}");
		}
		write("{success:true}");

	}
	/*删除*/
	public void deleteNode() {
		Long nodeid = model.getFinanceId();
		remote.delete(remote.findById(nodeid));
		write("{success:true}");
	}
	public CbmCFinanceItem getModel() {
		return model;
	}
	public void setModel(CbmCFinanceItem model) {
		this.model = model;
	}

}
