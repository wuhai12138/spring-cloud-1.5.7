package com.sfy.gateway.web;

import com.google.gson.Gson;
import com.sfy.gateway.result.Routes;
import com.sfy.gateway.utils.ConstantSFY;
import com.sfy.gateway.utils.StringUtilSFY;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.swagger.web.SwaggerResource;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

@Controller
@Slf4j
public class IndexController {
    @Autowired
    ZuulProperties properties;

    @RequestMapping(value = "/index")
    public String getIndex() {
        return "index";
    }

    /**
     * 列表
     * */
    @RequestMapping(value = "/getRoutes", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void getRoutes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        List<Routes> routesList = new ArrayList <>();
        Map<String, ZuulProperties.ZuulRoute> routeMap = properties.getRoutes();

//        InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
//        String hostAddress = this.getLocalIP();//address.getHostAddress();//192.168.0.121
        String hostAddress = ConstantSFY.IP_AGENT;
        String hostPort = ConstantSFY.PORT_AGENT;

        for (String key : routeMap.keySet()) {
            ZuulProperties.ZuulRoute zuulRoute = routeMap.get(key);
            Routes routes = new Routes();
            routes.setName(StringUtilSFY.setZh(zuulRoute.getId()) + "==" + zuulRoute.getServiceId());
            routes.setUrl("http://" + hostAddress + ":" + hostPort + "/" + zuulRoute.getServiceId() + "/v2/api-docs");
            routesList.add(routes);
        }

        resultMap.put("routesList", routesList);
        String json = new Gson().toJson(resultMap);
        response.getWriter().write(json);
    }

    /**
     * 获取本地IP地址
     *
     * @throws SocketException
     */
    public static String getLocalIP() throws UnknownHostException, SocketException {
        if (isWindowsOS()) {
            return InetAddress.getLocalHost().getHostAddress();
        } else {
            return getLinuxLocalIp();
        }
    }

    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
    private static String getLinuxLocalIp() throws SocketException {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); ((Enumeration) en).hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                                System.out.println(ipaddress);
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("获取ip地址异常");
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        System.out.println("IP:"+ip);
        return ip;
    }
}
