package power.web.productiontec.dependability.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxTurbineInfo;
import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxTurbineInfoFacadeRemote;
import power.ejb.productiontec.dependabilityAnalysis.business.form.TurbineInfoForm;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class TurbineInfoAction extends AbstractAction
{
	private PtKkxTurbineInfoFacadeRemote remote;
	private PtKkxTurbineInfo info;
	public TurbineInfoAction()
	{
		remote = (PtKkxTurbineInfoFacadeRemote)factory.getFacadeRemote("PtKkxTurbineInfoFacade");
	}
	public PtKkxTurbineInfo getInfo() {
		return info;
	}
	public void setInfo(PtKkxTurbineInfo info) {
		this.info = info;
	}
	
	public void getTurbineInfoByBlockId() throws JSONException
	{
		String blockId = request.getParameter("blockId");
		TurbineInfoForm form = new TurbineInfoForm();
		PtKkxTurbineInfo temp = new PtKkxTurbineInfo();
		form.setData(temp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (blockId != null) {
			List<PtKkxTurbineInfo> list = remote.findByProperty("blockId", Long
					.parseLong(blockId));
			if(list != null && list.size() > 0)
			{
				form.setData(list.get(0));
				if(list.get(0).getManufactureDate() != null)
					form.setManufactureDate(sdf.format(list.get(0).getManufactureDate()));
				write(JSONUtil.serialize(form));
			}
			else
			{
				write(JSONUtil.serialize(form));
			}
		}
		else
		{
			write(JSONUtil.serialize(form));
		}
//		write("{data:{'boilerModel':'123'}}");
	}
	
	public void saveTurbineInfoOrUpdate()
	{
		String manufactureDate = request.getParameter("manufactureDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(manufactureDate != null && !manufactureDate.equals(""))
			try {
				info.setManufactureDate(sdf.parse(manufactureDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if(info.getTurbineId() == null)
		{
			info.setIsUse("Y");
			info.setEnterpriseCode(employee.getEnterpriseCode());
			remote.save(info);
		}
		else
		{
			info.setIsUse("Y");
			info.setEnterpriseCode(employee.getEnterpriseCode());
			remote.update(info);
		}
	}
}