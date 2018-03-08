/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.hr.document.empdocument.basetablemaint.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCAppellation;
import power.ejb.hr.HrCAppellationFacadeRemote;
import power.ejb.hr.HrCDegree;
import power.ejb.hr.HrCDegreeFacadeRemote;
import power.ejb.hr.HrCEducation;
import power.ejb.hr.HrCEducationFacadeRemote;
import power.ejb.hr.HrCEmpType;
import power.ejb.hr.HrCEmpTypeFacadeRemote;
import power.ejb.hr.HrCLanguage;
import power.ejb.hr.HrCLanguageFacadeRemote;
import power.ejb.hr.HrCNation;
import power.ejb.hr.HrCNationFacadeRemote;
import power.ejb.hr.HrCNativePlace;
import power.ejb.hr.HrCNativePlaceFacadeRemote;
import power.ejb.hr.HrCPolitics;
import power.ejb.hr.HrCPoliticsFacadeRemote;
import power.ejb.hr.HrCSchool;
import power.ejb.hr.HrCSchoolFacadeRemote;
import power.ejb.hr.HrCSpecialty;
import power.ejb.hr.HrCSpecialtyFacadeRemote;
import power.ejb.hr.HrCStudytype;
import power.ejb.hr.HrCStudytypeFacadeRemote;
import power.ejb.hr.HrCTechnologyGrade;
import power.ejb.hr.HrCTechnologyGradeFacadeRemote;
import power.ejb.hr.HrCWorkid;
import power.ejb.hr.HrCWorkidFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 基础表维护 Action
 * 
 * @author huangweijie
 * @version 1.0
 */
