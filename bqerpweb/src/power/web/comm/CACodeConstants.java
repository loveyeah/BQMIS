/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.comm;

/**
 * 共通常量类
 *
 * @author qzhang
 * @version 1.0
 */
public class CACodeConstants {

   
    /** 出勤类别 (出勤) */
    public static final String ATTENDANCE_TYPE_WORK = "0";

    /** 出勤类别 (休息) */
    public static final String ATTENDANCE_TYPE_REST = "1";
    /** 出勤类别 (旷工) */
    public static final String ATTENDANCE_TYPE_ABSENT_WORK = "2";

    /** 出勤类别 (迟到) */
    public static final String ATTENDANCE_TYPE_LATE_WORK = "3";

    /** 出勤类别 (早退) */
    public static final String ATTENDANCE_TYPE_LEAVE_EARLY = "4";

    /** 出勤类别 (外借) */
    public static final String ATTENDANCE_TYPE_OUT_WORK = "5";

    /** 出勤类别 (出差) */
    public static final String ATTENDANCE_TYPE_EVECTION = "6";

    /** 签字状态 (未上报) */
    public static final String SIGN_STATE_UNREPORT = "0";

    /** 签字状态 (已上报) */
    public static final String SIGN_STATE_REPORTED = "1";

    /** 签字状态 (已终结) */
    public static final String SIGN_STATE_COMPLETE = "2";

    /** 签字状态 (已退回) */
    public static final String SIGN_STATE_BACK = "3";

    /** 考勤部门类别 (考勤部门) */
    public static final String ATTEND_DEP_TYPE_ATTEND = "1";

    /** 考勤部门类别 (考勤审核部门) */
    public static final String ATTEND_DEP_TYPE_ATTEND_CHECKE = "2";

    /** 考勤部门类别 (代考勤部门) */
    public static final String ATTEND_DEP_TYPE_PROXY_ATTEND = "3";

    /** 节假日类别 (非周末休息日期) */
    public static final String HOLIDAY_TYPE_WEEKDAY_REST = "1";

    /** 节假日类别 (周末上班日期) */
    public static final String HOLIDAY_TYPE_WEEKEND_WORK = "2";

    /** 考勤合计项类型 (加班) */
    public static final String STAT_ITEM_TYPE_OVERTIME = "1";

    /** 考勤合计项类型 (运行班) */
    public static final String STAT_ITEM_TYPE_RUN = "2";

    /** 考勤合计项类型 (请假) */
    public static final String STAT_ITEM_TYPE_VACATIONID = "3";

    /** 考勤合计项类型 (出勤) */
    public static final String STAT_ITEM_TYPE_WORK = "4";

    /** 考勤单位 (天) */
    public static final String ATTEND_UNIT_DAY = "1";

    /** 考勤单位 (小时) */
    public static final String ATTEND_UNIT_HOUR = "2";

    /** 合计项是否使用标志 (是) */
    public static final String USE_FLG_YES = "1";

    /** 合计项是否使用标志 (否) */
    public static final String USE_FLG_NO = "0";

    /** 是否设置周期 (是) */
    public static final String CYCLE_FLG_YES = "1";

    /** 是否设置周期 (否) */
    public static final String CYCLE_FLG_NO = "0";

    /** 是否包含周末 (是) */
    public static final String INCLUDE_WEEKEND_FLG_YES = "1";

    /** 是否包含周末 (否) */
    public static final String INCLUDE_WEEKEND_FLG_NO = "0";

    /** 迟到FLG (是) */
    public static final String LATE_FLG_YES = "1";

    /** 迟到FLG (否) */
    public static final String LATE_FLG_NO = "0";

    /** 早退FLG (是) */
    public static final String LEAVE_EARLY_FLG_YES = "1";

    /** 早退FLG (否) */
    public static final String LEAVE_EARLY_FLG_NO = "0";

    /** 休息FLG (是) */
    public static final String REST_FLG_YES = "1";

    /** 休息FLG (否) */
    public static final String REST_FLG_NO = "0";

    /** 旷工FLG (是)  */
    public static final String ABSENT_WORK_FLG_YES = "1";

    /** 旷工FLG (否) */
    public static final String ABSENT_WORK_FLG_NO = "0";

    /** 外借FLG (是) */
    public static final String OUT_WORK_FLG_YES = "1";

    /** 外借FLG (否) */
    public static final String OUT_WORK_FLG_NO = "0";

    /** 出差FLG (是) */
    public static final String EVECTION_FLG_YES = "1";

    /** 出差FLG (否) */
    public static final String EVECTION_FLG_NO = "0";

    /** 是否销假 (是) */
    public static final String IF_CLEAR_YES = "1";

    /** 是否销假 (否) */
    public static final String IF_CLEAR_NO = "0";

    /** 是否发放费用 (是) */
    public static final String IF_OVERTIME_FEE_YES = "1";

    /** 是否发放费用 (否) */
    public static final String IF_OVERTIME_FEE_NO = "0";
}
