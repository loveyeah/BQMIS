/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 进出车辆登记AdJCarpass
 * @author li chensheng
 *  
 */
@Entity
@Table(name = "AD_J_CARPASS")
public class AdJCarpass implements java.io.Serializable {
	//流水号
	private Long id;
	//通行证号
	private String passcode;
	//通行时间
	private Date passtime;
	//车牌号
	private String carNo;
	//证件号
	private String paperId;
	//车辆单位
	private String firm;
	//前经办人
	private String preman;
	//证件类别
	private String papertypeCd;
	//退证日期
	private Date giveDate;
	//后经办人
	private String postman;
	//是否使用
	private String isUse;
	private String crtUser;
	private String dcmStatus;
	//更新者
	private String updateUser;
	//更新时间
	private Date updateTime;
	// 企业编码
	private String enterpriseCode;

	// 构造函数 



	/** default constructor */
	public AdJCarpass() {
	}

	/** minimal constructor */
	public AdJCarpass(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJCarpass(Long id, String passcode, Date passtime, String carNo,
			String paperId, String firm, String preman, String papertypeCd,
			Date giveDate, String postman, String isUse, String crtUser,
			String dcmStatus, String updateUser, Date updateTime,String enterpriseCode) {
		this.id = id;
		this.passcode = passcode;
		this.passtime = passtime;
		this.carNo = carNo;
		this.paperId = paperId;
		this.firm = firm;
		this.preman = preman;
		this.papertypeCd = papertypeCd;
		this.giveDate = giveDate;
		this.postman = postman;
		this.isUse = isUse;
		this.crtUser = crtUser;
		this.dcmStatus = dcmStatus;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterpriseCode = enterpriseCode;
	}

	/**
	 * 流水号取得
	 */
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}
	/**
	 * 流水号设置
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 通行证号取得
	 */
	@Column(name = "PASSCODE", length = 2)
	public String getPasscode() {
		return this.passcode;
	}
	/**
	 * 通行证号设置
	 */
	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
	/**
	 * 通行时间取得
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PASSTIME", length = 7)
	public Date getPasstime() {
		return this.passtime;
	}
	/**
	 * 通行时间设置
	 */
	public void setPasstime(Date passtime) {
		this.passtime = passtime;
	}
	/**
	 * 车牌号取得
	 */
	@Column(name = "CAR_NO", length = 10)
	public String getCarNo() {
		return this.carNo;
	}
	/**
	 * 车牌号设置
	 */
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	/**
	 * 证件号取得
	 */
	@Column(name = "PAPER_ID", precision = 18, scale = 0)
	public String getPaperId() {
		return this.paperId;
	}
	/**
	 * 证件号设置
	 */
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	/**
	 * 车辆单位取得
	 */
	@Column(name = "FIRM", length = 50)
	public String getFirm() {
		return this.firm;
	}
	/**
	 * 车辆单位设置
	 */
	public void setFirm(String firm) {
		this.firm = firm;
	}
	/**
	 * 前经办人取得
	 */
	@Column(name = "PREMAN", length = 20)
	public String getPreman() {
		return this.preman;
	}
	/**
	 * 前经办人设置
	 */
	public void setPreman(String preman) {
		this.preman = preman;
	}
	/**
	 * 证件类别取得
	 */
	@Column(name = "PAPERTYPE_CD", length = 2)
	public String getPapertypeCd() {
		return this.papertypeCd;
	}
	/**
	 * 证件类别设置
	 */
	public void setPapertypeCd(String papertypeCd) {
		this.papertypeCd = papertypeCd;
	}
	/**
	 * 退证日期取得
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GIVE_DATE", length = 7)
	public Date getGiveDate() {
		return this.giveDate;
	}
	/**
	 * 退证日期设置
	 */
	public void setGiveDate(Date giveDate) {
		this.giveDate = giveDate;
	}
	/**
	 * 后经办人取得
	 */
	@Column(name = "POSTMAN", length = 20)
	public String getPostman() {
		return this.postman;
	}
	/**
	 * 后经办人设置
	 */
	public void setPostman(String postman) {
		this.postman = postman;
	}
	/**
	 * 是否使用取得
	 */
	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}
	/**
	 * 是否使用设置
	 */
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "CRT_USER", length = 10)
	public String getCrtUser() {
		return this.crtUser;
	}

	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}

	@Column(name = "DCM_STATUS", length = 1)
	public String getDcmStatus() {
		return this.dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}
	/**
	 * 更新者取得
	 */
	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}
	/**
	 * 更新者设置
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	/**
	 * 更新时间取得
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}
	/**
	 * 更新时间设置
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 企业编码
	 */            
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
}