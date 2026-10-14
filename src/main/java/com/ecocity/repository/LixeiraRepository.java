package com.ecocity.repository;

import com.ecocity.model.LixeiraInteligente;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LixeiraRepository extends MongoRepository<LixeiraInteligente, String> {
    List<LixeiraInteligente> findByOcupacaoAtualPctGreaterThanEqual(Integer pct);
}
