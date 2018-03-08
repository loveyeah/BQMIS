package power.ejb.equ.planrepair;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCPlanContent entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_PLAN_CONTENT")
public class EquCPlanContent implements java.io.Serializable {

	// Fields

	private Long contentId;
	private String contentName;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public EquCPlanContent() {
	}

	/** minimal constructor */
	public EquCPlanContent(Long contentId) {
		this.contentId = contentId;
	}

	/** full constructor */
	public EquCPlanContent(Long contentId, String contentName, String memo,
			String isUse, String enterpriseCode) {
		this.contentId = contentId;
		this.contentName = contentName;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "CONTENT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getContentId() {
		return this.contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	@Column(name = "CONTENT_NAME", length = 100)
	public String getContentName() {
		return this.contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}