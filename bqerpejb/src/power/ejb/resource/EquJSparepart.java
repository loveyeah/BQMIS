package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquJSparepart entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_SPAREPART")
public class EquJSparepart implements java.io.Serializable {

    // Fields

    private Long equSparepartId;
    private String equclassCode;
    private String attributeCode;
    private Long materialId;
    private Double qty;
    private Date lastModifiedDate;
    private String lastModifiedBy;
    private String enterpriseCode;
    private String isUse;

    // Constructors

    /** default constructor */
    public EquJSparepart() {
    }

    /** minimal constructor */
    public EquJSparepart(Long equSparepartId, Date lastModifiedDate, String lastModifiedBy, String enterpriseCode,
            String isUse) {
        this.equSparepartId = equSparepartId;
        this.lastModifiedDate = lastModifiedDate;
        this.lastModifiedBy = lastModifiedBy;
        this.enterpriseCode = enterpriseCode;
        this.isUse = isUse;
    }

    /** full constructor */
    public EquJSparepart(Long equSparepartId, String equclassCode, String attributeCode, Long materialId, Double qty,
            Date lastModifiedDate, String lastModifiedBy, String enterpriseCode, String isUse) {
        this.equSparepartId = equSparepartId;
        this.equclassCode = equclassCode;
        this.attributeCode = attributeCode;
        this.materialId = materialId;
        this.qty = qty;
        this.lastModifiedDate = lastModifiedDate;
        this.lastModifiedBy = lastModifiedBy;
        this.enterpriseCode = enterpriseCode;
        this.isUse = isUse;
    }

    // Property accessors
    @Id
    @Column(name = "EQU_SPAREPART_ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getEquSparepartId() {
        return this.equSparepartId;
    }

    public void setEquSparepartId(Long equSparepartId) {
        this.equSparepartId = equSparepartId;
    }

    @Column(name = "EQUCLASS_CODE", length = 3)
    public String getEquclassCode() {
        return this.equclassCode;
    }

    public void setEquclassCode(String equclassCode) {
        this.equclassCode = equclassCode;
    }

    @Column(name = "ATTRIBUTE_CODE", length = 30)
    public String getAttributeCode() {
        return this.attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    @Column(name = "MATERIAL_ID", precision = 10, scale = 0)
    public Long getMaterialId() {
        return this.materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    @Column(name = "QTY", precision = 15, scale = 4)
    public Double getQty() {
        return this.qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
    public Date getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
    public String getEnterpriseCode() {
        return this.enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    @Column(name = "IS_USE", nullable = false, length = 1)
    public String getIsUse() {
        return this.isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

}