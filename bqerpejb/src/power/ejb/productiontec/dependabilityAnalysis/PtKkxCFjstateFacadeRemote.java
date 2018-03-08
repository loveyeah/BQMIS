package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.comm.TreeNode;

/**
 * 辅机设备状态维护
 * 
 * @author fyyang 091015
 */
@Remote
public interface PtKkxCFjstateFacadeRemote {
	
	public PtKkxCFjstate save(PtKkxCFjstate entity);

	
	public void delete(PtKkxCFjstate entity);

	
	public PtKkxCFjstate update(PtKkxCFjstate entity);

	public PtKkxCFjstate findById(Long id);
	
	public List<TreeNode> findStatTreeList(String node, String enterpriseCode);
}