package power.ejb.productiontec.report;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ptJDependReport entity
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name="pt_j_depend_report",schema="power")
public class ptJDependReport implements java.io.Serializable {
	private Long dependId;
	private String dependName;
	private String dependPath;
	private Date dependDate;
	private String dependType;//机组类型
	private String dependYear;
	private String dependMonth;
	private String dependSesson;
	private String dependEntry;
	private String dependEntryName;//上报人工号
	private String dependMemo;
	private String dependTimeType;
	
	public ptJDependReport(){
		
	}
	
	public ptJDependReport(long dependId,String dependName,String dependPath,Date dependDate,
			String dependType,String dependYear,String dependMonth,String dependSesson,
			String dependEntry,String dependMemo,String dependEntryName ){
		this.dependId = dependId;
		this.dependName = dependName;
		this.dependPath = dependPath;
		this.dependDate = dependDate;
		this.dependType = dependType;
		this.dependYear = dependYear;
		this.dependMonth = dependMonth;
		this.dependSesson = dependSesson;
		this.dependEntry = dependEntry;
		this.dependMemo = dependMemo;
		this.dependTimeType = dependTimeType;
		this.dependEntryName = dependEntryName;
	}
	
	@Id
	@Column(name="DEPEND_ID",unique=true,nullable=false,precision=10,scale=0)//scale:含义？
	public long getDependId() {
		return dependId;
	}
	public void setDependId(long dependId) {
		this.dependId = dependId;
	}
	@Column(name="Depend_Name",length=200)
	public String getDependName() {
		return dependName;
	}
	public void setDependName(String dependName) {
		this.dependName = dependName;
	}
	
	@Column(name="depend_path",length=100)
	public String getDependPath() {
		return dependPath;
	}
	public void setDependPath(String dependPath) {
		this.dependPath = dependPath;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="depend_date",length=7)//7?
	public Date getDependDate() {
		return dependDate;
	}
	public void setDependDate(Date dependDate) {
		this.dependDate = dependDate;
	}

	@Column(name="depend_type",length=20)
	public String getDependType() {
		return dependType;
	}

	public void setDependType(String dependType) {
		this.dependType = dependType;
	}

	@Column(name="depend_year",length=4)
	public String getDependYear() {
		return dependYear;
	}

	public void setDependYear(String dependYear) {
		this.dependYear = dependYear;
	}

	@Column(name="depend_month",length=4)
	public String getDependMonth() {
		return dependMonth;
	}

	public void setDependMonth(String dependMonth) {
		this.dependMonth = dependMonth;
	}

	@Column(name="depend_sesson",length=4)
	public String getDependSesson() {
		return dependSesson;
	}

	public void setDependSesson(String dependSesson) {
		this.dependSesson = dependSesson;
	}

	@Column(name="depend_entry",length=10)
	public String getDependEntry() {
		return dependEntry;
	}

	public void setDependEntry(String dependEntry) {
		this.dependEntry = dependEntry;
	}

	@Column (name="depend_memo",length=100)
	public String getDependMemo() {
		return dependMemo;
	}
	@Column (name="depend_Time_Type",length=100)
	public String getDependTimeType() {
		return dependTimeType;
	}

	public void setDependTimeType(String dependTimeType) {
		this.dependTimeType = dependTimeType;
	}

	public void setDependMemo(String dependMemo) {
		this.dependMemo = dependMemo;
	}

	@Column (name="depend_Entry_Name",length=100)
	public String getDependEntryName() {
		return dependEntryName;
	}

	public void setDependEntryName(String dependEntryName) {
		this.dependEntryName = dependEntryName;
	}

	

}
