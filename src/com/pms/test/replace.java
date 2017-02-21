package com.pms.test;

public class replace {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sfilenum = "test[Name]";
		
		sfilenum = sfilenum.replaceAll("\\[", "AA");
		sfilenum = sfilenum.replaceAll("\\]", "BB");
		sfilenum = sfilenum.replaceAll("AA", "[[]");
		sfilenum = sfilenum.replaceAll("BB", "[]]");
		
		sfilenum = "<abc> <abc>     <fdsad>      </ssdfasd>";
		sfilenum = sfilenum.replaceAll("> *<", "><");
		
		String ori = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--\n要求返回所有字段\nselect * From NB_TAB_HTTP where CAPTURE_TIME between 1426694400 and 1434556799  AND ISP_ID IN ( '01','02','03','04','05','06')  AND HTTP_TYPE IN ('1000001','1000002','1000003','1000004','1009996','1009997','1009999') and DATA_SOURCE in (111,120,123,124)\n-->\n<MESSAGE>\n<DATASET name=\"WA_COMMON_010000\" rmk=\"数据交互通用信息\">\n<DATA>\n<ITEM key=\"H010006\" eng=\"FROM\" val=\"320000\" chn=\"发起节点的标识\"/>\n<ITEM key=\"H010007\" eng=\"TO\" val=\"320000\" chn=\"目的节点的标识\"/>\n<ITEM key=\"I010014\" eng=\"MESSAGE_SEQUENCE\" val=\"320000201506181040590001\" chn=\"消息流水号\"/>\n<ITEM key=\"I010013\" eng=\"MESSAGE_TYPE\" val=\"152011\" chn=\"消息类型\"/>\n<ITEM key=\"I010010\" eng=\"BUSINESS_SERVER_TYPE\" val=\"07\" chn=\"业务服务类型\"/>\n</DATA>\n</DATASET>\n</MESSAGE>";

		System.out.println(ori);
		System.out.println();
		
		String temp = ori.substring(ori.indexOf("<!--"), ori.indexOf("-->")+3);
		ori=ori.replace(temp, "");
		
		
//		ori = ori.replaceAll("<!--(\\s.*)*-->","1234567");
		System.out.println(ori);
	}

}
