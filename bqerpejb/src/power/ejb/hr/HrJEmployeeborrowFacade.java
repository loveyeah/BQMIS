package power.ejb.hr;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.DataChangeException;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity HrJEmployeeborrow.
 * 
 * @see power.ejb.hr.HrJEmployeeborrow
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJEmployeeborrowFacade implements HrJEmployeeborrowFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	/**实例NativeSqlHelper*/
    @EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    //删除
    private static final String DEF_FLAG_DELETE = "0";
    //更新
    private static final String DEF_FLAG_UPDATE = "1";
    //新增
    private static final String DEF_FLAG_ADD = "2";
	
    
    /**
	 * Perform an initial save of a previously unsaved HrJEmployeeborrow entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJEmployeeborrow entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJEmployeeborrow entity) {
		LogUtil.log("saving HrJEmployeeborrow instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrJEmployeeborrow entity.
	 * 
	 * @param entity
	 *            HrJEmployeeborrow entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJEmployeeborrow entity) {
		LogUtil.log("deleting HrJEmployeeborrow instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJEmployeeborrow.class, entity
					.getEmployeeborrowid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrJEmployeeborrow entity and return it or a
	 * copy of it to the sender. A copy of the HrJEmployeeborrow entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJEmployeeborrow entity to update
	 * @return HrJEmployeeborrow the persisted HrJEmployeeborrow entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJEmployeeborrow update(HrJEmployeeborrow entity) {
		LogUtil.log("updating HrJEmployeeborrow instance", Level.INFO, null);
		try {
			HrJEmployeeborrow result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJEmployeeborrow findById(Long id) {
		LogUtil.log("finding HrJEmployeeborrow instance with id: " + id,
				Level.INFO, null);
		try {
			HrJEmployeeborrow instance = entityManager.find(
					HrJEmployeeborrow.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJEmployeeborrow entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJEmployeeborrow property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJEmployeeborrow> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJEmployeeborrow> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJEmployeeborrow instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJEmployeeborrow model where model."
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
	 * Find all HrJEmployeeborrow entities.
	 * 
	 * @return List<HrJEmployeeborrow> all HrJEmployeeborrow entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJEmployeeborrow> findAll() {
		LogUtil
				.log("finding all HrJEmployeeborrow instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from HrJEmployeeborrow model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 
	 * @param list
	 * @throws SQLException
	 * @throws CodeRepeatException
	 */
	public void  save2(List<HrJEmployeeborrow> list)throws SQLException,CodeRepeatException{
		try{
			for(int i=0;i<list.size();i++){
				//取得外借人员bean
				HrJEmployeeborrow entity = list.get(i);
				//合同ID
				long maxBorrowcontractidId = bll.getMaxId("HR_J_BORROWCONTRACT", "BORROWCONTRACTID");
				//员工ID
				long maxEmployeeId =  bll.getMaxId("HR_J_EMPLOYEEBORROW", "EMPLOYEEBORROWID")+i;
				if(entity.getEmployeeborrowid() == null){
					entity.setEmployeeborrowid(maxEmployeeId);
				}
				if(entity.getBorrowcontractid() == null){
					entity.setBorrowcontractid(maxBorrowcontractidId);
				}
				//检查重复性 
				
				this.save(entity);
			}
			
		}catch(Exception e){
			LogUtil.log("EJB:劳务DB操作失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	/**
	 * 
	 * @param list
	 * @throws DataChangeException
	 * @throws SQLException
	 * @throws CodeRepeatException
	 */	
	public void update2(List<HrJEmployeeborrow> list)throws DataChangeException,SQLException,CodeRepeatException{
		try{
			for(int i=0;i<list.size();i++){
				//取得修改外借人员bean
				HrJEmployeeborrow entity = list.get(i);;
				//排它处理
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strOldDate = sdf.format(entity.getLastModifiedDate());
				//取得外借人员Id
				HrJEmployeeborrow entity1 = findById(entity.getEmployeeborrowid());
				//db时间
				String strNewDate = entity1.getLastModifiedDate().toString().substring(0, 19);
				if(!strNewDate.equals(strOldDate)){
		        	throw new DataChangeException("");
		        }
				//重新设置
				//是否已回
				entity1.setIfBack(entity.getIfBack());
				//开始日期
				entity1.setStartDate(entity.getStartDate());
				//结束日期
				entity1.setEndDate(entity.getEndDate());
				//停薪日期
				entity1.setStopPayDate(entity.getStopPayDate());
				//起薪日期
				entity1.setStartPayDate(entity.getStartPayDate());
				//备注
				entity1.setMemo(entity.getMemo());
				//上次修改人
				entity1.setLastModifiedBy(entity.getLastModifiedBy());
				//修改时间
				entity1.setLastModifiedDate(new Date());
		        
		        //更新db
		        this.update(entity1);
				
				
			}
			
		}catch(DataChangeException de){
			LogUtil.log("EJB:劳务派遣合同异常", Level.SEVERE, de);
			throw de;
		}catch(Exception e){
			LogUtil.log("EJB:劳务DB操作失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	/**
	 * 
	 * @param list
	 * @throws DataChangeException
	 * @throws SQLException
	 * @throws CodeRepeatException
	 */	
	public void delete2(List<HrJEmployeeborrow> list)throws DataChangeException,SQLException,CodeRepeatException{
		try{
			for(int i=0;i<list.size();i++){
				//取得外借人员bean
				//取得外借人员Id
				HrJEmployeeborrow entity = list.get(i);;
				//排它处理
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strOldDate = sdf.format(entity.getLastModifiedDate());
				//取得外借人员Id
				HrJEmployeeborrow entity1 = findById(entity.getEmployeeborrowid());
				//db时间
				String strNewDate = entity1.getLastModifiedDate().toString().substring(0, 19);
				
				if(!strNewDate.equals(strOldDate)){
	        		throw new DataChangeException("");
	        	}
	        	entity1.setIsUse("N");
	        	update(entity1);
				
			}
			
		}catch(DataChangeException de){
			LogUtil.log("EJB:劳务派遣合同异常", Level.SEVERE, de);
			throw de;
		}catch(Exception e){
			LogUtil.log("EJB:劳务DB操作失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 重复性check
	 * @throws CodeRepeatException
	 */
	public void checkEmployeeRepeat(HrJEmployeeborrow entity)throws CodeRepeatException{
		LogUtil
		.log("finding all HrJEmployeeborrow instances", Level.INFO,null);
		try{
			//取得员工ID
			long employeeId = entity.getEmpId();
			String sql = "SELECT COUNT(*) " +
					"FROM HR_J_EMPLOYEEBORROW " +
					"WHERE EMP_ID = "+employeeId+" AND IF_BACK ='0' AND IS_USE ='Y' AND ENTERPRISE_CODE = ? ";
			List lstParams = new ArrayList();
			
			//取得企业CODE
			lstParams.add(entity.getEnterpriseCode());
			int num = Integer.parseInt(bll.getSingal(sql,lstParams.toArray()).toString());
			if( num > 0){
				throw new CodeRepeatException("");
			}
		}catch(CodeRepeatException de){
			
			LogUtil.log("EJB:员工重复异常", Level.SEVERE, de);
			throw de;
		}
	}

}