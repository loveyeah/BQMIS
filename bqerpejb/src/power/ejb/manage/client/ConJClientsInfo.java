package power.ejb.manage.client;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJClientsInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_CLIENTS_INFO")
public class ConJClientsInfo implements java.io.Serializable {

	// Fields

	private Long cliendId;
	private Long typeId;
	private Long characterId;
	private Long importanceId;
	private Long tradeId;
	private String clientCode;
	private String clientName;
	private String cliendChargeby;
	private String clientOverview;
	private String mainProducts;
	private String mainPerformance;
	private String address;
	private String telephone;
	private Long zipcode;
	private String email;
	private String website;
	private String fax;
	private String legalRepresentative;
	private String taxQualification;
	private String bank;
	private String account;
	private String taxnumber;
	private Double regFunds;
	private String regAddress;
	private String memo;
	private Long approveFlag;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ConJClientsInfo() {
	}

	/** minimal constructor */
	public ConJClientsInfo(Long cliendId) {
		this.cliendId = cliendId;
	}

	/** full constructor */
	public ConJClientsInfo(Long cliendId, Long typeId, Long characterId,
			Long importanceId, Long tradeId, String clientCode,
			String clientName, String cliendChargeby, String clientOverview,
			String mainProducts, String mainPerformance, String address,
			String telephone, Long zipcode, String email, String website,
			String fax, String legalRepresentative, String taxQualification,
			String bank, String account, String taxnumber, Double regFunds,
			String regAddress, String memo, Long approveFlag,
			String lastModifiedBy, Date lastModifiedDate, String enterpriseCode) {
		this.cliendId = cliendId;
		this.typeId = typeId;
		this.characterId = characterId;
		this.importanceId = importanceId;
		this.tradeId = tradeId;
		this.clientCode = clientCode;
		this.clientName = clientName;
		this.cliendChargeby = cliendChargeby;
		this.clientOverview = clientOverview;
		this.mainProducts = mainProducts;
		this.mainPerformance = mainPerformance;
		this.address = address;
		this.telephone = telephone;
		this.zipcode = zipcode;
		this.email = email;
		this.website = website;
		this.fax = fax;
		this.legalRepresentative = legalRepresentative;
		this.taxQualification = taxQualification;
		this.bank = bank;
		this.account = account;
		this.taxnumber = taxnumber;
		this.regFunds = regFunds;
		this.regAddress = regAddress;
		this.memo = memo;
		this.approveFlag = approveFlag;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "CLIEND_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCliendId() {
		return this.cliendId;
	}

	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}

	@Column(name = "TYPE_ID", precision = 10, scale = 0)
	public Long getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	@Column(name = "CHARACTER_ID", precision = 10, scale = 0)
	public Long getCharacterId() {
		return this.characterId;
	}

	public void setCharacterId(Long characterId) {
		this.characterId = characterId;
	}

	@Column(name = "IMPORTANCE_ID", precision = 10, scale = 0)
	public Long getImportanceId() {
		return this.importanceId;
	}

	public void setImportanceId(Long importanceId) {
		this.importanceId = importanceId;
	}

	@Column(name = "TRADE_ID", precision = 10, scale = 0)
	public Long getTradeId() {
		return this.tradeId;
	}

	public void setTradeId(Long tradeId) {
		this.tradeId = tradeId;
	}

	@Column(name = "CLIENT_CODE", length = 10)
	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	@Column(name = "CLIENT_NAME", length = 100)
	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Column(name = "CLIEND_CHARGEBY", length = 10)
	public String getCliendChargeby() {
		return this.cliendChargeby;
	}

	public void setCliendChargeby(String cliendChargeby) {
		this.cliendChargeby = cliendChargeby;
	}

	@Column(name = "CLIENT_OVERVIEW", length = 500)
	public String getClientOverview() {
		return this.clientOverview;
	}

	public void setClientOverview(String clientOverview) {
		this.clientOverview = clientOverview;
	}

	@Column(name = "MAIN_PRODUCTS", length = 500)
	public String getMainProducts() {
		return this.mainProducts;
	}

	public void setMainProducts(String mainProducts) {
		this.mainProducts = mainProducts;
	}

	@Column(name = "MAIN_PERFORMANCE", length = 500)
	public String getMainPerformance() {
		return this.mainPerformance;
	}

	public void setMainPerformance(String mainPerformance) {
		this.mainPerformance = mainPerformance;
	}

	@Column(name = "ADDRESS", length = 200)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "TELEPHONE", length = 20)
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "ZIPCODE", precision = 6, scale = 0)
	public Long getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(Long zipcode) {
		this.zipcode = zipcode;
	}

	@Column(name = "EMAIL", length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "WEBSITE", length = 100)
	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Column(name = "FAX", length = 30)
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "LEGAL_REPRESENTATIVE", length = 10)
	public String getLegalRepresentative() {
		return this.legalRepresentative;
	}

	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}

	@Column(name = "TAX_QUALIFICATION", length = 100)
	public String getTaxQualification() {
		return this.taxQualification;
	}

	public void setTaxQualification(String taxQualification) {
		this.taxQualification = taxQualification;
	}

	@Column(name = "BANK", length = 50)
	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@Column(name = "ACCOUNT", length = 50)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "TAXNUMBER", length = 50)
	public String getTaxnumber() {
		return this.taxnumber;
	}

	public void setTaxnumber(String taxnumber) {
		this.taxnumber = taxnumber;
	}

	@Column(name = "REG_FUNDS", precision = 15, scale = 4)
	public Double getRegFunds() {
		return this.regFunds;
	}

	public void setRegFunds(Double regFunds) {
		this.regFunds = regFunds;
	}

	@Column(name = "REG_ADDRESS", length = 300)
	public String getRegAddress() {
		return this.regAddress;
	}

	public void setRegAddress(String regAddress) {
		this.regAddress = regAddress;
	}

	@Column(name = "MEMO", length = 300)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "APPROVE_FLAG", precision = 1, scale = 0)
	public Long getApproveFlag() {
		return this.approveFlag;
	}

	public void setApproveFlag(Long approveFlag) {
		this.approveFlag = approveFlag;
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

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}