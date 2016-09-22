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

/**
 * Thrown when there is any type of error during the usage of
 * CsvReader object. As an example, if there is no header specified
 * for the CSV file and the CsvReader object is used to read the header,
 * then this Exception is thrown.
 * 
 * @author Mohamed Idris
 *
 */
public class CsvReaderException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new CsvReaderException
	 */
	public CsvReaderException() {
		super();
	}

	/**
	 * Constructs a new CsvReaderException with message, cause,
	 * option to enable suppression and writable stack trace
	 * @param message the detail message about the CsvReaderException
	 * @param cause the cause for the CsvReaderException
	 * @param enableSuppression the option for enabling suppression
	 * @param writableStackTrace the option for writable stack trace
	 */
	public CsvReaderException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructs a new CsvReaderException with message and cause
	 * @param message the detail message about the CsvReaderException
	 * @param cause the cause for the CsvReaderException
	 */
	public CsvReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new CsvReaderException with message
	 * @param message the detail message about the CsvReaderException
	 */
	public CsvReaderException(String message) {
		super(message);
	}

	/**
	 * Constructs a new CsvReaderException with cause
	 * @param cause  the cause for the CsvReaderException
	 */
	public CsvReaderException(Throwable cause) {
		super(cause);
	} 
}
