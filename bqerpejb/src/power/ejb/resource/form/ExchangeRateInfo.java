package power.ejb.resource.form;

public class ExchangeRateInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 基准币别  
	private String oriCurrencyName;
	// 兑换币别  
    private String dstCurrencyName;
	// 基准币别  id
	private String oriCurrencyId;
	// 兑换币别  id  
    private String dstCurrencyId;
    // 汇率
    private String rate;
    // 有效开始日期
	private String effectiveDate;
	// 有效截止日期
	private String discontinueDate; 
	//流水号
	private String exchangeRateId;
	
	public String getOriCurrencyName() {
		return oriCurrencyName;
	}
	public void setOriCurrencyName(String oriCurrencyName) {
		this.oriCurrencyName = oriCurrencyName;
	}
	public String getDstCurrencyName() {
		return dstCurrencyName;
	}
	public void setDstCurrencyName(String dstCurrencyName) {
		this.dstCurrencyName = dstCurrencyName;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getDiscontinueDate() {
		return discontinueDate;
	}
	public void setDiscontinueDate(String discontinueDate) {
		this.discontinueDate = discontinueDate;
	}
	public String getExchangeRateId() {
		return exchangeRateId;
	}
	public void setExchangeRateId(String exchangeRateId) {
		this.exchangeRateId = exchangeRateId;
	}
	public String getOriCurrencyId() {
		return oriCurrencyId;
	}
	public void setOriCurrencyId(String oriCurrencyId) {
		this.oriCurrencyId = oriCurrencyId;
	}
	public String getDstCurrencyId() {
		return dstCurrencyId;
	}
	public void setDstCurrencyId(String dstCurrencyId) {
		this.dstCurrencyId = dstCurrencyId;
	}

}
