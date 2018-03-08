package power.ejb.run.securityproduction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SpJAntiAccidentDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_ANTI_ACCIDENT_DETAILS")
public class SpJAntiAccidentDetails implements java.io.Serializable {

	// Fields

	private Long detailsId;
	private String measureCode;
	private String content;

	// Constructors

	/** default constructor */
	public SpJAntiAccidentDetails() {
	}

	/** minimal constructor */
	public SpJAntiAccidentDetails(Long detailsId, String measureCode) {
		this.detailsId = detailsId;
		this.measureCode = measureCode;
	}

	/** full constructor */
	public SpJAntiAccidentDetails(Long detailsId, String measureCode,
			String content) {
		this.detailsId = detailsId;
		this.measureCode = measureCode;
		this.content = content;
	}

	// Property accessors
	@Id
	@Column(name = "DETAILS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDetailsId() {
		return this.detailsId;
	}

	public void setDetailsId(Long detailsId) {
		this.detailsId = detailsId;
	}

	@Column(name = "MEASURE_CODE", nullable = false, length = 6)
	public String getMeasureCode() {
		return this.measureCode;
	}

	public void setMeasureCode(String measureCode) {
		this.measureCode = measureCode;
	}

	@Column(name = "CONTENT", length = 500)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}