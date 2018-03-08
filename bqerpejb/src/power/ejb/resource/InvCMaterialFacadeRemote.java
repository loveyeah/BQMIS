package power.ejb.resource;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvCMaterialFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvCMaterialFacadeRemote {
	/**
	 * modify by fyyang 090521
	 * 返回值从void 改为 InvCMaterial
	 * 编码改为由后台生成
	 */
	public InvCMaterial save(InvCMaterial entity);


	public void delete(InvCMaterial entity);


	public InvCMaterial update(InvCMaterial entity);

	public InvCMaterial findById(Long id);

	


	/**
	 * 根据编码查询物料信息
	 * 
	 * @param materialNo 编码
	 * @param enterpriseCode 企业编码
	 * @return PageObject 物料
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByMaterialNo(String materialNo, String enterpriseCode);



	/**
	 * 根据物料分类ID获得物料列表
	 * 
	 * @param maertialClassId 物料分类ID
	 * @param enterpriseCode 企业编码
	 * @return PageObject 物料列表
	 */
	public PageObject findByMaertialClassId(String maertialClassId, String enterpriseCode);

	/**
	 * Find all InvCMaterial entities.
	 * 
	 * @return List<InvCMaterial> all InvCMaterial entities
	 */
	public List<InvCMaterial> findAll();
	
	/**
     * 模糊查询
     * @param fuzzy 查询字符串
     * @param rowStartIdxAndCount 分页
     * @return PageObject all InvCMaterial entities
     */
	public PageObject findAllMaterial(String fuzzy,String enterpriseCode, final int... rowStartIdxAndCount);
	
	/**
     * 查找替代物料信息
     * @param materialId 查询字符串
     * @param rowStartIdxAndCount 分页
     * @return PageObject all InvCMaterial entities
     */
	public PageObject findAlertMaterial(String materialId,String enterpriseCode, final int... rowStartIdxAndCount);
	/**
	 * Find all InvCMaterial entities.
	 * 
	 * @return List<WareHouseListBean> all WareHouseListBean entities
	 */
	public List<WareHouseListBean> findAllForWareHouse(String enterpriseCode);
	/**
	 * Find all InvCMaterial entities.
	 * @param String enterpriseCode
	 * @return List<PanDianListBean> all PanDianListBean entities
	 */
	public List<CheckBalanceListBean> findAllForPanDian(String enterpriseCode,String bookNo);
	/**
	 * 查找该当物料分类节点下所有的物料编码
	 * 
	 * @param classNo 物料分类编码
	 * @param enterpriseCode 企业编码
	 * @return 返回该当物料分类节点下所有的物料编码
     * @throws Exception 
	 */
    @SuppressWarnings("unchecked")
	public List findAllChildrenNode(String classNo, String enterpriseCode);
	/**
     * 物料基础资料维护 模糊查询
     * @param fuzzy 查询字符串
     * @param rowStartIdxAndCount 分页
     * @return PageObject InvCMaterial entities
     * materialClassCode add by ywliu 2009/6/29
     */
	public PageObject getMaterialList(String fuzzy, String enterpriseCode, String materialClassCode, final int... rowStartIdxAndCount);
	/**
	 * 下拉框初始化[物料分类]
	 */
	public PageObject getClassNameList(String enterpriseCode);
	/**
	 * 根据所选择记录的流水号从[物料主文件]中检索相关信息
	 */
	public PageObject getMaterialByIdAndEnterpriseCode(Long materialId, String enterpriseCode);
	/**
	 * 下拉框初始化[物料类型]
	 */
	public PageObject getMaterialTypeList(String enterpriseCode);
	/**
	 * 下拉框初始化[物料状态]
	 */
	public PageObject getMaterialStatusList(String enterpriseCode);
	/**
	 * 下拉框初始化[缺省仓库]
	 */
	public PageObject getWarehouseList(String enterpriseCode);
	/**
	 * 下拉框初始化[缺省库位]
	 */
	public PageObject getLocationList(String enterpriseCode, String whsNo);
	/**
	 * 搜索所有盘点单号
	 */
	public List<String> fillAllCheckBalanceNo();
	
	/**
	 * 获取物资正式编码
	 * add by fyyang 
	 * @param whsNo
	 * @param materialClassId
	 * @return
	 */
	public String createMaterialNo(String whsNo,Long materialClassId);
	
	/**
	 * 判断是否可以删除该物资 true 能删除 false 不能删除
	 * add by fyyang 090605
	 * @param materialId 物资id
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	public boolean checkDelete(Long materialId,String enterpriseCode);
	
	/**
	 * 更新物料标准成本 
	 * add by fyyang 091130
	 * @param materialId
	 * @param enterpriseCode
	 * @param qty
	 * @param price
	 * @return
	 */
	public Double updateStdCost(Long materialId,String enterpriseCode,Double qty,Double price);
}