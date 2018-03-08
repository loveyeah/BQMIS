/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.hr.ca;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ca.HrJVacationByW;
import power.ejb.hr.ca.HrJVacationFacadeRemote;
import power.web.birt.bean.hr.ca.VacationRegisterQueryBean;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;

/**
 * 请假登记表报表Action
 * @author zhaozhijie
 *
 */
public class VacationRegisterQueryAction  extends AbstractAction {

	   /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** 远程 */
    private HrJVacationFacadeRemote remote;
    /** 员工姓名截断字节数 */
    private int TEN = 10;
    /** 9%截断字节数 */
    private int NINE_TYNINE = 17;
    /** 8%截断字节数 */
    private int EIGHT_TYNINE = 15;
	/** 天数格式化 */
	private static final String PATTERN_NUMBER_DAY = "##############0.00";
	/** 时长格式化 */
	private static final String PATTERN_NUMBER_TIME = "##############0.0";
    /** 常量&nbsp; */
    public static final String SEPARATOR_NBSP = "&nbsp;"; 
    /** 常量  */
    public static final String SEPARATOR_BLANK = " "; 
    /** 是否销假0（否）*/
    private String IFCLEAR_0 = "0";
    private String IFCLEAR_NO = "否";
    /** 是否销假1(是）*/
    private String IFCLEAR_1 = "1";
    private String IFCLEAR_YES = "是";
    /** 未上报 */
    private String NOT_REPORT_MESSAGE = "未上报";
    private String NOT_REPORT = "0";
	/** 已上报 */
	private String ALREADY_REPORT_MESSAGE = "已上报";
	private String ALREADY_REPORT = "1";
	/** 已终结 */
	private String ALREADY_OVER_MESSAGE = "已终结";
	private String ALREADY_OVER = "2";
	/** 已退回 */
	private String ALREADY_RETURN_MESSAGE = "已退回";
	private String ALREADY_RETURN = "3";

    /**
     * 构造函数
     */
    public VacationRegisterQueryAction() {
        remote = (HrJVacationFacadeRemote) factory.getFacadeRemote("HrJVacationFacade");
    }

    /**
	 * 请假登记查询
	 * @param argFromDate 开始时间
	 * @param argToDate 结束时间
	 * @param argEnterpriseCode 企业编码
	 * @return DeptOndutyStatisticsBean
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public VacationRegisterQueryBean getVacationRegisterQuery(
			String argFromDate, String argToDate, String argEnterpriseCode)
			throws SQLException {
		VacationRegisterQueryBean entity = new VacationRegisterQueryBean();
		PageObject pg = new PageObject();
		List<HrJVacationByW> result = new ArrayList<HrJVacationByW>();
		// 数字输出格式化
		String patternNumber = PATTERN_NUMBER_DAY;
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
		String patternNum = PATTERN_NUMBER_TIME;
		DecimalFormat dfNum = new DecimalFormat(patternNum);
		pg = remote.getAllVacations(argEnterpriseCode, argFromDate, argToDate);
		if (pg != null) {
			result = pg.getList();
		}
		// 对信息进行处理
		if (result != null && result.size() > 0) {
			HrJVacationByW hrJVacationByW = null;
			for (int i = 0; i < result.size(); i++) {
				List<String> lstString = new ArrayList();
				hrJVacationByW = result.get(i);
				// 对员工姓名进行截断前10个字节数处理
				if (hrJVacationByW.getChsName() != null
						&& hrJVacationByW.getChsName().getBytes().length > TEN) {
					hrJVacationByW.setChsName(commUtils.cutByByte(
							hrJVacationByW.getChsName(), TEN));
				}
				// 对部门名称进行换行处理
				String deptName = commUtils.addBrByByteLengthForHR(hrJVacationByW
						.getDeptName(), EIGHT_TYNINE);
				hrJVacationByW.setDeptName(deptName.replace(SEPARATOR_BLANK, SEPARATOR_NBSP));
				lstString.add(deptName);
				// 对请假原因进行换行处理
				String reason = commUtils.addBrByByteLengthForHR(hrJVacationByW
						.getReason(), NINE_TYNINE);
				hrJVacationByW.setReason(reason.replace(SEPARATOR_BLANK, SEPARATOR_NBSP));
				lstString.add(reason);
				// 对去向进行换行处理
				String whither = commUtils.addBrByByteLengthForHR(hrJVacationByW
						.getWhither(), NINE_TYNINE);
				hrJVacationByW.setWhither(whither.replace(SEPARATOR_BLANK, SEPARATOR_NBSP));
				lstString.add(whither);
				// 对备注进行换行处理
				String memo = commUtils.addBrByByteLengthForHR(hrJVacationByW
						.getMemo(), NINE_TYNINE);
				hrJVacationByW.setMemo(memo.replace(SEPARATOR_BLANK, SEPARATOR_NBSP));
				lstString.add(memo);
				// 对请假时长进行处理
				String vacationHours = hrJVacationByW.getVacationTime();
				if (vacationHours != null && !"".equals(vacationHours)) {
					vacationHours = dfNum.format(Double.parseDouble(vacationHours));
				}
				hrJVacationByW.setVacationTime(vacationHours);
				// 对请假天数进行处理
				String vacationDays = hrJVacationByW.getVacationDays();
				if (vacationDays != null && !"".equals(vacationDays)) {
					vacationDays = dfNumber.format(Double.parseDouble(vacationDays));
				}
				hrJVacationByW.setVacationDays(vacationDays);
				// 对审批状态进行处理
				String signState = hrJVacationByW.getSignState();
				// 未上报
				if (NOT_REPORT.equals(signState)) {
					hrJVacationByW.setSignState(NOT_REPORT_MESSAGE);
					// 已上报
				} else if (ALREADY_REPORT.equals(signState)) {
					hrJVacationByW.setSignState(ALREADY_REPORT_MESSAGE);
					// 已终结
				} else if (ALREADY_OVER.equals(signState)) {
					hrJVacationByW.setSignState(ALREADY_OVER_MESSAGE);
					// 已退回
				} else if (ALREADY_RETURN.equals(signState)) {
					hrJVacationByW.setSignState(ALREADY_RETURN_MESSAGE);
				}
				// 对是否销假进行处理
				String ifClear = hrJVacationByW.getIfClear();
				if (IFCLEAR_0.equals(ifClear)) {
					hrJVacationByW.setIfClear(IFCLEAR_NO);
				} else if (IFCLEAR_1.equals(ifClear)) {
					hrJVacationByW.setIfClear(IFCLEAR_YES);
				}
				// 叠加后的计数
				hrJVacationByW.setCntRow(commUtils.countMaxContain(lstString,
						Constant.HTML_CHANGE_LINE) + 1);
			}
			entity.setList(result);
		}
		return entity;
	}
}
