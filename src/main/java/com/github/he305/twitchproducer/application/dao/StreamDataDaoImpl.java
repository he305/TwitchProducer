package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.application.repositories.StreamDataRepository;
import com.github.he305.twitchproducer.common.dao.StreamDataDao;
import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.exception.EntitySaveFailedException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StreamDataDaoImpl implements StreamDataDao {
    private final StreamDataRepository streamDataRepository;

    @Override
    public Optional<StreamData> get(@NonNull Long id) {
        return streamDataRepository.findById(id);
    }

    @Override
    public List<StreamData> getAll() {
        return streamDataRepository.findAll();
    }

    @Override
    public StreamData save(@NonNull StreamData streamData) throws EntitySaveFailedException {
        try {
            return streamDataRepository.save(streamData);
        } catch (RuntimeException e) {
            throw new EntitySaveFailedException(e.getMessage());
        }
    }

    @Override
    public void delete(@NonNull StreamData streamData) {
        streamDataRepository.delete(streamData);
    }

    @Override
    public List<StreamData> getStreamDataForStream(Long streamId) {
        return streamDataRepository.findAll().stream().filter(s -> s.getStream().getId().equals(streamId)).collect(Collectors.toList());
    }
}
