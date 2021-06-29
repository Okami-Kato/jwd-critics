package com.epam.jwd_critics.controller.command.impl.user;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.controller.command.impl.common.OpenUserProfilePage;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.message.SuccessMessage;
import com.epam.jwd_critics.service.UserService;
import com.epam.jwd_critics.service.impl.UserServiceImpl;
import com.epam.jwd_critics.validation.ConstraintViolation;
import com.epam.jwd_critics.validation.UserValidator;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UpdateUser implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        new UploadPicture().execute(req);
        String newPicture = (String) req.getAttribute(Attribute.NEW_IMAGE);
        String firstName = req.getParameter(Parameter.FIRST_NAME);
        String lastName = req.getParameter(Parameter.LAST_NAME);
        String userIdStr = req.getParameter(Parameter.USER_ID);
        if (firstName == null || lastName == null) {
            req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, ErrorMessage.EMPTY_FIELDS);
        } else if (userIdStr == null) {
            throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
        } else {
            UserValidator userValidator = new UserValidator();
            List<ConstraintViolation> violations = new LinkedList<>();
            userValidator.validateFirstName(firstName).ifPresent(violations::add);
            userValidator.validateLastName(lastName).ifPresent(violations::add);

            if (violations.isEmpty()) {
                try {
                    Optional<User> userToUpdate = userService.getEntityById(Integer.parseInt(userIdStr));
                    if (userToUpdate.isPresent()) {
                        userToUpdate.get().setFirstName(firstName);
                        userToUpdate.get().setLastName(lastName);
                        if (newPicture != null && !newPicture.equals("")) {
                            userToUpdate.get().setImagePath(newPicture);
                        }
                        userService.update(userToUpdate.get());
                        req.setSessionAttribute(Attribute.USER, new UserDTO(userToUpdate.get()));
                        req.setSessionAttribute(Attribute.SUCCESS_NOTIFICATION, SuccessMessage.USER_UPDATED);
                    } else {
                        req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, ErrorMessage.USER_DOES_NOT_EXIST);
                    }
                } catch (ServiceException e) {
                    req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
                }
            } else {
                req.setSessionAttribute(Attribute.VALIDATION_WARNINGS, violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
            }
        }
        new OpenUserProfilePage().execute(req);
        return new CommandResponse(ServletDestination.USER_PROFILE, TransferType.REDIRECT);
    }
}