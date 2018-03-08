package power.ejb.productiontec.chemistry;

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
import power.ejb.productiontec.chemistry.form.ThermalEquCheckForm;

/**
 * Facade for entity PtHxjdJRlsbjcqk.
 * 
 * @see power.ejb.productiontec.chemistry.PtHxjdJRlsbjcqk
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtHxjdJRlsbjcqkFacade implements PtHxjdJRlsbjcqkFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public PtHxjdJRlsbjcqk save(PtHxjdJRlsbjcqk entity) {
		LogUtil.log("saving PtHxjdJRlsbjcqk instance", Level.INFO, null);
		try {
			entity.setRlsbjcId(bll.getMaxId("PT_HXJD_J_RLSBJCQK", "rlsbjc_id"));
			entity.setFillDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids) {
		String sql1 = "delete PT_HXJD_J_RLSBJCQK a where a.rlsbjc_id in(" + ids + ")";
		bll.exeNativeSQL(sql1);
		
		String sql2 = "delete PT_HXJD_J_RLSBJCQK_DETAIL b where b.rlsbjc_id in(" + ids + ")";
		bll.exeNativeSQL(sql2);
	}

	public PtHxjdJRlsbjcqk update(PtHxjdJRlsbjcqk entity) {
		LogUtil.log("updating PtHxjdJRlsbjcqk instance", Level.INFO, null);
		try {
			PtHxjdJRlsbjcqk result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtHxjdJRlsbjcqk findById(Long id) {
		LogUtil.log("finding PtHxjdJRlsbjcqk instance with id: " + id,
				Level.INFO, null);
		try {
			PtHxjdJRlsbjcqk instance = entityManager.find(
					PtHxjdJRlsbjcqk.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findThermalEquCheckList(String enterpriseCode,String deviceCode,String sDate,String eDate,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql = "select a.rlsbjc_id,\n" +
			"       a.device_code,\n" + 
			"       (select b.block_name\n" + 
			"          from equ_c_block b\n" + 
			"         where b.block_code = a.device_code\n" + 
			"           and b.is_use = 'Y') device_name,\n" + 
			"       a.test_type,\n" + 
			"       to_char(a.start_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
			"       to_char(a.end_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
			"       a.examine_by,\n" + 
			"       getworkername(a.examine_by),\n" + 
			"       a.charge_by,\n" + 
			"       getworkername(a.charge_by),\n" + 
			"       a.content,\n" + 
			"       a.fill_by,\n" + 
			"       getworkername(a.fill_by),\n" + 
			"       a.dep_code,\n" + 
			"       getdeptname(a.dep_code),\n" + 
			"       to_char(a.fill_date, 'yyyy-MM-dd hh24:mi:ss')\n" + 
			"  from PT_HXJD_J_RLSBJCQK a\n" + 
			" where a.enterprise_code = '"+enterpriseCode+"'";

		String sqlCount = "select count(1) from PT_HXJD_J_RLSBJCQK a where a.enterprise_code = '"+enterpriseCode+"'";
		String strWhere = "";
		if (deviceCode != null && deviceCode.length() > 0) {
			strWhere += " and a.device_code = '" + deviceCode + "'";
		} 
		if (sDate != null && sDate.length() > 0) {
			strWhere += " and a.start_date >= to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			strWhere += "and a.start_date <= to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		sql+=strWhere; 
		sqlCount+=strWhere;
		sql = sql + " order by a.start_date desc";
		sqlCount = sqlCount + " order by a.start_date desc";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		
		if(list != null && list.size() > 0)
		{
			while(it.hasNext())
			{
				ThermalEquCheckForm form = new ThermalEquCheckForm();
				PtHxjdJRlsbjcqk model = new PtHxjdJRlsbjcqk();
				Object[] data = (Object[]) it.next();
				model.setRlsbjcId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					model.setDeviceCode(data[1].toString());
				if(data[2] != null)
					form.setDeviceName(data[2].toString());
				if(data[3] != null)
					model.setTestType(Long.parseLong(data[3].toString()));
				if(data[4] != null)
					form.setStartDate(data[4].toString());
				if(data[5] != null)
					form.setEndDate(data[5].toString());
				if(data[6] != null)
					model.setExamineBy(data[6].toString());
				if(data[7] != null)
					form.setExamineName(data[7].toString());
				if(data[8] != null)
					model.setChargeBy(data[8].toString());
				if(data[9] != null)
					form.setChargeName(data[9].toString());
				if(data[10] != null){
					model.setContent(data[10].toString());
				}else{
					model.setContent("");
				}
				if(data[11] != null)
					model.setFillBy(data[11].toString());
				if(data[12] != null)
					form.setFillName(data[12].toString());
				if(data[13] != null)
					model.setDepCode(data[13].toString());
				if(data[14] != null)
					form.setDepName(data[14].toString());
				if(data[15] != null)
					form.setFillDate(data[15].toString());
				
				form.setEqu(model);
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}
}