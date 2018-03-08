package power.ejb.run.securityproduction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * SpJSafetrainingAttend entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="SP_J_SAFETRAINING_ATTEND"
    ,schema="POWER"
)

public class SpJSafetrainingAttend  implements java.io.Serializable {


    // Fields    

     private Long attendId;
     private Long trainingId;
     private String attendCode;
     private String enterpriseCode;


    // Constructors

    /** default constructor */
    public SpJSafetrainingAttend() {
    }

	/** minimal constructor */
    public SpJSafetrainingAttend(Long attendId) {
        this.attendId = attendId;
    }
    
    /** full constructor */
    public SpJSafetrainingAttend(Long attendId, Long trainingId, String attendCode, String enterpriseCode) {
        this.attendId = attendId;
        this.trainingId = trainingId;
        this.attendCode = attendCode;
        this.enterpriseCode = enterpriseCode;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="ATTEND_ID", unique=true, nullable=false, precision=10, scale=0)

    public Long getAttendId() {
        return this.attendId;
    }
    
    public void setAttendId(Long attendId) {
        this.attendId = attendId;
    }
    
    @Column(name="TRAINING_ID", precision=10, scale=0)

    public Long getTrainingId() {
        return this.trainingId;
    }
    
    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }
    
    @Column(name="ATTEND_CODE", length=16)

    public String getAttendCode() {
        return this.attendCode;
    }
    
    public void setAttendCode(String attendCode) {
        this.attendCode = attendCode;
    }
    
    @Column(name="ENTERPRISE_CODE", length=20)

    public String getEnterpriseCode() {
        return this.enterpriseCode;
    }
    
    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }
   








}