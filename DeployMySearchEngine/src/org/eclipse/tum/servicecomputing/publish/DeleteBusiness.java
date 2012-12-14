package org.eclipse.tum.servicecomputing.publish;

import java.rmi.RemoteException;

import org.apache.juddi.v3.client.transport.Transport;
import org.apache.juddi.v3.client.transport.TransportException;
import org.uddi.api_v3.AuthToken;
import org.uddi.api_v3.BusinessInfo;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.FindBusiness;
import org.uddi.api_v3.GetAuthToken;
import org.uddi.api_v3.Name;
import org.uddi.v3_service.DispositionReportFaultMessage;
import org.uddi.v3_service.UDDIInquiryPortType;
import org.uddi.v3_service.UDDISecurityPortType;

public class DeleteBusiness {
	
	private static Transport transport;
	
	private static AuthToken myAuthToken = null;
	
	private static UDDISecurityPortType security = null;


	private static AuthToken getAuthToken(String username, String password)
			throws Exception {
		if (myAuthToken == null) {

			GetAuthToken getAuthTokenMyPub = new GetAuthToken();
			getAuthTokenMyPub.setUserID(username);
			getAuthTokenMyPub.setCred(password);

			// Making API call that retrieves the authentication token for the
			// 'root' user.
			myAuthToken = security.getAuthToken(getAuthTokenMyPub);
			System.out
					.println(" User AUTHTOKEN = " + myAuthToken.getAuthInfo());
		}

		return myAuthToken;
	}

	public DeleteBusiness(Transport transport) {
		DeleteBusiness.transport = transport;
		
		
	}
	
	
	public void deleteBusiness(String authToken, String businessName) throws TransportException, DispositionReportFaultMessage, RemoteException {
		
		UDDIInquiryPortType uddiInquiryService = transport.getUDDIInquiryService();
		
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
	
	public static void main(String[] args) {
		try {
			myAuthToken = getAuthToken("root", "root");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			new DeleteBusiness(transport).deleteBusiness(myAuthToken.getAuthInfo(), "My Service");
		} catch (DispositionReportFaultMessage e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
}
