package power.ejb.hr.labor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrJSsYearcount entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_SS_YEARCOUNT")
public class HrJSsYearcount implements java.io.Serializable {

	// Fields

	private Long detailId;
	private Long mainId;
	private String yearCountCode;
	private String identityCardNumber;
	private String deptName;
	private String personelName;
	private Double monthEnterAccount;
	private Double monthPersonnelAccount;
	private Double monthTotal;
	private String paymentMonth;
	private String selfSign;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJSsYearcount() {
	}

	/** minimal constructor */
	public HrJSsYearcount(Long detailId) {
		this.detailId = detailId;
	}

	/** full constructor */
	public HrJSsYearcount(Long detailId, Long mainId, String yearCountCode,
			String identityCardNumber, String deptName, String personelName,
			Double monthEnterAccount, Double monthPersonnelAccount,
			Double monthTotal, String paymentMonth, String selfSign,
			String isUse, String enterpriseCode) {
		this.detailId = detailId;
		this.mainId = mainId;
		this.yearCountCode = yearCountCode;
		this.identityCardNumber = identityCardNumber;
		this.deptName = deptName;
		this.personelName = personelName;
		this.monthEnterAccount = monthEnterAccount;
		this.monthPersonnelAccount = monthPersonnelAccount;
		this.monthTotal = monthTotal;
		this.paymentMonth = paymentMonth;
		this.selfSign = selfSign;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDetailId() {
		return this.detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	@Column(name = "MAIN_ID", precision = 10, scale = 0)
	public Long getMainId() {
		return this.mainId;
	}

	public void setMainId(Long mainId) {
		this.mainId = mainId;
	}

	@Column(name = "YEAR_COUNT_CODE", length = 30)
	public String getYearCountCode() {
		return this.yearCountCode;
	}

	public void setYearCountCode(String yearCountCode) {
		this.yearCountCode = yearCountCode;
	}

	@Column(name = "IDENTITY_CARD_NUMBER", length = 30)
	public String getIdentityCardNumber() {
		return this.identityCardNumber;
	}

	public void setIdentityCardNumber(String identityCardNumber) {
		this.identityCardNumber = identityCardNumber;
	}

	@Column(name = "DEPT_NAME", length = 30)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "PERSONEL_NAME", length = 30)
	public String getPersonelName() {
		return this.personelName;
	}

	public void setPersonelName(String personelName) {
		this.personelName = personelName;
	}

	@Column(name = "MONTH_ENTER_ACCOUNT", precision = 15, scale = 4)
	public Double getMonthEnterAccount() {
		return this.monthEnterAccount;
	}

	public void setMonthEnterAccount(Double monthEnterAccount) {
		this.monthEnterAccount = monthEnterAccount;
	}

	@Column(name = "MONTH_PERSONNEL_ACCOUNT", precision = 15, scale = 4)
	public Double getMonthPersonnelAccount() {
		return this.monthPersonnelAccount;
	}

	public void setMonthPersonnelAccount(Double monthPersonnelAccount) {
		this.monthPersonnelAccount = monthPersonnelAccount;
	}

	@Column(name = "MONTH_TOTAL", precision = 15, scale = 4)
	public Double getMonthTotal() {
		return this.monthTotal;
	}

	public void setMonthTotal(Double monthTotal) {
		this.monthTotal = monthTotal;
	}

	@Column(name = "PAYMENT_MONTH", length = 10)
	public String getPaymentMonth() {
		return this.paymentMonth;
	}

	public void setPaymentMonth(String paymentMonth) {
		this.paymentMonth = paymentMonth;
	}

	@Column(name = "SELF_SIGN", length = 30)
	public String getSelfSign() {
		return this.selfSign;
	}

	public void setSelfSign(String selfSign) {
		this.selfSign = selfSign;
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