package power.ejb.workticket.test;

import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketSafetyKey;
import power.ejb.workticket.RunCWorkticketSafetyKeyFacadeRemote;

public class RunCWorkticketSafetyKeyFacadeRemoteTest {
	
	protected static RunCWorkticketSafetyKeyFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunCWorkticketSafetyKeyFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("RunCWorkticketSafetyKeyFacade");
	}

	@Test
	public void testSave() {
		RunCWorkticketSafetyKey m = new RunCWorkticketSafetyKey();
		m.setSafetyKeyName("安全");
		m.setKeyType("1");
		m.setEnterpriseCode("hfdc");
		m.setModifyBy("999999");
		m.setOrderBy(9L);
		m.setReverseKeyId(2L);
		m.setWorkticketTypeCode("1");
		try {
			RunCWorkticketSafetyKey mo = remote.save(m);
			System.out.println(mo.getSafetyKeyId());
		} catch (CodeRepeatException e) { 
			e.printStackTrace();
		}
	}

	@Test
	public void testDelete() { 
		remote.delete(1L);
	}

	@Test
	public void testDeleteMulti() {
		remote.deleteMulti("1,2");
	}

	@Test
	public void testUpdate() {
		RunCWorkticketSafetyKey m = remote.findById(1L);
		m.setSafetyKeyName("安全1");
		try {
			remote.update(m);
		} catch (CodeRepeatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testFindById() {
		//
	}

	@Test
	public void testFindAll() {
		PageObject po = remote.findAllWithComm("hfdc", "1","1");
		System.out.println(po.getList().size()+"**"+po.getTotalCount());
	}

}
