package power.ejb.workticket.business;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunJWorkticketContent.
 * 
 * @see power.ejb.workticket.business.RunJWorkticketContent
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJWorkticketContentFacade implements
		RunJWorkticketContentFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunJWorkticketContent save(RunJWorkticketContent entity) {
		try {
				if(entity.getId()==null)
				{
					entity.setId(bll.getMaxId("RUN_J_WORKTICKET_CONTENT", "ID"));
				}
				if(entity.getAttributeCode()==null||entity.getAttributeCode().equals(""))
				{
					entity.setAttributeCode("temp");
				}
				entity.setCreateDate(new java.util.Date());
				entity.setIsUse("Y"); 
			  entityManager.persist(entity);
			  return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	//delete by fyyang 090523
//	public void modifyRecords(List<RunJWorkticketContent> list,String delIds){
//		String workticketNo="";
//		if(list !=null)
//		{ 
//			Long id = bll.getMaxId("RUN_J_WORKTICKET_CONTENT", "ID");
//			int i=0;
//			for (RunJWorkticketContent m : list) {
//				workticketNo=m.getWorkticketNo();
//				if (m.getId() != null) { 
//					update(m);
//				} else {
//					m.setId(id + (i++));
//					save(m);
//				} 
//			}
//		}
//		if(delIds != null&& !delIds.trim().equals(""))
//		{
//			if(workticketNo.equals(""))
//			{
//				//add by fyyang 090513
//				String[] contentIds=delIds.split(",");
//				String mySql=
//					"select a.workticket_no from run_j_workticket_content a\n" +
//					"where a.id="+contentIds[0];
//				Object obj=bll.getSingal(mySql);
//				if(obj!=null)
//				{
//					workticketNo=obj.toString();
//				}
//
//			}
//			String sql = "delete from run_j_workticket_content c where c.id in("+delIds+")";  
//			bll.exeNativeSQL(sql);
//			
//		}
//		//add by fyyang 090513
//		entityManager.flush();
//		if(!workticketNo.equals(""))
//		{
//			String content=this.getWorkticketContent(workticketNo);
//			String sql=
//				"update run_j_worktickets t\n" +
//				"set t.workticket_content='"+content+"'\n" + 
//				"where t.workticket_no='"+workticketNo+"'";
//			bll.exeNativeSQL(sql);
//
//		}
//		
//	}
	
	//add by fyyang 090523
	public void modifyRecords(String workticketNo,List<RunJWorkticketContent> list,String delIds){
		if(list !=null)
		{ 
			Long id = bll.getMaxId("RUN_J_WORKTICKET_CONTENT", "ID");
			int i=0;
			for (RunJWorkticketContent m : list) { 
				if (m.getId() != null) { 
					update(m);
				} else {
					m.setId(id + (i++));
					save(m);
				} 
			}
		}
		if(delIds != null&& !delIds.trim().equals(""))
		{
			String sql = "delete from run_j_workticket_content c where c.id in("+delIds+")";  
			bll.exeNativeSQL(sql);
		}
		entityManager.flush();
		String Content = getWorkticketContent(workticketNo);
		String sql = "update run_j_worktickets  t set t.workticket_content=? where t.workticket_no=?";
		bll.exeNativeSQL(sql, new Object[]{Content,workticketNo});
	}

	public void delete(Long id) {
		try {
			RunJWorkticketContent entity=this.findById(id);
			if(entity!=null)
			{
				entity.setIsUse("N");
				this.update(entity);
			}
		}
		catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteByNO(String workticketNO)
	{
		String sql = 
			"select count(1) from RUN_J_WORKTICKET_CONTENT t\n" +
			"where t.workticket_no = '" + workticketNO + "'";
		try{
			if(Integer.parseInt(bll.getSingal(sql).toString()) != 0)
			{
				sql = 
					"update RUN_J_WORKTICKET_CONTENT t set t.is_use = 'N'\n" +
					"where t.workticket_no = '" + workticketNO + "'";
				bll.exeNativeSQL(sql);
			}
		}
		catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}
	
	public void deleteMulti(String Ids)
	{
		try{
			String sql=
				"update RUN_J_WORKTICKET_CONTENT t\n" +
				"set t.is_use='N'\n" + 
				"where t.ID in("+Ids+")";
	        bll.exeNativeSQL(sql);
		}
		catch(RuntimeException re){
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}
	
	public RunJWorkticketContent update(RunJWorkticketContent entity) {
		try {
				entity.setCreateDate(new java.util.Date()); 
				RunJWorkticketContent result = entityManager.merge(entity);
				return result;
		} catch (RuntimeException re) {
				LogUtil.log("update failed", Level.SEVERE, re);
				throw re;
		}
	}

	public RunJWorkticketContent findById(Long id) {
		try {
			RunJWorkticketContent instance = entityManager.find(
					RunJWorkticketContent.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,String workticketNO,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = null;
			String sqlCount = "select count(1) from RUN_J_WORKTICKET_CONTENT t where t.is_use = 'Y' \n"
					+ "and t.enterprise_code =?\n" + "and t.workticket_no =?";
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
					new Object[] { enterpriseCode, workticketNO }).toString());
			if (totalCount.longValue() > 0) {
				result = new PageObject();
				result.setTotalCount(totalCount); 
				String sql = "select t.* from RUN_J_WORKTICKET_CONTENT t where t.is_use = 'Y' \n"
						+ "and t.enterprise_code =?\n"
						+ "and t.workticket_no =?"; 
				List<RunJWorkticketContent> list = bll.queryByNativeSQL(sql,
						new Object[] { enterpriseCode, workticketNO },
						RunJWorkticketContent.class, rowStartIdxAndCount); 
				result.setList(list);
			}
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getWorkticketContent(String workticketNo)
	{
		//modify by fyyang 090401 去掉前面的编号
		String content = "";
//		String sql = 
//			"SELECT t.isreturn,\n" +
//			"       t.front_key_desc || t.location_name || t.equ_name ||\n" + 
//			"       t.front_key_desc || t.equ_name ||\n" + 			
//			"       decode(t.attribute_code,\n" + 
//			"              'temp',\n" + 
//			"              '',\n" + 
//			"              '（' || t.attribute_code || '）') || t.back_key_desc ||\n" + 
//			"       t.worktype_name || t.flag_desc ||\n" + 
//			"       decode(t.isreturn, 'N', null, chr(13) || chr(10)) content\n" + 
//			"  FROM run_j_workticket_content t\n" + 
//			" where t.is_use = 'Y'\n" + 
//			"   and t.workticket_no = '"+workticketNo+"'\n" + 
//			" order by t.line asc";
		
		//modify by lwqi 090523 工作内容从主表取
		String sql =
		"select t.workticket_content from run_j_worktickets t \n"+
		" where t.is_use = 'Y'\n" + 
		"   and t.workticket_no = '"+workticketNo+"'\n" ;


		//List list = bll.queryByNativeSQL(sql); 
		if(bll.getSingal(sql) != null)
		{
		content=bll.getSingal(sql).toString();
		}
//		if(list!=null && list.size()>0)
//		{
//			content = "";
//			Iterator it = list.iterator();
//			//int i=2;
//			while (it.hasNext()) {
//				Object[] data = (Object[]) it.next();
//				//String line ="";
////				if((data[0]==null ||data[0].toString().equals("") ||data[0].toString().equals("Y"))&&it.hasNext())
////				{	
////					line = "("+i+") ";
////					i++;
////				}
//				content += data[1].toString();
//			}
//		}
		return content;
	}
	
	//回写主表工作内容
	public void updateWorkticketsContent(String workticketNo)
	{
		String content=this.getWorkticketContent(workticketNo);
		String sql=
			"update run_j_worktickets t\n" +
			"set t.workticket_content='"+content+"'\n" + 
			"where t.workticket_no='"+workticketNo+"'";
		bll.exeNativeSQL(sql);
	}
}