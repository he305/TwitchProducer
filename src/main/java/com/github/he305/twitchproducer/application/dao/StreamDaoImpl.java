package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.application.repositories.StreamRepository;
import com.github.he305.twitchproducer.common.dao.StreamDao;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.exception.EntitySaveFailedException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StreamDaoImpl implements StreamDao {
    private final StreamRepository streamRepository;

    @Override
    public List<Stream> getAll() {
        return streamRepository.findAll();
    }

    @Override
    public List<Stream> getCurrentStreams() {
        List<Stream> streams = streamRepository.findAll();

        return streams.stream()
                .filter(s -> s.getEndedAt() == null)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Stream> getCurrentStreamForChannel(Channel channel) {
        return getCurrentStreams().stream().filter(c -> c.getChannel().equals(channel)).findFirst();
    }

    @Override
    public Stream save(@NonNull Stream stream) {
        try {
            return streamRepository.save(stream);
        } catch (RuntimeException e) {
            throw new EntitySaveFailedException(e.getMessage());
        }
    }

    @Override
    public void delete(@NonNull Stream stream) {
        streamRepository.delete(stream);
    }

    @Override
    public Optional<Stream> get(@NonNull Long id) {
        return streamRepository.findById(id);
    }
}
