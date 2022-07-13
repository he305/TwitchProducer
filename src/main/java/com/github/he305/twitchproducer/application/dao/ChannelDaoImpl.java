package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.application.repositories.ChannelRepository;
import com.github.he305.twitchproducer.common.dao.ChannelDao;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.exception.EntitySaveFailedException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ChannelDaoImpl implements ChannelDao {
    @Autowired
    private final ChannelRepository channelRepository;

    @Override
    public List<Channel> getAll() {
        return channelRepository.findAll();
    }

    @Override
    public Optional<Channel> getChannelByName(@NonNull String nickname) {
        return channelRepository.findByNickname(nickname).stream().findFirst();
    }

    @Override
    public Optional<Channel> get(@NonNull Long id) {
        return channelRepository.findById(id);
    }


    @Override
    public Channel save(@NonNull Channel channel) {
        try {
            return channelRepository.save(channel);
        } catch (RuntimeException e) {
            throw new EntitySaveFailedException(e.getMessage());
        }
    }

    @Override
    public void delete(Channel channel) {
        channelRepository.delete(channel);
    }
}
