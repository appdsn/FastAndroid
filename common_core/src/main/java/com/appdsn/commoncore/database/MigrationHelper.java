package com.appdsn.commoncore.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 数据库升级帮助类
 * please call {@link #migrate(SQLiteDatabase, Class[])} or {@link #migrate(Database, Class[])}
 * MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
 *
 * @Override public void onCreateAllTables(Database db, boolean ifNotExists) {
 * DaoMaster.createAllTables(db, ifNotExists);
 * }
 * @Override public void onDropAllTables(Database db, boolean ifExists) {
 * DaoMaster.dropAllTables(db, ifExists);
 * }
 * }, MigrationHelper.MigrationInfo.create(UserDao.class)
 * .addRenameColumn("NAME", "NICKNAME")
 * .addRenameColumn("MOBILE", "PHONE"));
 */
public final class MigrationHelper {

    public static boolean DEBUG = true;
    private static String TAG = "MigrationHelper";
    private static final String SQLITE_MASTER = "sqlite_master";
    private static final String SQLITE_TEMP_MASTER = "sqlite_temp_master";

    private static WeakReference<ReCreateAllTableListener> weakListener;

    public interface ReCreateAllTableListener {
        void onCreateAllTables(Database db, boolean ifNotExists);

        void onDropAllTables(Database db, boolean ifExists);
    }

    /**
     * 表字段改变信息
     */
    public static class MigrationInfo {
        Class<? extends AbstractDao<?, ?>> daoClass;
        HashMap<String, String> columnMap = new HashMap<>();

        public MigrationInfo(Class<? extends AbstractDao<?, ?>> daoClass) {
            this.daoClass = daoClass;
        }

        public static MigrationInfo create(Class<? extends AbstractDao<?, ?>> daoClass) {
            return new MigrationInfo(daoClass);
        }

        /**
         * @param oldColumn 旧表字段名
         * @param newColumn 新表字段名
         */
        public MigrationInfo addRenameColumn(String oldColumn, String newColumn) {
            columnMap.put(oldColumn, newColumn);
            return this;
        }
    }


    public static void migrate(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        printLog("【The Old Database Version】" + db.getVersion());
        Database database = new StandardDatabase(db);
        migrate(database, daoClasses);
    }

    public static void migrate(SQLiteDatabase db, ReCreateAllTableListener listener, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        weakListener = new WeakReference<>(listener);
        migrate(db, daoClasses);
    }

    public static void migrate(Database database, ReCreateAllTableListener listener, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        weakListener = new WeakReference<>(listener);
        migrate(database, daoClasses);
    }

    public static void migrate(Database database, ReCreateAllTableListener listener, MigrationInfo... migrationInfos) {
        weakListener = new WeakReference<>(listener);
        migrate(database, migrationInfos);
    }

    public static void migrate(Database database, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        MigrationInfo[] migrationInfos = new MigrationInfo[daoClasses.length];
        for (int i = 0; i < daoClasses.length; i++) {
            migrationInfos[i] = MigrationInfo.create(daoClasses[i]);
        }
        migrate(database, migrationInfos);
    }

    public static void migrate(Database database, MigrationInfo... migrationInfos) {
        printLog("【Generate temp table】start");
        generateTempTables(database, migrationInfos);
        printLog("【Generate temp table】complete");

        ReCreateAllTableListener listener = null;
        if (weakListener != null) {
            listener = weakListener.get();
        }

        if (listener != null) {
            listener.onDropAllTables(database, true);
            printLog("【Drop all table by listener】");
            listener.onCreateAllTables(database, false);
            printLog("【Create all table by listener】");
        } else {
            dropAllTables(database, true, migrationInfos);
            createAllTables(database, false, migrationInfos);
        }
        printLog("【Restore data】start");
        restoreData(database, migrationInfos);
        printLog("【Restore data】complete");
    }

