package com.xeviosoft;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.concurrent.TimeUnit;

/**
 * @author David Hill, Jr.
 * @date 3/20/2021
 * @description This is a simple class that uses OSHI to sample network data received and calculates
 *              the utilization % of a network interface.
 * */
public class App {

    public static void main(String[] args) throws InterruptedException {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        NetworkIF nic =  hal.getNetworkIFs().get(0);
        getNetworkUtil(nic);
    }

    public static void getNetworkUtil(NetworkIF nic) throws InterruptedException{
        int sample_time = 5;
        long link_speed = nic.getSpeed();

        nic.updateAttributes();
        long rec1 = nic.getBytesRecv();
        TimeUnit.SECONDS.sleep(sample_time);
        nic.updateAttributes();
        long rec2 = nic.getBytesRecv();

        System.out.println(nic.getDisplayName());
        System.out.println("Link Speed: " + link_speed); //bps
        System.out.println("Bytes Rx 1: " + rec1);
        System.out.println("Bytes Rx 2: " + rec2);
        System.out.println("Bits Rx 1: " + rec1 * 8);
        System.out.println("Bits Rx 2: " + rec2 * 8);

        long bytes_rx = rec2 - rec1;
        System.out.println("Bytes sampled: " + bytes_rx);

        //https://support.solarwinds.com/SuccessCenter/s/article/Calculate-interface-bandwidth-utilization?language=en_US
        float rx_util = (bytes_rx * 8 * 100)/(sample_time * link_speed);

        System.out.println("NIC Util: " + rx_util + "%");

    }
}

