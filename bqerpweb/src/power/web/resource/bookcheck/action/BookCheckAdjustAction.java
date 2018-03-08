/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.bookcheck.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.ejb.resource.InvCTransaction;
import power.ejb.resource.InvCTransactionFacadeRemote;
import power.ejb.resource.InvJBook;
import power.ejb.resource.InvJBookDetails;
import power.ejb.resource.InvJBookDetailsFacadeRemote;
import power.ejb.resource.InvJBookFacadeRemote;
import power.ejb.resource.InvJLocation;
import power.ejb.resource.InvJLocationFacadeRemote;
import power.ejb.resource.InvJLot;
import power.ejb.resource.InvJLotFacadeRemote;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJTransactionHisFacadeRemote;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.InvJWarehouseFacadeRemote;
import power.ejb.resource.form.BookCheckInfo;
import power.ejb.resource.business.BookCheck;
import power.ejb.resource.business.IssueManage;

/**
 * 物料盘点调整Action
 * @author huangweijie
 */
public class BookCheckAdjustAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	/** 日期形式字符串 yyyy-MM-dd */
    private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    /** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
    private static final String DATE_FORMAT_YYYYMMDD_TIME = "yyyy-MM-dd HH:mm:ss";
	/** remote */
	/** 盘点单remote */
	private BookCheck remote;
	/** 事务表 */
	private InvCTransactionFacadeRemote transremote;
	/** 盘点主表 */
	private InvJBookFacadeRemote bookremote;
	/** 盘点明细表 */
	private InvJBookDetailsFacadeRemote bookdtlremote;
	/** 库存物料记录表 */
	private InvJWarehouseFacadeRemote whremote;
	/** 库位物料记录表 */
	private InvJLocationFacadeRemote locationremote;
	/** 批号记录表 */
	private InvJLotFacadeRemote lotremote;
	/** 判断是否在结账 */
	private IssueManage issmanager;
	/** 事务历史表 */
	private InvJTransactionHisFacadeRemote transHisRemote;

	/** 盘点单号 */
	private String bookNo;
	

	public BookCheckAdjustAction() {
		remote = (BookCheck) factory.getFacadeRemote("BookCheckImp");
		transremote = (InvCTransactionFacadeRemote) factory
				.getFacadeRemote("InvCTransactionFacade");
		bookremote = (InvJBookFacadeRemote) factory
				.getFacadeRemote("InvJBookFacade");
		bookdtlremote = (InvJBookDetailsFacadeRemote) factory
				.getFacadeRemote("InvJBookDetailsFacade");
		whremote = (InvJWarehouseFacadeRemote) factory
				.getFacadeRemote("InvJWarehouseFacade");
		locationremote = (InvJLocationFacadeRemote) factory
				.getFacadeRemote("InvJLocationFacade");
		lotremote = (InvJLotFacadeRemote) factory
				.getFacadeRemote("InvJLotFacade");
		issmanager = (IssueManage) factory.getFacadeRemote("IssueManageImpl");
		transHisRemote = (InvJTransactionHisFacadeRemote) factory
				.getFacadeRemote("InvJTransactionHisFacade");
	}

	/**
	 * 返回该盘点单的所有内容
	 */
	public void getBookCheckList() throws Exception {
		PageObject pobj = new PageObject();
		pobj = remote.findBookDetails(bookNo, employee.getEnterpriseCode());
		// 解析字符串
		String str = null;
		if(null == pobj.getList()) {
			str = "{\"list\":[],\"totalCount\":0}";
		} else {
			str = JSONUtil.serialize(pobj);
		}
		write(str);
	}

	/**
	 * 取得并处理来自前台的盘点信息列表
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateBookCheckList() throws Exception {
		String strSaveInfo = request.getParameter("saveInfo");
		Object object = JSONUtil.deserialize(strSaveInfo);
		// 判断是否在结账
		if (!issmanager.isBalanceNow(Constants.BKCK_TRANSCODE)) {
			try {
				if (object != null) {
					if (List.class.isAssignableFrom(object.getClass())) {
						// 如果是数组
						List lst = (List) object;
						int intLen = lst.size();
						for (int intCnt = 0; intCnt < intLen; intCnt++) {
							// 保存盘点信息
							// 把map中的信息放到BookCheckInfo实体中，然后更新各表
							Map mapInfo = (Map) lst.get(intCnt);
							BookCheckInfo info = new BookCheckInfo();
							// 取得盈亏数量
							info.setBalance(Double.parseDouble(mapInfo.get(
									"balance").toString()));
							info.setWhsNo(mapInfo.get("whsNo").toString());
							// 库位可能为空
							if (null != mapInfo.get("locationNo")) {
								info.setLocationNo(mapInfo.get("locationNo")
										.toString());
							} else {
								info.setLocationNo(null);
							}
							info.setLotNo(mapInfo.get("lotNo").toString());
							info.setMaterialId(Long.parseLong(mapInfo.get(
									"materialId").toString()));
							info.setBookId(Long.parseLong(mapInfo.get("bookId")
									.toString()));
							info.setModifyBy(employee.getWorkerCode());
							info.setBookDetailId(Long.parseLong(mapInfo.get(
									"bookDetailId").toString()));
							info.setPhysicalQty(Double.parseDouble(mapInfo.get(
									"physicalQty").toString()));
							if (null != mapInfo.get("reason")) {
								info.setReason(mapInfo.get("reason").toString());
							} else {
								info.setReason(null);
							}
							info.setModifyDate(mapInfo.get("modifyDate").toString());
							
							Employee emp = (Employee)session.getAttribute("employee");
							// 取得事务
							InvCTransaction trans = transremote
									.findByCode(Constants.BKCK_TRANSCODE, emp.getEnterpriseCode());
							info.setTrans(trans);
							// 更新数据库中各个表
							updateBookCheck(info);
							// 盘点主表更新且只更新一次
							if (intCnt == 0) {
								InvJBook book = new InvJBook();
								// 通过流水号取得盘点主表
								book = bookremote.findById(info.getBookId());
								try {
									// 排他
									if (book.getLastModifiedDate().getTime() != 
										formatStringToDate(info.getModifyDate()).getTime()) {
										throw new UsingException("排它处理");
									}
								} catch( UsingException e) {
									write("{success:true,msg:'USING'}");
									throw(e);
								}
								try {
									// 设置盘点状态
									book.setBookStatus(Constants.CMT);
									// 设置修改人
									book.setLastModifiedBy(info.getModifyBy());
									// 设置修改日期
									book.setLastModifiedDate(new java.util.Date());
									// 更新到数据库
									bookremote.update(book);
								} catch (Exception e) {
									write("{success:true,msg:'sql'}");
								}
							}
						}
					} else {
						// 保存盘点信息
						// 把map中的信息放到BookCheckInfo实体中，然后更新各表
						Map mapInfo = (Map) object;
						BookCheckInfo info = new BookCheckInfo();
						// 取得盈亏数量
						info.setBalance(Double.parseDouble(mapInfo.get("balance")
								.toString()));
						info.setBookId(Long.parseLong(mapInfo.get("bookId")
								.toString()));
						info.setModifyBy(mapInfo.get("modifyBy").toString());
						info.setBookDetailId(Long.parseLong(mapInfo.get(
								"bookDetailId").toString()));
						info.setPhysicalQty(Double.parseDouble(mapInfo.get(
								"physicalQty").toString()));
						info.setReason(mapInfo.get("reason").toString());
						info.setModifyDate(mapInfo.get("modifyDate").toString());
						// 取得事务
						InvCTransaction trans = transremote
								.findByCode(Constants.BKCK_TRANSCODE);
						info.setTrans(trans);
						// 更新数据库中各个表
						updateBookCheck(info);
						// 盘点主表更新且只更新一次
						InvJBook book = new InvJBook();
						// 通过流水号取得盘点主表
						book = bookremote.findById(info.getBookId());
						try {
							// 排他
							if (book.getLastModifiedDate().getTime() != 
								formatStringToDate(info.getModifyDate()).getTime()) {
								throw new UsingException("排它处理");
							}
						} catch( UsingException e) {
							write("{success:true,msg:'USING'}");
							throw(e);
						}
						try {
							// 设置盘点状态
							book.setBookStatus(Constants.CMT);
							// 设置修改人
							book.setLastModifiedBy(info.getModifyBy());
							// 设置修改日期
							book.setLastModifiedDate(new java.util.Date());
							// 更新到数据库
							bookremote.update(book);
						} catch (Exception e) {
							write("{success:true,msg:'sql'}");
						}
					}
				}
			} catch (Exception e) {
				write("{success:true,msg:'sql'}");
			}
		// 正在结账
		} else {
			write(Constants.ACCOUNTING);
		}
		write(Constants.MODIFY_SUCCESS);
	}

	/**
	 * 更新某个盘点单表及其他表
	 * @param argMap
	 * @throws Exception
	 */
	public void updateBookCheck(BookCheckInfo info) throws Exception {
		try {
			BookCheckInfo bookinfo = info;
			// 以下更新物料盘点明细表
			InvJBookDetails bookdtl = new InvJBookDetails();
			// 通过明细表流水号获得明细表
			bookdtl = bookdtlremote.findById(bookinfo.getBookDetailId());
			// 设置实盘数量
			bookdtl.setPhysicalQty(bookinfo.getPhysicalQty());
			// 设置盘点状态
			bookdtl.setBookStatus(Constants.CMT);
			// 设置盘点时间
			bookdtl.setBookDate(new java.util.Date());
			// 设置差异原因
			bookdtl.setReason(bookinfo.getReason());
			// 设置更新人
			bookdtl.setLastModifiedBy(employee.getWorkerCode());
			// 设置更新时间
			bookdtl.setLastModifiedDate(new Date());
			// 更新到数据库
			bookdtlremote.update(bookdtl);
	
			/**********更新库存，库位，批号表中的数量*****/
			// 获得事务表影响参数
			InvCTransaction trans = new InvCTransaction();
			trans = bookinfo.getTrans();
	
			/** 更新库存物料记录表 */
			InvJWarehouse wh = new InvJWarehouse();
			// 通过仓库编码和物料编码获得该条记录
			List<InvJWarehouse> whlist = whremote.findByWHandMaterial(employee.getEnterpriseCode(), 
					bookinfo.getWhsNo(), bookinfo.getMaterialId());
			if (whlist.size() > 0) {
				wh = whlist.get(0);
				// 是否影响期初
				if (Constants.FLAG_Y.equals(trans.getIsOpenBalance())) {
					wh.setOpenBalance(wh.getOpenBalance() + bookinfo.getBalance());
				}
				// 是否影响接收
				if (Constants.FLAG_Y.equals(trans.getIsReceive())) {
					wh.setReceipt(wh.getReceipt() + bookinfo.getBalance());
				}
				// 是否影响出库
				if (Constants.FLAG_Y.equals(trans.getIsIssues())) {
					wh.setIssue(wh.getIssue() + bookinfo.getBalance());
				}
				// 是否影响调整
				if (Constants.FLAG_Y.equals(trans.getIsAdjust())) {
					wh.setAdjust(wh.getAdjust() + bookinfo.getBalance());
				}
				wh.setLastModifiedBy(bookinfo.getModifyBy());
				wh.setLastModifiedDate(new java.util.Date());
				// 更新库存物料记录表
				whremote.update(wh);
			}
	
			/** 更新库位物料记录表 */
			// 判断库位编码是否为空
			if (null != bookinfo.getLocationNo()
					&& !"".equals(bookinfo.getLocationNo())) {
				// 通过仓库编码，库位编码和物料编码获得该条记录
				InvJLocation location = new InvJLocation();
				List<InvJLocation> locationlist = locationremote.findByWHLM(employee.getEnterpriseCode(), 
						bookinfo.getWhsNo(), bookinfo.getLocationNo(), bookinfo
								.getMaterialId());
				if (locationlist.size() > 0) {
					location = locationlist.get(0);
					// 是否影响期初
					if (Constants.FLAG_Y.equals(trans.getIsOpenBalance())) {
						location.setOpenBalance(location.getOpenBalance()
								+ bookinfo.getBalance());
					}
					// 是否影响接收
					if (Constants.FLAG_Y.equals(trans.getIsReceive())) {
						location.setReceipt(location.getReceipt()
								+ bookinfo.getBalance());
					}
					// 是否影响出库
					if (Constants.FLAG_Y.equals(trans.getIsIssues())) {
						location.setIssue(location.getIssue()
								+ bookinfo.getBalance());
					}
					// 是否影响调整
					if (Constants.FLAG_Y.equals(trans.getIsAdjust())) {
						location.setAdjust(location.getAdjust()
								+ bookinfo.getBalance());
					}
					location.setLastModifiedBy(bookinfo.getModifyBy());
					location.setLastModifiedDate(new java.util.Date());
					// 更新库存物料记录表
					locationremote.update(location);
				}
			}
	
			/** 更新批号记录表 */
			// 通过仓库编码，库位编码和物料编码获得该条记录
			InvJLot lot = new InvJLot();
			List<InvJLot> lotlist = lotremote.findByLWHLM(employee.getEnterpriseCode(), 
					bookinfo.getLotNo(), bookinfo.getWhsNo(), bookinfo.getLocationNo(), bookinfo
							.getMaterialId());
			if (lotlist.size() > 0) {
				lot = lotlist.get(0);
				// 是否影响期初
				if (Constants.FLAG_Y.equals(trans.getIsOpenBalance())) {
					lot
							.setOpenBalance(lot.getOpenBalance()
									+ bookinfo.getBalance());
				}
				// 是否影响接收
				if (Constants.FLAG_Y.equals(trans.getIsReceive())) {
					lot.setReceipt(lot.getReceipt() + bookinfo.getBalance());
				}
				// 是否影响出库
				if (Constants.FLAG_Y.equals(trans.getIsIssues())) {
					lot.setIssue(lot.getIssue() + bookinfo.getBalance());
				}
				// 是否影响调整
				if (Constants.FLAG_Y.equals(trans.getIsAdjust())) {
					lot.setAdjust(lot.getAdjust() + bookinfo.getBalance());
				}
				lot.setLastModifiedBy(bookinfo.getModifyBy());
				lot.setLastModifiedDate(new java.util.Date());
				// 更新库存物料记录表
				lotremote.update(lot);
			}
	
			/** 登陆事务历史表 */
			InvJTransactionHis transHis = new InvJTransactionHis();
			// 设置ID
			transHis.setTransHisId(transHisRemote.getMaxId());
			// 设置单号
			transHis.setOrderNo(bookNo);
			// 设置序号
			transHis.setSequenceId(bookinfo.getBookDetailId());
			// 可用
			transHis.setIsUse(Constants.IS_USE_Y);
			// 事务ID
			transHis.setTransId(trans.getTransId());
			// 异动数量
			transHis.setTransQty(bookinfo.getBalance());
			// 设置企业代码
			transHis.setEnterpriseCode(employee.getEnterpriseCode());
			// 设置物料ID
			transHis.setMaterialId(bookinfo.getMaterialId());
			// 修改人
			transHis.setLastModifiedBy(employee.getWorkerCode());
//			transHis.setOrderNo(Constants.NUMBER_ZERO);
//			transHis.setSequenceId(Long.parseLong(Constants.NUMBER_ZERO));
			transHis.setFromWhsNo(bookinfo.getWhsNo());
			transHis.setFromLocationNo(bookinfo.getLocationNo());
			transHis.setLotNo(bookinfo.getLotNo());
			// 修改时间
			transHis.setLastModifiedDate(new java.util.Date());
			transHisRemote.save(transHis);
		} catch (RuntimeException re) {
        
			write("{success:true,msg:'sql'}");
		}
	}
	
	public void findBookNoSelectList() throws JSONException
	{
		List<InvJBook> list = bookremote.findBookNoList(employee.getEnterpriseCode());
		String str = null;
		str = JSONUtil.serialize(list);
		write("{list:"+str+"}");
	}
	
	/**
     * 根据日期日期字符串和形式返回日期
     * @param argDateStr 日期字符串
     * @return 日期
     */
    private Date formatStringToDate(String argDateStr) {
        if (argDateStr == null ||argDateStr.trim().length() < 1) {
            return null;
        }
        
        // 日期形式
        SimpleDateFormat sdfFrom = null;
        // 返回日期
        Date result = null;
        
        try {
        	String strFormat = DATE_FORMAT_YYYYMMDD;
        	if (argDateStr.length() > 10) {
        		strFormat = DATE_FORMAT_YYYYMMDD_TIME;
        	}
            sdfFrom = new SimpleDateFormat(strFormat);
            // 格式化日期
            result = sdfFrom.parse(argDateStr);
        } catch (Exception e) {
            result = null;
        } finally {
            sdfFrom = null;
        }
        
        return result;
    }

    /**
     * 排它异常
     * @author huyou
     *
     */
    private class UsingException extends RuntimeException {
    	/**
		 * serialVersion
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 构造函数
		 */
		public UsingException() {
    		super();
    	}
    	
		/**
		 * 构造函数
		 * @param argMsg 消息
		 */
    	public UsingException(String argMsg) {
    		super(argMsg);
    	}
    }

	/**
	 * @return the bookNo
	 */
	public String getBookNo() {
		return bookNo;
	}

	/**
	 * @param bookNo the bookNo to set
	 */
	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}


}
