package power.ejb.manage.project;

import java.text.SimpleDateFormat;
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

/**
 * Facade for entity PrjCCheckFile.
 * 
 * @see power.ejb.manage.project.PrjCCheckFile
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjCCheckFileFacade implements PrjCCheckFileFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public PrjCCheckFile save(PrjCCheckFile entity) {
		LogUtil.log("saving PrjCCheckFile instance", Level.INFO, null);
		try {
			entity.setCheckFileId(bll.getMaxId("PRJ_C_CHECK_FILE", "CHECK_FILE_ID"));
			if("Y".equals(entity.getIsModel())) {
//				if(this.isRepeat(entity.getFileType()) > 0) {
//					return null;
//				} else {
					entity.setIsUse("Y");
					entity.setLastModifiedDate(new Date());
					entityManager.persist(entity);
				//}
			} else {
				entity.setFileNo(this.createFileNo(entity.getFileType()));
				entity.setIsUse("Y");
				entity.setLastModifiedDate(new Date());
				entityManager.persist(entity);
			}
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(PrjCCheckFile entity) {
		LogUtil.log("deleting PrjCCheckFile instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PrjCCheckFile.class, entity
					.getCheckFileId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteMulti(String checkFileIds) {
		String sql = "update  PRJ_C_CHECK_FILE a\n"
				+ "set a.is_use='N' \n" + "where a.check_file_id in (" + checkFileIds
				+ ")";
		bll.exeNativeSQL(sql);
	}

	public PrjCCheckFile update(PrjCCheckFile entity) {
		LogUtil.log("updating PrjCCheckFile instance", Level.INFO, null);
		try {
			PrjCCheckFile result = null;
			if("Y".equals(entity.getIsModel())) {
//				if(this.isRepeat(entity.getFileType()) > 0) {
//					return null;
//				} else {
					entity.setIsUse("Y");
					entity.setLastModifiedDate(new Date());
					result = entityManager.merge(entity);
//				}
			} else {
				//entity.setFileNo(this.createFileNo(entity.getFileType()));
				entity.setIsUse("Y");
				entity.setLastModifiedDate(new Date());
				result = entityManager.merge(entity);
			}
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PrjCCheckFile findById(Long id) {
		LogUtil.log("finding PrjCCheckFile instance with id: " + id,
				Level.INFO, null);
		try {
			PrjCCheckFile instance = entityManager
					.find(PrjCCheckFile.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PrjCCheckFile> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PrjCCheckFile instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PrjCCheckFile model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,String fileName,String flag) {
		LogUtil.log("finding all PrjCCheckFile instances", Level.INFO, null);
		PageObject obj = new PageObject();
		try {
			String sql = "select t.check_file_id,\n" +
					"       t.file_type,\n" + 
					"       t.file_no,\n" + 
					"       t.file_name,\n" + 
					"       getworkername(t.last_modified_by) as last_modified_by,\n" + 
					"       t.last_modified_date,\n" + 
					"       t.is_model,\n" + 
					"       t.enterprise_code,\n" + 
					"       t.is_use,\n" + 
					"       t.file_url\n" + 
					"  from PRJ_C_CHECK_FILE t\n" + 
					" where t.is_use = 'Y'\n" + 
					//"   and t.is_model = 'Y'\n" + 
					"   and t.enterprise_code = '"+enterpriseCode+"'\n";
			String strWhere="";
			if(fileName!=null&&!fileName.equals(""))
			{
				strWhere +="  and t.file_name like '%"+fileName+"%'";
			}
			if(flag !=null && !flag.equals(""))
			{
				if(flag.equals("Y"))//Y----------模板
				{
					strWhere+="   and t.is_model = 'Y'\n";
				}else if(flag.equals("N"))//N--------不是模板
				{
					strWhere+="   and t.is_model = 'N'\n";
				}
			}
			sql +=strWhere;
			List list = bll.queryByNativeSQL(sql, PrjCCheckFile.class);
			obj.setList(list);
			obj.setTotalCount(Long.valueOf(list.size()));
			return obj;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private String createFileNo(Long fileType)
	{
		String myyear = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		myyear = sdf.format(new java.util.Date());
		myyear = myyear.substring(0, 4);
		String no =myyear;
		String sql=
			"select '"+no+"' ||\n" +
			"       (select Trim(case\n" + 
			"                 when max(t.file_no) is null then\n" + 
			"                  '001'\n" + 
			"                 else\n" + 
			"                  to_char(to_number(substr(max(t.file_no), 5, 3) + 1),\n" + 
			"                          '000')\n" + 
			"               end)\n" + 
			"          from PRJ_C_CHECK_FILE t\n" + 
			"         where t.is_use = 'Y'\n" + 
			"         and t.file_type = '"+fileType+"'\n" + 
			"           and substr(t.file_no, 0, 4) = '"+no+"')\n" + 
			"  from dual";
		no= bll.getSingal(sql).toString().trim();
		return no;
	}
	
	private int isRepeat(Long fileType){
		String sql = "select * from PRJ_C_CHECK_FILE t where t.is_use = 'Y' and t.file_type = '"+fileType+"'";
		int count = bll.exeNativeSQL(sql);
		return count;
	}

}