/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.OutQuestQueryInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.LogUtil;

/**
 * 签报信息查阅Facade
 * 
 * @author jincong
 * @version 1.0
 */
@Stateless
public class OutInfoReadFacade implements OutInfoReadFacadeRemote {

	@EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	
	/**
	 * 查找签报信息
	 * 
	 * @param status
	 *            阅读状态
	 * @param applyId
	 *            申请单号
	 * @param readMan
	 *            抄送人员
	 * @param enterpriseCode
	 * 			  企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 签报信息
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findOutInfo(String status, String applyId,
			String readMan, String enterpriseCode, final int... rowStartIdxAndCount)
			throws SQLException {
		// Log开始
		LogUtil.log("EJB:查询签报申请信息开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	// 查询sql
        	String sql = "SELECT "
        		+ "A.ID, "
	        	+ "A.APPLY_ID, "
	        	+ "A.APPLY_MAN, "
	        	+ "TO_CHAR(A.APPLY_DATE, 'YYYY-MM-DD'), "
	        	+ "A.APPLY_TOPIC, "
	        	+ "A.APPLY_TEXT, "
	        	+ "A.CHECKED_MAN, "
	        	+ "decode(A.DCM_STATUS, '0', '未上报', '1', '已上报', '2', '已终结', '3', '已退回'), "
	        	+ "A.REPORT_ID, "
	        	+ "A.DEP_BOSS_CODE, "
	        	+ "A.DEP_IDEA, "
	        	+ "TO_CHAR(A.DEP_SIGN_DATE, 'YYYY-MM-DD HH24:mi'), "
	        	+ "A.XZ_BOSS_CODE, "
	        	+ "A.XZ_BOSS_IDEA, "
	        	+ "TO_CHAR(A.XZ_SIGN_DATE, 'YYYY-MM-DD HH24:mi'), "
	        	+ "A.BIG_BOSS_CODE, "
	        	+ "A.BIG_BOSS_IDEA, "
	        	+ "TO_CHAR(A.BIG_BOSS_SIGN_DATE, 'YYYY-MM-DD HH24:mi'), "
	        	+ "B.DEPT_NAME, "
	        	+ "C.CHS_NAME, "
	        	+ "TO_CHAR(A.UPDATE_TIME, 'YYYY-MM-DD HH24:mi:ss') ";
        	String sqlWhere = "FROM "
	        	+ "AD_J_OUT_QUEST A LEFT JOIN HR_J_EMP_INFO C "
	        	+ "ON A.APPLY_MAN = C.EMP_CODE "
	        	+ "AND C.ENTERPRISE_CODE = ? "
	        	+ "LEFT JOIN HR_C_DEPT B "
	        	+ "ON C.DEPT_ID = B.DEPT_ID "
	        	+ "AND B.ENTERPRISE_CODE = ?, "
	        	+ "AD_J_OUT_QUEST_READER D "
	        + "WHERE "
		        + "A.IS_USE = ? AND "
		        + "A.ENTERPRISE_CODE = ? AND "
		        + "D.READ_MAN = ? AND "
		        + "D.APPLY_ID = A.APPLY_ID AND "
		        + "D.IS_USE = ? ";
        	if(status != null && !("".equals(status))) {
        		sqlWhere += "AND A.IF_READ = ? ";
        		listParams.add(status);
        	}
        	if(applyId != null && !("".equals(applyId))) {
        		sqlWhere += "AND A.APPLY_ID = ? ";
        		listParams.add(applyId);
        	}
        	sql += sqlWhere;
        	String sqlCount = "SELECT COUNT(1) " + sqlWhere;
        	
        	LogUtil.log("SQL=" + sql, Level.INFO, null);
        	// 生成查询参数
        	Object[] params = listParams.toArray();
        	Object[] paramsNew = new Object[params.length + 6];
        	// 是否使用
        	paramsNew[2] = "Y";
        	paramsNew[0] = enterpriseCode;
        	paramsNew[1] = enterpriseCode;
        	paramsNew[3] = enterpriseCode;
        	paramsNew[4] = readMan;
        	paramsNew[5] = "Y";
        	for(int i = 0; i < params.length; i++) {
        		paramsNew[6 + i] = params[i];
        	}
        	// 查询
        	List list = bll.queryByNativeSQL(sql, paramsNew, rowStartIdxAndCount);
        	List<OutQuestQueryInfo> arrlist = new ArrayList<OutQuestQueryInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) {
            	OutQuestQueryInfo info = new OutQuestQueryInfo();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setId(Long.parseLong(data[0].toString()));
            	}
            	if(null != data[1]) {
            		info.setApplyId(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setApplyMan(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setApplyDate(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setApplyTopic(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setApplyText(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setCheckedMan(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setDcmStatus(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setReportId(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setDepBossCode(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setDepIdea(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setDepSignDate(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setXzBossCode(data[12].toString());
            	}
            	if(null != data[13]) {
            		info.setXzIdea(data[13].toString());
            	}
            	if(null != data[14]) {
            		info.setXzSignDate(data[14].toString());
            	}
            	if(null != data[15]) {
            		info.setBigBossCode(data[15].toString());
            	}
            	if(null != data[16]) {
            		info.setBigIdea(data[16].toString());
            	}
            	if(null != data[17]) {
            		info.setBigSignDate(data[17].toString());
            	}
            	if(null != data[18]) {
            		info.setDepName(data[18].toString());
            	}
            	if(null != data[19]) {
            		info.setName(data[19].toString());
            	}
            	if(null != data[20]) {
            		info.setUpdateTime(data[20].toString());
            	}
            	arrlist.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		paramsNew).toString());
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            // Log结束
    		LogUtil.log("EJB:查询签报申请信息结束。", Level.INFO, null);
        	return object;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:查询签报申请信息失败。", Level.SEVERE, e);
            throw new SQLException();
        }
	}
	/**
	 * 查找签报信息	 
	 * @param applyId
	 *            申请单号	
	 * @param enterpriseCode
	 * 			  企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 签报信息
	 * @throws SQLException
	 * @author daichunlin
	 */
	@SuppressWarnings("unchecked")
	public PageObject findOutInfo(String applyId,String enterpriseCode, final int... rowStartIdxAndCount)
			throws SQLException {
		// Log开始
		LogUtil.log("EJB:查询签报申请信息开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	// 查询sql
        	String sql = "SELECT "
        		+ "A.ID, "
	        	+ "A.APPLY_ID, "
	        	+ "A.APPLY_MAN, "
	        	+ "TO_CHAR(A.APPLY_DATE, 'YYYY-MM-DD'), "
	        	+ "A.APPLY_TOPIC, "
	        	+ "A.APPLY_TEXT, "
	        	+ "A.CHECKED_MAN, "
	        	+ "decode(A.DCM_STATUS, '0', '未上报', '1', '已上报', '2', '已终结', '3', '已退回'), "
	        	+ "A.REPORT_ID, "
	        	+ "A.DEP_BOSS_CODE, "
	        	+ "A.DEP_IDEA, "
	        	+ "TO_CHAR(A.DEP_SIGN_DATE, 'YYYY-MM-DD'), "
	        	+ "A.XZ_BOSS_CODE, "
	        	+ "A.XZ_BOSS_IDEA, "
	        	+ "TO_CHAR(A.XZ_SIGN_DATE, 'YYYY-MM-DD'), "
	        	+ "A.BIG_BOSS_CODE, "
	        	+ "A.BIG_BOSS_IDEA, "
	        	+ "TO_CHAR(A.BIG_BOSS_SIGN_DATE, 'YYYY-MM-DD'), "
	        	+ "B.DEPT_NAME, "
	        	+ "C.CHS_NAME, "
	        	+ "TO_CHAR(A.UPDATE_TIME, 'YYYY-MM-DD HH24:mi:ss') ";
        	String sqlWhere = "FROM "
	        	+ "AD_J_OUT_QUEST A LEFT JOIN HR_J_EMP_INFO C "
	        	+ "ON A.APPLY_MAN = C.EMP_CODE AND "
	        	+ " C.ENTERPRISE_CODE = ? "
	        	+ "LEFT JOIN HR_C_DEPT B "
	        	+ "ON C.DEPT_ID = B.DEPT_ID AND "
	        	+ " C.ENTERPRISE_CODE = ? "	        	
	            + "WHERE "
		        + "A.IS_USE = ? AND "
		        + "A.ENTERPRISE_CODE = ? ";	
        	if(applyId != null && !("".equals(applyId))) {
        		sqlWhere += "AND A.APPLY_ID = ? ";
        		listParams.add(applyId);
        	}
        	sql += sqlWhere;
        	String sqlCount = "SELECT COUNT(1) " + sqlWhere;
        	
        	LogUtil.log("SQL=" + sql, Level.INFO, null);
        	// 生成查询参数
        	Object[] params = listParams.toArray();
        	Object[] paramsNew = new Object[params.length + 4];
        	// 是否使用
        	
        	paramsNew[0] = enterpriseCode;
        	paramsNew[1] = enterpriseCode;
        	paramsNew[2] = "Y";
        	paramsNew[3] = enterpriseCode;
        	for(int i = 0; i < params.length; i++) {
        		paramsNew[4 + i] = params[i];
        	}
        	// 查询
        	List list = bll.queryByNativeSQL(sql, paramsNew, rowStartIdxAndCount);
        	List<OutQuestQueryInfo> arrlist = new ArrayList<OutQuestQueryInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) {
            	OutQuestQueryInfo info = new OutQuestQueryInfo();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setId(Long.parseLong(data[0].toString()));
            	}
            	if(null != data[1]) {
            		info.setApplyId(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setApplyMan(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setApplyDate(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setApplyTopic(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setApplyText(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setCheckedMan(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setDcmStatus(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setReportId(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setDepBossCode(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setDepIdea(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setDepSignDate(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setXzBossCode(data[12].toString());
            	}
            	if(null != data[13]) {
            		info.setXzIdea(data[13].toString());
            	}
            	if(null != data[14]) {
            		info.setXzSignDate(data[14].toString());
            	}
            	if(null != data[15]) {
            		info.setBigBossCode(data[15].toString());
            	}
            	if(null != data[16]) {
            		info.setBigIdea(data[16].toString());
            	}
            	if(null != data[17]) {
            		info.setBigSignDate(data[17].toString());
            	}
            	if(null != data[18]) {
            		info.setDepName(data[18].toString());
            	}
            	if(null != data[19]) {
            		info.setName(data[19].toString());
            	}
            	if(null != data[20]) {
            		info.setUpdateTime(data[20].toString());
            	}
            	arrlist.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		paramsNew).toString());
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            // Log结束
    		LogUtil.log("EJB:查询签报申请信息结束。", Level.INFO, null);
        	return object;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:查询签报申请信息失败。", Level.SEVERE, e);
            throw new SQLException();
        }
	}
	
	/**
	 * 根据人员编码查找人员信息
	 * 
	 * @param workerCode 人员编码
	 * @param enterpriseCode 企业编码
	 * @return 人员信息
	 * @throws SQLException
	 */
	public HrJEmpInfo findNameByCode(String workerCode, String enterpriseCode)throws SQLException {
		// Log开始
		LogUtil.log("EJB:查询人员信息开始。", Level.INFO, null);
        try {
        	HrJEmpInfo info = new HrJEmpInfo();
        	String sql = "SELECT " +
	    		"A.CHS_NAME " +
	    	"FROM " +
	    		"HR_J_EMP_INFO A " +
	    	"WHERE " +
	    		"A.EMP_CODE = ? AND " +
        		"A.ENTERPRISE_CODE = ? ";
        	
        	LogUtil.log("SQL=" + sql, Level.INFO, null);
        	String strName = "";
        	Object obj = null;
        	obj = bll.getSingal(sql, new Object[]{workerCode, enterpriseCode});
        	if (obj != null) {
				strName = (obj.toString());
			}
        	info.setChsName(strName);
        	
        	// Log结束
    		LogUtil.log("EJB:查询人员信息结束。", Level.INFO, null);
        	return info;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:查询人员信息失败。", Level.SEVERE, e);
            throw new SQLException();
        }
	}
}
