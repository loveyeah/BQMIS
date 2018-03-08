package power.ejb.run.securityproduction;

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
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.run.securityproduction.form.SpJSpecialoperatorsInfo;


/**
 * Facade for entity SpJSpecialoperators.
 * 
 * @see power.ejb.run.securityproduction.SpJSpecialoperators
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSpecialoperatorsFacade implements
		SpJSpecialoperatorsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	private String workerName;

	
	public SpJSpecialoperators save(SpJSpecialoperators entity) {
		LogUtil.log("saving SpJSpecialoperators instance", Level.INFO, null);
		try {
			entity.setOfferId(bll.getMaxId("sp_j_specialoperators", "offer_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	

	
	public SpJSpecialoperators update(SpJSpecialoperators entity) {
		LogUtil.log("updating SpJSpecialoperators instance", Level.INFO, null);
		try {
			SpJSpecialoperators result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteMulti(String ids)
	{
		String sql = "delete from  "+
				"sp_j_specialoperators a\n"
			 + " where a.offer_id in (" + ids
			+ ")\n" ;
	bll.exeNativeSQL(sql);
	}

	public SpJSpecialoperators findById(Long id) {
		LogUtil.log("finding SpJSpecialoperators instance with id: " + id,
				Level.INFO, null);
		try {
			SpJSpecialoperators instance = entityManager.find(
					SpJSpecialoperators.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String workName,String enterpriseCode,final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.offer_id,\n"
			+ "       a.worker_code,\n"
			+ "       getworkername(a.worker_code),\n"
			+ "       a.project_operation,\n"
			+ "       a.post_year,\n"
			+ "       a.offer_name,\n"
			+ "       a.offer_code,\n"
			+ "       to_char(a.offer_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
			+ "       to_char(a.offer_start_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
			+ "       to_char(a.offer_end_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
			+ "       to_char(a.medical_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
			+ "       a.medical_result,\n"
			+ "       a.memo,\n"
			+ "       a.enterprise_code, \n"
			+ "       to_char(a.medical_date, 'yyyy-MM-dd'),\n"
			+ "       to_char(a.offer_date, 'yyyy-MM-dd'),\n"
			+ "       to_char(a.offer_start_date, 'yyyy-MM-dd'),\n"
			+ "       to_char(a.offer_end_date, 'yyyy-MM-dd') \n"
			+ "  from sp_j_specialoperators a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		String sqlCount = "select count(a.offer_id)\n"
			+ "  from sp_j_specialoperators a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		if(workName !=null && (!workName.equals("")))
		{
			sql = sql + " and getworkername(a.worker_code) like '%" + workName + "%' \n";
			sqlCount = sqlCount + " and getworkername(a.worker_code) like '%" + workName + "%' \n";
		}
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				SpJSpecialoperators spj = new SpJSpecialoperators();
				SpJSpecialoperatorsInfo info = new SpJSpecialoperatorsInfo();
				Object []data = (Object[])it.next();
				spj.setOfferId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					spj.setWorkerCode(data[1].toString());
				if(data[2] != null)
					info.setWorkerName(data[2].toString());
				if(data[3] != null)
					spj.setProjectOperation(data[3].toString());
				if(data[4] != null)
					spj.setPostYear(Double.parseDouble(data[4].toString()));
				if(data[5] != null)
					spj.setOfferName(data[5].toString());
				if(data[6] != null)
					spj.setOfferCode(data[6].toString());
				if(data[7] != null)
					try {
						spj.setOfferDate(sdf.parse(data[7].toString()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if(data[8] != null)
					try {
						spj.setOfferStartDate(sdf.parse(data[8].toString()));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				if(data[9] != null)
					try {
						spj.setOfferEndDate(sdf.parse(data[9].toString()));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				if(data[10] != null)
					try {
						spj.setMedicalDate(sdf.parse(data[10].toString()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if(data[11] != null)
					spj.setMedicalResult(data[11].toString());
				if(data[12] != null)
					spj.setMemo(data[12].toString());
				if(data[14] != null)
					info.setMedicalDate(data[14].toString());
				if(data[15] != null)
					info.setOfferDate(data[15].toString());
				if(data[16] != null)
					info.setOfferStartDate(data[16].toString());
				if(data[17] != null)
					info.setOfferEndDate(data[17].toString());
				info.setSpj(spj);
				arrlist.add(info);
			 }
		  }
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
	}





	public String getWorkerName() {
		return workerName;
	}





	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

}