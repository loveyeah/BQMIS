package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * InvJBook entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="INV_J_BOOK")

public class InvJBook  implements java.io.Serializable {


    // Fields    

     private Long bookId;
     private String bookNo;
     private Date bookDate;
     private String bookWhs;
     private String bookLocation;
     private String bookMaterialClass;
     private String bookMan;
     private String bookStatus;
     private String lastModifiedBy;
     private Date lastModifiedDate;
     private String enterpriseCode;
     private String isUse;


    // Constructors

    /** default constructor */
    public InvJBook() {
    }

	/** minimal constructor */
    public InvJBook(Long bookId, String bookNo, Date bookDate, String bookMan, String bookStatus, String lastModifiedBy, Date lastModifiedDate, String enterpriseCode, String isUse) {
        this.bookId = bookId;
        this.bookNo = bookNo;
        this.bookDate = bookDate;
        this.bookMan = bookMan;
        this.bookStatus = bookStatus;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.enterpriseCode = enterpriseCode;
        this.isUse = isUse;
    }
    
    /** full constructor */
    public InvJBook(Long bookId, String bookNo, Date bookDate, String bookWhs, String bookLocation, String bookMaterialClass, String bookMan, String bookStatus, String lastModifiedBy, Date lastModifiedDate, String enterpriseCode, String isUse) {
        this.bookId = bookId;
        this.bookNo = bookNo;
        this.bookDate = bookDate;
        this.bookWhs = bookWhs;
        this.bookLocation = bookLocation;
        this.bookMaterialClass = bookMaterialClass;
        this.bookMan = bookMan;
        this.bookStatus = bookStatus;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.enterpriseCode = enterpriseCode;
        this.isUse = isUse;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="BOOK_ID", unique=true, nullable=false, precision=10, scale=0)

    public Long getBookId() {
        return this.bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    @Column(name="BOOK_NO", nullable=false, length=30)

    public String getBookNo() {
        return this.bookNo;
    }
    
    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }
@Temporal(TemporalType.DATE)
    @Column(name="BOOK_DATE", nullable=false, length=7)

    public Date getBookDate() {
        return this.bookDate;
    }
    
    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }
    
    @Column(name="BOOK_WHS", length=10)

    public String getBookWhs() {
        return this.bookWhs;
    }
    
    public void setBookWhs(String bookWhs) {
        this.bookWhs = bookWhs;
    }
    
    @Column(name="BOOK_LOCATION", length=10)

    public String getBookLocation() {
        return this.bookLocation;
    }
    
    public void setBookLocation(String bookLocation) {
        this.bookLocation = bookLocation;
    }
    
    @Column(name="BOOK_MATERIAL_CLASS", length=10)

    public String getBookMaterialClass() {
        return this.bookMaterialClass;
    }
    
    public void setBookMaterialClass(String bookMaterialClass) {
        this.bookMaterialClass = bookMaterialClass;
    }
    
    @Column(name="BOOK_MAN", nullable=false, length=16)

    public String getBookMan() {
        return this.bookMan;
    }
    
    public void setBookMan(String bookMan) {
        this.bookMan = bookMan;
    }
    
    @Column(name="BOOK_STATUS", nullable=false, length=3)

    public String getBookStatus() {
        return this.bookStatus;
    }
    
    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }
    
    @Column(name="LAST_MODIFIED_BY", nullable=false, length=16)

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }
    
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LAST_MODIFIED_DATE", nullable=false, length=7)

    public Date getLastModifiedDate() {
        return this.lastModifiedDate;
    }
    
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    @Column(name="ENTERPRISE_CODE", nullable=false, length=10)

    public String getEnterpriseCode() {
        return this.enterpriseCode;
    }
    
    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }
    
    @Column(name="IS_USE", nullable=false, length=1)

    public String getIsUse() {
        return this.isUse;
    }
    
    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }
   








}