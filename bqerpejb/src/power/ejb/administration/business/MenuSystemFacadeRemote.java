/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 菜谱信息Remote 接口
 *
 * @author zhaomingjian
 * @version 1.0
 */
@Remote
public interface MenuSystemFacadeRemote {
	 /**
     * 订餐信息查询
     * @param strDate 查询时间	
     * @param strManType 人员类别
     * @param strMenuType 用餐类别
     * @throws SQLException
     */
    public PageObject getMenuSystemInfo(String strDate,String strManType,String strWorkType,String strEnterpriseCode) throws SQLException ;
    /**
     * 订餐信息张票查询
     * @param strDate 查询时间	
     * @param strManType 人员类别
     * @param strMenuType 用餐类别
     * @throws SQLException
     */
    public PageObject getMenuSystemInfoZp(String enterpriseCode,String strDate,String strManType,String strWorkType) throws SQLException ;
    /**
     * 订餐信息统计查询
     * @param strDate 查询时间	
     * @param strManType 人员类别
     * @param strMenuType 用餐类别
     * @throws SQLException
     */
    public PageObject getMenuSystemSubInfo(String strDate,String strManType,String strWorkType,String strEnterpriseCode) throws SQLException;
}
