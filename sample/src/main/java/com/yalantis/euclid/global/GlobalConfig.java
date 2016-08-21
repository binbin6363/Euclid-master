package com.yalantis.euclid.global;

/**
 * Created by bbwang on 2016/8/13.
 */
public class GlobalConfig {
    public static final String DEV_SERVER_IP = "139.196.42.222";     // 开发环境ip
    public static final int DEV_SERVER_PORT = 6000;                  // 开发环境port
    public static final String PRODUCT_SERVER_IP = "139.196.42.222"; // 生产环境ip
    public static final int PRODUCT_SERVER_PORT = 6000;              // 生产环境port
    public static final boolean IS_PRODUCT_ENV = false;              // 默认是开发环境，改成true就是生产环境

    public static final int NET_RECONNECT_INTERNAL = 2 * 1000;       // 网络重连间隔
    public static final int NET_CONNECT_TIMEOUT = 5 * 1000;          // 网络连接超时值

    // db
    public static final String DB_PATH = "/task_db.db";                  // db路径
    public static final int DB_VERSION = 1;                          // db版本
    public static final String DB_TASK = "db_task";                  // task db名称
    public static final String TABLE_TASK_LIST_UC = "t_task_uc";     // task uc表名称
    public static final String[] TABLE_TASK_LIST_COL = {"id", "name", "uc", "create_time", "update_time"};     // task uc表列名称

}
