/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.bean.bqmis;
/**
 * 供应商资料数据
 * @author wangjunjie 
 */
public class SupplierDetailBean {

    
    /** 合作伙伴资质登记表 */
    /** 行号 */
    private String strNo="";
    /** 资料齐备否 */
    private String ifQb="";
    /** 资质证书名称 */
    private String qualificationName="";
    /** 证书有效日期 */
    private String effectiveFromDate="";
    /** 证书失效日期 */
    private String effectiveToDate="";
    /** 行数计数 */
    private Integer cntRow;
    /**
     * @return the strNo
     */
    public String getStrNo() {
        return strNo;
    }
    /**
     * @param strNo the strNo to set
     */
    public void setStrNo(String strNo) {
        this.strNo = strNo;
    }
    /**
     * @return the ifQb
     */
    public String getIfQb() {
        return ifQb;
    }
    /**
     * @param ifQb the ifQb to set
     */
    public void setIfQb(String ifQb) {
        this.ifQb = ifQb;
    }
    /**
     * @return the qualificationName
     */
    public String getQualificationName() {
        return qualificationName;
    }
    /**
     * @param qualificationName the qualificationName to set
     */
    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }
    /**
     * @return the effectiveFromDate
     */
    public String getEffectiveFromDate() {
        return effectiveFromDate;
    }
    /**
     * @param effectiveFromDate the effectiveFromDate to set
     */
    public void setEffectiveFromDate(String effectiveFromDate) {
        this.effectiveFromDate = effectiveFromDate;
    }
    /**
     * @return the effectiveToDate
     */
    public String getEffectiveToDate() {
        return effectiveToDate;
    }
    /**
     * @param effectiveToDate the effectiveToDate to set
     */
    public void setEffectiveToDate(String effectiveToDate) {
        this.effectiveToDate = effectiveToDate;
    }
    /**
     * @return the cntRow
     */
    public Integer getCntRow() {
        return cntRow;
    }
    /**
     * @param cntRow the cntRow to set
     */
    public void setCntRow(Integer cntRow) {
        this.cntRow = cntRow;
    }

    
}
