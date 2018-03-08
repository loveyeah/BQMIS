package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJMonthSafeMeeting;

public class SpJMonthSafeMeetingFrom {

	private SpJMonthSafeMeeting meetingInfo;
	// 部门
	private String depName;
	// 主持人
	private String moderatorName;
	// 记录人
	private String recordByName;
	// 会议时间
	private String meetingDateString;

	public String getMeetingDateString() {
		return meetingDateString;
	}

	public void setMeetingDateString(String meetingDateString) {
		this.meetingDateString = meetingDateString;
	}

	public SpJMonthSafeMeeting getMeetingInfo() {
		return meetingInfo;
	}

	public void setMeetingInfo(SpJMonthSafeMeeting meetingInfo) {
		this.meetingInfo = meetingInfo;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getModeratorName() {
		return moderatorName;
	}

	public void setModeratorName(String moderatorName) {
		this.moderatorName = moderatorName;
	}

	public String getRecordByName() {
		return recordByName;
	}

	public void setRecordByName(String recordByName) {
		this.recordByName = recordByName;
	}

}
