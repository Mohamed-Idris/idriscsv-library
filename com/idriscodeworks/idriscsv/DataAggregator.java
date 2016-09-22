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

import java.math.BigDecimal;
import java.util.List;

/**
 * DataAggregator class is for finding aggregate values like sum, average,
 * minimum, maximum and count of a list of numbers. The constructor 
 * accepts list of String values because the String values are useful in 
 * passing them as arguments while instantiating BigDecimal objects. Thus, 
 * this class can be useful in finding aggregate values for both integer 
 * as well as decimal values.
 *   
 * @author Mohamed Idris
 *
 */
public class DataAggregator {
	
	private BigDecimal sum;
	private BigDecimal average;
	private BigDecimal min;
	private BigDecimal max;
	private int count;

	/**
	 * Constructs a new instance of the DataAggregator
	 * @param data list of values on which aggregation has to be performed
	 */
	public DataAggregator(List<String> data) {
		aggregate(data);
	}

	/**
	 * Calculates aggregation on the given list of values
	 * @param data list of values on which aggregation has to be performed
	 */
	private void aggregate(List<String> data) {
		min = new BigDecimal(data.get(0));
		max = new BigDecimal(data.get(0));
		sum = BigDecimal.ZERO;
		for(String number : data) {
			BigDecimal currentValue = new BigDecimal(number);
			if(currentValue.compareTo(min) < 0) {
				min = currentValue;
			}
			if(currentValue.compareTo(max) > 0) {
				max = currentValue;
			}
			sum = sum.add(new BigDecimal(number));
		}
		average = sum.divide(new BigDecimal(data.size()));
		count = data.size();
	}
	
	/**
	 * Returns the sum of the list of values
	 * @return the sum of the list of values
	 */
	public BigDecimal getSum() {
		return sum;
	}

	/**
	 * Returns the average of the list of values
	 * @return the average of the list of values
	 */
	public BigDecimal getAverage() {
		return average;
	}

	/**
	 * Returns the minimum among the list of values
	 * @return the minimum among the list of values
	 */
	public BigDecimal getMin() {
		return min;
	}

	/**
	 * Returns the maximum among the list of values
	 * @return the maximum among the list of values
	 */
	public BigDecimal getMax() {
		return max;
	}

	/**
	 * Returns the count of the list of values
	 * @return the count of the list of values
	 */
	public int getCount() {
		return count;
	}
}
