package org.eclipse.tum.servicecomputing.publish;
/**
 * 
 * @author shaktisaxena
 *
 */
public interface Constants {
	
	//user name for juddi server
	//final String USERNAME="g12";
	final String USERNAME="g12";
	//password for juddi server
	final String PASSWORD="5qrf8dnmvnpcfm3jkau2hlmpuv";
	//Business Group
	final String BUSINESS_GROUP="The ServiceComputing Group Ralf,Yolanda,Shakti";
	//WeatherService Description
	final String DESCWEATHERSERVICE="The underline Services processes weather Queries!!!";
	//PaymentService Description
	final String DESCPAYMENTSSYSTEM="The underline Services processes Transfer Requests from One account to another another!!!";
	//FlightService Description
	final String DESCFLIGHTSYSTEM = "The underline Services processes Flight Reservation!!!";
	 /**
     * This creates the unique, well-defined tModel representing the service's WSDL
     */
	
	// URL for weather webservices
	final String URLWEATHER = "http://vmjacobsen4.informatik.tu-muenchen.de:8080/axis2/services/g12/ProcessWeatherQuery?wsdl";
	//URL for PAYMENT webservice
	final String URLPAYMENT = "http://vmjacobsen4.informatik.tu-muenchen.de:8080/axis2/services/g12/PaymentService?wsdl";
	//URL for FLIGHT booking webservices
	final String URLFLIGHT = "http://vmjacobsen4.informatik.tu-muenchen.de:8080/axis2/services/g12/FlightTicketService?wsdl";
	//tmodel for weather webservice
	final String NAMETMODELPROCESSWEATHER = "ProcessWeatherWSDL2TModel SC2012 Demo Winter";
	//key information stored in tmodel of weather service
	final String KEYINFOFORMATIONPROCESSWEATHER = "Process Weather";
	//tmodel for payment system
	final String NAMETMODELPAYMENTSYSTEM = "PayMentWSDL2TModel SC2012 Demo Winter";
	//key information stored in tmodel of payment system
	final String KEYINFOFORMATIONPAYEMENTSYSTEM = "Process Payment";
	//tmodel for flight sytem
	final String NAMETMODELFLIGHTSYSTEM = "FlightSystemWSDL2TModel SC2012 Demo Winter";
	//key information stored in tmodel of flightSytem
	final String KEYINFOFORMATIONFIGHTSYSTEM = "Flight Booking System";
}
