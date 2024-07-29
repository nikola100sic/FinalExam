package test.web.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import test.model.User;
import test.security.TokenUtils;
import test.service.UserService;
import test.support.UserDTOToUser;
import test.support.UserToUserDTO;
import test.web.dto.AuthUserDto;
import test.web.dto.UserChangePasswordDTO;
import test.web.dto.UserDTO;
import test.web.dto.UserRegistrationDTO;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserDTOToUser toUser;

	@Autowired
	private UserToUserDTO toUserDTO;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PreAuthorize("permitAll()")
	@PostMapping
	public ResponseEntity<UserDTO> create(@RequestBody @Validated UserRegistrationDTO dto) {

		if (dto.getId() != null || !dto.getPassword().equals(dto.getRepeatPassword())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		User user = toUser.convert(dto);

		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		user.setPassword(encodedPassword);

		return new ResponseEntity<>(toUserDTO.convert(userService.save(user)), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		User deletedUser = userService.delete(id);

		if (deletedUser != null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO korisnikDTO) {

		if (!id.equals(korisnikDTO.getId())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		User user = toUser.convert(korisnikDTO);

		return new ResponseEntity<>(toUserDTO.convert(userService.save(user)), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> get(@PathVariable Long id) {
		Optional<User> user = userService.findOne(id);

		if (user.isPresent()) {
			return new ResponseEntity<>(toUserDTO.convert(user.get()), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping
	public ResponseEntity<List<UserDTO>> get(@RequestParam(defaultValue = "0") int page) {
		Page<User> users = userService.findAll(page);
		return new ResponseEntity<>(toUserDTO.convert(users.getContent()), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_KORISNIK')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, params = "changePassword")
	public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody UserChangePasswordDTO dto) {

		if (!dto.getPassword().equals(dto.getRepeatPassword())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		boolean result;
		try {
			result = userService.changePassword(id, dto);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (result) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@PreAuthorize("permitAll()")
	@RequestMapping(path = "/auth", method = RequestMethod.POST)
	public ResponseEntity authenticateUser(@RequestBody AuthUserDto dto) {
		// Perform the authentication
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				dto.getUsername(), dto.getPassword());
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		try {
			// Reload user details so we can generate token
			UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());
			return ResponseEntity.ok(tokenUtils.generateToken(userDetails));
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
