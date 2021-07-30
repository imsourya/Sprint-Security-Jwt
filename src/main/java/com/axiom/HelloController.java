package com.axiom;

import javax.xml.ws.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.axiom.models.AuthenticationRequest;
import com.axiom.models.AuthenticationResponse;
import com.axiom.service.MyUserDetailsService;
import com.axiom.util.JwtUtil;

@RestController
public class HelloController {
	
	@Autowired
	private AuthenticationManager authenticationManager; 
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@RequestMapping("/hello")
	public String hello() {
		return "Hello World";
	}
	
	@RequestMapping(value="/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authReq) throws Exception{
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUserName(), authReq.getPassword()));
		} catch(BadCredentialsException e) {
			throw new Exception("Incorrect Username Password",e);
		}
		
		final UserDetails userDetail = userDetailsService
				.loadUserByUsername(authReq.getUserName());
		final String jwt = jwtUtil.generateToken(userDetail);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
