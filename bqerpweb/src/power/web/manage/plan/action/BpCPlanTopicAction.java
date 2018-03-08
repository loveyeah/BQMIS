package power.web.manage.plan.action;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ear.comm.ejb.PageObject;

import power.ejb.manage.plan.BpCPlanTopic;
import power.ejb.manage.plan.BpCPlanTopicFacadeRemote;
import power.ejb.manage.plan.BpCPlanTopicItem;
import power.ejb.manage.plan.BpCPlanTopicItemFacadeRemote;
import power.ejb.manage.plan.BpCPlanTopicItemId;

import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BpCPlanTopicAction extends AbstractAction {
	private BpCPlanTopicItemFacadeRemote itemRemote;
	private BpCPlanTopicFacadeRemote remote;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public BpCPlanTopicAction() {
		itemRemote = (BpCPlanTopicItemFacadeRemote) factory
				.getFacadeRemote("BpCPlanTopicItemFacade");
		remote = (BpCPlanTopicFacadeRemote) factory
				.getFacadeRemote("BpCPlanTopicFacade");
	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveBpCPlanTopicItem() {

		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");

			Object obj = JSONUtil.deserialize(str);

			List<BpCPlanTopicItem> addList = new ArrayList<BpCPlanTopicItem>();
			List<BpCPlanTopicItem> updateList = new ArrayList<BpCPlanTopicItem>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				String topicCode = null;

				String itemCode = null;
				String itemName = null;
				String displayNo = null;

				if (data.get("baseInfo.id.topicCode") != null) {
					topicCode = data.get("baseInfo.id.topicCode").toString();
				}

				if (data.get("baseInfo.id.itemCode") != null) {
					itemCode = data.get("baseInfo.id.itemCode").toString();
				}
				if (data.get("baseInfo.id.itemName") != null) {
					itemName = data.get("baseInfo.id.itemName").toString();
				}
				if (data.get("baseInfo.displayNo") != null) {
					displayNo = data.get("baseInfo.displayNo").toString();
				}

				BpCPlanTopicItem model = new BpCPlanTopicItem();
				BpCPlanTopicItemId id = new BpCPlanTopicItemId();
				// 增加
				if (itemRemote.isNew(topicCode, itemCode) == 0) {
					id.setItemCode(itemCode);
					id.setTopicCode(topicCode);

					model.setId(id);
					model.setEnterpriseCode(employee.getEnterpriseCode());

					if (displayNo != null && !displayNo.equals(""))
						model.setDisplayNo(Long.parseLong(displayNo));

					addList.add(model);
				} else {
					id.setTopicCode(topicCode);
					id.setItemCode(itemCode);

					model = itemRemote.findById(id);

					if (displayNo != null && !displayNo.equals(""))
						model.setDisplayNo(Long.parseLong(displayNo));

					updateList.add(model);
				}
			}

			if (addList.size() > 0 || updateList.size() > 0
					|| (deleteIds != null && !deleteIds.trim().equals("")))
				itemRemote.save(addList, updateList, deleteIds);

			write("{success: true,msg:'保存成功!'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败!'}");
		}
	}

	public void getBpCPlanTopicItem() throws JSONException {
		String topicCode = request.getParameter("topicCode");
		PageObject obj = itemRemote.findAll(employee.getEnterpriseCode(),
				topicCode, start, limit);

		write(JSONUtil.serialize(obj));
	}

	public void saveBpCPlanTopic() throws Exception {
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<BpCPlanTopic> addList = new ArrayList<BpCPlanTopic>();
			List<BpCPlanTopic> updateList = new ArrayList<BpCPlanTopic>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				String topicCode = null;
				String topicName = null;
				String topicMemo = null;
				String displayNo = null;

				if (data.get("topicCode") != null) {
					topicCode = data.get("topicCode").toString();
				}

				if (data.get("topicName") != null) {
					topicName = data.get("topicName").toString();
				}
				if (data.get("topicMemo") != null) {
					topicMemo = data.get("topicMemo").toString();
				}

				if (data.get("displayNo") != null) {
					displayNo = data.get("displayNo").toString();
				}

				BpCPlanTopic model = new BpCPlanTopic();

				// 增加
				if (topicCode == null) {
					if (remote.checkTopicName(topicName) > 0) {
						write("{success: false,msg:'主题名称已被占用!'}");
						return;
					} else {
						model.setTopicCode(topicCode);
						model.setTopicName(topicName);
						model.setTopicMemo(topicMemo);
						if (displayNo != null && !displayNo.equals(""))
							model.setDisplayNo(Long.parseLong(displayNo));

						model.setEnterpriseCode(employee.getEnterpriseCode());

						addList.add(model);
					}
				} else {
					model = remote.findById(topicCode);
					if ((topicName.equals(model.getTopicName()) && remote
							.checkTopicName(topicName) > 1)
							|| (!topicName.equals(model.getTopicName()) && remote
									.checkTopicName(topicName) > 0)) {
						write("{success: false,msg:'主题名称已被占用!'}");
						return;
					} else {
						model.setTopicCode(topicCode);
						model.setTopicName(topicName);
						model.setTopicMemo(topicMemo);
						if (displayNo != null && !displayNo.equals(""))
							model.setDisplayNo(Long.parseLong(displayNo));

						updateList.add(model);
					}
				}

			}

			if (addList.size() > 0)
				remote.save(addList);

			if (updateList.size() > 0)

				remote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))

				remote.delete(deleteIds);

			write("{success: true,msg:'保存成功!'}");

		} catch (Exception exc) {
			write("{success: false,msg:'操作失败!'}");
			throw exc;

		}
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */

	public void getBpCPlanTopicList() throws JSONException {
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), start,
				limit);

		write(JSONUtil.serialize(obj));
	}

	public void getAllFitItemaList() throws JSONException {

		String argFuzzy = request.getParameter("argFuzzy");

		PageObject obj = remote.findAllFitItem(argFuzzy, start, limit);

		write(JSONUtil.serialize(obj));
	}

	// ******************************************get/set变量方法******************************************

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
