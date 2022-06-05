package com.github.he305.twitchproducer.application.schedulers;

import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.entities.Streamer;
import com.github.he305.twitchproducer.common.interfaces.StreamDataService;
import com.github.he305.twitchproducer.common.interfaces.StreamerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TwitchScheduleTask {
    @Autowired
    private StreamDataService streamDataService;
    @Autowired
    private StreamerService streamerService;

    @Scheduled(fixedDelayString = "${twitch-producer.scheduler.delay}")
    public void retrieveStreamData() {
        List<Streamer> streamers = streamerService.getAllStreamers();

        streamers.forEach(streamer -> {
            StreamData streamData = streamDataService.getStream(streamer.getNickname());
            System.out.println("stream " + streamData);
        });
    }
}
