/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.util.zip.DataFormatException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCTimeWork;
import power.ejb.administration.AdCTimeWorkD;

/**
 * 定期工作安排接口.
 * 
 * @author liugonglei
 */
@Remote
public interface RegularWorkArrangeFacadeRemote {
    /**
     * 定期工作信息检索
     * @param strWorkTypeCode 工作类型code
     * @param start 开始项
     * @param limit 限制长度
     * @return
     */
    public PageObject findRegularWorkInfo(String strWorkTypeCode,String strEnterPriseCode, int start,
            int limit) throws Exception;
    /**
     * 定期工作详细信息检索
     * @param workItemCode 工作项目code
     * @param start 开始项
     * @param limit 限制长度
     * @return
     * @throws SQLException 
     */
    public PageObject findCycleParInfo(String workItemCode) throws SQLException;
    /**
     * 取得定期工作维护信息
     * @param lngId 定期工作维护id
     * @return
     * @throws SQLException 
     */
    public AdCTimeWork findAdcTimeWork(Long lngId) throws SQLException;
    /**
     * 取得定期工作明细信息
     * @param strWorkItemCode 工作项目code
     * @return
     * @throws SQLException 
     */
    public PageObject findAdCTimeWorkD(String strWorkItemCode) throws SQLException;
    /**
     * 取得新的定期工作明细id
     * @return
     * @throws SQLException 
     */
    public Long getNewAdCTimeWorkDId() throws SQLException;
    /**
     * 取得新的定期工作维护id
     * @return
     * @throws SQLException 
     */
    public Long getNewAdCTimeWorkId() throws SQLException;
    /**
     * 更新定期工作明细
     * @param adcTWDEntity
     * @param strUpdateTime
     * @throws SQLException
     * @throws DataFormatException
     */
    public void update(AdCTimeWorkD adcTWDEntity,String strUpdateTime)throws SQLException,DataFormatException;
}
