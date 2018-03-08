package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtKkxJSjyb entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_KKX_J_SJYB")
public class PtKkxJSjyb implements java.io.Serializable {

	// Fields

	private Long kkxybId;
	private Date month;
	private String blockCode;
	private Double fdl;
	private Double uth;
	private Double ph;
	private Double undh;
	private Double sh;
	private Double rh;
	private Double pot;
	private Double poh;
	private Double uot;
	private Double uoh;
	private Double fot;
	private Double foh;
	private Double for1;
	private Double eaf;
	private Double exr;
	private Double pof;
	private Double uof;
	private Double fof;
	private Double af;
	private Double sf;
	private Double udf;
	private Double utf;
	private Double uor;
	private Double foor;
	private Double mttpo;
	private Double mttuo;
	private Double cah;
	private Double mtbf;
	private String enterpriseCode;
	private Double eundh;

	// Constructors

	/** default constructor */
	public PtKkxJSjyb() {
	}

	/** minimal constructor */
	public PtKkxJSjyb(Long kkxybId) {
		this.kkxybId = kkxybId;
	}

	/** full constructor */
	public PtKkxJSjyb(Long kkxybId, Date month, String blockCode, Double fdl,
			Double uth, Double ph, Double undh, Double sh, Double rh,
			Double pot, Double poh, Double uot, Double uoh, Double fot,
			Double foh, Double for1, Double eaf, Double exr, Double pof,
			Double uof, Double fof, Double af, Double sf, Double udf,
			Double utf, Double uor, Double foor, Double mttpo, Double mttuo,
			Double cah, Double mtbf, String enterpriseCode, Double eundh) {
		this.kkxybId = kkxybId;
		this.month = month;
		this.blockCode = blockCode;
		this.fdl = fdl;
		this.uth = uth;
		this.ph = ph;
		this.undh = undh;
		this.sh = sh;
		this.rh = rh;
		this.pot = pot;
		this.poh = poh;
		this.uot = uot;
		this.uoh = uoh;
		this.fot = fot;
		this.foh = foh;
		this.for1 = for1;
		this.eaf = eaf;
		this.exr = exr;
		this.pof = pof;
		this.uof = uof;
		this.fof = fof;
		this.af = af;
		this.sf = sf;
		this.udf = udf;
		this.utf = utf;
		this.uor = uor;
		this.foor = foor;
		this.mttpo = mttpo;
		this.mttuo = mttuo;
		this.cah = cah;
		this.mtbf = mtbf;
		this.enterpriseCode = enterpriseCode;
		this.eundh = eundh;
	}

	// Property accessors
	@Id
	@Column(name = "KKXYB_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getKkxybId() {
		return this.kkxybId;
	}

