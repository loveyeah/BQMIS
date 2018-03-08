package power.ejb.productiontec.dependabilityAnalysis.business;

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
import power.ejb.productiontec.dependabilityAnalysis.business.form.BlockForm;

/**
 * Facade for entity PtKkxBlockInfo.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.business.PtKkxBlockInfo
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtKkxBlockInfoFacade implements PtKkxBlockInfoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public PtKkxBlockInfo save(PtKkxBlockInfo entity) throws CodeRepeatException {
		LogUtil.log("saving PtKkxBlockInfo instance", Level.INFO, null);
		try {
			if(!this.checkCodeSame(entity.getBlockCode(),entity.getEnterpriseCode()))
			{
			entity.setBlockId(bll.getMaxId("PT_KKX_BLOCK_INFO", "block_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			}else{
				throw new CodeRepeatException("编码不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	 public void deleteMulti(String ids){
		String sql  = "update PT_KKX_BLOCK_INFO a\n" +
			"set a.is_use = 'N'\n" + 
			" where a.block_id in ("+ids+")";
		bll.exeNativeSQL(sql);
	}

	public PtKkxBlockInfo update(PtKkxBlockInfo entity) throws CodeRepeatException {
		LogUtil.log("updating PtKkxBlockInfo instance", Level.INFO, null);
		try {
			if(!this.checkCodeSame(entity.getBlockCode(), entity.getEnterpriseCode(), entity.getBlockId()))
			{
			PtKkxBlockInfo result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}else{
				throw new CodeRepeatException("编码不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtKkxBlockInfo findById(Long id) {
		LogUtil.log("finding PtKkxBlockInfo instance with id: " + id,
				Level.INFO, null);
		try {
			PtKkxBlockInfo instance = entityManager.find(PtKkxBlockInfo.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findBlockList(String enterpriseCode,String blockName,final int... rowStartIdxAndCount) 
	{
		PageObject pg = new PageObject();
		String sql = 
			"select a.block_id,\n" +
			"       a.block_code,\n" + 
			"       a.block_name,\n" + 
			"       a.holding_company,\n" + 
			"       a.holding_percent,\n" + 
			"       a.manage_company,\n" + 
			"       a.nameplate_capability,\n" + 
			"       a.attemper_company,\n" + 
			"       a.belong_grid,\n" + 
			"       a.block_type,\n" + 
			"       a.fuel_name,\n" + 
			"       a.production_date,\n" + 
			"       a.stat_date,\n" + 
			"       a.stop_stat_date,\n" + 
			"       (select b.manufacturer_name\n" + 
			"          from PT_KKX_BOILER_INFO b\n" + 
			"         where b.block_id = a.block_id\n" + 
			"           and b.is_use = 'Y') fatory1,\n" + 
			"       (select c.manufacturer_name\n" + 
			"          from PT_KKX_TURBINE_INFO c\n" + 
			"         where c.block_id = a.block_id\n" + 
			"           and c.is_use = 'Y') factory2,\n" + 
			"       (select d.manufacturer_name\n" + 
			"          from PT_KKX_GENERATOR_INFO d\n" + 
			"         where d.block_id = a.block_id\n" + 
			"           and d.is_use = 'Y') factory3,\n" + 
			"       (select e.manufacturer_name\n" + 
			"          from PT_KKX_TRANSFORMER_INFO e\n" + 
			"         where e.block_id = a.block_id\n" + 
			"           and e.is_use = 'Y') factory4\n" + 
			"  from PT_KKX_BLOCK_INFO a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'";

		String sqlCount = "select count(1)\n" 
				+ "  from PT_KKX_BLOCK_INFO a\n"
				+ " where a.is_use = 'Y'\n" 
				+ "   and a.enterprise_code = '"+ enterpriseCode + "'";
		String strWhere = "";
		if (blockName != null && !blockName.equals("")) {
			strWhere += " and a.block_name like '%" + blockName + "%'";
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
				BlockForm form = new BlockForm();
				Object[] data = (Object[]) it.next();
				form.setBlockId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					form.setBlockCode(data[1].toString());
				if(data[2] != null)
					form.setBlockName(data[2].toString());
				if(data[3] != null)
					form.setHoldingCompany(data[3].toString());
				if(data[4] != null)
					form.setHoldingPercent(Double.parseDouble(data[4].toString()));
				if(data[5] != null)
					form.setManageCompany(data[5].toString());
				if(data[6] != null)
					form.setNameplateCapability(Double.parseDouble(data[6].toString()));
				if(data[7] != null)
					form.setAttemperCompany(data[7].toString());
				if(data[8] != null)
					form.setBelongGrid(data[8].toString());
				if(data[9] != null)
					form.setBlockType(data[9].toString());
				if(data[10] != null)
					form.setFuelName(data[10].toString());
				if(data[11] != null)
					form.setProductionDate(data[11].toString());
				if(data[12] != null)
					form.setStatDate(data[12].toString());
				if(data[13] != null)
					form.setStopStatDate(data[13].toString());
				if(data[14] != null)
					form.setBoilerFactory(data[14].toString());
				if(data[15] != null)
					form.setTurbineFactory(data[15].toString());
				if(data[16] != null)
					form.setGeneratorFactory(data[16].toString());
				if(data[17] != null)
					form.setTransformerFactory(data[17].toString());
				arrlist.add(form);
			}
		}
		pg.setList(arrlist);
		return pg;
	}
	
	@SuppressWarnings("unused")
	private boolean checkCodeSame(String blockCode,String enterpriseCode, Long... blockId) {
		boolean isSame = false;
		String sql = "select count(*) from PT_KKX_BLOCK_INFO t\n"
				+ "where t.block_code = '" + blockCode + "' and t.enterprise_code='"+enterpriseCode+"'";

		if (blockId != null && blockId.length > 0) {
			sql += "  and t.block_id <> " + blockId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

}