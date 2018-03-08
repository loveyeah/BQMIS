/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.exchangeMaterial;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvCAlternateMaterial;
import power.ejb.resource.InvCAlternateMaterialFacadeRemote;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 替代物料维护
 *
 * @author wujiao
 * @version 1.0
 */
public class ExchangeMaterialAction extends AbstractAction {
	/** serial id*/
	private static final long serialVersionUID = 1L;
	/** 物料主文件remote*/
	private InvCMaterialFacadeRemote materialRemote;
	/** 替代物料remote*/
	private InvCAlternateMaterialFacadeRemote alertRemote;
	/** 时间格式化对象 */
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 构造函数
	 */
	public ExchangeMaterialAction() {
		// 物料主文件remote
		materialRemote = (InvCMaterialFacadeRemote) factory.getFacadeRemote("InvCMaterialFacade");
		// 替代物料remote
		alertRemote = (InvCAlternateMaterialFacadeRemote) factory.getFacadeRemote("InvCAlternateMaterialFacade");
	}
	/**
	 * 检索含有替换物料的所有物料信息
	 * @throws JSONException
	 */
	public void getMaterialList() throws JSONException {
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
		PageObject obj = null;
		if((strStart!=null && !"".equals(strStart) )&& (strLimit!=null && !"".equals(strLimit)) ) {
		     obj = materialRemote.findAllMaterial(fuzzy,employee.getEnterpriseCode(), Integer.parseInt(strStart), Integer.parseInt(strLimit));
		} else {
			 obj = materialRemote.findAllMaterial(fuzzy,employee.getEnterpriseCode());
		}
		// 解析字符串
		String str = null;
		 if(obj == null) {
			// 如果返回为null
			 str = "{\"list\":[],\"totalCount\":0}";
		 } else {
			 str = JSONUtil.serialize(obj);
		 }
		write(str);
	}
	/**
	 * 删除替换物料的所有物料信息
	 * @throws JSONException
	 * @throws CodeRepeatException 
	 */
	public void deleteMaterial() throws JSONException, CodeRepeatException {
		// 获取替换物料id
		String materialId  = request.getParameter("materialId");
		// 更新
		alertRemote.deleteByMaterialId(materialId, employee.getWorkerCode(),employee.getEnterpriseCode());
	}
	/**
	 * 检索替换物料的所有物料信息
	 * @throws JSONException
	 */
	public void getAlertMaterialList() throws JSONException {
		// 查询字符串
		String materialId = request.getParameter("materialId");
		// 开始行
		String strStart = request.getParameter("start");
		// 行数
		String strLimit = request.getParameter("limit");
		PageObject obj = null;
		// 查询
		if(materialId != null && materialId.length() != 0 && !"".equals(materialId) ){
			if((strStart!=null && !"".equals(strStart) )&& (strLimit!=null && !"".equals(strLimit)) ) {
				 obj = materialRemote.findAlertMaterial(materialId,employee.getEnterpriseCode(), Integer.parseInt(strStart), Integer.parseInt(strLimit));
			} else {
				 obj = materialRemote.findAlertMaterial(materialId,employee.getEnterpriseCode());
			}
		}
		// 解析字符串
		String str = null;
		 if(obj == null) {
			// 如果返回为null
			 str = "{\"list\":[],\"totalCount\":0}";
		 } else {
			 str = JSONUtil.serialize(obj);
		 }
		write(str);
	}
	/**
	 * 保存替代物料信息
	 * @throws JSONException
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 * @throws CodeRepeatException 
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unchecked")
	public void saveAlertMaterial() throws JSONException, NumberFormatException, ParseException, CodeRepeatException {
	
		// 替代物料信息是否变更
		String objIsLocationChanged = request.getParameter("isLocationChanged");
		boolean isLocationChanged = Boolean.parseBoolean(objIsLocationChanged);
		// 新增加的替代物料记录
		String newLocation = request.getParameter("newLocation");
		// 原db中修改的替代物料记录
 		String dbLocation = request.getParameter("dbLocation");
		// 已删除的Id集
		String deleteLocationIds = request.getParameter("deleteLocationIds");
		// 物料id
		String materialId = request.getParameter("materialId");
	
		// 如果替代物料信息变更
		if(isLocationChanged){
			
			// 新记录
			List<Map> newLocations = (List<Map>)JSONUtil.deserialize(newLocation);
			// 修改的记录
			List<Map> dbLocations = (List<Map>)JSONUtil.deserialize(dbLocation);
			
			try {
				deleteLocations(deleteLocationIds);
			} catch (CodeRepeatException e) {
				write("{success:false,msg:''}");
				return;
			}
			if(newLocations.size() > 0){
				// 增加新替代物料信息
				try {
					addLocations(newLocations,Long.parseLong(materialId));
				} catch (CodeRepeatException e) {
					write("{success:false,msg:''}");
					return;
				}
			}
			if(dbLocations.size() > 0) {
				try {
					// 修改db记录
 					updateLocations(dbLocations,Long.parseLong(materialId));
				} catch (CodeRepeatException e) {
					write("{success:false,msg:''}");
					return;
				}
			}
		}
		write("{success:true,msg:''}");
		return;
	}
	/**
	 * 删除替代物料信息
	 * @param deleteLocationIds 要删除的替代物料ID集
	 * @throws CodeRepeatException
	 */
	private void deleteLocations(String deleteLocationIds) throws CodeRepeatException {
		// 如果id集为空不需要删除
		if(deleteLocationIds == null || deleteLocationIds.length() < 1){
			return;
		}
		String[]ids = deleteLocationIds.split(",");
		// 检索已删除的记录
		for(int i = 0; i < ids.length; i ++){
			// 替代物料信息
			InvCAlternateMaterial location = alertRemote.findById(Long.parseLong(ids[i]));
			if( location != null ) {
			// 修改者
			location.setLastModifiedBy(employee.getWorkerCode());
			// 是否使用
			location.setIsUse(Constants.IS_USE_N);
			// 更新
			alertRemote.update(location);
			}
		}
	}
	
