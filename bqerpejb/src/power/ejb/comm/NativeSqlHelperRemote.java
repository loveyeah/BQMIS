package power.ejb.comm;
import java.util.List;
import javax.ejb.Remote;


/**
 * 原生语句操作类
 * @author wzhyan  
 */
@Remote 
public interface NativeSqlHelperRemote {
	/**
	 * 执行一条无参数的sql语句,返回第一行第一列的值
	 * @param sql 原生语句
	 * @return Object
	 */
	public Object getSingal(String sql);
	/**
	 * 取得
	 * @param sql
	 * @return
	 */
	public Long getMaxId(String tabelName,String idColumnName);
	/**
	 * 执行一条有参数的sql语句,返回第一行第一列的值
	 * @param sql     原生语句
	 * @param params  参数值数组
	 * @return Object
	 */
	public Object getSingal(String sql, Object[] params);
	/**
	 * 查询一条无参数sql语句,返回查询结果
	 * @param sql 原生语句
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List queryByNativeSQL(String sql, final int... rowStartIdxAndCount);
	/**
	 *  查询一条有参数sql语句,返回查询结果
	 * @param sql 原生语句
	 * @param params  参数值数组
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return List 
	 */
	@SuppressWarnings("unchecked")
	public List queryByNativeSQL(String sql, Object[] params,
			final int... rowStartIdxAndCount);
	/**
	 * 查询一条无参数sql语句,返回查询结果
	 * @param sql 原生语句
	 * @param type 实体Bean类型
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List queryByNativeSQL(String sql, Class type,
			final int... rowStartIdxAndCount);
	/**
	 * 查询一条有参数sql语句,返回查询结果
	 * @param sql 原生语句
	 * @param params  参数值数组
	 * @param type 实体Bean类型
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List queryByNativeSQL(String sql, Object[] params, Class type,
			final int... rowStartIdxAndCount);
	/**
	 * 执行一条无参数sql语句,返回影响的行数
	 * @param sql 原生语句
	 * @return int
	 */
	public int exeNativeSQL(String sql);
	/**
	 * 执行一条有参数sql语句,返回影响的行数
	 * @param sql 原生语句 
	 * @param params 参数值数组
	 * @return int
	 */
	public int exeNativeSQL(String sql, Object[] params);
	/**
	 * 执行一组有参数sql语句,返回影响的行数
	 * @param sqls 原生语句集合
	 * @return boolean
	 */
	public boolean exeNativeSQL(List<String> sqls);
	public String subStr(String str,String split,int itervel,String columnName);
	
}
