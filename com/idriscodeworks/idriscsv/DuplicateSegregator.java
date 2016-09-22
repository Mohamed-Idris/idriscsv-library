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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DuplicateSegregator class is for segregating the given list of data
 * items into duplicate and unique items. This requires a one-time 
 * initialization of data list. After segregation, both the duplicate items
 * as well as the unique items are maintained as separate sets and returned
 * as such whenever required.
 * 
 * @author Mohamed Idris
 *
 */
public class DuplicateSegregator {
	
	private Map<String, Integer> dataFrequencyMap;
	private List<String> uniqueData;
	private int duplicateCount;
	private HashSet<String> duplicateData;

	/**
	 * Constructs a new DuplicateSegregator with the given data list
	 * @param data list of data items to be segregated
	 */
	public DuplicateSegregator(List<String> data) {
		buildFrequencyMap(data);
		segregateData();
	}

	/**
	 * Returns the set of unique data items in the available data list
	 * @return unique data set
	 */
	public List<String> getUniqueData() {
		return uniqueData;
	}
	
	/**
	 * Returns the number of unique data items in the available data list
	 * @return unique count
	 */
	public int getUniqueCount() {
		return uniqueData.size();
	}
	
	/**
	 * Returns the set of duplicate data items in the available data list
	 * @return duplicate data set
	 */
	public Set<String> getDuplicateData() {
		return duplicateData;
	}

	/**
	 * Returns the number of duplicates in the available data list
	 * @return duplicate count
	 */
	public int getDuplicateCount() {
		return duplicateCount;
	}

	/**
	 * Returns the frequency for the given element
	 * @param element element whose frequency is required
	 * @return frequency
	 */
	public int getFrequency(String element) {
		return dataFrequencyMap.get(element);
	}
	
	/**
	 * Builds frequency map by counting how frequent a data item
	 * occurs in the given data list
	 * @param data the data for which frequency map is to be built
	 */
	private void buildFrequencyMap(List<String> data) {
		dataFrequencyMap = new LinkedHashMap<String, Integer>();
		for(String element : data) {
			if(dataFrequencyMap.containsKey(element)) {
				int frequency = dataFrequencyMap.get(element);
				frequency++;
				dataFrequencyMap.put(element, frequency);
			}
			else {
				dataFrequencyMap.put(element, 1);
			}
		}
	}
	
	/**
	 * Segregates data into duplicate and unique categories
	 */
	private void segregateData() {
		uniqueData = new ArrayList<String>();
		duplicateData = new LinkedHashSet<String>();
		duplicateCount = 0;
		for(String element : dataFrequencyMap.keySet()) {
			if(dataFrequencyMap.get(element) == 1) {
				uniqueData.add(element);
			}
			else {
				duplicateData.add(element);
				duplicateCount++;
			}
		}
	}
}
