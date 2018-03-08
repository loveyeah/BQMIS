/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.comm;

/**
 * AdCPaper entity.
 * 
 * @author xsTan
 */
public class ComAdCPaper implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
	/** 类别编码 */
	private String papertypeCode;
	/** 类别名称 */
	private String papertypeName;

	/** default constructor */
	public ComAdCPaper() {
	}

	/** minimal constructor */
	public ComAdCPaper(String papertypeCode) {
		this.papertypeCode = papertypeCode;
	}

	/** full constructor */
	public ComAdCPaper(String papertypeCode, String papertypeName) {
		this.papertypeCode = papertypeCode;
		this.papertypeName = papertypeName;
	}
	
	/**
	 * @return 类别编码
	 */
	public String getPapertypeCode() {
		return papertypeCode;
	}

	/**
	 * @param 类别编码
	 */
	public void setPapertypeCode(String papertypeCode) {
		this.papertypeCode = papertypeCode;
	}

	/**
	 * @return 类别名称
	 */
	public String getPapertypeName() {
		return papertypeName;
	}

	/**
	 * @param 类别名称
	 */
	public void setPapertypeName(String papertypeName) {
		this.papertypeName = papertypeName;
	}
}