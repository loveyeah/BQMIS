package power.ejb.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JxlReports entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "JXL_REPORTS")
public class JxlReports implements java.io.Serializable {

	// Fields

	private String code;
	private byte[] content;
	private String memo;
	private String isUse;
	private String dateType;

	// Constructors

	/** default constructor */
	public JxlReports() {
	}

	/** minimal constructor */
	public JxlReports(String code) {
		this.code = code;
	}

	/** full constructor */
	public JxlReports(String code, byte[] content, String memo,String isUse,String dateType) {
		this.code = code;
		this.content = content;
		this.memo = memo;
		this.isUse = isUse;
		this.dateType = dateType;
	}

	// Property accessors
	@Id
	@Column(name = "CODE", unique = true, nullable = false, length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "CONTENT")
	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Column(name = "MEMO", length = 50)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	@Column(name = "DATE_TYPE", length = 1)
	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}


}