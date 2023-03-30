package com.knotslicer.server.ports.interactor;

import com.knotslicer.server.domain.Poll;
import java.util.Set;

public class FindEventTimesCommandInvokerImpl implements FindEventTimesCommandInvoker {
    private FindEventTimesCommand findEventTimesCommand;
    public FindEventTimesCommandInvokerImpl(FindEventTimesCommand findEventTimesCommand) {
        this.findEventTimesCommand = findEventTimesCommand;
    }
    @Override
    public Set<Poll> executeCommand() {
        return findEventTimesCommand.execute();
    }
}
