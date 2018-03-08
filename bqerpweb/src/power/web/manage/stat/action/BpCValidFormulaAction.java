package power.web.manage.stat.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.manage.stat.BpCValidFormula;
import power.ejb.manage.stat.BpCValidFormulaFacadeRemote;
import power.web.comm.AbstractAction;

public class BpCValidFormulaAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 596211562332L;
	private BpCValidFormulaFacadeRemote remote;
	
	public BpCValidFormulaAction(){
		remote = (BpCValidFormulaFacadeRemote)factory.getFacadeRemote("BpCValidFormulaFacade");
	}
	
	public void addOrUpdateValidFormulaEntity(){
			String itemCode = request.getParameter("itemCode");
			String connItemCode = request.getParameter("connItemCode");
			String minString = request.getParameter("min");
			String maxString = request.getParameter("max");
			BpCValidFormula entity = remote.findById(itemCode);
			if(entity == null)
			{
				entity = new BpCValidFormula();
				entity.setItemCode(itemCode);
				entity.setConnItemCode(connItemCode);
				if(minString != null && !minString.equals(""))
					entity.setMin(Double.parseDouble(minString));
				if(maxString != null && !maxString.equals(""))
					entity.setMax(Double.parseDouble(maxString));
				remote.save(entity);
				write("{success:true,msg:'公式新增成功！'}");
			}else{
				entity.setConnItemCode(connItemCode);
				if(minString != null && !minString.equals(""))
					entity.setMin(Double.parseDouble(minString));
				if(maxString != null && !maxString.equals(""))
					entity.setMax(Double.parseDouble(maxString));
				remote.update(entity);
				write("{success:true,msg:'公式修改成功！'}");
			}
	}
	
	public void deleteValidFormula(){
		String itemCode = request.getParameter("itemCode");
		remote.delete(itemCode);
		write("{success:true,msg:'公式删除成功！'}");
	}
	
	public void queryValidFormulaEntity() throws JSONException{
		String itemCode = request.getParameter("itemCode");
		List list = remote.findValidFormulaEntity(itemCode);
		write(JSONUtil.serialize(list));
	}
}