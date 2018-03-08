package power.ejb.manage.exam.form;

@SuppressWarnings("serial")
public class BpJCbmExecutionForm implements java.io.Serializable {

	private String executionid1;
	private String executionid2;
	private String executionid3;
	private String itemid;
	private String itemcode;
	private String itemname;
	private String unitname;
	private String value1;
	private String value2;
	private String value3;
	private String dateTime;

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getExecutionid1() {
		return executionid1;
	}

	public void setExecutionid1(String executionid1) {
		this.executionid1 = executionid1;
	}

	public String getExecutionid2() {
		return executionid2;
	}

	public void setExecutionid2(String executionid2) {
		this.executionid2 = executionid2;
	}

	public String getExecutionid3() {
		return executionid3;
	}

	public void setExecutionid3(String executionid3) {
		this.executionid3 = executionid3;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

}