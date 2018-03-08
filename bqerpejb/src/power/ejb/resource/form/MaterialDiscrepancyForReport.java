package power.ejb.resource.form;

import java.util.List;

public class MaterialDiscrepancyForReport {
	/**
	 * 材料差异期初值
	 */
	private String disOpenBalance;
	/**
	 * 材料差异本期收入值
	 */
	private String disReceipt;
	
	/**
	 * 材料差异合计数
	 */
	private String disTotal;
	
	/**
	 * 原材料期初值
	 */
	private String oriOpenBalance;
	/**
	 * 原材料本期收入值
	 */
	private String oriReceipt;
	/**
	 * 原材料合计数
	 */
	private String  oriTotal;
	/**
	 * 材料差异率
	 */
	private String disRate;
	/**
	 * 材料差异暂收
	 */
	private String  disZanShou;
	/**
	 * 原材料暂收
	 */
	private String  oriZanShou;
	
	private List<IssueInfoForReport> issueList;

	public String getDisOpenBalance() {
		return disOpenBalance;
	}

	public void setDisOpenBalance(String disOpenBalance) {
		this.disOpenBalance = disOpenBalance;
	}

	public String getDisReceipt() {
		return disReceipt;
	}

	public void setDisReceipt(String disReceipt) {
		this.disReceipt = disReceipt;
	}

	public String getDisTotal() {
		return disTotal;
	}

	public void setDisTotal(String disTotal) {
		this.disTotal = disTotal;
	}

	public String getOriOpenBalance() {
		return oriOpenBalance;
	}

	public void setOriOpenBalance(String oriOpenBalance) {
		this.oriOpenBalance = oriOpenBalance;
	}

	public String getOriReceipt() {
		return oriReceipt;
	}

	public void setOriReceipt(String oriReceipt) {
		this.oriReceipt = oriReceipt;
	}

	public String getOriTotal() {
		return oriTotal;
	}

	public void setOriTotal(String oriTotal) {
		this.oriTotal = oriTotal;
	}

	public String getDisRate() {
		return disRate;
	}

	public void setDisRate(String disRate) {
		this.disRate = disRate;
	}

	public List<IssueInfoForReport> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<IssueInfoForReport> issueList) {
		this.issueList = issueList;
	}

	public String getDisZanShou() {
		return disZanShou;
	}

	public void setDisZanShou(String disZanShou) {
		this.disZanShou = disZanShou;
	}

	public String getOriZanShou() {
		return oriZanShou;
	}

	public void setOriZanShou(String oriZanShou) {
		this.oriZanShou = oriZanShou;
	}

}
