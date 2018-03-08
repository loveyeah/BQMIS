/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.materialbaseinfo.action;

import java.util.Date;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;

import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 物料基础资料维护action
 * @author zhengzhipeng
 * @version 1.0
 */
public class MaterialBaseInfoAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    // ejb之been
    private InvCMaterial materialBeen;
    // ejb之remote
    private InvCMaterialFacadeRemote materialRemote;
    /** 供应商查询远程接口 ConJClientInfoFacadeRemote */
   // private ConJClientInfoFacadeRemote clientInfoRemote;
    // 物料状态，分类，类型id
    private String materialStatusIdStr;
    private String maertialClassIdStr;
    private String materialTypeIdStr;
    // 单位id
    private String stockUmIdStr;
    private String purUmIdStr;
    private String saleUmIdStr;
    private String lengthUmIdStr;
    private String widthUmIdStr;
    private String heighUmIdStr;
    private String wightUmIdStr;
    private String volumUmIdStr;
    // 供应商id
    private String primarySupplierIdStr;
    private String secondarySupplierIdStr;


	/**
     * 构造函数
     */
    public MaterialBaseInfoAction() {
    	materialRemote = (InvCMaterialFacadeRemote) factory.getFacadeRemote("InvCMaterialFacade");
    	//clientInfoRemote = (ConJClientInfoFacadeRemote) factory.getFacadeRemote("resource/ConJClientInfoFacade");
    }

    /**
     * 页面加载   物料编码 物料名称 物料类别（名称） 规格型号 材质/参数 单位 缺省仓库 缺省库位
     * @throws JSONException
     */
    public void getMaterialBaseInfoList() throws JSONException {
		// 查询字符串
		String fuzzy = request.getParameter("fuzzy");
		// modify by ywliu 090629 
		String materialClassCode=request.getParameter("materialClassCode");
		if(fuzzy == null || fuzzy.length() == 0){
			// 检索所有
			fuzzy = Constants.ALL_DATA;
		}
		// 取得查询参数: 开始行
		int intStart = Integer.parseInt(request.getParameter("start"));
		// 取得查询参数: 结束行
		int intLimit = Integer.parseInt(request.getParameter("limit"));
		String enterpriseCode = employee.getEnterpriseCode();
    	PageObject obj = materialRemote.getMaterialList(fuzzy, enterpriseCode, materialClassCode, intStart, intLimit);
    	// 查询结果为null,设置页面显示
		if(obj == null) {
		    String str = "{\"list\":[],\"totalCount\":0}";
			write(str);
		 // 不为null
		} else {
			if(obj.getList() == null) {
				String str = "{\"list\":[],\"totalCount\":0}";
				write(str);
			}
			String str = JSONUtil.serialize(obj);
			write(str);
		}
    }

	/**
	 * 物料分类List
	 */
	public void getClassNameList() throws JSONException {
		// 根据企业编码取得所有物料分类
		PageObject obj = new PageObject();
		obj = materialRemote.getClassNameList(employee.getEnterpriseCode());
        String str = JSONUtil.serialize(obj);
        write(str);
	}
	/**
	 * 物料类型List
	 */
	public void getTypeNameList() throws JSONException {
	  //  getMaterialById();
		// 根据企业编码取得所有物料类型
		PageObject obj = new PageObject();
		obj = materialRemote.getMaterialTypeList(employee.getEnterpriseCode());
        String str = JSONUtil.serialize(obj);
	    write(str);
	}
	/**
	 * 物料状态List
	 */
	public void getStatusNameList() throws JSONException {
        // 根据企业编码取得所有物料类型
		PageObject obj = new PageObject();
		obj = materialRemote.getMaterialStatusList(employee.getEnterpriseCode());
	    String str = JSONUtil.serialize(obj);
	    write(str);
	}
	/**
	 * 缺省仓库List
	 */
	public void getWarehouseNameList() throws JSONException {
        // 根据企业编码取得所有仓库
		PageObject obj = new PageObject();
		obj = materialRemote.getWarehouseList(employee.getEnterpriseCode());
	    String str = JSONUtil.serialize(obj);
	    write(str);
	}
	/**
	 * 缺省库位List//====联动
	 */
	public void getLocationNameList() throws JSONException {
		// 仓库号
		String whsNo = request.getParameter("whsNo");
        // 根据企业编码取得所有物料类型
		PageObject obj = new PageObject();
		obj = materialRemote.getLocationList(employee.getEnterpriseCode(),whsNo);
	    String str = JSONUtil.serialize(obj);
	    write(str);
	}
	/**
	 * 根据所选择记录的流水号从[物料主文件]中检索相关信息
	 * @throws JSONException
	 */
	public void getMaterialById() throws JSONException{
		// 流水号取得
	 	Long materialId = Long.parseLong(request.getParameter("materialId"));
		InvCMaterial entity = materialRemote.findById(materialId);
		// 返回实例
		String str = JSONUtil.serialize(entity);
		write(str);
	}
	/**
	 * 保存物料基础资料信息
	 */
	public void saveMaterialInfo() {
		// 流水号取得
		Long materialId = materialBeen.getMaterialId();
	 	if(materialId == null){
	 	    // 无流水号，则为新增
	 		addMaterialInfo();
	 	} else {
	 	    // 有流水号，则为修改
	 		updateMaterialInfo(materialId);
	 	}
	}
	/**
	 * 新增保存
	 */
	public void addMaterialInfo() {
		// 获取新增的编码
		String materialNo = materialBeen.getMaterialNo();
		// 获取企业编码
		String enterpriseCode = employee.getEnterpriseCode();
		// 检查新增的编码是否已经存在
		PageObject obj = materialRemote.findByMaterialNo(materialNo, enterpriseCode);
		if(obj.getTotalCount() > 0) {
			// 编码重复
			write("{success:true,flag:'1'}");
			return;
		}
	    // 是否分配仓库
		String isLot = Constants.FLAG_N;
        // 是否预留
        String isReserved = Constants.FLAG_N;
		// 是否消费品
        String isCommodity = Constants.FLAG_N;
		// 是否危险品
		String isDanger = Constants.FLAG_N;
		// 是否免检
		String qaControlFlag = Constants.FLAG_N;
        if(Constants.FLAG_Y.equals(materialBeen.getIsLot())){
        	isLot = Constants.FLAG_Y;
        }
        if(Constants.FLAG_Y.equals(materialBeen.getIsReserved())){
        	isReserved = Constants.FLAG_Y;
        }
        if(Constants.FLAG_Y.equals(materialBeen.getIsCommodity())){
        	isCommodity = Constants.FLAG_Y;
        }
        if(Constants.FLAG_Y.equals(materialBeen.getIsDanger())){
        	isDanger = Constants.FLAG_Y;
        }
        if(Constants.FLAG_Y.equals(materialBeen.getQaControlFlag())){
        	qaControlFlag = Constants.FLAG_Y;
        }
        materialBeen.setIsLot(isLot);
        materialBeen.setIsReserved(isReserved);
        materialBeen.setIsCommodity(isCommodity);
        materialBeen.setIsDanger(isDanger);
        materialBeen.setQaControlFlag(qaControlFlag);

        // 设定15个combox
        setIds(materialBeen);

		// 设定企业编码
 		materialBeen.setEnterpriseCode(enterpriseCode);
    	// 设定修改者
 		materialBeen.setLastModifiedBy(employee.getWorkerCode());
    	// 设定是否可用
 		materialBeen.setIsUse(Constants.IS_USE_Y);
    	// 修改时间
 		materialBeen.setLastModifiedDate(new Date());
    	// 流水号在保存时设定
 		InvCMaterial newBeen=materialRemote.save(materialBeen);

 		//modify by fyyang 090521 保存成功后返回model，无需再查id
// 		// 获取保存后的流水号
// 		PageObject newObj = materialRemote.findByMaterialNo(materialNo, enterpriseCode);
// 		InvCMaterial newBeen =  (InvCMaterial) newObj.getList().get(0);
 		Long materialId = newBeen.getMaterialId();
 		// 保存成功 传出id
 		write("{success:true,flag:'0',theId:'" + materialId + "'}");
	}
	/**
	 * 修改保存===
	 */
	public void updateMaterialInfo(Long materialId) {
		// 根据画面选择项目的流水号找到类型实体
		InvCMaterial oldBeen = materialRemote.findById(materialId);
		// 修改前后的编码
		String newNO = materialBeen.getMaterialNo();
		String oldNo = oldBeen.getMaterialNo();
		// 获取企业编码
		String enterpriseCode = employee.getEnterpriseCode();
		PageObject obj = materialRemote.findByMaterialNo(newNO, enterpriseCode);
		// [编码检索]>0，且编码不是原编码
		if(obj.getTotalCount() > 0 && !oldNo.equals(newNO)) {
			// 编码重复
			write("{success:true,flag:'1'}");
			return;
		}

		// 设定要更新的内容===

		// 5个checkBox
	    // 是否分配仓库
		String isLot = Constants.FLAG_N;
        // 是否预留
        String isReserved = Constants.FLAG_N;
		// 是否消费品
        String isCommodity = Constants.FLAG_N;
		// 是否危险品
		String isDanger = Constants.FLAG_N;
		// 是否免检
		String qaControlFlag = Constants.FLAG_N;
        if(Constants.FLAG_Y.equals(materialBeen.getIsLot())){
        	isLot = Constants.FLAG_Y;
        }
        if(Constants.FLAG_Y.equals(materialBeen.getIsReserved())){
        	isReserved = Constants.FLAG_Y;
        }
        if(Constants.FLAG_Y.equals(materialBeen.getIsCommodity())){
        	isCommodity = Constants.FLAG_Y;
        }
        if(Constants.FLAG_Y.equals(materialBeen.getIsDanger())){
        	isDanger = Constants.FLAG_Y;
        }
        if(Constants.FLAG_Y.equals(materialBeen.getQaControlFlag())){
        	qaControlFlag = Constants.FLAG_Y;
        }
        oldBeen.setIsLot(isLot);
        oldBeen.setIsReserved(isReserved);
        oldBeen.setIsCommodity(isCommodity);
        oldBeen.setIsDanger(isDanger);
        oldBeen.setQaControlFlag(qaControlFlag);

		// 15个ComboBox
        setIds(oldBeen);

		// 定义/描述栏 (名称，编码，描述)
        oldBeen.setMaterialNo(newNO);
		oldBeen.setMaterialName(materialBeen.getMaterialName());
		oldBeen.setMaterialDesc(materialBeen.getMaterialDesc());

		// 需求/采购栏
		oldBeen.setFrozenCost(materialBeen.getFrozenCost());
		oldBeen.setFobPrice(materialBeen.getFobPrice());
		oldBeen.setTaxRate(materialBeen.getTaxRate());
		oldBeen.setLeadDays(materialBeen.getLeadDays());
		oldBeen.setPurStockUm(materialBeen.getPurStockUm());
		oldBeen.setSaleStockUm(materialBeen.getSaleStockUm());
		oldBeen.setMaxStock(materialBeen.getMaxStock());
		oldBeen.setMinStock(materialBeen.getMinStock());

		// 规格/质量栏
		oldBeen.setLen(materialBeen.getLen());
		oldBeen.setWidth(materialBeen.getWidth());
		oldBeen.setHeigh(materialBeen.getHeigh());
		oldBeen.setWeight(materialBeen.getWeight());
		oldBeen.setVolumUnit(materialBeen.getVolumUnit());

		oldBeen.setSpecNo(materialBeen.getSpecNo());
		oldBeen.setDocNo(materialBeen.getDocNo());
		oldBeen.setParameter(materialBeen.getParameter());
		oldBeen.setFactory(materialBeen.getFactory());

		oldBeen.setCentigrade(materialBeen.getCentigrade());
		oldBeen.setQualityClass(materialBeen.getQualityClass());
		oldBeen.setStockYears(materialBeen.getStockYears());
		oldBeen.setCheckDays(materialBeen.getCheckDays());

		// 设定修改者
		oldBeen.setLastModifiedBy(employee.getWorkerCode());
 	    // 修改时间
		oldBeen.setLastModifiedDate(new Date());
		materialRemote.update(oldBeen);
		// 保存成功 传出id
 		write("{success:true,flag:'0',theId:'" + materialId + "'}");
	}
	/**
	 * 删除 通过流水号找到实体，然后进行逻辑删除
	 */
	public void deleteMaterialBaseInfo() {
		// 根据画面选择项目的流水号找到类型实体
		Long materialId = Long.parseLong(request.getParameter("materialId"));
		boolean check=materialRemote.checkDelete(materialId, employee.getEnterpriseCode());
		if(check)
		{
		InvCMaterial oldStatusBeen =  materialRemote.findById(materialId);
		// 设定修改者
		oldStatusBeen.setLastModifiedBy(employee.getWorkerCode());
 	    // 修改时间
		oldStatusBeen.setLastModifiedDate(new Date());
		// 设定是否可用
 	 	oldStatusBeen.setIsUse(Constants.IS_USE_N);
		// 逻辑删除
 	 	
 		materialRemote.update(oldStatusBeen);
 		write("{success:true,flag:'1'}");
		}
		else
		{
			write("{success:true,flag:'2'}");
		}
	}

	public InvCMaterial getMaterialBeen() {
		return materialBeen;
	}

	public void setMaterialBeen(InvCMaterial materialBeen) {
		this.materialBeen = materialBeen;
	}

	public String getMaterialStatusIdStr() {
		return materialStatusIdStr;
	}

	public void setMaterialStatusIdStr(String materialStatusIdStr) {
		this.materialStatusIdStr = materialStatusIdStr;
	}

	public String getMaertialClassIdStr() {
		return maertialClassIdStr;
	}

	public void setMaertialClassIdStr(String maertialClassIdStr) {
		this.maertialClassIdStr = maertialClassIdStr;
	}

	public String getMaterialTypeIdStr() {
		return materialTypeIdStr;
	}

	public void setMaterialTypeIdStr(String materialTypeIdStr) {
		this.materialTypeIdStr = materialTypeIdStr;
	}
	private boolean checkIdStr(String value) {
		if(value == null || "null".equals(value) || "".equals(value)){
			return false;
		}
		return true;
	}
	/**
	 * 单位List
	 */
	public void getAllUnitList() throws JSONException{
		// 对应 comminterface jar包删除
		BpCMeasureUnitFacadeRemote a = (BpCMeasureUnitFacadeRemote)
			factory.getFacadeRemote("BpCMeasureUnitFacade");
		String fuzzy = Constants.ALL_DATA;
		String enterpriseCode = employee.getEnterpriseCode();
		PageObject obj = a.findUnitList(fuzzy, enterpriseCode);
		String str = JSONUtil.serialize(obj);
     	write(str);
	}
