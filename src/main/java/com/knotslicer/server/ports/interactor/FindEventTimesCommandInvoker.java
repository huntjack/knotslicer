package com.knotslicer.server.ports.interactor;

import com.knotslicer.server.domain.Poll;
import java.util.Set;

public interface FindEventTimesCommandInvoker {
    Set<Poll> executeCommand();
}
