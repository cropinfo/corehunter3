/*******************************************************************************
 * Copyright Herman De Beukelaer, 2014 Guy Davenport Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *******************************************************************************/
package org.corehunter.data.simple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.corehunter.data.NamedSubsetData;

import uno.informatics.common.model.Dataset;
import uno.informatics.common.model.impl.EntityImpl;

/**
 * @author Guy Davenport
 */
public class AbstractNamedSubsetData extends EntityImpl implements NamedSubsetData, Dataset
{
	// item names
	private final String[] itemNames;

	private final Set<Integer>	ids;

	public AbstractNamedSubsetData(String name, String[] itemNames)
	{
	  super(name) ;
	  
	  if (itemNames == null)
	  	throw new IllegalArgumentException("Names not defined!") ;
	  
		this.itemNames = new String[itemNames.length] ;

		// infer IDs: 0..N-1 in case of N items
		// (indices in distance matrix and name array)
		ids = new HashSet<Integer>();

		for (int id = 0; id < itemNames.length; id++)
		{
			ids.add(id);
			this.itemNames[id] = itemNames[id] ;
		}
	}
	
	public AbstractNamedSubsetData(String name, List<String> names)
  {
    super(name) ;
    
	  if (names == null)
	  	throw new IllegalArgumentException("Names not defined!") ;
	  
		this.itemNames = new String[names.size()] ;

		// infer IDs: 0..N-1 in case of N items
		// (indices in distance matrix and name array)
		ids = new HashSet<Integer>();

		Iterator<String> iterator = names.iterator() ;
		
		int id = 0 ;
		
		while (iterator.hasNext())
		{
			ids.add(id);
			this.itemNames[id] = iterator.next() ;
			++id ;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jamesframework.core.problems.datatypes.SubsetData#getIDs()
	 */
	@Override
	public final Set<Integer> getIDs()
	{
		return ids;
	}

	/*
	 * (non-Javadoc)
	 * @see org.corehunter.data.NamedSubsetData#getName(int)
	 */
	//@Override // TODO why is @Override causing an error?
	public final String getName(int id)
	{
		return itemNames[id];
  }
  
  public final String[] getNames()
  {
    return itemNames;
  }
}
