/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.monthbalance.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.InvCTransaction;
import power.ejb.resource.InvCTransactionFacadeRemote;
import power.ejb.resource.InvJBalance;
import power.ejb.resource.InvJBalanceFacadeRemote;
import power.ejb.resource.InvJLocation;
import power.ejb.resource.InvJLocationFacadeRemote;
import power.ejb.resource.InvJLot;
import power.ejb.resource.InvJLotFacadeRemote;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJTransactionHisFacadeRemote;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.InvJWarehouseFacadeRemote;
import power.ejb.resource.business.MonthBalance;
import power.ejb.resource.form.MonthBalanceInfo;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 库存月结 Action
 * @author huangweijie
 */
public class MonthBalanceAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    /** 仓库月结remote */
    private MonthBalance remote;
    /** 结算主表remote */
    private InvJBalanceFacadeRemote balanceRemote;
    /** 事务历史表remote */
    private InvJTransactionHisFacadeRemote transRemote;
    /** 批号物料记录表bean */
    private InvJLotFacadeRemote lotRemote;
    /** 库位物料记录表bean */
    private InvJLocationFacadeRemote locationRemote;
    /** 库存物料记录表remote */
    private InvJWarehouseFacadeRemote warehouseRemote;
    
    private InvCTransactionFacadeRemote invCTransactionRemote;
    
    private InvCMaterialFacadeRemote invCMaterialFacadeRemote;
    
    /** 结账年月份 */
    private Long yearMonth;
    /** 画面结账年月份 */
    private Long userYearMonth;
    /** 结账起始流水号 */
    private Long transHisMinid;
    /** 结账信息 */
    private String e_message = "";
    /** 结账类型（A：重新结算上次；B：结算本月） */
    private String balanceType = "";
    
    /** action构造函数，用于初始化remote */
    public MonthBalanceAction() {
        remote = (MonthBalance) factory.getFacadeRemote("MonthBalanceImp");
        balanceRemote = (InvJBalanceFacadeRemote) factory.getFacadeRemote("InvJBalanceFacade");
        transRemote = (InvJTransactionHisFacadeRemote) factory.getFacadeRemote("InvJTransactionHisFacade");
        lotRemote = (InvJLotFacadeRemote) factory.getFacadeRemote("InvJLotFacade");
        locationRemote = (InvJLocationFacadeRemote) factory.getFacadeRemote("InvJLocationFacade");
        warehouseRemote = (InvJWarehouseFacadeRemote) factory.getFacadeRemote("InvJWarehouseFacade");
        invCTransactionRemote = (InvCTransactionFacadeRemote) factory.getFacadeRemote("InvCTransactionFacade");
        invCMaterialFacadeRemote = (InvCMaterialFacadeRemote) factory.getFacadeRemote("InvCMaterialFacade");
    }
    
    /**
     * 检索结算主表中最新记录的结账年月份
     */
    @SuppressWarnings("deprecation")
    public void getLatestBalance() {
        InvJBalance balance = new InvJBalance();
        // 获得最近的一次结算记录
        try {
        	Long latestId = balanceRemote.getLatestId(employee.getEnterpriseCode());
        	if (null != latestId) {
        		balance = balanceRemote.findById(latestId);
        	} else {
        		balance = null;
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        	write("{success:true,msg:'sql'}");
        }
        // 如果存在
        if (null != balance) {
            yearMonth = balance.getBalanceYearMonth();
        // 如果不存在（系统的第一次结算），则取系统时间的当年当月来结算
        } else {
            Date datetemp = new Date();
            int year = datetemp.getYear() + 1900;
            int month = datetemp.getMonth() + 1;
            int t = year*100 + month;
            yearMonth = Long.parseLong(String.valueOf(t));
        }
        write("{success:true,msg:'" + yearMonth + "'}");
    }
    /**
     * check画面年月份和结算主表.年月份
     */
    @SuppressWarnings("deprecation")
    public void checkYearMonth() {
        InvJBalance balance = new InvJBalance();
        try {
        	Long latestId = balanceRemote.getLatestId(employee.getEnterpriseCode());
        	if (null != latestId) {
        		balance = balanceRemote.findById(latestId);
        	} else {
        		balance = null;
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        	write("{success:true,msg:'sql'}");
        }
        // 如果存在
        if (null != balance) {
            yearMonth = balance.getBalanceYearMonth();
            transHisMinid = balance.getTransHisMinid();
        // 如果不存在（系统的第一次结算），则取系统时间的当年当月来结算
        } else {
            Date datetemp = new Date();
            int year = datetemp.getYear() + 1900;
            int month = datetemp.getMonth() + 1;
            int t = year*100 + month;
            yearMonth = Long.parseLong(String.valueOf(t));
            transHisMinid = 0L;
            balanceType = Constants.BALANCE_TYPEB;
            write("{success:true,msg:'B'}");
        	return;
        }
        /***********结账年月份check******************/
        Date datetemp = new Date();
        int year = datetemp.getYear() + 1900;
        int month = datetemp.getMonth() + 1;
        long nowYearMonth = year*100 + month;
        long yearMonthadd1 = yearMonth + 1;
        String syearMonth = String.valueOf(yearMonthadd1);
        // 如果当前是12月
        if (Long.parseLong(syearMonth.substring(4, 6)) > 12) {
            yearMonthadd1 = yearMonth + 100;
            yearMonthadd1 -= 11;
        }
        // 上次结算失败，不能进行结算。
        if (null != balance && Constants.BALANCE_ON.equals(balance.getBalanceStatus())) {
        	write("{success:true,msg:'ISON'}");
        	return;
        }
        // 重新结算上次记录
        else if (userYearMonth.equals(yearMonth)) {
            balanceType = Constants.BALANCE_TYPEA;
            write("{success:true,msg:'A'}");
        	return;
        // 结算本月
        } else if (userYearMonth == yearMonthadd1
                && (userYearMonth <= nowYearMonth)) {
            balanceType = Constants.BALANCE_TYPEB;
            write("{success:true,msg:'B'}");
        	return;
        // 都不满足，返回错误
        } else {
            write("{success:true,msg:'TIME'}");
        	return;
        }
    }
//    /**
//     * 月结函数
//     */
//    @SuppressWarnings({ "deprecation", "unchecked" })
//    public void doBalance() {
//        /** 保存仓库编码，物料ID的list */
//        List<String> whsList = new ArrayList<String>();
//        /** 保存仓库编码，库位编码，物料ID的list */
//        List<String> locationList = new ArrayList<String>();
//        /** 保存物料ID的list */
//        List<Long> materialIdList = new ArrayList<Long>();
//        // 获得最近的一次结算记录
//        InvJBalance balance = null;
//        Long latestId = null;
//        Long transHisMinidForTypeA = null;
//        latestId = balanceRemote.getLatestId(employee.getEnterpriseCode());
//        if (latestId != null) {
//        	balance = balanceRemote.findById(latestId);
//        }
//        InvCTransaction transaction = invCTransactionRemote.findByTransCode(employee.getEnterpriseCode(), "O");
//        // 获得最近的一次结算记录
//        try {
//	        // 如果存在
//	        if (null != balance) {	        	
//	            yearMonth = balance.getBalanceYearMonth();
//	            transHisMinid = balance.getTransHisMaxid() + 1L;	            
//	            transHisMinidForTypeA = balance.getTransHisMinid();
//	        // 如果不存在（系统的第一次结算），则取系统时间的当年当月来结算
//	        } else {
//	        	balance = new InvJBalance();
//	            Date datetemp = new Date();
//	            int year = datetemp.getYear() + 1900;
//	            int month = datetemp.getMonth() + 1;
//	            long nowYearMonth = year*100 + month;
//	            yearMonth = nowYearMonth;
//	            transHisMinid = 0L;
//	            balanceType = Constants.BALANCE_TYPEB;
//	        }
//	        // 结算本月
//	        if (Constants.BALANCE_TYPEB.equalsIgnoreCase(balanceType)) {
//	            /***********插入结算主表一条新的记录************/
//	            // 设置结算类型为月结算
//	            balance.setBalanceType(Constants.BALANCE_MONTH);
//	            // 设置结账年月份
//	            balance.setBalanceYearMonth(userYearMonth);
//	            // 设置结账日期
//	            balance.setBalanceDate(new Date());
//	            // 设置结账起始流水号
//	            balance.setTransHisMinid(transHisMinid);
//	            // 设置上次修改人
//	            balance.setLastModifiedBy(employee.getWorkerCode());
//	            // 设置修改日期
//	            balance.setLastModifiedDate(new Date());
//	            // 设置企业代码
//	            balance.setEnterpriseCode(employee.getEnterpriseCode());
//	            // 设置结账状态
//	            balance.setBalanceStatus(Constants.BALANCE_ON);
//	            // 设置结账截止流水号
//	            Long maxId = transRemote.getLatestId(employee.getEnterpriseCode());
//	            if(null != maxId) {
//	            	balance.setTransHisMaxid(maxId);
//	            } else {
//	            	write("{success:true,msg:'OVER'}");
//	            	return;
//	            }
//	            // 设置流水号
//	            balance.setBalanceId(balanceRemote.getMaxId());
//	            balance.setIsUse(Constants.IS_USE_Y);
//	            // 比较起始流水号跟截止流水号，如果截止流水号<起始流水号时，则返回错误
//	            if (balance.getTransHisMaxid() < balance.getTransHisMinid()) {
//	            	write("{success:true,msg:'OVER'}");
//	            	return;
//	            }
//	            // 保存到数据库中
//	            balanceRemote.save(balance);
//	        } else {
//	        // 重新结算上次
//	            // 查找上次的结算记录
//	            balance = balanceRemote.findById(balanceRemote.getLatestId(employee.getEnterpriseCode()));
//	            // 设置结账日期
//	            balance.setBalanceDate(new Date());
//	            // 设置结账起始流水号
//	            balance.setTransHisMinid(transHisMinidForTypeA);
//	            // 设置上次修改人
//	            balance.setLastModifiedBy(employee.getWorkerCode());
//	            // 设置修改日期
//	            balance.setLastModifiedDate(new Date());
//	            // 设置企业代码
//	            balance.setEnterpriseCode(employee.getEnterpriseCode());
//	            // 设置结账状态
//	            balance.setBalanceStatus(Constants.BALANCE_ON);
//	            // 设置结账截止流水号
//	            balance.setTransHisMaxid(transRemote.getLatestId(employee.getEnterpriseCode()));
//	            // 比较起始流水号跟截止流水号，如果截止流水号<起始流水号时，则返回错误
//	            if (balance.getTransHisMaxid() < balance.getTransHisMinid()) {
//	            	write("{success:true,msg:'OVER'}");
//	            	return;
//	            }
//	            // 保存到数据库中
//	            balanceRemote.update(balance);
//	        }
//	        
//	        /***********check************/
//	        /********check批号记录表*******/
//	        // 月结bean的List
//	        List<MonthBalanceInfo> mbLotInfoList = new ArrayList<MonthBalanceInfo>();
//	        // 获得批号记录表里的月结信息
//	        mbLotInfoList = remote.getLotTable(employee.getEnterpriseCode());
//	        if (mbLotInfoList.size() > 0) {
//	            // 每条批号记录表的总本期接收的记录逐个跟事务表中的批号的总接收，总调整，总出库比较
//	            Iterator it = mbLotInfoList.iterator();
//	            while (it.hasNext()) {
//	                MonthBalanceInfo mbInfo = (MonthBalanceInfo) it.next();
//	                // 把该记录的仓库号，库位号，批号，物料ID提出来，以到下列list中找出对应记录来
//	                String whsNo = mbInfo.getWhsNo();
//	                String locationNo = mbInfo.getLocationNo();
//	                String lotNo = mbInfo.getLotNo();
//	                Long materialId = mbInfo.getMaterialId();
//	                // 把批号记录表月结信息中的仓库编码和库位编码记下来，
//	                // 用于库存物料记录和库位物料记录的check
//	                // 再加一个物料ID，因为更新表时要用 add@12-26
//	                whsList.add("w:" + whsNo + ",m:" + materialId);
//	                locationList.add("w:" + whsNo + ",l:" + locationNo 
//	                        + ",m:" + materialId);
//	                // 比较事务历史表中批号本期接收
//	                // 获得事务历史表中所有批号本期总接收记录
//	                List<MonthBalanceInfo> mbReceiptList = remote.getReceipt(
//	                        transHisMinid, 
//	                        balance.getEnterpriseCode());
//	                if (mbReceiptList.size() > 0) {
//	                    // 找出该轮的该库该位该批号的记录来，进行比较
//	                    Iterator it2 = mbReceiptList.iterator();
//	                    Double qty = 0d;
//	                    // 找!
//	                    while(it2.hasNext()) {
//	                        // 一条批号总接收记录
//	                        MonthBalanceInfo mbReceiptInfo = (MonthBalanceInfo) it2.next();
//	                        // 找到了！
//	                        if(whsNo.equals(mbReceiptInfo.getWhsNo()) &&
//	                            ((null == locationNo && null == mbReceiptInfo.getLocationNo()) ||
//	                            ((null != mbReceiptInfo.getLocationNo() && locationNo != null) ? 
//	                                    locationNo.equals(mbReceiptInfo.getLocationNo()):false) && 
//	                            lotNo.equals(mbReceiptInfo.getLotNo()) &&
//	                            materialId.equals(mbReceiptInfo.getMaterialId()))) {
//	                            // 把数量加上（因为有入（退）库和移库两种），循环完了跟批号记录表中批号本期接收比较check
//	                            qty += mbReceiptInfo.getQuantity();
//	                        }
//	                    }
//	                    // 比较！如果不相等就放到message中
//	                    if (!mbInfo.getReceipt().equals(qty)) {
//	                        e_message += ("<br/>批号记录表（本期接收）-" + mbInfo.getLotNo() + "、" + 
//	                                mbInfo.getWhsNo() + "、" + mbInfo.getLocationNo() + "、" + 
//	                                mbInfo.getMaterialId());
//	                    }
//	                }
//	                // 比较事务历史表中批号本期调整
//	                // 获得事务历史表中所有批号本期总调整记录
//	                List<MonthBalanceInfo> mbAdjustList = remote.getAdjust(
//	                        transHisMinid, 
//	                        balance.getEnterpriseCode());
//	                if (mbAdjustList.size() > 0) {
//	                    // 找出该轮的该库该位该批号的记录来，进行比较
//	                    Iterator it2 = mbAdjustList.iterator();
//	                    Double qty = 0d;
//	                    // 找!
//	                    while(it2.hasNext()) {
//	                        // 一条批号总调整记录
//	                        MonthBalanceInfo mbAdjustInfo = (MonthBalanceInfo) it2.next();
//	                        // 找到了！
//	                        if(whsNo.equals(mbAdjustInfo.getWhsNo()) &&
//	                                ((null == locationNo && null == mbAdjustInfo.getLocationNo()) ||
//	                                ((null != mbAdjustInfo.getLocationNo() && locationNo != null) ? 
//	                                        locationNo.equals(mbAdjustInfo.getLocationNo()):false) && 
//	                                lotNo.equals(mbAdjustInfo.getLotNo()) &&
//	                                materialId.equals(mbAdjustInfo.getMaterialId()))) {
//	                            // 把数量加上，循环完了跟批号记录表中批号本期调整比较check
//	                            qty += mbAdjustInfo.getQuantity();
//	                            break;
//	                        }
//	                    }
//	                    // 比较！如果不相等就放到message中
//	                    if (!mbInfo.getAdjust().equals(qty)) {
//	                        e_message += ("<br/>批号记录表（本期调整）-" + mbInfo.getLotNo() + "、" + 
//	                                mbInfo.getWhsNo() + "、" + mbInfo.getLocationNo() + "、" + 
//	                                mbInfo.getMaterialId());
//	                    }
//	                }
//	                
//	                // 比较事务历史表中批号本期出库
//	                // 获得事务历史表中所有批号本期总出库记录
//	                List<MonthBalanceInfo> mbIssueList = remote.getIssue(
//	                        transHisMinid, 
//	                        balance.getEnterpriseCode());
//	                if (mbIssueList.size() > 0) {
//	                    // 找出该轮的该库该位该批号的记录来，进行比较
//	                    Iterator it2 = mbIssueList.iterator();
//	                    Double qty = 0d;
//	                    // 找!
//	                    while(it2.hasNext()) {
//	                        // 一条批号总出库记录
//	                        MonthBalanceInfo mbIssueInfo = (MonthBalanceInfo) it2.next();
//	                        // 找到了！
//	                        if(whsNo.equals(mbIssueInfo.getWhsNo()) &&
//	                            ((null == locationNo && null == mbIssueInfo.getLocationNo()) ||
//	                            ((null != mbIssueInfo.getLocationNo() && locationNo != null) ? 
//	                                    locationNo.equals(mbIssueInfo.getLocationNo()):false) && 
//	                            lotNo.equals(mbIssueInfo.getLotNo()) &&
//	                            materialId.equals(mbIssueInfo.getMaterialId()))) {
//	                            // 把数量加上，循环完了跟批号记录表中批号本期出库比较check
//	                            qty += mbIssueInfo.getQuantity();
//	                            break;
//	                        }
//	                    }
//	                    // 比较！如果不相等就放到message中
//	                    if (!mbInfo.getIssue().equals(qty)) {
//	                        e_message += ("<br/>批号记录表（本期出库）-" + mbInfo.getLotNo() + "、" + 
//	                                mbInfo.getWhsNo() + "、" + mbInfo.getLocationNo() + "、" + 
//	                                mbInfo.getMaterialId());
//	                    }
//	                }
//	            }
//	        }
//	        // 把上面的批号记录表月结信息中的仓库编码和库位编码distince处理，用于下面的check
//	        HashSet h = new HashSet(whsList);
//	        whsList.clear();
//	        whsList.addAll(h);
//	        h = new HashSet(locationList);
//	        locationList.clear();
//	        locationList.addAll(h);
//	        /********check库存记录表*******/
//	        // 仓库编码的List
//	        List<MonthBalanceInfo> mbWhsInfoList = new ArrayList<MonthBalanceInfo>();
//	        // “仓库编码，物料ID”的字符串list
//	        Iterator itwhs = whsList.iterator();
//	        while(itwhs.hasNext()) {
//	            String stemp = (String)itwhs.next();
//	            // 把仓库编号和物料ID分开
//	            String[] tempArray = stemp.split(",");
//	            // 从中取得仓库编号（物料ID用不着），并把“w:”去掉
//	            String swhsNo = tempArray[0].substring(2, tempArray[0].length());
//	            // 从中取得物料ID，把“m:”去掉，在最后插入历史事务表时用 add@12.26
//	            String smaterialId = tempArray[1].substring(2, tempArray[1].length());
//	            Long lMaterialId = Long.parseLong(smaterialId);
//	            materialIdList.add(Long.parseLong(smaterialId));
//	            // 以上26日修改
//	            // 库存月结bean的list
//	            mbWhsInfoList = remote.getWarehouseTable(swhsNo, employee.getEnterpriseCode());
//	            // 每条库存记录表的总本期接收的记录逐个跟事务表中的库存的总接收，总调整，总出库比较
//	            Iterator it = mbWhsInfoList.iterator();
//	            while (it.hasNext()) {
//	                MonthBalanceInfo mbInfo = (MonthBalanceInfo) it.next();
//	                // 把该记录的仓库号，物料ID提出来，以到下列list中找出对应记录来
//	                String whsNo = swhsNo;
//	                Long materialId = mbInfo.getMaterialId();
//	                // 比较事务历史表中库存本期接收
//	                // 获得事务历史表中所有库存本期总接收记录
//	                List<MonthBalanceInfo> mbReceiptList = remote.getReceipt(
//	                        transHisMinid, 
//	                        whsNo, 
//	                        balance.getEnterpriseCode());
//	                if (mbReceiptList.size() > 0) {
//	                    // 找出该轮的该库位该批号的记录来，进行比较
//	                    Iterator it2 = mbReceiptList.iterator();
//	                    Double qty = 0d;
//	                    // 找!
//	                    while(it2.hasNext()) {
//	                        // 一条库存总接收记录
//	                        MonthBalanceInfo mbReceiptInfo = (MonthBalanceInfo) it2.next();
//	                        // 找到了！
//	                        if(materialId.equals(mbReceiptInfo.getMaterialId())) {
//	                            // 把数量加上（因为有入（退）库和移库两种），循环完了跟库存记录表中库存本期接收比较check
//	                            qty += mbReceiptInfo.getQuantity();
//	                        }
//	                    }
//	                    // 比较！如果不相等就放到message中
//	                    if (!mbInfo.getReceipt().equals(qty) && 
//	                    		lMaterialId.equals(materialId)) {
//	                        e_message += ("<br/>库存物料记录表（本期接收）-" + 
//	                        		swhsNo + "、" + 
//	                                mbInfo.getMaterialId());
//	                    }
//	                }
//	                // 比较事务历史表中库存本期调整
//	                // 获得事务历史表中所有库存本期总调整记录
//	                List<MonthBalanceInfo> mbAdjustList = remote.getAdjust(
//	                        transHisMinid, 
//	                        whsNo, 
//	                        balance.getEnterpriseCode());
//	                if (mbAdjustList.size() > 0) {
//	                    // 找出该轮的该库位该批号的记录来，进行比较
//	                    Iterator it2 = mbAdjustList.iterator();
//	                    Double qty = 0d;
//	                    // 找!
//	                    while(it2.hasNext()) {
//	                        // 一条库存总调整记录
//	                        MonthBalanceInfo mbAdjustInfo = (MonthBalanceInfo) it2.next();
//	                        // 找到了！
//	                        if(materialId.equals(mbAdjustInfo.getMaterialId())) {
//	                            // 把数量加上，循环完了跟库存记录表中库存本期调整比较check
//	                            qty += mbAdjustInfo.getQuantity();
//	                            break;
//	                        }
//	                    }
//	                    // 比较！如果不相等就放到message中
//	                    if (!mbInfo.getAdjust().equals(qty) && 
//	                    		lMaterialId.equals(materialId)) {
//	                        e_message += ("<br/>库存物料记录表（本期调整）-" + 
//	                        		swhsNo + "、" + 
//	                                mbInfo.getMaterialId());
//	                    }
//	                }
//	                
//	                // 比较事务历史表中库存本期出库
//	                // 获得事务历史表中所有库存本期总出库记录
//	                List<MonthBalanceInfo> mbIssueList = remote.getIssue(
//	                        transHisMinid, 
//	                        whsNo, 
//	                        balance.getEnterpriseCode());
//	                if (mbIssueList.size() > 0) {
//	                    // 找出该轮的该库位该批号的记录来，进行比较
//	                    Iterator it2 = mbIssueList.iterator();
//	                    Double qty = 0d;
//	                    // 找!
//	                    while(it2.hasNext()) {
//	                        // 一条库存总出库记录
//	                        MonthBalanceInfo mbIssueInfo = (MonthBalanceInfo) it2.next();
//	                        // 找到了！
//	                        if(materialId.equals(mbIssueInfo.getMaterialId())) {
//	                            // 把数量加上，循环完了跟库存记录表中库存本期出库比较check
//	                            qty += mbIssueInfo.getQuantity();
//	                            break;
//	                        }
//	                    }
//	                    // 比较！如果不相等就放到message中
//	                    if (!mbInfo.getIssue().equals(qty) && 
//	                    		lMaterialId.equals(materialId)) {
//	                        e_message += ("<br/>库存物料记录表（本期出库）-" + 
//	                        		swhsNo + "、" + 
//	                                mbInfo.getMaterialId());
//	                    }
//	                }
//	            }
//	        }
//	        
//	        /********check库位记录表*******/
//	        // “仓库编码，库位编码，物料ID”的List
//	        List<MonthBalanceInfo> mbLocationInfoList = new ArrayList<MonthBalanceInfo>();
//	        Iterator itLocation = locationList.iterator();
//	        while(itLocation.hasNext()) {
//	            // “仓库编码，库位编码，物料ID”的String
//	            String stemp = (String)itLocation.next();
//	            // 把仓库编码和库位编码，物料ID分开
//	            String[] tempArray = stemp.split(",");
//	            // 仓库编码
//	            String sWhsNo = null;
//	            // 库位编码
//	            String sLocationNo = null;
//	            // 物料ID
//	            Long lMaterialId = null;
//	            // 从“w:XXXX”和“l:XXXX”中截取出仓库号和库位号
//	            if(tempArray[0].length() > 2) {
//	                sWhsNo = tempArray[0].substring(2, tempArray[0].length());
//	            }
//	            if(tempArray[1].length() > 2) {
//	                sLocationNo = tempArray[1].substring(2, tempArray[1].length());
//	            }
//	            if(tempArray[2].length() > 2) {
//	            	lMaterialId = Long.parseLong(tempArray[2].substring(2, tempArray[2].length()));
//	            }
//	            // 库位月结bean的list
//	            mbLocationInfoList = remote.getLocationTable(sWhsNo, sLocationNo, balance.getEnterpriseCode());
//	            // 每条库位记录表的总本期接收的记录逐个跟事务表中的库位的总接收，总调整，总出库比较
//	            Iterator it = mbLocationInfoList.iterator();
//	            while (it.hasNext()) {
//	                MonthBalanceInfo mbInfo = (MonthBalanceInfo) it.next();
//	                // 把该记录的仓库号，库位号，物料ID提出来，以到下列list中找出对应记录来
//	                String whsNo = sWhsNo;
//	                String locationNo = sLocationNo;
//	                Long materialId = mbInfo.getMaterialId();
//	                // 比较事务历史表中库位本期接收
//	                // 获得事务历史表中所有库位本期总接收记录
//	                List<MonthBalanceInfo> mbReceiptList = remote.getReceipt(
//	                        transHisMinid, 
//	                        whsNo, 
//	                        locationNo, 
//	                        balance.getEnterpriseCode());
//	                if (mbReceiptList.size() > 0) {
//	                    // 找出该轮的该库位该批号的记录来，进行比较
//	                    Iterator it2 = mbReceiptList.iterator();
//	                    Double qty = 0d;
//	                    // 找!
//	                    while(it2.hasNext()) {
//	                        // 一条库位总接收记录
//	                        MonthBalanceInfo mbReceiptInfo = (MonthBalanceInfo) it2.next();
//	                        // 找到了！
//	                        if(materialId.equals(mbReceiptInfo.getMaterialId())) {
//	                            // 把数量加上（因为有入（退）库和移库两种），循环完了跟库位记录表中库位本期接收比较check
//	                            qty += mbReceiptInfo.getQuantity();
//	                        }
//	                    }
//	                    // 比较！如果不相等就放到message中
//	                    if (!mbInfo.getReceipt().equals(qty) && 
//	                    		lMaterialId.equals(materialId)) {
//	                        e_message += ("<br/>库位物料记录表（本期接收）-" + 
//	                        		sWhsNo + "、" + 
//	                        		locationNo + "、" + 
//	                                mbInfo.getMaterialId());
//	                    }
//	                }
//	                // 比较事务历史表中库位本期调整
//	                // 获得事务历史表中所有库位本期总调整记录
//	                List<MonthBalanceInfo> mbAdjustList = remote.getAdjust(
//	                        transHisMinid, 
//	                        whsNo, 
//	                        locationNo, 
//	                        balance.getEnterpriseCode());
//	                if (mbAdjustList.size() > 0) {
//	                    // 找出该轮的该库位该批号的记录来，进行比较
//	                    Iterator it2 = mbAdjustList.iterator();
//	                    Double qty = 0d;
//	                    // 找!
//	                    while(it2.hasNext()) {
//	                        // 一条库位总调整记录
//	                        MonthBalanceInfo mbAdjustInfo = (MonthBalanceInfo) it2.next();
//	                        // 找到了！
//	                        if(materialId.equals(mbAdjustInfo.getMaterialId())) {
//	                            // 把数量加上，循环完了跟库位记录表中库位本期调整比较check
//	                            qty += mbAdjustInfo.getQuantity();
//	                            break;
//	                        }
//	                    }
//	                    // 比较！如果不相等就放到message中
//	                    if (!mbInfo.getAdjust().equals(qty) && 
//	                    		lMaterialId.equals(materialId)) {
//	                        e_message += ("<br/>库位物料记录表（本期调整）-" + 
//	                        		sWhsNo + "、" + 
//	                        		locationNo + "、" + 
//	                                mbInfo.getMaterialId());
//	                    }
//	                }
//	                
//	                // 比较事务历史表中库位本期出库
//	                // 获得事务历史表中所有库位本期总出库记录
//	                List<MonthBalanceInfo> mbIssueList = remote.getIssue(
//	                        transHisMinid, 
//	                        whsNo, 
//	                        locationNo, 
//	                        balance.getEnterpriseCode());
//	                if (mbIssueList.size() > 0) {
//	                    // 找出该轮的该库位该批号的记录来，进行比较
//	                    Iterator it2 = mbIssueList.iterator();
//	                    Double qty = 0d;
//	                    // 找!
//	                    while(it2.hasNext()) {
//	                        // 一条库位总出库记录
//	                        MonthBalanceInfo mbIssueInfo = (MonthBalanceInfo) it2.next();
//	                        // 找到了！
//	                        if(materialId.equals(mbIssueInfo.getMaterialId())) {
//	                            // 把数量加上，循环完了跟库位记录表中库存本期出库比较check
//	                            qty += mbIssueInfo.getQuantity();
//	                            break;
//	                        }
//	                    }
//	                    // 比较！如果不相等就放到message中
//	                    if (!mbInfo.getIssue().equals(qty) && 
//	                    		lMaterialId.equals(materialId)) {
//	                        e_message += ("<br/>库位物料记录表（本期出库）-" + 
//	                        		sWhsNo + "、" + 
//	                        		locationNo + "、" + 
//	                                mbInfo.getMaterialId());
//	                    }
//	                }
//	            }
//	        }
//	        
//	        /***********对e_message判断************/
//	        // 结算check出现结算错误，提示用户
//	        if (e_message.length() > 0) {
//	            // 更新结算主表
//	//            balance.setBalanceStatus(Constants.BALANCE_OK);
//	//            balance.setLastModifiedDate(new Date());
//	//            balanceRemote.update(balance);
//	            write("{success:true,msg:'" + e_message + "'}");
//            	return;
//	        } else {
//	        // 没有出现错误，更新各表
//	            /***********更新各表************/
//	            // 把要更新的entity放到list中，传到ejb端进行事务更新
//	            // 批号记录list
//	            List<InvJLot> listUpdateLot = new ArrayList<InvJLot>();
//	            // 库位记录list
//	            List<InvJLocation> listUpdateLocation = new ArrayList<InvJLocation>();
//	            // 库存记录list
//	            List<InvJWarehouse> listUpdateWarehouse = new ArrayList<InvJWarehouse>();
//	            // 事务历史表
//	            List<InvJTransactionHis> listSaveTrans = new ArrayList<InvJTransactionHis>();
//	            // 更新批号记录表
//	            Iterator it = mbLotInfoList.iterator();
//	            while (it.hasNext()) {
//	                MonthBalanceInfo mbInfo = (MonthBalanceInfo) it.next();
//	                // 批号记录表bean
//	                List<InvJLot> lotList = new ArrayList<InvJLot>();
//	                // 用批号，仓库号，库位号，物料ID查找批号物料记录
//	                lotList = lotRemote.findByLWHLM(employee.getEnterpriseCode(),
//	                        mbInfo.getLotNo(), mbInfo.getWhsNo(), 
//	                        mbInfo.getLocationNo(), mbInfo.getMaterialId());
//	                if (lotList.size() > 0) {
//	                    InvJLot lot = new InvJLot();
//	                    // 取得该条批号记录
//	                    lot = lotList.get(0);
//	                    // 设置本期初始 = 原本期初始 + 本期接收 + 本期调整 - 本期出库
//	                    lot.setOpenBalance(lot.getOpenBalance() + 
//	                            lot.getReceipt() + lot.getAdjust() - 
//	                            lot.getIssue());
//	                    // 设置本期接收，本期调整，本期出库为0
//	                    lot.setReceipt(Double.parseDouble(Constants.NUMBER_ZERO));
//	                    lot.setAdjust(Double.parseDouble(Constants.NUMBER_ZERO));
//	                    lot.setIssue(Double.parseDouble(Constants.NUMBER_ZERO));
//	                    // 设置修改人
//	                    lot.setLastModifiedBy(employee.getWorkerCode());
//	                    // 设置修改时间
//	                    lot.setLastModifiedDate(new Date());
//	                    // 更新到数据库
//	                    listUpdateLot.add(lot);
//	                }
//	            }
//	            // 更新库存记录表
//	            // “仓库号，物料ID”list
//	            itwhs = whsList.iterator();
//	            while(itwhs.hasNext()) {
//	                String stemp = (String)itwhs.next();
//	                // 把仓库编号和物料ID分开
//	                String[] tempArray = stemp.split(",");
//	                // 从中取得仓库编号和物料ID，并把“w:”和“m:”去掉
//	                String swhsNo = tempArray[0].substring(2, tempArray[0].length());
//	                String smaterialId = null;
//	                if (tempArray[1].length() > 2) {
//	                    smaterialId = tempArray[1].substring(2, tempArray[1].length());
//	                }
//	                List<InvJWarehouse> warehouseBeanList = new ArrayList<InvJWarehouse>();
//	                // 用仓库号，物料ID查找库存物料记录
//	                warehouseBeanList = warehouseRemote.findByWHandMaterial(employee.getEnterpriseCode(), 
//	                        swhsNo, Long.parseLong(smaterialId));
//	                if (warehouseBeanList.size() > 0) {
//	                    InvJWarehouse warehouse = new InvJWarehouse();
//	                    // 取得该条库存记录
//	                    warehouse = warehouseBeanList.get(0);
//	                    // 设置本期初始 = 原本期初始 + 本期接收 + 本期调整 - 本期出库
//	                    warehouse.setOpenBalance(warehouse.getOpenBalance() + 
//	                            warehouse.getReceipt() + warehouse.getAdjust() - 
//	                            warehouse.getIssue());
//	                    // 设置本期接收，本期调整，本期出库为0
//	                    warehouse.setReceipt(Double.parseDouble(Constants.NUMBER_ZERO));
//	                    warehouse.setAdjust(Double.parseDouble(Constants.NUMBER_ZERO));
//	                    warehouse.setIssue(Double.parseDouble(Constants.NUMBER_ZERO));
//	                    // 设置修改人
//	                    warehouse.setLastModifiedBy(employee.getWorkerCode());
//	                    // 设置修改时间
//	                    warehouse.setLastModifiedDate(new Date());
//	                    // 更新到数据库
//	                    listUpdateWarehouse.add(warehouse);
//	                }
//	            }
//	            // 更新库位记录表
//	            itLocation = locationList.iterator();
//	            while(itLocation.hasNext()) {
//	                String stemp = (String)itLocation.next();
//	                // 把仓库编号，库位编号和物料ID分开
//	                String[] tempArray = stemp.split(",");
//	                // 从中取得仓库编号，库位编号和物料ID，并把“w:”，“l:”和“m:”去掉
//	                String swhsNo = null;
//	                String sLocationNo = null;
//	                String smaterialId = null;
//	                // 取得仓库编号
//	                if (tempArray[0].length() > 2) {
//	                    swhsNo = tempArray[0].substring(2, tempArray[0].length());
//	                }
//	                // 取得库位编号（可能为空）
//	                if (tempArray[1].length() > 2) {
//	                    sLocationNo = tempArray[1].substring(2, tempArray[1].length());
//	                }
//	                // 取得物料ID
//	                if (tempArray[2].length() > 2) {
//	                    smaterialId = tempArray[2].substring(2, tempArray[2].length());
//	                }
//	                List<InvJLocation> locationBeanList = new ArrayList<InvJLocation>();
//	                // 用仓库号，物料ID查找库存物料记录
//	                locationBeanList = locationRemote.findByWHLM(employee.getEnterpriseCode(), 
//	                        swhsNo, sLocationNo, Long.parseLong(smaterialId));
//	                if (locationBeanList.size() > 0) {
//	                    InvJLocation location = new InvJLocation();
//	                    // 取得该条库位记录
//	                    location = locationBeanList.get(0);
//	                    // 设置本期初始 = 原本期初始 + 本期接收 + 本期调整 - 本期出库
//	                    location.setOpenBalance(location.getOpenBalance() + 
//	                            location.getReceipt() + location.getAdjust() - 
//	                            location.getIssue());
//	                    // 设置本期接收，本期调整，本期出库为0
//	                    location.setReceipt(Double.parseDouble(Constants.NUMBER_ZERO));
//	                    location.setAdjust(Double.parseDouble(Constants.NUMBER_ZERO));
//	                    location.setIssue(Double.parseDouble(Constants.NUMBER_ZERO));
//	                    // 设置修改人
//	                    location.setLastModifiedBy(employee.getWorkerCode());
//	                    // 设置修改时间
//	                    location.setLastModifiedDate(new Date());
//	                    // 更新到数据库
//	                    listUpdateLocation.add(location);
//	                }
//	            }
//	            // 插入事务历史表
//	            // 把物料ID distinct 处理，得到【物料ID】list
//	            h = new HashSet(materialIdList);
//	            materialIdList.clear();
//	            materialIdList.addAll(h);
//	            // 到库存物料记录表中查询该【物料ID】list中每个物料的异动数量，然后写入到
//	            // 事务历史表中
//	            List<MonthBalanceInfo> mandqList = new ArrayList<MonthBalanceInfo>();
//	            String smaterial = materialIdList.toString();
//	            if (smaterial.length() > 2) {
//	                smaterial = smaterial.substring(1, smaterial.length() - 1);
//	            }
//	            mandqList = remote.getMandQ(smaterial, employee.getEnterpriseCode());
//	            it = mandqList.iterator();
//	            while(it.hasNext()) {
//	                MonthBalanceInfo mandq = (MonthBalanceInfo)it.next();
//	                // 事务历史表bean
//	                InvJTransactionHis transHis = new InvJTransactionHis();
//	                // 设置物料ID
//	                transHis.setMaterialId(mandq.getMaterialId());
//	                // 设置标准成本 add by ywliu 20091102
//	                InvCMaterial model = invCMaterialFacadeRemote.findById(mandq.getMaterialId());
//	                transHis.setStdCost(model.getStdCost());
//	                // 设置事务作用类型ID
//	                transHis.setTransId(transaction.getTransId());
//	                // 设置异动数量
//	                transHis.setTransQty(mandq.getQuantity());
//	                // 设置操作人
//	                transHis.setLastModifiedBy(employee.getWorkerCode());
//	                // 设置操作时间
//	                transHis.setLastModifiedDate(new Date());
//	                // 设置企业代码
//	                transHis.setEnterpriseCode(employee.getEnterpriseCode());
//	                // 设置是否使用
//	                transHis.setIsUse(Constants.IS_USE_Y);
//	                // 设置单号
//	                transHis.setOrderNo(Constants.NUMBER_ZERO);
//	                // 设置序号
//	                transHis.setSequenceId(Long.parseLong(Constants.NUMBER_ZERO));          
//	                // 保存
//	                listSaveTrans.add(transHis);
//	//                transRemote.save(transHis);
//	            }
//	            // 更新结算主表
//	            balance.setBalanceStatus(Constants.BALANCE_OK);
//	            balance.setLastModifiedDate(new Date());
//	//            balance.setBalanceYearMonth(balanceYearMonth)
//	//            balanceRemote.update(balance);
//	            try {
//	                // 到ejb端采用事务方式保存各表中的数据
//	                balanceRemote.saveAll(listUpdateLot, listUpdateLocation,
//	                        listUpdateWarehouse, listSaveTrans, balance);
//	            } catch (Exception e) {
//	//            	// 查找上次的结算记录
//	//                balance = balanceRemote.findById(balanceRemote.getLatestId(employee.getEnterpriseCode()));
//	//                // 把结算状态还原成OK
//	//                balance.setBalanceStatus(Constants.BALANCE_OK);
//	//                balance.setLastModifiedDate(new Date());
//	//                balanceRemote.update(balance);
//	                write("{success:true,msg:'sql'}");
//	            }
//	            // 都做完了，check顺利结束，返回成功
//	            write("{success:true,msg:'ok'}");
//	        }
//        } catch (RuntimeException e) {
//        	e.printStackTrace();
//        	write("{success:true,msg:'sql'}");
//        }
//    }
    /**
     * 月结 
     * add by fyyang 091124
     */
    public void doBalance() {
    	//add by fyyang 091204
    	InvJBalance myBalance=null;
        // 获得最近的一次结算记录
        InvJBalance balance = null;
        Long latestId = null;
        Long laterTransId=0l;
        Long transHisMinidForTypeA = null;
        latestId = balanceRemote.getLatestId(employee.getEnterpriseCode());
        if (latestId != null) {
        	balance = balanceRemote.findById(latestId);
        }
        // 获得最近的一次结算记录
        try {
	        // 如果存在
	        if (null != balance) {	        	
	            yearMonth = balance.getBalanceYearMonth();
	            transHisMinid = balance.getTransHisMaxid() + 1L;	            
	            transHisMinidForTypeA = balance.getTransHisMinid();

	        	laterTransId=balance.getTransHisMaxid()+1;//add by fyyang
	        // 如果不存在（系统的第一次结算），则取系统时间的当年当月来结算
	        } else {
	        	balance = new InvJBalance();
	            Date datetemp = new Date();
	            int year = datetemp.getYear() + 1900;
	            int month = datetemp.getMonth() + 1;
	            long nowYearMonth = year*100 + month;
	            yearMonth = nowYearMonth;
	            transHisMinid = 0L;
	            balanceType = Constants.BALANCE_TYPEB;
	        }
	        // 结算本月
	        if (Constants.BALANCE_TYPEB.equalsIgnoreCase(balanceType)) {
	            /***********插入结算主表一条新的记录************/
	            // 设置结算类型为月结算
	            balance.setBalanceType(Constants.BALANCE_MONTH);
	            // 设置结账年月份
	            balance.setBalanceYearMonth(userYearMonth);
	            // 设置结账日期
	            balance.setBalanceDate(new Date());
	            // 设置结账起始流水号
	            balance.setTransHisMinid(transHisMinid);
	            // 设置上次修改人
	            balance.setLastModifiedBy(employee.getWorkerCode());
	            // 设置修改日期
	            balance.setLastModifiedDate(new Date());
	            // 设置企业代码
	            balance.setEnterpriseCode(employee.getEnterpriseCode());
	            // 设置结账状态
	            balance.setBalanceStatus(Constants.BALANCE_ON);
	            // 设置结账截止流水号
	            Long maxId = transRemote.getLatestId(employee.getEnterpriseCode());
	            if(null != maxId) {
	            	balance.setTransHisMaxid(maxId);
	            } else {
	            	write("{success:true,msg:'OVER'}");
	            	return;
	            }
	            // 设置流水号
	            balance.setBalanceId(balanceRemote.getMaxId());
	            balance.setIsUse(Constants.IS_USE_Y);
	            // 比较起始流水号跟截止流水号，如果截止流水号<起始流水号时，则返回错误
	            if (balance.getTransHisMaxid() < balance.getTransHisMinid()) {
	            	write("{success:true,msg:'OVER'}");
	            	return;
	            }
	            // 保存到数据库中
	            balanceRemote.save(balance);
	        } else {
	        
	        // 重新结算上次
	            // 查找上次的结算记录
	        	myBalance=balanceRemote.findById(balanceRemote.getLatestId(employee.getEnterpriseCode()));
	            balance = balanceRemote.findById(balanceRemote.getLatestId(employee.getEnterpriseCode()));
	            // 设置结账日期
	            balance.setBalanceDate(new Date());
	            // 设置结账起始流水号
	            balance.setTransHisMinid(transHisMinidForTypeA);
	            // 设置上次修改人
	            balance.setLastModifiedBy(employee.getWorkerCode());
	            // 设置修改日期
	            balance.setLastModifiedDate(new Date());
	            // 设置企业代码
	            balance.setEnterpriseCode(employee.getEnterpriseCode());
	            // 设置结账状态
	            balance.setBalanceStatus(Constants.BALANCE_ON);
	            // 设置结账截止流水号
	            balance.setTransHisMaxid(transRemote.getLatestId(employee.getEnterpriseCode()));
	            // 比较起始流水号跟截止流水号，如果截止流水号<起始流水号时，则返回错误
	            if (balance.getTransHisMaxid() < balance.getTransHisMinid()) {
	            	write("{success:true,msg:'OVER'}");
	            	return;
	            }
	            
	        	
	            // 保存到数据库中
	            balanceRemote.update(balance);
	        }
	      //  System.out.println("月结表更新完毕");
	        /***********check************/
	        e_message="";
	        /********check批号记录表*******/
	        e_message=balanceRemote.checkInvLot(employee.getEnterpriseCode(),laterTransId);
	      //  System.out.println("批号表检查完毕");
	      //  System.out.println(e_message);
	        /********check库存记录表*******/
	        e_message+=balanceRemote.checkInvWarehouse(employee.getEnterpriseCode(),laterTransId);
	      //  System.out.println("库存表检查完毕");
	      //  System.out.println(e_message);
	        /********check库位记录表*******/
	        e_message+=balanceRemote.checkInvLocation(employee.getEnterpriseCode(),laterTransId);
	    //    System.out.println("库位检查完毕");
	      //  System.out.println(e_message);
	        /***********对e_message判断************/
	        // 结算check出现结算错误，提示用户
	        if (e_message.length() > 0) {
	         //---------add by fyyang 091204-----------
	        	if(myBalance==null)
	        	{
	        		balanceRemote.deleteLastRecord();
	        	}
	        	else
	        	{
	        		balanceRemote.update(myBalance);
	        	}
	        //--------------------------------------------	
	            write("{success:true,msg:'" + e_message + "'}");
            	return;
	        } else {
	        // 没有出现错误，更新各表
	            /***********更新各表************/
	           
	
	            // 更新库存记录表

	            // 更新库位记录表

	            // 插入事务历史表
	  
	            // 更新结算主表
	            try {
	            //	System.out.println("检查完毕");
	            	 // 更新批号记录表  更新库存记录表 更新库位记录表  插入事务历史表 更新结算主表
	            	balanceRemote.updateForBalance(employee.getEnterpriseCode(), employee.getWorkerCode());
	            //	System.out.println("月结成功");
	            } catch (Exception e) {
	                write("{success:true,msg:'sql'}");
	            }
	            // 都做完了，check顺利结束，返回成功
	            write("{success:true,msg:'ok'}");
	        }
        } catch (RuntimeException e) {
        	e.printStackTrace();
        	write("{success:true,msg:'sql'}");
        }
    }

    /**
     * @return the userYearMonth
     */
    public Long getUserYearMonth() {
        return userYearMonth;
    }

    /**
     * @param userYearMonth the userYearMonth to set
     */
    public void setUserYearMonth(Long userYearMonth) {
        this.userYearMonth = userYearMonth;
    }

    /**
     * @return the balanceType
     */
    public String getBalanceType() {
        return balanceType;
    }

    /**
     * @param balanceType the balanceType to set
     */
    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }
}
