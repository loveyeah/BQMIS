package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmCCenterTopic;
import power.ejb.manage.budget.CbmCCenterTopicFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BudgetDeptTopicAction extends AbstractAction {

	private CbmCCenterTopicFacadeRemote remote;
	private int start;
	private int limit;

	public BudgetDeptTopicAction() {
		remote = (CbmCCenterTopicFacadeRemote) factory
				.getFacadeRemote("CbmCCenterTopicFacade");
	}

	public void findDeptTopicByCode() throws JSONException {
		String centerId = request.getParameter("centerId");
		PageObject obj = remote.findAll(centerId, employee.getEnterpriseCode(),
				start, limit);
		write(JSONUtil.serialize(obj));
	}

	public void findDeptTopicInMaint() throws JSONException {
		String isquery = request.getParameter("isquery");// update by ltong
		PageObject obj = remote.findAllInMaint(employee.getEnterpriseCode(),
				isquery, start, limit);
		write(JSONUtil.serialize(obj));
	}

	@SuppressWarnings("unchecked")
	public void addAndUpdateDeptTopic() {
		try {
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");

			Object obj = JSONUtil.deserialize(str);

			List<CbmCCenterTopic> addList = new ArrayList<CbmCCenterTopic>();
			List<CbmCCenterTopic> updateList = new ArrayList<CbmCCenterTopic>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				long centerTopicId = 0;
				long centerId = 0;
				long topicId = 0;
				String directManager = null;

				if (data.get("top.centerTopicId") != null
						&& !data.get("top.centerTopicId").equals("")) {
					centerTopicId = Long.parseLong(data
							.get("top.centerTopicId").toString());
				}
				if (data.get("top.centerId") != null
						&& !data.get("top.centerId").equals("")) {
					centerId = Long.parseLong(data.get("top.centerId")
							.toString());
				}
				if (data.get("top.topicId") != null
						&& !data.get("top.topicId").equals("")) {
					topicId = Long
							.parseLong(data.get("top.topicId").toString());
				}
				if (data.get("top.directManager") != null
						&& !data.get("top.directManager").equals("")) {
					directManager = data.get("top.directManager").toString();
				}

				CbmCCenterTopic model = new CbmCCenterTopic();
				if (remote.isNew(centerTopicId) == 0) {
					model.setCenterTopicId(centerTopicId);
					model.setCenterId(centerId);
					model.setTopicId(topicId);
					if (directManager != null && !directManager.equals("")) {
						model.setDirectManager(directManager);
					}
					model.setIsUse("Y");
					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);
				} else {
					model = remote.findById(centerTopicId);
					model.setCenterId(centerId);
					model.setTopicId(topicId);
					if (directManager != null && !directManager.equals("")) {
						model.setDirectManager(directManager);
					}
					updateList.add(model);

				}
			}
			if (addList.size() > 0)
				remote.save(addList);

			if (updateList.size() > 0)

				remote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))

				remote.deleteMuti(deleteIds);

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
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
}