public class BaseTableMaintAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    
    /** remote */
    /** 称谓编码表 remote */
    private HrCAppellationFacadeRemote appRemote;
    /** 学位编码表 remote */
    private HrCDegreeFacadeRemote degRemote;
    /** 学历编码表 remote */
    private HrCEducationFacadeRemote eduRemote;
    /** 语种编码表 remote */
    private HrCLanguageFacadeRemote langRemote;
    /** 民族编码表 remote */
    private HrCNationFacadeRemote nationRemote;
    /** 籍贯编码表 remote */
    private HrCNativePlaceFacadeRemote nativePlaceRemote;
    /** 政治面貌表 remote */
    private HrCPoliticsFacadeRemote politicsRemote;
    /** 学校名称表 remote */
    private HrCSchoolFacadeRemote schoolRemote;
    /** 学习类别表 remote */
    private HrCStudytypeFacadeRemote studytypeRemote;
    /** 学习专业表 remote */
    private HrCSpecialtyFacadeRemote specialRemote;
    /** 技术等级表 remote */
    private HrCTechnologyGradeFacadeRemote techRemote;
    /** 员工身份表 remote */
    private HrCWorkidFacadeRemote workidRemote;
    /** 员工类别表 remote */
    private HrCEmpTypeFacadeRemote empTypeRemote;
    @EJB (beanName="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    
    
    

    /** 画面参数开始页 */
    public Long start;
    /** 画面参数页面最大值 */
    public Long limit;
    /** 操作的表名 */
    public String tableName;
    /** 通用类型实体 */
    public MyObj userObj;
    /** 要操作的记录ID */
    public Long recordId;
    /** 名字 */
    public String recordName;
    /** 检索码 */
    private String retrieveCode;
    /** 显示顺序 */
    private Long orderBy;
    
    // 13个表的名字
    public String TABLE1 = "称谓编码表";
    public String TABLE2 = "学位编码表";
    public String TABLE3 = "学历编码表";
    public String TABLE4 = "语种编码表";
    public String TABLE5 = "民族编码表";
    public String TABLE6 = "籍贯编码表";
    public String TABLE7 = "政治面貌表";
    public String TABLE8 = "学校名称表";
    public String TABLE9 = "学习类别表";
    public String TABLEa = "学习专业表";
    public String TABLEb = "技术等级表";
    public String TABLEc = "员工身份表";
    public String TABLEd = "员工类别表";
    
    public class MyObj implements java.io.Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        /** ID */
        private Long recordId;
        /** 名称 */
        private String recordName;
        /** 检索码 */
        private String retrieveCode;
        /** 显示顺序 */
        private Long orderBy;
        /**
         * @return the recordId
         */
        public Long getRecordId() {
            return recordId;
        }
        /**
         * @param recordId the recordId to set
         */
        public void setRecordId(Long recordId) {
            this.recordId = recordId;
        }
        /**
         * @return the recordName
         */
        public String getRecordName() {
            return recordName;
        }
        /**
         * @param recordName the recordName to set
         */
        public void setRecordName(String recordName) {
            this.recordName = recordName;
        }
        /**
         * @return the retrieveCode
         */
        public String getRetrieveCode() {
            return retrieveCode;
        }
        /**
         * @param retrieveCode the retrieveCode to set
         */
        public void setRetrieveCode(String retrieveCode) {
            this.retrieveCode = retrieveCode;
        }
        /**
         * @return the orderBy
         */
        public Long getOrderBy() {
            return orderBy;
        }
        /**
         * @param orderBy the orderBy to set
         */
        public void setOrderBy(Long orderBy) {
            this.orderBy = orderBy;
        }
        
    }
    
    /**
     * 初始化函数，用于实例化remote
     */
    public BaseTableMaintAction() {
        /** 称谓编码表 remote */
        appRemote = (HrCAppellationFacadeRemote) 
        factory.getFacadeRemote("HrCAppellationFacade");
        /** 学位编码表 remote */
        degRemote = (HrCDegreeFacadeRemote) 
        factory.getFacadeRemote("HrCDegreeFacade");
        /** 学历编码表 remote */
        eduRemote = (HrCEducationFacadeRemote) 
        factory.getFacadeRemote("HrCEducationFacade");
        /** 语种编码表 remote */
        langRemote = (HrCLanguageFacadeRemote) 
        factory.getFacadeRemote("HrCLanguageFacade");
        /** 民族编码表 remote */
        nationRemote = (HrCNationFacadeRemote) 
        factory.getFacadeRemote("HrCNationFacade");
        /** 籍贯编码表 remote */
        nativePlaceRemote = (HrCNativePlaceFacadeRemote) 
        factory.getFacadeRemote("HrCNativePlaceFacade");
        /** 政治面貌表 remote */
        politicsRemote = (HrCPoliticsFacadeRemote) 
        factory.getFacadeRemote("HrCPoliticsFacade");
        /** 学校编码表 remote */
        schoolRemote = (HrCSchoolFacadeRemote) 
        factory.getFacadeRemote("HrCSchoolFacade");
        /** 学习类别表 remote */
        studytypeRemote = (HrCStudytypeFacadeRemote) 
        factory.getFacadeRemote("HrCStudytypeFacade");
        /** 学习专业表 remote */
        specialRemote = (HrCSpecialtyFacadeRemote) 
        factory.getFacadeRemote("HrCSpecialtyFacade");
        /** 技术等级表 remote */
        techRemote = (HrCTechnologyGradeFacadeRemote) 
        factory.getFacadeRemote("HrCTechnologyGradeFacade");
        /** 员工身份表 remote */
        workidRemote = (HrCWorkidFacadeRemote) 
        factory.getFacadeRemote("HrCWorkidFacade");
        /** 员工类别表 remote */
        empTypeRemote = (HrCEmpTypeFacadeRemote) 
        factory.getFacadeRemote("HrCEmpTypeFacade");
    }
    
    /**
     * 查询基本表数据
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getRecordList() throws Exception {
        try {
            LogUtil.log("Action:查询基本表（员工档案）开始", Level.INFO, null);
            PageObject pobj = new PageObject();
            int intStart = Integer.parseInt(start.toString());
            int intLimit = Integer.parseInt(limit.toString());
            // 查询操作
            pobj = appRemote.getRecordList(tableName, 
                    employee.getEnterpriseCode(), 
                    intStart, intLimit);
            List<MyObj> mylist = new ArrayList<MyObj>();
            List list = pobj.getList();
            if (null != list && list.size() > 0) {
                // 根据表名，把各字段的值赋给通用实体
                if (TABLE1.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCAppellation o = (HrCAppellation)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getCallsCodeId());
                        record.setRecordName(o.getCallsName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询学位编码表
                } else if (TABLE2.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCDegree o = (HrCDegree)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getDegreeId());
                        record.setRecordName(o.getDegreeName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        // modified by liuyi 091123 表中无属性 已解决
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询学历编码表
                } else if (TABLE3.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCEducation o = (HrCEducation)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getEducationId());
                        record.setRecordName(o.getEducationName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        // modified by liuyi 091123 表中无属性 已解决
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询语种编码表
                } else if (TABLE4.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCLanguage o = (HrCLanguage)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getLanguageCodeId());
                        record.setRecordName(o.getLanguageName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询民族编码表
                } else if (TABLE5.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCNation o = (HrCNation)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getNationCodeId());
                        record.setRecordName(o.getNationName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询籍贯编码表
                } else if (TABLE6.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCNativePlace o = (HrCNativePlace)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getNativePlaceId());
                        record.setRecordName(o.getNativePlaceName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询政治面貌表
                } else if (TABLE7.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCPolitics o = (HrCPolitics)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getPoliticsId());
                        record.setRecordName(o.getPoliticsName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询学校编码表
                } else if (TABLE8.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCSchool o = (HrCSchool)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getSchoolCodeId());
                        record.setRecordName(o.getSchoolName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询学习类别表
                } else if (TABLE9.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCStudytype o = (HrCStudytype)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getStudyTypeCodeId());
                        record.setRecordName(o.getStudyType());
                        record.setRetrieveCode(o.getRetrieveCode());
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询学习专业表
                } else if (TABLEa.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCSpecialty o = (HrCSpecialty)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getSpecialtyCodeId());
                        record.setRecordName(o.getSpecialtyName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询技术等级表
                } else if (TABLEb.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCTechnologyGrade o = (HrCTechnologyGrade)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getTechnologyGradeId());
                        record.setRecordName(o.getTechnologyGradeName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        // modified by liuyi 091123 表中无属性 已解决
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询员工身份表
                } else if (TABLEc.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCWorkid o = (HrCWorkid)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getWorkId());
                        record.setRecordName(o.getWorkName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                    // 查询员工类别表
                } else if (TABLEd.equals(tableName)) {
                    for(int i = 0; i < list.size(); i++) {
                        HrCEmpType o = (HrCEmpType)list.get(i);
                        MyObj record = new MyObj();
                        record.setRecordId(o.getEmpTypeId());
                        record.setRecordName(o.getEmpTypeName());
                        record.setRetrieveCode(o.getRetrieveCode());
                        // modified by liuyi 091123 表中无属性 已解决
                        record.setOrderBy(o.getOrderBy());
                        mylist.add(record);
                    }
                }
                pobj.setList(mylist);
            }
            // 解析字符串
            String str = null;
            if(null == pobj.getList() || 0 == pobj.getList().size()) {
                str = "{\"list\":[],\"totalCount\":0}";
            } else {
                str = JSONUtil.serialize(pobj);
            }
            LogUtil.log("Action:查询基本表（员工档案）结束", Level.INFO, null);
            write(str);
        } catch (JSONException jsone) {
            LogUtil.log("Action:查询基本表（员工档案）失败", Level.SEVERE, jsone);
            write(Constants.DATA_FAILURE);
        } catch (SQLException e) {
            LogUtil.log("Action:查询基本表（员工档案）失败", Level.INFO, e);
            write(Constants.SQL_FAILURE);
        }
    }
    
    /**
     * 新增基本表记录
     */
    public void addBaseTableRecord() throws Exception {
        try {
            LogUtil.log("Action:新增基本表记录开始（" + tableName 
                    + "）", Level.INFO, null);
            // 根据表名，把各字段的值赋给通用实体
            if (TABLE1.equals(tableName)) {
                    HrCAppellation entity = new HrCAppellation();
                    // 设置名称
                    entity.setCallsName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    // 记录人
                    entity.setInsertby(employee.getWorkerCode());
                    // 记录日期
                    entity.setInsertdate(new Date());
                    appRemote.save(entity);
                // 查询学位编码表
            } else if (TABLE2.equals(tableName)) {
                    HrCDegree entity = new HrCDegree();
                    // 设置名称
                    entity.setDegreeName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                 // modified by liuyi 091123 表中无属性 已解决
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                 // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                 // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    degRemote.save(entity);
                // 查询学历编码表
            } else if (TABLE3.equals(tableName)) {
                    HrCEducation entity = new HrCEducation();
                    // 设置名称
                    entity.setEducationName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    eduRemote.save(entity);
                // 查询语种编码表
            } else if (TABLE4.equals(tableName)) {
                    HrCLanguage entity = new HrCLanguage();
                    // 设置名称
                    entity.setLanguageName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    // 记录人
                    entity.setInsertby(employee.getWorkerCode());
                    // 记录日期
                    entity.setInsertdate(new Date());
                    langRemote.save(entity);
                // 查询民族编码表
            } else if (TABLE5.equals(tableName)) {
                    HrCNation entity = new HrCNation();
                    // 设置名称
                    entity.setNationName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    // 记录人
                    entity.setInsertby(employee.getWorkerCode());
                    // 记录日期
                    entity.setInsertdate(new Date());
                    nationRemote.save(entity);
                // 查询籍贯编码表
            } else if (TABLE6.equals(tableName)) {
                    HrCNativePlace entity = new HrCNativePlace();
                    // 设置名称
                    entity.setNativePlaceName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    nativePlaceRemote.save(entity);
                // 查询政治面貌表
            } else if (TABLE7.equals(tableName)) {
                    HrCPolitics entity = new HrCPolitics();
                    // 设置名称
                    entity.setPoliticsName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    politicsRemote.save(entity);
                // 查询学校编码表
            } else if (TABLE8.equals(tableName)) {
                    HrCSchool entity = new HrCSchool();
                    // 设置名称
                    entity.setSchoolName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    // 记录人
                    entity.setInsertby(employee.getWorkerCode());
                    // 记录日期
                    entity.setInsertdate(new Date());
                    schoolRemote.save(entity);
                // 查询学习类别表
            } else if (TABLE9.equals(tableName)) {
                    HrCStudytype entity = new HrCStudytype();
                    // 设置名称
                    entity.setStudyType(recordName);
                    // 设置检索码  
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    studytypeRemote.save(entity);
                // 查询学习专业表
            } else if (TABLEa.equals(tableName)) {
                    HrCSpecialty entity = new HrCSpecialty();
                    // 设置名称
                    entity.setSpecialtyName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号  
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    // 记录人
                    entity.setInsertby(employee.getWorkerCode());
                    // 记录日期
                    entity.setInsertdate(new Date());
                    specialRemote.save(entity);
                // 查询技术等级表
            } else if (TABLEb.equals(tableName)) {
                    HrCTechnologyGrade entity = new HrCTechnologyGrade();
                    // 设置名称
                    entity.setTechnologyGradeName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    techRemote.save(entity);
                // 查询员工身份表
            } else if (TABLEc.equals(tableName)) {
                    HrCWorkid entity = new HrCWorkid();
                    // 设置名称
                    entity.setWorkName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    workidRemote.save(entity);
                // 查询员工类别表
            } else if (TABLEd.equals(tableName)) {
                    HrCEmpType entity = new HrCEmpType();
                    // 设置名称
                    entity.setEmpTypeName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setOrderBy(orderBy);
                    // 企业编码
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setEnterpriseCode(employee.getEnterpriseCode());
                    // 是否使用
                    entity.setIsUse(Constants.IS_USE_Y);
                    // 上次修改人
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    empTypeRemote.save(entity);
            }
            LogUtil.log("Action:新增基本表记录结束（" + tableName 
                    + "）", Level.INFO, null);
            write(Constants.ADD_SUCCESS);
        } catch (Exception e) {
            LogUtil.log("Action:新增基本表记录失败（" + tableName 
                    + "）", Level.INFO, e);
            write(Constants.SQL_FAILURE);
        }
    }
    
    /**
     * 修改基本表记录
     */
    public void modifyBaseTableRecord() throws Exception {
        try {
            LogUtil.log("Action:修改基本表记录开始（" + tableName 
                    + "）", Level.INFO, null);
            // 根据表名，把各字段的值赋给具体实体
            if (TABLE1.equals(tableName)) {
                    HrCAppellation entity = new HrCAppellation();
                    // 通过ID得到实体
                    entity = appRemote.findById(recordId);
                    // 设置名称
                    entity.setCallsName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    appRemote.update(entity);
                // 查询学位编码表
            } else if (TABLE2.equals(tableName)) {
                    HrCDegree entity = new HrCDegree();
                    // 通过ID得到实体
                    entity = degRemote.findById(recordId);
                    // 设置名称
                    entity.setDegreeName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                 // modified by liuyi 091123 表中无属性 已解决
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                 // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                 // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    degRemote.update(entity);
                // 查询学历编码表
            } else if (TABLE3.equals(tableName)) {
                    HrCEducation entity = new HrCEducation();
                    // 通过ID得到实体
                    entity = eduRemote.findById(recordId);
                    // 设置名称
                    entity.setEducationName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    eduRemote.update(entity);
                // 查询语种编码表
            } else if (TABLE4.equals(tableName)) {
                    HrCLanguage entity = new HrCLanguage();
                    // 通过ID得到实体
                    entity = langRemote.findById(recordId);
                    // 设置名称
                    entity.setLanguageName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    langRemote.update(entity);
                // 查询民族编码表
            } else if (TABLE5.equals(tableName)) {
                    HrCNation entity = new HrCNation();
                    // 通过ID得到实体
                    entity = nationRemote.findById(recordId);
                    // 设置名称
                    entity.setNationName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    nationRemote.update(entity);
                // 查询籍贯编码表
            } else if (TABLE6.equals(tableName)) {
                    HrCNativePlace entity = new HrCNativePlace();
                    // 通过ID得到实体
                    entity = nativePlaceRemote.findById(recordId);
                    // 设置名称
                    entity.setNativePlaceName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    nativePlaceRemote.update(entity);
                // 查询政治面貌表
            } else if (TABLE7.equals(tableName)) {
                    HrCPolitics entity = new HrCPolitics();
                    // 通过ID得到实体
                    entity = politicsRemote.findById(recordId);
                    // 设置名称
                    entity.setPoliticsName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    politicsRemote.update(entity);
                // 查询学校编码表
            } else if (TABLE8.equals(tableName)) {
                    HrCSchool entity = new HrCSchool();
                    // 通过ID得到实体
                    entity = schoolRemote.findById(recordId);
                    // 设置名称
                    entity.setSchoolName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    schoolRemote.update(entity);
                // 查询学习类别表
            } else if (TABLE9.equals(tableName)) {
                    HrCStudytype entity = new HrCStudytype();
                    // 通过ID得到实体
                    entity = studytypeRemote.findById(recordId);
                    // 设置名称
                    entity.setStudyType(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    studytypeRemote.update(entity);
                // 查询学习专业表
            } else if (TABLEa.equals(tableName)) {
                    HrCSpecialty entity = new HrCSpecialty();
                    // 通过ID得到实体
                    entity = specialRemote.findById(recordId);
                    // 设置名称
                    entity.setSpecialtyName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    specialRemote.update(entity);
                // 查询技术等级表
            } else if (TABLEb.equals(tableName)) {
                    HrCTechnologyGrade entity = new HrCTechnologyGrade();
                    // 通过ID得到实体
                    entity = techRemote.findById(recordId);
                    // 设置名称
                    entity.setTechnologyGradeName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    techRemote.update(entity);
                // 查询员工身份表
            } else if (TABLEc.equals(tableName)) {
                    HrCWorkid entity = new HrCWorkid();
                    // 通过ID得到实体
                    entity = workidRemote.findById(recordId);
                    // 设置名称
                    entity.setWorkName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    workidRemote.update(entity);
                // 查询员工类别表
            } else if (TABLEd.equals(tableName)) {
                    HrCEmpType entity = new HrCEmpType();
                    // 通过ID得到实体
                    entity = empTypeRemote.findById(recordId);
                    // 设置名称
                    entity.setEmpTypeName(recordName);
                    // 设置检索码
                    entity.setRetrieveCode(retrieveCode);
                    // 排序号
                 // modified by liuyi 091123 表中无属性 已解决
                    entity.setOrderBy(orderBy);
                    // 上次修改人
                 // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                 // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    empTypeRemote.update(entity);
            }
            LogUtil.log("Action:修改基本表记录结束（" + tableName 
                    + "）", Level.INFO, null);
            write(Constants.ADD_SUCCESS);
        } catch (Exception e) {
            LogUtil.log("Action:修改基本表记录失败（" + tableName 
                    + "）", Level.INFO, e);
            write(Constants.SQL_FAILURE);
        }
    }
    
    /**
     * 删除基本表记录
     */
    public void deleteBaseTableRecord() throws Exception {
        try {
            LogUtil.log("Action:删除基本表记录开始（" + tableName 
                    + "）", Level.INFO, null);
            // 根据表名，逻辑删除具体实体
            if (TABLE1.equals(tableName)) {
                    HrCAppellation entity = new HrCAppellation();
                    // 通过ID得到实体
                    entity = appRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    appRemote.update(entity);
                // 查询学位编码表
            } else if (TABLE2.equals(tableName)) {
                    HrCDegree entity = new HrCDegree();
                    // 通过ID得到实体
                    entity = degRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
//                     modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    degRemote.update(entity);
                // 查询学历编码表
            } else if (TABLE3.equals(tableName)) {
                    HrCEducation entity = new HrCEducation();
                    // 通过ID得到实体
                    entity = eduRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    eduRemote.update(entity);
                // 查询语种编码表
            } else if (TABLE4.equals(tableName)) {
                    HrCLanguage entity = new HrCLanguage();
                    // 通过ID得到实体
                    entity = langRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    langRemote.update(entity);
                // 查询民族编码表
            } else if (TABLE5.equals(tableName)) {
                    HrCNation entity = new HrCNation();
                    // 通过ID得到实体
                    entity = nationRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    nationRemote.update(entity);
                // 查询籍贯编码表
            } else if (TABLE6.equals(tableName)) {
                    HrCNativePlace entity = new HrCNativePlace();
                    // 通过ID得到实体
                    entity = nativePlaceRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    nativePlaceRemote.update(entity);
                // 查询政治面貌表
            } else if (TABLE7.equals(tableName)) {
                    HrCPolitics entity = new HrCPolitics();
                    // 通过ID得到实体
                    entity = politicsRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    politicsRemote.update(entity);
                // 查询学校编码表
            } else if (TABLE8.equals(tableName)) {
                    HrCSchool entity = new HrCSchool();
                    // 通过ID得到实体
                    entity = schoolRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    schoolRemote.update(entity);
                // 查询学习类别表
            } else if (TABLE9.equals(tableName)) {
                    HrCStudytype entity = new HrCStudytype();
                    // 通过ID得到实体
                    entity = studytypeRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    studytypeRemote.update(entity);
                // 查询学习专业表
            } else if (TABLEa.equals(tableName)) {
                    HrCSpecialty entity = new HrCSpecialty();
                    // 通过ID得到实体
                    entity = specialRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    specialRemote.update(entity);
                // 查询技术等级表
            } else if (TABLEb.equals(tableName)) {
                    HrCTechnologyGrade entity = new HrCTechnologyGrade();
                    // 通过ID得到实体
                    entity = techRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    techRemote.update(entity);
                // 查询员工身份表
            } else if (TABLEc.equals(tableName)) {
                    HrCWorkid entity = new HrCWorkid();
                    // 通过ID得到实体
                    entity = workidRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                    entity.setLastModifiedDate(new Date());
                    workidRemote.update(entity);
                // 查询员工类别表
            } else if (TABLEd.equals(tableName)) {
                    HrCEmpType entity = new HrCEmpType();
                    // 通过ID得到实体
                    entity = empTypeRemote.findById(recordId);
                    // 设置是否使用为否
                    entity.setIsUse(Constants.IS_USE_N);
                    // 上次修改人
                 // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedBy(employee.getWorkerCode());
                    // 上次修改时间
                 // modified by liuyi 091123 表中无属性 已解决
                    entity.setLastModifiedDate(new Date());
                    empTypeRemote.update(entity);
            }
            LogUtil.log("Action:删除基本表记录结束（" + tableName 
                    + "）", Level.INFO, null);
            write(Constants.ADD_SUCCESS);
        } catch (Exception e) {
            LogUtil.log("Action:删除基本表记录失败（" + tableName 
                    + "）", Level.INFO, e);
            write(Constants.SQL_FAILURE);
        }
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the userObj
     */
    public MyObj getUserObj() {
        return userObj;
    }

    /**
     * @param userObj the userObj to set
     */
    public void setUserObj(MyObj userObj) {
        this.userObj = userObj;
    }

    /**
     * @return the recordId
     */
    public Long getRecordId() {
        return recordId;
    }

    /**
     * @param recordId the recordId to set
     */
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    /**
     * @return the recordName
     */
    public String getRecordName() {
        return recordName;
    }

    /**
     * @param recordName the recordName to set
     */
    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    /**
     * @return the retrieveCode
     */
    public String getRetrieveCode() {
        return retrieveCode;
    }

    /**
     * @param retrieveCode the retrieveCode to set
     */
    public void setRetrieveCode(String retrieveCode) {
        this.retrieveCode = retrieveCode;
    }

    /**
     * @return the orderBy
     */
    public Long getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(Long orderBy) {
        this.orderBy = orderBy;
    }
}
