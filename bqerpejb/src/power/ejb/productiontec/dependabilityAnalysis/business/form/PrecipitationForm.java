package power.ejb.productiontec.dependabilityAnalysis.business.form;

import java.io.Serializable;

import power.ejb.productiontec.dependabilityAnalysis.business.PtJPrecipitationParameter;

@SuppressWarnings("serial")
public class PrecipitationForm implements Serializable{
	private PtJPrecipitationParameter data;

	public PtJPrecipitationParameter getData() {
		return data;
	}

	public void setData(PtJPrecipitationParameter data) {
		this.data = data;
	}
}
