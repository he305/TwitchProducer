package com.github.he305.twitchproducer.application.repositories;

import com.github.he305.twitchproducer.common.entities.StreamData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamDataRepository extends JpaRepository<StreamData, Long> {
}
