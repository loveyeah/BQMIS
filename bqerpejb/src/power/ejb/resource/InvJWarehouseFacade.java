package power.ejb.resource;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.opticket.RunCOpticketTask;

/**
 * Facade for entity 库存物料记录
 *
 * @see power.ejb.logistics.InvJWarehouse
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvJWarehouseFacade implements InvJWarehouseFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * 查询
     * @param enterpriseCode 企业编码
     * @param whsNo 仓库编号
     * @param rowStartIdxAndCount 分页
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
    public PageObject findByWhsNo(String enterpriseCode, String whsNo) {
        LogUtil.log("finding all InvCLocation instances", Level.INFO, null);
        try {
            PageObject result = new PageObject();
            // 查询sql
            String sql=
                "select * from INV_J_WAREHOUSE t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.whs_no='" + whsNo + "'\n" +
                "and t.is_use='Y'";
            List<InvJWarehouse> list=bll.queryByNativeSQL(sql, InvJWarehouse.class);
            String sqlCount=
                "select count(*) from INV_J_WAREHOUSE t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.whs_no='" + whsNo + "'\n" +
                "and t.is_use='Y'";
            Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
            result.setList(list);
            result.setTotalCount(totalCount);
            return result;

        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 更新
     * @param entity
     * @return
     */
    public InvJWarehouse update(InvJWarehouse entity) {
		LogUtil.log("updating InvJBookDetails instance", Level.INFO, null);
		try {
			// 更新时间
			entity.setLastModifiedDate(new java.util.Date());
			InvJWarehouse result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
    /**
     * 由仓库编码和物料编码查询库存物料记录
     * @param enterpriseCode 企业编码
     * @param whsNo 仓库编码
     * @param materialId 物料流水号
     * @return InvJWarehouse entity 库存物料记录
     */
    @SuppressWarnings("unchecked")
	public List<InvJWarehouse> findByWHandMaterial(String enterpriseCode, String whsNo, Long materialId) {
    	String sql=
			"select *\n" +
			"   from INV_J_WAREHOUSE t\n" + 
			"where t.is_use = 'Y'\n" +
			"   AND t.ENTERPRISE_CODE = ?\n" + 
			"   and t.WHS_NO = ?\n" + 
			"   and t.MATERIAL_ID=?";
		return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, whsNo,materialId}, InvJWarehouse.class);
    }
    /**
     * 登陆库存物料记录
     * @param whsNo 库存物料记录
     * @return InvJWarehouse entity 库存物料记录
     */
	public InvJWarehouse save(InvJWarehouse entity) {
		LogUtil.log("saving InvJWarehouse instance", Level.INFO, null);
		// 设定主键值
		if(entity.getWarehouseInvId()==null){
        entity.setWarehouseInvId(bll.getMaxId("INV_J_WAREHOUSE", "WAREHOUSE_INV_ID"));
		}
        // 设定修改时间
        entity.setLastModifiedDate(new java.util.Date());
        // 保存
        entityManager.persist(entity);
        // 打印log 
        LogUtil.log("save successful", Level.INFO, null);
        return entity;
	}
	public InvJWarehouse findById(Long id) {
		LogUtil.log("finding InvJWarehouse instance with id: " + id,
				Level.INFO, null);
		try {
			InvJWarehouse instance = entityManager
					.find(InvJWarehouse.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
}