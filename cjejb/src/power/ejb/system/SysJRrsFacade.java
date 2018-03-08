package power.ejb.system;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

@Stateless
public class SysJRrsFacade implements SysJRrsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(SysJRrs entity) { 
		try {
			if(entity.getId()==null)
			{
				entity.setId(bll.getMaxId("sys_j_rrs", "id"));
				
			}
			entity.setIsUse("Y");
			entity.setModifyDate(new Date());
			entityManager.persist(entity); 
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(SysJRrs entity) { 
		try {
			entity = entityManager.getReference(SysJRrs.class, entity.getId());
			entityManager.remove(entity); 
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysJRrs update(SysJRrs entity) { 
		try {
			SysJRrs result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<SysCFls> findFilesByRoleId(Long roleId, boolean iswait) {
		if (!iswait) {
			final String sql = "select f.*\n"
					+ "  from sys_c_fls f, sys_j_rrs rf\n"
					+ " where rf.role_id = " + roleId + "\n"
					+ "   and rf.file_id = f.file_id\n"
					+ "   and rf.is_use = 'Y'";
			return bll.queryByNativeSQL(sql, SysCFls.class);
		} else {
			final String waitsql = "select t.*\n" + "  from sys_c_fls t\n"
					+ " where t.is_file = 'Y'\n" + "   and t.is_use = 'Y'\n"
					+ "   and t.file_id not in\n"
					+ "       (select h.file_id\n"
					+ "          from sys_j_rrs h\n"
					+ "         where h.role_id = " + roleId + "\n"
					+ "           and h.is_use = 'Y'\n" + "          )";
			return bll.queryByNativeSQL(waitsql, SysCFls.class);
		}
	}

	public SysJRrs findByRoleIdFileId(Long roleId,Long fileId){
		final String sql=
			"select *\n" +
			"  from sys_j_rrs\n" + 
			" where file_id = "+fileId+"\n" + 
			"   and role_id = "+roleId+"\n" ;
		final String sqlcount="select count(1)\n" +
		"  from sys_j_rrs\n" + 
		" where file_id = "+fileId+"\n" + 
		"   and role_id = "+roleId+"\n" ;
		if(Integer.parseInt(bll.getSingal(sqlcount).toString())!=0){
			Query query = entityManager.createNativeQuery(sql, SysJRrs.class);
			return (SysJRrs)query.getSingleResult();
		}
		else{
			return null;
		}		
	}
}