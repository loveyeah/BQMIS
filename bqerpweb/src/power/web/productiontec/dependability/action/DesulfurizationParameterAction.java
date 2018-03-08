package power.web.productiontec.dependability.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.productiontec.dependabilityAnalysis.business.PtJDesulfurizationParameter;
import power.ejb.productiontec.dependabilityAnalysis.business.PtJDesulfurizationParameterFacadeRemote;
import power.ejb.productiontec.dependabilityAnalysis.business.form.DesulfurizationParameterForm;
import power.web.comm.AbstractAction;
@SuppressWarnings("serial")
public class DesulfurizationParameterAction extends AbstractAction
{
	private PtJDesulfurizationParameterFacadeRemote remote;
	private PtJDesulfurizationParameter param;
	public PtJDesulfurizationParameter getParam() {
		return param;
	}
	public void setParam(PtJDesulfurizationParameter param) {
		this.param = param;
	}
	public DesulfurizationParameterAction()
	{
		remote = (PtJDesulfurizationParameterFacadeRemote)factory.getFacadeRemote("PtJDesulfurizationParameterFacade");
	}
	
	public void getDesulfurizationParamByAuxId() throws JSONException
	{
		String auxiliaryIdStr = request.getParameter("auxiliaryId");
		Long auxiliaryId = null;
		DesulfurizationParameterForm form = new DesulfurizationParameterForm();
		if(auxiliaryIdStr != null && !auxiliaryIdStr.equals(""))
			auxiliaryId = Long.parseLong(auxiliaryIdStr);
		List<PtJDesulfurizationParameter> list = remote.findByProperty("auxiliaryId", auxiliaryId);
		if(list != null && list.size() > 0)
		{
			form.setData(list.get(0));
			write(JSONUtil.serialize(form));
		}
		else
			write(JSONUtil.serialize(form));
	}
	
	public void saveDesulfurizationParam()
	{
		param.setIsUse("Y");
		param.setEnterpriseCode(employee.getEnterpriseCode());
		if(param.getDesulfurizationId() == null)
			remote.save(param);
		else
			remote.update(param);
	}
}