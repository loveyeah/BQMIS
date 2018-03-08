package power.ejb.resource;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity InvCWarehouse.
 *
 * @see power.ejb.logistics.InvCWarehouse
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvCWarehouseFacade implements InvCWarehouseFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    @EJB(beanName = "InvCLocationFacade")
    private InvCLocationFacadeRemote locationRemote;

    /**
     *增加一条记录
     * @param entity 要增加的记录
     * @throws RuntimeException
     *             when the operation fails
     */
    public InvCWarehouse save(InvCWarehouse entity) throws CodeRepeatException{
        LogUtil.log("saving InvCWarehouse instance", Level.INFO, null);
        try {
        	// 检查仓库编号和仓库名称是否唯一
        	String msg = "";
            if(checkWhsNameForAdd(entity.getWhsName(), "",entity.getEnterpriseCode())){
                msg +="仓库名称"+ "已存在。请重新输入。<br/>";
            }
            if(checkWhsNoNoForAdd(entity.getWhsNo(), "",entity.getEnterpriseCode())){
                msg +="仓库编码"+ "已存在。请重新输入。<br/>";
            }
            if(msg.length() > 0){
            	throw new CodeRepeatException(msg);
            }
                if(entity.getWhsId()==null)
                {
                    // 设定主键值
                    entity.setWhsId(bll.getMaxId("INV_C_WAREHOUSE", "WHS_ID"));
                }
                // 设定修改时间
                entity.setLastModifiedDate(new java.util.Date());
                // 设定是否使用
                entity.setIsUse("Y");
                // 保存
                entityManager.persist(entity);
                LogUtil.log("save successful", Level.INFO, null);
                return entity;
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 删除一条记录
     *
     * @param whsId 流水号
     * @throws CodeRepeatException
     */
    public void delete(Long whsId) throws CodeRepeatException{
        LogUtil.log("deleting InvCWarehouse instance", Level.INFO, null);
        try {
            InvCWarehouse entity=this.findById(whsId);
             if(entity!=null)
             {
                 // is_use设为N
                 entity.setIsUse("N");
                 this.update(entity);
                 LogUtil.log("delete successful", Level.INFO, null);
             }
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 修改一条记录
     *
     * @param entity 要修改的记录
     * @return InvCWarehouse 修改的记录
     * @throws CodeRepeatException
     */
    public InvCWarehouse update(InvCWarehouse entity) throws CodeRepeatException{
        LogUtil.log("updating InvCWarehouse instance", Level.INFO, null);
        try {
            // 检查仓库编号和仓库名称是否唯一
        	String msg = "";
            if (checkWhsNameForAdd(entity.getWhsName(), String.valueOf(entity.getWhsId()), entity
                    .getEnterpriseCode())) {
            	msg +="仓库名称"+ "已存在。请重新输入。<br/>";
            }
            if (checkWhsNoNoForAdd(entity.getWhsNo(), String.valueOf(entity.getWhsId()), entity
                    .getEnterpriseCode())) {
            	 msg +="仓库编码"+ "已存在。请重新输入。<br/>";
            }
            if(msg.length() > 0){
            	throw new CodeRepeatException(msg);
            }
            // 修改时间
            entity.setLastModifiedDate(new Date());
            InvCWarehouse result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 根据主键查找记录
     * @param whsId  流水号
     */
    public InvCWarehouse findById(Long whsId) {
        LogUtil.log("finding InvCWarehouse instance with id: " + whsId,
                Level.INFO, null);
        try {
            InvCWarehouse instance = entityManager
                    .find(InvCWarehouse.class, whsId);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 根据仓库编号查找记录
     * @param whsNo  仓库号
     */
    public InvCWarehouse findByWhsNo(String enterpriseCode, String whsNo) {
        LogUtil.log("finding InvCWarehouse instance with whsNo: " + whsNo,
                Level.INFO, null);
        try {
            InvCWarehouse instance = null;
            // 查询sql
            String sql=
                "select * from inv_c_warehouse t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.whs_no='" + whsNo +  "'\n" +
                "and t.is_use='Y' \n" +
                "order by t.whs_no";
            // 执行查询
            List<InvCWarehouse> list=bll.queryByNativeSQL(sql, InvCWarehouse.class);
            // 如果查到结果
            if(list!=null && list.size()> 0){
                instance = list.get(0);
            }
            // 返回
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 删除仓库及其对应的库位信息
     * @param entity 要删除的仓库
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteWareHouse(InvCWarehouse entity){
    	try{
    		// 更新
    		update(entity);
    		// 删除该仓库对应的库位记录
    		locationRemote.deleteByWhsNo(entity.getWhsNo(), entity.getLastModifiedBy());
    	}catch(Exception e){
    		throw new RuntimeException();
    	}

    }
   /**
    * 增加仓库信息
    * @param entity 仓库
    * @param nLocations 库位
 * @throws CodeRepeatException
    */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addWareHouse(InvCWarehouse entity, List<InvCLocation> nLocations) throws CodeRepeatException{
    	try{
    		// 新规仓库
    		save(entity);
    		Long locId = bll.getMaxId("INV_C_LOCATION","LOCATION_ID");
    		// 新规库位
    		for(InvCLocation location : nLocations){
    			// 流水号
    			location.setLocationId(locId++);
    			// 新规
    			locationRemote.save(location);
    		}
    	}catch(CodeRepeatException e){
    		throw e;
    	}
    	catch(Exception e){
    		throw new RuntimeException();
    	}
    }
    /**
     * 更新仓库信息
     * @param ware 仓库
     * @param delLocations 删除的库位
     * @param nLocations 新增的库位
     * @param upLocations 更新的库位
     * @throws CodeRepeatException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateWareHouse(
    		InvCWarehouse ware,
    		List<InvCLocation> delLocations,
    		List<InvCLocation> nLocations,
    		List<InvCLocation> upLocations) throws CodeRepeatException{
    	try{
    		// 更新仓库
    		if(ware != null){
    			update(ware);
    		}
    		// 删除库位
    		for(InvCLocation location : delLocations){
    			locationRemote.update(location);
    		}
    		// 更新库位
    		for(InvCLocation location : upLocations){
    			locationRemote.update(location);
    		}
    		// 新增库位
    		Long locId = bll.getMaxId("INV_C_LOCATION","LOCATION_ID");
    		// 新规库位
    		for(InvCLocation location : nLocations){
    			// 流水号
    			location.setLocationId(locId++);
    			// 新规
    			locationRemote.save(location);
    		}
    	}catch(CodeRepeatException e){
    		throw e;
    	}
    	catch(Exception e){
    		throw new RuntimeException();
    	}

    }
    /**
     * 模糊查询
     * @param enterpriseCode 企业编码
     * @param fuzzy 查询字符串
     * @param rowStartIdxAndCount 分页
     * @return PageObject all InvCWarehouse entities
     */
    @SuppressWarnings("unchecked")
    public PageObject findAll(String enterpriseCode, String fuzzy, final int... rowStartIdxAndCount) {
        LogUtil.log("finding all InvCWarehouse instances", Level.INFO, null);
        try {
            PageObject result = new PageObject();
            // 查询sql
            String sql=
                "select * from inv_c_warehouse t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and (t.whs_no like '%" + fuzzy + "%' or t.whs_name like '%" +fuzzy + "%')"+ "\n" +
                "and t.is_use='Y'\n" +
                "order by t.whs_no";
            List<InvCWarehouse> list=bll.queryByNativeSQL(sql, InvCWarehouse.class, rowStartIdxAndCount);
            String sqlCount=
                "select count(t.whs_id) from inv_c_warehouse t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and (t.whs_no like '%" + fuzzy + "%' or t.whs_name like '%" +fuzzy + "%')"+ "\n" +
                "and t.is_use='Y'\n" +
                "order by t.whs_no";
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
     * 检查仓库名称是否是唯一的
     * @param whsName 仓库名称
     * @param enterpriseCode 企业编码
     * @return false 如果仓库名称是唯一的
     */
    private boolean checkWhsNameForAdd(String whsName,    String whsId, String enterpriseCode) {

        boolean isSame = false;
        String sql = "select count(t.whs_id) from inv_c_warehouse t\n"
                + "where t.whs_name='" + whsName + "'\n"
                + "and t.enterprise_code='" + enterpriseCode + "'\n"
                + "and t.is_use='Y'";
        if(!"".equals(whsId)){
            sql += "\n and t.whs_Id <> " + whsId ;
        }
        if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
            isSame = true;
        }
        return isSame;
    }
    /**
     * 检查仓库编号是否是唯一的
     * @param whsNo 仓库号
     * @param enterpriseCode 企业编码
     * @return false 如果仓库编号是唯一的
     */
    private boolean checkWhsNoNoForAdd(String whsNo,    String whsId, String enterpriseCode) {

        boolean isSame = false;
        String sql = "select count(t.whs_id) from inv_c_warehouse t\n"
                + "where t.enterprise_code='" + enterpriseCode + "'\n"
                + "and t.whs_no='" +whsNo + "'\n"
                + "and t.is_use='Y'";
        if(!"".equals(whsId)){
            sql += "\n and t.whs_Id <> " + whsId ;
        }
        if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
            isSame = true;
        }
        return isSame;
    }

	/**
	 * 查询仓库编码和仓库名称
	 * @param enterpriseCode 企业编码
	 */
	@SuppressWarnings("unchecked")
	public PageObject findMaterialNoName(String enterpriseCode) {
		LogUtil.log("finding WHS_NO and WHS_NAME:",
                Level.INFO, null);
        try {
        	PageObject result = new PageObject();
            // 查询sql
            String sql=
                "select * from inv_c_warehouse t\n" +
                "where  t.enterprise_code='"+ enterpriseCode + "'\n" +
                "and t.is_use='Y'";
            // 执行查询
            List<InvCWarehouse> list = bll.queryByNativeSQL(sql, InvCWarehouse.class);
            result.setList(list);
            // 返回
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
	}
}