package power.ejb.report;

import java.util.ArrayList;
import java.util.Iterator;
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
import power.ejb.report.form.JxlReportsRightForm;

/**
 * Facade for entity JxlReportsRight.
 * 
 * @see power.ejb.report.JxlReportsRight
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class JxlReportsRightFacade implements JxlReportsRightFacadeRemote {
	// property constants
	public static final String CODE = "code";
	public static final String WORKER_CODE = "workerCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved JxlReportsRight entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            JxlReportsRight entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(JxlReportsRight entity) {
		LogUtil.log("saving JxlReportsRight instance", Level.INFO, null);
		try {
			Long maxId=bll.getMaxId("JXL_REPORTS_RIGHT", "id");
			entity.setId(maxId);
			entityManager.persist(entity);
			entityManager.flush();
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	//add by wpzhu20100824
  public int  isHas(String workCode,String code)
  {
	  String sql="select count(1) from  JXL_REPORTS_RIGHT a\n" +
	          "where  a.worker_code='"+workCode+"'  and a.code='"+code+"'" ;
//	 System.out.println("the sql"+sql);
	 Object obj=bll.getSingal(sql);
	 int count=0;
	 if(obj!=null)
	 {
		    count=Integer.parseInt(obj.toString());
		 
	 }
     
     return  count;
    	   }
   public String getWorkName(String workCode)
   {
	   String workName="";
	   String sql="select  i.chs_name   from hr_j_emp_info   i\n " +
	   		     " where i.emp_code='"+workCode+"'\n" +
	   				" or i.new_emp_code='"+workCode+"'\n" +
	   		       "and i.is_use='Y' ";
//	   System.out.println("thesql"+sql);
	   Object obj=bll.getSingal(sql);
	   if(obj!=null)
	   {
		   workName=obj.toString();
	   }
	return workName;
	   
   }
	public void save(List<JxlReportsRight> addList) throws CodeRepeatException {
			if (addList != null && addList.size() > 0) {//modify by wpzhu  20100824
		    String workName="";
		    String  name="";
	        int count=0;
	        int i=0;
	        List<String>   lis = new ArrayList<String>(); 
	        for (JxlReportsRight entity : addList) {
				if(!lis.contains(entity.getWorkerCode()))
			    {			    
					lis.add(entity.getWorkerCode());
					
			    }else
			    {
			    	 workName+=this.getWorkName(entity.getWorkerCode());
			    	 workName+=",";
			    	i++;
			    	
			    }
			}
	    
	       
				for (JxlReportsRight entity : addList) {
					int j=0;
					 count+=this.isHas(entity.getWorkerCode(),entity.getCode());
					 j=this.isHas(entity.getWorkerCode(),entity.getCode());
					 
					 if(j>0)
					 {
					 name+=this.getWorkName(entity.getWorkerCode());
					 name+=",";
					 }
				
				}
				List<String>   list = new ArrayList<String>(); 
				String str=workName+name;
				String st[]=str.split(",");
				String a="";
				for(int k=0;k<st.length;k++)
				{
					if(!list.contains(st[k]))
				    {			    
						list.add(st[k]);
						 a+=st[k];
				    	 a+=",";
				    	
				    }
				}
				for (JxlReportsRight entity : addList) {
					if((count)==0&&(i==0))
					{ 
							this.save(entity);
					}else {  
						
						throw new CodeRepeatException(" "+a+" 重复，请重新核实！");
					}
	              
				}
				
			}
		}
	/**
	 * Delete a persistent JxlReportsRight entity.
	 * 
	 * @param entity
	 *            JxlReportsRight entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(JxlReportsRight entity) {
		LogUtil.log("deleting JxlReportsRight instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(JxlReportsRight.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	public boolean delete(String ids) {
		try {

			String[] temp1 = ids.split(",");

			for (String i : temp1) {
			
				this.delete(this.findById(Long.parseLong(i)));
			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved JxlReportsRight entity and return it or a copy
	 * of it to the sender. A copy of the JxlReportsRight entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            JxlReportsRight entity to update
	 * @return JxlReportsRight the persisted JxlReportsRight entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public JxlReportsRight update(JxlReportsRight entity) {
		LogUtil.log("updating JxlReportsRight instance", Level.INFO, null);
		try {
			JxlReportsRight result = entityManager.merge(entity);
			entityManager.flush();
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void update(List<JxlReportsRight> updateList) throws CodeRepeatException {

		try {//modify by wpzhu 20100824
			int count=0;
			 String workName="";
		        int flag=0;
		        int i=0;
		        List<String>   lis = new ArrayList<String>(); 
		        for (JxlReportsRight entity : updateList) {
					if(!lis.contains(entity.getWorkerCode()))
				    {			    
						lis.add(entity.getWorkerCode());
						
				    }else
				    {
				    	 workName+=this.getWorkName(entity.getWorkerCode());
				    	 workName+=",";
				    	i++;
				    	
				    }
				}
		        workName=workName.substring(0,workName.lastIndexOf(",")-1);
			for (JxlReportsRight entity : updateList) {
				flag+=this.isHas(entity.getWorkerCode(),entity.getCode());
				 
			
			}
			for (JxlReportsRight data : updateList) {
				if((flag)==0&&(i==0))
				{ 
						this.update(data);
				}else {  
					
					throw new CodeRepeatException(" "+workName+"有重复，请重新核实！");
				}
				
			}
		} catch (RuntimeException e) {
			throw e;
		}

	}

	public JxlReportsRight findById(Long id) {
		LogUtil.log("finding JxlReportsRight instance with id: " + id,
				Level.INFO, null);
		try {
			JxlReportsRight instance = entityManager.find(
					JxlReportsRight.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all JxlReportsRight entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the JxlReportsRight property to query
	 * @param value
	 *            the property value to match
	 * @return List<JxlReportsRight> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<JxlReportsRight> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding JxlReportsRight instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from JxlReportsRight model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<JxlReportsRight> findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List<JxlReportsRight> findByWorkerCode(Object workerCode) {
		return findByProperty(WORKER_CODE, workerCode);
	}

	/**
	 * Find all JxlReportsRight entities.
	 * 
	 * @return List<JxlReportsRight> all JxlReportsRight entities
	 */
	@SuppressWarnings("unchecked")
	public List<JxlReportsRight> findAll() {
		LogUtil.log("finding all JxlReportsRight instances", Level.INFO, null);
		try {
			final String queryString = "select model from JxlReportsRight model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public PageObject getAllUsers(String code,final int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();
			String sqlCount = "select count(1) from jxl_reports_right a where a.code = '"+code+"'";
	
			Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

			String sql = "select a.id, a.code, a.worker_code, getworkername(a.worker_code) wokerName " +
					"from jxl_reports_right a where a.code = '"+code+"'";

			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				JxlReportsRightForm form = new JxlReportsRightForm();
				JxlReportsRight model = new JxlReportsRight();
				if (data[0] != null)
					model.setId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setCode(data[1].toString());
				if (data[2] != null)
					model.setWorkerCode(data[2].toString());
				if (data[3] != null)
					form.setWorkerName(data[3].toString());
				form.setModel(model);
				arrlist.add(form);
			}
			pg.setList(arrlist);
			pg.setTotalCount(count);
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}