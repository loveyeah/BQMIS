/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.DataChangeException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 考勤员审核方法体
 * 
 * @author zhouxu
 */
@Stateless
public class TimeKeeperExamineFacade implements TimeKeeperExamineFacadeRemote {
    @Resource
    SessionContext ctx;
    /** db操作类接口 */
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    /** 考勤登记表操作类接口 */
    @EJB(beanName = "HrJWorkattendanceFacade")
    protected HrJWorkattendanceFacadeRemote empAttendanceManager;
    /** 考勤审核表操作类接口 */
    @EJB(beanName = "HrJAttendancecheckFacade")
    protected HrJAttendancecheckFacadeRemote attendanceCheckRemote;
    /** 加班统计表操作类接口 */
    @EJB(beanName = "HrDOvertimetotalFacade")
    protected HrDOvertimetotalFacadeRemote overTimeTotalManager;
    /** 运行班统计表操作类接口 */
    @EJB(beanName = "HrDWorkshifttotalFacade")
    protected HrDWorkshifttotalFacadeRemote workShiftTotalManager;
    /** 请假统计表操作接口 */
    @EJB(beanName = "HrDVacationtotalFacade")
    protected HrDVacationtotalFacadeRemote vacationTotalManager;
    /** 出勤统计表操作接口 */
    @EJB(beanName = "HrDWorkdaysFacade")
    protected HrDWorkdaysFacadeRemote workDaysTotalManager;
    /** 是否使用: 是 */
    private static final String IS_USE_Y = "Y";
    /** 标识: 0 */
    private static final String FLAG_0 = "0";
    /** 标识: 1 */
    private static final String FLAG_1 = "1";
    /** 标识: 2 */
    private static final String FLAG_2 = "2";
    /** flag: 是 */
    private static final String FLAG_Y = "Y";
    /** flag: 否 */
    private static final String FLAG_N = "N";
    /** 审核部门类别 */
    private static final String ATTEND_DEP_TYPE_2 = "2";
    /** 日期格式 */
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    /** 时间格式 */
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 空字符串 */
    private static final String BLANK_STRING = "";
    /** 没有结果抛出错误类型 */
    private static final String NO_RESULT_N = "N";
    /** 日期列dataindex标识符 */
    private static final String DATE_DATAINDEX = "D";
    /** 合计项列dataindex标识符 */
    private static final String TOTAL_DATAINDEX = "C";
    /** 未审核部门分隔符 */
    private static final String UNCHECK_CUT = ",";
    /** 日期列开始位置 */
    private static final int DATE_START_LOCAL = 6;
    /** 员工信息列宽度 */
    private static final int WIDTH_EMP_INFO = 100;
    /** 日期列宽度 */
    private static final int WIDTH_DATE = 75;
    /** 合计项列宽度 */
    private static final int WIDTH_TOTAL = 95;

    /** 开始日期结束日期集 */
    private List<Map<String, Object>> dayFields = new ArrayList<Map<String, Object>>();
    /** 合计项表头集 */
    private List<HrCStatitem> totalNameList = new ArrayList<HrCStatitem>();

    @SuppressWarnings("unchecked")
    public TimeKeeperExamineForm getExamineInfo(String examineDate, String argDeptId, String argEnterpriseCode,
            Properties p) throws Exception {
        LogUtil.log("EJB:考勤员审核查询开始", Level.INFO, null);
        try {
            TimeKeeperExamineForm dataAll = new TimeKeeperExamineForm();
            StoreObject obj = new StoreObject();
            // 初始化画面为未审核
            dataAll.setCheckFlag(FLAG_N);
            String strYear = examineDate.substring(0, 4);
            String strMonth = examineDate.substring(5, 7);
            String strStartDate = BLANK_STRING;
            String strEndDate = BLANK_STRING;
            // 标准出勤时间
            String standardTime = BLANK_STRING;
            // 查询该父部门下所有子部门的考勤信息
            List<TimeKeeperExamine> arrlist = gridMainList(strYear, strMonth, argEnterpriseCode, argDeptId);
            Long totalCount = 0L;
            if (arrlist != null) {
                totalCount = (long) arrlist.size();
            }
            // 如果查询有结果
            if (arrlist.size() > 0) {
                // 所有的包括父部门子部门查询信息
                PageObject pObj = new PageObject();
                // 主store的数据构造
                pObj.setList(arrlist);
                pObj.setTotalCount((long) arrlist.size());
                dataAll.setDeptAllStore(pObj);
                // 获取审核部门
                String tempDept = arrlist.get(0).getExamineADept();
                // 设置审核人
                dataAll.setStrExamine(arrlist.get(0).getMan2ChsName());
                // 设置考勤员
                dataAll.setStrAttendance(arrlist.get(0).getMan1ChsName());
                // 设置审核部门
                dataAll.setStrExamineDeptName(arrlist.get(0).getExamineBDeptName());
                // 设置审核部门ID
                dataAll.setStrExamineDeptId(arrlist.get(0).getExamineBDeptId());
                List<String> detpList = new ArrayList<String>();
                for (int j = 0; j < arrlist.size(); j++) {
                    if (!isEmpty(arrlist.get(j).getExamineBDeptId()))
                        detpList.add(arrlist.get(j).getExamineBDeptId());
                }
                // 如果审核部门=画面的审核部门，且审核人1或审核人2不为空的场合。
                if (argDeptId.equals(tempDept)
                        && (null != arrlist.get(0).getExamineMan1() || null != arrlist.get(0).getExamineMan2())) {
                    // 画面设为已审核
                    dataAll.setCheckFlag(FLAG_Y);
                } else {
                    // 定义msg
                    String msgInfo = null;
                    // 调用本地的递归方法获取msg信息
                    msgInfo = this.checkDeptType(arrlist, argDeptId);
                    // 如果msg不为空中断操作
                    if (!isEmpty(msgInfo)) {
                        dataAll.setUnCheckDeptName(msgInfo);
                        return dataAll;
                    }
                }
                // 获取该部门该年该月的开始日期结束日期
                AttendanceStandard startAndEndDateInfo = this.getStartAndEndDate(strYear, strMonth, argDeptId,
                        BLANK_STRING, argEnterpriseCode);
                // 开始日期结束日期不为空
                if (null != startAndEndDateInfo) {
                    strStartDate = startAndEndDateInfo.getStartDate();
                    strEndDate = startAndEndDateInfo.getEndDate();
                    standardTime = startAndEndDateInfo.getStandardTime();
                    dataAll.setStrStartDate(strStartDate);
                    dataAll.setStrEndDate(strEndDate);
                    // 设置返回值的metadata
                    obj.setMetaData(creatGridHeader(strStartDate, strEndDate, argEnterpriseCode, p));
                }
                // 查询上面部门里的所有员工的考勤信息
                List<TimeKeeperExamineContent> arrListContent = allEmpAttendanceList(detpList, strStartDate,
                        strEndDate, argEnterpriseCode, p);
                List resultList = new ArrayList();
                Calendar cs = Calendar.getInstance();
                Calendar cn = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                Date s = sdf.parse(strStartDate);
                cs.setTime(s);
                // 设置天数
                int daysCount = dayFields.size();
                // 定义查询的记录集开始位置
                int i = 0;
                // 获取查询记录集数量
                int recordsCount = arrListContent.size();
                if (recordsCount == 0)
                    throw new DataChangeException(NO_RESULT_N);
                // 如果还有记录
                while (i < recordsCount) {
                    // 定义单元格
                    Map<Object, Object> cell = new HashMap<Object, Object>();
                    cell.put("empName", arrListContent.get(i).getEmpName());
                    cell.put("empId", arrListContent.get(i).getEmpId());
                    cell.put("deptName", arrListContent.get(i).getDeptName());
                    cell.put("deptId", arrListContent.get(i).getDeptId());
                    cell.put("attendanceDeptId", arrListContent.get(i).getAttendanceDeptId());
                    // 如果该员工的考勤时间为空，则添加30的空白记录
                    if (isEmpty(arrListContent.get(i).getAtendanceDate())) {
                        for (int j = 0; j < daysCount; j++) {
                            cell.put(obj.getMetaData().getFields().get(j + DATE_START_LOCAL).get("dataIndex"),
                                    BLANK_STRING);
                            countTotalItem(arrListContent.get(i), cell, totalNameList, standardTime);
                        }
                        resultList.add(cell);
                        i++;
                        continue;
                    }
                    cs.setTime(s);
                    // 以天数作循环
                    for (int j = 0; j < daysCount; j++) {
                        Date e;
                        if (i < recordsCount && !isEmpty(arrListContent.get(i).getAtendanceDate())) {
                            e = sdf.parse(arrListContent.get(i).getAtendanceDate());
                            cn.setTime(e);
                        } else {
                            cn.setTimeInMillis((long) 0);
                        }
                        // 如果有当天的记录
                        if (i < recordsCount && cs.equals(cn)
                                && cell.get("empId").equals(arrListContent.get(i).getEmpId())) {
                            countTotalItem(arrListContent.get(i), cell, totalNameList, standardTime);
                            cell.put(obj.getMetaData().getFields().get(j + DATE_START_LOCAL).get("dataIndex"),
                                    arrListContent.get(i).getMark());
                            i++;
                        } else {
                            // 没有当天的记录则插入空白
                            cell.put(obj.getMetaData().getFields().get(j + DATE_START_LOCAL).get("dataIndex"),
                                    BLANK_STRING);
                            // if(i < recordsCount)
                            // countTotalItem(arrListContent.get(i), cell,
                            // totalNameList, standardTime);
                        }
                        cs.add(Calendar.DAY_OF_MONTH, 1);
                    }

                    // 将该行记录插入整个数据集
                    resultList.add(cell);
                }

                // 设置list
                obj.setList(resultList);
                // 设置总数
                obj.setTotalCount(totalCount);
                dataAll.setStore(obj);
                dataAll.setWorkOrRestList(dayFields);
                dataAll.setStrColor(p.getProperty("COLOR"));
                LogUtil.log("EJB:考勤员审核查询结束。", Level.INFO, null);
            }
            return dataAll;
        } catch (DataChangeException e) {
            LogUtil.log("EJB:考勤员审核查询错误。子部门未审核。", Level.SEVERE, e);
            ctx.setRollbackOnly();
            throw e;
        } catch (ParseException e) {
            LogUtil.log("EJB:考勤员审核查询错误，日期转换失败。", Level.SEVERE, e);
            ctx.setRollbackOnly();
            throw e;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:没有对应的考勤日期。", Level.SEVERE, e);
            ctx.setRollbackOnly();
            throw e;
        } catch (Exception e) {
            LogUtil.log("EJB:考勤员审核查询错误。", Level.SEVERE, e);
            ctx.setRollbackOnly();
            throw e;
        }

    }

