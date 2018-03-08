package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJMeet entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_MEET")
public class AdJMeet implements java.io.Serializable {

	// Fields

	private Long id;
	private String meetId;
	private String applyMan;
	private String meetName;
	private Date startmeetDate;
	private Date endmeetDate;
	private String meetPlace;
	private byte[] meetPerson;
	private String roomNeed;
	private String cigName;
	private Double cigPrice;
	private Long cigNum;
	private String wineName;
	private Double winePrice;
	private Long wineNum;
	private Long tfNum;
	private String tfThing;
	private Long djNum;
	private String djThing;
	private Long bjNum;
	private String bjThing;
	private Date dinnerTime;
	private Long dinnerNum;
	private Double dinnerBz;
	private Double budpayInall;
	private Double realpayInall;
	private String meetOther;
	private String dcmStatus;
	private String workFlowNo;
	private String isUse;
	private String updateUser;
	private Date updateTime;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJMeet() {
	}

	/** minimal constructor */
	public AdJMeet(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJMeet(Long id, String meetId, String applyMan, String meetName,
			Date startmeetDate, Date endmeetDate, String meetPlace,
			byte[] meetPerson, String roomNeed, String cigName,
			Double cigPrice, Long cigNum, String wineName, Double winePrice,
			Long wineNum, Long tfNum, String tfThing, Long djNum,
			String djThing, Long bjNum, String bjThing, Date dinnerTime,
			Long dinnerNum, Double dinnerBz, Double budpayInall,
			Double realpayInall, String meetOther, String dcmStatus,
			String workFlowNo, String isUse, String updateUser,
			Date updateTime, String enterpriseCode) {
		this.id = id;
		this.meetId = meetId;
		this.applyMan = applyMan;
		this.meetName = meetName;
		this.startmeetDate = startmeetDate;
		this.endmeetDate = endmeetDate;
		this.meetPlace = meetPlace;
		this.meetPerson = meetPerson;
		this.roomNeed = roomNeed;
		this.cigName = cigName;
		this.cigPrice = cigPrice;
		this.cigNum = cigNum;
		this.wineName = wineName;
		this.winePrice = winePrice;
		this.wineNum = wineNum;
		this.tfNum = tfNum;
		this.tfThing = tfThing;
		this.djNum = djNum;
		this.djThing = djThing;
		this.bjNum = bjNum;
		this.bjThing = bjThing;
		this.dinnerTime = dinnerTime;
		this.dinnerNum = dinnerNum;
		this.dinnerBz = dinnerBz;
		this.budpayInall = budpayInall;
		this.realpayInall = realpayInall;
		this.meetOther = meetOther;
		this.dcmStatus = dcmStatus;
		this.workFlowNo = workFlowNo;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "MEET_ID", length = 12)
	public String getMeetId() {
		return this.meetId;
	}

	public void setMeetId(String meetId) {
		this.meetId = meetId;
	}

	@Column(name = "APPLY_MAN", length = 6)
	public String getApplyMan() {
		return this.applyMan;
	}

	public void setApplyMan(String applyMan) {
		this.applyMan = applyMan;
	}

	@Column(name = "MEET_NAME", length = 100)
	public String getMeetName() {
		return this.meetName;
	}

	public void setMeetName(String meetName) {
		this.meetName = meetName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STARTMEET_DATE", length = 7)
	public Date getStartmeetDate() {
		return this.startmeetDate;
	}

	public void setStartmeetDate(Date startmeetDate) {
		this.startmeetDate = startmeetDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENDMEET_DATE", length = 7)
	public Date getEndmeetDate() {
		return this.endmeetDate;
	}

	public void setEndmeetDate(Date endmeetDate) {
		this.endmeetDate = endmeetDate;
	}

	@Column(name = "MEET_PLACE", length = 50)
	public String getMeetPlace() {
		return this.meetPlace;
	}

	public void setMeetPlace(String meetPlace) {
		this.meetPlace = meetPlace;
	}

	@Column(name = "MEET_PERSON")
	public byte[] getMeetPerson() {
		return this.meetPerson;
	}

	public void setMeetPerson(byte[] meetPerson) {
		this.meetPerson = meetPerson;
	}

	@Column(name = "ROOM_NEED", length = 150)
	public String getRoomNeed() {
		return this.roomNeed;
	}

	public void setRoomNeed(String roomNeed) {
		this.roomNeed = roomNeed;
	}

	@Column(name = "CIG_NAME", length = 50)
	public String getCigName() {
		return this.cigName;
	}

	public void setCigName(String cigName) {
		this.cigName = cigName;
	}

	@Column(name = "CIG_PRICE", precision = 13)
	public Double getCigPrice() {
		return this.cigPrice;
	}

	public void setCigPrice(Double cigPrice) {
		this.cigPrice = cigPrice;
	}

	@Column(name = "CIG_NUM", precision = 8, scale = 0)
	public Long getCigNum() {
		return this.cigNum;
	}

	public void setCigNum(Long cigNum) {
		this.cigNum = cigNum;
	}

	@Column(name = "WINE_NAME", length = 50)
	public String getWineName() {
		return this.wineName;
	}

	public void setWineName(String wineName) {
		this.wineName = wineName;
	}

	@Column(name = "WINE_PRICE", precision = 13)
	public Double getWinePrice() {
		return this.winePrice;
	}

	public void setWinePrice(Double winePrice) {
		this.winePrice = winePrice;
	}

	@Column(name = "WINE_NUM", precision = 8, scale = 0)
	public Long getWineNum() {
		return this.wineNum;
	}

	public void setWineNum(Long wineNum) {
		this.wineNum = wineNum;
	}

	@Column(name = "TF_NUM", precision = 8, scale = 0)
	public Long getTfNum() {
		return this.tfNum;
	}

	public void setTfNum(Long tfNum) {
		this.tfNum = tfNum;
	}

	@Column(name = "TF_THING", length = 100)
	public String getTfThing() {
		return this.tfThing;
	}

	public void setTfThing(String tfThing) {
		this.tfThing = tfThing;
	}

	@Column(name = "DJ_NUM", precision = 8, scale = 0)
	public Long getDjNum() {
		return this.djNum;
	}

	public void setDjNum(Long djNum) {
		this.djNum = djNum;
	}

	@Column(name = "DJ_THING", length = 100)
	public String getDjThing() {
		return this.djThing;
	}

	public void setDjThing(String djThing) {
		this.djThing = djThing;
	}

	@Column(name = "BJ_NUM", precision = 8, scale = 0)
	public Long getBjNum() {
		return this.bjNum;
	}

	public void setBjNum(Long bjNum) {
		this.bjNum = bjNum;
	}

	@Column(name = "BJ_THING", length = 100)
	public String getBjThing() {
		return this.bjThing;
	}

	public void setBjThing(String bjThing) {
		this.bjThing = bjThing;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DINNER_TIME", length = 7)
	public Date getDinnerTime() {
		return this.dinnerTime;
	}

	public void setDinnerTime(Date dinnerTime) {
		this.dinnerTime = dinnerTime;
	}

	@Column(name = "DINNER_NUM", precision = 8, scale = 0)
	public Long getDinnerNum() {
		return this.dinnerNum;
	}

	public void setDinnerNum(Long dinnerNum) {
		this.dinnerNum = dinnerNum;
	}

	@Column(name = "DINNER_BZ", precision = 13)
	public Double getDinnerBz() {
		return this.dinnerBz;
	}

	public void setDinnerBz(Double dinnerBz) {
		this.dinnerBz = dinnerBz;
	}

	@Column(name = "BUDPAY_INALL", precision = 13)
	public Double getBudpayInall() {
		return this.budpayInall;
	}

	public void setBudpayInall(Double budpayInall) {
		this.budpayInall = budpayInall;
	}

	@Column(name = "REALPAY_INALL", precision = 13)
	public Double getRealpayInall() {
		return this.realpayInall;
	}

	public void setRealpayInall(Double realpayInall) {
		this.realpayInall = realpayInall;
	}

	@Column(name = "MEET_OTHER", length = 200)
	public String getMeetOther() {
		return this.meetOther;
	}

	public void setMeetOther(String meetOther) {
		this.meetOther = meetOther;
	}

	@Column(name = "DCM_STATUS", length = 1)
	public String getDcmStatus() {
		return this.dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

	@Column(name = "WORK_FLOW_NO", length = 26)
	public String getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "UPDATE_USER", length = 6)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}