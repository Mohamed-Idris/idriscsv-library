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
 * ObjectBindableCsvWriter object. As an example, such an exception is
 * thrown as a result of anomalies encountered while binding the
 * object's attributes to columns in the CSV file.
 * 
 * @author Mohamed Idris
 *
 */
public class CsvWriterException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new CsvWriterException
	 */
	public CsvWriterException() {
		super();
	}

	/**
	 * Constructs a new CsvWriterException with message, cause,
	 * option to enable suppression and writable stack trace
	 * @param message the detail message about the CsvWriterException
	 * @param cause the cause for the CsvWriterException
	 * @param enableSuppression the option for enabling suppression
	 * @param writableStackTrace the option for writable stack trace
	 */
	public CsvWriterException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructs a new CsvWriterException with message and cause
	 * @param message the detail message about the CsvWriterException
	 * @param cause the cause for the CsvWriterException
	 */
	public CsvWriterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new CsvWriterException with message
	 * @param message the detail message about the CsvWriterException
	 */
	public CsvWriterException(String message) {
		super(message);
	}

	/**
	 * Constructs a new CsvWriterException with cause
	 * @param cause  the cause for the CsvWriterException
	 */
	public CsvWriterException(Throwable cause) {
		super(cause);
	} 
}