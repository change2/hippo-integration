package com.change.hippo.utils.http;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.StringTokenizer;

@SuppressWarnings("unused")
public class Utility {
    public static boolean isValidIpAddress(String addr) {
        String tokenStr;
        int token;
        StringTokenizer tkzr = new StringTokenizer(addr, ".");
        if (tkzr.countTokens() != 4) {
            return false;
        }
        int i = 0;
        while (tkzr.hasMoreTokens()) {
            tokenStr = tkzr.nextToken();
            try {
                token = new Integer(tokenStr).intValue();
            } catch (Exception ex) {
                return false;
            }
            if (i == 0 && (token < 1 || token > 223)) {
                return false;
            }
            if (token < 0 || token > 255) {
                return false;
            }
            i++;
        }
        return true;
    }

    // 更改JFileChooser中文乱码问题。 因为JFileChooser本身就是许多component的集合，只要用一个递归函数即可设定其中的字体
    public static void updateFont(Component comp, Font font) {
        comp.setFont(font);
        if (comp instanceof Container) {
            Container c = (Container) comp;
            int n = c.getComponentCount();
            for (int i = 0; i < n; i++) {
                updateFont(c.getComponent(i), font);
            }
        }
    }

    public static String generateKey() {
        StringBuilder s = new StringBuilder();

        int size = 15;
        for (int i = 0; i < size; i++) {
            s.append(getRandomChar());
        }
        /*
		 * try { KeyPairGenerator kpg; kpg =
		 * KeyPairGenerator.getInstance("RSA"); kpg.initialize(1024); KeyPair kp =
		 * kpg.genKeyPair(); s=kp.getPublic().toString(); } catch
		 * (NoSuchAlgorithmException e) {
		 *  }
		 */

        return s.toString();
    }

    public static char getRandomChar() {
        Random random = new Random();
		/*
		 * if (random.nextInt(5) < 3) { // A - Z return (char) (65 +
		 * random.nextInt(26)); }
		 */
        // a - z
        return (char) (97 + random.nextInt(26));

    }

    public static String getBrocadCastAddress(String subnetAddress, int subnet) {
        String newIP = "";
        String[] ipS1 = subnetAddress.split("\\.");

        int ipLength = ipS1.length;
        if (ipLength != 4) {
            return null;
        }
        if (subnet > 32 || subnet < 1)
            return null;
        int number = 32 - subnet;
        int index1 = number / 8;
        int index2 = number % 8;
        // if(index2!=0)index1++;

        for (int i = index1 - 1; i >= 0; i--) {
            ipS1[3 - i] = "255";
        }

        if (index2 > 0) {
            Object newValue = (Integer.parseInt(ipS1[3 - index1]) | (int) Math
                    .pow(2, index2) - 1);
            ipS1[3 - index1] = newValue.toString();
        }
        return ipS1[0] + "." + ipS1[1] + "." + ipS1[2] + "." + ipS1[3];
    }

    public final static long convertIP(String ipAddress) {
        long rtn = 0;
        if (ipAddress == null || ipAddress.trim().equals("")) {
            return 0;
        }
        String[] ss = ipAddress.split("\\.");
        for (int i = 0; i < ss.length; i++) {
            rtn <<= 8;
            rtn += Long.parseLong(ss[i]);
        }
        for (int i = ss.length; i < 4; i++) {
            rtn <<= 8;
        }
        return rtn;
    }

    public static final void populateInt(byte[] buf, int offset, int data) {
        buf[offset] = (byte) ((data & 0xff000000) >> 24);
        buf[offset + 1] = (byte) ((data & 0xff0000) >> 16);
        buf[offset + 2] = (byte) ((data & 0xff00) >> 8);
        buf[offset + 3] = (byte) ((data & 0xff));
    }

    public static final String ConverMask(int mask) {
        String Dest_IP = null;
        int init = 0;
        int tmp = 1;
        if (mask == 0) {
            return null;
        }
        if (mask > 32) {
            return null;
        }
        for (int j = 0; j < 32; j++) {
            init = init << 1;
            if (j < mask) {
                init = init + 1;
            }
        }

        byte[] b_tmp = new byte[4];
        populateInt(b_tmp, 0, init);
        try {
            InetAddress dest = InetAddress.getByAddress(b_tmp);
            Dest_IP = dest.getHostAddress();
        } catch (Exception e) {
        }
        return Dest_IP;

    }

