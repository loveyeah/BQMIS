package power.ejb.comm; 
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
@Stateless
public class NativeSqlHelper implements NativeSqlHelperRemote {
	@PersistenceContext
	private EntityManager entityManager; 
	@SuppressWarnings("unchecked")
	public Object getSingal(String sql){ 
		Query query = entityManager.createNativeQuery(sql);
		List list = query.getResultList();  
		if(list!=null && list.size()>0){ 
			Object  o = (Object)list.get(0); 
			return o;
		}
		else 
		{
			return null;
		} 
	}
	@SuppressWarnings("unchecked")
	public Object getSingal(String sql,Object[] params){
		Query query = entityManager.createNativeQuery(sql);
		if(params != null && params.length>0){
			for(int i=0;i<params.length;i++)
			{
				query.setParameter(i+1, params[i]);
			}
		}
		List list = query.getResultList();  
		if(list!=null && list.size()>0){ 
			Object  o = (Object)list.get(0); 
			return o;
		}
		else 
		{
			return null;
		} 
	}
	@SuppressWarnings("unchecked")
	public  List queryByNativeSQL(String sql,final int... rowStartIdxAndCount){  
		Query query = entityManager.createNativeQuery(sql);
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
	}
	@SuppressWarnings("unchecked")
	public List queryByNativeSQL(String sql,Object[] params,final int... rowStartIdxAndCount){
		Query query = entityManager.createNativeQuery(sql);
		if(params != null && params.length>0){
			for(int i=0;i<params.length;i++)
			{
				query.setParameter(i+1, params[i]);
			}
		}
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
	}
	@SuppressWarnings("unchecked")
	public  List queryByNativeSQL(String sql,Class type,final int... rowStartIdxAndCount)
	{
		Query query = entityManager.createNativeQuery(sql,type);
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
	}
	@SuppressWarnings("unchecked")
	public  List queryByNativeSQL(String sql,Object[] params,Class type,final int... rowStartIdxAndCount)
	{
		Query query = entityManager.createNativeQuery(sql,type);
		if(params != null && params.length>0){
			for(int i=0;i<params.length;i++)
			{
				query.setParameter(i+1, params[i]);
			}
		} 
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
	}
	public int exeNativeSQL(String sql)
	{ 
		Query query = entityManager.createNativeQuery(sql);
		return query.executeUpdate(); 
	}
	public  int  exeNativeSQL(String sql,Object[] params){
		Query query = entityManager.createNativeQuery(sql);
		if(params != null && params.length>0){
			for(int i=0;i<params.length;i++)
			{
				query.setParameter(i+1, params[i]);
			}
		} 
		return query.executeUpdate(); 
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean exeNativeSQL(List<String> sqls){  
		boolean b = false; 
		for(Object sql : sqls){
			Query query = entityManager.createNativeQuery(sql.toString());
		    query.executeUpdate();
		}  
		entityManager.flush();
		b = true; 
		return b;
	}
	/**
	 * 取得表的最大值加1
	 */
	public Long getMaxId(String tabelName,String idColumnName) {
		String sql = "select case when  max("+idColumnName+") is  null then 1 else max("+idColumnName+")+1 end from "+tabelName;
		return Long.parseLong(this.getSingal(sql).toString());
	} 
	
}
