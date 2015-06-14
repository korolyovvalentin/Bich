package org.fuc.signup;

import org.fuc.entities.RoleProvider;
import org.fuc.entities.Account;
import org.hibernate.validator.constraints.NotBlank;


public class SignupForm {

	private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

	@NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
	private String email;

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
	private String password;

	private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Account createAccount() {
		return new Account(getEmail(), getPassword(), RoleProvider.getRole(role));
	}


}
