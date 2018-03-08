package power.ejb.productiontec.chemistry;

import java.util.ArrayList;
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
import power.ejb.productiontec.chemistry.form.CondenserLeakForm;

/**
 * Facade for entity PtHxjdJNqqxl.
 * 
 * @see power.ejb.productiontec.chemistry.PtHxjdJNqqxl
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtHxjdJNqqxlFacade implements PtHxjdJNqqxlFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "PtHxjdJNqqxlDetailFacade")
	protected PtHxjdJNqqxlDetailFacadeRemote remote;
	
	public PtHxjdJNqqxl save(PtHxjdJNqqxl entity) {
		LogUtil.log("saving PtHxjdJNqqxl instance", Level.INFO, null);
		try {
			entity.setNqjxlId(bll.getMaxId("PT_HXJD_J_NQQXL", "nqjxl_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids) {
		String sql = "delete from PT_HXJD_J_NQQXL b where b.nqjxl_id in(" + ids + ")";
		bll.exeNativeSQL(sql);
		String sql1 = "delete from PT_HXJD_J_NQQXL_DETAIL b where b.nqjxl_id in(" + ids + ")";
		bll.exeNativeSQL(sql1);
	}
	
	public PtHxjdJNqqxl update(PtHxjdJNqqxl entity) {
		LogUtil.log("updating PtHxjdJNqqxl instance", Level.INFO, null);
		try {
			PtHxjdJNqqxl result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtHxjdJNqqxl findById(Long id) {
		LogUtil.log("finding PtHxjdJNqqxl instance with id: " + id, Level.INFO,
				null);
		try {
			PtHxjdJNqqxl instance = entityManager.find(PtHxjdJNqqxl.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void modifyRecords(List<CondenserLeakForm> list, String nqjxlId) {
		if (list != null) {
			for (CondenserLeakForm m : list) {
				// 主表
				PtHxjdJNqqxl phjz = new PtHxjdJNqqxl();
				// 明细表
				PtHxjdJNqqxlDetail tempPh = new PtHxjdJNqqxlDetail();
				// 主表id不为空
				if (m.getCondenser().getNqjxlId() != null
						&& !(m.getCondenser().getNqjxlId().toString().equals(""))) {
					phjz = update(m.getCondenser());
				} else {
					phjz = save(m.getCondenser());
				}
				// 明细表id不为空
				if(m.getNqjxlDetailId() != null && !(m.getNqjxlDetailId().toString().equals("")))
				{
					tempPh.setNqjxlDetailId(m.getNqjxlDetailId());
					if (phjz.getNqjxlId() != null && !(phjz.getNqjxlId().equals("")))
						tempPh.setNqjxlId(phjz.getNqjxlId());
					if (m.getProjectNames() != null && !(m.getProjectNames().equals("")))
						tempPh.setProjectNames(m.getProjectNames());
					if (m.getItemName1() != null
							&& !(m.getItemName1().equals("")))
						tempPh.setItemName1(m.getItemName1());
					if (m.getItemName2() != null
							&& !(m.getItemName2().equals("")))
						tempPh.setItemName2(m.getItemName2());
					if (m.getItemName3() != null
							&& !(m.getItemName3().equals("")))
						tempPh.setItemName3(m.getItemName3());
					if (m.getItemName4() != null
							&& !(m.getItemName4().equals("")))
						tempPh.setItemName4(m.getItemName4());

					if(m.getEnterpriseCode() != null && !(m.getEnterpriseCode().equals("")))
						tempPh.setEnterpriseCode(m.getEnterpriseCode());
					
					remote.update(tempPh);
					//remote.save(tempPh);
				} else {
					if (phjz.getNqjxlId() != null
							&& !(phjz.getNqjxlId().equals("")))
						tempPh.setNqjxlId(phjz.getNqjxlId());
					if (m.getProjectNames() != null
							&& !(m.getProjectNames().equals("")))
						tempPh.setProjectNames(m.getProjectNames());
					if (m.getItemName1() != null
							&& !(m.getItemName1().equals("")))
						tempPh.setItemName1(m.getItemName1());
					if (m.getItemName2() != null
							&& !(m.getItemName2().equals("")))
						tempPh.setItemName2(m.getItemName2());
					if (m.getItemName3() != null
							&& !(m.getItemName3().equals("")))
						tempPh.setItemName3(m.getItemName3());
					if (m.getItemName4() != null
							&& !(m.getItemName4().equals("")))
						tempPh.setItemName4(m.getItemName4());

					if(m.getEnterpriseCode() != null && !(m.getEnterpriseCode().equals("")))
						tempPh.setEnterpriseCode(m.getEnterpriseCode());
				
					remote.save(tempPh);
					entityManager.flush();
				}
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject findCondenserLeakList(String deviceCode,String sDate,String eDate,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql =   "select a.nqjxl_id,\n" +
			"       a.device_code,\n" + 
			"       (select b.block_name\n" + 
			"          from equ_c_block b\n" + 
			"         where b.block_code = a.device_code\n" + 
			"           and b.is_use = 'Y'),\n" + 
			"       a.water_quanlity,\n" + 
			"       a.place,\n" + 
			"       to_char(a.start_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
			"       to_char(a.end_date,'yyyy-MM-dd hh24:mi:ss'),\n" + 
			"       a.content,\n" + 
			"       a.handle_step,\n" + 
			"       a.handle_result,\n" + 
			"       a.memo,\n" + 
			"       a.fill_by,\n" + 
			"       getworkername(a.fill_by),\n" + 
			"       to_char(a.fill_date, 'yyyy-MM-dd hh24:mi:ss')\n" + 
			"  from PT_HXJD_J_NQQXL a\n" + 
			" where a.enterprise_code = '"+enterpriseCode+"'";
		
		String sqlCount = "select count(1) from PT_HXJD_J_NQQXL a where a.enterprise_code = '"+enterpriseCode+"'";
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
		
		if (list != null && list.size() > 0) {
			while (it.hasNext()) {
				CondenserLeakForm form = new CondenserLeakForm();
				PtHxjdJNqqxl model = new PtHxjdJNqqxl();
				Object[] data = (Object[]) it.next();
				model.setNqjxlId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					model.setDeviceCode(data[1].toString());
				if(data[2] != null)
					form.setDeviceName(data[2].toString());
				if(data[3] != null)
				{
					model.setWaterQuanlity(data[3].toString());
				}else{
					model.setWaterQuanlity("");
				}
				if(data[4] != null)
				{
					model.setPlace(data[4].toString());
				}else{
					model.setPlace("");
				}
				if(data[5] != null)
					form.setStartDate(data[5].toString());
				if(data[6] != null)
					form.setEndDate(data[6].toString());
				if(data[7] != null)
					model.setContent(data[7].toString());
				if(data[8] != null)
				{
					model.setHandleStep(data[8].toString());
				}else{
					model.setHandleStep("");
				}
				if(data[9] != null)
				{
					model.setHandleResult(data[9].toString());
				}else
				{
					model.setHandleResult("");
				}
				if(data[10] != null)
				{
					model.setMemo(data[10].toString());
				}else{
					model.setMemo("");
				}
				if(data[11] != null)
					model.setFillBy(data[11].toString());
				if(data[12] != null)
					form.setFillName(data[12].toString());
				if(data[13] != null)
					form.setFillDate(data[13].toString());
				
				form.setCondenser(model);
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

}