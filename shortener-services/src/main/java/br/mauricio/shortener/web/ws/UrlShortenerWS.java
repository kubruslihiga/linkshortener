package br.mauricio.shortener.web.ws;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import br.mauricio.shortener.UrlShort;
import br.mauricio.shortener.UrlShortFilter;
import br.mauricio.shortener.exception.ShortenerException;
import br.mauricio.shortener.service.UrlShortenerService;

@RestController
@CrossOrigin
public class UrlShortenerWS implements Serializable {

	private static final long serialVersionUID = 1L;

	@Value("${shortener.app.hostname}")
	private String hostname;

	@Autowired
	private UrlShortenerService urlShortenerService;

	@PostMapping(path = "/post", consumes = "application/json", produces = "application/json")
	public UrlShort shortMyLink(@Validated @RequestBody UrlShort urlShort) throws ShortenerException {
		UrlShort ret = urlShortenerService.shortMyLink(urlShort.getUrlComplete());
		StringBuilder shortUrl = new StringBuilder(hostname);
		if (shortUrl.charAt(shortUrl.length() - 1) != '/') {
			shortUrl.append("/");
		}
		ret.setShortUrl(shortUrl.append(ret.getId()).toString());
		return ret;
	}

	@GetMapping(path = "/{id}", produces = "application/json")
	public RedirectView redirect(@PathVariable("id") String id) throws ShortenerException {
		try {
			UrlShort urlShort = urlShortenerService.getByShortLink(id);
			return new RedirectView(urlShort.getUrlComplete());
		} catch (ShortenerException e) {
			if ("LINK_INVALID".equals(e.getCode())) {
				return new RedirectView("/error/invalidpage");
			}
			throw e;
		}
	}

	@GetMapping(path = "/link/access", produces = "application/json")
	public List<UrlShort> linkAccess(UrlShortFilter filter) throws ShortenerException {
		List<UrlShort> ret = urlShortenerService.find(filter);
		for (UrlShort urlShort : ret) {
			StringBuilder sb = new StringBuilder(hostname);
			urlShort.setShortUrl(sb.append("/").append(urlShort.getId()).toString());
		}
		return ret;
		
	}
}
