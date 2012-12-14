package com.tum.shakti;

public class TransactionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String errorMessage;
	
	
	TransactionException(String errorMessage) {
	     setErrorMessage(errorMessage);
	   }
	   public String toString(){
	     return ("Exception Number =  "+errorMessage) ;
	  }
	   
	   public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
}
