package power.ejb.hr.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;

public class HrJEmpInfoFacadeRemoteTest {

	protected static HrJEmpInfoFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (HrJEmpInfoFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("HrJEmpInfoFacade");
	}

	@Test
	public final void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public final void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public final void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindUsersByName() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByProperty() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByChsName() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByEnName() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByEmpCode() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByDeptId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByRetrieveCode() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByNationId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByNativePlaceId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByPoliticsId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindBySex() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByIsWedded() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByArchivesId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByEmpStationId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByStationId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByStationLevel() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByGradation() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByEmpTypeId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByTechnologyTitlesTypeId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByTechnologyGradeId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByTypeOfWorkId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByIdentityCard() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByTimeCardId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByPayCardId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindBySocialInsuranceId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByMobilePhone() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByFamilyTel() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByQq() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByMsn() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByPostalcode() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByFamilyAddress() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByOfficeTel1() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByOfficeTel2() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByFax() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByEMail() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByInstancyMan() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByInstancyTel() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByGraduateSchool() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByEducationId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByDegreeId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindBySpeciality() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByIsVeteran() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByIsBorrow() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByIsRetired() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByEmpState() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByOrderBy() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByRecommendMan() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByAssistantManagerUnitsId() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByOneStrongSuit() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByMemo() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByCreateBy() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindByLastModifiyBy() {
		fail("Not yet implemented");
	}

	@Test
	public final void testFindAll() {
		fail("Not yet implemented");
	}

	@Test
	public final void testQueryByFuzzy() {
		PageObject result = remote.queryByFuzzy("hfdc", "4", "王", 0,18);
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
	@Test
	public final void testQueryByDeptTypeId(){
		PageObject pg=remote.queryByDeptTypeId("hfdc", Long.valueOf(1),"",1,18);
		System.out.print(pg.getTotalCount());
	}
}
