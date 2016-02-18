package cn.framework.rest;

import cn.framework.core.utils.Property;
import cn.framework.rest.utils.Packages;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main(String[] args) {
        System.setProperty("upk.enabled", "false");
        System.setProperty(Property.MAIN_JAR_PATH, "c:/framework.jar");
        Packages.scanServicePackage("cn.etcp.coupon_2016.controller");
    }
}
