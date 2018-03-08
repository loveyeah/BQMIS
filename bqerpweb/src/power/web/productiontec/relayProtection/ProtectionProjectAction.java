package power.web.productiontec.relayProtection;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.PtJdbhCBhlxwh;
import power.ejb.productiontec.relayProtection.PtJdbhCBhlxwhFacadeRemote;
import power.ejb.productiontec.relayProtection.PtJdbhCDzxmwh;
import power.ejb.productiontec.relayProtection.PtJdbhCDzxmwhFacadeRemote;
import power.ejb.productiontec.relayProtection.PtJdbhJZzdylxFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 继电定值项目维护表
 * @author liuyi 090713
 *
 */
public class ProtectionProjectAction extends AbstractAction
{
	private static final long serialVersionUID = 1462646377365L;
	private PtJdbhCDzxmwh pjcd;
	PtJdbhCDzxmwhFacadeRemote remote;
	PtJdbhJZzdylxFacadeRemote rem;
	
	public ProtectionProjectAction()
	{
		remote = (PtJdbhCDzxmwhFacadeRemote)factory.getFacadeRemote("PtJdbhCDzxmwhFacade");
		rem = (PtJdbhJZzdylxFacadeRemote)factory.getFacadeRemote("PtJdbhJZzdylxFacade");
	}
	
	
	/**
	 * 增加一条继电定值项目维护表记录
	 */
	public void addPtJdbhCDzxmwh()
	{
		pjcd.setEnterpriseCode(employee.getEnterpriseCode());
		pjcd = remote.save(pjcd);
		if(pjcd == null)
		{
			write("{failure:true,msg:'该继电定值项目的此保护类型名称已存在！'}");
		}
		else 
		{
			write("{success:true,id:'" + pjcd.getFixvalueItemId() + "',msg:'数据增加成功！'}");
		}
	}
	
	/**
	 * 修改一条继电定值项目维护表记录
	 */
	public void updatePtJdbhCDzxmwh()
	{
		PtJdbhCDzxmwh temp = remote.findById(pjcd.getFixvalueItemId());
		temp.setProtectTypeId(pjcd.getProtectTypeId());
		temp.setFixvalueItemName(pjcd.getFixvalueItemName());
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		
		pjcd = remote.update(temp);
		if(pjcd == null)
		{
			write("{failure:true,msg:'该继电定值项目的此保护类型名称已存在！'}");
		}
		else 
		{
			write("{success:true,id:'" + pjcd.getFixvalueItemId() + "',msg:'数据修改成功！'}");
		}
	}
	
	/**
	 * 删除一条或多条继电定值项目维护表记录
	 */
	public void deletePtJdbhCDzxmwh() {
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}
	
	/**
	 * 根据名称和企业编码查询继电定值项目维护表记录
	 * 
	 * @throws JSONException
	 */
	public void findPtJdbhCDzxmwhList() throws JSONException
	{
		PageObject obj = new PageObject();
		String name = request.getParameter("name");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findAll(name, employee.getEnterpriseCode(), start,
					limit);
		} else {
			obj = remote.findAll(name, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}


	public PtJdbhCDzxmwh getPjcd() {
		return pjcd;
	}


	public void setPjcd(PtJdbhCDzxmwh pjcd) {
		this.pjcd = pjcd;
	}
	
	
	/**
	 * 保护装置对应保护类型表中的数据进行增加或删除
	 */
	public void saveDeviceAndType()
	{
		String devId = request.getParameter("devId");
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa  "+devId);
		String typeIds = request.getParameter("typeIds");
		System.out.println("bbbbbbbbbbbbbbbbbbb " + typeIds);
		String enterpriseCode = employee.getEnterpriseCode();
		if(devId == null || devId.equals("") || typeIds == null || typeIds.equals(""))
		{
			write("{failure:true,msg:'装置选择或类型选择时出现错误'}");
			return;
		}
		else
		{
			rem.saveDevicesAndTypes(devId, typeIds,enterpriseCode);
		}
		write("{success:true,msg:'数据保存成功'}");
		
	}
	
	/**
	 * 保护装置对应保护类型表中的数据查找
	 * @throws JSONException 
	 */
	public void findDeviceProList() throws JSONException
	{
		PageObject obj = new PageObject();
		String devId = request.getParameter("devId");
		if(devId == null)
		{
			devId = "";
		}
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = rem.findDeviceProList(devId, start,
					limit);
		} else {
			obj = rem.findDeviceProList(devId);
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
}
