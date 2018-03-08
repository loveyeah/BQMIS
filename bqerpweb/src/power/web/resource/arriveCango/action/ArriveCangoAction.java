/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.arriveCango.action;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJTransactionHisFacadeRemote;
import power.ejb.resource.PurJArrival;
import power.ejb.resource.PurJArrivalDetails;
import power.ejb.resource.PurJArrivalDetailsFacadeRemote;
import power.ejb.resource.PurJArrivalFacadeRemote;
import power.ejb.resource.PurJOrderDetails;
import power.ejb.resource.PurJOrderDetailsFacadeRemote;
import power.ejb.resource.business.ArriveCangoFacadeRemote;
import power.ejb.resource.business.IssueManage;
import power.ejb.resource.form.ArriveCangoDetailInfo;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 到货登记
 * 
 * @author zhaozhijie
 * @version 1.0
 */
public class ArriveCangoAction extends AbstractAction{
	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 排他处理 */
	private static final String OTHER_USE = "{success:true,msg:'otherUse'}";
    /** 日期形式字符串 yyyy-MM-dd */
    private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    /** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
    private static final String DATE_FORMAT_YYYYMMDD_TIME = "yyyy-MM-dd HH:mm:ss";
	/** 到货remote */
	private ArriveCangoFacadeRemote remote;
	/** 到货登记remote */
	private PurJArrivalFacadeRemote arrivalRemote;
	/** 到货登记明细remote */
	private PurJArrivalDetailsFacadeRemote arrivalDetailsRemote;
	/** 事务历史remote */
	private InvJTransactionHisFacadeRemote invJTransactionHisFacadeRemote;
	/** 采购单明细remote */
	private PurJOrderDetailsFacadeRemote purJOrderDetailsFacadeRemote;
	/** 结帐remote */
	private IssueManage issueManageRemote;
	/** 到货登记验收bean */
	private PurJArrival arriveCango;
	/** 采货单号/供应商/物资名称: */
	private String queryString;
	/** 回滚处理MSG */
	private static final String ROLL_BACK = "{success:'0',msg:'操作数据库过程中异常终了。',msgid:''}";
	/** 采购单号 */
	private String purNo;
	private String id;
	private String arrivalId;
	private String mifNo;
	private String supplierCode;
	private String memo;
	private boolean flag;
	private String invNo;

	/**
	 * 构造函数
	 */
	public ArriveCangoAction() {
		// remote
		remote = (ArriveCangoFacadeRemote) factory.getFacadeRemote("ArriveCangoFacade");
		arrivalRemote = (PurJArrivalFacadeRemote)factory.getFacadeRemote("PurJArrivalFacade");
		arrivalDetailsRemote = (PurJArrivalDetailsFacadeRemote)factory.getFacadeRemote("PurJArrivalDetailsFacade");
		issueManageRemote = (IssueManage) factory.getFacadeRemote("IssueManageImpl");
		invJTransactionHisFacadeRemote = (InvJTransactionHisFacadeRemote)
		      factory.getFacadeRemote("InvJTransactionHisFacade");
		purJOrderDetailsFacadeRemote = (PurJOrderDetailsFacadeRemote)
		     factory.getFacadeRemote("PurJOrderDetailsFacade");
	}

	/**
	 * 检索采购单
	 * 
	 * @exception JSONException 
	 */
	public void getStockDesc() throws JSONException {
		
		PageObject obj = new PageObject();
		try {
			// 开始行
			String strStart = request.getParameter("start");
			// 行数
			String strLimit = request.getParameter("limit");
			obj = remote.getstockData(queryString, employee.getEnterpriseCode(), Integer.parseInt(strStart), Integer.parseInt(strLimit));
		} catch (ParseException e) {
			e.printStackTrace();
		}	

		// 解析字符串
		String string = null;
		 if(obj.getTotalCount() == null) {
			 string = "{\"list\":[],\"totalCount\":0}";
		 } else {
			 string = JSONUtil.serialize(obj);
		 }
 		write(string);
	}