//	/**
//	 * TODO通过id去供应商名称
//	 */
//	public void getSupplyNameById() throws JSONException{
////		// 流水号取得
////	 	Long supplyId = Long.parseLong(request.getParameter("supplyId"));
////	 	ConJClientInfo entity = clientInfoRemote.findById(supplyId);
////		// 返回实例
////		String str = JSONUtil.serialize(entity);
////		write(str);
//	}
	/**
	 * 设置相应的id
	 */
    public void setIds(InvCMaterial oldBeen){
		// 15个ComboBox
        // 物料状态,分类,类型,仓库,库位,ABC种类,计价方式（7个）
        if(checkIdStr(materialStatusIdStr)){
			oldBeen.setMaterialStatusId(Long.parseLong(materialStatusIdStr));
        } else {
        	oldBeen.setMaterialStatusId(null);
        }
        if(checkIdStr(maertialClassIdStr)){
			oldBeen.setMaertialClassId(Long.parseLong(maertialClassIdStr));
        } else {
        	oldBeen.setMaertialClassId(null);
        }
        if(checkIdStr(materialTypeIdStr)){
			oldBeen.setMaterialTypeId(Long.parseLong(materialTypeIdStr));
		} else {
	    	oldBeen.setMaterialTypeId(null);
	    }
	    oldBeen.setAbcType(materialBeen.getAbcType());
		oldBeen.setDefaultWhsNo(materialBeen.getDefaultWhsNo());
		oldBeen.setDefaultLocationNo(materialBeen.getDefaultLocationNo());
		oldBeen.setCostMethod(materialBeen.getCostMethod());
		// 计量单位（8个） stockUmIdStr purUmIdStr saleUmIdStr
		// lengthUmIdStr widthUmIdStr heighUmIdStr wightUmIdStr volumUmIdStr;
		if(checkIdStr(stockUmIdStr)){
			oldBeen.setStockUmId(Long.parseLong(stockUmIdStr));
        } else {
        	oldBeen.setStockUmId(null);
        }
		if(checkIdStr(purUmIdStr)){
			oldBeen.setPurUmId(Long.parseLong(purUmIdStr));
        } else {
        	oldBeen.setPurUmId(null);
        }
		if(checkIdStr(saleUmIdStr)){
			oldBeen.setSaleUmId(Long.parseLong(saleUmIdStr));
        } else {
        	oldBeen.setSaleUmId(null);
        }
		if(checkIdStr(lengthUmIdStr)){
			oldBeen.setLengthUmId(Long.parseLong(lengthUmIdStr));
        } else {
        	oldBeen.setLengthUmId(null);
        }
		if(checkIdStr(widthUmIdStr)){
			oldBeen.setWidthUmId(Long.parseLong(widthUmIdStr));
        } else {
        	oldBeen.setWidthUmId(null);
        }
		if(checkIdStr(heighUmIdStr)){
			oldBeen.setHeighUmId(Long.parseLong(heighUmIdStr));
        } else {
        	oldBeen.setHeighUmId(null);
        }
		if(checkIdStr(wightUmIdStr)){
			oldBeen.setWightUmId(Long.parseLong(wightUmIdStr));
        } else {
        	oldBeen.setWightUmId(null);
        }
		if(checkIdStr(volumUmIdStr)){
			oldBeen.setVolumUmId(Long.parseLong(volumUmIdStr));
        } else {
        	oldBeen.setVolumUmId(null);
        }
		// 主次供应商
		if(checkIdStr(primarySupplierIdStr)){
			oldBeen.setPrimarySupplier(Long.parseLong(primarySupplierIdStr));
        } else {
        	oldBeen.setPrimarySupplier(null);
        }
		if(checkIdStr(secondarySupplierIdStr)){
			oldBeen.setSecondarySupplier(Long.parseLong(secondarySupplierIdStr));
        } else {
        	oldBeen.setSecondarySupplier(null);
        }
    }
	public String getStockUmIdStr() {
		return stockUmIdStr;
	}

	public void setStockUmIdStr(String stockUmIdStr) {
		this.stockUmIdStr = stockUmIdStr;
	}

	public String getPurUmIdStr() {
		return purUmIdStr;
	}

	public void setPurUmIdStr(String purUmIdStr) {
		this.purUmIdStr = purUmIdStr;
	}

	public String getSaleUmIdStr() {
		return saleUmIdStr;
	}

	public void setSaleUmIdStr(String saleUmIdStr) {
		this.saleUmIdStr = saleUmIdStr;
	}

	public String getLengthUmIdStr() {
		return lengthUmIdStr;
	}

	public void setLengthUmIdStr(String lengthUmIdStr) {
		this.lengthUmIdStr = lengthUmIdStr;
	}

	public String getWidthUmIdStr() {
		return widthUmIdStr;
	}

	public void setWidthUmIdStr(String widthUmIdStr) {
		this.widthUmIdStr = widthUmIdStr;
	}

	public String getHeighUmIdStr() {
		return heighUmIdStr;
	}

	public void setHeighUmIdStr(String heighUmIdStr) {
		this.heighUmIdStr = heighUmIdStr;
	}

	public String getWightUmIdStr() {
		return wightUmIdStr;
	}

	public void setWightUmIdStr(String wightUmIdStr) {
		this.wightUmIdStr = wightUmIdStr;
	}

	public String getVolumUmIdStr() {
		return volumUmIdStr;
	}

	public void setVolumUmIdStr(String volumUmIdStr) {
		this.volumUmIdStr = volumUmIdStr;
	}
	public String getPrimarySupplierIdStr() {
		return primarySupplierIdStr;
	}

	public void setPrimarySupplierIdStr(String primarySupplierIdStr) {
		this.primarySupplierIdStr = primarySupplierIdStr;
	}

	public String getSecondarySupplierIdStr() {
		return secondarySupplierIdStr;
	}

	public void setSecondarySupplierIdStr(String secondarySupplierIdStr) {
		this.secondarySupplierIdStr = secondarySupplierIdStr;
	}
}
