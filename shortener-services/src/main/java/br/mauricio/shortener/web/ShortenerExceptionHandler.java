package br.mauricio.shortener.web;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.mauricio.shortener.exception.ShortenerException;

@ControllerAdvice
public class ShortenerExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Log LOG = LogFactory.getLog(ShortenerExceptionHandler.class);

	@Autowired
	private MessageSource messageSource;

	private Locale locale = LocaleContextHolder.getLocale();

	@ResponseBody
	@ExceptionHandler(ShortenerException.class)
	public ResponseEntity<ErrorResponse> handler(ShortenerException e) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Error...");
		}
		String message = messageSource.getMessage(e.getCode(), e.getParams(), locale);
		ErrorResponse ret = new ErrorResponse(e.getCode(), message);
		return ResponseEntity.badRequest().body(ret);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return super.handleBindException(ex, headers, status, request);
	}

	@Override

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<Object>(ex.getBindingResult(), HttpStatus.BAD_REQUEST);
	}
}
