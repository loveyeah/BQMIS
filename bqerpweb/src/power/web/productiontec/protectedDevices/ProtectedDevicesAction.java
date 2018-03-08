package power.web.productiontec.protectedDevices;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.PtJdbhCBhlxwh;
import power.ejb.productiontec.relayProtection.PtJdbhJBbhsbtz;
import power.ejb.productiontec.relayProtection.PtJdbhJBbhsbtzFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 被保护设备台帐
 * @author liuyi 090716 
 */
public class ProtectedDevicesAction extends AbstractAction
{
	private static final long serialVersionUID = 146269855L;
	private PtJdbhJBbhsbtz pjjb;
	private PtJdbhJBbhsbtzFacadeRemote remote;
	public ProtectedDevicesAction()
	{
		remote = (PtJdbhJBbhsbtzFacadeRemote)factory.getFacadeRemote("PtJdbhJBbhsbtzFacade");
	}
	
	/**
	 * 新增一条被保护设备台帐记录
	 */
	public void addProtectedDevice()
	{
		pjjb.setEnterpriseCode(employee.getEnterpriseCode());
		pjjb = remote.save(pjjb);
		if(pjjb == null)
		{
			write("{failure:true,msg:'该被保护设备已存在！'}");
		}
		else 
		{
			write("{success:true,id:'" + pjjb.getProtectedDeviceId()+ "',msg:'数据增加成功！'}");
		}
	}
	
	/**
	 * 删除一条或多条被保护设备台帐记录
	 */
	public void deleteProtectedDevice() {
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}
	
	/**
	 * 更新一条被保护设备台帐记录
	 */
	public void updateProtectedDevice()
	{
		PtJdbhJBbhsbtz temp = remote.findById(pjjb.getProtectedDeviceId());
		temp.setEquCode(pjjb.getEquCode());
		temp.setEquLevel(pjjb.getEquLevel());
		temp.setVoltage(pjjb.getVoltage());
		temp.setInstallPlace(pjjb.getInstallPlace());
		temp.setManufacturer(pjjb.getManufacturer());
		temp.setSizes(pjjb.getSizes()); 
		temp.setOutFactoryNo(pjjb.getOutFactoryNo());
		temp.setOutFactoryDate(pjjb.getOutFactoryDate());
		temp.setChargeMan(pjjb.getChargeMan());
		temp.setMemo(pjjb.getMemo());
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		
		pjjb = remote.update(temp);
		if(pjjb == null)
		{
			write("{failure:true,msg:'该被保护设备已存在'}");
		}
		else 
		{
			write("{success:true,id:'" + pjjb.getProtectedDeviceId() + "',msg:'数据修改成功！'}");
		}
	}
	
	/**
	 * 根据名称和企业编码查询被保护设备列表
	 * 
	 * @throws JSONException
	 */
	public void findProtectedDeviceList() throws JSONException
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

	public PtJdbhJBbhsbtz getPjjb() {
		return pjjb;
	}

	public void setPjjb(PtJdbhJBbhsbtz pjjb) {
		this.pjjb = pjjb;
	}
}