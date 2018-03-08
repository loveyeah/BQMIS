package power.web.manage.system.action;

import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.system.BpCItemFacadeRemote;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class UnitMaintenanceAction extends AbstractAction {
	private BpCMeasureUnitFacadeRemote remote;
	private BpCMeasureUnit unit;
	private String unitId;
	public BpCMeasureUnit getUnit() {
		return unit;
	}
	public void setUnit(BpCMeasureUnit unit) {
		this.unit = unit;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
	/**
	 * 构造函数
	 */
	public UnitMaintenanceAction()
	{
		remote=(BpCMeasureUnitFacadeRemote)factory.getFacadeRemote("BpCMeasureUnitFacade");
	}
	
	/**
	 * 获得计量单位列表
	 * @throws JSONException
	 */
	public void getUnitList() throws JSONException
	{
		String fuzzy="";
		Object myobj=request.getParameter("fuzzy");
		if(myobj!=null)
		{
			fuzzy=myobj.toString();
		}
	    String enterpriseCode="hfdc";
	    Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			 obj=remote.findUnitList(fuzzy, enterpriseCode,start,limit);
	    }
	    else
	    {
	    	obj=remote.findUnitList(fuzzy, enterpriseCode);
	    }

		 String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 增加计量单位
	 */
	public void addUnit()
	{
		unit.setEnterpriseCode("hfdc");
	    int i=remote.save(unit);
		if(i != -1)
		{
			write("{success:true,msg:'增加成功！'}");
		}
		else
		{
			write("{success:true,msg:'增加失败:名称重复！'}");
		}
	}
	
	/**
	 * 修改计量单位
	 */
	public void updateUnit()
	{
		BpCMeasureUnit model=remote.findById(Long.parseLong(unitId));
		model.setRetrieveCode(unit.getRetrieveCode());
		model.setUnitAlias(unit.getUnitAlias());
		model.setUnitName(unit.getUnitName());
		if(remote.update(model))
		{
			  write("{success:true,msg:'修改成功！'}");
		}
		else
		{
			write("{success:true,msg:'修改失败:名称重复！'}");
		}
	}
	
	/**
	 * 删除计量单位
	 */
	public void deleteUnit()
	{
		 String ids= request.getParameter("ids");
	     String [] unitIds= ids.split(",");
			for(int i=0;i<unitIds.length;i++)
			{
				if(!unitIds[i].equals(""))
				{
					remote.delete(Long.parseLong(unitIds[i]));
				}
			}
			write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 通过id获得计量单位名称（暂没用到）
	 * @param id
	 * @return
	 */
	public String findNameById(Long id)
	{
		String unitName=id.toString();
		
		BpCMeasureUnit model=remote.findById(id);
		if(model!=null)
		{
			unitName=model.getUnitName();
		}
		return unitName;
	}
	
	


}
