package com.epam.jwd_critics.controller.command;

import com.epam.jwd_critics.controller.command.impl.ChangeLocaleCommand;
import com.epam.jwd_critics.controller.command.impl.SignInCommand;
import com.epam.jwd_critics.controller.command.impl.RegisterCommand;
import com.epam.jwd_critics.controller.command.impl.OpenSignInPageCommand;
import com.epam.jwd_critics.controller.command.impl.OpenMainPageCommand;
import com.epam.jwd_critics.controller.command.impl.SignOutCommand;
import com.epam.jwd_critics.model.entity.Role;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum CommandInstance {
    OPEN_MAIN(new OpenMainPageCommand(), Role.values()),
    OPEN_SIGN_IN(new OpenSignInPageCommand(), Role.GUEST),
    CHANGE_LANGUAGE(new ChangeLocaleCommand(), Role.values()),
    SIGN_IN(new SignInCommand(), Role.GUEST),
    REGISTER(new RegisterCommand(), Role.GUEST),
    SIGN_OUT(new SignOutCommand(), Role.ADMIN, Role.USER);
    private final Command command;
    private final List<Role> allowedRoles = new LinkedList<>();

    CommandInstance(Command command, Role ...roles) {
        this.command = command;
        this.allowedRoles.addAll(Arrays.asList(roles));
    }

    public boolean isRoleAllowed(Role role){
        return allowedRoles.contains(role);
    }

    public static Command commandOf(String commandName) {
        for (CommandInstance v : values()) {
            if (v.name().equalsIgnoreCase(commandName)) {
                return v.command;
            }
        }
        return OPEN_MAIN.command;
    }
}
