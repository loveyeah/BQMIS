package power.web.productiontec.dependability.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.productiontec.dependabilityAnalysis.business.PtJPrecipitationParameter;
import power.ejb.productiontec.dependabilityAnalysis.business.PtJPrecipitationParameterFacadeRemote;
import power.ejb.productiontec.dependabilityAnalysis.business.form.PrecipitationForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class PrecipitationAction extends AbstractAction{

	public PtJPrecipitationParameterFacadeRemote remote;
	private PtJPrecipitationParameter para;
	
	public PrecipitationAction()
	{
		remote = (PtJPrecipitationParameterFacadeRemote)factory.getFacadeRemote("PtJPrecipitationParameterFacade");
	}
	
	public void savePrecipitationParameter()
	{
		if(para.getPrecipitationId() == null)
		{
			para.setIsUse("Y");
			para.setEnterpriseCode(employee.getEnterpriseCode());
			remote.save(para);
		}
		else{
			para.setIsUse("Y");
			para.setEnterpriseCode(employee.getEnterpriseCode());
			remote.update(para);
		}
		write("{success : true,msg : '数据保存成功！'}");
	}
	
	public void getPrecipitationParameterById() throws JSONException
	{
		String auxiliaryIdStr = request.getParameter("auxiliaryId");
		Long auxiliaryId = null;
		PrecipitationForm form = new PrecipitationForm();
		if(auxiliaryIdStr != null && !auxiliaryIdStr.equals(""))
			auxiliaryId = Long.parseLong(auxiliaryIdStr);
		List<PtJPrecipitationParameter> list = remote.findByProperty("auxiliaryId", auxiliaryId);

		if(list != null && list.size() > 0)
		{
			form.setData(list.get(0));
			write(JSONUtil.serialize(form));
		}
		else
			write(JSONUtil.serialize(form));
	}
	
	public PtJPrecipitationParameter getPara() {
		return para;
	}
	public void setPara(PtJPrecipitationParameter para) {
		this.para = para;
	}
	
}
