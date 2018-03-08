package power.ejb.productiontec.dependabilityAnalysis.business.form;

import java.io.Serializable;

import power.ejb.productiontec.dependabilityAnalysis.business.PtJFeedpumpParameter;

@SuppressWarnings("serial")
public class FeedpumpParameterForm implements Serializable{
	private PtJFeedpumpParameter data;
	
	public PtJFeedpumpParameter getData() {
		return data;
	}
	public void setData(PtJFeedpumpParameter data) {
		this.data = data;
	}
}
