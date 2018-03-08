package power.ejb.administration;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * AdJOutQuestReader entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AD_J_OUT_QUEST_READER")
public class AdJOutQuestReader implements java.io.Serializable {

	// Fields

	private AdJOutQuestReaderId id;

	// Constructors

	/** default constructor */
	public AdJOutQuestReader() {
	}

	/** full constructor */
	public AdJOutQuestReader(AdJOutQuestReaderId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "id", column = @Column(name = "ID", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "applyId", column = @Column(name = "APPLY_ID", length = 12)),
			@AttributeOverride(name = "readMan", column = @Column(name = "READ_MAN", length = 6)),
			@AttributeOverride(name = "updateUser", column = @Column(name = "UPDATE_USER", length = 10)),
			@AttributeOverride(name = "updateTime", column = @Column(name = "UPDATE_TIME", length = 7)),
			@AttributeOverride(name = "isUse", column = @Column(name = "IS_USE", length = 1)) })
	public AdJOutQuestReaderId getId() {
		return this.id;
	}

	public void setId(AdJOutQuestReaderId id) {
		this.id = id;
	}

}