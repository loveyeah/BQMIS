package power.web.resource.transReason;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvCReason;
import power.ejb.resource.InvCReasonFacadeRemote;
import power.ejb.resource.InvCTransaction;
import power.ejb.resource.InvCTransactionFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

public class TransReasonAction extends AbstractAction {
	/** serial id*/
	private static final long serialVersionUID = 1L;
	/** 事务作用remote*/
	private InvCTransactionFacadeRemote transRemote;
	/** 事务原因码remote*/
	private InvCReasonFacadeRemote reasonRemote;
	/** 事务作用bean*/
	private InvCTransaction trans;
	/** 用于记录事务id是否变更过 */
	private boolean transIdFlag = false;
	/**
	 * 构造函数
	 */
	public TransReasonAction() {
		// 事务作用remote
		transRemote = (InvCTransactionFacadeRemote) factory.getFacadeRemote("InvCTransactionFacade");
		// 事务原因码remote
		reasonRemote = (InvCReasonFacadeRemote) factory.getFacadeRemote("InvCReasonFacade");
	}
	
	/**
	 * 模糊查询事务作用的信息
	 * @throws JSONException
	 */
	public void getTransList() throws JSONException {
		// 查询字符串
		String fuzzy = request.getParameter("fuzzy");
		if(fuzzy == null || fuzzy.length() == 0){
			// 检索所有
			fuzzy = Constants.ALL_DATA;
		}
		// 开始行
		String strStart = request.getParameter("start");
		// 行数
		String strLimit = request.getParameter("limit");
		// 查询
		PageObject obj = transRemote.findByFuzzy(fuzzy,employee.getEnterpriseCode(),Integer.parseInt(strStart), Integer.parseInt(strLimit));
		// 解析字符串
		String str = null;
		str = JSONUtil.serialize(obj);
		write(str);
	}
	/**
	 * 查询事务原因码的信息
	 * @throws JSONException
	 */
	public void getReasonList() throws JSONException {
		// 查询字符串
		String transId = request.getParameter("transId");
		// 开始行
		String strStart = request.getParameter("start");
		// 行数
		String strLimit = request.getParameter("limit");
		// 查询
		PageObject obj = reasonRemote.findByTransId(transId, employee.getEnterpriseCode(), Integer.parseInt(strStart), Integer.parseInt(strLimit));
		
		// 解析字符串
		String str = null;
		str = JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 保存事务作用及事务原因码信息
	 * @throws JSONException
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 * @throws CodeRepeatException 
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unchecked")
	public void saveTransAndReason() throws JSONException, NumberFormatException, ParseException, CodeRepeatException {
	
		// 事务作用信息是否变更
		String objIsFormChanged = request.getParameter("isFormChanged");
		boolean isFormChanged = Boolean.parseBoolean(objIsFormChanged);
		// 事务原因码信息是否变更
		String objIsReasonChanged  = request.getParameter("isReasonChanged");
		boolean isReasonChanged  = Boolean.parseBoolean(objIsReasonChanged);
		// 新增加的事务作用
		String newReason = request.getParameter("newReason");
		// 原db中修改的记录
		String dbReason = request.getParameter("dbReason");
		// 已删除的Id集
		String deleteReasonIds = request.getParameter("deleteReasonIds");
		// 新记录
		List<Map> newReasons = (List<Map>)JSONUtil.deserialize(newReason);
		// 修改的记录
		List<Map> dbReasons = (List<Map>)JSONUtil.deserialize(dbReason);
		// 重复性check
		String msg = "{success:false,msg:'";
		String msgSr = "{success:false,msg:'";
		//check[事务编码],[事务名称]在DB中的唯一性
		if(isFormChanged) {
			// 新增时判断
			
			if (trans.getTransId() == null) {
				msg += checkTransNoAndName("isNew"); 
			} else { 
				//修改时check
			    msg += checkTransNoAndName("isOld");
			}
		}
		
		
		if(!msgSr.equals(msg)) {
			msg += "'}";
			write(msg);
			return;
		}
		
		// 如果事务作用信息变更,保存事务作用信息
		if(isFormChanged && trans.getTransId()!= null){
			try {
				// 修改事务作用信息
				updateTrans(trans.getTransId());
			}catch (RuntimeException e) {
				write("{success:false,msg:'操作数据库过程中异常终了。'}");
				return;
			}
		}else if(isFormChanged && trans.getTransId()== null ) {
			try {
				// 增加事务作用信息
				addTrans();
				// 标志事务作用是后来增加的
				transIdFlag = true;
			}catch (RuntimeException e) {
				write("{success:false,msg:'操作数据库过程中异常终了。'}");
				return;
			}
		}
		// 如果事务原因码信息变更
		if(isReasonChanged){
			// 如果是新增加的事务作用,不做事务原因删除
			if(trans.getTransId() != null && !transIdFlag) {
				// 如果有被删除的记录,从db中删除
				try{
					deleteReasons(deleteReasonIds);
				}catch(RuntimeException e) {
					write(Constants.DELETE_FAILURE);
					return;
				}
			}
			// 增加事务原因码信息
			if(newReasons.size() > 0){
				try {
					addReasons(newReasons);
				} catch (RuntimeException e) {
					write("{success:false,msg:'操作数据库过程中异常终了。'}");
					return;
				}
			}
			// 修改db记录
			if(dbReasons.size() > 0) {
				try {
					updateReasons(dbReasons);
				} catch (RuntimeException e) {
					write("{success:false,msg:'操作数据库过程中异常终了。'}");
					return;
				}
			}
		}
		write("{success:true,msg:'  保存成功！'}");
	}

	/**
	 * 修改事务作用信息
	 * @param whsId 事务作用流水号
	 * @throws RuntimeException 
	 */
	private void updateTrans(Long transId)   {
		try {
			// 找到对应事务作用记录
			InvCTransaction entity = transRemote.findById(trans.getTransId());
			// 事务编码 
			entity.setTransCode(trans.getTransCode());
			// 事务名称 
			entity.setTransName(trans.getTransName());
			//事务说明 
			entity.setTransDesc(getString(trans.getTransDesc()));
			// 是否影响期初  
			if(!"Y".equals(trans.getIsOpenBalance())){
				entity.setIsOpenBalance("N");
			}else {
				entity.setIsOpenBalance("Y");
			}
			// 是否影响接收  
			if(!"Y".equals(trans.getIsReceive())){
				entity.setIsReceive("N");
			}else {
				entity.setIsReceive("Y");
			}
			// 是否影响调整  
			if(!"Y".equals(trans.getIsAdjust())){
				entity.setIsAdjust("N");
			}else {
				entity.setIsAdjust("Y");
			}
			// 是否影响出货  
			if(!"Y".equals(trans.getIsIssues())){
				entity.setIsIssues("N");
			}else {
				entity.setIsIssues("Y");
			}
			// 是否影响预留  
			if(!"Y".equals(trans.getIsReserved())){
				entity.setIsReserved("N");
			}else {
				entity.setIsReserved("Y");
			}
			// 是否影响暂收  
			if(!"Y".equals(trans.getIsInspection())){
				entity.setIsInspection("N");
			}else {
				entity.setIsInspection("Y");
			}
			// 是否影响销售金额  
			if(!"Y".equals(trans.getIsSaleAmount())){
				entity.setIsSaleAmount("N");
			}else {
				entity.setIsSaleAmount("Y");
			}
			// 是否影响账目成本  
			if(!"Y".equals(trans.getIsEntryCost())){
				entity.setIsEntryCost("N");
			}else {
				entity.setIsEntryCost("Y");
			}
			// 是否影响采购成本  
			if(!"Y".equals(trans.getIsPoCost())){
				entity.setIsPoCost("N");
			}else {
				entity.setIsPoCost("Y");
			}
			// 是否影响调整成本  
			if(!"Y".equals(trans.getIsAjustCost())){
				entity.setIsAjustCost("N");
			}else {
				entity.setIsAjustCost("Y");
			}
			// 是否影响实际成本  
			if(!"Y".equals(trans.getIsActualCost())){
				entity.setIsActualCost("N");
			}else {
				entity.setIsActualCost("Y");
			}
			// 需要验证采购单  
			if(!"Y".equals(trans.getIsCheckPo())){
				entity.setIsCheckPo("N");
			}else {
				entity.setIsCheckPo("Y");
			}
			// 影响采购单到货数量  
			if(!"Y".equals(trans.getIsPoQuantity())){
				entity.setIsPoQuantity("N");
			}else {
				entity.setIsPoQuantity("Y");
			}
			// 是否影响工单  
			if(!"Y".equals(trans.getIsShopOrder())){
				entity.setIsShopOrder("N");
			}else {
				entity.setIsShopOrder("Y");
			}
			// 是否需要验证工单  
			if(!"Y".equals(trans.getIsCheckShopOrder())){
				entity.setIsCheckShopOrder("N");
			}else {
				entity.setIsCheckShopOrder("Y");
			}
			// 是否影响工单发料  
			if(!"Y".equals(trans.getIsShopOrderIssue())){
				entity.setIsShopOrderIssue("N");
			}else {
				entity.setIsShopOrderIssue("Y");
			}
			// 上次修改人  
			entity.setLastModifiedBy(employee.getWorkerCode());
			// 上次修改日期  
			// 企业代码  
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			
			transRemote.update(entity);
		} catch (RuntimeException e){
			throw e;
		}
	}
	
	/**
	 * 增加一条事务作用信息记录
	 * @throws RuntimeException
	 */
	private void addTrans()   {
		InvCTransaction entity = new InvCTransaction();
		// 事务编码 
		entity.setTransCode(trans.getTransCode());
		// 事务名称 
		entity.setTransName(trans.getTransName());
		//事务说明 
		entity.setTransDesc(getString(trans.getTransDesc()));
		// 是否影响期初  
		if(!"Y".equals(trans.getIsOpenBalance())){
			entity.setIsOpenBalance("N");
		}else {
			entity.setIsOpenBalance("Y");
		}
		// 是否影响接收  
		if(!"Y".equals(trans.getIsReceive())){
			entity.setIsReceive("N");
		}else {
			entity.setIsReceive("Y");
		}
		// 是否影响调整  
		if(!"Y".equals(trans.getIsAdjust())){
			entity.setIsAdjust("N");
		}else {
			entity.setIsAdjust("Y");
		}
		// 是否影响出货  
		if(!"Y".equals(trans.getIsIssues())){
			entity.setIsIssues("N");
		}else {
			entity.setIsIssues("Y");
		}
		// 是否影响预留  
		if(!"Y".equals(trans.getIsReserved())){
			entity.setIsReserved("N");
		}else {
			entity.setIsReserved("Y");
		}
		// 是否影响暂收  
		if(!"Y".equals(trans.getIsInspection())){
			entity.setIsInspection("N");
		}else {
			entity.setIsInspection("Y");
		}
		// 是否影响销售金额  
		if(!"Y".equals(trans.getIsSaleAmount())){
			entity.setIsSaleAmount("N");
		}else {
			entity.setIsSaleAmount("Y");
		}
		// 是否影响账目成本  
		if(!"Y".equals(trans.getIsEntryCost())){
			entity.setIsEntryCost("N");
		}else {
			entity.setIsEntryCost("Y");
		}
		// 是否影响采购成本  
		if(!"Y".equals(trans.getIsPoCost())){
			entity.setIsPoCost("N");
		}else {
			entity.setIsPoCost("Y");
		}
		// 是否影响调整成本  
		if(!"Y".equals(trans.getIsAjustCost())){
			entity.setIsAjustCost("N");
		}else {
			entity.setIsAjustCost("Y");
		}
		// 是否影响实际成本  
		if(!"Y".equals(trans.getIsActualCost())){
			entity.setIsActualCost("N");
		}else {
			entity.setIsActualCost("Y");
		}
		// 需要验证采购单  
		if(!"Y".equals(trans.getIsCheckPo())){
			entity.setIsCheckPo("N");
		}else {
			entity.setIsCheckPo("Y");
		}
		// 影响采购单到货数量  
		if(!"Y".equals(trans.getIsPoQuantity())){
			entity.setIsPoQuantity("N");
		}else {
			entity.setIsPoQuantity("Y");
		}
		// 是否影响工单  
		if(!"Y".equals(trans.getIsShopOrder())){
			entity.setIsShopOrder("N");
		}else {
			entity.setIsShopOrder("Y");
		}
		// 是否需要验证工单  
		if(!"Y".equals(trans.getIsCheckShopOrder())){
			entity.setIsCheckShopOrder("N");
		}else {
			entity.setIsCheckShopOrder("Y");
		}
		// 是否影响工单发料  
		if(!"Y".equals(trans.getIsShopOrderIssue())){
			entity.setIsShopOrderIssue("N");
		}else {
			entity.setIsShopOrderIssue("Y");
		}
		// 上次修改人  
		entity.setLastModifiedBy(employee.getWorkerCode());
		// 企业编码  
		entity.setEnterpriseCode(employee.getEnterpriseCode());
		// 添加
		try {
			transRemote.save(entity);	
			// 查找transId
			entity = transRemote.checkTransNameForUpdate(entity.getTransName(),employee.getEnterpriseCode());
			// 设置transId和变更标志位
			trans.setTransId(entity.getTransId());
		} catch (RuntimeException e) {
		    throw e;
		}
	}
	
	/**
	 * 删除事务原因信息
	 * @param deleteLocationIds 要删除的事务原因ID集
	 * @throws RuntimeException
	 */
	private void deleteReasons(String deleteReasonIds) {
		// 如果id集为空不需要删除
		if(deleteReasonIds == null || deleteReasonIds.length() < 1){
			return;
		}
		String[]ids = deleteReasonIds.split(",");
		// 检索已删除的记录
		for(int i = 0; i < ids.length; i ++){
			// 事务原因信息
			InvCReason reason = reasonRemote.findById(Long.parseLong(ids[i]));
			// 修改者
			reason.setLastModifiedBy(employee.getWorkerCode());
			// 是否使用
			reason.setIsUse(Constants.IS_USE_N);
			try {
				// 更新
				reasonRemote.update(reason);
			} catch(RuntimeException e) {
				throw e;
			}
		}
	}
	/**
	 * 增加事务原因码
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	private void addReasons(List<Map> newReasons) {
		// 事务原因
		InvCReason entity;
		// 保存新增事务原因
		List<InvCReason> addList = new ArrayList();
		Map map;
		// 循环
		for(int i = 0; i < newReasons.size(); i ++) {
			// 事务原因map
			map = newReasons.get(i);
			// 事务原因bean
			entity = new InvCReason();
			// 企业编码
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			// 事务作用id
			entity.setTransId(trans.getTransId());
			// 事务原因编码
			entity.setReasonCode(getString(map.get("reasonCode")));
			// 事务原因名称
			entity.setReasonName(getString(map.get("reasonName")));
			// 上次修改人
			entity.setLastModifiedBy(employee.getWorkerCode());
			// 添加到arraylist
			addList.add(entity);
			try {
				reasonRemote.save(entity);
			}catch(RuntimeException e) {
				throw e;
			}
		}
	}
	
	/**
	 * 修改事务原因信息
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	private void updateReasons(List<Map> dbReasons)  {
		// 事务原因
		InvCReason entity;
		// 保存修改事务原因
		List<InvCReason> updateList = new ArrayList();
		Map map;
		Long reasonId;
		// 循环
		for(int i = 0; i < dbReasons.size(); i ++) {
			// 修改记录的map
			map = dbReasons.get(i);
			// 流水号
			reasonId = (Long)map.get("reasonId");
			// 事务原因信息
			entity = reasonRemote.findById(reasonId);
			// 事务原因号
			entity.setReasonCode(getString(map.get("reasonCode")));
			// 事务原因名称
			entity.setReasonName(getString(map.get("reasonName")));
			// 上次修改人
			entity.setLastModifiedBy(employee.getWorkerCode());
			//添加修改事务原因
			updateList.add(entity);
			try {
			    reasonRemote.update(entity);
			} catch(RuntimeException e) {
				throw e;
			}
		}
	}
	/**
	 * check事务编码/名称在db中的唯一性
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unchecked")
	private String checkTransNoAndName(String flag) throws CodeRepeatException{
		// 事务作用
		InvCTransaction entity;
		String msg = "";
		boolean bFlag = false;
		if (flag.equals("isNew")) {
			if (transRemote.checkTransCode(trans.getTransCode(), employee
					.getEnterpriseCode())) {
			    msg += "事务编码已经存在,请重新输入";
			    bFlag = true;
			}
			if (transRemote.checkTransName(trans.getTransName(), employee
					.getEnterpriseCode())) {
				if(bFlag) {
				msg += "</br>事务名称已经存在,请重新输入";
				} else {
					msg += "事务名称已经存在,请重新输入";
				}
			}
		}else {
			entity = transRemote.checkTransNameForUpdate(trans.getTransName(),employee
					.getEnterpriseCode());
			if(entity!=null) {
				if( !entity.getTransId().equals(trans.getTransId()) ) {
					msg += "事务名称已经存在,请重新输入";
				}
			}
		}
		return msg;
	}

   /**
	* 删除事务作用及附带的事务原因信息
 * @throws CodeRepeatException 
    */
	public void deleteTrans() throws CodeRepeatException {
		// 流水号
		Object transId = request.getParameter("transId");
		// 查询仓库
		InvCTransaction entity = transRemote.findById(Long.parseLong(transId.toString()));
		// 修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		// 是否使用
		entity.setIsUse("N");
		// 更新
		transRemote.update(entity);
		// 删除该仓事务作用对应的所有事务原因信息
		reasonRemote.deleteByTransId(transId.toString(), employee.getWorkerCode());
	}
	/**
	 * 根据流水号查找事务作用信息
	 * 
	 * @throws JSONException
	 */
	public void getTransIdByTransNo() throws JSONException {
		// 事务作用编码
		Object transCode = request.getParameter("transCode");
		// 事务实体
		InvCTransaction entity = transRemote.findByTransCode(employee.getEnterpriseCode(),transCode.toString());
		// 返回实例
		write(JSONUtil.serialize(entity));

	}
	/**
	 * 获得字符串值
	 */
	private String getString(Object obj) {
		if(obj != null) {
			return obj.toString();
		}else{
			return "";
		}

	}
	/**
	 * @return the trans
	 */
	public InvCTransaction getTrans() {
		return trans;
	}

	/**
	 * @param trans the trans to set
	 */
	public void setTrans(InvCTransaction trans) {
		this.trans = trans;
	}
		
}
