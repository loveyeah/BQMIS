package power.ejb.resource;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity InvCMaterialClass.
 * 
 * @see power.ejb.logistics.InvCMaterialClass
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvCMaterialClassFacade implements InvCMaterialClassFacadeRemote {
	// property constants
	public static final String PARENT_CLASS_NO = "parentClassNo";
	public static final String CLASS_NO = "classNo";
	public static final String CLASS_NAME = "className";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved InvCMaterialClass entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
    /**
     *增加一条记录
     * @param entity 要增加的记录
     */
	public void save(InvCMaterialClass entity) {
		LogUtil.log("saving InvCMaterialClass instance", Level.INFO, null);
		try {
				entity.setMaertialClassId(bll.getMaxId(
					"INV_C_MATERIAL_CLASS", "MAERTIAL_CLASS_ID"));
                // 设定修改时间
                entity.setLastModifiedDate(new Date());
                // 设定是否使用
                entity.setIsUse("Y");
                entity.setClassNo(this.createClassNo(entity.getParentClassNo()));
                // 保存
                entityManager.persist(entity);
                LogUtil.log("save successful", Level.INFO, null);
                return;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * Persist a previously saved InvCMaterialClass entity and return it or a
	 * copy of it to the sender. A copy of the InvCMaterialClass entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            InvCMaterialClass entity to update
	 * @return InvCMaterialClass the persisted InvCMaterialClass entity
	 *         instance, may not be the same
	 * @throws CodeRepeatException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCMaterialClass update(InvCMaterialClass entity) throws CodeRepeatException {
		LogUtil.log("updating InvCMaterialClass instance", Level.INFO, null);
		try {
			if (this.checkMaertialClassID(entity.getMaertialClassId(), entity
					.getEnterpriseCode())) {
				entity.setLastModifiedDate(new Date());
				entity.setIsUse("Y");
				entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("物料分类不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvCMaterialClass findById(Long id) {
		LogUtil.log("finding InvCMaterialClass instance with id: " + id,
				Level.INFO, null);
		try {
			InvCMaterialClass instance = entityManager.find(
					InvCMaterialClass.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvCMaterialClass entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCMaterialClass property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCMaterialClass> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvCMaterialClass> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvCMaterialClass instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvCMaterialClass model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据父编码查询物料分类信息
	 * 
	 * @param parentClassNo 父编码
	 * @param enterpriseCode 企业编码
	 * @return PageObject 物料分类列表
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByParentClassNo(String parentClassNo, String enterpriseCode) {
		LogUtil.log("finding InvCMaterialClass instance with parentClassNo: " + parentClassNo,
                Level.INFO, null);
        try {
        	PageObject result = new PageObject();
            // 查询sql
            String sql=
                "select * from inv_c_material_class t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.parent_class_no='" + parentClassNo +  "'\n" +
                "and t.is_use='Y' order by  t.Class_No asc";
            // 执行查询
            List<InvCMaterialClass> list = bll.queryByNativeSQL(sql, InvCMaterialClass.class);
            // 查询sql
            String sqlCount =
            	"select count(*) from inv_c_material_class t\n" +
	            "where  t.enterprise_code='"+enterpriseCode + "'\n" +
	            "and t.parent_class_no='" + parentClassNo +  "'\n" +
	            "and t.is_use='Y'";
            // 执行查询
            Long totalCount = Long
				.parseLong(bll.getSingal(sqlCount).toString());
            // 设置PageObject
            result.setList(list);
            result.setTotalCount(totalCount);
            // 返回
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
	}
	
	

	/**
	 * 根据编码查询物料分类信息
	 * 
	 * @param classNo 编码
	 * @param enterpriseCode 企业编码
	 * @return PageObject 物料分类列表
	 */
	public PageObject findByClassNo(String classNo, String enterpriseCode) {
		LogUtil.log("finding InvCMaterialClass instance with classNo: " + classNo,
                Level.INFO, null);
        try {
        	PageObject result = new PageObject();
            // 查询sql
            String sql=
                "select * from inv_c_material_class t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.class_no='" + classNo +  "'\n" +
                "and t.is_use='Y'";
            // 执行查询
            List<InvCMaterialClass> list = bll.queryByNativeSQL(sql, InvCMaterialClass.class);
            // 查询sql
            String sqlCount =
            	"select count(*) from inv_c_material_class t\n" +
	            "where  t.enterprise_code='"+enterpriseCode + "'\n" +
	            "and t.class_no='" + classNo +  "'\n" +
	            "and t.is_use='Y'";
            // 执行查询
            Long totalCount = Long
				.parseLong(bll.getSingal(sqlCount).toString());
            // 设置PageObject
            result.setList(list);
            result.setTotalCount(totalCount);
            // 返回
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
	}

	public List<InvCMaterialClass> findByClassName(Object className) {
		return findByProperty(CLASS_NAME, className);
	}

	public List<InvCMaterialClass> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<InvCMaterialClass> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<InvCMaterialClass> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all InvCMaterialClass entities.
	 * 
	 * @return List<InvCMaterialClass> all InvCMaterialClass entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvCMaterialClass> findAll() {
		LogUtil
				.log("finding all InvCMaterialClass instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from InvCMaterialClass model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 根据编码查找父编码和父名称
	 * 
	 * @param classNo 编码
	 * @param enterpriseCode 企业编码
	 */
	@SuppressWarnings("unchecked")
	public List findByParentCodeName(String classNo, String enterpriseCode) {
		LogUtil.log("finding parentClassNo and parentClassName with classNo: " + classNo,
                Level.INFO, null);
        try {
            // 查询sql
            String sql=
                "select * from inv_c_material_class t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.is_use='Y' and t.class_no = ( select parent_class_no from inv_c_material_class m\n" +
                "where  m.enterprise_code='"+enterpriseCode + "'\n" +
                "and m.class_no ='" + classNo +"'\n" + 
                "and m.is_use='Y')";
            // 执行查询
            List<InvCMaterialClass> list = bll.queryByNativeSQL(sql, InvCMaterialClass.class);
            // 返回
            return list;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
	}
	
	/**
	 * 删除物料数据
	 * 
	 * @param entity 物料分类
	 */
	public void delete(InvCMaterialClass entity) {
		LogUtil.log("deleting InvCMaterialClass instance", Level.INFO, null);
		try {
			if (this.checkMaertialClassID(entity.getMaertialClassId(), entity
					.getEnterpriseCode())) {
				//逻辑删除
				entity.setIsUse("N");
				entity.setLastModifiedDate(new Date());
				entityManager.merge(entity);
			} 
			LogUtil.log("delete successful", Level.INFO, null);
			return;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 根据编码查找孩子结点
	 * 
	 * @param classNo 编码
	 * @param enterpriseCode 企业编码
	 */
	@SuppressWarnings("unchecked")
	public List findChildNode(String classNo, String enterpriseCode) {
		LogUtil.log("finding child node with classNo: " + classNo,
                Level.INFO, null);
        try {
            // 查询sql
            String sql=
                "select * from inv_c_material_class t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.parent_class_no ='" + classNo +"'\n" + 
                "and t.is_use='Y' order by  t.Class_No asc";
            // 执行查询
            List<InvCMaterialClass> list = bll.queryByNativeSQL(sql, InvCMaterialClass.class);
            // 返回
            return list;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
	}
	
	/**
	 * 检查编码是否唯一
	 * 
	 * @param classNo 编码
	 * @param enterpriseCode 企业编码
	 */
	@SuppressWarnings("unchecked")
	public boolean checkClassNo(String classNo, String enterpriseCode) {
		LogUtil.log("checking ClassNo with classNo: " + classNo,
                Level.INFO, null);
        try {
        	boolean isSame = false;
            // 查询sql
            String sql=
                "select count(class_no) from inv_c_material_class t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.class_no ='" + classNo +"'\n" + 
                "and t.is_use='Y'";
    		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
    			isSame = true;
    		}
    		return isSame;
        } catch (RuntimeException re) {
            LogUtil.log("check failed", Level.SEVERE, re);
            throw re;
        }
	}
	
	/**
	 * check当前的流水号是否存在
	 * 
	 * @param maertialClassID 流水号
	 * @param enterpriseCode 企业编码
	 * @return 是否存在
	 */
    public boolean checkMaertialClassID(Long maertialClassID,
			String enterpriseCode) {
		boolean isSame = false;
		String sql = "select count(maertial_class_id) from inv_c_material_class t\n"
				+ "where t.maertial_class_id='" + maertialClassID + "'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.is_use='Y'";
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}
    
    /**
	 * 查找该当物料分类节点下所有的子节点
	 * 
	 * @param classNo 物料分类编码
	 * @param enterpriseCode 企业编码
	 * @return 返回该当物料分类节点下所有的子节点和自己
     * @throws Exception 
	 */
    @SuppressWarnings("unchecked")
	public List findAllChildrenNode(String classNo, String enterpriseCode){
    	LogUtil.log("finding child node with classNo: " + classNo,
                Level.INFO, null);
    	try{
    		// 查询sql
    		String sql = 
    			"SELECT * \n" + 
    			"FROM inv_c_material_class \n" +
    			"WHERE is_use = 'Y' AND enterprise_code = '" + enterpriseCode +"'\n" +
    			"AND maertial_class_id IN \n" +
    			"(SELECT maertial_class_id \n" +
    			"FROM inv_c_material_class START WITH parent_class_no = '" + classNo + "' and is_use = 'Y' \n" +
    			"CONNECT BY PRIOR class_no = parent_class_no AND IS_USE = 'Y'\n" +
    			"UNION \n" + 
    			" SELECT maertial_class_id \n" +
    			" FROM inv_c_material_class \n" +
    			" WHERE class_no = '" + classNo + "' and is_use = 'Y')";
    		// 执行查询
            List<InvCMaterialClass> list = bll.queryByNativeSQL(sql, InvCMaterialClass.class);
            // 返回
            return list;
    	}catch(Exception e)
    	{
    		LogUtil.log("find failed", Level.SEVERE, e);
    		return null;
    	}    	
    }
    
    /**    
     * 通过物料分类id查找其祖先编码
     * add by fyyang 090519
     * @return 祖父+父亲+本节点编码
     */
    public String getAllParentCode(Long classId)
    {
    	String parentCode="";
    	String sql=
    		"select  sys_connect_by_path(a.class_no,',')   path\n" +
    		"        from INV_C_MATERIAL_CLASS a\n" + 
    		"        where a.maertial_class_id="+classId+"\n" + 
    		"       start   with  a.parent_class_no='-1'\n" + 
    		"  connect by nocycle prior a.class_no=a.parent_class_no and a.is_use='Y' ";
    	
    	Object obj = bll.getSingal(sql);
		if(obj!=null)
		{
			String [] data=obj.toString().split(",");
			if(data.length>0)
			{
				if(data.length==2) parentCode=data[1]+"-00-00";
				if(data.length==3) parentCode=data[1]+"-"+data[2]+"-00";
				if(data.length>=4) parentCode=data[1]+"-"+data[2]+"-"+data[3];
			}
		}
		return parentCode;
    }
    
    
    //add by fyyang 091104
    private String createClassNo(String parentClassNo)
    {
    	String sql="";
    	if(parentClassNo.equals("-1")||parentClassNo.length()==1)
    	{
    		sql=
    			"select chr(ascii(substr(max(t.class_no),\n" +
    			"                        instr(max(t.class_no), '-', -1) + 1,\n" + 
    			"                        1)) +\n" + 
    			"           decode(ascii(substr(max(t.class_no),\n" + 
    			"                               instr(max(t.class_no), '-', -1) + 1,\n" + 
    			"                               1)),\n" + 
    			"                  57,\n" + 
    			"                  8,\n" + 
    			"                  90,\n" + 
    			"                  7,\n" + 
    			"                  1))\n" + 
    			"  from inv_c_material_class t\n" + 
    			" where t.parent_class_no = '"+parentClassNo+"'\n" + 
    			"   and t.is_use = 'Y'";
    		Object obj=bll.getSingal(sql);
    		if(obj==null||obj.equals(""))
    		{
    			if(parentClassNo.equals("-1")) return "1";
    			else return parentClassNo+"-1";
    				
    		}
    		else
    		{
    			if(parentClassNo.equals("-1")) return obj.toString().trim();
    			else return parentClassNo+"-"+obj.toString().trim();
    		}

    	}
    	else
    	{
    		sql=
    			"select case\n" +
    			"         when substr(max(t.class_no), instr(max(t.class_no), '-', -1) + 1, 2) = '99' then\n" + 
    			"          'A1'\n" + 
    			"         else\n" + 
    			"\n" + 
    			"          decode(instr('0123456789',\n" + 
    			"                       substr(max(t.class_no),\n" + 
    			"                              instr(max(t.class_no), '-', -1) + 1,\n" + 
    			"                              1)),\n" + 
    			"                 '0',\n" + 
    			"                 decode(substr(max(t.class_no),\n" + 
    			"                               instr(max(t.class_no), '-', -1) + 2,\n" + 
    			"                               1),\n" + 
    			"                        'Z',\n" + 
    			"                        chr(ascii(substr(max(t.class_no),\n" + 
    			"                                         instr(max(t.class_no), '-', -1) + 1,\n" + 
    			"                                         1)) +\n" + 
    			"                            decode(ascii(substr(max(t.class_no),\n" + 
    			"                                                instr(max(t.class_no), '-', -1) + 1,\n" + 
    			"                                                1)),\n" + 
    			"                                   57,\n" + 
    			"                                   8,\n" + 
    			"                                   90,\n" + 
    			"                                   7,\n" + 
    			"                                   1)) || '1',\n" + 
    			"                        substr(max(t.class_no),\n" + 
    			"                               instr(max(t.class_no), '-', -1) + 1,\n" + 
    			"                               1) ||\n" + 
    			"                        chr(ascii(substr(max(t.class_no),\n" + 
    			"                                         instr(max(t.class_no), '-', -1) + 2,\n" + 
    			"                                         1)) +\n" + 
    			"                            decode(ascii(substr(max(t.class_no),\n" + 
    			"                                                instr(max(t.class_no), '-', -1) + 2,\n" + 
    			"                                                1)),\n" + 
    			"                                   57,\n" + 
    			"                                   8,\n" + 
    			"                                   90,\n" + 
    			"                                   7,\n" + 
    			"                                   1))),\n" + 
    			"                 to_char(to_number(substr(max(t.class_no),\n" + 
    			"                                          instr(max(t.class_no), '-', -1) + 1,\n" + 
    			"                                          2)) + 1,\n" + 
    			"                         '00'))\n" + 
    			"\n" + 
    			"       end\n" + 
    			"\n" + 
    			"  from inv_c_material_class t\n" + 
    			" where t.parent_class_no = '"+parentClassNo+"'\n" + 
    			"   and t.is_use = 'Y'";
    		
                 Object obj=bll.getSingal(sql);
                 if(obj==null||obj.equals(""))
                 {
                	 return parentClassNo+"-00";
                 }
                 else
                 {
                	 return parentClassNo+"-"+obj.toString().trim();
                 }
    	}
    	
   
    }
    
    
}