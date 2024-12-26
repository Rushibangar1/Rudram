package com.app.entity;

import java.util.Collection;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

/**
 * Entity class representing user information in the database.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;


    @Column(unique = true)
    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;


    @NotBlank(message = "Role cannot be blank")
    private String role;

	@Transient
    @NotNull(message = "OTP cannot be null")
    @Pattern(regexp = "\\d{6}", message = "OTP must be exactly 6 digits")
    private String userOtp;


	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
}
