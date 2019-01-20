package br.mauricio.shortener;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Document
public class UrlShort {

	@Id
	@JsonIgnore
	private String id;
	@Transient
	private String shortUrl;
	@NotNull
	@URL
	private String urlComplete;
	@JsonProperty(access = Access.READ_ONLY)
	private Long counter = Long.valueOf(0L);

	public UrlShort() {

	}

	@PersistenceConstructor
	public UrlShort(String urlComplete, Long counter, String id) {
		this.urlComplete = urlComplete;
		this.counter = counter;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrlComplete() {
		return urlComplete;
	}

	public void setUrlComplete(String urlComplete) {
		this.urlComplete = urlComplete;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public Long getCounter() {
		return counter;
	}

	public void setCounter(Long counter) {
		this.counter = counter;
	}

}
