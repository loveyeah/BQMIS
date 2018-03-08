/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.hr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

//import com.googlecode.jsonplugin.JSONException;

/**
 * 员工调动单申报
 * MeetApproveFacadeRemote interface.
 * 
 * @author chenshoujiang
 */
@Stateless
public class EmpMoveDeclareFacade implements EmpMoveDeclareFacadeRemote {

	//fields
	@EJB(beanName ="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**空值*/
	private String BLANK = "";
	/** 是否使用 */
	private static final String IS_USE_Y = "Y";
	/** 调动类别 */
	private static final String  STATION_MOVE_TYPE_1 = "1"; 
	
	/**岗位调动单remote*/
	private HrJStationremoveFacadeRemote hrJStationremoveFacadeRemote;
	/**薪酬变动登记 */
	private HrJSalayradjustFacadeRemote hrJSalayradjustFacade;
	/** 员工调动单申报remote**/
	private EmpMoveDeclareFacadeRemote empMoveDeclareFacadeRemote;
	/**
	 * 构造函数
	 */
	public EmpMoveDeclareFacade() {
		hrJStationremoveFacadeRemote = (HrJStationremoveFacadeRemote)(Ejb3Factory.getInstance())
		.getFacadeRemote("HrJStationremoveFacade");
		hrJSalayradjustFacade = (HrJSalayradjustFacadeRemote)(Ejb3Factory.getInstance())
		.getFacadeRemote("HrJSalayradjustFacade");
		empMoveDeclareFacadeRemote = (EmpMoveDeclareFacadeRemote) (Ejb3Factory.getInstance())
		.getFacadeRemote("EmpMoveDeclareFacade");
	}
	
	/**
	 * 员工调动单申报查询
	 * @param flag 标识是否是初始状态
	 * @param strStartDate 开始日期
	 * @param strEndDate 结束日期
	 * @param strbeforeDeptCode 调动前部门
	 * @param strafterDeptCode 调动后部门
	 * @param strDcmStatus 单据状态
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 分页数据
	 * @return PageObject
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEmpMoveDeclareInfo(String deptFlag,String queryText,String insertDate,String queryNo,String moveType,String flag,String strStartDate,String strEndDate,String strbeforeDeptCode,
			String strafterDeptCode,String strDcmStatus,String enterpriseCode,final int ...rowStartIdxAndCount)
			throws SQLException{
		LogUtil.log("Ejb:员工调动单申报查询开始", Level.INFO, null);
		try{
			PageObject pobj = new PageObject();	
			// 参数List
        	List listParams = new ArrayList();
			// 查询sql
			String sqlData = " select \n"
				+"B.CHS_NAME as chsName, \n"
				+"to_char(A.REMOVE_DATE,'yyyy-mm-dd') AS removeDate, \n"
				+"to_char(A.DO_DATE2,'yyyy-mm-dd') AS doDate2, \n"  
				+"A.REQUISITION_NO as requisition, \n"
				//update by sychen 20100803
				+"decode(a.station_move_type_id,'0',c.dept_name,getdeptname(getfirstlevelbyid(a.old_dep_id))) as bfDeptName, \n"
				+"decode(a.station_move_type_id,'0',d.dept_name,getdeptname(getfirstlevelbyid(a.new_dep_id))) as afDeptName, \n"
//				+"C.DEPT_NAME as bfDeptName, \n"
//				+"D.DEPT_NAME as afDeptName, \n"
				+"E.STATION_NAME as bfStationName, \n"
				+"F.STATION_NAME as afStationName, \n"
				+"G.STATION_LEVEL_NAME as bfStationLevelName, \n"
				+"H.STATION_LEVEL_NAME as afStationLevelName, \n"
				+"A.DCM_STATE as dcmState, \n"
				+"A.REASON as reason, \n" 
				+"A.MEMO as memo, \n"
				+ " A.EMP_ID  as empId,"
				+ " A.STATIONREMOVEID AS stationRemoveId,"
				+ " A.STATION_MOVE_TYPE_ID AS stationMoveTypeId,"
				+ " B.CHECK_STATION_GRADE as checkStationGrade,"
				+ " B.STATION_GRADE AS stationGrade,"
				+ " B.SALARY_LEVEL AS salaryLevel, "
				+ " A.NEW_DEP_ID AS afDeptId, \n"
				+ " A.NEW_STATION_ID as afStationId, \n"
				+ " F.STATION_LEVEL_ID AS afStationLevel, \n"
				+" to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS lastModifiedDate, \n"
				+ " I.STATION_MOVE_TYPE as stationMoveType, \n"
				+ " C.DEPT_ID as bfdeptId, \n"
				+ " E.STATION_ID as bfStationId, \n"
				+ "G.STATION_LEVEL_ID as bfStationLevel,\n "
				+ " to_char(a.insertdate,'yyyy-mm-dd') as insertdate"
				+ "  ,a.old_salary_grade,a.new_salary_grade,a.old_salary_point,a.new_salary_point,a.old_station_grade,a.new_station_grade \n"
				+" ,M.DEPT_NAME as beforeBanZu \n" //add by sychen 20100721
				+" ,N.DEPT_NAME as afterBanZu\n"//add by sychen 20100721
			    +" ,to_char(A.PRINT_DATE,'yyyy-mm-dd')\n"//add by sychen 20100723
		        +" ,A.old_banzu AS beforeBanZuId\n"//add by sychen 20100723
		        +" ,A.new_banzu AS afterBanZuId\n";//add by sychen 20100723
			String sqlCount = " select count(A.STATIONREMOVEID) \n";
			String sql = 
				" from \n"
				+"HR_J_STATIONREMOVE A \n"
				+"INNER JOIN HR_J_EMP_INFO B \n"
				+"ON A.EMP_ID = B.EMP_ID \n"
				+" AND B.ENTERPRISE_CODE = ? \n"
				+"AND B.IS_USE = ? \n"
				+"LEFT JOIN HR_C_DEPT C \n"
				+"ON A.OLD_DEP_ID = C.DEPT_ID \n"
				+"AND C.ENTERPRISE_CODE = ? \n"
				+"LEFT JOIN HR_C_DEPT D \n"
				+"ON A.NEW_DEP_ID = D.DEPT_ID \n"
				+"AND D.ENTERPRISE_CODE = ? \n"
				+"LEFT JOIN HR_C_STATION E \n"
				+"ON A.OLD_STATION_ID = E.STATION_ID \n"
				+"AND E.ENTERPRISE_CODE = ? \n"
				+"LEFT JOIN HR_C_STATION F \n"
				+"ON A.NEW_STATION_ID = F.STATION_ID  " 
				+"AND F.ENTERPRISE_CODE = ? \n"
				+"LEFT JOIN HR_C_STATION_LEVEL G \n"
				// modify by liuyi 090924 直接从人员表中的岗位级别id查找岗位级别名
//				+"ON E.STATION_LEVEL_ID = G.STATION_LEVEL_ID \n"
				+ "ON  a.old_station_grade=g.station_level_id  \n"// b.station_level=g.station_level_id   \n"
				+"AND G.ENTERPRISE_CODE = ? \n"
				+ " AND G.IS_USE = ? \n"
				+"LEFT JOIN HR_C_STATION_LEVEL H \n"
				+"on    a.new_station_grade=h.station_level_id   \n" //F.STATION_LEVEL_ID = H.STATION_LEVEL_ID \n"
				+"AND H.ENTERPRISE_CODE = ? \n"
				+ " AND H.IS_USE = ? \n"
				+"LEFT JOIN HR_C_STATIONMOVETYPE I \n"
				+"ON A.STATION_MOVE_TYPE_ID = I.STATION_MOVE_TYPE_ID \n" 
				+"AND I.ENTERPRISE_CODE = ? \n"
				//add by sychen 20100721
				+"LEFT JOIN HR_C_DEPT M \n"
				+"ON A.OLD_BANZU = M.DEPT_ID \n"
				+"AND M.ENTERPRISE_CODE = ? \n"
				+"LEFT JOIN HR_C_DEPT N \n"
				+"ON A.NEW_BANZU = N.DEPT_ID \n"
				+"AND N.ENTERPRISE_CODE = ? \n"
				//add by sychen 20100721 end
				+"WHERE A.IS_USE = ? \n"
				+"AND A.ENTERPRISE_CODE = ? \n";
			// modified by liuyi 20100507
			
//				+ " AND A.STATION_MOVE_TYPE_ID = ? ";
				listParams.add(enterpriseCode);
				listParams.add(IS_USE_Y);
				listParams.add(enterpriseCode);
				listParams.add(enterpriseCode);
				listParams.add(enterpriseCode);
				listParams.add(enterpriseCode);
				listParams.add(enterpriseCode);
				// modify by liuyi 090924 13：52 岗位级别表中用U标识使用中
//				listParams.add(IS_USE_Y);
				listParams.add("Y");//update by sychen 20100901
//				listParams.add("U");
				listParams.add(enterpriseCode);
				// modify by liuyi 090924 13：52 岗位级别表中用U标识使用中
//				listParams.add(IS_USE_Y);
				listParams.add("Y");//update by sychen 20100901
//				listParams.add("U");
				listParams.add(enterpriseCode);
				listParams.add(enterpriseCode);//add by sychen 20100721
				listParams.add(enterpriseCode);//add by sychen 20100721
				listParams.add(IS_USE_Y);
				listParams.add(enterpriseCode);
				// modified by liuyi 20100507
				if(moveType != null && !moveType.equals("")){
					sql += " AND A.STATION_MOVE_TYPE_ID = ? ";
					listParams.add(moveType);
				}
				if(queryNo != null && !queryNo.equals("")){
					//update by sychen 20100716
					sql += " AND  decode(a.requisition_no,'','',(decode(a.station_move_type_id,'0','调字（'||to_char(sysdate,'yyyy')||'）第'||a.requisition_no||'号',\n" +
      "                                                                    '1','人内调字（'||to_char(sysdate,'yyyy')||'）第'||a.requisition_no||'号',\n" + 
      "                                                                    '2','借调字（'||to_char(sysdate,'yyyy')||'）第'||a.requisition_no||'号',\n" + 
      "                                                                    a.requisition_no))) LIKE  '%"+queryNo+"%' ";

//					sql += " AND A.REQUISITION_NO like '%"+queryNo+"%' ";
					//update by sychen 20100716 end
				}
				if(queryText != null && !queryText.equals("")){
					sql += " and B.CHS_NAME like '%"+queryText+"%' ";
				}
				if(insertDate != null && !insertDate.equals("")){
					sql += " and to_char(a.insertdate,'yyyy-mm-dd') = '"+insertDate+"' \n";
				}
//				listParams.add(STATION_MOVE_TYPE_1);
				// 如果画面.调动开始日期存在
				if(strStartDate != null && !strStartDate.equals("")) {
					sql += " AND TO_CHAR(A.REMOVE_DATE,'YYYY-MM-DD') >= ? \n" ;
					listParams.add(strStartDate);
				}
				// 如果画面.调动结束日期存在
				if(strEndDate != null && !strEndDate.equals("")) {
					sql += " AND TO_CHAR(A.REMOVE_DATE,'YYYY-MM-DD') <= ? \n" ;
					listParams.add(strEndDate);
				}
				// 如果画面.调动前部门存在
				//add by sychen 20100716
				if(deptFlag!=null&&deptFlag.equals("deptFlag")){
					if(strbeforeDeptCode != null && !strbeforeDeptCode.equals("")) {
						sql += "AND A.OLD_DEP_ID   in (select t.dept_id\n" +
	            			"                        from hr_c_dept t\n" + 
	            			"                       where t.is_use = 'Y'\n" + //update by sychen 20100901
//	            			"                       where t.is_use = 'U'\n" + 
	            			"                       start with t.dept_id = ?\n" + 
	            			"                      connect by prior t.dept_id = t.pdept_id)\n";
						listParams.add(strbeforeDeptCode);
					}
				}
				//add by sychen 20100716 end 
				else {
					if(strbeforeDeptCode != null && !strbeforeDeptCode.equals("")) {
						sql += "AND A.OLD_DEP_ID = ? \n" ;
						listParams.add(strbeforeDeptCode);
					}
				}
				// 如果画面.调动后部门存在
				//add by sychen 20100716
				if(deptFlag!=null&&deptFlag.equals("deptFlag")){
					if(strafterDeptCode != null && !strafterDeptCode.equals("")) {
						sql += "AND A.NEW_DEP_ID  in (select t.dept_id\n" +
	            			"                        from hr_c_dept t\n" + 
	            			"                       where t.is_use = 'Y'\n" + //update by sychen 20100901
//	            			"                       where t.is_use = 'U'\n" + 
	            			"                       start with t.dept_id = ?\n" + 
	            			"                      connect by prior t.dept_id = t.pdept_id)\n";
						listParams.add(strafterDeptCode);
					}
				}
				//add by sychen 20100716 end 
				else {
					if(strafterDeptCode != null && !strafterDeptCode.equals("")) {
						sql += "AND A.NEW_DEP_ID = ? \n" ;
						listParams.add(strafterDeptCode);
					}
				}
				// 如果画面.单据状态存在
				if(strDcmStatus != null && !strDcmStatus.equals("")) {
					sql += "AND A.DCM_STATE = ? \n" ;
					listParams.add(strDcmStatus);
				}
			sql += "ORDER BY A.STATIONREMOVEID";
			// 打印sql文
			LogUtil.log("sql 文："+sql, Level.INFO, null);	
			Object[] params= listParams.toArray();
			sqlData += sql;
			sqlCount += sql;
			
			//list
			List list = bll.queryByNativeSQL(sqlData,params,rowStartIdxAndCount);
			List<EmpMoveDeclare> arrList = new ArrayList<EmpMoveDeclare>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				EmpMoveDeclare info = new EmpMoveDeclare();
				// 中文姓名
				if(data[0] != null && !data[0].toString().equals(BLANK)){
					info.setChsName(data[0].toString());
				}
				// 调动日期
				if(data[1] != null && !data[1].toString().equals(BLANK)){
					info.setRemoveDate(data[1].toString());
				}
				// 起薪日期
				if(data[2] != null && !data[2].toString().equals(BLANK)){
					info.setDoDate2(data[2].toString());
				}
				// 调动通知单号	
				if(data[3] != null && !data[3].toString().equals(BLANK)){
					info.setRequisition(data[3].toString());
				}
				// 调动前部门
				if(data[4] != null && !data[4].toString().equals(BLANK)){
					info.setBfDeptName(data[4].toString());
				}
				// 调动后部门 		
				if(data[5] != null && !data[5].toString().equals(BLANK)){
					info.setAfDeptName(data[5].toString());
				}
				// 调动前岗位
				if(data[6] != null && !data[6].toString().equals(BLANK)){
					info.setBfStationName(data[6].toString());
				}
				// 调动后岗位		
				if(data[7] != null && !data[7].toString().equals(BLANK)){
					info.setAfStationName(data[7].toString());
				}
				// 调动前岗位级别	
				if(data[8] != null && !data[8].toString().equals(BLANK)){
					info.setBfStationLevelName(data[8].toString());
				}
				// 调动后岗位级别
				if(data[9] != null && !data[9].toString().equals(BLANK)){
					info.setAfStationLevelName(data[9].toString());
				}
				// 单据状态 		
				if(data[10] != null && !data[10].toString().equals(BLANK)){
					info.setDcmState(data[10].toString());
				}
				// 原因
				if(data[11] != null && !data[11].toString().equals(BLANK)){
					info.setReason(data[11].toString());
				}
				// 备注		
				if(data[12] != null && !data[12].toString().equals(BLANK)){
					info.setMemo(data[12].toString());
				}
				// 人员id
				if(data[13] != null && !data[13].toString().equals(BLANK)){
					info.setEmpId(data[13].toString());
				}
				// 岗位调动单ID
				if(data[14] != null && !data[14].toString().equals(BLANK)){
					info.setStationRemoveId(data[14].toString());
				}
				// 岗位调动类别ID 
				if(data[15] != null && !data[15].toString().equals(BLANK)){
					info.setStationMoveTypeId(data[15].toString());
				}
				/**执行岗级*/	
				if(data[16] != null && !data[16].toString().equals(BLANK)){
					info.setCheckStationGrade(data[16].toString());
				}
				/** 标准岗级 */
				if(data[17] != null && !data[17].toString().equals(BLANK)){
					info.setStationGrade(data[17].toString());
				}
				/** 薪级 */
				if(data[18] != null && !data[18].toString().equals(BLANK)){
					info.setSalaryLevel(data[18].toString());
				}
				/** 调动后部门 */	
				if(data[19] != null && !data[19].toString().equals(BLANK)){
					info.setAfDeptId(data[19].toString());
				}
				/** 调动后岗位id */	
				if(data[20] != null && !data[20].toString().equals(BLANK)){
					info.setAfStationId(data[20].toString());
				}
				/** 调动前岗位级别id */
				if(data[21] != null && !data[21].toString().equals(BLANK)){
					info.setAfStationLevel(data[21].toString());
				}
				/** 最后修改时间*/
				if(data[22] != null && !data[22].toString().equals(BLANK)){
					info.setLastModifiedDate(data[22].toString());
				}
				/** 岗位调动类别名称*/
				if(data[23] != null && !data[23].toString().equals(BLANK)){
					info.setStationMoveType(data[23].toString());
				}
				if(data[24] != null && !data[24].toString().equals(BLANK)){
					info.setBfdeptId(data[24].toString());
				}
				if(data[25] != null && !data[25].toString().equals(BLANK)){
					info.setBfStationId(data[25].toString());
				}
				if(data[26] != null && !data[26].toString().equals(BLANK)){
					info.setBfStationLevel(data[26].toString());
				}
				// 登记时间
				if(data[27] != null)
					info.setInsertdate(data[27].toString());
				// 调动前后薪级
				if(data[28] != null)
					info.setOldSalaryLevel(data[28].toString());
				if(data[29] != null)
					info.setNewSalaryLevel(data[29].toString());
				// 调动前后薪点
				if(data[30] != null)
					info.setOldSalaryPoint(data[30].toString());
				if(data[31] != null)
					info.setNewSalaryPoint(data[31].toString());
				// 调动前后岗位级别
				if(data[32] != null)
					info.setOldStationGrade(data[32].toString());
				if(data[33] != null)
					info.setNewStationGrade(data[33].toString());

				//add by sychen 20100721
				// 调动前班组
				if(data[34] != null && !data[34].toString().equals(BLANK)){
					info.setBeforeBanZu(data[34].toString());
				}
				// 调动后班组 		
				if(data[35] != null && !data[35].toString().equals(BLANK)){
					info.setAfterBanZu(data[35].toString());
				}
				//add by sychen 20100721 end
				
				// 打印日期 	//add by sychen 20100723
				if(data[36] != null && !data[36].toString().equals(BLANK)){
					info.setPrintDate(data[36].toString());
				}
				// 调动前班组ID  //add by sychen 20100723
				if(data[37] != null && !data[37].toString().equals(BLANK)){
					info.setBeforeBanZuId(data[37].toString());
				}
				// 调动后班组ID 	//add by sychen 20100723	
				if(data[38] != null && !data[38].toString().equals(BLANK)){
					info.setAfterBanZuId(data[38].toString());
				}
				arrList.add(info);
			}
			// 行数
			pobj.setTotalCount(Long.parseLong(bll.getSingal(sqlCount, params).toString()));
			pobj.setList(arrList);
			LogUtil.log("Ejb:员工调动单申报查询结束", Level.INFO, null);
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log(" Ejb:员工调动单申报查询失败", Level.INFO, null);
			throw new SQLException();
		}
	}
	
	/**
	 *  根据人员code查找部门,岗位,级别信息
	 * @param empCode 人员code
	 * @param enterpriseCode 企业编码
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getDeptStationLevel(String empId,String enterpriseCode)throws SQLException{
		LogUtil.log("Ejb:根据人员code查找部门,岗位,级别信息开始", Level.INFO, null);
		try{
			PageObject pobj = new PageObject();	
			// 参数List
        	List listParams = new ArrayList();
			// 查询sql
			String sql = "SELECT "
				+ "A.EMP_ID as empId, "
				+ " A.EMP_CODE as empCode, " 
				+ " A.DEPT_ID as bfdeptId,"
				+ " B.DEPT_NAME as bfDeptName, " 
				+ " A.STATION_ID as bfStationId, "  
				+ " C.STATION_NAME as bfStationName ,"
				+ " A.Check_Station_Grade as bfStationLevel, "
				+ " D.STATION_LEVEL_NAME as bfStationLevelName, "
				+ " A.CHECK_STATION_GRADE as checkStationGrade,"
				+ " A.STATION_GRADE AS stationGrade,"
				+ " A.SALARY_LEVEL AS salaryLevel "
				// add by liuyi 20100617 
				+ " ,e.salary_point "
				+ " FROM HR_J_EMP_INFO A "
				+ " LEFT JOIN HR_C_DEPT B ON A.DEPT_ID = B.DEPT_ID "
				+ "AND B.ENTERPRISE_CODE = ?  "
				+ "LEFT JOIN HR_C_STATION C ON A.STATION_ID = C.STATION_ID  "
				+ " AND C.ENTERPRISE_CODE = ?  "
				// modify by liuyi 090923 直接从人员表中的岗位级别查找岗位级别名称
//				+ "LEFT JOIN HR_C_STATION_LEVEL D ON C.STATION_LEVEL_ID = D.STATION_LEVEL_ID  "
				+ "LEFT JOIN HR_C_STATION_LEVEL D ON a.Check_Station_Grade=d.station_level_id  "
				
				+ " AND D.ENTERPRISE_CODE = ?  "
				// add by liuyi 20100617 
				+ "  left join hr_c_salary_personal e on a.emp_id=e.emp_id and e.is_use='Y'and rownum=1 "
				+ " WHERE A.EMP_ID = ?  "
				+ "AND A.IS_USE = ?  "
				+ "AND A.ENTERPRISE_CODE = ?" ;
				
				listParams.add(enterpriseCode);
				listParams.add(enterpriseCode);
				listParams.add(enterpriseCode);
				listParams.add(empId);
				listParams.add(IS_USE_Y );
				listParams.add(enterpriseCode);
			sql += " ORDER BY A.EMP_ID";
			// 打印sql文
			LogUtil.log("sql 文："+sql, Level.INFO, null);	
			Object[] params= listParams.toArray();
			//list
			List list = bll.queryByNativeSQL(sql,params);
			List<EmpMoveDeclare> arrList = new ArrayList<EmpMoveDeclare>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				EmpMoveDeclare info = new EmpMoveDeclare();
				// 人员id
				if(data[0] != null && !data[0].toString().equals(BLANK)){
					info.setEmpId(data[0].toString());
				}
				// 人员code
				if(data[1] != null && !data[1].toString().equals(BLANK)){
					info.setEmpCode(data[1].toString());
				}
				// 部门id
				if(data[2] != null && !data[2].toString().equals(BLANK)){
					info.setBfdeptId(data[2].toString());
				}
				// 部门名称
				if(data[3] != null && !data[3].toString().equals(BLANK)){
					info.setBfDeptName(data[3].toString());
				}
				// 岗位id
				if(data[4] != null && !data[4].toString().equals(BLANK)){
					info.setBfStationId(data[4].toString());
				}
				// 岗位名称		
				if(data[5] != null && !data[5].toString().equals(BLANK)){
					info.setBfStationName(data[5].toString());
				}
				// 岗位级别id
				if(data[6] != null && !data[6].toString().equals(BLANK)){
					info.setBfStationLevel(data[6].toString());
				}
				// 岗位级别名称		
				if(data[7] != null && !data[7].toString().equals(BLANK)){
					info.setBfStationLevelName(data[7].toString());
				}
				/**执行岗级*/	
				if(data[8] != null && !data[8].toString().equals(BLANK)){
					info.setCheckStationGrade(data[8].toString());
				}
				/** 标准岗级 */
				if(data[9] != null && !data[9].toString().equals(BLANK)){
					info.setStationGrade(data[9].toString());
				}
				/** 薪级 */
				if(data[10] != null && !data[10].toString().equals(BLANK)){
					info.setSalaryLevel(data[10].toString());
				}
				// 薪点
				if(data[11] != null && !data[11].toString().equals(BLANK)){
					info.setOldSalaryPoint(data[11].toString());
				}
					
				arrList.add(info);
			}
			// 行数
			pobj.setTotalCount((long)(arrList.size()));
			pobj.setList(arrList);
			LogUtil.log("Ejb:根据人员code查找部门,岗位,级别信息结束", Level.INFO, null);
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log(" Ejb:根据人员code查找部门,岗位,级别信息失败", Level.INFO, null);
			throw new SQLException();
		}
	}
	
	/**
	 *  上报岗位调动单记录
	 * @param stationRemoveId
	 * @param workerCode
	 * @param enterpriseCode
	 * @param lastModifiedDate
	 * @throws JSONException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void report(String stationRemoveId,String workerCode,String enterpriseCode,String lastModifiedDate)
		throws Exception  {
		LogUtil.log("ejb:上报岗位调动单记录开始", Level.SEVERE, null);
		try {
			// 创建一个
			HrJStationremove entity = new HrJStationremove();
			// 如果序号非空的话
			if(null != stationRemoveId && !BLANK.equals(stationRemoveId))
			{
				// 找到对应序号信息		
				entity = hrJStationremoveFacadeRemote.findById(Long.parseLong(stationRemoveId));
				// 上次修改人
				entity.setLastModifiedBy(workerCode);
				// 上报
				hrJStationremoveFacadeRemote.report(entity,lastModifiedDate);
			}
			LogUtil.log("ejb:上报岗位调动单记录结束", Level.SEVERE, null);
		}catch (DataChangeException e){
			 LogUtil.log("ejb:上报岗位调动单记录失败", Level.SEVERE, null);
			 throw new DataChangeException("");
		}catch (SQLException sqle) {
	         LogUtil.log("ejb:上报岗位调动单记录失败", Level.SEVERE, null);
	         throw new SQLException(sqle.getMessage());
		} 
	}
	
	/**
	 * 更新岗位调动单和薪酬登记表
	 * @param entity
	 * @param lastModifiedDate
	 * @param bean
	 * @param lastModifiedDateSub
	 * @param flag
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void update(HrJStationremove entity,String lastModifiedDate,HrJSalayradjust bean,String lastModifiedDateSub,
			String flag)throws Exception {
		LogUtil.log("ejb:更新岗位调动单开始", Level.INFO, null);
		try{
			if(entity.getStationremoveid() != null && !entity.getStationremoveid().equals("")) {
					hrJStationremoveFacadeRemote.update(entity,lastModifiedDate);
					if(bean.getNewCheckStationGrade() != null && !BLANK.equals(bean.getNewCheckStationGrade())) {
						if(flag.equals("1")) {
							hrJSalayradjustFacade.updateData(bean,lastModifiedDateSub);
						}else {
							hrJSalayradjustFacade.save(bean);
						}
					}
			}
			LogUtil.log("ejb:更新岗位调动单结束", Level.INFO, null);
		}catch (DataChangeException e){
			 LogUtil.log("ejb:上报岗位调动单记录失败", Level.SEVERE, null);
			 throw new DataChangeException("");
		}catch (SQLException sqle) {
            LogUtil.log("ejb:更新岗位调动单失败", Level.SEVERE, null);
            throw new SQLException(sqle.getMessage());
            
		} 
	}
	
	/**
	 *  增加
	 * @param entity
	 * @param subBean
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void add(HrJStationremove entity,HrJSalayradjust subBean)throws Exception{
		LogUtil.log("Action:新增岗位调动单开始", Level.INFO, null);
//		try{
			if(entity.getEmpId() != null && !entity.getEmpId().toString().equals("")){
				HrJStationremove bean = hrJStationremoveFacadeRemote.save(entity);
				// modified by liuyi 20100617 注释掉
//				if(subBean.getNewCheckStationGrade() != null && !subBean.getNewCheckStationGrade().equals("")) {
//					subBean.setStationremoveid(bean.getStationremoveid());
//					hrJSalayradjustFacade.save(subBean);
//				}
			}
			LogUtil.log("Action:新增岗位调动单结束", Level.INFO, null);
//		}
//		catch (SQLException sqle) {
//           LogUtil.log("ejb:新增岗位调动单失败", Level.SEVERE, null);
//           throw new SQLException(sqle.getMessage());
//		}
	} 
	
	/**
	 * 删除岗位调动单记录
	 * @param stationRemoveId
	 * @param workerCode
	 * @param enterpriseCode
	 * @param lastModifiedDate
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(String stationRemoveId,String workerCode,String enterpriseCode,String lastModifiedDate)
		throws Exception{
		LogUtil.log("ejb:删除岗位调动单记录开始", Level.INFO, null);
		try{
			// 创建一个
			HrJStationremove entity = new HrJStationremove();
			// 如果序号非空的话
			if(null != stationRemoveId && !BLANK.equals(stationRemoveId))
			{
				// 找到对应序号信息		
				entity = hrJStationremoveFacadeRemote.findById(Long.parseLong(stationRemoveId));
				// 上次修改人
				entity.setLastModifiedBy(workerCode);
				// 删除
				hrJStationremoveFacadeRemote.delete(entity,lastModifiedDate);
				PageObject obj = hrJSalayradjustFacade.getSalayAdjustByRemoveId(stationRemoveId,enterpriseCode);
				if(obj.getList() != null && obj.getList().size() > 0) {
					List<HrJSalayradjust> list = obj.getList();
					HrJSalayradjust bean = list.get(0);
					bean.setEnterpriseCode(enterpriseCode);
					hrJSalayradjustFacade.delete(bean);
				}
			}
			LogUtil.log("ejb:删除岗位调动单记录结束", Level.INFO, null);
		}catch (DataChangeException e){
			 LogUtil.log("ejb:上报岗位调动单记录失败", Level.SEVERE, null);
			 throw new DataChangeException("");
		}catch (SQLException sqle) {
            LogUtil.log("ejb:删除岗位调动单记录失败", Level.SEVERE, null);
            throw new SQLException(sqle.getMessage());
		} 
	}
}
