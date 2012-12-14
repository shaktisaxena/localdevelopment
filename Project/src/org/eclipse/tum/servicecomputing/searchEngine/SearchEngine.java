package org.eclipse.tum.servicecomputing.searchEngine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.tum.servicecomputing.publish.SimpleQuery;

public class SearchEngine implements WSSearchEngine{
	
	//list to contain url of webservices
	private ArrayList<String> serviceDescription;
	
	//list of webservices itself
	private ArrayList<ServiceInfo>serviceInfos;
	
	//class to represent a webservice
	private ServiceInfo serviceInfo;

	@Override
	public List<ServiceInfo> search(String keywords) {
		
		SimpleQuery query= new SimpleQuery();
		try {
			int i=0;
			
			serviceDescription=query.find();
			
			Iterator<String> iter=serviceDescription.iterator();
			
			while(iter.hasNext())
			{
				 serviceInfo= new ServiceInfo();
				
				String description= iter.next();
				if(description.contains(keywords))
				{
				//set url of the service
				serviceInfo.setUrl(query.urlServicesDescription.get(i));
				//sets url description already recorded in the collection
				serviceInfo.setDescription(description);
				//add into the serviceinfo list 
				serviceInfos.add(serviceInfo);
				}
				i++;
			}

		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		return serviceInfos;
	}

}