    /**
     * 根据部门id、年份、月份查询部门grid
     * 
     * @param strYear
     * @param strMonth
     * @param argEnterpriseCode
     * @param argDeptId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<TimeKeeperExamine> gridMainList(String strYear, String strMonth, String argEnterpriseCode,
            String argDeptId) throws Exception {
        try {
            LogUtil.log("EJB:根据部门id、年份、月份查询部门grid", Level.INFO, null);
            List<TimeKeeperExamine> arrlist = new ArrayList<TimeKeeperExamine>();
            StringBuilder sbd = new StringBuilder();
            sbd.append("SELECT    ");
            sbd.append("    A.ATTENDANCE_DEP,    ");
            sbd.append("    A.DEP_CHARGE1,    ");
            sbd.append("    TO_CHAR(A.CHECKED_DATE1 , 'yyyy-mm-dd'),    ");
            sbd.append("    A.DEP_CHARGE2,    ");
            sbd.append("    TO_CHAR(A.CHECKED_DATE2 , 'yyyy-mm-dd'),    ");
            sbd.append("    B.ATTEND_DEP_TYPE,    ");
            sbd.append("    B.TOP_CHECK_DEP_ID,    ");
            sbd.append("    B.ATTENDANCE_DEPT_NAME,    ");
            sbd.append("    B.ATTENDANCE_DEPT_ID,    ");
            sbd.append("    B.ATTEND_WRITER_ID,    ");
            sbd.append("    C.CHS_NAME AS MAN1,    ");
            sbd.append("    B.ATTEND_CHECKER_ID,    ");
            sbd.append("    D.CHS_NAME AS MAN2,    ");
            sbd.append("    A.ATTENDANCE_YEAR,    ");
            sbd.append("    A.ATTENDANCE_MONTH,    ");
            sbd.append("    TO_CHAR( A.LAST_MODIFIY_DATE,'yyyy-mm-dd hh24:mi:ss'),    ");
            sbd.append("    LEVEL    ");
            sbd.append("FROM    ");
            sbd.append("    HR_C_ATTENDANCEDEP B    ");
            sbd.append("    LEFT JOIN HR_J_ATTENDANCECHECK A ON A.ATTENDANCE_YEAR = ?    ");
            sbd.append("    AND A.ATTENDANCE_MONTH = ?    ");
            sbd.append("    AND A.ATTENDANCE_DEP = B.ATTENDANCE_DEPT_ID    ");
            sbd.append("    AND A.IS_USE = ?    ");
            sbd.append("    AND A.ENTERPRISE_CODE = ?    ");
            sbd.append("    LEFT JOIN HR_J_EMP_INFO C ON A.DEP_CHARGE1 = C.EMP_ID   ");
            // modify by liuyi 090916 表中无该属性 090923 9:42 去掉注释
            sbd.append("    AND C.IS_USE = ?    ");
            sbd.append("    AND C.ENTERPRISE_CODE = ?    ");
            sbd.append("    LEFT JOIN HR_J_EMP_INFO D ON A.DEP_CHARGE2 = D.EMP_ID   ");
            // modify by liuyi 090916 表中无该属性 090923 9:42 去掉注释
            sbd.append("    AND D.IS_USE = ?    ");
            sbd.append("    AND D.ENTERPRISE_CODE = ?    ");
            sbd.append("WHERE     ");
            sbd.append("    B.IS_USE = ?    ");
            sbd.append("    AND B.ENTERPRISE_CODE = ?     ");
            sbd.append("    START WITH B.ATTENDANCE_DEPT_ID = ?   ");
            sbd.append("    CONNECT BY PRIOR B.ATTENDANCE_DEPT_ID = B.TOP_CHECK_DEP_ID    ");
            // 打印SQL语句
            LogUtil.log("EJB:sql=" + sbd.toString(), Level.INFO, null);

            List<String> listParams = new ArrayList<String>();
            listParams.add(strYear);
            listParams.add(strMonth);
            listParams.add(IS_USE_Y);
            listParams.add(argEnterpriseCode);
            listParams.add(IS_USE_Y);
            listParams.add(argEnterpriseCode);
            listParams.add(IS_USE_Y);
            listParams.add(argEnterpriseCode);
            listParams.add(IS_USE_Y);
            listParams.add(argEnterpriseCode);
            listParams.add(argDeptId);
            Object[] params = listParams.toArray();
            // 查询一条有参数sql语句
            List lstResult = bll.queryByNativeSQL(sbd.toString(), params);
            Iterator it = lstResult.iterator();
            while (it.hasNext()) {
                TimeKeeperExamine tempBean = new TimeKeeperExamine();
                Object[] data = (Object[]) it.next();
                // 人员工号
                if (null != data[0]) {
                    tempBean.setExamineADept(data[0].toString());
                }
                if (null != data[1]) {
                    tempBean.setExamineMan1(data[1].toString());
                }
                if (null != data[2]) {
                    tempBean.setExamineDate1(data[2].toString());
                }
                if (null != data[3]) {
                    tempBean.setExamineMan2(data[3].toString());
                }
                if (null != data[4]) {
                    tempBean.setExamineDate2(data[4].toString());
                }
                if (null != data[5]) {
                    tempBean.setExamineType(data[5].toString());
                }
                if (null != data[6]) {
                    tempBean.setExamineDeptTop(data[6].toString());
                }
                if (null != data[7]) {
                    tempBean.setExamineBDeptName(data[7].toString());
                }
                if (null != data[8]) {
                    tempBean.setExamineBDeptId(data[8].toString());
                }
                if (null != data[9]) {
                    tempBean.setExamineRegisterMan(data[9].toString());
                }
                if (null != data[10]) {
                    tempBean.setMan1ChsName(data[10].toString());
                }
                if (null != data[11]) {
                    tempBean.setExamineMan(data[11].toString());
                }
                if (null != data[12]) {
                    tempBean.setMan2ChsName(data[12].toString());
                }
                if (null != data[13]) {
                    tempBean.setAttendanceYear(data[13].toString());
                }
                if (null != data[14]) {
                    tempBean.setAttendanceMonth(data[14].toString());
                }
                if (null != data[15]) {
                    tempBean.setLastModifiyDate(data[15].toString());
                }
                if (null != data[16]) {
                    tempBean.setLevel(Integer.parseInt(data[16].toString()));
                }

                tempBean.setCancelFlag(FLAG_N);
                arrlist.add(tempBean);
            }
            LogUtil.log("EJB:根据部门id、年份、月份查询部门grid结束", Level.INFO, null);
            return arrlist;
        } catch (Exception e) {
            LogUtil.log("EJB:根据部门id、年份、月份查询部门grid错误", Level.INFO, null);
            throw e;
        }
    }

    /**
     * 根据部门id、开始结束日期、查询所有员工考勤登记记录
     * 
     * @param deptIds
     * @param strStartDate
     * @param strEndDate
     * @param argEnterpriseCode
     * @param p
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<TimeKeeperExamineContent> allEmpAttendanceList(List<String> deptIds, String strStartDate,
            String strEndDate, String argEnterpriseCode, Properties p) throws Exception {
        LogUtil.log("EJB:根据部门id、开始结束日期、查询所有员工考勤登记记录开始", Level.INFO, null);
        try {
            List<TimeKeeperExamineContent> arrListContent = new ArrayList<TimeKeeperExamineContent>();
            StringBuilder sbdContent = new StringBuilder();
            sbdContent.append(" SELECT B.DEPT_ID    ");
            sbdContent.append("       ,B.DEPT_NAME  ");
            sbdContent.append("       ,C.EMP_ID ");
            sbdContent.append("       ,C.CHS_NAME   ");
            sbdContent.append("       ,TO_CHAR(D.ATTENDANCE_DATE,'yyyy-mm-dd')    ");
            sbdContent.append("       ,CONCAT(CONCAT(G.WORK_SHIFT_MARK,E.VACATION_MARK),F.OVERTIME_MARK) AS MARK  ");
            sbdContent.append("       ,D.REST_TYPE AS REST  ");
            sbdContent.append("       ,D.WORK AS WORK   ");
            sbdContent.append("       ,D.ABSENT_WORK AS ABSENTWORK  ");
            sbdContent.append("       ,D.EVECTION_TYPE  ");
            sbdContent.append("       ,D.OUT_WORK   ");
            sbdContent.append("       ,D.LATE_WORK  ");
            sbdContent.append("       ,D.LEAVE_EARLY    ");
            sbdContent.append("       ,D.WORK_SHIFT_ID    ");
            sbdContent.append("       ,D.VACATION_TYPE_ID    ");
            sbdContent.append("       ,D.OVERTIME_TYPE_ID    ");
            sbdContent.append("       ,C.ATTENDANCE_DEPT_ID    ");
            sbdContent.append("       ,G.WORK_SHIT_FEE    ");
            sbdContent.append("   FROM HR_C_DEPT B  ");
            sbdContent.append("       ,HR_J_EMP_INFO C  ");
            sbdContent.append("   LEFT JOIN HR_J_WORKATTENDANCE D ON C.EMP_ID = D.EMP_ID    ");
            sbdContent.append("    AND to_char(D.ATTENDANCE_DATE,'yyyy-mm-dd') >= ? ");
            sbdContent.append("    AND to_char(D.ATTENDANCE_DATE,'yyyy-mm-dd') <= ? ");
            sbdContent.append("    AND D.IS_USE = ? ");
            sbdContent.append("    AND D.ENTERPRISE_CODE = ?    ");
            sbdContent.append("   LEFT JOIN HR_C_VACATIONTYPE E ON D.VACATION_TYPE_ID = E.VACATION_TYPE_ID  ");
            sbdContent.append("    AND E.IS_USE = ? ");
            sbdContent.append("    AND E.ENTERPRISE_CODE = ?    ");
            sbdContent.append("   LEFT JOIN HR_C_OVERTIME F ON D.OVERTIME_TYPE_ID = F.OVERTIME_TYPE_ID  ");
            sbdContent.append("    AND F.IS_USE = ? ");
            sbdContent.append("    AND F.ENTERPRISE_CODE = ?    ");
            sbdContent.append("   LEFT JOIN HR_C_WORKSHIFT G ON G.WORK_SHIFT_ID = D.WORK_SHIFT_ID   ");
            sbdContent.append("    AND G.IS_USE = ? ");
            sbdContent.append("    AND G.ENTERPRISE_CODE = ?    ");
            sbdContent.append("  WHERE B.IS_USE = ? ");
            sbdContent.append("    AND B.DEPT_ID = C.DEPT_ID    ");
            sbdContent.append("    AND B.ENTERPRISE_CODE = ?    ");
            sbdContent.append("    AND C.IS_USE = ? ");
            sbdContent.append("    AND C.ENTERPRISE_CODE = ?    ");
            sbdContent.append("    AND NVL(C.ATTENDANCE_DEPT_ID,C.DEPT_ID) IN("
                    + deptIds.toString().substring(1, deptIds.toString().length() - 1) + ")  ");
            sbdContent.append("  ORDER BY B.DEPT_ID ");
            sbdContent.append("          ,C.EMP_ID ,D.ATTENDANCE_DATE    ");
            // 打印SQL语句
            LogUtil.log("EJB:sql=" + sbdContent.toString(), Level.INFO, null);
            List<String> listParamsContent = new ArrayList<String>();
            listParamsContent.add(strStartDate);
            listParamsContent.add(strEndDate);
            listParamsContent.add(IS_USE_Y);
            listParamsContent.add(argEnterpriseCode);
            listParamsContent.add(IS_USE_Y);
            listParamsContent.add(argEnterpriseCode);
            listParamsContent.add(IS_USE_Y);
            listParamsContent.add(argEnterpriseCode);
            listParamsContent.add(IS_USE_Y);
            listParamsContent.add(argEnterpriseCode);
            // modify by liuyi 090923 9:50 部门表中用U标识使用中
//            listParamsContent.add(IS_USE_Y);
            listParamsContent.add("Y");//updadte by sychen 20100831
//            listParamsContent.add("U");
            listParamsContent.add(argEnterpriseCode);
            listParamsContent.add(IS_USE_Y);
            listParamsContent.add(argEnterpriseCode);
            Object[] paramsContent = listParamsContent.toArray();
            // 查询一条有参数sql语句
            List listContent = bll.queryByNativeSQL(sbdContent.toString(), paramsContent);
            Iterator itContent = listContent.iterator();
            while (itContent.hasNext()) {
                TimeKeeperExamineContent tempBean = new TimeKeeperExamineContent();
                Object[] data = (Object[]) itContent.next();
                if (null != data[0]) {
                    tempBean.setDeptId(data[0].toString());
                }
                if (null != data[1]) {
                    tempBean.setDeptName(data[1].toString());
                }
                if (null != data[2]) {
                    tempBean.setEmpId(data[2].toString());
                }
                if (null != data[3]) {
                    tempBean.setEmpName(data[3].toString());
                }
                if (null != data[4]) {
                    tempBean.setAtendanceDate(data[4].toString());
                }
                if (null != data[5]) {
                    tempBean.setMark(data[5].toString());
                }
                if (null != data[6]) {
                    tempBean.setRest(data[6].toString());
                }
                if (null != data[7]) {
                    tempBean.setWork(data[7].toString());
                }
                if (null != data[8]) {
                    tempBean.setAbsent(data[8].toString());
                }
                if (null != data[9]) {
                    tempBean.setEvection(data[9].toString());
                }
                if (null != data[10]) {
                    tempBean.setOut(data[10].toString());
                }
                if (null != data[11]) {
                    tempBean.setLate(data[11].toString());
                }
                if (null != data[12]) {
                    tempBean.setLeave(data[12].toString());
                }
                if (null != data[13]) {
                    tempBean.setWorkShiftId(data[13].toString());
                }
                if (null != data[14]) {
                    tempBean.setVacationTypeId(data[14].toString());
                }
                if (null != data[15]) {
                    tempBean.setOvertimeTypeId(data[15].toString());
                }
                if (!isEmpty(data[16])) {
                    tempBean.setAttendanceDeptId(data[16].toString());
                } else {
                    tempBean.setAttendanceDeptId(data[0].toString());
                }
                if (null != data[17]) {
                    tempBean.setWorkShitFee(Double.parseDouble(data[17].toString()));
                }
                // 设置考勤标志
                tempBean.setMark(totalMark(tempBean, p));
                arrListContent.add(tempBean);
            }
            LogUtil.log("EJB:根据部门id、开始结束日期、查询所有员工考勤登记记录结束", Level.INFO, null);
            return arrListContent;
        } catch (Exception e) {
            LogUtil.log("EJB:根据部门id、开始结束日期、查询所有员工考勤登记记录错误", Level.SEVERE, null);
            throw e;
        }
    }

    /**
     * 考勤审核
     * 
     * @param deptIds
     * @param strStartDate
     * @param strEndDate
     * @param argEnterpriseCode
     * @param p
     * @param workerCode
     * @param attendanceDeptId
     * @param checkEmpId
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveTotalCountByEmp(List<String> deptIds, String strStartDate, String strEndDate,
            String argEnterpriseCode, Properties p, String workerCode, String attendanceDeptId, Long checkEmpId)
            throws Exception {
        LogUtil.log("EJB:考勤审核开始", Level.INFO, null);
        try {
            String strYear = strStartDate.substring(0, 4);
            String strMonth = strStartDate.substring(5, 7);
            // 查询上面部门里的所有员工的考勤信息
            List<TimeKeeperExamineContent> arrListContent = allEmpAttendanceList(deptIds, strStartDate, strEndDate,
                    argEnterpriseCode, p);
            // 定义津贴费用map
            Map<Object, Double> fee = new HashMap<Object, Double>();
            // 定义存储是数据结构
            Map<Object, Map<Object, Map<Object, Long>>> mapAll = new HashMap<Object, Map<Object, Map<Object, Long>>>();
            // 遍历所有员工
            for (int index = 0; index < arrListContent.size(); index++) {
                TimeKeeperExamineContent tempBean = arrListContent.get(index);
                if (!isEmpty(tempBean.getWorkShiftId())) {
                    if (isEmpty(fee.get(tempBean.getWorkShiftId()))) {
                        fee.put(tempBean.getWorkShiftId(), tempBean.getWorkShitFee());
                    }
                }
                Map<Object, Map<Object, Long>> empMap = new HashMap<Object, Map<Object, Long>>();
                // 判断员工在map里是否存在
                if (isEmpty(mapAll.get(tempBean.getEmpId()))) {
                    // 不存在则new一个该员工的map
                    empMap.put(CACodeConstants.STAT_ITEM_TYPE_OVERTIME, new HashMap<Object, Long>());
                    empMap.put(CACodeConstants.STAT_ITEM_TYPE_RUN, new HashMap<Object, Long>());
                    empMap.put(CACodeConstants.STAT_ITEM_TYPE_VACATIONID, new HashMap<Object, Long>());
                    empMap.put(CACodeConstants.STAT_ITEM_TYPE_WORK, new HashMap<Object, Long>());
                    Map<Object, Long> deptId = new HashMap<Object, Long>();
                    deptId.put("deptId", Long.parseLong(tempBean.getDeptId()));
                    deptId.put("attendanceDeptId", isEmpty(tempBean.getAttendanceDeptId()) ? Long.parseLong(tempBean
                            .getDeptId()) : Long.parseLong(tempBean.getAttendanceDeptId()));
                    empMap.put("deptAndAttendanceId", deptId);
                    mapAll.put(tempBean.getEmpId(), empMap);

                }
                empMap = mapAll.get(tempBean.getEmpId());
                // 判断该员工有无加班id
                if (!isEmpty(tempBean.getOvertimeTypeId())) {
                    // 判断该加班id是否已经存在
                    if (isEmpty(empMap.get(CACodeConstants.STAT_ITEM_TYPE_OVERTIME).get(tempBean.getOvertimeTypeId()))) {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_OVERTIME).put(tempBean.getOvertimeTypeId(), (long) 1);
                    } else {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_OVERTIME)
                                .put(
                                        tempBean.getOvertimeTypeId(),
                                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_OVERTIME).get(
                                                tempBean.getOvertimeTypeId()) + 1);
                    }
                }
                // 判断该员工有无运行班id
                if (!isEmpty(tempBean.getWorkShiftId())) {
                    // 判断该运行班id是否已经存在
                    if (isEmpty(empMap.get(CACodeConstants.STAT_ITEM_TYPE_RUN).get(tempBean.getWorkShiftId()))) {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_RUN).put(tempBean.getWorkShiftId(), (long) 1);
                    } else {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_RUN).put(tempBean.getWorkShiftId(),
                                empMap.get(CACodeConstants.STAT_ITEM_TYPE_RUN).get(tempBean.getWorkShiftId()) + 1);
                    }
                }
                // 判断该员工有无假别id
                if (!isEmpty(tempBean.getVacationTypeId())) {
                    // 判断该运行班id是否已经存在
                    if (isEmpty(empMap.get(CACodeConstants.STAT_ITEM_TYPE_VACATIONID).get(tempBean.getVacationTypeId()))) {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_VACATIONID).put(tempBean.getVacationTypeId(),
                                (long) 1);
                    } else {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_VACATIONID)
                                .put(
                                        tempBean.getVacationTypeId(),
                                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_VACATIONID).get(
                                                tempBean.getVacationTypeId()) + 1);
                    }
                }
                // 判断该员工有无出勤
                if (FLAG_1.equals(tempBean.getWork())) {
                    // 判断出勤是否已经存在
                    if (isEmpty(empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                            CACodeConstants.ATTENDANCE_TYPE_WORK))) {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(CACodeConstants.ATTENDANCE_TYPE_WORK,
                                (long) 1);
                    } else {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(
                                CACodeConstants.ATTENDANCE_TYPE_WORK,
                                empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                                        CACodeConstants.ATTENDANCE_TYPE_WORK) + 1);
                    }
                }
                // 判断该员工有无休息
                if (FLAG_1.equals(tempBean.getRest())) {
                    // 判断出勤是否已经存在
                    if (isEmpty(empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                            CACodeConstants.ATTENDANCE_TYPE_REST))) {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(CACodeConstants.ATTENDANCE_TYPE_REST,
                                (long) 1);
                    } else {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(
                                CACodeConstants.ATTENDANCE_TYPE_REST,
                                empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                                        CACodeConstants.ATTENDANCE_TYPE_REST) + 1);
                    }
                }
                // 判断该员工有无旷工
                if (FLAG_1.equals(tempBean.getAbsent())) {
                    // 判断旷工是否已经存在
                    if (isEmpty(empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                            CACodeConstants.ATTENDANCE_TYPE_ABSENT_WORK))) {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(
                                CACodeConstants.ATTENDANCE_TYPE_ABSENT_WORK, (long) 1);
                    } else {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(
                                CACodeConstants.ATTENDANCE_TYPE_ABSENT_WORK,
                                empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                                        CACodeConstants.ATTENDANCE_TYPE_ABSENT_WORK) + 1);
                    }
                }
                // 判断该员工有无迟到
                if (FLAG_1.equals(tempBean.getLate())) {
                    // 判断迟到是否已经存在
                    if (isEmpty(empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                            CACodeConstants.ATTENDANCE_TYPE_LATE_WORK))) {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(CACodeConstants.ATTENDANCE_TYPE_LATE_WORK,
                                (long) 1);
                    } else {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(
                                CACodeConstants.ATTENDANCE_TYPE_LATE_WORK,
                                empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                                        CACodeConstants.ATTENDANCE_TYPE_LATE_WORK) + 1);
                    }
                }
                // 判断该员工有无早退
                if (FLAG_1.equals(tempBean.getLeave())) {
                    // 判断早退是否已经存在
                    if (isEmpty(empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                            CACodeConstants.ATTENDANCE_TYPE_LEAVE_EARLY))) {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(
                                CACodeConstants.ATTENDANCE_TYPE_LEAVE_EARLY, (long) 1);
                    } else {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(
                                CACodeConstants.ATTENDANCE_TYPE_LEAVE_EARLY,
                                empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                                        CACodeConstants.ATTENDANCE_TYPE_LEAVE_EARLY) + 1);
                    }
                }
                // 判断该员工有无外借
                if (FLAG_1.equals(tempBean.getOut())) {
                    // 判断外借是否已经存在
                    if (isEmpty(empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                            CACodeConstants.ATTENDANCE_TYPE_OUT_WORK))) {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(CACodeConstants.ATTENDANCE_TYPE_OUT_WORK,
                                (long) 1);
                    } else {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(
                                CACodeConstants.ATTENDANCE_TYPE_OUT_WORK,
                                empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                                        CACodeConstants.ATTENDANCE_TYPE_OUT_WORK) + 1);
                    }
                }
                // 判断该员工有无出差
                if (FLAG_1.equals(tempBean.getEvection())) {
                    // 判断出差是否已经存在
                    if (isEmpty(empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                            CACodeConstants.ATTENDANCE_TYPE_EVECTION))) {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(CACodeConstants.ATTENDANCE_TYPE_EVECTION,
                                (long) 1);
                    } else {
                        empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).put(
                                CACodeConstants.ATTENDANCE_TYPE_EVECTION,
                                empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(
                                        CACodeConstants.ATTENDANCE_TYPE_EVECTION) + 1);
                    }
                }

            }
            // 加班数据list
            List<HrDOvertimetotal> overtimeTotal = new ArrayList<HrDOvertimetotal>();
            // 运行班数据list
            List<HrDWorkshifttotal> workShiftTotal = new ArrayList<HrDWorkshifttotal>();
            // 请假数据list
            List<HrDVacationtotal> vacationTotal = new ArrayList<HrDVacationtotal>();
            // 出勤数据list
            List<HrDWorkdays> workDaysTotal = new ArrayList<HrDWorkdays>();
            for (Object empId : mapAll.keySet()) {
                Map<Object, Map<Object, Long>> empMap = mapAll.get(empId);
                for (Object id : empMap.get(CACodeConstants.STAT_ITEM_TYPE_OVERTIME).keySet()) {
                    // 定义加班bean
                    HrDOvertimetotal overTimeBean = new HrDOvertimetotal();
                    HrDOvertimetotalId overTimeBeanId = new HrDOvertimetotalId();
                    overTimeBeanId.setEmpId(Long.parseLong(empId.toString()));
                    overTimeBeanId.setAttendanceYear(strYear);
                    overTimeBeanId.setAttendanceMonth(strMonth);
                    overTimeBeanId.setOvertimeTypeId(Long.parseLong(id.toString()));
                    // 赋值id
                    overTimeBean.setId(overTimeBeanId);
                    // 赋值天数
                    overTimeBean.setDays((double) empMap.get(CACodeConstants.STAT_ITEM_TYPE_OVERTIME).get(id));
                    // 设置部门
                    overTimeBean.setDeptId(empMap.get("deptAndAttendanceId").get("deptId"));
                    // 设置考勤部门
                    overTimeBean.setAttendanceDeptId(empMap.get("deptAndAttendanceId").get("attendanceDeptId"));
                    // 赋值是否使用
                    overTimeBean.setIsUse(FLAG_Y);
                    // 赋值最后修改人
                    overTimeBean.setLastModifiyBy(workerCode);
                    // 赋值企业编码
                    overTimeBean.setEnterpriseCode(argEnterpriseCode);
                    // 加入到list
                    overtimeTotal.add(overTimeBean);

                }
                for (Object id : empMap.get(CACodeConstants.STAT_ITEM_TYPE_RUN).keySet()) {
                    // 定义运行班bean
                    HrDWorkshifttotal workShiftBean = new HrDWorkshifttotal();
                    HrDWorkshifttotalId workshiftBeanId = new HrDWorkshifttotalId();
                    workshiftBeanId.setEmpId(Long.parseLong(empId.toString()));
                    workshiftBeanId.setAttendanceYear(strYear);
                    workshiftBeanId.setAttendanceMonth(strMonth);
                    workshiftBeanId.setWorkShiftId(Long.parseLong(id.toString()));
                    // 赋值id
                    workShiftBean.setId(workshiftBeanId);
                    // 赋值天数
                    workShiftBean.setDays((double) empMap.get(CACodeConstants.STAT_ITEM_TYPE_RUN).get(id));
                    // 设置部门
                    workShiftBean.setDeptId(empMap.get("deptAndAttendanceId").get("deptId"));
                    // 设置金钱
                    workShiftBean.setMoney(fee.get(id) * workShiftBean.getDays());
                    // 赋值是否使用
                    workShiftBean.setIsUse(FLAG_Y);
                    // 赋值最后修改人
                    workShiftBean.setLastModifiyBy(workerCode);
                    // 赋值企业编码
                    workShiftBean.setEnterpriseCode(argEnterpriseCode);
                    // 加入到list
                    workShiftTotal.add(workShiftBean);

                }
                for (Object id : empMap.get(CACodeConstants.STAT_ITEM_TYPE_VACATIONID).keySet()) {
                    // 定义请假bean
                    HrDVacationtotal vacationBean = new HrDVacationtotal();
                    HrDVacationtotalId vacationBeanId = new HrDVacationtotalId();
                    vacationBeanId.setEmpId(Long.parseLong(empId.toString()));
                    vacationBeanId.setAttendanceYear(strYear);
                    vacationBeanId.setAttendanceMonth(strMonth);
                    vacationBeanId.setVacationTypeId(Long.parseLong(id.toString()));
                    // 赋值id
                    vacationBean.setId(vacationBeanId);
                    // 赋值天数
                    vacationBean.setDays((double) empMap.get(CACodeConstants.STAT_ITEM_TYPE_VACATIONID).get(id));
                    // 设置部门
                    vacationBean.setDeptId(empMap.get("deptAndAttendanceId").get("deptId"));
                    // 赋值是否使用
                    vacationBean.setIsUse(FLAG_Y);
                    // 赋值最后修改人
                    vacationBean.setLastModifiyBy(workerCode);
                    // 赋值企业编码
                    vacationBean.setEnterpriseCode(argEnterpriseCode);
                    // 加入到list
                    vacationTotal.add(vacationBean);

                }
                for (Object id : empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).keySet()) {
                    // 定义考勤bean
                    HrDWorkdays workDaysBean = new HrDWorkdays();
                    HrDWorkdaysId workDaysBeanId = new HrDWorkdaysId();
                    workDaysBeanId.setEmpId(Long.parseLong(empId.toString()));
                    workDaysBeanId.setAttendanceYear(strYear);
                    workDaysBeanId.setAttendanceMonth(strMonth);
                    workDaysBeanId.setAttendanceTypeId(Long.parseLong(id.toString()));
                    // 赋值id
                    workDaysBean.setId(workDaysBeanId);
                    // 赋值天数
                    workDaysBean.setDays((double) empMap.get(CACodeConstants.STAT_ITEM_TYPE_WORK).get(id));
                    // 设置部门
                    workDaysBean.setDeptId(empMap.get("deptAndAttendanceId").get("deptId"));
                    // 赋值是否使用
                    workDaysBean.setIsUse(FLAG_Y);
                    // 赋值最后修改人
                    workDaysBean.setLastModifiyBy(workerCode);
                    // 赋值企业编码
                    workDaysBean.setEnterpriseCode(argEnterpriseCode);
                    // 加入到list
                    workDaysTotal.add(workDaysBean);
                }
            }
            // 保存操作
            // 保存加班统计
            for (int i = 0; i < overtimeTotal.size(); i++) {
                HrDOvertimetotal newBean = overtimeTotal.get(i);
                HrDOvertimetotal tempBean = overTimeTotalManager.findById(newBean.getId(), argEnterpriseCode);
                if (isEmpty(tempBean)) {
                    newBean.setLastModifiyDate(new Date());
                    overTimeTotalManager.save(newBean);
                } else {
                    tempBean.setDays(newBean.getDays());
                    tempBean.setDeptId(newBean.getDeptId());
                    tempBean.setAttendanceDeptId(newBean.getAttendanceDeptId());
                    tempBean.setLastModifiyBy(newBean.getLastModifiyBy());
                    overTimeTotalManager.update(tempBean);
                }
            }
            // 保存运行班统计
            for (int i = 0; i < workShiftTotal.size(); i++) {
                HrDWorkshifttotal newBean = workShiftTotal.get(i);
                HrDWorkshifttotal tempBean = workShiftTotalManager.findById(newBean.getId(), argEnterpriseCode);
                if (isEmpty(tempBean)) {
                    newBean.setLastModifiyDate(new Date());
                    workShiftTotalManager.save(newBean);
                } else {
                    tempBean.setDays(newBean.getDays());
                    tempBean.setDeptId(newBean.getDeptId());
                    tempBean.setMoney(newBean.getMoney());
                    tempBean.setLastModifiyBy(newBean.getLastModifiyBy());
                    workShiftTotalManager.update(tempBean);
                }
            }
            // 保存请假统计
            for (int i = 0; i < vacationTotal.size(); i++) {
                HrDVacationtotal newBean = vacationTotal.get(i);
                HrDVacationtotal tempBean = vacationTotalManager.findById(newBean.getId(), argEnterpriseCode);
                if (isEmpty(tempBean)) {
                    newBean.setLastModifiyDate(new Date());
                    vacationTotalManager.save(newBean);
                } else {
                    tempBean.setDays(newBean.getDays());
                    tempBean.setDeptId(newBean.getDeptId());
                    tempBean.setLastModifiyBy(newBean.getLastModifiyBy());
                    vacationTotalManager.update(tempBean);
                }
            }
            // 保存出勤统计
            for (int i = 0; i < workDaysTotal.size(); i++) {
                HrDWorkdays newBean = workDaysTotal.get(i);
                HrDWorkdays tempBean = workDaysTotalManager.findById(newBean.getId(), argEnterpriseCode);
                if (isEmpty(tempBean)) {
                    newBean.setLastModifiyDate(new Date());
                    workDaysTotalManager.save(newBean);
                } else {
                    tempBean.setDays(newBean.getDays());
                    tempBean.setDeptId(newBean.getDeptId());
                    tempBean.setLastModifiyBy(newBean.getLastModifiyBy());
                    workDaysTotalManager.update(tempBean);
                }
            }
            // 实例化一个bean
            HrJAttendancecheck newBean = new HrJAttendancecheck();
            HrJAttendancecheckId newBeanId = new HrJAttendancecheckId();
            newBeanId.setAttendanceDep(Long.parseLong(attendanceDeptId));
            newBeanId.setAttendanceYear(strYear);
            newBeanId.setAttendanceMonth(strMonth);
            newBean.setId(newBeanId);
            // 查找该id的记录是否存在
            HrJAttendancecheck lastBean = attendanceCheckRemote.findById(newBeanId, argEnterpriseCode);
            if (isEmpty(lastBean)) {
                // 如果不存在，插入新记录
                newBean.setDepCharge1(checkEmpId);
                newBean.setCheckedDate1(new Date());
                newBean.setLastModifiyBy(workerCode);
                newBean.setLastModifiyDate(new Date());
                newBean.setEnterpriseCode(argEnterpriseCode);
                newBean.setIsUse(FLAG_Y);
                attendanceCheckRemote.save(newBean);
            } else {
                if (isEmpty(lastBean.getDepCharge1())) {
                    // 如果存在，更新记录
                    lastBean.setDepCharge1(checkEmpId);
                    lastBean.setCheckedDate1(new Date());
                    lastBean.setLastModifiyBy(workerCode);
                    lastBean.setEnterpriseCode(argEnterpriseCode);
                    attendanceCheckRemote.update(lastBean);
                } else {
                    throw new DataChangeException(null);
                }
            }
            LogUtil.log("EJB:考勤审核结束", Level.INFO, null);
        } catch (DataChangeException e) {
            LogUtil.log("EJB:考勤审核错误", Level.SEVERE, null);
            ctx.setRollbackOnly();
            throw e;
        } catch (NumberFormatException e) {
            LogUtil.log("EJB:考勤审核错误", Level.SEVERE, null);
            ctx.setRollbackOnly();
            throw e;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:考勤审核错误", Level.SEVERE, null);
            ctx.setRollbackOnly();
            throw e;
        } catch (Exception e) {
            ctx.setRollbackOnly();
            LogUtil.log("EJB:考勤审核错误", Level.SEVERE, null);
            throw e;
        }
    }

    /**
     * 设置考勤标志
     * 
     * @param tempBean
     * @param p
     * @return
     */
    public String totalMark(TimeKeeperExamineContent tempBean, Properties p) {
        String returnMark = BLANK_STRING;
        if (isEmpty(tempBean.getWorkShiftId()))
            returnMark += returnMark(tempBean.getWork(), p, "DUTY");
        returnMark += returnMark(tempBean.getRest(), p, "REST");
        returnMark += returnMark(tempBean.getAbsent(), p, "ABSENT");
        returnMark += returnMark(tempBean.getEvection(), p, "EVECTION");
        returnMark += returnMark(tempBean.getLate(), p, "LATE");
        returnMark += returnMark(tempBean.getLeave(), p, "AHEAD");
        returnMark += returnMark(tempBean.getOut(), p, "OUT");
        if (tempBean.getMark() != null) {
            return tempBean.getMark() + returnMark;
        } else {
            return returnMark;
        }
    }

