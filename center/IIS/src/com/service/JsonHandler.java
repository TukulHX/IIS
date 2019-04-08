package com.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHandler implements RsHandler<JSONArray> {

	@Override
	public JSONArray handle(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		JSONArray jsonArray = new JSONArray();
		
		
		ResultSetMetaData rsmd = rs.getMetaData();
		while( rs.next() )
		{
			JSONObject obj = new JSONObject();
			getType(rs,rsmd,obj);
			jsonArray.put(obj);
		}
		return jsonArray;
	}
	private void getType(ResultSet rs, ResultSetMetaData rsmd,
            JSONObject obj) throws SQLException {
int total_rows = rsmd.getColumnCount();
for (int i = 0; i < total_rows; i++) {
String columnName = rsmd.getColumnLabel(i + 1);
if (obj.has(columnName)) {
   columnName += "1";
}
try {
   switch (rsmd.getColumnType(i + 1)) {
       case java.sql.Types.ARRAY:
           obj.put(columnName, rs.getArray(columnName));
           break;
       case java.sql.Types.BIGINT:
           obj.put(columnName, rs.getInt(columnName));
           break;
       case java.sql.Types.BOOLEAN:
           obj.put(columnName, rs.getBoolean(columnName));
           break;
       case java.sql.Types.BLOB:
           obj.put(columnName, rs.getBlob(columnName));
           break;
       case java.sql.Types.DOUBLE:
           obj.put(columnName, rs.getDouble(columnName));
           break;
       case java.sql.Types.FLOAT:
           obj.put(columnName, rs.getFloat(columnName));
           break;
       case java.sql.Types.INTEGER:
           obj.put(columnName, rs.getInt(columnName));
           break;
       case java.sql.Types.NVARCHAR:
           obj.put(columnName, rs.getNString(columnName));
           break;
       case java.sql.Types.VARCHAR:
           obj.put(columnName, rs.getString(columnName));
           break;
       case java.sql.Types.TINYINT:
           obj.put(columnName, rs.getInt(columnName));
           break;
       case java.sql.Types.SMALLINT:
           obj.put(columnName, rs.getInt(columnName));
           break;
       case java.sql.Types.DATE:
           obj.put(columnName, rs.getDate(columnName));
           break;
       case java.sql.Types.TIMESTAMP:
           obj.put(columnName, rs.getTimestamp(columnName));
           break;
       default:
           obj.put(columnName, rs.getObject(columnName));
           break;
   		}
		} catch (JSONException e) {
			e.printStackTrace();
}
}
}


}
