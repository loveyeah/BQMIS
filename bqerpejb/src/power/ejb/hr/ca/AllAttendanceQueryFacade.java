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

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.DataChangeException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 全公司考勤查询实现
 * 
 * @author huangweijie
 */
@Stateless
public class AllAttendanceQueryFacade implements AllAttendanceQueryFacadeRemote {
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    /** 是否使用: 是 */
    private static final String IS_USE_Y = "Y";
    /** flag: 是 */
    private static final String FLAG_Y = "Y";
    /** flag: 否 */
    private static final String FLAG_N = "N";
    /** 标识: 0 */
    private static final String FLAG_0 = "0";
    /** 上班休息标志1 休息 */
    private static final String FLAG_1 = "1";
    /** 上班休息标志2 上班 */
    private static final String FLAG_2 = "2";
    /** 画面日期（中文） */
    private static final String FLAG_WEB = "WEB";
    /** 报表日期（阿拉伯数字） */
    private static final String FLAG_RPT = "RPT";
    /** 空字符串 */
    public static final String BLANK_STRING = "";
    /** 日期格式 */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /** 日期的头：D */
    public static final String DATE_HEADER = "D";
    /** 合计项的头：D */
    public static final String CONCAT_HEADER = "C";
    /** 开始日期结束日期集 */
    public List<Map<String, Object>> dayFields = new ArrayList<Map<String, Object>>();
    /** 合计项表头集 */
    public List<HrCStatitem> totalNameList = new ArrayList<HrCStatitem>();

