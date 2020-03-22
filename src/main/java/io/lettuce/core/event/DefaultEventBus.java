/*
 * Copyright 2011-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.event;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Scheduler;

/**
 * Default implementation for an {@link EventBus}. Events are published using a {@link Scheduler}.
 *
 * @author Mark Paluch
 * @since 3.4
 */
public class DefaultEventBus implements EventBus {

    public static final long DEFAULT_EMIT_INTERVAL = 10;
    private final DirectProcessor<Event> bus;
    private final FluxSink<Event> sink;
    private final Scheduler scheduler;

    public DefaultEventBus(Scheduler scheduler) {
        this.bus = DirectProcessor.create();
        this.sink = bus.sink();
        this.scheduler = scheduler;
    }

    /**
     * Create a disabled {@link DefaultEventPublisherOptions} using default settings.
     *
     * @return a new instance of a default {@link DefaultEventPublisherOptions} instance with disabled event emission
     */
    public static DefaultEventPublisherOptions disabled() {
        return DefaultEventPublisherOptions.DISABLED;
    }

    @Override
    public Flux<Event> get() {
        return bus.onBackpressureDrop().publishOn(scheduler);
    }

    @Override
    public void publish(Event event) {
        sink.next(event);
    }
}
