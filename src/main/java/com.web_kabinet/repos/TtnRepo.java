package com.web_kabinet.repos;

import com.web_kabinet.ttn.Ttn;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface TtnRepo extends CrudRepository<Ttn, String> {
    @Query(value = "select max(num) from ttn", nativeQuery = true)
    Long findMaxNum();

//    List<Ttn> findAllByContragentId(String contragent_id);

}
