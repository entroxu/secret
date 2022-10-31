package cn.tocute.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        String url = "http://k3cloudmobtest.kingdee.com/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.NLPService.InitContext.common.kdsvc";
        Map<String,String> bodyMap = new HashMap<>();

        bodyMap.put("lcid","2052");
        bodyMap.put("entryrole","XT");
        bodyMap.put("acctID","5eccbe1301c99a");

        String s = HttpUtil.postByUrlEncodedForm(url, null,null, bodyMap);
        int sa = 1;
    }
}
