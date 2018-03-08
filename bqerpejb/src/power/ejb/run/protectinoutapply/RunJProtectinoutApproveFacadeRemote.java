package power.ejb.run.protectinoutapply;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for 保护投退申请.
 * 
 * @author fyyang
 */
@Remote
public interface RunJProtectinoutApproveFacadeRemote {
	
	public void save(RunJProtectinoutApprove entity);

	public void delete(RunJProtectinoutApprove entity);
	
	public RunJProtectinoutApprove update(RunJProtectinoutApprove entity);

	public RunJProtectinoutApprove findById(Long id);
}