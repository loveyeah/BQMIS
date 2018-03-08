package power.ejb.manage.contract.business.test;

import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.manage.contract.business.ContractApprove;
import power.ejb.manage.contract.form.ConApproveBean;

public class ContractApproveTest {
	protected static ContractApprove remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(ContractApprove)Ejb3Factory.getInstance().getFacadeRemote("ContractApproveImp");
	}

	@Test
	public void testContractReport() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testContractApprove() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetSignReportData() {
		ConApproveBean  m = remote.getSignReportData(5l);
		System.out.println(m.getContractName()+m.getApproveList().get(0).getStepName());
	}

}
