/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 进出车辆登记AdCPaper
 * @author li chensheng
 *  
 */
@Entity
@Table(name = "AD_C_PAPER")
public class AdCPaper implements java.io.Serializable {

	// Fields
    //流水号
	private Long id;
	//证件编码
	private String papertypeCode;
	//证件名
	private String papertypeName;
	//检索码
	private String retrieveCode;
	//是否使用
	private String isUse;
	//更新者
	private String updateUser;
	//更新时间
	private Date updateTime;
	// 企业编码
	private String enterpriseCode;

	// Constructors

	

	/** default constructor */
	public AdCPaper() {
	}

	/** minimal constructor */
	public AdCPaper(Long id, String papertypeCode) {
		this.id = id;
		this.papertypeCode = papertypeCode;
	}

	/** full constructor */
	public AdCPaper(Long id, String papertypeCode, String papertypeName,
			String retrieveCode, String isUse, String updateUser,
			Date updateTime,String enterpriseCode) {
		this.id = id;
		this.papertypeCode = papertypeCode;
		this.papertypeName = papertypeName;
		this.retrieveCode = retrieveCode;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	//取得流水号
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}
	//设置流水号
	public void setId(Long id) {
		this.id = id;
	}
	//取得证件编码
	@Column(name = "PAPERTYPE_CODE", nullable = false, length = 2)
	public String getPapertypeCode() {
		return this.papertypeCode;
	}
	//设置证件编码
	public void setPapertypeCode(String papertypeCode) {
		this.papertypeCode = papertypeCode;
	}
	//取得证件名
	@Column(name = "PAPERTYPE_NAME", length = 50)
	public String getPapertypeName() {
		return this.papertypeName;
	}
	//设置证件名
	public void setPapertypeName(String papertypeName) {
		this.papertypeName = papertypeName;
	}
	//取得检索码
	@Column(name = "RETRIEVE_CODE", length = 6)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}
	//设置检索码
	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}
	//取得是否使用
	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}
	//设置是否使用
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	//取得更新者
	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}
	//设置更新者
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	//取得更新时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}
	//设置更新时间
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	// 企业编码
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}