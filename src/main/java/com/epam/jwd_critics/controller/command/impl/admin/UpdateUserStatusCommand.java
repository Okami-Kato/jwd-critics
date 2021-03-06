package com.epam.jwd_critics.controller.command.impl.admin;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Destination;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.impl.common.OpenUserProfilePageCommand;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.InfoMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.UserServiceImpl;

import java.util.Optional;

public class UpdateUserStatusCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
        String userToUpdateId = req.getParameter(Parameter.USER_ID);
        String newStatusStr = req.getParameter(Parameter.NEW_STATUS);
        if (newStatusStr == null || userToUpdateId == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        }
        try {
            Optional<User> userToUpdate = userService.getEntityById(Integer.parseInt(userToUpdateId));
            if (userToUpdate.isPresent()) {
                if (userToUpdate.get().getStatus().equals(Status.INACTIVE)) {
                    req.setSessionAttribute(Attribute.INFO_MESSAGE, InfoMessage.INACTIVE_USER);
                    return resp;
                }
                Status newStatus = Status.valueOf(newStatusStr.toUpperCase());
                userToUpdate.get().setStatus(newStatus);
                userService.update(userToUpdate.get());
                String message;
                if (newStatus.equals(Status.BANNED)) {
                    message = SuccessMessage.USER_BANNED;
                } else {
                    message = SuccessMessage.USER_UNBANNED;
                }
                req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, message);
                Destination prevPage = resp.getDestination();
                if (prevPage != ServletDestination.MAIN) {
                    if (prevPage.getPath().equals(ServletDestination.USER_PROFILE.getPath())) {
                        new OpenUserProfilePageCommand().execute(req);
                    } else if (prevPage.getPath().equals(ServletDestination.ALL_USERS.getPath())) {
                        new OpenAllUsersPageCommand().execute(req);
                    }
                }
            } else {
                req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.USER_DOES_NOT_EXIST);
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
        }

        return resp;
    }
}
