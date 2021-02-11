package com.la.repository;

import com.la.model.Genre;
import com.la.model.users.Reader;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface ReaderRepository extends UserRepository<Reader> {
    Reader findByUsername(String username);

    List<Reader> findByBetaIsTrueAndBetaReaderGenresContains(Genre genre);

}
