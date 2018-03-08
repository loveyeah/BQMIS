package power.web.productiontec.relayProtection;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.PtJdbhJDzjssm;
import power.ejb.productiontec.relayProtection.PtJdbhJDzjssmFacadeRemote;
import power.web.comm.AbstractAction;

public class CalculateInstructionAction extends AbstractAction {
	
	private PtJdbhJDzjssmFacadeRemote instructionRemote;
	
	private PtJdbhJDzjssm dzjssm;

	public CalculateInstructionAction() {
		instructionRemote = (PtJdbhJDzjssmFacadeRemote)factory.getFacadeRemote("PtJdbhJDzjssmFacade");
	}
	
	public void saveDzjssm() {
		dzjssm.setEnterpriseCode(employee.getEnterpriseCode());
		dzjssm = instructionRemote.save(dzjssm);
		if(dzjssm == null)
		{
			write("{failure:true,msg:'该继电保护定值计算说明名称已存在！'}");
		}
		else 
		{
			write("{success:true,id:'" + dzjssm.getDzjssmId() + "',msg:'数据增加成功！'}");
		}
	}
	
	public void updateDzjssm() {
		PtJdbhJDzjssm temp = instructionRemote.findById(dzjssm.getDzjssmId());
		temp.setJssmName(dzjssm.getJssmName());
		temp.setContent(dzjssm.getContent());
		temp.setMemo(dzjssm.getMemo());
		temp.setFillBy(dzjssm.getFillBy());
		temp.setFillDate(dzjssm.getFillDate());
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		dzjssm = instructionRemote.update(temp);
		if(dzjssm == null)
		{
			write("{failure:true,msg:'该继电保护定值计算说明名称已存在！'}");
		}
		else 
		{
			write("{success:true,id:'" + dzjssm.getDzjssmId() + "',msg:'数据修改成功！'}");
		}
		
	}
	
	public void deleteDzjssm() {
		String ids = request.getParameter("ids");
		instructionRemote.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}
	
	public void getAllDzjssm() throws JSONException {
		PageObject obj = new PageObject();
		String name = request.getParameter("name");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = instructionRemote.findAll(name, employee.getEnterpriseCode(), start,
					limit);
		} else {
			obj = instructionRemote.findAll(name, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * @return the dzjssm
	 */
	public PtJdbhJDzjssm getDzjssm() {
		return dzjssm;
	}

	/**
	 * @param dzjssm the dzjssm to set
	 */
	public void setDzjssm(PtJdbhJDzjssm dzjssm) {
		this.dzjssm = dzjssm;
	}

}
