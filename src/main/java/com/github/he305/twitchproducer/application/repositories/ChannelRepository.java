package com.github.he305.twitchproducer.application.repositories;

import com.github.he305.twitchproducer.common.entities.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByNickname(String nickname);
}
