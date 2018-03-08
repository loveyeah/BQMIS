package power.web.manage.plan.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.BpCPlanTopic;
import power.ejb.manage.plan.BpCPlanTopicFacadeRemote;
import power.ejb.manage.plan.BpCPlanTopicItemFacadeRemote;

import power.ejb.manage.plan.BpJPlanTopic;
import power.ejb.manage.plan.BpJPlanTopicFacadeRemote;
import power.ejb.manage.plan.BpJPlanTopicItem;
import power.ejb.manage.plan.BpJPlanTopicItemFacadeRemote;

import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class MainItemInputAction extends AbstractAction {
	protected BpJPlanTopicFacadeRemote JTRemote;
	protected BpJPlanTopicItemFacadeRemote JTIRemote;
	protected BpCPlanTopicFacadeRemote CTRemote;
	protected BpCPlanTopicItemFacadeRemote CTIRemote;

	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public MainItemInputAction() {
		JTRemote = (BpJPlanTopicFacadeRemote) factory
				.getFacadeRemote("BpJPlanTopicFacade");
		JTIRemote = (BpJPlanTopicItemFacadeRemote) factory
				.getFacadeRemote("BpJPlanTopicItemFacade");
		CTRemote = (BpCPlanTopicFacadeRemote) factory
				.getFacadeRemote("BpCPlanTopicFacade");
		CTIRemote = (BpCPlanTopicItemFacadeRemote) factory
				.getFacadeRemote("BpCPlanTopicItemFacade");
	}

	/**
	 * 取得维护的计划主题
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getPlanTopic() throws Exception {
		List<BpCPlanTopic> list = CTRemote.findAll(
				employee.getEnterpriseCode(), new int[] { 0, 0 }).getList();
		String str = "[";
		int i = 0;
		for (BpCPlanTopic model : list) {
			i++;
			str += "[\"" + model.getTopicName() + "\",\""
					+ model.getTopicCode() + "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		str += "]";
		write(str);
	}

	public void deleteAllRefer() throws JSONException {
		try {
			String planTime = request.getParameter("planTime");
			String topicCode = request.getParameter("topicCode");
			String reportIdString = JTRemote.findByPlanTimeAndTopicCode(
					planTime, topicCode);
			JTIRemote.deleteByReportId(reportIdString);
			JTRemote.delete(JTRemote.findById(Long.parseLong(reportIdString)));

		} catch (Exception e) {
			write("success:false,msg:'删除失败！'");
		}

	}

	public void queryBpJPlanTopicItemList() throws JSONException {

		String planTime = request.getParameter("planTime");
		String topicCode = request.getParameter("topicCode");
		
		PageObject obj = new PageObject();

		obj = JTIRemote.queryBpJPlanTopicItemList(planTime, topicCode, start,
				limit);

		write(JSONUtil.serialize(obj));
	}

	public BpJPlanTopic saveBpJPlanTopic() throws ParseException {

		String planTime = request.getParameter("planTime");
		String topicCode = request.getParameter("topicCode");
		BpJPlanTopic model = new BpJPlanTopic();
		model.setEditBy(employee.getWorkerCode());
		model.setEditDate(new Date());
		model.setTopicCode(topicCode);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date planDate = sdf.parse(planTime);
		model.setPlanTime(planDate);

		model.setEnterpriseCode(employee.getEnterpriseCode());
		BpJPlanTopic baseInfoBpJPlanTopic = JTRemote.save(model);
		return baseInfoBpJPlanTopic;

	}

	public void updateBpJPlanTopic(String mainId) throws ParseException {

		BpJPlanTopic model = JTRemote.findById(Long.parseLong(mainId));
		model.setEditBy(employee.getWorkerCode());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date editDate = sdf.parse(sdf.format(new Date()));
		model.setEditDate(editDate);

		JTRemote.update(model);

	}

	public void saveBpJPlanTopicAndItem() throws Exception {
		try {
			String planTime = request.getParameter("planTime");
			String topicCode = request.getParameter("topicCode");

			String reportId = JTRemote.findByPlanTimeAndTopicCode(planTime,
					topicCode);
			if (reportId == null) {

				reportId = saveBpJPlanTopic().getReportId().toString();

			} else {
				updateBpJPlanTopic(reportId);
			}
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<BpJPlanTopicItem> addList = new ArrayList<BpJPlanTopicItem>();
			List<BpJPlanTopicItem> updateList = new ArrayList<BpJPlanTopicItem>();

			List<Map> list = (List<Map>) obj;

			for (Map data : list) {

				String topicItemId = null;
				String itemCode = null;
				String planValue = null;
				String lastValue = null;

				if (data.get("topicItemId") != null) {
					topicItemId = data.get("topicItemId").toString();
				}
				if (data.get("itemCode") != null) {
					itemCode = data.get("itemCode").toString();
				}
				if (data.get("planValue") != null) {
					planValue = data.get("planValue").toString();
				}
				if (data.get("lastValue") != null) {
					lastValue = data.get("lastValue").toString();
				}

				BpJPlanTopicItem model = new BpJPlanTopicItem();

				// 增加
				if (topicItemId == null) {

					model.setItemCode(itemCode);

					if (lastValue != null && !lastValue.equals(""))
						model.setLastValue(Double.parseDouble(lastValue));

					if (planValue != null && !planValue.equals(""))
						model.setPlanValue(Double.parseDouble(planValue));

					model.setReportId(Long.parseLong(reportId));

					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);

				} else {
					model = JTIRemote.findById(Long.parseLong(topicItemId));

					model.setItemCode(itemCode);

					if (lastValue != null && !lastValue.equals(""))
						model.setLastValue(Double.parseDouble(lastValue));

					if (planValue != null && !planValue.equals(""))
						model.setPlanValue(Double.parseDouble(planValue));

					updateList.add(model);

				}

			}

			if ((addList.size() > 0) || (updateList.size() > 0)
					|| (deleteIds != null && !deleteIds.trim().equals("")))
				JTIRemote.save(addList, updateList, deleteIds);

			write("{success:true,msg:'保存成功！'}");

		} catch (Exception exc) {
			write("{success:false,msg:'操作失败'}");
			throw exc;

		}
	}
}
