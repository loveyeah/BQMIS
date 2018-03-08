/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.jobmanagement.dismissionregister.action;

import java.util.Date;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.DismissionInfoFacadeRemote;
import power.ejb.hr.HrJDimission;
import power.ejb.hr.HrJDimissionFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.*;

/**
 * 员工离职登记Action
 * 
 * @author zhengzhipeng
 * @version 1.0
 */

public class DismissionRegisterAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private static final String MSG_ONLY_SAVE = "保存";
    private static final String MSG_NOT_ONLY_SAVE = "存档";
    /** 修改 or 存档 */
    private static String STR_FLAG_0 = "0";
    /** 新增 or 保存 */
    private static String STR_FLAG_1 = "1";
    /** 可使用 */
    private static String STR_ISUSE_Y = "Y";
    /** 空 */
    private static String STR_NULL = "";
    /** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
    /** 成功时，传到前台的string */
    private static String STR_SUCCESS = "{success:true,msg:'success'}";
    /** 员工离职Remote */
    private HrJDimissionFacadeRemote hrJDimissionFacadeRemote;
    /** 员工离职登记信息Remote */
    private DismissionInfoFacadeRemote dismissionInfoFacadeRemote;

    /** 查询参数：年度 */
    private String year;
    /** 查询参数：部门 */
    private String deptId;
    /** 查询参数：离职类别 */
    private String typeId;
    /** 查询参数: 开始行 */
    private int start;
    /** 查询参数: 开始行 */
    private int limit;

    /** 离职登记人员id */
    private String dimissionid;
    /** 排他参数：修改时间 */
    private String lastModifiedDate;
    /** 排他参数：修改时间 */
    private String empLastModifiedDate;

    /** 保存:"1" 存档:"0" */
    private String onlySave;
    /** 新增:"1" 修改:"0" */
    private String addOrUpdate;
    /** 离职类别 */
    private String outTypeId;
    /** 离职日期 */
    private Date dimissionDate;
    /** 止薪日期 add by ywliu 20100617*/
    private Date stopsalaryDate;
    /** 止薪日期 add by ywliu 20100617*/
    private String advicenoteNo;
    /** 离职原因 */
    private String dimissionReason;
    /** 离职去向 */
    private String whither;
    /** 离职备注 */
    private String memo;

    // /** 排他参数：修改时间 */
    // private String empupdateTime;

    // /** 员工姓名加载参数 */
    // private String deptId;

    /**
     * @return the onlySave
     */
    public String getOnlySave() {
        return onlySave;
    }

    /**
     * @param onlySave
     *            the onlySave to set
     */
    public void setOnlySave(String onlySave) {
        this.onlySave = onlySave;
    }

    /**
     * 构造函数
     */
    public DismissionRegisterAction() {
        // 远程处理对象的取得
        dismissionInfoFacadeRemote = (DismissionInfoFacadeRemote) factory
                .getFacadeRemote("DismissionInfoFacade");
        hrJDimissionFacadeRemote = (HrJDimissionFacadeRemote) factory
                .getFacadeRemote("HrJDimissionFacade");
    }

    /**
     * 查询
     */
    public void getDismissionInfoList() {
        try {
            LogUtil.log("Action:员工离职登记查询开始", Level.INFO, null);

			String flag=request.getParameter("flag");//add by sychen 20100716
            // 取得查询参数: 企业编码
            String enterpriseCode = employee.getEnterpriseCode();
            // 根据查询条件，取得相应信息
            PageObject object = dismissionInfoFacadeRemote
                    .getDismissionInfoList(flag,year, deptId, typeId,advicenoteNo,
                            enterpriseCode, start, limit);
            // 查询结果为null,设置页面显示
            String str = STR_JSON_NULL;
            if (object == null) {
                write(str);
                // 不为null
            } else {
                if (object.getList() == null) {
                    write(str);
                } else {
                    str = JSONUtil.serialize(object);
                    write(str);
                }
            }
            LogUtil.log("Action:员工离职登记查询结束", Level.INFO, null);
        } catch (Exception e) {
            LogUtil.log("Action:员工离职登记查询失败", Level.SEVERE, e);
        }

    }

    /**
     * 删除
     */
    public void deleteDismissionInfo() throws JSONException, Exception {
        LogUtil.log("Action:删除员工离职登记信息开始", Level.INFO, null);
        try {
            dismissionInfoFacadeRemote.delete(dimissionid, employee
                    .getWorkerCode(), employee.getEnterpriseCode(),
                    lastModifiedDate);
            write(Constants.DELETE_SUCCESS);
            LogUtil.log("Action:删除员工离职登记信息结束", Level.INFO, null);
        } catch (DataChangeException de) {
            LogUtil.log("Action:删除员工离职登记信息失败", Level.SEVERE, de);
            write(Constants.DATA_USING);
        } catch (Exception e) {
            LogUtil.log("Action:删除员工离职登记信息失败", Level.SEVERE, e);
            write(Constants.SQL_FAILURE);
        }
    }

    /**
     * 更新数据(新增or修改)
     */
    public void updateDismissionInfo() throws JSONException, Exception {
        // message 种类 :保存or存档
        String msgStr = MSG_ONLY_SAVE;
        if (STR_FLAG_0.equals(onlySave)) {
            msgStr = MSG_NOT_ONLY_SAVE;
        }
        // 处理种类 : 新增or修改
        if (STR_FLAG_1.equals(addOrUpdate)) {
            save(msgStr);
        } else {
            update(msgStr);
        }
    }

    /**
     * 新增保存(存档)
     */
    public void save(String msgStr) throws JSONException, Exception {
        LogUtil.log("Action:修改" + msgStr + "员工离职登记信息开始", Level.INFO, null);
        try {
            HrJDimission hrJDimissionBeen = new HrJDimission();
            // 设置画面数据信息
            hrJDimissionBeen.setEmpId(Long.parseLong(dimissionid));
            if (outTypeId != null && !STR_NULL.equals(outTypeId)) {
                hrJDimissionBeen.setOutTypeId(Long.parseLong(outTypeId));
            }
            hrJDimissionBeen.setDimissionDate(dimissionDate);
            hrJDimissionBeen.setDimissionReason(dimissionReason);
            hrJDimissionBeen.setWhither(whither);
            hrJDimissionBeen.setMemo(memo);
            hrJDimissionBeen.setRegisterDate(new Date());//add by sychen 20100717
            // add by ywliu 20100617
            hrJDimissionBeen.setAdvicenoteNo(advicenoteNo);
            hrJDimissionBeen.setStopsalaryDate(stopsalaryDate);
            // 设定插入者
            hrJDimissionBeen.setInsertby(employee.getWorkerCode());
            // 设定修改者
            hrJDimissionBeen.setLastModifiedBy(employee.getWorkerCode());
            // 设置企业编码
            hrJDimissionBeen.setEnterpriseCode(employee.getEnterpriseCode());
            // 设置可用
            hrJDimissionBeen.setIsUse(STR_ISUSE_Y);
            // 是否存档
            if (STR_FLAG_0.equals(onlySave)) {
                // 新增存档
                hrJDimissionBeen.setIfSave(STR_FLAG_1);
            } else {
                // 新增保存
                hrJDimissionBeen.setIfSave(STR_FLAG_0);
            }
            // 保存or存档
            dismissionInfoFacadeRemote.save(hrJDimissionBeen, lastModifiedDate,
                    empLastModifiedDate);
            write(STR_SUCCESS);
            LogUtil.log("Action:修改" + msgStr + "员工离职登记信息结束", Level.INFO, null);
        } catch (DataChangeException de) {
            LogUtil.log("Action:修改" + msgStr + "员工离职登记信息失败", Level.SEVERE, de);
            write(Constants.DATA_USING);
        } catch (CodeRepeatException ce) {
            LogUtil.log("Action:修改" + msgStr + "员工离职登记信息失败", Level.SEVERE, ce);
            write(Constants.DATE_REPEAT);
        } catch (Exception e) {
            LogUtil.log("Action:修改" + msgStr + "员工离职登记信息失败", Level.SEVERE, e);
            write(Constants.SQL_FAILURE);
        }
    }

    /**
     * 修改保存(存档)
     */
    public void update(String msgStr) throws JSONException, Exception {
        LogUtil.log("Action:修改" + msgStr + "员工离职登记信息开始", Level.INFO, null);
        try {
            // 通过流水号找到员工离职been
            HrJDimission hrJDimissionBeen = hrJDimissionFacadeRemote
                    .findById(Long.parseLong(dimissionid));
            // 设置画面数据信息
            // lichensheng 修改
            if (STR_NULL.equals(outTypeId) || outTypeId == null) {
                hrJDimissionBeen.setOutTypeId(null);
            } else {
                hrJDimissionBeen.setOutTypeId(Long.parseLong(outTypeId));
            }
            hrJDimissionBeen.setDimissionDate(dimissionDate);
            hrJDimissionBeen.setDimissionReason(dimissionReason);
            hrJDimissionBeen.setWhither(whither);
            hrJDimissionBeen.setMemo(memo);

            hrJDimissionBeen.setRegisterDate(new Date());//add by sychen 20100717
            // add by ywliu 20100617
            hrJDimissionBeen.setAdvicenoteNo(advicenoteNo);
            hrJDimissionBeen.setStopsalaryDate(stopsalaryDate);
            // 设定修改者
            hrJDimissionBeen.setLastModifiedBy(employee.getWorkerCode());
            // 是否存档
            if (STR_FLAG_0.equals(onlySave)) {
                // 修改存档
                hrJDimissionBeen.setIfSave(STR_FLAG_1);

            }
            // 保存or存档
            dismissionInfoFacadeRemote.update(hrJDimissionBeen,
                    lastModifiedDate, empLastModifiedDate);
            write(STR_SUCCESS);
            LogUtil.log("Action:修改" + msgStr + "员工离职登记信息结束", Level.INFO, null);
        } catch (DataChangeException de) {
            LogUtil.log("Action:修改" + msgStr + "员工离职登记信息失败", Level.SEVERE, de);
            write(Constants.DATA_USING);
        } catch (Exception e) {
            LogUtil.log("Action:修改" + msgStr + "员工离职登记信息失败", Level.SEVERE, e);
            write(Constants.SQL_FAILURE);
        }
    }

    public void getEmpInfoByEmpId() throws JSONException {
        // 根据企业编码，部门id取得所有物料类型
        PageObject obj = new PageObject();
        obj = dismissionInfoFacadeRemote.getEmpInfo(employee
                .getEnterpriseCode(), dimissionid);
        String str = JSONUtil.serialize(obj);
        write(str);
    }

    /**
     * 离职通知单号自增
     * add by drdu 20100629
     */
    public void getAdvicenoteNoNum() {
		String sqlstr = String.format("select nvl(max(advicenote_no)+1,1) from hr_j_dimission  t where t.is_use='Y'");
//		String sqlstr = String.format("select nvl(max(advicenote_no)+1,1) from hr_j_dimission");
		try {
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance().getFacadeRemote("NativeSqlHelper");
			Object ob = bll.getSingal(sqlstr);
			String advicenoteNo = (ob.toString());
			ListRange<HrJDimission> dimissionlist = new ListRange<HrJDimission>();
			dimissionlist.setMessage(advicenoteNo);
			dimissionlist.setSuccess(true);
			String jsonstr = JSONUtil.serialize(dimissionlist);
			write(jsonstr);
		} catch (NumberFormatException e) {
			LogUtil.log("SYS_C_WEBPAGE err", Level.INFO, e);
		} catch (Exception e) {
			LogUtil.log("SYS_C_WEBPAGE err", Level.INFO, e);
		}
	}
    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return the deptId
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * @param deptId
     *            the deptId to set
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * @return the typeId
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
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
     * @return the dimissionid
     */
    public String getDimissionid() {
        return dimissionid;
    }

    /**
     * @param dimissionid
     *            the dimissionid to set
     */
    public void setDimissionid(String dimissionid) {
        this.dimissionid = dimissionid;
    }

    /**
     * @return the lastModifiedDate
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate
     *            the lastModifiedDate to set
     */
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * @return the empLastModifiedDate
     */
    public String getEmpLastModifiedDate() {
        return empLastModifiedDate;
    }

    /**
     * @param empLastModifiedDate
     *            the empLastModifiedDate to set
     */
    public void setEmpLastModifiedDate(String empLastModifiedDate) {
        this.empLastModifiedDate = empLastModifiedDate;
    }

    /**
     * @return the outTypeId
     */
    public String getOutTypeId() {
        return outTypeId;
    }

    /**
     * @param outTypeId
     *            the outTypeId to set
     */
    public void setOutTypeId(String outTypeId) {
        this.outTypeId = outTypeId;
    }

    /**
     * @return the dimissionDate
     */
    public Date getDimissionDate() {
        return dimissionDate;
    }

    /**
     * @param dimissionDate
     *            the dimissionDate to set
     */
    public void setDimissionDate(Date dimissionDate) {
        this.dimissionDate = dimissionDate;
    }

    /**
     * @return the dimissionReason
     */
    public String getDimissionReason() {
        return dimissionReason;
    }

    /**
     * @param dimissionReason
     *            the dimissionReason to set
     */
    public void setDimissionReason(String dimissionReason) {
        this.dimissionReason = dimissionReason;
    }

    /**
     * @return the whither
     */
    public String getWhither() {
        return whither;
    }

    /**
     * @param whither
     *            the whither to set
     */
    public void setWhither(String whither) {
        this.whither = whither;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo
     *            the memo to set
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @return the addOrUpdate
     */
    public String getAddOrUpdate() {
        return addOrUpdate;
    }

    /**
     * @param addOrUpdate
     *            the addOrUpdate to set
     */
    public void setAddOrUpdate(String addOrUpdate) {
        this.addOrUpdate = addOrUpdate;
    }

	public Date getStopsalaryDate() {
		return stopsalaryDate;
	}

	public void setStopsalaryDate(Date stopsalaryDate) {
		this.stopsalaryDate = stopsalaryDate;
	}

	public String getAdvicenoteNo() {
		return advicenoteNo;
	}

	public void setAdvicenoteNo(String advicenoteNo) {
		this.advicenoteNo = advicenoteNo;
	}
}
