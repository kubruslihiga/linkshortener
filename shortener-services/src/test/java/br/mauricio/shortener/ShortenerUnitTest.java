package br.mauricio.shortener;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.mauricio.shortener.exception.ShortenerException;
import br.mauricio.shortener.repository.SequenceRepository;
import br.mauricio.shortener.repository.UrlShortRepository;
import br.mauricio.shortener.service.impl.UrlShortenerServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ShortenerUnitTest {

	@InjectMocks
	private UrlShortenerServiceImpl urlShortenerService;

	@Mock
	private SequenceRepository sequenceRepository;

	@Mock
	private UrlShortRepository urlShortRepository;

	@Test(expected = ShortenerException.class)
	public void itsNotALink() throws ShortenerException {
		assertNotNull(urlShortenerService);
		UrlShort urlShort = new UrlShort();
		urlShort.setCounter(Long.valueOf(0));
		urlShort.setId("my id");
		urlShort.setShortUrl("http://su.do/a");
		urlShort.setUrlComplete("http://www.google.com");
		when(urlShortRepository.findByUrlComplete(anyString())).thenReturn(Optional.empty());
		urlShortenerService.shortMyLink("it's not a link");
	}

	@Test
	public void saveMock() throws ShortenerException {
		assertNotNull(urlShortenerService);
		UrlShort urlShort = new UrlShort();
		urlShort.setCounter(Long.valueOf(0));
		urlShort.setId("my id");
		urlShort.setShortUrl("http://su.do/a");
		urlShort.setUrlComplete("http://www.google.com");
		when(urlShortRepository.findByUrlComplete(anyString())).thenReturn(Optional.empty());
		when(urlShortRepository.save(any())).thenReturn(urlShort);
		urlShortenerService.shortMyLink("http://www.itsalink.com.br");
		urlShortenerService.shortMyLink("https://www.itsalink.com.br");
	}
}
