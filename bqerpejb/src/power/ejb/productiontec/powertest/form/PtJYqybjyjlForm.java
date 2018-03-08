package power.ejb.productiontec.powertest.form;



import power.ejb.productiontec.powertest.PtJYqybjyjl;

@SuppressWarnings("serial")
public class PtJYqybjyjlForm implements java.io.Serializable {
	private PtJYqybjyjl model;
	private String checkManName;
	private String checkDepName;
	private String testManName;
	private String names;
	
	// add by liuyi 20100512 
	private String testCycle;
	
	public String getTestCycle() {
		return testCycle;
	}
	public void setTestCycle(String testCycle) {
		this.testCycle = testCycle;
	}
	public PtJYqybjyjl getModel() {
		return model;
	}
	public void setModel(PtJYqybjyjl model) {
		this.model = model;
	}
	public String getCheckManName() {
		return checkManName;
	}
	public void setCheckManName(String checkManName) {
		this.checkManName = checkManName;
	}
	public String getCheckDepName() {
		return checkDepName;
	}
	public void setCheckDepName(String checkDepName) {
		this.checkDepName = checkDepName;
	}
	public String getTestManName() {
		return testManName;
	}
	public void setTestManName(String testManName) {
		this.testManName = testManName;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}
	
}
