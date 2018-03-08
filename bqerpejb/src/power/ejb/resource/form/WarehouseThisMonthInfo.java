package power.ejb.resource.form;

public class WarehouseThisMonthInfo {
	
	/** 仓库号 */
	private String whsNo = "";
	/** 仓库名称 */
	private String whsName = "";
	private String month = "";
	/** 物资编码 */
	private String materialNo = "";
	/** 物资名称 */
	private String materialName = "";
	/** 规格型号 */
	private String specNo = "";
	/** 单位 */
	private String purUm = "";
	/** 计划单价 */
	private String stdCost = "";
	/** 上期结存数 */
	private String openBalance = "";
	/** 上期结存金额 */
	private String OBCost= "";
	/** 本期入库数 */
	private String receipt = "";
	/** 本期入库金额 */
	private String receiptCost = "";
	/** 本期支出数量 */
	private String issue = "";
	/** 本期支出金额 */
	private String issueCost = "";
	/** 本期结余数量 */
	private String thisBalance = "";
	/** 本期结余金额 */
	private String TBCost = "";
	/** 本期暂收金额 */
	private String zanShou="";  //add by fyyang 091210
	
	
	// add by liuyi 20100412 增加额外八个标识  用于存储数据用
	/** 库存/额外用 上期结存数 */
	private String eWopenBalance = "";
	/** 库存/额外用上期结存金额 */
	private String eWOBCost= "";
	/** 库存/额外用本期入库数 */
	private String eWreceipt = "";
	/** 库存/额外用本期入库金额 */
	private String eWreceiptCost = "";
	/** 库存/额外用本期支出数量 */
	private String eWissue = "";
	/** 库存/额外用本期支出金额 */
	private String eWissueCost = "";
	/** 库存/额外用本期结余数量 */
	private String eWthisBalance = "";
	/** 库存/额外用本期结余金额 */
	private String eWTBCost = "";
	
	/**
	 * @return the whsNo
	 */
	public String getWhsNo() {
		return whsNo;
	}
	/**
	 * @param whsNo the whsNo to set
	 */
	public void setWhsNo(String whsNo) {
		this.whsNo = whsNo;
	}
	/**
	 * @return the whsName
	 */
	public String getWhsName() {
		return whsName;
	}
	/**
	 * @param whsName the whsName to set
	 */
	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}
	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}
	/**
	 * @return the materialNo
	 */
	public String getMaterialNo() {
		return materialNo;
	}
	/**
	 * @param materialNo the materialNo to set
	 */
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	/**
	 * @return the materialName
	 */
	public String getMaterialName() {
		return materialName;
	}
	/**
	 * @param materialName the materialName to set
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	/**
	 * @return the specNo
	 */
	public String getSpecNo() {
		return specNo;
	}
	/**
	 * @param specNo the specNo to set
	 */
	public void setSpecNo(String specNo) {
		this.specNo = specNo;
	}
	/**
	 * @return the purUm
	 */
	public String getPurUm() {
		return purUm;
	}
	/**
	 * @param purUm the purUm to set
	 */
	public void setPurUm(String purUm) {
		this.purUm = purUm;
	}
	/**
	 * @return the stdCost
	 */
	public String getStdCost() {
		return stdCost;
	}
	/**
	 * @param stdCost the stdCost to set
	 */
	public void setStdCost(String stdCost) {
		this.stdCost = stdCost;
	}
	/**
	 * @return the openBalance
	 */
	public String getOpenBalance() {
		return openBalance;
	}
	/**
	 * @param openBalance the openBalance to set
	 */
	public void setOpenBalance(String openBalance) {
		this.openBalance = openBalance;
	}
	/**
	 * @return the oBCost
	 */
	public String getOBCost() {
		return OBCost;
	}
	/**
	 * @param cost the oBCost to set
	 */
	public void setOBCost(String cost) {
		OBCost = cost;
	}
	/**
	 * @return the receipt
	 */
	public String getReceipt() {
		return receipt;
	}
	/**
	 * @param receipt the receipt to set
	 */
	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}
	/**
	 * @return the receiptCost
	 */
	public String getReceiptCost() {
		return receiptCost;
	}
	/**
	 * @param receiptCost the receiptCost to set
	 */
	public void setReceiptCost(String receiptCost) {
		this.receiptCost = receiptCost;
	}
	/**
	 * @return the issue
	 */
	public String getIssue() {
		return issue;
	}
	/**
	 * @param issue the issue to set
	 */
	public void setIssue(String issue) {
		this.issue = issue;
	}
	/**
	 * @return the issueCost
	 */
	public String getIssueCost() {
		return issueCost;
	}
	/**
	 * @param issueCost the issueCost to set
	 */
	public void setIssueCost(String issueCost) {
		this.issueCost = issueCost;
	}
	/**
	 * @return the thisBalance
	 */
	public String getThisBalance() {
		return thisBalance;
	}
	/**
	 * @param thisBalance the thisBalance to set
	 */
	public void setThisBalance(String thisBalance) {
		this.thisBalance = thisBalance;
	}
	/**
	 * @return the tBCost
	 */
	public String getTBCost() {
		return TBCost;
	}
	/**
	 * @param cost the tBCost to set
	 */
	public void setTBCost(String cost) {
		TBCost = cost;
	}
	public String getZanShou() {
		return zanShou;
	}
	public void setZanShou(String zanShou) {
		this.zanShou = zanShou;
	}
	public String getEWopenBalance() {
		return eWopenBalance;
	}
	public void setEWopenBalance(String wopenBalance) {
		eWopenBalance = wopenBalance;
	}
	public String getEWOBCost() {
		return eWOBCost;
	}
	public void setEWOBCost(String cost) {
		eWOBCost = cost;
	}
	public String getEWreceipt() {
		return eWreceipt;
	}
	public void setEWreceipt(String wreceipt) {
		eWreceipt = wreceipt;
	}
	public String getEWreceiptCost() {
		return eWreceiptCost;
	}
	public void setEWreceiptCost(String wreceiptCost) {
		eWreceiptCost = wreceiptCost;
	}
	public String getEWissue() {
		return eWissue;
	}
	public void setEWissue(String wissue) {
		eWissue = wissue;
	}
	public String getEWissueCost() {
		return eWissueCost;
	}
	public void setEWissueCost(String wissueCost) {
		eWissueCost = wissueCost;
	}
	public String getEWthisBalance() {
		return eWthisBalance;
	}
	public void setEWthisBalance(String wthisBalance) {
		eWthisBalance = wthisBalance;
	}
	public String getEWTBCost() {
		return eWTBCost;
	}
	public void setEWTBCost(String cost) {
		eWTBCost = cost;
	}
	
}