    /**
     * 获取考勤标志
     * 
     * @param value
     * @param p
     * @param col
     * @return
     */
    public String returnMark(String value, Properties p, String col) {
        if (FLAG_0.equals(value)) {
            return p.getProperty(col + FLAG_0);
        } else {
            return p.getProperty(col + FLAG_1);
        }
    }

    /**
     * 计算合计项
     * 
     * @param statitem
     *            合计项bean
     * @param Datas
     *            数据
     * @return 计算结果
     */
    private void countTotalItem(TimeKeeperExamineContent content, Map<Object, Object> map,
            List<HrCStatitem> totalNameList, String standardTime) {
        // 合计项
        HrCStatitem statItem = null;
        // 合计项对应id
        String statItemId = null;
        // 合计项类型
        String statItemType = null;
        // 出勤flg
        String work_flg = FLAG_1;
        for (int index = 0; index < totalNameList.size(); index++) {

            statItem = totalNameList.get(index);
            statItemId = statItem.getStatItemId().toString();
            statItemType = statItem.getStatItemType();

            String key = TOTAL_DATAINDEX + index;
            boolean isAdd = false;

            // 加班合计
            if (CACodeConstants.STAT_ITEM_TYPE_OVERTIME.equals(statItemType)) {
                // 加班类别id相同
                if (statItemId != null && statItemId.equals(content.getOvertimeTypeId())) {
                    isAdd = true;
                }
            }// 运行班合计
            else if (CACodeConstants.STAT_ITEM_TYPE_RUN.equals(statItemType)) {
                if (statItemId != null && statItemId.equals(content.getWorkShiftId())) {
                    isAdd = true;
                }
            }// 假别统计
            else if (CACodeConstants.STAT_ITEM_TYPE_VACATIONID.equals(statItemType)) {
                if (statItemId != null && statItemId.equals(content.getVacationTypeId())) {
                    isAdd = true;
                }
            }// 出勤统计
            else if (CACodeConstants.STAT_ITEM_TYPE_WORK.equals(statItemType)) {
                // 出勤
                if (CACodeConstants.ATTENDANCE_TYPE_WORK.equals(statItemId) && work_flg.equals(content.getWork())) {
                    isAdd = true;
                }
                // 休息
                else if (CACodeConstants.ATTENDANCE_TYPE_REST.equals(statItemId)
                        && CACodeConstants.REST_FLG_YES.equals(content.getRest())) {
                    isAdd = true;
                }
                // 旷工
                else if (CACodeConstants.ATTENDANCE_TYPE_ABSENT_WORK.equals(statItemId)
                        && CACodeConstants.ABSENT_WORK_FLG_YES.equals(content.getAbsent())) {
                    isAdd = true;
                }
                // 迟到
                else if (CACodeConstants.ATTENDANCE_TYPE_LATE_WORK.equals(statItemId)
                        && CACodeConstants.LATE_FLG_YES.equals(content.getLate())) {
                    isAdd = true;
                }
                // 早退
                else if (CACodeConstants.ATTENDANCE_TYPE_LEAVE_EARLY.equals(statItemId)
                        && CACodeConstants.LEAVE_EARLY_FLG_YES.equals(content.getLeave())) {
                    isAdd = true;
                }
                // 外借
                else if (CACodeConstants.ATTENDANCE_TYPE_OUT_WORK.equals(statItemId)
                        && CACodeConstants.OUT_WORK_FLG_YES.equals(content.getOut())) {
                    isAdd = true;
                }
                // 出差
                else if (CACodeConstants.ATTENDANCE_TYPE_EVECTION.equals(statItemId)
                        && CACodeConstants.EVECTION_FLG_YES.equals(content.getEvection())) {
                    isAdd = true;
                }
            }
            if (isAdd) {
                AddTotalItemValue(map, key, standardTime);
            }
        }

    }

