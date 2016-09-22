/*
 * Copyright 2016 Mohamed Idris. All Rights Reserved.
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

package com.idriscodeworks.idriscsv;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * ObjectBindableCsvReader class is used for reading a CSV file and
 * load the records read into a list of objects. Internally, a binding 
 * is required between the objects and the CSV file records. This is 
 * accomplished with the help of GSON library, which is required additionally
 * for using this class.
 * 
 * @author Mohamed Idris
 *
 */
public class ObjectBindableCsvReader {	
	private CsvReader reader;
	private Map<String, String> bindingMap;
	private Class<?> type;

	/**
	 * Constructs a new ObjectBindableCsvReader
	 * @param reader CsvReader instance
	 * @param type class type of the object
	 * @throws CsvReaderException if there is any exception in reading the CSV file
	 */
	public ObjectBindableCsvReader(CsvReader reader, Class<?> type) throws CsvReaderException {
		this.reader = reader;
		this.type = type;
		buildBindingMap();
	}

	/**
	 * Builds a binding map between the CSV file column name and the 
	 * attribute name of the object
	 * @throws CsvReaderException if there is any exception in reading the CSV file
	 */
	private void buildBindingMap() throws CsvReaderException {
		bindingMap = new HashMap<String, String>();
		List<String> columnNames =  Arrays.asList(reader.getColumnNames());
		Field[] fields = type.getDeclaredFields();
		for(Field field : fields) {
			String fieldName = field.getName(); 
			if(columnNames.contains(fieldName)) {
				bindingMap.put(fieldName, fieldName);
			}
			else {
				CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
				if(csvColumn != null) {
					String representation = csvColumn.name();
					if(columnNames.contains(representation)) {
						bindingMap.put(representation, fieldName);
					}
				}
			}
		}
	}

	/**
	 * Returns the list of objects corresponding to the records in the CSV file
	 * @return list of objects corresponding to the records in the CSV file
	 * @throws CsvReaderException if there is any exception in reading the CSV file
	 */
	public List<Object> getObjects() throws CsvReaderException {
		String[] columnNames = reader.getColumnNames();
		List<Object> objectList = new ArrayList<Object>();
		Gson gson = new Gson();
		int initialIndex = reader.getInitialRowIndex();
		for(int i=initialIndex; i<=reader.getRowCount(); i++) {
			String[] values = reader.getRowSplit(i);
			String json = getJson(columnNames, values);
			Object object = gson.fromJson(json, type);
			objectList.add(object);
		}
		return objectList;
	}

	/**
	 * Returns the JSON representation for the given array of column names and values
	 * @param columnNames name of the columns present in the CSV file
	 * @param values values corresponding to the column names in the CSV file
	 * @return the JSON representation for the given array of column names and values
	 */
	private String getJson(String[] columnNames, String[] values) {
		StringBuffer jsonBuffer = new StringBuffer();
		jsonBuffer.append("{");
		for(int i=0; i<columnNames.length; i++) {
			jsonBuffer.append("\"").append(bindingMap.get(columnNames[i])).append("\":\"").append(values[i]).append("\"").append(",");
		}
		jsonBuffer.deleteCharAt(jsonBuffer.length()-1);
		jsonBuffer.append("}");
		String json = jsonBuffer.toString();
		return json;
	}
}
