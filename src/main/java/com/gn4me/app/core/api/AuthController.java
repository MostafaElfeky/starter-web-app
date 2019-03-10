package com.gn4me.app.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gn4me.app.core.service.UserService;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.User;
import com.gn4me.app.entities.enums.Security;
import com.gn4me.app.entities.response.AppResponse;
import com.gn4me.app.entities.response.GeneralResponse;
import com.gn4me.app.log.Loggable;
import com.gn4me.app.log.Type;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/auth")
@Loggable(Type = Type.CONTROLLER)
public class AuthController {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "Sign in User Based on email and password")
	@PostMapping("/signin")
	public ResponseEntity<AppResponse<User>> signin(@RequestParam String username, @RequestParam String password, 
			Transition transition) throws Exception {
		
		AppResponse<User> response = userService.signin(username, password, transition);
		
		return new ResponseEntity<AppResponse<User>>(response, response.getHttpStatus());
	}

	@PostMapping("/signup")
	public GeneralResponse signup(@RequestBody User user, 
			Transition transition) throws Exception {
		
		GeneralResponse response = null;
		
		response  =  userService.signup(user, transition);
		
		return response;
	}

	@PostMapping("/refresh-key")
	public GeneralResponse refreshKey(
			@RequestHeader(value = "Authorization") String bearerToken,
			Transition transition) throws Exception {

		String token = "";

		if (bearerToken != null && bearerToken.startsWith(Security.BEARER_PREFIX.getValue())) {
			token = bearerToken.substring(7, bearerToken.length());
		}

		return userService.refreshKey(token, transition);
	}
	
	@ApiOperation(value = "Forget password send email to get an email with reset process")
	@PostMapping("/forget-password")
	public GeneralResponse forgetPassword(
			@RequestParam String email, Transition transition) throws Exception {
		
		return userService.forgetPassword(email, transition);
		
	}
	
	@ApiOperation(value = "Forget password send email to get an email with reset process")
	@PostMapping("/reset-password")
	public GeneralResponse resetPassword(
			@RequestParam String token,
			@RequestParam String password,
			Transition transition) throws Exception {
		
		return userService.resetPassword(token, password, transition);
	}

}
