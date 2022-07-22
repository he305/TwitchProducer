package com.github.he305.twitchproducer.common.dao;

import com.github.he305.twitchproducer.common.entities.StreamData;

import java.util.List;

public interface StreamDataDao extends Dao<StreamData, Long> {
    List<StreamData> getStreamDataForStream(Long streamId);
}
