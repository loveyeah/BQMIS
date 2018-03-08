package power.web.productiontec.dependability.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxBlockInfo;
import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxBlockInfoFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class KkxBlockInfoAction extends AbstractAction{

	private PtKkxBlockInfoFacadeRemote remote;
	private PtKkxBlockInfo block;
	
	public KkxBlockInfoAction()
	{
		remote = (PtKkxBlockInfoFacadeRemote)factory.getFacadeRemote("PtKkxBlockInfoFacade");
	}
	
	public void addKkxBlockInfo()
	{
		block.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			remote.save(block);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}

	public void updateKkxBlockInfo()
	{
		PtKkxBlockInfo model = remote.findById(block.getBlockId());
		model.setBlockCode(block.getBlockCode());
		model.setBlockName(block.getBlockName());
		model.setAttemperCompany(block.getAttemperCompany());
		model.setBelongGrid(block.getBelongGrid());
		model.setBlockType(block.getBlockType());
		model.setFuelName(block.getFuelName());
		model.setHoldingCompany(block.getHoldingCompany());
		model.setHoldingPercent(block.getHoldingPercent());
		model.setManageCompany(block.getManageCompany());
		model.setNameplateCapability(block.getNameplateCapability());
		model.setProductionDate(block.getProductionDate());
		model.setStatDate(block.getStatDate());
		model.setStopStatDate(block.getStopStatDate());
		try {
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
		
	}
	
	public void deleteKkxBlockInfo()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	public void findKkxBlockList() throws JSONException
	{
		String blockName= request.getParameter("fuzzytext");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findBlockList(employee.getEnterpriseCode(), blockName, start,limit);
		} else {
			object = remote.findBlockList(employee.getEnterpriseCode(), blockName);
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	public PtKkxBlockInfo getBlock() {
		return block;
	}

	public void setBlock(PtKkxBlockInfo block) {
		this.block = block;
	}
}
