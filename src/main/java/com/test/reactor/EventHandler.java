package com.test.reactor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EventHandler {

    private InputSource source;
    public abstract void handle(Event event);
}