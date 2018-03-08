package power.ejb.hr.labor;

import java.util.List;

public class DynaicQueryClass {

	private List list;
	private String[] headerNames;
	private String[] planID;
	private int headerCount;
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public String[] getHeaderNames() {
		return headerNames;
	}
	public void setHeaderNames(String[] headerNames) {
		this.headerNames = headerNames;
	}
	public int getHeaderCount() {
		return headerCount;
	}
	public void setHeaderCount(int headerCount) {
		this.headerCount = headerCount;
	}
	public String[] getPlanID() {
		return planID;
	}
	public void setPlanID(String[] planID) {
		this.planID = planID;
	}
	
	
	
}
