package org.fuc.unit;

import org.fuc.entities.Account;
import org.fuc.repositories.AccountRepository;
import org.fuc.services.UserService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@InjectMocks
	private UserService userService = new UserService();

	@Mock
	private AccountRepository accountRepositoryMock;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldThrowExceptionWhenUserNotFound() {
		// arrange
		thrown.expect(UsernameNotFoundException.class);
		thrown.expectMessage("user not found");

		when(accountRepositoryMock.findByEmail("user@example.com")).thenReturn(null);
		// act
		userService.loadUserByUsername("user@example.com");
	}

	@Test
	public void shouldReturnUserDetails() {
		// arrange
		Account demoUser = new Account("user@example.com", "demo", "ROLE_USER");
		when(accountRepositoryMock.findByEmail("user@example.com")).thenReturn(demoUser);

		// act
		UserDetails userDetails = userService.loadUserByUsername("user@example.com");

		// assert
		assertThat(demoUser.getEmail()).isEqualTo(userDetails.getUsername());
		assertThat(demoUser.getPassword()).isEqualTo(userDetails.getPassword());
        assertThat(hasAuthority(userDetails, demoUser.getRole()));
	}

	private boolean hasAuthority(UserDetails userDetails, String role) {
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		for(GrantedAuthority authority : authorities) {
			if(authority.getAuthority().equals(role)) {
				return true;
			}
		}
		return false;
	}
}
