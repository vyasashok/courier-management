package com.cm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cm.core.service.FileService;

@RestController
@RequestMapping("/api/file")
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	@PostMapping("/uploadfile")
	public boolean uploadFile(@RequestParam("file") MultipartFile multipart, HttpServletRequest request){
		
		String fileKey = "cm-upload/documents/"+multipart.getOriginalFilename();
		fileService.uploadFile(fileKey, multipart);
		return true;
	}

}
