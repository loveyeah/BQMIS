package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.comm.TreeNode;

/**
 * Remote interface for JsJKkxJzztwhFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface JsJKkxJzztwhFacadeRemote {
	
	public JsJKkxJzztwh  save(JsJKkxJzztwh entity);

	
	public void delete(JsJKkxJzztwh entity);

	
	public JsJKkxJzztwh update(JsJKkxJzztwh entity);

	public JsJKkxJzztwh findById(Long id);

	
	public List<JsJKkxJzztwh> findAll();
	public List<TreeNode> findStatTreeList(String node, String enterpriseCode);
}