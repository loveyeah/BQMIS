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
import power.ejb.administration.AdJEmployeesignpicture;
import power.ejb.administration.form.SignPictureInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 个性签名维护Facade
 * 
 * @author jincong
 * @version 1.0
 */
@Stateless
public class SignPictureFacade implements SignPictureFacadeRemote {

	@EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	
	/**
	 * 查找人员签名信息
	 * 
	 * @param enterpriseCode
	 *            企业代码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 查找人员签名信息
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findPersonSignInfo(String queryName,String enterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException {
		// Log开始
		LogUtil.log("EJB:查询人员签名信息开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 查询sql
        	String sql = "SELECT " +
        		"A.ID, " +
        		"A.WORKER_CODE, " +
        		"B.CHS_NAME " +
        	"FROM " +
        		"AD_J_EMPLOYEESIGNPICTURE A LEFT JOIN HR_J_EMP_INFO B " +
        		"ON A.WORKER_CODE = B.EMP_CODE AND " +
        		"B.ENTERPRISE_CODE = ? " +
        	"WHERE " +
        		"A.IS_USE = ? AND " +
        		"A.ENTERPRISE_CODE = ? ";
        	String sqlCount = "SELECT COUNT(1) " +
	        "FROM " +
		        "AD_J_EMPLOYEESIGNPICTURE A LEFT JOIN HR_J_EMP_INFO B " +
	    		"ON A.WORKER_CODE = B.EMP_CODE AND " +
	    		"B.ENTERPRISE_CODE = ? " +
	        "WHERE " +
	        	"A.IS_USE = ? AND " +
	        	"A.ENTERPRISE_CODE = ? ";
        	
        	if(queryName !=null && !"".equals(queryName)) {
        		sql+=" and getworkername(a.worker_code) like '%"+queryName+"%' ";
        		sqlCount+=" and getworkername(a.worker_code) like '%"+queryName+"%' ";
        	}
        	
        	LogUtil.log("SQL=" + sql, Level.INFO, null);
        	// 查询
        	List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, "Y", enterpriseCode}, rowStartIdxAndCount);
        	List<SignPictureInfo> arrlist = new ArrayList<SignPictureInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) {
            	SignPictureInfo info = new SignPictureInfo();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setId(Long.parseLong(data[0].toString()));
            	}
            	if(null != data[1]) {
            		info.setWorkerCode(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setWorkerName(data[2].toString());
            	}
            	arrlist.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		new Object[]{enterpriseCode, "Y", enterpriseCode}).toString());
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            // Log结束
    		LogUtil.log("EJB:查询人员签名信息结束。", Level.INFO, null);
        	return object;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:查询人员签名信息失败。", Level.SEVERE, e);
            throw new SQLException();
        }
	}
	
	/**
	 * 根据id查找图片信息
	 * 
	 * @param id
	 * @return 图片信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public AdJEmployeesignpicture findPictrueById(String id) throws SQLException {
		// Log开始
		LogUtil.log("EJB:查询图片信息开始。", Level.INFO, null);
        try {
        	String sql = "SELECT " +
	    		"A.ID, " +
	    		"A.WORKER_CODE, " +
	    		"A.SIGN_PIC, " +
	    		"A.UPDATE_USER, " +
	    		"A.UPDATE_TIME, " +
	    		"A.IS_USE, " +
	    		"A.ENTERPRISE_CODE " +
	    	"FROM " +
	    		"AD_J_EMPLOYEESIGNPICTURE A " +
	    	"WHERE " +
	    		"A.ID = ? AND " +
	    		"A.IS_USE = ?";
        	
        	LogUtil.log("SQL=" + sql, Level.INFO, null);
        	List list = bll.queryByNativeSQL(sql, new Object[]{id, "Y"}, AdJEmployeesignpicture.class);
        	AdJEmployeesignpicture picture = new AdJEmployeesignpicture();
        	if(list != null && list.size() > 0) {
        		picture = (AdJEmployeesignpicture)list.get(0);
        	}
        	// Log结束
    		LogUtil.log("EJB:查询图片信息结束。", Level.INFO, null);
        	return picture;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:查询图片信息失败。", Level.SEVERE, e);
            throw new SQLException();
        }
	}
	
	/**
	 * 根据人员编码查找图片信息
	 * 
	 * @param workerCode 人员编码
	 * @param enterpriseCode 企业编码
	 * @return 图片信息
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findPictrueByCode(String workerCode, String enterpriseCode) throws SQLException {
		// Log开始
		LogUtil.log("EJB:查询图片信息开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	String sql = "SELECT " +
	    		"A.ID, " +
	    		"A.WORKER_CODE, " +
	    		"A.SIGN_PIC, " +
	    		"A.UPDATE_USER, " +
	    		"A.UPDATE_TIME, " +
	    		"A.IS_USE, " +
	    		"A.ENTERPRISE_CODE " +
	    	"FROM " +
	    		"AD_J_EMPLOYEESIGNPICTURE A " +
	    	"WHERE " +
	    		"A.WORKER_CODE = ? AND " +
	    		"A.IS_USE = ? AND " +
	    		"A.ENTERPRISE_CODE = ?";
        	String sqlCount = "SELECT COUNT(A.ID) " +
	        "FROM " +
	    		"AD_J_EMPLOYEESIGNPICTURE A " +
	    	"WHERE " +
	    		"A.WORKER_CODE = ? AND " +
	    		"A.IS_USE = ? AND " +
	    		"A.ENTERPRISE_CODE = ?";
        	
        	LogUtil.log("SQL=" + sql, Level.INFO, null);
        	List list = bll.queryByNativeSQL(sql, new Object[]{workerCode, "Y", enterpriseCode}, AdJEmployeesignpicture.class);
        	Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
        			new Object[]{workerCode, "Y", enterpriseCode}).toString());
        	if(list != null && list.size() > 0) {
        		object.setList(list);
        		object.setTotalCount(totalCount);
        	} else {
        		object.setList(new ArrayList());
        		object.setTotalCount((long)0);
        	}
        	// Log结束
    		LogUtil.log("EJB:查询图片信息结束。", Level.INFO, null);
        	return object;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:查询图片信息失败。", Level.SEVERE, e);
            throw new SQLException();
        }
	}
}
