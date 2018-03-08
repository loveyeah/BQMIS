package power.web.manage.contract.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.manage.contract.ConCItemSource;
import power.ejb.manage.contract.ConCItemSourceFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ConItemSourceAction extends AbstractAction{

	private ConCItemSourceFacadeRemote remote;
	private ConCItemSource source;
	private Long node;
	
	public ConItemSourceAction()
	{
		remote = (ConCItemSourceFacadeRemote)factory.getFacadeRemote("ConCItemSourceFacade");
	}

	public void addItemSource() {
		try {
			source.setIsUse("Y");
			source.setEnterpriseCode(employee.getEnterpriseCode());
			ConCItemSource model = remote.save(source);
			String str = JSONUtil.serialize(model);
			write("{success:true,source:" + str + "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateItemSource()
	{
		ConCItemSource model = remote.findById(source.getItemId());
		model.setPItemId(source.getPItemId());
		model.setItemCode(source.getItemCode());
		model.setItemName(source.getItemName());
		model.setMemo(source.getMemo());
		model.setDisplayNo(source.getDisplayNo());
		model.setIsUse(source.getIsUse());
		remote.update(model);
	}
	
	public void deleteItemSource()
	{
		ConCItemSource model=remote.findById(source.getItemId());
		remote.delete(model);
	}
	
	public void findByPItemId() {
		List<ConCItemSource> list=remote.findByPItemId(node);
		String str=toJsonStr(list);
		write(str);
	}

	public void findInfoByItemId(){
		try{
			// modify by liuyi 091016
//			ConCItemSource model=remote.findInfoById(node);
			ConCItemSource model=remote.findById(node);
			String str=JSONUtil.serialize(model);
			write(str);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	private String toJsonStr(List<ConCItemSource> list) {
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		for (int i = 0; i < list.size(); i++) {
			ConCItemSource cct = list.get(i);
			boolean isLeaf = !remote.getByPItemId(cct.getItemId());
			String icon = isLeaf ? "file" : "folder";
			JSONStr.append("{\"text\":\"" + cct.getItemName() + "\","
					+ "\"id\":\"" + cct.getItemId() + "\","
					+ "\"code\":\"" + cct.getItemCode() + "\","
					+ "\"memo\":\"" + cct.getMemo() + "\","
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
	
	public ConCItemSource getSource() {
		return source;
	}

	public void setSource(ConCItemSource source) {
		this.source = source;
	}

	public Long getNode() {
		return node;
	}

	public void setNode(Long node) {
		this.node = node;
	}
}
