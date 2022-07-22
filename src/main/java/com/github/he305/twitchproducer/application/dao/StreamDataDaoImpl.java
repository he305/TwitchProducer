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

@Service
@RequiredArgsConstructor
public class StreamDataDaoImpl implements StreamDataDao {
    private final StreamDataRepository streamDataRepository;
//    private final StreamRepository streamRepository;
//    private final StreamDataAddMapper addMapper;
//    private final StreamDataResponseMapper responseMapper;

//    @Override
//    public StreamDataResponseDto addStreamData(Long streamId, StreamDataAddDto dto) {
//        Optional<Stream> stream = streamRepository.findById(streamId);
//        if (stream.isEmpty())
//            throw new EntityNotFoundException();
//
//        Stream targetStream = stream.get();
//        if (targetStream.getEndedAt() != null)
//            throw new StreamHasEndedException();
//
//        StreamData streamData = addMapper.toStreamData(dto);
//        streamData.setStream(targetStream);
//        StreamData saved = streamDataRepository.save(streamData);
//        return responseMapper.toDto(saved);
//    }
//
//    @Override
//    public List<StreamDataResponseDto> getStreamDataForStreamId(Long id) {
//        List<StreamData> streamDataList = streamDataRepository
//                .findAll()
//                .stream()
//                .filter(s -> s.getStream().getId().equals(id))
//                .collect(Collectors.toList());
//        return streamDataList.stream().map(responseMapper::toDto).collect(Collectors.toList());
//    }

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
        return streamDataRepository.save(streamData);
    }

    @Override
    public void delete(@NonNull StreamData streamData) {
        streamDataRepository.delete(streamData);
    }
}
