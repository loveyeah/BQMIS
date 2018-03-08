package power.ejb.resource;

import java.nio.ReadOnlyBufferException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for 库存物料记录.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvJWarehouseFacadeRemote {
    /**
     * 查询
     * @param enterpriseCode 企业编码
     * @param whsNo 仓库编号
     * @param rowStartIdxAndCount 分页
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
    public PageObject findByWhsNo(String enterpriseCode, String whsNo);
    /**
     * 更新
     * @param entity
     * @return
     */
    public InvJWarehouse update(InvJWarehouse entity);
    /**
     * 由仓库编码和物料编码查询库存物料记录
     * @param enterpriseCode 企业编码
     * @param whsNo 仓库编码
     * @param materialId 物料流水号
     * @return InvJWarehouse entity 库存物料记录
     */
    public List<InvJWarehouse> findByWHandMaterial(String enterpriseCode, String whsNo, Long materialId);
    /**
     * 登陆
     * @param entity
     * @return
     */
    public InvJWarehouse save(InvJWarehouse entity);
    
    public InvJWarehouse findById(Long id);
}