package club.lowscore.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import club.lowscore.app.entity.Tag;
import club.lowscore.app.service.TagService;

@RestController
@CrossOrigin("*")
public class TagController {
	
	@Autowired
	private TagService tagService;
	
	@GetMapping("/getAllTags")
	public List<Tag> getAllTags(){
		
		try {
			return tagService.getAllTags();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
