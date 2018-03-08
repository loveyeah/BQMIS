package power.web.manage.contract.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.manage.contract.ConCConType;
import power.ejb.manage.contract.ConCConTypeFacadeRemote;
import power.ejb.manage.contract.form.ContypeInfo;
import power.web.comm.AbstractAction;

public class ConTypeAction extends AbstractAction {
	private ConCConType contype;
	private Long node;
	protected ConCConTypeFacadeRemote remote;

	public ConTypeAction() {
		remote = (ConCConTypeFacadeRemote) factory
				.getFacadeRemote("ConCConTypeFacade");
	}

	public void addConType() {
		try{
		contype.setEnterpriseCode(employee.getEnterpriseCode());
		contype.setLastModifiedBy(employee.getWorkerCode());
		ConCConType model=remote.save(contype);
		String str=JSONUtil.serialize(model);
		write("{success:true,type:"+str+"}");
		}
		catch(Exception e){
			e.printStackTrace();
			}
	}

	public void updateConType() {
		ConCConType model=remote.findById(contype.getContypeId());
		model.setConTypeDesc(contype.getConTypeDesc());
		model.setIsUse(contype.getIsUse());
		model.setMarkCode(contype.getMarkCode());
		model.setMemo(contype.getMemo());
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setLastModifiedBy(employee.getWorkerCode());
		remote.update(model);
	}

	public void deleteConType() {
		ConCConType model=remote.findById(contype.getContypeId());
		remote.delete(model);
	}

	public void findByPId() {
		List<ConCConType> list=remote.findByPContypeId(node);
		String str=toJsonStr(list);
		write(str);
	}

	public void findInfoById(){
		try{
			ContypeInfo model=remote.findInfoById(node);
			String str=JSONUtil.serialize(model);
			write(str);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

	private String toJsonStr(List<ConCConType> list) {
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		for (int i = 0; i < list.size(); i++) {
			ConCConType cct = list.get(i);
			boolean isLeaf = !remote.getByPContypeId(cct.getContypeId());
			String icon = isLeaf ? "file" : "folder";
			JSONStr.append("{\"text\":\"" + cct.getConTypeDesc() + "\","
					+ "\"id\":\"" + cct.getContypeId() + "\","
					+ "\"markCode\":\"" + cct.getMarkCode() + "\","
					+ "\"leaf\":" + isLeaf + ","+"\"cls\":\"" + icon + "\","
					+ "\"enterpriseCode\":" + "\"" + cct.getEnterpriseCode()
					+ "\"},");
		}
		if (JSONStr.length() > 1) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]"); 
		return JSONStr.toString();
	}

	public ConCConType getContype() {
		return contype;
	}

	public void setContype(ConCConType contype) {
		this.contype = contype;
	}

	public Long getNode() {
		return node;
	}

	public void setNode(Long node) {
		this.node = node;
	}
}
