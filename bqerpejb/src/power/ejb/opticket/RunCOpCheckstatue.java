package power.ejb.opticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCOpCheckstatue entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_OP_CHECKSTATUE")
public class RunCOpCheckstatue implements java.io.Serializable {

	// Fields

	private Long checkStatueId;
	private String checkStatue;
	private Long displayNo;
	private String checkBefFlag;
	private String isUse;
	private String isRunFlag;
	private String enterpriseCode;
	private String modifyBy;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public RunCOpCheckstatue() {
	}

	/** minimal constructor */
	public RunCOpCheckstatue(Long checkStatueId) {
		this.checkStatueId = checkStatueId;
	}

	/** full constructor */
	public RunCOpCheckstatue(Long checkStatueId, String checkStatue,
			Long displayNo, String checkBefFlag, String isUse,
			String isRunFlag, String enterpriseCode, String modifyBy,
			Date modifyDate) {
		this.checkStatueId = checkStatueId;
		this.checkStatue = checkStatue;
		this.displayNo = displayNo;
		this.checkBefFlag = checkBefFlag;
		this.isUse = isUse;
		this.isRunFlag = isRunFlag;
		this.enterpriseCode = enterpriseCode;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
	}

	// Property accessors
	@Id
	@Column(name = "CHECK_STATUE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCheckStatueId() {
		return this.checkStatueId;
	}

	public void setCheckStatueId(Long checkStatueId) {
		this.checkStatueId = checkStatueId;
	}

	@Column(name = "CHECK_STATUE", length = 500)
	public String getCheckStatue() {
		return this.checkStatue;
	}

	public void setCheckStatue(String checkStatue) {
		this.checkStatue = checkStatue;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "CHECK_BEF_FLAG", length = 1)
	public String getCheckBefFlag() {
		return this.checkBefFlag;
	}

	public void setCheckBefFlag(String checkBefFlag) {
		this.checkBefFlag = checkBefFlag;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "IS_RUN_FLAG", length = 1)
	public String getIsRunFlag() {
		return this.isRunFlag;
	}

	public void setIsRunFlag(String isRunFlag) {
		this.isRunFlag = isRunFlag;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}