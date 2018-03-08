package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for KksCElectricGeneratorFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface KksCElectricGeneratorFacadeRemote {
	
	public void save(KksCElectricGenerator entity);

	
	public void delete(KksCElectricGenerator entity);

	
	public KksCElectricGenerator update(KksCElectricGenerator entity);

	public KksCElectricGenerator findById(String id);

	
	public List<KksCElectricGenerator> findAll(int... rowStartIdxAndCount);
}