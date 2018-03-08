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
	 * ������Ϣ
	 */
	public Long save(SysJMessage entity);

	/**
	 *ɾ��
	 */
	public void delete(SysJMessage entity);

	/**
	 *������Ϣ
	 */
	public SysJMessage update(SysJMessage entity);

	public SysJMessage findById(Long id);
}