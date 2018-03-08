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
import power.ejb.productiontec.relayProtection.form.ProtectDevMotionForm;
import power.ejb.productiontec.relayProtection.form.ProtectEquForm;

/**
 *继电保护装置动作情况
 *@author liuyi 090717
 */
@Stateless
public class PtJdbhJBhzzdzqkFacade implements PtJdbhJBhzzdzqkFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条继电保护装置动作情况记录
	 */
	public PtJdbhJBhzzdzqk save(PtJdbhJBhzzdzqk entity) {
		LogUtil.log("saving PtJdbhJBhzzdzqk instance", Level.INFO, null);
		try {
			entity.setBhzzdzId(bll.getMaxId("PT_JDBH_J_BHZZDZQK", "BHZZDZ_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}		
	}

	/**
	 * 删除一条继电保护装置动作情况记录
	 */
	public void delete(PtJdbhJBhzzdzqk entity) {
		LogUtil.log("deleting PtJdbhJBhzzdzqk instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJdbhJBhzzdzqk.class, entity
					.getBhzzdzId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}


	/**
	 * 删除一条或多条继电保护装置动作情况记录
	 */
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_JDBH_J_BHZZDZQK a\n"
	    + " where a.BHZZDZ_ID in (" + ids
	   + ")\n" ;
       bll.exeNativeSQL(sql);
	}
	
	/**
	 * 更新一条继电保护装置动作情况记录
	 */
	public PtJdbhJBhzzdzqk update(PtJdbhJBhzzdzqk entity) {
		LogUtil.log("updating PtJdbhJBhzzdzqk instance", Level.INFO, null);
		try {
			PtJdbhJBhzzdzqk result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条继电保护装置动作情况记录
	 */
	public PtJdbhJBhzzdzqk findById(Long id) {
		LogUtil.log("finding PtJdbhJBhzzdzqk instance with id: " + id,
				Level.INFO, null);
		try {
			PtJdbhJBhzzdzqk instance = entityManager.find(
					PtJdbhJBhzzdzqk.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findAll(String name,String fromTime,String toTime, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.BHZZDZ_ID,\n"
			+ "       a.DEVICE_ID,\n"
			+ "       a.ACT_DATE, \n"
			+ "       a.CHARGE_DEP, \n"
			+ "       a.ACT_APPAISE, \n"
			+ "       a.ACT_NUM, \n"
			+ "       a.WAVE_NUMBER, \n"
			+ "       a.WAVE_GOOD_NUMBER, \n"
			+ "       a.PROTECT_ACT, \n"
			+ "       a.ERROR_ANALYZE, \n"
			+ "       a.FILL_BY, \n"
			+ "       a.FILL_DATE, \n"
			+ "       a.ENTERPRISE_CODE, \n"
			+ "		getequnamebycode(b.equ_code), \n"		
			+ "       to_char(a.ACT_DATE,'yyyy-MM-dd'), \n"			
			+ "       getdeptname(a.CHARGE_DEP), \n"
			+ "       getworkername(a.FILL_BY), \n"
			+ "       to_char(a.FILL_DATE,'yyyy-MM-dd') \n"	
			+ "  from PT_JDBH_J_BHZZDZQK a,PT_JDBH_J_BHZZTZ b  \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n"
			+ " and b.enterprise_code ='" + enterpriseCode + "' \n"
			+ " and a.DEVICE_ID =b.DEVICE_ID \n";
		
		
		String sqlCount = "select count(a.BHZZDZ_ID)\n"
			+ "  from PT_JDBH_J_BHZZDZQK a,PT_JDBH_J_BHZZTZ b \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n"
			+ " and b.enterprise_code ='" + enterpriseCode + "' \n"
			+ " and a.DEVICE_ID =b.DEVICE_ID \n";
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and GETEQUNAMEBYCODE(b.equ_code) like '%" + name + "%' \n";
			sqlCount = sqlCount + " and GETEQUNAMEBYCODE(b.equ_code) like '%" + name + "%' \n";
		}
		if(fromTime != null && (!fromTime.equals("")))
		{
			sql = sql + " and to_char(a.ACT_DATE,'yyyy-MM-dd') >= '" +fromTime + "' ";
			sqlCount = sqlCount + " and to_char(a.ACT_DATE,'yyyy-MM-dd') >= '" +fromTime + "' ";
		}
		if(toTime != null && (!toTime.equals("")))
		{
			sql = sql + " and to_char(a.ACT_DATE,'yyyy-MM-dd') <= '" + toTime + "' ";
			sqlCount = sqlCount + " and to_char(a.ACT_DATE,'yyyy-MM-dd') <= '" + toTime + "' ";
		}
		sql = sql + " order by a.BHZZDZ_ID \n";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJdbhJBhzzdzqk pjj = new PtJdbhJBhzzdzqk();
				ProtectDevMotionForm form = new ProtectDevMotionForm();
				Object []data = (Object[])it.next();
				pjj.setBhzzdzId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					pjj.setDeviceId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					try {
						pjj.setActDate(sbf.parse(data[2].toString()));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				if(data[3] != null)
					pjj.setChargeDep(data[3].toString());
				if(data[4] != null)
					pjj.setActAppaise(data[4].toString());
				if(data[5] != null)
					pjj.setActNum(Long.parseLong(data[5].toString()));
				if(data[6] != null)
					pjj.setWaveNumber(Long.parseLong(data[6].toString()));
				if(data[7] != null)
					pjj.setWaveGoodNumber(Long.parseLong(data[7].toString()));
				if(data[8] != null)
					pjj.setProtectAct(data[8].toString());
				if(data[9] != null)
					pjj.setErrorAnalyze(data[9].toString());
				if(data[10] != null)
					pjj.setFillBy(data[10].toString());
				if(data[11] != null)
					try {
						pjj.setFillDate(sbf.parse(data[11].toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				if(data[12] != null)
					pjj.setEnterpriseCode(data[12].toString());
				if(data[13] != null)
					form.setDeviceName(data[13].toString());
				if(data[14] != null)
					form.setActDate(data[14].toString());
				if(data[15] != null)
					form.setChargeDeptName(data[15].toString());
				if(data[16] != null)
					form.setFillName(data[16].toString());
				if(data[17] != null)
					form.setFillDate(data[17].toString());
					
				form.setPjj(pjj);
				arrlist.add(form);
			 }
		  }
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}


}