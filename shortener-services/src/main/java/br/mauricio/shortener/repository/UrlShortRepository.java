package br.mauricio.shortener.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.mauricio.shortener.UrlShort;

public interface UrlShortRepository extends MongoRepository<UrlShort, String> {

	Optional<UrlShort> findByUrlComplete(String urlComplete);
}
