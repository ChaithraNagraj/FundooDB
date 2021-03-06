package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.model.LabelModel;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.LabelService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/label")
public class LabelController {

	@Autowired
	private LabelService labelService;

	/*
	 * API to add label
	 */
	@PostMapping("/addlabel")
	@ApiOperation(value = "Api to add label for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> createLabel(@RequestBody LabelDto labeldto, @RequestHeader("token") String token) {
		int result = labelService.createLabel(labeldto, token);
		if (result != 0)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is Created", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Already exist in label ", 400));
	}
	
	/*
	 * API to update label
	 */
	@PostMapping("/updatelabel")
	@ApiOperation(value = "Api to update label for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> updateLabel(@RequestBody LabelDto labeldto, @RequestHeader("token") String token, @RequestParam("labelId") long labelId)
	{
		boolean result = labelService.updateLabel(labeldto, token, labelId);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is updated", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
	}
	
	/*
	 * API to delete label
	 */
	@PostMapping("/deletelabel")
	@ApiOperation(value = "Api to delete label for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> deleteLabel(@RequestParam("token") String token, @RequestHeader("labelId") long labelId)
	{
		boolean result = labelService.deleteLabel(token, labelId);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is deleted", 200));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Something went wrong", 400));
	}
	
	/*
	 * API to get all label
	 */
	@GetMapping("/alllabel")
	@ApiOperation(value = "Api to get all label for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> getAllLabel(@RequestHeader("token") String token)
	{
		List<LabelModel> labelList = labelService.getAllLabel(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "all labels of user", labelList));
	}
	
	/*
	 * API to map label to note
	 */
	@PostMapping("/maptonote")
	@ApiOperation(value = "Api to create or map label with note for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> mapToNote(@RequestBody LabelDto labeldto,@RequestHeader("token") String token, @PathVariable("noteid") Long noteid)
	{
		LabelModel result = labelService.createOrMapWithNote(labeldto, noteid, token);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is mapped to note", 200));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The label you are trying to create is already exist!!!", 400));
	}
	
	/*
	 * API to add some note to a label
	 */
	@PostMapping("/addLabelsToNote")
	@ApiOperation(value = "Api to add label to note for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> addLabels(@RequestHeader("token") String token, @RequestParam("labelid") long labelid, @RequestParam("noteid") long noteid)
	{
		LabelModel result = labelService.addLabelsToNote(token, labelid, noteid);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("label added", 200));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Something went wrong", 400));
	}

}
