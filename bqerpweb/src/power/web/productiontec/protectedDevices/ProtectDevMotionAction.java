package power.web.productiontec.protectedDevices;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.PtJdbhCBhlxwh;
import power.ejb.productiontec.relayProtection.PtJdbhJBbhsbtz;
import power.ejb.productiontec.relayProtection.PtJdbhJBbhsbtzFacadeRemote;
import power.ejb.productiontec.relayProtection.PtJdbhJBhzzdzqk;
import power.ejb.productiontec.relayProtection.PtJdbhJBhzzdzqkFacadeRemote;
import power.ejb.productiontec.relayProtection.PtJdbhJBhzztz;
import power.ejb.productiontec.relayProtection.PtJdbhJBhzztzFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 继电保护装置动作情况
 * @author liuyi 090717
 */
public class ProtectDevMotionAction extends AbstractAction
{
	private static final long serialVersionUID = 146255855L;
	private PtJdbhJBhzzdzqk pjj;
	private PtJdbhJBhzzdzqkFacadeRemote remote;
	public ProtectDevMotionAction()
	{
		remote = (PtJdbhJBhzzdzqkFacadeRemote)factory.getFacadeRemote("PtJdbhJBhzzdzqkFacade");
	}
	
	/**
	 * 新增一条继电保护装置动作情况记录
	 */
	public void addPProtectDevMotion()
	{
		pjj.setEnterpriseCode(employee.getEnterpriseCode());
		pjj = remote.save(pjj);
//		if(pjj == null)
//		{
//			write("{failure:true,msg:'该保护装置已存在！'}");
//		}
//		else 
//		{
			write("{success:true,id:'" + pjj.getBhzzdzId()+ "',msg:'数据增加成功！'}");
//		}
	}
	
	/**
	 * 删除一条或多条继电保护装置动作情况记录
	 */
	public void deleteProtectDevMotion() {
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}
	
	/**
	 * 更新一条继电保护装置动作情况记录
	 */
	public void updateProtectDevMotion()
	{
		PtJdbhJBhzzdzqk temp = remote.findById(pjj.getBhzzdzId());
		temp.setDeviceId(pjj.getDeviceId());
		temp.setActDate(pjj.getActDate());
		temp.setChargeDep(pjj.getChargeDep());
		temp.setActAppaise(pjj.getActAppaise());
		temp.setActNum(pjj.getActNum());
		temp.setWaveNumber(pjj.getWaveNumber());
		temp.setWaveGoodNumber(pjj.getWaveGoodNumber());
		temp.setProtectAct(pjj.getProtectAct());
		temp.setErrorAnalyze(pjj.getErrorAnalyze());
		temp.setFillBy(pjj.getFillBy());
		temp.setFillDate(pjj.getFillDate());
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		
		pjj = remote.update(temp);
//		if(pjj == null)
//		{
//			write("{failure:true,msg:'该继电保护装置已存在'}");
//		}
//		else 
//		{
			write("{success:true,id:'" + pjj.getBhzzdzId() + "',msg:'数据修改成功！'}");
//		}
	}
	
	/**
	 * 根据名称和企业编码查询继电保护装置动作情况列表
	 * 
	 * @throws JSONException
	 */
	public void findProtectDevMotionList() throws JSONException
	{
		PageObject obj = new PageObject();
		String name = request.getParameter("name");
		String fromTime = request.getParameter("fromTime");
		String toTime = request.getParameter("toTime");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findAll(name,fromTime,toTime, employee.getEnterpriseCode(), start,
					limit);
		} else {
			obj = remote.findAll(name,fromTime,toTime, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	public PtJdbhJBhzzdzqk getPjj() {
		return pjj;
	}

	public void setPjj(PtJdbhJBhzzdzqk pjj) {
		this.pjj = pjj;
	}

	

	
}