package power.ejb.productiontec.insulation;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJyjdJYqybysjh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JYJD_J_YQYBYSJH")
public class PtJyjdJYqybysjh implements java.io.Serializable {

	// Fields

	private Long yqybysjhId;
	private Long regulatorId;
	private Date nextDate;
	private String operateBy;
	private Date operateDate;

	// Constructors

	/** default constructor */
	public PtJyjdJYqybysjh() {
	}

	/** minimal constructor */
	public PtJyjdJYqybysjh(Long yqybysjhId) {
		this.yqybysjhId = yqybysjhId;
	}

	/** full constructor */
	public PtJyjdJYqybysjh(Long yqybysjhId, Long regulatorId, Date nextDate,
			String operateBy, Date operateDate) {
		this.yqybysjhId = yqybysjhId;
		this.regulatorId = regulatorId;
		this.nextDate = nextDate;
		this.operateBy = operateBy;
		this.operateDate = operateDate;
	}

	// Property accessors
	@Id
	@Column(name = "YQYBYSJH_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getYqybysjhId() {
		return this.yqybysjhId;
	}

	public void setYqybysjhId(Long yqybysjhId) {
		this.yqybysjhId = yqybysjhId;
	}

	@Column(name = "REGULATOR_ID", precision = 10, scale = 0)
	public Long getRegulatorId() {
		return this.regulatorId;
	}

	public void setRegulatorId(Long regulatorId) {
		this.regulatorId = regulatorId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "NEXT_DATE", length = 7)
	public Date getNextDate() {
		return this.nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

	@Column(name = "OPERATE_BY", length = 16)
	public String getOperateBy() {
		return this.operateBy;
	}

	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OPERATE_DATE", length = 7)
	public Date getOperateDate() {
		return this.operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

}