    private static void generateTempTables(Database db, MigrationInfo... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            String tempTableName = null;

            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i].daoClass);
            String tableName = daoConfig.tablename;
            if (!isTableExists(db, false, tableName)) {
                printLog("【New Table】" + tableName);
                continue;
            }
            try {
                tempTableName = daoConfig.tablename.concat("_TEMP");
                StringBuilder dropTableStringBuilder = new StringBuilder();
                dropTableStringBuilder.append("DROP TABLE IF EXISTS ").append(tempTableName).append(";");
                db.execSQL(dropTableStringBuilder.toString());

                StringBuilder insertTableStringBuilder = new StringBuilder();
                insertTableStringBuilder.append("CREATE TEMPORARY TABLE ").append(tempTableName);
                insertTableStringBuilder.append(" AS SELECT * FROM `").append(tableName).append("`;");
                db.execSQL(insertTableStringBuilder.toString());
                printLog("【Table】" + tableName + "\n ---Columns-->" + getColumnsStr(daoConfig));
                printLog("【Generate temp table】" + tempTableName);
            } catch (SQLException e) {
                Log.e(TAG, "【Failed to generate temp table】" + tempTableName, e);
            }
        }
    }

    private static boolean isTableExists(Database db, boolean isTemp, String tableName) {
        if (db == null || TextUtils.isEmpty(tableName)) {
            return false;
        }
        String dbName = isTemp ? SQLITE_TEMP_MASTER : SQLITE_MASTER;
        String sql = "SELECT COUNT(*) FROM `" + dbName + "` WHERE type = ? AND name = ?";
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.rawQuery(sql, new String[]{"table", tableName});
            if (cursor == null || !cursor.moveToFirst()) {
                return false;
            }
            count = cursor.getInt(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count > 0;
    }


    private static String getColumnsStr(DaoConfig daoConfig) {
        if (daoConfig == null) {
            return "no columns";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < daoConfig.allColumns.length; i++) {
            builder.append(daoConfig.allColumns[i]);
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }


    private static void dropAllTables(Database db, boolean ifExists, @NonNull MigrationInfo... daoClasses) {
        reflectMethod(db, "dropTable", ifExists, daoClasses);
        printLog("【Drop all table by reflect】");
    }

    private static void createAllTables(Database db, boolean ifNotExists, @NonNull MigrationInfo... daoClasses) {
        reflectMethod(db, "createTable", ifNotExists, daoClasses);
        printLog("【Create all table by reflect】");
    }

    /**
     * dao class already define the sql exec method, so just invoke it
     */
    private static void reflectMethod(Database db, String methodName, boolean isExists, @NonNull MigrationInfo... daoClasses) {
        if (daoClasses.length < 1) {
            return;
        }
        try {
            for (MigrationInfo cls : daoClasses) {
                Method method = cls.daoClass.getDeclaredMethod(methodName, Database.class, boolean.class);
                method.invoke(null, db, isExists);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void restoreData(Database db, MigrationInfo... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i].daoClass);
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");

            if (!isTableExists(db, true, tempTableName)) {
                continue;
            }

            HashMap<String, String> columnMap = daoClasses[i].columnMap;

            try {
                // get all columns from tempTable, take careful to use the columns list
                List<TableInfo> newTableInfos = TableInfo.getTableInfo(db, tableName);
                List<TableInfo> tempTableInfos = TableInfo.getTableInfo(db, tempTableName);
                ArrayList<String> selectColumns = new ArrayList<>(newTableInfos.size());
                ArrayList<String> intoColumns = new ArrayList<>(newTableInfos.size());
                for (TableInfo tableInfo : tempTableInfos) {
                    if (newTableInfos.contains(tableInfo)) {
                        String column = '`' + tableInfo.name + '`';
                        intoColumns.add(column);
                        selectColumns.add(column);

                        /*旧表字段效验，去除相同的字段，防止外边误传*/
                        columnMap.remove(tableInfo.name);
                    }
                    /*旧表字段效验，去除没有的字段，防止外边误传*/
                    if (!columnMap.containsKey(tableInfo.name)) {
                        columnMap.remove(tableInfo.name);
                    }
                }

                HashMap<String, String> renamedColumnMap = new HashMap<>();
                // NOT NULL columns list
                for (TableInfo tableInfo : newTableInfos) {

                    /*新表字段效验，去除没有的字段，防止外边误传*/
                    Map.Entry<String, String> targetEntry = null;
                    Iterator<Map.Entry<String, String>> iterator = columnMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, String> entry = iterator.next();
                        if (tableInfo.name.equals(entry.getValue())) {
                            targetEntry = entry;
                            iterator.remove();
                        }
                    }

                    if (targetEntry != null) {
                        renamedColumnMap.put(targetEntry.getKey(), targetEntry.getValue());
                    } else if (tableInfo.notnull && !tempTableInfos.contains(tableInfo)) {
                        String column = '`' + tableInfo.name + '`';
                        intoColumns.add(column);

                        String value;
                        if (tableInfo.dfltValue != null) {
                            value = "'" + tableInfo.dfltValue + "' AS ";
                        } else {
                            value = "'' AS ";
                        }
                        selectColumns.add(value + column);
                    }
                }

                Iterator<Map.Entry<String, String>> iterator = renamedColumnMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    String column = '`' + entry.getValue() + '`';
                    intoColumns.add(column);
                    selectColumns.add("`" + entry.getKey() + "` AS " + column);
                }

                if (intoColumns.size() != 0) {
                    StringBuilder insertTableStringBuilder = new StringBuilder();
                    insertTableStringBuilder.append("REPLACE INTO `").append(tableName).append("` (");
                    insertTableStringBuilder.append(TextUtils.join(",", intoColumns));
                    insertTableStringBuilder.append(") SELECT ");
                    insertTableStringBuilder.append(TextUtils.join(",", selectColumns));
                    insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");
                    printLog("【Restore SQL】 " + insertTableStringBuilder.toString());
                    db.execSQL(insertTableStringBuilder.toString());
                    printLog("【Restore data】 to " + tableName);
                }
                StringBuilder dropTableStringBuilder = new StringBuilder();
                dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
                db.execSQL(dropTableStringBuilder.toString());
                printLog("【Drop temp table】" + tempTableName);
            } catch (SQLException e) {
                Log.e(TAG, "【Failed to restore data from temp table 】" + tempTableName, e);
            }
        }
    }

    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 0", null);
            if (null != cursor && cursor.getColumnCount() > 0) {
                columns = Arrays.asList(cursor.getColumnNames());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (null == columns) {
                columns = new ArrayList<>();
            }
        }
        return columns;
    }

    private static void printLog(String info) {
        if (DEBUG) {
            Log.d(TAG, info);
        }
    }

    private static class TableInfo {
        int cid;
        String name;
        String type;
        boolean notnull;
        String dfltValue;
        boolean pk;

        @Override
        public boolean equals(Object o) {
            return this == o
                    || o != null
                    && getClass() == o.getClass()
                    && name.equals(((TableInfo) o).name);
        }

        @Override
        public String toString() {
            return "TableInfo{" +
                    "cid=" + cid +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", notnull=" + notnull +
                    ", dfltValue='" + dfltValue + '\'' +
                    ", pk=" + pk +
                    '}';
        }

        private static List<TableInfo> getTableInfo(Database db, String tableName) {
            String sql = "PRAGMA table_info(`" + tableName + "`)";
            printLog(sql);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor == null) {
                return new ArrayList<>();
            }

            TableInfo tableInfo;
            List<TableInfo> tableInfos = new ArrayList<>();
            while (cursor.moveToNext()) {
                tableInfo = new TableInfo();
                tableInfo.cid = cursor.getInt(0);
                tableInfo.name = cursor.getString(1);
                tableInfo.type = cursor.getString(2);
                tableInfo.notnull = cursor.getInt(3) == 1;
                tableInfo.dfltValue = cursor.getString(4);
                tableInfo.pk = cursor.getInt(5) == 1;
                tableInfos.add(tableInfo);
                // printLog(tableName + "：" + tableInfo);
            }
            cursor.close();
            return tableInfos;
        }
    }
}

