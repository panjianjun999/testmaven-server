package com.good.hie.server;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	long ti1 = System.currentTimeMillis();
    	long ti2 = System.nanoTime();
        System.out.println( "Hello World!1=" + ti1);
        System.out.println( "Hello World!2=" + ti2);
        
		Map<String, Object> pMap = new HashMap<String, Object>();//参数
		pMap.put("accountId", 922347558000001L);
		pMap.put("playerId", 76677513L);
		pMap.put("accountName", "pan1234");
		pMap.put("sdkUid", "QGBMgv3uR2uBGlPhbwN4LA");
		String jsonObj = JSONObject.toJSONString(pMap);
		
		System.out.println("jsonObj=" + jsonObj);
		
		long ti11 = System.currentTimeMillis();
    	long ti22 = System.nanoTime();
		System.out.println( "Hello World!1-1=" + (ti11 - ti1));
        System.out.println( "Hello World!2-2=" + (ti22 - ti2));
    }
    
    /**
     * 测试加法
     * @param x
     * @param y
     * @return
     */
    public int sum(int x,int y) {
    	if(y > 10) {
    		throw new RuntimeException("不支持的参数");
    	}
    	return x + y;
    }
}
