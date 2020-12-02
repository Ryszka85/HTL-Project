package com.ryszka.imageRestApi.dao;

import com.ryszka.imageRestApi.persistenceEntities.SessionEntity;
import com.ryszka.imageRestApi.repository.SessionDbRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionDAO {
    private final SessionDbRepository sessionDbRepository;

    public SessionDAO(SessionDbRepository sessionDbRepository) {
        this.sessionDbRepository = sessionDbRepository;
    }

    public Optional<SessionEntity> findUserBySessionID(String sessionID) {
        return Optional.ofNullable(sessionDbRepository.getBySessionId(sessionID));
    }
}
