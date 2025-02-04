package club.lowscore.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.lowscore.app.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
	
}
