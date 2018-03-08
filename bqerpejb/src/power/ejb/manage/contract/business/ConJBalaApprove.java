package power.ejb.manage.contract.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJBalaApprove entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_BALA_APPROVE")
public class ConJBalaApprove implements java.io.Serializable {

	// Fields

	private Long approveId;
	private Long balanceId;
	private String approveBy;
	private Date approveDate;
	private String approveOpinion;
	private Double approveAmount;
	private String billType;
	private String enterpriseCode;
	private String isUse;
	private String approveType;

	// Constructors

	/** default constructor */
	public ConJBalaApprove() {
	}

	/** minimal constructor */
	public ConJBalaApprove(Long approveId, Long balanceId,
			String enterpriseCode, String isUse) {
		this.approveId = approveId;
		this.balanceId = balanceId;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public ConJBalaApprove(Long approveId, Long balanceId, String approveBy,
			Date approveDate, String approveOpinion, Double approveAmount,
			String billType, String enterpriseCode, String isUse,
			String approveType) {
		this.approveId = approveId;
		this.balanceId = balanceId;
		this.approveBy = approveBy;
		this.approveDate = approveDate;
		this.approveOpinion = approveOpinion;
		this.approveAmount = approveAmount;
		this.billType = billType;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.approveType = approveType;
	}

	// Property accessors
	@Id
	@Column(name = "APPROVE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getApproveId() {
		return this.approveId;
	}

	public void setApproveId(Long approveId) {
		this.approveId = approveId;
	}

	@Column(name = "BALANCE_ID", nullable = false, precision = 10, scale = 0)
	public Long getBalanceId() {
		return this.balanceId;
	}

	public void setBalanceId(Long balanceId) {
		this.balanceId = balanceId;
	}

	@Column(name = "APPROVE_BY", length = 16)
	public String getApproveBy() {
		return this.approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "APPROVE_DATE", length = 7)
	public Date getApproveDate() {
		return this.approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	@Column(name = "APPROVE_OPINION", length = 1000)
	public String getApproveOpinion() {
		return this.approveOpinion;
	}

	public void setApproveOpinion(String approveOpinion) {
		this.approveOpinion = approveOpinion;
	}

	@Column(name = "APPROVE_AMOUNT", precision = 15, scale = 4)
	public Double getApproveAmount() {
		return this.approveAmount;
	}

	public void setApproveAmount(Double approveAmount) {
		this.approveAmount = approveAmount;
	}

	@Column(name = "BILL_TYPE", length = 16)
	public String getBillType() {
		return this.billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", nullable = false, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "APPROVE_TYPE", length = 2)
	public String getApproveType() {
		return this.approveType;
	}

	public void setApproveType(String approveType) {
		this.approveType = approveType;
	}

}