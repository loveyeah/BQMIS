package power.ejb.manage.contract.form;

@SuppressWarnings("serial")
public class PrjQualityForm implements java.io.Serializable{
	//工程项目编号
	private String prjNo;
	//工程项目名称
	private String prjName;
	//施工单位
	private String constructionUnit;
	//付款说明
	private String balaCause;
	//质保金额
	private Double applicatPrice;
	//支付金额
	private Double balaPrice;
	//质保期限
	private Long warrantyPeriod;
	
	public String getPrjNo() {
		return prjNo;
	}
	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
	}
	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	public String getConstructionUnit() {
		return constructionUnit;
	}
	public void setConstructionUnit(String constructionUnit) {
		this.constructionUnit = constructionUnit;
	}
	public String getBalaCause() {
		return balaCause;
	}
	public void setBalaCause(String balaCause) {
		this.balaCause = balaCause;
	}
	public Double getApplicatPrice() {
		return applicatPrice;
	}
	public void setApplicatPrice(Double applicatPrice) {
		this.applicatPrice = applicatPrice;
	}
	public Double getBalaPrice() {
		return balaPrice;
	}
	public void setBalaPrice(Double balaPrice) {
		this.balaPrice = balaPrice;
	}
	public Long getWarrantyPeriod() {
		return warrantyPeriod;
	}
	public void setWarrantyPeriod(Long warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	} 
}
