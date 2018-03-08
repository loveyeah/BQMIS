package power.ejb.workticket;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCWorkticketBusiStatusFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCWorkticketBusiStatusFacadeRemote {
	public List<RunCWorkticketBusiStatus> findAll() throws ParseException;
}