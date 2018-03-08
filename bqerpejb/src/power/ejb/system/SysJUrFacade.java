package power.ejb.system;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 用户对应角色管理 
 * @author wzhyan
 */
@Stateless
public class SysJUrFacade implements SysJUrFacadeRemote {

	@PersistenceContext 
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB (beanName="SysJUrFacade")
	protected SysJUrFacadeRemote remote; 
	public void save(SysJUr entity) { 
		try {
			if(entity.getId() == null)
			{
				entity.setId(bll.getMaxId("sys_j_ur", "id"));
			}
			entity.setIsUse("Y");
			entity.setModifyDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	} 
	public void delete(SysJUr entity) {
		entity.setIsUse("N");
		entity.setModifyDate(new Date());
		this.update(entity);
	}

	 
	public SysJUr update(SysJUr entity) {
		LogUtil.log("updating SysJUr instance", Level.INFO, null);
		try {
			SysJUr result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysJUr findURByURId(Long roleId,Long workerId,boolean isuse){
		try{
			String sql="";
			if(isuse){
				sql="select * from sys_j_ur where role_id="+roleId+" and worker_id="+workerId+" and is_use='Y' and rownum=1 ";
			}else{
				sql="select * from sys_j_ur where role_id="+roleId+" and worker_id="+workerId+" and is_use='N' and rownum=1 ";
			}
			List list=bll.queryByNativeSQL(sql,SysJUr.class);
			if(list.size()>0){
				SysJUr model=(SysJUr)bll.queryByNativeSQL(sql,SysJUr.class).get(0);
				return model;
			}
			else{
				return null;
			}
		}catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public boolean validateFileRight(Long workerId,String fileId,String filePath){ 
		String sqlstr =  "select count(1)\n" +
			"  from sys_c_fls c\n" + 
			" where trim(c.file_addr) = ?\n" + 
			"   and c.is_file = 'Y'\n" + 
			"   and c.is_use = 'Y'";
		int isValFile = Integer.parseInt(bll.getSingal(sqlstr, new Object[]{filePath}).toString());
		if(isValFile>0)
		{ 
			if(fileId == null)
			{ 
				return false;
			}
			String sql =  
				"select count(1)\n" +
				"  from sys_j_ur a, sys_j_rrs b, sys_c_fls c\n" + 
				" where a.role_id = b.role_id\n" + 
				"   and b.file_id = c.file_id\n" + 
				"   and c.is_file = 'Y'\n" + 
				"   and c.is_use = 'Y'\n" + 
				"   and a.worker_id = ?\n" + 
				"   and a.is_use = 'Y'\n" + 
				"   and b.is_use = 'Y'\n" + 
				"   and c.file_id=? \n" + 
				"   and trim(c.file_addr) = ?"; 
			Object o = bll.getSingal(sql, new Object[]{workerId,fileId,filePath});
			if(Integer.parseInt(o.toString())==0)
			{
				return false;
			}  
		}
		return true;
	}
	private boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	} 
	
	public void deleteByWorkCodeAndRoleId(String workCode,Long roleId)
	{
		String sql=
			"delete sys_j_ur a\n" +
			"where a.role_id="+roleId+"\n" + 
			"and a.worker_id=(select b.worker_id from sys_c_ul b where b.worker_code='"+workCode+"' and b.is_use='Y')";
		bll.exeNativeSQL(sql);
	}
//	/**
//	 *  
//	 * @param workerId
//	 * @param fileId
//	 * @return
//	 */
//	private boolean validateFileRight(Long workerId,Long fileId){ 
//		String sql = 
//			"select count(1)\n" +
//			"  from sys_j_ur a, sys_j_rrs b\n" + 
//			" where a.role_id = b.role_id\n" + 
//			"   and a.worker_id = ? \n" + 
//			"   and a.is_use = 'Y'\n" + 
//			"   and b.is_use = 'Y'\n" + 
//			"   and b.file_id = ? ";
//		Object o = bll.getSingal(sql, new Object[]{workerId,fileId});
//		if(Long.parseLong(o.toString())>0)
//		{
//			return true;
//		} 
//		return false;
//	}
}