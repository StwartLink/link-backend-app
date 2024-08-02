package br.com.linkagrotech.visita_service.repository;

import br.com.linkagrotech.visita_service.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente,Long>, FooRepositorio {
}
