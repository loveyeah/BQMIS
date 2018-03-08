package power.ejb.manage.client;

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

/**
 * Facade for entity ConCAppraiseItem.
 * 
 * @see power.ejb.manage.client.ConCAppraiseItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConCAppraiseItemFacade implements ConCAppraiseItemFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;


	public ConCAppraiseItem save(ConCAppraiseItem entity) throws CodeRepeatException {
		LogUtil.log("saving ConCAppraiseItem instance", Level.INFO, null);
		try {
			if(!this.checkName(entity.getAppraiseItem(), entity.getEnterpriseCode())){
				if(!this.checkDisplayNo(entity.getDisplayNo(), entity.getEnterpriseCode())){
				entity.setEventId(bll.getMaxId("CON_C_APPRAISE_ITEM", "event_id"));
				entity.setIsUse("N");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
				}
				else{
					throw new CodeRepeatException("显示顺序不能重复！");
				}
			}else{
				throw new CodeRepeatException("评价项目名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids) {
		String sql = "delete CON_C_APPRAISE_ITEM b where b.event_id in(" + ids
				+ ")";
		bll.exeNativeSQL(sql);
	}

	public void confirmMulti()
	{
		String sql = "update CON_C_APPRAISE_ITEM b set b.is_use = 'Y' where b.is_use = 'N'";
		bll.exeNativeSQL(sql);
	}
	public ConCAppraiseItem update(ConCAppraiseItem entity) throws CodeRepeatException {
		LogUtil.log("updating ConCAppraiseItem instance", Level.INFO, null);
		try {
			if(!this.checkName(entity.getAppraiseItem(), entity.getEnterpriseCode(), entity.getEventId())){
				if(!this.checkDisplayNo(entity.getDisplayNo(), entity.getEnterpriseCode(), entity.getEventId())){
			ConCAppraiseItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
				}else{
					throw new CodeRepeatException("显示顺序不能重复！");
				}
			}
			else{
				throw new CodeRepeatException("评价项目名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConCAppraiseItem findById(Long id) {
		LogUtil.log("finding ConCAppraiseItem instance with id: " + id,
				Level.INFO, null);
		try {
			ConCAppraiseItem instance = entityManager.find(
					ConCAppraiseItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String appraiseItem, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			if (appraiseItem == null || "".equals(appraiseItem)) {
				appraiseItem = "%";
			}
			String sqlCount = "select count(1) from CON_C_APPRAISE_ITEM t where t.appraise_item like '%"
					+ appraiseItem
					+ "%'  and t.enterprise_code='"
					+ enterpriseCode + "' order by t.display_no";
			Long totalCount = Long.valueOf(bll.getSingal(sqlCount).toString());
			result.setTotalCount(totalCount);

			String sql = "select *\n" +
				"  from (select *\n" + 
				"          from CON_C_APPRAISE_ITEM t\n" + 
				"         where t.appraise_item like '%"+appraiseItem+"%'\n" + 
				"           and t.enterprise_code = '"+enterpriseCode+"'\n" + 
				"        union\n" + 
				"        SELECT 0, '合计', sum(t.appraise_mark), '', 9999, '', ''\n" + 
				"          from CON_C_APPRAISE_ITEM t\n" + 
				"         where t.appraise_item like '%"+appraiseItem+"%'\n" + 
				"           and t.enterprise_code = '"+enterpriseCode+"') tt\n" + 
				" order by tt.display_no";

			List<ConCAppraiseItem> list = bll.queryByNativeSQL(sql,
					ConCAppraiseItem.class, rowStartIdxAndCount);
			result.setList(list);

			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 判断名称是否重复
	 * @param appraiseItem
	 * @param enterpriseCode
	 * @param eventId
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean checkName(String appraiseItem,String enterpriseCode, Long... eventId) {
		boolean isSame = false;
		String sql = "select count(*) from CON_C_APPRAISE_ITEM t\n"
				+ "where t.appraise_item = '" + appraiseItem + "' and t.enterprise_code='"+enterpriseCode+"'";

		if (eventId != null && eventId.length > 0) {
			sql += "  and t.event_id <> " + eventId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}
	
	/**
	 * 判断显示顺序是否重复
	 * @param displayNo
	 * @param enterpriseCode
	 * @param eventId
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean checkDisplayNo(Long displayNo,String enterpriseCode, Long... eventId) {
		boolean isSame = false;
		String sql = "select count(*) from CON_C_APPRAISE_ITEM t\n"
				+ "where t.display_no = '" + displayNo + "' and t.enterprise_code='"+enterpriseCode+"'";

		if (eventId != null && eventId.length > 0) {
			sql += "  and t.event_id <> " + eventId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}
}