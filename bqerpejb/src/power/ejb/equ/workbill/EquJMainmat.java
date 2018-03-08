package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * EquJMainmat entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name="EQU_J_MAINMAT"
    ,schema="POWER"
)

public class EquJMainmat  implements java.io.Serializable {


    // Fields    

     private Long id;
     private String woCode;
     private String woticketCode;
     private String operationStep;
     private String planMaterialCode;
     private String planLocationId;
     private Double planItemQty;
     private String planUnit;
     private Double planMaterialPrice;
     private String planDirectReq;
     private String planVendor;
     private String planMatPicid;
     private String factMaterialCode;
     private String factLocationId;
     private Double factItemQty;
     private String factUnit;
     private Double factMaterialPrice;
     private String factDirectReq;
     private String factVendor;
     private String factMatPicid;
     private Long orderby;
     private String enterprisecode;
     private String ifUse;


    // Constructors

    /** default constructor */
    public EquJMainmat() {
    }

	/** minimal constructor */
    public EquJMainmat(Long id, String woCode, String operationStep, String enterprisecode) {
        this.id = id;
        this.woCode = woCode;
        this.operationStep = operationStep;
        this.enterprisecode = enterprisecode;
    }
    
    /** full constructor */
    public EquJMainmat(Long id, String woCode, String woticketCode, String operationStep, String planMaterialCode, String planLocationId, Double planItemQty, String planUnit, Double planMaterialPrice, String planDirectReq, String planVendor, String planMatPicid, String factMaterialCode, String factLocationId, Double factItemQty, String factUnit, Double factMaterialPrice, String factDirectReq, String factVendor, String factMatPicid, Long orderby, String enterprisecode, String ifUse) {
        this.id = id;
        this.woCode = woCode;
        this.woticketCode = woticketCode;
        this.operationStep = operationStep;
        this.planMaterialCode = planMaterialCode;
        this.planLocationId = planLocationId;
        this.planItemQty = planItemQty;
        this.planUnit = planUnit;
        this.planMaterialPrice = planMaterialPrice;
        this.planDirectReq = planDirectReq;
        this.planVendor = planVendor;
        this.planMatPicid = planMatPicid;
        this.factMaterialCode = factMaterialCode;
        this.factLocationId = factLocationId;
        this.factItemQty = factItemQty;
        this.factUnit = factUnit;
        this.factMaterialPrice = factMaterialPrice;
        this.factDirectReq = factDirectReq;
        this.factVendor = factVendor;
        this.factMatPicid = factMatPicid;
        this.orderby = orderby;
        this.enterprisecode = enterprisecode;
        this.ifUse = ifUse;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="ID", unique=true, nullable=false, precision=10, scale=0)

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(name="WO_CODE", nullable=false, length=20)

    public String getWoCode() {
        return this.woCode;
    }
    
    public void setWoCode(String woCode) {
        this.woCode = woCode;
    }
    
    @Column(name="WOTICKET_CODE", length=30)

    public String getWoticketCode() {
        return this.woticketCode;
    }
    
    public void setWoticketCode(String woticketCode) {
        this.woticketCode = woticketCode;
    }
    
    @Column(name="OPERATION_STEP", nullable=false, length=20)

    public String getOperationStep() {
        return this.operationStep;
    }
    
    public void setOperationStep(String operationStep) {
        this.operationStep = operationStep;
    }
    
    @Column(name="PLAN_MATERIAL_CODE", length=20)

    public String getPlanMaterialCode() {
        return this.planMaterialCode;
    }
    
    public void setPlanMaterialCode(String planMaterialCode) {
        this.planMaterialCode = planMaterialCode;
    }
    
    @Column(name="PLAN_LOCATION_ID", length=10)

    public String getPlanLocationId() {
        return this.planLocationId;
    }
    
    public void setPlanLocationId(String planLocationId) {
        this.planLocationId = planLocationId;
    }
    
    @Column(name="PLAN_ITEM_QTY", precision=15, scale=4)

    public Double getPlanItemQty() {
        return this.planItemQty;
    }
    
