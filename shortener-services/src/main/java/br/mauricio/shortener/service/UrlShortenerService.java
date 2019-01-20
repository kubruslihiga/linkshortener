package br.mauricio.shortener.service;

import java.util.List;

import br.mauricio.shortener.UrlShort;
import br.mauricio.shortener.UrlShortFilter;
import br.mauricio.shortener.exception.ShortenerException;

public interface UrlShortenerService {

	UrlShort shortMyLink(String urlComplete) throws ShortenerException;
	
	UrlShort getByShortLink(String shortLink) throws ShortenerException;

	List<UrlShort> find(UrlShortFilter filter) throws ShortenerException;
}
