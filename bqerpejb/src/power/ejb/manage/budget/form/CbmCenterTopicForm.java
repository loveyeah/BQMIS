package power.ejb.manage.budget.form;

import power.ejb.manage.budget.CbmCCenterTopic;

@SuppressWarnings("serial")
public class CbmCenterTopicForm implements java.io.Serializable {

	private CbmCCenterTopic top;
	private String deptCode;
	private String deptName;
	private String topicCode;
	private String topicName;
	private String directManageName;

	public CbmCCenterTopic getTop() {
		return top;
	}

	public void setTop(CbmCCenterTopic top) {
		this.top = top;
	}

	public String getTopicCode() {
		return topicCode;
	}

	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getDirectManageName() {
		return directManageName;
	}

	public void setDirectManageName(String directManageName) {
		this.directManageName = directManageName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
