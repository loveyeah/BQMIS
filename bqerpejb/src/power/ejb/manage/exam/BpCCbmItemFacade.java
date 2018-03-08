package power.ejb.manage.exam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity BpCCbmItem.
 * 
 * @see power.ejb.manage.exam.BpCCbmItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCCbmItemFacade implements BpCCbmItemFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected static NativeSqlHelperRemote bll;

	public boolean save(BpCCbmItem entity) {
		try {
			entity.setItemId(bll.getMaxId("bp_c_cbm_item", "item_id"));
			entity.setItemCode(getItemcode(entity.getItemName()));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean delete(BpCCbmItem entity) {
		try {
			entity = entityManager.getReference(BpCCbmItem.class, entity
					.getItemId());
			entityManager.remove(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean delete(String ids) {
		try {
			String sql = "UPDATE BP_C_CBM_ITEM t\n" + "   SET t.is_use = 'N'\n"
					+ " WHERE t.ITEM_ID IN (" + ids + ")";
			bll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean update(BpCCbmItem entity) {
		try {
			entity.setItemCode(getItemcode(entity.getItemName()));
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean namePermitted(String name) {
		try {
			String sql = "SELECT COUNT(1)\n" + "  FROM bp_c_cbm_item t\n"
					+ " WHERE t.item_name = '" + name + "'";
			Long count = Long.parseLong(bll.getSingal(sql).toString());
			if (count > 0)
				return true;
			else
				return false;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean namePermitted(String name, Long id) {
		try {
			String sql = "SELECT COUNT(1)\n" + "  FROM bp_c_cbm_item t\n"
					+ " WHERE t.item_name = '" + name + "'\n"
					+ "   AND t.item_id <> " + id + "";
			Long count = Long.parseLong(bll.getSingal(sql).toString());
			if (count > 0)
				return true;
			else
				return false;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCCbmItem entities.
	 * 
	 * @return List<BpCCbmItem> all BpCCbmItem entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllForTopic(String enterpriseCode) {
		try {
			PageObject obj = new PageObject();
			String sql = "SELECT a.item_id,\n" + "		a.item_code,\n"
					+ "       a.item_name,\n" + "       a.alias,\n"
					+ "       a.data_type,\n" + "       a.display_no\n"
					+ "  FROM bp_c_cbm_item     a\n"
					+ " WHERE a.ENTERPRISE_CODE = '" + enterpriseCode + "'";
			List list = bll.queryByNativeSQL(sql);
			List arraylist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				BpCCbmItem model = new BpCCbmItem();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					model.setItemId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setItemCode(data[1].toString());
				if (data[2] != null)
					model.setItemName(data[2].toString());
				if (data[3] != null)
					model.setAlias(data[3].toString());
				if (data[4] != null)
					model.setDataType(data[4].toString());
				if (data[5] != null)
					model.setDisplayNo(Long.parseLong(data[5].toString()));
				arraylist.add(model);
			}
			obj.setList(arraylist);
			return obj;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<TreeNode> findStatTreeList(String node, String enterpriseCode,
			String searchKey) {
		List<TreeNode> res = null;
		searchKey = (searchKey == null || ("").equals(searchKey)) ? ""
				: searchKey;
		try {
			String sql = "SELECT t.item_id,\n" + "       t.item_code,\n"
					+ "       t.item_name,\n" + "       t.is_item,\n"
					+ "       (SELECT COUNT(1)\n"
					+ "          FROM bp_c_cbm_item a\n"
					+ "         WHERE a.f_item_id = t.item_id) COUNT\n"
					+ "  FROM bp_c_cbm_item t\n" + " WHERE t.f_item_id = "
					+ node + "\n" + "   AND t.ENTERPRISE_CODE = '"
					+ enterpriseCode + "'\n" + "   AND (t.item_code LIKE '%"
					+ searchKey + "%' OR t.item_name LIKE '%" + searchKey
					+ "%')\n" 
					+" and t.is_use='Y'" + " ORDER BY t.display_no";
			List<Object[]> list = bll.queryByNativeSQL(sql);
			if (list != null && list.size() > 0) {
				res = new ArrayList<TreeNode>();
				for (Object[] o : list) {
					TreeNode n = new TreeNode();
					n.setId(o[0].toString());
					n.setCode(o[1].toString());
					if (o[2] != null)
						n.setText(o[2].toString());
					String isItem = "N";
					if (o[3] != null)
						isItem = o[3].toString();
					n.setDescription(isItem);
					if (o[4] != null)
						n.setLeaf(o[4].toString().equals("0") ? true : false);
					String icon = "";
					if (isItem.equals("N")) {
						icon = "box";
					} else {
						if (("Y").equals(isItem))
							icon = n.getLeaf() ? "my-iconCls" : "my-iconCls";
						else
							icon = n.getLeaf() ? "file" : "folder";
					}
					n.setIconCls(icon);
					res.add(n);
				}
			}
			return res;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAllItemToSelect(String searchKey,
			String enterpriseCode, int... rowStartIdxAndCount) throws Exception {
		try {
			PageObject obj = new PageObject();
			searchKey = (searchKey == null || ("").equals(searchKey)) ? ""
					: searchKey;
			String sql = "SELECT t.item_id,\n" + "       t.item_code,\n"
					+ "       t.item_name,\n" + "       t.is_item\n"
					+ "  FROM bp_c_cbm_item t\n"
					+ " WHERE t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   AND (t.item_code LIKE '%" + searchKey
					+ "%' OR t.item_name LIKE '%" + searchKey + "%')\n"
					+ " ORDER BY t.display_no";
			String sqlcount = "SELECT COUNT(1)\n" + "  FROM bp_c_cbm_item t\n"
					+ " WHERE t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   AND (t.item_code LIKE '%" + searchKey
					+ "%'  OR t.item_name LIKE '%" + searchKey + "%')";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Long count = Long.parseLong(bll.getSingal(sqlcount).toString());
			List arraylist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				BpCCbmItem model = new BpCCbmItem();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					model.setItemId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setItemCode(data[1].toString());
				if (data[2] != null)
					model.setItemName(data[2].toString());
				if (data[3] != null)
					model.setIsItem(data[3].toString());
				arraylist.add(model);
			}
			obj.setList(arraylist);
			obj.setTotalCount(count);
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}

	public BpCCbmItem findById(Long id) {
		try {
			BpCCbmItem instance = entityManager.find(BpCCbmItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Object findItemInfo(String node) {
		String sql = "SELECT t.item_code,\n" + "       t.item_name,\n"
				+ "       t.is_item,\n" + "       (SELECT a.item_name\n"
				+ "          FROM bp_c_cbm_item a\n"
				+ "         WHERE a.item_id = t.f_item_id) parentItemName,\n"
				+ "       t.unit_id,\n" + "       (SELECT a.unit_name\n"
				+ "          FROM bp_c_measure_unit a\n"
				+ "         WHERE a.unit_id = t.unit_id\n"
				+ "           AND a.is_used = 'Y') unitName,\n"
				+ "       t.alias,\n" + "       t.item_id,\n"
//				+ "       t.if_branch_item,\n" 
				+ "       t.data_type,\n"
				+ "       t.account_order,\n" + "       t.display_no,\n"
				+ "       t.is_use,\n" + "       t.enterprise_code\n"
				// add by liuyi 091207 增加主题
				+ ", t.topic_id,(select b.topic_name from bp_c_cbm_topic b where b.topic_id=t.topic_id) \n"
				+ "  FROM bp_c_cbm_item t\n" + " WHERE t.item_code = '" + node
				+ "'";
		return bll.getSingal(sql);
	}

	/*
	 * private String getItemcode(String name) { String sql = "SELECT
	 * lower(FUN_SPELLCODE('" + name + "')) ||\n" + " decode((SELECT COUNT(1)\n" + "
	 * FROM bp_c_item t\n" + " WHERE REGEXP_LIKE(t.item_code, ('^' ||\n" + "
	 * lower(FUN_SPELLCODE('" + name + "')) ||\n" + " '[0-9]*$'))), 0, '',
	 * (SELECT COUNT(1)\n" + " FROM bp_c_item t\n" + " WHERE
	 * REGEXP_LIKE(t.item_code, ('^' ||\n" + " lower(FUN_SPELLCODE('" + name +
	 * "')) ||\n" + " '[0-9]*$'))), (SELECT COUNT(1)\n" + " FROM bp_c_item t\n" + "
	 * WHERE REGEXP_LIKE(t.item_code, ('^' ||\n" + " lower(FUN_SPELLCODE('" +
	 * name + "')) ||\n" + " '[0-9]*$'))))\n" + " FROM dual"; String str =
	 * bll.getSingal(sql).toString(); return str; }
	 */
	public static String getItemcode(String a) {
		// 汉字区位码
		int li_SecPosValue[] = { 1601, 1637, 1833, 2078, 2274, 2302, 2433,
				2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
				4086, 4390, 4558, 4684, 4925, 5249, 5590 };
		// 存放国标一级汉字不同读音的起始区位码对应读音
		char lc_FirstLetter[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'W', 'X',
				'Y', 'Z' };
		// 二级字库偏移量
		int ioffset = 0;
		// //存放所有国标二级汉字读音
		java.lang.String ls_SecondSecTable = "CJWGNSPGCGNE[Y[BTYYZDXYKYGT[JNNJQMBSGZSCYJSYY"
				+ "[PGKBZGY[YWJKGKLJYWKPJQHY[W[DZLSGMRYPYWWCCKZNKYYGTTNJJNYKKZYTCJNMCYLQLYPYQFQRPZSLWBTGKJFYXJWZLTBNCXJJJJTXDTTSQZYCDXXHGCK"
				+ "[PHFFSS[YBGXLPPBYLL[HLXS[ZM[JHSOJNGHDZQYKLGJHSGQZHXQGKEZZWYSCSCJXYEYXADZPMDSSMZJZQJYZC[J"
				+ "[WQJBYZPXGZNZCPWHKXHQKMWFBPBYDTJZZKQHYLYGXFPTYJYYZPSZLFCHMQSHGMXXSXJ["
				+ "[DCSBBQBEFSJYHXWGZKPYLQBGLDLCCTNMAYDDKSSNGYCSGXLYZAYBNPTSDKDYLHGYMYLCXPY"
				+ "[JNDQJWXQXFYYFJLEJPZRXCCQWQQSBNKYMGPLBMJRQCFLNYMYQMSQYRBCJTHZTQFRXQHXMJJCJLXQGJMSHZKBSWYEMYLTXFSYDSWLYCJQXSJNQBSCTYHBFTDCYZDJWY"
				+ "GHQFRXWCKQKXEBPTLPXJZSRMEBWHJLBJSLYYSMDXLCLQKXLHXJRZJMFQHXHWYWSBHTRXXGLHQHFNM[YKLDYXZPYLGG[MTCFPAJJZYLJTYANJGBJPLQGDZYQY"
				+ "AXBKYSECJSZNSLYZHSXLZCGHPXZHZNYTDSBCJKDLZAYFMYDLEBBGQYZKXGLDNDNYSKJSHDLYXBCGHXYPKDJMMZNGMMCLGWZSZXZJFZNMLZZTHCSYDBDLLSCDD"
				+ "NLKJYKJSYCJLKWHQASDKNHCSGANHDAASHTCPLCPQYBSDMPJLPZJOQLCDHJJYSPRCHN[NNLHLYYQYHWZPTCZGWWMZFFJQQQQYXACLBHKDJXDGMMYDJXZLLSYGX"
				+ "GKJRYWZWYCLZMSSJZLDBYD[FCXYHLXCHYZJQ[[QAGMNYXPFRKSSBJLYXYSYGLNSCMHZWWMNZJJLXXHCHSY[[TTXRYCYXBYHCSMXJSZNPWGPXXTAYBGAJCXLY"
				+ "[DCCWZOCWKCCSBNHCPDYZNFCYYTYCKXKYBSQKKYTQQXFCWCHCYKELZQBSQYJQCCLMTHSYWHMKTLKJLYCXWHEQQHTQH[PQ"
				+ "[QSCFYMNDMGBWHWLGSLLYSDLMLXPTHMJHWLJZYHZJXHTXJLHXRSWLWZJCBXMHZQXSDZPMGFCSGLSXYMJSHXPJXWMYQKSMYPLRTHBXFTPMHYXLCHLHLZY"
				+ "LXGSSSSTCLSLDCLRPBHZHXYYFHB[GDMYCNQQWLQHJJ[YWJZYEJJDHPBLQXTQKWHLCHQXAGTLXLJXMSL[HTZKZJECXJCJNMFBY[SFYWYBJZGNYSDZSQYRSLJ"
				+ "PCLPWXSDWEJBJCBCNAYTWGMPAPCLYQPCLZXSBNMSGGFNZJJBZSFZYNDXHPLQKZCZWALSBCCJX[YZGWKYPSGXFZFCDKHJGXDLQFSGDSLQWZKXTMHSBGZMJZRGLYJ"
				+ "BPMLMSXLZJQQHZYJCZYDJWBMYKLDDPMJEGXYHYLXHLQYQHKYCWCJMYYXNATJHYCCXZPCQLBZWWYTWBQCMLPMYRJCCCXFPZNZZLJPLXXYZTZLGDLDCKLYRZZGQTG"
				+ "JHHGJLJAXFGFJZSLCFDQZLCLGJDJCSNZLLJPJQDCCLCJXMYZFTSXGCGSBRZXJQQCTZHGYQTJQQLZXJYLYLBCYAMCSTYLPDJBYREGKLZYZHLYSZQLZNWCZCLLWJQ"
				+ "JJJKDGJZOLBBZPPGLGHTGZXYGHZMYCNQSYCYHBHGXKAMTXYXNBSKYZZGJZLQJDFCJXDYGJQJJPMGWGJJJPKQSBGBMMCJSSCLPQPDXCDYYKY[CJDDYYGYWRHJRTGZ"
				+ "NYQLDKLJSZZGZQZJGDYKSHPZMTLCPWNJAFYZDJCNMWESCYGLBTZCGMSSLLYXQSXSBSJSBBSGGHFJLYPMZJNLYYWDQSHZXTYYWHMZYHYWDBXBTLMSYYYFSXJC[DXX"
				+ "LHJHF[SXZQHFZMZCZTQCXZXRTTDJHNNYZQQMNQDMMG[YDXMJGDHCDYZBFFALLZTDLTFXMXQZDNGWQDBDCZJDXBZGSQQDDJCMBKZFFXMKDMDSYYSZCMLJDSYNSBRS"
				+ "KMKMPCKLGDBQTFZSWTFGGLYPLLJZHGJ[GYPZLTCSMCNBTJBQFKTHBYZGKPBBYMTDSSXTBNPDKLEYCJNYDDYKZDDHQHSDZSCTARLLTKZLGECLLKJLQJAQNBDKKGHP"
				+ "JTZQKSECSHALQFMMGJNLYJBBTMLYZXDCJPLDLPCQDHZYCBZSCZBZMSLJFLKRZJSNFRGJHXPDHYJYBZGDLQCSEZGXLBLGYXTWMABCHECMWYJYZLLJJYHLG[DJLSLY"
				+ "GKDZPZXJYYZLWCXSZFGWYYDLYHCLJSCMBJHBLYZLYCBLYDPDQYSXQZBYTDKYXJY[CNRJMPDJGKLCLJBCTBJDDBBLBLCZQRPPXJCJLZCSHLTOLJNMDDDLNGKAQHQH"
				+ "JGYKHEZNMSHRP[QQJCHGMFPRXHJGDYCHGHLYRZQLCYQJNZSQTKQJYMSZSWLCFQQQXYFGGYPTQWLMCRNFKKFSYYLQBMQAMMMYXCTPSHCPTXXZZSMPHPSHMCLMLDQF"
				+ "YQXSZYYDYJZZHQPDSZGLSTJBCKBXYQZJSGPSXQZQZRQTBDKYXZKHHGFLBCSMDLDGDZDBLZYYCXNNCSYBZBFGLZZXSWMSCCMQNJQSBDQSJTXXMBLTXZCLZSHZCXRQ"
				+ "JGJYLXZFJPHYMZQQYDFQJJLZZNZJCDGZYGCTXMZYSCTLKPHTXHTLBJXJLXSCDQXCBBTJFQZFSLTJBTKQBXXJJLJCHCZDBZJDCZJDCPRNPQCJPFCZLCLZXZDMXMPH"
				+ "JSGZGSZZQLYLWTJPFSYASMCJBTZKYCWMYTCSJJLJCQLWZMALBXYFBPNLSFHTGJWEJJXXGLLJSTGSHJQLZFKCGNNNSZFDEQFHBSAQTGYLBXMMYGSZLDYDQMJJRGBJ"
				+ "TKGDHGKBLQKBDMBYLXWCXYTTYBKMRTJZXQJBHLMHMJJZMQASLDCYXYQDLQCAFYWYXQHZ";
		java.lang.String sreturn = "";
		for (int j = 0; j < a.length(); j++) {
			String stemp = a.substring(j, j + 1);
			byte[] by = stemp.getBytes();
			if (("#").equals(stemp)) {
				sreturn = sreturn + stemp;
			} else if (by.length == 1) {
				sreturn = sreturn + stemp;
			} else {
				int ia = 96 + (int) by[0]; // 区码
				int ib = 96 + (int) by[1]; // 位码
				int in = ia * 100 + ib;
				if (in > 1600 && in < 5590) {
					for (int i = 0; i < 24; i++) {
						if (in < li_SecPosValue[i]) {
							sreturn = sreturn + lc_FirstLetter[i - 1];
							break;
						}
					}
				} else {
					ioffset = (ia - 56) * 94 + ib - 1;
					if (ioffset >= 0 && ioffset <= 3007) {
						sreturn = sreturn
								+ ls_SecondSecTable.substring(ioffset,
										ioffset + 1);
					}
				}
			}
			sreturn = sreturn.toLowerCase();
		}
		String sql = "SELECT COUNT(1)\n" + "  FROM bp_c_cbm_item t\n"
				+ " WHERE t.ITEM_CODE LIKE '" + sreturn + "%'";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0) {
			sreturn = sreturn + count.toString();
		}
		return sreturn;
	}
}