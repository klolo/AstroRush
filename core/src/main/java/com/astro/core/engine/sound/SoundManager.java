package com.astro.core.engine.sound;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum SoundManager {

    instance;


    public void playSound(final AvailableSound sound) {
        log.info("Play sound");
    }


}
