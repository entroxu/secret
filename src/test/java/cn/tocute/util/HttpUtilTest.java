package cn.tocute.util;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


class HttpUtilTest {

    @Test
    void doPostByJson() {
        assert true;
    }

    @Test
    void doPostByUrlEncodedForm() throws IOException {
        String url = "https://k3cloudmobtest.kingdee.com/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.NLPService.InitContext.common.kdsvc";
        Map<String,String> bodyMap = new HashMap<>();

        bodyMap.put("lcid","2052");
        bodyMap.put("entryrole","XT");
        bodyMap.put("acctID","5eccbe1301c99a");

        String s = HttpUtil.doPostByUrlEncodedForm(url, null,null, bodyMap);


        assert s !=null;

    }
}