/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.hr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity WorkContractI.
 * 
 * @see power.ejb.hr.WorkContractFacade
 * @author sufeiyu
 */
@Stateless
public class WorkContractFacade implements WorkContractFacadeRemote{
	
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	/**
	 * 取得指定的合同信息
	 * @param strEnterpriseCode
	 * @param workcontractid
	 * @return
	 * @throws SQLException 
	 */
    @SuppressWarnings("unchecked")
	public PageObject getContractDetail(String strEnterpriseCode, Long empId) throws SQLException {
    	PageObject objResult = new PageObject();
    	objResult.setTotalCount(new Long(0));
    	
    	try {
			String strSql   = "SELECT"
							+ " A.WORKCONTRACTID,"
							+ " B.CHS_NAME,"
							+ " D.DEPT_NAME,"
							+ " C.STATION_NAME,"
							+ " A.WROK_CONTRACT_NO,"
							+ " A.FRIST_DEP_ID ,"
							+ " A.FRIST_ADDREST,"
							+ " A.CONTRACT_TERM_ID,"
							+ " TO_CHAR(A.WORK_SIGN_DATE,'YYYY-MM-DD'),"
							+ " TO_CHAR(A.START_DATE,'YYYY-MM-DD'),"
							+ " TO_CHAR(A.END_DATE,'YYYY-MM-DD'),"
							+ " A.IF_EXECUTE,"
							+ " A.CONTRACT_CONTINUE_MARK,"
							+ " A.MEMO,"
							+ " TO_CHAR(A.LAST_MODIFIED_DATE,'YYYY-MM-DD HH24:MI:SS'),"
							+ " A.EMP_ID,"
							+ " A.DEPT_ID,"
							+ " A.STATION_ID "
							+ "FROM"
							+ " HR_J_WORKCONTRACT A "
							+ "LEFT JOIN "
							+ " HR_J_EMP_INFO B "
							+ "ON  "
							+ " A.EMP_ID = B.EMP_ID "
							+ "AND"
							+ " B.ENTERPRISE_CODE = ? "
							+ "LEFT JOIN "
							+ " HR_C_STATION C "
							+ "ON"
							+ " A.STATION_ID = C.STATION_ID "
							// modify by liuyi 090911 该表中无该属性
//							+ "AND"
//							+ " C.ENTERPRISE_CODE = ? "
							+ "LEFT JOIN "
							+ " HR_C_DEPT D "
							+ "ON  "
							+ " A.DEPT_ID = D.DEPT_ID  "
							+ "AND "
							+ " D.ENTERPRISE_CODE = ? "
							+ "WHERE"
							+ " A.ENTERPRISE_CODE = ? "
							+ "AND"
							+ " A.IS_USE = ? "
							+ "AND"
							+ " A.EMP_ID = ? "
//							+ "AND"
//							+ " A.IF_EXECUTE = ? "
							+ "ORDER BY "
							+ " A.WORKCONTRACTID DESC";
			// modify by liuyi 090911
//			Object params[] = new Object[6];
//			params[0] = strEnterpriseCode;
//			params[1] = strEnterpriseCode;
//			params[2] = strEnterpriseCode;
//			params[3] = strEnterpriseCode;
//			params[4] = "Y";
//			params[5] = empId;
			Object params[] = new Object[5];
			params[0] = strEnterpriseCode;
			params[1] = strEnterpriseCode;
			params[2] = strEnterpriseCode;
			params[3] = "Y";
			params[4] = empId;

			
			LogUtil.log("EJB:取得指定的合同信息开始。SQL：", Level.INFO, null);
			LogUtil.log(strSql, Level.INFO, null);
			List lstResult = bll.queryByNativeSQL(strSql, params);
			List<WorkContractI> arlstResult = new ArrayList<WorkContractI>();
			
			if (lstResult != null && lstResult.size() > 0) {
				for (int i = 0; i < lstResult.size(); i++) {
					WorkContractI objTemp = new WorkContractI();
					Object[] data = (Object[])(lstResult.get(i));
					// 劳动合同签定ID
					if (data[0] != null) {
						objTemp.setWorkcontractid(Long.parseLong(data[0].toString()));
					}
					// 人员
					if (data[1] != null) {
						objTemp.setEmpName(data[1].toString());
					}
					// 部门
					if (data[2] != null) {
						objTemp.setDeptName(data[2].toString());
					}
					// 岗位
					if (data[3] != null) {
						objTemp.setStationName(data[3].toString());
					}
					// 劳动合同编号
					if (data[4] != null) {
						objTemp.setWrokContractNo(data[4].toString());
					}
					// 甲方部门
					if (data[5] != null) {
						objTemp.setFristDepId(Long.parseLong(data[5].toString()));
						objTemp.setFristDepName(getFirstdeptName(strEnterpriseCode, objTemp.getFristDepId()));
					}
					// 甲方地址
					if (data[6] != null) {
						objTemp.setFristAddrest(data[6].toString());
					}
					// 劳动合同有效期
					if (data[7] != null) {
						objTemp.setContractTermId(Long.parseLong(data[7].toString()));
					}
					// 劳动合同签字日期
					if (data[8] != null) {
						objTemp.setWorkSignDate(data[8].toString());
					}
					// 开始日期
					if (data[9] != null) {
						objTemp.setStartDate(data[9].toString());
					}
					// 结束日期
					if (data[10] != null) {
						objTemp.setEndDate(data[10].toString());
					}
					// 是否劳动合同正在执行
					if (data[11] != null) {
						objTemp.setIfExecute(data[11].toString());
					}
					// 劳动合同续签标志
					if (data[12] != null) {
						objTemp.setContractContinueMark(data[12].toString());
					}
					// 备注
					if (data[13] != null) {
						objTemp.setMemo(data[13].toString());
					}
					// 上次修改日期
					if (data[14] != null) {
						objTemp.setLastModifiedDate(data[14].toString());
					}
					// 人员ID
					if (data[15] != null) {
						objTemp.setEmpId(Long.parseLong(data[15].toString()));
					}
					// 部门ID
					if (data[16] != null) {
						objTemp.setDeptId(Long.parseLong(data[16].toString()));
					}
					// 岗位ID
					if (data[17] != null) {
						objTemp.setStationId(Long.parseLong(data[17].toString()));
					}
					arlstResult.add(objTemp);
					objResult.setList(arlstResult);
					objResult.setTotalCount(new Long(1));
				}
			}
			LogUtil.log("EJB:取得指定的合同信息正常结束", Level.INFO, null);
			return objResult;
		} catch (NumberFormatException e) {
			LogUtil.log("EJB:取得指定的合同信息异常结束", Level.SEVERE, e);
		    throw new SQLException("");
		}
    	
    } 
    