    public static int convertToSubnetIndex(String subnetMask) {

        if (subnetMask != null) {
            for (int i = 1; i <= 32; i++) {
                if (ConverMask(i).equals(subnetMask.trim())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void main(String[] a) {
        System.out.println(isInIPRange("192.168.0.165", "192.168.0.1/255.255.255.0"));
        for (int i = 1; i <= 32; i++) {
            System.out.println(i + ":" + ConverMask(i));
        }
        if (true) return;
        String[] s = SetMoreIP("192.168.0.165", "192.168.0.165");
        System.out.println(convertToSubnetIndex("255.255.255.0"));
        System.out.println(ConverMask(24));

        String subnetaddsrss = getSubnetAddress("192.168.0.1", "255.255.255.0");
        System.out.println(subnetaddsrss);
        System.out.println(getBrocadCastAddress(subnetaddsrss, 24));
        if (true)
            return;
        for (int i = 1; i < 33; i++) {
            System.out.println(i + "," + ConverMask(i));
            subnetaddsrss = getSubnetAddress("1.1.0.1", ConverMask(i));
            System.out.println(getBrocadCastAddress(subnetaddsrss, i));

        }
    }

    public static final String[] getIPAndSubMask(Object value) {
        String[] ips = new String[2];
        String s = (String) value;
        String r = null;
        int position = s.indexOf("/");
        if (position > 0) {
            r = s.substring(position + 1, s.length());
            s = s.substring(0, position);
        }
        if (position == 0) {
            return null;
        } else {
            // s = s.substring(1, s.length());
        }
        ips[0] = (String) value;
        try {
            if (true) {
                if (r == null) {
                    r = "";
                    ips[1] = r;
                    return ips;
                }

                if (r.length() <= 2 && r.length() > 0) {
                    int mask = 0;
                    Integer g = new Integer(r);
                    mask = g.intValue();

                    if (mask > 0 && mask <= 32) {
                        String tmp = ConverMask(mask);
                        // System.out.println(tmp);
                        // ips[0]=(String) value;
                        ips[1] = tmp;
                    } else {
                        return null;
                    }
                }

            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
        return ips;

    }

    public static String getSubnetAddress(String ip, int subnet) {
        return getSubnetAddress(ip, ConverMask(subnet));
    }

    public static String getSubnetAddress(String ip, String subnetMask) {
        String newIP = "";
        String[] ipS1 = ip.split("\\.");
        String[] ipS2 = subnetMask.split("\\.");
        int ipLength = ipS1.length;
        if (ipLength != 4 || ipS2.length != 4) {
            return null;
        }
        for (int i = 0; i < ipLength; i++) {
            Object newValue = (Integer.parseInt(ipS1[i]) & Integer
                    .parseInt(ipS2[i]));
            newIP = newIP + "." + newValue.toString();
        }
        if (newIP.startsWith(".")) {
            newIP = newIP.substring(1, newIP.length());
        }
        return newIP;
    }

    public static String[] SetMoreIP(String beginip, String endip) {
        try {
            long long_beginip = Long.MIN_VALUE;
            long long_endip = Long.MIN_VALUE;

            long_beginip = convertIP(beginip);

            long_endip = convertIP(endip);

            long[] long_ip = new long[(int) Math.abs(long_beginip - long_endip) + 1];
            for (int k = 0; k <= Math.abs(long_beginip - long_endip); k++) {
                if (long_beginip - long_endip < 0) {
                    long_ip[k] = long_beginip + (long) k;
                } else {
                    long_ip[k] = long_endip + (long) k;
                }
            }

            String[] strip = new String[4];
            String[] ipList = new String[long_ip.length];
            for (int m = 0; m < long_ip.length; m++) {
                strip[0] = String.valueOf(long_ip[m] & 0x00000000000000ff);
                strip[1] = String.valueOf(long_ip[m] >> 8 & 0x00000000000000ff);
                strip[2] = String
                        .valueOf(long_ip[m] >> 16 & 0x00000000000000ff);
                strip[3] = String
                        .valueOf(long_ip[m] >> 24 & 0x00000000000000ff);
                ipList[m] = strip[3] + "." + strip[2] + "." + strip[1] + "."
                        + strip[0];
            }

            return ipList;

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public static String[] SetMoreIPBySubnet(String startIP, String subnetStr) {
        int subnetIndex = -1;
        try {

            subnetIndex = Integer.parseInt(subnetStr.trim());
        } catch (Exception ex) {
        }

        if (subnetIndex == -1) {
            subnetIndex = Utility.convertToSubnetIndex(subnetStr.trim());
        }

        if (subnetIndex >= 1 && subnetIndex <= 32) {
            long long_beginip = Utility.convertIP(startIP);
            String broadcastIP = Utility.getBrocadCastAddress(Utility
                    .getSubnetAddress(startIP, subnetIndex), subnetIndex);
            long long_broadcastip = Utility.convertIP(broadcastIP);
            long long_endip = long_broadcastip;
            String endIP = broadcastIP;
            String[] ip = Utility.SetMoreIP(startIP, endIP);
            return ip;
        }
        return null;

    }

    public static boolean isInIPRange(String ip, String ipRangeS) {
        boolean f = false;
        if (!isValidIpAddress(ip))
            return false;
        if (ipRangeS.indexOf("/") > 0) {
            // check the format ip/subnet
            String[] ipRanges = ipRangeS.split("/");
            if (ipRanges.length == 2) {
                String startIP = ipRanges[0].trim();

                if (!Utility.isValidIpAddress(startIP)) {

                    return false;
                }

                int subnetIndex = Utility.convertToSubnetIndex(ipRanges[1]
                        .trim());

                if (subnetIndex == -1) {
                    try {

                        subnetIndex = Integer.parseInt(ipRanges[1].trim());
                    } catch (Exception ex) {

                    }
                }

                if (subnetIndex >= 1 && subnetIndex <= 32) {
                    long long_beginip = Utility.convertIP(startIP);
                    String broadcastIP = Utility.getBrocadCastAddress(Utility
                                    .getSubnetAddress(startIP, subnetIndex),
                            subnetIndex);
                    long long_broadcastip = Utility.convertIP(broadcastIP);
                    long long_endip = long_broadcastip;
                    long compareIP = Utility.convertIP(ip);
                    if (compareIP <= long_endip && compareIP >= long_beginip) {
                        return true;
                    }

                }

            }
        }
        return f;
    }

    static String localIP = null;

    public static String getLocalIP() {
        if (localIP == null) {
            try {
                localIP = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {

            }
        }
        if (localIP == null) {
            localIP = "127.0.0.1";
        }
        return localIP;
    }

    public static boolean isValidMac(String s) {
        if (s != null && s.trim().length() == 17) {
            s = s.trim().replaceAll(":", "-");
            if (s.split("-").length == 6) {
                return true;
            }
        }
        return false;
    }

}
