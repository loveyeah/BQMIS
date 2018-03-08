package power.web.productiontec.insulation.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.insulation.PtJyjdJSbtzlh;
import power.ejb.productiontec.insulation.PtJyjdJSbtzlhFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 绝缘仪器仪表台帐
 * @author liuyi 090706
 *
 */
public class DeviceAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 14563424465464L;
	private PtJyjdJSbtzlh pjj;
	private PtJyjdJSbtzlhFacadeRemote remote; 
	
	public DeviceAction()
	{
		remote = (PtJyjdJSbtzlhFacadeRemote)factory.getFacadeRemote("PtJyjdJSbtzlhFacade");
	}
	
	/**
	 * 增加一条绝缘设备信息
	 */
	public void addPtJyjdJSbtzlh()
	{
		
		pjj.setEnterpriseCode(employee.getEnterpriseCode());
		pjj = remote.save(pjj);
		if(pjj == null)
		{
			write("{failure:true,msg:'该绝缘设备名称已存在！'}");
		}
		else
		write("{success:true,id:'"+pjj.getDeviceId()+"',msg:'数据增加成功！'}");
	}
	
	/**
	 * 修改一条绝缘设备信息
	 */
	public void updatePtJyjdJSbtzlh()
	{
		PtJyjdJSbtzlh temp = remote.findById(pjj.getDeviceId());
		temp.setDeviceName(pjj.getDeviceName());
		temp.setTestCycle(pjj.getTestCycle());
		temp.setFactory(pjj.getFactory());
		temp.setSizes(pjj.getSizes());
		temp.setUserRange(pjj.getUserRange());
		temp.setVoltage(pjj.getVoltage());
		temp.setMemo(pjj.getMemo());
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		
		pjj = remote.update(temp);
		if(pjj == null)
		{
			write("{failure:true,msg:'该绝缘设备名称已存在！'}");
		}
		else
		write("{success:true,msg:'数据修改成功！'}");
	}
	
	/**
	 * 删除一条或多条绝缘设备信息
	 */
	public void deletePtJyjdJSbtzlh()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}
	
	/**
	 * 根据姓名和企业编码查询绝缘设备信息列表
	 * @throws JSONException 
	 */
	public void findPtJyjdJSbtzlhList() throws JSONException
	{
        PageObject obj=new  PageObject();
		
		String name = request.getParameter("name");
		
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findAll(name, employee.getEnterpriseCode(), start,limit);
	    }
	    else
	    {
	    	obj=remote.findAll(name, employee.getEnterpriseCode());
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 根据姓名和企业编码查询绝缘设备信息列表以及预试计划
	 * @throws JSONException 
	 */
	public void findDeviceTryList() throws JSONException
	{
        PageObject obj=new  PageObject();
		
		String name = request.getParameter("name");
		
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findDeviceTryAll(name, employee.getEnterpriseCode(), start,limit);
	    }
	    else
	    {
	    	obj=remote.findDeviceTryAll(name, employee.getEnterpriseCode());
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 对绝缘设备台帐和预试计划表中的一条信息进行编辑
	 */
	public void editDeviceTryInfo() throws JSONException
	{
		String deviceId = request.getParameter("deviceId");
		System.out.println(deviceId);
		String operateBy = request.getParameter("operateBy");
		System.out.println(operateBy);
		String operateDate = request.getParameter("operateDate");
		System.out.println(operateDate);
		String memo = request.getParameter("memo");
		System.out.println(memo);
		String test = request.getParameter("testCycle");
		if(test == null || test.equals(""))
		{
			test ="0";
		}
		System.out.println(test);
		Long id = Long.parseLong(deviceId);
		int testCycle = Integer.parseInt(test);
		boolean bool = remote.editDeviceTryInfo(id,operateBy,operateDate,memo,testCycle);
		if(!bool)
		{
			write("{failure:true,msg:'数据保存失败！'}");
		}
		else
		write("{success:true,msg:'数据保存成功！'}");
	}
	
	public PtJyjdJSbtzlh getPjj() {
		return pjj;
	}

	public void setPjj(PtJyjdJSbtzlh pjj) {
		this.pjj = pjj;
	}


}
