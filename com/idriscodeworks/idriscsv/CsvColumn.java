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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a column is mapped to the member attribute of the class with the
 * properties specified through the annotation. If the member attribute of a class
 * is annotated with this type of annotation, then the name attribute of the annotation
 * represents the column name found in the CSV file. This is particularly useful if
 * the member attribute name differs from the column name in the CSV file thereby 
 * requiring a mapping between the two.  
 * 
 * <pre>
 * Example:
 * public class SmartPhone {
 * 		@CsvColumn(name="Model Name")
 * 		private String modelName;
 * }
 * </pre>
 * 
 * @author Mohamed Idris
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvColumn {
	/**
	 * The column name found in the CSV file corresponding to the member attribute of 
	 * the class representing the model of the CSV file
	 */
	public String name();
}
