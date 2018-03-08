package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvJMaterialAttachment entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_J_MATERIAL_ATTACHMENT")
public class InvJMaterialAttachment implements java.io.Serializable {

	// Fields
	private Long id;
	private String docCode;
	private String oriDocName;
	private String oriDocExt;
	private byte[] docContent;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public InvJMaterialAttachment() {
	}

	/** full constructor */
	public InvJMaterialAttachment(Long id, String docCode, String oriDocName,
			String oriDocExt,byte[] docContent, String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.docCode = docCode;
		this.oriDocName = oriDocName;
		this.oriDocExt = oriDocExt;
		this.docContent = docContent;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}	

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DOC_CODE", nullable = false, length = 50)
	public String getDocCode() {
		return this.docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	@Column(name = "ORI_DOC_NAME", nullable = false, length = 200)
	public String getOriDocName() {
		return this.oriDocName;
	}

	public void setOriDocName(String oriDocName) {
		this.oriDocName = oriDocName;
	}

	@Column(name = "ORI_DOC_EXT", nullable = false, length = 10)
	public String getOriDocExt() {
		return this.oriDocExt;
	}

	public void setOriDocExt(String oriDocExt) {
		this.oriDocExt = oriDocExt;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 10)
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
	@Column(name = "DOC_CONTENT")
	public byte[] getDocContent() {
		return this.docContent;
	}

	public void setDocContent(byte[] docContent) {
		this.docContent = docContent;
	}

	
}