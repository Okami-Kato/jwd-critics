package com.epam.jwd_critics.model.service;

import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.model.entity.Celebrity;

import java.util.List;
import java.util.Optional;

public interface CelebrityService {
    List<Celebrity> getAllBetween(int begin, int end) throws ServiceException;

    int getCount() throws ServiceException;

    Optional<Celebrity> getEntityById(Integer id) throws ServiceException;

    void update(Celebrity celebrity) throws ServiceException;

    Celebrity create(Celebrity celebrity) throws ServiceException;

    void delete(Integer id) throws ServiceException;
}
