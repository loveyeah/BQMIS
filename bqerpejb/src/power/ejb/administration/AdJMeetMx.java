package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJMeetMx entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_MEET_MX")
public class AdJMeetMx implements java.io.Serializable {

	// Fields

	private Long id;
	private String meetId;
	private String payName;
	private Double payBudget;
	private Double payReal;
	private String note;
	private String isUse;
	private String updateUser;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public AdJMeetMx() {
	}

	/** minimal constructor */
	public AdJMeetMx(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJMeetMx(Long id, String meetId, String payName, Double payBudget,
			Double payReal, String note, String isUse, String updateUser,
			Date updateTime) {
		this.id = id;
		this.meetId = meetId;
		this.payName = payName;
		this.payBudget = payBudget;
		this.payReal = payReal;
		this.note = note;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
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

	@Column(name = "MEET_ID", length = 12)
	public String getMeetId() {
		return this.meetId;
	}

	public void setMeetId(String meetId) {
		this.meetId = meetId;
	}

	@Column(name = "PAY_NAME", length = 100)
	public String getPayName() {
		return this.payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	@Column(name = "PAY_BUDGET", precision = 15, scale = 4)
	public Double getPayBudget() {
		return this.payBudget;
	}

	public void setPayBudget(Double payBudget) {
		this.payBudget = payBudget;
	}

	@Column(name = "PAY_REAL", precision = 15, scale = 4)
	public Double getPayReal() {
		return this.payReal;
	}

	public void setPayReal(Double payReal) {
		this.payReal = payReal;
	}

	@Column(name = "NOTE", length = 200)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "UPDATE_USER", length = 6)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}