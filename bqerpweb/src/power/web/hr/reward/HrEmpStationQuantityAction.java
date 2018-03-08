package power.web.hr.reward;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.reward.HrCStationQuantify;
import power.ejb.hr.reward.HrCStationQuantifyFacadeRemote;
import power.web.comm.AbstractAction;

public class HrEmpStationQuantityAction  extends  AbstractAction 
{
	private HrCStationQuantifyFacadeRemote  remote;
	public HrEmpStationQuantityAction()
	{
		remote = (HrCStationQuantifyFacadeRemote) factory.getFacadeRemote("HrCStationQuantifyFacade");
	}
	public void getStationQuantity() throws JSONException
	{
		PageObject obj=new PageObject();
		obj=remote.getStationQuantity(employee.getEnterpriseCode());
		if(obj!=null)
		{
			write(JSONUtil.serialize(obj));
		}
	}
	public void delStationQuantity()
	{
		String ids=request.getParameter("ids");
		remote.delStationQuantity(ids);
		write("{success:'true',msg:'删除成功！'}");
	}
	public void saveStationQuantity() throws JSONException
	{
		String str=request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<HrCStationQuantify> addList = null;
		List<HrCStationQuantify> updateList = null;
		if (list != null && list.size() > 0) {
			addList = new ArrayList<HrCStationQuantify>();
			updateList = new ArrayList<HrCStationQuantify>();
			for (Map data : list) {
				if(data!=null)
				{
				String quantifyId = null;
				String stationName = null;
				String quantifyProportion = null;
				
			    if (data.get("quantifyId") != null&&!data.get("quantifyId").equals("") )
					quantifyId = data.get("quantifyId").toString();
				if (data.get("stationName") != null&&!data.get("stationName").equals(""))
					stationName = data.get("stationName").toString();
				if (data.get("quantifyProportion") != null&&!data.get("quantifyProportion").equals(""))
					quantifyProportion = data.get("quantifyProportion").toString();
				
			
				
				HrCStationQuantify model = new HrCStationQuantify();
				if (quantifyId == null) {
					if(stationName!=null)
					{
					model.setStationName(stationName);
					}
					
					if (quantifyProportion != null)
					{
						model.setQuantifyProportion(Double.parseDouble(quantifyProportion));
					}

					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);
				} else {
					model = remote.findById(Long.parseLong(quantifyId));
					if(stationName!=null)
					{
					model.setStationName(stationName);
					}
					
					if (quantifyProportion != null)
					{
						model.setQuantifyProportion(Double.parseDouble(quantifyProportion));
					}
					model.setEnterpriseCode(employee.getEnterpriseCode());
					updateList.add(model);
				}
			}
		}
		}
		
			remote.saveStationQuantity(addList, updateList);
			write("{success:true,msg:'操作成功！'}");
		
	
	}
}