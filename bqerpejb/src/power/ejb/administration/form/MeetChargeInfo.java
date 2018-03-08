/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * 会务费用管理.
 * 
 * @author wangyun
 */
public class MeetChargeInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 会议审批单表 */
	/** 会议申请单号 */
	private String meetId;
	/** 申请人 */
	private String name;
	/** 申请部门 */
	private String depName;
	/** 会议开始时间 */
	private String startMeetDate;
	/** 会议结束时间 */
	private String endMeetDate;
	/** 会议名称 */
	private String meetName;
	/** 会场要求 */
	private String roomNeed;
	/** 会议地点 */
	private String meetPlace;
	/** 会议其他要求 */
	private String meetOther;
	/** 就餐时间 */
	private String dinnerTime;
	/** 就餐人数 */
	private Long dinnerNum;
	/** 会议用烟名称 */
	private String cigName;
	/** 会议用烟价格 */
	private Double cigPrice;
	/** 会议用烟数量 */
	private Long cigNum;
	/** 会议用酒名称 */
	private String wineName;
	/** 会议用酒价格 */
	private Double winePrice;
	/** 会议用酒数量 */
	private Long wineNum;
	/** 会议住宿-套房数量 */
	private Long tfNum;
	/** 会议住宿-套房用品 */
	private String tfThing;
	/** 会议住宿-单间数量 */
	private Long djNum;
	/** 会议住宿-单间用品 */
	private String djThing;
	/** 会议住宿-标间数量 */
	private Long bjNum;
	/** 会议住宿-标间用品 */
	private String bjThing;
	/** 用餐标准 */
	private Double dinnerBz;
	/** 预计费用汇总 */
	private Double budpayInall;
	/** 实际费用汇总 */
	private Double realpayInall;
	/** 修改时间 */
	private String meetUpdateTime;

	/** 会议附件信息表 */
	/** 序号 */
	private Long id;
	/** 附件名称 */
	private String fileName;
	/** 修改时间 */
	private String meetFileUpdateTime;

	/** 会议接待审批费用 */
	private String meetMxUpdateTime;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the meetId
	 */
	public String getMeetId() {
		return meetId;
	}

	/**
	 * @param meetId
	 *            the meetId to set
	 */
	public void setMeetId(String meetId) {
		this.meetId = meetId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the depName
	 */
	public String getDepName() {
		return depName;
	}

	/**
	 * @param depName
	 *            the depName to set
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/**
	 * @return the startMeetDate
	 */
	public String getStartMeetDate() {
		return startMeetDate;
	}

	/**
	 * @param startMeetDate
	 *            the startMeetDate to set
	 */
	public void setStartMeetDate(String startMeetDate) {
		this.startMeetDate = startMeetDate;
	}

	/**
	 * @return the endMeetDate
	 */
	public String getEndMeetDate() {
		return endMeetDate;
	}

	/**
	 * @param endMeetDate
	 *            the endMeetDate to set
	 */
	public void setEndMeetDate(String endMeetDate) {
		this.endMeetDate = endMeetDate;
	}

	/**
	 * @return the meetName
	 */
	public String getMeetName() {
		return meetName;
	}

	/**
	 * @param meetName
	 *            the meetName to set
	 */
	public void setMeetName(String meetName) {
		this.meetName = meetName;
	}

	/**
	 * @return the roomNeed
	 */
	public String getRoomNeed() {
		return roomNeed;
	}

	/**
	 * @param roomNeed
	 *            the roomNeed to set
	 */
	public void setRoomNeed(String roomNeed) {
		this.roomNeed = roomNeed;
	}

	/**
	 * @return the meetPlace
	 */
	public String getMeetPlace() {
		return meetPlace;
	}

	/**
	 * @param meetPlace
	 *            the meetPlace to set
	 */
	public void setMeetPlace(String meetPlace) {
		this.meetPlace = meetPlace;
	}

	/**
	 * @return the meetOther
	 */
	public String getMeetOther() {
		return meetOther;
	}

	/**
	 * @param meetOther
	 *            the meetOther to set
	 */
	public void setMeetOther(String meetOther) {
		this.meetOther = meetOther;
	}

	/**
	 * @return the dinnerTime
	 */
	public String getDinnerTime() {
		return dinnerTime;
	}

	/**
	 * @param dinnerTime
	 *            the dinnerTime to set
	 */
	public void setDinnerTime(String dinnerTime) {
		this.dinnerTime = dinnerTime;
	}

	/**
	 * @return the dinnerNum
	 */
	public Long getDinnerNum() {
		return dinnerNum;
	}

	/**
	 * @param dinnerNum
	 *            the dinnerNum to set
	 */
	public void setDinnerNum(Long dinnerNum) {
		this.dinnerNum = dinnerNum;
	}

	/**
	 * @return the cigName
	 */
	public String getCigName() {
		return cigName;
	}

	/**
	 * @param cigName
	 *            the cigName to set
	 */
	public void setCigName(String cigName) {
		this.cigName = cigName;
	}

	/**
	 * @return the cigNum
	 */
	public Long getCigNum() {
		return cigNum;
	}

	/**
	 * @param cigNum
	 *            the cigNum to set
	 */
	public void setCigNum(Long cigNum) {
		this.cigNum = cigNum;
	}

	/**
	 * @return the wineName
	 */
	public String getWineName() {
		return wineName;
	}

	/**
	 * @param wineName
	 *            the wineName to set
	 */
	public void setWineName(String wineName) {
		this.wineName = wineName;
	}

	/**
	 * @return the wineNum
	 */
	public Long getWineNum() {
		return wineNum;
	}

	/**
	 * @param wineNum
	 *            the wineNum to set
	 */
	public void setWineNum(Long wineNum) {
		this.wineNum = wineNum;
	}

	/**
	 * @return the tfNum
	 */
	public Long getTfNum() {
		return tfNum;
	}

	/**
	 * @param tfNum
	 *            the tfNum to set
	 */
	public void setTfNum(Long tfNum) {
		this.tfNum = tfNum;
	}

	/**
	 * @return the tfThing
	 */
	public String getTfThing() {
		return tfThing;
	}

	/**
	 * @param tfThing
	 *            the tfThing to set
	 */
	public void setTfThing(String tfThing) {
		this.tfThing = tfThing;
	}

	/**
	 * @return the djNum
	 */
	public Long getDjNum() {
		return djNum;
	}

	/**
	 * @param djNum
	 *            the djNum to set
	 */
	public void setDjNum(Long djNum) {
		this.djNum = djNum;
	}

	/**
	 * @return the djThing
	 */
	public String getDjThing() {
		return djThing;
	}

	/**
	 * @param djThing
	 *            the djThing to set
	 */
	public void setDjThing(String djThing) {
		this.djThing = djThing;
	}

	/**
	 * @return the bjNum
	 */
	public Long getBjNum() {
		return bjNum;
	}

	/**
	 * @param bjNum
	 *            the bjNum to set
	 */
	public void setBjNum(Long bjNum) {
		this.bjNum = bjNum;
	}

	/**
	 * @return the bjThing
	 */
	public String getBjThing() {
		return bjThing;
	}

	/**
	 * @param bjThing
	 *            the bjThing to set
	 */
	public void setBjThing(String bjThing) {
		this.bjThing = bjThing;
	}

	/**
	 * @return the cigPrice
	 */
	public Double getCigPrice() {
		return cigPrice;
	}

	/**
	 * @param cigPrice
	 *            the cigPrice to set
	 */
	public void setCigPrice(Double cigPrice) {
		this.cigPrice = cigPrice;
	}

	/**
	 * @return the winePrice
	 */
	public Double getWinePrice() {
		return winePrice;
	}

	/**
	 * @param winePrice
	 *            the winePrice to set
	 */
	public void setWinePrice(Double winePrice) {
		this.winePrice = winePrice;
	}

	/**
	 * @return the dinnerBz
	 */
	public Double getDinnerBz() {
		return dinnerBz;
	}

	/**
	 * @param dinnerBz
	 *            the dinnerBz to set
	 */
	public void setDinnerBz(Double dinnerBz) {
		this.dinnerBz = dinnerBz;
	}

	/**
	 * @return the budpayInall
	 */
	public Double getBudpayInall() {
		return budpayInall;
	}

	/**
	 * @param budpayInall
	 *            the budpayInall to set
	 */
	public void setBudpayInall(Double budpayInall) {
		this.budpayInall = budpayInall;
	}

	/**
	 * @return the realpayInall
	 */
	public Double getRealpayInall() {
		return realpayInall;
	}

	/**
	 * @param realpayInall
	 *            the realpayInall to set
	 */
	public void setRealpayInall(Double realpayInall) {
		this.realpayInall = realpayInall;
	}

	/**
	 * @return the meetUpdateTime
	 */
	public String getMeetUpdateTime() {
		return meetUpdateTime;
	}

	/**
	 * @param meetUpdateTime
	 *            the meetUpdateTime to set
	 */
	public void setMeetUpdateTime(String meetUpdateTime) {
		this.meetUpdateTime = meetUpdateTime;
	}

	/**
	 * @return the meetFileUpdateTime
	 */
	public String getMeetFileUpdateTime() {
		return meetFileUpdateTime;
	}

	/**
	 * @param meetFileUpdateTime
	 *            the meetFileUpdateTime to set
	 */
	public void setMeetFileUpdateTime(String meetFileUpdateTime) {
		this.meetFileUpdateTime = meetFileUpdateTime;
	}

	/**
	 * @return the meetMxUpdateTime
	 */
	public String getMeetMxUpdateTime() {
		return meetMxUpdateTime;
	}

	/**
	 * @param meetMxUpdateTime
	 *            the meetMxUpdateTime to set
	 */
	public void setMeetMxUpdateTime(String meetMxUpdateTime) {
		this.meetMxUpdateTime = meetMxUpdateTime;
	}

}
