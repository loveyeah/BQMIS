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
import power.ejb.administration.form.AdJOutQuestFileInfo;
import power.ejb.administration.form.OutQuestQueryInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 签报申请查询Facade
 * 
 * @author jincong
 * @version 1.0
 */
@Stateless
public class OutQuestQueryFacade implements OutQuestQueryFacadeRemote {

	@EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	
	/**
	 * 查询签报申请信息
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param deptCode
	 *            部门编码
	 * @param workerCode
	 *            人员编码
	 * @param reportStatus
	 *            单据状态
	 * @param enterpriseCode
	 * 			  企业代码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 签报申请信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findOutQueryApply(String startDate, String endDate,
			String deptCode, String workerCode, String reportStatus,
			String enterpriseCode, final int... rowStartIdxAndCount) throws SQLException {
		// Log开始
		LogUtil.log("EJB:查询签报申请信息开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	// 查询sql
        	String sql = "SELECT "
	        	+ "A.APPLY_ID, "
	        	+ "A.APPLY_MAN, "
	        	+ "TO_CHAR(A.APPLY_DATE, 'YYYY-MM-DD'), "
	        	+ "A.APPLY_TOPIC, "
	        	+ "A.APPLY_TEXT, "
	        	+ "A.CHECKED_MAN, "
	        	+ "decode(A.DCM_STATUS, '0', '未上报', '1', '已上报', '2', '已终结', '3', '已退回'), "
	        	+ "decode(A.APP_TYPE, 'D', '董事会签报', 'I', '内部签报'), "
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
	        	+ "C.CHS_NAME ";
	        String sqlWhere = "FROM "
	        	+ "AD_J_OUT_QUEST A LEFT JOIN HR_J_EMP_INFO C "
	        	+ "ON A.APPLY_MAN = C.EMP_CODE "
	        	+ "AND C.ENTERPRISE_CODE = ? "
	        	+ "LEFT JOIN HR_C_DEPT B "
	        	+ "ON C.DEPT_ID = B.DEPT_ID "
	        	+ "AND B.ENTERPRISE_CODE = ? "
	        + "WHERE "
	        	+ "A.IS_USE = ? AND "
	        	+ "A.ENTERPRISE_CODE = ? ";
        	if(startDate != null && !("".equals(startDate))) {
        		sqlWhere += "AND TO_CHAR(A.APPLY_DATE, 'YYYY/MM/DD') >= ? ";
        		listParams.add(startDate);
        	}
        	if(endDate != null && !("".equals(endDate))) {
        		sqlWhere += "AND TO_CHAR(A.APPLY_DATE, 'YYYY/MM/DD') <= ? ";
        		listParams.add(endDate);
        	}
        	if(deptCode != null && !("".equals(deptCode))) {
        		sqlWhere += "AND B.DEPT_CODE = ? ";
        		listParams.add(deptCode);
        	}
        	if(workerCode != null && !("".equals(workerCode))) {
        		sqlWhere += "AND A.APPLY_MAN = ? ";
        		listParams.add(workerCode);
        	}
        	if(reportStatus != null && !("".equals(reportStatus))) {
        		sqlWhere += "AND A.DCM_STATUS = ? ";
        		listParams.add(reportStatus);
        	}
        	sql += sqlWhere;
        	String sqlCount = "SELECT COUNT(1) " + sqlWhere;
        	// 查询附件条数sql
        	String sqlFile = "SELECT "
        		+ "COUNT(A.ID) "
        	+ "FROM "
        		+ "AD_J_OUT_QUEST_FILE A "
        	+ "WHERE "
	        	+ "A.APPLY_ID = ? AND "
	        	+ "A.IS_USE = ?";
        	
        	LogUtil.log("SQL=" + sql, Level.INFO, null);
        	// 生成查询参数
        	Object[] params = listParams.toArray();
        	Object[] paramsNew = new Object[params.length + 4];
        	// 是否使用
        	paramsNew[2] = "Y";
        	// 企业代码
        	paramsNew[0] = enterpriseCode;
        	paramsNew[1] = enterpriseCode;
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
            		info.setApplyId(data[0].toString());
            		// 查询附件Log开始
                	LogUtil.log("SQL=" + sqlFile, Level.INFO, null);
            		// 查询附件条数
            		Long fileCount = Long.parseLong(bll.getSingal(sqlFile,
                    		new Object[]{data[0].toString(), "Y"}).toString());
            		info.setFileCount(fileCount);
            	}
            	if(null != data[1]) {
            		info.setApplyMan(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setApplyDate(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setApplyTopic(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setApplyText(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setCheckedMan(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setDcmStatus(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setAppType(data[7].toString());
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
	 * 查询签报申请附件信息
	 * 
	 * @param applyId
	 *            申请单号
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 签报申请附件信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findOutQueryFile(String applyId,
			final int... rowStartIdxAndCount) throws SQLException {
		// Log开始
    	LogUtil.log("EJB:查询签报申请附件信息开始。", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// 查询sql
        	String sql = "SELECT " +
        		"A.ID, " +
        		"A.FILE_NAME " +
        		"FROM " +
        		"AD_J_OUT_QUEST_FILE A " +
        		"WHERE " +
        		"A.APPLY_ID = ? AND " +
        		"A.IS_USE = ?";
        	String sqlCount = "SELECT COUNT(A.ID) " +
	        	"FROM " +
	    		"AD_J_OUT_QUEST_FILE A " +
	    		"WHERE " +
	    		"A.APPLY_ID = ? AND " +
	    		"A.IS_USE = ?";
			
        	LogUtil.log("SQL=" + sql, Level.INFO, null);
        	// 查询
        	List list = bll.queryByNativeSQL(sql, new Object[]{applyId, "Y"},
        		rowStartIdxAndCount);
        	List<OutQuestQueryInfo> arrlist = new ArrayList<OutQuestQueryInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) {
            	OutQuestQueryInfo info = new OutQuestQueryInfo();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setId(Long.parseLong(data[0].toString()));
            	}
            	if(null != data[1]) {
            		info.setFileName(data[1].toString());
            	}
            	arrlist.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		new Object[]{applyId, "Y"}).toString());
            object.setList(arrlist);
            object.setTotalCount(totalCount);
			// Log结束
    		LogUtil.log("EJB:查询签报申请附件信息结束。", Level.INFO, null);
        	return object;
		} catch (RuntimeException e) {
        	LogUtil.log("EJB:查询签报申请附件信息失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}
	
	/**
	 * 根据ID查询签报申请附件信息
	 * 
	 * @param id
	 * @return 签报申请附件信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findOutQueryFileById(String id) throws SQLException {
		// Log开始
    	LogUtil.log("EJB:查询签报申请附件信息开始。", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// 查询sql
        	String sql = "SELECT " +
	        	"A.ID, " +
	        	"A.APPLY_ID, " +
	    		"A.FILE_TYPE, " +
	    		"A.FILE_KIND, " +
	    		"A.FILE_NAME, " +
	    		"A.FILE_TEXT, " +
	    		"A.IS_USE, " +
	    		"A.UPDATE_USER, " +
	    		"A.UPDATE_TIME " +
        		"FROM " +
        		"AD_J_OUT_QUEST_FILE A " +
        		"WHERE " +
        		"A.ID = ? AND " +
        		"A.IS_USE = ?";	
        	String sqlCount = "SELECT COUNT(A.ID) " +
	        	"FROM " +
	    		"AD_J_OUT_QUEST_FILE A " +
	    		"WHERE " +
	    		"A.APPLY_ID = ? AND " +
	    		"A.IS_USE = ?";
        	
        	LogUtil.log("SQL=" + sql, Level.INFO, null);
        	// 查询
        	List list = bll.queryByNativeSQL(sql, new Object[]{id, "Y"}, AdJOutQuestFileInfo.class);
        	Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		new Object[]{id, "Y"}).toString());
            if(list == null) {
            	object.setList(new ArrayList());
            	object.setTotalCount((long)0);
            } else {
            	object.setList(list);
            	object.setTotalCount(totalCount);
            }
			// Log结束
    		LogUtil.log("EJB:查询签报申请附件信息结束。", Level.INFO, null);
        	return object;
		} catch (RuntimeException e) {
        	LogUtil.log("EJB:查询签报申请附件信息失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}
}
