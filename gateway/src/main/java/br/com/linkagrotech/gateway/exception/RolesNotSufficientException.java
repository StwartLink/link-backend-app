package br.com.linkagrotech.gateway.exception;

public class RolesNotSufficientException extends RuntimeException {
	public RolesNotSufficientException(String message) {
		super(message);
	}
}
