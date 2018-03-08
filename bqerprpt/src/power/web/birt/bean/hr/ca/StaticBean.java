/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.bean.hr.ca;

import java.util.ArrayList;
import java.util.List;
import power.ejb.hr.ca.StaticDetailBean;

/**
 * (请假、加班、运行班)统计报表Bean
 * @author zhujie
 *
 */
public class StaticBean {

	/** (请假、加班、运行班)统计报表数据 */
	private List<StaticDetailBean> list =new ArrayList<StaticDetailBean>();
	/** (请假、加班、运行班)统计报表标题 */
	private String title = "";
	/** CrossTab及Table的显示状态 */
	private String useFlag  = "0";
	/** 列的长度 */
	private int columnCount  = 0;
	/** Table第1列列名 */
	private String column1  = "";
	/** Table第2列列名 */
	private String column2  = "";
	/** Table第3列列名 */
	private String column3  = "";
	/** Table第4列列名 */
	private String column4  = "";
	/** Table第5列列名 */
	private String column5  = "";
	/** Table第6列列名 */
	private String column6  = "";
	/** Table第7列列名 */
	private String column7  = "";
	/** Table第8列列名 */
	private String column8  = "";
	/** Table第9列列名 */
	private String column9  = "";
	/** Table第10列列名 */
	private String column10  = "";
	/** Table第11列列名 */
	private String column11  = "";
	/** Table第12列列名 */
	private String column12  = "";
	/** Table第13列列名 */
	private String column13  = "";
	/** Table第14列列名 */
	private String column14  = "";
	/** Table第15列列名 */
	private String column15  = "";
	
	/**
	 * @return the list
	 */
	public List<StaticDetailBean> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<StaticDetailBean> list) {
		this.list = list;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the useFlag
	 */
	public String getUseFlag() {
		return useFlag;
	}
	/**
	 * @param useFlag the useFlag to set
	 */
	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}
	/**
	 * @return the column1
	 */
	public String getColumn1() {
		return column1;
	}
	/**
	 * @param column1 the column1 to set
	 */
	public void setColumn1(String column1) {
		this.column1 = column1;
	}
	/**
	 * @return the column2
	 */
	public String getColumn2() {
		return column2;
	}
	/**
	 * @param column2 the column2 to set
	 */
	public void setColumn2(String column2) {
		this.column2 = column2;
	}
	/**
	 * @return the column3
	 */
	public String getColumn3() {
		return column3;
	}
	/**
	 * @param column3 the column3 to set
	 */
	public void setColumn3(String column3) {
		this.column3 = column3;
	}
	/**
	 * @return the column4
	 */
	public String getColumn4() {
		return column4;
	}
	/**
	 * @param column4 the column4 to set
	 */
	public void setColumn4(String column4) {
		this.column4 = column4;
	}
	/**
	 * @return the column5
	 */
	public String getColumn5() {
		return column5;
	}
	/**
	 * @param column5 the column5 to set
	 */
	public void setColumn5(String column5) {
		this.column5 = column5;
	}
	/**
	 * @return the column6
	 */
	public String getColumn6() {
		return column6;
	}
	/**
	 * @param column6 the column6 to set
	 */
	public void setColumn6(String column6) {
		this.column6 = column6;
	}
	/**
	 * @return the column7
	 */
	public String getColumn7() {
		return column7;
	}
	/**
	 * @param column7 the column7 to set
	 */
	public void setColumn7(String column7) {
		this.column7 = column7;
	}
	/**
	 * @return the column8
	 */
	public String getColumn8() {
		return column8;
	}
	/**
	 * @param column8 the column8 to set
	 */
	public void setColumn8(String column8) {
		this.column8 = column8;
	}
	/**
	 * @return the column9
	 */
	public String getColumn9() {
		return column9;
	}
	/**
	 * @param column9 the column9 to set
	 */
	public void setColumn9(String column9) {
		this.column9 = column9;
	}
	/**
	 * @return the column10
	 */
	public String getColumn10() {
		return column10;
	}
	/**
	 * @param column10 the column10 to set
	 */
	public void setColumn10(String column10) {
		this.column10 = column10;
	}
	/**
	 * @return the column11
	 */
	public String getColumn11() {
		return column11;
	}
	/**
	 * @param column11 the column11 to set
	 */
	public void setColumn11(String column11) {
		this.column11 = column11;
	}
	/**
	 * @return the column12
	 */
	public String getColumn12() {
		return column12;
	}
	/**
	 * @param column12 the column12 to set
	 */
	public void setColumn12(String column12) {
		this.column12 = column12;
	}
	/**
	 * @return the column13
	 */
	public String getColumn13() {
		return column13;
	}
	/**
	 * @param column13 the column13 to set
	 */
	public void setColumn13(String column13) {
		this.column13 = column13;
	}
	/**
	 * @return the column14
	 */
	public String getColumn14() {
		return column14;
	}
	/**
	 * @param column14 the column14 to set
	 */
	public void setColumn14(String column14) {
		this.column14 = column14;
	}
	/**
	 * @return the column15
	 */
	public String getColumn15() {
		return column15;
	}
	/**
	 * @param column15 the column15 to set
	 */
	public void setColumn15(String column15) {
		this.column15 = column15;
	}
	/**
	 * @return the columnCount
	 */
	public int getColumnCount() {
		return columnCount;
	}
	/**
	 * @param columnCount the columnCount to set
	 */
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

}
