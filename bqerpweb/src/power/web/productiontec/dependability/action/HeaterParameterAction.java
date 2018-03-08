package power.web.productiontec.dependability.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.productiontec.dependabilityAnalysis.PtJHeaterParameter;
import power.ejb.productiontec.dependabilityAnalysis.PtJHeaterParameterFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class HeaterParameterAction extends AbstractAction {
	private PtJHeaterParameterFacadeRemote remote;
	private PtJHeaterParameter phParams;

	public HeaterParameterAction() {
		remote = (PtJHeaterParameterFacadeRemote) factory
				.getFacadeRemote("PtJHeaterParameterFacade");
	}

	public void getHeaterParameter() throws JSONException {
		String pId = request.getParameter("auxiliaryId");
		List<PtJHeaterParameter> list = remote.findInfoBypId(pId);
		// System.out.println(JSONUtil.serialize(list));
		write(JSONUtil.serialize(list));
	}

	public void saveHeaterParameter() {
		if (phParams.getHeaterId() == null) {
			phParams.setEnterpriseCode(employee.getEnterpriseCode());
			PtJHeaterParameter model = remote.save(phParams);
			write("{success : true,heaterId : " + model.getHeaterId() + "}");
		} else {
			PtJHeaterParameter entity = remote.findById(phParams.getHeaterId());
			phParams.setIsUse(entity.getIsUse());
			phParams.setEnterpriseCode(entity.getEnterpriseCode());
			PtJHeaterParameter model = remote.update(phParams);
			write("{success : true,heaterId : " + model.getHeaterId() + "}");
		}
	}

	public PtJHeaterParameter getPhParams() {
		return phParams;
	}

	public void setPhParams(PtJHeaterParameter phParams) {
		this.phParams = phParams;
	}
	
}
