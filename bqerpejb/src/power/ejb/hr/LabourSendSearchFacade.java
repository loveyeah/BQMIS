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

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 劳务派遣查询Facade
 * 
 * @author lichensheng
 * @version 1.0
 */

@Stateless
public class LabourSendSearchFacade implements LabourSendSearchFacadeRemote{
	@EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	/**使用标志**/
	private final static String IS_USE_Y = "Y";
	
	/**
	 * 查询劳务派遣信息
	 * @param strStartDate 起始日期
	 * @param strEndDate 结束日期
	 * @param strCooperateUnit 协助单位
	 * @param strDcmStatus 单据状态
	 * @param enterpriseCode 企业编码
	 * @return PageObject
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject ejbGetLabourBy(String strStartDate, String strEndDate,
			String strCooperateUnit, String strDcmStatus,String enterpriseCode,String transferType,
			final int... rowStartIdxAndCount) throws SQLException{
		// Log开始
		LogUtil.log("EJB:劳务派遣查询开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	// 查询sql
        	StringBuffer strSql = new StringBuffer();
        	strSql.append("SELECT ");
        	strSql.append("A.BORROWCONTRACTID,");
        	strSql.append("A.WROK_CONTRACT_NO,");
        	strSql.append("TO_CHAR(A.SIGNATURE_DATE,'YYYY-MM-DD'),");
        	strSql.append("A.COOPERATE_UNIT_ID,");
        	strSql.append("B.COOPERATE_UNIT,");
        	strSql.append("TO_CHAR(A.START_DATE,'YYYY-MM-DD'),");
        	strSql.append("TO_CHAR(A.END_DATE,'YYYY-MM-DD'),");
        	strSql.append("A.CONTRACT_CONTENT,");
        	strSql.append("A.DCM_STATUS,");
        	strSql.append("A.NOTE,");
        	strSql.append("A.TRANSFER_TYPE \n ");
        	
        	StringBuffer sqlWhere = new StringBuffer();
        	sqlWhere.append("FROM ");
        	sqlWhere.append("HR_J_BORROWCONTRACT A ");
        	sqlWhere.append("LEFT JOIN HR_J_COOPERATEUNIT B ");
        	sqlWhere.append("ON A.COOPERATE_UNIT_ID = B.COOPERATE_UNIT_ID ");
        	sqlWhere.append("AND B.IS_USE = ? ");
        	sqlWhere.append("AND B.ENTERPRISE_CODE = ? ");
        	sqlWhere.append("WHERE A.ENTERPRISE_CODE = ? ");
        	sqlWhere.append("AND A.IS_USE = ? ");
            // 拼接条件
            if(strStartDate != null && !("".equals(strStartDate))) {
            	sqlWhere.append("AND TO_CHAR(A.SIGNATURE_DATE, 'YYYY-MM-DD') >= ? ");
        		listParams.add(strStartDate);
        	}
            if(strEndDate != null && !("".equals(strEndDate))) {
            	sqlWhere.append("AND TO_CHAR(A.SIGNATURE_DATE, 'YYYY-MM-DD') <= ? ");
        		listParams.add(strEndDate);
        	}
            if(strCooperateUnit != null && !("".equals(strCooperateUnit))) {
            	sqlWhere.append("AND A.COOPERATE_UNIT_ID = ? ");
        		listParams.add(strCooperateUnit);
        	}
            if(strDcmStatus != null && !("".equals(strDcmStatus))) {
            	sqlWhere.append("AND A.DCM_STATUS = ? ");
            	listParams.add(strDcmStatus);
            }
            if(transferType != null && !("".equals(transferType))) {
            	sqlWhere.append("AND A.TRANSFER_TYPE = ? ");
        		listParams.add(transferType);
        	}
            sqlWhere.append("ORDER BY A.BORROWCONTRACTID ");
            strSql.append(sqlWhere);
            StringBuffer strSqlCount = new StringBuffer();
            strSqlCount.append("SELECT COUNT(A.BORROWCONTRACTID) ");
            strSqlCount.append(sqlWhere);
        	// 查询附件条数sql
            StringBuffer sqlFile = new StringBuffer();
            sqlFile.append("SELECT ");
            sqlFile.append("COUNT(A.FILE_ID) ");
            sqlFile.append("FROM ");
            sqlFile.append("HR_J_CARWH_INVOICE A ");
            sqlFile.append("WHERE ");
            sqlFile.append("A.WORKCONTRACTID = ? ");
            sqlFile.append("AND A.FILE_ORIGER = ? ");
            sqlFile.append("AND A.ENTERPRISE_CODE = ? ");
            sqlFile.append("AND A.IS_USE = ? ");
        	
        	LogUtil.log("SQL=" + strSql, Level.INFO, null);
        	// 生成查询参数
        	Object[] params = listParams.toArray();
        	Object[] paramsNew = new Object[params.length + 4];
        	// 是否使用
        	paramsNew[0] = IS_USE_Y;
        	paramsNew[3] = IS_USE_Y;
        	// 企业代码
        	paramsNew[1] = enterpriseCode;
        	paramsNew[2] = enterpriseCode;
        	for(int i = 0; i < params.length; i++) {
        		paramsNew[4 + i] = params[i];
        	}
        	// 查询
        	List list = bll.queryByNativeSQL(strSql.toString(), paramsNew, rowStartIdxAndCount);
        	List<LabourSendSearchInfo> arrlist = new ArrayList<LabourSendSearchInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) {
            	LabourSendSearchInfo info = new LabourSendSearchInfo();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setBorrowcontractid(Long.parseLong(data[0].toString()));
            		// 查询附件Log开始
                	LogUtil.log("SQL=" + sqlFile, Level.INFO, null);
            		// 查询附件条数
            		Long fileCount = Long.parseLong(bll.getSingal(sqlFile.toString(),
                    		new Object[]{data[0].toString(),
            			    HRCodeConstants.FILE_ORIGER_SERVICE,
            			    enterpriseCode,IS_USE_Y}).toString());
            		info.setFileCount(fileCount);
            	}
            	if(null != data[1]) {
            		info.setWorkContractNo(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setSignatureDate(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setCooperateUnitId(Long.parseLong(data[3].toString()));
            	}
            	if(null != data[4]) {
            		info.setCooperateUnit(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setStartDate(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setEndDate(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setContractContent(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setDcmStatus(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setNote(data[9].toString());
            	}
            	if(null !=data[10])
            	{
            		info.setTransferType(data[10].toString());
            	}
            	arrlist.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(strSqlCount.toString(),
            		paramsNew).toString());
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            // Log结束
    		LogUtil.log("EJB:劳务派遣查询正常结束。", Level.INFO, null);
        	return object;
        	
        }catch (Exception e) {
        	LogUtil.log("EJB:劳务派遣查询异常结束。", Level.SEVERE, e);
            throw new SQLException("");
        }
		
		
	}
	/**
	 * 查询人员一览信息
	 * @param lngBorrowcontractid 劳务派遣合同ID
	 * @param enterpriseCode 企业编码
	 * @return PageObject
	 * @throws SQLException
	 */
	public PageObject ejbGetEmpBy(Long lngBorrowcontractid,String enterpriseCode) throws SQLException{
		// Log开始
		LogUtil.log("EJB:人员一览查询开始。", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// 参数 
//			Object[] params = new Object[9];
			Object[] params = new Object[7];
//			params[0] = IS_USE_Y;
			params[0] = enterpriseCode;
			params[1] = "Y"; //update by sychen 20100901
//			params[1] = "U";
			params[2] = enterpriseCode;
			params[3] = "Y";//update by sychen 20100901
//			params[3] = "U";
//			params[5] = enterpriseCode;
			params[4] = lngBorrowcontractid;
			params[5] = enterpriseCode;
			params[6] = IS_USE_Y;
			// 查询sql
			StringBuffer strSql = new StringBuffer();
			strSql.append("SELECT ");
			strSql.append("A.EMPLOYEEBORROWID, ");
			strSql.append("B.EMP_CODE,");
			strSql.append("B.CHS_NAME,");
			strSql.append("C.DEPT_NAME,");
			strSql.append("D.STATION_NAME,");
			strSql.append("TO_CHAR(A.START_DATE,'YYYY-MM-DD'),");
			strSql.append("TO_CHAR(A.END_DATE,'YYYY-MM-DD'),");
			strSql.append("TO_CHAR(A.STOP_PAY_DATE,'YYYY-MM-DD'),");
			strSql.append("TO_CHAR(A.START_PAY_DATE,'YYYY-MM-DD'),");
			strSql.append("A.IF_BACK,");
			strSql.append("A.MEMO ");
			strSql.append("FROM HR_J_EMPLOYEEBORROW A ");
			strSql.append("LEFT JOIN HR_J_EMP_INFO B ");
			//strSql.append("ON A.EMPLOYEEBORROWID = B.EMP_ID ");
			strSql.append("ON A.EMP_ID = B.EMP_ID ");
			// modify by liuyi 090914 B表中无该属性
//			strSql.append("AND B.IS_USE = ? ");
			strSql.append("AND B.ENTERPRISE_CODE = ? ");
			strSql.append("LEFT JOIN HR_C_DEPT C ");
			strSql.append("ON B.DEPT_ID = C.DEPT_ID ");
			strSql.append("AND C.IS_USE = ? ");
			strSql.append("AND C.ENTERPRISE_CODE = ? ");
			strSql.append("LEFT JOIN HR_C_STATION D ");
			strSql.append("ON B.STATION_ID = D.STATION_ID ");
			strSql.append("AND D.IS_USE = ? ");
			// modify by liuyi 090914 D表中无该属性
//			strSql.append("AND D.ENTERPRISE_CODE = ? ");
			strSql.append("WHERE A.BORROWCONTRACTID = ? ");
			strSql.append("AND A.ENTERPRISE_CODE = ? ");
			strSql.append("AND A.IS_USE = ? ");
			strSql.append("ORDER BY A.EMPLOYEEBORROWID ");
			// sql Log
			LogUtil.log(strSql.toString(), Level.INFO, null);
			List lstQuery = bll.queryByNativeSQL(strSql.toString(), params);
			List<LabourSendSearchInfo> arrQuery = new ArrayList<LabourSendSearchInfo>();
			if (lstQuery != null && lstQuery.size() > 0) {
				Iterator it = lstQuery.iterator();
				while (it.hasNext()) {
					LabourSendSearchInfo model = new LabourSendSearchInfo();
					Object[] data = (Object[]) it.next();
					if (data[0] != null)
					model.setEmployeeborrowid(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						model.setEmpCode(data[1].toString());
					if (data[2] != null)
						model.setChsName(data[2].toString());
					if (data[3] != null)
						model.setDeptName(data[3].toString());
					if (data[4] != null)
						model.setStationName(data[4].toString());
					if (data[5] != null)
						model.setStartDate(data[5].toString());
					if (data[6] != null)
						model.setEndDate(data[6].toString());
					if (data[7] != null)
						model.setStopPayDate(data[7].toString());
					if (data[8] != null)
						model.setStartPayDate(data[8].toString());
					if (data[9] != null)
						model.setIfBack(data[9].toString());
					if (data[10] != null)
						model.setMemo(data[10].toString());
					arrQuery.add(model);
					model = null;
				}
				if (arrQuery.size() > 0) {
					Long lngOne = new Long(1);
					object.setList(arrQuery);
					object.setTotalCount(lngOne);
				}
			}  else {
				Long lngZero = new Long(0);
				object.setTotalCount(lngZero);
			}
			LogUtil.log("EJB:人员一览查询正常结束", Level.INFO, null);
			return object;
			
		}catch (Exception e) {
        	LogUtil.log("EJB:人员一览查询异常结束。", Level.SEVERE, e);
            throw new SQLException("");
        }
	}
	
}
