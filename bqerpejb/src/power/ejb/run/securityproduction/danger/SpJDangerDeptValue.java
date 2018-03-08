package power.ejb.run.securityproduction.danger;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJDangerDeptValue entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_DANGER_DEPT_VALUE")
public class SpJDangerDeptValue implements java.io.Serializable {

	// Fields

	private Long id;
	private Long dangerId;
	private String scoreSort;//1为L值，2为B2值
	private String score1;
	private String score2;
	private String score3;
	private String score4;
	private String score5;
	private String score6;
	private String score7;
	private String score8;
	private String score9;
	private String score10;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String isUse;//"Y"在用，"N"未使用
	private String enterpriseCode;
	// Constructors

	

	/** default constructor */
	public SpJDangerDeptValue() {
	}

	/** minimal constructor */
	public SpJDangerDeptValue(Long id) {
		this.id = id;
	}
	/** full constructor */
	public SpJDangerDeptValue(Long id, Long dangerId, String scoreSort,
			String score1, String score2, String score3, String score4,
			String score5, String score6, String score7, String score8,
			String score9, String score10, String lastModifiedBy,
			Date lastModifiedDate, String isUse, String enterpriseCode) {
		this.id = id;
		this.dangerId = dangerId;
		this.scoreSort = scoreSort;
		this.score1 = score1;
		this.score2 = score2;
		this.score3 = score3;
		this.score4 = score4;
		this.score5 = score5;
		this.score6 = score6;
		this.score7 = score7;
		this.score8 = score8;
		this.score9 = score9;
		this.score10 = score10;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "DANGER_ID", precision = 10, scale = 0)
	public Long getDangerId() {
		return this.dangerId;
	}

	public void setDangerId(Long dangerId) {
		this.dangerId = dangerId;
	}

	@Column(name = "SCORE_SORT", length = 1)
	public String getScoreSort() {
		return this.scoreSort;
	}

	public void setScoreSort(String scoreSort) {
		this.scoreSort = scoreSort;
	}

	@Column(name = "SCORE1", length = 20)
	public String getScore1() {
		return this.score1;
	}

	public void setScore1(String score1) {
		this.score1 = score1;
	}

	@Column(name = "SCORE2", length = 20)
	public String getScore2() {
		return this.score2;
	}

	public void setScore2(String score2) {
		this.score2 = score2;
	}

	@Column(name = "SCORE3", length = 20)
	public String getScore3() {
		return this.score3;
	}

	public void setScore3(String score3) {
		this.score3 = score3;
	}

	@Column(name = "SCORE4", length = 20)
	public String getScore4() {
		return this.score4;
	}

	public void setScore4(String score4) {
		this.score4 = score4;
	}

	@Column(name = "SCORE5", length = 20)
	public String getScore5() {
		return this.score5;
	}

	public void setScore5(String score5) {
		this.score5 = score5;
	}

	@Column(name = "SCORE6", length = 20)
	public String getScore6() {
		return this.score6;
	}

	public void setScore6(String score6) {
		this.score6 = score6;
	}

	@Column(name = "SCORE7", length = 20)
	public String getScore7() {
		return this.score7;
	}

	public void setScore7(String score7) {
		this.score7 = score7;
	}

	@Column(name = "SCORE8", length = 20)
	public String getScore8() {
		return this.score8;
	}

	public void setScore8(String score8) {
		this.score8 = score8;
	}

	@Column(name = "SCORE9", length = 20)
	public String getScore9() {
		return this.score9;
	}

	public void setScore9(String score9) {
		this.score9 = score9;
	}

	@Column(name = "SCORE10", length = 20)
	public String getScore10() {
		return this.score10;
	}

	public void setScore10(String score10) {
		this.score10 = score10;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 20)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}