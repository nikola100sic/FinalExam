package test.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import test.enumeration.UserRole;
import test.model.User;
import test.repository.UserRepository;
import test.service.UserService;
import test.web.dto.UserChangePasswordDTO;

@Service
public class JpaUserService implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Optional<User> findOne(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public Page<User> findAll(int pageNo) {
		return userRepository.findAll(PageRequest.of(pageNo, 10));
	}

	@Override
	public User save(User user) {
		user.setRole(UserRole.USER);
		return userRepository.save(user);
	}

	@Override
	public User delete(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			userRepository.deleteById(id);
			return user.get();
		}
		return null;
	}

	@Override
	public boolean changePassword(Long id, UserChangePasswordDTO userChangePasswordDto) {
		Optional<User> result = userRepository.findById(id);

		if (!result.isPresent()) {
			throw new EntityNotFoundException();
		}

		User user = result.get();

		boolean passwordsMatch = BCrypt.checkpw(userChangePasswordDto.getOldPassword(), user.getPassword());
		if (!user.getSurname().equals(userChangePasswordDto.getUsername()) || !passwordsMatch) {
			return false;
		}

		String password = userChangePasswordDto.getPassword();
		if (!userChangePasswordDto.getPassword().equals("")) {
			password = passwordEncoder.encode(userChangePasswordDto.getPassword());
		}

		user.setPassword(password);

		userRepository.save(user);

		return true;
	}

	@Override
	public Optional<User> findbyUsername(String username) {
		return userRepository.findFirstByUsername(username);
	}
}
