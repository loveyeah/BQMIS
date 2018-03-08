/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.bean.bqmis;

import java.util.List;

public class SupplierBaseBean {

    // 流水号
    private Long clientId;
    // 合作伙伴性质ID
    private Long characterId;
    // 合作伙伴性质名称
    private String characterName;
    // 合作伙伴类型ID
    private Long clienttypeId;
    // 合作伙伴类型名称
    private String clienttypeName;
    // 档案编号
    private String clientCode;
    // 合作伙伴名称
    private String clientName;
    // 公司电话
    private String telephone;
    // 办公地址
    private String address;
    // 邮编
    private String postmaster;
    // 开户银行
    private String bankaccount;
    // 帐号
    private String account;
    // 供应商概况
    private String clientsurvey;
    // 公司负责人
    private String burdenman;
    // 法人代表
    private String corporation;
    // 纳税人资格
    private String nsrzg;
    // 公司传真
    private String gscz;
    // 商务传真
    private String swcz;
    // 联系人1
    private String lxr1;
    // 电话1
    private String telephone1;
    // 手机1
    private String mobile1;
    // 联系人2
    private String lxr2;
    // 电话2
    private String telephone2;
    // 手机2
    private String mobile2;
    // 注册资金
    private String zczj;
    // 主要经营产品
    private String zycp;
    // 电子信箱1
    private String mail1;
    // 电子信箱2
    private String mail2;
    // 公司网址
    private String intetadd;
    // 资质列表
    private List<SupplierDetailBean> supplierDetailList;
    // 打印日期
    private String nowDate = "";
    // 行数修正值
    private Integer rowChange;

	/**
	 * @return the rowChange
	 */
	public Integer getRowChange() {
		return rowChange;
	}

	/**
	 * @param rowChange the rowChange to set
	 */
	public void setRowChange(Integer rowChange) {
		this.rowChange = rowChange;
	}

	/**
     * @return the clientId
     */
    public Long getClientId() {
        return clientId;
    }

    /**
     * @param clientId
     *            the clientId to set
     */
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the characterId
     */
    public Long getCharacterId() {
        return characterId;
    }

    /**
     * @param characterId
     *            the characterId to set
     */
    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    /**
     * @return the characterName
     */
    public String getCharacterName() {
        return characterName;
    }

    /**
     * @param characterName
     *            the characterName to set
     */
    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    /**
     * @return the clienttypeId
     */
    public Long getClienttypeId() {
        return clienttypeId;
    }

    /**
     * @param clienttypeId
     *            the clienttypeId to set
     */
    public void setClienttypeId(Long clienttypeId) {
        this.clienttypeId = clienttypeId;
    }

    /**
     * @return the clienttypeName
     */
    public String getClienttypeName() {
        return clienttypeName;
    }

    /**
     * @param clienttypeName
     *            the clienttypeName to set
     */
    public void setClienttypeName(String clienttypeName) {
        this.clienttypeName = clienttypeName;
    }

    /**
     * @return the clientCode
     */
    public String getClientCode() {
        return clientCode;
    }

    /**
     * @param clientCode
     *            the clientCode to set
     */
    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName
     *            the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone
     *            the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the postmaster
     */
    public String getPostmaster() {
        return postmaster;
    }

    /**
     * @param postmaster
     *            the postmaster to set
     */
    public void setPostmaster(String postmaster) {
        this.postmaster = postmaster;
    }

    /**
     * @return the bankaccount
     */
    public String getBankaccount() {
        return bankaccount;
    }

    /**
     * @param bankaccount
     *            the bankaccount to set
     */
    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account
     *            the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the clientsurvey
     */
    public String getClientsurvey() {
        return clientsurvey;
    }

    /**
     * @param clientsurvey
     *            the clientsurvey to set
     */
    public void setClientsurvey(String clientsurvey) {
        this.clientsurvey = clientsurvey;
    }

    /**
     * @return the burdenman
     */
    public String getBurdenman() {
        return burdenman;
    }

