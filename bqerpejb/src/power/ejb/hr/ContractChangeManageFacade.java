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
 * 合同变更管理Facade
 * 
 * @author jincong
 * @version 1.0
 */
@Stateless
public class ContractChangeManageFacade implements
		ContractChangeManageFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/** 是否使用 */
	public final String IS_USE_Y = "Y";
	/** 劳动合同登记表.是否劳动合同正在执行: 1 */
	public final String IF_EXECUTE = "1";
	/** 字符串: 新签合同 */
	public final String STRING_CONTRACT_TYPE_NEW = "新签合同";
	/** 字符串: 续签合同 */
	public final String STRING_CONTRACT_TYPE_CONTINUE = "续签合同";
	/** 劳动合同形式: 新签合同 */
	public final String CONTRACT_TYPE_NEW = "0";
	/** 劳动合同形式: 续签合同 */
	public final String CONTRACT_TYPE_CONTINUE = "1";

	/**
	 * 根据人员Id获得变更合同的信息
	 * 
	 * @param empId
	 *            人员Id
	 * @param workContractId
	 * 			  劳动合同签订ID
	 * @param enterpriseCode
	 *            企业编码
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getContractChangeInfo(String empId, String workContractId,
			String enterpriseCode) {
		LogUtil.log("EJB:根据人员Id获得变更合同的信息开始。", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// sql
			String sql =
				"SELECT " +
					"A.CONTRACTCHANGEID, " +
					"A.NEW_CONTRACT_CODE, " +
					"TO_CHAR(A.CHARGE_DATE, 'YYYY-MM-DD'), " +
					"TO_CHAR(A.NEW_STATE_TIME, 'YYYY-MM-DD'), " +
					"TO_CHAR(A.NEW_END_TIME, 'YYYY-MM-DD'), " +
					"A.CHANGE_REASON, " +
					"A.MEMO, " +
					"TO_CHAR(A.LAST_MODIFIED_DATE, 'YYYY-MM-DD hh24:mi:ss'), " +
					"C.DEPT_ID, " +
					"C.DEPT_NAME, " +
					"D.STATION_ID, " +
					"D.STATION_NAME " +
				"FROM HR_J_CONTRACTCHANGE A " +
					"LEFT JOIN HR_C_DEPT C " +
					"ON A.NEW_DEP_CODE = C.DEPT_ID AND C.ENTERPRISE_CODE = ? " +
					"LEFT JOIN HR_C_STATION D " +
					// modify by liuyi 090911 该表中无此属性
//					"ON A.NEW_STATION_CODE = D.STATION_ID AND D.ENTERPRISE_CODE = ? " +
					"ON A.NEW_STATION_CODE = D.STATION_ID  " +
				"WHERE " +
					"A.WORKCONTRACTID = ? AND " +
					"A.CONTRACTCHANGEID IN (" +
					"SELECT MAX(E.CONTRACTCHANGEID) " +
					"FROM HR_J_CONTRACTCHANGE E " +
					"WHERE E.EMP_ID2 = ? AND " +
					"E.IS_USE = ? " +
					"AND E.ENTERPRISE_CODE = ?)";
			List list = bll.queryByNativeSQL(sql,
        			new Object[] {enterpriseCode, workContractId,
					empId, IS_USE_Y, enterpriseCode});
        	Iterator it = list.iterator();
        	List<ContractChangeManage> arrlist = new ArrayList<ContractChangeManage>();
            while (it.hasNext()) {
            	ContractChangeManage info = new ContractChangeManage();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setContractChangeId(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setNewContractTermId(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setChangeDate(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setNewStartDate(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setNewEndDate(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setChangeReason(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setMemo(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setLastModifiedDateChange(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setNewDeptId(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setNewDeptName(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setNewStationId(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setNewStationName((data[11].toString()));
            	}
            	arrlist.add(info);
            }
            object.setList(arrlist);
            object.setTotalCount((long)arrlist.size());
			LogUtil.log("EJB:根据人员Id获得变更合同的信息结束。", Level.INFO, null);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:根据人员Id获得变更合同的信息失败。", Level.SEVERE, e);
			throw e;
		}
	}
	
	/**
	 * 根据人员Id获得登记合同的信息
	 * 
	 * @param empId
	 *            人员Id
	 * @param enterpriseCode
	 *            企业编码
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getContractInfoForChange(String empId,
			String enterpriseCode) {
		LogUtil.log("EJB:根据人员Id获得登记合同的信息开始。", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// sql
			String sql =
				"SELECT " +
					"A.WORKCONTRACTID, " +
					"A.WROK_CONTRACT_NO, " +
					"A.CONTRACT_TERM_ID, " +
					"A.FRIST_ADDREST, " +
					"A.CONTRACT_CONTINUE_MARK, " +
					"TO_CHAR(A.WORK_SIGN_DATE, 'YYYY-MM-DD'), " +
					"TO_CHAR(A.START_DATE, 'YYYY-MM-DD'), " +
					"TO_CHAR(A.END_DATE, 'YYYY-MM-DD'), " +
					"TO_CHAR(A.LAST_MODIFIED_DATE, 'YYYY-MM-DD hh24:mi:ss'), " +
					"B.DEPT_ID DEPT_ID1, " +
					"B.DEPT_NAME DEPT_NAME1, " +
					"C.DEPT_ID DEPT_ID2, " +
					"C.DEPT_NAME DEPT_NAME2, " +
					"D.STATION_ID, " +
					"D.STATION_NAME " +
				"FROM " +
					"HR_J_WORKCONTRACT A LEFT JOIN HR_C_DEPT B " +
					"ON A.FRIST_DEP_ID = B.DEPT_ID AND " +
					"B.ENTERPRISE_CODE = ? " +
					"LEFT JOIN HR_C_DEPT C " +
					"ON A.DEPT_ID = C.DEPT_ID AND " +
					"C.ENTERPRISE_CODE = ? " +
					"LEFT JOIN HR_C_STATION D " +
					// modify by liuyi 090911 该表中无此属性
//					"ON A.STATION_ID = D.STATION_ID AND " +
//					"D.ENTERPRISE_CODE = ? " +
					"ON A.STATION_ID = D.STATION_ID " +
				"WHERE " +
					"A.EMP_ID = ? AND " +
					"A.IF_EXECUTE = ? AND " +
					"A.IS_USE = ? AND " +
					"A.ENTERPRISE_CODE = ?";
			List list = bll.queryByNativeSQL(sql,
        			new Object[] {enterpriseCode, enterpriseCode,
					 empId, IF_EXECUTE, IS_USE_Y,
					enterpriseCode});
        	Iterator it = list.iterator();
        	List<ContractChangeManage> arrlist = new ArrayList<ContractChangeManage>();
            while (it.hasNext()) {
            	ContractChangeManage info = new ContractChangeManage();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setWorkContractId(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setWorkContractNo(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setContractTermId(data[2].toString());
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
            		info.setLastModifiedDate(data[8].toString());
            	}
            	
            	if(null != data[9]) {
            		info.setFirstDeptId(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setDeptNameFirst(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setDeptId(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setDeptName(data[12].toString());
            	}
            	if(null != data[13]) {
            		info.setStationId(data[13].toString());
            	}
            	if(null != data[14]) {
            		info.setStationName(data[14].toString());
            	}
            	arrlist.add(info);
            }
            object.setList(arrlist);
            object.setTotalCount((long)arrlist.size());
			LogUtil.log("EJB:根据人员Id获得登记合同的信息结束。", Level.INFO, null);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:根据人员Id获得登记合同的信息失败。", Level.SEVERE, e);
			throw e;
		}
	}
	
	/**
	 * 查找合同有效期数据
	 * 
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject findContractTerm(String enterpriseCode,
			final int... rowStartIdxAndCount) {
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
	
	/**
	 * 根据人员Id取得部门和岗位
	 * 
	 * @param empId 人员Id
	 * @param enterpriseCode 企业编码
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getBaseInfoForChange(String empId, String enterpriseCode) {
		LogUtil.log("EJB:根据人员Id取得部门和岗位开始。", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// sql
			String sql =
				"SELECT " +
					"B.DEPT_ID, " +
					"B.DEPT_NAME, " +
					"C.STATION_ID " +
				"FROM " +
					"HR_J_EMP_INFO A, " +
					"HR_C_DEPT B, " +
					"HR_C_STATION C " +
				"WHERE " +
					"A.EMP_ID = ? AND " +
					"A.DEPT_ID = B.DEPT_ID(+) AND " +
					"A.STATION_ID = C.STATION_ID(+) AND " +
					//modify by liuyi 090911 该表中无此属性
//					"A.IS_USE = ? AND " +
					"A.ENTERPRISE_CODE = ? AND " +
					"B.IS_USE = 'Y' AND " +//update by sychen 20100901
//					"B.IS_USE = 'U' AND " +
					//modify by liuyi 090911 该表中无此属性
//					"B.ENTERPRISE_CODE = ? AND " +
//					"C.ENTERPRISE_CODE = ?";
					"B.ENTERPRISE_CODE = ? " ;
					
			List list = bll.queryByNativeSQL(sql,
        			new Object[] {empId,  enterpriseCode,
					 enterpriseCode});
        	Iterator it = list.iterator();
        	List<ContractChangeManage> arrlist = new ArrayList<ContractChangeManage>();
            while (it.hasNext()) {
            	ContractChangeManage info = new ContractChangeManage();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setNewDeptId(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setNewDeptName(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setNewStationId(data[2].toString());
            	}
            	arrlist.add(info);
            }
            object.setList(arrlist);
            object.setTotalCount((long)arrlist.size());
			LogUtil.log("EJB:根据人员Id取得部门和岗位结束。", Level.INFO, null);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:根据人员Id取得部门和岗位失败。", Level.SEVERE, e);
			throw e;
		}
	}
}
