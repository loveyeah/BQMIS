/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCContractterm entity.
 * 
 * @author zhouxu，jincong
 */
@Entity
@Table(name = "HR_C_CONTRACTTERM")
public class HrCContractterm implements java.io.Serializable {

	// Fields

    /** 劳动合同有效期ID */
	private Long contractTermId;
	/** 劳动合同有效期 */
	private String contractTerm;
	/** 显示顺序 */
	private Long contractDisplayNo;
	/** 企业编码 */
	private String enterpriseCode;
	/** 是否使用 */
	private String isUse;
	/** 上次修改人 */
	private String lastModifiedBy;
	/** 上次修改日期 */
	private Date lastModifiedDate;
	/** 记录人 */
	private String insertby;
	/** 记录日期 */
	private Date insertdate;

	// Constructors

	/** default constructor */
	public HrCContractterm() {
	}

	/** minimal constructor */
	public HrCContractterm(Long contractTermId) {
		this.contractTermId = contractTermId;
	}

	/** full constructor */
	public HrCContractterm(Long contractTermId, String contractTerm,
			Long contractDisplayNo, String enterpriseCode, String isUse,
			String lastModifiedBy, Date lastModifiedDate, String insertby,
			Date insertdate) {
		this.contractTermId = contractTermId;
		this.contractTerm = contractTerm;
		this.contractDisplayNo = contractDisplayNo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.insertby = insertby;
		this.insertdate = insertdate;
	}

	// Property accessors
	@Id
	@Column(name = "CONTRACT_TERM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getContractTermId() {
		return this.contractTermId;
	}

	public void setContractTermId(Long contractTermId) {
		this.contractTermId = contractTermId;
	}

	@Column(name = "CONTRACT_TERM", length = 30)
	public String getContractTerm() {
		return this.contractTerm;
	}

	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}

	@Column(name = "CONTRACT_DISPLAY_NO", precision = 10, scale = 0)
	public Long getContractDisplayNo() {
		return this.contractDisplayNo;
	}

	public void setContractDisplayNo(Long contractDisplayNo) {
		this.contractDisplayNo = contractDisplayNo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

}