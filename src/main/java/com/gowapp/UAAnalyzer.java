package com.gowapp;


import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class UAAnalyzer {
    
    UserAgentAnalyzer uaa = null;
    
    @RequestMapping("/Analyzer")
    @PostMapping
    @GetMapping
    public Object Analyzer(HttpServletRequest request, String ua){
        Map<String, Object> map = new HashMap<>();
        try {
            // 计时开始
            Long start_date  = System.currentTimeMillis();
            //
            // 默认优先使用入参解析
            ua = ua == null?"":ua;
            String userAgent = ua.equals("") ? request.getHeader("user-agent") : ua;
            userAgent = userAgent == null?"":userAgent;
            // 开始解析
            Object object = AnalyzerService(request, userAgent);
            map.put("status", 200);
            map.put("UA", userAgent);
            map.put("value", object);
            // 结束计时
            Long end_date  = System.currentTimeMillis();
            map.put("when", (end_date - start_date) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 500);
            map.put("value", "系统错误");
        }
        return map;
    }
    
    public Object AnalyzerService(HttpServletRequest request, String userAgent){
        //
        Map<String, String> valMap = new TreeMap<>();
        //
        UserAgent agent = AnalyzerBuilder().parse(userAgent);
        // 循环遍历获取各项的值
        for (String fieldName: agent.getAvailableFieldNamesSorted()) {
            valMap.put(fieldName, agent.getValue(fieldName));
        }
        return valMap;
    }
    
    /**
     * 实例化一个新的UserAgentAnalyzer的次数尽可能少，因为完整的UserAgentAnalyzer（即所有字段）的初始化步骤通常需要2-5秒的时间。
     * @return
     */
    public UserAgentAnalyzer AnalyzerBuilder(){
        if(uaa == null){
            uaa = UserAgentAnalyzer.newBuilder()
                          .hideMatcherLoadStats()
                          .withCache(10000)
                          .build();
            return uaa;
        }else{
            return uaa;
        }
    }
    
    @GetMapping("/")
    public String index(){
        
        return "<pre>" +
                       "\n" +
                       "  _    _                                      _                        _                    \n" +
                       " | |  | |               /\\                   | |     /\\               | |                   \n" +
                       " | |  | |___  ___ _ __ /  \\   __ _  ___ _ __ | |_   /  \\   _ __   __ _| |_   _ _______ _ __ \n" +
                       " | |  | / __|/ _ \\ '__/ /\\ \\ / _` |/ _ \\ '_ \\| __| / /\\ \\ | '_ \\ / _` | | | | |_  / _ \\ '__|\n" +
                       " | |__| \\__ \\  __/ | / ____ \\ (_| |  __/ | | | |_ / ____ \\| | | | (_| | | |_| |/ /  __/ |   \n" +
                       "  \\____/|___/\\___|_|/_/    \\_\\__, |\\___|_| |_|\\__/_/    \\_\\_| |_|\\__,_|_|\\__, /___\\___|_|   \n" +
                       "                              __/ |                                       __/ |             \n" +
                       "                             |___/                                       |___/              \n" +
                       "</pre>";
    }
}
























