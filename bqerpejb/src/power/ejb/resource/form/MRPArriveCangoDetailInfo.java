package power.ejb.resource.form;

public class MRPArriveCangoDetailInfo {
	
	/** 到货单号 */
	private String mifNo = "";
	/** 检验不合格数 */
	private String badQty = "";
	/** 已收数量 */
	private String rcvQty = "";
	/** 已入库数量 */
	private String recQty = "";
	/** 本次到货数 */
	private String theQty = "";
	/** 物料状态 */
	private String itemStatus = "";
	
	public String getMifNo() {
		return mifNo;
	}
	public void setMifNo(String mifNo) {
		this.mifNo = mifNo;
	}
	public String getBadQty() {
		return badQty;
	}
	public void setBadQty(String badQty) {
		this.badQty = badQty;
	}
	public String getRcvQty() {
		return rcvQty;
	}
	public void setRcvQty(String rcvQty) {
		this.rcvQty = rcvQty;
	}
	public String getRecQty() {
		return recQty;
	}
	public void setRecQty(String recQty) {
		this.recQty = recQty;
	}
	public String getTheQty() {
		return theQty;
	}
	public void setTheQty(String theQty) {
		this.theQty = theQty;
	}
	public String getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

}