	/**
	 * 增加替代物料信息
	 * @throws CodeRepeatException
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	private void addLocations(List<Map> newLocations,long materialId) throws CodeRepeatException, ParseException {
		// 替代物料
		InvCAlternateMaterial entity;
		Map map;
		// 循环
		for(int i = 0; i < newLocations.size(); i ++) {
			// 替代物料map
			map = newLocations.get(i);
			// 替代物料bean
			entity = new InvCAlternateMaterial();
			// 企业编码
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			// 物料id   
			entity.setMaterialId(materialId);
			// 替代物料id
			entity.setAlterMaterialId(Long.parseLong(getString(map.get("alterMaterialId"))));
			// 相对替换质量
			entity.setQty((Double.parseDouble(getString(map.get("qty")))));
			// 优先级
			entity.setPriority(Long.parseLong((getString(map.get("priority")))));
			// 有效开始时间
			String startDate;
			if(null == map.get("effectiveDate") || "".equals(map.get("effectiveDate")))
			{
				startDate = "1900-01-01";
			}else {
				startDate = (getString(map.get("effectiveDate")));
			}
			entity.setEffectiveDate(format.parse(startDate));
			// 有效截止时间
			String endDate;
			if(null == map.get("discontinueDate") || "".equals(map.get("discontinueDate")))
			{
				endDate = "2099-12-31";
			}else {
				endDate = (getString(map.get("discontinueDate")));
			}
			entity.setDiscontinueDate(format.parse(endDate));
			// 上次修改人
			entity.setLastModifiedBy(employee.getWorkerCode());
            // 保存
			alertRemote.save(entity);

		}
	}
	 /**
	  *检查物料编码和替代物料编码是否已存在 
	  *@param dbLocations 要修改信息集
	  *@param newLocations 新增的信息集
	  *@throws CodeRepeatException
	  * 
	  **/
	     @SuppressWarnings("unchecked")
		private boolean checkMaterialAndAlert(List<Map> newLocations,List<Map> dbLocations,long materialId) throws CodeRepeatException, ParseException {
	    	// 新增加:替代物料
			 InvCAlternateMaterial entity = new InvCAlternateMaterial() ;
			 Map map;
	    	 for(int i = 0; i < newLocations.size(); i ++) {
	    		map = newLocations.get(i);
	    		// 物料id  
	 			entity.setMaterialId(materialId);
	 			// 替代物料id
	 			long alterMaterialId = Long.parseLong(getString(map.get("alterMaterialId")));
	 			// 有效开始日期
	 			String effectiveDate = getString(map.get("effectiveDate"));
	 			// 有效截止日期
	 			String discontinueDate = getString(map.get("discontinueDate"));
	 			if ( alertRemote.checkDate(materialId, alterMaterialId, effectiveDate, discontinueDate, employee.getEnterpriseCode())!=null) {
						//日期重叠性
			        	return false;
			    } 

	    	 }
	    	 for(int i = 0; i < dbLocations.size(); i ++) {
	    		map = dbLocations.get(i);
	    		// 物料id  
	 			entity.setMaterialId(materialId);
	 			// 替代物料id
	 			long alterMaterialId = Long.parseLong(getString(map.get("alterMaterialId")));
	 			// 有效开始日期
	 			String effectiveDate = getString(map.get("effectiveDate"));
	 			// 有效截止日期
	 			String discontinueDate = getString(map.get("discontinueDate"));
	 			// 替代物料编码
	 			entity.setAlterMaterialId(Long.parseLong(getString(map.get("alterMaterialId"))));
	 			// 流水号
	 			InvCAlternateMaterial result = alertRemote.checkDate(materialId, alterMaterialId, effectiveDate, discontinueDate, employee.getEnterpriseCode());
	 			if(result!= null)
		        {   
	 				if ( !result.getAlternateMaterialId().equals((Long)map.get("alternateMaterialId")) ) {
	 					return false;
	 				}
		        } 
	    	 }
			return true;
	     }
		/**
		 * 修改替代物料信息
		 * @throws CodeRepeatException
		 * @throws ParseException 
		 */
		@SuppressWarnings("unchecked")
		private void updateLocations(List<Map> dbLocations,long materialId) throws CodeRepeatException, ParseException{
			// 替代物料
			InvCAlternateMaterial entity;
			Map map;
			Long alternateMaterialId;
			// 循环
			for(int i = 0; i < dbLocations.size(); i ++) {
				// 替代物料map
				map = dbLocations.get(i);
				// 流水号
				alternateMaterialId = (Long)map.get("alternateMaterialId");
				// 替代物料bean
				entity = alertRemote.findById(alternateMaterialId);
				if(entity != null) {
					// 物料编号      
					entity.setMaterialId(materialId);
					// 替代物料编码 
					entity.setAlterMaterialId((Long)map.get("alterMaterialId"));
					// 相对替换质量
					entity.setQty((Double.parseDouble(getString(map.get("qty")))));
					// 优先级
					entity.setPriority(Long.parseLong((getString(map.get("priority")))));
					// 有效开始时间
					entity.setEffectiveDate(format.parse((getString(map.get("effectiveDate")))));
					// 有效截止时间
					entity.setDiscontinueDate(format.parse((getString(map.get("discontinueDate")))));
					// 上次修改人
					entity.setLastModifiedBy(employee.getWorkerCode());
					// update
					alertRemote.update(entity);
				}
				
			}
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

}

