package club.lowscore.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.lowscore.app.entity.VoteType;

public interface VoteTypeRepository extends JpaRepository<VoteType, Long> {

}
