package com.github.he305.twitchproducer.common.dao;

import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Stream;

import java.util.List;
import java.util.Optional;

public interface StreamDao extends Dao<Stream, Long> {
    List<Stream> getCurrentStreams();

    Optional<Stream> getCurrentStreamForChannel(Channel channel);
}
