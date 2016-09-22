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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CsvReader class is used for reading CSV files. By default, the column delimiter is comma, though
 * the reader can be used for reading files with any delimiter. This reader can read files that
 * have header as well as those that do not have header. Header here means the column names
 * appearing in the file as the first row.
 * 
 * @author Mohamed Idris
 * 
 */
public class CsvReader {
	private List<String> rowList;
	private List<Integer> numColumnsList;
	private boolean headerAvailable;
	private String delimiter;
	private String[] columnNames;

	/**
	 * Constructs a new CsvReader for the given file name with comma as the default delimiter
	 * and assumes that the first line of the file is the header
	 * @param fileName Name of the file to be read by the CsvReader
	 * @throws CsvReaderException if any exception occurs while reading the CSV file
	 */
	public CsvReader(String fileName) throws CsvReaderException {
		this(fileName, true);
	}

	/**
	 * Constructs a new CsvReader for the given file name and delimiter and assumes that
	 * the first line of the file is the header
	 * @param fileName Name of the file to be read by the CsvReader
	 * @param delimiter Delimiter used to separate columns in the file
	 * @throws CsvReaderException if any exception occurs while reading the CSV file
	 */
	public CsvReader(String fileName, String delimiter) throws CsvReaderException {
		this(fileName, delimiter, true);
	}

	/**
	 * Constructs a new CsvReader for the given file name and header availability indicator
	 * assuming comma as the default delimiter
	 * @param fileName Name of the file to be read by the CsvReader
	 * @param headerAvailable true if the first line of the file is its header and false otherwise
	 * @throws CsvReaderException if any exception occurs while reading the CSV file
	 */
	public CsvReader(String fileName, boolean headerAvailable) throws CsvReaderException {
		this(fileName, CsvConstants.DEFAULT_DELIMITER, headerAvailable);
	}

