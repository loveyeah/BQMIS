package power.ejb.productiontec.report;

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

import power.ejb.productiontec.report.ptJDependReport;
import power.ejb.productiontec.technologySupervise.PtJJdhdjl;

@Stateless
public class ptJDependReportFacade implements ptJDependReportFacadeRemote {
	
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	/**
	 * 删除可靠性报表 ghzhou 091102
	 */
	public void delete(ptJDependReport entity){
		try {
			entity = entityManager.getReference(ptJDependReport.class, entity.getDependId());
			entityManager.remove(entity);
		} catch  (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * update可靠性报表 ghzhou 091102
	 */
	public ptJDependReport update(ptJDependReport entity){
		try {
			ptJDependReport result = entityManager.merge(entity);
			return result;
		} catch(RuntimeException re){
			throw re;
		}
	}
	/**
	 * 根据ID查找可靠性报表 ghzhou 091102
	 */
	public ptJDependReport findById(String Id){
		Long dependId = Long.parseLong(Id);
		try{
			ptJDependReport instance = entityManager.find(ptJDependReport.class, dependId);
			return instance;
		}catch (RuntimeException re) {
			throw re;
		}
	}
	public void save(ptJDependReport entity){
		try {
			entity.setDependId(bll.getMaxId("pt_j_depend_report", "depend_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;}
	}
	
	/**
	 * writed by ghzhou 09-10-27
	 * @param dependType 机组类型
	 * @param dependYear 年
	 * @return PageObject
	 */
	public PageObject findAllOnlyYear(String dependType,String dependYear,int... rowStartIdxAndCount){
		PageObject pg = new PageObject();
		String temp = "where \n";
		if(dependType.trim().length()!=0)
		{
			temp = "where t.depend_type like '%" + dependType + "%' and\n" ;
		}
		
		String sql = 
			"select t.depend_id,t.depend_name,t.depend_path,t.depend_date,t.depend_type,\n" +
			"t.depend_year,t.depend_time_type,depend_entry,getworkername(t.depend_entry) depend_entryName,t.depend_memo,t.depend_entry depend_EntryCode\n" + 
			"from pt_j_depend_report t\n" + 
			temp + 
			"t.depend_year = '" + dependYear + "'";
		String sqlCount = 
			"select count(*) from pt_j_depend_report t\n" +
			temp + 
			"t.depend_year = '" + dependYear + "'";
		if(dependType.equals(null)){
			sql+=sql+"";
		}
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if(list != null && list.size() > 0){
			while(it.hasNext())
			{
				ptJDependReport modelDependReport = new ptJDependReport();
				Object[] data = (Object[]) it.next();
				modelDependReport.setDependId(Integer.parseInt(data[0].toString()));
				if(data[1] != null)
				{
					modelDependReport.setDependName(data[1].toString());
				}
				if(data[2] != null)
				{
					modelDependReport.setDependPath(data[2].toString());
				}
				if(data[3] != null){
					modelDependReport.setDependDate((Date)data[3]);
				}
				if(data[4] != null){
					modelDependReport.setDependType(data[4].toString());
				}
				if(data[5] != null){
					modelDependReport.setDependYear(data[5].toString());
				}
				if(data[6] != null){
					modelDependReport.setDependTimeType(data[6].toString());
				}
				if(data[7] != null){
					modelDependReport.setDependEntry(data[7].toString());
				}
				if(data[8] != null){
					modelDependReport.setDependEntryName(data[8].toString());
				}
				if(data[9] != null){
					modelDependReport.setDependMemo(data[9].toString());
				}
				arrlist.add(modelDependReport);
			}	
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"pt_j_depend_report a\n"
	    + " where a.depend_id in (" + ids + ")\n" ;
        bll.exeNativeSQL(sql);
		
	}

}
