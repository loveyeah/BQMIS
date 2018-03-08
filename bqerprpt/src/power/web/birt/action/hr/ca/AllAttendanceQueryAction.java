/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.hr.ca;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import power.ejb.hr.ca.AllAttendanceQueryFacadeRemote;
import power.ejb.hr.ca.MetaData;
import power.ejb.hr.ca.StoreObject;
import power.ejb.hr.ca.TimeKeeperExamineForm;
import power.web.birt.bean.hr.ca.AllAttendanceQueryBean;
import power.web.birt.bean.hr.ca.AllAttendanceQueryDetailBean;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;

/**
 * 职工考勤记录表Action
 * 
 * @author zhujie
 * 
 */
public class AllAttendanceQueryAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 配置文件名 */
	private static final String CONFIG_FILE_NAME = "colorAndsharpConfig.properties";
	/** property文件 */
	public static Properties props = null;
	/** 定义ejb接口 */
	public AllAttendanceQueryFacadeRemote remote;
	/** 日期列的长度 */
	private static final int COLUMN_DATE_LENGTH = 34;
	/** 合计项列的长度 */
	private static final int COLUMN_COUNT_LENGTH = 11;
	/** 员工姓名截断字节数 */
	private static final int TEN = 10;
	/** 部门名称截断字节数 */
	private static final int TWENTY = 20;
	/** 审计部门截断字节数 */
	private static final int THIRTY = 30;
	/** 符号截断字节数 */
	private static final int FIVE = 5;
	/** HTML字体样式标签 */
	private static final String HTML_FONT = "</font>";
	/** HTML换行符 */
	private static final String HTML_BREAK = "<br />周";
	/** 反尖括号 */
	private static final String FANKUOHAO = ">";
	/** 报表日期（阿拉伯数字） */
    private static final String FLAG_RPT = "RPT";
    /** 方法名：setColumn */
    private static final String METHOD_SETCOLUMN = "setColumn";
    /** 格式化天数 */
    private static final String FORMAT_DAYS = "###,###,###,###,##0";
    /** 合计项为空且单位为（天）时补上0 */
    private static final String BLACK_DAYS = "0";
    /** 表头：行号 */
    private static final String LINT_NUMBER = "行号";
    /** MAP里的参数：header */
    private static final String PRAMETER_HEADER = "header";
    /** MAP里的参数：name */
    private static final String PRAMETER_NAME = "name";

	/**
	 * 构造函数
	 */
	public AllAttendanceQueryAction() {
		remote = (AllAttendanceQueryFacadeRemote) factory
				.getFacadeRemote("AllAttendanceQueryFacade");
	}

	/**
	 * 获取职工考勤记录表信息
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public AllAttendanceQueryBean getAllAttendanceQuery(String examineDate,
			String examineDept, String enterpriseCode,String empDeptId) throws Exception {
		// 加载配置文件
		InputStream fileStream = AllAttendanceQueryAction.class
				.getResourceAsStream(CONFIG_FILE_NAME);
		props = new Properties();
		props.load(fileStream);
		AllAttendanceQueryBean entity = new AllAttendanceQueryBean();
		List<AllAttendanceQueryDetailBean> lstDetail = new ArrayList<AllAttendanceQueryDetailBean>();
		// 获得需要打印的内容
		TimeKeeperExamineForm bossForm = remote.getExamineInfo(examineDate,
				examineDept, enterpriseCode, props,empDeptId,FLAG_RPT);
		// 列名中日期部分
		List<String> lstDateHead = new ArrayList<String>();
		// 列名中合计项部分
		List<String> lstCountHead = new ArrayList<String>();
		// 日期列的长度
		int intDateLength = 0;
		// 合计项列的长度
		int intCountLength = 0;
		// 休假时字体的颜色
		String color = "";
		// 判断是否已给color赋值
		boolean flag = false;
		// 数字输出格式化
		DecimalFormat dfDays = new DecimalFormat(FORMAT_DAYS);

		// 从formBean中取得列头信息
		StoreObject sObj = bossForm.getStore();
		MetaData metaData = sObj.getMetaData();
		// 每列标题头
		List<String> lstHeader = new ArrayList<String>();
		List<Map<String, Object>> fields = metaData.getFields();
		List<String> nameList = new ArrayList();
		Iterator itFields = fields.iterator();
		// 添加第一列：行号
		lstHeader.add(LINT_NUMBER);
		while (itFields.hasNext()) {
			Map<String, Object> mapHeader = (HashMap<String, Object>) itFields
					.next();
			String header = (String) mapHeader.get(PRAMETER_HEADER);
			String names = (String) mapHeader.get(PRAMETER_NAME);
			nameList.add(names);
			if (null != header) {
				// 把名字放进列头
				lstHeader.add(header);
			}
		}
		// 将Table列名的数据分开为日期数据和合计项数据两部分
		if (lstHeader != null && lstHeader.size() > 0) {
			for (int i = 0; i < lstHeader.size(); i++) {
				if (i < 3) {
					lstDateHead.add(lstHeader.get(i));
				} else {
					// 日期列
					if (commUtils.countSubStringNumber(lstHeader.get(i),
							HTML_BREAK)>0) {
						// 取得字体颜色
						if (flag == false
								&& commUtils.countSubStringNumber(lstHeader
										.get(i), HTML_FONT) > 0) {
							color = copyColor(lstHeader.get(i));
							flag = true;
						}
						lstDateHead.add(lstHeader.get(i));
					    // 合计项列   
					} else {
						lstCountHead.add(lstHeader.get(i));
					}
				}
			}
		}
		// 给日期列列名赋值
		if (lstDateHead != null && lstDateHead.size() > 0) {
			intDateLength = lstDateHead.size();
			entity.setColumnCount(intDateLength);
			Class beanClass = AllAttendanceQueryBean.class;
			Method method;
			for (int i = 0; i < Math
					.min(lstDateHead.size(), COLUMN_DATE_LENGTH); i++) {
				method = beanClass.getMethod(METHOD_SETCOLUMN + (i + 1),
						String.class);
				method.invoke(entity, lstDateHead.get(i));
			}
		}
		// 给合计项列列名赋值
		if (lstCountHead != null && lstCountHead.size() > 0) {
			intCountLength = lstCountHead.size();
			entity.setCountColumnLength(intCountLength);
			Class beanClass = AllAttendanceQueryBean.class;
			Method method;
			for (int i = 0; i < Math.min(lstCountHead.size(),
					COLUMN_COUNT_LENGTH); i++) {
				method = beanClass.getMethod(METHOD_SETCOLUMN + (i + 35),
						String.class);
				method.invoke(entity, lstCountHead.get(i));
			}
		}
		// 打印文件中的所有行集合
		List lstRowSet = new ArrayList();
		lstRowSet = sObj.getList();
		// 打印文件单行实体
		Iterator itRow = lstRowSet.iterator();
		int rowNo = 0;
		while (itRow.hasNext()) {
			int colNo = 0;
			Map<Object, Object> cell = new HashMap<Object, Object>();
			cell = (Map<Object, Object>) itRow.next();
			// Table的Body中合计项部分
			List<String> lstCountBody = new ArrayList<String>();
			// Table的Body部分
			List<String> lstBody = new ArrayList<String>();
			// Table的Body中日期部分
			List<String> lstDateBody = new ArrayList<String>();
			// 设置行号
			lstBody.add(rowNo + 1 + "");
			// 取得Table中Body部分的数据
			for (colNo = 0; colNo < Math.min(nameList.size(),
					COLUMN_DATE_LENGTH + COLUMN_COUNT_LENGTH + 3); colNo++) {
				if (0 == colNo || 2 == colNo || 4 == colNo || 5 == colNo)
					continue;
				if (null == cell.get(nameList.get(colNo))) {
					lstBody.add("");
				} else {
					if (String.class == cell.get(nameList.get(colNo))
							.getClass()) {
						lstBody.add((String) cell.get(nameList.get(colNo)));
					} else {
						lstBody.add(String.valueOf(cell
								.get(nameList.get(colNo))));
					}
				}
			}
			// 将Table的数据分开为日期数据和合计项数据两部分
			if (lstBody != null && lstBody.size() > 0) {
				for (int i = 0; i < lstBody.size(); i++) {
					if (i < intDateLength) {
						lstDateBody.add(lstBody.get(i));
					} else {
						lstCountBody.add(lstBody.get(i));
					}
				}
			}
			// 给Table赋值
			AllAttendanceQueryDetailBean detailBean = new AllAttendanceQueryDetailBean();
			// 计算每行高度用的List
			List<String> lstCountMax = new ArrayList();
			if (intDateLength > 0) {
				Class beanClass = AllAttendanceQueryDetailBean.class;
				Method method;
				// 给日期列赋值
				for (int i = 0; i < Math.min(intDateLength, COLUMN_DATE_LENGTH); i++) {
					method = beanClass.getMethod(METHOD_SETCOLUMN + (i + 1),
							String.class);
					if (i == 1) {
						// 员工姓名（截断前10字节）
						method.invoke(detailBean, commUtils.cutByByte(
								lstDateBody.get(i), TEN));
					} else if (i == 2) {
						// 部门名称
						String dept = commUtils.addBrByByteLengthForHR(
								lstDateBody.get(i), TWENTY);
						method.invoke(detailBean, dept.replace(Constant.BLANK,
								Constant.MARK_BLACK));
						lstCountMax.add(dept);
					} else {
						// 出勤标志同时添加字体颜色
						String mark = commUtils.addBrByByteLengthForHR(
								lstDateBody.get(i), FIVE);
						lstCountMax.add(mark);
						mark = mark
								.replace(Constant.BLANK, Constant.MARK_BLACK);
						if (commUtils.countSubStringNumber(lstDateHead.get(i),
								HTML_FONT) > 0) {
							mark = color + mark + HTML_FONT;
						}
						method.invoke(detailBean, mark);
					}
					// 行高度
					detailBean.setCntRow(commUtils.countMaxContain(lstCountMax,
							Constant.HTML_CHANGE_LINE) + 1);
				}
			}
			if (intCountLength > 0) {
				Class beanClass = AllAttendanceQueryDetailBean.class;
				Method method;
				for (int i = 0; i < Math.min(intCountLength,
						COLUMN_COUNT_LENGTH); i++) {
					method = beanClass.getMethod(METHOD_SETCOLUMN + (i + 35),
							String.class);
					if (lstCountBody.get(i) != null
							&& !"".equals(lstCountBody.get(i))) {
						method.invoke(detailBean, dfDays.format(Double
								.parseDouble(lstCountBody.get(i))));
					}else{
						// 单位为天,合计项为空时补上0.00时
						method.invoke(detailBean, BLACK_DAYS);
					}
				}
			}
			lstDetail.add(detailBean);
			rowNo++;
		}
		entity.setList(lstDetail);
		// 审核部门
		entity.setCheckDept(commUtils.cutByByte(bossForm
				.getStrExamineDeptName(), THIRTY));
		// 审核员
		entity.setCheckMan(commUtils.cutByByte(bossForm.getStrExamine(), TEN));
		// 考勤员
		entity.setAttendentMan(commUtils.cutByByte(bossForm.getStrAttendance(),
				TEN));
		return entity;
	}

	/**
	 * 取得目标字符串的颜色的HTML标签
	 * @param String strTarget 目标字符串
	 * @return String
	 */
	public String copyColor(String strTarget) {
		int index = 0;
		if (strTarget != null && !"".equals(strTarget)) {
			index = Math.max(strTarget.indexOf(FANKUOHAO), 0);
		}
		if (index > 0) {
			return strTarget.substring(0, index + 1);
		} else {
			return "";
		}
	}
}