    public void setPlanItemQty(Double planItemQty) {
        this.planItemQty = planItemQty;
    }
    
    @Column(name="PLAN_UNIT", length=15)

    public String getPlanUnit() {
        return this.planUnit;
    }
    
    public void setPlanUnit(String planUnit) {
        this.planUnit = planUnit;
    }
    
    @Column(name="PLAN_MATERIAL_PRICE", precision=18, scale=5)

    public Double getPlanMaterialPrice() {
        return this.planMaterialPrice;
    }
    
    public void setPlanMaterialPrice(Double planMaterialPrice) {
        this.planMaterialPrice = planMaterialPrice;
    }
    
    @Column(name="PLAN_DIRECT_REQ", length=1)

    public String getPlanDirectReq() {
        return this.planDirectReq;
    }
    
    public void setPlanDirectReq(String planDirectReq) {
        this.planDirectReq = planDirectReq;
    }
    
    @Column(name="PLAN_VENDOR", length=10)

    public String getPlanVendor() {
        return this.planVendor;
    }
    
    public void setPlanVendor(String planVendor) {
        this.planVendor = planVendor;
    }
    
    @Column(name="PLAN_MAT_PICID", length=30)

    public String getPlanMatPicid() {
        return this.planMatPicid;
    }
    
    public void setPlanMatPicid(String planMatPicid) {
        this.planMatPicid = planMatPicid;
    }
    
    @Column(name="FACT_MATERIAL_CODE", length=20)

    public String getFactMaterialCode() {
        return this.factMaterialCode;
    }
    
    public void setFactMaterialCode(String factMaterialCode) {
        this.factMaterialCode = factMaterialCode;
    }
    
    @Column(name="FACT_LOCATION_ID", length=10)

    public String getFactLocationId() {
        return this.factLocationId;
    }
    
    public void setFactLocationId(String factLocationId) {
        this.factLocationId = factLocationId;
    }
    
    @Column(name="FACT_ITEM_QTY", precision=15, scale=4)

    public Double getFactItemQty() {
        return this.factItemQty;
    }
    
    public void setFactItemQty(Double factItemQty) {
        this.factItemQty = factItemQty;
    }
    
    @Column(name="FACT_UNIT", length=15)

    public String getFactUnit() {
        return this.factUnit;
    }
    
    public void setFactUnit(String factUnit) {
        this.factUnit = factUnit;
    }
    
    @Column(name="FACT_MATERIAL_PRICE", precision=18, scale=5)

    public Double getFactMaterialPrice() {
        return this.factMaterialPrice;
    }
    
    public void setFactMaterialPrice(Double factMaterialPrice) {
        this.factMaterialPrice = factMaterialPrice;
    }
    
    @Column(name="FACT_DIRECT_REQ", length=1)

    public String getFactDirectReq() {
        return this.factDirectReq;
    }
    
    public void setFactDirectReq(String factDirectReq) {
        this.factDirectReq = factDirectReq;
    }
    
    @Column(name="FACT_VENDOR", length=10)

    public String getFactVendor() {
        return this.factVendor;
    }
    
    public void setFactVendor(String factVendor) {
        this.factVendor = factVendor;
    }
    
    @Column(name="FACT_MAT_PICID", length=30)

    public String getFactMatPicid() {
        return this.factMatPicid;
    }
    
    public void setFactMatPicid(String factMatPicid) {
        this.factMatPicid = factMatPicid;
    }
    
    @Column(name="ORDERBY", precision=10, scale=0)

    public Long getOrderby() {
        return this.orderby;
    }
    
    public void setOrderby(Long orderby) {
        this.orderby = orderby;
    }
    
    @Column(name="ENTERPRISECODE", nullable=false, length=20)

    public String getEnterprisecode() {
        return this.enterprisecode;
    }
    
    public void setEnterprisecode(String enterprisecode) {
        this.enterprisecode = enterprisecode;
    }
    
    @Column(name="IF_USE", length=1)

    public String getIfUse() {
        return this.ifUse;
    }
    
    public void setIfUse(String ifUse) {
        this.ifUse = ifUse;
    }
   








}