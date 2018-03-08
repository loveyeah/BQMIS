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
    // 行政管理 追加开始
    /**
     * 订餐类别：早餐
     */
    public static final String DINGCAN_ZC = "1";
    /**
     * 订餐类别：中餐
     */
    public static final String DINGCAN_Z = "2";
    /**
     * 订餐类别：晚餐
     */
    public static final String DINGCAN_W = "3";
    /**
     * 订餐类别：夜宵
     */
    public static final String DINGCAN_Y = "4";
    /**
     * 人员类别：运行人员
     */
    public static final String RENYUAN_Y = "1";
    /**
     * 人员类别：非运行人员
     */
    public static final String RENYUAN_F = "2";
    // 行政管理 追加结束
 }
