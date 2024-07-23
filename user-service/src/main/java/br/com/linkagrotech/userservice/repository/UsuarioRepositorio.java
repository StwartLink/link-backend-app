package br.com.linkagrotech.userservice.repository;

import br.com.linkagrotech.userservice.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,Long> {

    Usuario findByUsername(String username);

    boolean existsByUsername(String username);
}
