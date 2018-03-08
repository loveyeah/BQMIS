package power.ejb.message;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for SysJMessageFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SysJMessageFacadeRemote {
	/**
	 * 增加消息
	 */
	public Long save(SysJMessage entity);

	/**
	 *删除
	 */
	public void delete(SysJMessage entity);

	/**
	 *更新消息
	 */
	public SysJMessage update(SysJMessage entity);

	public SysJMessage findById(Long id);
}