    /**
     * 根据部门ID取得部门名 
     * @param strEnterpriseCode
     * @param fristDepId
     * @return
     */
    public String getFirstdeptName(String strEnterpriseCode, Long fristDepId) {
    	String strFirstdeptName = "";
    	
    	String strSql   = "SELECT"
						+ " DEPT_NAME "
						+ "FROM"
						+ " HR_C_DEPT "
						+ "WHERE"
						+ " DEPT_ID = ? "
						+ "AND"
						+ " ENTERPRISE_CODE = ?";
    	Object params[] = new Object[2];
    	params[0] = fristDepId;
    	params[1] = strEnterpriseCode;
    	strFirstdeptName = bll.getSingal(strSql, params).toString();
		return strFirstdeptName;
    }
    
    /**
     * 取得人员部门和岗位
     * @param strEnterpriseCode
     * @param lngEmpId
     * @return
     */
    @SuppressWarnings("unchecked")
	public Object[] getDeptAndStation(String strEnterpriseCode, Long lngEmpId) {
    	Object[] objTemp = new Object[4];
    	
    	String strSql   = "SELECT"
						+ " B.DEPT_ID,"
						+ " B.DEPT_NAME,"
						+ " C.STATION_ID,"
						+ " C.STATION_NAME "
						+ "FROM"
						+ " HR_J_EMP_INFO A "
						+ "LEFT JOIN "
						+ " HR_C_DEPT B "
						+ "ON"
						+ " A.DEPT_ID = B.DEPT_ID "
						+ "AND "
						+ " B.ENTERPRISE_CODE = ? "
						+ "LEFT JOIN"
						+ " HR_C_STATION C "
						+ "ON"
						+ " A.STATION_ID = C.STATION_ID "
						// modify by liuyi 090911 该表中无该属性
//						+ "AND  "
//						+ " C.ENTERPRISE_CODE = ? "
						+ "WHERE"
						+ " A.EMP_ID = ? ";
    	// modify liuyi 090911 
//    	Object params[] = new Object[3];
//    	params[0] = strEnterpriseCode;
//    	params[1] = strEnterpriseCode;
//    	params[2] = lngEmpId;
    	Object params[] = new Object[2];
    	params[0] = strEnterpriseCode;
    	params[1] = lngEmpId;
    	
    	List lstResult = bll.queryByNativeSQL(strSql, params);
    	
    	if (lstResult != null && lstResult.size() > 0) {
    		objTemp = (Object[])(lstResult.get(0));
    	}
    	return objTemp;
    }
}
