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

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 员工借调登记
 * EmpMoveRegisterFacade interface.
 * 
 * @author chenshoujiang
 */
@Stateless
public class EmpMoveRegisterFacade implements EmpMoveRegisterFacadeRemote {

	//fields
	@EJB(beanName ="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**空值*/
	private String BLANK = "";
	/** 是否使用Y*/
	private String IS_USE_Y = "Y";
	/** 是否退回 */
	private String IF_BACK_0 = "0";
	/**
	 * 查询员工借调登记信息
	 * @param strStartDate
	 * @param strEndDate
	 * @param strSSDeptCode
	 * @param strJDDeptCode
	 * @param strDcmStatus
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEmpMoveRegisterInfo(String strStartDate,String strEndDate,String strSSDeptId,
			String strJDDeptId,String strDcmStatus,String enterpriseCode,final int ...rowStartIdxAndCount)
			throws SQLException{
		LogUtil.log("Ejb:员工借调登记查询开始", Level.INFO, null);
		try{
			PageObject pobj = new PageObject();	
			// 参数List
        	List listParams = new ArrayList();
			// 查询sql
        	String sqlData = " select \n"
				+ " A.HR_J_EMPLOYEEBORROW_IN_ID AS hrJEmployeeborrowInId, \n"
				+ " C.EMP_ID as empId,  \n"
				+ " C.EMP_CODE AS empCode, \n"
				+ " C.CHS_NAME as chsName, \n"
				+ " D.DEPT_ID AS ssDeptId, \n"
				+ " D.DEPT_NAME AS ssDeptName, \n"
				+ " E.STATION_ID as stationId, \n"
				+ " E.STATION_NAME as stationName, \n"
				+ " B.DEPT_ID AS jdDeptId, \n"
				+ " B.DEPT_NAME AS jdDeptName, \n"
				+"to_char(A.START_DATE,'yyyy-mm-dd') AS startDate, \n"
				+"to_char(A.END_DATE,'yyyy-mm-dd') AS endDate, \n"
				+ " A.DCM_STATUS AS dcmStatus, \n"
				+ " A.MEMO AS memo, \n"
				+ " to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS lastModifiedDate, \n"
				+ " A.IF_BACK AS ifBack";
        	String sqlCount = " select count(A.HR_J_EMPLOYEEBORROW_IN_ID) \n";
			String sql =	
				" from HR_J_EMPLOYEEBORROW_IN A \n"
				+ " LEFT JOIN HR_C_DEPT B  \n"
				+ " ON A.BORROW_DEPT_ID = B.DEPT_ID  \n"
				+ " AND B.ENTERPRISE_CODE = ? \n"
				+ " and B.IS_USE = ? \n"
				+ " LEFT JOIN HR_J_EMP_INFO C  \n"
				+ " ON A.EMP_ID = C.EMP_ID  \n"
				+ " AND C.ENTERPRISE_CODE = ?  \n"
				// modify by liuyi 090914 c表中无is_use属性
//				+ " and C.IS_USE = ? \n"
				+ " LEFT JOIN HR_C_DEPT D  \n"
				+ " ON C.DEPT_ID = D.DEPT_ID  \n"
				+ " AND D.ENTERPRISE_CODE = ?  \n"
				+ " and D.IS_USE = ? \n"
				+ " LEFT JOIN HR_C_STATION E  \n"
				+ " ON C.STATION_ID = E.STATION_ID  \n"
				// modify by liuyi 090914 E表中无该属性
//				+ " AND E.ENTERPRISE_CODE = ?  \n"
				+ " and E.IS_USE = ? \n"
				+ " where A.IF_BACK = ? \n"
				+	" and A.IS_USE = ? \n"
				+ " and A.ENTERPRISE_CODE = ? \n" ;
        	listParams.add(enterpriseCode);
        	// modify by liuyi 090924 部门表中is_use用U表示使用中
//        	listParams.add(IS_USE_Y);
			listParams.add("Y");//update by sychen 20100901
//			listParams.add("U");
			listParams.add(enterpriseCode);
//			listParams.add(IS_USE_Y);
			listParams.add(enterpriseCode);
			// modify by liuyi 090914 部门表中is_use用U表示使用中
//			listParams.add(IS_USE_Y);
			listParams.add("Y");//update by sychen 20100901
//			listParams.add("U");
//			listParams.add(enterpriseCode);
			// modify by liuyi 090924 岗位表中is_use用U表示使用中
//			listParams.add(IS_USE_Y);
			listParams.add("Y");//update by sychen 20100901
//			listParams.add("U");
			listParams.add(IF_BACK_0);
			listParams.add(IS_USE_Y);
			listParams.add(enterpriseCode);
        	// 如果画面.借调开始日期存在
			if(strStartDate != null && !strStartDate.equals(BLANK)) {
				sql += " AND TO_CHAR(A.START_DATE,'YYYY-MM-DD') >= ? \n" ;
				listParams.add(strStartDate);
			}
			// 如果画面.借调结束日期存在
			if(strEndDate != null && !strEndDate.equals(BLANK)) {
				sql += " AND TO_CHAR(A.START_DATE,'YYYY-MM-DD') <=? \n" ;
				listParams.add(strEndDate);
			}
			// 如果画面.所属部门存在
				if(strSSDeptId != null && !strSSDeptId.equals(BLANK)) {
					sql += "AND D.DEPT_ID = ? \n" ;
					listParams.add(strSSDeptId);
				}
			// 如果画面.借调部门存在
		    	if(strJDDeptId != null && !strJDDeptId.equals("")) {
					sql += "AND A.BORROW_DEPT_ID = ? \n" ;
					listParams.add(strJDDeptId);
				}
			// 如果画面.单据状态存在
			if(strDcmStatus != null && !strDcmStatus.equals(BLANK)) {
				sql += "AND A.DCM_STATUS = ? \n" ;
				listParams.add(strDcmStatus);
			}
			sql += "ORDER BY A.START_DATE";
			// 打印sql文
			LogUtil.log("sql 文："+sql, Level.INFO, null);	
			Object[] params= listParams.toArray();
			sqlData += sql;
			sqlCount += sql;
			//list
			List list = bll.queryByNativeSQL(sqlData,params,rowStartIdxAndCount);
			List<EmpMoveRegister> arrList = new ArrayList<EmpMoveRegister>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				EmpMoveRegister info = new EmpMoveRegister();
				/** 员工借调ID */
				if(data[0] != null && !data[0].toString().equals(BLANK)){
					info.setHrJEmployeeborrowInId(data[0].toString());
				}
				/** 员工ID */
				if(data[1] != null && !data[1].toString().equals(BLANK)){
					info.setEmpId(data[1].toString());
				}
				/** 员工Code*/
				if(data[2] != null && !data[2].toString().equals(BLANK)){
					info.setEmpCode(data[2].toString());
				}
				/**中文名称 */
				if(data[3] != null && !data[3].toString().equals(BLANK)){
					info.setChsName(data[3].toString());
				}
				/** 所属部门ID */
				if(data[4] != null && !data[4].toString().equals(BLANK)){
					info.setSsDeptId(data[4].toString());
				}
				/** 所属部门名称 */		
				if(data[5] != null && !data[5].toString().equals(BLANK)){
					info.setSsDeptName(data[5].toString());
				}
				/** 岗位ID */
				if(data[6] != null && !data[6].toString().equals(BLANK)){
					info.setStationId(data[6].toString());
				}
				/** 岗位名称 */		
				if(data[7] != null && !data[7].toString().equals(BLANK)){
					info.setStationName(data[7].toString());
				}
				/** 借调部门ID */
				if(data[8] != null && !data[8].toString().equals(BLANK)){
					info.setJdDeptId(data[8].toString());
				}
				/** 借调部门名称*/
				if(data[9] != null && !data[9].toString().equals(BLANK)){
					info.setJdDeptName(data[9].toString());
				}
				/** 开始日期 */ 		
				if(data[10] != null && !data[10].toString().equals(BLANK)){
					info.setStartDate(data[10].toString());
				}
				/** 结束日期 */
				if(data[11] != null && !data[11].toString().equals(BLANK)){
					info.setEndDate(data[11].toString());
				}
				/** 单据状态 */	
				if(data[12] != null && !data[12].toString().equals(BLANK)){
					info.setDcmStatus(data[12].toString());
				}
				/** 备注 */
				if(data[13] != null && !data[13].toString().equals(BLANK)){
					info.setMemo(data[13].toString());
				}
				/** 上次修改时间*/
				if(data[14] != null && !data[14].toString().equals(BLANK)){
					info.setLastModifiedDate(data[14].toString());
				}
				/** 是否已回*/
				if(data[15] != null && !data[15].toString().equals(BLANK)){
					info.setIfBack(data[15].toString());
				}
				arrList.add(info);
			}
			// 行数
			pobj.setTotalCount(Long.parseLong(bll.getSingal(sqlCount, params).toString()));
			pobj.setList(arrList);
			LogUtil.log("Ejb:员工借调登记查询结束", Level.INFO, null);
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log(" Ejb:员工借调登记查询失败", Level.INFO, null);
			throw new SQLException(e.getMessage());
		}
	}
	
	/**
	 *  通过部门ID查询员工借调登记
	 * @param strStartDate
	 * @param strEndDate
	 * @param strJDDeptId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEmpBorrowInByDeptId(String strJDDeptId,String enterpriseCode,final int ...rowStartIdxAndCount)
			throws SQLException{
		LogUtil.log("Ejb:通过部门ID查询员工借调登记开始", Level.INFO, null);
		try{
			PageObject pobj = new PageObject();	
			// 参数List
        	List listParams = new ArrayList();
			// 查询sql
        	String sqlData = " select \n"
        		+" A.HR_J_EMPLOYEEBORROW_IN_ID AS hrJEmployeeborrowInId, \n"
        		+" A.START_DATE as startDate, \n"
        		+" C.EMP_ID as empId, \n"
        		+" C.EMP_CODE AS empCode, \n"
        		+" C.CHS_NAME as chsName, \n"
        		+" B.DEPT_ID AS jdDeptId, \n"
        		+" B.DEPT_NAME AS jdDeptName, \n"
        		+" to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS lastModifiedDate \n";
    		String sqlCount = " select count(A.HR_J_EMPLOYEEBORROW_IN_ID) \n"; 
        	String sql = 
        		" from HR_J_EMPLOYEEBORROW_IN A \n"
        		+ " LEFT JOIN HR_C_DEPT B  \n"
				+ " ON A.BORROW_DEPT_ID = B.DEPT_ID  \n"
				+ " AND B.ENTERPRISE_CODE = ? \n"
				+ " and B.IS_USE = ? \n"
				+ " LEFT JOIN HR_J_EMP_INFO C  \n"
				+ " ON A.EMP_ID = C.EMP_ID  \n"
				+ " AND C.ENTERPRISE_CODE = ?  \n"
				+ " and C.IS_USE = ? \n"
        		+" WHERE A.IF_BACK =　? \n" 
        		+" AND A.ENTERPRISE_CODE = ? \n" 
        		+ " and A.IS_USE =  ? \n"
        		+" AND B.ENTERPRISE_CODE =  ? \n" 
        		+ " and B.IS_USE =  ?  \n"
        		+" AND C.ENTERPRISE_CODE =  ? \n"
        		+ "　and C.IS_USE =  ?　\n"
        		+" AND  A.BORROW_DEPT_ID =  ?　\n";
        	listParams.add(enterpriseCode);
			listParams.add(IS_USE_Y);
			listParams.add(enterpriseCode);
			listParams.add(IS_USE_Y);
        	listParams.add(IF_BACK_0);
			listParams.add(enterpriseCode);
			listParams.add(IS_USE_Y);
			listParams.add(enterpriseCode);
			listParams.add(IS_USE_Y);
			listParams.add(enterpriseCode);
			listParams.add(IS_USE_Y);
			listParams.add(strJDDeptId);
			sql += "ORDER BY A.START_DATE";
			// 打印sql文
			LogUtil.log("sql 文："+sql, Level.INFO, null);	
			Object[] params= listParams.toArray();
			sqlData += sql;
			sqlCount += sql;
			//list
			List list = bll.queryByNativeSQL(sqlData,params,rowStartIdxAndCount);
			List<EmpMoveRegister> arrList = new ArrayList<EmpMoveRegister>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				EmpMoveRegister info = new EmpMoveRegister();
				/** 员工借调ID */
				if(data[0] != null && !data[0].toString().equals(BLANK)){
					info.setHrJEmployeeborrowInId(data[0].toString());
				}
				/** 开始日期 */ 		
				if(data[1] != null && !data[1].toString().equals(BLANK)){
					info.setStartDate(data[1].toString());
				}
				/** 员工ID */
				if(data[2] != null && !data[2].toString().equals(BLANK)){
					info.setEmpId(data[2].toString());
				}
				/** 员工Code*/
				if(data[3] != null && !data[3].toString().equals(BLANK)){
					info.setEmpCode(data[3].toString());
				}
				/**中文名称 */
				if(data[4] != null && !data[4].toString().equals(BLANK)){
					info.setChsName(data[4].toString());
				}
				/** 借调部门ID */
				if(data[5] != null && !data[5].toString().equals(BLANK)){
					info.setJdDeptId(data[5].toString());
				}
				/** 借调部门名称*/
				if(data[6] != null && !data[6].toString().equals(BLANK)){
					info.setJdDeptName(data[6].toString());
				}
				/** 上次修改时间*/
				if(data[7] != null && !data[7].toString().equals(BLANK)){
					info.setLastModifiedDate(data[7].toString());
				}
				arrList.add(info);
			}
			// 行数
			pobj.setTotalCount(Long.parseLong(bll.getSingal(sqlCount, params).toString()));
			pobj.setList(arrList);
			LogUtil.log("Ejb:通过部门ID查询员工借调登记结束", Level.INFO, null);
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log(" Ejb:通过部门ID查询员工借调登记失败", Level.INFO, null);
			throw new SQLException(e.getMessage());
		}
	}
}
