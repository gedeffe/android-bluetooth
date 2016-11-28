/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.gedeffe.bluetoothlowenergy;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        attributes.put("00001805-0000-1000-8000-00805f9b34fb", "Current Time Service");
        attributes.put("00001800-0000-1000-8000-00805f9b34fb", "Generic Access");
        attributes.put("00001801-0000-1000-8000-00805f9b34fb", "Generic Attribute");
        attributes.put("00007801-0000-1000-8000-00805f9b34fb", "Pedometer");
        attributes.put("00007806-0000-1000-8000-00805f9b34fb", "Unknown service two");

        // Sample Characteristics.
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");

        // Generic Access characteristics
        attributes.put("00002a00-0000-1000-8000-00805f9b34fb", "Device Name"); // utf8s
        attributes.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance"); // 16 bits
        attributes.put("00002a02-0000-1000-8000-00805f9b34fb", "Peripheral Privacy Flag"); // boolean
        attributes.put("00002a03-0000-1000-8000-00805f9b34fb", "Reconnection Address"); // uint48
        attributes.put("00002a04-0000-1000-8000-00805f9b34fb", "Peripheral Preferred Connection Parameters"); // 4 x uint16

        // Generic Attribute characteristics
        attributes.put("00002a05-0000-1000-8000-00805f9b34fb", "Service Changed"); // 2 x utf16

        // Device information characteristics
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name"); // utf8s
        attributes.put("00002a24-0000-1000-8000-00805f9b34fb", "Model Number"); // utf8s
        attributes.put("00002a25-0000-1000-8000-00805f9b34fb", "Serial Number"); // utf8s
        attributes.put("00002a27-0000-1000-8000-00805f9b34fb", "Hardware Revision"); // utf8s
        attributes.put("00002a26-0000-1000-8000-00805f9b34fb", "Firmware Revision"); // utf8s
        attributes.put("00002a28-0000-1000-8000-00805f9b34fb", "Software Revision"); // utf8s
        attributes.put("00002a23-0000-1000-8000-00805f9b34fb", "System ID"); // uint40 + uint24
        attributes.put("00002a2a-0000-1000-8000-00805f9b34fb", "IEEE 11073-20601 Regulatory Certification Data List"); // reg-cert-data-list
        attributes.put("00002a50-0000-1000-8000-00805f9b34fb", "PnP ID"); // uint8 + uint16 + uint16 + uint16

        // Pedometer characteristics
        attributes.put("00008a11-0000-1000-8000-00805f9b34fb", "Measurement"); //
        attributes.put("00008a10-0000-1000-8000-00805f9b34fb", "Feature"); //
        attributes.put("0000a400-0000-1000-8000-00805f9b34fb", "Pedometer service UUID A4"); //
        attributes.put("0000a402-0000-1000-8000-00805f9b34fb", "Pedometer characteristics UUID A4"); //
        attributes.put("0000a401-0000-1000-8000-00805f9b34fb", "Pedometer write characteristics UUID A4"); //
        attributes.put("0000fee7-0000-1000-8000-00805f9b34fb", "Pedometer service UUID wechat"); //
        attributes.put("00008a81-0000-1000-8000-00805f9b34fb", "Download information or command"); //
        attributes.put("00008a82-0000-1000-8000-00805f9b34fb", "Upload information or event"); //
        // 8a60 ?
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
