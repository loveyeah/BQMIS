package power.ejb.productiontec.relayProtection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import power.ejb.productiontec.chemistry.form.ChemistryReportForm;
import power.ejb.productiontec.relayProtection.form.ExperiReportDetailsForm;
import power.ejb.productiontec.relayProtection.form.ExperimentReportForm;

/**
 * 继电试验报告记录
 * @author liuyi 090720
 */
@Stateless
public class PtJdbhJSybgjlFacade implements PtJdbhJSybgjlFacadeRemote {
	// property constants
	public static final String JDSYBG_ID = "jdsybgId";
	public static final String SYD_ID = "sydId";
	public static final String REGULATOR_ID = "regulatorId";
	public static final String RESULT = "result";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条继电试验报告记录
	 */
	public PtJdbhJSybgjl save(PtJdbhJSybgjl entity) {
		LogUtil.log("saving PtJdbhJSybgjl instance", Level.INFO, null);
		try {
			entity.setJdsybgjgId(bll.getMaxId("PT_JDBH_J_SYBGJL", "JDSYBGJG_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}

	}

	/**
	 * 删除一条继电试验报告记录
	 */
	public void delete(PtJdbhJSybgjl entity) {
		LogUtil.log("deleting PtJdbhJSybgjl instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJdbhJSybgjl.class, entity
					.getJdsybgjgId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条继电试验报告记录
	 */
	public PtJdbhJSybgjl update(PtJdbhJSybgjl entity) {
		LogUtil.log("updating PtJdbhJSybgjl instance", Level.INFO, null);
		try {
			PtJdbhJSybgjl result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条继电试验报告记录
	 */
	public PtJdbhJSybgjl findById(Long id) {
		LogUtil.log("finding PtJdbhJSybgjl instance with id: " + id,
				Level.INFO, null);
		try {
			PtJdbhJSybgjl instance = entityManager
					.find(PtJdbhJSybgjl.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	

	/**
	 * 查询继电试验报告记录列表
	 * @param jdsybgId 试验报告编号
	 * @param enterpriseCode 企业编号
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllReprotDetails(String jdsybgId,String enterpriseCode,final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "";
		String addSql = "";
		String addSqlCount = "";
		String sqlCount=
			"select count(*)\n" +
			"from PT_JDBH_J_SYBGJL a,PT_JDBH_J_SYBG b,PT_JDBH_C_SYXMWH f,PT_JDBH_C_SYDWH g,PT_J_YQYBTZ h \n" + 
			"where a.jdsybg_id = b.jdsybg_id(+)  \n" 
			
			+ " and  g.syxm_id = f.syxm_id(+) \n" 
			+ " and a.syd_id = g.syd_id(+) \n" 
			+ "and a.enterprise_code='"+enterpriseCode+"'\n"
			+ "and f.enterprise_code(+)='"+enterpriseCode+"'\n"  
			+ "and g.enterprise_code(+)='"+enterpriseCode+"'\n" 
			+ "and h.enterprise_code(+)='"+enterpriseCode+"'\n"
			+ " and a.regulator_id = h.regulator_id(+) \n";
		if(jdsybgId != null && (!jdsybgId.equals("")))
		{
			sqlCount = sqlCount + " and a.jdsybg_id=" +jdsybgId+ " \n";
		}
         Long totalCount= Long.parseLong(bll.getSingal(sqlCount).toString());
         if(totalCount>0)
         {
        	 
        	 sql=
        		 "select distinct a.jdsybgjg_id,a.jdsybg_id,a.syd_id,a.regulator_id,a.result,a.enterprise_code, \n" 
        		 + " b.jdsybg_name,f.syxm_id,f.syxm_name,g.syd_name, \n"
        		 + " h.names, \n"
     			 + " g.minimum,g.maximum \n"
        		 +"from PT_JDBH_J_SYBGJL a,PT_JDBH_J_SYBG b,PT_JDBH_C_SYXMWH f,PT_JDBH_C_SYDWH g,PT_J_YQYBTZ h \n" 
        		 + " where a.jdsybg_id = b.jdsybg_id(+)  \n" 
     			+ " and a.syd_id = g.syd_id(+) \n" 
     			+ " and  g.syxm_id = f.syxm_id \n" 
     			+ "and a.enterprise_code='"+enterpriseCode+"'\n"
     			+ "and b.enterprise_code(+)='"+enterpriseCode+"'\n" 
     			+ "and f.enterprise_code(+)='"+enterpriseCode+"'\n"  
     			+ "and g.enterprise_code(+)='"+enterpriseCode+"'\n"
     			+ "and h.enterprise_code(+)='"+enterpriseCode+"'\n"
    			+ " and a.regulator_id = h.regulator_id(+) \n";
     		if(jdsybgId != null && (!jdsybgId.equals("")))
     		{
     			sql = sql + " and a.jdsybg_id=" +jdsybgId+ " \n";
     		}
     		sql = sql + " order by f.syxm_id,g.syd_name ";
            List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
            List objList=new ArrayList();
            Iterator it= list.iterator();
            while(it.hasNext())
            {
            	Object[] data=(Object [])it.next();
            	ExperiReportDetailsForm model=new ExperiReportDetailsForm();
            	PtJdbhJSybgjl pjjs=new PtJdbhJSybgjl();
            	if(data[0]!=null)
            	{
            		pjjs.setJdsybgjgId(Long.parseLong(data[0].toString()));
            	}
            	if(data[1]!=null)
            	{
            		pjjs.setJdsybgId(Long.parseLong(data[1].toString()));
            	}
            	if(data[2]!=null)
            	{
            		pjjs.setSydId(Long.parseLong(data[2].toString()));
            	}
            	if(data[3]!=null)
            	{
            		pjjs.setRegulatorId(Long.parseLong(data[3].toString()));
            	}
            	if(data[4]!=null)
            	{
            		pjjs.setResult(Double.parseDouble(data[4].toString()));
            	}
            	if(data[5]!=null)
            	{
            		pjjs.setEnterpriseCode(data[5].toString());
            	}
            	if(data[6]!=null)
            	{
            		model.setJdsybgName(data[6].toString());
            	}
            	if(data[7]!=null)
            	{
            		model.setSyxmId(data[7].toString());
            	}
            	if(data[8]!=null)
            	{
            		model.setSyxmName(data[8].toString());
            	}
            	if(data[9]!=null)
            	{
            		model.setSydName(data[9].toString());
            	}
            	if(data[10]!=null)
            	{
            		model.setRegulatorName(data[10].toString());
            	}
            	if(data[11]!=null)
            	{
            		model.setMinimum(data[11].toString());
            	}
            	if(data[12]!=null)
            	{
            		model.setMaximum(data[12].toString());
            	}
            	
            	model.setPjjs(pjjs);
            	objList.add(model);
            }
//            addSql = "select b.jdsybg_id,g.syd_id,b.jdsybg_name,f.syxm_id, \n"
//    		 	+ " f.syxm_name,g.syd_name,g.minimum,g.maximum \n"
//    		 	+ " from PT_JDBH_J_SYBG b,PT_JDBH_J_BHZZTZ c,PT_JDBH_C_SYLBWH d,PT_JDBH_C_LBYXMDY e,PT_JDBH_C_SYXMWH f,PT_JDBH_C_SYDWH g \n"
//    		 	+ " where b.device_id = c.device_id and b.sylb_id = d.sylb_id and d.sylb_id = e.sylb_id \n"
//    		 	+ " and e.syxm_id = f.syxm_id and f.syxm_id = g.syxm_id \n"
//    		 	+ "and b.enterprise_code='"+enterpriseCode+"'\n" 
//     			+ "and c.enterprise_code='"+enterpriseCode+"'\n" 
//     			+ "and d.enterprise_code='"+enterpriseCode+"'\n" 
//     			+ "and e.enterprise_code='"+enterpriseCode+"'\n" 
//     			+ "and f.enterprise_code='"+enterpriseCode+"'\n"  
//     			+ "and g.enterprise_code='"+enterpriseCode+"'\n"
//     			+ "and a.jdsybg_id = b.jdsybg_id \n"
//            if(jdsybgId != null && (!jdsybgId.equals("")))
//            {
//            	addSql = addSql + " and a.jdsybg_id=" +jdsybgId+ " \n";
//            }
//              addSql = addSql + " and (g.syd_id,f.syxm_id) not in  \n"
//              	+ "(select gg.syd_id,ff.syxm_id \n"
//              	+ " from PT_JDBH_J_SYBGJL aa,PT_JDBH_J_SYBG bb,PT_JDBH_C_SYXMWH ff,PT_JDBH_C_SYDWH gg,PT_J_YQYBTZ hh \n"
//              	+ " where aa.jdsybg_id = bb.jdsybg_id(+) \n"
//              	+ " and aa.syd_id = gg.syd_id(+) \n"
//              	+ "  and  gg.syxm_id = ff.syxm_id(+) \n"
//              	+ "and aa.enterprise_code='"+enterpriseCode+"'\n"
//     			+ "and bb.enterprise_code='"+enterpriseCode+"'\n" 
//     			+ "and ff.enterprise_code='"+enterpriseCode+"'\n"  
//     			+ "and gg.enterprise_code='"+enterpriseCode+"'\n"
//     			+ "and hh.enterprise_code='"+enterpriseCode+"'\n"
//    			+ " and aa.regulator_id = hh.regulator_id(+) \n";
//              if(jdsybgId != null && (!jdsybgId.equals("")))
//       		{
//       			addSql = addSql + " and a.jdsybg_id=" +jdsybgId+ " \n";
//       		}
//            addSql = addSql + " order by a.JDSYBG_ID ";
//            addSqlCount = " select count(*) from (addSql)";
//            List addList=bll.queryByNativeSQL(addSql, rowStartIdxAndCount);
//            Iterator addIt= addList.iterator();
//            while(addIt.hasNext())
//            {
//            	Object[] data=(Object [])addIt.next();
//            	ExperiReportDetailsForm addModel=new ExperiReportDetailsForm();
//            	PtJdbhJSybgjl addPjjs=new PtJdbhJSybgjl();
//            	if(data[0]!=null)
//            	{
//            		addPjjs.setJdsybgId(Long.parseLong(data[0].toString()));
//            	}
//            	if(data[1]!=null)
//            	{
//            		addPjjs.setSydId(Long.parseLong(data[1].toString()));
//            	}
//            	if(data[2]!=null)
//            	{
//            		addModel.setJdsybgName(data[2].toString());
//            	}
//            	if(data[3]!=null)
//            	{
//            		addModel.setSyxmId(data[3].toString());
//            	}
//            	if(data[4]!=null)
//            	{
//            		addModel.setSyxmName(data[4].toString());
//            	}
//            	if(data[5]!=null)
//            	{
//            		addModel.setSydName(data[5].toString());
//            	}
//            	if(data[6]!=null)
//            	{
//            		addModel.setMinimum(data[6].toString());
//            	}
//            	if(data[7]!=null)
//            	{
//            		addModel.setMaximum(data[7].toString());
//            	}
//            	
//            	addModel.setPjjs(addPjjs);
//            	objList.add(addModel);
//            }
//            Long addCount= Long.parseLong(bll.getSingal(addSqlCount).toString());
//            pg.setTotalCount(totalCount + addCount);
            pg.setTotalCount(totalCount);
            pg.setList(objList);
         }
         else
         {
        	 sql = "select b.jdsybg_id,g.syd_id,b.jdsybg_name,f.syxm_id, \n"
        		 	+ " f.syxm_name,g.syd_name,g.minimum,g.maximum \n"
        		 	+ " from PT_JDBH_J_SYBG b,PT_JDBH_J_BHZZTZ c,PT_JDBH_C_SYLBWH d,PT_JDBH_C_LBYXMDY e,PT_JDBH_C_SYXMWH f,PT_JDBH_C_SYDWH g \n"
        		 	+ " where b.device_id = c.device_id and b.sylb_id = d.sylb_id and d.sylb_id = e.sylb_id \n"
        		 	+ " and e.syxm_id = f.syxm_id and f.syxm_id = g.syxm_id \n"
        		 	+ "and b.enterprise_code='"+enterpriseCode+"'\n" 
         			+ "and c.enterprise_code='"+enterpriseCode+"'\n" 
         			+ "and d.enterprise_code='"+enterpriseCode+"'\n" 
         			+ "and e.enterprise_code='"+enterpriseCode+"'\n" 
         			+ "and f.enterprise_code='"+enterpriseCode+"'\n"  
         			+ "and g.enterprise_code='"+enterpriseCode+"'\n";
        	 if(jdsybgId != null && (!jdsybgId.equals("")))
      		{
      			sql = sql + " and b.jdsybg_id=" +jdsybgId+ " \n";
      		}
      		sql = sql + " order by f.syxm_id,g.syd_name ";
      		sqlCount = " select count(*) \n"
      			+ " from PT_JDBH_J_SYBG b,PT_JDBH_J_BHZZTZ c,PT_JDBH_C_SYLBWH d,PT_JDBH_C_LBYXMDY e,PT_JDBH_C_SYXMWH f,PT_JDBH_C_SYDWH g \n"
    		 	+ " where b.device_id = c.device_id and b.sylb_id = d.sylb_id and d.sylb_id = e.sylb_id \n"
    		 	+ " and e.syxm_id = f.syxm_id and f.syxm_id = g.syxm_id \n"
    		 	+ "and b.enterprise_code='"+enterpriseCode+"'\n" 
     			+ "and c.enterprise_code='"+enterpriseCode+"'\n" 
     			+ "and d.enterprise_code='"+enterpriseCode+"'\n" 
     			+ "and e.enterprise_code='"+enterpriseCode+"'\n" 
     			+ "and f.enterprise_code='"+enterpriseCode+"'\n"  
     			+ "and g.enterprise_code='"+enterpriseCode+"'\n";
      		if(jdsybgId != null && (!jdsybgId.equals("")))
      		{
      			sqlCount = sqlCount + " and b.jdsybg_id=" +jdsybgId+ " \n";
      		}
      		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
            List objList=new ArrayList();
            Iterator it= list.iterator();
            while(it.hasNext())
            {
            	Object[] data=(Object [])it.next();
            	ExperiReportDetailsForm model=new ExperiReportDetailsForm();
            	PtJdbhJSybgjl pjjs=new PtJdbhJSybgjl();
            	if(data[0]!=null)
            	{
            		pjjs.setJdsybgId(Long.parseLong(data[0].toString()));
            	}
            	if(data[1]!=null)
            	{
            		pjjs.setSydId(Long.parseLong(data[1].toString()));
            	}
            	if(data[2]!=null)
            	{
            		model.setJdsybgName(data[2].toString());
            	}
            	if(data[3]!=null)
            	{
            		model.setSyxmId(data[3].toString());
            	}
            	if(data[4]!=null)
            	{
            		model.setSyxmName(data[4].toString());
            	}
            	if(data[5]!=null)
            	{
            		model.setSydName(data[5].toString());
            	}
            	if(data[6]!=null)
            	{
            		model.setMinimum(data[6].toString());
            	}
            	if(data[7]!=null)
            	{
            		model.setMaximum(data[7].toString());
            	}
            	
            	model.setPjjs(pjjs);
            	objList.add(model);
            }
            Long total= Long.parseLong(bll.getSingal(sqlCount).toString());
            pg.setTotalCount(total);
            pg.setList(objList);
        }
         return pg;
	}

	/**
	 * 批量修改继电保护试验记录数据
	 */
	public void modifyRecords(List<PtJdbhJSybgjl> list) {
		if(list!=null)
		{
			for(PtJdbhJSybgjl m:list)
			{
				if(m.getJdsybgjgId() == null || m.getJdsybgjgId().toString().equals(""))
				{
					this.save(m);
					entityManager.flush();
				}
				else 
				{
					this.update(m);
				}
			}
		}
	}
}