/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
/**
 * 接待费用统计查询接口.
 * 
 * @author zhouxu
 */
@Remote
public interface ReceptionCostStatisticsFacadeRemote {

    /**
     * 根据开始时间，结束时间查询接待费用统计
     * @param strStartDate
     * @param strEndDate
     * @return PageObject
     * @throws SQLException 
     */
    public PageObject findByFuzzy(String strStartDate, String strEndDate, String enterpriseCode) throws SQLException;
}
