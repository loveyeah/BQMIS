/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 合同台帐查询Facade
 * 
 * @author jincong
 * @version 1.0
 */
@Stateless
public class ContractQueryFacade implements ContractQueryFacadeRemote {

	@EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	
	/** 字符串: 空字符串 */
	public final String STRING_BLANK = "";
	/** 字符串: 新签合同 */
	public final String STRING_CONTRACT_TYPE_NEW = "新签合同";
	/** 字符串: 续签合同 */
	public final String STRING_CONTRACT_TYPE_CONTINUE = "续签合同";
	/** 字符串: 否 */
	public final String STRING_NO = "否";
	/** 字符串: 是 */
	public final String STRING_YES = "是";
	/** 字符串: 终止 */
	public final String STRING_STOP = "终止";
	/** 字符串: 解除 */
	public final String STRING_RELEASE = "解除";
	/** 字符串: 男 */
	public final String STRING_MALE = "男";
	/** 字符串: 女 */
	public final String STRING_FEMALE = "女";
	/** 是否使用 */
	public final String IS_USE_Y = "Y";
	/** 劳动合同附件.附件来源: 新合同 */
	public final String FILE_ORIGER_NEW = "0";
	/** 劳动合同附件.附件来源: 变更合同 */
	public final String FILE_ORIGER_CHANGE = "1";
	/** 劳动合同附件.附件来源: 续签合同 */
	public final String FILE_ORIGER_CONTINUE = "2";
	/** 劳动合同附件.附件来源: 终止合同 */
	public final String FILE_ORIGER_STOP = "3";
	/** 劳动合同形式: 新签合同 */
	public final String CONTRACT_TYPE_NEW = "0";
	/** 劳动合同形式: 续签合同 */
	public final String CONTRACT_TYPE_CONTINUE = "1";
	/** 劳动合同登记表.是否劳动合同正在执行: 否 */
	public final String IF_EXECUTE_N = "0";
	/** 劳动合同登记表.是否劳动合同正在执行: 是 */
	public final String IF_EXECUTE_Y = "1";
	/** 劳动合同解除终止登记表.终止类别: 终止 */
	public final String STOP_TYPE_STOP = "1";
	/** 劳动合同解除终止登记表.终止类别: 解除 */
	public final String STOP_TYPE_RELEASE = "2";
	/** 人员基本信息表.性别: 男 */
	public final String SEX_MALE = "M";
	/** 人员基本信息表.性别: 女 */
	public final String SEX_FEMALE = "W";
	
