package power.ejb.productiontec.insulation;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJyjdJSbysjhlh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JYJD_J_SBYSJHLH")
public class PtJyjdJSbysjhlh implements java.io.Serializable {

	// Fields

	private Long jysbysjhId;
	private Long deviceId;
	private Date nextDate;
	private String operateBy;
	private Date operateDate;

	// Constructors

	/** default constructor */
	public PtJyjdJSbysjhlh() {
	}

	/** minimal constructor */
	public PtJyjdJSbysjhlh(Long jysbysjhId) {
		this.jysbysjhId = jysbysjhId;
	}

	/** full constructor */
	public PtJyjdJSbysjhlh(Long jysbysjhId, Long deviceId, Date nextDate,
			String operateBy, Date operateDate) {
		this.jysbysjhId = jysbysjhId;
		this.deviceId = deviceId;
		this.nextDate = nextDate;
		this.operateBy = operateBy;
		this.operateDate = operateDate;
	}

	// Property accessors
	@Id
	@Column(name = "JYSBYSJH_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJysbysjhId() {
		return this.jysbysjhId;
	}

	public void setJysbysjhId(Long jysbysjhId) {
		this.jysbysjhId = jysbysjhId;
	}

	@Column(name = "DEVICE_ID", precision = 10, scale = 0)
	public Long getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
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