package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 奖惩类别维护
 * fyyang 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCRewardspunishFacadeRemote {
	
	public void save(HrCRewardspunish entity);

	
	public void delete(HrCRewardspunish entity);

	
	public HrCRewardspunish update(HrCRewardspunish entity);

	public HrCRewardspunish findById(Long id);

	
	
	
	/**
     * 查询
     * @param enterpriseCode 企业编码
     * @param rowStartIdxAndCount 分页
     * @return PageObject  查询结果
     * @throws SQLException
     */
    public PageObject findAllRewards(String enterpriseCode, final int... rowStartIdxAndCount)throws SQLException;
    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @throws SQLException
     */
    public HrCRewardspunish addReward(HrCRewardspunish entity)throws SQLException;
    /**
     * 删除一条奖惩类别信息
     *
     * @param 奖惩名称ID
     * @param workerCode 登录者id
     * @throws SQLException
     *             
     */
    public void deleteByRewardsPunishId(HrCRewardspunish entity)throws SQLException;
    /**
     * 修改一条记录
     *
     * @param entity  要修改的记录
     * @return InvCLocation  修改的记录
     * @throws SQLException
     */
    public HrCRewardspunish updateReward(HrCRewardspunish entity)throws SQLException;
}