    /**
     * @param burdenman
     *            the burdenman to set
     */
    public void setBurdenman(String burdenman) {
        this.burdenman = burdenman;
    }

    /**
     * @return the corporation
     */
    public String getCorporation() {
        return corporation;
    }

    /**
     * @param corporation
     *            the corporation to set
     */
    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    /**
     * @return the nsrzg
     */
    public String getNsrzg() {
        return nsrzg;
    }

    /**
     * @param nsrzg
     *            the nsrzg to set
     */
    public void setNsrzg(String nsrzg) {
        this.nsrzg = nsrzg;
    }

    /**
     * @return the gscz
     */
    public String getGscz() {
        return gscz;
    }

    /**
     * @param gscz
     *            the gscz to set
     */
    public void setGscz(String gscz) {
        this.gscz = gscz;
    }

    /**
     * @return the swcz
     */
    public String getSwcz() {
        return swcz;
    }

    /**
     * @param swcz
     *            the swcz to set
     */
    public void setSwcz(String swcz) {
        this.swcz = swcz;
    }

    /**
     * @return the lxr1
     */
    public String getLxr1() {
        return lxr1;
    }

    /**
     * @param lxr1
     *            the lxr1 to set
     */
    public void setLxr1(String lxr1) {
        this.lxr1 = lxr1;
    }

    /**
     * @return the telephone1
     */
    public String getTelephone1() {
        return telephone1;
    }

    /**
     * @param telephone1
     *            the telephone1 to set
     */
    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    /**
     * @return the mobile1
     */
    public String getMobile1() {
        return mobile1;
    }

    /**
     * @param mobile1
     *            the mobile1 to set
     */
    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    /**
     * @return the lxr2
     */
    public String getLxr2() {
        return lxr2;
    }

    /**
     * @param lxr2
     *            the lxr2 to set
     */
    public void setLxr2(String lxr2) {
        this.lxr2 = lxr2;
    }

    /**
     * @return the telephone2
     */
    public String getTelephone2() {
        return telephone2;
    }

    /**
     * @param telephone2
     *            the telephone2 to set
     */
    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    /**
     * @return the mobile2
     */
    public String getMobile2() {
        return mobile2;
    }

    /**
     * @param mobile2
     *            the mobile2 to set
     */
    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    /**
     * @return the zczj
     */
    public String getZczj() {
        return zczj;
    }

    /**
     * @param zczj
     *            the zczj to set
     */
    public void setZczj(String zczj) {
        this.zczj = zczj;
    }

    /**
     * @return the zycp
     */
    public String getZycp() {
        return zycp;
    }

    /**
     * @param zycp
     *            the zycp to set
     */
    public void setZycp(String zycp) {
        this.zycp = zycp;
    }

    /**
     * @return the mail1
     */
    public String getMail1() {
        return mail1;
    }

    /**
     * @param mail1
     *            the mail1 to set
     */
    public void setMail1(String mail1) {
        this.mail1 = mail1;
    }

    /**
     * @return the mail2
     */
    public String getMail2() {
        return mail2;
    }

    /**
     * @param mail2
     *            the mail2 to set
     */
    public void setMail2(String mail2) {
        this.mail2 = mail2;
    }

    /**
     * @return the intetadd
     */
    public String getIntetadd() {
        return intetadd;
    }

    /**
     * @param intetadd
     *            the intetadd to set
     */
    public void setIntetadd(String intetadd) {
        this.intetadd = intetadd;
    }

    /**
     * @return the supplierDetailList
     */
    public List<SupplierDetailBean> getSupplierDetailList() {
        return supplierDetailList;
    }

    /**
     * @param supplierDetailList
     *            the supplierDetailList to set
     */
    public void setSupplierDetailList(
            List<SupplierDetailBean> supplierDetailList) {
        this.supplierDetailList = supplierDetailList;
    }

    /**
     * @return the nowDate
     */
    public String getNowDate() {
        return nowDate;
    }

    /**
     * @param nowDate
     *            the nowDate to set
     */
    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }
}
