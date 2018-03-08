package power.ejb.workticket.business;

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
import power.ejb.workticket.form.WorkticketDhMeasureInfo;

/**
 * Facade for entity RunJWorkticketMeasureData.
 * 
 * @see power.ejb.workticket.business.RunJWorkticketMeasureData
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJWorkticketMeasureDataFacade implements
		RunJWorkticketMeasureDataFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunJWorkticketMeasureData save(RunJWorkticketMeasureData entity) {
		LogUtil.log("saving RunJWorkticketMeasureData instance", Level.INFO,
				null);
		try {
			entity.setMeasureDataId(bll.getMaxId("RUN_J_WORKTICKET_MEASURE_DATA", "measure_data_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void deleteMulti(String ids) {
	    String sql=
	    	"update RUN_J_WORKTICKET_MEASURE_DATA t\n" +
	    	"set t.is_use='N'\n" + 
	    	"where t.measure_data_id in ("+ids+")\n" + 
	    	"and t.is_use='Y'";
        bll.exeNativeSQL(sql);
	}

	
	public RunJWorkticketMeasureData update(RunJWorkticketMeasureData entity) {
		LogUtil.log("updating RunJWorkticketMeasureData instance", Level.INFO,
				null);
		try {
			RunJWorkticketMeasureData result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJWorkticketMeasureData findById(Long id) {
		LogUtil.log(
				"finding RunJWorkticketMeasureData instance with id: " + id,
				Level.INFO, null);
		try {
			RunJWorkticketMeasureData instance = entityManager.find(
					RunJWorkticketMeasureData.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findAllByWorkticketNo(String workticketNo,final int... rowStartIdxAndCount)
	{
		PageObject result = new PageObject(); 
		String sql=
			"select t.measure_data_id,\n" +
			"       t.workticket_no,\n" + 
			"       t.measure_data,\n" + 
			"       t.measure_man,\n" + 
			"       nvl(GETWORKERNAME(t.measure_man),t.measure_man) measuremanName,\n" + 
			"      to_char(t.measure_date, 'yyyy-MM-dd hh24:mi:ss') \n" + 
			"  from RUN_J_WORKTICKET_MEASURE_DATA t\n" + 
			" where t.workticket_no = '"+workticketNo+"'\n" + 
			" and t.is_use='Y'";
		String sqlCount=
			"select count(1)\n" +
			"  from RUN_J_WORKTICKET_MEASURE_DATA t\n" + 
			" where t.workticket_no = ''\n" + 
			" and t.is_use='Y'";

		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		List measureList=new ArrayList();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			WorkticketDhMeasureInfo model=new WorkticketDhMeasureInfo();
			if(data[0]!=null)
			{
				model.setMeasureDataId(data[0].toString());
			}
			if(data[1]!=null)
			{
				model.setWorkticketNo(data[1].toString());
			}
			if(data[2]!=null)
			{
				model.setMeasureData(data[2].toString());
			}
			if(data[3]!=null)
			{
				model.setMeasureMan(data[3].toString());
			}
			if(data[4]!=null)
			{
				model.setMeasureManName(data[4].toString());
			}
			if(data[5]!=null)
			{
				model.setMeasureDate(data[5].toString());
			}
			measureList.add(model);
		}
		result.setList(measureList);
		result.setTotalCount(totalCount);
		return result;
	}

	

}