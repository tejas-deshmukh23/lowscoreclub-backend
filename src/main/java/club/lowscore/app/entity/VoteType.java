package club.lowscore.app.entity;

import club.lowscore.app.genericentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="t_vote_type")
public class VoteType extends BaseEntity {
	
	@Column(name="vote_type")
	private String voteType;

	public String getVoteType() {
		return voteType;
	}

	public void setVoteType(String voteType) {
		this.voteType = voteType;
	}

}
