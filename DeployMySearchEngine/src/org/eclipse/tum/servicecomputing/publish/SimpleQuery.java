package org.eclipse.tum.servicecomputing.publish;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.juddi.ClassUtil;
import org.apache.juddi.v3.client.config.UDDIClientContainer;
import org.apache.juddi.v3.client.transport.Transport;
import org.apache.juddi.v3.client.transport.TransportException;
import org.uddi.api_v3.AuthToken;
import org.uddi.api_v3.BindingDetail;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BusinessInfo;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.Description;
import org.uddi.api_v3.FindBusiness;
import org.uddi.api_v3.FindQualifiers;
import org.uddi.api_v3.FindService;
import org.uddi.api_v3.FindTModel;
import org.uddi.api_v3.GetAuthToken;
import org.uddi.api_v3.GetBindingDetail;
import org.uddi.api_v3.GetServiceDetail;
import org.uddi.api_v3.Name;
import org.uddi.api_v3.ServiceDetail;
import org.uddi.api_v3.ServiceInfo;
import org.uddi.api_v3.ServiceList;
import org.uddi.api_v3.TModelInfo;
import org.uddi.api_v3.TModelInfos;
import org.uddi.api_v3.TModelList;
import org.uddi.v3_service.DispositionReportFaultMessage;
import org.uddi.v3_service.UDDIInquiryPortType;
import org.uddi.v3_service.UDDISecurityPortType;

public class SimpleQuery {
    
	private static UDDISecurityPortType security = null;	

	private static UDDIInquiryPortType inquiry = null;
	
	private static AuthToken myAuthToken = null;
	
	private ArrayList<String> urlServices=null;
	
	public ArrayList<String> urlServicesDescription=null;
	UDDIInquiryPortType uddiInquiryService=null;
	Transport transport;
	
	

	
	private static AuthToken getAuthToken(String username, String password) throws Exception {
		if(myAuthToken == null) {
			GetAuthToken getAuthTokenMyPub = new GetAuthToken();
			getAuthTokenMyPub.setUserID(username);
			getAuthTokenMyPub.setCred(password);

			// Making API call that retrieves the authentication token for the 'root' user.
			myAuthToken = security.getAuthToken(getAuthTokenMyPub);
			System.out.println (" User AUTHTOKEN = " + myAuthToken.getAuthInfo());
		}
		
		return myAuthToken;
	}
	
	public SimpleQuery() {
		 try {

	         String clazz = UDDIClientContainer.getUDDIClerkManager(null).
	         getClientConfig().getUDDINode("default").getProxyTransport();
	         Class<?> transportClass = ClassUtil.forName(clazz, Transport.class);

             if (transportClass!=null) {
            	  transport = (Transport) transportClass.getConstructor(String.class).newInstance("default");	  
	             security = transport.getUDDISecurityService();
	             inquiry = transport.getUDDIInquiryService();
	             transport.getUDDIPublishService();
	     		 uddiInquiryService = transport.getUDDIInquiryService();

	             	
	       
	         }   

	     } catch (Exception e) {
	            e.printStackTrace();
         }   
		 
		 
	}
	
