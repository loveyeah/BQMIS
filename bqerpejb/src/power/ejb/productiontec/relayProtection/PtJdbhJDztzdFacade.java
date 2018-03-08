package power.ejb.productiontec.relayProtection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.productiontec.relayProtection.form.ProtectNoticeForm;

/**
 * Facade for entity PtJdbhJDztzd.
 * 
 * @see power.ejb.productiontec.relayProtection.PtJdbhJDztzd
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJdbhJDztzdFacade implements PtJdbhJDztzdFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "PtJdbhJDzdjbFacade")
	protected PtJdbhJDzdjbFacadeRemote remote;

	
	public PtJdbhJDztzd save(PtJdbhJDztzd entity) throws CodeRepeatException {
		LogUtil.log("saving PtJdbhJDztzd instance", Level.INFO, null);
		if (!this.checkCode(entity.getDztzdCode())) {
			entity.setDztzdId(bll.getMaxId("PT_JDBH_J_DZTZD", "dztzd_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} else {
			throw new CodeRepeatException("通知单编号不能重复！");
		}
		
	}

	public void deleteMulti(String ids) {
		String sql = "delete PT_JDBH_J_DZTZD b where b.dztzd_id in(" + ids + ")";
		bll.exeNativeSQL(sql);
		
		String sql1 = "delete PT_JDBH_J_DZDJB b where b.dztzd_id in(" + ids + ")";
		bll.exeNativeSQL(sql1);
	}

	public PtJdbhJDztzd update(PtJdbhJDztzd entity) throws CodeRepeatException {
		LogUtil.log("updating PtJdbhJDztzd instance", Level.INFO, null);
		if (!this.checkCode(entity.getDztzdCode(), entity.getDztzdId())) {
			PtJdbhJDztzd result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} else {
			throw new CodeRepeatException("通知单编号不能重复！");
		}
	}

	public PtJdbhJDztzd findById(Long id) {
		LogUtil.log("finding PtJdbhJDztzd instance with id: " + id, Level.INFO,
				null);
		try {
			PtJdbhJDztzd instance = entityManager.find(PtJdbhJDztzd.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 判断编码是否重复
	 * @param dztzdCode
	 * @param dztzdId
	 * @return
	 */
	private boolean checkCode(String dztzdCode, Long... dztzdId) {
		boolean isSame = false;
		String sql = "select count(*) from PT_JDBH_J_DZTZD t\n"
				+ "where t.dztzd_code = '" + dztzdCode + "'";

		if (dztzdId != null && dztzdId.length > 0) {
			sql += "  and t.dztzd_id <> " + dztzdId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}
	
	public void modifyRecords(List<ProtectNoticeForm> list, String noticeId) throws CodeRepeatException 
	{
		if(list != null)
		{
			for(ProtectNoticeForm m : list)
			{
				//继电保护定值通知单
				PtJdbhJDztzd notice= new PtJdbhJDztzd();
				//继电保护定值登记表
				PtJdbhJDzdjb noticeDetail = new PtJdbhJDzdjb();
				if(m.getNotice().getDztzdId() != null && !(m.getNotice().getDztzdId().toString().equals("")))
				{
					notice = this.update(m.getNotice());
				}else{
					notice = this.save(m.getNotice());
				}
				if(m.getDzdjbId() != null && !(m.getDzdjbId().toString().equals("")))
				{
					noticeDetail.setDzdjbId(m.getDzdjbId());
					if(notice.getDztzdId() != null && !(notice.getDztzdId().equals("")))
						noticeDetail.setDztzdId(notice.getDztzdId());
					if(m.getFixvalueItemId() != null && !(m.getFixvalueItemId().equals("")))
						noticeDetail.setFixvalueItemId(m.getFixvalueItemId());
					if(m.getWholeFixvalue() != null && !(m.getWholeFixvalue().equals("")))
						noticeDetail.setWholeFixvalue(m.getWholeFixvalue());
					if(m.getMemo() != null && !(m.getMemo().equals("")))
						noticeDetail.setMemo(m.getMemo());
					
					remote.update(noticeDetail);
				}
				else{
					if(notice.getDztzdId() != null && !(notice.getDztzdId().equals("")))
						noticeDetail.setDztzdId(notice.getDztzdId());
					if(m.getFixvalueItemId() != null && !(m.getFixvalueItemId().equals("")))
						noticeDetail.setFixvalueItemId(m.getFixvalueItemId());
					if(m.getWholeFixvalue() != null && !(m.getWholeFixvalue().equals("")))
						noticeDetail.setWholeFixvalue(m.getWholeFixvalue());
					if(m.getMemo() != null && !(m.getMemo().equals("")))
						noticeDetail.setMemo(m.getMemo());
					
					remote.save(noticeDetail);
					entityManager.flush();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findRelayProtectionNoticeList(String enterpriseCode,
			String sDate, String eDate, String equName,
			final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.dztzd_id,\n"
				+ "       a.device_id,\n"
				+ "       getequnamebycode((select b.equ_code\n"
				+ "                          from PT_JDBH_J_BHZZTZ b\n"
				+ "                         where b.device_id = a.device_id)),\n"
				+ "       a.dzjssm_id,\n"
				+ "       (select c.jssm_name\n"
				+ "          from PT_JDBH_J_DZJSSM c\n"
				+ "         where c.dzjssm_id = a.dzjssm_id),\n"
				+ "       a.dztzd_code,\n"
				+ "       a.ct_code,\n"
				+ "       a.pt_code,\n"
				+ "       a.memo,\n"
				+ "       to_char(a.effective_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       a.fill_by,\n"
				+ "       getworkername(a.fill_by),\n"
				+ "       a.use_status,\n"
				+ "       a.save_by,\n"
				+ "       getworkername(a.save_by),\n"
				+ "       a.save_mark,\n"
				+ "       getequnamebycode(p.equ_code)\n"
				+ "\n"
				+ "  from PT_JDBH_J_DZTZD a, PT_JDBH_J_BBHSBTZ p, PT_JDBH_J_BHZZTZ t\n"
				+ " where t.device_id = a.device_id(+)\n"
				+ "   and p.protected_device_id = t.protected_device_id(+)\n"
				+ "   and a.enterprise_code = '" + enterpriseCode + "'";

		String sqlCount = "select count(1) from PT_JDBH_J_DZTZD a, PT_JDBH_J_BBHSBTZ p, PT_JDBH_J_BHZZTZ t where t.device_id = a.device_id(+) and p.protected_device_id = t.protected_device_id(+) and a.enterprise_code = '"
				+ enterpriseCode + "'";
		String strWhere = "";
		if (equName != null && equName.length() > 0) {
			strWhere += " and getequnamebycode((select b.equ_code\n"
					+ "                          from PT_JDBH_J_BHZZTZ b\n"
					+ "                         where b.device_id = a.device_id)) = '"
					+ equName + "'";
		}
		if (sDate != null && sDate.length() > 0) {
			strWhere += " and a.effective_date >= to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			strWhere += "and a.effective_date <= to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		sql += strWhere;
		sqlCount += strWhere;
		sql = sql + " order by a.effective_date desc";
		sqlCount = sqlCount + " order by a.effective_date desc";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while (it.hasNext()) {
				ProtectNoticeForm form = new ProtectNoticeForm();
				PtJdbhJDztzd model = new PtJdbhJDztzd();
				Object[] data = (Object[]) it.next();
				model.setDztzdId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setDeviceId(Long.parseLong(data[1].toString()));
				if (data[2] != null)
					form.setEquName(data[2].toString());
				if (data[3] != null)
					model.setDzjssmId(Long.parseLong(data[3].toString()));
				if (data[4] != null)
					form.setJssmName(data[4].toString());
				if (data[5] != null)
					model.setDztzdCode(data[5].toString());
				if (data[6] != null)
				{
					model.setCtCode(data[6].toString());
				}else{
					model.setCtCode("");
				}
				if (data[7] != null)
				{
					model.setPtCode(data[7].toString());
				}else{
					model.setPtCode("");
				}
				if (data[8] != null)
				{
					model.setMemo(data[8].toString());
				}else{
					model.setMemo("");
				}
				if (data[9] != null)
					form.setEffectiveDate(data[9].toString());
				if (data[10] != null)
					model.setFillBy(data[10].toString());
				if (data[11] != null)
					form.setFillName(data[11].toString());
				if (data[12] != null)
					model.setUseStatus(data[12].toString());
				if (data[13] != null)
					model.setSaveBy(data[13].toString());
				if (data[14] != null)
					form.setSaveName(data[14].toString());
				if (data[15] != null)
					model.setSaveMark(data[15].toString());
				if (data[16] != null)
					form.setProtectedDevice(data[16].toString());
				form.setNotice(model);
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findNoticeDetailList(String enterpriseCode,String noticeId,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		
		if(noticeId == null || noticeId.equals(""))
		{
			String sql =  "select a.fixvalue_item_id,\n" +
				"       a.fixvalue_item_name,\n" + 
				"       b.protect_type_id,\n" + 
				"       b.protect_type_name\n" + 
				"  from PT_JDBH_C_DZXMWH a, PT_JDBH_C_BHLXWH b\n" + 
				" where b.protect_type_id = a.protect_type_id\n" + 
				"   and a.enterprise_code = '"+enterpriseCode+"'";

			String sqlCount = "select count(1) from PT_JDBH_C_DZXMWH a, PT_JDBH_C_BHLXWH b  where b.protect_type_id = a.protect_type_id and a.enterprise_code = '"+enterpriseCode+"'";

			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			//System.out.println("sss " + sql);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			if (list != null && list.size() > 0) {
				while(it.hasNext()){
					PtJdbhJDztzd model = new PtJdbhJDztzd();
					ProtectNoticeForm form = new ProtectNoticeForm();
					Object []data = (Object[])it.next();
					if(data[0] != null)
					form.setFixvalueItemId(Long.parseLong(data[0].toString()));
					if(data[1] != null)
						form.setFixvalueItemName(data[1].toString());
					if(data[2] != null)
						form.setProtectTypeId(Long.parseLong(data[2].toString()));
					if(data[3] != null)
						form.setProtectTypeName(data[3].toString());
					form.setNotice(model);
					arrlist.add(form);
				}
			}
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
		}else{
		String sql = "select a.dzdjb_id,\n" +
			"       a.fixvalue_item_id,\n" + 
			"       c.fixvalue_item_name,\n" + 
			"       c.protect_type_id,\n" + 
			"       b.protect_type_name,\n" + 
			"       a.whole_fixvalue,\n" + 
			"       a.memo,\n" + 
			"       d.dztzd_id\n" + 
			"  from PT_JDBH_J_DZDJB  a,\n" + 
			"       PT_JDBH_C_BHLXWH b,\n" + 
			"       PT_JDBH_C_DZXMWH c,\n" + 
			"       PT_JDBH_J_DZTZD  d\n" + 
			" where a.dztzd_id = d.dztzd_id\n" + 
			"   and a.fixvalue_item_id = c.fixvalue_item_id(+)\n" + 
			"   and c.protect_type_id = b.protect_type_id(+)\n" + 
			"   and d.enterprise_code = '"+enterpriseCode+"'";

		String sqlCount = 
			"select count(1)\n" +
			"  from PT_JDBH_J_DZDJB  a,\n" + 
			"       PT_JDBH_C_BHLXWH b,\n" + 
			"       PT_JDBH_C_DZXMWH c,\n" + 
			"       PT_JDBH_J_DZTZD  d\n" + 
			" where a.dztzd_id = d.dztzd_id\n" + 
			"   and a.fixvalue_item_id = c.fixvalue_item_id(+)\n" + 
			"   and c.protect_type_id = b.protect_type_id(+)\n" + 
			"   and d.enterprise_code = '"+enterpriseCode+"'";
		
		if(noticeId !=null && (!noticeId.equals("")))
		{
			sql = sql + " and d.dztzd_id =" + noticeId + " \n";
			sqlCount = sqlCount + " and d.dztzd_id =" + noticeId + " \n";
		}
		
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while (it.hasNext()) {
				ProtectNoticeForm form = new ProtectNoticeForm();
				PtJdbhJDztzd model = new PtJdbhJDztzd();
				Object []data = (Object[])it.next();
				form.setDzdjbId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
				form.setFixvalueItemId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					form.setFixvalueItemName(data[2].toString());
				if(data[3] != null)
					form.setProtectTypeId(Long.parseLong(data[3].toString()));
				if(data[4] != null)
					form.setProtectTypeName(data[4].toString());
				if(data[5] != null)
					form.setWholeFixvalue(data[5].toString());
				if(data[6] != null)
					form.setMemo(data[6].toString());
				if(data[7] != null)
					model.setDztzdId(Long.parseLong(data[7].toString()));
				form.setNotice(model);
				arrlist.add(form);
			}
		}
		
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		}
		return pg;
	}
}