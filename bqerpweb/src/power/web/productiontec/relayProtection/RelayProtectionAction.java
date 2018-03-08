package power.web.productiontec.relayProtection;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.PtJdbhCBhlxwh;
import power.ejb.productiontec.relayProtection.PtJdbhCBhlxwhFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 继电保护类型维护
 * @author liuyi 090713
 *
 */
public class RelayProtectionAction extends AbstractAction
{
	private static final long serialVersionUID = 1462646365L;
	private PtJdbhCBhlxwh pjc;
	PtJdbhCBhlxwhFacadeRemote remote;
	
	public RelayProtectionAction()
	{
		remote = (PtJdbhCBhlxwhFacadeRemote)factory.getFacadeRemote("PtJdbhCBhlxwhFacade");
	}
	
	
	/**
	 * 增加一条继电保护类型维护记录
	 */
	public void addRelayProtection()
	{
		pjc.setEnterpriseCode(employee.getEnterpriseCode());
		pjc = remote.save(pjc);
		if(pjc == null)
		{
			write("{failure:true,msg:'该继电保护类型名称已存在'}");
		}
		else 
		{
			write("{success:true,id:'" + pjc.getProtectTypeId() + "',msg:'数据增加成功！'}");
		}
	}
	
	/**
	 * 修改一条继电保护类型维护记录
	 */
	public void updateRelayProtection()
	{
		PtJdbhCBhlxwh temp = remote.findById(pjc.getProtectTypeId());
		temp.setProtectTypeName(pjc.getProtectTypeName());
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		
		pjc = remote.update(temp);
		if(pjc == null)
		{
			write("{failure:true,msg:'该继电保护类型名称已存在'}");
		}
		else 
		{
			write("{success:true,id:'" + pjc.getProtectTypeId() + "',msg:'数据修改成功！'}");
		}
	}
	
	/**
	 * 删除一条或多条继电保护类型维护记录
	 */
	public void deleteRelayProtection() {
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}
	
	/**
	 * 根据名称和企业编码查询继电保护类型维护列表
	 * 
	 * @throws JSONException
	 */
	public void findRelayProtectionList() throws JSONException
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


	public PtJdbhCBhlxwh getPjc() {
		return pjc;
	}


	public void setPjc(PtJdbhCBhlxwh pjc) {
		this.pjc = pjc;
	}
}
