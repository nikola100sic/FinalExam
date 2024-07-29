package test.web.dto;

import javax.validation.constraints.NotBlank;

public class UserChangePasswordDTO {

	@NotBlank(message = "Username not entered")
	private String username;

	@NotBlank(message = "Old password not entered")
	private String oldPassword;

	@NotBlank(message = "Password not entered")
	private String password;

	@NotBlank(message = "Repeat password not entered")
	private String repeatPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

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
