package org.looko.mycloud.commonstarter.dto;

import java.io.Serializable;

public record UserAuthDTO(String userid, String username, String password) implements Serializable {}
