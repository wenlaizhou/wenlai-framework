package cn.framework.db.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * project code
 * package cn.framework.db.util
 * create at 16-3-12 下午3:58
 *
 * @author wenlai
 */
public final class Dbs {
    /**
     * 将 ResultSet 转换成 JSONObject
     *
     * @param dataSet resultSet 数据
     *
     * @return 转换数据
     */
    public static JSONArray convertResultSet(ResultSet dataSet) {
        try (ResultSet data = dataSet;){
            JSONArray result = new JSONArray();
            if (data != null) {
                ResultSetMetaData meta = data.getMetaData();
                if (meta != null) {
                    int columeCount = meta.getColumnCount();
                    while (data.next()) {
                        JSONObject row = new JSONObject();
                        for (int i = 0; i < columeCount; i++) {
                            switch (meta.getColumnType(i))
                            {
                            }
                            String columnLabel = meta.getColumnLabel(i);
                            row.put(columnLabel, columnLabel);
                        }
                        result.add(row);
                    }
                }
            }
            return result;
        }
        catch (Exception x) {
            x.printStackTrace();
        }
        return null;
    }
}
