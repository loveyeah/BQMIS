/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.io.Serializable;
@SuppressWarnings("serial")

/**
 * 劳务派遣登记
 * 
 * @author zhaomingjian
 * @version 1.0
 */
public class LaborBorrowRegisterQuery implements Serializable {
	
	/**劳务派遣合同ID**/
	private String  borrowContractId;
	/**劳动合同编号**/
	private String  wrokContractNo;
	/**签字日期**/
	private String  signatureDate;
	/**协作单位ID**/
	private String  cooperateUnitId;
	/**协作单位**/
	private String  cooperateUnit;
	/**开始日期**/
	private String  startDate;
	/**结束日期**/
	private String  endDate;
	/**劳务内容**/
	private String  contractContent;
	/**单据状态**/
	private String  dcmStatus;
	/**备注**/
	private String  note;
	/**上次修改人**/
	private String  lastModifiedBy;
	/**上次修改日期**/
	private String  lastModifiedDate;
	/**flag**/
	private String flag;
	/**派遣合同Id**/
	private String workContractId;
	
	/** 调动类型* */
	private String transferType;
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**
	 * 
	 * @return 劳务派遣合同ID
	 */
	public String getBorrowContractId() {
		return borrowContractId;
	}
    public void setBorrowContractId(String borrowContractId) {
		this.borrowContractId = borrowContractId;
	}
	/**
	 * 
	 * @return 劳动合同编号
	 */
	public String getWrokContractNo() {
		return wrokContractNo;
	}
	public void setWrokContractNo(String wrokContractNo) {
		this.wrokContractNo = wrokContractNo;
	}
	/**
	 * 
	 * @return 签字日期
	 */
	public String getSignatureDate() {
		return signatureDate;
	}
	public void setSignatureDate(String signatureDate) {
		this.signatureDate = signatureDate;
	}
	/**
	 * 
	 * @return 协作单位ID
	 */
	public String getCooperateUnitId() {
		return cooperateUnitId;
	}
	public void setCooperateUnitId(String cooperateUnitID) {
		this.cooperateUnitId = cooperateUnitID;
	}
	/**
	 * 
	 * @return 协作单位
	 */
	public String getCooperateUnit() {
		return cooperateUnit;
	}
	public void setCooperateUnit(String cooperateUnit) {
		this.cooperateUnit = cooperateUnit;
	}
	/**
	 * 
	 * @return 开始日期
	 */
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * 
	 * @return 结束日期
	 */
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * 
	 * @return 劳务内容
	 */
	public String getContractContent() {
		return contractContent;
	}
	public void setContractContent(String contractContent) {
		this.contractContent = contractContent;
	}
	/**
	 * 
	 * @return 单据状态
	 */
	public String getDcmStatus() {
		return dcmStatus;
	}
	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}
	/**
	 * 
	 * @return 备注
	 */
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * 
	 * @return 上次修改人
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	/**
	 * 
	 * @return 上次修改日期
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	/**
	 * 
	 * @return
	 */
	public String getWorkContractId() {
		return workContractId;
	}
	public void setWorkContractId(String workContractId) {
		this.workContractId = workContractId;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	

}
