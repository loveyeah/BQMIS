package power.ejb.resource.form;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 到货明细bean
 * @author zhaozhijie
 */
public class ArriveCangoDetailInfo implements java.io.Serializable{

	/** 采购订单明细 */
	/** 采购单号 */
	private String purNo = "";
	/** 供应商 */
	private String supplier = "";
	/** 供应商id */
	private Long supplierId = null;
	/** 合同号 */
	private String contract = "";
	/** 到货单号 */
	private String mifNo = "";
	/** 日期 */
	private Date date = null;
	/** 流水号 */
	private Long id = null;
	/** 到货登记流水号 */
	private Long arrivalId = null;
	/** 备注 */
	private String memo = "";
	/** 明细备注 */
	private String detailMemo = "";

	/** 操作员 */
	private String operateBy = "";
	/** 物资编码 */
	private String materialNo = "";
	/** 物料ID */
	private Long materialID = null;
	/** 物资名称 */
	private String materialName = "";
	/** 规格型号 */
	private String specNo = "";
	/** 单位 */
	private Long purUm = null;
	/** 采购数 */
	private Double purQty = null;
	/** 已收数 */
	private Double rcvQty = null;
	/** 待收数 */
	private Double insQty = null;
	/** 到货数 */
	private Double theQty = null;
	/** 批号 */
	private String lotCode = "";
	/** 到货单ID */
	private Long arrivalID = null;
	/** 到货单详细ID */
	private Long arrivalDID = null;
	/** 到货登记/验收表最后修改时间*/
	private String dtArrivalInfo = "";
	/** 到货登记/验收明细表最后修改时间*/
	private String dtArrivalDetailInfo = "";
	/** 采购单明细最后修改时间*/
	private String dtOrderDetailInfo =  "";
	
	private String  qaControlFlag;//是否免检 add by fyyang 090511

	private String invoiceNo;//add by fyyang 091109 发票号
	
	private String sbMemo;// 需求备注
	private String sbDeptName;// 申报部门

	public String getSbMemo() {
		return sbMemo;
	}
	public void setSbMemo(String sbMemo) {
		this.sbMemo = sbMemo;
	}
	public String getSbDeptName() {
		return sbDeptName;
	}
	public void setSbDeptName(String sbDeptName) {
		this.sbDeptName = sbDeptName;
	}
	/**
	 * @return the arrivalID
	 */
	public Long getArrivalID() {
		return arrivalID;
	}
	/**
	 * @param arrivalID the arrivalID to set
	 */
	public void setArrivalID(Long arrivalID) {
		this.arrivalID = arrivalID;
	}
	/**
	 * @return the arrivalDID
	 */
	public Long getArrivalDID() {
		return arrivalDID;
	}
	/**
	 * @param arrivalDID the arrivalDID to set
	 */
	public void setArrivalDID(Long arrivalDID) {
		this.arrivalDID = arrivalDID;
	}
	/**
	 * @return the purNo
	 */
	public String getPurNo() {
		return purNo;
	}
	/**
	 * @param purNo the purNo to set
	 */
	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}
	/**
	 * @return the supplier
	 */
	public String getSupplier() {
		return supplier;
	}
	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	/**
	 * @return the contract
	 */
	public String getContract() {
		return contract;
	}
	/**
	 * @param contract the contract to set
	 */
	public void setContract(String contract) {
		this.contract = contract;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return the mifNo
	 */
	public String getMifNo() {
		return mifNo;
	}
	/**
	 * @param mifNo the mifNo to set
	 */
	public void setMifNo(String mifNo) {
		this.mifNo = mifNo;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the operateBy
	 */
	public String getOperateBy() {
		return operateBy;
	}
	/**
	 * @param operateBy the operateBy to set
	 */
	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
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
	 * @return the materialID
	 */
	public Long getMaterialID() {
		return materialID;
	}
	/**
	 * @param materialID the materialID to set
	 */
	public void setMaterialID(Long materialID) {
		this.materialID = materialID;
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
	public Long getPurUm() {
		return purUm;
	}
	/**
	 * @param purUm the purUm to set
	 */
	public void setPurUm(Long purUm) {
		this.purUm = purUm;
	}
	/**
	 * @return the purQty
	 */
	public Double getPurQty() {
		return purQty;
	}
	/**
	 * @param purQty the purQty to set
	 */
	public void setPurQty(Double purQty) {
		this.purQty = purQty;
	}
	/**
	 * @return the rcvQty
	 */
	public Double getRcvQty() {
		return rcvQty;
	}
	/**
	 * @param rcvQty the rcvQty to set
	 */
	public void setRcvQty(Double rcvQty) {
		this.rcvQty = rcvQty;
	}
	/**
	 * @return the insQty
	 */
	public Double getInsQty() {
		return insQty;
	}
	/**
	 * @param insQty the insQty to set
	 */
	public void setInsQty(Double insQty) {
		this.insQty = insQty;
	}
	/**
	 * @return the theQty
	 */
	public Double getTheQty() {
		return theQty;
	}
	/**
	 * @param theQty the theQty to set
	 */
	public void setTheQty(Double theQty) {
		this.theQty = theQty;
	}
	/**
	 * @return the lotCode
	 */
	public String getLotCode() {
		return lotCode;
	}
	/**
	 * @param lotCode the lotCode to set
	 */
	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}
	/**
	 * @return the arrivalId
	 */
	public Long getArrivalId() {
		return arrivalId;
	}
	/**
	 * @param arrivalId the arrivalId to set
	 */
	public void setArrivalId(Long arrivalId) {
		this.arrivalId = arrivalId;
	}
	/**
	 * @return the detailMemo
	 */
	public String getDetailMemo() {
		return detailMemo;
	}
	/**
	 * @param detailMemo the detailMemo to set
	 */
	public void setDetailMemo(String detailMemo) {
		this.detailMemo = detailMemo;
	}
	/**
	 * @return the supplierId
	 */
	public Long getSupplierId() {
		return supplierId;
	}
	/**
	 * @param supplierId the supplierId to set
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the dtArrivalInfo
	 */
	public String getDtArrivalInfo() {
		return dtArrivalInfo;
	}
	/**
	 * @param dtArrivalInfo the dtArrivalInfo to set
	 */
	public void setDtArrivalInfo(String dtArrivalInfo) {
		this.dtArrivalInfo = dtArrivalInfo;
	}
	/**
	 * @return the dtArrivalDetailInfo
	 */
	public String getDtArrivalDetailInfo() {
		return dtArrivalDetailInfo;
	}
	/**
	 * @param dtArrivalDetailInfo the dtArrivalDetailInfo to set
	 */
	public void setDtArrivalDetailInfo(String dtArrivalDetailInfo) {
		this.dtArrivalDetailInfo = dtArrivalDetailInfo;
	}
	/**
	 * @return the dtOrderDetailInfo
	 */
	public String getDtOrderDetailInfo() {
		return dtOrderDetailInfo;
	}
	/**
	 * @param dtOrderDetailInfo the dtOrderDetailInfo to set
	 */
	public void setDtOrderDetailInfo(String dtOrderDetailInfo) {
		this.dtOrderDetailInfo = dtOrderDetailInfo;
	}
	public String getQaControlFlag() {
		return qaControlFlag;
	}
	public void setQaControlFlag(String qaControlFlag) {
		this.qaControlFlag = qaControlFlag;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}


	

}
