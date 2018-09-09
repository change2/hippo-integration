package com.change.hippo.utils.http;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * mike  2018/09/09
 */
public class IPUtil {


    private static String serverIp;

    public static String getIpAddress(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取本地服务的IP
     *
     * @return
     */
    public static String getServerIp() {

        if (serverIp != null) {
            return serverIp;
        }
        // 一个主机有多个网络接口
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = netInterfaces.nextElement();
                // 每个网络接口,都会有多个"网络地址",比如一定会有lookback地址,会有siteLocal地址等.以及IPV4或者IPV6 .
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet6Address) {
                        continue;
                    }
                    if (address.isSiteLocalAddress() && !address.isLoopbackAddress()) {
                        serverIp = address.getHostAddress();
                        continue;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return serverIp;
    }

    public static List<String> getIPRange(Object ipRangeStr, StringBuffer invalidIP) {
        List<String> ipList = new java.util.Vector<String>();
        if (ipRangeStr == null)
            return ipList;
        String ipRangeS = ipRangeStr.toString().trim();
        // check the format ip1 - ip2 or ip/subnet
        if (ipRangeS.indexOf("/") > 0) {
            // check the format ip/subnet
            String[] ipRanges = ipRangeS.split("/");
            if (ipRanges.length == 2) {
                String startIP = ipRanges[0].trim();

                if (!Utility.isValidIpAddress(startIP)) {
                    invalidIP.append("," + ipRangeS);
                    return ipList;
                }
                // startIP=startIP.substring(0,startIP.lastIndexOf("."))+".1";
                int subnetIndex = Utility.convertToSubnetIndex(ipRanges[1].trim());
                if (subnetIndex == -1) {
                    try {
                        subnetIndex = Integer.parseInt(ipRanges[1].trim());
                    } catch (Exception ex) {

                    }
                }
                if (subnetIndex >= 1 && subnetIndex <= 32) {
                    long long_beginip = Utility.convertIP(startIP);
                    String subnetAddress = Utility.getSubnetAddress(startIP, subnetIndex);
                    long long_subnetIP = Utility.convertIP(subnetAddress);
                    if (long_subnetIP + 1 <= long_beginip) {
                        startIP = subnetAddress.split("\\.")[0]
                                + "."
                                + subnetAddress.split("\\.")[1]
                                + "."
                                + subnetAddress.split("\\.")[2]
                                + "."
                                + (Integer.parseInt(subnetAddress.split("\\.")[3]) + 1);
                    }
                    String broadcastIP = Utility.getBrocadCastAddress(Utility.getSubnetAddress(startIP, subnetIndex), subnetIndex);
//					long long_broadcastip = Utility.convertIP(broadcastIP);
//					long long_endip = long_broadcastip;
                    String endIP = broadcastIP;
                    // if((long_endip-long_beginip)>WiFiConstants.DiscoverMaxNumber){
                    // throw new
                    // WiFiException(ErrorCode.ERROR_Discover_IP_Range_TooLarger_5003,"Discover devices failed. The valid IP range counter is greater than 5000",
                    // String.valueOf(WiFiConstants.DiscoverMaxNumber));
                    // //throw new
                    // Exception("The IP count is greater than 5000.");
                    // }
                    String[] ip = Utility.SetMoreIP(startIP, endIP);
                    ipList.addAll(Arrays.asList(ip));
                } else {
                    invalidIP.append("," + ipRangeS);
                }

            } else {
                invalidIP.append("," + ipRangeS);
            }
        } else if (ipRangeS.indexOf("-") > 0) {
            // check the format ip1 - ip2
            String[] ipRanges = ipRangeS.split("-");
            if (ipRanges.length == 2) {
                if (!Utility.isValidIpAddress(ipRanges[0].trim()) || !Utility.isValidIpAddress(ipRanges[1].trim())) {
                    invalidIP.append("," + ipRangeS);
                    return ipList;
                }
                long long_beginip = Utility.convertIP(ipRanges[0].trim());
                long long_endip = Utility.convertIP(ipRanges[1].trim());

                if (long_endip >= long_beginip) {
                    // if((long_endip-long_beginip)>WiFiConstants.DiscoverMaxNumber){
                    // throw new
                    // WiFiException(ErrorCode.ERROR_Discover_IP_Range_TooLarger_5003,"Discover devices failed. The valid IP range counter is greater than 5000",
                    // String.valueOf(WiFiConstants.DiscoverMaxNumber));
                    // //throw new
                    // Exception("The IP count is greater than 5000.");
                    // }
                    String startIP = ipRanges[0].trim();
                    String endIP = ipRanges[1].trim();
                    String[] ip = Utility.SetMoreIP(startIP, endIP);
                    ipList.addAll(Arrays.asList(ip));

                } else {
                    invalidIP.append("," + ipRangeS);
                }
            } else {
                invalidIP.append("," + ipRangeS);
            }
        } else if (Utility.isValidIpAddress(ipRangeS)) {
            // is a single ip
            ipList.add(ipRangeS);
            return ipList;
        } else {
            invalidIP.append("," + ipRangeS);
        }

        return ipList;
    }
}
