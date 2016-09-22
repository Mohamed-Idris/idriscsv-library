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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ObjectBindableCsvWriter class is used for converting a list of objects
 * into a sequence of records so as to write them into a CSV file.
 * 
 * @author Mohamed Idris
 *
 */
public class ObjectBindableCsvWriter {
	
	private Class<?> type;
	private String fileName;
	private String delimiter;
	private Map<String, Method> getterMap;
	private Field[] fields;
	
	/**
	 * Constructs a new ObjectBindableCsvWriter for the given class type,
	 * file name and delimiter
	 * @param type class type of the object to be written to file
	 * @param fileName name of the file
	 * @param delimiter delimiter for column values
	 */
	public ObjectBindableCsvWriter(Class<?> type, String fileName, String delimiter) {
		this.type = type;
		this.fileName = fileName;
		this.delimiter = delimiter;
		this.fields = type.getDeclaredFields();
		buildGetterMap();
	}
	
	/**
	 * Constructs a new ObjectBindableCsvWriter for the given class type,
	 * file name with comma as the default delimiter
	 * @param type class type of the object to be written to file
	 * @param fileName name of the file
	 */
	public ObjectBindableCsvWriter(Class<?> type, String fileName) {
		this(type, fileName, CsvConstants.DEFAULT_DELIMITER);
	}
		
	/**
	 * Builds a map of attributes and corresponding getter methods
	 */
	private void buildGetterMap() {
		Method[] methods = type.getDeclaredMethods();
		getterMap = new HashMap<String, Method>();
		for(Method method : methods) {
			String methodName = method.getName();
			if(methodName.startsWith("get")) {
				StringBuffer attribute = new StringBuffer(methodName.replaceFirst("get", ""));
				attribute.setCharAt(0, Character.toLowerCase(attribute.charAt(0)));
				getterMap.put(attribute.toString(), method);
			}
		}
	}
	
	/**
	 * Returns the header for the CSV file by parsing the class field names
	 * @return header for the CSV file
	 */
	private String getHeader() {
		StringBuffer headerBuffer = new StringBuffer();
		for(Field field : fields) {
			String columnName = field.getName();
			CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
			if(csvColumn != null) {
				columnName = csvColumn.name();
			}
			headerBuffer.append(columnName).append(delimiter);
		}
		headerBuffer.deleteCharAt(headerBuffer.length()-1);
		return headerBuffer.toString();
	}

	/**
	 * Returns the CSV file record for the given object
	 * @param object object whose CSV representation is required
	 * @return CSV file record for the given object
	 * @throws CsvWriterException if there is any exception during method invocation
	 */
	private String getRecord(Object object) throws CsvWriterException {
		StringBuffer recordBuffer = new StringBuffer();
		for(Field field : fields) {
			Method method = (Method) getterMap.get(field.getName());
			String value = null;
			try {
				value = String.valueOf(method.invoke(object));
			} 
			catch (Exception e) {
				throw new CsvWriterException(e);
			} 
			if(value != null) {
				recordBuffer.append(value).append(delimiter);
			}
		}
		recordBuffer.deleteCharAt(recordBuffer.length()-1);
		return recordBuffer.toString();
	}
	
	/**
	 * Writes the given list of objects as a sequence of records including the
	 * header into the CSV file
	 * @param objectList list of objects whose CSV representation is required
	 * @throws FileNotFoundException if the specified file is not found
	 * @throws CsvWriterException if there is any exception during conversion of object to CSV record
	 */
	public void write(List<Object> objectList) throws FileNotFoundException, CsvWriterException {
		PrintWriter writer = new PrintWriter(fileName);
		writer.println(getHeader());
		for(Object object : objectList) {
			writer.println(getRecord(object));
		}
		writer.close();
	}

}
