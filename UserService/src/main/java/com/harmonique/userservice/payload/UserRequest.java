//package com.harmonique.userservice.payload;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import lombok.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserRequest {
//	private String about;
//
//    @NotBlank(message = "Name is required !!")
//    private String name;
//
//    @Email(message = "Invalid email format !!")
//    @NotBlank(message = "Email is required !!")
//    private String email;
//
//    @NotBlank(message = "Password is required !!")
//    private String password;
//}
package com.harmonique.userservice.payload;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

	private String profilePictureUrl;

	@NotBlank(message = "First name is required !!")
	private String firstName;

	private String lastName;

	@NotBlank(message = "Username is required !!")
	private String username;

	@Email(message = "Invalid email format !!")
	@NotBlank(message = "Email is required !!")
	private String email;

	@NotBlank(message = "Password is required !!")
	@Size(min = 6, message = "Password must be at least 6 characters !!")
	private String password;

	private String phoneNo;

	private String location;

    private String about;
    
    private MultipartFile file;
}