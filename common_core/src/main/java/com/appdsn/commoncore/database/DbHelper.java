package com.appdsn.commoncore.database;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.appdsn.commoncore.utils.ResourceUtils;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class DbHelper {

    /**
     * 将assets文件夹下文件拷贝到/databases/下
     *
     * @param db_name
     */
    public static boolean copyDbFile(Context context, String db_name) {
        //创建文件夹
        File dbFile = context.getDatabasePath(db_name);
        return ResourceUtils.copyFileFromAssets(db_name, dbFile.getAbsolutePath());
    }

    /**
     * 新建表
     * "create table if not exists " + tableName + "(name text, pwd text, tel text)";
     *
     * @param columnMap key是字段名，value为字段类型
     */
    public static void createTable(Database db, String tableName, HashMap<String, String> columnMap) {
        if (TextUtils.isEmpty(tableName) || columnMap == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = columnMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            stringBuilder.append(entry.getKey()).append(" ");
            stringBuilder.append(entry.getValue());
            if (iterator.hasNext()) {
                stringBuilder.append(entry.getValue()).append(", ");
            }
        }
        String sql = "create table if not exists " + tableName + "(" + stringBuilder + ");";
        executeSql(db, sql);
    }


    /**
     * 删除表
     */
    public static void deleteTable(Database db, String tableName) {
        String sql = "drop table if exists " + tableName + ";";
        executeSql(db, sql);
    }

    /**
     * 从已有表创建临时表，数据也会同步过来
     */
    public static void createTempTable(Database db, String oldTableName, String tempTableName) {
        StringBuilder dropTableStringBuilder = new StringBuilder();
        dropTableStringBuilder.append("DROP TABLE IF EXISTS ").append(tempTableName).append(";");
        executeSql(db, dropTableStringBuilder.toString());

        StringBuilder insertTableStringBuilder = new StringBuilder();
        insertTableStringBuilder.append("CREATE TEMPORARY TABLE ").append(tempTableName);
        insertTableStringBuilder.append(" AS SELECT * FROM `").append(oldTableName).append("`;");
        executeSql(db, insertTableStringBuilder.toString());
    }

    /**
     * 表重命名
     */
    public static void renameTable(Database db, String oldName, String newName) {
        String sql = "ALTER TABLE ? RENAME TO ?;";
        executeSql(db, sql, new String[]{oldName, newName});
    }

    /**
     * 表中添加新的列
     *
     * @param columnName 新增字段名
     * @param dataType   新增字段数据类型
     */
    public static void addColumn(Database db, String tableName, String columnName, String dataType) {
        String sql = "ALTER TABLE ? ADD COLUMN ? ?;";
        executeSql(db, sql, new String[]{tableName, columnName, dataType});
    }

    /**
     * 执行sql语句
     *
     * @param sql
     */
    public static void executeSql(Database db, String sql) {
        executeSql(db, sql);
    }

    /**
     * 执行sql语句
     *
     * @param sql
     * @param bindArgs 可变参数
     */
    public static void executeSql(Database db, String sql, Object[] bindArgs) {
        try {
            if (bindArgs == null) {
                db.execSQL(sql);
            } else {
                db.execSQL(sql, bindArgs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<HashMap<String, String>> getTableInfo(Database db, String tableName) {
        String sql = "PRAGMA table_info(`" + tableName + "`)";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor == null) {
            return new ArrayList<>();
        }
        List<HashMap<String, String>> tableInfos = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> tableInfo = new HashMap<>();
            tableInfo.put("cid", cursor.getInt(0) + "");
            tableInfo.put("name", cursor.getString(1));
            tableInfo.put("type", cursor.getString(2));
            tableInfo.put("notnull", (cursor.getInt(3) == 1) + "");
            tableInfo.put("dfltValue", cursor.getString(4));
            tableInfo.put("pk", (cursor.getInt(5) == 1) + "");
            tableInfos.add(tableInfo);
        }
        cursor.close();
        return tableInfos;
    }

    /**
     * 开始始事务
     */
    public static void beginTransaction(Database db) {
        if (db != null) {
            db.beginTransaction();
        }
    }

    /**
     * 提交事务及结束事物
     */
    public static void commit(Database db) {
        if (db != null) {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    /**
     * 回滚事务
     */
    public static void rollback(Database db) {
        if (db != null) {
            db.endTransaction();
        }
    }
}
