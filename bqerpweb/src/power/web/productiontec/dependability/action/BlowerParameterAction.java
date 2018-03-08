package power.web.productiontec.dependability.action;

import java.util.List;

import power.ejb.productiontec.dependabilityAnalysis.business.PtJBlowerParameter;
import power.ejb.productiontec.dependabilityAnalysis.business.PtJBlowerParameterFacadeRemote;
import power.ejb.productiontec.dependabilityAnalysis.business.form.BlowerParameterForm;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
@SuppressWarnings("serial")
public class BlowerParameterAction extends AbstractAction
{
	private PtJBlowerParameterFacadeRemote remote;
	private PtJBlowerParameter param;
	public PtJBlowerParameter getParam() {
		return param;
	}
	public void setParam(PtJBlowerParameter param) {
		this.param = param;
	}
	public BlowerParameterAction()
	{
		remote = (PtJBlowerParameterFacadeRemote)factory.getFacadeRemote("PtJBlowerParameterFacade");
	}
	
	public void getBlowerParamByAuxId() throws JSONException
	{     
		String auxiliaryIdStr = request.getParameter("auxiliaryId");
		Long auxiliaryId = null;
		BlowerParameterForm form = new BlowerParameterForm();
		if(auxiliaryIdStr != null && !auxiliaryIdStr.equals(""))
			auxiliaryId = Long.parseLong(auxiliaryIdStr);
		List<PtJBlowerParameter> list = remote.findByProperty("auxiliaryId", auxiliaryId);
		if(list != null && list.size() > 0)
		{
			form.setData(list.get(0));
			write(JSONUtil.serialize(form));
		}
		else
			write(JSONUtil.serialize(form));
	}
	
	public void saveBlowerParam()
	{
		param.setIsUse("Y");
		param.setEnterpriseCode(employee.getEnterpriseCode());
		if(param.getBlowerId() == null)
			remote.save(param);
		else
			remote.update(param);
	}
}