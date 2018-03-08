/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.comm;

/**
 * 共通常量类
 * 
 * @author pbao
 * @version 1.0
 */
public class Constants {
	
	/**
     * 企业编码
     */
    public static final String ENTERPRISE_CODE = "hfdc";
    
    /**
     * 增加成功信息
     */
    public static final String ADD_SUCCESS = "{success:true,msg:'&nbsp&nbsp&nbsp增加成功！&nbsp&nbsp&nbsp'}";
    
    /**
     * 增加失败:编码重复信息
     */
    public static final String ADD_CODE_FAILURE = "{success:true,msg:'增加失败:编码重复！  '}";
    
    /**
     * 增加失败:名称重复信息
     */
    public static final String ADD_FAILURE = "{success:true,msg:'增加失败:名称重复！  '}";
    
    /**
     * 修改成功信息
     */
    public static final String MODIFY_SUCCESS = "{success:true,msg:'&nbsp&nbsp&nbsp修改成功！&nbsp&nbsp&nbsp'}";
    
    /**
     * 修改失败:编码重复信息
     */
    public static final String MODIFY_CODE_FAILURE = "{success:true,msg:'修改失败:编码重复！  '}";
    
    
    /**
     * 修改失败:名称重复信息
     */
    public static final String MODIFY_FAILURE = "{success:true,msg:'修改失败:名称重复！  '}";
    
    /**
     * 修改失败:标示牌编号重复信息
     */
    public static final String MODIFY_FAILURE_CARD = "{success:true,msg:'修改失败:标识牌编号重复！'}";
    
    /**
     * 签字成功
     */
    public static final String SIGN_SUCCESS = "{success:true,msg:'&nbsp&nbsp&nbsp审批成功&nbsp&nbsp&nbsp'}";
    
    /**
     * 删除失败信息:出现未知错误
     */
    public static final String DELETE_FAILURE = "{success:true,msg:'删除失败:出现未知错误！'}";
	
    /**
     * 删除成功信息
     */
    public static final String DELETE_SUCCESS = "{success:true,msg:'&nbsp&nbsp&nbsp删除成功！&nbsp&nbsp&nbsp'}";
    
    /**
     * IS_USE为Y
     */
    public static final String IS_USE_Y = "Y";
    
    /**
     * IS_USE为N
     */
    public static final String IS_USE_N = "N";
    
    /**
     * IS_RETURN为Y
     */
    public static final String IS_RETURN_Y = "Y";
    
    /**
     * 取出所有数据
     */
    public static final String ALL_DATA = "%";
    
    /**
     * 空字符串
     */
    public static final String BLANK_STRING = "";
    
    /**
     * 所有
     */
    public static final String ALL_SELECT = "所有";

    /**
     * 公用工作票类型名称
     */
    public static final String PUBLIC_TYPE = "公用";

    /**
     * 公用工作票类型编码
     */
    public static final String PUBLIC_TYPE_CODE = "C";
    
    /**
     * 跟节点标号 "-1"
     */
    public static final String ROOT_NODE = "-1";
    
    /**
     * 标识: Y
     */
    public static final String FLAG_Y = "Y";
    
    /**
     * 标识: N
     */
    public static final String FLAG_N = "N";
    /** 
     * 已打印标志 
     */
    public static final String PRT = "PRT";
	/** 
	 * 已盘点标志 
	 */
    public static final String CMT = "CMT";
	/** 
	 * 盘点事务编码 
	 */
    public static final String BKCK_TRANSCODE = "A";
    /**
     * 正在结账
     */
    public static final String ACCOUNTING = "{success:true,msg:'on'}";
    /**
     * 数字 ：0 
     */
    public static final String NUMBER_ZERO = "0";
    /**
     * 结算类型：月结算
     */
    public static final String BALANCE_MONTH = "M";
    /**
     * 结账状态：正在结账
     */
    public static final String BALANCE_ON = "ON";
    /**
     * 结账状态：没在结账
     */
    public static final String BALANCE_OK = "OK";
    /**
     * 排他处理 ： 数据正在被使用
     */
    public static final String DATA_USING = "{success:true,msg:'U'}";
    /**
     * 日期重叠判断 ： 供应商的同一种物料在相同时段内有不同报价
     */
    public static final String DATE_REPEAT = "{success:true,msg:'R'}";
    /**
     * 结账类型 ： 重新结算上次
     */
    public static final String BALANCE_TYPEA = "A";
    /**
     * 结账类型 ： 结算本月
     */
    public static final String BALANCE_TYPEB = "B";
    
    // 行政管理 追加开始
        /**
     * 上传成功信息
     */
    public static final String UPLOAD_SUCCESS = "{success:true,msg:'&nbsp&nbsp&nbsp上传成功！&nbsp&nbsp&nbsp'}";
    
    /**
     * 操作DB失败
     */
    public static final String SQL_FAILURE = "{success:true,msg:'SQL'}";
    
    /**
     * 操作文件失败
     */
    public static final String IO_FAILURE = "{success:true,msg:'IO'}";
    
    /**
     * 数据格式化失败
     */
    public static final String DATA_FAILURE = "{success:true,msg:'DATA'}";
    
    /**
     * 上传文件地址错误
     */
    public static final String FILE_NOT_EXIST = "{success:true,msg:'FILE'}";

    /**
     * 上报状态：1（已上报）
     */
    public static final String REPORT_STATUS_1 = "1";
    
    public static final String ADD_DRIVERCODE_FAILURE = "{success:true,msg:'DCF'}";
    /* 追加开始 追加人：柴浩 */
    /**
     * 车牌号重复
     */
    public static final String ADD_CARNO_FAILURE = "{success:true,msg:'CNF'}";
    /* 追加结束 追加人：柴浩 */
    // 行政管理 追加结束
 }
