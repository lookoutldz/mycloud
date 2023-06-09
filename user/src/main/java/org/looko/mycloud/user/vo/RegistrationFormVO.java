package org.looko.mycloud.user.vo;

import java.io.Serializable;

public record RegistrationFormVO(String username, String password, String validcode, String email) implements Serializable {}
