/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.web.administration.regularworkrightset.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCRight;
import power.ejb.administration.AdCRightFacadeRemote;
import power.ejb.administration.business.RegularWorkRightSetFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 定期工作权限设置Action
 * 
 * @author chaihao
 * 
 */
public class RegularWorkRightSetAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    /** 读取位置 */
    private int start = 0;
    /** 读取记录数 */
    private int limit = 0;
    /** 工作类别编码 */
    private String strWorkTypeCode = "";
    /** 人员编码 */
    private String strUserCode = "";
    /** 修改时间 */
    private String updateTime;
    /** 定期工作权限实体 */
    private AdCRight right;
    /** 定期工作权限设置远程对象 */
    RegularWorkRightSetFacadeRemote remote;
    /** 定期工作权限处理远程对象 */
    AdCRightFacadeRemote adcremote;

    /**
     * 构造函数
     */
    public RegularWorkRightSetAction() {
        remote = (RegularWorkRightSetFacadeRemote) factory
                .getFacadeRemote("RegularWorkRightSetFacade");
        adcremote = (AdCRightFacadeRemote)factory.getFacadeRemote("AdCRightFacade");
    }

    /**
     * 定期工作权限查询
     * 
     */
    public void regularWorkRightQuery() {
        LogUtil.log("Action:定期工作权限查询开始", Level.INFO, null);
        try {
            PageObject pob = remote.findRegularWorkRight(strWorkTypeCode, employee.getEnterpriseCode(),
                    start, limit);
            if (pob.getTotalCount() != 0) {
                String strRes = JSONUtil.serialize(pob);
                write(strRes);
            } else {
                write("{totalCount:0,list:[]}");
            }
            LogUtil.log("Action:定期工作权限查询结束", Level.INFO, null);
        } catch (JSONException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:数据格式化失败", Level.SEVERE, e);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
    }

    /**
	 * 权限重复检查
	 */
	public void checkRegularWorkRight() {
		LogUtil.log("Action:权限是否重复检查开始", Level.INFO, null);
		try {
			boolean isOnly = adcremote.checkRight(strUserCode, employee
					.getEnterpriseCode());
			if (isOnly) {
				write("{isOnly:true}");
			} else {
				write("{isOnly:false}");
			}
			LogUtil.log("Action:权限是否重复检查结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

    /**
	 * 增加定期工作权限
	 */
    public void addRegularWorkRight() {
        LogUtil.log("Action:增加定期工作权限开始", Level.INFO, null);
        try {
            right.setIsUse("Y");
            right.setUpdateUser(employee.getWorkerCode());
            right.setUpdateTime(new Date());
            right.setEnterpriseCode(employee.getEnterpriseCode());
            adcremote.save(right);
            LogUtil.log("Action:增加定期工作权限结束", Level.INFO, null);
        } catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
    }

    /**
     * 删除定期工作权限
     */
    public void deleteRegularWorkRight() {
        LogUtil.log("Action:删除定期工作权限开始", Level.INFO, null);
        try {
            AdCRight model = adcremote.findById(right.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 取得最后一次修改时间
			String strLastModifiedTime = sdf.format(model.getUpdateTime());
			// 排他
			if (!strLastModifiedTime.equals(updateTime)) {
				write(Constants.DATA_USING);
				return;
			}
            model.setIsUse("N");
            model.setUpdateUser(employee.getWorkerCode());
            model.setUpdateTime(new Date());
            adcremote.update(model);
            write(Constants.DELETE_SUCCESS);
            LogUtil.log("Action:删除定期工作权限结束", Level.INFO, null);
        } catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
    }
    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start
     *            the start to set
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
     * @param limit
     *            the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return the strWorkTypeCode
     */
    public String getStrWorkTypeCode() {
        return strWorkTypeCode;
    }

    /**
     * @param strWorkTypeCode
     *            the strWorkTypeCode to set
     */
    public void setStrWorkTypeCode(String strWorkTypeCode) {
        this.strWorkTypeCode = strWorkTypeCode;
    }

    /**
     * @return the strUserCode
     */
    public String getStrUserCode() {
        return strUserCode;
    }

    /**
     * @param strUserCode the strUserCode to set
     */
    public void setStrUserCode(String strUserCode) {
        this.strUserCode = strUserCode;
    }

    /**
     * @return the right
     */
    public AdCRight getRight() {
        return right;
    }

    /**
     * @param right the right to set
     */
    public void setRight(AdCRight right) {
        this.right = right;
    }

	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
