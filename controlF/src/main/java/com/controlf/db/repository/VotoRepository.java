package com.controlf.db.repository;

import com.controlf.db.schema.Voto;
import com.controlf.db.schema.enums.TipoVoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Integer> {
    List<Voto> findByLeyId(Integer leyId);
    List<Voto> findByPoliticoId(Integer politicoId);

    long countByLeyIdAndTipoVoto(Integer leyId, TipoVoto tipoVoto);
    long countByTipoVoto(TipoVoto tipoVoto);
}
