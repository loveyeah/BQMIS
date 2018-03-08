package power.ejb.equ.failure;

import java.util.List;

@SuppressWarnings("serial")
public class EquYearReportForm implements java.io.Serializable{
	String specialCode;
	String specialName;
	List<EquFailuresQueryForm> list;
	public String getSpecialCode() {
		return specialCode;
	}
	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}
	public String getSpecialName() {
		return specialName;
	}
	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}
	public List<EquFailuresQueryForm> getList() {
		return list;
	}
	public void setList(List<EquFailuresQueryForm> list) {
		this.list = list;
	}

}
