package com.yalantis.euclid.packet;

import android.database.sqlite.SQLiteDatabase;

import com.yalantis.euclid.TPApp;
import com.yalantis.euclid.global.GlobalConfig;
import com.yalantis.euclid.net.TPPacket;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by bbwang on 2016/8/14.
 */
public class SyncTaskListPacket extends TPPacket {

    private static final String TAG = "SyncTaskListPacket";
    private long uc = 0;

    public SyncTaskListPacket(){
        loadUcFromDb();
    }

    public int loadUcFromDb(){
        int ret = 0;
        String cardPath = TPApp.getSdCardPath();
        String dbPath = cardPath  + GlobalConfig.DB_PATH;
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SQLiteDatabase sql = SQLiteDatabase.openOrCreateDatabase(dbFile, null);

        /*
        TPSQLiteHelper dbHelper = new TPSQLiteHelper(TPApp.getContext()
                , GlobalConfig.DB_PATH
                , null
                , GlobalConfig.DB_VERSION);
        //得到一个可读的数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(GlobalConfig.TABLE_TASK_LIST_UC
                , GlobalConfig.TABLE_TASK_LIST_COL
                , "name=?"
                , new String[]{"task"}
                , null, null, null);

        if (cursor.getCount() == 0) {
            Log.i(TAG, "not found data in db.");
            return 0;
        }

        // assert，只会取到一行数据
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String ucStr = cursor.getString(2);
            uc = Long.valueOf(ucStr);
            Log.i(TAG, "show db result, id="+id+", name="+name+", uc="+ucStr);
        }

*/
        return ret;
    }

    public byte[] encode() {
        // TODO: 不同的数据包，有不一样的编码方式，这个只是基类
        ByteBuffer buffer = ByteBuffer.allocate(TPPacket.LOW_NET_PACKET_LEN);


        return new byte[0];
    }

    public int make(){
        return -1;
    }

}
