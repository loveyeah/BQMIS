package power.web.birt.constant;

/**
 * 共通常量类
 * 
 * @author zhaozhijie
 * @version 1.0
 */
public class Constant {

	/**
     * 一行成员数
     */
    public static final int LineMemberCount = 30;

	/**
     * 两行成员数
     */
    public static final int TLineMemberCount = 50;

	/**
     * 热控成员一行字数
     */
    public static final int ControlMemLCount = 26;
    public static final int safetyActionLCount=27;

	/**
     * 热控内容一行字数
     */
    public static final int ControlTentLCount = 49;
    /**
     * 热机内容一行字数
     */
    public static final int MACHINE_LINE_MAX = 43;


	/**
     * 日期年截取数
     */
    public static final int DateYearNum = 4;

	/**
     * 日期月截取开始数
     */
    public static final int DateMonthSNum = 5;

	/**
     * 日期月截取结束数
     */
    public static final int DateMonthFNum = 7;

	/**
     * 日期日截取开始数
     */
    public static final int DateDaySNum = 8;

	/**
     * 日期日截取结束数
     */
    public static final int DateDayFNum = 10;

	/**
     * 日期时截取开始数
     */
    public static final int DateHourSNum = 11;

	/**
     * 日期时截取结束数
     */
    public static final int DateHourFNum = 13;

	/**
     * 日期分截取开始数
     */
    public static final int DateMintueSNum = 14;

	/**
     * 日期分截取结束数
     */
    public static final int DateMintueFNum = 16;
    

    /**
     * 日期的长度
     */
    public static final int DateLength = 10;

    /**
     * √
     */
    public static final String Right = "√";
    /**
     * ×
     */
    public static final String HAVE_NOT="×";
    
    /**
     * 动火票级别标识 一级
     */
    public static final String FIRE_LEVEL_1="1";    
    
    /**
     * 动火票级别标识 二级
     */
    public static final String FIRE_LEVEL_2="2";    
    
    /**
     * 操作票 执行情况 未执行status
     */
    public static final String UNDO_CODE="0";    
    
    /**
     * 操作票 执行情况 已执行status
     */
    public static final String DONE_CODE="1";
    
    /**
     * 数据换行符（db取出）
     */
    public static final String DB_CHANGE_LINE="\r\n";
    
    /**
     * 数据换行符（db取出2）
     */
    public static final String DB_CHANGE_LINE2="\n";
    
    /**
     * 数据换行符（报表表现）
     */
    public static final String HTML_CHANGE_LINE="<br>";

    /**
     * 热力机械安措内容第一条
     */
    public static final String MACHINE_SAFETY_ONE = "31";

    /**
     * 热力机械安措内容第二条
     */
    public static final String MACHINE_SAFETY_Two = "32";

    /**
     * 热力机械安措内容第三条
     */
    public static final String MACHINE_SAFETY_Three = "33";

    /**
     * 热力机械安措内容第四条
     */
    public static final String MACHINE_SAFETY_Four = "34";

    /**
     * 热力机械安措内容第五条
     */
    public static final String MACHINE_SAFETY_Five = "35";
    
    /**
     * 热力机械补充安措内容
     */
    public static final String MACHINE_SAFETY_REPAIR = "36";

    /**
     * 补充安措内容
     */
    public static final String SAFETY_REPAIR = "55";

    /**
     * 热控安措内容第一条
     */
    public static final String CONTROL_SAFETY_ONE = "51";

    /**
     * 热控安措内容第二条
     */
    public static final String CONTROL_SAFETY_Two = "52";

    /**
     * 热控安措内容第三条
     */
    public static final String CONTROL_SAFETY_Three = "53";

    /**
     * 热控安措内容第四条
     */
    public static final String CONTROL_SAFETY_Four = "54";

    /**
     * 已签发
     */
    public static final String STATUS_SIGN = "3";

    /**
     * 已接票
     */
    public static final String STATUS_ACCEPT = "4";

    /**
     * 已批准
     */
    public static final String STATUS_APPROVE = "5";

    /**
     * 已许可
     */
    public static final String STATUS_ADMISSION = "7";
    
    /**
     * 已结束
     */
    public static final String STATUS_END = "8";

    /**
     * 消防部门负责人已审批
     */
    public static final String STATUS_FIRECHARGE = "10";

    /**
     * 安监专工已审批
     */
    public static final String STATUS_SAFETYCHARGE = "11";

    /**
     * 厂领导已审批
     */
    public static final String STATUS_COMPANYMAN = "12";

    /**
     * 动火部门负责人已审批
     */
    public static final String STATUS_FIREADD_CHARGE = "13";

    /**
     * 已交回
     */
    public static final String STATUS_DEAL_BACK = "15";

    /**
     * 已恢复
     */
    public static final String STATUS_RESUME = "16";

    /**
     * 变更
     */
    public static final String STATUS_CHANGE = "changecharge";
    

