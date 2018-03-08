package power.web.equ.standardpackage.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.standardpackage.EquCStandardOrderstep;
import power.ejb.equ.standardpackage.EquCStandardOrderstepFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquCStandardOrderstepAction extends AbstractAction {
	private EquCStandardOrderstepFacadeRemote remote;
	private String ids;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquCStandardOrderstepAction() {
		remote = (EquCStandardOrderstepFacadeRemote) factory
				.getFacadeRemote("EquCStandardOrderstepFacade");
	}

	/**
	 * 增加记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveEquCStandardOrderstep() {
		try {
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<EquCStandardOrderstep> addList = new ArrayList<EquCStandardOrderstep>();
			List<EquCStandardOrderstep> updateList = new ArrayList<EquCStandardOrderstep>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				EquCStandardOrderstep baseInfo = new EquCStandardOrderstep();
				if (data.get("id") != null) {
					baseInfo.setId(Long.parseLong(data.get("id").toString()));
				}
				if (data.get("description") != null) {
					baseInfo.setDescription(data.get("description").toString());
				}
				if (data.get("opDuration") != null) {
					baseInfo.setOpDuration(Double.parseDouble(data.get(
							"opDuration").toString()));
				}
				if (data.get("operationStep") != null) {
					baseInfo.setOperationStep(data.get("operationStep")
							.toString());
				}
				if (data.get("operationStepTitle") != null) {
					baseInfo.setOperationStepTitle(data.get(
							"operationStepTitle").toString());
				}
				if (data.get("orderby") != null
						&& !("").equals(data.get("orderby"))) {
					baseInfo.setOrderby(Long.parseLong(data.get("orderby")
							.toString()));
				}
				if (data.get("pointName") != null) {
					baseInfo.setPointName(data.get("pointName").toString());
				}
				if (data.get("woCode") != null) {
					baseInfo.setWoCode(data.get("woCode").toString());
				}

				// 增加
				if (baseInfo.getId() == null) {
					baseInfo.setEnterprisecode(employee.getEnterpriseCode());
					addList.add(baseInfo);
				} else {
					// 修改
					EquCStandardOrderstep model = new EquCStandardOrderstep();
					model = remote.findById(baseInfo.getId());
					baseInfo.setEnterprisecode(model.getEnterprisecode());
					baseInfo.setIfUse(model.getIfUse());
					baseInfo.setOperationStep(model.getOperationStep());
					if (baseInfo.getOrderby() == null)
						baseInfo.setOrderby(model.getOrderby());
					updateList.add(baseInfo);
				}
			}

			if ((addList.size() > 0 || updateList.size() > 0)
					|| (deleteIds != null && !deleteIds.trim().equals(""))) {
				remote.save(addList, updateList, deleteIds);
			}
			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	/**
	 * 修改记录
	 * 
	 */
	public void updateEquCStandardOrderstep() {

	}

	/**
	 * 删除记录
	 * 
	 */
	public void deleteEquCStandardOrderstep() {
		try {
			if (remote.delete(ids))
				write("{success:true,msg:'删除成功'}");
			else
				write("{success:false,msg:'删除失败'}");
		} catch (Exception e) {
			write("{success:false,msg:'删除失败'}");
		}
	}

	/**
	 * 取列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getEquCStandardOrderstepList() throws JSONException {
		String woCode = request.getParameter("woCode");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), woCode,
				start, limit);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
