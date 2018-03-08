package power.ejb.manage.contract.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJArchives entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_ARCHIVES")
public class ConJArchives implements java.io.Serializable {

	// Fields

	private Long archivesId;
	private String undertakeNo;
	private String archivesName;
	private Long archivesCount;
	private Long pieceCount;
	private Long pageCount;
	private String unitName;
	private String weaveDate;
	private String timeLimit;
	private String keepLevel;
	private String keepPosition;
	private String upbuildPeople;
	private Date upbuildDate;
	private String jerquePeople;
	private Date jerqueDate;
	private String archivesNo;
	private String microNo;
	private Long charCount;
	private Long drawCount;
	private String memo;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public ConJArchives() {
	}

	/** minimal constructor */
	public ConJArchives(Long archivesId, String undertakeNo,
			String enterpriseCode, String isUse) {
		this.archivesId = archivesId;
		this.undertakeNo = undertakeNo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public ConJArchives(Long archivesId, String undertakeNo,
			String archivesName, Long archivesCount, Long pieceCount,
			Long pageCount, String unitName, String weaveDate,
			String timeLimit, String keepLevel, String keepPosition,
			String upbuildPeople, Date upbuildDate, String jerquePeople,
			Date jerqueDate, String archivesNo, String microNo, Long charCount,
			Long drawCount, String memo, String enterpriseCode, String isUse) {
		this.archivesId = archivesId;
		this.undertakeNo = undertakeNo;
		this.archivesName = archivesName;
		this.archivesCount = archivesCount;
		this.pieceCount = pieceCount;
		this.pageCount = pageCount;
		this.unitName = unitName;
		this.weaveDate = weaveDate;
		this.timeLimit = timeLimit;
		this.keepLevel = keepLevel;
		this.keepPosition = keepPosition;
		this.upbuildPeople = upbuildPeople;
		this.upbuildDate = upbuildDate;
		this.jerquePeople = jerquePeople;
		this.jerqueDate = jerqueDate;
		this.archivesNo = archivesNo;
		this.microNo = microNo;
		this.charCount = charCount;
		this.drawCount = drawCount;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "ARCHIVES_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getArchivesId() {
		return this.archivesId;
	}

	public void setArchivesId(Long archivesId) {
		this.archivesId = archivesId;
	}

	@Column(name = "UNDERTAKE_NO", nullable = false, length = 30)
	public String getUndertakeNo() {
		return this.undertakeNo;
	}

	public void setUndertakeNo(String undertakeNo) {
		this.undertakeNo = undertakeNo;
	}

	@Column(name = "ARCHIVES_NAME", length = 300)
	public String getArchivesName() {
		return this.archivesName;
	}

	public void setArchivesName(String archivesName) {
		this.archivesName = archivesName;
	}

	@Column(name = "ARCHIVES_COUNT", precision = 4, scale = 0)
	public Long getArchivesCount() {
		return this.archivesCount;
	}

	public void setArchivesCount(Long archivesCount) {
		this.archivesCount = archivesCount;
	}

	@Column(name = "PIECE_COUNT", precision = 4, scale = 0)
	public Long getPieceCount() {
		return this.pieceCount;
	}

	public void setPieceCount(Long pieceCount) {
		this.pieceCount = pieceCount;
	}

	@Column(name = "PAGE_COUNT", precision = 4, scale = 0)
	public Long getPageCount() {
		return this.pageCount;
	}

	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}

	@Column(name = "UNIT_NAME", length = 300)
	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name = "WEAVE_DATE", length = 30)
	public String getWeaveDate() {
		return this.weaveDate;
	}

	public void setWeaveDate(String weaveDate) {
		this.weaveDate = weaveDate;
	}

	@Column(name = "TIME_LIMIT", length = 2)
	public String getTimeLimit() {
		return this.timeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}

	@Column(name = "KEEP_LEVEL", length = 10)
	public String getKeepLevel() {
		return this.keepLevel;
	}

	public void setKeepLevel(String keepLevel) {
		this.keepLevel = keepLevel;
	}

	@Column(name = "KEEP_POSITION", length = 10)
	public String getKeepPosition() {
		return this.keepPosition;
	}

	public void setKeepPosition(String keepPosition) {
		this.keepPosition = keepPosition;
	}

	@Column(name = "UPBUILD_PEOPLE", length = 16)
	public String getUpbuildPeople() {
		return this.upbuildPeople;
	}

	public void setUpbuildPeople(String upbuildPeople) {
		this.upbuildPeople = upbuildPeople;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPBUILD_DATE", length = 7)
	public Date getUpbuildDate() {
		return this.upbuildDate;
	}

	public void setUpbuildDate(Date upbuildDate) {
		this.upbuildDate = upbuildDate;
	}

	@Column(name = "JERQUE_PEOPLE", length = 16)
	public String getJerquePeople() {
		return this.jerquePeople;
	}

	public void setJerquePeople(String jerquePeople) {
		this.jerquePeople = jerquePeople;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "JERQUE_DATE", length = 7)
	public Date getJerqueDate() {
		return this.jerqueDate;
	}

	public void setJerqueDate(Date jerqueDate) {
		this.jerqueDate = jerqueDate;
	}

	@Column(name = "ARCHIVES_NO", length = 30)
	public String getArchivesNo() {
		return this.archivesNo;
	}

	public void setArchivesNo(String archivesNo) {
		this.archivesNo = archivesNo;
	}

	@Column(name = "MICRO_NO", length = 30)
	public String getMicroNo() {
		return this.microNo;
	}

	public void setMicroNo(String microNo) {
		this.microNo = microNo;
	}

	@Column(name = "CHAR_COUNT", precision = 4, scale = 0)
	public Long getCharCount() {
		return this.charCount;
	}

	public void setCharCount(Long charCount) {
		this.charCount = charCount;
	}

	@Column(name = "DRAW_COUNT", precision = 4, scale = 0)
	public Long getDrawCount() {
		return this.drawCount;
	}

	public void setDrawCount(Long drawCount) {
		this.drawCount = drawCount;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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