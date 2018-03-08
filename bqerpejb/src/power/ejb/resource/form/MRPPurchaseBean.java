package power.ejb.resource.form;

public class MRPPurchaseBean {
	
	/** 采购单明细号 */
	private String orderDetailsId = "";
	/** 采购单号 */
	private String purNo = "";
	 /** 采购数量 */
    private String purchaseQuatity="";
    /** 已收数量 */
    private String achieveQuantity="";
    /** 占收数量 */
    private String INS_QTY="";
    
	public String getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(String orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	public String getPurNo() {
		return purNo;
	}
	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}
	public String getPurchaseQuatity() {
		return purchaseQuatity;
	}
	public void setPurchaseQuatity(String purchaseQuatity) {
		this.purchaseQuatity = purchaseQuatity;
	}
	public String getAchieveQuantity() {
		return achieveQuantity;
	}
	public void setAchieveQuantity(String achieveQuantity) {
		this.achieveQuantity = achieveQuantity;
	}
	public String getINS_QTY() {
		return INS_QTY;
	}
	public void setINS_QTY(String ins_qty) {
		INS_QTY = ins_qty;
	}
}
