package power.web.productiontec.dependability.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxTransformerInfo;
import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxTransformerInfoFacadeRemote;
import power.ejb.productiontec.dependabilityAnalysis.business.form.TransformerInfoForm;
import power.web.comm.AbstractAction;
@SuppressWarnings("serial")
public class TransformerInfoAction extends AbstractAction
{
	private PtKkxTransformerInfoFacadeRemote remote;
	private PtKkxTransformerInfo info;
	public TransformerInfoAction()
	{
		remote = (PtKkxTransformerInfoFacadeRemote)factory.getFacadeRemote("PtKkxTransformerInfoFacade");
	}
	public PtKkxTransformerInfo getInfo() {
		return info;
	}
	public void setInfo(PtKkxTransformerInfo info) {
		this.info = info;
	}
	
	public void getTransformerInfoByBlockId() throws JSONException
	{
		String blockId = request.getParameter("blockId");
		TransformerInfoForm form = new TransformerInfoForm();
		PtKkxTransformerInfo temp = new PtKkxTransformerInfo();
		form.setData(temp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (blockId != null) {
			List<PtKkxTransformerInfo> list = remote.findByProperty("blockId", Long
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
	}
	
	public void saveTransformerInfoOrUpdate()
	{
		String manufactureDate = request.getParameter("manufactureDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(manufactureDate != null && !manufactureDate.equals(""))
			try {
				info.setManufactureDate(sdf.parse(manufactureDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if(info.getTransformerId() == null)
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