package org.eclipse.tum.servicecomputing.publish;

import java.util.List;

import org.apache.juddi.ClassUtil;
import org.apache.juddi.v3.client.config.UDDIClientContainer;
import org.apache.juddi.v3.client.transport.Transport;
import org.apache.juddi.v3_service.JUDDIApiPortType;
import org.uddi.api_v3.AccessPoint;
import org.uddi.api_v3.AuthToken;
import org.uddi.api_v3.BindingDetail;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.BusinessServices;
import org.uddi.api_v3.CategoryBag;
import org.uddi.api_v3.GetAuthToken;
import org.uddi.api_v3.IdentifierBag;
import org.uddi.api_v3.InstanceDetails;
import org.uddi.api_v3.KeyedReference;
import org.uddi.api_v3.Name;
import org.uddi.api_v3.OverviewDoc;
import org.uddi.api_v3.OverviewURL;
import org.uddi.api_v3.SaveBinding;
import org.uddi.api_v3.SaveBusiness;
import org.uddi.api_v3.SaveService;
import org.uddi.api_v3.SaveTModel;
import org.uddi.api_v3.ServiceDetail;
import org.uddi.api_v3.TModel;
import org.uddi.api_v3.TModelDetail;
import org.uddi.api_v3.TModelInstanceDetails;
import org.uddi.api_v3.TModelInstanceInfo;
import org.uddi.v3_service.UDDIPublicationPortType;
import org.uddi.v3_service.UDDISecurityPortType;

public class PublishMyWebServices implements Constants {

	private static UDDISecurityPortType security = null;
	
	private static JUDDIApiPortType juddiApi = null;
	
	private static UDDIPublicationPortType publish = null;
	
	private static AuthToken myAuthToken = null;

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

	// main function that invokes publish services function.
	
	public static void main(String[] args) {
		
		PublishMyWebServices publishMywebServices = new PublishMyWebServices();
		
		publishMywebServices.publish();
		
	}

