package com.knotslicer.server.ports.interactor;

public class InteractorCommandInvokerImpl implements InteractorCommandInvoker {
    private InteractorCommand interactorCommand;
    public InteractorCommandInvokerImpl(InteractorCommand interactorCommand) {
        this.interactorCommand = interactorCommand;
    }
    @Override
    public Boolean executeCommand() {
        return interactorCommand.execute();
    }
}
