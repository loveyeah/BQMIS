package power.web.manage.stat.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCInputReportItem;
import power.ejb.manage.stat.BpCInputReportItemFacadeRemote;
import power.ejb.manage.stat.BpCInputReportItemId;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BpInputReportItemAction extends AbstractAction{

	protected BpCInputReportItemFacadeRemote remote;
	private int start;
	private int limit;
	private long code;
	public BpInputReportItemAction()
	{
		remote = (BpCInputReportItemFacadeRemote)factory.getFacadeRemote("BpCInputReportItemFacade");
	}
	
	/**
	 * 保存操作
	 */
	@SuppressWarnings("unchecked")
	public void addReprotItem()
	{
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String code = request.getParameter("isCode");
			
			Object obj = JSONUtil.deserialize(str);

			List<BpCInputReportItem> addList = new ArrayList<BpCInputReportItem>();
			List<BpCInputReportItem> updateList = new ArrayList<BpCInputReportItem>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				String itemCode = null;
				String reportCode = null;
				String displayNo = null;
				String dataType = null;
				String itemAlias=null;
				String itemBaseName=null;
				if (data.get("model.id.reportCode") != null&& !data.get("model.id.reportCode").equals("")) {
					reportCode = data.get("model.id.reportCode").toString();
				}
				if (data.get("model.id.itemCode") != null&&!data.get("model.id.itemCode").equals("")) {
					itemCode = data.get("model.id.itemCode").toString();
				}
				if (data.get("model.itemAlias") != null&&!data.get("model.itemAlias").equals("")) {
					itemAlias = data.get("model.itemAlias").toString();
				}
				if (data.get("model.displayNo") != null&&!data.get("model.displayNo").equals("")) {
					displayNo = data.get("model.displayNo").toString();
				}
				if(data.get("model.dataType") != null&&!data.get("model.dataType").equals("")){
					dataType = data.get("model.dataType").toString();
				}
				if(data.get("model.itemBaseName") != null&&!data.get("model.itemBaseName").equals("")){
					itemBaseName = data.get("model.itemBaseName").toString();
				}
				BpCInputReportItem model = new BpCInputReportItem();
				BpCInputReportItemId id = new BpCInputReportItemId();
				// 增加
				if (remote.isNew(reportCode, itemCode) == 0) 
				{
					id.setReportCode(Long.parseLong(reportCode));
					id.setItemCode(itemCode);
					model.setId(id);
					model.setDataType(dataType);
					model.setItemAlias(itemAlias);
					model.setItemBaseName(itemBaseName);
					if(displayNo != null && !displayNo.equals("")){
					model.setDisplayNo(Long.parseLong(displayNo));
					}
					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);
				} 
				else {
					id.setReportCode(Long.parseLong(reportCode));
					id.setItemCode(itemCode);
					model = remote.findById(id);
					model.setItemAlias(itemAlias);
					if(dataType != null && !dataType.equals("")){
					model.setDataType(dataType);
					}
					if(displayNo != null && !displayNo.equals(""))
					{
						model.setDisplayNo(Long.parseLong(displayNo));
					}
					model.setItemBaseName(itemBaseName);
					updateList.add(model);
				}
			}

			if (addList.size() > 0)
				remote.save(addList);

			if (updateList.size() > 0)

				remote.update(updateList);
			
			//String [] ids=deleteIds.split(",");
			

			if (deleteIds != null && !deleteIds.trim().equals(""))

			
			//	remote.delete(deleteIds);
				
				remote.deleteMuti(deleteIds,Long.parseLong(code));

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	/**
	 * 根据录入报表编码查找列表信息
	 * @throws JSONException
	 */
	public void findReprotItemList() throws JSONException {
		String reportCode = request.getParameter("reportCode");
		PageObject obj = remote.findAll(reportCode, employee.getEnterpriseCode(), start,limit);
		
		write(JSONUtil.serialize(obj));
		
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

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}
}
