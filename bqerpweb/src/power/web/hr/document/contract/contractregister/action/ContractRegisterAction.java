/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.contract.contractregister.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJCarwhInvoiceFacadeRemote;
import power.ejb.hr.HrJWorkcontract;
import power.ejb.hr.HrJWorkcontractFacadeRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.WorkContract;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 新签合同登记Action
 * 
 * @author zhouxu
 * 
 */
public class ContractRegisterAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    /** 日期形式字符串 DATE_FORMAT_YYYYMMDD */
    private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    /** 合同正在执行 */
    private static final String EXECUTE_YES = "1";
    /** 新签合同 */
    private static final String CONTINUE_MARK_NO = "0";
    /** 定义合同查询接口 */
    protected HrJWorkcontractFacadeRemote remote;
    /** 定义附件查询接口 */
    protected HrJCarwhInvoiceFacadeRemote remoteApppend;
    /** 定义员工id */
    private Long empId;
    /** 批量员工信息 */
    private String workers;
    /** 定义附件id */
    private Long fileId;
    /** 定义含员工、部门和合同信息的bean */
    private WorkContract bean;
    /** 定义空store常量 */
    private String NULL_STORE = "{\"list\":[],\"totalCount\":null}";
    /** 操作成功定义 */
    private String DO_SUCCESS = "{success:true,msg:'S'}";

    /** 构造函数 */
    public ContractRegisterAction() {
        remote = (HrJWorkcontractFacadeRemote) factory.getFacadeRemote("HrJWorkcontractFacade");
        remoteApppend = (HrJCarwhInvoiceFacadeRemote) factory.getFacadeRemote("HrJCarwhInvoiceFacade");
    }

    /**
     * 按员工ID查找合同
     */
    public void getContractByEmpId() {
        try {
            LogUtil.log("Action:按员工ID查询合同开始。", Level.INFO, null);
            // 定义最后write的字符串变量
            String str = "";
            // 查询并返回pageobject型
            PageObject obj = remote.getWorkContractInfo(empId, employee.getEnterpriseCode());
            // 如果查询结果不为空
            if (obj.getTotalCount() > 0) {
                str = JSONUtil.serialize(obj);
            } else {
                // 如果查询结果为空
                str = NULL_STORE;
            }
            // 输出结果
            write(str);
            LogUtil.log("Action:按员工ID查询合同结束。", Level.INFO, null);
        } catch (JSONException jsone) {
            // 格式化失败
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:按员工ID查询合同失败。", Level.SEVERE, jsone);
        } catch (RuntimeException sqle) {
            // ejb失败
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:按员工ID查询合同失败。", Level.SEVERE, sqle);
        } catch (ParseException e) {
            // 格式化失败
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:按员工ID查询合同失败。", Level.SEVERE, e);
        }
    }

    /**
     * 批量查询劳动合同
     */
    @SuppressWarnings("unchecked")
    public void getContractByEmpIds() {
        try {
            LogUtil.log("Action:批量查询劳动合同开始。", Level.INFO, null);
            // 定义最后write的字符串变量
            String str = "";
            List<Long> empIdsWithContract = new ArrayList<Long>();
            // 获取前台的批处理所有员工的信息
            List<Map> newWorkers = (List<Map>) JSONUtil.deserialize(workers);
            // 循环将员工信息放到list里
            for (int i = 0; i < newWorkers.size(); i++) {
                Map<Object, Object> info = newWorkers.get(i);
                if (info.get("empId") != null && !Constants.BLANK_STRING.equals(info.get("empId")))
                    empIdsWithContract.add(Long.parseLong(info.get("empId").toString()));
            }
            // 查询并返回pageobject型
            PageObject obj = remote.getWorkContractInfos(empIdsWithContract, employee.getEnterpriseCode());
            // 如果查询结果不为空
            if (obj.getTotalCount() > 0) {
                str = JSONUtil.serialize(obj);
            } else {
                // 如果查询结果为空
                str = NULL_STORE;
            }
            // 输出结果
            write(str);
            LogUtil.log("Action:批量查询劳动合同结束。", Level.INFO, null);
        } catch (JSONException jsone) {
            // 格式化失败
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:批量查询劳动合同失败。", Level.SEVERE, jsone);
        } catch (RuntimeException sqle) {
            // ejb失败
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:批量查询劳动合同失败。", Level.SEVERE, sqle);
        } catch (ParseException e) {
            // 格式化失败
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:批量查询劳动合同失败。", Level.SEVERE, e);
        }
    }

    /**
     * 新增一条劳动合同
     */
    public void saveContract() {
        try {
            LogUtil.log("Action:新增劳动合同开始。", Level.INFO, null);
            HrJWorkcontract contractBean = new HrJWorkcontract();
            // 赋值员工id
            contractBean.setEmpId(bean.getEmpId());
            // 赋值部门id
            contractBean.setDeptId(bean.getDeptId());
            // 赋值岗位id
            contractBean.setStationId(bean.getStationId());
            // 赋值合同号
            contractBean.setWrokContractNo(bean.getWrokContractNo());
            // 赋值有效期
            contractBean.setContractTermId(bean.getContractTermId());
            // 赋值甲方部门
            contractBean.setFristDepId(bean.getFristDepId());
            // 赋值甲方地址
            contractBean.setFristAddrest(bean.getFristAddrest());
            // 赋值签字日期
            contractBean.setWorkSignDate(formatStringToDate(bean.getWorkSignDate(), DATE_FORMAT_YYYYMMDD));
            // 赋值开始日期
            contractBean.setStartDate(formatStringToDate(bean.getStartDate(), DATE_FORMAT_YYYYMMDD));
            // 赋值结束日期
            contractBean.setEndDate(formatStringToDate(bean.getEndDate(), DATE_FORMAT_YYYYMMDD));
            // 赋值合同正在执行
            contractBean.setIfExecute(EXECUTE_YES);
            // 赋值新签合同
            contractBean.setContractContinueMark(CONTINUE_MARK_NO);
            // 赋值备注
            contractBean.setMemo(bean.getMemo());
            // 赋值记录人
            contractBean.setInsertby(employee.getWorkerCode());
            // 赋值修改人
            contractBean.setLastModifiedBy(employee.getWorkerCode());
            // 赋值正在使用
            contractBean.setIsUse(Constants.IS_USE_Y);
            // 赋值企业编码
            contractBean.setEnterpriseCode(employee.getEnterpriseCode());
            // add by ywliu 20100611
            // 赋值甲方
            contractBean.setOwner(bean.getOwner());
            // 赋值签订机构
            contractBean.setSignedInstitutions(bean.getSignedInstitutions());
            // 赋值合同期限
            contractBean.setContractPeriod(bean.getContractPeriod());
            // 赋值用工形式
            contractBean.setLaborType(bean.getLaborType());
            // 赋值合同类别
            contractBean.setContractType(bean.getContractType());
            // end
            // 保存该合同
            remote.save(contractBean);
            write(DO_SUCCESS);
            LogUtil.log("Action:新增劳动合同结束。", Level.SEVERE, null);
        } catch (DataFormatException e) {
            // 格式化失败
            LogUtil.log("Action:新增劳动合同失败。", Level.SEVERE, null);
            write(Constants.DATA_FAILURE);
        } catch (RuntimeException e) {
            // ejb失败
            LogUtil.log("Action:新增劳动合同失败。", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
        } catch (DataChangeException e) {
            LogUtil.log("Action:新增劳动合同失败。", Level.SEVERE, null);
            write("{success:true,msg:'X',empName:'"+bean.getEmpName()+"'}");
        }

    }

    /**
     * 新增多条劳动合同
     */
    @SuppressWarnings("unchecked")
    public void saveContractBat() {
        try {
            LogUtil.log("Action:批量新增劳动合同开始。", Level.INFO, null);
            List<HrJWorkcontract> list = new ArrayList<HrJWorkcontract>();
            // 获取前台传来的所有员工信息
            List<Map> newWorkers = (List<Map>) JSONUtil.deserialize(workers);
            // 循环赋值员工信息添加到list
            for (int i = 0; i < newWorkers.size(); i++) {
                HrJWorkcontract contractBean = new HrJWorkcontract();
                Map<Object, Object> info = newWorkers.get(i);
                // 如果员工id不为空，赋值员工id
                if (info.get("empId") != null && !Constants.BLANK_STRING.equals(info.get("empId")))
                    contractBean.setEmpId(Long.parseLong(info.get("empId").toString()));
                // 如果部门id不为空，赋值部门id
                if (info.get("deptId") != null && !Constants.BLANK_STRING.equals(info.get("deptId")))
                    contractBean.setDeptId(Long.parseLong(info.get("deptId").toString()));
                // 如果岗位id不为空，赋值岗位id
                if (info.get("stationId") != null && !Constants.BLANK_STRING.equals(info.get("stationId")))
                    contractBean.setStationId(Long.parseLong(info.get("stationId").toString()));
                // 赋值劳动合同号
                contractBean.setWrokContractNo(info.get("wrokContractNo").toString());
                // 赋值有效期id
                contractBean.setContractTermId(bean.getContractTermId());
                // 赋值甲方部门id
                contractBean.setFristDepId(bean.getFristDepId());
                // 赋值甲方地址
                contractBean.setFristAddrest(bean.getFristAddrest());
                // 赋值签字日期
                contractBean.setWorkSignDate(formatStringToDate(bean.getWorkSignDate(), DATE_FORMAT_YYYYMMDD));
                // 赋值开始日期
                contractBean.setStartDate(formatStringToDate(bean.getStartDate(), DATE_FORMAT_YYYYMMDD));
                // 赋值结束日期
                contractBean.setEndDate(formatStringToDate(bean.getEndDate(), DATE_FORMAT_YYYYMMDD));
                // 赋值合同正在执行
                contractBean.setIfExecute(EXECUTE_YES);
                // 赋值新签合同
                contractBean.setContractContinueMark(CONTINUE_MARK_NO);
                // 赋值备注
                contractBean.setMemo(bean.getMemo());
                // 赋值记录人
                contractBean.setInsertby(employee.getWorkerCode());
                // 赋值修改人
                contractBean.setLastModifiedBy(employee.getWorkerCode());
                // 赋值正在使用
                contractBean.setIsUse(Constants.IS_USE_Y);
                // 赋值企业编码
                contractBean.setEnterpriseCode(employee.getEnterpriseCode());
                // 加入list
                list.add(contractBean);
            }
            // 批量保存
            remote.saveBat(list);
            write(DO_SUCCESS);
            LogUtil.log("Action:批量新增劳动合同结束。", Level.SEVERE, null);
        } catch (DataFormatException e) {
            // 格式化失败
            LogUtil.log("Action:批量新增劳动合同失败。", Level.SEVERE, null);
            write(Constants.DATA_FAILURE);
        } catch (RuntimeException e) {
            // ejb失败
            LogUtil.log("Action:批量新增劳动合同失败。", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
        } catch (JSONException e) {
            // 格式化失败
            LogUtil.log("Action:批量新增劳动合同失败。", Level.SEVERE, null);
            write(Constants.DATA_FAILURE);
        } catch (DataChangeException e) {
            LogUtil.log("Action:新增劳动合同失败。", Level.SEVERE, null);
            write("{success:true,msg:'X',empName:"+e.getMessage()+"}");
        }
    }

    /**
     * 更新一个合同记录
     */
    public void updateContract() {
        try {
            LogUtil.log("Action:更新劳动合同开始。", Level.INFO, null);
            HrJWorkcontract contractBean = new HrJWorkcontract();
            // 赋值合同id
            contractBean.setWorkcontractid(bean.getWorkcontractid());
            // 赋值合同号
            contractBean.setWrokContractNo(bean.getWrokContractNo());
            // 赋值有效期id
            contractBean.setContractTermId(bean.getContractTermId());
            // 赋值甲方部门
            contractBean.setFristDepId(bean.getFristDepId());
            // 赋值甲方地址
            contractBean.setFristAddrest(bean.getFristAddrest());
            // 赋值签约时间
            contractBean.setWorkSignDate(formatStringToDate(bean.getWorkSignDate(), DATE_FORMAT_YYYYMMDD));
            // 赋值开始时间
            contractBean.setStartDate(formatStringToDate(bean.getStartDate(), DATE_FORMAT_YYYYMMDD));
            // 赋值结束日期
            contractBean.setEndDate(formatStringToDate(bean.getEndDate(), DATE_FORMAT_YYYYMMDD));
            // 赋值备注
            contractBean.setMemo(bean.getMemo());
            // 赋值企业编码
            contractBean.setEnterpriseCode(employee.getEnterpriseCode());
            // 赋值是否使用
            contractBean.setIsUse(Constants.IS_USE_Y);
            // 赋值是否执行
            contractBean.setIfExecute(EXECUTE_YES);
            // 赋值更新人
            contractBean.setLastModifiedBy(employee.getWorkerCode());
            // 赋值最后更新时间
            contractBean.setLastModifiedDate(bean.getLastModifiedDate());
            // add by ywliu 20100611
            // 赋值甲方
            contractBean.setOwner(bean.getOwner());
            // 赋值签订机构
            contractBean.setSignedInstitutions(bean.getSignedInstitutions());
            // 赋值合同期限
            contractBean.setContractPeriod(bean.getContractPeriod());
            // 赋值用工形式
            if(!"null".equals(bean.getLaborType())) {
            	contractBean.setLaborType(bean.getLaborType());
            }
            // 赋值合同类别
            if(!"null".equals(bean.getContractType())) {
            	contractBean.setContractType(bean.getContractType());
            }
            // end
            // 更新合同
            remote.update(contractBean);
            write(DO_SUCCESS);
            LogUtil.log("Action:更新劳动合同结束。", Level.SEVERE, null);
        } catch (DataFormatException e) {
            // 排他失败
            LogUtil.log("Action:更新劳动合同失败。", Level.SEVERE, e);
            write(Constants.DATA_FAILURE);
        } catch (DataChangeException e) {
            // 格式化失败
            LogUtil.log("Action:更新劳动合同失败。", Level.SEVERE, e);
            write(Constants.DATA_USING);
        } catch (RuntimeException e) {
            // ejb失败
            LogUtil.log("Action:更新劳动合同失败。", Level.SEVERE, e);
            write(Constants.SQL_FAILURE);
        }
    }

    /**
     * 根据日期日期字符串和形式返回日期
     * 
     * @param argDateStr
     *            日期字符串
     * @param argFormat
     *            日期形式字符串
     * @return 日期
     * @throws DataFormatException
     */
    private Date formatStringToDate(String argDateStr, String argFormat) throws DataFormatException {
        if (argDateStr == null || argDateStr.trim().length() < 1) {
            return null;
        }
        // 日期形式
        SimpleDateFormat sdfFrom = null;
        // 返回日期
        Date result = null;

        try {
            sdfFrom = new SimpleDateFormat(argFormat);
            // 格式化日期
            result = sdfFrom.parse(argDateStr);
        } catch (Exception e) {
            write(Constants.DATA_FAILURE);
            result = null;
            throw new DataFormatException();
        } finally {
            sdfFrom = null;
        }
        return result;
    }

    /**
     * @return the empId
     */
    public Long getEmpId() {
        return empId;
    }

    /**
     * @param empId
     *            the empId to set
     */
    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    /**
     * @return the bean
     */
    public WorkContract getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(WorkContract bean) {
        this.bean = bean;
    }

    /**
     * @return the fileId
     */
    public Long getFileId() {
        return fileId;
    }

    /**
     * @param fileId
     *            the fileId to set
     */
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    /**
     * @return the workers
     */
    public String getWorkers() {
        return workers;
    }

    /**
     * @param workers
     *            the workers to set
     */
    public void setWorkers(String workers) {
        this.workers = workers;
    }

}