	public void setKkxybId(Long kkxybId) {
		this.kkxybId = kkxybId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MONTH", length = 7)
	public Date getMonth() {
		return this.month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	@Column(name = "BLOCK_CODE", length = 2)
	public String getBlockCode() {
		return this.blockCode;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	@Column(name = "FDL", precision = 14, scale = 4)
	public Double getFdl() {
		return this.fdl;
	}

	public void setFdl(Double fdl) {
		this.fdl = fdl;
	}

	@Column(name = "UTH", precision = 14, scale = 4)
	public Double getUth() {
		return this.uth;
	}

	public void setUth(Double uth) {
		this.uth = uth;
	}

	@Column(name = "PH", precision = 14, scale = 4)
	public Double getPh() {
		return this.ph;
	}

	public void setPh(Double ph) {
		this.ph = ph;
	}

	@Column(name = "UNDH", precision = 14, scale = 4)
	public Double getUndh() {
		return this.undh;
	}

	public void setUndh(Double undh) {
		this.undh = undh;
	}

	@Column(name = "SH", precision = 14, scale = 4)
	public Double getSh() {
		return this.sh;
	}

	public void setSh(Double sh) {
		this.sh = sh;
	}

	@Column(name = "RH", precision = 14, scale = 4)
	public Double getRh() {
		return this.rh;
	}

	public void setRh(Double rh) {
		this.rh = rh;
	}

	@Column(name = "POT", precision = 14, scale = 4)
	public Double getPot() {
		return this.pot;
	}

	public void setPot(Double pot) {
		this.pot = pot;
	}

	@Column(name = "POH", precision = 14, scale = 4)
	public Double getPoh() {
		return this.poh;
	}

	public void setPoh(Double poh) {
		this.poh = poh;
	}

	@Column(name = "UOT", precision = 14, scale = 4)
	public Double getUot() {
		return this.uot;
	}

	public void setUot(Double uot) {
		this.uot = uot;
	}

	@Column(name = "UOH", precision = 14, scale = 4)
	public Double getUoh() {
		return this.uoh;
	}

	public void setUoh(Double uoh) {
		this.uoh = uoh;
	}

	@Column(name = "FOT", precision = 14, scale = 4)
	public Double getFot() {
		return this.fot;
	}

	public void setFot(Double fot) {
		this.fot = fot;
	}

	@Column(name = "FOH", precision = 14, scale = 4)
	public Double getFoh() {
		return this.foh;
	}

	public void setFoh(Double foh) {
		this.foh = foh;
	}

	@Column(name = "FOR1", precision = 14, scale = 4)
	public Double getFor1() {
		return this.for1;
	}

	public void setFor1(Double for1) {
		this.for1 = for1;
	}

	@Column(name = "EAF", precision = 14, scale = 4)
	public Double getEaf() {
		return this.eaf;
	}

	public void setEaf(Double eaf) {
		this.eaf = eaf;
	}

	@Column(name = "EXR", precision = 14, scale = 4)
	public Double getExr() {
		return this.exr;
	}

	public void setExr(Double exr) {
		this.exr = exr;
	}

	@Column(name = "POF", precision = 14, scale = 4)
	public Double getPof() {
		return this.pof;
	}

	public void setPof(Double pof) {
		this.pof = pof;
	}

	@Column(name = "UOF", precision = 14, scale = 4)
	public Double getUof() {
		return this.uof;
	}

	public void setUof(Double uof) {
		this.uof = uof;
	}

	@Column(name = "FOF", precision = 14, scale = 4)
	public Double getFof() {
		return this.fof;
	}

	public void setFof(Double fof) {
		this.fof = fof;
	}

	@Column(name = "AF", precision = 14, scale = 4)
	public Double getAf() {
		return this.af;
	}

	public void setAf(Double af) {
		this.af = af;
	}

	@Column(name = "SF", precision = 14, scale = 4)
	public Double getSf() {
		return this.sf;
	}

	public void setSf(Double sf) {
		this.sf = sf;
	}

	@Column(name = "UDF", precision = 14, scale = 4)
	public Double getUdf() {
		return this.udf;
	}

	public void setUdf(Double udf) {
		this.udf = udf;
	}

	@Column(name = "UTF", precision = 14, scale = 4)
	public Double getUtf() {
		return this.utf;
	}

	public void setUtf(Double utf) {
		this.utf = utf;
	}

	@Column(name = "UOR", precision = 14, scale = 4)
	public Double getUor() {
		return this.uor;
	}

	public void setUor(Double uor) {
		this.uor = uor;
	}

	@Column(name = "FOOR", precision = 14, scale = 4)
	public Double getFoor() {
		return this.foor;
	}

	public void setFoor(Double foor) {
		this.foor = foor;
	}

	@Column(name = "MTTPO", precision = 14, scale = 4)
	public Double getMttpo() {
		return this.mttpo;
	}

	public void setMttpo(Double mttpo) {
		this.mttpo = mttpo;
	}

	@Column(name = "MTTUO", precision = 14, scale = 4)
	public Double getMttuo() {
		return this.mttuo;
	}

	public void setMttuo(Double mttuo) {
		this.mttuo = mttuo;
	}

	@Column(name = "CAH", precision = 14, scale = 4)
	public Double getCah() {
		return this.cah;
	}

	public void setCah(Double cah) {
		this.cah = cah;
	}

	@Column(name = "MTBF", precision = 14, scale = 4)
	public Double getMtbf() {
		return this.mtbf;
	}

	public void setMtbf(Double mtbf) {
		this.mtbf = mtbf;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "EUNDH", precision = 14, scale = 4)
	public Double getEundh() {
		return this.eundh;
	}

	public void setEundh(Double eundh) {
		this.eundh = eundh;
	}

}