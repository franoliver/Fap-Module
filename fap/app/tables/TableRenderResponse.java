package tables;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import flexjson.JSONSerializer;

import models.Firmante;
import models.TableKeyValue;
import tags.ReflectionUtils;
import validation.ValueFromTable;

public class TableRenderResponse<T> {
	public int total;
	public List<T> rows;
	
	public TableRenderResponse(List<T> rows) {		
		if(rows == null){
			this.rows = new ArrayList<T>();
		}else{
			this.rows = rows;
		}
		total = this.rows.size();
	}
		
	public String toJSON(String ... fields){
		Set<String> fieldsSet = new HashSet<String>(Arrays.asList(fields));
		
		String[] includeParams = new String[fields.length + 1];
		for(int i = 0; i < fields.length; i++)
			includeParams[i] = "rows." + fields[i];
		includeParams[fields.length] = "total";
		
		Map<String,List<String>> valueFromTable = getValueFromTableField();
		JSONSerializer flex = new JSONSerializer()
											.include(includeParams)
											.transform(new serializer.DateTimeTransformer(), org.joda.time.DateTime.class);
											
		for (String table : valueFromTable.keySet())
			for (String field : valueFromTable.get(table))
				if (fieldsSet.contains(field))
					flex = flex.transform(new serializer.ValueFromTableTransformer(table), "rows." + field);
					
		flex = flex.exclude("*");

		String serialize = flex.serialize(this);
		return serialize;
	}
	
	public HashMap<String,List<String>> getValueFromTableField() {
		HashMap<String,List<String>> valueFromTable = new HashMap<String,List<String>>();
		if ((rows != null) && (!rows.isEmpty())) {
			T row = rows.get(0);
			java.util.List<String> fields = ReflectionUtils.getFieldsNamesForClass(row.getClass());
			for (String field : fields) {
				Field f = null;
				try { f = row.getClass().getField(field);} 
				catch (Exception e) {e.printStackTrace();}
				if (f != null) {
					ValueFromTable annotation = f.getAnnotation(ValueFromTable.class);
					if(annotation != null){
						if (!valueFromTable.containsKey(annotation.value()))
							valueFromTable.put(annotation.value(), new ArrayList<String>());
						valueFromTable.get(annotation.value()).add(field);
					}
				}
			}
		}
		return valueFromTable;
	}
	
	
}
