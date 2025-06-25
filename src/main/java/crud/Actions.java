package crud;

import interfaces.Command;

public class Actions<T> {
    public void executeCommand(Command<T> command, T element) {
        command.execute(element);
    }
}

