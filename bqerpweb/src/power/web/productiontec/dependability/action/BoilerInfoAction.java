package power.web.productiontec.dependability.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;

import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxBoilerInfo;
import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxBoilerInfoFacadeRemote;
import power.ejb.productiontec.dependabilityAnalysis.business.form.BoilerInfoForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BoilerInfoAction extends AbstractAction
{
	private PtKkxBoilerInfoFacadeRemote remote;
	private PtKkxBoilerInfo info;
	public BoilerInfoAction()
	{
		remote = (PtKkxBoilerInfoFacadeRemote)factory.getFacadeRemote("PtKkxBoilerInfoFacade");
	}
	public PtKkxBoilerInfo getInfo() {
		return info;
	}
	public void setInfo(PtKkxBoilerInfo info) {
		this.info = info;
	}
	
	public void getBoilerInfoByBlockId() throws JSONException
	{
		String blockId = request.getParameter("blockId");
		BoilerInfoForm form = new BoilerInfoForm();
		PtKkxBoilerInfo temp = new PtKkxBoilerInfo();
		form.setData(temp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (blockId != null) {
			List<PtKkxBoilerInfo> list = remote.findByProperty("blockId", Long
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
	
	public void saveBoilderInfoOrUpdate()
	{
		String manufactureDate = request.getParameter("manufactureDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(manufactureDate != null && !manufactureDate.equals(""))
			try {
				info.setManufactureDate(sdf.parse(manufactureDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if(info.getBoilerId() == null)
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