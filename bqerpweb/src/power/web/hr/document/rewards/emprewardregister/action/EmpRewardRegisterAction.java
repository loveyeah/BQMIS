/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.rewards.emprewardregister.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.EmpRewardRegisterInfo;
import power.ejb.hr.HrCRewardspunish;
import power.ejb.hr.HrCRewardspunishFacadeRemote;
import power.ejb.hr.HrJRewardspunish;
import power.ejb.hr.HrJRewardspunishFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 员工奖惩登记
 * 
 * @author zhaozhijie
 * @version 1.0
 */
public class EmpRewardRegisterAction  extends AbstractAction {

	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 开始 */
	private String start;
	/** 限制 */
	private String limit;
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd";
	/** 奖惩月份 */
	private String rewardMonth;
	/** 判断更新，删除 */
	private String flag = "0";
    /** 常量pageObject为空 */
    public static final String PAGEOBJECT_NULL = "{\"list\":[],\"totalCount\":0}";
    /** 常量修改 */
    public static final String FLAG_ZERO = "0";
    /** 常量更新 */
    public static final String FLAG_TRUE = "1";
    /** 常量更新，删除 */
    public static final String FLAG_DEL_ZERO = "0";
	/** 职工奖惩id */
	private String id;
	/** 员工奖惩登记ejb远程维护对象 */
	protected HrJRewardspunishFacadeRemote remote;
    /**奖惩维护ejb远程维护对象 */
    private HrCRewardspunishFacadeRemote rewardsRemote;
	/** 员工奖惩登记实体 */
	private EmpRewardRegisterInfo empReward;

	/**
	 * 构造函数
	 */
	public EmpRewardRegisterAction() {
		// 员工奖惩登记ejb远程维护对象
		remote = (HrJRewardspunishFacadeRemote) factory
		.getFacadeRemote("HrJRewardspunishFacade");
		// 奖惩维护ejb远程维护对象
		rewardsRemote = (HrCRewardspunishFacadeRemote) factory
		.getFacadeRemote("HrCRewardspunishFacade");
	}