	/**
	 * 员工合同查询
	 * 到期合同查询
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param deptCode 部门编码
	 * @param contractTerm 合同有效期
	 * @param contractType 合同形式
	 * @param duetoTime 合同到期月份
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getContractQueryEmployee(String startDate, String endDate,
			String deptCode, String contractTerm, String contractType,
			String duetoTime, String enterpriseCode, final int... rowStartIdxAndCount) {
		// Log开始
		if(duetoTime != null && !(STRING_BLANK.equals(duetoTime))) {
			LogUtil.log("EJB:到期合同查询开始。", Level.INFO, null);
		} else {
			LogUtil.log("EJB:员工合同查询开始。", Level.INFO, null);
		}
        try {
        	PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	// 查询sql
        	String sql =
        		"SELECT DISTINCT " +
        			"A.CHS_NAME, " +
        			"B.WROK_CONTRACT_NO, " +
        			"B.EMP_ID, " +
        			"B.FRIST_ADDREST, " +
        			"decode(B.CONTRACT_CONTINUE_MARK, '" +
        			CONTRACT_TYPE_NEW + "', '" + STRING_CONTRACT_TYPE_NEW + "', '" +
        			CONTRACT_TYPE_CONTINUE + "', '" + STRING_CONTRACT_TYPE_CONTINUE + "'), " +
        			"TO_CHAR(B.WORK_SIGN_DATE, 'YYYY-MM-DD'), " +
        			"TO_CHAR(B.START_DATE, 'YYYY-MM-DD'), " +
        			"TO_CHAR(B.END_DATE, 'YYYY-MM-DD'), " +
        			"TO_CHAR(B.TRYOUT_START_DATE, 'YYYY-MM-DD'), " +
        			"TO_CHAR(B.TRYOUT_END_DATE, 'YYYY-MM-DD'), " +
        			"B.MEMO, " +
        			"C.DEPT_NAME DEPT_NAME1, " +
        			"D.DEPT_NAME DEPT_NAME2, " +
        			"E.STATION_NAME, " +
        			"F.CONTRACT_TERM, " +
        			"B.WORKCONTRACTID WORKCONTRACTID1, " +
        			"G.WORKCONTRACTID WORKCONTRACTID2, " +
        			"G.FILE_ORIGER, " +
        			"B.IF_EXECUTE " +
        		"FROM " +
        			"HR_J_EMP_INFO A, " +
        			"HR_J_WORKCONTRACT B " +
        			"LEFT JOIN HR_J_CARWH_INVOICE G " +
        			"ON B.WORKCONTRACTID = G.WORKCONTRACTID " +
        			"AND G.FILE_ORIGER IN(" + FILE_ORIGER_NEW + 
        			", " + FILE_ORIGER_CONTINUE + ") " +
        			"AND G.IS_USE = ? " +
        			"AND G.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_CONTRACTTERM F " +
        			"ON B.CONTRACT_TERM_ID = F.CONTRACT_TERM_ID AND " +
        			"F.IS_USE = ? AND " +
        			"F.ENTERPRISE_CODE = ? " +
        			" LEFT JOIN HR_C_STATION E on b.station_id=e.station_id, " + 
        			"HR_C_DEPT C, " +
        			"HR_C_DEPT D " +
        			
        		"WHERE " +
        			"B.EMP_ID = A.EMP_ID AND " +
        			"B.FRIST_DEP_ID = C.DEPT_ID AND " +
        			"B.DEPT_ID = D.DEPT_ID AND " +
        			// modify by liuyi 090910 
//        			"A.IS_USE = ? AND " +
        			"B.IS_USE = ? AND " +
        			"A.ENTERPRISE_CODE = ? AND " +
        			"B.ENTERPRISE_CODE = ? AND " +
        			"C.ENTERPRISE_CODE = ? AND " +
        			// modify by liuyi 090910 17:17
//        			"D.ENTERPRISE_CODE = ? AND " +
//        			"E.ENTERPRISE_CODE = ? ";
        			"D.ENTERPRISE_CODE = ?  ";
        			
        	if(startDate != null && !(STRING_BLANK.equals(startDate))) {
        		sql += "AND TO_CHAR(B.WORK_SIGN_DATE, 'YYYY-MM-DD') >= ? ";
        		listParams.add(startDate);
        	}
        	if(endDate != null && !(STRING_BLANK.equals(endDate))) {
        		sql += "AND TO_CHAR(B.WORK_SIGN_DATE, 'YYYY-MM-DD') <= ? ";
        		listParams.add(endDate);
        	}
        	if(contractTerm != null && !(STRING_BLANK.equals(contractTerm))) {
        		sql += "AND B.CONTRACT_TERM_ID = ? ";
        		listParams.add(contractTerm);
        	}
        	if(deptCode != null && !(STRING_BLANK.equals(deptCode))) {
        		sql += "AND B.DEPT_ID = ? ";
        		listParams.add(deptCode);
        	}
        	if(contractType != null && !(STRING_BLANK.equals(contractType))) {
        		sql += "AND B.CONTRACT_CONTINUE_MARK = ? ";
        		listParams.add(contractType);
        	}
        	if(duetoTime != null && !(STRING_BLANK.equals(duetoTime))) {
        		sql += "AND TO_CHAR(B.END_DATE, 'YYYYMM') <= ? ";
        		listParams.add(duetoTime);
        	}
        	sql +=
        		"ORDER BY B.EMP_ID, " +
        		"B.WORKCONTRACTID";
        	String sqlCount = "SELECT COUNT(1) FROM ( " + sql + " ) Z";
        	// 生成查询参数
        	Object[] params = listParams.toArray();
        	Object[] paramsNew = new Object[params.length + 9];
        	// 是否使用
        	paramsNew[0] = IS_USE_Y;
        	paramsNew[2] = IS_USE_Y;
        	// 企业代码
        	paramsNew[1] = enterpriseCode;
        	paramsNew[3] = enterpriseCode;
        	// 是否使用
//        	for(int i = 4; i < 6; i++) {
        	for(int i = 4; i < 5; i++) {
        		paramsNew[i] = IS_USE_Y;
        	}
        	// 企业代码
//        	for(int i = 6; i < 11; i++) {
        	for(int i = 5; i < 9; i++) {
        		paramsNew[i] = enterpriseCode;
        	}
        	// 其他参数
        	for(int i = 0; i < params.length; i++) {
        		paramsNew[9 + i] = params[i];
        	}
        	List list = bll.queryByNativeSQL(sql,
        			paramsNew, rowStartIdxAndCount);
        	Iterator it = list.iterator();
        	List<ContractQuery> arrlist = new ArrayList<ContractQuery>();
            while (it.hasNext()) {
            	ContractQuery info = new ContractQuery();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setChsName(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setWorkContractNo(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setEmpId(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setFirstAddress(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setContractContinueMark(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setWorkSignDate(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setStartDate(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setEndDate(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setTryoutStartDate(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setTryoutEndDate(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setMemoContract(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setDeptNameFirst(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setDeptNameSecond(data[12].toString());
            	}
            	if(null != data[13]) {
            		info.setStationName(data[13].toString());
            	}
            	if(null != data[14]) {
            		info.setContractTerm(data[14].toString());
            	}
            	if(null != data[15]) {
            		info.setWorkContractIdContract(data[15].toString());
            	}
            	if(null != data[16]) {
            		info.setWorkContractIdInvoice(data[16].toString());
            	}
            	if(null != data[17]) {
            		info.setFileOriger(data[17].toString());
            	}
            	if(null != data[18]) {
            		info.setIfExecute(data[18].toString());
            	}
            	arrlist.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		paramsNew).toString());
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            // Log结束
            if(duetoTime != null && !(STRING_BLANK.equals(duetoTime))) {
            	LogUtil.log("EJB:到期合同查询结束。", Level.INFO, null);
            } else {
            	LogUtil.log("EJB:员工合同查询结束。", Level.INFO, null);
            }
            return object;
        } catch (RuntimeException e) {
        	if(duetoTime != null && !(STRING_BLANK.equals(duetoTime))) {
        		LogUtil.log("EJB:到期合同查询失败。", Level.SEVERE, e);
        	} else {
        		LogUtil.log("EJB:员工合同查询失败。", Level.SEVERE, e);
        	}
            throw e;
        }
	}
	
	/**
	 * 续签合同查询
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param deptCode 部门编码
	 * @param contractTerm 合同有效期
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getContractQueryContinue(String startDate, String endDate,
			String deptCode, String contractTerm, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		// Log开始
		LogUtil.log("EJB:续签合同查询开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	// 查询sql
        	String sql =
        		"SELECT DISTINCT " +
        			"A.CHS_NAME, " +
        			"B.WROK_CONTRACT_NO, " +
        			"B.EMP_ID, " +
        			"B.FRIST_ADDREST, " +
        			"decode(B.IF_EXECUTE, '" +
        			IF_EXECUTE_N + "', '" + STRING_NO + "', '" +
        			IF_EXECUTE_Y + "', '" + STRING_YES + "'), " +
        			"TO_CHAR(B.WORK_SIGN_DATE, 'YYYY-MM-DD'), " +
        			"TO_CHAR(B.START_DATE, 'YYYY-MM-DD'), " +
        			"TO_CHAR(B.END_DATE, 'YYYY-MM-DD'), " +
        			"B.MEMO, " +
        			"C.DEPT_NAME DEPT_NAME1, " +
        			"D.DEPT_NAME DEPT_NAME2, " +
        			"E.STATION_NAME, " +
        			"F.CONTRACT_TERM, " +
        			"B.WORKCONTRACTID WORKCONTRACTID1, " +
        			"G.WORKCONTRACTID WORKCONTRACTID2, " +
        			"G.FILE_ORIGER " +
        		"FROM " +
        			"HR_J_EMP_INFO A, " +
        			"HR_J_WORKCONTRACT B " +
        			"LEFT JOIN HR_J_CARWH_INVOICE G " +
        			"ON B.WORKCONTRACTID = G.WORKCONTRACTID " +
        			"AND G.FILE_ORIGER = '" + FILE_ORIGER_CONTINUE + "' " +
        			"AND G.IS_USE = ? " +
        			"AND G.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_CONTRACTTERM F " +
        			"ON B.CONTRACT_TERM_ID = F.CONTRACT_TERM_ID AND " +
        			"F.IS_USE = ? AND " +
        			"F.ENTERPRISE_CODE = ? " +
        			" LEFT JOIN HR_C_STATION E on b.station_id=e.station_id, " + 
        			"HR_C_DEPT C, " +
        			"HR_C_DEPT D " +
        		"WHERE " +
        			"B.CONTRACT_CONTINUE_MARK = '" + CONTRACT_TYPE_CONTINUE + "' AND " +
        			"B.EMP_ID = A.EMP_ID AND " +
        			"B.FRIST_DEP_ID = C.DEPT_ID AND " +
        			"B.DEPT_ID = D.DEPT_ID AND " +
        			"B.STATION_ID = E.STATION_ID AND " +
        			// modify by liuyi 090910 
//        			"A.IS_USE = ? AND " +
        			"B.IS_USE = ? AND " +
        			"A.ENTERPRISE_CODE = ? AND " +
        			"B.ENTERPRISE_CODE = ? AND " +
        			"C.ENTERPRISE_CODE = ? AND " +
        			// modify by liuyi 090910 17:17
//        			"D.ENTERPRISE_CODE = ? AND " +
//        			"E.ENTERPRISE_CODE = ? ";
        			"D.ENTERPRISE_CODE = ?  ";
        	if(startDate != null && !(STRING_BLANK.equals(startDate))) {
        		sql += "AND TO_CHAR(B.WORK_SIGN_DATE, 'YYYY-MM-DD') >= ? ";
        		listParams.add(startDate);
        	}
        	if(endDate != null && !(STRING_BLANK.equals(endDate))) {
        		sql += "AND TO_CHAR(B.WORK_SIGN_DATE, 'YYYY-MM-DD') <= ? ";
        		listParams.add(endDate);
        	}
        	if(contractTerm != null && !(STRING_BLANK.equals(contractTerm))) {
        		sql += "AND B.CONTRACT_TERM_ID = ? ";
        		listParams.add(contractTerm);
        	}
        	if(deptCode != null && !(STRING_BLANK.equals(deptCode))) {
        		sql += "AND B.DEPT_ID = ? ";
        		listParams.add(deptCode);
        	}
        	sql +=
        		"ORDER BY B.EMP_ID, " +
        		"B.WORKCONTRACTID";
        	String sqlCount = "SELECT COUNT(1) FROM ( " + sql + " ) Z";
        	// 生成查询参数
        	Object[] params = listParams.toArray();
        	Object[] paramsNew = new Object[params.length + 9];
        	// 是否使用
        	paramsNew[0] = IS_USE_Y;
        	paramsNew[2] = IS_USE_Y;
        	// 企业代码
        	paramsNew[1] = enterpriseCode;
        	paramsNew[3] = enterpriseCode;
        	// 是否使用
//        	for(int i = 4; i < 6; i++) {
        	for(int i = 4; i < 5; i++) {
        		paramsNew[i] = IS_USE_Y;
        	}
        	// 企业代码
//        	for(int i = 6; i < 11; i++) {
        	for(int i = 5; i < 9; i++) {
        		paramsNew[i] = enterpriseCode;
        	}
        	// 其他参数
        	for(int i = 0; i < params.length; i++) {
        		paramsNew[9 + i] = params[i];
        	}
        	List list = bll.queryByNativeSQL(sql,
        			paramsNew, rowStartIdxAndCount);
        	Iterator it = list.iterator();
        	List<ContractQuery> arrlist = new ArrayList<ContractQuery>();
            while (it.hasNext()) {
            	ContractQuery info = new ContractQuery();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setChsName(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setWorkContractNo(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setEmpId(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setFirstAddress(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setIfExecute(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setWorkSignDate(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setStartDate(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setEndDate(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setMemoContract(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setDeptNameFirst(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setDeptNameSecond(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setStationName(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setContractTerm(data[12].toString());
            	}
            	if(null != data[13]) {
            		info.setWorkContractIdContract(data[13].toString());
            	}
            	if(null != data[14]) {
            		info.setWorkContractIdInvoice(data[14].toString());
            	}
            	if(null != data[15]) {
            		info.setFileOriger(data[15].toString());
            	}
            	arrlist.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		paramsNew).toString());
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            // Log结束
    		LogUtil.log("EJB:续签合同查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:续签合同查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 合同变更查询
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param deptBeforeCode 变更前部门编码
	 * @param deptAfterCode 变更后部门编码
	 * @param contractTerm 合同有效期
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getContractQueryChange(String startDate, String endDate,
			String deptBeforeCode, String deptAfterCode, String contractTerm,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		// Log开始
		LogUtil.log("EJB:合同变更查询开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	// 查询sql
        	String sql =
        		"SELECT DISTINCT " +
        			"A.CHS_NAME, " +
        			"TO_CHAR(B.CHARGE_DATE, 'YYYY-MM-DD'), " +
        			"TO_CHAR(B.OLD_STATE_TIME, 'YYYY-MM-DD'), " +
        			"TO_CHAR(B.NEW_STATE_TIME, 'YYYY-MM-DD'), " +
        			"TO_CHAR(B.OLD_END_TIME, 'YYYY-MM-DD'), " +
        			"TO_CHAR(B.NEW_END_TIME, 'YYYY-MM-DD'), " +
        			"B.CHANGE_REASON, " +
        			"B.MEMO, " +
        			"B.CONTRACTCHANGEID, " +
        			"C.EMP_ID, " +
        			"D.DEPT_NAME DEPT_NAME1, " +
        			"E.DEPT_NAME DEPT_NAME2, " +
        			"F.STATION_NAME STATION_NAME1, " +
        			"G.STATION_NAME STATION_NAME2, " +
        			"H.CONTRACT_TERM CONTRACT_TERM1, " +
        			"I.CONTRACT_TERM CONTRACT_TERM2, " +
        			"J.WORKCONTRACTID WORKCONTRACTID1, " +
        			"K.WORKCONTRACTID WORKCONTRACTID2, " +
        			"J.FILE_ORIGER FILE_ORIGER1, " +
        			"K.FILE_ORIGER FILE_ORIGER2 " +
        		"FROM " +
        			"HR_J_EMP_INFO A, " +
        			"HR_J_WORKCONTRACT C, " +
        			"HR_J_CONTRACTCHANGE B " +
        			"LEFT JOIN HR_C_CONTRACTTERM H " +
        			"ON B.OLD_CONTRACT_CODE = H.CONTRACT_TERM_ID " +
        			"AND H.IS_USE = ? " +
        			"AND H.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_CONTRACTTERM I " +
        			"ON B.NEW_CONTRACT_CODE = I.CONTRACT_TERM_ID " +
        			"AND I.IS_USE = ? " +
        			"AND I.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_J_CARWH_INVOICE J " +
        			"ON B.WORKCONTRACTID = J.WORKCONTRACTID " +
        			"AND J.FILE_ORIGER IN('" + FILE_ORIGER_NEW + 
        			"', '" + FILE_ORIGER_CONTINUE + "') " +
        			"AND J.IS_USE = ? " +
        			"AND J.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_J_CARWH_INVOICE K " +
        			"ON B.CONTRACTCHANGEID = K.WORKCONTRACTID " +
        			"AND K.FILE_ORIGER = '" + FILE_ORIGER_CHANGE + "' " +
        			"AND K.IS_USE = ? " +
        			"AND K.ENTERPRISE_CODE = ?, " +
        			"HR_C_DEPT D, " +
        			"HR_C_DEPT E, " +
        			"HR_C_STATION F, " +
        			"HR_C_STATION G " +
        		"WHERE " +
        			"B.WORKCONTRACTID = C.WORKCONTRACTID AND " +
        			"C.EMP_ID = A.EMP_ID AND " +
        			"B.OLD_DEP_CODE = D.DEPT_ID AND " +
        			"B.NEW_DEP_CODE = E.DEPT_ID AND " +
        			"B.OLD_STATION_CODE = F.STATION_ID AND " +
        			"B.NEW_STATION_CODE = G.STATION_ID AND " +
        			// modify by liuyi 090911 
//        			"A.IS_USE = ? AND " +
        			"B.IS_USE = ? AND " +
        			"C.IS_USE = ? AND " +
        			"A.ENTERPRISE_CODE = ? AND " +
        			"B.ENTERPRISE_CODE = ? AND " +
        			"C.ENTERPRISE_CODE = ? AND " +
        			"D.ENTERPRISE_CODE = ? AND " +
        			"E.ENTERPRISE_CODE = ? " ;
        	// modify by liuyi 090911 
//        			"and F.ENTERPRISE_CODE = ? AND " +
//        			"G.ENTERPRISE_CODE = ? "; 
        	if(startDate != null && !(STRING_BLANK.equals(startDate))) {
        		sql += "AND TO_CHAR(B.CHARGE_DATE, 'YYYY-MM-DD') >= ? ";
        		listParams.add(startDate);
        	}
        	if(endDate != null && !(STRING_BLANK.equals(endDate))) {
        		sql += "AND TO_CHAR(B.CHARGE_DATE, 'YYYY-MM-DD') <= ? ";
        		listParams.add(endDate);
        	}
        	if(contractTerm != null && !(STRING_BLANK.equals(contractTerm))) {
        		sql += "AND B.NEW_CONTRACT_CODE = ? ";
        		listParams.add(contractTerm);
        	}
        	if(deptBeforeCode != null && !(STRING_BLANK.equals(deptBeforeCode))) {
        		sql += "AND B.OLD_DEP_CODE = ? ";
        		listParams.add(deptBeforeCode);
        	}
        	if(deptAfterCode != null && !(STRING_BLANK.equals(deptAfterCode))) {
        		sql += "AND B.NEW_DEP_CODE = ? ";
        		listParams.add(deptAfterCode);
        	}
        	sql +=
        		"ORDER BY C.EMP_ID, " +
        		"B.CONTRACTCHANGEID";
        	String sqlCount = "SELECT COUNT(1) FROM ( " + sql + " ) Z";
        	// 生成查询参数
        	Object[] params = listParams.toArray();
        	Object[] paramsNew = new Object[params.length + 15];
        	// 是否使用
        	paramsNew[0] = IS_USE_Y;
        	paramsNew[2] = IS_USE_Y;
        	paramsNew[4] = IS_USE_Y;
        	paramsNew[6] = IS_USE_Y;
        	// 企业代码
        	paramsNew[1] = enterpriseCode;
        	paramsNew[3] = enterpriseCode;
        	paramsNew[5] = enterpriseCode;
        	paramsNew[7] = enterpriseCode;
        	// 是否使用
        	for(int i = 8; i < 10; i++) {
        		paramsNew[i] = IS_USE_Y;
        	}
        	// 企业代码
        	for(int i = 10; i < 15; i++) {
        		paramsNew[i] = enterpriseCode;
        	}
        	// 其他参数
        	for(int i = 0; i < params.length; i++) {
        		paramsNew[15 + i] = params[i];
        	}
        	List list = bll.queryByNativeSQL(sql,
        			paramsNew, rowStartIdxAndCount);
        	Iterator it = list.iterator();
        	List<ContractQuery> arrlist = new ArrayList<ContractQuery>();
            while (it.hasNext()) {
            	ContractQuery info = new ContractQuery();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setChsName(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setChangeDate(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setOldStartTime(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setNewStartTime(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setOldEndTime(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setNewEndTime(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setChangeReason(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setMemoChange(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setContractChangedId(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setEmpId(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setDeptNameFirst(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setDeptNameSecond(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setStationNameBefore(data[12].toString());
            	}
            	if(null != data[13]) {
            		info.setStationNameAfter(data[13].toString());
            	}
            	if(null != data[14]) {
            		info.setContractTermBefore(data[14].toString());
            	}
            	if(null != data[15]) {
            		info.setContractTermAfter(data[15].toString());
            	}
            	if(null != data[16]) {
            		info.setWorkContractIdInvoiceBefore(data[16].toString());
            	}
            	if(null != data[17]) {
            		info.setWorkContractIdInvoiceAfter(data[17].toString());
            	}
            	if(null != data[18]) {
            		info.setFileOrigerBefore(data[18].toString());
            	}
            	if(null != data[19]) {
            		info.setFileOrigerAfter(data[19].toString());
            	}
            	arrlist.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		paramsNew).toString());
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            // Log结束
    		LogUtil.log("EJB:合同变更查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:合同变更查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 合同终止查询
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param deptCode 部门编码
	 * @param contractTerm 合同有效期
	 * @param stopType 终止类别
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getContractQueryStop(String startDate, String endDate,
			String deptCode, String contractTerm, String stopType,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		// Log开始
		LogUtil.log("EJB:合同终止查询开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	// 查询sql
        	String sql =
        		"SELECT DISTINCT " +
        			"A.CHS_NAME, " +
        			"TO_CHAR(B.REAL_END_TIME, 'YYYY-MM-DD'), " +
        			"DECODE(B.CONTRACT_STOP_TYPE, '" +
        			STOP_TYPE_STOP + "', '" + STRING_STOP + "', '" +
        			STOP_TYPE_RELEASE + "', '" + STRING_RELEASE + "'), " +
        			"B.RELEASE_REASON, " +
        			"TO_CHAR(B.START_DATE, 'YYYY-MM-DD'), " +
        			"TO_CHAR(B.END_DATE, 'YYYY-MM-DD'), " +
        			"B.DOSSIER_DIRECTION, " +
        			"B.SOCIETY_INSURANCE_DIRECTION, " +
        			"B.MEMO, " +
        			"B.CONTRACTSTOPID, " +
        			"B.EMP_ID, " +
        			"D.DEPT_NAME, " +
        			"E.STATION_NAME, " +
        			"F.CONTRACT_TERM, " +
        			"G.WORKCONTRACTID, " +
        			"G.FILE_ORIGER " +
        		"FROM " +
        			"HR_J_EMP_INFO A, " +
        			"HR_J_CONTRACTSTOP B " +
        			"LEFT JOIN HR_J_CARWH_INVOICE G " +
        			"ON B.CONTRACTSTOPID = G.WORKCONTRACTID " +
        			"AND G.FILE_ORIGER = '" + FILE_ORIGER_STOP + "' " +
        			"AND G.IS_USE = ? " +
        			"AND G.ENTERPRISE_CODE = ?, " +
        			"HR_J_WORKCONTRACT C " +
        			"LEFT JOIN HR_C_CONTRACTTERM F " +
        			"ON C.CONTRACT_TERM_ID = F.CONTRACT_TERM_ID " +
        			"AND F.IS_USE = ? " +
        			"AND F.ENTERPRISE_CODE = ?, " +
        			"HR_C_DEPT D, " +
        			"HR_C_STATION E " +
        		"WHERE " +        			
        			"B.WORKCONTRACTID = C.WORKCONTRACTID AND " +
        			"B.EMP_ID = A.EMP_ID AND " +
        			"B.DEPT_ID = D.DEPT_ID AND " +
        			"B.STATION_ID = E.STATION_ID AND " +
        			// modify by liuyi 090911 
//        			"A.IS_USE = ? AND " +
        			"B.IS_USE = ? AND " +        			
        			"A.ENTERPRISE_CODE = ? AND " +
        			"B.ENTERPRISE_CODE = ? AND " +
        			"C.ENTERPRISE_CODE = ? AND " +
        			"D.ENTERPRISE_CODE = ?";
        	// modify by liuyi 090911 
//        			" AND E.ENTERPRISE_CODE = ? ";
        	if(startDate != null && !(STRING_BLANK.equals(startDate))) {
        		sql += "AND TO_CHAR(B.REAL_END_TIME, 'YYYY-MM-DD') >= ? ";
        		listParams.add(startDate);
        	}
        	if(endDate != null && !(STRING_BLANK.equals(endDate))) {
        		sql += "AND TO_CHAR(B.REAL_END_TIME, 'YYYY-MM-DD') <= ? ";
        		listParams.add(endDate);
        	}
        	if(contractTerm != null && !(STRING_BLANK.equals(contractTerm))) {
        		sql += "AND C.CONTRACT_TERM_ID = ? ";
        		listParams.add(contractTerm);
        	}
        	if(stopType != null && !(STRING_BLANK.equals(stopType))) {
        		sql += "AND B.CONTRACT_STOP_TYPE = ? ";
        		listParams.add(stopType);
        	}
        	if(deptCode != null && !(STRING_BLANK.equals(deptCode))) {
        		sql += "AND B.DEPT_ID = ? ";
        		listParams.add(deptCode);
        	}
        	sql +=
        		"ORDER BY B.EMP_ID, " +
        		"B.CONTRACTSTOPID";
        	String sqlCount = "SELECT COUNT(1) FROM ( " + sql + " ) Z";
        	// 生成查询参数
        	Object[] params = listParams.toArray();
        	Object[] paramsNew = new Object[params.length + 9];
        	// 是否使用
        	paramsNew[0] = IS_USE_Y;
        	paramsNew[2] = IS_USE_Y;
        	// 企业代码
        	paramsNew[1] = enterpriseCode;
        	paramsNew[3] = enterpriseCode;
        	// 是否使用
        	for(int i = 4; i < 5; i++) {
        		paramsNew[i] = IS_USE_Y;
        	}
        	// 企业代码
        	for(int i = 5; i < 9; i++) {
        		paramsNew[i] = enterpriseCode;
        	}
        	// 其他参数
        	for(int i = 0; i < params.length; i++) {
        		paramsNew[9 + i] = params[i];
        	}
        	List list = bll.queryByNativeSQL(sql,
        			paramsNew, rowStartIdxAndCount);
        	Iterator it = list.iterator();
        	List<ContractQuery> arrlist = new ArrayList<ContractQuery>();
            while (it.hasNext()) {
            	ContractQuery info = new ContractQuery();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setChsName(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setRealEndTime(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setContractStopType(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setReleaseReason(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setStartDateStop(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setEndDateStop(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setDossierDirection(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setSocietyInsuranceDirection(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setMemoStop(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setContractStopId(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setEmpId(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setDeptName(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setStationName(data[12].toString());
            	}
            	if(null != data[13]) {
            		info.setContractTerm(data[13].toString());
            	}
            	if(null != data[14]) {
            		info.setWorkContractIdInvoice(data[14].toString());
            	}
            	if(null != data[15]) {
            		info.setFileOriger(data[15].toString());
            	}
            	arrlist.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		paramsNew).toString());
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            // Log结束
    		LogUtil.log("EJB:合同终止查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:合同终止查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 未签合同查询
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param deptCode 部门编码
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getContractQueryNotsign(String startDate, String endDate,
			String deptCode, String enterpriseCode, final int... rowStartIdxAndCount) {
		// Log开始
		LogUtil.log("EJB:未签合同查询开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	// 查询sql
        	String sql =
        		"SELECT " +
        			"A.DEPT_NAME, " +
        			"B.EMP_ID, " +
        			"B.CHS_NAME, " +
        			"B.EMP_CODE, " +
        			"B.ARCHIVES_ID, " +
        			"decode(B.SEX, '" +
        			SEX_MALE + "', '" + STRING_MALE + "', '" +
        			SEX_FEMALE + "', '" + STRING_FEMALE + "'), " +
        			"TO_CHAR(B.BRITHDAY,'YYYY-MM-DD'), " +
        			"TO_CHAR(B.WORK_DATE,'YYYY-MM-DD'), " +
        			"TO_CHAR(B.MISSION_DATE,'YYYY-MM-DD') ";
        	String sqlWhere =
        		"FROM " +
        			"HR_C_DEPT A, " +
        			"HR_J_EMP_INFO B " +
        		"WHERE " +
        			"B.DEPT_ID = A.DEPT_ID AND " +
        			"B.EMP_ID NOT IN " +
        			"(SELECT DISTINCT " +
        			"C.EMP_ID " +
        			"FROM HR_J_WORKCONTRACT C " +
        			"WHERE C.IF_EXECUTE = '" + IF_EXECUTE_Y + "') " +
        			"AND A.ENTERPRISE_CODE = ? " +
        			// modify by liuyi 090911 该表中无此属性
//        			"AND B.IS_USE = ?  " +
        			"AND B.ENTERPRISE_CODE = ? ";
        	if(startDate != null && !(STRING_BLANK.equals(startDate))) {
        		sqlWhere += "AND TO_CHAR(B.MISSION_DATE, 'YYYY-MM-DD') >= ? ";
        		listParams.add(startDate);
        	}
        	if(endDate != null && !(STRING_BLANK.equals(endDate))) {
        		sqlWhere += "AND TO_CHAR(B.MISSION_DATE, 'YYYY-MM-DD') <= ? ";
        		listParams.add(endDate);
        	}
        	if(deptCode != null && !(STRING_BLANK.equals(deptCode))) {
        		sqlWhere += "AND B.DEPT_ID = ? ";
        		listParams.add(deptCode);
        	}
        	sqlWhere +=
        		"ORDER BY B.EMP_ID";
        	sql += sqlWhere;
        	String sqlCount = "SELECT COUNT(1) " + sqlWhere;
        	// 生成查询参数
        	Object[] params = listParams.toArray();
        	Object[] paramsNew = new Object[params.length + 2];
        	// 是否使用
//        	paramsNew[1] = IS_USE_Y;
        	// 企业代码
        	paramsNew[0] = enterpriseCode;
        	paramsNew[1] = enterpriseCode;
        	// 其他参数
        	for(int i = 0; i < params.length; i++) {
        		paramsNew[2 + i] = params[i];
        	}
        	List list = bll.queryByNativeSQL(sql,
        			paramsNew, rowStartIdxAndCount);
        	Iterator it = list.iterator();
        	List<ContractQuery> arrlist = new ArrayList<ContractQuery>();
            while (it.hasNext()) {
            	ContractQuery info = new ContractQuery();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setDeptName(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setEmpId(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setChsName(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setEmpCode(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setArchivesId(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setSex(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setBirthday(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setWorkDate(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setMissionDate(data[8].toString());
            	}
            	arrlist.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		paramsNew).toString());
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            // Log结束
    		LogUtil.log("EJB:未签合同查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:未签合同查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 查找合同有效期数据
	 * 
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findContractTerm(String enterpriseCode,
			final int... rowStartIdxAndCount) throws RuntimeException {
		LogUtil.log("EJB:劳动合同有效期查询开始。",
                Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// sql
        	String sql =
        		"SELECT * " +
        		"FROM " +
        			"HR_C_CONTRACTTERM A " +
        		"WHERE " +
        			"A.IS_USE = ? AND " +
        			"A.ENTERPRISE_CODE = ?"+
                "ORDER BY "+
                    "A.CONTRACT_TERM_ID";
        	String sqlCount =
        		"SELECT COUNT(A.CONTRACT_TERM_ID) " +
        		"FROM " +
        			"HR_C_CONTRACTTERM A " +
        		"WHERE " +
        			"A.IS_USE = ? AND " +
        			"A.ENTERPRISE_CODE = ? "+
        		"ORDER BY "+
        		    "A.CONTRACT_TERM_ID";
        	List<HrCContractterm> list = bll.queryByNativeSQL(sql,
        			new Object[]{IS_USE_Y, enterpriseCode},
        			HrCContractterm.class, rowStartIdxAndCount);
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		new Object[]{IS_USE_Y, enterpriseCode}).toString());
            if(list == null) {
            	list = new ArrayList<HrCContractterm>();
            }
            object.setList(list);
            object.setTotalCount(totalCount);
            LogUtil.log("EJB:劳动合同有效期维护查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:劳动合同有效期维护查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
}
