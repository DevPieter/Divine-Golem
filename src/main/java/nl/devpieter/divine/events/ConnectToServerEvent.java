package nl.devpieter.divine.events;

import nl.devpieter.sees.event.SEvent;

public record ConnectToServerEvent(String ip) implements SEvent {
}
