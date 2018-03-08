/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 考勤统计查询 AttendanceStatisticsQueryFacade.
 * 
 * @author fangjihu
 */
@Stateless
public class AttendanceStatisticsQueryFacade implements AttendanceStatisticsQueryFacadeRemote {

	// fields
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	private static String IS_USE ="Y";
	
	private static String STR_BLANK = "";
	private static String DEPT_SUM = "部门小计";
	private static String PDEPT_SUM = "上级部门小计";
	private static String SUM = "总计";
	private static String PDEPT = "上级部门";
	private static String DEPT = "部门";
	private static String NAME = "姓名";
	private static String SUBSUM  = "小计";
	
	private static String ZERO ="0";
	private static int ZERO_INT = 0;
	private static String ZERO_POINT = "0.00";
	private static String ZERO_POINT1 = "0.0";
	
	private static String ONE ="1";
	private static String TWO ="2";
	private static String THREE ="3";
	private static String FOUR ="4";
	private static String FIVE ="5";
	private static String SIX ="6";
	
	/**
	 * 部门出勤统计查询
	 * @param deptId 部门id
	 * @param year 考勤年份
	 * @param month 考勤月份
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findDeptOndutyStatisticsQueryInfo(String deptId,String year,String month,String enterpriseCode){
		
		// Log开始
		LogUtil.log("EJB:部门出勤统计查询开始。", Level.INFO, null);
        try {
        	@SuppressWarnings("unused")
			PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	StringBuilder strSql = new StringBuilder();
        	// 某个员工对应的出勤类别个数
        	StringBuilder strSqlCount = new StringBuilder();
        	strSql.append("SELECT B.ATTENDANCE_YEAR");
        	strSql.append(",B.ATTENDANCE_MONTH");
        	strSql.append(",B.DEPT_ID");
        	strSql.append(",A.DEPT_NAME");
        	strSql.append(",C.EMP_ID");
        	strSql.append(",C.CHS_NAME");
        	strSql.append(",B.ATTENDANCE_TYPE_ID");
        	strSql.append(",B.DAYS ");
        	strSql.append("FROM HR_C_DEPT A");
        	strSql.append(",HR_D_WORKDAYS B");
        	strSql.append(",HR_J_EMP_INFO C WHERE ");
        	strSql.append("B.DEPT_ID = A.DEPT_ID ");
        	strSql.append("AND B.EMP_ID = C.EMP_ID ");
        	strSql.append("AND A.IS_USE=? ");
        	strSql.append("AND B.IS_USE =? ");
        	strSql.append("AND C.IS_USE =? ");
        	strSql.append("AND A.ENTERPRISE_CODE = ? ");
        	strSql.append("AND B.ENTERPRISE_CODE = ? ");
        	strSql.append("AND C.ENTERPRISE_CODE = ? ");
        	strSql.append(" AND B.DAYS > ? ");
        	
        	// modify by liuyi 10:20 部门表中用U标识使用中
//        	listParams.add(IS_USE);
//        	listParams.add("U");//update by sychen 20100831
        	listParams.add("Y");
        	listParams.add(IS_USE);
        	listParams.add(IS_USE);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(ZERO_INT);
        	
        	strSqlCount.append("SELECT COUNT(B.ATTENDANCE_TYPE_ID) ");
        	strSqlCount.append("FROM HR_C_DEPT A");
        	strSqlCount.append(",HR_D_WORKDAYS B");
        	strSqlCount.append(",HR_J_EMP_INFO C WHERE ");
        	strSqlCount.append("B.DEPT_ID = A.DEPT_ID ");
        	strSqlCount.append("AND B.EMP_ID = C.EMP_ID ");
        	strSqlCount.append("AND A.IS_USE=? ");
        	strSqlCount.append("AND B.IS_USE =? ");
        	strSqlCount.append("AND C.IS_USE =? ");
        	strSqlCount.append("AND A.ENTERPRISE_CODE = ? ");
        	strSqlCount.append("AND B.ENTERPRISE_CODE = ? ");
        	strSqlCount.append("AND C.ENTERPRISE_CODE = ? ");
        	strSqlCount.append(" AND B.DAYS > ? ");
        	
        	if(deptId != null && !STR_BLANK.equals(deptId)){
        		strSql.append(" AND B.DEPT_ID IN (SELECT T.DEPT_ID ");
        		strSql.append("FROM HR_C_DEPT T WHERE T.IS_USE = ? AND T.ENTERPRISE_CODE = ? ");
        		strSql.append("START WITH T.DEPT_ID = ? ");
        		strSql.append("CONNECT BY PRIOR T.DEPT_ID = T.PDEPT_ID)");
        		listParams.add("Y");//update by sychen 20100831
//        		listParams.add("U");
        		listParams.add(enterpriseCode);
        		listParams.add(deptId);
        		
        		strSqlCount.append(" AND B.DEPT_ID IN (SELECT T.DEPT_ID ");
        		strSqlCount.append("FROM HR_C_DEPT T WHERE T.IS_USE = ?  AND T.ENTERPRISE_CODE = ? ");
        		strSqlCount.append("START WITH T.DEPT_ID = ? ");
        		strSqlCount.append("CONNECT BY PRIOR T.DEPT_ID = T.PDEPT_ID)");
        		
        	}
        	if(year != null && !STR_BLANK.equals(year)){
        		strSql.append(" AND B.ATTENDANCE_YEAR =? ");
        		listParams.add(year);
        		
        		strSqlCount.append(" AND B.ATTENDANCE_YEAR =? ");
        	}
        	if(month != null && !STR_BLANK.equals(month)){
        		strSql.append(" AND B.ATTENDANCE_MONTH =? ");
        		listParams.add(month);
        		
        		strSqlCount.append(" AND B.ATTENDANCE_MONTH =? ");
        	}
        	strSql.append(" ORDER BY B.ATTENDANCE_YEAR,B.ATTENDANCE_MONTH,B.DEPT_ID,B.EMP_ID");
        	strSqlCount.append(" GROUP BY (B.ATTENDANCE_YEAR,B.ATTENDANCE_MONTH,B.DEPT_ID,B.EMP_ID)");
        	strSqlCount.append(" ORDER BY B.ATTENDANCE_YEAR,B.ATTENDANCE_MONTH,B.DEPT_ID,B.EMP_ID");
        	
        	LogUtil.log("EJB:部门出勤统计查询开始。SQL=" + strSql.toString(), Level.INFO, null);
        	List<Object> list = bll.queryByNativeSQL(strSql.toString(), listParams.toArray());
        	PageObject obj = new PageObject();
			@SuppressWarnings("unused")
			List<DeptOndutyStatisticsQueryInfo> arraylist = new ArrayList<DeptOndutyStatisticsQueryInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DeptOndutyStatisticsQueryInfo model = new DeptOndutyStatisticsQueryInfo();
				if(data[0]!=null){
					model.setAttendanceYear(data[0].toString());
				}
				if(data[1]!=null){
					model.setAttendanceMonth(data[1].toString());
				}
				
				if(data[2]!=null){
					model.setDeptId(data[2].toString());
				}
				if(data[3]!=null){
					model.setDeptName(data[3].toString());
				}
				if(data[4]!=null){
					model.setEmpId(data[4].toString());
				}
				if(data[5]!=null){
					model.setChsName(data[5].toString());
				}
				if(data[6]!=null){
					model.setAttendanceTypeId(data[6].toString());
				}
				if(data[7]!=null){
					model.setDays(dataOperate(Double.parseDouble(data[7].toString())));
				}
				arraylist.add(model);
			}
			List<Object> listCount = bll.queryByNativeSQL(strSqlCount.toString(), listParams.toArray());
			List<DeptOndutyStatisticsQueryInfo> listForOutput = new ArrayList<DeptOndutyStatisticsQueryInfo>(
					list.size());
			// 用于存放某一个员工的特定出勤年月下的出勤类别的个数
			int k=0;
			// 用于循环设定值
			int j = 0;
			// 用于不变值的设定，以避免重复设定
			int copyJ =0;
			String attendanceTypeId="";
			for(int i =0;i<listCount.size();i++){
				k = Integer.parseInt(listCount.get(i).toString())+j;
				DeptOndutyStatisticsQueryInfo info = new DeptOndutyStatisticsQueryInfo();
				copyJ =j;
				while(j<k){
					attendanceTypeId = arraylist.get(j).getAttendanceTypeId();
					if (j == copyJ) {
						info.setAttendanceMonth(arraylist.get(j)
								.getAttendanceMonth());
						info.setAttendanceYear(arraylist.get(j)
								.getAttendanceYear());
						info.setDeptId(arraylist.get(j).getDeptId());
						info.setDeptName(arraylist.get(j).getDeptName());
						info.setEmpId(arraylist.get(j).getEmpId());
						info.setChsName(arraylist.get(j).getChsName());
					}
					if(ZERO.equals(attendanceTypeId)){
						if(!ZERO_POINT.equals(arraylist.get(j).getDays())){
						info.setDays0(arraylist.get(j).getDays());
						}
					}
					if(ONE.equals(attendanceTypeId)){
						if(!ZERO_POINT.equals(arraylist.get(j).getDays())){
						info.setDays1(arraylist.get(j).getDays());
						}
					}
					if(TWO.equals(attendanceTypeId)){
						if(!ZERO_POINT.equals(arraylist.get(j).getDays())){
						info.setDays2(arraylist.get(j).getDays());
						}
					}
					if(THREE.equals(attendanceTypeId)){
						if(!ZERO_POINT.equals(arraylist.get(j).getDays())){
						info.setDays3(arraylist.get(j).getDays());
						}
					}
					if(FOUR.equals(attendanceTypeId)){
						if (!ZERO_POINT.equals(arraylist.get(j).getDays())) {
							info.setDays4(arraylist.get(j).getDays());
						}
					}
					if(FIVE.equals(attendanceTypeId)){
						if(!ZERO_POINT.equals(arraylist.get(j).getDays())){
							info.setDays5(arraylist.get(j).getDays());
						}
					}
					if(SIX.equals(attendanceTypeId)){
						if(!ZERO_POINT.equals(arraylist.get(j).getDays())){
							info.setDays6(arraylist.get(j).getDays());
						}
					}
					j++;
				}
				listForOutput.add(info);
			}
			obj.setList(listForOutput);
			obj.setTotalCount(new Long(listForOutput.size()));
			LogUtil.log("EJB:部门出勤统计查询结束。", Level.INFO, null);
        	return obj;
        }catch (RuntimeException e) {
        	LogUtil.log("EJB:部门出勤统计查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	
	/**
	 * 部门请假单查询
	 * @param deptId 部门id
	 * @param yearMonth 选择年月
	 * @param signState 签字状态
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findDeptleaveStatisticsQueryInfo(String deptId,String yearMonth,String signState,String enterpriseCode){
		// Log开始
		LogUtil.log("EJB:部门请假单查询开始。", Level.INFO, null);
        try {
        	@SuppressWarnings("unused")
			PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	StringBuilder strSql = new StringBuilder();
        	strSql.append("SELECT A.VACATIONID,");
        	strSql.append("A.DEPT_ID,C.DEPT_NAME,");
        	strSql.append("A.EMP_ID,B.CHS_NAME,");
        	strSql.append("A.VACATION_TYPE_ID,D.VACATION_TYPE,");
        	strSql.append("TO_CHAR(A.START_TIME,'YYYY-MM-dd hh24:mi'),");
        	strSql.append("TO_CHAR(A.END_TIME,'YYYY-MM-dd hh24:mi'),");
        	strSql.append("A.VACATION_DAYS,");
        	strSql.append("A.VACATION_TIME,");
        	strSql.append("A.REASON,");
        	strSql.append("A.WHITHER,");
        	strSql.append("A.MEMO,");
        	strSql.append("DECODE(A.IF_CLEAR,'0','否','1','是',A.IF_CLEAR),");
        	strSql.append("TO_CHAR(A.CLEAR_DATE,'YYYY-MM-dd hh24:mi'),");
        	strSql.append("DECODE(A.SIGN_STATE,'0','未上报','1','已上报','2','已终结','3','已退回',A.SIGN_STATE)" );
        	strSql.append("FROM HR_J_VACATION  A,");
        	strSql.append("HR_J_EMP_INFO B,");
        	strSql.append("HR_C_DEPT C,");
        	strSql.append("HR_C_VACATIONTYPE D ");
        	strSql.append("WHERE A.EMP_ID = B.EMP_ID ");
        	strSql.append("AND A.DEPT_ID = C.DEPT_ID ");
        	strSql.append("AND A.VACATION_TYPE_ID = D.VACATION_TYPE_ID ");
        	strSql.append("AND A.IS_USE =? ");
        	strSql.append("AND B.IS_USE =? ");
        	strSql.append("AND C.IS_USE =? ");
        	strSql.append("AND D.IS_USE =? ");
        	strSql.append("AND A.ENTERPRISE_CODE=? ");
        	strSql.append("AND B.ENTERPRISE_CODE =? ");
        	strSql.append("AND C.ENTERPRISE_CODE = ? ");
        	strSql.append("AND D.ENTERPRISE_CODE =? ");
        	strSql.append("AND A.VACATION_DAYS>? ");
        	strSql.append("AND A.VACATION_TIME>? ");
        	
        	listParams.add(IS_USE);
        	listParams.add(IS_USE);
        	// modify by liuyi 090923 10:34 部门表中用 U标识使用中
//        	listParams.add(IS_USE);
        	listParams.add("Y");//update by sychen 20100831
//        	listParams.add("U");
        	listParams.add(IS_USE);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(ZERO_INT);
        	listParams.add(ZERO_INT);
        	
        	if(deptId != null && !STR_BLANK.equals(deptId)){
        		strSql.append(" AND A.DEPT_ID IN (SELECT T.DEPT_ID ");
        		strSql.append("FROM HR_C_DEPT T WHERE T.IS_USE = ? AND T.ENTERPRISE_CODE = ? ");
        		strSql.append("START WITH T.DEPT_ID = ? ");
        		strSql.append("CONNECT BY PRIOR T.DEPT_ID = T.PDEPT_ID)");
        		listParams.add("Y");//update by sychen 20100831
//        		listParams.add("U");
        		listParams.add(enterpriseCode);
        		listParams.add(deptId);
        	}
        	
        	if(yearMonth != null && !STR_BLANK.equals(yearMonth)){
        		strSql.append(" AND TO_CHAR(A.START_TIME,'YYYY-MM') = ?");
        		listParams.add(yearMonth);
        		
        	}
        	if(signState != null && !STR_BLANK.equals(signState)){
        		strSql.append(" AND A.SIGN_STATE =? ");
        		listParams.add(signState);
        	}
        	strSql.append(" ORDER BY A.DEPT_ID,A.EMP_ID,A.START_TIME");
        	
        	LogUtil.log("EJB:部门请假单查询开始。SQL=" + strSql.toString(), Level.INFO, null);
        	List<Object> list = bll.queryByNativeSQL(strSql.toString(), listParams.toArray());
        	PageObject obj = new PageObject();
			@SuppressWarnings("unused")
			List<DeptleaveStatisticsQueryInfo> arraylist = new ArrayList<DeptleaveStatisticsQueryInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DeptleaveStatisticsQueryInfo model = new DeptleaveStatisticsQueryInfo();
				if(data[0]!=null){
					model.setVacationId(data[0].toString());
				}
				if(data[1]!=null){
					model.setDeptId(data[1].toString());
				}
				
				if(data[2]!=null){
					model.setDeptName(data[2].toString());
				}
				if(data[3]!=null){
					model.setEmpId(data[3].toString());
				}
				if(data[4]!=null){
					model.setChsName(data[4].toString());
				}
				if(data[5]!=null){
					model.setVacationTypeId(data[5].toString());
				}
				if(data[6]!=null){
					model.setVacationType((data[6].toString()));
				}
				if(data[7]!=null){
					model.setStartTime(data[7].toString());
				}
				if(data[8]!=null){
					model.setEndTime(data[8].toString());
				}
				if(data[9]!=null){
					model.setVacationDays(dataOperate(Double.parseDouble(data[9].toString())));
				}
				if(data[10]!=null){
					NumberFormat formatter = new DecimalFormat(ZERO_POINT1);
					model.setVacationTime(format(formatter.format(Double.parseDouble(data[10].toString()))));
				}
				if(data[11]!=null){
					model.setReason(data[11].toString());
				}
				if(data[12]!=null){
					model.setWhither(data[12].toString());
				}
				if(data[13]!=null){
					model.setMemo(data[13].toString());
				}
				if(data[14]!=null){
					model.setIfClear(data[14].toString());
				}
				if(data[15]!=null){
					model.setClearDate(data[15].toString());
				}
				if(data[16]!=null){
					model.setSignState(data[16].toString());
				}
				
				arraylist.add(model);
			}
			obj.setList(arraylist);
			obj.setTotalCount(new Long(arraylist.size()));
			LogUtil.log("EJB:部门请假单查询结束。", Level.INFO, null);
        	return obj;
        }catch (RuntimeException e) {
        	LogUtil.log("EJB:部门请假单查询失败。", Level.SEVERE, e);
            throw e;
        }
		
	}
	
	/**
	 * 请假统计查询
	 * @param year 考勤年份
	 * @param month 考勤月份
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findLeaveStatisticsQueryInfo(String year,String month,String enterpriseCode){
		// Log开始
		LogUtil.log("EJB:请假统计查询开始。", Level.INFO, null);
        try {
        	@SuppressWarnings("unused")
			PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	StringBuilder strSql = new StringBuilder();
        	strSql.append("	SELECT D.PDEPT_ID");
        	strSql.append(",D.DEPT_ID");
        	strSql.append(",V.EMP_ID");
        	strSql.append(",D1.DEPT_NAME AS PDEPT_NAME");
        	strSql.append(",D.DEPT_NAME");
        	strSql.append(",E.CHS_NAME");
        	strSql.append(",V.VACATION_TYPE_ID");
        	strSql.append(",V.DAYS");
        	strSql.append(",F.VACATION_TYPE");
        	strSql.append("  FROM HR_D_VACATIONTOTAL V");
        	strSql.append(",HR_C_DEPT D");
        	strSql.append(" left join HR_C_DEPT D1 ON D1.IS_USE=? ");
        	strSql.append(" AND D1.ENTERPRISE_CODE=? AND D.PDEPT_ID = D1.DEPT_ID");		
        	strSql.append(",HR_J_EMP_INFO E");
        	strSql.append(",HR_C_VACATIONTYPE F");
        	strSql.append(" WHERE V.IS_USE = ?");
        	strSql.append(" AND D.IS_USE = ?");
        	strSql.append(" AND E.IS_USE = ?");
        	strSql.append(" AND F.IS_USE = ?");
        	strSql.append(" AND V.ENTERPRISE_CODE = ?");
        	strSql.append(" AND D.ENTERPRISE_CODE = ?");
        	strSql.append(" AND E.ENTERPRISE_CODE = ?");
        	strSql.append(" AND F.ENTERPRISE_CODE = ?");
        	strSql.append(" AND V.EMP_ID = E.EMP_ID");
        	strSql.append(" AND V.DEPT_ID = D.DEPT_ID");
        	strSql.append(" AND F.VACATION_TYPE_ID = V.VACATION_TYPE_ID");
        	strSql.append(" AND V.DAYS > ? ");
        	listParams.add(IS_USE);
        	listParams.add(enterpriseCode);
        	listParams.add(IS_USE);
        	listParams.add("Y");//update by sychen 20100831
//        	listParams.add("U");
        	listParams.add(IS_USE);
        	listParams.add(IS_USE);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(ZERO_INT);
        	
        	if(year != null && !STR_BLANK.equals(year)){
        		strSql.append(" AND V.ATTENDANCE_YEAR = ? ");
        		listParams.add(year);
        	}
        	
        	if(month != null && !STR_BLANK.equals(month)){
        		strSql.append(" AND V.ATTENDANCE_MONTH = ? ");
        		listParams.add(month);
        	}
        	
        	strSql.append(" ORDER BY D.PDEPT_ID");
        	strSql.append(" ,D.DEPT_ID");
        	strSql.append(" ,V.EMP_ID");
        	strSql.append(" ,V.VACATION_TYPE_ID");
        	
        	LogUtil.log("EJB:请假统计查询开始。SQL=" + strSql.toString(), Level.INFO, null);
        	List<Object> list = bll.queryByNativeSQL(strSql.toString(), listParams.toArray());
        	@SuppressWarnings("unused")
			PageObject obj = new PageObject();
			@SuppressWarnings("unused")
			List<AttendanceStatisticsQueryInfo> arraylist = new ArrayList<AttendanceStatisticsQueryInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				AttendanceStatisticsQueryInfo model = new AttendanceStatisticsQueryInfo();
				if(data[0]!=null){
					model.setPdeptId(data[0].toString());
				}
				if(data[1]!=null){
					model.setDeptId(data[1].toString());
				}
				
				if(data[2]!=null){
					model.setEmpId(data[2].toString());
				}
				if(data[3]!=null){
					model.setPdeptName(data[3].toString());
				}
				if(data[4]!=null){
					model.setDeptName(data[4].toString());
				}
				if(data[5]!=null){
					model.setChsName(data[5].toString());
				}
				if(data[6]!=null){
					model.setTypeId((data[6].toString()));
				}
				if(data[7]!=null){
					model.setDays(Double.parseDouble(data[7].toString()));
				}
				if(data[8]!=null){
					model.setType(data[8].toString());
				}
				arraylist.add(model);
			}
			
			List<AttendanceStatisticsQueryInfo>  typeList = findVacationTypeId(enterpriseCode).getList();
			LogUtil.log("EJB:请假统计查询结束。", Level.INFO, null);
        	return dbDataTransfer(arraylist, typeList);
        }catch (RuntimeException e) {
        	LogUtil.log("EJB:请假统计查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 加班统计查询
	 * @param year 考勤年份
	 * @param month 考勤月份
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findWorkOvertimeStatisticsQueryInfo(String year,String month,String enterpriseCode){

		// Log开始
		LogUtil.log("EJB:加班统计查询开始。", Level.INFO, null);
        try {
        	@SuppressWarnings("unused")
			PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	StringBuilder strSql = new StringBuilder();
        	strSql.append("	SELECT D.PDEPT_ID");
        	strSql.append(",D.DEPT_ID");
        	strSql.append(",V.EMP_ID");
        	strSql.append(",D1.DEPT_NAME AS PDEPT_NAME");
        	strSql.append(",D.DEPT_NAME");
        	strSql.append(",E.CHS_NAME");
        	strSql.append(",V.OVERTIME_TYPE_ID");
        	strSql.append(",V.DAYS");
        	strSql.append(",F.OVERTIME_TYPE");
        	strSql.append("  FROM HR_D_OVERTIMETOTAL V");
        	strSql.append(",HR_C_DEPT D");
        	strSql.append(" left join HR_C_DEPT D1 ON D1.IS_USE=? ");
        	strSql.append(" AND D1.ENTERPRISE_CODE=? AND D.PDEPT_ID = D1.DEPT_ID");		
        	strSql.append(",HR_J_EMP_INFO E");
        	strSql.append(",HR_C_OVERTIME  F");
        	strSql.append(" WHERE V.IS_USE = ?");
        	strSql.append(" AND D.IS_USE = ?");
        	strSql.append(" AND E.IS_USE = ?");
        	strSql.append(" AND F.IS_USE = ?");
        	strSql.append(" AND V.ENTERPRISE_CODE = ?");
        	strSql.append(" AND D.ENTERPRISE_CODE = ?");
        	strSql.append(" AND E.ENTERPRISE_CODE = ?");
        	strSql.append(" AND F.ENTERPRISE_CODE = ?");
        	strSql.append(" AND V.EMP_ID = E.EMP_ID");
        	strSql.append(" AND V.DEPT_ID = D.DEPT_ID");
        	strSql.append(" AND F.OVERTIME_TYPE_ID = V.OVERTIME_TYPE_ID");
        	strSql.append(" AND V.DAYS > ? ");

        	listParams.add(IS_USE);
        	listParams.add(enterpriseCode);
        	listParams.add(IS_USE);
        	listParams.add("Y");//update by sychen 20100831
//        	listParams.add("U");
        	listParams.add(IS_USE);
        	listParams.add(IS_USE);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(ZERO_INT);
        	
        	if(year != null && !STR_BLANK.equals(year)){
        		strSql.append(" AND V.ATTENDANCE_YEAR = ? ");
        		listParams.add(year);
        	}
        	
        	if(month != null && !STR_BLANK.equals(month)){
        		strSql.append(" AND V.ATTENDANCE_MONTH = ? ");
        		listParams.add(month);
        	}
        	
        	strSql.append(" ORDER BY D.PDEPT_ID");
        	strSql.append(" ,D.DEPT_ID");
        	strSql.append(" ,V.EMP_ID");
        	strSql.append(" ,V.OVERTIME_TYPE_ID");
        	
        	LogUtil.log("EJB:加班统计查询开始。SQL=" + strSql.toString(), Level.INFO, null);
        	List<Object> list = bll.queryByNativeSQL(strSql.toString(), listParams.toArray());
        	@SuppressWarnings("unused")
			PageObject obj = new PageObject();
			@SuppressWarnings("unused")
			List<AttendanceStatisticsQueryInfo> arraylist = new ArrayList<AttendanceStatisticsQueryInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				AttendanceStatisticsQueryInfo model = new AttendanceStatisticsQueryInfo();
				if(data[0]!=null){
					model.setPdeptId(data[0].toString());
				}
				if(data[1]!=null){
					model.setDeptId(data[1].toString());
				}
				
				if(data[2]!=null){
					model.setEmpId(data[2].toString());
				}
				if(data[3]!=null){
					model.setPdeptName(data[3].toString());
				}
				if(data[4]!=null){
					model.setDeptName(data[4].toString());
				}
				if(data[5]!=null){
					model.setChsName(data[5].toString());
				}
				if(data[6]!=null){
					model.setTypeId((data[6].toString()));
				}
				if(data[7]!=null){
					model.setDays(Double.parseDouble(data[7].toString()));
				}
				if(data[8]!=null){
					model.setType(data[8].toString());
				}
				arraylist.add(model);
			}
			
			List<AttendanceStatisticsQueryInfo>  typeList = findOvertimeTypeId(enterpriseCode).getList();
			LogUtil.log("EJB:加班统计查询结束。", Level.INFO, null);
        	return dbDataTransfer(arraylist, typeList);
        }catch (RuntimeException e) {
        	LogUtil.log("EJB:加班统计查询失败。", Level.SEVERE, e);
            throw e;
        }
	
	}
	
	/**
	 * 加班类别维护表查询
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findOvertimeTypeId(String enterpriseCode){

		// Log开始
		LogUtil.log("EJB:加班类别维护表查询开始。", Level.INFO, null);
        try {
        	@SuppressWarnings("unused")
			PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	StringBuilder strSql = new StringBuilder();
        	strSql.append("SELECT A.OVERTIME_TYPE_ID,");
        	strSql.append("A.OVERTIME_TYPE ");
        	strSql.append("FROM  HR_C_OVERTIME A ");
        	strSql.append("WHERE A.IS_USE =? ");
        	strSql.append("AND A.ENTERPRISE_CODE=? ");
        	strSql.append("ORDER BY A.OVERTIME_TYPE_ID");
        	listParams.add(IS_USE);
        	listParams.add(enterpriseCode);
        	
        	LogUtil.log("EJB:加班类别维护表查询开始。SQL=" + strSql.toString(), Level.INFO, null);
        	List<Object> list = bll.queryByNativeSQL(strSql.toString(), listParams.toArray());
        	PageObject obj = new PageObject();
			@SuppressWarnings("unused")
			List<AttendanceStatisticsQueryInfo> arraylist = new ArrayList<AttendanceStatisticsQueryInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				AttendanceStatisticsQueryInfo model = new AttendanceStatisticsQueryInfo();
				if(data[0]!=null){
					model.setTypeId(data[0].toString());
				}
				if(data[1]!=null){
					model.setType(data[1].toString());
				}
				arraylist.add(model);
			}
			obj.setList(arraylist);
			obj.setTotalCount(new Long(arraylist.size()));
			LogUtil.log("EJB:加班类别维护表查询结束。", Level.INFO, null);
        	return obj;
        }catch (RuntimeException e) {
        	LogUtil.log("EJB:加班类别维护表查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	
	/**
	 * 运行班统计查询
	 * @param year 考勤年份
	 * @param month 考勤月份
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findWorkshiftStatisticsQueryInfo(String year,String month,String enterpriseCode){

		// Log开始
		LogUtil.log("EJB:运行班统计查询开始。", Level.INFO, null);
        try {
        	@SuppressWarnings("unused")
			PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	StringBuilder strSql = new StringBuilder();
        	strSql.append("	SELECT D.PDEPT_ID");
        	strSql.append(",D.DEPT_ID");
        	strSql.append(",V.EMP_ID");
        	strSql.append(",D1.DEPT_NAME AS PDEPT_NAME");
        	strSql.append(",D.DEPT_NAME");
        	strSql.append(",E.CHS_NAME");
        	strSql.append(",V.WORK_SHIFT_ID");
        	strSql.append(",V.DAYS");
        	strSql.append(",F.WORK_SHIFT");
        	strSql.append("  FROM HR_D_WORKSHIFTTOTAL V");
        	strSql.append(",HR_C_DEPT D");
        	strSql.append(" left join HR_C_DEPT D1 ON D1.IS_USE=? ");
        	strSql.append(" AND D1.ENTERPRISE_CODE=? AND D.PDEPT_ID = D1.DEPT_ID");		
        	strSql.append(",HR_J_EMP_INFO E");
        	strSql.append(",HR_C_WORKSHIFT F");
        	strSql.append(" WHERE V.IS_USE = ?");
        	strSql.append(" AND D.IS_USE = ?");
        	strSql.append(" AND E.IS_USE = ?");
        	strSql.append(" AND F.IS_USE = ?");
        	strSql.append(" AND V.ENTERPRISE_CODE = ?");
        	strSql.append(" AND D.ENTERPRISE_CODE = ?");
        	strSql.append(" AND E.ENTERPRISE_CODE = ?");
        	strSql.append(" AND F.ENTERPRISE_CODE = ?");
        	strSql.append(" AND V.EMP_ID = E.EMP_ID");
        	strSql.append(" AND V.DEPT_ID = D.DEPT_ID");
        	strSql.append(" AND F.WORK_SHIFT_ID = V.WORK_SHIFT_ID");
        	strSql.append(" AND V.DAYS > ? ");

        	listParams.add(IS_USE);
        	listParams.add(enterpriseCode);
        	listParams.add(IS_USE);
        	listParams.add("Y");//update by sychen 20100831
//        	listParams.add("U");
        	listParams.add(IS_USE);
        	listParams.add(IS_USE);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(enterpriseCode);
        	listParams.add(ZERO_INT);
        	
        	if(year != null && !STR_BLANK.equals(year)){
        		strSql.append(" AND V.ATTENDANCE_YEAR = ? ");
        		listParams.add(year);
        	}
        	
        	if(month != null && !STR_BLANK.equals(month)){
        		strSql.append(" AND V.ATTENDANCE_MONTH = ? ");
        		listParams.add(month);
        	}
        	
        	strSql.append(" ORDER BY D.PDEPT_ID");
        	strSql.append(" ,D.DEPT_ID");
        	strSql.append(" ,V.EMP_ID");
        	strSql.append(" ,V.WORK_SHIFT_ID");
        	
        	LogUtil.log("EJB:运行班统计查询开始。SQL=" + strSql.toString(), Level.INFO, null);
        	List<Object> list = bll.queryByNativeSQL(strSql.toString(), listParams.toArray());
        	@SuppressWarnings("unused")
			PageObject obj = new PageObject();
			@SuppressWarnings("unused")
			List<AttendanceStatisticsQueryInfo> arraylist = new ArrayList<AttendanceStatisticsQueryInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				AttendanceStatisticsQueryInfo model = new AttendanceStatisticsQueryInfo();
				if(data[0]!=null){
					model.setPdeptId(data[0].toString());
				}
				if(data[1]!=null){
					model.setDeptId(data[1].toString());
				}
				
				if(data[2]!=null){
					model.setEmpId(data[2].toString());
				}
				if(data[3]!=null){
					model.setPdeptName(data[3].toString());
				}
				if(data[4]!=null){
					model.setDeptName(data[4].toString());
				}
				if(data[5]!=null){
					model.setChsName(data[5].toString());
				}
				if(data[6]!=null){
					model.setTypeId((data[6].toString()));
				}
				if(data[7]!=null){
					model.setDays(Double.parseDouble(data[7].toString()));
				}
				if(data[8]!=null){
					model.setType(data[8].toString());
				}
				arraylist.add(model);
			}
			
			List<AttendanceStatisticsQueryInfo>  typeList = findWorkshiftTypeId(enterpriseCode).getList();
			LogUtil.log("EJB:运行班统计查询结束。", Level.INFO, null);
        	return dbDataTransfer(arraylist, typeList);
        }catch (RuntimeException e) {
        	LogUtil.log("EJB:运行班统计查询失败。", Level.SEVERE, e);
            throw e;
        }
	
	}
	
	/**
	 * 运行班类别维护表查询
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findWorkshiftTypeId(String enterpriseCode){


		// Log开始
		LogUtil.log("EJB:运行班类别维护表查询开始。", Level.INFO, null);
        try {
        	@SuppressWarnings("unused")
			PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	StringBuilder strSql = new StringBuilder();
        	strSql.append("SELECT A.WORK_SHIFT_ID,");
        	strSql.append("A.WORK_SHIFT ");
        	strSql.append("FROM  HR_C_WORKSHIFT A ");
        	strSql.append("WHERE A.IS_USE =? ");
        	strSql.append("AND A.ENTERPRISE_CODE=? ");
        	strSql.append("ORDER BY A.WORK_SHIFT_ID");
        	listParams.add(IS_USE);
        	listParams.add(enterpriseCode);
        	
        	LogUtil.log("EJB:运行班类别维护表查询开始。SQL=" + strSql.toString(), Level.INFO, null);
        	List<Object> list = bll.queryByNativeSQL(strSql.toString(), listParams.toArray());
        	PageObject obj = new PageObject();
			@SuppressWarnings("unused")
			List<AttendanceStatisticsQueryInfo> arraylist = new ArrayList<AttendanceStatisticsQueryInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				AttendanceStatisticsQueryInfo model = new AttendanceStatisticsQueryInfo();
				if(data[0]!=null){
					model.setTypeId(data[0].toString());
				}
				if(data[1]!=null){
					model.setType(data[1].toString());
				}
				arraylist.add(model);
			}
			obj.setList(arraylist);
			obj.setTotalCount(new Long(arraylist.size()));
			LogUtil.log("EJB:运行班类别维护表查询结束。", Level.INFO, null);
        	return obj;
        }catch (RuntimeException e) {
        	LogUtil.log("EJB:运行班类别维护表查询失败。", Level.SEVERE, e);
            throw e;
        }
	
	}
	
	@SuppressWarnings("unchecked")
	private PageObject dbDataTransfer(List<AttendanceStatisticsQueryInfo> arraylist,List<AttendanceStatisticsQueryInfo> typeList){
		
		// 用于导出的数据
		List outputList = new ArrayList();
		int i =0;
		String empId ="";
		String deptId = "";
		String pdeptId = "";
		// 部门合计
		Double deptSum =0.00d;
		Double pdeptSum = 0.00d;
		// 个人合计
		Double personSum = 0.00d;
		// 合计
		Double Sum = 0.00d;
		// 某个人的请假类型个数
		int personCount =0;
		int personCountTemp =0;
		// 某个部门下人员的请假个数
		int deptCount = 0;
		int deptCountTemp =0;
		// 某个父部门下的人员的请假个数
		int pdeptCount =0;
		int pdeptCountTemp =0;
		String daysStr = "";
		String typeId = "";
		int pdeptFlag =0;
		Double days = 0d;
		Map sumMap = new HashMap();
		while(i<arraylist.size()){
			AttendanceStatisticsQueryInfo info = arraylist.get(i);
			// 父部门id
			pdeptId = info.getPdeptId();
			// 父部门下的请假人员的请假类别个数的和
			pdeptCount = findSamePDeptIdCount(arraylist, pdeptId);
			int j =0;
			// 父部门下请假类别对应的请假天数的合计
			Map sumPDeptMap = new HashMap();
			pdeptSum = 0.00d;
			pdeptFlag =0; 
			while(j < pdeptCount){
				// 部门id
				deptId  = arraylist.get(j+pdeptCountTemp).getDeptId();
				// 该部门下的请假人员的请假类别个数的和
				deptCount = findSameDeptIdCount(arraylist, deptId,pdeptId);
				int k = 0;
				// 该部门下请假类别对应的请假天数的合计
				Map sumDeptMap = new HashMap();
				int flag = 0;
				 deptSum = 0.00d;
				while(k<deptCount){
					empId = arraylist.get(k+deptCountTemp).getEmpId();
					// 人员请假的类别个数和
					personCount = findSameEmpIdCount(arraylist, empId,deptId,pdeptId);
					List<String> record = new ArrayList<String>();
					Map map =new HashMap();
					// 个人请假合计
					personSum = 0.00d;
					for(int index = 0;index<personCount;index++){
						AttendanceStatisticsQueryInfo bean = arraylist.get(index+personCountTemp);
						if (pdeptFlag == 0) {
							if (index == 0) {
								// 执行第一次循环
								// flag 相同部门下不同人员的record处理不相同
								if (flag == 0) {
									// 父部门为空的情况下的处理
									if (bean.getPdeptName() != null) {
										record.add(bean.getPdeptName());
									} else {
										record.add(STR_BLANK);
									}
									record.add(bean.getDeptName());
									record.add(bean.getChsName());
								} else {
									record.add(STR_BLANK);
									record.add(STR_BLANK);
									record.add(bean.getChsName());
								}
							}
							pdeptFlag =1;
						} else {
							if (index == 0) {
								// 执行第一次循环
								// flag 相同部门下不同人员的record处理不相同
								if (flag == 0) {
									record.add(STR_BLANK);
									record.add(bean.getDeptName());
									record.add(bean.getChsName());
								} else {
									record.add(STR_BLANK);
									record.add(STR_BLANK);
									record.add(bean.getChsName());
								}
							}
						}
						typeId = bean.getTypeId();
						days = bean.getDays()+0.00d;
						map.put(typeId, days);
						if(sumDeptMap.get(typeId)!=null){
							sumDeptMap.put(typeId, Double.parseDouble(sumDeptMap.get(typeId).toString())+days);
						}else{
							sumDeptMap.put(typeId, days);
						}
						personSum = personSum + days;
						
					}
					// 将请假类别对应的天数写到相应的类别中去，组成一条记录
					for(int index=0;index<typeList.size();index++){
						if(map.get(typeList.get(index).getTypeId())!=null){
							daysStr = map.get(typeList.get(index).getTypeId()).toString();
							if(ZERO_POINT1.equals(daysStr)){
								record.add(STR_BLANK);
							}else{
								record.add(dataOperate(Double.parseDouble(daysStr)));
							}
						}else{
							record.add(STR_BLANK);
						}
					}
					record.add(dataOperate(Double.parseDouble(personSum.toString())));
					deptSum = deptSum +personSum;
					flag++;
					k = k +personCount;
					personCountTemp =personCountTemp + personCount;
					outputList.add(record);
				}
				List<String> record = new ArrayList<String>();
				record.add(STR_BLANK);
				record.add(STR_BLANK);
				record.add(DEPT_SUM);
				// 将请假类别对应的天数写到相应的类别中去，组成一条记录（合计一个部门的数据）
				for(int index=0;index<typeList.size();index++){
					if(sumDeptMap.get(typeList.get(index).getTypeId())!=null){
						record.add(dataOperate(Double.parseDouble(sumDeptMap.get(typeList.get(index).getTypeId()).toString())));
					}else{
						record.add(ZERO_POINT);
					}
				}
				deptSum = deptSum +0.00d;
				record.add(dataOperate(Double.parseDouble(deptSum.toString())));
				outputList.add(record);
				// 合计一个部门请假类别对应的天数
				for(int index=0;index<typeList.size();index++){
					typeId = typeList.get(index).getTypeId();
					if(sumDeptMap.get(typeId)!=null){
						days =Double.parseDouble(sumDeptMap.get(typeId).toString())+0.00d;
						if(sumPDeptMap.get(typeId)!=null){
							sumPDeptMap.put(typeId,Double.parseDouble(sumPDeptMap.get(typeId).toString())+days);
						}else{
							sumPDeptMap.put(typeId, days);
						}
					}
				}
				// 计算父部门的合计
				pdeptSum = pdeptSum + deptSum;
				j = j+deptCount;
				deptCountTemp = deptCountTemp+deptCount;
			}
			List<String> record = new ArrayList<String>();
			record.add(STR_BLANK);
			record.add(PDEPT_SUM);
			record.add(STR_BLANK);
			// 将请假类别对应的天数写到相应的类别中去，组成一条记录（合计一个父部门的数据）
			for(int index=0;index<typeList.size();index++){
				if(sumPDeptMap.get(typeList.get(index).getTypeId())!=null){
					record.add(dataOperate(Double.parseDouble(sumPDeptMap.get(typeList.get(index).getTypeId()).toString())));
				}else{
					record.add(ZERO_POINT);
				}
			}
			pdeptSum = pdeptSum +0.00d;
			record.add(dataOperate(Double.parseDouble(pdeptSum.toString())));
			outputList.add(record);
			Sum  = Sum + pdeptSum;
			pdeptCountTemp =pdeptCountTemp + pdeptCount;
			i = i+pdeptCount;
			// 计算父部门下的请假类别对应天数，用于最后的每一列对应的合计
			for(int index=0;index<typeList.size();index++){
				typeId = typeList.get(index).getTypeId();
				if(sumPDeptMap.get(typeId)!=null){
					days =Double.parseDouble(sumPDeptMap.get(typeId).toString())+0.00d;
					if(sumMap.get(typeId)!=null){
						sumMap.put(typeId,Double.parseDouble(sumMap.get(typeId).toString())+days);
					}else{
						sumMap.put(typeId, days);
					}
				}
			}
		}
		List<String> record = new ArrayList<String>();
		record.add(SUM);
		record.add(STR_BLANK);
		record.add(STR_BLANK);
		for(int index=0;index<typeList.size();index++){
			if(sumMap.get(typeList.get(index).getTypeId())!=null){
				record.add(dataOperate(Double.parseDouble(sumMap.get(typeList.get(index).getTypeId()).toString())));
			}else{
				record.add(ZERO_POINT);
			}
		}
		Sum =Sum +0.00d;
		record.add(dataOperate(Double.parseDouble(Sum.toString())));
		outputList.add(record);
		// 增加一个标题记录
		record = new ArrayList<String>();
		record.add(PDEPT);
		record.add(DEPT);
		record.add(NAME);
		for(int index=0;index<typeList.size();index++){
			record.add(typeList.get(index).getType());
		}
		record.add(SUBSUM);
		outputList.add(record);
		PageObject obj = new PageObject();
		obj.setList(outputList);
		return obj;
	}
	/**
	 * 保留两位小数
	 * @param data
	 * @return
	 */
	private String dataOperate(Double data){
		NumberFormat formatter = new DecimalFormat(ZERO_POINT);
		return format(formatter.format(data.doubleValue()));
	}
	