	public void queryTModelByName(String name, String authinfo) {
		FindTModel ftm = new FindTModel();
		ftm.setAuthInfo(authinfo);
		Name tn = new Name();
		tn.setLang("en");
		tn.setValue(name);
		ftm.setName(tn);
		
		try {
		   TModelList tmlist = inquiry.findTModel(ftm);
			   
		   if(tmlist!=null) {
			   TModelInfos tminfos = tmlist.getTModelInfos();
			   
			   List<TModelInfo> tminfolist = tminfos.getTModelInfo();
			   for(TModelInfo tmif: tminfolist) {
				   
				   
				   System.out.println("TModel name:" + tmif.getName().getValue().toString());
				   System.out.println("TModel key:" + tmif.getTModelKey());
				   
				   List<Description> des = tmif.getDescription();
				   
				   
				   for(Description ds: des)
				      System.out.println("TModel description:" + ds.getValue());
			   }
		   }
		} catch(Exception e) {
		   e.printStackTrace();	
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			 
			
		     SimpleQuery query = new SimpleQuery();
		 
		     myAuthToken = getAuthToken("g12", "5qrf8dnmvnpcfm3jkau2hlmpuv");
		 //    myAuthToken = getAuthToken("root", "root");
		     
		     
		     query.queryTModelByName("ProcessWeatherWSDL2TModel SC2012 Demo Winter", myAuthToken.getAuthInfo());
		     query.queryTModelByName("PayMentWSDL2TModel SC2012 Demo Winter", myAuthToken.getAuthInfo());
		     query.queryTModelByName("FlightSystemWSDL2TModel SC2012 Demo Winter", myAuthToken.getAuthInfo());
		    
		    query.find();
		   //  query.deleteBusiness(myAuthToken.getAuthInfo(), "Group Name : G12");
		   
	   
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		

	}
	
	public static BusinessList findBusiness(String searchPattern) throws Exception  
    {  
		Name name = new Name();  
        name.setValue("%");  
        FindQualifiers qualifiers = new FindQualifiers();  
        qualifiers.getFindQualifier().add(org.apache.juddi.query.util.FindQualifiers.APPROXIMATE_MATCH);  
  
        // find business  
        org.uddi.api_v3.FindBusiness findBusiness = new org.uddi.api_v3.FindBusiness();  
        findBusiness.getName().add(name);  
        findBusiness.setFindQualifiers(qualifiers);  
  
        BusinessList lst = inquiry.findBusiness(findBusiness);
		return lst;    
    } 
	
	public ArrayList<String> find() throws Exception
	{
		//iniitialize list of urls and add them at the end
		urlServices= new ArrayList<String>();
		urlServicesDescription= new ArrayList<String>();
		
		org.uddi.api_v3.Name name = new org.uddi.api_v3.Name();  
        name.setValue("%");  
        name.setLang("en");
        org.uddi.api_v3.FindQualifiers qualifiers = new org.uddi.api_v3.FindQualifiers();  
        qualifiers.getFindQualifier().add(org.apache.juddi.query.util.FindQualifiers.APPROXIMATE_MATCH);  
       
		
        
        

        //UDDIInquiryPortType uddiInquiryService = transport.getUDDIInquiryService();
    	//	ServiceList foundServices = uddiInquiryService.findService(fs);
    		
		FindService findservice= new FindService();
		
		findservice.setAuthInfo(myAuthToken.getAuthInfo());
		findservice.getName().add(getWildcardName());
	
		findservice.setFindQualifiers(approximateQualifier());
		
		ServiceList list1=inquiry.findService(findservice);
		
		GetServiceDetail gsd=new GetServiceDetail();
		
		for(ServiceInfo serviceInfo :list1.getServiceInfos().getServiceInfo()){
		    gsd.getServiceKey().add(serviceInfo.getServiceKey());
		    System.out.println(serviceInfo.getServiceKey());
		    String servicekey=serviceInfo.getServiceKey();

		    GetServiceDetail getServiceDetail=new GetServiceDetail();
		    getServiceDetail.setAuthInfo(myAuthToken.getAuthInfo());
		    getServiceDetail.getServiceKey().add(servicekey);
		    ServiceDetail serviceDetail=inquiry.getServiceDetail(getServiceDetail);
		    BusinessService businessservice=serviceDetail.getBusinessService().get(0);
		    
		    System.out.println("fetched service name:"+businessservice.getName().get(0).getValue());
		    
		    if(businessservice.getBindingTemplates()!=null)
		    {
		    String bindingkey = businessservice.getBindingTemplates().getBindingTemplate().get(0).getBindingKey();
		    
		    System.out.println(bindingkey);

		    GetBindingDetail gbd = new GetBindingDetail();
		    gbd.setAuthInfo(myAuthToken.getAuthInfo());
		    gbd.getBindingKey().add(bindingkey);
		    BindingDetail bindingdetail=inquiry.getBindingDetail(gbd);
		    BindingTemplate bindingtemplate=bindingdetail.getBindingTemplate().get(0);
		    
		    
		    //gives service description
		    //System.out.println(bindingtemplate.getDescription().get(0).getValue());
		    
		    urlServicesDescription.add(businessservice.getName().get(0).getValue());
		    
		    String accesspoint=bindingtemplate.getAccessPoint().getValue();
		    
		    //adds urls or say accesspoints to the urlservices list
		    urlServices.add(accesspoint);
		    
		    System.out.println(accesspoint);
		    }
		    
		}
		return urlServicesDescription;
	}
	
	private Name getWildcardName() {
		Name name = new Name();
		name.setValue("%");
		return name;
	}

	private FindQualifiers approximateQualifier() {
		FindQualifiers fq = new FindQualifiers();
		fq.getFindQualifier().add("approximateMatch");
		return fq;
	}
public void deleteBusiness(String authToken, String businessName) throws TransportException, DispositionReportFaultMessage, RemoteException {
		
		
		Name name = new Name();
		name.setValue(businessName);
		
		FindBusiness fb = new FindBusiness();
		fb.setAuthInfo(authToken);
		fb.getName().add(name);
		fb.setMaxRows(999);
		
		BusinessList foundBusinesses = uddiInquiryService.findBusiness(fb);
		
		
		if(foundBusinesses.getBusinessInfos() != null) {		
			for(BusinessInfo business : foundBusinesses.getBusinessInfos().getBusinessInfo()) {
				System.out.println("delete business: " + business.getName() + " - " + business.getBusinessKey());
				org.uddi.api_v3.DeleteBusiness db = new org.uddi.api_v3.DeleteBusiness();
				db.setAuthInfo(authToken);
				db.getBusinessKey().add(business.getBusinessKey());
				transport.getUDDIPublishService().deleteBusiness(db);
			}
		}
		else {
			System.out.println("didn't found any business");
		}
	}
	

}
