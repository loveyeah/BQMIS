/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.rewards.action;

import java.sql.SQLException;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.Constants;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCRewardspunish;
import power.ejb.hr.HrCRewardspunishFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;

/**
 * 奖惩类别维护
 * 
 * @author wujiao
 * @version 1.0
 */
public class RewardsTypeAction extends AbstractAction {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /** 查询，保存，修改数据远程对象 */
    private HrCRewardspunishFacadeRemote rewardsRemote;
    /** 分页信息 */
    private int start;
    private int limit;
    /** 奖惩类别ID */
    private String rewardsPunishId;
    /** 奖惩类别名称 */
    private String rewardsPunish;
    /** 奖惩类别 */
    private String rewardsPunishType;
    /** 显示顺序 */
    private Long orderBy;

    /**
     * 构造函数
     */
    public RewardsTypeAction() {
        rewardsRemote = (HrCRewardspunishFacadeRemote) factory
                .getFacadeRemote("HrCRewardspunishFacade");
    }

    /**
     * 奖惩类别维护 查询所有信息
     * 
     * @throws SQLException
     * 
     * @throws SQLException,JSONException
     * @throws JSONException
     * 
     */
    public void searchRewards() throws SQLException, JSONException {
        try {
            LogUtil.log("Action:奖惩类别维护查询开始。", Level.INFO, null);
            PageObject obj = new PageObject();
            // 分页信息
            // 分页信息不为空时执行
//            if (this.start != '0' && this.limit != '0') {
                obj = rewardsRemote.findAllRewards(employee.getEnterpriseCode(), start, limit);
//            } else {
//                // 无分页信息时执行
//                obj = rewardsRemote.findAllRewards(employee.getEnterpriseCode());
//            }
            String str = JSONUtil.serialize(obj);
            write(str);
            LogUtil.log("Action:奖惩类别维护查询结束。", Level.INFO, null);
        } catch (JSONException jsone) {
            LogUtil.log("Action:奖惩类别维护查询失败。", Level.SEVERE, jsone);
            throw jsone;
        } catch (SQLException sqle) {
            LogUtil.log("Action:奖惩类别维护查询失败。", Level.SEVERE, sqle);
            throw sqle;
        }
    }

    /**
     * 奖惩类别维护 删除信息
     * 
     * @throws SQLException
     * 
     * @throws SQLException
     * 
     */
    public void deleteReward() throws SQLException {
        try {
            LogUtil.log("Action:奖惩类别维护删除开始。", Level.INFO, null);
            // 根据流水号寻找这条奖惩类别维护信息
            HrCRewardspunish entity = rewardsRemote.findById(Long
                    .parseLong(rewardsPunishId.toString()));
            // 设置IS_USE字段为N
            entity.setIsUse(Constants.IS_USE_N);
            // 上次修改人
            entity.setLastModifiedBy(employee.getWorkerCode());
            // 取得奖惩类别id
            rewardsRemote.deleteByRewardsPunishId(entity);
            write(Constants.DELETE_SUCCESS);
            LogUtil.log("Action:奖惩类别维护删除结束。", Level.INFO, null);
        } catch (SQLException sqle) {
            LogUtil.log("Action:奖惩类别维护删除失败。", Level.SEVERE, sqle);
            throw sqle;
        }
    }

    /**
     * 奖惩类别维护 修改信息
     * 
     * @param entity
     * 
     * @throws SQLException
     * 
     */
    public void updateReward() throws SQLException {
        try {
            LogUtil.log("Action:奖惩类别维护修改开始。", Level.INFO, null);
            // 根据流水号寻找这条奖惩类别维护信息
            HrCRewardspunish entity = rewardsRemote.findById(Long
                    .parseLong(rewardsPunishId.toString()));
            // 设置新名称
            entity.setRewardsPunish(rewardsPunish);
            // 设置新类别
            entity.setRewardsPunishType(rewardsPunishType);
            // 设置新的显示顺序
            if (Constants.BLANK_STRING.equals(orderBy)) {
                entity.setOrderBy(null);
            } else {
                entity.setOrderBy(orderBy);
            }
            // 设置更新时间
            entity.setLastModifiedDate(new java.util.Date());
            // 设置更新者
            entity.setLastModifiedBy(employee.getWorkerCode());
            // 更新数据
            if (!Constants.BLANK_STRING.equals(rewardsPunishId)) {
                rewardsRemote.updateReward(entity);
            }
            write(Constants.MODIFY_SUCCESS);
            LogUtil.log("Action:奖惩类别维护修改结束。", Level.INFO, null);
        } catch (SQLException sqle) {
            LogUtil.log("Action:奖惩类别维护修改失败。", Level.INFO, null);
            throw sqle;
        }
    }
    
    /**
     * 奖惩类别维护 增加信息
     * 
     * @param entity
     * 
     * @throws SQLException
     * 
     */
    public void addReward() throws SQLException {
        try {
            LogUtil.log("Action:奖惩类别维护新增开始。", Level.INFO, null);
            // 根据流水号寻找这条币种信息
            HrCRewardspunish entity = new HrCRewardspunish();
            // 设置新惩类别维护名称
            entity.setRewardsPunish(rewardsPunish);
            // 设置新奖惩类别维护类别
            entity.setRewardsPunishType(rewardsPunishType);
            // 企业代码
            entity.setEnterpriseCode(employee.getEnterpriseCode());
            // 设置新奖惩类别维护显示顺序
            if (Constants.BLANK_STRING.equals(orderBy)) {
                entity.setOrderBy(null);
            } else {
                entity.setOrderBy(orderBy);
            }
            // -----------------------------------------------------------------------
            entity.setInsertby(employee.getWorkerCode());
            entity.setInsertdate(new java.util.Date());
            // 设置创建时间
            entity.setLastModifiedDate(new java.util.Date());
            // 设置创建者
            entity.setLastModifiedBy(employee.getWorkerCode());
            // 更新数据
            rewardsRemote.addReward(entity);
            LogUtil.log("Action:奖惩类别维护新增结束。", Level.INFO, null);
        } catch (SQLException sql) {
            LogUtil.log("Action:奖惩类别维护新增失败。", Level.INFO, null);
            throw sql;
        }
    }

  

    /**
     * @return the rewardsPunishId
     */
    public String getRewardsPunishId() {
        return rewardsPunishId;
    }

    /**
     * @param rewardsPunishId
     *            the rewardsPunishId to set
     */
    public void setRewardsPunishId(String rewardsPunishId) {
        this.rewardsPunishId = rewardsPunishId;
    }

    /**
     * @return the rewardsPunish
     */
    public String getRewardsPunish() {
        return rewardsPunish;
    }

    /**
     * @param rewardsPunish
     *            the rewardsPunish to set
     */
    public void setRewardsPunish(String rewardsPunish) {
        this.rewardsPunish = rewardsPunish;
    }

    /**
     * @return the rewardsPunishType
     */
    public String getRewardsPunishType() {
        return rewardsPunishType;
    }

    /**
     * @param rewardsPunishType
     *            the rewardsPunishType to set
     */
    public void setRewardsPunishType(String rewardsPunishType) {
        this.rewardsPunishType = rewardsPunishType;
    }

    /**
     * @return the orderBy
     */
    public Long getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(Long orderBy) {
        this.orderBy = orderBy;
    }

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
