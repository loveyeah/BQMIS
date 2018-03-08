package power.ejb.hr;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.DataChangeException;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity HrJBorrowcontract.
 * 
 * @see power.ejb.hr.HrJBorrowcontract
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJBorrowcontractFacade implements HrJBorrowcontractFacadeRemote {
	/**实例NativeSqlHelper*/
    @EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved HrJBorrowcontract entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJBorrowcontract entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJBorrowcontract entity) {
		LogUtil.log("saving HrJBorrowcontract instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 
	 * @param list
	 */
    public long save2(HrJBorrowcontract entity){
    	LogUtil.log("saving HrJBorrowcontract instance", Level.INFO, null);
    	try{
    		long maxId = bll.getMaxId("HR_J_BORROWCONTRACT", "BORROWCONTRACTID");
    		entity.setBorrowcontractid(maxId);
    		save(entity);
    		
    		LogUtil.log("save successful", Level.INFO, null);
    		return maxId;
    	}catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
    }
	/**
	 * Delete a persistent HrJBorrowcontract entity.
	 * 
	 * @param entity
	 *            HrJBorrowcontract entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJBorrowcontract entity) {
		LogUtil.log("deleting HrJBorrowcontract instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJBorrowcontract.class, entity
					.getBorrowcontractid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 
	 * @param entity
	 */
	public void logicDelete(String strBorrowContractId,String user_id,String strOldUpdateTime)
	              throws DataChangeException,SQLException{
        try{
        	LogUtil.log("EJB:劳务派遣合同删除开始" , Level.INFO, null);
        	HrJBorrowcontract entity = (HrJBorrowcontract)this.findById(Long.parseLong(strBorrowContractId));
        	//取得db最新更新时间
        	String strNewUpdateTime = entity.getLastModifiedDate().toString().substring(0,19);
        	if(!strNewUpdateTime.equals(strOldUpdateTime)){
        		throw new DataChangeException("");
        	}
        	entity.setLastModifiedBy(user_id);
        	entity.setLastModifiedDate(new Date());
        	entity.setIsUse("N");
        	
        	this.update(entity);
        }catch(DataChangeException de){
        	LogUtil.log("EJB:劳务派遣合同删除异常", Level.SEVERE, de);
			throw de;
        } catch (Exception e) {
			LogUtil.log("EJB:劳务派遣合同删除异常", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	/**
	 * 
	 * @param strBorrowContractId
	 * @param user_id
	 * @param strOldUpdateTime
	 * @throws DataChangeException
	 * @throws SQLException
	 */
	public void repoet(String strBorrowContractId,String user_id,String strOldUpdateTime)
	              throws  DataChangeException ,SQLException{
		try{
			LogUtil.log("EJB:劳务派遣合同上报开始" , Level.INFO, null);
			HrJBorrowcontract entity = (HrJBorrowcontract)this.findById(Long.parseLong(strBorrowContractId));
			String strNewUpdateTime = entity.getLastModifiedDate().toString().substring(0,19);
        	if(!strNewUpdateTime.equals(strOldUpdateTime)){
        		throw new DataChangeException("");
        	}
        	entity.setLastModifiedBy(user_id);
        	entity.setLastModifiedDate(new Date());
        	entity.setDcmStatus("1");
        	this.update(entity);
        	LogUtil.log("EJB:劳务派遣合同上报正常结束" , Level.INFO, null);
		}catch(DataChangeException de){
        	LogUtil.log("EJB:劳务派遣合同上报异常", Level.SEVERE, de);
			throw de;
        } catch (Exception e) {
			LogUtil.log("EJB:劳务派遣合同上报异常", Level.SEVERE, e);
			throw new SQLException();
		}
		
	}

	/**
	 * Persist a previously saved HrJBorrowcontract entity and return it or a
	 * copy of it to the sender. A copy of the HrJBorrowcontract entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJBorrowcontract entity to update
	 * @return HrJBorrowcontract the persisted HrJBorrowcontract entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJBorrowcontract update(HrJBorrowcontract entity) {
		LogUtil.log("updating HrJBorrowcontract instance", Level.INFO, null);
		try {
			HrJBorrowcontract result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	public HrJBorrowcontract update2(HrJBorrowcontract entity)throws DataChangeException,SQLException {
		try{
			HrJBorrowcontract entity1 = findById(entity.getBorrowcontractid());
			// 排他处理
			String strNewDate = entity1.getLastModifiedDate().toString().substring(0, 19);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dteDB = sdf.format(entity.getLastModifiedDate());
			String strOldDate = dteDB.toString();
			if(!strNewDate.equals(strOldDate)){
				throw new DataChangeException("");
			}
			//设置合同
			entity1.setWrokContractNo(entity.getWrokContractNo());
			//设置签字日期
			entity1.setSignatureDate(entity.getSignatureDate());
			//设置开始日期
			entity1.setStartDate(entity.getStartDate());
			//设置结束日期
			entity1.setEndDate(entity.getEndDate());
			//设置协作单位ID
			entity1.setCooperateUnitId(entity.getCooperateUnitId());
			//设置调动类型
			entity1.setTransferType(entity.getTransferType());
			//设置备注
			entity1.setNote(entity.getNote());
			//设置内容
			entity1.setContractContent(entity.getContractContent());
			//设置修改人
			entity1.setLastModifiedBy(entity.getLastModifiedBy());
			//设置修改时间
			entity1.setLastModifiedDate(new Date());
			update(entity1);
			return entity1;
		} catch (DataChangeException de) {
			LogUtil.log("EJB:劳务DB操作失败", Level.SEVERE, de);
			throw de;
		} catch (Exception e) {
			LogUtil.log("EJB:劳务订餐DB操作失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
    
	public HrJBorrowcontract findById(Long id) {
		LogUtil.log("finding HrJBorrowcontract instance with id: " + id,
				Level.INFO, null);
		try {
			HrJBorrowcontract instance = entityManager.find(
					HrJBorrowcontract.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJBorrowcontract entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJBorrowcontract property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJBorrowcontract> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJBorrowcontract> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJBorrowcontract instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJBorrowcontract model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJBorrowcontract entities.
	 * 
	 * @return List<HrJBorrowcontract> all HrJBorrowcontract entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJBorrowcontract> findAll() {
		LogUtil
				.log("finding all HrJBorrowcontract instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from HrJBorrowcontract model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}