package br.mauricio.shortener.repository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import br.mauricio.shortener.SequenceId;

@Repository
public class SequenceRepository {

	@Autowired
	private MongoOperations mongoOperation;

	@Value("${sequence.key}")
	private String sequenceKey;

	@PostConstruct
	void init() {
		SequenceId sequenceId = mongoOperation.findById(sequenceKey, SequenceId.class);
		if (sequenceId == null) {
			sequenceId = new SequenceId();
			sequenceId.setId(sequenceKey);
			sequenceId.setSeq(-1);
			mongoOperation.insert(sequenceId);
		}
	}

	public Long getNextSequenceId() {
		SequenceId sequenceId = mongoOperation.findById(sequenceKey, SequenceId.class);
		sequenceId.setSeq(sequenceId.getSeq() + 1);
		SequenceId ret = mongoOperation.save(sequenceId);
		return ret.getSeq();
	}

}