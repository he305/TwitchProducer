package com.github.he305.twitchproducer.application.repositories;

import com.github.he305.twitchproducer.common.entities.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamRepository extends JpaRepository<Stream, Long> {
}
