package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJLeftDay entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_LEFT_DAY", schema = "POWER")
public class HrJLeftDay implements java.io.Serializable {

	// Fields

	private Long id;
	private Long empId;
	private Long hisDay;
	private Long leftDay;
	private Date lastTime;

	// Constructors

	/** default constructor */
	public HrJLeftDay() {
	}

	/** minimal constructor */
	public HrJLeftDay(Long id, Long empId) {
		this.id = id;
		this.empId = empId;
	}

	/** full constructor */
	public HrJLeftDay(Long id, Long empId, Long hisDay, Long leftDay,
			Date lastTime) {
		this.id = id;
		this.empId = empId;
		this.hisDay = hisDay;
		this.leftDay = leftDay;
		this.lastTime = lastTime;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "EMP_ID", nullable = false, precision = 22, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "HIS_DAY", precision = 22, scale = 0)
	public Long getHisDay() {
		return this.hisDay;
	}

	public void setHisDay(Long hisDay) {
		this.hisDay = hisDay;
	}

	@Column(name = "LEFT_DAY", precision = 22, scale = 0)
	public Long getLeftDay() {
		return this.leftDay;
	}

	public void setLeftDay(Long leftDay) {
		this.leftDay = leftDay;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_TIME", length = 7)
	public Date getLastTime() {
		return this.lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

}