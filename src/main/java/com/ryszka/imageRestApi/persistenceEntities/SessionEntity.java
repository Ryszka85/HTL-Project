package com.ryszka.imageRestApi.persistenceEntities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Entity(name = "spring_session")
public class SessionEntity implements Serializable {
    private static final long serialVersionUID = -7705888567615469857L;
    @Id
    @Column(name = "PRIMARY_ID")
    private String id;
    @Column(name = "SESSION_ID")
    private String sessionId;
    @Column(name = "CREATION_TIME")
    private BigInteger creationTime;
    @Column(name = "LAST_ACCESS_TIME")
    private BigInteger lastAccessTime;
    @Column(name = "MAX_INACTIVE_TIME")
    private Long maxInactiveTime;
    @Column(name = "EXPIRY_TIME")
    private BigInteger expiryTime;
    @Column(name = "PRINCIPAL_NAME")
    private String principal;

    public SessionEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public BigInteger getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(BigInteger creationTime) {
        this.creationTime = creationTime;
    }

    public BigInteger getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(BigInteger lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public long getMaxInactiveTime() {
        return maxInactiveTime;
    }

    public void setMaxInactiveTime(long maxInactiveTime) {
        this.maxInactiveTime = maxInactiveTime;
    }

    public BigInteger getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(BigInteger expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
