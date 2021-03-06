package com.bridgelabz.fundoonotes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.CollaboratorDto;
import com.bridgelabz.fundoonotes.model.CollaboratorModel;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.CollaboratorService;

import io.swagger.annotations.ApiOperation;
/**
 *In this class we are creating an APi for Collaborator
 * @author  chaithra B N
 *
 */
@RestController
@RequestMapping("/collaborator")
@CrossOrigin(allowedHeaders = "*", origins = "*", exposedHeaders = { "jwtToken" })
public class CollaboratorController {
	
	@Autowired
	private CollaboratorService collaboratorService;
	
	/*
	 * API to add collaborator
	 */
	@PostMapping("/addCollaborator")
	@ApiOperation(value = "Api to add collaborator for Fundoonotes", response = Response.class)
	private ResponseEntity<Response> addCollaborator(@RequestParam("email") String email, @RequestParam("noteId") long noteId, @RequestHeader("token") String token) {
		
		CollaboratorDto collaboratorDto=new CollaboratorDto();
		collaboratorDto.setEmail(email);
		CollaboratorModel result = collaboratorService.addCollaborator(collaboratorDto, token, noteId);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "added collabrator sucessfully!!!", result));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong!!!", 400));
	}
	
	/*
	 * API to delete collaborator
	 */
	@DeleteMapping("/deleteCollaborator/{noteId}")
	@ApiOperation(value = "Api to remove collaborator", response = Response.class)
	public ResponseEntity<Response> deleteCollaborator(@PathVariable(value = "noteId") Long noteId, @RequestHeader("token") String token, @RequestHeader("collaboratorId") Long collaboratorId) {
		
		Optional<CollaboratorModel> result = collaboratorService.deleteCollaborator(collaboratorId, token, noteId);
		if(result != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "delete collaborator sucessfully!!!", result));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong!!!", 400));
	}
	
	/*
	 * API to get all collaborator of a note
	 */
	@GetMapping("/getAllNoteCollaborator/{noteId}")
	@ApiOperation(value = "Api to show a collaborator", response = Response.class)
	public ResponseEntity<Response> getAllCollaborator(@PathVariable(value = "noteId") Long noteId,
			@RequestHeader("token") String token) {
		List<CollaboratorModel> collaboratorList = collaboratorService.getNoteCollaborators(token, noteId);
		
		if(collaboratorList != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response(200, "all note collaborators are", collaboratorList));
		}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
	}

}
