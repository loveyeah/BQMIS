package power.ejb.productiontec.chemistry;

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
import power.ejb.manage.client.ConJAppraiseRecord;
import power.ejb.manage.stat.BpCInputReport;
import power.ejb.productiontec.chemistry.form.ThermalEquCheckDetailForm;

/**
 * Facade for entity PtHxjdJRlsbjcqkDetail.
 * 
 * @see power.ejb.productiontec.chemistry.PtHxjdJRlsbjcqkDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtHxjdJRlsbjcqkDetailFacade implements
		PtHxjdJRlsbjcqkDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public void save(PtHxjdJRlsbjcqkDetail entity) {
		LogUtil.log("saving PtHxjdJRlsbjcqkDetail instance", Level.INFO, null);
		try {
			if(entity.getRlsbjcDetailId() == null){
			entity.setRlsbjcDetailId(bll.getMaxId("PT_HXJD_J_RLSBJCQK_DETAIL", "rlsbjc_detail_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 批量增加
	 * @param list
	 */
	public void save(List<PtHxjdJRlsbjcqkDetail> addList) {
		if (addList != null && addList.size() > 0) {
			Long detailId = bll.getMaxId("PT_HXJD_J_RLSBJCQK_DETAIL", "rlsbjc_detail_id");
			int i=0;
			for (PtHxjdJRlsbjcqkDetail entity : addList) {
				entity.setRlsbjcDetailId(detailId+(i++));
				this.save(entity);
			}
		}
	}
	
	/**
	 * 删除一条数据
	 * @param entity
	 */
	public void delete(PtHxjdJRlsbjcqkDetail entity) {
		LogUtil.log("deleting PtHxjdJRlsbjcqkDetail instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtHxjdJRlsbjcqkDetail.class, entity.getRlsbjcDetailId());
			entityManager.remove(entity);
			String sql = "DELETE FROM BP_C_INPUT_REPORT_SETUP t\n"
				+ " WHERE t.REPORT_CODE = '"
				+ entity.getRlsbjcDetailId()
				+ "'\n"
				+ "   AND t.enterprise_code = '"
				+ entity.getEnterpriseCode() + "'";
			bll.exeNativeSQL(sql);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 批量删除数据
	 * @param ids 
	 * @return true 删除成功  false 删除不成功
	 */
	public boolean delete(String ids) {
		try {
			String[] temp1 = ids.split(",");
			for (String i : temp1) {
				PtHxjdJRlsbjcqkDetail entity=new PtHxjdJRlsbjcqkDetail();
					entity=this.findById(Long.parseLong(i));
				this.delete(entity);
			}
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}
	
	/**
	 * 批量修改
	 * @param updateList
	 */
	public void update(List<PtHxjdJRlsbjcqkDetail> updateList) {
		try {
			for (PtHxjdJRlsbjcqkDetail data : updateList) {
				String sql = "update PT_HXJD_J_RLSBJCQK_DETAIL t\n"
						+ "set t.rlsbjc_detail_id='" + data.getRlsbjcDetailId() + "',"
						+ " t.rlsbjc_id='" + data.getRlsbjcId() + "',"
						+ " t.equ_code='" + data.getEquCode() + "',"
						+ " t.repair_date='" + data.getRepairDate() + "',"					
						+ " t.repair_type='" + data.getRepairType() + "',"
						+ " t.repair_number='" + data.getRepairNumber() + "',"
						+ " t.check_high='" + data.getCheckHigh() + "',"
						+ " t.check_name='" + data.getCheckName() + "',"
						+ " t.check_part='" + data.getCheckPart() + "',"						
						+ " t.dirty_capacity='" + data.getDirtyCapacity() + "',"
						+ " t.sediment_quantity='" + data.getSedimentQuantity() + "',"
						+ " t.memo='" + data.getMemo() + "',"		
						+ " t.course_number='" + data.getCourseNumber() + "'"
						+ "where t.rlsbjc_detail_id='" + data.getRlsbjcDetailId() + "'\n";

				bll.exeNativeSQL(sql);
			}
		} catch (RuntimeException e) {
			throw e;
		}

	}
	public PtHxjdJRlsbjcqkDetail update(PtHxjdJRlsbjcqkDetail entity) {
		LogUtil.log("updating PtHxjdJRlsbjcqkDetail instance", Level.INFO,null);
		try {
			PtHxjdJRlsbjcqkDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtHxjdJRlsbjcqkDetail findById(Long id) {
		LogUtil.log("finding PtHxjdJRlsbjcqkDetail instance with id: " + id,
				Level.INFO, null);
		try {
			PtHxjdJRlsbjcqkDetail instance = entityManager.find(
					PtHxjdJRlsbjcqkDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findEquCheckDetailList(String rlsbjcId, String enterpriseCode,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql =  "select a.rlsbjc_detail_id,\n" +
			"       a.rlsbjc_id,\n" + 
			"       a.equ_code,\n" + 
			"       getequnamebycode(a.equ_code),\n" + 
			"       to_char(a.repair_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
			"       a.course_number,\n" + 
			"       a.repair_type,\n" + 
			"       a.repair_number,\n" + 
			"       a.check_high,\n" + 
			"       a.check_name,\n" + 
			"       a.check_part,\n" + 
			"       a.dirty_capacity,\n" + 
			"       a.sediment_quantity,\n" + 
			"       a.memo\n" + 
			"  from PT_HXJD_J_RLSBJCQK_DETAIL a\n" + 
			" where a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.rlsbjc_id = '"+rlsbjcId+"'";

		String sqlCount =  "select count(1)\n" +
			"  from PT_HXJD_J_RLSBJCQK_DETAIL a\n" + 
			" where a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.rlsbjc_id = '"+rlsbjcId+"'";
		
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if(list != null && list.size() > 0)
		{
			while(it.hasNext())
			{
				ThermalEquCheckDetailForm form = new ThermalEquCheckDetailForm();
				Object[] data = (Object[]) it.next();
				form.setRlsbjcDetailId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					form.setRlsbjcId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					form.setEquCode(data[2].toString());
				if(data[3] != null)
					form.setEquName(data[3].toString());
				if(data[4] != null)
					form.setRepairDate(data[4].toString());
				if(data[5] != null)
					form.setCourseNumber(Double.parseDouble(data[5].toString()));
				if(data[6] != null)
					form.setRepairType(data[6].toString());
				if(data[7] != null)
					form.setRepairNumber(Long.parseLong(data[7].toString()));
				if(data[8] != null)
					form.setCheckHigh(data[8].toString());
				if(data[9] != null)
					form.setCheckName(data[9].toString());
				if(data[10] != null)
					form.setCheckPart(data[10].toString());
				if(data[11] != null)
					form.setDirtyCapacity(data[11].toString());
				if(data[12] != null)
					form.setSedimentQuantity(data[12].toString());
				if(data[13] != null)
					form.setMemo(data[13].toString());
				arrlist.add(form);
				
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		
		return pg;
	}

}