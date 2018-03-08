/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.warehousebaseinfo.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.resource.InvCLocation;
import power.ejb.resource.InvCLocationFacadeRemote;
import power.ejb.resource.InvCWarehouse;
import power.ejb.resource.InvCWarehouseFacadeRemote;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.InvJWarehouseFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
/**
 * 仓库基础资料维护
 *
 * @author zhangqi
 * @version 1.0
 */
public class WarehouseBaseInfoMaintAction extends AbstractAction{
	/** serial id*/
	private static final long serialVersionUID = 1L;
	/** 库位主文件remote*/
	private InvCLocationFacadeRemote locationRemote;
	/** 仓库主文件remote*/
	private InvCWarehouseFacadeRemote warehouseRemote;
	/** 仓库物料记录remote*/
	private InvJWarehouseFacadeRemote materielRemote;
	/** 共通*/
	private BaseDataManager baseRemote;
	/**仓库bean*/
	private InvCWarehouse whs;
	private static final String MESSAGE_ROLLBACK = "'操作数据库过程中异常终了。'";
	private static final String MESSAGE_LOCK ="'他人使用中。'";
	/**
	 * 构造函数
	 */
	public WarehouseBaseInfoMaintAction() {
		// 库位主文件remote
		locationRemote = (InvCLocationFacadeRemote) factory.getFacadeRemote("InvCLocationFacade");
		// 仓库主文件remote
		warehouseRemote = (InvCWarehouseFacadeRemote) factory.getFacadeRemote("InvCWarehouseFacade");
		// 仓库物料记录remote
		materielRemote = (InvJWarehouseFacadeRemote) factory.getFacadeRemote("InvJWarehouseFacade");
		// 共通
		baseRemote = (BaseDataManager) factory.getFacadeRemote("BaseDataManagerImpl");
	}
	/**
	 * 检索人员名称
	 */
	public void findPersonNameByCode(){
		// code
		String code = request.getParameter("workerCode");
		// 检索
		Employee person = baseRemote.getEmployeeInfo(code);
		if(person!= null){
			write(person.getWorkerName());
		}else{
			write("");
		}

	}
	/**
	 * 检索所有仓库信息
	 * @throws JSONException
	 */
	public void getWarehouseInfoList() throws JSONException {
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
		PageObject obj = warehouseRemote.
			findAll(employee.getEnterpriseCode(), fuzzy, Integer.parseInt(strStart), Integer.parseInt(strLimit));
		write(JSONUtil.serialize(obj));
	}
	/**
	 * 删除仓库
	 */
	public void deleteWarehouse() {
		// 获取仓库号
		String strWhsId  = request.getParameter("whsId");
		// 上次修改时间
		String lastModifiedDate = request.getParameter("lastModifiedDate");
		// 如果时间里有T，去除
		lastModifiedDate = lastModifiedDate.replace("T", " ");
		try{
			// 查询仓库
			InvCWarehouse entity = warehouseRemote.findById(Long.parseLong(strWhsId));
			// 已被删除
			if(entity == null){
				throw new RuntimeException();
			}
			// db中现在日期
			String dbDate = DateToString(entity.getLastModifiedDate());
			  // 时间不相同
			  if(!dbDate.equals(lastModifiedDate)){
				  throw new Exception();
			  }
			// 修改人
			entity.setLastModifiedBy(employee.getWorkerCode());
			// 是否使用
			entity.setIsUse("N");
			// 更新
			warehouseRemote.deleteWareHouse(entity);
			  write("{success:true}");
		  }catch(RuntimeException e){
	    	  write("{success:false,msg:" + MESSAGE_ROLLBACK + "}");
	      }catch(Exception e){
	    	  write("{success:false,msg:"+MESSAGE_LOCK + "}");
	      }
	}
	/**
	 * 确认仓库是否可以删除
	 */
	@SuppressWarnings("unchecked")
	public void  isWarehouseCanRemove() {
		// 获取仓库号
		String strWhsNo  = request.getParameter("whsNo");
		// 查询
		PageObject obj = materielRemote.findByWhsNo(employee.getEnterpriseCode(), strWhsNo);
		List<InvJWarehouse> list = obj.getList();
		InvJWarehouse materiel;
		// 判断仓库物料数是否为零
		for(int i = 0; i < list.size(); i ++){
			materiel = list.get(i);
			// 物料数量=opb+rcv+adj-iss
			double num = materiel.getOpenBalance() + materiel.getReceipt() + materiel.getAdjust() - materiel.getIssue();
			if(num > 0){
				// 如果物料数量大于零，不允许删除
				write("false");
				return;
			}
		}
		write("true");
	}
	/**
	 * 获取仓库对应的所有库位
	 * @throws JSONException
	 */
	public void getLocationListByWhsNo() throws JSONException {
		// 仓库号
		String whsNo = request.getParameter("whsNo");
		// 开始行
		String strStart = request.getParameter("start");
		// 行数
		String strLimit = request.getParameter("limit");
		// 查询
		PageObject obj = locationRemote.
			findAll(employee.getEnterpriseCode(), whsNo, Integer.parseInt(strStart), Integer.parseInt(strLimit));
		// 返回
		write(JSONUtil.serialize(obj));
	}
	/**
	 * 保存仓库和库位信息
	 * @throws JSONException
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unchecked")
	public void saveWarehouseAndLocation() throws JSONException {
		// 仓库信息是否变更
		String objIsWhsChanged = request.getParameter("isWhsChanged");
		boolean isWhsChanged = Boolean.parseBoolean(objIsWhsChanged);
		// 库位信息是否变更
		String objIsLocationChanged = request.getParameter("isLocationChanged");
		boolean isLocationChanged = Boolean.parseBoolean(objIsLocationChanged);
		// 新增加的库位记录
		String newLocation = request.getParameter("newLocation");
		// 原db中修改的记录
		String dbLocation = request.getParameter("dbLocation");
		// 已删除的Id集
		String deleteLocationIds = request.getParameter("deleteLocationIds");
		try{
			// 新记录
			List<Map> newLocations = (List<Map>)JSONUtil.deserialize(newLocation);
			// 修改的记录
			List<Map> dbLocations = (List<Map>)JSONUtil.deserialize(dbLocation);
			// 删除数据
	        List<Map> deletedLocations = (List<Map>) JSONUtil.deserialize(deleteLocationIds);
			if(whs.getWhsId() == null) {
				// 增加仓库信息
				addWhs();
				InvCWarehouse ware = whs;
				// 增加新库位信息
				List<InvCLocation> nLocations = addLocations(newLocations);
				warehouseRemote.addWareHouse(ware, nLocations);
			}// 保存仓库信息
			else{
					// 修改仓库信息
					InvCWarehouse ware = null;
					if(isWhsChanged){
						ware = updateWhs(whs.getWhsId());
					}
					List<InvCLocation> delLocations = new ArrayList<InvCLocation>();
					List<InvCLocation> nLocations = new ArrayList<InvCLocation>();
					List<InvCLocation> upLocations = new ArrayList<InvCLocation>();
					// 如果库位信息变更
					if(isLocationChanged){
						// 如果有被删除的记录，从db中删除
						delLocations = deleteLocations(deletedLocations);
						// 新规的记录
						nLocations = addLocations(newLocations);
						// 修改db记录
						upLocations = updateLocations(dbLocations);
					}
					warehouseRemote.updateWareHouse(ware, delLocations, nLocations, upLocations);
			}
			  write("{success:true,msg:''}");
		  }catch(CodeRepeatException e){
			  write("{success:false,msg:'" + e.getMessage() + "'}");
		  }catch(RuntimeException e){
			 // 判断是不是名称重复导致
			if (e.getCause() != null) {
				if (CodeRepeatException.class.equals(e.getCause().getClass())) {
					write("{success:false,msg:'" + e.getCause().getMessage()
							+ "'}");
					return;
				}
			}
			write("{success:false,msg:" + MESSAGE_ROLLBACK + "}");
	      }catch(Exception e){
	    	  write("{success:false,msg:"+MESSAGE_LOCK + "}");
	      }
	}
	/**
	 * 根据仓库号查找仓库
	 * @throws JSONException
	 */
	public void getWarehouseInfoByWhsNo() throws JSONException {
		// 仓库号
		String whsNo = request.getParameter("whsNo");
		// 仓库实体
		InvCWarehouse entity = warehouseRemote.findByWhsNo(Constants.ENTERPRISE_CODE, whsNo);
		// 返回实例
		write(JSONUtil.serialize(entity));

	}
	/**
	 * 修改仓库信息
	 * @param whsId 仓库流水号
	 * @throws Exception
	 */
	private InvCWarehouse updateWhs(Long whsId) throws Exception {
		// 找到对应仓库记录
		InvCWarehouse entity = warehouseRemote.findById(whs.getWhsId());
		// 不存在
		if(entity == null){
			throw new RuntimeException();
		}
		// 排他check
		String lastModifiedDate  = DateToString(whs.getLastModifiedDate());
		String dbDate = DateToString(entity.getLastModifiedDate());
		if(!dbDate.equals(lastModifiedDate)){
			throw new Exception();
		}
		// 仓库编号
		entity.setWhsNo(whs.getWhsNo());
		// 联系人
		entity.setContactMan(whs.getContactMan());
		// 仓库名称
		entity.setWhsName(whs.getWhsName());
		// 可分配仓库
		if(!"Y".equals(whs.getIsAllocatableWhs())){
			entity.setIsAllocatableWhs("N");
		}else {
			entity.setIsAllocatableWhs("Y");
		}
		// 可否寄存库
		if(!"Y".equals(whs.getIsInspect())) {
			entity.setIsInspect("N");
		}else {
			entity.setIsInspect("Y");
		}
		// 是否计成本仓库
		if(!"Y".equals(whs.getIsCost())){
			entity.setIsCost("N");
		}else {
			entity.setIsCost("Y");
		}
		// 电话
		entity.setTel(whs.getTel());
		// 传真
		entity.setFax(whs.getFax());
		// 仓库地址
		entity.setAddress(whs.getAddress());
		// 仓库描述
		entity.setWhsDesc(whs.getWhsDesc());
		// 修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		// 修改
		return entity;
	}
	/**
	 * 增加一条库存记录
	 * @throws CodeRepeatException
	 */
	private void addWhs() {
		// 可分配仓库
		if(!"Y".equals(whs.getIsAllocatableWhs())){
			whs.setIsAllocatableWhs("N");
		}
		// 可否寄存库
		if(!"Y".equals(whs.getIsInspect())) {
			whs.setIsInspect("N");
		}
		// 是否计成本仓库
		if(!"Y".equals(whs.getIsCost())){
			whs.setIsCost("N");
		}
		// 企业编码
		whs.setEnterpriseCode(employee.getEnterpriseCode());
		// 修改人
		whs.setLastModifiedBy(employee.getWorkerCode());
	}
	/**
	 * 增加库位信息
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unchecked")
	private List<InvCLocation> addLocations(List<Map> newLocations) throws CodeRepeatException {
		// 库位
		InvCLocation entity;
		Map map;
		// 库位
		List<InvCLocation> locations = new ArrayList<InvCLocation>();
		// 循环
		for(int i = 0; i < newLocations.size(); i ++) {
			// 库位map
			map = newLocations.get(i);
			// 库位bean
			entity = new InvCLocation();
			// 企业编码
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			// 仓库号
			entity.setWhsNo(whs.getWhsNo());
			// 库位号
			entity.setLocationNo(getString(map.get("locationNo")));
			// 库位名称
			entity.setLocationName(getString(map.get("locationName")));
			// 库位描述
			entity.setLocationDesc(getString(map.get("locationDesc")));
			// 是否默认库位
			if(!"Y".equals(getString(map.get("isDefault")))){
				entity.setIsDefault("N");
			}else{
				entity.setIsDefault("Y");
			}
			// 上次修改人
			entity.setLastModifiedBy(employee.getWorkerCode());
			// 保存
			locations.add(entity);
		}
		return locations;
	}
	/**
	 * 修改库位信息
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<InvCLocation> updateLocations(List<Map> dbLocations) throws Exception{
		// 库位
		InvCLocation entity;
		Map map;
		Long locationId;
		List<InvCLocation> locations = new ArrayList<InvCLocation>();
		// 循环
		for(int i = 0; i < dbLocations.size(); i ++) {
			// 修改记录的map
			map = dbLocations.get(i);
			// 流水号
			locationId = (Long)map.get("locationId");
			// 库位信息
			entity = locationRemote.findById(locationId);
			// 不存在
			if(entity == null){
				throw new RuntimeException();
			}
			// 排他check
			String lastModifiedDate  = getString(map.get("lastModifiedDate"));
			lastModifiedDate = lastModifiedDate.replace("T", " ");
			String dbDate = DateToString(entity.getLastModifiedDate());
			if(!dbDate.equals(lastModifiedDate)){
				throw new Exception();
			}
			// 如果记录没有变更不更新
			if(!isLocationChanged(entity, map)){
				continue;
			}
			// 库位号
			entity.setLocationNo(getString(map.get("locationNo")));
			// 库位名称
			entity.setLocationName(getString(map.get("locationName")));
			// 库位描述
			entity.setLocationDesc(getString(map.get("locationDesc")));
			// 是否默认库位
			if(!"Y".equals(getString(map.get("isDefault")))){
				entity.setIsDefault("N");
			}else{
				entity.setIsDefault("Y");
			}
			// 上次修改人
			entity.setLastModifiedBy(employee.getWorkerCode());
			// update
			locations.add(entity);
		}
		return locations;
	}
	/**
	 * 删除库位信息
	 * @param deleteLocationIds 要删除的库位ID集
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<InvCLocation> deleteLocations(List<Map> deletedLocations) throws Exception {
		// 检索已删除的记录
		Map map;
		List<InvCLocation> locations = new ArrayList<InvCLocation>();
		for(int i = 0; i < deletedLocations.size(); i ++){
			// 要删除的记录
			map = deletedLocations.get(i);
			// 库位信息
			InvCLocation location = locationRemote.findById(getLong((map.get("locationId"))));
			// 不存在
			if(location == null){
				throw new RuntimeException();
			}
			// 排他check
			String lastModifiedDate  = getString(map.get("lastModifiedDate"));
			lastModifiedDate = lastModifiedDate.replace("T", " ");
			String dbDate = DateToString(location.getLastModifiedDate());
			if(!dbDate.equals(lastModifiedDate)){
				throw new Exception();
			}
			// 修改者
			location.setLastModifiedBy(employee.getWorkerCode());
			// 是否使用
			location.setIsUse(Constants.IS_USE_N);
			// 更新
			locations.add(location);
		}
		return locations;
	}
	/**
	 * 判断库位信息是否变更了
	 * @param location db中库位信息
	 * @param map 用户修改后的库位信息
	 */
	@SuppressWarnings("unchecked")
	private boolean isLocationChanged(InvCLocation location, Map map){
		// 库位号
		if(!getString(map.get("locationNo")).equals(location.getLocationNo())){
			return true;
		}
		// 库位名称
		if(!getString(map.get("locationName")).equals(location.getLocationName())){
			return true;
		}
		// 库位描述
		if(!getString(map.get("locationDesc")).equals(location.getLocationDesc())){
			return true;
		}
		// 是否默认库位
		if(!getString(map.get("isDefault")).equals(location.getIsDefault())){
			return true;
		}
		return false;
	}
	/**
	 * 获得字符串值
	 */
	private String getString(Object obj) {
		if(obj != null) {
			return (String) obj;
		}else{
			return "";
		}
	}
	/**
	 * 获取map里面的long型值
	 * @return 结果
	 */
	private Long getLong(Object obj){
		if(obj != null){
			return Long.parseLong(obj.toString());
		}
		return null;
	}
	/**
	 * 日期格式化为字符串
	 * @param date 日期
	 * @return date对应的字符串
	 */
	private String DateToString(Date date) {
		SimpleDateFormat defaultFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String sysDate = defaultFormat.format(date);
		return sysDate;
	}
	/**
	 * 获取仓库实例
	 * @return whs 仓库实例
	 */
	public InvCWarehouse getWhs() {
		return whs;
	}
	/**
	 * 设置仓库实例
	 * @param whs 仓库实例
	 */
	public void setWhs(InvCWarehouse whs) {
		this.whs = whs;
	}
}