	private String format(String s) {
		int index = s.lastIndexOf(".");
		if (index < 0)
			index = s.length();
		for (int i = index; i > 3; i -= 3) {
			s = s.substring(0, i - 3) + "," + s.substring(i - 3, s.length());
		}
		return s;
	}
	/**
	 * 该员工的请假类别的个数
	 * @param list
	 * @param empId
	 * @return
	 */
	@SuppressWarnings("unused")
	private int findSameEmpIdCount(List<AttendanceStatisticsQueryInfo> list,String empId,String deptId,String pdeptId){
		int count =0;
		for (int i = 0; i < list.size(); i++) {
			AttendanceStatisticsQueryInfo info = list.get(i);
			if (info.getEmpId().equals(empId)
					&& info.getDeptId().equals(deptId)
					&& info.getPdeptId().equals(pdeptId)) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 相同部门下的数据个数
	 * @param list
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings("unused")
	private int findSameDeptIdCount(List<AttendanceStatisticsQueryInfo> list,String deptId,String pdeptId){
		int count =0;
		for (int i = 0; i < list.size(); i++) {
			AttendanceStatisticsQueryInfo info = list.get(i);
			if (info.getDeptId().equals(deptId)
					&& info.getPdeptId().equals(pdeptId)) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 相同父部门下的数据个数
	 * @param list
	 * @param pdeptId
	 * @return
	 */
	@SuppressWarnings("unused")
	private int findSamePDeptIdCount(List<AttendanceStatisticsQueryInfo> list,String pdeptId){
		int count =0;
		for(int i =0;i<list.size();i++){
			AttendanceStatisticsQueryInfo info = list.get(i);
			if(info.getPdeptId().equals(pdeptId)){
				count++;
			}
		}
		return count;
	}
	
	
	/**
	 * 假别编码表查询
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findVacationTypeId(String enterpriseCode){

		// Log开始
		LogUtil.log("EJB:假别编码表查询开始。", Level.INFO, null);
        try {
        	@SuppressWarnings("unused")
			PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	StringBuilder strSql = new StringBuilder();
        	strSql.append("SELECT A.VACATION_TYPE_ID,");
        	strSql.append("A.VACATION_TYPE ");
        	strSql.append("FROM  HR_C_VACATIONTYPE A ");
        	strSql.append("WHERE A.IS_USE =? ");
        	strSql.append("AND A.ENTERPRISE_CODE=? ");
        	strSql.append("ORDER BY A.VACATION_TYPE_ID");
        	listParams.add(IS_USE);
        	listParams.add(enterpriseCode);
        	
        	LogUtil.log("EJB:假别编码表查询开始。SQL=" + strSql.toString(), Level.INFO, null);
        	List<Object> list = bll.queryByNativeSQL(strSql.toString(), listParams.toArray());
        	PageObject obj = new PageObject();
			@SuppressWarnings("unused")
			List<AttendanceStatisticsQueryInfo> arraylist = new ArrayList<AttendanceStatisticsQueryInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				AttendanceStatisticsQueryInfo model = new AttendanceStatisticsQueryInfo();
				if(data[0]!=null){
					model.setTypeId(data[0].toString());
				}
				if(data[1]!=null){
					model.setType(data[1].toString());
				}
				arraylist.add(model);
			}
			obj.setList(arraylist);
			obj.setTotalCount(new Long(arraylist.size()));
			LogUtil.log("EJB:假别编码表查询结束。", Level.INFO, null);
        	return obj;
        }catch (RuntimeException e) {
        	LogUtil.log("EJB:假别编码表查询失败。", Level.SEVERE, e);
            throw e;
        }
	
	}

}
