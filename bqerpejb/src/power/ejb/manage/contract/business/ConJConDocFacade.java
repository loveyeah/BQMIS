package power.ejb.manage.contract.business;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import power.ejb.manage.contract.form.ConDocForm;

/**
 * Facade for entity ConJConDoc.
 * 
 * @see power.ejb.manage.contract.business.ConJConDoc
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConJConDocFacade implements ConJConDocFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	

	public ConJConDoc save(ConJConDoc entity) {
		LogUtil.log("saving ConJConDoc instance", Level.INFO, null);
		try {
			if(entity.getConDocId() == null)
			{
				entity.setConDocId(bll.getMaxId("CON_J_CON_DOC", "con_doc_id"));
			}
			entity.setIsUse("Y");
			entity.setLastModifiedDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(Long contypeId) {
		ConJConDoc entity = new ConJConDoc();
		entity = this.findById(contypeId);
		if (entity != null) {
			entity.setIsUse("N");
			this.update(entity);
		}
	}
	
	public void deleteMulti(String contypeIds)
	{
		String sql=
			"update con_j_con_doc  t\n" +
			"set t.is_use='N'\n" + 
			"where t.con_doc_id in ("+contypeIds+")";
		bll.exeNativeSQL(sql);
	}

	public ConJConDoc update(ConJConDoc entity) {
		LogUtil.log("updating ConJConDoc instance", Level.INFO, null);
		try {
			entity.setLastModifiedDate(new Date());
			ConJConDoc result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJConDoc findById(Long id) {
		LogUtil.log("finding ConJConDoc instance with id: " + id, Level.INFO,
				null);
		try {
			ConJConDoc instance = entityManager.find(ConJConDoc.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ConJConDoc> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding ConJConDoc instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from ConJConDoc model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ConJConDoc> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all ConJConDoc instances", Level.INFO, null);
		try {
			final String queryString = "select model from ConJConDoc model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PageObject findConDocList(String enterpriseCode,Long keyId,String docType,Long conDocId)
	{
		PageObject result = new PageObject();
		String sql = "select c.con_doc_id,\n" +
					"       c.key_id,\n" + 
					"       c.doc_type,\n" + 
					"       c.doc_name,\n" + 
					"       c.doc_content,\n" + 
					"       c.doc_memo,\n" + 
					"       c.ori_file_name,\n" + 
					"       c.ori_file_ext,\n" + 
					"       c.last_modified_by,\n" + 
					"       nvl(getworkername(c.LAST_MODIFIED_BY), c.last_modified_by),\n" + 
					"       to_char(c.last_modified_date, 'yyyy-mm-dd hh24:mi:ss'),\n" + 
					"       c.is_use,\n" + 
					"       c.enterprise_code,\n" + 
					"       (select f.con_modify_id\n" + 
					"          from con_j_modify f\n" + 
					"         where f.con_modify_id = c.key_id\n" + 
					"           and f.is_use = 'Y'\n" + 
					"           and f.enterprise_code = '"+enterpriseCode+"') con_modify_id\n" + 
					"  from con_j_con_doc c\n" + 
					" where c.is_use = 'Y'\n" + 
					"   and c.key_id = "+keyId+"\n" + 
//					"   and c.doc_type = '"+docType+"'\n" + 
					"   and c.enterprise_code = '"+enterpriseCode+"'\n" ;
//					" order by c.doc_name";

		String strWhere = ""; 
		if (keyId != null && !"".equals(keyId)) {
			strWhere += " and c.key_id='" + keyId + "'\n";
		}
		if (docType != null && !"".equals(docType)) {
			strWhere += " and c.doc_type='" + docType + "'\n";
		}
		if (conDocId != null && !"".equals(conDocId)) {
			strWhere += " and c.con_doc_id='" + conDocId + "'\n";
		}
		strWhere += " order by c.doc_name";
		sql += strWhere;
		List list = bll.queryByNativeSQL(sql);
		List arraylist = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			ConDocForm model = new ConDocForm();
			Object[] data=(Object[])it.next();
			if(data[0] != null)
			{
				model.setConDocId(Long.parseLong(data[0].toString()));
			}
			if(data[1] != null)
			{
				model.setKeyId(Long.parseLong(data[1].toString()));
			}
			if(data[2]!=null)
			{
				model.setDocType(data[2].toString());
			}
			if(data[3] != null)
			{
				model.setDocName(data[3].toString());
			}
			if(data[4] != null)
			{
				model.setDocContent(data[4].toString());
			}
			if(data[5] != null)
			{
				model.setDocMemo(data[5].toString());
			}
			if(data[6] != null)
			{
				model.setOriFileName(data[6].toString());
				model.setOriFile(data[6].toString() +"." + data[7].toString());
			}
			if(data[7] != null)
			{
				model.setOriFileExt(data[7].toString());
			}
			if(data[8] != null)
			{
				model.setLastModifiedBy(data[8].toString());
			}
			if(data[9] != null)
			{
				model.setLastModifiedName(data[9].toString());
			}
			if(data[10] != null)
			{
				model.setLastModifiedDate(data[10].toString());
			}
			if(data[11]!= null)
			{
				model.setIsUse(data[11].toString());
			}
			if(data[12] != null)
			{
				model.setEnterpriseCode(data[12].toString());
			}
			if(data[13] != null)
			{
				model.setConModifyId(Long.parseLong(data[13].toString()));
			}		
				
			arraylist.add(model);
		}
		result.setList(arraylist);
		return result;
	}
	public ConJConDoc findConDocModel(String enterpriseCode,Long keyId,String docType)
	{
		String sql ="select *\n" +
					"  from con_j_con_doc c\n" + 
					" where c.is_use = 'Y'\n" + 
					"   and c.enterprise_code = '"+enterpriseCode+"'\n" + 
					"   and c.key_id = "+keyId+"\n" + 
					"   and c.doc_type = '"+docType+"'";
        List<ConJConDoc> list = bll.queryByNativeSQL(sql, ConJConDoc.class);
		
		if(list != null&& list.size()>0)
		{
			return list.get(0);
		}
		return null;
	}
}