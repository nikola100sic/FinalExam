package test.web.dto;

import javax.validation.constraints.NotBlank;

public class UserRegistrationDTO extends UserDTO {

	@NotBlank(message = "Password not entered")
	private String password;

	@NotBlank(message = "Repeat password not entered")
	private String repeatPassword;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

}
