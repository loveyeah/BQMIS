/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.util.List;

/**
 * 物料盘点表打印
 * @author zhujie 
 */
public class MaterialCheckPrintReportBean {

	/** 制单人 */
    private String createMan="";
    /** 当前日期 */
    private String nowDate="";
    /** 盘点损益表数据 */
    private List<MaterialCheckPrintReportListBean> materialCheckPrintReportList;
	/**
	 * @return the createMan
	 */
	public String getCreateMan() {
		return createMan;
	}
	/**
	 * @param createMan the createMan to set
	 */
	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}
	/**
	 * @return the nowDate
	 */
	public String getNowDate() {
		return nowDate;
	}
	/**
	 * @param nowDate the nowDate to set
	 */
	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}
	/**
	 * @return the materialCheckPrintReportList
	 */
	public List<MaterialCheckPrintReportListBean> getMaterialCheckPrintReportList() {
		return materialCheckPrintReportList;
	}
	/**
	 * @param materialCheckPrintReportList the materialCheckPrintReportList to set
	 */
	public void setMaterialCheckPrintReportList(
			List<MaterialCheckPrintReportListBean> materialCheckPrintReportList) {
		this.materialCheckPrintReportList = materialCheckPrintReportList;
	}
}
