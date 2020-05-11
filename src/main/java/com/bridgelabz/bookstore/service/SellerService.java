package com.bridgelabz.bookstore.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.bookstore.dto.BookDto;
import com.bridgelabz.bookstore.dto.SellerLoginDto;
import com.bridgelabz.bookstore.dto.SellerPasswordUpdateDto;
import com.bridgelabz.bookstore.dto.SellerDto;
import com.bridgelabz.bookstore.entity.Seller;
import com.bridgelabz.bookstore.exception.SellerException;

public interface SellerService {
	
public Seller register(SellerDto dto);
public Seller login(SellerLoginDto login);
public Boolean verify(String token);
public List<Seller> getSellers();
 public Boolean bookVerify(String token,Long bookId);
 public Boolean updatePassword(SellerPasswordUpdateDto update, String token);
 //public SellerEntity forgetPassword(String Email);
boolean addBookBySeller(String token, BookDto dto, MultipartFile multipartFile) throws SellerException;

}
