package power.ejb.manage.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.client.form.ConJClientsQualificationForm;

/**
 * Facade for entity ConJClientsQualification.
 * 
 * @see power.ejb.manage.client.ConJClientsQualification
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConJClientsQualificationFacade implements
		ConJClientsQualificationFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public ConJClientsQualification save(ConJClientsQualification entity) throws CodeRepeatException {
		LogUtil.log("saving ConJClientsQualification instance", Level.INFO,
				null);
		try {
			if(!this.checkName(entity.getAptitudeName(),entity.getEnterpriseCode())){
			entity.setQualificationId(bll.getMaxId("CON_J_CLIENTS_QUALIFICATION", "qualification_id"));
			entity.setLastModifiedDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			}
			else{
				throw new CodeRepeatException("资质名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void deleteMulti(String ids) {
		String sql = "delete CON_J_CLIENTS_QUALIFICATION b where b.qualification_id in(" + ids + ")";
		bll.exeNativeSQL(sql);
	}


	
	public ConJClientsQualification update(ConJClientsQualification entity) throws CodeRepeatException {
		LogUtil.log("updating ConJClientsQualification instance", Level.INFO,
				null);
		try {
			if(!this.checkName(entity.getAptitudeName(),entity.getEnterpriseCode(), entity.getQualificationId())){
			entity.setLastModifiedDate(new Date());
			ConJClientsQualification result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}else{
				throw new CodeRepeatException("资质名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJClientsQualification findById(Long id) {
		LogUtil.log("finding ConJClientsQualification instance with id: " + id,
				Level.INFO, null);
		try {
			ConJClientsQualification instance = entityManager.find(
					ConJClientsQualification.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 判断名称是否重复
	 * @param aptitudeName
	 * @param qualificationId
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean checkName(String aptitudeName,String enterpriseCode, Long... qualificationId) {
		boolean isSame = false;
		String sql = "select count(*) from CON_J_CLIENTS_QUALIFICATION t\n"
				+ "where t.aptitude_name = '" + aptitudeName + "' and t.enterprise_code='"+enterpriseCode+"'";

		if (qualificationId != null && qualificationId.length > 0) {
			sql += "  and t.qualification_id <> " + qualificationId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findClientsQualificationList(String enterpriseCode,
			String fuzzy,String clientId, final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		
		String sql = "select b.qualification_id,\n"
				+ "       (select c.client_name\n"
				+ "          from con_j_clients_info c\n"
				+ "         where c.cliend_id = b.cliend_id) client_name,\n"
				+ "       b.aptitude_name,\n"
				+ "       b.qualification_org,\n"
				+ "       b.send_paper_date,\n"
				+ "       b.begin_date,\n"
				+ "       b.end_date,\n" 
				+ "       b.memo,\n"
				+ "       getworkername(b.last_modified_by),\n"
				+ "       b.last_modified_date,\n" 
				+ "       b.cliend_id,\n"
				+ "       b.last_modified_by\n"
				+ "  from CON_J_CLIENTS_QUALIFICATION b\n"
				+ " where b.enterprise_code = '" + enterpriseCode + "'";
		
		String sqlCount = "select count(1)\n"
			+ "  from CON_J_CLIENTS_QUALIFICATION b\n"
			+ " where b.enterprise_code = '" + enterpriseCode + "'";
		
		String strWhere = "";
		if (fuzzy != null && !fuzzy.equals("")) {
			strWhere += " and b.aptitude_name like '%" + fuzzy + "%' or (select c.client_name from con_j_clients_info c where c.cliend_id = b.cliend_id) like '%" + fuzzy + "%' \n";
		}
		if(clientId != null && clientId !="")
		{
			strWhere += " and b.cliend_id = '" + clientId + "'";
		}
		sqlCount = sqlCount + strWhere;
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setTotalCount(totalCount);
		
		sql = sql + strWhere;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				ConJClientsQualificationForm form = new ConJClientsQualificationForm();
				Object[] data = (Object[]) it.next();
				form.setQualificationId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					form.setClientName(data[1].toString());
				if (data[2] != null)
					form.setAptitudeName(data[2].toString());
				if (data[3] != null)
					form.setQualificationOrg(data[3].toString());
				if (data[4] != null)
					form.setSendPaperDate(data[4].toString());
				if (data[5] != null)
					form.setBeginDate(data[5].toString());
				if (data[6] != null)
					form.setEndDate(data[6].toString());
				if (data[7] != null)
				{
					form.setMemo(data[7].toString());
				}else{
					form.setMemo("");
				}
				if (data[8] != null)
					form.setLastModifiedName(data[8].toString());
				if (data[9] != null)
					form.setLastModifiedDate(data[9].toString());
				if (data[10] != null)
					form.setCliendId(Long.parseLong(data[10].toString()));
				if (data[11] != null)
					form.setLastModifiedBy(data[11].toString());
				arrlist.add(form);

			}
		}
		pg.setList(arrlist);
		return pg;
	}

	public ConJClientsQualificationForm findQualificationById(Long qualificationId,String enterpriseCode)
	{
		String sql =  "select b.qualification_id,\n" +
			"       (select c.client_name\n" + 
			"          from con_j_clients_info c\n" + 
			"         where c.cliend_id = b.cliend_id) client_name,\n" + 
			"       b.aptitude_name,\n" + 
			"       b.qualification_org,\n" + 
			"       b.send_paper_date,\n" + 
			"       b.begin_date,\n" + 
			"       b.end_date,\n" + 
			"       b.memo,\n" + 
			"       getworkername(b.last_modified_by),\n" + 
			"       b.last_modified_date,\n" + 
			"       b.cliend_id,\n" + 
			"       b.last_modified_by\n" + 
			"  from CON_J_CLIENTS_QUALIFICATION b\n" + 
			" where b.qualification_id = "+qualificationId+"\n" + 
			"   and b.enterprise_code = '"+enterpriseCode+"'";
		
			
			Object[] data = (Object[]) bll.getSingal(sql);
			ConJClientsQualificationForm form = new ConJClientsQualificationForm();
			form.setQualificationId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				form.setClientName(data[1].toString());
			if (data[2] != null)
				form.setAptitudeName(data[2].toString());
			if (data[3] != null)
				form.setQualificationOrg(data[3].toString());
			if (data[4] != null)
				form.setSendPaperDate(data[4].toString());
			if (data[5] != null)
				form.setBeginDate(data[5].toString());
			if (data[6] != null)
				form.setEndDate(data[6].toString());
			if (data[7] != null)
				form.setMemo(data[7].toString());
			if (data[8] != null)
				form.setLastModifiedName(data[8].toString());
			if (data[9] != null)
				form.setLastModifiedDate(data[9].toString());
			if (data[10] != null)
				form.setCliendId(Long.parseLong(data[10].toString()));
			if (data[11] != null)
				form.setLastModifiedBy(data[11].toString());
		
		return form;
	}
}