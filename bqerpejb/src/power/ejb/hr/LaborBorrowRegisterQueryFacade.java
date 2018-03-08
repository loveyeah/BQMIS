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
 * 劳务派遣登记
 * 查询主画面数据
 * @author zhaomingjian
 * @version 1.0
 */
@Stateless
public class LaborBorrowRegisterQueryFacade implements
		LaborBorrowRegisterQueryFacadeRemote {
	@EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	/**删除状态********/
	private static final String DEF_FLAG_DELETE = "0";
	/**修改状态********/
	private static final String DEF_FLAG_UPDATE = "1";
	/**新增状态********/
	private static final String DEF_FLAG_ADD    = "2";
	/**是否使用**/
	public final String IS_USE_Y ="Y";
	
	/**
	 * 
	 * @param strSignatureDateFrom
	 * @param strSignatureDateTo
	 * @param cooperateUnitId
	 * @param dcmStatus
	 * @param strEnterpriseCode
	 * @param rowStartIdxAndCount
	 * @return @return 劳务派遣登记信息
	 */
	public PageObject getLaborBorrowRegisterInfo(String strSignatureDateFrom,
			  String strSignatureDateTo,String cooperateUnitId,
			  String dcmStatus,String strEnterpriseCode,String strTransferType,final int...rowStartIdxAndCount) {
		try{
			LogUtil.log("EJB:劳务派遣登记信息查询开始。", Level.INFO, null);
			
			PageObject result = new PageObject();
			// 参数List
        	List lstParams = new ArrayList();
        	// 查询sql
        	lstParams.add(IS_USE_Y);
        	lstParams.add(strEnterpriseCode);
        	lstParams.add(IS_USE_Y);
        	lstParams.add(strEnterpriseCode);
            lstParams.add(IS_USE_Y);
        	lstParams.add(strEnterpriseCode);
        	//sql
        	String strSql = "SELECT DISTINCT " 
        			      + " A.BORROWCONTRACTID, "
        		          + " A.WROK_CONTRACT_NO, "
        		          + " TO_CHAR(A.SIGNATURE_DATE,'YYYY-MM-DD'), "
        		          + " A.COOPERATE_UNIT_ID, "
        		          + " B.COOPERATE_UNIT,  "
        		          + " TO_CHAR(A.START_DATE,'YYYY-MM-DD'), "
        		          + " TO_CHAR(A.END_DATE,'YYYY-MM-DD'), "
        		          + " A.CONTRACT_CONTENT, "
        		          + " A.DCM_STATUS, "
        		          + " A.NOTE, "
        		          + " A.LAST_MODIFIED_BY, "
        		          + " TO_CHAR(A.LAST_MODIFIED_DATE,'YYYY-MM-DD HH24:MI:SS'), "
        		          + " C.WORKCONTRACTID ,"
        		          + " a.TRANSFER_TYPE "
        		          + "FROM HR_J_BORROWCONTRACT A " 
        		          +	"LEFT JOIN HR_J_COOPERATEUNIT B "
        		          + "ON A.COOPERATE_UNIT_ID = B.COOPERATE_UNIT_ID "
        		          + "AND B.IS_USE = ? AND B.ENTERPRISE_CODE = ? "
        		          + "LEFT JOIN HR_J_CARWH_INVOICE C "
        		          + "ON A.BORROWCONTRACTID = C.WORKCONTRACTID "
        		          + "AND C.IS_USE= ? AND C.ENTERPRISE_CODE = ? AND C.FILE_ORIGER = '4'";
            //sqlCount
        	//WHERE
        	String strSqlWhere =" WHERE A.IS_USE = ? AND A.ENTERPRISE_CODE = ? ";
        	//签字开始日期
        	if((strSignatureDateFrom != null) && !"".equals(strSignatureDateFrom)){
        		lstParams.add(strSignatureDateFrom);
        		strSqlWhere += " AND TO_CHAR(A.SIGNATURE_DATE,'YYYY-MM-DD') >= ?";
        	}
        	//签字结束日期
        	if((strSignatureDateTo != null) && (!"".equals(strSignatureDateTo))){
        		lstParams.add(strSignatureDateTo);
        		strSqlWhere += " AND TO_CHAR(A.SIGNATURE_DATE,'YYYY-MM-DD') <= ?";
        	}
        	//协作单位ID
        	if((cooperateUnitId != null) && (!"".equals(cooperateUnitId))&&(!"0".equals(cooperateUnitId))){
        		
        		strSqlWhere += " AND A.COOPERATE_UNIT_ID = "+cooperateUnitId;
        	}
        	//单据状态
        	if((dcmStatus != null) && (!"".equals(dcmStatus))){
        		
        		strSqlWhere += " AND A.DCM_STATUS =  "+ dcmStatus;
        	}
        	//调动类型
           if((strTransferType != null) && (!"".equals(strTransferType))){
        		
        		strSqlWhere += " AND A.TRANSFER_TYPE =  "+ strTransferType;
        	}
        	strSqlWhere +="  ORDER BY A.BORROWCONTRACTID ";
        	     	//拼接sql
        	strSql += strSqlWhere;
        	String strSqlCount = " SELECT  COUNT(1) "
		          + "FROM ("+  strSql+")"; 
        	LogUtil.log("EJB:SQL = " + strSql, Level.INFO, null);
        	LogUtil.log("EJB:SQL = " + strSqlCount, Level.INFO, null);
        	
        	//总共
        	Long totalCount = Long.parseLong(bll.getSingal(strSqlCount,
            		lstParams.toArray()).toString());
        	List  list = bll.queryByNativeSQL(strSql,lstParams.toArray(), rowStartIdxAndCount);
        	List<LaborBorrowRegisterQuery> arrList = new ArrayList<LaborBorrowRegisterQuery>();
        	Iterator it = list.iterator();
        	while(it.hasNext()){
        		Object[] data = (Object[])it.next(); 
        		LaborBorrowRegisterQuery info = new LaborBorrowRegisterQuery();
        		//劳务派遣合同ID
        		if(data[0]!=null){
        			info.setBorrowContractId(data[0].toString());
        		}else{
        			info.setBorrowContractId("");
        		}
        		//劳动合同编号
        		if(data[1]!=null){
        			info.setWrokContractNo(data[1].toString());
        		}else{
        			info.setWrokContractNo("");
        		}
        		//签字日期
        		if(data[2]!=null){
        			info.setSignatureDate(data[2].toString());
        		}else{
        			info.setSignatureDate("");
        		}
        		//协作单位ID
        		if(data[3]!=null){
        			info.setCooperateUnitId(data[3].toString());
        		}else{
        			info.setCooperateUnitId("");
        		}
        		//协作单位ID
        		if(data[4]!=null){
        			info.setCooperateUnit(data[4].toString());
        		}else{
        			info.setCooperateUnit("");
        		}
        		//开始日期
        		if(data[5]!=null){
        			info.setStartDate(data[5].toString());
        		}else{
        			info.setStartDate("");
        		}
        		//结束日期
        		if(data[6]!=null){
        			info.setEndDate(data[6].toString());
        		}else{
        			info.setEndDate("");
        		}
        		//劳务内容
        		if(data[7]!=null){
        			info.setContractContent(data[7].toString());
        		}else{
        			info.setContractContent("");
        		}
        		//单据状态
        		if(data[8]!=null){
        			if(data[8].equals("0")){
        				info.setDcmStatus("未上报");
        			}else if(data[8].equals("1")){
        				info.setDcmStatus("已上报");
        			}else if(data[8].equals("2")){
        				info.setDcmStatus("已终结");
        			}else if(data[8].equals("3")){
        				info.setDcmStatus("已退回");
        			}
        			
        		}else{
        			info.setDcmStatus("");
        		}
        		//备注
        		if(data[9]!=null){
        			info.setNote(data[9].toString());
        		}else{
        			info.setNote("");
        		}
        		//上次修改人
        		if(data[10]!=null){
        			info.setLastModifiedBy(data[10].toString());
        		}else{
        			info.setLastModifiedBy("");
        		}
        		//上次修改人
        		if(data[11]!=null){
        			info.setLastModifiedDate(data[11].toString());
        		}else{
        			info.setLastModifiedDate("");
        		}
        		info.setFlag(DEF_FLAG_UPDATE);
        		if(data[12]!=null){
        			info.setWorkContractId(data[12].toString());
        		}else{
        			info.setWorkContractId("");
        		}
        		if(data[13]!=null){
        			info.setTransferType(data[13].toString());
        		}
        		arrList.add(info);
        	}
        	result.setList(arrList);
        	result.setTotalCount(totalCount);
        	LogUtil.log("EJB:劳务派遣登记信息查询正常结束。", Level.INFO, null);
			return result;
		}catch(RuntimeException e){
			LogUtil.log("EJB:劳务派遣登记信息查询失败。", Level.SEVERE, e);
			throw e;
		}
		
	}
	/**
	 * 派遣人员信息
	 * @param strEnterpriseCode
	 * @return PageObject
	 * @param strBorrowContractId
	 */
	public PageObject getBorrowEmployeeInfo(String strBorrowContractId,
			String strEnterpriseCode, int... rowStartIdxAndCount) {
		// TODO Auto-generated method stub
		try{
			LogUtil.log("EJB:检索派遣人员信息开始。", Level.INFO, null);
			PageObject result = new PageObject();
			// 参数List
        	List lstParams = new ArrayList();
        	//企业code
        	lstParams.add(strEnterpriseCode);
        	lstParams.add(strEnterpriseCode);
        	lstParams.add(strEnterpriseCode);
        	lstParams.add(strEnterpriseCode);
        	//派遣合同id
        	
        	String strSql = "SELECT B.EMP_CODE, "
        		          + " B.CHS_NAME,"
        		          + " C.DEPT_NAME,"
        		          + " D.STATION_NAME,"
        		          + " A.EMPLOYEEBORROWID, "
        		          + " TO_CHAR(A.START_DATE,'YYYY-MM-DD'), "
        		          + " TO_CHAR(A.END_DATE,'YYYY-MM-DD'), " 
        		          + " TO_CHAR(A.STOP_PAY_DATE,'YYYY-MM-DD'), "
        		          + " TO_CHAR(A.START_PAY_DATE,'YYYY-MM-DD'), "
        		          + " A.IF_BACK, "
        		          + " A.MEMO, "
        		          + " A.EMP_ID, "
        		          + " C.DEPT_ID, "
        		          + " D.STATION_ID, "
        		          + " TO_CHAR(A.LAST_MODIFIED_DATE,'YYYY-MM-DD HH24:MI:SS') "
        		          + "FROM HR_J_EMPLOYEEBORROW A "
        		          + " LEFT JOIN HR_J_EMP_INFO B ON A.EMP_ID = B.EMP_ID AND B.ENTERPRISE_CODE = ? "
        		          + " LEFT JOIN HR_C_DEPT C ON A.DEPT_ID = C.DEPT_ID AND C.ENTERPRISE_CODE = ? "
        		          + " LEFT JOIN HR_C_STATION D ON A.STATION_ID = D.STATION_ID AND D.ENTERPRISE_CODE = ? "
        		          + "WHERE A.IS_USE = 'Y' AND A.ENTERPRISE_CODE = ? AND A.BORROWCONTRACTID = "+strBorrowContractId
        		          + " ORDER BY A.EMPLOYEEBORROWID ";
        	LogUtil.log("EJB:SQL = " + strSql, Level.INFO, null);
        	//总共
        	List  list = bll.queryByNativeSQL(strSql,lstParams.toArray(), rowStartIdxAndCount);
        	List<BorrowEmployeeInfo> arrList = new ArrayList<BorrowEmployeeInfo>();
        	Iterator it = list.iterator();
        	while(it.hasNext()){
        		Object[] data = (Object[])it.next(); 
        		BorrowEmployeeInfo info = new BorrowEmployeeInfo();
        		if(data[0] != null){
        			info.setEmpCode(data[0].toString());
        		}else{
        			info.setEmpCode("");
        		}
        		if(data[1] != null){
        			info.setChsName(data[1].toString());
        		}else{
        			info.setChsName("");
        		}
        		if(data[2] != null){
        			info.setDeptName(data[2].toString());
        		}else{
        			info.setDeptName("");
        		}
        		if(data[3] != null){
        			info.setStationName(data[3].toString());
        		}else{
        			info.setStationName("");
        		}
        		if(data[4] != null){
        			info.setEmployeeBorrowId(data[4].toString());
        		}else{
        			info.setEmployeeBorrowId("");
        		}
        		if(data[5] != null){
        			info.setStartDate(data[5].toString());
        		}else{
        			info.setStartDate("");
        		}
        		if(data[6] != null){
        			info.setEndDate(data[6].toString());
        		}else{
        			info.setEndDate("");
        		}
        		if(data[7] != null){
        			info.setStopPayDate(data[7].toString());
        		}else{
        			info.setStopPayDate("");
        		}
        		if(data[8] != null){
        			info.setStartPayDate(data[8].toString());
        		}else{
        			info.setStartPayDate("");
        		}
        		if(data[9] != null){
        			if(data[9].equals("1")){
        				info.setIfBack("是");	
        			}
        			else{
        				info.setIfBack("否");
        			}
        		}else{
        			info.setIfBack("");
        		}
        		if(data[10] != null){
        			info.setMemo(data[10].toString());
        		}else{
        			info.setMemo("");
        		}
        		if(data[11] != null){
        			info.setEmpId(data[11].toString());
        		}else{
        			info.setEmpId("");
        		}
        		if(data[12] != null){
        			info.setDeptId(data[12].toString());
        		}else{
        			info.setDeptId("");
        		}
        		if(data[13] != null){
        			info.setStationId(data[13].toString());
        		}else{
        			info.setStationId("");
        		}
        		if(data[14] != null){
        			info.setUpdateTime(data[14].toString());
        		}else{
        			info.setUpdateTime("");
        		}
        		info.setFlag(DEF_FLAG_UPDATE);
        		arrList.add(info);
        	}
        	result.setTotalCount((long)list.size());
        	result.setList(arrList);
        	LogUtil.log("EJB:检索派遣人员信息正常开始。", Level.INFO, null);
			return result;
		}catch(RuntimeException e){
			LogUtil.log("EJB:检索派遣人员信息失败。", Level.SEVERE, e);
			throw e;
		}
	}

}
