package com.teamg.BookBee.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.teamg.BookBee.model.ListaDeLeitura;

public interface ListaDeLeituraRepositorio extends CrudRepository<ListaDeLeitura, Long> {

    List<ListaDeLeitura> findAllByLeitorId(long id);

}
