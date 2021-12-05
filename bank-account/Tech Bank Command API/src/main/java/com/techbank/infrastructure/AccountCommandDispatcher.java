package com.techbank.infrastructure;

import com.example.core.commands.BaseCommand;
import com.example.core.commands.CommandHandlerMethod;
import com.example.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        List<CommandHandlerMethod> handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        List<CommandHandlerMethod> handlerMethods = routes.get(command.getClass());

        if(handlerMethods == null || handlerMethods.size() == 0) {
            throw new RuntimeException("No command handler was registered");
        }

        if(handlerMethods.size() > 1) {
            throw new RuntimeException("Cannot send command to more than one handler...");
        }

        handlerMethods.get(0).handle(command);
    }
}
