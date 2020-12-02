package com.ryszka.imageRestApi.repository;

import com.ryszka.imageRestApi.persistenceEntities.SessionEntity;
import com.ryszka.imageRestApi.security.DBQueryStatements;
import com.ryszka.imageRestApi.util.mapper.dbMappers.PrincipalBySessionQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface SessionDbRepository extends CrudRepository<SessionEntity, String> {
    PrincipalBySessionQuery getSessionEntityBySessionId(String id);
    @Async
    @Query(value = DBQueryStatements.GET_PRINCIPAL_FROM_SESSION_ID, nativeQuery = true)
    CompletableFuture<PrincipalBySessionQuery> someQuery(@Param("sessionId") String sessionId);

    SessionEntity getBySessionId(String sessionId);
    
}
