package power.ejb.resource;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 仓库主文件 entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_C_WAREHOUSE")
public class InvCWarehouse implements java.io.Serializable {

    // Fields

    private Long whsId;
    private String whsNo;
    private String whsName;
    private String whsDesc;
    private String contactMan;
    private String address;
    private String country;
    private String province;
    private String city;
    private String postalCode;
    private String tel;
    private String fax;
    private String email;
    private String isAllocatableWhs;
    private String isInspect;
    private String isCost;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private String enterpriseCode;
    private String isUse;

    // Constructors

    /** default constructor */
    public InvCWarehouse() {
    }

    /** minimal constructor */
    public InvCWarehouse(Long whsId, String whsNo, String whsName) {
        this.whsId = whsId;
        this.whsNo = whsNo;
        this.whsName = whsName;
    }

    /** full constructor */
    public InvCWarehouse(Long whsId, String whsNo, String whsName,
            String whsDesc, String contactMan, String address, String country,
            String province, String city, String postalCode, String tel,
            String fax, String email, String isAllocatableWhs,
            String isInspect, String isCost, String lastModifiedBy,
            Date lastModifiedDate, String enterpriseCode, String isUse) {
        this.whsId = whsId;
        this.whsNo = whsNo;
        this.whsName = whsName;
        this.whsDesc = whsDesc;
        this.contactMan = contactMan;
        this.address = address;
        this.country = country;
        this.province = province;
        this.city = city;
        this.postalCode = postalCode;
        this.tel = tel;
        this.fax = fax;
        this.email = email;
        this.isAllocatableWhs = isAllocatableWhs;
        this.isInspect = isInspect;
        this.isCost = isCost;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.enterpriseCode = enterpriseCode;
        this.isUse = isUse;
    }

    // Property accessors
    @Id
    @Column(name = "WHS_ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getWhsId() {
        return this.whsId;
    }

    public void setWhsId(Long whsId) {
        this.whsId = whsId;
    }

    @Column(name = "WHS_NO", nullable = false, length = 10)
    public String getWhsNo() {
        return this.whsNo;
    }

    public void setWhsNo(String whsNo) {
        this.whsNo = whsNo;
    }

    @Column(name = "WHS_NAME", nullable = false, length = 100)
    public String getWhsName() {
        return this.whsName;
    }

    public void setWhsName(String whsName) {
        this.whsName = whsName;
    }

    @Column(name = "WHS_DESC", length = 200)
    public String getWhsDesc() {
        return this.whsDesc;
    }

    public void setWhsDesc(String whsDesc) {
        this.whsDesc = whsDesc;
    }

    @Column(name = "CONTACT_MAN", length = 10)
    public String getContactMan() {
        return this.contactMan;
    }

    public void setContactMan(String contactMan) {
        this.contactMan = contactMan;
    }

    @Column(name = "ADDRESS", length = 100)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "COUNTRY", length = 50)
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "PROVINCE", length = 50)
    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Column(name = "CITY", length = 50)
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "POSTAL_CODE", length = 10)
    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Column(name = "TEL", length = 18)
    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Column(name = "FAX", length = 18)
    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column(name = "EMAIL", length = 100)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "IS_ALLOCATABLE_WHS", length = 1)
    public String getIsAllocatableWhs() {
        return this.isAllocatableWhs;
    }

    public void setIsAllocatableWhs(String isAllocatableWhs) {
        this.isAllocatableWhs = isAllocatableWhs;
    }

    @Column(name = "IS_INSPECT", length = 1)
    public String getIsInspect() {
        return this.isInspect;
    }

    public void setIsInspect(String isInspect) {
        this.isInspect = isInspect;
    }

    @Column(name = "IS_COST", length = 1)
    public String getIsCost() {
        return this.isCost;
    }

    public void setIsCost(String isCost) {
        this.isCost = isCost;
    }

    @Column(name = "LAST_MODIFIED_BY", length = 16)
    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE", length = 7)
    public Date getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Column(name = "ENTERPRISE_CODE", length = 10)
    public String getEnterpriseCode() {
        return this.enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    @Column(name = "IS_USE", length = 1)
    public String getIsUse() {
        return this.isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

}