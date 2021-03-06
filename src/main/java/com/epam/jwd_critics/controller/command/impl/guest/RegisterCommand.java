package com.epam.jwd_critics.controller.command.impl.guest;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.InfoMessage;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.UserServiceImpl;
import com.epam.jwd_critics.validation.ConstraintViolation;
import com.epam.jwd_critics.validation.UserValidator;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class RegisterCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) {
        CommandResponse resp = new CommandResponse(ServletDestination.SIGN_IN, TransferType.REDIRECT);
        String firstName = req.getParameter(Parameter.FIRST_NAME);
        String lastName = req.getParameter(Parameter.LAST_NAME);
        String email = req.getParameter(Parameter.EMAIL);
        String login = req.getParameter(Parameter.LOGIN);
        String password = req.getParameter(Parameter.PASSWORD);
        if (firstName == null || lastName == null || email == null || login == null || password == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, Collections.singletonList(ErrorMessage.EMPTY_FIELDS));
        } else {
            UserValidator userValidator = new UserValidator();
            Set<ConstraintViolation> violations = userValidator.validateRegistrationData(firstName, lastName, email, login, password);
            if (violations.isEmpty()) {
                try {
                    User user = userService.register(firstName, lastName, email, login, password.toCharArray());
                    req.setSessionAttribute(Attribute.USER, new UserDTO(user));
                    String key = UUID.randomUUID().toString();
                    userService.createActivationKey(user.getId(), key);
                    String lang = (String) req.getSessionAttribute(Attribute.LANG);
                    userService.buildAndSendActivationMail(user, key, lang);
                    req.setSessionAttribute(Attribute.INFO_MESSAGE, InfoMessage.ACTIVATION_MAIL);
                    resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
                    req.removeSessionAttribute(Attribute.PREVIOUS_PAGE);
                } catch (ServiceException e) {
                    req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
                }
            } else {
                req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
            }
        }
        return resp;
    }
}