	/**
	 * Constructs a new CsvReader for the given file name, delimiter and header availability indicator
	 * @param fileName Name of the file to be read by the CsvReader
	 * @param delimiter Delimiter used to separate columns in the file
	 * @param headerAvailable true if the first line of the file is its header and false otherwise
	 * @throws CsvReaderException if any exception occurs while reading the CSV file
	 */
	public CsvReader(String fileName, String delimiter, boolean headerAvailable) throws CsvReaderException {
		this.headerAvailable = headerAvailable;
		this.delimiter = delimiter;
		rowList = new ArrayList<String>();
		numColumnsList = new ArrayList<Integer>();
		BufferedReader reader = null;
		Throwable exception = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String record;
			while((record = reader.readLine()) != null) {
				rowList.add(record);
				if(Utils.isNullOrEmpty(record)) {
					numColumnsList.add(0);
				}
				else {
					String[] fields = record.split(delimiter);
					numColumnsList.add(fields.length);
				}
			}
			if(headerAvailable) {
				columnNames = rowList.get(0).split(delimiter);
			}
		} catch(IOException e) {
			exception = e;
		}
		finally {
			if(reader != null) {
				try {
					reader.close();
					if(exception != null) {
						throw new CsvReaderException(exception);
					}
				} catch (IOException e) {
					throw new CsvReaderException(e);
				}
			}
		}
	}

	/**
	 * Returns the rows without header in the file being read by this reader 
	 * @return rows as a list of String
	 */
	public List<String> getRows() {
		return getRows(false);
	}

	/**
	 * Returns the rows in the file being read by this reader. Based on the boolean indicator parameter,
	 * the header may or may not be included in the returned rows list. If the withHeader indicator is true,
	 * then the header will be included in the returned list. If the indicator is false, the header will
	 * not be included in the returned list 
	 * @param withHeader true if the rows are required along with header and false otherwise
	 * @return rows as a list of String
	 */
	public List<String> getRows(boolean withHeader) {
		List<String> rows = new ArrayList<String>(rowList);
		if(headerAvailable && !withHeader) {
			rows.remove(0);
		}
		return rows;
	}
	
	/**
	 * Returns rows that have the given row numbers
	 * @param rowNumbers List of integers representing the row numbers
	 * @return rows having the given row numbers
	 */
	public List<String> getRows(List<Integer> rowNumbers) {
		List<String> rows = new ArrayList<String>();
		for(Integer rowNumber : rowNumbers) {
			rows.add(getRow(rowNumber));
		}
		return rows;
	}
	
	/**
	 * Returns rows that have row numbers starting with the given start index and
	 * ending with the given end index. Both start and end index are inclusive for
	 * the returned rows.
	 * @param start start index for the rows to be returned
	 * @param end end index for the rows to be returned
	 * @return rows that have row numbers starting with given start index and ending with given end index
	 */
	public List<String> getRows(int start, int end) {
		List<String> rows = new ArrayList<String>();
		for(int rowIndex=start; rowIndex<=end; rowIndex++) {
			rows.add(getRow(rowIndex));
		}
		return rows;
	}
	
	/**
	 * Returns the row for the given row index by splitting it by the delimiter
	 * @param rowIndex index for the required row
	 * @return the row for the given row index by splitting it by the delimiter
	 */
	public String[] getRowSplit(int rowIndex) {
		return getRow(rowIndex).split(delimiter);
	}
	
	/**
	 * Returns the row for the given row index as a String without splitting it by the delimiter
	 * @param rowIndex index for the required row
	 * @return the row for the given row index as a String without splitting it by the delimiter
	 */
	public String getRow(int rowIndex) {
		if(!Utils.isIndexBound(rowIndex, rowList.size())) {
			throw new IllegalArgumentException("rowIndex is out of the bounds of rowList");
		} 
		return rowList.get(rowIndex);
	}

	
	/**
	 * Returns the rows which have column values for the given column index and
	 * matching the given regular expression. Header will not be included as the
	 * first row in the list of returned rows
	 * @param columnIndex index of the column
	 * @param regex regular expression for matching with column values
	 * @return rows having matched column values for the given column index without header
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	public List<String> getRows(int columnIndex, String regex) throws CsvReaderException {
		return getRows(columnIndex, regex, false);
	}

	/**
	 * Returns the rows which have column values for the given column index and
	 * matching the given regular expression. Header will be included as the first 
	 * row in the returned list of rows if the withHeader parameter is true. Header 
	 * will not be included if the withHeader parameter is false. 
	 * @param columnIndex index of the column
	 * @param regex regular expression for matching with column values
	 * @param withHeader true if the header has to be included as the first row and false otherwise
	 * @return rows having matched column values for the given column index as per withHeader option
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	public List<String> getRows(int columnIndex, String regex, boolean withHeader) throws CsvReaderException {
		List<String> rows = getRows(getRowNumbers(columnIndex, regex));
		if(withHeader && headerAvailable) {
			rows.add(0, getRow(0));
		}
		return rows;
	}
	
	/**
	 * Returns the rows which have column values for the given column name and
	 * matching the given regular expression. Header will not be included as the
	 * first row in the list of returned rows
	 * @param columnName name of the column
	 * @param regex regular expression for matching with column values
	 * @return rows having matched column values for the given column name without header
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	public List<String> getRows(String columnName, String regex) throws CsvReaderException {
		return getRows(columnName, regex, false);
	}
	
	/**
	 * Returns the rows which have column values for the given column name and
	 * matching the given regular expression. Header will be included as the first 
	 * row in the returned list of rows if the withHeader parameter is true. Header 
	 * will not be included if the withHeader parameter is false. 
	 * @param columnName name of the column
	 * @param regex regular expression for matching with column values
	 * @param withHeader true if the header has to be included as the first row and false otherwise
	 * @return rows having matched column values for the given column name as per withHeader option
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	public List<String> getRows(String columnName, String regex, boolean withHeader) throws CsvReaderException {
		int columnIndex = getColumnIndex(columnName);
		return getRows(columnIndex, regex);
	}

	/**
	 * Returns the number of rows in the file read by this reader
	 * @return the number of rows in the file
	 */
	public int getRowCount() {
		int rowCount = rowList.size();
		if(headerAvailable) {
			rowCount--;
		}
		return rowCount;
	}
	
	/**
	 * Returns the row numbers for the rows which have column values for the given column
	 * index and matching the given regular expression
	 * @param columnIndex index of the column
	 * @param regex regular expression for matching with column values
	 * @return row numbers of the rows having matched column values
	 * @throws CsvReaderException if any exception occurs during the method execution
	 */
	public List<Integer> getRowNumbers(int columnIndex, String regex) throws CsvReaderException {
		Pattern pattern = Pattern.compile(regex);
		List<String> column = getColumn(columnIndex);
		List<Integer> rowNumbers = new ArrayList<Integer>();
		for(int i=1; i<column.size(); i++) {
			Matcher matcher = pattern.matcher(column.get(i));
			if(matcher.matches()) {
				rowNumbers.add(i);
			}
		}
		return rowNumbers;
	}

	/**
	 * Returns the row numbers for the rows which have column values for the given column
	 * name and matching the given regular expression
	 * @param columnName name of the column
	 * @param regex regular expression for matching with column values
	 * @return row numbers of the rows having matched column values
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	public List<Integer> getRowNumbers(String columnName, String regex) throws CsvReaderException {
		int columnIndex = getColumnIndex(columnName);
		return getRowNumbers(columnIndex, regex);
	}

	
	/**
	 * Returns the rows having the given number of columns
	 * @param numColumns number of columns
	 * @return the rows having the given number of columns
	 */
	public List<String> getRowsHavingColumns(int numColumns) {
		List<String> rows = new ArrayList<String>();
		int initialIndex = getInitialRowIndex();
		for(int i=initialIndex; i<numColumnsList.size(); i++) {
			if(numColumnsList.get(i) == numColumns) {
				rows.add(rowList.get(i));
			}
		}
		return rows;
	}

	/**
	 * Returns the rows not having the given number of columns
	 * @param numColumns number of columns
	 * @return the rows not having the given number of columns
	 */
	public List<String> getRowsNotHavingColumns(int numColumns) {
		List<String> rows = new ArrayList<String>();
		int initialIndex = getInitialRowIndex();
		for(int i=initialIndex; i<numColumnsList.size(); i++) {
			if(numColumnsList.get(i) != numColumns) {
				rows.add(rowList.get(i));
			}
		}
		return rows;
	}

	/**
	 * Returns the rows having column count lesser than the given number
	 * @param numColumns number of columns
	 * @return the rows having column count lesser than the given number
	 */
	public List<String> getRowsHavingColsLesserThan(int numColumns) {
		List<String> rows = new ArrayList<String>();
		int initialIndex = getInitialRowIndex();		
		for(int i=initialIndex; i<numColumnsList.size(); i++) {
			if(numColumnsList.get(i) < numColumns) {
				rows.add(rowList.get(i));
			}
		}
		return rows;
	}
	
	/**
	 * Returns the rows having column count more than the given number
	 * @param numColumns number of columns
	 * @return the rows having column count more than the given number
	 */
	public List<String> getRowsHavingColsMoreThan(int numColumns) {
		List<String> rows = new ArrayList<String>();
		int initialIndex = getInitialRowIndex();		
		for(int i=initialIndex; i<numColumnsList.size(); i++) {
			if(numColumnsList.get(i) > numColumns) {
				rows.add(rowList.get(i));
			}
		}
		return rows;
	}
	
	/**
	 * Returns the initial row index as 1 if headerAvailable attribute of this reader has value true and 0 otherwise
	 * @return 1 if headerAvailable is true and 0 otherwise
	 */
	public int getInitialRowIndex() {
		return headerAvailable ? 1 : 0; 
	}

	/**
	 * Checks whether all the rows in file is consistent or not and 
	 * returns true or false accordingly. If all rows of the file have
	 * the same number of columns, then the file is considered to be consistent.
	 * @return true if all rows are consistent, false otherwise
	 */
	public boolean isConsistent() {
		Set<Integer> numColumnsSet = new HashSet<Integer>();
		numColumnsSet.addAll(numColumnsList);
		return numColumnsSet.size() == 1 ? true : false;
	}

	/**
	 * Returns the names of columns in the file being read
	 * @return column names array of column names
	 * @throws CsvReaderException if the headerAvailable attribute of this reader is false
	 */
	public String[] getColumnNames() throws CsvReaderException {
		if(!headerAvailable) {
			throw new CsvReaderException("Cannot get column names because headerAvailable is set to false");
		}
		return columnNames;
	}

	/**
	 * Returns the maximum number of columns in the given file
	 * @return the maximum number of columns 
	 */
	public int getMaxNumOfColumns() {
		return Collections.max(numColumnsList);
	}

	/**
	 * Returns the minimum number of columns in the given file
	 * @return the maximum number of columns
	 */
	public int getMinNumOfColumns() {
		return Collections.min(numColumnsList);
	}

	/**
	 * Returns the number of columns in the row having the given row index
	 * @param rowIndex row index of the row whos column count is required
	 * @return the number of columns in the row having the given row index
	 */
	public int getColumnCount(int rowIndex) {
		return getRowSplit(rowIndex).length;
	}


	/**
	 * Returns the column having the given column index. The column is returned as a list of String
	 * @param columnIndex index of the column required
	 * @return column having the column index
	 */
	public List<String> getColumn(int columnIndex) {
		return getColumn(columnIndex, false);
	}

	/**
	 * Returns the column having the given column index. The column is returned as a list of String
	 * If withHeader is true, then the column name is included as the first value in the returned list.
	 * @param columnIndex index of the column required
	 * @param withHeader true if the column should have column name as the first value and false otherwise
	 * @return column with or without header based on parameter and having the given column index
	 */
	public List<String> getColumn(int columnIndex, boolean withHeader) {
		List<String> column = new ArrayList<String>();
		for(String record : rowList) {
			String[] fields = record.split(delimiter);
			if(Utils.isIndexBound(columnIndex, fields.length)) {
				column.add(fields[columnIndex]);
			}
			else {
				column.add(CsvConstants.EMPTY_STRING);
			}
		}
		if(headerAvailable && !withHeader && !column.isEmpty()) {
			column.remove(0);
		}
		return column;
	}

	/**
	 * Returns the column having the specified column index and matching the given regular 
	 * expression. The column is returned as a list of String without column name
	 * @param columnIndex index of the column
	 * @param regex regular expression to match with the column values
	 * @return column having the specified column index and matching the given regular expression with column name
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	public List<String> getColumn(int columnIndex, String regex) throws CsvReaderException {
		return getColumn(columnIndex, regex, false);
	}

	/**
	 * Returns the column having the specified column index and matching the given regular 
	 * expression. The column is returned as a list of String. If the withHeader parameter is
	 * true, then the column name is included in the list as the first value. Otherwise,
	 * the column will have only the data values and not the column name 
	 * @param columnIndex index of the column
	 * @param regex regular expression to match with the column values
	 * @param withHeader if true, then the column name will be included as first value in the returned column
	 * @return column having the specified column index and matching the given regular expression with or without column name
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	
	public List<String> getColumn(int columnIndex, String regex, boolean withHeader) throws CsvReaderException {
		Pattern pattern = Pattern.compile(regex);
		List<String> column = getColumn(columnIndex, false);
		List<String> filteredColumn = new ArrayList<String>();
		for(String columnValue : column) {
			Matcher matcher = pattern.matcher(columnValue);
			if(matcher.matches()) {
				filteredColumn.add(columnValue);
			}
		}
		if(withHeader) {
			String columnName = getColumnName(columnIndex);
			filteredColumn.add(0, columnName);
		}
		return filteredColumn;
	}


	/**
	 * Returns the column having the given column index as a list of Integer objects
	 * @param columnIndex index of the column
	 * @return the column having the given column name as a list of Integer objects
	 */
	public List<Integer> getColumnAsIntegers(int columnIndex) {
		List<String> column = getColumn(columnIndex, !headerAvailable);
		List<Integer> integerColumn = new ArrayList<Integer>();
		for(String value : column) {
			integerColumn.add(Integer.parseInt(value));
		}
		return integerColumn;
	}
		
	/**
	 * Returns the column having the given column index as a list of BigDecimal objects
	 * @param columnIndex index of the column
	 * @return the column having the given column index as a list of BigDecimal objects
	 */
	public List<BigDecimal> getColumnAsDecimals(int columnIndex) {
		List<String> column = getColumn(columnIndex, !headerAvailable);
		List<BigDecimal> decimalColumn = new ArrayList<BigDecimal>();
		for(String value : column) {
			decimalColumn.add(new BigDecimal(value));
		}
		return decimalColumn;
	}
	
	/**
	 * Returns the column index corresponding to the given column name
	 * @param columnName name of the column whose index is required
	 * @return the column index corresponding to the given column name
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	public int getColumnIndex(String columnName) throws CsvReaderException {
		return Arrays.asList(getColumnNames()).indexOf(columnName);
	}
	
	/**
	 * Returns the column name corresponding to the given column index
	 * @param columnIndex index of the column name required
	 * @return the column name having the given column index
	 * @throws CsvReaderException if the headerAvailable attribute of this reader is false
	 */
	public String getColumnName(int columnIndex) throws CsvReaderException {
		String columnName = null;
		String[] columnNames = getColumnNames();
		if(Utils.isIndexBound(columnIndex, columnNames.length)) {
			columnName = columnNames[columnIndex];
		}
		return columnName;
	}

	/**
	 * Returns the column having the given column name as a list of Integer objects
	 * @param columnName name of the column
	 * @return the column having the given column name as a list of Integer objects
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	public List<Integer> getColumnAsIntegers(String columnName) throws CsvReaderException {
		int columnIndex = getColumnIndex(columnName);
		return getColumnAsIntegers(columnIndex);
	}

	/**
	 * Returns the column having the given column name as a list of BigDecimal objects
	 * @param columnName name of the column
	 * @return the column having the given column name as a list of BigDecimal objects
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	public List<BigDecimal> getColumnAsDecimals(String columnName) throws CsvReaderException {
		int columnIndex = getColumnIndex(columnName);
		return getColumnAsDecimals(columnIndex);
	}
	
	/**
	 * Returns the column having the given column name. The column is returned as a list of String
	 * @param columnName name of the column
	 * @return column having the column name
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	public List<String> getColumn(String columnName) throws CsvReaderException {
		return getColumn(columnName, false);
	}

	/**
	 * Returns the column having the given column name. The column is returned as a list of String
	 * If withHeader is true, then the column name is included as the first value in the returned list.
	 * @param columnName name of the column
	 * @param withHeader true if the column should have column name as the first value and false otherwise
	 * @return column with or without header based on parameter and having the given column index
	 * @throws CsvReaderException if headerAvailable attribute of this reader is false
	 */
	public List<String> getColumn(String columnName, boolean withHeader) throws CsvReaderException {
		int columnIndex = getColumnIndex(columnName);
		return getColumn(columnIndex, withHeader);
	}

	/**
	 * Returns the column having the specified column name and matching the given regular 
	 * expression. The column is returned as a list of String with column name by default
	 * @param columnName Name of the column
	 * @param regex regular expression to match with the column values
	 * @return column having the specified column name and matching the given regular expression with column name
	 * @throws CsvReaderException if the headerAvailable indicator is set to false
	 */
	public List<String> getColumn(String columnName, String regex) throws CsvReaderException {
		return getColumn(columnName, regex, true);
	}

	/**
	 * Returns the column having the specified column name and matching the given regular 
	 * expression. The column is returned as a list of String with or without column name depending
	 * upon whether the value of the withHeader parameter is true or false
	 * @param columnName Name of the column
	 * @param regex regular expression to match with the column values
	 * @param withHeader if true, then the column name will be included as first value in the returned column
	 * @return column having the specified column name and matching the given regular expression with or without column name
	 * @throws CsvReaderException if the headerAvailable attribute of this reader is false
	 */
	public List<String> getColumn(String columnName, String regex, boolean withHeader) throws CsvReaderException {
		int columnIndex = getColumnIndex(columnName);
		return getColumn(columnIndex, regex, withHeader);
	}

	/**
	 * Returns the sub matrix defined by row start, row end, column start and column end parameters
	 * @param rowStart starting row index for the sub matrix
	 * @param rowEnd ending row index for the sub matrix
	 * @param colStart starting column index for the sub matrix
	 * @param colEnd ending column index for the sub matrix
	 * @return the sub matrix defined by row start, row end, column start and column end parameters
	 */
	public List<String> getSubMatrix(int rowStart, int rowEnd, int colStart, int colEnd) {
		List<String> subMatrix = new ArrayList<String>();
		for(int i=rowStart; i<=rowEnd; i++) {
			StringBuffer rowBuffer = new StringBuffer();
			for(int j=colStart; j<=colEnd; j++) {
				String value = getValue(i,j);
				rowBuffer.append(value).append(delimiter);
			}
			Utils.removeLastCharacter(rowBuffer);
			subMatrix.add(rowBuffer.toString());
		}
		return subMatrix;
	}

	/**
	 * Returns the value at specified row index and column index
	 * @param rowIndex index of the row
	 * @param columnIndex index of the column
	 * @return the value at specified row index and column index
	 */
	public String getValue(int rowIndex, int columnIndex) {
		String value = null;
		String[] row = getRowSplit(rowIndex);
		if(Utils.isIndexBound(columnIndex, row.length)) {
			value = row[columnIndex];
		}
		return value;
	}

	/**
	 * Returns DataAggregator object for the column required so that aggregate
	 * operations like sum, min, max, average and count can be calculated
	 * on the column having the given column index
	 * @param columnIndex index of the column
	 * @return DataAggregator object for the column having given index
	 */
	public DataAggregator getDataAggregator(int columnIndex) {
		return new DataAggregator(getColumn(columnIndex));
	}
	
	/**
	 * Returns DataAggregator object for the column required so that aggregate
	 * operations like sum, min, max, average and count can be calculated
	 * on the column having the given column name
	 * @param columnName name of the column
	 * @return DataAggregator object for the column having given name 
	 * @throws CsvReaderException if the headerAvailable attribute of this reader is false
	 */
	public DataAggregator getDataAggregator(String columnName) throws CsvReaderException {
		return new DataAggregator(getColumn(columnName));
	}
	
	/**
	 * Returns DuplicateSegregator object for the column required so that
	 * unique and duplicate values can be segregated for the column having 
	 * the given index 
	 * @param columnIndex index of the column
	 * @return DuplicateSegregator object for the column having given index
	 */
	public DuplicateSegregator getDuplicateSegregator(int columnIndex) {
		return new DuplicateSegregator(getColumn(columnIndex));
	}
	
	/**
	 * Returns DuplicateSegregator object for the column required so that
	 * unique and duplicate values can be segregated for the column having
	 * the given name
	 * @param columnName name of the column
	 * @return DuplicateSegregator object for the column having given name
	 * @throws CsvReaderException if the headerAvailable attribute of this reader is false
	 */
	public DuplicateSegregator getDuplicateSegregator(String columnName) throws CsvReaderException {
		return new DuplicateSegregator(getColumn(columnName));
	}
	
	/**
	 * Returns rows after sorting by the column having the given index in
	 * alphabetically ascending order of the values
	 * @param columnIndex index of the column
	 * @return rows sorted by alphabetically ascending order of the column having given index
	 */
	public List<String> getSortedBy(int columnIndex) {
		return getSortedBy(columnIndex, false);
	}
		
	/**
	 * Returns rows after sorting by the column having the given index in numerically 
	 * ascending order of the values if sortByNumericOrder is true. Otherwise, sorting
	 * is done in alphabetically ascending order of the values
	 * @param columnIndex index of the column
	 * @param sortByNumericOrder if true, rows are sorted numerically. Otherwise, sorted alphabetically.
	 * @return rows sorted in the required sorting option of the column having given index
	 */
	public List<String> getSortedBy(int columnIndex, boolean sortByNumericOrder) {
		return sortByNumericOrder ? numericSort(columnIndex) : stringSort(columnIndex);
	}

	/**
	 * Sorts rows by numerically ascending order on the column having given index
	 * @param columnIndex index of the column
	 * @return rows sorted numerically ascending order on the column having given index	
	 */
	private List<String> numericSort(int columnIndex) {
		List<String> sortedRows = new ArrayList<String>();
		Map<BigDecimal, List<String>> columnToRecordMap = new TreeMap<BigDecimal, List<String>>();
		for(String row : getRows()) {
			String[] columnValues = row.split(delimiter);
			if(columnToRecordMap.containsKey(new BigDecimal(columnValues[columnIndex]))) {
				List<String> mappedRows = columnToRecordMap.get(columnValues[columnIndex]);
				mappedRows.add(row);
			}
			else {
				List<String> mappedRows = new ArrayList<String>();
				mappedRows.add(row);
				columnToRecordMap.put(new BigDecimal(columnValues[columnIndex]), mappedRows);
			}
		}
		for(BigDecimal key : columnToRecordMap.keySet()) {
			for(String row : columnToRecordMap.get(key)) {
				sortedRows.add(row);
			}
		}
		return sortedRows;
	}

	/**
	 * Sorts rows by alphabetically ascending order on the column having given index
	 * @param columnIndex index of the column
	 * @return rows sorted alphabetically ascending order on the column having given index
	 */
	private List<String> stringSort(int columnIndex) {
		List<String> sortedRows = new ArrayList<String>();
		Map<String, List<String>> columnToRecordMap = new TreeMap<String, List<String>>();
		for(String row : getRows()) {
			String[] columnValues = row.split(delimiter);
			if(columnToRecordMap.containsKey(columnValues[columnIndex])) {
				List<String> mappedRows = columnToRecordMap.get(columnValues[columnIndex]);
				mappedRows.add(row);
			}
			else {
				List<String> mappedRows = new ArrayList<String>();
				mappedRows.add(row);
				columnToRecordMap.put(columnValues[columnIndex], mappedRows);
			}
		}
		for(String key : columnToRecordMap.keySet()) {
			for(String row : columnToRecordMap.get(key)) {
				sortedRows.add(row);
			}
		}
		return sortedRows;
	}
	
	/**
	 * Returns rows after sorting by the column having the given name in
	 * alphabetically ascending order of the values
	 * @param columnName name of the column
	 * @return rows sorted by alphabetically ascending order of the column having given name
	 * @throws CsvReaderException if the headerAvailable attribute of this reader is false
	 */
	public List<String> getSortedBy(String columnName) throws CsvReaderException {
		return getSortedBy(columnName, false);
	}
	
	/**
	 * Returns rows after sorting by the column having the given name in numerically 
	 * ascending order of the values if sortByNumericOrder is true. Otherwise, sorting
	 * is done in alphabetically ascending order of the values
	 * @param columnName name of the column
	 * @param sortByNumericOrder if true, rows are sorted numerically. Otherwise, sorted alphabetically.
	 * @return rows sorted in the required sorting option of the column having given name
	 * @throws CsvReaderException if the headerAvailable attribute of this reader is false
	 */
	public List<String> getSortedBy(String columnName, boolean sortByNumericOrder) throws CsvReaderException {
		int columnIndex = getColumnIndex(columnName);
		return getSortedBy(columnIndex, sortByNumericOrder);
	}

	/**
	 * Returns the header availability status for the file being read
	 * @return true if headerAvailable is set to be true, false otherwise
	 */
	public boolean isHeaderAvailable() {
		return headerAvailable;
	}

	/**
	 * Returns the delimiter used to separate columns in the file
	 * @return the delimiter used to separate columns in the file
	 */
	public String getDelimiter() {
		return delimiter;
	}
}