    /**
     * 延期
     */
    public static final String STATUS_DELAY = "delay";
    /**
     * <BR>的长度
     */
    public static final int BR_LENGTH = 4;
    /**
     * A3纸50%宽度能显示Large字体的个数
     */
    public static final int A3_FIFTY = 30;
    /**
     * A3纸35%宽度能显示Large字体的个数（电气一 已装接地线一行显示数）
     */
    public static final int EXCUTE_11_LINE_MAX = 21;
    /**
     * 见附页
     */
    public static final String MEETFUYE = "见附页";
    /**
     * 安全措施1CODE
     */
    public static final String ELECTRIONE_SAFEONE_CODE = "10";
    /**
     * 安全措施1
     */
    public static final String ELECTIRONE_SAFEONE = "应拉断路器及隔离开关，包括已拉开的断路器及隔离开关（注明编号）及保险:";
    /**
     * 安全措施2CODE
     */
    public static final String ELECTRIONE_SAFETWO_CODE = "11";
    /**
     * 安全措施2
     */
    public static final String ELECTIRONE_SAFETWO = "应装接地线（注明确实地点）及应合接地闸刀：装设地点";
    /**
     * 安全措施3CODE
     */
    public static final String ELECTRIONE_SAFETHREE_CODE = "12";
    /**
     * 安全措施3
     */
    public static final String ELECTIRONE_SAFETHREE = "应设遮拦，应挂标示牌：";
    /**
     * 安全措施4CODE
     */
    public static final String ELECTRIONE_SAFEFOUR_CODE = "13";
    /**
     * 安全措施4
     */
    public static final String ELECTIRONE_SAFEFOUR = "工作地点保留带电部分和补充措施：";
    /**
     * 执行措施1
     */
    public static final String ELECTIRONE_EXECUONE = "已拉断路器及隔离开关（注明编号）及保险：";
    /**
     * 执行措施2
     */
    public static final String ELECTIRONE_EXECUTWO = "已装接地线（注明接地线编号及装设地点）及应合接地闸刀：";
    /**
     * 执行措施3
     */
    public static final String ELECTIRONE_EXECUTHREE = "已设遮拦，已挂标示牌（注明地点）：";
    /**
     * 热机措施1
     */
    public static final String ENERGMACHE_SAFEONE = "5、（1）应拉开下列开关、刀闸和保险等，并在操作把手（按钮）上设置“禁止合闸，有人工作”警告牌";
    /**
     * 热机措施2
     */
    public static final String ENERGMACHE_SAFETWO = "5、（2）应开启下列阀门、挡板（闸板），使燃烧室、管道、容器内余汽、水、油、灰、烟排放尽，并将温度降至规程规定值：";
    /**
     * 热机措施3
     */
    public static final String ENERGMACHE_SAFETHREE = "5、（3）应关闭下列截门、挡板（闸板），并挂“禁止操作”警告牌：";
    /**
     * 热机措施4
     */
    public static final String ENERGMACHE_SAFEFOUR = "5、（4）应将下列截门停电、加锁：";
    /**
     * 热机措施5
     */
    public static final String ENERGMACHE_SAFEFIVE = "5、（5）其它安全措施（包括拆开连接法兰或加装堵板）：";
    /**
     * 热机补充措施
     */
    public static final String ENERGMACHE_EXECU = "运行值班人员补充安全措施：";
    /**
     * 热力措施1
     */
    public static final String ENERGCONTROL_SAFEONE = "A、热控安全措施";
    /**
     * 热力措施2
     */
    public static final String ENERGCONTROL_SAFETWO = "（1）热控安全措施";
    /**
     * 热力措施3
     */
    public static final String ENERGCONTROL_SAFETHREE = "（2）退出以下控制、检测系统";
    /**
     * 热力措施4
     */
    public static final String ENERGCONTROL_SAFEFOUR = "B、热力安全措施（阀门、挡板等）应挂标示牌";
    /**
     * 热力措施5
     */
    public static final String ENERGCONTROL_SAFEFIVE = "C、电气安全措施（电源开关、刀闸、保险等）应挂标示牌";
    
    /**
     * 动火 运行应采取的安全措施：
     */
    public static final String SECURITY_TITLE_41 = "运行应采取的安全措施：";
    public static final String SAFETY_CODE_41 = "41";    
    
    /**
     * 动火 检修应采取的安全措施：
     */
    public static final String SECURITY_TITLE_42 = "检修应采取的安全措施：";
    public static final String SAFETY_CODE_42 = "42";
    
    /**
     * 动火 消防队应采取的安全措施：
     */
    public static final String SECURITY_TITLE_43 = "消防队应采取的安全措施：";
    public static final String SAFETY_CODE_43 = "43";
    
    /**
     * 动火 运行补充措施：
     */
    public static final String SECURITY_TITLE_44 = "运行补充措施：";
    public static final String SAFETY_CODE_44 = "44";
    
    /**
     * 动火 检修补充措施：
     */
    public static final String SECURITY_TITLE_45 = "检修补充措施：";
    public static final String SAFETY_CODE_45 = "45";
    
    /**
     * 动火 检修补充措施：
     */
    public static final String SECURITY_TITLE_46 = "消防队补充措施：";
    public static final String SAFETY_CODE_46 = "46";
    
