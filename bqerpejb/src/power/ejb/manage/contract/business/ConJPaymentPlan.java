package power.ejb.manage.contract.business;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJPaymentPlan entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_PAYMENT_PLAN")
public class ConJPaymentPlan implements java.io.Serializable {

	// Fields

	private Long paymentId;
	private Long conId;
	private String paymentMoment;
	private Double payPrice;
	private Date payDate;
	private String memo;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public ConJPaymentPlan() {
	}

	/** minimal constructor */
	public ConJPaymentPlan(Long paymentId, Long conId, Double payPrice,
			Date payDate, String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.paymentId = paymentId;
		this.conId = conId;
		this.payPrice = payPrice;
		this.payDate = payDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public ConJPaymentPlan(Long paymentId, Long conId, String paymentMoment,
			Double payPrice, Date payDate, String memo, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.paymentId = paymentId;
		this.conId = conId;
		this.paymentMoment = paymentMoment;
		this.payPrice = payPrice;
		this.payDate = payDate;
		this.memo = memo;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "PAYMENT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPaymentId() {
		return this.paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	@Column(name = "CON_ID", nullable = false, precision = 10, scale = 0)
	public Long getConId() {
		return this.conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	@Column(name = "PAYMENT_MOMENT", length = 200)
	public String getPaymentMoment() {
		return this.paymentMoment;
	}

	public void setPaymentMoment(String paymentMoment) {
		this.paymentMoment = paymentMoment;
	}

	@Column(name = "PAY_PRICE", nullable = false, precision = 15, scale = 4)
	public Double getPayPrice() {
		return this.payPrice;
	}

	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PAY_DATE", nullable = false, length = 7)
	public Date getPayDate() {
		return this.payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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

}