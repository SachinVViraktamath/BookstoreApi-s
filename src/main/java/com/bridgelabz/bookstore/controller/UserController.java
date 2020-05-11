package com.bridgelabz.bookstore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.bookstore.dto.UserPasswordDto;
import com.bridgelabz.bookstore.dto.UserRegisterDto;
import com.bridgelabz.bookstore.dto.UserAddressDto;
import com.bridgelabz.bookstore.dto.UserLoginDto;
import com.bridgelabz.bookstore.entity.UserAddress;
import com.bridgelabz.bookstore.entity.Users;
import com.bridgelabz.bookstore.exception.UserException;
import com.bridgelabz.bookstore.response.Response;
import com.bridgelabz.bookstore.service.UserAddressService;
import com.bridgelabz.bookstore.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private UserAddressService serviceAdd;
	
	@PostMapping("/register")
	public ResponseEntity<Response> registeration(@RequestBody UserRegisterDto userInfoDto) throws UserException{
		Users user =service.userRegistration(userInfoDto);
		
		if(user!=null) {
	
			return ResponseEntity.badRequest().body(new Response(HttpStatus.NOT_ACCEPTABLE, "Registered Successfully",userInfoDto));
		}
		
		else {
			return ResponseEntity.badRequest().body(new Response(HttpStatus.NOT_ACCEPTABLE, "User Already Exist",userInfoDto));
		}
				
	}
	
	
	@GetMapping("/verify/{token}")
	public ResponseEntity<Response> verification(@PathVariable("token") String token) throws UserException{
			Users user=service.userVerification(token);
		
			if (user!=null) {
				return ResponseEntity.badRequest().body(new Response(HttpStatus.ACCEPTED,"Successfully verified", 200));
			}
			return ResponseEntity.badRequest().body(new Response(HttpStatus.NOT_ACCEPTABLE,"user already verified..", 400));
		}
	

	@PostMapping("/login/")
	public ResponseEntity<Response> login(@RequestBody UserLoginDto login) throws UserException {
		Users user = service.userLogin(login);
		
		if(user!=null) {
			return ResponseEntity.badRequest().body(new Response(HttpStatus.ACCEPTED,"Login Successfull",200));
		}
		return ResponseEntity.badRequest().body(new Response(HttpStatus.NOT_ACCEPTABLE,"something went wrong..", 400));

	}
	
	@PostMapping("/forgetPassword")
	public ResponseEntity<Response> forgetPassword(@RequestBody String email ) throws UserException {
		Users user = service.forgetPassword(email);
		
		if(user!=null) {
			return ResponseEntity.badRequest().body(new Response(HttpStatus.ACCEPTED," Success Login",200));
		}
		return ResponseEntity.badRequest().body(new Response(HttpStatus.NOT_ACCEPTABLE,"user does not exists .", 400));

	}
	
	@ApiOperation(value = "Api to Update User Password for BookStore", response = Response.class)
	@PutMapping("/updatePassword/{token}")
	public ResponseEntity<Response> updatePassword(@RequestBody UserPasswordDto password,@Valid @PathVariable("token") String token) throws UserException {
		Users user = service.userVerification(token);

		if (user!=null) {
			return ResponseEntity.badRequest().body(new Response(HttpStatus.ACCEPTED," Successfully Updated ",200));
		} else {
			return ResponseEntity.badRequest().body(new Response(HttpStatus.NOT_ACCEPTABLE,"something went wrong..", 400));
		}
	}
	
	
	@PostMapping("/addAddress/create")
	public ResponseEntity<Response> addAddress(@RequestBody UserAddressDto addDto, @RequestHeader String token){
		
		UserAddress add=serviceAdd.addAddress(addDto, token);
		
		if(add!=null) {
			return ResponseEntity.badRequest().body(new Response(HttpStatus.ACCEPTED," User Address added Successfully  ",200));
		}
		
		else {
			return ResponseEntity.badRequest().body(new Response(HttpStatus.NOT_ACCEPTABLE,"something went wrong..", 400));
		}
	}
	
	@PutMapping("/updateAddress/{addressId}")
	public ResponseEntity<Response> updateAddress(String token,@PathVariable long addressId, @RequestBody UserAddressDto addDto ){
		UserAddress add=serviceAdd.updateAddress(token, addDto, addressId);
		
		if(add!=null) {
			
			return ResponseEntity.badRequest().body(new Response(HttpStatus.ACCEPTED," User Address Updated Successfully  ",200));

		}
		else {
			return ResponseEntity.badRequest().body(new Response(HttpStatus.NOT_ACCEPTABLE,"something went wrong..", 400));
		}
		
	}
}
