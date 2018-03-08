package power.ejb.message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity SysCMessageDocType.
 * 
 * @see power.ejb.message.SysCMessageDocType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SysCMessageDocTypeFacade implements SysCMessageDocTypeFacadeRemote {
	// property constants
	public static final String DOC_TYPE_NAME = "docTypeName";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String LAST_MODIFIED_DATE = "lastModifiedDate";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	@SuppressWarnings("unchecked")
	public SysCMessageDocType save(SysCMessageDocType entity) throws CodeRepeatException {
		LogUtil.log("saving SysCMessageDocType instance", Level.INFO, null);
		try {
			if(entity.getDocTypeId() == null){
			entity.setDocTypeId(bll.getMaxId("SYS_C_MESSAGE_DOC_TYPE", "doc_type_id"));
			}
			if(checkNameSameForAdd(entity.getDocTypeName())){
				throw new CodeRepeatException("该文档类型已经存在!");
			}
			Date date = new Date();
			entity.setLastModifiedDate(date);
			entity.setIsUse("Y");
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
//新增判断
	private boolean checkNameSameForAdd(String docTypeName) {
		String sql = "select count(1) from SYS_C_MESSAGE_DOC_TYPE t where t.doc_type_name=? and t.is_use='Y'";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] {docTypeName}).toString());
		if (size > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Delete a persistent SysCMessageDocType entity.
	 * 
	 * @param entity
	 *            SysCMessageDocType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	
//	public void delete(SysCMessageDocType entity) {
//		LogUtil.log("deleting SysCMessageDocType instance", Level.INFO, null);
//		try {
//			entity = entityManager.getReference(SysCMessageDocType.class,
//					entity.getDocTypeId());
//			entityManager.remove(entity);
//			LogUtil.log("delete successful", Level.INFO, null);
//		} catch (RuntimeException re) {
//			LogUtil.log("delete failed", Level.SEVERE, re);
//			throw re;
//		}
//	}

	
	public SysCMessageDocType update(SysCMessageDocType entity) throws CodeRepeatException {
		LogUtil.log("updating SysCMessageDocType instance", Level.INFO, null);
		try {
			if(checkNameSameForUpdate(entity.getDocTypeId(),entity.getDocTypeName())){
				throw new CodeRepeatException("该文档类型已经存在!");
			}
//			SimpleDateFormat codeFormat = new SimpleDateFormat(
//			"yyyyMMddmmss");
			
			Date codevalue = new Date();
			entity.setLastModifiedDate(codevalue);
			SysCMessageDocType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
//修改同名判断
	@SuppressWarnings("unchecked")
	public boolean checkNameSameForUpdate(Long docTypeId,String docTypeName){
		String sql = "select count(1) from sys_c_message_doc_type t where t.doc_type_name=? and t.doc_type_id<>?";
		int size = Integer.parseInt(bll.getSingal(sql, new Object[] {docTypeName,docTypeId}).toString());
		if(size > 0){
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public SysCMessageDocType findById(Long id) {
		LogUtil.log("finding SysCMessageDocType instance with id: " + id,
				Level.INFO, null);
		try {
			SysCMessageDocType instance = entityManager.find(
					SysCMessageDocType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	//删除
	@SuppressWarnings("unchecked")
	public void delete (SysCMessageDocType entity) {;
		entity.setIsUse("N");
		try {
			this.update(entity);
		} catch (CodeRepeatException e) {
			e.printStackTrace();
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<SysCMessageDocType> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding SysCMessageDocType instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SysCMessageDocType model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<SysCMessageDocType> findByDocTypeName(Object docTypeName) {
		return findByProperty(DOC_TYPE_NAME, docTypeName);
	}

	public List<SysCMessageDocType> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<SysCMessageDocType> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<SysCMessageDocType> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all SysCMessageDocType entities.
	 * 
	 * @return List<SysCMessageDocType> all SysCMessageDocType entities
	 */
	@SuppressWarnings("unchecked")
	public List<SysCMessageDocType> findAll() {
		LogUtil.log("finding all SysCMessageDocType instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from SysCMessageDocType model where is_Use='Y'";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void delete(String ids) {
		// TODO Auto-generated method stub
		
	}

}