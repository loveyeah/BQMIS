package power.ejb.productiontec.dependabilityAnalysis.business.form;

import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
public class AuxiliaryForm implements Serializable
{
	private Long auxiliaryId;
	private Long blockId;
	private Long auxiliaryTypeId;
	private String auxiliaryCode;
	private String auxiliaryName;
	private String auxiliaryModel;
	private String updateNo;
	private Date startProDate;
	private Date stopStatDate;
	private Date leaveFactoryDate;
	private Date statDate;
	private Date stopUseDate;
	private String leaveFactoryNo;
	private String factoryCode;
	private String produceFactory;
	
	// 机组名
	private String blockName;
	// 类型名
	private String typeName;
	private String startProDateString;
	private String stopStatDateString;
	private String leaveFactoryDateString;
	private String statDateString;
	private String stopUseDateString;
	// 容量
	private Double nameplateCapability;
	public Double getNameplateCapability() {
		return nameplateCapability;
	}
	public void setNameplateCapability(Double nameplateCapability) {
		this.nameplateCapability = nameplateCapability;
	}
	public Long getAuxiliaryId() {
		return auxiliaryId;
	}
	public void setAuxiliaryId(Long auxiliaryId) {
		this.auxiliaryId = auxiliaryId;
	}
	public Long getBlockId() {
		return blockId;
	}
	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}
	public Long getAuxiliaryTypeId() {
		return auxiliaryTypeId;
	}
	public void setAuxiliaryTypeId(Long auxiliaryTypeId) {
		this.auxiliaryTypeId = auxiliaryTypeId;
	}
	public String getAuxiliaryCode() {
		return auxiliaryCode;
	}
	public void setAuxiliaryCode(String auxiliaryCode) {
		this.auxiliaryCode = auxiliaryCode;
	}
	public String getAuxiliaryName() {
		return auxiliaryName;
	}
	public void setAuxiliaryName(String auxiliaryName) {
		this.auxiliaryName = auxiliaryName;
	}
	public String getAuxiliaryModel() {
		return auxiliaryModel;
	}
	public void setAuxiliaryModel(String auxiliaryModel) {
		this.auxiliaryModel = auxiliaryModel;
	}
	public String getUpdateNo() {
		return updateNo;
	}
	public void setUpdateNo(String updateNo) {
		this.updateNo = updateNo;
	}
	public Date getStartProDate() {
		return startProDate;
	}
	public void setStartProDate(Date startProDate) {
		this.startProDate = startProDate;
	}
	public Date getStopStatDate() {
		return stopStatDate;
	}
	public void setStopStatDate(Date stopStatDate) {
		this.stopStatDate = stopStatDate;
	}
	public Date getLeaveFactoryDate() {
		return leaveFactoryDate;
	}
	public void setLeaveFactoryDate(Date leaveFactoryDate) {
		this.leaveFactoryDate = leaveFactoryDate;
	}
	public Date getStatDate() {
		return statDate;
	}
	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}
	public Date getStopUseDate() {
		return stopUseDate;
	}
	public void setStopUseDate(Date stopUseDate) {
		this.stopUseDate = stopUseDate;
	}
	public String getLeaveFactoryNo() {
		return leaveFactoryNo;
	}
	public void setLeaveFactoryNo(String leaveFactoryNo) {
		this.leaveFactoryNo = leaveFactoryNo;
	}
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	public String getProduceFactory() {
		return produceFactory;
	}
	public void setProduceFactory(String produceFactory) {
		this.produceFactory = produceFactory;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getStartProDateString() {
		return startProDateString;
	}
	public void setStartProDateString(String startProDateString) {
		this.startProDateString = startProDateString;
	}
	public String getStopStatDateString() {
		return stopStatDateString;
	}
	public void setStopStatDateString(String stopStatDateString) {
		this.stopStatDateString = stopStatDateString;
	}
	public String getLeaveFactoryDateString() {
		return leaveFactoryDateString;
	}
	public void setLeaveFactoryDateString(String leaveFactoryDateString) {
		this.leaveFactoryDateString = leaveFactoryDateString;
	}
	public String getStatDateString() {
		return statDateString;
	}
	public void setStatDateString(String statDateString) {
		this.statDateString = statDateString;
	}
	public String getStopUseDateString() {
		return stopUseDateString;
	}
	public void setStopUseDateString(String stopUseDateString) {
		this.stopUseDateString = stopUseDateString;
	}
}