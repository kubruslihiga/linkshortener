package br.mauricio.shortener.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.mauricio.shortener.CriteriaSort;
import br.mauricio.shortener.UrlShort;
import br.mauricio.shortener.UrlShortFilter;
import br.mauricio.shortener.exception.ShortenerException;
import br.mauricio.shortener.repository.SequenceRepository;
import br.mauricio.shortener.repository.UrlShortRepository;
import br.mauricio.shortener.service.UrlShortenerService;

@Service
@Transactional(rollbackFor = Exception.class)
public class UrlShortenerServiceImpl implements UrlShortenerService {

	private final List<Character> indexToCharTable = new ArrayList<>();

	@Autowired
	private SequenceRepository sequenceRepository;

	@Autowired
	private UrlShortRepository urlShortRepository;

	public UrlShortenerServiceImpl() {
		initialize();
	}

	@Override
	public UrlShort shortMyLink(@NotNull final String urlComplete) throws ShortenerException {
		Optional<UrlShort> opt = urlShortRepository.findByUrlComplete(urlComplete);
		if (opt.isPresent()) {
			return opt.get();
		}
		if (UrlValidator.getInstance().isValid(urlComplete)) {
			Long nextSequenceId = sequenceRepository.getNextSequenceId();
			String uniqueID = createUniqueID(nextSequenceId);
			UrlShort urlShort = new UrlShort();
			urlShort.setId(uniqueID);
			urlShort.setUrlComplete(urlComplete);
			UrlShort ret = urlShortRepository.save(urlShort);
			return ret;
		}
		throw new ShortenerException("URL_INVALID", urlComplete);
	}

	@Override
	public UrlShort getByShortLink(@NotNull final String shortLink) throws ShortenerException {
		Optional<UrlShort> opt = urlShortRepository.findById(shortLink);
		if (opt.isPresent()) {
			UrlShort urlShort = opt.get();
			urlShort.setCounter(urlShort.getCounter() + Long.valueOf(1L));
			return urlShortRepository.save(urlShort);
		}
		throw new ShortenerException("LINK_INVALID");
	}

	@Override
	public List<UrlShort> find(UrlShortFilter filter) throws ShortenerException {
		Sort sort = null;
		if (filter != null) {
			if (CriteriaSort.DESC == filter.getCounterSort()) {
				sort = new Sort(Direction.DESC, "counter");
			} else if (CriteriaSort.ASC == filter.getCounterSort()) {
				sort = new Sort(Direction.ASC, "counter");
			}
		}
		if (sort == null) {
			return urlShortRepository.findAll();
		} else {
			return urlShortRepository.findAll(sort);
		}
	}

	private String createUniqueID(Long id) {
		List<Integer> base62ID = convertBase10ToBase62ID(id);
		StringBuilder uniqueURLID = new StringBuilder();
		for (int digit : base62ID) {
			uniqueURLID.append(indexToCharTable.get(digit));
		}
		return uniqueURLID.toString();
	}

	private List<Integer> convertBase10ToBase62ID(Long id) {
		LinkedList<Integer> digits = new LinkedList<>();
		if (Long.valueOf(0).equals(id)) {
			return Arrays.asList(Integer.valueOf(0));
		}
		while (id > 0) {
			int remainder = (int) (id % 62);
			digits.addFirst(remainder);
			id /= 62;
		}
		return digits;
	}

	private void initialize() {
		// 0->a, 1->b, ..., 25->z, ..., 52->0, 61->9
		for (int i = 0; i < 26; ++i) {
			char c = 'a';
			c += i;
			indexToCharTable.add(c);
		}
		for (int i = 26; i < 52; ++i) {
			char c = 'A';
			c += (i - 26);
			indexToCharTable.add(c);
		}
		for (int i = 52; i < 62; ++i) {
			char c = '0';
			c += (i - 52);
			indexToCharTable.add(c);
		}
	}

}