	/**
	 * 检索物资详细单
	 * 
	 * @exception JSONException 
	 */
	public void getMaterialDetails() throws JSONException {

		PageObject obj = new PageObject();
//		// 开始行
//		String strStart = request.getParameter("start");
//		// 行数
//		String strLimit = request.getParameter("limit");
		try {
			if(flag){
				if (!"".equals(arrivalId) && arrivalId != null){
					obj = remote.findAMaterialDetails(Long.parseLong(arrivalId),
							employee.getEnterpriseCode());
					ArriveCangoDetailInfo arr = null;
					if (obj.getTotalCount() != null && obj.getList() !=null) {
						List<ArriveCangoDetailInfo> list = obj.getList();
						Iterator it = list.iterator();
						while (it.hasNext()) {
							arr = (ArriveCangoDetailInfo) it.next();
							PurJArrival purJArrival = arrivalRemote.findById(arr.getArrivalID());
							arr.setDtArrivalInfo(formatDate(purJArrival.getLastModifiedDate(), DATE_FORMAT_YYYYMMDD_TIME));
							PurJArrivalDetails purJArrivalDetails = arrivalDetailsRemote.findById(arr.getArrivalDID());
							String dtArrivalDetailInfo = formatDate(purJArrivalDetails.getLastModifiedDate(),
									DATE_FORMAT_YYYYMMDD_TIME);
							arr.setDtArrivalDetailInfo(dtArrivalDetailInfo);
							PurJOrderDetails purJOrderDetails = purJOrderDetailsFacadeRemote.findById(arr.getId());
							String dtOrderDetailInfo = formatDate(purJOrderDetails.getLastModifiedDate(),
									DATE_FORMAT_YYYYMMDD_TIME);
							arr.setDtOrderDetailInfo(dtOrderDetailInfo);
						}
					}
				}
			} else {
				obj = remote.findMaterialDetails(purNo,
						employee.getEnterpriseCode());
				ArriveCangoDetailInfo arr = null;
				if (obj.getTotalCount() != null && obj.getList() !=null) {
					List<ArriveCangoDetailInfo> list = obj.getList();
					Iterator it = list.iterator();
					while (it.hasNext()) { 
						arr = (ArriveCangoDetailInfo) it.next();
						PurJOrderDetails purJOrderDetails = purJOrderDetailsFacadeRemote.findById(arr.getId());
						String dtOrderDetailInfo = formatDate(purJOrderDetails.getLastModifiedDate(),
								DATE_FORMAT_YYYYMMDD_TIME);
						arr.setDtOrderDetailInfo(dtOrderDetailInfo);
					}
				}

			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 解析字符串
		String string = null;
		 if(obj.getTotalCount() == null) {
			 string = "{\"list\":[],\"totalCount\":0}";
		 } else {
			 string = JSONUtil.serialize(obj);
		 }
 		write(string);
	}
    
	/**
	 * 检索列表Tab详细单
	 * 
	 * @exception JSONException 
	 */
	public void getTableDetails() throws JSONException, ParseException {

		PageObject obj = new PageObject();
		// 开始行
		String strStart = request.getParameter("start");
		// 行数
		String strLimit = request.getParameter("limit");
		obj = remote.getArriveTabData(employee.getWorkerCode(),
				employee.getEnterpriseCode(), Integer.parseInt(strStart), Integer.parseInt(strLimit));

		// 解析字符串
		String string = null;
		 if(obj.getTotalCount()  == null) {
			 string = "{\"list\":[],\"totalCount\":0}";
		 } else {
			 string = JSONUtil.serialize(obj);
		 }
 		write(string);
	}

	/**
	 * 检索未上报的到货单
	 * @throws ParseException 
	 * 
	 * @exception JSONException 
	 */
	public void getReportArrival() throws ParseException {
		PageObject obj = new PageObject();
		obj = remote.findReportArrivalDetails(purNo, employee.getEnterpriseCode());

		// 解析字符串
		String string = "{success:'',msg:''}";
		 if(obj.getTotalCount() != null) {
			 List<PurJArrival> list = obj.getList();
			 Iterator it = list.iterator();
			 while(it.hasNext()) {
				 PurJArrival data = (PurJArrival) it.next();
				 if (data != null) {
					 if ("0".equals(data.getArrivalState()) || "9".equals(data.getArrivalState())){
						 string = "{success:'"+data.getId().toString() +"',msg:'"+data.getArrivalNo() +"',msgNo:'"+data.getInvoiceNo() +"'}";
					 }
				 }
			 }
		 }
 		write(string);
	}

	/**
	 * 设置列表Tab详细单
	 * 
	 * @exception JSONException 
	 */
	public void setDesc() throws JSONException, ParseException {

		PageObject obj = new PageObject();
		// 开始行
		String strStart = request.getParameter("start");
		// 行数
		String strLimit = request.getParameter("limit");
		if (!"".equals(arrivalId)) {
			obj = remote.findAMaterialDetails(Long.parseLong(arrivalId),
					employee.getEnterpriseCode(), Integer.parseInt(strStart), Integer.parseInt(strLimit));
		}
		
		ArriveCangoDetailInfo arr = null;
		if (obj.getTotalCount() != null && obj.getList() !=null) {
			arr  = (ArriveCangoDetailInfo)obj.getList().get(0);
		}
		// 解析字符串
		String string = null;
		 if(arr == null) {
			 string = "{\"list\":[],\"totalCount\":0}";
		 } else {
			 string = JSONUtil.serialize(arr);
		 }
 		write(string);
	}

	/**
	 * 是否结帐
	 * 
	 * @exception JSONException 
	 */
	public void isReimBurse() throws JSONException {

		String reim = null;
		if (issueManageRemote.isBalanceNow("N")) {
			reim = "1";
		} else {
			reim = "0";
		} 
		
 		write(JSONUtil.serialize(reim));
	}

	/**
	 * 获取合作伙伴id
	 * @throws UnsupportedEncodingException 
	 */
	public void getSupplier() throws UnsupportedEncodingException{
		
		String clientName = request.getParameter("supplierName");
		String clientId = remote.findClientId(
				new String(clientName.getBytes("ISO-8859-1"), "gb2312"), employee.getEnterpriseCode());
		 write(clientId);
	}

	/**
	 * 保存操作
	 * 
	 * @param lstSavePurJArrivalInfo 
	 * @exception JSONException 
	 */
	public void saveStockDesc(List<PurJArrival> lstSavePurJArrivalInfo) throws JSONException {

		arriveCango.setEnterpriseCode(employee.getEnterpriseCode());
		arriveCango.setLastModifiedBy(employee.getWorkerCode());
		arriveCango.setArrivalState("0");
		arriveCango.setLastModifiedDate(new java.util.Date());
		arriveCango.setIsUse(Constants.IS_USE_Y);
		//add by drdu 090618
		arriveCango.setInvoiceNo(invNo);
		// 增加一条记录
		lstSavePurJArrivalInfo.add(arriveCango);
	}

	/**
	 * 保存操作
	 * 
	 * @param lstSavePurJArrivalDetailsInfo 
	 * @exception JSONException 
	 */
	public void saveMaterialDetails(List<PurJArrivalDetails> lstSavePurJArrivalDetailsInfo) throws JSONException {

		PurJArrivalDetails purJArrivalD = new PurJArrivalDetails();
    	String theQty = null;
		String strSaveInfo = request.getParameter("saveInfo");
	    Object object = JSONUtil.deserialize(strSaveInfo);
	    Date lastModifiedDate = new java.util.Date();
        if (object != null) {
            if (List.class.isAssignableFrom(object.getClass())) {
                // 如果是数组
                List lst = (List) object;
                int intLen = lst.size();
                for (int intCnt = 0; intCnt < intLen; intCnt++) {
                	purJArrivalD = new PurJArrivalDetails();
                	theQty=null;
                	if (!"".equals(((Map) lst.get(intCnt)).get("theQty")) && ((Map) lst.get(intCnt)).get("theQty") !=null) {
	                	theQty = ((Map) lst.get(intCnt)).get("theQty").toString();
                	}
                 	purJArrivalD.setEnterpriseCode(employee.getEnterpriseCode());
                	purJArrivalD.setLastModifiedBy(employee.getWorkerCode());
        			if (!"".equals(((Map) lst.get(intCnt)).get("id").toString())){
        				purJArrivalD.setPurLine(Long.parseLong(((Map) lst.get(intCnt)).get("id").toString()));
        			}
        			if (!"".equals(purNo)) {
	        			purJArrivalD.setPurNo(purNo);
        			}
        			if (!"".equals(((Map) lst.get(intCnt)).get("materialID").toString())){
        				purJArrivalD.setMaterialId(Long.parseLong(
        						((Map) lst.get(intCnt)).get("materialID").toString()));
        			}
        			purJArrivalD.setLotCode(((Map) lst.get(intCnt)).get("lotCode").toString());
        			if (!"".equals(theQty) && theQty !=null){
        				purJArrivalD.setTheQty(Double.parseDouble(theQty));
        			} else {
        				purJArrivalD.setTheQty(Double.parseDouble("0"));
        			}
        			purJArrivalD.setMemo(((Map) lst.get(intCnt)).get("detailMemo").toString());

        			purJArrivalD.setBadQty(Double.parseDouble("0"));
        			purJArrivalD.setRcvQty(Double.parseDouble("0"));
        			purJArrivalD.setRecQty(Double.parseDouble("0"));
        			purJArrivalD.setIsUse("Y");
        			purJArrivalD.setLastModifiedDate(lastModifiedDate);
        			purJArrivalD.setItemStatus("0");

        			// 增加一条记录
        			lstSavePurJArrivalDetailsInfo.add(purJArrivalD);
                }
        }
      }
	}

	/**
	 * 更新操作
	 * 
	 * @param lstUpdatePurJArrivaInfo 
	 * @exception JSONException 
	 */
	public void updateStockDesc(List<PurJArrival> lstUpdatePurJArrivaInfo) throws JSONException {

		PurJArrival purJArrival = new PurJArrival();
		purJArrival = arrivalRemote.findById(Long.parseLong(arrivalId));
		purJArrival.setMemo(arriveCango.getMemo());
		purJArrival.setEnterpriseCode(employee.getEnterpriseCode());
		purJArrival.setLastModifiedBy(employee.getWorkerCode());
		purJArrival.setLastModifiedDate(new java.util.Date());
		purJArrival.setIsUse(Constants.IS_USE_Y);
		// add by drdu 090618
		purJArrival.setInvoiceNo(invNo);
		// 更新一条记录
		lstUpdatePurJArrivaInfo.add(purJArrival);
	}

	/**
	 * 更新操作
	 * 
	 * @param lstUpdatePurJArrivalDetailsInfo 
	 * @exception JSONException 
	 */
	public void updateMaterialDetails(List<PurJArrivalDetails> lstUpdatePurJArrivalDetailsInfo) throws JSONException {

		PurJArrivalDetails purJArrivalD = new PurJArrivalDetails();
		String strSaveInfo = request.getParameter("saveInfo");
        Object object = JSONUtil.deserialize(strSaveInfo);
        Date lastModifiedDate=new java.util.Date();
        if (object != null) {
            if (List.class.isAssignableFrom(object.getClass())) {
                // 如果是数组
                List lst = (List) object;
                int intLen = lst.size();
                for (int intCnt = 0; intCnt < intLen; intCnt++) {
                	Long dId = Long.parseLong(((Map) lst.get(intCnt)).get("arrivalDID").toString());
            		purJArrivalD = arrivalDetailsRemote.findById(dId);
            		purJArrivalD.setLastModifiedBy(employee.getWorkerCode());
            		if (((Map) lst.get(intCnt)).get("lotCode") != null) {
	            		purJArrivalD.setLotCode(((Map) lst.get(intCnt)).get("lotCode").toString());
            		}
            		if (((Map) lst.get(intCnt)).get("detailMemo") != null) {
	            		purJArrivalD.setMemo(((Map) lst.get(intCnt)).get("detailMemo").toString());
            		}

        			if (((Map) lst.get(intCnt)).get("theQty") != null &&
        					!"".equals(((Map) lst.get(intCnt)).get("theQty").toString())){
        				purJArrivalD.setTheQty(Double.parseDouble(
        						((Map) lst.get(intCnt)).get("theQty").toString()));
        			}
        			purJArrivalD.setLastModifiedDate(lastModifiedDate);

        			// 更新一条记录
        			lstUpdatePurJArrivalDetailsInfo.add(purJArrivalD);
                }
            }
        }
	}

	/**
	 * 删除操作
	 * 
	 * @param lstDeletePurJArrivalDetailsInfo 
	 * @param lstDeletePurJArrivalInfo 
	 * @exception JSONException 
	 * @throws CodeRepeatException 
	 */
	public void deleteStockDesc(List<PurJArrival> lstDeletePurJArrivalInfo,
			List<PurJArrivalDetails> lstDeletePurJArrivalDetailsInfo) throws JSONException, CodeRepeatException {

		//  删除到货明细记录
	    if (mifNo != null && !"".equals(mifNo)) {
			PageObject obj = new PageObject();
			obj = arrivalDetailsRemote.findByArrivalNo(mifNo, employee.getEnterpriseCode());
			if (obj.getList() != null) {
				List<PurJArrivalDetails> list = obj.getList();
				Iterator it = list.iterator();
				while(it.hasNext()) {
					PurJArrivalDetails purJArrivalDetails = (PurJArrivalDetails) it.next();
					Long ids= purJArrivalDetails.getId();
					PurJArrivalDetails purjArrivalD = arrivalDetailsRemote.findById(ids);

            		if (ids != null) {
						purjArrivalD.setLastModifiedBy(employee.getWorkerCode());
						purjArrivalD.setLastModifiedDate(new java.util.Date());
            			lstDeletePurJArrivalDetailsInfo.add(purjArrivalD);
            		}
				}
			}
	    }

		if (arrivalId != null && !"".equals(arrivalId)) {
			// 删除到货记录
			PurJArrival purJArrival = arrivalRemote.findById(Long.parseLong(arrivalId));
			purJArrival.setLastModifiedBy(employee.getWorkerCode());
			purJArrival.setLastModifiedDate(new java.util.Date());
			lstDeletePurJArrivalInfo.add(purJArrival);

		}
	}

	/**
	 * 上报操作
	 * @param lstSaveInvJTransactionHisInfo 
	 * @param lstUpdatePurJOrderDetailsInfo 
	 * @param lstRptUpdatePurJArrivalDetailsInfo 
	 * @param lstRptUpdatePurJArrivaInfo 
	 * @exception JSONException 
	 */
	public void reportStockDesc(List<PurJArrival> lstRptUpdatePurJArrivaInfo,
			List<PurJArrivalDetails> lstRptUpdatePurJArrivalDetailsInfo,
			List<PurJOrderDetails> lstUpdatePurJOrderDetailsInfo,
			List<InvJTransactionHis> lstSaveInvJTransactionHisInfo) throws JSONException {

		PurJArrival purJArrival = new PurJArrival();
		String strSaveInfo = request.getParameter("saveInfo");
        Object object = JSONUtil.deserialize(strSaveInfo);
        if (object != null) {
            if (List.class.isAssignableFrom(object.getClass())) {
                // 如果是数组
                List lst = (List) object;
                int intLen = lst.size();
    			// 更新到货登记/验收表

                if (arrivalId != null) {
    				purJArrival = arrivalRemote.findById(Long.parseLong(arrivalId));
	    			purJArrival.setEnterpriseCode(employee.getEnterpriseCode());
	    			purJArrival.setLastModifiedBy(employee.getWorkerCode());
	    			purJArrival.setArrivalState("2"); //modify by fyyang 090415
	    			purJArrival.setLastModifiedDate(new java.util.Date());
	    			// 更新一条记录
	    			lstRptUpdatePurJArrivaInfo.add(purJArrival);
                }

                for (int intCnt = 0; intCnt < intLen; intCnt++) {

                	String arrivalDID = "";
                	if (!"".equals(mifNo)) {
            			// 更新到货登记/验收明细表 
            			try {
            				PageObject obj = new PageObject();
            				obj = arrivalDetailsRemote.findByArrivalNo(mifNo, employee.getEnterpriseCode());
    						if (obj.getList() != null) {
    							List<PurJArrivalDetails> list = obj.getList();
    							Iterator it = list.iterator();
    							while(it.hasNext()) {
    								PurJArrivalDetails purJArrivalD = (PurJArrivalDetails) it.next();
    		                		if (((Map) lst.get(intCnt)).get("arrivalDID").toString() != null &&
    		                				!"".equals(((Map) lst.get(intCnt)).get("arrivalDID").toString())) {
    		                			arrivalDID = ((Map) lst.get(intCnt)).get("arrivalDID").toString();
    		                			
    		                			if (arrivalDID.equals(purJArrivalD.getId().toString())) {
    	    		    					PurJArrivalDetails purjArrivalD = arrivalDetailsRemote.findById(purJArrivalD.getId());
    	    		            			if (((Map) lst.get(intCnt)).get("theQty")!=null &&
    	    		            					!"".equals(((Map) lst.get(intCnt)).get("theQty").toString())){
    	    		    	        			purJArrivalD.setRcvQty(Double.parseDouble(
    	    		    	        					((Map) lst.get(intCnt)).get("theQty").toString()));
    	    		            			}
    	    		            			purJArrivalD.setLastModifiedBy(employee.getWorkerCode());
    	    		            			purJArrivalD.setLastModifiedDate(new java.util.Date());
    	    		            			//purJArrivalD.setItemStatus("3");//modify by fyyang 090423  090507
    	    		            			// 更新一条记录
    	    		            			lstRptUpdatePurJArrivalDetailsInfo.add(purJArrivalD);
    		                			}
    		                		}
    							}
    						}
    					} catch (CodeRepeatException e) {
    						e.printStackTrace();
    					}
                	}
        			PurJOrderDetails purJOrderDetails = new PurJOrderDetails();
        			// 更新采购订单明细表
        			purJOrderDetails = purJOrderDetailsFacadeRemote.findById(
        					Long.parseLong(((Map) lst.get(intCnt)).get("id").toString()));
        			if (((Map) lst.get(intCnt)).get("theQty")!=null &&
        					!"".equals(((Map) lst.get(intCnt)).get("theQty").toString())){
            			purJOrderDetails.setInsQty(Double.parseDouble(
	        					((Map) lst.get(intCnt)).get("theQty").toString()) + purJOrderDetails.getInsQty());
        			}

        			purJOrderDetails.setLastModifiedBy(employee.getWorkerCode());
        			purJOrderDetails.setLastModifiedDate(new java.util.Date());
        			lstUpdatePurJOrderDetailsInfo.add(purJOrderDetails);

        			InvJTransactionHis invJTransactionHis = new InvJTransactionHis();
        			// 插入事物历史表
        			if (!"".equals(mifNo)) {
            			invJTransactionHis.setOrderNo(mifNo);
        			} else {
        				invJTransactionHis.setOrderNo("");
        			}
        			if (!"".equals(arrivalDID)){
	        			invJTransactionHis.setSequenceId(Long.parseLong(arrivalDID));
        			} else {
        				invJTransactionHis.setSequenceId(Long.parseLong("0"));
        			}
	        		invJTransactionHis.setTransId(Long.parseLong(remote.findTransId("N",
	        				employee.getEnterpriseCode())));
        			if (((Map) lst.get(intCnt)).get("materialID")!=null &&
        					!"".equals(((Map) lst.get(intCnt)).get("materialID").toString())){
	        			invJTransactionHis.setMaterialId(
	        					Long.parseLong(((Map) lst.get(intCnt)).get("materialID").toString()));
        			} else {
        				invJTransactionHis.setMaterialId(Long.parseLong("0"));
        			}
        			if (((Map) lst.get(intCnt)).get("lotCode") != null) {
            			invJTransactionHis.setLotNo(((Map) lst.get(intCnt)).get("lotCode").toString());
        			}
        			if (((Map) lst.get(intCnt)).get("theQty")!=null &&
        					!"".equals(((Map) lst.get(intCnt)).get("theQty").toString())){
        				invJTransactionHis.setTransQty(Double.parseDouble(
        						((Map) lst.get(intCnt)).get("theQty").toString()));
        			} else {
        				invJTransactionHis.setTransQty(Double.parseDouble("0"));
        			}
        			invJTransactionHis.setEnterpriseCode(employee.getEnterpriseCode());
        			if (((Map) lst.get(intCnt)).get("purUm")!=null  &&
        					!"".equals(((Map) lst.get(intCnt)).get("purUm").toString())){
	        			invJTransactionHis.setTransUmId(
	        					Long.parseLong(((Map) lst.get(intCnt)).get("purUm").toString()));
        			}
        			if(!"".equals(supplierCode) && supplierCode != null){
            			invJTransactionHis.setSupplier(Long.parseLong(supplierCode));
        			}
        			if (((Map) lst.get(intCnt)).get("detailMemo")!=null  &&
        					!"".equals(((Map) lst.get(intCnt)).get("detailMemo").toString())) {
            			invJTransactionHis.setMemo(((Map) lst.get(intCnt)).get("detailMemo").toString());
        			}
        			invJTransactionHis.setIsUse(Constants.IS_USE_Y);
        			invJTransactionHis.setLastModifiedDate(new java.util.Date());
        			invJTransactionHis.setLastModifiedBy(employee.getWorkerCode());
        			lstSaveInvJTransactionHisInfo.add(invJTransactionHis);
                }
            }
        }
	}

	/**
	 * 到货登记保存操作
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void updateArrival() throws JSONException {
		try{
			boolean flagUpdate = false;
			// 判断是变更还是插入
			if (!"".equals(request.getParameter("flagUpdate"))){
				flagUpdate = Boolean
				.parseBoolean(request.getParameter("flagUpdate"));
			}

			// 到货登记/验收表最后修改时间
			String dtArrivalInfo = "";
			//  到货登记/验收明细表最后修改时间
			Map<Long, String> dtArrivalDetailInfo = new HashMap<Long, String>();

			// 保存到货登记/验收表
			List<PurJArrival> lstSavePurJArrivalInfo = new ArrayList<PurJArrival>();
			//更新到货登记/验收表
			List<PurJArrival> lstUpdatePurJArrivaInfo = new ArrayList<PurJArrival>();
			// 保存到货登记/验收明细表
			List<PurJArrivalDetails> lstSavePurJArrivalDetailsInfo = new ArrayList<PurJArrivalDetails>();
			// 更新到货登记/验收明细表
			List<PurJArrivalDetails> lstUpdatePurJArrivalDetailsInfo = new ArrayList<PurJArrivalDetails>();

			String strSaveInfo = request.getParameter("saveInfo");
	        Object object = JSONUtil.deserialize(strSaveInfo);
	        if (object != null) {
	            if (List.class.isAssignableFrom(object.getClass())) {
	                // 如果是数组
	                List lst = (List) object;
	                int intLen = lst.size();
	                for (int intCnt = 0; intCnt < intLen; intCnt++) {
	                	  if (((Map) lst.get(intCnt)).get("dtArrivalInfo") != null &&
	                			  !"".equals(((Map) lst.get(intCnt)).get("dtArrivalInfo"))) {
	 	                     dtArrivalInfo = ((Map) lst.get(intCnt)).get("dtArrivalInfo").toString();
	                	  }
	                	  if (((Map) lst.get(intCnt)).get("arrivalDID") != null &&
	                			  !"".equals(((Map) lst.get(intCnt)).get("arrivalDID")) &&
	                			  ((Map) lst.get(intCnt)).get("dtArrivalDetailInfo") != null &&
	                			  !"".equals(((Map) lst.get(intCnt)).get("dtArrivalDetailInfo"))) {
	 	                      dtArrivalDetailInfo.put(Long.parseLong(((Map) lst.get(intCnt)).get("arrivalDID").toString()),
		                    		 ((Map) lst.get(intCnt)).get("dtArrivalDetailInfo").toString());
	                	  }
	                }
	            }
	       }

			// 更新操作
			if (flagUpdate) {
				// 更新到货登记/验收表
				updateStockDesc(lstUpdatePurJArrivaInfo);
				updateMaterialDetails(lstUpdatePurJArrivalDetailsInfo);
				PurJArrival purJArrival = arrivalRemote.findById(lstUpdatePurJArrivaInfo.get(0).getId());
				String newDate = formatDate(purJArrival.getLastModifiedDate(),DATE_FORMAT_YYYYMMDD_TIME);
				// 排他操作
				if (!newDate.equals(dtArrivalInfo)) {
					write(OTHER_USE);
					return;
				} else {
					for (int i = 0; i < lstUpdatePurJArrivalDetailsInfo.size(); i++) {
						PurJArrivalDetails purJArrivalD =
							arrivalDetailsRemote.findById(lstUpdatePurJArrivalDetailsInfo.get(i).getId());
						Long dId = purJArrivalD.getId();
						String newDetailDate = formatDate(purJArrivalD.getLastModifiedDate(),
								DATE_FORMAT_YYYYMMDD_TIME);
						
						// 排他操作
		        		if (!(newDetailDate.equals
		        				((dtArrivalDetailInfo.get(dId)).toString()))) {
							write(OTHER_USE);
							return;
		        		}
					}
				}
			// 保存操作
			} else {
				saveStockDesc(lstSavePurJArrivalInfo);
				saveMaterialDetails(lstSavePurJArrivalDetailsInfo);
			}

			// 回滚处理
			remote.saveRegister(lstUpdatePurJArrivaInfo, lstUpdatePurJArrivalDetailsInfo,
					lstSavePurJArrivalInfo, lstSavePurJArrivalDetailsInfo);

			String msg = "{success:true,msg:''}";
			String arrivalNo = "";
			String id = "";
		//	String invoiceNo = "";
			// 更新操作
			if (flagUpdate) {
				arrivalNo = lstUpdatePurJArrivaInfo.get(0).getArrivalNo();
				id = lstUpdatePurJArrivaInfo.get(0).getId().toString();
			// 保存操作
			} else {
			    if (arrivalRemote.findMaxArrivalNo() != null) {
			    	arrivalNo = arrivalRemote.findMaxArrivalNo();
			    //	invoiceNo = arrivalRemote.findByArrivalNo(arrivalNo).getInvoiceNo();
			      }
		    	id = arrivalRemote.findMaxId().toString();
			}
		  //   msg = "{success:true,msg:'"+arrivalNo+ "',msgid:'" +id +"',msgNo:'"+invoiceNo+ "'}";
			msg = "{success:true,msg:'"+arrivalNo+ "',msgid:'" +id +"'}";
			write(msg); 
		}catch(RuntimeException e) {
			write(ROLL_BACK);
		}catch (Exception e) {
			write("{success:false,msg:''}");
		}
	
	}

	/**
	 * 到货登记删除操作
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void deleteArrival() throws JSONException {
		try {
			// 到货登记/验收表最后修改时间
			String dtArrivalInfo = "";
			//  到货登记/验收明细表最后修改时间
			Map<Long, String> dtArrivalDetailInfo = new HashMap<Long, String>();

			// 删除到货登记/验收表
			List<PurJArrival> lstDeletePurJArrivalInfo = new ArrayList<PurJArrival>();
			// 删除到货登记/验收明细表
			List<PurJArrivalDetails> lstDeletePurJArrivalDetailsInfo = new ArrayList<PurJArrivalDetails>();
			// 删除操作
			deleteStockDesc(lstDeletePurJArrivalInfo,lstDeletePurJArrivalDetailsInfo);

			String strSaveInfo = request.getParameter("saveInfo");
	        Object object = JSONUtil.deserialize(strSaveInfo);
	        if (object != null) {
	            if (List.class.isAssignableFrom(object.getClass())) {
	                // 如果是数组
	                List lst = (List) object;
	                int intLen = lst.size();
	                for (int intCnt = 0; intCnt < intLen; intCnt++) {
	                	  if (((Map) lst.get(intCnt)).get("dtArrivalInfo") != null &&
	                			  !"".equals(((Map) lst.get(intCnt)).get("dtArrivalInfo"))) {
	 	                     dtArrivalInfo = ((Map) lst.get(intCnt)).get("dtArrivalInfo").toString();
	                	  }
	                	  if (((Map) lst.get(intCnt)).get("arrivalDID") != null &&
	                			  !"".equals(((Map) lst.get(intCnt)).get("arrivalDID")) &&
	                			  ((Map) lst.get(intCnt)).get("dtArrivalDetailInfo") != null &&
	                			  !"".equals(((Map) lst.get(intCnt)).get("dtArrivalDetailInfo"))) {
	 	                      dtArrivalDetailInfo.put(Long.parseLong(((Map) lst.get(intCnt)).get("arrivalDID").toString()),
		                    		 ((Map) lst.get(intCnt)).get("dtArrivalDetailInfo").toString());
	                	  }
	                }
	            }
	       }

			// 排他操作
			PurJArrival purJArrival = arrivalRemote.findById(lstDeletePurJArrivalInfo.get(0).getId());
			String newDate = formatDate(purJArrival.getLastModifiedDate(),DATE_FORMAT_YYYYMMDD_TIME);

			if (!newDate.equals(dtArrivalInfo)) {
				write(OTHER_USE);
				return;
			} else {
				for (int i = 0; i < lstDeletePurJArrivalDetailsInfo.size(); i++) {
					PurJArrivalDetails purjArrivalD =
						arrivalDetailsRemote.findById(lstDeletePurJArrivalDetailsInfo.get(i).getId());
					Long ids = purjArrivalD.getId();
					String newDetailDate =  formatDate(purjArrivalD.getLastModifiedDate(),
							DATE_FORMAT_YYYYMMDD_TIME);
		    		if (!(newDetailDate.equals(
		    				(dtArrivalDetailInfo.get(ids)).toString()))) {
						write(OTHER_USE);
						return;
		    		}
				}

			}

			// 回滚处理
			remote.deleteRegister(lstDeletePurJArrivalInfo,lstDeletePurJArrivalDetailsInfo);
			write("{success:true,msg:'Use'}"); 
		} catch(RuntimeException e) {
			write(ROLL_BACK);
		}catch (Exception e) {
//			write(ROLL_BACK);
		}
	}

	/**
	 * 到货登记上报操作
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void reportArrival() throws JSONException {
		try {
			// 上报中更新到货登记/验收表
			List<PurJArrival> lstRptUpdatePurJArrivaInfo = new ArrayList<PurJArrival>();
			// 上报中更新到货登记/验收明细表
			List<PurJArrivalDetails> lstRptUpdatePurJArrivalDetailsInfo = new ArrayList<PurJArrivalDetails>();
			// 更新采购订单明细表
			List<PurJOrderDetails> lstUpdatePurJOrderDetailsInfo = new ArrayList<PurJOrderDetails>();
			// 更新事务历史表
			List<InvJTransactionHis> lstSaveInvJTransactionHisInfo = new ArrayList<InvJTransactionHis>(); 	
			// 到货登记/验收表最后修改时间
			String dtArrivalInfo = "";
			//  到货登记/验收明细表最后修改时间
			Map<Long, String> dtArrivalDetailInfo = new HashMap<Long, String>();
			// 采购单明细最后修改时间
			Map<Long, String> dtOrderDetailInfo = new HashMap<Long, String>();

			
			// 上报操作
			reportStockDesc(lstRptUpdatePurJArrivaInfo,lstRptUpdatePurJArrivalDetailsInfo,
					lstUpdatePurJOrderDetailsInfo,lstSaveInvJTransactionHisInfo);

			String strSaveInfo = request.getParameter("saveInfo");
	        Object object = JSONUtil.deserialize(strSaveInfo);
	        if (object != null) {
	            if (List.class.isAssignableFrom(object.getClass())) {
	                // 如果是数组
	                List lst = (List) object;
	                int intLen = lst.size();
	                for (int intCnt = 0; intCnt < intLen; intCnt++) {
	                	  if (((Map) lst.get(intCnt)).get("dtArrivalInfo") != null &&
	                			  !"".equals(((Map) lst.get(intCnt)).get("dtArrivalInfo"))) {
	 	                     dtArrivalInfo = ((Map) lst.get(intCnt)).get("dtArrivalInfo").toString();
	                	  }
	                	  if (((Map) lst.get(intCnt)).get("arrivalDID") != null &&
	                			  !"".equals(((Map) lst.get(intCnt)).get("arrivalDID")) &&
	                			  ((Map) lst.get(intCnt)).get("dtArrivalDetailInfo") != null &&
	                			  !"".equals(((Map) lst.get(intCnt)).get("dtArrivalDetailInfo"))) {
	 	                      dtArrivalDetailInfo.put(Long.parseLong(((Map) lst.get(intCnt)).get("arrivalDID").toString()),
		                    		 ((Map) lst.get(intCnt)).get("dtArrivalDetailInfo").toString());
	                	  }
	                	  if (((Map) lst.get(intCnt)).get("id") != null &&
	                			  !"".equals(((Map) lst.get(intCnt)).get("id")) &&
	                			  ((Map) lst.get(intCnt)).get("dtOrderDetailInfo") != null &&
	                			  !"".equals(((Map) lst.get(intCnt)).get("dtOrderDetailInfo"))) {
	 	                     dtOrderDetailInfo.put(Long.parseLong(((Map) lst.get(intCnt)).get("id").toString()),
		                    		 ((Map) lst.get(intCnt)).get("dtOrderDetailInfo").toString());
	                	  }

	                }
	            }
	       }
			// 排他操作
			PurJArrival purJArrival =arrivalRemote.findById(lstRptUpdatePurJArrivaInfo.get(0).getId());
			String newDate = formatDate(purJArrival.getLastModifiedDate(),DATE_FORMAT_YYYYMMDD_TIME);

			if (!newDate.equals(dtArrivalInfo)) {
				write(OTHER_USE);
				return;
			} else {
				for (int i = 0; i < lstRptUpdatePurJArrivalDetailsInfo.size(); i++) {
					PurJArrivalDetails purjArrivalD =
						arrivalDetailsRemote.findById(lstRptUpdatePurJArrivalDetailsInfo.get(i).getId());
					Long ids = purjArrivalD.getId();
					String newDetailDate = formatDate(purjArrivalD.getLastModifiedDate(),DATE_FORMAT_YYYYMMDD_TIME);
		    		if (!(newDetailDate.equals(
		    				(dtArrivalDetailInfo.get(ids)).toString()))) {
						write(OTHER_USE);
						return;
		    		}
				}
				for (int i = 0; i < lstUpdatePurJOrderDetailsInfo.size(); i++) {
					PurJOrderDetails purjOrderD = purJOrderDetailsFacadeRemote.findById(
							lstUpdatePurJOrderDetailsInfo.get(i).getPurOrderDetailsId());
					Long ids = purjOrderD.getPurOrderDetailsId();
					String newOrderDate = formatDate(purjOrderD.getLastModifiedDate(),DATE_FORMAT_YYYYMMDD_TIME);
		    		if (!(newOrderDate.equals(
		    				(dtOrderDetailInfo.get(ids)).toString()))) {
						write(OTHER_USE);
						return;
		    		}
				}
			} 
			// 回滚处理
			remote.reportRegister(lstRptUpdatePurJArrivaInfo,lstRptUpdatePurJArrivalDetailsInfo,
					lstUpdatePurJOrderDetailsInfo,lstSaveInvJTransactionHisInfo);
			write("{success:true,msg:'Use'}"); 
		} catch(RuntimeException e) {
			write(ROLL_BACK);
		}catch (Exception e) {
//			write(ROLL_BACK);
		}
	}

	/**
	 * 获取登录用户
	 */
	public void getArrivalWorkerCode() {
		// 为空则返回
		if(employee == null){
			write(Constants.BLANK_STRING);
			return;
		}
		// 返回登录用户的名字
		write(employee.getWorkerName());
	}

	/**
	 * 获取单位名称
	 */
	@SuppressWarnings("unchecked")
	public void getRB003UnitName(){
		BpCMeasureUnitFacadeRemote unitRemote = (BpCMeasureUnitFacadeRemote) factory
		.getFacadeRemote("BpCMeasureUnitFacade");

		String unitCode = request.getParameter("unitCode");
		if(unitCode==null || (Constants.BLANK_STRING.equals(unitCode)) || "null".equals(unitCode)){
			unitCode="0";
		}
		BpCMeasureUnit obj  = unitRemote.findById(Long.parseLong(unitCode));
		if(obj!=null){
			write(obj.getUnitName());
		}else{
			write(Constants.BLANK_STRING);
		}


	}
	 /**
     * 根据日期和形式返回日期字符串
     * @param argDate 日期
     * @param argFormat 日期形式字符串
     * @return 日期字符串
     */
    private String formatDate(Date argDate, String argFormat) {
        if (argDate == null) {
            return "";
        }
        
        // 日期形式
        SimpleDateFormat sdfFrom = null;
        // 返回字符串
        String strResult = null;

        try {
            sdfFrom = new SimpleDateFormat(argFormat);
            // 格式化日期
            strResult = sdfFrom.format(argDate).toString();
        } catch (Exception e) {
            strResult = "";
        } finally {
            sdfFrom = null;
        }

        return strResult;
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
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * @return the purNo
	 */
	public String getPurNo() {
		return purNo;
	}

	/**
	 * @param purNo the purNo to set
	 */
	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}

	/**
	 * @return the arriveCango
	 */
	public PurJArrival getArriveCango() {
		return arriveCango;
	}

	/**
	 * @param arriveCango the arriveCango to set
	 */
	public void setArriveCango(PurJArrival arriveCango) {
		this.arriveCango = arriveCango;
	}

	/**
	 * @return the mifNo
	 */
	public String getMifNo() {
		return mifNo;
	}

	/**
	 * @param mifNo the mifNo to set
	 */
	public void setMifNo(String mifNo) {
		this.mifNo = mifNo;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the supplierCode
	 */
	public String getSupplierCode() {
		return supplierCode;
	}

	/**
	 * @param supplierCode the supplierCode to set
	 */
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	/**
	 * @return the arrivalId
	 */
	public String getArrivalId() {
		return arrivalId;
	}

	/**
	 * @param arrivalId the arrivalId to set
	 */
	public void setArrivalId(String arrivalId) {
		this.arrivalId = arrivalId;
	}

	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

}