    /**
     * 合计项值加1
     * 
     * @param map
     *            map
     * @param key
     *            map key
     * @param statItemUnit
     *            单位
     */
    private void AddTotalItemValue(Map<Object, Object> map, Object key, String standardTime) {
        double addValue = 1;
        Double value = (Double) map.get(key);
        if (value == null) {
            map.put(key, addValue);
        } else {
            map.put(key, value + addValue);
        }
    }

    /**
     * 递归判断所有部门考勤记录均已申报
     * 
     * @param argDeptId
     * @param arrlist
     * @return 返回未申报的部门，没有则为空。
     */
    private String checkDeptType(List<TimeKeeperExamine> arrlist, String argDeptId) {
        // 定义msg信息
        String msgInfo = BLANK_STRING;
        TimeKeeperExamine bean;
        int level = -1;
        // 循环list
        for (int j = 0; j < arrlist.size(); j++) {
            bean = arrlist.get(j);
            if (argDeptId.equals(bean.getExamineBDeptId())) {
                continue;
            }
            // 考勤类别为2的部门的下层部门不需要check
            if (level != -1 && bean.getLevel() > level) {
                continue;
            }
            // 考勤类别为2
            if (ATTEND_DEP_TYPE_2.equals(bean.getExamineType())) {
                // 可撤销审核FLG = 是
                bean.setCancelFlag(FLAG_Y);
                // 审核人2为空
                if (null == bean.getExamineMan2() || BLANK_STRING.equals(bean.getExamineMan2())) {
                    if (!isEmpty(msgInfo)) {
                        msgInfo += UNCHECK_CUT;
                    }
                    // msg
                    msgInfo += bean.getExamineBDeptName();
                }
                level = bean.getLevel();
            } else {
                level = -1;
            }
        }
        // 返回msg
        return msgInfo;
    }

