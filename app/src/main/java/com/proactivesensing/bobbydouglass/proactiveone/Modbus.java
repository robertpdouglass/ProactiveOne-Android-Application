package com.proactivesensing.bobbydouglass.proactiveone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Modbus extends SQLiteOpenHelper {

    /* ****************** 033 ***** Modbus 1000 ***** 033 ****************** */
    static final short unused_1 =                           0;          // 1000
    static final short leg_locations_per_day =              1;          // 1001
    static final short unused_2 =                           0;          // 1002
    static final short unused_3 =                           0;          // 1003
    static final short leg_a_sensor_msg =                   0;          // 1004
    static final short unused_4 =                           0;          // 1005
    static final short leg_b_sensor_msg =                   0;          // 1006
    static final short unused_5 =                           0;          // 1007
    static final short battery_format =                     0;          // 1008
    static final short battery_low_alarm =                  15;         // 1009
    static final short command_input_reg =                  0;          // 1010
    static final short airplane_mode =                      0;          // 1011
    static final short power_config_3v3_1 =                 0;          // 1012
    static final short power_config_3v3_2 =                 0;          // 1013
    static final short power_config_15v =                   0;          // 1014
    static final short unused_6 =                           0;          // 1015
    static final short nfc_pw_enable =                      0;          // 1016
    static final short nfc_pw_set =                         0;          // 1017
    static final short nfc_pw_check =                       0;          // 1018
    static final short sp_prog_version =                    0;          // 1019
    static final short sensor_cycle =                       20;         // 1020
    static final short unused_delete =                      0;          // 1021
    static final short time_zone =                          5;          // 1022
    static final short time_first_gps =                    (6 * 60);    // 1023
    static final short time_between_gps_msgs =              1440;       // 1024
    static final short total_gps_msg =                      1;          // 1025
    static final short auto_location =                      0;          // 1026
    static final short time_first_a_msg =                  (7 * 60);    // 1027
    static final short time_between_a_msgs =                1440;       // 1028
    static final short total_a_msgs =                       0;          // 1029
    static final short time_first_b_msg =                  (8 * 60);    // 1030
    static final short time_between_b_msgs =                1440;       // 1031
    static final short total_b_msgs =                       0;          // 1032
    /* ****************** 033 ***** Modbus 1000 ***** 033 ****************** */

    /* ****************** 056 ***** Modbus 1100 ***** 056 ****************** */
    static final short sensor_1_config =                    1;          // 1100
    static final short sensor_1_power_source =              0;          // 1101
    static final short sensor_1_power_wait =                2;          // 1102
    static final short sensor_1_skip_count =                0;          // 1103
    static final short sensor_1_recog_time =                3;          // 1104
    static final short sensor_1_analog_low =                0;          // 1105
    static final short sensor_1_analog_high =               100;        // 1106
    static final short sensor_1_calibration =               0;          // 1107
    static final short sensor_1_multiplier =                1;          // 1108
    static final short sensor_1_unused_a =                  0;          // 1109
    static final short sensor_1_unused_b =                  0;          // 1110
    static final short sensor_1_unused_c =                  0;          // 1111
    static final short sensor_1_low_1 =                     0;          // 1112
    static final short sensor_1_high_1 =                    100;        // 1113

    static final short sensor_2_config =                    1;          // 1114
    static final short sensor_2_power_source =              0;          // 1115
    static final short sensor_2_power_wait =                2;          // 1116
    static final short sensor_2_skip_count =                0;          // 1117
    static final short sensor_2_recog_time =                3;          // 1118
    static final short sensor_2_analog_low =                0;          // 1119
    static final short sensor_2_analog_high =               100;        // 1120
    static final short sensor_2_calibration =               0;          // 1121
    static final short sensor_2_multiplier =                1;          // 1122
    static final short sensor_2_unused_a =                  0;          // 1123
    static final short sensor_2_unused_b =                  0;          // 1124
    static final short sensor_2_unused_c =                  0;          // 1125
    static final short sensor_2_low_1 =                     0;          // 1126
    static final short sensor_2_high_1 =                    100;        // 1127

    static final short sensor_3_config =                    1;          // 1128
    static final short sensor_3_power_source =              0;          // 1129
    static final short sensor_3_power_wait =                2;          // 1130
    static final short sensor_3_skip_count =                0;          // 1131
    static final short sensor_3_recog_time =                3;          // 1132
    static final short sensor_3_analog_low =                0;          // 1133
    static final short sensor_3_analog_high =               100;        // 1134
    static final short sensor_3_calibration =               0;          // 1135
    static final short sensor_3_multiplier =                1;          // 1136
    static final short sensor_3_unused_a =                  0;          // 1137
    static final short sensor_3_unused_b =                  0;          // 1138
    static final short sensor_3_unused_c =                  0;          // 1139
    static final short sensor_3_low_1 =                     0;          // 1140
    static final short sensor_3_high_1 =                    100;        // 1141

    static final short sensor_4_config =                    1;          // 1142
    static final short sensor_4_power_source =              0;          // 1143
    static final short sensor_4_power_wait =                2;          // 1144
    static final short sensor_4_skip_count =                0;          // 1145
    static final short sensor_4_recog_time =                3;          // 1146
    static final short sensor_4_analog_low =                0;          // 1147
    static final short sensor_4_analog_high =               100;        // 1148
    static final short sensor_4_calibration =               0;          // 1149
    static final short sensor_4_multiplier =                1;          // 1150
    static final short sensor_4_unused_a =                  0;          // 1151
    static final short sensor_4_unused_b =                  0;          // 1152
    static final short sensor_4_unused_c =                  0;          // 1153
    static final short sensor_4_low_1 =                     0;          // 1154
    static final short sensor_4_high_1 =                    100;        // 1155
    /* ****************** 056 ***** Modbus 1100 ***** 056 ****************** */

    /* ****************** 016 ***** Modbus 1200 ***** 016 ****************** */
    static final short temp_config =                        1;          // 1200
    static final short temp_skip_count =                    0;          // 1201
    static final short temp_calibration =                   0;          // 1202
    static final short temp_multiplier =                    2;          // 1203
    static final short temp_low =                           0;          // 1204
    static final short temp_high =                          150;        // 1205
    static final short temp_unused_a =                      0;          // 1206
    static final short temp_unused_b =                      0;          // 1207
    static final short accel_config =                       0;          // 1208
    static final short accel_skip_count =                   0;          // 1209
    static final short accel_x_high =                       0;          // 1210
    static final short accel_y_high =                       0;          // 1211
    static final short accel_z_high =                       0;          // 1212
    static final short accel_combined_high =                0;          // 1213
    static final short accel_unused_a =                     0;          // 1214
    static final short accel_unused_b =                     0;          // 1215
    /* ****************** 016 ***** Modbus 1200 ***** 016 ****************** */

    /* ****************** 056 ***** Modbus 1300 ***** 056 ****************** */
    static final short i2c_1_enable_library =               0;          // 1300
    static final short i2c_1_power_mode =                   0;          // 1301
    static final short i2c_1_power_source =                 0;          // 1302
    static final short i2c_1_power_wait =                   2;          // 1303
    static final short i2c_1_shorterupt_mode =              0;          // 1304
    static final short i2c_1_shorterupt_channel =           0;          // 1305
    static final short i2c_1_skip_count =                   0;          // 1306
    static final short i2c_1_address =                      0;          // 1307
    static final short i2c_1_calibration =                  0;          // 1308
    static final short i2c_1_multiplier =                   2;          // 1309
    static final short i2c_1_unused_a =                     0;          // 1310
    static final short i2c_1_unused_b =                     0;          // 1311
    static final short i2c_1_low_1 =                        0;          // 1312
    static final short i2c_1_high_1 =                       100;        // 1313

    static final short i2c_2_enable_library =               0;          // 1314
    static final short i2c_2_power_mode =                   0;          // 1315
    static final short i2c_2_power_source =                 0;          // 1316
    static final short i2c_2_power_wait =                   2;          // 1317
    static final short i2c_2_shorterupt_mode =              0;          // 1318
    static final short i2c_2_shorterupt_channel =           0;          // 1319
    static final short i2c_2_skip_count =                   0;          // 1320
    static final short i2c_2_address =                      0;          // 1321
    static final short i2c_2_calibration =                  0;          // 1322
    static final short i2c_2_multiplier =                   2;          // 1323
    static final short i2c_2_unused_a =                     0;          // 1324
    static final short i2c_2_unused_b =                     0;          // 1325
    static final short i2c_2_low_1 =                        0;          // 1326
    static final short i2c_2_high_1 =                       100;        // 1327

    static final short i2c_3_enable_library =               0;          // 1328
    static final short i2c_3_power_mode =                   0;          // 1329
    static final short i2c_3_power_source =                 0;          // 1330
    static final short i2c_3_power_wait =                   2;          // 1331
    static final short i2c_3_shorterupt_mode =              0;          // 1332
    static final short i2c_3_shorterupt_channel =           0;          // 1333
    static final short i2c_3_skip_count =                   0;          // 1334
    static final short i2c_3_address =                      0;          // 1335
    static final short i2c_3_calibration =                  0;          // 1336
    static final short i2c_3_multiplier =                   2;          // 1337
    static final short i2c_3_unused_a =                     0;          // 1338
    static final short i2c_3_unused_b =                     0;          // 1339
    static final short i2c_3_low_1 =                        0;          // 1340
    static final short i2c_3_high_1 =                       100;        // 1341

    static final short i2c_4_enable_library =               0;          // 1342
    static final short i2c_4_power_mode =                   0;          // 1343
    static final short i2c_4_power_source =                 0;          // 1344
    static final short i2c_4_power_wait =                   2;          // 1345
    static final short i2c_4_shorterupt_mode =              0;          // 1346
    static final short i2c_4_shorterupt_channel =           0;          // 1347
    static final short i2c_4_skip_count =                   0;          // 1348
    static final short i2c_4_address =                      0;          // 1349
    static final short i2c_4_calibration =                  0;          // 1350
    static final short i2c_4_multiplier =                   2;          // 1351
    static final short i2c_4_unused_a =                     0;          // 1352
    static final short i2c_4_unused_b =                     0;          // 1353
    static final short i2c_4_low_1 =                        0;          // 1354
    static final short i2c_4_high_1 =                       100;        // 1355
    /* ****************** 056 ***** Modbus 1300 ***** 056 ****************** */

    /* ****************** 024 ***** Modbus 1500 ***** 024 ****************** */
    static final short data_a1_selection =                  0;          // 1500
    static final short data_a1_index =                      0;          // 1501
    static final short data_a2_selection =                  0;          // 1502
    static final short data_a2_index =                      0;          // 1503
    static final short data_a3_selection =                  0;          // 1504
    static final short data_a3_index =                      0;          // 1505
    static final short data_a4_selection =                  0;          // 1506
    static final short data_a4_index =                      0;          // 1507
    static final short data_a5_selection =                  0;          // 1508
    static final short data_a5_index =                      0;          // 1509
    static final short data_b6_selection =                  0;          // 1510
    static final short data_b6_index =                      0;          // 1511
    static final short data_b7_selection =                  0;          // 1512
    static final short data_b7_index =                      0;          // 1513
    static final short data_b8_selection =                  0;          // 1514
    static final short data_b8_index =                      0;          // 1515
    static final short data_b9_selection =                  0;          // 1516
    static final short data_b9_index =                      0;          // 1517
    static final short data_b10_selection =                 0;          // 1518
    static final short data_b10_index =                     0;          // 1519
    static final short data_c11_selection =                 0;          // 1520
    static final short data_c11_index =                     0;          // 1521
    static final short data_c12_selection =                 0;          // 1522
    static final short data_c12_index =                     0;          // 1523
    /* ****************** 024 ***** Modbus 1500 ***** 024 ****************** */

    /* ****************** 120 ***** Modbus 1700 ***** 120 ****************** */
    static final short virtual_1_config =                   0;          // 1700
    static final short virtual_1_skip_count =               0;          // 1701
    static final short virtual_1_data_type_a =              0;          // 1702
    static final short virtual_1_data_index_a =             0;          // 1703
    static final short virtual_1_data_type_b =              0;          // 1704
    static final short virtual_1_data_index_b =             0;          // 1705
    static final short virtual_1_data_type_c =              0;          // 1706
    static final short virtual_1_data_index_c =             0;          // 1707
    static final short virtual_1_data_type_d =              0;          // 1708
    static final short virtual_1_data_index_d =             0;          // 1709
    static final short virtual_1_parameter_1 =              0;          // 1710
    static final short virtual_1_parameter_2 =              0;          // 1711
    static final short virtual_1_unused_a =                 0;          // 1712
    static final short virtual_1_unused_b =                 0;          // 1713
    static final short virtual_1_output_result =            0;          // 1714

    static final short virtual_2_config =                   0;          // 1715
    static final short virtual_2_skip_count =               0;          // 1716
    static final short virtual_2_data_type_a =              0;          // 1717
    static final short virtual_2_data_index_a =             0;          // 1718
    static final short virtual_2_data_type_b =              0;          // 1719
    static final short virtual_2_data_index_b =             0;          // 1720
    static final short virtual_2_data_type_c =              0;          // 1721
    static final short virtual_2_data_index_c =             0;          // 1722
    static final short virtual_2_data_type_d =              0;          // 1723
    static final short virtual_2_data_index_d =             0;          // 1724
    static final short virtual_2_parameter_1 =              0;          // 1725
    static final short virtual_2_parameter_2 =              0;          // 1726
    static final short virtual_2_unused_a =                 0;          // 1727
    static final short virtual_2_unused_b =                 0;          // 1728
    static final short virtual_2_output_result =            0;          // 1729

    static final short virtual_3_config =                   0;          // 1730
    static final short virtual_3_skip_count =               0;          // 1731
    static final short virtual_3_data_type_a =              0;          // 1732
    static final short virtual_3_data_index_a =             0;          // 1733
    static final short virtual_3_data_type_b =              0;          // 1734
    static final short virtual_3_data_index_b =             0;          // 1735
    static final short virtual_3_data_type_c =              0;          // 1736
    static final short virtual_3_data_index_c =             0;          // 1737
    static final short virtual_3_data_type_d =              0;          // 1738
    static final short virtual_3_data_index_d =             0;          // 1739
    static final short virtual_3_parameter_1 =              0;          // 1740
    static final short virtual_3_parameter_2 =              0;          // 1741
    static final short virtual_3_unused_a =                 0;          // 1742
    static final short virtual_3_unused_b =                 0;          // 1743
    static final short virtual_3_output_result =            0;          // 1744

    static final short virtual_4_config =                   0;          // 1745
    static final short virtual_4_skip_count =               0;          // 1746
    static final short virtual_4_data_type_a =              0;          // 1747
    static final short virtual_4_data_index_a =             0;          // 1748
    static final short virtual_4_data_type_b =              0;          // 1749
    static final short virtual_4_data_index_b =             0;          // 1750
    static final short virtual_4_data_type_c =              0;          // 1751
    static final short virtual_4_data_index_c =             0;          // 1752
    static final short virtual_4_data_type_d =              0;          // 1753
    static final short virtual_4_data_index_d =             0;          // 1754
    static final short virtual_4_parameter_1 =              0;          // 1755
    static final short virtual_4_parameter_2 =              0;          // 1756
    static final short virtual_4_unused_a =                 0;          // 1757
    static final short virtual_4_unused_b =                 0;          // 1758
    static final short virtual_4_output_result =            0;          // 1759

    static final short virtual_5_config =                   0;          // 1760
    static final short virtual_5_skip_count =               0;          // 1761
    static final short virtual_5_data_type_a =              0;          // 1762
    static final short virtual_5_data_index_a =             0;          // 1763
    static final short virtual_5_data_type_b =              0;          // 1764
    static final short virtual_5_data_index_b =             0;          // 1765
    static final short virtual_5_data_type_c =              0;          // 1766
    static final short virtual_5_data_index_c =             0;          // 1767
    static final short virtual_5_data_type_d =              0;          // 1768
    static final short virtual_5_data_index_d =             0;          // 1769
    static final short virtual_5_parameter_1 =              0;          // 1770
    static final short virtual_5_parameter_2 =              0;          // 1771
    static final short virtual_5_unused_a =                 0;          // 1772
    static final short virtual_5_unused_b =                 0;          // 1773
    static final short virtual_5_output_result =            0;          // 1774

    static final short virtual_6_config =                   0;          // 1775
    static final short virtual_6_skip_count =               0;          // 1776
    static final short virtual_6_data_type_a =              0;          // 1777
    static final short virtual_6_data_index_a =             0;          // 1778
    static final short virtual_6_data_type_b =              0;          // 1779
    static final short virtual_6_data_index_b =             0;          // 1780
    static final short virtual_6_data_type_c =              0;          // 1781
    static final short virtual_6_data_index_c =             0;          // 1782
    static final short virtual_6_data_type_d =              0;          // 1783
    static final short virtual_6_data_index_d =             0;          // 1784
    static final short virtual_6_parameter_1 =              0;          // 1785
    static final short virtual_6_parameter_2 =              0;          // 1786
    static final short virtual_6_unused_a =                 0;          // 1787
    static final short virtual_6_unused_b =                 0;          // 1788
    static final short virtual_6_output_result =            0;          // 1789

    static final short virtual_7_config =                   0;          // 1790
    static final short virtual_7_skip_count =               0;          // 1791
    static final short virtual_7_data_type_a =              0;          // 1792
    static final short virtual_7_data_index_a =             0;          // 1793
    static final short virtual_7_data_type_b =              0;          // 1794
    static final short virtual_7_data_index_b =             0;          // 1795
    static final short virtual_7_data_type_c =              0;          // 1796
    static final short virtual_7_data_index_c =             0;          // 1797
    static final short virtual_7_data_type_d =              0;          // 1798
    static final short virtual_7_data_index_d =             0;          // 1799
    static final short virtual_7_parameter_1 =              0;          // 1800
    static final short virtual_7_parameter_2 =              0;          // 1801
    static final short virtual_7_unused_a =                 0;          // 1802
    static final short virtual_7_unused_b =                 0;          // 1803
    static final short virtual_7_output_result =            0;          // 1804

    static final short virtual_8_config =                   0;          // 1805
    static final short virtual_8_skip_count =               0;          // 1806
    static final short virtual_8_data_type_a =              0;          // 1807
    static final short virtual_8_data_index_a =             0;          // 1808
    static final short virtual_8_data_type_b =              0;          // 1809
    static final short virtual_8_data_index_b =             0;          // 1810
    static final short virtual_8_data_type_c =              0;          // 1811
    static final short virtual_8_data_index_c =             0;          // 1812
    static final short virtual_8_data_type_d =              0;          // 1813
    static final short virtual_8_data_index_d =             0;          // 1814
    static final short virtual_8_parameter_1 =              0;          // 1815
    static final short virtual_8_parameter_2 =              0;          // 1816
    static final short virtual_8_unused_a =                 0;          // 1817
    static final short virtual_8_unused_b =                 0;          // 1818
    static final short virtual_8_output_result =            0;          // 1819
    /* ****************** 120 ***** Modbus 1700 ***** 120 ****************** */

    public static final short Size =        305;

    public static int[] address =          {1000,   1001,   1002,   1003,   1004,   1005,   1006,   1007,   1008,   1009,   1010,   1011,
                                            1012,   1013,   1014,   1015,   1016,   1017,   1018,   1019,   1020,   1021,   1022,   1023,
                                            1024,   1025,   1026,   1027,   1028,   1029,   1030,   1031,   1032,   1100,   1101,   1102,
                                            1103,   1104,   1105,   1106,   1107,   1108,   1109,   1110,   1111,   1112,   1113,   1114,
                                            1115,   1116,   1117,   1118,   1119,   1120,   1121,   1122,   1123,   1124,   1125,   1126,
                                            1127,   1128,   1129,   1130,   1131,   1132,   1133,   1134,   1135,   1136,   1137,   1138,
                                            1139,   1140,   1141,   1142,   1143,   1144,   1145,   1146,   1147,   1148,   1149,   1150,
                                            1151,   1152,   1153,   1154,   1155,   1200,   1201,   1202,   1203,   1204,   1205,   1206,
                                            1207,   1208,   1209,   1210,   1211,   1212,   1213,   1214,   1215,   1300,   1301,   1302,
                                            1303,   1304,   1305,   1306,   1307,   1308,   1309,   1310,   1311,   1312,   1313,   1314,
                                            1315,   1316,   1317,   1318,   1319,   1320,   1321,   1322,   1323,   1324,   1325,   1326,
                                            1327,   1328,   1329,   1330,   1331,   1332,   1333,   1334,   1335,   1336,   1337,   1338,
                                            1339,   1340,   1341,   1342,   1343,   1344,   1345,   1346,   1347,   1348,   1349,   1350,
                                            1351,   1352,   1353,   1354,   1355,   1500,   1501,   1502,   1503,   1504,   1505,   1506,
                                            1507,   1508,   1509,   1510,   1511,   1512,   1513,   1514,   1515,   1516,   1517,   1518,
                                            1519,   1520,   1521,   1522,   1523,   1700,   1701,   1702,   1703,   1704,   1705,   1706,
                                            1707,   1708,   1709,   1710,   1711,   1712,   1713,   1714,   1715,   1716,   1717,   1718,
                                            1719,   1720,   1721,   1722,   1723,   1724,   1725,   1726,   1727,   1728,   1729,   1730,
                                            1731,   1732,   1733,   1734,   1735,   1736,   1737,   1738,   1739,   1740,   1741,   1742,
                                            1743,   1744,   1745,   1746,   1747,   1748,   1749,   1750,   1751,   1752,   1753,   1754,
                                            1755,   1756,   1757,   1758,   1759,   1760,   1761,   1762,   1763,   1764,   1765,   1766,
                                            1767,   1768,   1769,   1770,   1771,   1772,   1773,   1774,   1775,   1776,   1777,   1778,
                                            1779,   1780,   1781,   1782,   1783,   1784,   1785,   1786,   1787,   1788,   1789,   1790,
                                            1791,   1792,   1793,   1794,   1795,   1796,   1797,   1798,   1799,   1800,   1801,   1802,
                                            1803,   1804,   1805,   1806,   1807,   1808,   1809,   1810,   1811,   1812,   1813,   1814,
                                            1815,   1816,   1817,   1818,   1819};

    public static final short[] defaults = {unused_1,                   leg_locations_per_day,      unused_2,                   unused_3,
                                            leg_a_sensor_msg,           unused_4,                   leg_b_sensor_msg,           unused_5,
                                            battery_format,             battery_low_alarm,          command_input_reg,          airplane_mode,
                                            power_config_3v3_1,         power_config_3v3_2,         power_config_15v,           unused_6,
                                            nfc_pw_enable,              nfc_pw_set,                 nfc_pw_check,               sp_prog_version,
                                            sensor_cycle,               unused_delete,              time_zone,                  time_first_gps,
                                            time_between_gps_msgs,      total_gps_msg,              auto_location,              time_first_a_msg,
                                            time_between_a_msgs,        total_a_msgs,               time_first_b_msg,           time_between_b_msgs,
                                            total_b_msgs,               sensor_1_config,            sensor_1_power_source,      sensor_1_power_wait,
                                            sensor_1_skip_count,        sensor_1_recog_time,        sensor_1_analog_low,        sensor_1_analog_high,
                                            sensor_1_calibration,       sensor_1_multiplier,        sensor_1_unused_a,          sensor_1_unused_b,
                                            sensor_1_unused_c,          sensor_1_low_1,             sensor_1_high_1,            sensor_2_config,
                                            sensor_2_power_source,      sensor_2_power_wait,        sensor_2_skip_count,        sensor_2_recog_time,
                                            sensor_2_analog_low,        sensor_2_analog_high,       sensor_2_calibration,       sensor_2_multiplier,
                                            sensor_2_unused_a,          sensor_2_unused_b,          sensor_2_unused_c,          sensor_2_low_1,
                                            sensor_2_high_1,            sensor_3_config,            sensor_3_power_source,      sensor_3_power_wait,
                                            sensor_3_skip_count,        sensor_3_recog_time,        sensor_3_analog_low,        sensor_3_analog_high,
                                            sensor_3_calibration,       sensor_3_multiplier,        sensor_3_unused_a,          sensor_3_unused_b,
                                            sensor_3_unused_c,          sensor_3_low_1,             sensor_3_high_1,            sensor_4_config,
                                            sensor_4_power_source,      sensor_4_power_wait,        sensor_4_skip_count,        sensor_4_recog_time,
                                            sensor_4_analog_low,        sensor_4_analog_high,       sensor_4_calibration,       sensor_4_multiplier,
                                            sensor_4_unused_a,          sensor_4_unused_b,          sensor_4_unused_c,          sensor_4_low_1,
                                            sensor_4_high_1,            temp_config,                temp_skip_count,            temp_calibration,
                                            temp_multiplier,            temp_low,                   temp_high,                  temp_unused_a,
                                            temp_unused_b,              accel_config,               accel_skip_count,           accel_x_high,
                                            accel_y_high,               accel_z_high,               accel_combined_high,        accel_unused_a,
                                            accel_unused_b,             i2c_1_enable_library,       i2c_1_power_mode,           i2c_1_power_source,
                                            i2c_1_power_wait,           i2c_1_shorterupt_mode,      i2c_1_shorterupt_channel,   i2c_1_skip_count,
                                            i2c_1_address,              i2c_1_calibration,          i2c_1_multiplier,           i2c_1_unused_a,
                                            i2c_1_unused_b,             i2c_1_low_1,                i2c_1_high_1,               i2c_2_enable_library,
                                            i2c_2_power_mode,           i2c_2_power_source,         i2c_2_power_wait,           i2c_2_shorterupt_mode,
                                            i2c_2_shorterupt_channel,   i2c_2_skip_count,           i2c_2_address,              i2c_2_calibration,
                                            i2c_2_multiplier,           i2c_2_unused_a,             i2c_2_unused_b,             i2c_2_low_1,
                                            i2c_2_high_1,               i2c_3_enable_library,       i2c_3_power_mode,           i2c_3_power_source,
                                            i2c_3_power_wait,           i2c_3_shorterupt_mode,      i2c_3_shorterupt_channel,   i2c_3_skip_count,
                                            i2c_3_address,              i2c_3_calibration,          i2c_3_multiplier,           i2c_3_unused_a,
                                            i2c_3_unused_b,             i2c_3_low_1,                i2c_3_high_1,               i2c_4_enable_library,
                                            i2c_4_power_mode,           i2c_4_power_source,         i2c_4_power_wait,           i2c_4_shorterupt_mode,
                                            i2c_4_shorterupt_channel,   i2c_4_skip_count,           i2c_4_address,              i2c_4_calibration,
                                            i2c_4_multiplier,           i2c_4_unused_a,             i2c_4_unused_b,             i2c_4_low_1,
                                            i2c_4_high_1,               data_a1_selection,          data_a1_index,              data_a2_selection,
                                            data_a2_index,              data_a3_selection,          data_a3_index,              data_a4_selection,
                                            data_a4_index,              data_a5_selection,          data_a5_index,              data_b6_selection,
                                            data_b6_index,              data_b7_selection,          data_b7_index,              data_b8_selection,
                                            data_b8_index,              data_b9_selection,          data_b9_index,              data_b10_selection,
                                            data_b10_index,             data_c11_selection,         data_c11_index,             data_c12_selection,
                                            data_c12_index,             virtual_1_config,           virtual_1_skip_count,       virtual_1_data_type_a,
                                            virtual_1_data_index_a,     virtual_1_data_type_b,      virtual_1_data_index_b,     virtual_1_data_type_c,
                                            virtual_1_data_index_c,     virtual_1_data_type_d,      virtual_1_data_index_d,     virtual_1_parameter_1,
                                            virtual_1_parameter_2,      virtual_1_unused_a,         virtual_1_unused_b,         virtual_1_output_result,
                                            virtual_2_config,           virtual_2_skip_count,       virtual_2_data_type_a,      virtual_2_data_index_a,
                                            virtual_2_data_type_b,      virtual_2_data_index_b,     virtual_2_data_type_c,      virtual_2_data_index_c,
                                            virtual_2_data_type_d,      virtual_2_data_index_d,     virtual_2_parameter_1,      virtual_2_parameter_2,
                                            virtual_2_unused_a,         virtual_2_unused_b,         virtual_2_output_result,    virtual_3_config,
                                            virtual_3_skip_count,       virtual_3_data_type_a,      virtual_3_data_index_a,     virtual_3_data_type_b,
                                            virtual_3_data_index_b,     virtual_3_data_type_c,      virtual_3_data_index_c,     virtual_3_data_type_d,
                                            virtual_3_data_index_d,     virtual_3_parameter_1,      virtual_3_parameter_2,      virtual_3_unused_a,
                                            virtual_3_unused_b,         virtual_3_output_result,    virtual_4_config,           virtual_4_skip_count,
                                            virtual_4_data_type_a,      virtual_4_data_index_a,     virtual_4_data_type_b,      virtual_4_data_index_b,
                                            virtual_4_data_type_c,      virtual_4_data_index_c,     virtual_4_data_type_d,      virtual_4_data_index_d,
                                            virtual_4_parameter_1,      virtual_4_parameter_2,      virtual_4_unused_a,         virtual_4_unused_b,
                                            virtual_4_output_result,    virtual_5_config,           virtual_5_skip_count,       virtual_5_data_type_a,
                                            virtual_5_data_index_a,     virtual_5_data_type_b,      virtual_5_data_index_b,     virtual_5_data_type_c,
                                            virtual_5_data_index_c,     virtual_5_data_type_d,      virtual_5_data_index_d,     virtual_5_parameter_1,
                                            virtual_5_parameter_2,      virtual_5_unused_a,         virtual_5_unused_b,         virtual_5_output_result,
                                            virtual_6_config,           virtual_6_skip_count,       virtual_6_data_type_a,      virtual_6_data_index_a,
                                            virtual_6_data_type_b,      virtual_6_data_index_b,     virtual_6_data_type_c,      virtual_6_data_index_c,
                                            virtual_6_data_type_d,      virtual_6_data_index_d,     virtual_6_parameter_1,      virtual_6_parameter_2,
                                            virtual_6_unused_a,         virtual_6_unused_b,         virtual_6_output_result,    virtual_7_config,
                                            virtual_7_skip_count,       virtual_7_data_type_a,      virtual_7_data_index_a,     virtual_7_data_type_b,
                                            virtual_7_data_index_b,     virtual_7_data_type_c,      virtual_7_data_index_c,     virtual_7_data_type_d,
                                            virtual_7_data_index_d,     virtual_7_parameter_1,      virtual_7_parameter_2,      virtual_7_unused_a,
                                            virtual_7_unused_b,         virtual_7_output_result,    virtual_8_config,           virtual_8_skip_count,
                                            virtual_8_data_type_a,      virtual_8_data_index_a,     virtual_8_data_type_b,      virtual_8_data_index_b,
                                            virtual_8_data_type_c,      virtual_8_data_index_c,     virtual_8_data_type_d,      virtual_8_data_index_d,
                                            virtual_8_parameter_1,      virtual_8_parameter_2,      virtual_8_unused_a,         virtual_8_unused_b,
                                            virtual_8_output_result};

    public static short[] values =         {unused_1,                   leg_locations_per_day,      unused_2,                   unused_3,
                                            leg_a_sensor_msg,           unused_4,                   leg_b_sensor_msg,           unused_5,
                                            battery_format,             battery_low_alarm,          command_input_reg,          airplane_mode,
                                            power_config_3v3_1,         power_config_3v3_2,         power_config_15v,           unused_6,
                                            nfc_pw_enable,              nfc_pw_set,                 nfc_pw_check,               sp_prog_version,
                                            sensor_cycle,               unused_delete,              time_zone,                  time_first_gps,
                                            time_between_gps_msgs,      total_gps_msg,              auto_location,              time_first_a_msg,
                                            time_between_a_msgs,        total_a_msgs,               time_first_b_msg,           time_between_b_msgs,
                                            total_b_msgs,               sensor_1_config,            sensor_1_power_source,      sensor_1_power_wait,
                                            sensor_1_skip_count,        sensor_1_recog_time,        sensor_1_analog_low,        sensor_1_analog_high,
                                            sensor_1_calibration,       sensor_1_multiplier,        sensor_1_unused_a,          sensor_1_unused_b,
                                            sensor_1_unused_c,          sensor_1_low_1,             sensor_1_high_1,            sensor_2_config,
                                            sensor_2_power_source,      sensor_2_power_wait,        sensor_2_skip_count,        sensor_2_recog_time,
                                            sensor_2_analog_low,        sensor_2_analog_high,       sensor_2_calibration,       sensor_2_multiplier,
                                            sensor_2_unused_a,          sensor_2_unused_b,          sensor_2_unused_c,          sensor_2_low_1,
                                            sensor_2_high_1,            sensor_3_config,            sensor_3_power_source,      sensor_3_power_wait,
                                            sensor_3_skip_count,        sensor_3_recog_time,        sensor_3_analog_low,        sensor_3_analog_high,
                                            sensor_3_calibration,       sensor_3_multiplier,        sensor_3_unused_a,          sensor_3_unused_b,
                                            sensor_3_unused_c,          sensor_3_low_1,             sensor_3_high_1,            sensor_4_config,
                                            sensor_4_power_source,      sensor_4_power_wait,        sensor_4_skip_count,        sensor_4_recog_time,
                                            sensor_4_analog_low,        sensor_4_analog_high,       sensor_4_calibration,       sensor_4_multiplier,
                                            sensor_4_unused_a,          sensor_4_unused_b,          sensor_4_unused_c,          sensor_4_low_1,
                                            sensor_4_high_1,            temp_config,                temp_skip_count,            temp_calibration,
                                            temp_multiplier,            temp_low,                   temp_high,                  temp_unused_a,
                                            temp_unused_b,              accel_config,               accel_skip_count,           accel_x_high,
                                            accel_y_high,               accel_z_high,               accel_combined_high,        accel_unused_a,
                                            accel_unused_b,             i2c_1_enable_library,       i2c_1_power_mode,           i2c_1_power_source,
                                            i2c_1_power_wait,           i2c_1_shorterupt_mode,      i2c_1_shorterupt_channel,   i2c_1_skip_count,
                                            i2c_1_address,              i2c_1_calibration,          i2c_1_multiplier,           i2c_1_unused_a,
                                            i2c_1_unused_b,             i2c_1_low_1,                i2c_1_high_1,               i2c_2_enable_library,
                                            i2c_2_power_mode,           i2c_2_power_source,         i2c_2_power_wait,           i2c_2_shorterupt_mode,
                                            i2c_2_shorterupt_channel,   i2c_2_skip_count,           i2c_2_address,              i2c_2_calibration,
                                            i2c_2_multiplier,           i2c_2_unused_a,             i2c_2_unused_b,             i2c_2_low_1,
                                            i2c_2_high_1,               i2c_3_enable_library,       i2c_3_power_mode,           i2c_3_power_source,
                                            i2c_3_power_wait,           i2c_3_shorterupt_mode,      i2c_3_shorterupt_channel,   i2c_3_skip_count,
                                            i2c_3_address,              i2c_3_calibration,          i2c_3_multiplier,           i2c_3_unused_a,
                                            i2c_3_unused_b,             i2c_3_low_1,                i2c_3_high_1,               i2c_4_enable_library,
                                            i2c_4_power_mode,           i2c_4_power_source,         i2c_4_power_wait,           i2c_4_shorterupt_mode,
                                            i2c_4_shorterupt_channel,   i2c_4_skip_count,           i2c_4_address,              i2c_4_calibration,
                                            i2c_4_multiplier,           i2c_4_unused_a,             i2c_4_unused_b,             i2c_4_low_1,
                                            i2c_4_high_1,               data_a1_selection,          data_a1_index,              data_a2_selection,
                                            data_a2_index,              data_a3_selection,          data_a3_index,              data_a4_selection,
                                            data_a4_index,              data_a5_selection,          data_a5_index,              data_b6_selection,
                                            data_b6_index,              data_b7_selection,          data_b7_index,              data_b8_selection,
                                            data_b8_index,              data_b9_selection,          data_b9_index,              data_b10_selection,
                                            data_b10_index,             data_c11_selection,         data_c11_index,             data_c12_selection,
                                            data_c12_index,             virtual_1_config,           virtual_1_skip_count,       virtual_1_data_type_a,
                                            virtual_1_data_index_a,     virtual_1_data_type_b,      virtual_1_data_index_b,     virtual_1_data_type_c,
                                            virtual_1_data_index_c,     virtual_1_data_type_d,      virtual_1_data_index_d,     virtual_1_parameter_1,
                                            virtual_1_parameter_2,      virtual_1_unused_a,         virtual_1_unused_b,         virtual_1_output_result,
                                            virtual_2_config,           virtual_2_skip_count,       virtual_2_data_type_a,      virtual_2_data_index_a,
                                            virtual_2_data_type_b,      virtual_2_data_index_b,     virtual_2_data_type_c,      virtual_2_data_index_c,
                                            virtual_2_data_type_d,      virtual_2_data_index_d,     virtual_2_parameter_1,      virtual_2_parameter_2,
                                            virtual_2_unused_a,         virtual_2_unused_b,         virtual_2_output_result,    virtual_3_config,
                                            virtual_3_skip_count,       virtual_3_data_type_a,      virtual_3_data_index_a,     virtual_3_data_type_b,
                                            virtual_3_data_index_b,     virtual_3_data_type_c,      virtual_3_data_index_c,     virtual_3_data_type_d,
                                            virtual_3_data_index_d,     virtual_3_parameter_1,      virtual_3_parameter_2,      virtual_3_unused_a,
                                            virtual_3_unused_b,         virtual_3_output_result,    virtual_4_config,           virtual_4_skip_count,
                                            virtual_4_data_type_a,      virtual_4_data_index_a,     virtual_4_data_type_b,      virtual_4_data_index_b,
                                            virtual_4_data_type_c,      virtual_4_data_index_c,     virtual_4_data_type_d,      virtual_4_data_index_d,
                                            virtual_4_parameter_1,      virtual_4_parameter_2,      virtual_4_unused_a,         virtual_4_unused_b,
                                            virtual_4_output_result,    virtual_5_config,           virtual_5_skip_count,       virtual_5_data_type_a,
                                            virtual_5_data_index_a,     virtual_5_data_type_b,      virtual_5_data_index_b,     virtual_5_data_type_c,
                                            virtual_5_data_index_c,     virtual_5_data_type_d,      virtual_5_data_index_d,     virtual_5_parameter_1,
                                            virtual_5_parameter_2,      virtual_5_unused_a,         virtual_5_unused_b,         virtual_5_output_result,
                                            virtual_6_config,           virtual_6_skip_count,       virtual_6_data_type_a,      virtual_6_data_index_a,
                                            virtual_6_data_type_b,      virtual_6_data_index_b,     virtual_6_data_type_c,      virtual_6_data_index_c,
                                            virtual_6_data_type_d,      virtual_6_data_index_d,     virtual_6_parameter_1,      virtual_6_parameter_2,
                                            virtual_6_unused_a,         virtual_6_unused_b,         virtual_6_output_result,    virtual_7_config,
                                            virtual_7_skip_count,       virtual_7_data_type_a,      virtual_7_data_index_a,     virtual_7_data_type_b,
                                            virtual_7_data_index_b,     virtual_7_data_type_c,      virtual_7_data_index_c,     virtual_7_data_type_d,
                                            virtual_7_data_index_d,     virtual_7_parameter_1,      virtual_7_parameter_2,      virtual_7_unused_a,
                                            virtual_7_unused_b,         virtual_7_output_result,    virtual_8_config,           virtual_8_skip_count,
                                            virtual_8_data_type_a,      virtual_8_data_index_a,     virtual_8_data_type_b,      virtual_8_data_index_b,
                                            virtual_8_data_type_c,      virtual_8_data_index_c,     virtual_8_data_type_d,      virtual_8_data_index_d,
                                            virtual_8_parameter_1,      virtual_8_parameter_2,      virtual_8_unused_a,         virtual_8_unused_b,
                                            virtual_8_output_result};
    /* *************** Arrays ************** */

    /* *********** Stored Values *********** */
    int   m_address;
    short m_value;
    int   m_index;
    /* *********** Stored Values *********** */

    /* *************** Other *************** */
    List<Short>   dbMediumVal =  new ArrayList<Short>();
    List<Integer> dbMediumAdd =  new ArrayList<Integer>();
    int           dbMediumSize = 0;
    /* *************** Other *************** */

    /* ************* SQL Stuff ************* */
    public static final String DATABASE_NAME =      "Modbus.db";
    public static final int    DATABASE_VERSION =   6;
    public static final String TABLE_NAME =         "ProactiveOne6";
    public static final String OLD_TABLE_NAME =     "ProactiveOne";
    public static final String BASE_NAME =          "modbus_";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    /* ************* SQL Stuff ************* */

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        constructSQLiteTable(db);
        insertValues(db);
        readValues(db);

        String str1 = "SELECT * FROM " + OLD_TABLE_NAME;
        Cursor c = db.rawQuery(str1, null);
        c.moveToFirst();
        int OldSize = c.getColumnCount() - 1;
        for(int i = 0; i < OldSize; i++) {
            for (int j = 0; j < Size; j++) {
                String colName = c.getColumnName(1 + i);
                int add = Integer.parseInt("" + colName.charAt(7) + colName.charAt(8) + colName.charAt(9) + colName.charAt(10));
                if (this.address[j] == add) {
                    short temp = ((short) c.getInt(1 + i));
                    if(this.values[j] != temp) {
                        db.execSQL("UPDATE " + TABLE_NAME + " SET " + BASE_NAME + address[j] + " = " + temp + ";");
                    }
                }
            }
        }
        c.close();

        db.execSQL("DROP TABLE IF EXISTS " + OLD_TABLE_NAME);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Modbus(Context c, int value) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase sql = getWritableDatabase();

        // First time use modbus setup
        if(value == 0) {
            constructSQLiteTable(sql);
            insertValues(sql);
            readValues(sql);
        }

        // Restore defaults
        else if(value == 1) {
            sql.execSQL(SQL_DELETE_ENTRIES);
            constructSQLiteTable(sql);
            insertValues(sql);
            readValues(sql);
        }

        // Second time use and beyond modbus setup
        else if(value == 2) {
            readValues(sql);
        }

        // Get value
        else {
            if(addressExists(value))
                m_value = values[m_index];
            else
                Log.e("ERROR", "ADDRESS DOES NOT EXIST");
        }
    }

    public Modbus(Context c, short[][][] changes, int sizeY, int sizeZ) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase sql = getWritableDatabase();
        for(short i = 0; i < sizeY; i++) {
            for(short j = 0; j < sizeZ; j++) {
                if(changes[1][i][j] == 1) {
                    dbMediumSize++;
                    dbMediumVal.add(changes[0][i][j]);
                    dbMediumAdd.add((int) changes[2][i][j]);
                }
            }
        }
        updateValues(sql);
        readValues(sql);
    }

    public Modbus(Context c, short[][] changes, int sizeZ) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase sql = getWritableDatabase();
        for(short i = 0; i < sizeZ; i++) {
            if (changes[1][i] == 1) {
                dbMediumSize++;
                dbMediumVal.add(changes[0][i]);
                dbMediumAdd.add((int) changes[2][i]);
            }
        }
        updateValues(sql);
        readValues(sql);
    }

    public void constructSQLiteTable(SQLiteDatabase db) {
        String str = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY,";
        for(short i = 0; i < Size - 1; i++)
            str = str + BASE_NAME + Integer.toString(address[i]) + " INTEGER,";
        str = str + BASE_NAME + Integer.toString(address[Size - 1]) + " INTEGER)";
        db.execSQL(str);
    }

    public void insertValues(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        for(short i = 0; i < Size; i++)
            cv.put(BASE_NAME + address[i], defaults[i]);
        db.insert(TABLE_NAME, null, cv);
    }

    public void readValues(SQLiteDatabase db) {
        String str = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(str, null);
        c.moveToFirst();
        for(short i = 0; i < Size; i++)
            values[i] = ((short) c.getInt(1 + i));
        c.close();
    }

    public void updateValues(SQLiteDatabase db) {
        String str = "UPDATE " + TABLE_NAME + " SET ";
        for(short i = 0; i < dbMediumSize - 1; i++)
            str = str + BASE_NAME + dbMediumAdd.get(i) + " = " + dbMediumVal.get(i) + ", ";
        str = str + BASE_NAME + dbMediumAdd.get(dbMediumSize - 1) + " = " + dbMediumVal.get(dbMediumSize - 1) + ";";
        db.execSQL(str);
    }

    public short getValue() {
        return m_value;
    }

    public boolean addressExists(int add) {
        for(short i = 0; i < Size; i++) {
            if(address[i] == add) {
                m_address = add;
                m_index = i;
                return true;
            }
        }
        return false;
    }

    public void databaseTransition(SQLiteDatabase db) {
    }
}
