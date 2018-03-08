package power.ejb.productiontec.chemistry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import power.ejb.productiontec.insulation.PtJyjdJYqybtzlh;
import power.ejb.workticket.business.RunJWorkticketSafety;

/**
 * @author liuyi 090708
 */
@Stateless
public class PtHxjdJZxybybzbFacade implements PtHxjdJZxybybzbFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "PtHxjdJZxybybFacade")
	protected PtHxjdJZxybybFacadeRemote remote;

	/**
	 * 增加一条化学在线仪表月报信息
	 */
	public PtHxjdJZxybybzb save(PtHxjdJZxybybzb entity) {
		LogUtil.log("saving PtHxjdJZxybybzb instance", Level.INFO, null);
		try {
//			Calendar calendar = new GregorianCalendar();
//			calendar.setTime(entity.getReportTime());
//			String sql = "select count(*) from PT_HXJD_J_ZXYBYBZB t\n"
//				+ "where t.DEVICE_CODE = '" + entity.getDeviceCode() + "'"
//				+ " and to_char(a.REPORT_TIME, 'yyyy-MM') =" + calendar.YEAR + "-" + calendar.MONTH ;
//			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
//			{
//				return null;
//			}
			entity.setWorkFlowStatus(1l);
			entity.setWorkFlowNo(1L);
			entity.setZxybybzbId(bll.getMaxId("PT_HXJD_J_ZXYBYBZB", "ZXYBYBZB_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}

	/**
	 * 删除一条记录
	 */
	public void delete(PtHxjdJZxybybzb entity) {
		LogUtil.log("deleting PtHxjdJZxybybzb instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtHxjdJZxybybzb.class, entity
					.getZxybybzbId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条或多条记录
	 */
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_HXJD_J_ZXYBYBZB a\n"
	    + " where a.ZXYBYBZB_ID in (" + ids
	   + ")\n" ;
       bll.exeNativeSQL(sql);
       
       String detailsSql = "delete from  "+
		"PT_HXJD_J_ZXYBYB b\n"
	    + " where b.ZXYBYBZB_ID in (" + ids
	   + ")\n" ;
      bll.exeNativeSQL(detailsSql);
		
	}
	/**
	 * 更新一条记录
	 */
	public PtHxjdJZxybybzb update(PtHxjdJZxybybzb entity) {
		LogUtil.log("updating PtHxjdJZxybybzb instance", Level.INFO, null);
		try {
//			Calendar calendar = new GregorianCalendar();
//			calendar.setTime(entity.getReportTime());
//			String sql = "select count(*) from PT_HXJD_J_ZXYBYBZB t\n"
//				+ "where t.DEVICE_CODE = '" + entity.getDeviceCode() + "'"
//				+ " and to_char(a.REPORT_TIME, 'yyyy-MM') =" + calendar.YEAR + "-" + calendar.MONTH 
//				+ " and t.ZXYBYBZB_ID !=" + entity.getZxybybzbId();
//			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
//			{
//				return null;
//			}
			PtHxjdJZxybybzb result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条记录
	 */
	public PtHxjdJZxybybzb findById(Long id) {
		LogUtil.log("finding PtHxjdJZxybybzb instance with id: " + id,
				Level.INFO, null);
		try {
			PtHxjdJZxybybzb instance = entityManager.find(
					PtHxjdJZxybybzb.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


	

	/**
	 * 查询化学在线仪表月报列表
	 * @param name 机组名称
	 * @param month 月份
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String name, String month, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.ZXYBYBZB_ID,\n"
			+ "       a.DEVICE_CODE,\n"
			+ "       a.REPORT_TIME,\n"
			+ "       a.MEMO,\n"
			+ "       a.FILL_BY,\n"
			+ "       a.FILL_DATE,\n"
			+ "       a.WORK_FLOW_NO,\n"
			+ "       a.WORK_FLOW_STATUS,\n"
			+ "       a.ENTERPRISE_CODE, \n"
			+ "       GETBlockBYCODE(a.DEVICE_CODE), \n"
			+ "       to_char(a.REPORT_TIME, 'yyyy-MM'),\n"
			+ "       getworkername(a.FILL_BY),\n"
			+ "       to_char(a.FILL_DATE, 'yyyy-MM-dd') \n"
			+ "  from PT_HXJD_J_ZXYBYBZB a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		String sqlCount = "select count(a.ZXYBYBZB_ID)\n"
			+ "  from PT_HXJD_J_ZXYBYBZB a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and a.DEVICE_CODE =" + name + " \n";
			sqlCount = sqlCount + " and a.DEVICE_CODE =" + name + " \n";
		}
		if(month != null && (!month.equals("")))
		{
			sql = sql + " and to_char(a.REPORT_TIME, 'yyyy-MM') like '%" + month + "%' \n";
			sqlCount = sqlCount + " and to_char(a.REPORT_TIME, 'yyyy-MM') like '%" + month + "%' \n";
		}
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtHxjdJZxybybzb phj = new PtHxjdJZxybybzb();
				ChemistryReportForm info = new ChemistryReportForm();
				Object []data = (Object[])it.next();
				phj.setZxybybzbId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					phj.setDeviceCode(data[1].toString());
				if(data[2] != null)
					try {
						phj.setReportTime(sbf.parse(data[2].toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				if(data[3] != null)
					phj.setMemo(data[3].toString());
				if(data[4] != null)
					phj.setFillBy(data[4].toString());
				if(data[5] != null)
					try {
						phj.setFillDate(sbf.parse(data[5].toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				if(data[6] != null)
					phj.setWorkFlowNo(Long.parseLong((data[6].toString())));
				if(data[7] != null)
					phj.setWorkFlowStatus(Long.parseLong(data[7].toString()));
				if(data[8] != null)
					phj.setEnterpriseCode(data[8].toString());
				if(data[9] != null)
					info.setDeviceName(data[9].toString());
				if(data[10] != null)
					info.setMonth(data[10].toString());
				if(data[11] != null)
					info.setFillName(data[11].toString());
				if(data[12] != null)
					info.setFillDate(data[12].toString());
				
				info.setPhj(phj);
				arrlist.add(info);
			 }
		  }
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
	}

	
	/**
	 * 根据化学在线仪表月报主表的id和企业编码查询化学在线仪表月报主表、明细表和化学在线仪器维护表
	 * @param zxybybzbId 化学在线仪表月报主表id
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findDetailsAll(String zxybybzbId, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "";
		String sqlCount = "";
		if(zxybybzbId ==null || zxybybzbId.equals(""))
		{
			sql = "select a.METER_ID,\n"
				+ "       a.METER_NAME,\n"
				+ "       a.ENTERPRISE_CODE \n"
				+ "  from PT_HXJD_C_HXZXYBWH a \n"
				+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
			
			
			sqlCount = "select count(a.METER_ID)\n"
				+ "  from PT_HXJD_C_HXZXYBWH a \n"
				+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
			
			
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			System.out.println("sss " + sql);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			if (list != null && list.size() > 0) {
				while(it.hasNext()){
					PtHxjdJZxybybzb phj = new PtHxjdJZxybybzb();
					ChemistryReportForm info = new ChemistryReportForm();
					Object []data = (Object[])it.next();
					info.setMeterId(data[0].toString());
					if(data[1] != null)
						info.setMeterName(data[1].toString());
					info.setPhj(phj);
					arrlist.add(info);
				 }
			  }
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
				pg.setList(arrlist);
				pg.setTotalCount(totalCount);
		}
		else
		{
			 sql = "select a.ZXYBYBZB_ID,\n"
				+ "       a.DEVICE_CODE,\n"
				+ "       a.REPORT_TIME,\n"
				+ "       a.MEMO,\n"
				+ "       a.FILL_BY,\n"
				+ "       a.FILL_DATE,\n"
				+ "       a.WORK_FLOW_NO,\n"
				+ "       a.WORK_FLOW_STATUS,\n"
				+ "       a.ENTERPRISE_CODE, \n"
				+ "       GETBlockBYCODE(a.DEVICE_CODE), \n"
				+ "       to_char(a.REPORT_TIME, 'yyyy-MM'),\n"
				+ "       getworkername(a.FILL_BY),\n"
				+ "       to_char(a.FILL_DATE, 'yyyy-MM-dd'), \n"
				+ "       b.ZXYBYB_ID,\n"
				+ "       c.METER_ID,\n"
				+ "       b.MUST_THROW_NUM,\n"
				+ "       b.EQUIP_NUM,\n"
				+ "       b.THROW_NUM,\n"
				+ "       b.EQUIP_RATE,\n"
				+ "       b.THROW_RATE,\n"
				+ "       b.SEARCH_RATE,\n"
				
				+ "       c.METER_NAME \n"
				+ "  from PT_HXJD_J_ZXYBYBZB a,PT_HXJD_J_ZXYBYB b, PT_HXJD_C_HXZXYBWH c \n"
				+ " where c.enterprise_code ='" + enterpriseCode + "' \n"
				+ " and c.METER_ID = b.METER_ID(+) \n"
				+ " and b.ZXYBYBZB_ID = a.ZXYBYBZB_ID(+) \n";
			
			
			 sqlCount = "select count(*)\n"
				+ "  from PT_HXJD_J_ZXYBYBZB a,PT_HXJD_J_ZXYBYB b, PT_HXJD_C_HXZXYBWH c \n"
				+ " where c.enterprise_code ='" + enterpriseCode + "' \n"
				+ " and c.METER_ID = b.METER_ID(+) \n"
				+ " and b.ZXYBYBZB_ID = a.ZXYBYBZB_ID(+) \n";
			
			
			if(zxybybzbId !=null && (!zxybybzbId.equals("")))
			{
				sql = sql + " and a.ZXYBYBZB_ID =" + zxybybzbId + " \n";
				sqlCount = sqlCount + " and a.ZXYBYBZB_ID =" + zxybybzbId + " \n";
			}
			
			
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			System.out.println(sql);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
			if (list != null && list.size() > 0) {
				while(it.hasNext()){
					PtHxjdJZxybybzb phj = new PtHxjdJZxybybzb();
					ChemistryReportForm info = new ChemistryReportForm();
					Object []data = (Object[])it.next();
					phj.setZxybybzbId(Long.parseLong(data[0].toString()));
					if(data[1] != null)
						phj.setDeviceCode(data[1].toString());
					if(data[2] != null)
						try {
							phj.setReportTime(sbf.parse(data[2].toString()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					if(data[3] != null)
						phj.setMemo(data[3].toString());
					if(data[4] != null)
						phj.setFillBy(data[4].toString());
					if(data[5] != null)
						try {
							phj.setFillDate(sbf.parse(data[5].toString()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					if(data[6] != null)
						phj.setWorkFlowNo(Long.parseLong((data[6].toString())));
					if(data[7] != null)
						phj.setWorkFlowStatus(Long.parseLong(data[7].toString()));
					if(data[8] != null)
						phj.setEnterpriseCode(data[8].toString());
					if(data[9] != null)
						info.setDeviceName(data[9].toString());
					if(data[10] != null)
						info.setMonth(data[10].toString());
					if(data[11] != null)
						info.setFillName(data[11].toString());
					if(data[12] != null)
						info.setFillDate(data[12].toString());
					if(data[13] != null)
						info.setZxybybId(data[13].toString());
					if(data[14] != null)
						info.setMeterId(data[14].toString());
					if(data[15] != null)
						info.setMustThrowNum(data[15].toString());
					if(data[16] != null)
						info.setEquipNum(data[16].toString());
					if(data[17] != null)
						info.setThrowNum(data[17].toString());
					if(data[18] != null)
						info.setEquipRate(data[18].toString());
					if(data[19] != null)
						info.setThrowRate(data[19].toString());
					if(data[20] != null)
						info.setSearchRate(data[20].toString());
					if(data[21] != null)
						info.setMeterName(data[21].toString());
					
					info.setPhj(phj);
					arrlist.add(info);
				 }
			  }
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
				pg.setList(arrlist);
				pg.setTotalCount(totalCount);
				
		}
		return pg;		
		
	}

	public void modifyRecords(List<ChemistryReportForm> list, String zxybybzbId) {
		if(list!=null)
		{
			
			for(ChemistryReportForm m:list)
			{
				// 月报主表
				PtHxjdJZxybybzb phjz = new PtHxjdJZxybybzb();
				// 月报明细表
				PtHxjdJZxybyb tempPh = new PtHxjdJZxybyb();
				// 主表id不为空
				if(m.getPhj().getZxybybzbId() != null && !(m.getPhj().getZxybybzbId().toString().equals("")))
				{
					phjz = update(m.getPhj());
				}
				else
				{
					phjz = save(m.getPhj());
				}
				//明细表id不为空
				if(m.getZxybybId() != null && !(m.getZxybybId().toString().equals("")))
				{
					tempPh.setZxybybId(Long.parseLong(m.getZxybybId()));
					if(phjz.getZxybybzbId() != null && !(phjz.getZxybybzbId().equals("")))
						tempPh.setZxybybzbId(phjz.getZxybybzbId());
					if(m.getMeterId() != null && !(m.getMeterId().equals("")))
						tempPh.setMeterId(Long.parseLong(m.getMeterId()));
					if(m.getMustThrowNum() != null && !(m.getMustThrowNum().equals("")))
						tempPh.setMustThrowNum(Long.parseLong(m.getMustThrowNum()));
					if(m.getEquipNum() != null && !(m.getEquipNum().equals("")))
						tempPh.setEquipNum(Long.parseLong(m.getEquipNum()));
					if(m.getThrowNum() != null && !(m.getThrowNum().equals("")))
						tempPh.setThrowNum(Long.parseLong(m.getThrowNum()));
					if(m.getMustThrowNum() != null && !(m.getMustThrowNum().equals(""))
							&& m.getEquipNum() != null && !(m.getEquipNum().equals(""))
							&& !(m.getMustThrowNum().equals("0")))
					tempPh.setEquipRate(Double.valueOf(tempPh.getEquipNum())/Double.valueOf(tempPh.getMustThrowNum()));
					
					if(m.getThrowNum() != null && !(m.getThrowNum().equals(""))
							&& m.getEquipNum() != null && !(m.getEquipNum().equals(""))
							&& !(m.getEquipNum().equals("0")))
					tempPh.setThrowRate(Double.valueOf(tempPh.getThrowNum())/Double.valueOf(tempPh.getEquipNum()));
					if(m.getSearchRate() != null && !(m.getSearchRate().equals("")))
					tempPh.setSearchRate(Double.parseDouble(m.getSearchRate()));
					
					remote.update(tempPh);
				}
				else
				{
					if(phjz.getZxybybzbId() != null && !(phjz.getZxybybzbId().equals("")))
						tempPh.setZxybybzbId(phjz.getZxybybzbId());
					if(m.getMeterId() != null && !(m.getMeterId().equals("")))
						tempPh.setMeterId(Long.parseLong(m.getMeterId()));
					if(m.getMustThrowNum() != null && !(m.getMustThrowNum().equals("")))
						tempPh.setMustThrowNum(Long.parseLong(m.getMustThrowNum()));
					if(m.getEquipNum() != null && !(m.getEquipNum().equals("")))
						tempPh.setEquipNum(Long.parseLong(m.getEquipNum()));
					if(m.getThrowNum() != null && !(m.getThrowNum().equals("")))
						tempPh.setThrowNum(Long.parseLong(m.getThrowNum()));
					if(m.getMustThrowNum() != null && !(m.getMustThrowNum().equals(""))
							&& m.getEquipNum() != null && !(m.getEquipNum().equals("")))
					tempPh.setEquipRate(Double.valueOf(tempPh.getEquipNum())/Double.valueOf(tempPh.getMustThrowNum()));
					
					if(m.getThrowNum() != null && !(m.getThrowNum().equals(""))
							&& m.getEquipNum() != null && !(m.getEquipNum().equals("")))
					tempPh.setThrowRate(Double.valueOf(tempPh.getThrowNum())/Double.valueOf(tempPh.getEquipNum()));
					if(m.getSearchRate() != null && !(m.getSearchRate().equals("")))
					tempPh.setSearchRate(Double.parseDouble(m.getSearchRate()));
					
					remote.save(tempPh);
					entityManager.flush();
				}
			}

		}
		
	}



	

}