    /**
     * 全公司考勤查询方法
     * @param examineDate 审核年月
     * @param argDeptId 审核部门ID
     * @param argEnterpriseCode 企业编码
     */
    @SuppressWarnings("unchecked")
    public TimeKeeperExamineForm getExamineInfo(String examineDate,
            String argDeptId, String argEnterpriseCode, Properties p, String empDeptId, String webOrRpt)
            throws Exception {
        LogUtil.log("EJB:全公司考勤查询开始", Level.INFO, null);
        try {
            TimeKeeperExamineForm dataAll = new TimeKeeperExamineForm();
            StoreObject obj = new StoreObject();
            // 初始化画面为未审核
            dataAll.setCheckFlag(FLAG_N);
            String strYear = examineDate.substring(0, 4);
            String strMonth = examineDate.substring(5, 7);
            StringBuilder sbd = new StringBuilder();
            String strStartDate = "";
            String strEndDate = "";
            // 标准出勤时间
            String standardTime = "";
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
            sbd.append("    C.CHS_NAME AS AN,    ");
            sbd.append("    B.ATTEND_CHECKER_ID,    ");
            sbd.append("    D.CHS_NAME AS BN    ");
            sbd.append("FROM    ");
            sbd.append("    HR_C_ATTENDANCEDEP B    ");
            sbd.append("    LEFT JOIN HR_J_ATTENDANCECHECK A ON  A.ATTENDANCE_YEAR = ?     ");
            sbd.append("    AND A.ATTENDANCE_MONTH = ?    ");
            sbd.append("    AND A.ATTENDANCE_DEP = B.ATTENDANCE_DEPT_ID    ");
            sbd.append("    AND A.IS_USE = ?    ");
            sbd.append("    AND A.ENTERPRISE_CODE = ?    ");
            sbd.append("    LEFT JOIN HR_J_EMP_INFO C ON A.DEP_CHARGE1 = C.EMP_ID   ");
            sbd.append("    AND C.IS_USE = ?    ");
            sbd.append("    AND C.ENTERPRISE_CODE = ?    ");
            sbd.append("    LEFT JOIN HR_J_EMP_INFO D ON A.DEP_CHARGE2 = D.EMP_ID   ");
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
            Long totalCount = 0L;
            if (lstResult != null) {
                totalCount = (long) lstResult.size();
            }
            List<TimeKeeperExamine> arrlist = new ArrayList<TimeKeeperExamine>();
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
                
                // 如果不是本部门（即是子部门），且审核人为空，则把本部门的审核人赋值给它
                if (!argDeptId.equals(tempBean.getExamineBDeptId())
                		&& null == tempBean.getExamineMan2() 
                		|| BLANK_STRING.equals(tempBean.getExamineMan2())) {
                	TimeKeeperExamine chgInfo = getFatherDeptChargeMan(tempBean.getExamineBDeptId(),
                			argEnterpriseCode, strYear, strMonth);
                	if (null != chgInfo && tempBean.getExamineBDeptId().equals(chgInfo.getExamineADept())) {
                		if (null != chgInfo.getExamineMan2()) {
                			tempBean.setExamineADept(chgInfo.getExamineADept());
		                	tempBean.setMan2ChsName(chgInfo.getExamineMan2());
		                	tempBean.setExamineDate2(chgInfo.getExamineDate2());
                		}
                	}
                }
                tempBean.setCancelFlag(FLAG_N);
                arrlist.add(tempBean);
            }
            
            
            // 如果查询有结果
            if (arrlist.size() > 0) {
                // 所有的包括父部门子部门查询信息
                PageObject pObj = new PageObject();
                pObj.setList(arrlist);
                pObj.setTotalCount((long) arrlist.size());
                dataAll.setDeptAllStore(pObj);
                dataAll.setStrExamine(arrlist.get(0).getMan2ChsName());
                dataAll.setStrAttendance(arrlist.get(0).getMan1ChsName());
                dataAll.setStrExamineDeptName(arrlist.get(0)
                        .getExamineBDeptName());
                dataAll.setStrExamineDeptId(arrlist.get(0).getExamineBDeptId());
                List<String> detpList = new ArrayList<String>();
                for (int j = 0; j < arrlist.size(); j++) {
                	// 如果为null则不添加到部门list中
                	if (null != arrlist.get(j).getExamineBDeptId()) {
                		detpList.add(arrlist.get(j).getExamineBDeptId());
                	}
                }
                // 获取审核部门
                String tempDept = arrlist.get(0).getExamineADept();
                // 如果审核部门=画面的审核部门，且审核人2不为空的场合。
                if (argDeptId.equals(tempDept)
                        && (null != arrlist.get(0).getExamineMan2())) {
                    // 画面设为已审核
                    dataAll.setCheckFlag(FLAG_Y);
                }
                // 获取该部门该年该月的开始日期结束日期
                AttendanceStandard startAndEndDateInfo = this
                        .getStartAndEndDate(strYear, strMonth, argDeptId,
                                argEnterpriseCode, empDeptId);
                // 开始日期结束日期不为空
                if (null != startAndEndDateInfo) {
                    strStartDate = startAndEndDateInfo.getStartDate();
                    strEndDate = startAndEndDateInfo.getEndDate();
                    standardTime = startAndEndDateInfo.getStandardTime();
                    dataAll.setStrStartDate(strStartDate);
                    dataAll.setStrEndDate(strEndDate);
                    // 设置返回值的metadata
                    obj.setMetaData(creatGridHeader(strStartDate, strEndDate,
                            argEnterpriseCode, p, webOrRpt));

                } else {
                    // 如设计书中，报错
                    throw new Exception("E");
                }
                StringBuilder sbdContent = new StringBuilder();
                sbdContent.append(" SELECT B.DEPT_ID    ");
                sbdContent.append("       ,B.DEPT_NAME  ");
                sbdContent.append("       ,C.EMP_ID ");
                sbdContent.append("       ,C.CHS_NAME   ");
                sbdContent
                        .append("       ,TO_CHAR(D.ATTENDANCE_DATE,'yyyy-mm-dd')    ");
                sbdContent
                        .append("       ,CONCAT(CONCAT(G.WORK_SHIFT_MARK,E.VACATION_MARK),OVERTIME_MARK) AS MARK  ");
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
                sbdContent.append("   FROM HR_C_DEPT B  ");
                sbdContent.append("       ,HR_J_EMP_INFO C  ");
                sbdContent
                        .append("   LEFT JOIN HR_J_WORKATTENDANCE D ON C.EMP_ID = D.EMP_ID    ");
                sbdContent
                        .append("    AND to_char(D.ATTENDANCE_DATE,'yyyy-mm-dd') >= ? ");
                sbdContent
                        .append("    AND to_char(D.ATTENDANCE_DATE,'yyyy-mm-dd') <= ? ");
                sbdContent.append("    AND D.IS_USE = ? ");
                sbdContent.append("    AND D.ENTERPRISE_CODE = ?    ");
                sbdContent
                        .append("   LEFT JOIN HR_C_VACATIONTYPE E ON D.VACATION_TYPE_ID = E.VACATION_TYPE_ID  ");
                sbdContent.append("    AND E.IS_USE = ? ");
                sbdContent.append("    AND E.ENTERPRISE_CODE = ?    ");
                sbdContent
                        .append("   LEFT JOIN HR_C_OVERTIME F ON D.OVERTIME_TYPE_ID = F.OVERTIME_TYPE_ID  ");
                sbdContent.append("    AND F.IS_USE = ? ");
                sbdContent.append("    AND F.ENTERPRISE_CODE = ?    ");
                sbdContent
                        .append("   LEFT JOIN HR_C_WORKSHIFT G ON G.WORK_SHIFT_ID = D.WORK_SHIFT_ID   ");
                sbdContent.append("    AND G.IS_USE = ? ");
                sbdContent.append("    AND G.ENTERPRISE_CODE = ?    ");
                sbdContent.append("  WHERE B.IS_USE = ? ");
                sbdContent.append("    AND B.DEPT_ID = C.DEPT_ID    ");
                sbdContent.append("    AND B.ENTERPRISE_CODE = ?    ");
                sbdContent.append("    AND C.IS_USE = ? ");
                sbdContent.append("    AND C.ENTERPRISE_CODE = ?    ");
                sbdContent.append("    AND NVL(C.ATTENDANCE_DEPT_ID, C.DEPT_ID) IN("
                        + detpList.toString().substring(1,
                                detpList.toString().length() - 1) + ")  ");
                sbdContent.append("  ORDER BY B.DEPT_ID ");
                sbdContent
                        .append("          ,C.EMP_ID ,D.ATTENDANCE_DATE    ");
                // 打印SQL语句
                LogUtil.log("EJB:sql=" + sbdContent.toString(), Level.INFO,
                        null);
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
                // modify by liuyi 090923 11:02 部门表中用U标识使用中
//                listParamsContent.add(IS_USE_Y);
                listParamsContent.add("Y");//update by sychen 20100831
//                listParamsContent.add("U");
                listParamsContent.add(argEnterpriseCode);
                listParamsContent.add(IS_USE_Y);
                listParamsContent.add(argEnterpriseCode);
                Object[] paramsContent = listParamsContent.toArray();
                List listContent = new ArrayList();
                // 判断部门list是否有值，没有则不做查询，否则引发缺失表达式错误。
                if (0 < detpList.size()) {
	                // 查询一条有参数sql语句
	                listContent = bll.queryByNativeSQL(sbdContent.toString(),
	                        paramsContent);
                }
                List<TimeKeeperExamineContent> arrListContent = new ArrayList<TimeKeeperExamineContent>();
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
                    if (null != data[16]) {
                        tempBean.setAttendanceDeptId(data[16].toString());
                    }
                    // 设置考勤标志
                    tempBean.setMark(totalMark(tempBean, p));
                    arrListContent.add(tempBean);
                }
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
                // 如果还有记录
                while (i < recordsCount) {
                    // 定义单元格
                    Map<Object, Object> cell = new HashMap<Object, Object>();
                    cell.put("empName", arrListContent.get(i).getEmpName());
                    cell.put("empId", arrListContent.get(i).getEmpId());
                    cell.put("deptName", arrListContent.get(i).getDeptName());
                    cell.put("deptId", arrListContent.get(i).getDeptId());
                    cell.put("attendanceDeptId", arrListContent.get(i)
                            .getAttendanceDeptId());

                    // 如果该员工的考勤时间为空，则添加30的空白记录
                    if(isEmpty(arrListContent.get(i).getAtendanceDate())){
                        for (int j = 0; j < daysCount; j++) {
                            cell.put(obj.getMetaData().getFields().get(j + 6).get("dataIndex"), "");
                            countTotalItem(arrListContent.get(i), cell, totalNameList, standardTime);
                        }
                        resultList.add(cell);
                        i++;
                        continue;
                    }
                    cs.setTime(s);
                    // 以天数作循环
                    for (int j = 0; j < daysCount; j++) {
                        Date e = null;
                        cn = Calendar.getInstance();
                        if (i < arrListContent.size()) {
                        	if (null != arrListContent.get(i).getAtendanceDate()) {
	                            e = sdf.parse(arrListContent.get(i).getAtendanceDate());
	                            cn.setTime(e);
                        	} else {
                        		cn = null;
                        	}
                        }
//                        cn.setTime(e);
                        // 如果有当天的记录
                        if (cs.equals(cn)
                                && cell.get("empId").equals(
                                        arrListContent.get(i).getEmpId())) {
                            countTotalItem(arrListContent.get(i), cell,
                                    totalNameList, standardTime);
                            cell.put(obj.getMetaData().getFields().get(j + 6)
                                    .get("dataIndex"), arrListContent.get(i)
                                    .getMark());
                            i++;
                        } else {
                            // 没有当天的记录则插入空白
                            cell.put(obj.getMetaData().getFields().get(j + 6)
                                    .get("dataIndex"), "");
                        }
                        cs.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    // 将该行记录插入整个数据集
                    resultList.add(cell);
                }

                // 设置list
                obj.setList(resultList);
                // 设置总数
                // modify by liuyi 091027 查询数据数目
//                obj.setTotalCount(totalCount);
                if(resultList != null)
                { 
                	Integer countInteger = resultList.size();
                	obj.setTotalCount(countInteger.longValue());
                }
                dataAll.setStore(obj);
                dataAll.setWorkOrRestList(dayFields);
                dataAll.setStrColor(p.getProperty("COLOR"));
                LogUtil.log("EJB:全公司考勤查询结束。", Level.INFO, null);
            }
            return dataAll;
        } catch (DataChangeException e) {
            LogUtil.log("EJB:全公司考勤查询错误。子部门未审核。", Level.SEVERE, e);
            throw e;
        } catch (ParseException e) {
            LogUtil.log("EJB:全公司考勤查询错误，日期转换失败。", Level.SEVERE, null);
            throw e;
        } catch (Exception e) {
            LogUtil.log("EJB:全公司考勤查询错误。", Level.SEVERE, e);
            throw e;
        }

    }

    public TimeKeeperExamine getFatherDeptChargeMan(String examineDeptId, String argEnterpriseCode,
    		String argYear, String argMonth) {
    	StringBuilder sbd = new StringBuilder();
    	sbd.append("   SELECT A.ATTENDANCE_DEPT_ID, A.TOP_CHECK_DEP_ID, C.CHS_NAME, B.CHECKED_DATE2  ");
    	sbd.append(" FROM HR_C_ATTENDANCEDEP A LEFT JOIN ");
    	sbd.append(" HR_J_ATTENDANCECHECK B ON A.TOP_CHECK_DEP_ID = B.ATTENDANCE_DEP ");
    	sbd.append(" AND B.ATTENDANCE_YEAR = ? AND B.ATTENDANCE_MONTH = ?  ");
    	sbd.append(" AND A.IS_USE = ? AND A.ENTERPRISE_CODE = ?  ");
    	sbd.append(" AND B.IS_USE = ? AND B.ENTERPRISE_CODE = ?  ");
    	sbd.append(" LEFT JOIN HR_J_EMP_INFO C ON C.EMP_ID = B.DEP_CHARGE2 ");
    	sbd.append(" AND C.IS_USE = ? AND C.ENTERPRISE_CODE = ?  ");
    	sbd.append("    	 WHERE A.ATTENDANCE_DEPT_ID = ?  ");
    	
    	List<String> listParams = new ArrayList<String>();
        listParams.add(argYear);
        listParams.add(argMonth);
        listParams.add(IS_USE_Y);
        listParams.add(argEnterpriseCode);
        listParams.add(IS_USE_Y);
        listParams.add(argEnterpriseCode);
        listParams.add(IS_USE_Y);
        listParams.add(argEnterpriseCode);
        listParams.add(examineDeptId);
        Object[] params = listParams.toArray();
        // 查询一条有参数sql语句
        List chgInfo = bll.queryByNativeSQL(sbd.toString(), params);
        
        
        if (null != chgInfo && chgInfo.size() == 1) {
        	TimeKeeperExamine tempBean = new TimeKeeperExamine();
            Object[] data = (Object[]) chgInfo.get(0);
            // 该部门
            if (null != data[0]) {
                tempBean.setExamineADept(data[0].toString());
            }
            // 该部门的父部门
            if (null != data[1]) {
                tempBean.setExamineBDeptId(data[1].toString());
            }
            // 该部门的父部门的审核人2
            if (null != data[2]) {
                tempBean.setExamineMan2(data[2].toString());
            }
            // 该部门的父部门的审核日期
            if (null != data[3]) {
                tempBean.setExamineDate2(data[3].toString());
            }
    	    return tempBean;
        } else {
        	return null;
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
        String returnMark = "";
        if(isEmpty(tempBean.getWorkShiftId()))
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
    private void countTotalItem(TimeKeeperExamineContent content,
            Map<Object, Object> map, List<HrCStatitem> totalNameList,
            String standardTime) {
        // 合计项
        HrCStatitem statItem = null;
        // 合计项对应id
        String statItemId = null;
        // 合计项单位
        String statItemUnit = null;
        // 合计项类型
        String statItemType = null;
        // 出勤flg
        String work_flg = FLAG_1;
        for (int index = 0; index < totalNameList.size(); index++) {

            statItem = totalNameList.get(index);
            statItemId = statItem.getStatItemId().toString();
            statItemUnit = CACodeConstants.ATTEND_UNIT_DAY;
            statItemType = statItem.getStatItemType();

            String key = "C" + index;
            boolean isAdd = false;

            // 加班合计
            if (CACodeConstants.STAT_ITEM_TYPE_OVERTIME.equals(statItemType)) {
                // 加班类别id相同
                if (statItemId != null
                        && statItemId.equals(content.getOvertimeTypeId())) {
                    isAdd = true;
                }
            }// 运行班合计
            else if (CACodeConstants.STAT_ITEM_TYPE_RUN.equals(statItemType)) {
                if (statItemId != null
                        && statItemId.equals(content.getWorkShiftId())) {
                    isAdd = true;
                }
            }// 假别统计
            else if (CACodeConstants.STAT_ITEM_TYPE_VACATIONID
                    .equals(statItemType)) {
                if (statItemId != null
                        && statItemId.equals(content.getVacationTypeId())) {
                    isAdd = true;
                }
            }// 出勤统计
            else if (CACodeConstants.STAT_ITEM_TYPE_WORK.equals(statItemType)) {
                // 出勤
                if (CACodeConstants.ATTENDANCE_TYPE_WORK.equals(statItemId)
                        && work_flg.equals(content.getWork())) {
                    isAdd = true;
                }
                // 休息
                else if (CACodeConstants.ATTENDANCE_TYPE_REST
                        .equals(statItemId)
                        && CACodeConstants.REST_FLG_YES.equals(content
                                .getRest())) {
                    isAdd = true;
                }
                // 旷工
                else if (CACodeConstants.ATTENDANCE_TYPE_ABSENT_WORK
                        .equals(statItemId)
                        && CACodeConstants.ABSENT_WORK_FLG_YES.equals(content
                                .getAbsent())) {
                    isAdd = true;
                }
                // 迟到
                else if (CACodeConstants.ATTENDANCE_TYPE_LATE_WORK
                        .equals(statItemId)
                        && CACodeConstants.LATE_FLG_YES.equals(content
                                .getLate())) {
                    isAdd = true;
                }
                // 早退
                else if (CACodeConstants.ATTENDANCE_TYPE_LEAVE_EARLY
                        .equals(statItemId)
                        && CACodeConstants.LEAVE_EARLY_FLG_YES.equals(content
                                .getLeave())) {
                    isAdd = true;
                }
                // 外借
                else if (CACodeConstants.ATTENDANCE_TYPE_OUT_WORK
                        .equals(statItemId)
                        && CACodeConstants.OUT_WORK_FLG_YES.equals(content
                                .getOut())) {
                    isAdd = true;
                }
                // 出差
                else if (CACodeConstants.ATTENDANCE_TYPE_EVECTION
                        .equals(statItemId)
                        && CACodeConstants.EVECTION_FLG_YES.equals(content
                                .getEvection())) {
                    isAdd = true;
                }
            }
            if (isAdd) {
                AddTotalItemValue(map, key, statItemUnit, standardTime);
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
    private void AddTotalItemValue(Map<Object, Object> map, Object key,
            String statItemUnit, String standardTime) {
        double addValue = 0;
        addValue = 1;
        Double value = (Double) map.get(key);
        if (value == null) {
            map.put(key, addValue);
        } else {
            map.put(key, value + addValue);
        }
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
    private AttendanceStandard getStartAndEndDate(String strYear,
            String strMonth, String argDeptId, String argEnterpriseCode, String empDeptId)
            throws Exception {
        try {
            LogUtil.log("EJB:查询考勤开始日期和结束日期开始", Level.INFO, null);
            StringBuilder sbd = new StringBuilder();
            sbd.append("SELECT    ");
            sbd.append("    A.ATTENDANCE_DEPT_ID,    ");
            sbd.append("    TO_CHAR(A.START_DATE , 'yyyy-mm-dd'),    ");
            sbd.append("    TO_CHAR(A.END_DATE , 'yyyy-mm-dd'),    ");
            sbd.append("    A.AM_BEGING_TIME,");
            sbd.append("    A.AM_END_TIME,    ");
            sbd.append("    A.PM_BEGING_TIME,    ");
            sbd.append("    A.PM_END_TIME,   ");
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
            if(!isEmpty(argDeptId)){
                listParams.add(argDeptId);
            }else{
                listParams.add(empDeptId);
            }
            Object[] params = listParams.toArray();
            // 查询一条有参数sql语句
            List lstResult = bll.queryByNativeSQL(sbd.toString(), params);
            AttendanceStandard tempBean = new AttendanceStandard();
            // 如果返回有结果且记录数为1
            if (lstResult.size() == 1) {
            	if (null != ((Object[])lstResult.get(0))[0])
                    tempBean.setAttendanceDeptId(((Object[]) lstResult.get(0))[0].toString());
            	if (null != ((Object[])lstResult.get(0))[1])
                    tempBean.setStartDate(((Object[]) lstResult.get(0))[1].toString());
            	if (null != ((Object[])lstResult.get(0))[2])
                    tempBean.setEndDate(((Object[]) lstResult.get(0))[2].toString());
            	if (null != ((Object[])lstResult.get(0))[3])
                    tempBean.setAmBegingTime(((Object[]) lstResult.get(0))[3].toString());
            	if (null != ((Object[])lstResult.get(0))[4])
                    tempBean.setAmEndTime(((Object[]) lstResult.get(0))[4].toString());
            	if (null != ((Object[])lstResult.get(0))[5])
                    tempBean.setPmBegingTime(((Object[]) lstResult.get(0))[5].toString());
            	if (null != ((Object[])lstResult.get(0))[6])
                    tempBean.setPmEndTime(((Object[]) lstResult.get(0))[6].toString());
            	if (null != ((Object[])lstResult.get(0))[7])
                    tempBean.setStandardTime(((Object[]) lstResult.get(0))[7].toString());
            } else {
                tempBean = null;
            }
            LogUtil.log("EJB:查询考勤开始日期和结束日期结束", Level.INFO, null);
            return tempBean;
        } catch (Exception e) {
            LogUtil.log("EJB:查询考勤开始日期和结束日期失败", Level.INFO, null);
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
    public List<HrCStatitem> getStatItemList(String enterpriseCode)
            throws Exception {
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
            LogUtil.log("EJB:考勤合计项维护初始化开始。", Level.INFO, null);
            LogUtil.log("EJB:SQL= " + sbd.toString(), Level.INFO, null);
            List<HrCStatitem> arrList = bll.queryByNativeSQL(sbd.toString(),
                    params, HrCStatitem.class);
            // 按查询结果集设置返回结果
            LogUtil.log("EJB:考勤合计项维护初始化结束。", Level.INFO, null);
            return arrList;
        } catch (Exception e) {
            LogUtil.log("EJB:考勤合计项维护初始化失败。", Level.SEVERE, null);
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
    private MetaData creatGridHeader(String strStartDate, String strEndDate,
            String enterpriseCode, Properties p, String webOrRpt) throws Exception {
        try {
            List<Map<String, Object>> fields = new ArrayList<Map<String, Object>>();
            dayFields.clear();
            totalNameList.clear();
            String root = "list";
            String totalProperty = "totalCount";
            MetaData metas = new MetaData();
            
            String[] daysRpt = { BLANK_STRING, "1", "2", "3", "4", "5", "6", "7", "8", "9",
                    "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                    "20", "21", "22", "23", "24", "25", "26", "27",
                    "28", "29", "30", "31" };
            String[] daysWeb = { BLANK_STRING, "一", "二", "三", "四", "五", "六", "七", "八", "九",
                    "十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九",
                    "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七",
                    "二十八", "二十九", "三十", "三十一" };
            
            String[] weeks = { BLANK_STRING, "周日 ", "周一 ", "周二 ", "周三 ", "周四 ", "周五 ",
                    "周六 " };
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Calendar cs = Calendar.getInstance();
            Calendar ce = Calendar.getInstance();
            Calendar ch = Calendar.getInstance();
            Date s = sdf.parse(strStartDate);
            Date e = sdf.parse(strEndDate);
            cs.setTime(s);
            ce.setTime(e);
            ce.add(Calendar.DAY_OF_MONTH, 1);
            String headerName = DATE_HEADER;
            String headerCountName = CONCAT_HEADER;
            int i = 0;
            int j = 0;
            Map<String, Object> tempLine = new HashMap<String, Object>();
            tempLine.put("name", "lineNum");
            fields.add(tempLine);
            Map<String, Object> tempEmpName = new HashMap<String, Object>();
            tempEmpName.put("name", "empName");
            tempEmpName.put("dataIndex", "empName");
            tempEmpName.put("header",
                    "<span style='line-height:2.6em'>员工姓名</span>");
            tempEmpName.put("align", "left");
            tempEmpName.put("sortable", "true");
            tempEmpName.put("width", 100);
            fields.add(tempEmpName);
            Map<String, Object> tempEmpId = new HashMap<String, Object>();
            tempEmpId.put("name", "empId");
            tempEmpId.put("dataIndex", "empId");
            tempEmpId.put("hidden", "true");
            fields.add(tempEmpId);
            Map<String, Object> tempEmpDept = new HashMap<String, Object>();
            tempEmpDept.put("name", "deptName");
            tempEmpDept.put("dataIndex", "deptName");
            tempEmpDept.put("header",
                    "<span style='line-height:2.6em'>所属部门</span>");
            tempEmpDept.put("sortable", "true");
            tempEmpDept.put("align", "left");
            tempEmpDept.put("width", 100);
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
            List<HrCHoliday> holidaysList = getHolidays(strStartDate,
                    strEndDate, enterpriseCode);
            do {
                Map<String, Object> tempDayInfo = new HashMap<String, Object>();
                tempDayInfo.put("day", cs.get(Calendar.DAY_OF_MONTH));
                tempDayInfo.put("week", cs.get(Calendar.DAY_OF_WEEK));
                if (j < holidaysList.size()) {
                    ch.setTime(holidaysList.get(j).getHolidayDate());
                }
                String workOrRestFlag = "";
                if (cs.get(Calendar.DAY_OF_WEEK) == 1
                        || cs.get(Calendar.DAY_OF_WEEK) == 7) {
                    workOrRestFlag = FLAG_1;
                    if (ch.equals(cs)
                            && FLAG_2.equals(holidaysList.get(j)
                                    .getHolidayType())) {
                        workOrRestFlag = FLAG_2;
                        j++;
                    }
                } else {
                    workOrRestFlag = FLAG_2;
                    if (ch.equals(cs)
                            && FLAG_1.equals(holidaysList.get(j)
                                    .getHolidayType())) {
                        workOrRestFlag = FLAG_1;
                        j++;
                    }
                }
                tempDayInfo.put("workOrRest", workOrRestFlag);
                dayFields.add(tempDayInfo);
                Map<String, Object> tempCm = new HashMap<String, Object>();
                tempCm.put("name", headerName + i);
                tempCm.put("dataIndex", headerName + i);
                if (FLAG_WEB.equals(webOrRpt)) {
	                tempCm.put("header",
	                        workOrRestFlag.equals(FLAG_1) ? "<font color='"
	                                + p.getProperty("COLOR") + "'>"
	                                + daysWeb[cs.get(Calendar.DAY_OF_MONTH)]
	                                + "<br />"
	                                + weeks[cs.get(Calendar.DAY_OF_WEEK)]
	                                + "</font>" : daysWeb[cs
	                                .get(Calendar.DAY_OF_MONTH)]
	                                + "<br />"
	                                + weeks[cs.get(Calendar.DAY_OF_WEEK)]);
                } else if (FLAG_RPT.equals(webOrRpt)) {
                	tempCm.put("header",
	                        workOrRestFlag.equals(FLAG_1) ? "<font color='"
	                                + p.getProperty("COLOR") + "'>"
	                                + daysRpt[cs.get(Calendar.DAY_OF_MONTH)]
	                                + "<br />"
	                                + weeks[cs.get(Calendar.DAY_OF_WEEK)]
	                                + "</font>" : daysRpt[cs
	                                .get(Calendar.DAY_OF_MONTH)]
	                                + "<br />"
	                                + weeks[cs.get(Calendar.DAY_OF_WEEK)]);
                }
                tempCm.put("align", "Center");
                tempCm.put("width", 75);
                tempCm.put("renderer", "setColor");
                fields.add(tempCm);
                cs.add(Calendar.DAY_OF_MONTH, 1);
                i++;
            } while (cs.before(ce));
            // 获取合计项列名信息
            totalNameList = getStatItemList(enterpriseCode);
            for (int index = 0; index < totalNameList.size(); index++) {
                String formatTime = "";
                formatTime = "setTotalCountNumber2";
                Map<String, Object> tempCm = new HashMap<String, Object>();
                tempCm.put("name", headerCountName + index);
                tempCm.put("dataIndex", headerCountName + index);
                tempCm.put("header", (null == totalNameList.get(index).getStatItemByname() ?
                		    totalNameList.get(index).getStatItemName() : 
                			totalNameList.get(index).getStatItemByname()));
                tempCm.put("align", "right");
                tempCm.put("renderer", formatTime);
                tempCm.put("sortable", "true");
                tempCm.put("width", 95);
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
    public List<HrCHoliday> getHolidays(String strStartDate, String strEndDate,
            String strEnterpriseCode) throws Exception {
        try {

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
            List<HrCHoliday> arrList = bll.queryByNativeSQL(sbd.toString(),
                    params, HrCHoliday.class);
            return arrList;
        } catch (Exception e) {
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
    public PageObject getDeptsByTopDeptid(String enterpriseCode) {
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
        sql.append("    T.ENTERPRISE_CODE = ?    ");
        sql.append("ORDER BY        ");
        sql.append("    T.ATTENDANCE_DEPT_ID    ");
        List list = bll.queryByNativeSQL(sql.toString(), new Object[] { FLAG_Y, FLAG_2, enterpriseCode});
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object[] data = (Object[]) it.next();
            HrCAttendancedep info = new HrCAttendancedep();
            // 部门id
            if (data[0] != null) {
                info.setAttendanceDeptId(Long.parseLong(data[0].toString()));
            }
            // 部门名称
            if (data[1] != null) {
                info.setAttendanceDeptName(data[1].toString());
            }
            listReturn.add(info);
        }
        obj.setList(listReturn);
        obj.setTotalCount((long)listReturn.size());
        LogUtil.log("EJB:通过上级审核部门取得考勤部门结束。", Level.INFO, null);
        return obj;
    }

    /**
     * 查询职工考勤记录
     * @param empId
     * @param enterpriseCode
     * @param strStartDate
     * @param strEndDate
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageObject getEmpAttendance(String empId, String enterpriseCode,
            String strStartDate, String strEndDate,
            String checkFlag,
            List<Map<String, Object>> workOrRestList, String attendanceDeptId, String empDeptId)
            throws Exception {
        LogUtil.log("EJB:已审核明细部查询开始。", Level.INFO, null);
        try {
            PageObject object = new PageObject();
            // sql
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("  SELECT * ");
            sbSql.append("  FROM HR_J_WORKATTENDANCE A    ");
            sbSql.append(" WHERE A.EMP_ID = ?    ");
            sbSql
                    .append("   AND TO_CHAR(A.ATTENDANCE_DATE,'YYYY-MM-DD') >= ?    ");
            sbSql
                    .append("   AND TO_CHAR(A.ATTENDANCE_DATE,'YYYY-MM-DD') <= ?    ");
            sbSql.append("   AND A.ENTERPRISE_CODE = ?    ");
            sbSql.append("   AND A.IS_USE = ?    ");
            sbSql.append(" ORDER BY  A.ATTENDANCE_DATE    ");
            // 查询
            List<HrJWorkattendance> list = bll.queryByNativeSQL(sbSql
                    .toString(), new Object[] { empId, strStartDate,
                    strEndDate, enterpriseCode, IS_USE_Y },
                    HrJWorkattendance.class);
            List<HrJWorkattendance> listReturn = new ArrayList<HrJWorkattendance>();
            Calendar caStartDate = stringToCalendar(strStartDate);
            if (FLAG_Y.equals(checkFlag)) {
                // 如果画面是审核的
                // 循环将查询的记录插入返回的list里，并补全空日期的记录
                for (int j = 0, i = 0; j < dateFromTo(strStartDate, strEndDate); j++) {
                    if (i < list.size() && 
                    !isEmpty(list.get(i).getId().getAttendanceDate()) && 
                    dateCompare(caStartDate, list.get(i).getId().getAttendanceDate()) == 0) {
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
	            for (int i = 0, k = 0; i < dateFromTo(strStartDate, strEndDate); i++) {
	                // 如果考勤日期存在，则插入需要最终返回的list里
	                if (k < list.size()
	                        && dateCompare(caStartDate, list.get(k).getId()
	                                .getAttendanceDate()) == 0) {
	                    listReturn.add(list.get(k));
	                    k++;
	                } 
	                caStartDate.add(Calendar.DAY_OF_MONTH, 1);
	            }
            }
            object.setList(listReturn);
            object.setTotalCount((long) listReturn.size());
            LogUtil.log("EJB:已审核明细部查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:已审核明细部查询失败。", Level.SEVERE, null);
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
                String data = (String) list.get(0);
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
     * 得到审核记录信息
     * @param examineDate 审核年月
     * @param argDeptId 审核部门ID
     * @param argEnterpriseCode 企业编码
     * @return PageObject 审核记录信息
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public PageObject getAuditeList(String examineDate, String argDeptId,
            String argEnterpriseCode) throws Exception {
        LogUtil.log("EJB:审核记录信息查询开始", Level.INFO, null);
        try {
            TimeKeeperExamineForm dataAll = new TimeKeeperExamineForm();
            // 初始化画面为未审核
            dataAll.setCheckFlag(FLAG_N);
            String strYear = examineDate.substring(0, 4);
            String strMonth = examineDate.substring(5, 7);
            StringBuilder sbd = new StringBuilder();
            sbd.append("SELECT   ");
            sbd.append("    B.ATTENDANCE_DEPT_NAME,    ");
            sbd.append("    TO_CHAR(A.CHECKED_DATE1 , 'yyyy-mm-dd'),    ");
            sbd.append("    TO_CHAR(A.CHECKED_DATE2 , 'yyyy-mm-dd'),    ");
            sbd.append("    C.CHS_NAME AS AN,    ");
            sbd.append("    D.CHS_NAME AS BN    ");
            sbd.append("FROM    ");
            sbd.append("    HR_C_ATTENDANCEDEP B    ");
            sbd.append("    LEFT JOIN HR_J_ATTENDANCECHECK A ");
            sbd.append("    ON A.ATTENDANCE_DEP = B.ATTENDANCE_DEPT_ID    ");
            sbd.append("    AND A.ATTENDANCE_YEAR = ?    ");
            sbd.append("    AND A.ATTENDANCE_MONTH = ?    ");
            sbd.append("    AND A.IS_USE = ?    ");
            sbd.append("    AND A.ENTERPRISE_CODE = ?    ");
            sbd.append("    LEFT JOIN HR_J_EMP_INFO C ON A.DEP_CHARGE1 = C.EMP_ID   ");
            sbd.append("    AND C.IS_USE = ?    ");
            sbd.append("    AND C.ENTERPRISE_CODE = ?    ");
            sbd.append("    LEFT JOIN HR_J_EMP_INFO D ON A.DEP_CHARGE2 = D.EMP_ID   ");
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
            List<TimeKeeperExamine> arrlist = new ArrayList<TimeKeeperExamine>();
            Iterator it = lstResult.iterator();
            boolean one = false;
            boolean two = false;
            while (it.hasNext()) {
            	one = false;
                two = false;
                TimeKeeperExamine tempBean = new TimeKeeperExamine();
                Object[] data = (Object[]) it.next();
                // 审核部门
                if (null != data[0]) {
                    tempBean.setExamineBDeptName(data[0].toString());
                }
                // 审核日期1
                if (null != data[1]) {
                    tempBean.setExamineDate1(data[1].toString());
                }
                // 审核日期2
                if (null != data[2]) {
                    tempBean.setExamineDate2(data[2].toString());
                }
                // 审核人1
                if (null != data[3]) {
                    tempBean.setExamineMan1(data[3].toString());
                    // 记录中有审核人1
                    one = true;
                }
                // 审核人2
                if (null != data[4]) {
                    tempBean.setExamineMan2(data[4].toString());
                    // 记录中有审核人2
                    two = true;
                }
                // 如果有审核人1或者审核人2存在，才把记录列出
                if (one || two) {
                    arrlist.add(tempBean);
                }
            }
            PageObject pObj = new PageObject();
            if (0 != arrlist.size()) {
                pObj.setList(arrlist);
                pObj.setTotalCount(Long.parseLong(String
                        .valueOf(arrlist.size())));
            } else {
                pObj = null;
            }
            LogUtil.log("EJB:审核记录信息查询结束", Level.INFO, null);
            return pObj;
        } catch (Exception e) {
            LogUtil.log("EJB:审核记录信息查询失败", Level.INFO, null);
            throw e;
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
    public List<TimeKeeperExamineEmpHoliday> getEmpHoliday(String empId,
            String enterpriseCode, String strAttendanceDate) throws Exception {
        try {
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("  SELECT  ");
            sbSql.append("      A.VACATIONID   ");
            sbSql.append("      ,B.IF_WEEKEND      ");
            sbSql.append("      ,TO_CHAR(A.START_TIME,'YYYY-MM-DD')      ");
            sbSql.append("      ,TO_CHAR(A.END_TIME,'YYYY-MM-DD')      ");
            sbSql.append("  FROM HR_J_VACATION A      ");
            sbSql.append("      ,HR_C_VACATIONTYPE B      ");
            sbSql.append(" WHERE A.EMP_ID = ?      ");
            sbSql
                    .append("   AND A.VACATION_TYPE_ID = B.VACATION_TYPE_ID      ");
            sbSql
                    .append("        AND (    TO_CHAR(A.START_TIME , 'yyyy-mm') <= ?      ");
            sbSql
                    .append("             AND TO_CHAR(A.END_TIME , 'yyyy-mm') >= ? )         ");
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
            return arrlist;
        } catch (Exception e) {
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
    public int dateFromTo(String startDate, String endDate)
            throws ParseException {
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
     * 判断值是否为空
     * 
     * @param strValue
     *            值
     * @return 是否为空
     */
    public boolean isEmpty(String strValue) {
        return BLANK_STRING.equals(strValue) || strValue == null;
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
}