	// default constructor
	public PublishMyWebServices() {

		try {

			String clazz = UDDIClientContainer.getUDDIClerkManager(null)
					.getClientConfig().getUDDINode("default")
					.getProxyTransport();
			Class<?> transportClass = ClassUtil.forName(clazz, Transport.class);

			if (transportClass != null) {
				Transport transport = (Transport) transportClass
						.getConstructor(String.class).newInstance("default");
				security = transport.getUDDISecurityService();
				juddiApi = transport.getJUDDIApiService();
				publish = transport.getUDDIPublishService();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * return the key of the business entity
	 */
	public String createBusinessEntry(String entityname, String authinfo)
			throws Exception {
		// Creating the parent business entity that will contain our service.
		BusinessEntity myBusEntity = new BusinessEntity();
		Name myBusName = new Name();
		myBusName.setValue(entityname);
		myBusEntity.getName().add(myBusName);

		SaveBusiness sb = new SaveBusiness();
		sb.getBusinessEntity().add(myBusEntity);

		sb.setAuthInfo(authinfo);
		BusinessDetail bd = publish.saveBusiness(sb);
		String myBusKey = bd.getBusinessEntity().get(0).getBusinessKey();
		System.out.println("myBusiness key:  " + myBusKey);

		return myBusKey;
	}

	public void publishBusinessEntry(String BEname, String BEKey,
			String username, String password) throws Exception {
		// Creating the parent business entity that will contain our service.
		BusinessEntity myBusEntity = new BusinessEntity();
		Name myBusName = new Name();
		myBusName.setValue(BEname);
		myBusEntity.getName().add(myBusName);
		myBusEntity.setBusinessKey(BEKey);

		// Adding the business entity to the "save" structure, using our
		// publisher's authentication info and saving away.
		SaveBusiness sb = new SaveBusiness();
		sb.getBusinessEntity().add(myBusEntity);

		AuthToken myPubAuthToken = getAuthToken(username, password);
		sb.setAuthInfo(myPubAuthToken.getAuthInfo());
		BusinessDetail bd = publish.saveBusiness(sb);
		String myBusKey = bd.getBusinessEntity().get(0).getBusinessKey();
		System.out.println("myBusiness key:  " + myBusKey);
	}

	public void publishBusinessService() {

	}

	public void publish() {
		try {
			// Making API call that retrieves the authentication token for the
			// 'root' user.

			AuthToken myPubAuthToken = getAuthToken(Constants.USERNAME,
					Constants.PASSWORD);

			// Creating the parent business entity that will contain our
			// service.
			BusinessEntity myBusEntity = new BusinessEntity();
			Name myBusName = new Name();
			myBusName.setValue(Constants.BUSINESS_GROUP);
			myBusEntity.getName().add(myBusName);

			// Adding the business entity to the "save" structure, using our
			// publisher's authentication info and saving away.
			SaveBusiness sb = new SaveBusiness();
			sb.getBusinessEntity().add(myBusEntity);
			sb.setAuthInfo(myPubAuthToken.getAuthInfo());
			BusinessDetail bd = publish.saveBusiness(sb);
			String myBusKey = bd.getBusinessEntity().get(0).getBusinessKey();
			System.out.println("myBusiness key:  " + myBusKey);

			// Creating a service to save. Only adding the minimum data: the
			// parent business key retrieved from saving the business
			// above and a single name.
			BusinessService myService = new BusinessService();
			myService.setBusinessKey(myBusKey);
			Name myServName = new Name();
			myServName.setValue(Constants.DESCWEATHERSERVICE);
			myService.getName().add(myServName);

			BusinessService myServicePaymentSystem = new BusinessService();
			myServicePaymentSystem.setBusinessKey(myBusKey);
			Name myServPaymentSystemName = new Name();
			myServPaymentSystemName.setValue(Constants.DESCPAYMENTSSYSTEM);
			myServicePaymentSystem.getName().add(myServPaymentSystemName);

			BusinessService myServiceFlightQueriesSystem = new BusinessService();
			myServiceFlightQueriesSystem.setBusinessKey(myBusKey);
			Name myServFlightSystemName = new Name();
			myServFlightSystemName.setValue(Constants.DESCFLIGHTSYSTEM);
			myServiceFlightQueriesSystem.getName().add(myServFlightSystemName);

			BusinessServices businessServices = new BusinessServices();
			List<BusinessService> bslist = businessServices
					.getBusinessService();
			bslist.add(myService);// first service
			bslist.add(myServicePaymentSystem);// second service
			bslist.add(myServiceFlightQueriesSystem); // third service

			myBusEntity.setBusinessServices(businessServices);

			// Adding the service to the "save" structure, using our publisher's
			// authentication info and saving away.
			SaveService ss = new SaveService();

			SaveService ss2 = new SaveService();

			SaveService ss3 = new SaveService();
			ss.getBusinessService().add(myService);
			ss2.getBusinessService().add(myServicePaymentSystem);
			ss3.getBusinessService().add(myServiceFlightQueriesSystem);

			ss.setAuthInfo(myPubAuthToken.getAuthInfo());

			ss2.setAuthInfo(myPubAuthToken.getAuthInfo());

			ss3.setAuthInfo(myPubAuthToken.getAuthInfo());

			ServiceDetail sd = publish.saveService(ss);
			ServiceDetail sd2 = publish.saveService(ss2);
			ServiceDetail sd3 = publish.saveService(ss3);
			String myServKey = sd.getBusinessService().get(0).getServiceKey();

			String myServKey2 = sd2.getBusinessService().get(0).getServiceKey();

			String myServKey3 = sd3.getBusinessService().get(0).getServiceKey();

			System.out.println("myService key:  " + myServKey);

			System.out.println("myService key:  " + myServKey2);

			System.out.println("myService key:  " + myServKey3);

			// Add binding templates
			BindingTemplates templates = new BindingTemplates();
			myService.setBindingTemplates(templates);

			BindingTemplates templates2 = new BindingTemplates();
			myServiceFlightQueriesSystem.setBindingTemplates(templates2);

			BindingTemplates templates3 = new BindingTemplates();
			myServicePaymentSystem.setBindingTemplates(templates3);

			BindingTemplate myTemplate = new BindingTemplate();
			myTemplate.setServiceKey(myServKey);

			// second service
			BindingTemplate myTemplate2 = new BindingTemplate();
			myTemplate2.setServiceKey(myServKey2);

			// third service

			BindingTemplate myTemplate3 = new BindingTemplate();
			myTemplate3.setServiceKey(myServKey3);

			List<BindingTemplate> templateList = templates.getBindingTemplate();
			List<BindingTemplate> templateList2 = templates2
					.getBindingTemplate();
			List<BindingTemplate> templateList3 = templates3
					.getBindingTemplate();

			templateList.add(myTemplate); // first service
			templateList2.add(myTemplate2);// second service
			templateList3.add(myTemplate3);// third service

			// Add TModel

			TModelDetail tModelDetail = publishWSDL_tModel(
					Constants.URLWEATHER, myPubAuthToken.getAuthInfo(),
					Constants.NAMETMODELPROCESSWEATHER,
					Constants.KEYINFOFORMATIONPROCESSWEATHER);

			TModelDetail tModelDetail_1 = publishWSDL_tModel(
					Constants.URLPAYMENT, myPubAuthToken.getAuthInfo(),
					Constants.NAMETMODELPAYMENTSYSTEM,
					Constants.KEYINFOFORMATIONPAYEMENTSYSTEM);

			TModelDetail tModelDetail_2 = publishWSDL_tModel(
					Constants.URLFLIGHT, myPubAuthToken.getAuthInfo(),
					Constants.NAMETMODELFLIGHTSYSTEM,
					Constants.KEYINFOFORMATIONFIGHTSYSTEM);

			List<TModel> tModelList = tModelDetail.getTModel();

			List<TModel> tModelList1 = tModelDetail_1.getTModel();

			List<TModel> tModelList2 = tModelDetail_2.getTModel();

			TModelInstanceDetails tModelInstanceDetails = new TModelInstanceDetails();
			List<TModelInstanceInfo> tModelInstanceInfoList = tModelInstanceDetails
					.getTModelInstanceInfo();

			for (TModel tm : tModelList) {
				TModelInstanceInfo tModelInstanceInfo = new TModelInstanceInfo();
				tModelInstanceInfoList.add(tModelInstanceInfo);

				tModelInstanceInfo.setTModelKey(tm.getTModelKey());

				InstanceDetails instanceDetails = new InstanceDetails();
				instanceDetails
						.setInstanceParms("ProcessWeather tModel instance");
				tModelInstanceInfo.setInstanceDetails(instanceDetails);
			}

			for (TModel tm : tModelList1) {
				TModelInstanceInfo tModelInstanceInfo1 = new TModelInstanceInfo();
				tModelInstanceInfoList.add(tModelInstanceInfo1);

				tModelInstanceInfo1.setTModelKey(tm.getTModelKey());

				InstanceDetails instanceDetails = new InstanceDetails();
				instanceDetails.setInstanceParms("Payment tModel instance");
				tModelInstanceInfo1.setInstanceDetails(instanceDetails);
			}

			for (TModel tm : tModelList2) {
				TModelInstanceInfo tModelInstanceInfo2 = new TModelInstanceInfo();
				tModelInstanceInfoList.add(tModelInstanceInfo2);

				tModelInstanceInfo2.setTModelKey(tm.getTModelKey());

				InstanceDetails instanceDetails = new InstanceDetails();
				instanceDetails.setInstanceParms("Flight tModel instance");
				tModelInstanceInfo2.setInstanceDetails(instanceDetails);
			}

			/**
			 * Associate the set of tModels with the Service's BindingTemplate
			 */
			myTemplate.setTModelInstanceDetails(tModelInstanceDetails);

			myTemplate2.setTModelInstanceDetails(tModelInstanceDetails);

			myTemplate3.setTModelInstanceDetails(tModelInstanceDetails);

			/**
			 * The binding template needs an [ accessPoint | hostRedirector ]
			 */
			AccessPoint weatherAccessPoint = createAccessPoint(Constants.URLWEATHER
					.substring(0, Constants.URLWEATHER.lastIndexOf("?")));

			// second service access point
			AccessPoint paymentAccessPoint = createAccessPoint(Constants.URLPAYMENT
					.substring(0, Constants.URLPAYMENT.lastIndexOf("?")));

			AccessPoint flightAccessPoint = createAccessPoint(Constants.URLFLIGHT
					.substring(0, Constants.URLFLIGHT.lastIndexOf("?")));

			myTemplate.setAccessPoint(weatherAccessPoint);
			// second service
			myTemplate2.setAccessPoint(paymentAccessPoint);
			// third service
			myTemplate3.setAccessPoint(flightAccessPoint);

			// set CategoryBag
			myTemplate.setCategoryBag(tModelList.get(0).getCategoryBag());

			// set category bag for second service
			myTemplate2.setCategoryBag(tModelList1.get(0).getCategoryBag());

			// set category bag for third service
			myTemplate3.setCategoryBag(tModelList2.get(0).getCategoryBag());

			// Adding the binding to the "save" structure, using our publisher's
			// authentication info and saving away.
			SaveBinding sbinding = new SaveBinding();
			SaveBinding sbinding2 = new SaveBinding();
			SaveBinding sbinding3 = new SaveBinding();

			sbinding.getBindingTemplate().add(myTemplate);
			sbinding.setAuthInfo(myPubAuthToken.getAuthInfo());

			sbinding2.getBindingTemplate().add(myTemplate2);
			sbinding2.setAuthInfo(myPubAuthToken.getAuthInfo());

			sbinding3.getBindingTemplate().add(myTemplate3);
			sbinding3.setAuthInfo(myPubAuthToken.getAuthInfo());

			BindingDetail binddetail = publish.saveBinding(sbinding);
			BindingDetail binddetail2 = publish.saveBinding(sbinding2);
			BindingDetail binddetail3 = publish.saveBinding(sbinding3);

			System.out.println("My binding key:  "
					+ binddetail.getBindingTemplate().get(0).getBindingKey());
			System.out.println("My binding key2:  "
					+ binddetail2.getBindingTemplate().get(0).getBindingKey());

			System.out.println("My binding key3:  "
					+ binddetail3.getBindingTemplate().get(0).getBindingKey());

			// update the service to the "save" structure, using our publisher's
			// authentication info and saving away.
			SaveService updates = new SaveService();
			SaveService updates2 = new SaveService();

			SaveService updates3 = new SaveService();

			myService.setServiceKey(myServKey);
			// second service
			myServiceFlightQueriesSystem.setServiceKey(myServKey2);
			// third service
			myServicePaymentSystem.setServiceKey(myServKey3);

			updates.getBusinessService().add(myService);
			updates2.getBusinessService().add(myServicePaymentSystem);
			updates3.getBusinessService().add(myServiceFlightQueriesSystem);

			updates.setAuthInfo(myPubAuthToken.getAuthInfo());
			updates2.setAuthInfo(myPubAuthToken.getAuthInfo());
			updates3.setAuthInfo(myPubAuthToken.getAuthInfo());
			sd = publish.saveService(updates);

			sd2 = publish.saveService(updates2);
			sd3 = publish.saveService(updates3);

			myServKey = sd.getBusinessService().get(0).getServiceKey();
			myServKey2 = sd2.getBusinessService().get(0).getServiceKey();
			myServKey3 = sd3.getBusinessService().get(0).getServiceKey();

			System.out.println("myService key1:  " + myServKey);

			System.out.println("myService key2:  " + myServKey2);

			System.out.println("myService key3:  " + myServKey3);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//code to publish/register toModel to tum server
	
	private TModelDetail publishWSDL_tModel(String url, String authinfo,
			String tmodelName, String keyInformationAboutService)
			throws Exception {

		TModel serviceWSDL_tModel = new TModel();

		// set the name of tModel
		Name tModelName = new Name();
		tModelName.setLang("en");
		
		tModelName.setValue(tmodelName);
		serviceWSDL_tModel.setName(tModelName);

		serviceWSDL_tModel.setDeleted(false);

		// set Overview Docs
		OverviewDoc service_overviewDoc = new OverviewDoc();
		OverviewURL overviewurl = new OverviewURL();
		overviewurl.setUseType("WSDL source document");
		overviewurl.setValue(url);
		service_overviewDoc.setOverviewURL(overviewurl);
		List<OverviewDoc> overviewdoclist = serviceWSDL_tModel.getOverviewDoc();
		overviewdoclist.add(service_overviewDoc);

		TModelDetail tModelDetail = new TModelDetail();
		List<TModel> tModelList = tModelDetail.getTModel();
		tModelList.add(serviceWSDL_tModel);

		// Adding the tModel to the "save" structure, using our publisher's
		// authentication info and saving away.
		SaveTModel saveTModel = new SaveTModel();
		saveTModel.getTModel().addAll(tModelList);
		saveTModel.setAuthInfo(authinfo);
		tModelDetail = publish.saveTModel(saveTModel);

		String tModelKey = tModelDetail.getTModel().get(0).getTModelKey();
		System.out.println("tModel key: " + tModelKey);
		serviceWSDL_tModel.setTModelKey(tModelKey);

		// set CategoryBag
		CategoryBag catBag = new CategoryBag();
		List<KeyedReference> krlist = catBag.getKeyedReference();
		KeyedReference kr = new KeyedReference();
		kr.setKeyName("uuid-org:types");
		kr.setKeyValue("wsdlSpec");
		kr.setTModelKey(tModelKey);
		krlist.add(kr);

		serviceWSDL_tModel.setCategoryBag(catBag);

		// set the IdentifierBag
		IdentifierBag idBag = new IdentifierBag();
		List<KeyedReference> idkeylist = idBag.getKeyedReference();
		KeyedReference idKey = new KeyedReference();
		idKey.setKeyName("service name");
		idKey.setKeyValue(keyInformationAboutService);
		idKey.setTModelKey(tModelKey);
		idkeylist.add(idKey);

		serviceWSDL_tModel.setIdentifierBag(idBag);

		// update the tModel to the "save" structure, using our publisher's
		// authentication info and saving away.
		SaveTModel updateTModel = new SaveTModel();
		updateTModel.getTModel().addAll(tModelList);
		updateTModel.setAuthInfo(authinfo);
		tModelDetail = publish.saveTModel(updateTModel);

		tModelKey = tModelDetail.getTModel().get(0).getTModelKey();
		System.out.println("tModel key: " + tModelKey);

		return tModelDetail;
	}

	//return url of webservice registered
	
	private AccessPoint createAccessPoint(String url) {
		AccessPoint accessPoint = new AccessPoint();
		accessPoint.setUseType("http");
		accessPoint.setValue(url);
		return accessPoint;
	}

}
