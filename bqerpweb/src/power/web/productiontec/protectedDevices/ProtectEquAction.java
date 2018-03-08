package power.web.productiontec.protectedDevices;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.PtJdbhCBhlxwh;
import power.ejb.productiontec.relayProtection.PtJdbhJBbhsbtz;
import power.ejb.productiontec.relayProtection.PtJdbhJBbhsbtzFacadeRemote;
import power.ejb.productiontec.relayProtection.PtJdbhJBhzztz;
import power.ejb.productiontec.relayProtection.PtJdbhJBhzztzFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 继电保护装置台帐
 * @author liuyi 090716 
 */
public class ProtectEquAction extends AbstractAction
{
	private static final long serialVersionUID = 1462329855L;
	private PtJdbhJBhzztz ptjd;
	private PtJdbhJBhzztzFacadeRemote remote;
	public ProtectEquAction()
	{
		remote = (PtJdbhJBhzztzFacadeRemote)factory.getFacadeRemote("PtJdbhJBhzztzFacade");
	}
	
	/**
	 * 新增一条继电保护装置台帐记录
	 */
	public void addProtectEqu()
	{
		ptjd.setEnterpriseCode(employee.getEnterpriseCode());
		ptjd = remote.save(ptjd);
		if(ptjd == null)
		{
			write("{failure:true,msg:'该保护装置已存在！'}");
		}
		else 
		{
			write("{success:true,id:'" + ptjd.getDeviceId()+ "',msg:'数据增加成功！'}");
		}
	}
	
	/**
	 * 删除一条或多条继电保护装置台帐记录
	 */
	public void deleteProtectEqu() {
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}
	
	/**
	 * 更新一条继电保护装置台帐记录
	 */
	public void updateProtectEqu()
	{
		PtJdbhJBhzztz temp = remote.findById(ptjd.getDeviceId());
		temp.setProtectedDeviceId(ptjd.getProtectedDeviceId());
		temp.setEquCode(ptjd.getEquCode());
		temp.setVoltage(ptjd.getVoltage());
		temp.setDeviceType(ptjd.getDeviceType());
		temp.setSizeType(ptjd.getSizeType());
		temp.setSizes(ptjd.getSizes());
		temp.setManufacturer(ptjd.getManufacturer());
		temp.setOutFactoryDate(ptjd.getOutFactoryDate());
		temp.setOutFactoryNo(ptjd.getOutFactoryNo());
		temp.setInstallPlace(ptjd.getInstallPlace());
		temp.setTestCycle(ptjd.getTestCycle());
		temp.setChargeBy(ptjd.getChargeBy());
		temp.setMemo(ptjd.getMemo());
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		
		ptjd = remote.update(temp);
		if(ptjd == null)
		{
			write("{failure:true,msg:'该继电保护装置已存在'}");
		}
		else 
		{
			write("{success:true,id:'" + ptjd.getDeviceId() + "',msg:'数据修改成功！'}");
		}
	}
	
	/**
	 * 根据名称和企业编码查询被保护设备列表
	 * 
	 * @throws JSONException
	 */
	public void findProtectEquList() throws JSONException
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

	public PtJdbhJBhzztz getPtjd() {
		return ptjd;
	}

	public void setPtjd(PtJdbhJBhzztz ptjd) {
		this.ptjd = ptjd;
	}

	
}