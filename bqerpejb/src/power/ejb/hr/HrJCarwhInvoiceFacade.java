package power.ejb.hr;

import java.sql.SQLException;
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

/**
 * Facade for entity HrJCarwhInvoice.
 *
 * @see power.ejb.hr.HrJCarwhInvoice
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJCarwhInvoiceFacade implements HrJCarwhInvoiceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved HrJCarwhInvoice entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrJCarwhInvoice entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJCarwhInvoice entity) {
		LogUtil.log("新增劳动合同附件开始。", Level.INFO, null);
		try {
			Date date = new Date();
			// 记录时间
			entity.setInsertdate(date);
			// 上次修改时间
			entity.setLastModifiedDate(date);
			// id
			if(entity.getFileId() == null){
				entity.setFileId(bll.getMaxId(
						"HR_J_CARWH_INVOICE", "FILE_ID"));
			}
			entityManager.persist(entity);
			LogUtil.log("新增劳动合同附件结束。", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("新增劳动合同附件失败。", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrJCarwhInvoice entity.
	 *
	 * @param entity
	 *            HrJCarwhInvoice entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJCarwhInvoice entity) {
		LogUtil.log("deleting HrJCarwhInvoice instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJCarwhInvoice.class, entity
					.getFileId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrJCarwhInvoice entity and return it or a copy
	 * of it to the sender. A copy of the HrJCarwhInvoice entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 *
	 * @param entity
	 *            HrJCarwhInvoice entity to update
	 * @return HrJCarwhInvoice the persisted HrJCarwhInvoice entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJCarwhInvoice update(HrJCarwhInvoice entity) {
		LogUtil.log("更新劳动合同开始。", Level.INFO, null);
		try {
			// 上次修改时间
			entity.setLastModifiedDate(new Date());
			HrJCarwhInvoice result = entityManager.merge(entity);
			LogUtil.log("更新劳动合同结束。", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("更新劳动合同失败", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJCarwhInvoice findById(Long id) {
		LogUtil.log("finding HrJCarwhInvoice instance with id: " + id,
				Level.INFO, null);
		try {
			HrJCarwhInvoice instance = entityManager.find(
					HrJCarwhInvoice.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJCarwhInvoice entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrJCarwhInvoice property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJCarwhInvoice> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJCarwhInvoice> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJCarwhInvoice instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJCarwhInvoice model where model."
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
	 * Find all HrJCarwhInvoice entities.
	 *
	 * @return List<HrJCarwhInvoice> all HrJCarwhInvoice entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJCarwhInvoice> findAll() {
		LogUtil.log("finding all HrJCarwhInvoice instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJCarwhInvoice model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 根据合同id和附件来源查找劳动合同附件
	 * @param workContractId 合同id
	 * @param fileOriger 附件来源
	 * @param enterpriseCode 企业编码
	 * @return 查找结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAppendFileByIdAndOriger(Long workContractId,
			String fileOriger, String enterpriseCode)throws SQLException{
		try{
			String strSql = "SELECT I.FILE_ID,I.WORKCONTRACTID,I.FILE_TYPE,I.FILE_KIND,I.FILE_NAME" +
					",I.FILE_TEXT,I.FILE_ORIGER,I.ENTERPRISE_CODE,I.IS_USE,I.LAST_MODIFIED_BY" +
					",I.LAST_MODIFIED_DATE,I.INSERTDATE,I.INSERTBY " +
					"  FROM HR_J_CARWH_INVOICE I" +
					" WHERE I.WORKCONTRACTID = ?" +
					" AND I.FILE_ORIGER = ?" +
					" AND I.IS_USE = ?" +
					" AND I.ENTERPRISE_CODE = ?" +
					"  ORDER BY I.FILE_ID";
			LogUtil.log("查找劳动合同附件开始。" , Level.INFO, null);
			PageObject obj = new PageObject();
			List<HrJCarwhInvoice> list = bll.queryByNativeSQL(strSql, new Object[] {workContractId,fileOriger,
					"Y",enterpriseCode}, HrJCarwhInvoice.class);
			obj.setList(list);
			obj.setTotalCount((long)list.size());
			LogUtil.log("查找劳动合同附件结束。", Level.INFO, null);
			return obj;
		}catch(Exception e){
			LogUtil.log("EJB:查找劳动合同附件失败。", Level.SEVERE, e);
			throw new SQLException(e.getMessage());
		}
	}
}