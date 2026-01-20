package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import java.time.LocalDateTime;

import java.util.List;

public interface IFuente {
    TipoFuente get();
    Iterable<List<Hecho>> importarHechosPaginado(LocalDateTime fecha);
}
