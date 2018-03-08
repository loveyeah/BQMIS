/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.util.List;

/**
 * 到货登记表
 * @author zhujie 
 */
public class ReceiveGoodsBean {

	/** 采购单号 */
	private String purNo = "";
	/** 合同号 */
	private String contractNo = "";
	/** 供应商 */
	private String clientName = "";
	/** 备注 */
	private String meno = "";
	/** 制单人 */
    private String createMan = "";
    /** 当前日期 */
    private String nowDate = "";
    /** 事务作用码 */
    private String businessCode = "";
    /** 事务作用名称 */
    private String businessName = "";    
    /** 到货登记表明细 */
    private List<ReceiveGoodsListBean> receiveGoodsList;
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
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}
	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}
	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	/**
	 * @return the meno
	 */
	public String getMeno() {
		return meno;
	}
	/**
	 * @param meno the meno to set
	 */
	public void setMeno(String meno) {
		this.meno = meno;
	}
	/**
	 * @return the createMan
	 */
	public String getCreateMan() {
		return createMan;
	}
	/**
	 * @param createMan the createMan to set
	 */
	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}
	/**
	 * @return the nowDate
	 */
	public String getNowDate() {
		return nowDate;
	}
	/**
	 * @param nowDate the nowDate to set
	 */
	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}
	/**
	 * @return the receiveGoodsList
	 */
	public List<ReceiveGoodsListBean> getReceiveGoodsList() {
		return receiveGoodsList;
	}
	/**
	 * @param receiveGoodsList the receiveGoodsList to set
	 */
	public void setReceiveGoodsList(List<ReceiveGoodsListBean> receiveGoodsList) {
		this.receiveGoodsList = receiveGoodsList;
	}
	/**
	 * @return the businessCode
	 */
	public String getBusinessCode() {
		return businessCode;
	}
	/**
	 * @param businessCode the businessCode to set
	 */
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	/**
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}
	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
}
