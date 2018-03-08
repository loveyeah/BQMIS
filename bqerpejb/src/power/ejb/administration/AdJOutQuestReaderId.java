package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJOutQuestReaderId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Embeddable
public class AdJOutQuestReaderId implements java.io.Serializable {

	// Fields

	private Long id;
	private String applyId;
	private String readMan;
	private String updateUser;
	private Date updateTime;
	private String isUse;

	// Constructors

	/** default constructor */
	public AdJOutQuestReaderId() {
	}

	/** minimal constructor */
	public AdJOutQuestReaderId(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJOutQuestReaderId(Long id, String applyId, String readMan,
			String updateUser, Date updateTime, String isUse) {
		this.id = id;
		this.applyId = applyId;
		this.readMan = readMan;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.isUse = isUse;
	}

	// Property accessors

	@Column(name = "ID", nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "APPLY_ID", length = 12)
	public String getApplyId() {
		return this.applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	@Column(name = "READ_MAN", length = 20)
	public String getReadMan() {
		return this.readMan;
	}

	public void setReadMan(String readMan) {
		this.readMan = readMan;
	}

	@Column(name = "UPDATE_USER", length = 20)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AdJOutQuestReaderId))
			return false;
		AdJOutQuestReaderId castOther = (AdJOutQuestReaderId) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getApplyId() == castOther.getApplyId()) || (this
						.getApplyId() != null
						&& castOther.getApplyId() != null && this.getApplyId()
						.equals(castOther.getApplyId())))
				&& ((this.getReadMan() == castOther.getReadMan()) || (this
						.getReadMan() != null
						&& castOther.getReadMan() != null && this.getReadMan()
						.equals(castOther.getReadMan())))
				&& ((this.getUpdateUser() == castOther.getUpdateUser()) || (this
						.getUpdateUser() != null
						&& castOther.getUpdateUser() != null && this
						.getUpdateUser().equals(castOther.getUpdateUser())))
				&& ((this.getUpdateTime() == castOther.getUpdateTime()) || (this
						.getUpdateTime() != null
						&& castOther.getUpdateTime() != null && this
						.getUpdateTime().equals(castOther.getUpdateTime())))
				&& ((this.getIsUse() == castOther.getIsUse()) || (this
						.getIsUse() != null
						&& castOther.getIsUse() != null && this.getIsUse()
						.equals(castOther.getIsUse())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getApplyId() == null ? 0 : this.getApplyId().hashCode());
		result = 37 * result
				+ (getReadMan() == null ? 0 : this.getReadMan().hashCode());
		result = 37
				* result
				+ (getUpdateUser() == null ? 0 : this.getUpdateUser()
						.hashCode());
		result = 37
				* result
				+ (getUpdateTime() == null ? 0 : this.getUpdateTime()
						.hashCode());
		result = 37 * result
				+ (getIsUse() == null ? 0 : this.getIsUse().hashCode());
		return result;
	}

}