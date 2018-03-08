package power.web.manage.examine.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import power.ejb.manage.exam.BpCCbmDep;
import power.ejb.manage.exam.BpCCbmOverheadItem;
import power.ejb.manage.exam.BpJCbmDepSeason;
import power.ejb.manage.exam.BpJCbmDepSeasonFacadeRemote;
import power.ejb.manage.exam.form.BpCCbmDepForm;
import power.ejb.manage.exam.form.BpJCbmDepSeasonValue;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class OverHeadAction extends AbstractAction {
	private BpJCbmDepSeasonFacadeRemote remote;

	public OverHeadAction() {
		remote = (BpJCbmDepSeasonFacadeRemote) factory
				.getFacadeRemote("BpJCbmDepSeasonFacade");
	}

	public void getDeptList() throws JSONException {
		List<BpCCbmDepForm> result = remote.getBpDeptList(employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(result));
	}

	@SuppressWarnings("unchecked")
	public void saveDeptList() throws JSONException {
		String str = request.getParameter("isUpdate");
		String deleteIds = request.getParameter("isDelete");
		Object obj = JSONUtil.deserialize(str);
		List<BpCCbmDep> addList = new ArrayList<BpCCbmDep>();
		List<BpCCbmDep> updateList = new ArrayList<BpCCbmDep>();
		List<Map> list = (List<Map>) obj;
		for (Map data : list) {
			BpCCbmDep model = new BpCCbmDep();
			if (data.get("cdInfo.depId") != null)
				if (!("").equals(data.get("cdInfo.depId").toString()))
					model.setDepId(Long.parseLong(data.get("cdInfo.depId")
							.toString()));
			if (data.get("cdInfo.depCode") != null)
				model.setDepCode(data.get("cdInfo.depCode").toString());
			if (data.get("cdInfo.memo") != null)
				model.setMemo(data.get("cdInfo.memo").toString());
			model.setIsUse("Y");
			model.setEnterpriseCode("hfdc");
			if (model.getDepId() == null)
				addList.add(model);
			else
				updateList.add(model);
		}
		if (remote.saveBpDeptList(addList, updateList, deleteIds))
			write("{success:true}");
		else
			write("{success:false}");

	}

	public void getOverheadList() throws JSONException {
		List<BpCCbmOverheadItem> list = remote.getOverheadList(employee
				.getEnterpriseCode());
		if (list != null || list.size() > 0) {
			write(JSONUtil.serialize(list));
		} else {
			write("[]");
		}
	}

	@SuppressWarnings("unchecked")
	public void saveOverheadList() throws JSONException {
		try { 
			String modifyData = request.getParameter("modifyData");
			String deleteIds = request.getParameter("deleteIds");
			Object obj = JSONUtil.deserialize(modifyData);
			remote.saveBpCCbmOverheadItem((List<Map>) obj, deleteIds, employee
					.getEnterpriseCode());
			write("{success:true}");
		} catch (Exception exc) {
			write("{success:false}");
		}
	}

	public void getOverInputList() {

		String datetime = request.getParameter("datetime");
		datetime = (datetime == null) ? "2009" : datetime;
		List<BpCCbmDepForm> deptList = remote.getBpDeptList(employee
				.getEnterpriseCode());
		// Iterator deptItem = deptList.iterator();
		List<BpCCbmOverheadItem> itemList = remote.getOverheadList(employee
				.getEnterpriseCode());// .getList();///wzhyan modify
		String str = "{";
		String strData = "'data':[";
		String strCol = "'columModle':[{'header':'部门名称','width':100,'dataIndex': 'deptname','align':'center'},";
		String strRow = "'rows':[[{},";
		String strFild = "'fieldsNames':[{'name':'deptname'},{'name':'deptid'},";
		String colHeadText = "";
		int m = 0;
		int n = 0;
		if (deptList.size() > 0)
			for (int j = 0; j < deptList.size(); j++) {
				n++;
				BpCCbmDepForm dmodel = deptList.get(j);// (BpCCbmDepForm)
														// deptItem.next();
				strData += "{'deptname':'" + dmodel.getDeptname()
						+ "','deptid':'" + dmodel.getCdInfo().getDepId() + "',";
				Iterator itemItem = itemList.iterator();
				if (itemList.size() > 0)
					while (itemItem.hasNext()) {
						m++;
						BpCCbmOverheadItem omodel = (BpCCbmOverheadItem) itemItem
								.next();
						strData += "'" + omodel.getItemCode()
								+ "overheadid':'"
								+ omodel.getOverheadItemId() + "',";
						if (n == 1)
							strFild += "{'name':'"
									+ omodel.getItemCode()
									+ "overheadid'},";
						BpJCbmDepSeasonValue model = new BpJCbmDepSeasonValue();
						model = remote.getDataInputList(dmodel.getCdInfo()
								.getDepId().toString(), omodel.getOverheadItemId().toString(), datetime,
								"hfdc");
						String[] valueText = model.getValueList().split(",");
						String[] idText = model.getIdList().split(",");
						for (int i = 1; i < 6; i++) {
							switch (i) {
							case 1:
								colHeadText = "第一季度";
								break;
							case 2:
								colHeadText = "第二季度";
								break;
							case 3:
								colHeadText = "第三季度";
								break;
							case 4:
								colHeadText = "第四季度";
								break;
							case 5:
								colHeadText = "全年合计";
								break;
							}
							strData += "'"
									+ i
									+ omodel.getItemCode()
									+ "':'"
									+ ((" ").equals(valueText[i - 1]) ? ""
											: valueText[i - 1]) + "','" + i
									+ omodel.getItemCode()
									+ "id':'" + idText[i - 1] + "'";
							if (!(!itemItem.hasNext() && i == 5)) {
								strData += ",";
							}
							if (j == 0) {
								strCol += "{'header': '"
										+ colHeadText
										+ "','width':70,'dataIndex' : '"
										+ i
										+ omodel.getItemCode()
										+ "','align':'center','renderer':function(value, metadata, record){metadata.attr='style=\"white-space:normal;\"';return value;},'editor':new Ext.form.NumberField({allowDecimals : true,decimalPrecision : 0})}";
								strFild += "{'name':'" + i
										+ omodel.getItemCode()
										+ "'},{'name':'" + i
										+ omodel.getItemCode()
										+ "id'}";
								if (!(!itemItem.hasNext() && i == 5)) {
									strFild += ",";
									strCol += ",";
								}
							}
						}
						if (j == 0) {
							strRow += "{'header':'" + omodel.getItemName()
									+ "','colspan':5,'align':'center'}";
							if (m < itemList.size())
								strRow += ",";
						}
					}
				strData += "}";
				if (j != (deptList.size() - 1))
					strData += ",";
			}
		strData += "],";
		strCol += "],";
		strRow += "]],";
		strFild += "]";
		str += strData + strCol + strRow + strFild + "}";
		write(str);

	}

	@SuppressWarnings("unchecked")
	public void saveOverInputList() throws JSONException {
		String str = request.getParameter("addOrUpdateRecords");
		String datetime = request.getParameter("datetime");
		Object obj = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) obj;
		List<BpJCbmDepSeason> addlist = new ArrayList<BpJCbmDepSeason>();
		List<BpJCbmDepSeason> updatelist = new ArrayList<BpJCbmDepSeason>();
		String jd = "";
		String tempdatetime = "";
		for (Map data : list) {
			BpJCbmDepSeason model = new BpJCbmDepSeason();
			if (data.get("itemCode") != null) {
				jd = data.get("itemCode").toString().substring(0, 1);
				tempdatetime = datetime;
				tempdatetime += ("5").equals(jd) ? "" : ("0" + jd);
			}
			if (data.get("deptid") != null)
				model.setDepId(Long.parseLong(data.get("deptid").toString()));
			else {
				continue;
			}
			if (data.get("overheadid") != null)
				model.setOverheadItemId(Long.parseLong(data.get("overheadid")
						.toString()));
			else {
				continue;
			}
			if (data.get("value") != null)
				model.setSeasonValue(Double.parseDouble(data.get("value")
						.toString()));
			else {
				continue;
			}
			model.setYearSeason(tempdatetime);
			if ("null".equals(data.get("id").toString())) {
				addlist.add(model);
			} else {
				updatelist.add(model);
			}
		}
		if (remote.saveDataInputList(addlist, updatelist, employee
				.getEnterpriseCode()))
			write("{success:true}");
		else
			write("{success:false}");

	}
}
