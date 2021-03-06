package com.bridgelabz.fundoonotes.implementation;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.configuration.RabbitmqSender;
import com.bridgelabz.fundoonotes.dto.LoginInformation;
import com.bridgelabz.fundoonotes.dto.PasswordUpdate;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.entity.NoteInformation;
import com.bridgelabz.fundoonotes.entity.UserInformation;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.response.MailObject;
import com.bridgelabz.fundoonotes.response.MailResponse;
import com.bridgelabz.fundoonotes.service.UserServices;
import com.bridgelabz.fundoonotes.utility.JwtGenerator;
import com.bridgelabz.fundoonotes.utility.MailServiceProvider;

/**
 * 
 * @author chaithra B N
 *
 */
/*
 * Implementation for the service declaration
 */
@Service
public class UserImplementation implements UserServices {
	private UserInformation userInformation = new UserInformation();

	@Autowired
	private UserRepository repository;
	@Autowired
	private JwtGenerator generate;
	@Autowired
	private BCryptPasswordEncoder encryption;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private MailResponse response;
	@Autowired
	private MailObject mailObject;
	@Autowired
	private NoteRepository noterepository;

	@Autowired
	private RabbitmqSender rabbitmq;

	/*
	 * Method for the Registration
	 */

	@Transactional
	@Override
	public boolean register(UserDto information) {
		UserInformation user = repository.getUser(information.getEmail());
		if (user == null) {
			userInformation = modelMapper.map(information, UserInformation.class);
			userInformation.setCreateDate(LocalDateTime.now());
			String epassword = encryption.encode(information.getPassword());
			userInformation.setPassword(epassword);
			userInformation.setIs_verified(false);
			userInformation = repository.save(userInformation);
			String mailResponse = response.fromMessage("http://localhost:8080/users/verify",
					generate.JwtToken(userInformation.getUserId()));

			mailObject.setEmail(information.getEmail());
			mailObject.setMessage(mailResponse);
			mailObject.setSubject("verification");
			 MailServiceProvider.sendEmail(mailObject.getEmail(), mailObject.getSubject(),
			 mailObject.getMessage());
			rabbitmq.send(mailObject);
			return true;
		}
		
		return false;
	}

	/*
	 * Service for the Login of the user
	 */
	@Transactional
	@Override
	public UserInformation login(LoginInformation information) {
		UserInformation user = repository.getUser(information.getEmail());
		if (user != null) {
			if ((user.isIs_verified() == true) && (encryption.matches(information.getPassword(), user.getPassword()))) {
				System.out.println(generate.JwtToken(user.getUserId()));
				System.out.println(user);
				return user;
			} else {
				String mailResponse = response.fromMessage("http://localhost:8080/users/verify",
						generate.JwtToken(user.getUserId()));
				MailServiceProvider.sendEmail(information.getEmail(), "Verification", mailResponse);
				return null;
			}
		} else {
			return null;
		}

	}

	/*
	 * Controller method for the verify
	 */
	@Transactional
	@Override
	public boolean verify(String token) throws Exception {
		System.out.println("id in verification" + (long) generate.parseJWT(token));
		Long id = (long) generate.parseJWT(token);
		repository.verify(id);
		return true;
	}
	/*
	 * Used for the check the the user is present in database or not
	 */

	@Override
	public boolean isUserExist(String email) {
		try {
			UserInformation user = repository.getUser(email);
			if (user.isIs_verified() == true) {
//				String mailResponse = response.fromMessage("http://localhost:8080/users/verify",
//						generate.JwtToken(user.getUserId()));
				String mailResponse = response.fromMessage("http://localhost:4200/resetpassword",generate.JwtToken(user.getUserId()));
				MailServiceProvider.sendEmail(user.getEmail(), "Verification", mailResponse);
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			throw new UserException("User does not exit",HttpStatus.NOT_FOUND);
		}

	}

	/*
	 * Used to update the password of the user
	 */
	@Transactional
	@Override
	public boolean update(PasswordUpdate information, String token) {
		Long id = null;
		System.out.println("hello");
		try {
			id = (Long) generate.parseJWT(token);	
			String epassword = encryption.encode(information.getConfirmPassword());
			information.setConfirmPassword(epassword);
			return repository.upDate(information, id);

		} catch (Exception e) {
			e.printStackTrace();
			throw new UserException("User does not exit",HttpStatus.NOT_FOUND);
		}
	}

	/*
	 * Used to get all the details from the Databases
	 */
	@Transactional
	@Override
	public List<UserInformation> getUsers() {
		List<UserInformation> users = repository.getUsers();
		// UserInformation user = users.get(0);
		return users;
	}

	/*
	 * Used to get the Single details from the Databases
	 */
	@Transactional
	@Override
	public UserInformation getsingleUser(String token) {
		Long id;
		try {
			id = (Long) generate.parseJWT(token);

		} catch (Exception e) {
			throw new UserException("User does not exit",HttpStatus.NOT_FOUND);

		}
		UserInformation user = repository.getUserById(id);

		return user;

	}

	@Transactional
	@Override
	public NoteInformation addCollaborator(Long noteId, String email, String token) {

		UserInformation collaborator = repository.getEmail(email);
		try {
			Long id = (Long) generate.parseJWT(token);
			UserInformation user = repository.getUserById(id);

			if (user != null) {
				if (collaborator != null) {
					NoteInformation note = noterepository.findbyId(noteId);
					collaborator.getColaborateNote().add(note);

					mailObject.setEmail(email);
					mailObject.setMessage("Note Has Been Collaborated to your Email");
					mailObject.setSubject("Collaborated");
//					MailServiceProvider.sendEmail(mailObject.getEmail(), mailObject.getSubject(),
//							mailObject.getMessage());
					rabbitmq.send(mailObject);
					return note;
				} else {

					throw new UserException("The collaborator does not exist",HttpStatus.NOT_FOUND);
				}
			} else {
				throw new UserException("User does not exit",HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			throw new UserException("User does not exit",HttpStatus.NOT_FOUND);
			
		}

	}

	@Transactional
	@Override
	public void removecollaborator(Long noteId, String email, String token) {
		UserInformation collaborator = repository.getEmail(email);
		try {
			Long id = (Long) generate.parseJWT(token);
			UserInformation user = repository.getUserById(id);

			if (user != null) {
				if (collaborator != null) {
					NoteInformation note = noterepository.findbyId(noteId);
					// user.getColaborateNote().remove(collaborator);
					// user.getColaborateNote().remove(collaborator);
					collaborator.getColaborateNote().remove(note);

				}
			}

		} catch (Exception e) {
			throw new UserException("User does not exit",HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@Override
	public List<NoteInformation> getcollaborator(String token) {
		Long id = (Long) generate.parseJWT(token);
		UserInformation user = repository.getUserById(id);
		List<NoteInformation> notes = user.getColaborateNote();

		return notes;

	}

}
