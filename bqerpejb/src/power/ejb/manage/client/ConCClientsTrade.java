package power.ejb.manage.client;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ConCClientsTrade entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_C_CLIENTS_TRADE")
public class ConCClientsTrade implements java.io.Serializable {

	// Fields

	private Long tradeId;
	private String tradeName;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ConCClientsTrade() {
	}

	/** minimal constructor */
	public ConCClientsTrade(Long tradeId) {
		this.tradeId = tradeId;
	}

	/** full constructor */
	public ConCClientsTrade(Long tradeId, String tradeName, String memo,
			String enterpriseCode) {
		this.tradeId = tradeId;
		this.tradeName = tradeName;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TRADE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTradeId() {
		return this.tradeId;
	}

	public void setTradeId(Long tradeId) {
		this.tradeId = tradeId;
	}

	@Column(name = "TRADE_NAME", length = 80)
	public String getTradeName() {
		return this.tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}