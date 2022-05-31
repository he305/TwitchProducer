package com.github.he305.TwitchProducer.application.repositories;

import com.github.he305.TwitchProducer.common.entities.Streamer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreamerRepository extends JpaRepository<Streamer, Long> {
    List<Streamer> findByNickname(String nickname);
}
