package com.proactivesensing.bobbydouglass.proactiveone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Modbus extends SQLiteOpenHelper {

    static final int zero = ((0 & 0x00ff) << 8) | (0 & 0x00ff);

    /* **************** 015 ***** Modbus 1000 ***** 015 **************** */
    static final int locations_per_day =                1;          // 1001
    static final int time_daily_msg =                  (6 * 60);    // 1002
    static final int auto_location =                    0;          // 1003
    static final int a_sensor_msg =                     0;          // 1004
    static final int a_sensor_time =                   (7 * 60);    // 1005
    static final int b_sensor_msg =                     0;          // 1006
    static final int b_sensor_time =                   (8 * 60);    // 1007
    static final int battery_low_alarm =                15;         // 1009
    static final int command_input_reg =                0;          // 1010
    static final int airplane_mode =                    0;          // 1011
    static final int power_config_3v3_1 =               0;          // 1012
    static final int power_config_3v3_2 =               0;          // 1013
    static final int power_config_15v =                 0;          // 1014
    static final int sensor_cycle =                     20;         // 1020
    static final int vcc_wait_scan =                    2;          // 1021
    /* **************** 015 ***** Modbus 1000 ***** 015 **************** */

    /* **************** 056 ***** Modbus 1100 ***** 056 **************** */
    static final int sensor_input_1_config =            1;          // 1100
    static final int sensor_input_1_recog =             3;          // 1101
    static final int sensor_1_analog_low =              0;          // 1102
    static final int sensor_1_analog_high =             100;        // 1103
    static final int sensor_1_calibration =             0;          // 1104
    static final int sensor_1_multiplier =              1;          // 1105
    static final int sensor_1_low_1 =                   0;          // 1106
    static final int sensor_1_high_1 =                  100;        // 1107
    static final int sensor_1_low_2 =                   0;          // 1108
    static final int sensor_1_high_2 =                  100;        // 1109
    static final int sensor_1_low_3 =                   0;          // 1110
    static final int sensor_1_high_3 =                  100;        // 1111
    static final int sensor_1_low_4 =                   0;          // 1112
    static final int sensor_1_high_4 =                  100;        // 1113

    static final int sensor_input_2_config =            1;          // 1114
    static final int sensor_input_2_recog =             3;          // 1115
    static final int sensor_2_analog_low =              0;          // 1116
    static final int sensor_2_analog_high =             100;        // 1117
    static final int sensor_2_calibration =             0;          // 1118
    static final int sensor_2_multiplier =              1;          // 1119
    static final int sensor_2_low_1 =                   0;          // 1120
    static final int sensor_2_high_1 =                  100;        // 1121
    static final int sensor_2_low_2 =                   0;          // 1122
    static final int sensor_2_high_2 =                  100;        // 1123
    static final int sensor_2_low_3 =                   0;          // 1124
    static final int sensor_2_high_3 =                  100;        // 1125
    static final int sensor_2_low_4 =                   0;          // 1126
    static final int sensor_2_high_4 =                  100;        // 1127

    static final int sensor_input_3_config =            1;          // 1128
    static final int sensor_input_3_recog =             3;          // 1129
    static final int sensor_3_analog_low =              0;          // 1130
    static final int sensor_3_analog_high =             100;        // 1131
    static final int sensor_3_calibration =             0;          // 1132
    static final int sensor_3_multiplier =              1;          // 1133
    static final int sensor_3_low_1 =                   0;          // 1134
    static final int sensor_3_high_1 =                  100;        // 1135
    static final int sensor_3_low_2 =                   0;          // 1136
    static final int sensor_3_high_2 =                  100;        // 1137
    static final int sensor_3_low_3 =                   0;          // 1138
    static final int sensor_3_high_3 =                  100;        // 1139
    static final int sensor_3_low_4 =                   0;          // 1140
    static final int sensor_3_high_4 =                  100;        // 1141

    static final int sensor_input_4_config =            1;          // 1142
    static final int sensor_input_4_recog =             3;          // 1143
    static final int sensor_4_analog_low =              0;          // 1144
    static final int  sensor_4_analog_high =            100;        // 1145
    static final int sensor_4_calibration =             0;          // 1146
    static final int sensor_4_multiplier =              1;          // 1147
    static final int sensor_4_low_1 =                   0;          // 1148
    static final int sensor_4_high_1 =                  100;        // 1149
    static final int sensor_4_low_2 =                   0;          // 1150
    static final int sensor_4_high_2 =                  100;        // 1151
    static final int sensor_4_low_3 =                   0;          // 1152
    static final int sensor_4_high_3 =                  100;        // 1153
    static final int sensor_4_low_4 =                   0;          // 1154
    static final int sensor_4_high_4 =                  100;        // 1155
    /* **************** 056 ***** Modbus 1100 ***** 056 **************** */

    /* **************** 010 ***** Modbus 1200 ***** 010 **************** */
    static final int temp_config =                      0;          // 1200
    static final int temp_calibration =                 0;          // 1201
    static final int temp_multiplier =                  2;          // 1202
    static final int temp_low =                         0;          // 1203
    static final int temp_high =                        150;        // 1204
    static final int accel_config =                     0;          // 1205
    static final int accel_x_high =                     0;          // 1206
    static final int accel_y_high =                     0;          // 1207
    static final int accel_z_high =                     0;          // 1208
    static final int accel_combined_high =              0;          // 1209
    /* **************** 010 ***** Modbus 1200 ***** 010 **************** */

    /* **************** 044 ***** Modbus 1300 ***** 044 **************** */
    static final int i2c_1_enable_library =             0;          // 1300
    static final int i2c_1_power_mode =                 0;          // 1301
    static final int i2c_1_interupt_config =            0;          // 1302
    static final int i2c_1_polling_interval =           0;          // 1303
    static final int i2c_1_address =                    0;          // 1304
    static final int i2c_1_calibration =                0;          // 1305
    static final int i2c_1_multiplier =                 2;          // 1306
    static final int i2c_1_low_1 =                      0;          // 1307
    static final int i2c_1_high_1 =                     100;        // 1308
    static final int i2c_1_low_2 =                      0;          // 1309
    static final int i2c_1_high_2 =                     100;        // 1310

    static final int i2c_2_enable_library =             0;          // 1311
    static final int i2c_2_power_mode =                 0;          // 1312
    static final int i2c_2_interupt_config =            0;          // 1313
    static final int i2c_2_polling_interval =           0;          // 1314
    static final int i2c_2_address =                    0;          // 1315
    static final int i2c_2_calibration =                0;          // 1316
    static final int i2c_2_multiplier =                 2;          // 1317
    static final int i2c_2_low_1 =                      0;          // 1318
    static final int i2c_2_high_1 =                     100;        // 1319
    static final int i2c_2_low_2 =                      0;          // 1320
    static final int i2c_2_high_2 =                     100;        // 1321

    static final int i2c_3_enable_library =             0;          // 1322
    static final int i2c_3_power_mode =                 0;          // 1323
    static final int i2c_3_interupt_config =            0;          // 1324
    static final int i2c_3_polling_interval =           0;          // 1325
    static final int i2c_3_address =                 0;          // 1326
    static final int i2c_3_calibration =                0;          // 1327
    static final int i2c_3_multiplier =                 2;          // 1328
    static final int i2c_3_low_1 =                      0;          // 1329
    static final int i2c_3_high_1 =                     100;        // 1330
    static final int i2c_3_low_2 =                      0;          // 1331
    static final int i2c_3_high_2 =                     100;        // 1332

    static final int i2c_4_enable_library =             0;          // 1333
    static final int i2c_4_power_mode =                 0;          // 1334
    static final int i2c_4_interupt_config =            0;          // 1335
    static final int i2c_4_polling_interval =           0;          // 1336
    static final int i2c_4_address =                    0;          // 1337
    static final int i2c_4_calibration =                0;          // 1338
    static final int i2c_4_multiplier =                 2;          // 1339
    static final int i2c_4_low_1 =                      0;          // 1340
    static final int i2c_4_high_1 =                     100;        // 1341
    static final int i2c_4_low_2 =                      0;          // 1342
    static final int i2c_4_high_2 =                     100;        // 1343
    /* **************** 044 ***** Modbus 1300 ***** 044 **************** */

    /* **************** 012 ***** Modbus 1500 ***** 012 **************** */
    static final int data_a1_source =                   zero;       // 1500
    static final int data_a2_source =                   zero;       // 1501
    static final int data_a3_source =                   zero;       // 1502
    static final int data_a4_source =                   zero;       // 1503
    static final int data_a5_source =                   zero;       // 1504
    static final int data_b6_source =                   zero;       // 1505
    static final int data_b7_source =                   zero;       // 1506
    static final int data_b8_source =                   zero;       // 1507
    static final int data_b9_source =                   zero;       // 1508
    static final int data_b10_source =                  zero;       // 1509
    static final int data_b11_source =                  zero;       // 1510
    static final int data_b12_source =                  zero;       // 1511
    /* **************** 012 ***** Modbus 1500 ***** 012 **************** */

    final int AMOUNT = 137;

    int[] address =                {1001,   1002,   1003,   1004,   1005,   1006,   1007,   1009,   1010,   1011,   1012,   1013,
                                    1014,   1020,   1021,   1100,   1101,   1102,   1103,   1104,   1105,   1106,   1107,   1108,
                                    1109,   1110,   1111,   1112,   1113,   1114,   1115,   1116,   1117,   1118,   1119,   1120,
                                    1121,   1122,   1123,   1124,   1125,   1126,   1127,   1128,   1129,   1130,   1131,   1132,
                                    1133,   1134,   1135,   1136,   1137,   1138,   1139,   1140,   1141,   1142,   1143,   1144,
                                    1145,   1146,   1147,   1148,   1149,   1150,   1151,   1152,   1153,   1154,   1155,   1200,
                                    1201,   1202,   1203,   1204,   1205,   1206,   1207,   1208,   1209,   1300,   1301,   1302,
                                    1303,   1304,   1305,   1306,   1307,   1308,   1309,   1310,   1311,   1312,   1313,   1314,
                                    1315,   1316,   1317,   1318,   1319,   1320,   1321,   1322,   1323,   1324,   1325,   1326,
                                    1327,   1328,   1329,   1330,   1331,   1332,   1333,   1334,   1335,   1336,   1337,   1338,
                                    1339,   1340,   1341,   1342,   1343,   1500,   1501,   1502,   1503,   1504,   1505,   1506,
                                    1507,   1508,   1509,   1510,   1511};

    public static int[] values =   {locations_per_day,      time_daily_msg,         auto_location,          a_sensor_msg,
                                    a_sensor_time,          b_sensor_msg,           b_sensor_time,          battery_low_alarm,
                                    command_input_reg,      airplane_mode,          power_config_3v3_1,     power_config_3v3_2,
                                    power_config_15v,       sensor_cycle,           vcc_wait_scan,          sensor_input_1_config,
                                    sensor_input_1_recog,   sensor_1_analog_low,    sensor_1_analog_high,   sensor_1_calibration,
                                    sensor_1_multiplier,    sensor_1_low_1,         sensor_1_high_1,        sensor_1_low_2,
                                    sensor_1_high_2,        sensor_1_low_3,         sensor_1_high_3,        sensor_1_low_4,
                                    sensor_1_high_4,        sensor_input_2_config,  sensor_input_2_recog,   sensor_2_analog_low,
                                    sensor_2_analog_high,   sensor_2_calibration,   sensor_2_multiplier,    sensor_2_low_1,
                                    sensor_2_high_1,        sensor_2_low_2,         sensor_2_high_2,        sensor_2_low_3,
                                    sensor_2_high_3,        sensor_2_low_4,         sensor_2_high_4,        sensor_input_3_config,
                                    sensor_input_3_recog,   sensor_3_analog_low,    sensor_3_analog_high,   sensor_3_calibration,
                                    sensor_3_multiplier,    sensor_3_low_1,         sensor_3_high_1,        sensor_3_low_2,
                                    sensor_3_high_2,        sensor_3_low_3,         sensor_3_high_3,        sensor_3_low_4,
                                    sensor_3_high_4,        sensor_input_4_config,  sensor_input_4_recog,   sensor_4_analog_low,
                                    sensor_4_analog_high,   sensor_4_calibration,   sensor_4_multiplier,    sensor_4_low_1,
                                    sensor_4_high_1,        sensor_4_low_2,         sensor_4_high_2,        sensor_4_low_3,
                                    sensor_4_high_3,        sensor_4_low_4,         sensor_4_high_4,        temp_config,
                                    temp_calibration,       temp_multiplier,        temp_low,               temp_high,
                                    accel_config,           accel_x_high,           accel_y_high,           accel_z_high,
                                    accel_combined_high,    i2c_1_enable_library,   i2c_1_power_mode,       i2c_1_interupt_config,
                                    i2c_1_polling_interval, i2c_1_address,          i2c_1_calibration,      i2c_1_multiplier,
                                    i2c_1_low_1,            i2c_1_high_1,           i2c_1_low_2,            i2c_1_high_2,
                                    i2c_2_enable_library,   i2c_2_power_mode,       i2c_2_interupt_config,  i2c_2_polling_interval,
                                    i2c_2_address,          i2c_2_calibration,      i2c_2_multiplier,       i2c_2_low_1,
                                    i2c_2_high_1,           i2c_2_low_2,            i2c_2_high_2,           i2c_3_enable_library,
                                    i2c_3_power_mode,       i2c_3_interupt_config,  i2c_3_polling_interval, i2c_3_address,
                                    i2c_3_calibration,      i2c_3_multiplier,       i2c_3_low_1,            i2c_3_high_1,
                                    i2c_3_low_2,            i2c_3_high_2,           i2c_4_enable_library,   i2c_4_power_mode,
                                    i2c_4_interupt_config,  i2c_4_polling_interval, i2c_4_address,          i2c_4_calibration,
                                    i2c_4_multiplier,       i2c_4_low_1,            i2c_4_high_1,           i2c_4_low_2,
                                    i2c_4_high_2,           data_a1_source,         data_a2_source,         data_a3_source,
                                    data_a4_source,         data_a5_source,         data_b6_source,         data_b7_source,
                                    data_b8_source,         data_b9_source,         data_b10_source,        data_b11_source,
                                    data_b12_source};
    /* *************** Arrays ************** */

    /* *********** Stored Values *********** */
    int m_address;
    int m_value;
    int m_index;
    Context context;
    /* *********** Stored Values *********** */

    /* ************* SQL Stuff ************* */
    public static final String DATABASE_NAME = "Modbus.db";
    public static final int DATABASE_VERSION = 3;
    public static final String TABLE_NAME = "ProactiveOne";
    public static final String BASE_NAME = "modbus_";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    SQLiteDatabase sql;
    /* ************* SQL Stuff ************* */

    public Modbus(Context c, boolean firstTime) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        sql = getWritableDatabase();
        context = c;
        if(firstTime) {
            sql.execSQL(SQL_DELETE_ENTRIES);
            constructSQLiteTable();
            insertValues();
            readValues();
        }
        else {
            readValues();
        }
    }

    public Modbus(Context c, int add, int val) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        sql = getWritableDatabase();
        if(addressExists(add)) {
            m_value = val;
            save();
        }
        else {
            Log.e("ERROR", "ADDRESS DOES NOT EXIST");
        }
    }

    public Modbus(Context c, int add) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        sql = getWritableDatabase();

        if(addressExists(add))
            m_value = values[m_index];
        else
            Log.e("ERROR", "ADDRESS DOES NOT EXIST");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public void constructSQLiteTable() {
        String str = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY,";
        for(int i = 0; i < AMOUNT - 1; i++)
            str = str + BASE_NAME + Integer.toString(address[i]) + " INTEGER,";
        str = str + BASE_NAME + Integer.toString(address[AMOUNT - 1]) + " INTEGER)";
        sql.execSQL(str);
    }

    public void insertValues() {
        ContentValues cv = new ContentValues();
        for(int i = 0; i < AMOUNT; i++)
            cv.put(BASE_NAME + address[i], values[i]);
        sql.insert(TABLE_NAME, null, cv);
    }

    public void readValues() {
        String str = "SELECT * FROM " + TABLE_NAME;
        Cursor c = sql.rawQuery(str, null);
        c.moveToFirst();
        for(int i = 0; i < AMOUNT; i++)
            values[i] = c.getInt(1 + i);
        c.close();
    }

    public void updateValue() {
        String command = "UPDATE " + TABLE_NAME + " SET " + BASE_NAME + address[m_index] + " = ";
        command = command + values[m_index] + ";";
        sql.execSQL(command);
    }

    public void save() {
        saveToArray();
        updateValue();
        readValues();
    }

    public void saveToArray() {
        values[m_index] = m_value;
    }

    public int getValue() {
        return m_value;
    }

    public boolean addressExists(int add) {
        for(int i = 0; i < AMOUNT; i++) {
            if(address[i] == add) {
                m_address = add;
                m_index = i;
                return true;
            }
        }
        return false;
    }
}
