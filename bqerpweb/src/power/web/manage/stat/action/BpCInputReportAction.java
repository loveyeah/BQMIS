package power.web.manage.stat.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCInputReport;
import power.ejb.manage.stat.BpCInputReportFacadeRemote;
import power.ejb.manage.stat.BpCStatItem;
import power.ejb.manage.stat.BpCStatItemFacadeRemote;
import power.ejb.manage.stat.form.InputReprotItemForm;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BpCInputReportAction extends AbstractAction {

	private BpCInputReportFacadeRemote remote;
	private BpCStatItemFacadeRemote statItemRemote;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */

	public BpCInputReportAction() {
		remote = (BpCInputReportFacadeRemote) factory
				.getFacadeRemote("BpCInputReportFacade");
		statItemRemote = (BpCStatItemFacadeRemote) factory
		.getFacadeRemote("BpCStatItemFacade");
	}

	/**
	 * 新增，修改，删除记录方法
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveBpCInputReport() throws Exception {
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<BpCInputReport> addList = new ArrayList<BpCInputReport>();
			List<BpCInputReport> updateList = new ArrayList<BpCInputReport>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				BpCInputReport model = new BpCInputReport();
				model.setEnterpriseCode(employee.getEnterpriseCode());
				if (data.get("reportCode") != null
						&& !"".equals(data.get("reportCode"))) {
					model.setReportCode(Long.valueOf(data.get("reportCode")
							.toString()));
				}
				if (data.get("reportName") != null
						&& !"".equals(data.get("reportName"))) {
					model.setReportName(data.get("reportName").toString());
				}
				if (data.get("reportType") != null
						&& !"".equals(data.get("reportType"))) {
					model.setReportType(data.get("reportType").toString());
				}
				if (data.get("timeDelay") != null
						&& !"".equals(data.get("timeDelay"))) {
					model.setTimeDelay((Long) data.get("timeDelay"));
				}
				if (data.get("timeUnit") != null
						&& !"".equals(data.get("timeUnit"))) {
					model.setTimeUnit(data.get("timeUnit").toString());
				}
				if (data.get("displayNo") != null
						&& !"".equals(data.get("displayNo"))) {
					model.setDisplayNo(Long.parseLong(data.get("displayNo")
							.toString()));
				}
				if (data.get("groupNature") != null
						&& !"".equals(data.get("groupNature")))
					model.setGroupNature(data.get("groupNature").toString());
				// 增加
				if (model.getReportCode() == null
						|| "".equals(model.getReportCode())) {
					if (remote.checkReportName(model.getReportName()) > 0) {
						write("{success: false,msg:'报表名称不能重复！'}");
						return;
					} else {
						addList.add(model);
					}
				} else {
					if (!model.getReportName().equals(
							remote.findById(model.getReportCode())
									.getReportName())) {
						if (remote.checkReportName(model.getReportName()) > 0) {
							write("{success: false,msg:'报表名称不能重复！'}");
							return;
						} else {
							updateList.add(model);
						}
					} else {
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

			write("{success: true,msg:'保存成功！'}");

		} catch (Exception exc) {
			write("{success: false,msg:'操作失败'}");
			throw exc;

		}
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */

	public void getBpCInputReportList() throws JSONException {
		String type = request.getParameter("type");
		
		PageObject obj = null;
		if (start >= 0 && limit >= 0)
			obj = remote.findAll(employee.getEnterpriseCode(), type,employee.getWorkerCode(), start,
					limit);
		else
			obj = remote.findAll(employee.getEnterpriseCode(), type,employee.getWorkerCode());
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 班组报表录入查询
	 */
	public void getGroupReportItem() throws JSONException {
		String reportCode = request.getParameter("reportcode");
		String date = request.getParameter("reportdate");
		PageObject pg = remote.findGroupReportItem(date, reportCode);
		if (pg.getTotalCount() > 0) {
			write(JSONUtil.serialize(pg));
		} else {
			write("{list:[],totalCount:0}");
		}
	}

	/**
	 * 班组录入保存
	 * @throws JSONException 
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public void saveGroupReportValue() throws JSONException, ParseException{
		String str = request.getParameter("isUpdate");
		String reportCode = request.getParameter("reportcode");
		String date = request.getParameter("reportdate");
		Object obj = JSONUtil.deserialize(str);
		List<InputReprotItemForm> updateList = new ArrayList<InputReprotItemForm>();
		List<Map> list = (List<Map>) obj;
		for (Map data : list) {
			InputReprotItemForm model = new InputReprotItemForm();
			if (data.get("itemCode") != null
					&& !"".equals(data.get("itemCode"))) {
				model.setItemCode(data.get("itemCode")
						.toString());
			}
				//保存班组时间
				model.setDate(date);
			//保存班组班次
				BpCStatItem entity= statItemRemote.findById(data.get("itemCode").toString());
				model.setUnitName(entity.getGroupNature());
			if (data.get("dataValue") != null
					&& !"".equals(data.get("dataValue"))) {
				model.setDataValue(Double.parseDouble(data.get("dataValue").toString()));
			}
			updateList.add(model);
		}
		remote.saveGroupReportValue(updateList);
		write("{success: true,msg:'保存成功！'}");

	
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
