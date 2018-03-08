package power.web.productiontec.dependability.action;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxAuxiliaryInfo;
import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxAuxiliaryInfoFacadeRemote;
import power.web.comm.AbstractAction;
@SuppressWarnings("serial")
public class AuxiliaryInfoAction extends AbstractAction
{
	private PtKkxAuxiliaryInfoFacadeRemote remote;
	private PtKkxAuxiliaryInfo info;
	public PtKkxAuxiliaryInfo getInfo() {
		return info;
	}

	public void setInfo(PtKkxAuxiliaryInfo info) {
		this.info = info;
	}

	public AuxiliaryInfoAction()
	{
		remote = (PtKkxAuxiliaryInfoFacadeRemote)factory.getFacadeRemote("PtKkxAuxiliaryInfoFacade");
	}
	
	public void getAllAuxiliaryInfo() throws JSONException
	{
		Long blockId = null;
		Long typeId = null;
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String blockIdStr = request.getParameter("blockId");
		if(blockIdStr != null && !blockIdStr.equals(""))
			blockId = Long.parseLong(blockIdStr);
		String typeIdStr = request.getParameter("typeId");
		if(typeIdStr != null)
			typeId = Long.parseLong(typeIdStr);
		String name = request.getParameter("name");
		PageObject pg = new PageObject();
		if(start != null && limit != null)
			pg = remote.getAllAuxiliaryRec(blockId, typeId, name, employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
		else
			pg = remote.getAllAuxiliaryRec(blockId, typeId, name, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
	
	public void saveAuxiliaryInfo()
	{
		String startProDateString = request.getParameter("startProDateString");
		String stopStatDateString = request.getParameter("stopStatDateString");
		String leaveFactoryDateString = request.getParameter("leaveFactoryDateString");
		String statDateString = request.getParameter("statDateString");
		String stopUseDateString = request.getParameter("stopUseDateString");
		String auxiliaryTypeId = request.getParameter("auxiliaryTypeId");
		if(auxiliaryTypeId != null && !auxiliaryTypeId.equals(""))
			info.setAuxiliaryTypeId(Long.parseLong(auxiliaryTypeId));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (startProDateString != null && !startProDateString.equals(""))
			try {
				info.setStartProDate(sdf.parse(startProDateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (stopStatDateString != null && !stopStatDateString.equals(""))
			try {
				info.setStopStatDate(sdf.parse(stopStatDateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (leaveFactoryDateString != null
				&& !leaveFactoryDateString.equals(""))
			try {
				info.setLeaveFactoryDate(sdf.parse(leaveFactoryDateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (statDateString != null && !statDateString.equals(""))
			try {
				info.setStatDate(sdf.parse(statDateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (stopUseDateString != null && !stopUseDateString.equals(""))
			try {
				info.setStopUseDate(sdf.parse(stopUseDateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		info.setIsUse("Y");
		info.setEnterpriseCode(employee.getEnterpriseCode());
		if(info.getAuxiliaryId() == null)
			remote.save(info);
		else {
			remote.update(info);
		}
		write("{success : true,msg : '数据保存成功！'}");
	}
	
	public void deleteAuxiliaryInfo()
	{
		String ids = request.getParameter("ids");
		remote.delete(ids);
	}
}