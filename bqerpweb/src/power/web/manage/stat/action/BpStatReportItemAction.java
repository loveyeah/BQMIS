package power.web.manage.stat.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCStatReportItem;
import power.ejb.manage.stat.BpCStatReportItemFacadeRemote;
import power.ejb.manage.stat.BpCStatReportItemId;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BpStatReportItemAction extends AbstractAction{

	protected BpCStatReportItemFacadeRemote remote;
	private int start;
	private int limit;
	private long code;
	
	public BpStatReportItemAction()
	{
		remote = (BpCStatReportItemFacadeRemote)factory.getFacadeRemote("BpCStatReportItemFacade");
	}
	
	/**
	 * 保存操作
	 */
	@SuppressWarnings("unchecked")
	public void saveStatReportItem()
	{
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String code = request.getParameter("isCode");
			
			
			Object obj = JSONUtil.deserialize(str);

			List<BpCStatReportItem> addList = new ArrayList<BpCStatReportItem>();
			List<BpCStatReportItem> updateList = new ArrayList<BpCStatReportItem>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				String itemCode = null;
				String reportCode = null;
				String displayNo = null;
				
				if (data.get("model.id.reportCode") != null&& !data.get("model.id.reportCode").equals("")) {
					reportCode = data.get("model.id.reportCode").toString();
				}
				if (data.get("model.id.itemCode") != null&&!data.get("model.id.itemCode").equals("")) {
					itemCode = data.get("model.id.itemCode").toString();
				}
				if (data.get("model.displayNo") != null&&!data.get("model.displayNo").equals("")) {
					displayNo = data.get("model.displayNo").toString();
				}
			
				BpCStatReportItem model = new BpCStatReportItem();
				BpCStatReportItemId id = new BpCStatReportItemId();
				// 增加
				if (remote.isNew(reportCode, itemCode) == 0) 
				{
					id.setReportCode(Long.parseLong(reportCode));
					id.setItemCode(itemCode);
					model.setId(id);
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
					if(displayNo != null && !displayNo.equals(""))
					{
						model.setDisplayNo(Long.parseLong(displayNo));
					}
					updateList.add(model);
				}
			}
			if (addList.size() > 0||updateList.size() > 0||(deleteIds != null && !deleteIds.trim().equals("")))
				remote.save(addList,updateList,deleteIds,Long.parseLong(code));
//			if (updateList.size() > 0)
//				remote.update(updateList);
//			if (deleteIds != null && !deleteIds.trim().equals(""))		
//				remote.deleteMuti(deleteIds,Long.parseLong(code));
			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}
	
	/**
	 * 根据统计报表编码查找记录
	 * @throws JSONException 
	 */
	public void findStatReportItemList() throws JSONException
	{
		String reportCode = request.getParameter("reportCode");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), reportCode, start,limit);
		String str = JSONUtil.serialize(obj);
		write(str);
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
