package power.web.productiontec.dependability.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.productiontec.dependabilityAnalysis.business.PtJPulverizerParameter;
import power.ejb.productiontec.dependabilityAnalysis.business.PtJPulverizerParameterFacadeRemote;
import power.ejb.productiontec.dependabilityAnalysis.business.form.PulverizerParameterForm;
import power.web.comm.AbstractAction;
@SuppressWarnings("serial")
public class PulverizerParameterAction extends AbstractAction
{
	private PtJPulverizerParameterFacadeRemote remote;
	private PtJPulverizerParameter param;
	public PtJPulverizerParameter getParam() {
		return param;
	}
	public void setParam(PtJPulverizerParameter param) {
		this.param = param;
	}
	public PulverizerParameterAction()
	{
		remote = (PtJPulverizerParameterFacadeRemote)factory.getFacadeRemote("PtJPulverizerParameterFacade");
	}
	
	public void getPulverizerParamByAuxId() throws JSONException
	{
		String auxiliaryIdStr = request.getParameter("auxiliaryId");
		Long auxiliaryId = null;
		PulverizerParameterForm form = new PulverizerParameterForm();
		if(auxiliaryIdStr != null && !auxiliaryIdStr.equals(""))
			auxiliaryId = Long.parseLong(auxiliaryIdStr);
		List<PtJPulverizerParameter> list = remote.findByProperty("auxiliaryId", auxiliaryId);
		if(list != null && list.size() > 0)
		{
			form.setData(list.get(0));
			write(JSONUtil.serialize(form));
		}
		else
			write(JSONUtil.serialize(form));
	}
	
	public void savePulverizerParam()
	{
		param.setIsUse("Y");
		param.setEnterpriseCode(employee.getEnterpriseCode());
		if(param.getPulverizerId() == null)
			remote.save(param);
		else
			remote.update(param);
	}
}