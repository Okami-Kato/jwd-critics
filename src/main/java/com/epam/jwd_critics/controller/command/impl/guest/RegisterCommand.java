package com.epam.jwd_critics.controller.command.impl.guest;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.controller.validation.ConstraintViolation;
import com.epam.jwd_critics.controller.validation.UserValidator;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.model.entity.User;
import com.epam.jwd_critics.model.service.UserService;
import com.epam.jwd_critics.model.service.impl.UserServiceImpl;

import javax.servlet.http.HttpSession;
import java.util.Set;
import java.util.stream.Collectors;

public class RegisterCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) {
        CommandResponse response = new CommandResponse(ServletDestination.SIGN_IN_PAGE, TransferType.REDIRECT);
        String firstName = req.getParameter(Parameter.FIRST_NAME);
        String lastName = req.getParameter(Parameter.LAST_NAME);
        String email = req.getParameter(Parameter.EMAIL);
        String login = req.getParameter(Parameter.LOGIN);
        String password = req.getParameter(Parameter.PASSWORD);
        if (firstName == null ||
                lastName == null ||
                email == null ||
                login == null ||
                password == null) {
            req.getSession(true).setAttribute(Attribute.VALIDATION_ERRORS.getName(), "Registration fields can't be empty");
        } else {
            UserValidator userValidator = new UserValidator();
            Set<ConstraintViolation> violations = userValidator.validateRegistrationData(firstName, lastName, email, login, password);
            if (violations.isEmpty()) {
                try {
                    User user = userService.register(firstName, lastName, email, login, password.toCharArray());
                    HttpSession reqSession = req.getSession(true);
                    reqSession.setAttribute(Attribute.USER_ID.getName(), user.getId());
                    reqSession.setAttribute(Attribute.USER_ROLE.getName(), user.getRole());
                    response.setDestination(ServletDestination.MAIN_PAGE);
                    response.setTransferType(TransferType.REDIRECT);
                } catch (ServiceException e) {
                    req.getSession(true).setAttribute(Attribute.SERVICE_ERROR.getName(), e.getMessage());
                    return response;
                }
            } else {
                req.getSession(true).setAttribute(Attribute.VALIDATION_ERRORS.getName(), violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
                return response;
            }
        }
        return response;
    }
}