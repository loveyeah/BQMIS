package power.ejb.resource;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvCWarehouseFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvCWarehouseFacadeRemote {
    /**
     *增加一条记录
     * @param entity 要增加的记录
     * @throws RuntimeException
     *             when the operation fails
     */
    public InvCWarehouse save(InvCWarehouse entity) throws CodeRepeatException;

    /**
     * 删除一条记录
     *
     * @param whsId 流水号
     * @throws CodeRepeatException
     */
    public void delete(Long whsId) throws CodeRepeatException;

    /**
     * 修改一条记录
     *
     * @param entity 要修改的记录
     * @return InvCWarehouse 修改的记录
     * @throws CodeRepeatException
     */
    public InvCWarehouse update(InvCWarehouse entity) throws CodeRepeatException;

    /**
     * 根据主键查找记录
     * @param whsId  流水号
     */
    public InvCWarehouse findById(Long whsId);

    /**
     * 根据仓库编号查找记录
     * @param whsNo  仓库号
     */
    public InvCWarehouse findByWhsNo(String enterpriseCode, String whsNo);

    /**
     * 模糊查询
     * @param enterpriseCode 企业编码
     * @param fuzzy 查询字符串
     * @param rowStartIdxAndCount 分页
     * @return PageObject all InvCWarehouse entities
     */
    public PageObject findAll(String enterpriseCode, String fuzzy, final int... rowStartIdxAndCount);

	/**
	 * 查询仓库编码和仓库名称
	 * @param enterpriseCode 企业编码
	 */
	public PageObject findMaterialNoName(String enterpriseCode);
    /**
     * 删除仓库及其对应的库位信息
     * @param entity 要删除的仓库
     */
    public void deleteWareHouse(InvCWarehouse entity);
    /**
     * 更新仓库信息
     * @param ware 仓库
     * @param delLocations 删除的库位
     * @param nLocations 新增的库位
     * @param upLocations 更新的库位
     */
    public void updateWareHouse(
    		InvCWarehouse ware,
    		List<InvCLocation> delLocations,
    		List<InvCLocation> nLocations,
    		List<InvCLocation> upLocations)throws CodeRepeatException;
    /**
     * 增加仓库信息
     * @param entity 仓库
     * @param nLocations 库位
     */
     public void addWareHouse(InvCWarehouse entity, List<InvCLocation> nLocations)throws CodeRepeatException;
}