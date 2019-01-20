package br.mauricio.shortener.exception;

public class ShortenerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

	private Object[] params;
	public ShortenerException(String code) {
		super();
		this.code = code;
	}

	public ShortenerException(String code, Object... params) {
		super();
		this.code = code;
		this.params = params;
	}

	public String getCode() {
		return code;
	}

	public Object[] getParams() {
		return params;
	}
}
