package test.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import test.model.User;
import test.web.dto.UserDTO;

@Component
public class UserToUserDTO implements Converter<User, UserDTO> {

	@Override
	public UserDTO convert(User user) {
		UserDTO userDTO = new UserDTO();

		userDTO.setId(user.getId());
		userDTO.seteMail(user.geteMail());
		userDTO.setName(user.getName());
		userDTO.setSurname(user.getSurname());
		userDTO.setUsername(user.getUsername());

		return userDTO;
	}

	public List<UserDTO> convert(List<User> users) {
		List<UserDTO> usersDTO = new ArrayList<>();

		for (User user : users) {
			UserDTO dto = convert(user);
			usersDTO.add(dto);
		}

		return usersDTO;
	}
}
