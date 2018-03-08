package power.ejb.resource;

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
import power.ejb.resource.form.InvTempMaterialInfo;

/**
 * Facade for entity InvCTempMaterial.
 * 
 * @see power.ejb.resource.InvCTempMaterial
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvCTempMaterialFacade implements InvCTempMaterialFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public InvCTempMaterial save(InvCTempMaterial entity) {
		LogUtil.log("saving InvCTempMaterial instance", Level.INFO, null);
		try {

//			// 判断是否物料名称重复 liuyi 05/21/09 11：36
//			String sql = "select a.* from inv_c_temp_material a \n"
//				+ "where a.is_use='Y' \n";
//			List<InvCTempMaterial> list = bll.queryByNativeSQL(sql, InvCTempMaterial.class);
//						for(int i = 0; i <= list.size() - 1; i++)
//						{	// modify by 当物料名称和规格型号同时相同时不能保存 2009/07/13
//							if(list.get(i).getMaterialName().equals(entity.getMaterialName())&&list.get(i).getSpecNo().equals(entity.getSpecNo()) )
//							{
//								return null;
//							}
//						}
			//modify by fyyang 091106 判断物料是否重复
			if(this.checkMaterialSame(entity.getMaterialName(), entity.getSpecNo()))
					{
				          return null;
					}

			entity.setTempId(bll.getMaxId("inv_c_temp_material", "temp_id"));
			entity.setIsUse("Y");
			entity.setStatusId(1L);
			entity.setLastModifiedDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(Long id) {
		InvCTempMaterial entity = this.findById(id);
		if (entity != null) {
			entity.setIsUse("N");
			this.update(entity);
		}
	}

	public void deleteMulti(String ids) {
		String sql = "update inv_c_temp_material a\n"
				+ "   set a.is_use = 'N'\n" + " where a.temp_id in (" + ids
				+ ")\n" + "   and a.is_use = 'Y'";
		bll.exeNativeSQL(sql);
	}
	
	public InvCTempMaterial update(InvCTempMaterial entity) {
		LogUtil.log("updating InvCTempMaterial instance", Level.INFO, null);
		try {

			//modify by fyyang 091106 判断物料是否重复
			if(this.checkMaterialSame(entity.getMaterialName(), entity.getSpecNo(),entity.getTempId()))
					{
				          return null;
					}
			InvCTempMaterial result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvCTempMaterial findById(Long id) {
		LogUtil.log("finding InvCTempMaterial instance with id: " + id,
				Level.INFO, null);
		try {
			InvCTempMaterial instance = entityManager.find(
					InvCTempMaterial.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<InvCTempMaterial> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all InvCTempMaterial instances", Level.INFO, null);
		try {
			final String queryString = "select model from InvCTempMaterial model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findALLList(String strWhere,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql = "select a.temp_id,\n"
				+ "       a.material_name,\n"
				+ "       a.spec_no,\n"
				+ "       a.parameter,\n"
				+ "       a.stock_um_id,\n"
				+ "       a.default_whs_no,\n"
				+ "       a.default_location_no,\n"
				+ "       a.maertial_class_id,\n"
				+ "       b.class_name,\n"
				+ "       a.factory,\n"
				+ "       a.act_price,\n"
				+ "       to_char(a.check_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       a.check_by,\n"
				+ "       getworkername(a.check_by),\n"
				+ "       a.tel_no,\n"
				+ "       a.memo,\n"
				+ "       a.status_id,\n"
				+ "       a.material_no,\n"
				+ "       a.approve_by,\n"
				+ "       getworkername(a.approve_by),\n"
				+ "       to_char(a.approve_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       a.last_modified_by,\n"
				+ "       getworkername(a.last_modified_by),\n"
				+ "       to_char(a.last_modified_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+"        a.back_reason,\n"
				+"        (select  c.material_id from inv_c_material c where c.material_no = a.material_no and c.is_use = 'Y'  and rownum=1 ), \n"
				+"         (select d.unit_name from bp_c_measure_unit d where d.unit_id = a.stock_um_id and d.is_used = 'Y' and rownum=1) \n"
				+ "  from inv_c_temp_material a, inv_c_material_class b\n"
				+ " where a.maertial_class_id = b.maertial_class_id(+)\n";
		
		String sqlCount = "select count(1)\n"
				+ "  from inv_c_temp_material a, inv_c_material_class b\n"
				+ " where a.maertial_class_id = b.maertial_class_id(+)\n";

		if (strWhere != "") {
			   sql = sql + " and  " + strWhere;
			   sqlCount = sqlCount + " and  " + strWhere;
		    }
		sql = sql + " order by a.last_modified_date desc";
		sqlCount = sqlCount + " order by a.last_modified_date desc";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
		while(it.hasNext()){
			InvTempMaterialInfo info = new InvTempMaterialInfo();
			InvCTempMaterial model = new InvCTempMaterial();
			Object []data = (Object[])it.next();
			model.setTempId(Long.parseLong(data[0].toString()));
			if(data[1] != null)
				model.setMaterialName(data[1].toString());
			if(data[2] != null)
				model.setSpecNo(data[2].toString());
			if(data[3] != null)
				model.setParameter(data[3].toString());
			if(data[4] != null)
				model.setStockUmId(Long.parseLong(data[4].toString()));
			if(data[5] != null)
				model.setDefaultWhsNo(data[5].toString());
			if(data[6] != null)
				model.setDefaultLocationNo(data[6].toString());
			if(data[7] != null)
				model.setMaertialClassId(Long.parseLong(data[7].toString()));
			if(data[8] != null)
				info.setMaertialClassName(data[8].toString());
			if(data[9] != null)
				model.setFactory(data[9].toString());
			if(data[10] != null)
				model.setActPrice(Double.parseDouble(data[10].toString()));
			if(data[11] != null)
				info.setCheckDate(data[11].toString());
			if(data[12] != null)
				model.setCheckBy(data[12].toString());
			if(data[13] != null)
				info.setCheckName(data[13].toString());
			if(data[14] != null)
				model.setTelNo(data[14].toString());
			if(data[15] != null)
				model.setMemo(data[15].toString());
			if(data[16] != null)
				model.setStatusId(Long.parseLong(data[16].toString()));
			if(data[17] != null)
				model.setMaterialNo(data[17].toString());
			if(data[18] != null)
				model.setApproveBy(data[18].toString());
			if(data[19] != null)
				info.setApproveName(data[19].toString());
			if(data[20] != null)
				info.setApproveDate(data[20].toString());
			if(data[21] != null)
				model.setLastModifiedBy(data[21].toString());
			if(data[22] != null)
				info.setModifyName(data[22].toString());
			if(data[23] != null)
				info.setModifyDate(data[23].toString());
			//add by fyyang 090804 退回原因
			if(data[24]!=null)
				model.setBackReason(data[24].toString());
			if(data[25] != null)//add by drdu 091026
				info.setMaterialId(Long.parseLong(data[25].toString()));
			if(data[26] != null)//add by drdu 091026
				info.setUnitName(data[26].toString());
			info.setTemp(model);
			arrlist.add(info);
		 }
	  }
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}
	
	public PageObject findTempMaterialList(String workerCode,String enterpriseCode,String materialName,String status,final int... rowStartIdxAndCount)
	{
		String strWhere=" a.enterprise_code='"+enterpriseCode+"'  and a.is_use='Y' and b.enterprise_code(+) ='"+enterpriseCode+"'  and b.is_use(+) ='Y'\n";
		if (materialName != null && materialName.length() > 0) {
			strWhere += " and a.material_name like '%" + materialName + "%'";
		}
		if(status!=null&&!status.equals(""))
		{
			strWhere+=" and a.status_id="+status+"  \n";
		}
		if(workerCode != null && !workerCode.equals("") && !workerCode.equals("999999"))
			strWhere += " and a.check_by='" + workerCode + "' \n";
		return this.findALLList(strWhere, rowStartIdxAndCount);
	}
	
	public PageObject findApprovelList(String enterpriseCode,String materialName,final int... rowStartIdxAndCount)
	{
		String strWhere=" a.enterprise_code='"+enterpriseCode+"'  and a.is_use='Y' and b.enterprise_code(+) ='"+enterpriseCode+"'  and b.is_use(+) ='Y'\n";
		if (materialName != null && materialName.length() > 0) {
			strWhere += " and a.material_name like '%" + materialName + "%'";
		}
		strWhere += "and a.status_id = 2 \n";
		
		return this.findALLList(strWhere, rowStartIdxAndCount);
	}
	
	private boolean checkMaterialSame(String materialName,String specNo,Long ... tempId)
	{
		String sql=
			"select count(1)\n" +
			"  from (select a.material_name, a.spec_no\n" + 
			"          from inv_c_temp_material a\n" + 
			"         where a.is_use = 'Y'\n";
		if(tempId!=null&&tempId.length>0)
		{
			sql+=	"           and a.temp_id <> "+tempId[0]+"\n" ;
		}
		sql+="        union\n" + 
			"        select b.material_name, b.spec_no\n" + 
			"          from inv_c_material b\n" + 
			"         where b.is_use = 'Y') tt\n" + 
			" where tt.material_name = '"+materialName+"'\n" + 
			"   and tt.spec_no = '"+specNo+"'";
        Long count=Long.parseLong(bll.getSingal(sql).toString());
        if(count>0)
        {
        	return true;
        }
        return false;
	}
}