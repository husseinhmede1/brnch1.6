package com.mdsl.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdsl.model.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	@Getter
    private int id;
	private String username;
	@JsonIgnore
	private String password;
	private int instId;
	private int branchId;
	private char userLevel;
	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(int id, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public UserDetailsImpl(int id, String username, String password, char userLevel,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.userLevel = userLevel;
		this.authorities = authorities;
	}
	
	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getUserRoles().stream()
				.map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName())).collect(Collectors.toList());
		return new UserDetailsImpl(user.getUserId(), user.getUsername(), user.getPassword(), authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

    @Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public int getInstId() {
		return instId;
	}

	public int getBranchId() {
		return branchId;
	}
	
	public char getUserLevel() {
		return userLevel;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
	
}