	/**
	 * 查询员工奖惩登记信息
	 */
	public void getEmpRewardInfo() {
		LogUtil.log("Action:员工奖惩登记信息查询开始", Level.INFO, null);
		try {
			PageObject obj = remote.getEmpRewardInfo(rewardMonth,employee.getEnterpriseCode(),
					Integer.parseInt(start), Integer.parseInt(limit));
			if(obj.getTotalCount() == null) {
				write(PAGEOBJECT_NULL);
			} else {
				write(JSONUtil.serialize(obj));
			}
			LogUtil.log("Action:员工奖惩登记信息查询结束", Level.INFO, null);
		} catch(SQLException sqle) {
			LogUtil.log("Action:员工奖惩登记信息查询失败", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}catch (JSONException jsone) {
            LogUtil.log("Action:员工奖惩登记信息查询失败", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
        }
	}

	/**
	 * 查询奖惩类别维护信息
	 */
	@SuppressWarnings("unchecked")
	public void getEmpRewardsMainInfo() {
        try {
            LogUtil.log("Action:奖惩类别查询开始。", Level.INFO, null);
            PageObject obj = new PageObject();
            // 查询奖惩类别维护信息
            obj = rewardsRemote.findAllRewards(employee.getEnterpriseCode());
    		// 追加空格
    		if(obj.getList().size() > 0){
    			HrCRewardspunish info = new HrCRewardspunish();
    			info.setRewardsPunishId(null);
    			info.setRewardsPunish("");
    			info.setRewardsPunishType("");
    			obj.getList().add(0, info);
    		}
            String str = JSONUtil.serialize(obj);
            write(str);
            LogUtil.log("Action:奖惩类别查询结束。", Level.INFO, null);
        } catch (JSONException jsone) {
            LogUtil.log("Action:奖惩类别查询失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
        } catch (SQLException sqle) {
            LogUtil.log("Action:奖惩类别查询失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
        }
	}
	/**
	 * 新增操作
	 * @param lstSaveEmpRewardRegisterInfo 新增的数据
	 */
	public void newEmpRewards(List<HrJRewardspunish> lstSaveHrJRewardspunish) {
		LogUtil.log("Action:新增员工奖惩登记开始", Level.INFO, null);
		try {
			HrJRewardspunish entity = new HrJRewardspunish();
			// 人员id
			entity.setEmpId(Long.parseLong(empReward.getEmpID()));
			// 奖惩名称ID
			if(empReward.getRewardsPunishId() != null && !"".equals(empReward.getRewardsPunishId())) {
				entity.setRewardsPunishId(Long.parseLong(empReward.getRewardsPunishId()));
			}
			// 奖惩日期
			// 日期的格式化
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
			if (empReward.getRewardsDate() != null && !"".equals(empReward.getRewardsDate())) {
				Date dteRewardsDate = dateFormat.parse(empReward.getRewardsDate());
				entity.setRewardspunishDate(dteRewardsDate);
			}
			// 奖惩原因
			entity.setRewardsPunishReason(empReward.getRewardsReason());
			// 备注
			entity.setMemo(empReward.getMemo());
			// 记录人
			entity.setInsertby(employee.getWorkerCode());
			// 记录日期
			entity.setInsertdate(new java.util.Date());
			// 修改人
			entity.setLastModifiedBy(employee.getWorkerCode());
			// 修改日期
			entity.setLastModifiedDate(new java.util.Date());
			// 企业编码
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			// 是否使用
			entity.setIsUse(Constants.IS_USE_Y);
	
			lstSaveHrJRewardspunish.add(entity);
			LogUtil.log("Action:新增员工奖惩登记结束", Level.INFO, null);
		}catch (ParseException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:新增员工奖惩登记失败", Level.SEVERE, e);
		}
	}

	/**
	 * 保存操作
	 */
	public void saveEmpRewards() {
		try{
			LogUtil.log("Action:员工奖惩登记操作开始。", Level.INFO, null);
			 // 新增员工奖惩登记
			 List<HrJRewardspunish> lstSaveHrJRewardspunish = new ArrayList<HrJRewardspunish>();
			 // 更新员工奖惩登记
			 List<HrJRewardspunish> lstUpdateHrJRewardspunish = new ArrayList<HrJRewardspunish>();
			 // 删除员工奖惩登记
			 List<HrJRewardspunish> lstDeleteHrJRewardspunish = new ArrayList<HrJRewardspunish>();
			 // 判断更新，删除
			 if (FLAG_DEL_ZERO.equals(flag)) {
				 // 判断新增，更新
				 String strFlag = empReward.getFlag();
				 // 新增员工奖惩登记
				 if (FLAG_ZERO.equals(strFlag)) {
					 newEmpRewards(lstSaveHrJRewardspunish);
				 }
				 // 更新员工奖惩登记
				 if (FLAG_TRUE.equals(strFlag)) {
					 updateEmpRewards(lstUpdateHrJRewardspunish);
				 }
			 } else {
				 // 删除员工奖惩登记
				 deleteEmpRewards(lstDeleteHrJRewardspunish);
			 }


			 remote.save(lstSaveHrJRewardspunish,lstUpdateHrJRewardspunish,lstDeleteHrJRewardspunish);
			 write(Constants.MODIFY_SUCCESS);
			 LogUtil.log("Action:员工奖惩登记操作结束。", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:员工奖惩登记操作失败。", Level.SEVERE, null);
			write(Constants.DATA_USING);
		} catch (SQLException e) {
			LogUtil.log("Action:员工奖惩登记操作失败。", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		} catch (Exception e) {
			LogUtil.log("Action:员工奖惩登记操作失败。", Level.SEVERE, null);
		}
	}

	/**
	 * 删除操作
	 * @param  lstDeleteHrJRewardspunish 所需删除的数据
	 */
	private void deleteEmpRewards(
			List<HrJRewardspunish> lstDeleteHrJRewardspunish) {
		LogUtil.log("Action:删除员工奖惩登记开始", Level.INFO, null);
		HrJRewardspunish entity = new HrJRewardspunish();
		// 员工奖惩登记实体
		entity = remote.findById(Long.parseLong(id));
		// 是否使用
		entity.setIsUse(Constants.IS_USE_N);
		// 修改人
		entity.setLastModifiedBy(employee.getWorkerCode());

		lstDeleteHrJRewardspunish.add(entity);
		LogUtil.log("Action:删除员工奖惩登记结束", Level.INFO, null);
	}

	/**
	 * 更新操作
	 * @param lstUpdateHrJRewardspunish  所需更新的数据
	 */
	private void updateEmpRewards(
			List<HrJRewardspunish> lstUpdateHrJRewardspunish) {
		LogUtil.log("Action:更新员工奖惩登记开始", Level.INFO, null);
		try {
			HrJRewardspunish entity = new HrJRewardspunish();
			// 员工奖惩登记实体
			entity = remote.findById(Long.parseLong(empReward.getRewardsPunishID()));
			// 奖惩名称ID
			if(empReward.getRewardsPunishId() != null && !"".equals(empReward.getRewardsPunishId())) {
				entity.setRewardsPunishId(Long.parseLong(empReward.getRewardsPunishId()));
			}
			// 奖惩日期
			// 日期的格式化
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
			if (empReward.getRewardsDate() != null && !"".equals(empReward.getRewardsDate())) {
				Date dteRewardsDate = dateFormat.parse(empReward.getRewardsDate());
				entity.setRewardspunishDate(dteRewardsDate);
			}
			// 奖惩原因
			entity.setRewardsPunishReason(empReward.getRewardsReason());
			// 备注
			entity.setMemo(empReward.getMemo());
			// 修改人
			entity.setLastModifiedBy(employee.getWorkerCode());

			lstUpdateHrJRewardspunish.add(entity);
			LogUtil.log("Action:更新员工奖惩登记结束", Level.INFO, null);
		} catch (ParseException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:更新员工奖惩登记失败", Level.SEVERE, e);
		}
	}

	/**
	 * @return 开始
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param 开始
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return 限制
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * @param 限制
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}

	/**
	 * @return 奖惩月份
	 */
	public String getRewardMonth() {
		return rewardMonth;
	}

	/**
	 * @param 奖惩月份
	 */
	public void setRewardMonth(String rewardMonth) {
		this.rewardMonth = rewardMonth;
	}

	/**
	 * @return 员工奖惩登记实体
	 */
	public EmpRewardRegisterInfo getEmpReward() {
		return empReward;
	}

	/**
	 * @param 员工奖惩登记实体
	 */
	public void setEmpReward(EmpRewardRegisterInfo empReward) {
		this.empReward = empReward;
	}

	/**
	 * @return 职工奖惩id 
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param 职工奖惩id 
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return 判断更新，删除
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param 判断更新，删除
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

}
