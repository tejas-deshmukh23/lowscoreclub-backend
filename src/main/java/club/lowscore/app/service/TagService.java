package club.lowscore.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import club.lowscore.app.entity.Tag;
import club.lowscore.app.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired
	TagRepository tagRepository;

	public List<Tag> getAllTags() {
		return tagRepository.findAll();
	}

}
