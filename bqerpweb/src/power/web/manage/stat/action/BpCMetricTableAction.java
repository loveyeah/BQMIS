package power.web.manage.stat.action;

import java.text.SimpleDateFormat;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCMetricTable;
import power.ejb.manage.stat.BpCMetricTableFacadeRemote;
import power.ejb.manage.stat.BpCMetricTableId;
import power.ejb.manage.stat.BpCStatItem;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BpCMetricTableAction extends AbstractAction {

	private BpCMetricTableFacadeRemote remote;
	private int start;
	private int limit;

	public BpCMetricTableAction() {
		remote = (BpCMetricTableFacadeRemote) factory
				.getFacadeRemote("BpCMetricTableFacade");
	}

	@SuppressWarnings("unchecked")
	public void saveBpCMetricTable() throws Exception {
		BpCMetricTable model = new BpCMetricTable();
		String itemCode = request.getParameter("itemCode");
		//tableCode,tableName目录未使用,
		String tableCode = "001"; 
		String tableName = request.getParameter("tableName");
		String maxTableValue = request.getParameter("maxTableValue");
		String multiple = request.getParameter("multiple");
		String startValue = request.getParameter("startValue");
		String endValue = request.getParameter("endValue");
		String fixDate = request.getParameter("fixDate");
		String endDate = request.getParameter("endDate");
		String ifUsed = request.getParameter("ifUsed"); 
		BpCMetricTableId  id = new BpCMetricTableId();
		id.setItemCode(itemCode);
		id.setTableCode(tableCode);
		model.setId(id);
		model.setTableName(tableName);
		
		if(maxTableValue!=null&&!maxTableValue.equals(""))
		model.setMaxTableValue(Double.parseDouble(maxTableValue));
		
		if(multiple!=null&&!multiple.equals(""))
		model.setMultiple(Double.parseDouble(multiple));
		
		if(startValue!=null&&!startValue.equals(""))
		model.setStartValue(Double.parseDouble(startValue));
		
		if(endValue!=null&&!endValue.equals(""))
		model.setEndValue(Double.parseDouble(endValue));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(fixDate!=null&&!fixDate.equals(""))
		model.setFixDate(sdf.parse(fixDate));
		
		if(endDate!=null&&!endDate.equals(""))
		model.setEndDate(sdf.parse(endDate));
		model.setIfUsed(ifUsed);
		remote.saveOrUpdate(model);
		write("{success:true}");
//		try { 
//			String str = request.getParameter("isUpdate");
//			String deleteIds = request.getParameter("isDelete");
//			Object obj = JSONUtil.deserialize(str);
//			List<BpCMetricTable> addList = new ArrayList<BpCMetricTable>();
//			List<BpCMetricTableAdd> updateList = new ArrayList<BpCMetricTableAdd>();
//			List<Map> list = (List<Map>) obj;
//			for (Map data : list) {
//				String itemCode = null;
//				String tableCode = null;
//				String itemCodeAdd = null;
//				String tableCodeAdd = null;
//				String maxTableValue = null;
//				String multiple = null;
//				String startValue = null;
//				String endValue = null;
//				String fixDate = null;
//				String endDate = null;
//				String ifUsed = null;
//				if (data.get("baseInfo.id.itemCode") != null) {
//					itemCode = data.get("baseInfo.id.itemCode").toString();
//				}
//				if (data.get("baseInfo.id.tableCode") != null) {
//					tableCode = data.get("baseInfo.id.tableCode").toString();
//				}
//				if (itemCode != null && tableCode != null) {
//					if (data.get("itemCodeAdd") != null) {
//						itemCodeAdd = data.get("itemCodeAdd").toString();
//					}
//					if (data.get("tableCodeAdd") != null) {
//						tableCodeAdd = data.get("tableCodeAdd").toString();
//					}
//					if (data.get("baseInfo.maxTableValue") != null) {
//						maxTableValue = data.get("baseInfo.maxTableValue")
//								.toString();
//					}
//					if (data.get("baseInfo.multiple") != null) {
//						multiple = data.get("baseInfo.multiple").toString();
//					}
//					if (data.get("baseInfo.startValue") != null) {
//						startValue = data.get("baseInfo.startValue").toString();
//					}
//					if (data.get("baseInfo.endValue") != null) {
//						endValue = data.get("baseInfo.endValue").toString();
//					}
//					if (data.get("baseInfo.fixDate") != null) {
//						fixDate = data.get("baseInfo.fixDate").toString();
//					}
//					if (data.get("baseInfo.endDate") != null) {
//						endDate = data.get("baseInfo.endDate").toString();
//					}
//					if (data.get("baseInfo.ifUsed") != null) {
//						ifUsed = data.get("baseInfo.ifUsed").toString();
//					}
//
//					BpCMetricTable model = new BpCMetricTable();
//
//					BpCMetricTableAdd amodel = new BpCMetricTableAdd();
//					BpCMetricTableId id = new BpCMetricTableId();
//
//					// 增加
//					if (itemCodeAdd == null || tableCodeAdd == null) {
//						id.setItemCode(itemCode);
//						id.setTableCode(tableCode);
//						model.setId(id);
//						model.setIfUsed(ifUsed);
//						model.setEnterpriseCode(employee.getEnterpriseCode());
//
//						SimpleDateFormat sdf = new SimpleDateFormat(
//								"yyyy-MM-dd");
//
//						if (maxTableValue != null && !maxTableValue.equals("")) {
//							model.setMaxTableValue(Double
//									.parseDouble(maxTableValue));
//						}
//						if (multiple != null && !multiple.equals("")) {
//							model.setMultiple(Double.parseDouble(multiple));
//						}
//						if (startValue != null && !startValue.equals("")) {
//							model.setStartValue(Double.parseDouble(startValue));
//						}
//						if (endValue != null && !endValue.equals("")) {
//							model.setEndValue(Double.parseDouble(endValue));
//						}
//						if (fixDate != null && !fixDate.equals("")) {
//							model.setFixDate(sdf.parse(fixDate));
//						}
//						if (endDate != null && !endDate.equals("")) {
//							model.setEndDate(sdf.parse(endDate));
//						}
//
//						addList.add(model);
//					} else {
//
//						id.setItemCode(itemCode);
//						id.setTableCode(tableCode);
//						model.setId(id);
//						model.setIfUsed(ifUsed);
//						;
//
//						SimpleDateFormat sdf = new SimpleDateFormat(
//								"yyyy-MM-dd");
//
//						if (maxTableValue != null && !maxTableValue.equals("")) {
//							model.setMaxTableValue(Double
//									.parseDouble(maxTableValue));
//						}
//						if (multiple != null && !multiple.equals("")) {
//							model.setMultiple(Double.parseDouble(multiple));
//						}
//						if (startValue != null && !startValue.equals("")) {
//							model.setStartValue(Double.parseDouble(startValue));
//						}
//						if (endValue != null && !endValue.equals("")) {
//							model.setEndValue(Double.parseDouble(endValue));
//						}
//						if (fixDate != null && !fixDate.equals("")) {
//							model.setFixDate(sdf.parse(fixDate));
//						}
//						if (endDate != null && !endDate.equals("")) {
//							model.setEndDate(sdf.parse(endDate));
//						}
//						amodel.setBaseInfo(model);
//						amodel.setItemCodeAdd(itemCodeAdd);
//						amodel.setTableCodeAdd(tableCodeAdd);
//						updateList.add(amodel);
//					}
//				} else {
//					write("{success: false,msg:'指标编码和表值编码不能为空！'}");
//					return;
//
//				}
//
//			}
//
//			if (addList.size() > 0)
//				remote.save(addList);
//
//			if (updateList.size() > 0)
//
//				remote.update(updateList);
//
//			if (deleteIds != null && !deleteIds.trim().equals(""))
//
//				remote.delete(deleteIds);
//
//			write("{success: true,msg:'保存成功！'}");
//
//		} catch (Exception exc) {
//			write("{success: false,msg:'操作失败'}");
//			throw exc;
//
//		}
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */

	public void getBpCStatItemList() throws JSONException {
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), start,
				limit);

		write(JSONUtil.serialize(obj));
	}
	public void getBpCMetricTable()
	{
		try {
			String itemCode = request.getParameter("itemCode");
			String tableCode = "001";
			BpCMetricTableId id = new BpCMetricTableId(); 
			id.setItemCode(itemCode);
			id.setTableCode(tableCode);
			BpCMetricTable model = remote.findById(id);
			write(JSONUtil.serialize(model));
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void getItemListToUse() throws Exception {
		List<BpCStatItem> list = remote.getItemListToUse(employee
				.getEnterpriseCode());
		String str = "[";
		int i = 0;
		for (BpCStatItem model : list) {
			i++;
			str += "[\"" + model.getItemName() + "\",\"" + model.getItemCode()
					+ "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		str += "]";
		write(str);
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