    /**
     * 动火二级 应采取的安全措施 最大显示行数
     */ 
    public static final int SAFETY_NEED_MAX_ROW_COUNT_TWO = 12;
    /**
     * 动火二级 补充措施 最大显示行数
     */     
    public static final int SAFETY_SUPPLY_MAX_ROW_COUNT_TWO  = 9;
    public static final int SAFETY_Content_LINE_WORD_COUNT_TWO  = 30;
    /**
     * 动火一级 应采取的安全措施 最大显示行数
     */ 
    public static final int SAFETY_NEED_MAX_ROW_COUNT_ONE = 11;
    /**
     * 动火一级 补充措施 最大显示行数
     */     
    public static final int SAFETY_SUPPLY_MAX_ROW_COUNT_ONE  = 7;
    public static final int SAFETY_Content_LINE_WORD_COUNT_ONE  = 20;
    /**
     * 事务作用名称
     */
    public static final String RECEIVEGOODS_BUISENESSNAME = "到货登记";
    /**
     * 事务作用码
     */
    public static final String RECEIVEGOODS_BUISENESSCODE = "N";
    /**
     * 8point字体12%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY12 = 22;
    /**
     * 8point字体4%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY4 = 6;
    /**
     * 8point字体5%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY5 = 8;
    /**
     * 8point字体6%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY6 = 10;
    /**
     * 8point字体7%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY7 = 12;
    /**
     * 8point字体8%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY8 = 14;
    /**
     * 8point字体9%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY9 = 16;
    /**
     * 8point字体10%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY10 = 18;
    /**
     * 8point字体11%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY11 = 20;
    /**
     * 8point字体13%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY13 = 24;
    /**
     * 8point字体14%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY14 = 26;
    /**
     * 8point字体15%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY15 = 28;
    /**
     * 8point字体16%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY16 = 30;
    /**
     * 8point字体17%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY17 = 32;
    /**
     * 8point字体19%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY19 = 36;
    /**
     * 8point字体24%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY24 = 46;
    /**
     * 8point字体25%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY25 = 48;
    /**
     * 8point字体28%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY28 = 54;
    /**
     * 8point字体32%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY32 = 62;
    /**
     * 8point字体30%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY30 = 56;
    /**
     * 8point字体21%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY21 = 38;
    /**
     * 年
     */
    public static final String YEAR = "年";
    /**
     * 月
     */
    public static final String MONTH = "月";
    /**
     * 日
     */
    public static final String DAY = "日";
    /**
     * 时
     */
    public static final String HOUR = "时";
    /**
     * 分
     */
    public static final String MINUTE = "分";
    /**
     * 电气一工作内容每行显示最大文字数
     */
    public static final int WORK_CONTENT_LINE_MAX = 55;
    /**
     * 电气二工作内容每行显示最大文字数
     */
    public static final int WORK_CONTENT_LINE_MAX_TWO = 60;
    
    
    /**
     * 工作条件(停电或不停电)：
     */
    public static final String SECURITY_TITLE_21 = "工作条件(停电或不停电)：";
    public static final String SAFETY_CODE_21 = "21";   
    /**
     * 注意事项(安全措施)：
     */
    public static final String SECURITY_TITLE_22 = "注意事项(安全措施)：";
    public static final String SAFETY_CODE_22 = "22";    
    public static final String EXCUTE_TITLE="执行情况";
    
    /**
     * 电气二种票安措内容每行显示最大文字数：
     */
    public static final int ELECTWO_SAFETY_LINE_MAX = 50;
    /**
     * 动火票工作内容显示最大行数：
     */
    public static final int FIRE_CONTENT_COUNT_MAX=11;
    /**
     * 供应商报表(较大)最大文字数：
     */
    public static final int SUPPLIER_BASE_LINE_MAX_1 = 116;
    
    /**
     * 供应商报表(较小)最大文字数：
     */
    public static final int SUPPLIER_BASE_LINE_MAX_2 = 43;
    
    // 行政管理 追加开始
    /**
     * 8point字体32%长度能显示字符数(纵向)
     */
    public static final int EIGHTPOINTPERSETYPORTRAIT32 = 46;
    /**
     * 8point字体21%长度能显示字符数(纵向)
     */
    public static final int EIGHTPOINTPERSETYPORTRAIT21= 26;
    /**
     * 8point字体20%长度能显示字符数
     */
    public static final int EIGHTPOINTPERSETY20 = 38;
    // 行政管理 追加结束

    /**-------------------------------------------------
     *人事添加 add by liuyi 091026
     *--------------------------------------------------
     */
    /**
     * 空格字符
     */
    public static final String BLANK = " ";
    /**
     *&
     */
    public static final String MARK_AND = "&";
    
    /**
     *&amp;
     */
    public static final String MARK_AND_HTML = "&amp;";
    
    /**
     *<
     */
    public static final String MARK_LT= "<";
    
    /**
     *&lt;
     */
    public static final String MARK_LT_HTML = "&lt;";
    /**
     *>
     */
    public static final String MARK_GT = ">";
    
    /**
     *&gt;
     */
    public static final String MARK_GT_HTML = "&gt;";
    /**
     *&nbsp;
     */
    public static final String MARK_BLACK = "&nbsp;";
}
