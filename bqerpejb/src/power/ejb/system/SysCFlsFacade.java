package power.ejb.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.system.form.Menu;

/**
 * Facade for entity SysCFls.
 * 
 * @see power.ejb.system.SysCFls
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SysCFlsFacade implements SysCFlsFacadeRemote {
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager; 
	public SysCFls save(SysCFls entity) { 
		try {
			if(entity.getFileId() ==null)
			{
				entity.setFileId(Long.parseLong(bll.getMaxId("sys_c_fls", "file_id").toString()));
			}
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	} 
	public void delete(SysCFls entity) { 
		try {
			entity = entityManager.getReference(SysCFls.class, entity
					.getFileId());
			entityManager.remove(entity); 
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	} 
	public SysCFls update(SysCFls entity) {
		try {
			SysCFls result = entityManager.merge(entity); 
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysCFls findById(Long id) {
		try {
			SysCFls instance = entityManager.find(SysCFls.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	} 
	 
	@SuppressWarnings("unchecked")
	public List<SysCFls> findAll(Long parentFileId,final int... rowStartIdxAndCount) { 
		try {
			final String queryString =
				"select distinct f.*\n" +
				"    from sys_c_fls f\n" + 
				"    where f.parent_file_id="+parentFileId+
				"  and f.is_use='Y'"+
//				"  and f.is_disp='Y'"+
				"   order by f.line"; 
			return  bll.queryByNativeSQL(queryString,SysCFls.class); 
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	} 
	@SuppressWarnings("unchecked")
	public List<SysCFls> findFilesByWorkerId(Long parentFileId,
			Long workerId) {
		String sql = "select distinct * from  sys_c_fls e\n"
			+ "where e.is_use='Y' and e.is_disp='Y'\n"
			+ "and e.parent_file_id="
			+ parentFileId
			+ "\n"
			+ "start with e.file_id in\n"
			+ "            (select distinct c.file_id from sys_j_ur a,sys_j_rrs b,sys_c_fls c\n"
			+ "where a.role_id = b.role_id\n"
			+ "and b.file_id = c.file_id\n" + "and a.worker_id="
			+ workerId + "\n" + "and a.is_use='Y'\n"
			+ "and b.is_use='Y'\n" + "and c.is_use='Y'\n"
			+ "and c.is_disp='Y' )\n"
			+ "CONNECT BY PRIOR e.parent_file_id = e.file_id\n"
			+ "order by e.line asc";  
		return  bll.queryByNativeSQL(sql,SysCFls.class); 
	}
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveCatalog(List<Map> catalog) {
		 for(Map o : catalog)
	   {
	       String sid = ((Map) ((Map) o)).get("sid").toString();
	       String tid = ((Map) ((Map) o)).get("tid").toString(); 
	       SysCFls fls = this.findById(Long.parseLong(sid));
	       fls.setParentFileId(Long.parseLong(tid)); 
	       this.update(fls); 
	   }  
	} 
	public List<SysCFls> findByroleIdP(Long roleId,String propertyName,String propertyValue,boolean iswait){
		try {
			String sql="";
			if(!iswait){
				if(propertyValue.equals("%"))
				{
					sql=
						"select a.*\n" +
						"  from sys_c_fls a, sys_j_rrs b\n" + 
						" where a.file_id = b.file_id\n" + 
						"   and b.role_id = "+roleId+"\n" + 
						"   and b.is_use = 'Y'\n" + 
						"   and a.is_disp = 'Y'\n"+
						"   and a.is_use = 'Y'\n" ;
				}
				else{
					sql=
						"select a.*\n" +
						"  from sys_c_fls a, sys_j_rrs b\n" + 
						" where a.file_id||a." +propertyName+"\n"+
						" like '%"+propertyValue+"%'\n"+
						" and a.file_id = b.file_id\n" + 
						"   and b.role_id = "+roleId+"\n" + 
						"   and b.is_use = 'Y'\n" + 
						"   and a.is_disp = 'Y'\n"+
						"   and a.is_use = 'Y'\n" ;
				}
			}
			else{
				sql=
					"select a.*\n"+
					"from sys_c_fls a\n"+
					"where a.is_use = 'Y'"+
					"  and a.is_disp = 'Y'\n"+
					" and a.file_id||a." +propertyName+"\n"+
					" like '%"+propertyValue+"%'\n"+
					" and a.file_id not in ("+
						"select a.file_id\n"+
						           "from sys_c_fls a, sys_j_rrs b \n"+
						           "where a.file_id = b.file_id and b.is_use = 'Y' \n"+
						            "and b.role_id="+roleId+")";
			}
			List<SysCFls> list = bll.queryByNativeSQL(sql,SysCFls.class);
			return list;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public List<SysCFls> findFileBywId(Long workerId){
		try{
			final String sql =
			"select distinct f.*\n" +
			"    from sys_c_fls f, sys_c_ul u, sys_j_ur ur, sys_j_rrs rr\n" + 
			"   where u.worker_id = ur.worker_id\n" + 
			"     and u.worker_id = \n" + 
			workerId+
			"     and ur.role_id = rr.role_id\n" + 
			"     and rr.file_id = f.file_id\n" + 
			"     and rr.is_use = 'Y'\n" + 
			"     and f.is_use = 'Y'\n" + 
			"     and ur.is_use = 'Y'\n" + 
			"     and u.is_use = 'Y'\n" + 
			"     and u.is_use = 'Y'\n" + 
			"     and f.is_disp = 'Y'\n";
			List<SysCFls> list = bll.queryByNativeSQL(sql,SysCFls.class);						
			return list;
		}catch (RuntimeException re) {
			LogUtil.log("find SysCFls failed", Level.SEVERE, re);
			throw re;
		}
	}
	public List<SysCFls> findFileByPRoleId(Long roleId,Long parentFileId,boolean iswait){
		try{
			String sql=""; 
			if(iswait){
				sql=
					"select distinct f.*\n" +
					"  from sys_c_fls f\n" + 
					" where f.is_disp = 'Y'\n" + 
					"   and f.is_use = 'Y'\n" + 
					"   and f.parent_file_id ="+parentFileId+"\n" + 
					" start with f.file_id in (select distinct rf.file_id\n" + 
					"                            from sys_j_rrs rf\n" + 
					"                           where rf.role_id ="+roleId+"\n" +
					"                             and rf.is_use = 'Y')\n" + 
					"CONNECT BY PRIOR f.parent_file_id = f.file_id\n" + 
					" order by f.line asc";
			}
			else{
				sql=
					"select distinct a.* from sys_c_fls a " +
					"     where  a.parent_file_id ="+parentFileId+"\n" +  
					"	start with a.file_id  in (select distinct f.file_id\n" +
					"    from sys_c_fls f\n" + 
					"    where f.is_disp='Y'\n" + 
					"     and f.is_use='Y'\n" + 
					"      and  f.file_id  not in\n" + 
					"         (select  rf.file_id from sys_j_rrs rf where rf.role_id = "+roleId+" and rf.is_use='Y'))\n" + 
					"         CONNECT BY PRIOR a.parent_file_id =a.file_id\n" + 
					"	and a.is_use='Y'\n" +
					"   and a.is_disp='Y'\n"+

					"order by line asc";

//					"select distinct f.*\n" +
//					"    from sys_c_fls f\n" + 
//					"     where f.is_disp = 'Y'\n" + 
//					"     and f.is_use = 'Y'"+
//					"     and  f.parent_file_id ="+parentFileId+"\n" + 
//					"    start with f.file_id not in\n" + 
//					"         (select distinct rf.file_id from sys_j_rrs rf where rf.role_id = "+roleId+" and rf.is_use='Y')\n" + 
//					"CONNECT BY PRIOR f.parent_file_id = f.file_id\n"+
//					"order by f.line asc";

			}
			List<SysCFls> list = bll.queryByNativeSQL(sql,SysCFls.class);
			return list;
		}catch (RuntimeException re) {
			LogUtil.log("find SysCFls failed", Level.SEVERE, re);
			throw re;
		}
	}
	public boolean findFilesByPId(Long parentFileId){
		final String sql="select count(1) from sys_c_fls f where f.parent_file_id="+parentFileId+" and f.is_use='Y' and f.is_disp='Y'";
		if(Integer.parseInt(bll.getSingal(sql).toString())>0){
			return true;
		}
		else{
			return false;
		}
	}
	public Menu findMenusByWorkerId(String enterpriseCode,Long workerId){
		Menu menu = null;
		String sql =  "select distinct e.parent_file_id,\n" +
			"                e.file_id,\n" + 
			"                e.file_name,\n" + 
			"                e.file_addr,\n" + 
			"                e.memo,\n" + 
			"                e.is_file,\n" + 
			"                e.line\n" + 
			"  from sys_c_fls e\n" + 
			" where e.is_use = 'Y'\n" + 
			"   and e.is_disp = 'Y'\n" + 
			" start with e.file_id in (select distinct c.file_id\n" + 
			"                            from sys_j_ur a, sys_j_rrs b, sys_c_fls c\n" + 
			"                           where a.role_id = b.role_id\n" + 
			"                             and b.file_id = c.file_id\n" + 
			"                             and a.worker_id = "+workerId+"\n" + 
			"                             and a.is_use = 'Y'\n" + 
			"                             and b.is_use = 'Y'\n" + 
			"                             and c.is_use = 'Y'\n" + 
			"                             and c.is_disp = 'Y')\n" + 
			"CONNECT BY PRIOR e.parent_file_id = e.file_id\n" + 
			" order by e.parent_file_id, e.line asc";  
		List<Object[]> list = bll.queryByNativeSQL(sql);
		if(list != null)
		{
			menu = new Menu();
			fillMenuData(menu,list,1);
		} 
		return menu;
	} 
	private void fillMenuData(Menu menu,List<Object[]> list,long pid)
	{
		List<Object[]> childList = this.findChildMenus(list, pid);
		if(childList!=null && childList.size()>0)
		{
			menu.setMenu(new ArrayList<Menu>()); 
			for(Object[] obj:childList)
			{
				Menu temp = new Menu(); 
				temp.setId(Long.parseLong(obj[1].toString()));
				if (obj[2] != null)
					temp.setText(obj[2].toString());
				if (obj[4] != null)
					temp.setMemo(obj[4].toString());
				if (obj[5] != null)
					temp.setIsLeaf(obj[5].toString());
				if(obj[3] != null)
				temp.setUrl(obj[3].toString());
				menu.getMenu().add(temp);
				if("N".equals(temp.getIsLeaf()))
				{
					temp.setIconCls("menu-item-folder");
					fillMenuData(temp,list,temp.getId());
				}
				else
				{
					temp.setIconCls("menu-item-page");
				}
			}
		}
	}
	private List<Object[]> findChildMenus(List<Object[]> list,long pid)
	{
		List<Object[]> childMenus = new ArrayList<Object[]>();
		Iterator<Object[]> iter = list.iterator();
		boolean isBegin = false;
		while(iter != null && iter.hasNext())
		{ 
			Object[] obj = iter.next();
			long _pid =Long.parseLong(obj[0].toString());
			if(pid == _pid)
			{
				if(!isBegin)
				{
					isBegin = true;
				}
				childMenus.add(obj);
				iter.remove();
			}
			if(isBegin && pid != _pid)
			{ 
				break;
			}
		} 
		return childMenus;
	}
}