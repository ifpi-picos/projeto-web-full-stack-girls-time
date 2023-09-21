package com.teamg.BookBee.repositorios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.teamg.BookBee.model.Leitor;

public interface LeitorRepositorio extends CrudRepository<Leitor, Long>{

    public Leitor findByNomeUsuario(String nomeUsuario);

    @Query(value = "select * from leitores where email = :email and senha = :senha", nativeQuery = true)
    public Leitor login(@Param("email") String email, @Param("senha") String senha);

    public Leitor findByEmail(String string);
    
}
