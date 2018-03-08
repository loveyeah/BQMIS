package power.web.equ.workbill.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;

import power.ejb.equ.workbill.EquJOrderstep;
import power.ejb.equ.workbill.EquJOrderstepFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquJOrderstepAction extends AbstractAction {

	private EquJOrderstepFacadeRemote remote;
	private String ids;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquJOrderstepAction() {
		remote = (EquJOrderstepFacadeRemote) factory
				.getFacadeRemote("EquJOrderstepFacade");
	}

	/**
	 * 增加记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveEquJOrderstep() {
		try {
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String woCode=request.getParameter("woCode");
			Object obj = JSONUtil.deserialize(str);

			List<EquJOrderstep> addList = new ArrayList<EquJOrderstep>();
			List<EquJOrderstep> updateList = new ArrayList<EquJOrderstep>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				EquJOrderstep baseInfo = new EquJOrderstep();
				if (data.get("id") != null) {
					baseInfo.setId(Long.parseLong(data.get("id").toString()));
				}
				if (data.get("planDescription") != null) {
					baseInfo.setPlanDescription(data.get("planDescription")
							.toString());
				}
				if (data.get("planOpDuration") != null && !("").equals(data.get("planOpDuration"))) {
					baseInfo.setPlanOpDuration(Double.parseDouble(data.get(
							"planOpDuration").toString()));
				}
				if (data.get("operationStep") != null) {
					baseInfo.setOperationStep(data.get("operationStep")
							.toString());
				}
				if (data.get("planOperationStepTitle") != null) {
					baseInfo.setPlanOperationStepTitle(data.get(
							"planOperationStepTitle").toString());
				}
				if (data.get("orderby") != null
						&& !("").equals(data.get("orderby"))) {
					baseInfo.setOrderby(Long.parseLong(data.get("orderby")
							.toString()));
				}
				if (data.get("planPointName") != null) {
					baseInfo.setPlanPointName(data.get("planPointName")
							.toString());
				}
				if (woCode != null) {
					baseInfo.setWoCode(woCode);
				}
				if (data.get("factDescription") != null) {
					baseInfo.setFactDescription(data.get("factDescription")
							.toString());
				}
				if (data.get("factOpDuration") != null) {
					baseInfo.setFactOpDuration(Double.parseDouble(data.get(
							"factOpDuration").toString()));
				}
				if (data.get("factOperationStepTitle") != null) {
					baseInfo.setFactOperationStepTitle(data.get(
							"factOperationStepTitle").toString());
				}
				if (data.get("factPointName") != null) {
					baseInfo.setFactPointName(data.get("factPointName")
							.toString());
				}

				// 增加
				if (baseInfo.getId() == null) {
					baseInfo.setEnterprisecode(employee.getEnterpriseCode());
					addList.add(baseInfo);
				} else {
					// 修改

					EquJOrderstep model = new EquJOrderstep();
					model = remote.findById(baseInfo.getId());

					if (baseInfo.getPlanDescription() != null)
						model.setPlanDescription(baseInfo.getPlanDescription());
					if (baseInfo.getPlanOpDuration() != null)
						model.setPlanOpDuration(baseInfo.getPlanOpDuration());
					if (baseInfo.getPlanOperationStepTitle() != null)
						model.setPlanOperationStepTitle(baseInfo.getPlanOperationStepTitle());
					if (baseInfo.getPlanPointName() != null)
						model.setPlanPointName(baseInfo.getPlanPointName());

					if (baseInfo.getFactDescription() != null)
						model.setFactDescription(baseInfo.getFactDescription());
					if (baseInfo.getFactOpDuration() != null)
						model.setFactOpDuration(baseInfo.getFactOpDuration());
					if (baseInfo.getFactOperationStepTitle() != null)
						model.setFactOperationStepTitle(baseInfo.getFactOperationStepTitle());
					if (baseInfo.getFactPointName() != null)
						model.setFactPointName(baseInfo.getFactPointName());

					if (baseInfo.getOrderby() != null)
						model.setOrderby(baseInfo.getOrderby());

					// baseInfo.setEnterprisecode(model.getEnterprisecode());
					// baseInfo.setIfUse(model.getIfUse());
					// baseInfo.setOperationStep(model.getOperationStep());
					// if (baseInfo.getOrderby() == null)
					// baseInfo.setOrderby(model.getOrderby());
					//					

					updateList.add(model);
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
	public void deleteEquJOrderstep() {
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
	public void getEquJOrderstepList() throws JSONException {
		String woCode = request.getParameter("woCode");
		PageObject obj = remote.findAll(woCode, employee.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
	}
	public void getEquCOrderstepList() throws JSONException {
		String woCode = request.getParameter("woCode");
		PageObject obj = remote.getEquCOrderstepList(woCode, employee.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
	}
	

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