    /**
     * 根据年份月份部门id查询该部门该年该月开始日期和结束日期
     * 
     * @param strYear
     * @param strMonth
     * @param argDeptId
     * @param argEnterpriseCode
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private AttendanceStandard getStartAndEndDate(String strYear, String strMonth, String argAttendanceDeptId,
            String argDeptId, String argEnterpriseCode) {
        try {

            LogUtil.log("EJB:查询考勤开始日期和结束日期开始", Level.INFO, null);
            StringBuilder sbd = new StringBuilder();
            sbd.append("SELECT    ");
            sbd.append("    A.ATTENDANCE_DEPT_ID,    ");
            sbd.append("    TO_CHAR(A.START_DATE , 'yyyy-mm-dd'),    ");
            sbd.append("    TO_CHAR(A.END_DATE , 'yyyy-mm-dd'),    ");
            sbd.append("    NVL(A.AM_BEGING_TIME,' '),   ");
            sbd.append("    NVL(A.AM_END_TIME,' '),    ");
            sbd.append("    NVL(A.PM_BEGING_TIME,' '),    ");
            sbd.append("    NVL(A.PM_END_TIME,' '),   ");
            sbd.append("    A.STANDARD_TIME   ");
            sbd.append("FROM    ");
            sbd.append("    HR_C_ATTENDANCESTANDARD A    ");
            sbd.append("WHERE     ");
            sbd.append("    A.IS_USE = ?    ");
            sbd.append("    AND A.ENTERPRISE_CODE = ?     ");
            sbd.append("    AND A.ATTENDANCE_YEAR = ?     ");
            sbd.append("    AND A.ATTENDANCE_MONTH = ?     ");
            sbd.append("    AND A.ATTENDANCE_DEPT_ID = ?     ");
            // 打印SQL语句
            LogUtil.log("EJB:sql=" + sbd.toString(), Level.INFO, null);
            List<String> listParams = new ArrayList<String>();
            listParams.add(IS_USE_Y);
            listParams.add(argEnterpriseCode);
            listParams.add(strYear);
            listParams.add(strMonth);
            if (!isEmpty(argAttendanceDeptId)) {
                listParams.add(argAttendanceDeptId);
            } else {
                listParams.add(argDeptId);
            }
            Object[] params = listParams.toArray();
            // 查询一条有参数sql语句
            List lstResult = bll.queryByNativeSQL(sbd.toString(), params);
            AttendanceStandard tempBean = new AttendanceStandard();
            // 如果返回有结果且记录数为1
            if (lstResult.size() == 1) {
                tempBean.setAttendanceDeptId(((Object[]) lstResult.get(0))[0].toString());
                tempBean.setStartDate(((Object[]) lstResult.get(0))[1].toString());
                tempBean.setEndDate(((Object[]) lstResult.get(0))[2].toString());
                tempBean.setAmBegingTime(String.valueOf(((Object[]) lstResult.get(0))[3]));
                tempBean.setAmEndTime(String.valueOf(((Object[]) lstResult.get(0))[4]));
                tempBean.setPmBegingTime(String.valueOf(((Object[]) lstResult.get(0))[5]));
                tempBean.setPmEndTime(String.valueOf(((Object[]) lstResult.get(0))[6]));
                tempBean.setStandardTime(String.valueOf(((Object[]) lstResult.get(0))[7]));
            } else {
                throw new RuntimeException("X");
            }
            LogUtil.log("EJB:查询考勤开始日期和结束日期结束", Level.INFO, null);
            return tempBean;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:查询考勤开始日期和结束日期错误", Level.SEVERE, null);
            throw e;
        }
    }

    /**
     * 获得考勤合计项信息
     * 
     * @param enterpriseCode
     *            企业编码
     * @return List<StatItemNameInfo>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<HrCStatitem> getStatItemList(String enterpriseCode) throws Exception {
        LogUtil.log("EJB:考勤合计项维护初始化开始。", Level.INFO, null);
        try {
            StringBuilder sbd = new StringBuilder();
            sbd.append("SELECT   * ");
            sbd.append("    FROM HR_C_STATITEM T    ");
            sbd.append("    WHERE T.IS_USE = ?   AND T.USE_FLG = ? ");
            sbd.append("    AND T.ENTERPRISE_CODE = ?    ");
            sbd.append("     ORDER BY T.ORDER_BY    ");
            // 查询参数数组
            Object[] params = new Object[3];
            params[0] = IS_USE_Y;
            params[1] = FLAG_1;
            params[2] = enterpriseCode;
            LogUtil.log("EJB:SQL= " + sbd.toString(), Level.INFO, null);
            List<HrCStatitem> arrList = bll.queryByNativeSQL(sbd.toString(), params, HrCStatitem.class);
            // 按查询结果集设置返回结果
            LogUtil.log("EJB:考勤合计项维护初始化结束。", Level.INFO, null);
            return arrList;
        } catch (Exception e) {
            LogUtil.log("EJB:考勤合计项维护初始化失败。", Level.SEVERE, e);
            throw e;
        }
    }

    /**
     * 根据开始日期结束日期生成表头。
     * 
     * @param strStartDate
     * @param strEndDate
     * @return
     * @throws Exception
     */
    private MetaData creatGridHeader(String strStartDate, String strEndDate, String enterpriseCode, Properties p)
            throws Exception {
        try {
            List<Map<String, Object>> fields = new ArrayList<Map<String, Object>>();
            dayFields.clear();
            totalNameList.clear();
            String root = "list";
            String totalProperty = "totalCount";
            MetaData metas = new MetaData();
            String[] days = { BLANK_STRING, "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四",
                    "十五", "十六", "十七", "十八", "十九", "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八", "二十九",
                    "三十", "三十一" };
            String[] weeks = { BLANK_STRING, "周日 ", "周一 ", "周二 ", "周三 ", "周四 ", "周五 ", "周六 " };
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Calendar cs = Calendar.getInstance();
            Calendar ce = Calendar.getInstance();
            Calendar ch = Calendar.getInstance();
            Date s = sdf.parse(strStartDate);
            Date e = sdf.parse(strEndDate);
            cs.setTime(s);
            ce.setTime(e);
            ce.add(Calendar.DAY_OF_MONTH, 1);
            String headerName = DATE_DATAINDEX;
            String headerCountName = TOTAL_DATAINDEX;
            int i = 0;
            int j = 0;
            Map<String, Object> tempLine = new HashMap<String, Object>();
            tempLine.put("name", "lineNum");
            fields.add(tempLine);
            Map<String, Object> tempEmpName = new HashMap<String, Object>();
            tempEmpName.put("name", "empName");
            tempEmpName.put("dataIndex", "empName");
            tempEmpName.put("header", "<span style='line-height:2.6em'>员工姓名</span>");
            tempEmpName.put("align", "left");
            tempEmpName.put("sortable", "true");
            tempEmpName.put("width", WIDTH_EMP_INFO);
            fields.add(tempEmpName);
            Map<String, Object> tempEmpId = new HashMap<String, Object>();
            tempEmpId.put("name", "empId");
            tempEmpId.put("dataIndex", "empId");
            tempEmpId.put("hidden", "true");
            fields.add(tempEmpId);
            Map<String, Object> tempEmpDept = new HashMap<String, Object>();
            tempEmpDept.put("name", "deptName");
            tempEmpDept.put("dataIndex", "deptName");
            tempEmpDept.put("header", "<span style='line-height:2.6em'>所属部门</span>");
            tempEmpDept.put("sortable", "true");
            tempEmpDept.put("align", "left");
            tempEmpDept.put("width", WIDTH_EMP_INFO);
            fields.add(tempEmpDept);
            Map<String, Object> tempEmpDeptId = new HashMap<String, Object>();
            tempEmpDeptId.put("name", "deptId");
            tempEmpDeptId.put("dataIndex", "deptId");
            tempEmpDeptId.put("hidden", "true");
            fields.add(tempEmpDeptId);
            Map<String, Object> tempEmpCheckDeptId = new HashMap<String, Object>();
            tempEmpCheckDeptId.put("name", "attendanceDeptId");
            tempEmpCheckDeptId.put("dataIndex", "attendanceDeptId");
            tempEmpCheckDeptId.put("hidden", "true");
            fields.add(tempEmpCheckDeptId);
            List<HrCHoliday> holidaysList = getHolidays(strStartDate, strEndDate, enterpriseCode);
            do {
                Map<String, Object> tempDayInfo = new HashMap<String, Object>();
                tempDayInfo.put("day", cs.get(Calendar.DAY_OF_MONTH));
                tempDayInfo.put("week", cs.get(Calendar.DAY_OF_WEEK));
                if (j < holidaysList.size())
                    ch.setTime(holidaysList.get(j).getHolidayDate());
                String workOrRestFlag = BLANK_STRING;
                if (cs.get(Calendar.DAY_OF_WEEK) == 1 || cs.get(Calendar.DAY_OF_WEEK) == 7) {
                    workOrRestFlag = FLAG_1;
                    if (ch.equals(cs) && FLAG_2.equals(holidaysList.get(j).getHolidayType())) {
                        workOrRestFlag = FLAG_2;
                        j++;
                    }
                } else {
                    workOrRestFlag = FLAG_2;
                    if (ch.equals(cs) && FLAG_1.equals(holidaysList.get(j).getHolidayType())) {
                        workOrRestFlag = FLAG_1;
                        j++;
                    }
                }
                tempDayInfo.put("workOrRest", workOrRestFlag);
                dayFields.add(tempDayInfo);
                Map<String, Object> tempCm = new HashMap<String, Object>();
                tempCm.put("name", headerName + i);
                tempCm.put("dataIndex", headerName + i);
                tempCm.put("header", workOrRestFlag.equals(FLAG_1) ? "<font color='" + p.getProperty("COLOR") + "'>"
                        + days[cs.get(Calendar.DAY_OF_MONTH)] + "<br />" + weeks[cs.get(Calendar.DAY_OF_WEEK)]
                        + "</font>" : days[cs.get(Calendar.DAY_OF_MONTH)] + "<br />"
                        + weeks[cs.get(Calendar.DAY_OF_WEEK)]);
                tempCm.put("align", "Center");
                tempCm.put("width", WIDTH_DATE);
                tempCm.put("renderer", "setColor");
                fields.add(tempCm);
                cs.add(Calendar.DAY_OF_MONTH, 1);
                i++;
            } while (cs.before(ce));
            // 获取合计项列名信息
            totalNameList = getStatItemList(enterpriseCode);
            for (int index = 0; index < totalNameList.size(); index++) {
                String formatTime = BLANK_STRING;
                String strTotalCountName = BLANK_STRING;
                formatTime = "setTotalCountNumber2";
                if (isEmpty(totalNameList.get(index).getStatItemByname())) {
                    strTotalCountName = totalNameList.get(index).getStatItemName();
                } else {
                    strTotalCountName = totalNameList.get(index).getStatItemByname();
                }
                Map<String, Object> tempCm = new HashMap<String, Object>();
                tempCm.put("name", headerCountName + index);
                tempCm.put("dataIndex", headerCountName + index);
                tempCm.put("header", "<span style='line-height:2.6em'>" + strTotalCountName + "</span>");
                tempCm.put("align", "right");
                tempCm.put("renderer", formatTime);
                tempCm.put("sortable", "true");
                tempCm.put("width", WIDTH_TOTAL);
                fields.add(tempCm);
            }
            metas.setFields(fields);
            metas.setId("emp");
            metas.setRoot(root);
            metas.setTotalProperty(totalProperty);
            return metas;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 根据开始日期结束日期查询假日
     * 
     * @param strStartDate
     * @param strEndDate
     * @param strEnterpriseCode
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<HrCHoliday> getHolidays(String strStartDate, String strEndDate, String strEnterpriseCode)
            throws Exception {
        try {
            LogUtil.log("EJB:根据开始日期结束日期查询假日开始。", Level.INFO, null);
            // 查询sql
            StringBuffer sbd = new StringBuffer();
            // SELECT文
            sbd.append("SELECT *");
            // FROM文
            sbd.append("FROM HR_C_HOLIDAY A ");
            sbd.append("WHERE ");
            sbd.append("A.ENTERPRISE_CODE = ? ");
            sbd.append("AND A.IS_USE = ? ");
            sbd.append("AND TO_CHAR(A.HOLIDAY_DATE,'yyyy-mm-dd') >= ? ");
            sbd.append("AND TO_CHAR(A.HOLIDAY_DATE,'yyyy-mm-dd') <= ? ");
            sbd.append("ORDER BY A.HOLIDAY_DATE ");
            // 查询参数数组
            Object[] params = new Object[4];
            int i = 0;
            params[i++] = strEnterpriseCode;
            params[i++] = IS_USE_Y;
            params[i++] = strStartDate;
            params[i++] = strEndDate;
            List<HrCHoliday> arrList = bll.queryByNativeSQL(sbd.toString(), params, HrCHoliday.class);
            LogUtil.log("EJB:根据开始日期结束日期查询假日结束。", Level.INFO, null);
            return arrList;
        } catch (Exception e) {
            LogUtil.log("EJB:根据开始日期结束日期查询假日错误。", Level.SEVERE, null);
            throw e;
        }
    }

    /**
     * 通过上级审核部门取得考勤部门
     * 
     * @param pid
     * @param enterpriseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageObject getDeptsByTopDeptid(String enterpriseCode, Long loginEmp) {
        LogUtil.log("EJB:通过上级审核部门取得考勤部门开始。", Level.INFO, null);
        PageObject obj = new PageObject();
        List<HrCAttendancedep> listReturn = new ArrayList<HrCAttendancedep>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT    ");
        sql.append("    T.ATTENDANCE_DEPT_ID,    ");
        sql.append("    T.ATTENDANCE_DEPT_NAME    ");
        sql.append("FROM        ");
        sql.append("    HR_C_ATTENDANCEDEP T        ");
        sql.append("WHERE        ");
        sql.append("    T.IS_USE = ?    AND        ");
        sql.append("    T.ATTEND_DEP_TYPE = ? AND    ");
        sql.append("    T.ENTERPRISE_CODE = ? AND    ");
        sql.append("    T.ATTEND_WRITER_ID = ?     ");
        sql.append("ORDER BY        ");
        sql.append("    T.ATTENDANCE_DEPT_ID    ");
        List list = bll.queryByNativeSQL(sql.toString(), new Object[] { "Y", FLAG_2, enterpriseCode, loginEmp });
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object[] data = (Object[]) it.next();
            HrCAttendancedep info = new HrCAttendancedep();

            // 部门id
            if (data[0] != null) {
                info.setAttendanceDeptId(Long.parseLong(data[0].toString()));
            }
            // 调动日期
            if (data[1] != null) {
                info.setAttendanceDeptName(data[1].toString());
            }
            listReturn.add(info);
            
            // add by liuyi 20100203 将该考勤审批部门下各层级的代考勤部门的考勤员审核权限取到
            String powerSql = 
            	"select T.ATTENDANCE_DEPT_ID, T.ATTENDANCE_DEPT_NAME\n" +
            	"  from HR_C_ATTENDANCEDEP T\n" + 
            	" where t.is_use = 'Y'\n" + 
            	"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
            	"   and t.attend_dep_type = '3'\n" + 
            	" start with t.attendance_dept_id ="+info.getAttendanceDeptId()+" \n" + 
            	"connect by prior t.attendance_dept_id = t.replace_dep_id";
            List powerList = bll.queryByNativeSQL(powerSql);
            Iterator powerIt = powerList.iterator();
            while(powerIt.hasNext())
            {
            	 Object[] powerData = (Object[]) powerIt.next();
                 HrCAttendancedep powerInfo = new HrCAttendancedep();

                 // 部门id
                 if (powerData[0] != null) {
                	 powerInfo.setAttendanceDeptId(Long.parseLong(powerData[0].toString()));
                 }
                 // 调动日期
                 if (powerData[1] != null) {
                	 powerInfo.setAttendanceDeptName(powerData[1].toString());
                 }
                 listReturn.add(powerInfo);
            }
            // add 20100203 end
        }
        
        obj.setList(listReturn);
        obj.setTotalCount((long) listReturn.size());
        LogUtil.log("EJB:通过上级审核部门取得考勤部门结束。", Level.INFO, null);
        return obj;
    }

    /**
     * 查询职工考勤记录
     * 
     * @param empId
     * @param enterpriseCode
     * @param strStartDate
     * @param strEndDate
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public TimeKeeperExamineStandardTime getEmpAttendance(String empId, String enterpriseCode, String strStartDate,
            String strEndDate, String checkFlag, List<Map<String, Object>> workOrRestList, String attendanceDeptId,
            String deptId) throws Exception {
        LogUtil.log("EJB:已审核明细部查询开始。", Level.INFO, null);
        try {
            TimeKeeperExamineStandardTime resultBean = new TimeKeeperExamineStandardTime();
            PageObject object = new PageObject();
            // sql
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("  SELECT * ");
            sbSql.append("  FROM HR_J_WORKATTENDANCE A    ");
            sbSql.append(" WHERE A.EMP_ID = ?    ");
            sbSql.append("   AND TO_CHAR(A.ATTENDANCE_DATE,'YYYY-MM-DD') >= ?    ");
            sbSql.append("   AND TO_CHAR(A.ATTENDANCE_DATE,'YYYY-MM-DD') <= ?    ");
            sbSql.append("   AND A.ENTERPRISE_CODE = ?    ");
            sbSql.append("   AND A.IS_USE = ?    ");
            sbSql.append(" ORDER BY  A.ATTENDANCE_DATE    ");
            // 查询
            List<HrJWorkattendance> list = bll.queryByNativeSQL(sbSql.toString(), new Object[] { empId, strStartDate,
                    strEndDate, enterpriseCode, IS_USE_Y }, HrJWorkattendance.class);
            List<HrJWorkattendance> listReturn = new ArrayList<HrJWorkattendance>();
            Calendar caStartDate = stringToCalendar(strStartDate);
            // 获取该员工所在考勤部门的标准时间
            AttendanceStandard timeTemp = getStartAndEndDate(strStartDate.substring(0, 4),
                    strStartDate.substring(5, 7), attendanceDeptId, deptId, enterpriseCode);
            // 将标准时间赋值到返回的结果里
            resultBean.setAmBeginTime(timeTemp.getAmBegingTime());
            resultBean.setAmEndTime(timeTemp.getAmEndTime());
            resultBean.setPmBeginTime(timeTemp.getPmBegingTime());
            resultBean.setPmEndTime(timeTemp.getPmEndTime());
            // 判断画面是否审核
            if (FLAG_Y.equals(checkFlag)) {
                // 如果画面是审核的
                // 循环将查询的记录插入返回的list里，并补全空日期的记录
                for (int j = 0, i = 0; j < dateFromTo(strStartDate, strEndDate); j++) {
                    if (i < list.size() && !isEmpty(list.get(i).getId().getAttendanceDate())
                            && dateCompare(caStartDate, list.get(i).getId().getAttendanceDate()) == 0) {
                        listReturn.add(list.get(i));
                        i++;
                        caStartDate.add(Calendar.DAY_OF_MONTH, 1);
                    } else {
                        HrJWorkattendance tempBean = new HrJWorkattendance();
                        HrJWorkattendanceId tempId = new HrJWorkattendanceId();
                        tempId.setAttendanceDate(caStartDate.getTime());
                        tempBean.setId(tempId);
                        listReturn.add(tempBean);
                        caStartDate.add(Calendar.DAY_OF_MONTH, 1);
                    }
                }
                object.setList(listReturn);
                object.setTotalCount((long) listReturn.size());
            } else {
                // 如果画面是未审核的
                //
                // 查询该员工的当月的请假信息
                List<TimeKeeperExamineEmpHoliday> holidayList = getEmpHoliday(empId, enterpriseCode, strStartDate
                        .substring(0, 7));
                for (int i = 0, k = 0; i < dateFromTo(strStartDate, strEndDate); i++) {
                    // 如果考勤日期存在，则插入需要最终返回的list里
                    if (k < list.size() && dateCompare(caStartDate, list.get(k).getId().getAttendanceDate()) == 0) {
                        listReturn.add(list.get(k));
                        k++;
                    } else {
                        // 如果该考勤日期记录不存在的情况处理如下
                        HrJWorkattendance tempBean = new HrJWorkattendance();
                        HrJWorkattendanceId tempId = new HrJWorkattendanceId();
                        tempId.setAttendanceDate(caStartDate.getTime());
                        tempBean.setId(tempId);
                        tempBean.setDeptId(isEmpty(deptId) ? null : Long.parseLong(deptId));
                        tempBean.setAttendanceDeptId(isEmpty(attendanceDeptId) ? isEmpty(deptId) ? null : Long
                                .parseLong(deptId) : Long.parseLong(attendanceDeptId));
                        // 如果上班休息FLG = 休息 1
                        if (workOrRestList.get(i).get("workOrRest").equals(FLAG_1)) {
                            String inHoliday = FLAG_N;
                            // 循环判断是否是假期
                            for (int j = 0; j < holidayList.size(); j++) {
                                // 如果当天的日期在一条假日范围内
                                if (dateCompare(caStartDate, holidayList.get(j).getStrStartDate()) >= 0
                                        && dateCompare(caStartDate, holidayList.get(j).getStrEndDate()) <= 0) {
                                    // 如果包含周末：1
                                    if (FLAG_1.equals(holidayList.get(j).getIfWeekEnd())) {
                                        // 设置为假别id
                                        tempBean.setVacationTypeId(Long.parseLong(holidayList.get(j)
                                                .getVacationTypeId()));
                                        // 如果不包含周末 0
                                    } else if ("0".equals(holidayList.get(j).getIfWeekEnd())) {
                                        // 设置为休息
                                        tempBean.setRestType(FLAG_1);
                                    }
                                    inHoliday = FLAG_Y;
                                    break;
                                }
                            }
                            // 如果不在假期内
                            if (inHoliday.equals(FLAG_N)) {
                                // 设置为休息
                                tempBean.setRestType(FLAG_1);
                            }

                            // 如果上班休息FLG = 上班 2
                        } else if (workOrRestList.get(i).get("workOrRest").equals(FLAG_2)) {
                            String inHoliday = FLAG_N;
                            // 循环判断是否是假期
                            for (int j = 0; j < holidayList.size(); j++) {
                                // 如果当天的日期在一条假日范围内
                                if (dateCompare(caStartDate, holidayList.get(j).getStrStartDate()) >= 0
                                        && dateCompare(caStartDate, holidayList.get(j).getStrEndDate()) <= 0) {
                                    // 设置为假别id
                                    tempBean.setVacationTypeId(Long.parseLong(holidayList.get(j).getVacationTypeId()));
                                    inHoliday = FLAG_Y;
                                    break;
                                }
                            }
                            // 如果不在假期内
                            if (inHoliday.equals(FLAG_N)) {
                                // 如果该员工是行政班
                                if (checkWorkKind(empId, enterpriseCode)) {
                                    // 则设置标准考勤时间
                                    tempBean.setAmBegingTime(timeTemp.getAmBegingTime());
                                    tempBean.setAmEndTime(timeTemp.getAmEndTime());
                                    tempBean.setPmBegingTime(timeTemp.getPmBegingTime());
                                    tempBean.setPmEndTime(timeTemp.getPmEndTime());
                                }
                            }
                        }
                        // 将该记录插入返回的list里
                        listReturn.add(tempBean);
                    }
                    caStartDate.add(Calendar.DAY_OF_MONTH, 1);
                }
                object.setList(listReturn);
                object.setTotalCount((long) listReturn.size());
            }
            LogUtil.log("EJB:已审核明细部查询结束。", Level.INFO, null);
            resultBean.setPobj(object);
            return resultBean;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:已审核明细部查询失败。", Level.SEVERE, e);
            throw e;
        }
    }

    /**
     * 判断员工是否为行政班
     * 
     * @param empId
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean checkWorkKind(String empId, String strAttendanceDate) {
        try {
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("  SELECT  ");
            sbSql.append("      B.WORK_KIND   ");
            sbSql.append("    FROM HR_J_EMP_INFO A      ");
            sbSql.append("      ,HR_C_STATION B      ");
            sbSql.append(" WHERE A.EMP_ID = ?      ");
            sbSql.append("   AND A.IS_USE = ?     ");
            sbSql.append("   AND A.ENTERPRISE_CODE = ?    ");
            sbSql.append("   AND B.IS_USE = ?  ");
            sbSql.append("   AND B.ENTERPRISE_CODE = ?    ");
            sbSql.append("   AND A.STATION_ID = B.STATION_ID    ");
            Object[] params = new Object[5];
            int i = 0;
            params[i++] = empId;
            params[i++] = IS_USE_Y;
            params[i++] = strAttendanceDate;
            params[i++] = IS_USE_Y;
            params[i++] = strAttendanceDate;
            List list = bll.queryByNativeSQL(sbSql.toString(), params);
            if (list.size() == 1) {
                Object data = list.get(0);
                if (FLAG_1.equals(data.toString())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 查询该员工的请假信息
     * 
     * @param empId
     * @param enterpriseCode
     * @param strAttendanceDate
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<TimeKeeperExamineEmpHoliday> getEmpHoliday(String empId, String enterpriseCode, String strAttendanceDate)
            throws Exception {
        LogUtil.log("EJB:查询该员工的请假信息开始。", Level.INFO, null);
        try {
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("  SELECT  ");
            sbSql.append("      A.VACATION_TYPE_ID   ");
            sbSql.append("      ,B.IF_WEEKEND      ");
            sbSql.append("      ,TO_CHAR(A.START_TIME,'YYYY-MM-DD')      ");
            sbSql.append("      ,TO_CHAR(A.END_TIME,'YYYY-MM-DD')      ");
            sbSql.append("  FROM HR_J_VACATION A      ");
            sbSql.append("      ,HR_C_VACATIONTYPE B      ");
            sbSql.append(" WHERE A.EMP_ID = ?      ");
            sbSql.append("   AND A.VACATION_TYPE_ID = B.VACATION_TYPE_ID      ");
            sbSql.append("   AND TO_CHAR(A.START_TIME , 'yyyy-mm') <= ?    ");
            sbSql.append("   AND TO_CHAR(A.END_TIME , 'yyyy-mm') >= ?      ");
            sbSql.append("   AND A.IS_USE = ?      ");
            sbSql.append("   AND A.ENTERPRISE_CODE = ?      ");
            sbSql.append("   AND B.IS_USE = ?      ");
            sbSql.append("   AND B.ENTERPRISE_CODE = ?      ");
            sbSql.append(" ORDER BY  A.START_TIME    ");
            Object[] params = new Object[7];
            int i = 0;
            params[i++] = empId;
            params[i++] = strAttendanceDate;
            params[i++] = strAttendanceDate;
            params[i++] = IS_USE_Y;
            params[i++] = enterpriseCode;
            params[i++] = IS_USE_Y;
            params[i++] = enterpriseCode;
            List lstResult = bll.queryByNativeSQL(sbSql.toString(), params);
            List<TimeKeeperExamineEmpHoliday> arrlist = new ArrayList<TimeKeeperExamineEmpHoliday>();
            Iterator it = lstResult.iterator();
            while (it.hasNext()) {
                TimeKeeperExamineEmpHoliday tempBean = new TimeKeeperExamineEmpHoliday();
                Object[] data = (Object[]) it.next();
                // 假别id
                if (null != data[0]) {
                    tempBean.setVacationTypeId(data[0].toString());
                }
                // 是否包含周末
                if (null != data[1]) {
                    tempBean.setIfWeekEnd(data[1].toString());
                }
                // 开始日期
                if (null != data[2]) {
                    tempBean.setStrStartDate(data[2].toString());
                }
                // 结束日期
                if (null != data[3]) {
                    tempBean.setStrEndDate(data[3].toString());
                }
                arrlist.add(tempBean);
            }
            LogUtil.log("EJB:查询该员工的请假信息结束。", Level.INFO, null);
            return arrlist;
        } catch (Exception e) {
            LogUtil.log("EJB:查询该员工的请假信息错误。", Level.SEVERE, null);
            throw e;
        }
    }

    /**
     * String 转 Calendar
     * 
     * @param strDate
     * @return
     * @throws ParseException
     */
    public Calendar stringToCalendar(String strDate) throws ParseException {
        Calendar cn = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date s = sdf.parse(strDate);
        cn.setTime(s);
        return cn;
    }

    /**
     * 返回同月两个日期间的天数
     * 
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public int dateFromTo(String startDate, String endDate) throws ParseException {
        return stringToCalendar(endDate).get(Calendar.DAY_OF_MONTH)
                - stringToCalendar(startDate).get(Calendar.DAY_OF_MONTH) + 1;
    }

    /**
     * 比较两个日期的先后，如果date2等于date1，则返回 0 值；如果date1 的时间在date2之前，则返回小于 0
     * 的值；如果date1在date2之后，则返回大于 0 的值。
     * 
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public int dateCompare(String date1, String date2) throws ParseException {
        return stringToCalendar(date1).compareTo(stringToCalendar(date2));
    }

    /**
     * * 比较两个日期的先后，如果date2等于date1，则返回 0 值；如果date1 的时间在date2之前，则返回小于 0
     * 的值；如果date1在date2之后，则返回大于 0 的值。
     * 
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public int dateCompare(Calendar date1, Date date2) throws ParseException {
        Calendar ct = Calendar.getInstance();
        ct.setTime(date2);
        return date1.compareTo(ct);
    }

    /**
     * * 比较两个日期的先后，如果date2等于date1，则返回 0 值；如果date1 的时间在date2之前，则返回小于 0
     * 的值；如果date1在date2之后，则返回大于 0 的值。
     * 
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public int dateCompare(String date1, Date date2) throws ParseException {
        Calendar ct = Calendar.getInstance();
        ct.setTime(date2);
        return stringToCalendar(date1).compareTo(ct);
    }

    /**
     * * 比较两个日期的先后，如果date2等于date1，则返回 0 值；如果date1 的时间在date2之前，则返回小于 0
     * 的值；如果date1在date2之后，则返回大于 0 的值。
     * 
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public int dateCompare(Calendar date1, String date2) throws ParseException {
        return date1.compareTo(stringToCalendar(date2));
    }

    /**
     * 批量保存员工考勤记录
     * 
     * @param records
     * @param enterpriseCode
     * @param workerCode
     * @param empId
     * @throws DataChangeException
     * @throws DataFormatException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveEmpAttendance(ArrayList<Map<String, Object>> records, String enterpriseCode, String workerCode,
            String empId) throws DataChangeException, DataFormatException {
        try {
            LogUtil.log("EJB:批量保存员工考勤记录开始。", Level.INFO, null);
            for (int i = 0; i < records.size(); i++) {
                Map<String, Object> map = records.get(i);
                HrJWorkattendance bean = new HrJWorkattendance();
                // 如果该记录存在，则更新
                if (map.get("id.empId") != null) {
                    HrJWorkattendanceId workAttendanceId = new HrJWorkattendanceId();
                    // 设置ID
                    workAttendanceId.setEmpId(Long.parseLong(objectToString(map.get("id.empId"))));
                    // 设置考勤日期
                    workAttendanceId.setAttendanceDate(formatStringToDate((String) map.get("id.attendanceDate"),
                            DATE_FORMAT));
                    // 根据人员ID和考勤日期查询考勤记录
                    bean = empAttendanceManager.findById(workAttendanceId, enterpriseCode);
                    // 结果为空
                    if (bean == null) {
                        throw new DataChangeException(BLANK_STRING);
                    }
                }
                // 设置部门
                bean.setDeptId((Long) map.get("deptId"));
                // 设置考勤部门
                bean.setAttendanceDeptId((Long) map.get("attendanceDeptId"));
                // 设置上午上班时间
                bean.setAmBegingTime(objectToString(map.get("amBegingTime")));
                // 设置上午下班时间
                bean.setAmEndTime(objectToString(map.get("amEndTime")));
                // 设置下午上班时间
                bean.setPmBegingTime(objectToString(map.get("pmBegingTime")));
                // 设置下午下班时间
                bean.setPmEndTime(objectToString(map.get("pmEndTime")));
                // 设置运行班类别
                if (!BLANK_STRING.equals(objectToString(map.get("workShiftId")))) {
                    bean.setWorkShiftId(Long.parseLong(objectToString(map.get("workShiftId"))));
                } else {
                    bean.setWorkShiftId(null);
                }
                // 设置假别ID
                if (!BLANK_STRING.equals(objectToString(map.get("vacationTypeId")))) {
                    bean.setVacationTypeId(Long.parseLong(objectToString(map.get("vacationTypeId"))));
                } else {
                    bean.setVacationTypeId(null);
                }
                // 设置加班类别ID
                if (!BLANK_STRING.equals(objectToString(map.get("overtimeTypeId")))) {
                    bean.setOvertimeTypeId(Long.parseLong(objectToString(map.get("overtimeTypeId"))));
                } else {
                    bean.setOvertimeTypeId(null);
                }
                // 设置出勤
                bean.setWork(objectToString(map.get("work")));
                // 设置休息
                if (BLANK_STRING.equals(objectToString(map.get("restType")))) {
                    bean.setRestType(FLAG_0);
                } else {
                    bean.setRestType(objectToString(map.get("restType")));
                }
                // 设置旷工
                if (BLANK_STRING.equals(objectToString(map.get("absentWork")))) {
                    bean.setAbsentWork(FLAG_0);
                } else {
                    bean.setAbsentWork(objectToString(map.get("absentWork")));
                }
                // 设置迟到
                bean.setLateWork(objectToString(map.get("lateWork")));
                // 设置早退
                bean.setLeaveEarly(objectToString(map.get("leaveEarly")));
                // 设置外借
                if (BLANK_STRING.equals(objectToString(map.get("outWork")))) {
                    bean.setOutWork(FLAG_0);
                } else {
                    bean.setOutWork(objectToString(map.get("outWork")));
                }
                // 设置出差
                if (BLANK_STRING.equals(objectToString(map.get("evectionType")))) {
                    bean.setEvectionType(FLAG_0);
                } else {
                    bean.setEvectionType(objectToString(map.get("evectionType")));
                }
                
                
                
             // add by liuyi 20100202 
				// 设置加班时间Id
				if(!objectToString(map.get("overtimeTimeId")).equals(""))
					bean.setOvertimeTimeId(Long.parseLong(objectToString(map.get("overtimeTimeId"))));
				// 设置病假时间id
				if(!objectToString(map.get("sickLeaveTimeId")).equals(""))
					bean.setSickLeaveTimeId(Long.parseLong(objectToString(map.get("sickLeaveTimeId"))));
				// 事假时间Id
				if(!objectToString(map.get("eventTimeId")).equals(""))
					bean.setEventTimeId(Long.parseLong(objectToString(map.get("eventTimeId"))));
				// 旷工时间Id
				if(!objectToString(map.get("absentTimeId")).equals(""))
					bean.setAbsentTimeId(Long.parseLong(objectToString(map.get("absentTimeId"))));
				// 其他请假时间id
				if(!objectToString(map.get("otherTimeId")).equals(""))
					bean.setOtherTimeId(Long.parseLong(objectToString(map.get("otherTimeId"))));
				// add by liuyi 20100202 增加四个时间id结束
				
				
				
                // 设置备注
                bean.setMemo(objectToString(map.get("memo")));
                // 设置上次修改人
                bean.setLastModifiyBy(workerCode);
                // 设置是否使用
                bean.setIsUse(FLAG_Y);
                // 设置企业编码
                bean.setEnterpriseCode(enterpriseCode);
                if (map.get("id.empId") == null) {
                    // 如果该记录不存在则插入
                    HrJWorkattendanceId workAttendanceId = new HrJWorkattendanceId();
                    // 设置人员ID
                    workAttendanceId.setEmpId(Long.parseLong(empId));
                    // 设置考勤日期
                    workAttendanceId.setAttendanceDate(formatStringToDate((String) map.get("id.attendanceDate"),
                            DATE_FORMAT));
                    bean.setId(workAttendanceId);
                    bean.setInsertby(workerCode);
                    // 设置记录日期
                    bean.setInsertdate(new Date());
                    // 设置上次修改日期
                    bean.setLastModifiyDate(new Date());
                    // 保存
                    empAttendanceManager.save(bean);
                } else if (map.get("id.empId") != null) {
                    // 设置上次修改时间
                    bean
                            .setLastModifiyDate(formatStringToDate(objectToString(map.get("lastModifiyDate")),
                                    TIME_FORMAT));
                    // 更新
                    empAttendanceManager.update(bean);
                }
            }
            LogUtil.log("EJB:批量保存员工考勤记录结束。", Level.INFO, null);
        } catch (DataChangeException e) {
            LogUtil.log("EJB:批量保存员工考勤记录错误。（排他错误）", Level.SEVERE, null);
            ctx.setRollbackOnly();
            throw e;
        } catch (DataFormatException e) {
            LogUtil.log("EJB:批量保存员工考勤记录错误。（数据格式化错误）", Level.SEVERE, null);
            ctx.setRollbackOnly();
            throw e;
        } catch (RuntimeException re) {
            LogUtil.log("EJB:批量保存员工考勤记录错误。（db操作失败）", Level.SEVERE, re);
            ctx.setRollbackOnly();
            throw re;
        }

    }

    /**
     * 根据日期日期字符串和形式返回日期
     * 
     * @param argDateStr
     *            日期字符串
     * @param argFormat
     *            日期形式字符串
     * @return 日期
     */
    private Date formatStringToDate(String argDateStr, String argFormat) throws DataFormatException {
        if (argDateStr == null || argDateStr.trim().length() < 1) {
            return null;
        }
        argDateStr = argDateStr.replace('T', ' ');
        // 日期形式
        SimpleDateFormat sdfFrom = null;
        // 返回日期
        Date dtresult = null;

        sdfFrom = new SimpleDateFormat(argFormat);
        // 格式化日期
        try {
            dtresult = sdfFrom.parse(argDateStr);
        } catch (ParseException e) {
            throw new DataFormatException();
        }
        return dtresult;
    }

    /**
     * 判断值是否为空
     * 
     * @param strValue
     *            值
     * @return 是否为空
     */
    public boolean isEmpty(Object objValue) {
        return objValue == null || BLANK_STRING.equals(objValue.toString());
    }

    /**
     * 将对象转化为字符
     * 
     * @param objValue
     *            对象
     * @return 字符
     */
    public String objectToString(Object objValue) {
        if (objValue == null) {
            return BLANK_STRING;
        } else {
            return objValue.toString();
        }
    }

    /**
     * 撤销前回审核
     * 
     * @param attendanceDeptId
     * @param checkYear
     * @param checkMonth
     * @throws DataChangeException
     * @throws DataFormatException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void cancelLastCheck(ArrayList<Map<String, Object>> arrlist, String enterpriseCode, String workerCode)
            throws DataChangeException, DataFormatException {
        try {
            LogUtil.log("EJB:撤销前回审核开始。", Level.INFO, null);
            for (int i = 0; i < arrlist.size(); i++) {
                Map<String, Object> tempDept = arrlist.get(i);
                HrJAttendancecheckId checkId = new HrJAttendancecheckId();
                checkId.setAttendanceDep(Long.parseLong(tempDept.get("examineBDeptId").toString()));
                checkId.setAttendanceYear(tempDept.get("attendanceYear").toString());
                checkId.setAttendanceMonth(tempDept.get("attendanceMonth").toString());
                HrJAttendancecheck bean = attendanceCheckRemote.findById(checkId, enterpriseCode);
                if (isEmpty(bean.getCheckedDate2()) || isEmpty(bean.getDepCharge2())) {
                    throw new DataChangeException(null);
                }
                bean.setCheckedDate2(null);
                bean.setDepCharge2(null);
                bean.setLastModifiyDate(formatStringToDate(tempDept.get("lastModifiyDate").toString(), TIME_FORMAT));
                attendanceCheckRemote.update(bean);
            }
            LogUtil.log("EJB:撤销前回审核结束。", Level.INFO, null);
        } catch (DataChangeException e) {
            LogUtil.log("EJB:撤销前回审核错误。（排他错误）", Level.SEVERE, null);
            ctx.setRollbackOnly();
            throw e;
        } catch (DataFormatException e) {
            LogUtil.log("EJB:撤销前回审核错误。（数据格式化错误）", Level.SEVERE, null);
            ctx.setRollbackOnly();
            throw e;
        } catch (RuntimeException re) {
            LogUtil.log("EJB:撤销前回审核错误。（db操作失败）", Level.SEVERE, re);
            ctx.setRollbackOnly();
            throw re;
        }

    }

}
