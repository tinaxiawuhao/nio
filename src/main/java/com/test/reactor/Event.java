package com.test.reactor;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
    private InputSource source;
    private EventType type;
}
