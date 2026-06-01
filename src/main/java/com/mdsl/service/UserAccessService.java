package com.mdsl.service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdsl.model.entity.UserAccess;
import com.mdsl.repository.UserAccessRepository;
import com.mdsl.repository.UserRepository;

@Service
public class UserAccessService {
	private UserAccessRepository userAccessRepository;
	@Autowired
	private UserRepository userRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public UserAccessService(UserAccessRepository userAccessRepository) {
		super();
		this.userAccessRepository = userAccessRepository;
	}

	public List<UserAccess> listAll(){
		return userAccessRepository.findAll();
	}
	
	public void saveUserAccess(UserAccess userAccess) {
		if(userAccessRepository.existsByUser(userAccess.getUser())) {
			logger.debug("User already logged in, generate new Token");
			userAccess.setUserAccessId(userAccessRepository.getByUser(userAccess.getUser()).getUserAccessId());
		}
		
		userAccessRepository.save(userAccess);
	}
	
	public void updateToken(Integer userId, String jwtToken) {
		if(Objects.nonNull(userId) && Objects.nonNull(jwtToken)) {
			logger.debug("User already logged in, generated new Token from refresh token");
			UserAccess ua = userAccessRepository.getByUser(userRepository.findById(userId).get());
			ua.setJwt(jwtToken);
			userAccessRepository.save(ua);
		}
	}
	
	public UserAccess get(int id) {
		return userAccessRepository.findById(id).get();
	}
	
	public void delete(int id) {
		userAccessRepository.deleteById(id);
	}
}