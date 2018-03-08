package power.ejb.productiontec.dependabilityAnalysis.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * PtJBlowerParameter entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="PT_J_BLOWER_PARAMETER")

public class PtJBlowerParameter  implements java.io.Serializable {


    // Fields    

     private Long blowerId;
     private Long auxiliaryId;
     private Double ratingWind;
     private Double windPressure;
     private Double ratingSpeed;
     private Double ratingPower;
     private Double mediumWeight;
     private Double mediumTemperature;
     private Double windDiameter;
     private String adjustType;
     private String oldCode;
     private String memo;
     private String electromotorNo;
     private Double electRatingPower;
     private Double electRatingSpeed;
     private Double ratingVoltage;
     private Double ratingElectricity;
     private String shiftType;
     private String factoryCode;
     private String factoryName;
     private String isUse;
     private String enterpriseCode;
     private String parameterType;
     private String connType;


    // Constructors

    /** default constructor */
    public PtJBlowerParameter() {
    }

	/** minimal constructor */
    public PtJBlowerParameter(Long blowerId) {
        this.blowerId = blowerId;
    }
    
    /** full constructor */
    public PtJBlowerParameter(Long blowerId, Long auxiliaryId, Double ratingWind, Double windPressure, Double ratingSpeed, Double ratingPower, Double mediumWeight, Double mediumTemperature, Double windDiameter, String adjustType, String oldCode, String memo, String electromotorNo, Double electRatingPower, Double electRatingSpeed, Double ratingVoltage, Double ratingElectricity, String shiftType, String factoryCode, String factoryName, String isUse, String enterpriseCode, String parameterType, String connType) {
        this.blowerId = blowerId;
        this.auxiliaryId = auxiliaryId;
        this.ratingWind = ratingWind;
        this.windPressure = windPressure;
        this.ratingSpeed = ratingSpeed;
        this.ratingPower = ratingPower;
        this.mediumWeight = mediumWeight;
        this.mediumTemperature = mediumTemperature;
        this.windDiameter = windDiameter;
        this.adjustType = adjustType;
        this.oldCode = oldCode;
        this.memo = memo;
        this.electromotorNo = electromotorNo;
        this.electRatingPower = electRatingPower;
        this.electRatingSpeed = electRatingSpeed;
        this.ratingVoltage = ratingVoltage;
        this.ratingElectricity = ratingElectricity;
        this.shiftType = shiftType;
        this.factoryCode = factoryCode;
        this.factoryName = factoryName;
        this.isUse = isUse;
        this.enterpriseCode = enterpriseCode;
        this.parameterType = parameterType;
        this.connType = connType;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="BLOWER_ID", unique=true, nullable=false, precision=10, scale=0)

    public Long getBlowerId() {
        return this.blowerId;
    }
    
    public void setBlowerId(Long blowerId) {
        this.blowerId = blowerId;
    }
    
    @Column(name="AUXILIARY_ID", precision=10, scale=0)

    public Long getAuxiliaryId() {
        return this.auxiliaryId;
    }
    
    public void setAuxiliaryId(Long auxiliaryId) {
        this.auxiliaryId = auxiliaryId;
    }
    
    @Column(name="RATING_WIND", precision=10)

    public Double getRatingWind() {
        return this.ratingWind;
    }
    
    public void setRatingWind(Double ratingWind) {
        this.ratingWind = ratingWind;
    }
    
    @Column(name="WIND_PRESSURE", precision=10)

    public Double getWindPressure() {
        return this.windPressure;
    }
    
    public void setWindPressure(Double windPressure) {
        this.windPressure = windPressure;
    }
    
    @Column(name="RATING_SPEED", precision=10)

    public Double getRatingSpeed() {
        return this.ratingSpeed;
    }
    
    public void setRatingSpeed(Double ratingSpeed) {
        this.ratingSpeed = ratingSpeed;
    }
    
    @Column(name="RATING_POWER", precision=10)

    public Double getRatingPower() {
        return this.ratingPower;
    }
    
    public void setRatingPower(Double ratingPower) {
        this.ratingPower = ratingPower;
    }
    
    @Column(name="MEDIUM_WEIGHT", precision=10)

    public Double getMediumWeight() {
        return this.mediumWeight;
    }
    
    public void setMediumWeight(Double mediumWeight) {
        this.mediumWeight = mediumWeight;
    }
    
    @Column(name="MEDIUM_TEMPERATURE", precision=10)

    public Double getMediumTemperature() {
        return this.mediumTemperature;
    }
    
    public void setMediumTemperature(Double mediumTemperature) {
        this.mediumTemperature = mediumTemperature;
    }
    
    @Column(name="WIND_DIAMETER", precision=10)

    public Double getWindDiameter() {
        return this.windDiameter;
    }
    
    public void setWindDiameter(Double windDiameter) {
        this.windDiameter = windDiameter;
    }
    
    @Column(name="ADJUST_TYPE", length=20)

    public String getAdjustType() {
        return this.adjustType;
    }
    
    public void setAdjustType(String adjustType) {
        this.adjustType = adjustType;
    }
    
    @Column(name="OLD_CODE", length=20)

    public String getOldCode() {
        return this.oldCode;
    }
    
    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }
    
    @Column(name="MEMO", length=200)

    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    @Column(name="ELECTROMOTOR_NO", length=20)

    public String getElectromotorNo() {
        return this.electromotorNo;
    }
    
    public void setElectromotorNo(String electromotorNo) {
        this.electromotorNo = electromotorNo;
    }
    
    @Column(name="ELECT_RATING_POWER", precision=10)

    public Double getElectRatingPower() {
        return this.electRatingPower;
    }
    
    public void setElectRatingPower(Double electRatingPower) {
        this.electRatingPower = electRatingPower;
    }
    
    @Column(name="ELECT_RATING_SPEED", precision=10)

    public Double getElectRatingSpeed() {
        return this.electRatingSpeed;
    }
    
    public void setElectRatingSpeed(Double electRatingSpeed) {
        this.electRatingSpeed = electRatingSpeed;
    }
    
    @Column(name="RATING_VOLTAGE", precision=10)

    public Double getRatingVoltage() {
        return this.ratingVoltage;
    }
    
    public void setRatingVoltage(Double ratingVoltage) {
        this.ratingVoltage = ratingVoltage;
    }
    
    @Column(name="RATING_ELECTRICITY", precision=10)

    public Double getRatingElectricity() {
        return this.ratingElectricity;
    }
    
    public void setRatingElectricity(Double ratingElectricity) {
        this.ratingElectricity = ratingElectricity;
    }
    
    @Column(name="SHIFT_TYPE", length=20)

    public String getShiftType() {
        return this.shiftType;
    }
    
    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }
    
    @Column(name="FACTORY_CODE", length=20)

    public String getFactoryCode() {
        return this.factoryCode;
    }
    
    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }
    
    @Column(name="FACTORY_NAME", length=50)

    public String getFactoryName() {
        return this.factoryName;
    }
    
    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }
    
    @Column(name="IS_USE", length=1)

    public String getIsUse() {
        return this.isUse;
    }
    
    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }
    
    @Column(name="ENTERPRISE_CODE", length=20)

    public String getEnterpriseCode() {
        return this.enterpriseCode;
    }
    
    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }
    
    @Column(name="PARAMETER_TYPE", length=1)

    public String getParameterType() {
        return this.parameterType;
    }
    
    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
    
    @Column(name="CONN_TYPE", length=20)

    public String getConnType() {
        return this.connType;
    }
    
    public void setConnType(String connType) {
        this.connType = connType;
    }
   








}