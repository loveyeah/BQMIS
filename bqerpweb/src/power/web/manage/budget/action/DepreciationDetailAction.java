package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmJDepreciationDetailFacadeRemote;
import power.ejb.manage.budget.CbmJDepreciationFacadeRemote;
import power.ejb.manage.budget.form.CbmJDepreciationForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class DepreciationDetailAction extends AbstractAction {

	private CbmJDepreciationDetailFacadeRemote detailRemote;
	private CbmJDepreciationFacadeRemote remote;
	private Long depreciationId;
	
	public DepreciationDetailAction() {
		remote = (CbmJDepreciationFacadeRemote) factory
				.getFacadeRemote("CbmJDepreciationFacade");
		detailRemote = (CbmJDepreciationDetailFacadeRemote) factory
				.getFacadeRemote("CbmJDepreciationDetailFacade");
	}

	
	@SuppressWarnings("unchecked")
	public void saveDepreciationRecord() throws JSONException
	{
		String addString = request.getParameter("isAdd");
		String updateString = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		
		Object addObject = JSONUtil.deserialize(addString);
		Object updateObject = JSONUtil.deserialize(updateString);
		List<Map> alist  = (List<Map>)addObject;
		List<Map> ulist = (List<Map>)updateObject;
		List<CbmJDepreciationForm> addList = new ArrayList<CbmJDepreciationForm>();
		List<CbmJDepreciationForm> updateList = new ArrayList<CbmJDepreciationForm>();
		
		for(Map data : alist)
		{
			CbmJDepreciationForm temp = this.parseDepreciationDetail(data);		
			addList.add(temp);
		}
		for(Map data : ulist)
		{
			CbmJDepreciationForm temp = this.parseDepreciationDetail(data);
			updateList.add(temp);
		}
		
		if(addList != null || updateList != null || ids != null)
		{
			if(addList.size() >0 || updateList.size() > 0 || ids.length() > 0)
			{
				detailRemote.saveDepreciationDetails(addList, updateList, ids);
				write("{success: true,msg:'数据操作成功！'}");
			}
		}		
	}
	
	@SuppressWarnings("unchecked")
	public CbmJDepreciationForm parseDepreciationDetail(Map data)
	{
		CbmJDepreciationForm temp = new CbmJDepreciationForm();
		
		Long depreciationDetailId = null;
		Long depreciationId = null;
		String assetName = null;
		Double lastAsset = 0.0;
		Double addAsset = 0.0;
		Double reduceAsset = 0.0;
		Double newAsset = 0.0;
		Double depreciationRate = 0.0;
		Double depreciationNumber = 0.0;
		Double depreciationSum = 0.0;
		String memo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();

		String budgetTime = null;
		Long workFlowNo = null;
		String workFlowStatus = null;
		
		if (data.get("depreciationId") != null&& !"".equals(data.get("depreciationId"))) {
			depreciationId = Long.parseLong(data.get("depreciationId").toString());
		}
		if(data.get("budgetTime") != null){
			budgetTime = data.get("budgetTime").toString();
		}
		if(data.get("workFlowNo") != null){
			workFlowNo = Long.parseLong(data.get("workFlowNo").toString());
		}
		if(data.get("workFlowStatus") != null){
			workFlowStatus = data.get("workFlowStatus").toString();
		}
		if (data.get("depreciationDetailId") != null&& !"".equals(data.get("depreciationDetailId"))) {
			depreciationDetailId = Long.parseLong(data.get("depreciationDetailId").toString());
		}
		if (data.get("assetName") != null&& !"".equals(data.get("assetName"))) {
			assetName = data.get("assetName").toString();
		}
		if (data.get("lastAsset") != null&& !"".equals(data.get("lastAsset"))) {
			lastAsset = Double.parseDouble(data.get("lastAsset").toString());
		}
		if (data.get("addAsset") != null&& !"".equals(data.get("addAsset"))) {
			addAsset = Double.parseDouble(data.get("addAsset").toString());
		}
		if (data.get("reduceAsset") != null&& !"".equals(data.get("reduceAsset"))) {
			reduceAsset = Double.parseDouble(data.get("reduceAsset").toString());
		}
		if (data.get("newAsset") != null&& !"".equals(data.get("newAsset"))) {
			newAsset = Double.parseDouble(data.get("newAsset").toString());
		}
		if (data.get("depreciationRate") != null&& !"".equals(data.get("depreciationRate"))) {
			depreciationRate = Double.parseDouble(data.get("depreciationRate").toString());
		}
		if (data.get("depreciationNumber") != null&& !"".equals(data.get("depreciationNumber"))) {
			depreciationNumber = Double.parseDouble(data.get("depreciationNumber").toString());
		}
		if (data.get("depreciationSum") != null&& !"".equals(data.get("depreciationSum"))) {
			depreciationSum = Double.parseDouble(data.get("depreciationSum").toString());
		}
		if (data.get("memo") != null && !"".equals(data.get("memo"))) {
			memo = data.get("memo").toString();
		}
	
		temp.setDepreciationDetailId(depreciationDetailId);
		temp.setAddAsset(addAsset);
		temp.setAssetName(assetName);
		temp.setBudgetTime(budgetTime);
		temp.setDepreciationId(depreciationId);
		temp.setDepreciationNumber(depreciationNumber);
		temp.setDepreciationRate(depreciationRate);
		temp.setDepreciationSum(depreciationSum);
		temp.setEnterpriseCode(enterpriseCode);
		temp.setIsUse(isUse);
		temp.setLastAsset(lastAsset);
		temp.setMemo(memo);
		temp.setNewAsset(newAsset);
		temp.setReduceAsset(reduceAsset);
		temp.setWorkFlowNo(workFlowNo);
		temp.setWorkFlowStatus(workFlowStatus);
		
		return temp;
	}
	
	
	/**
	 * 查找预算明细记录列表
	 * 
	 * @throws JSONException
	 */
	public void findDepreciationList() throws JSONException {
		String budgetTime = request.getParameter("budgetTime");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = detailRemote.findAllList(budgetTime, employee.getEnterpriseCode(), start, limit);
		} else {
			object = detailRemote.findAllList(budgetTime, employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	public Long getDepreciationId() {
		return depreciationId;
	}

	public void setDepreciationId(Long depreciationId) {
		this.depreciationId = depreciationId;
	}
}
