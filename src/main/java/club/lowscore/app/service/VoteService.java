package club.lowscore.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import club.lowscore.app.entity.Post;
import club.lowscore.app.entity.User;
import club.lowscore.app.entity.Vote;
import club.lowscore.app.entity.VoteCount;
import club.lowscore.app.entity.VoteType;
import club.lowscore.app.exception.PostNotFoundException;
import club.lowscore.app.exception.UserNotFoundException;
import club.lowscore.app.exception.VoteCountNotFoundException;
import club.lowscore.app.exception.VoteTypeNotFoundException;
import club.lowscore.app.repository.PostRepository;
import club.lowscore.app.repository.UserRepository;
import club.lowscore.app.repository.VoteCountRepository;
import club.lowscore.app.repository.VoteRepository;
import club.lowscore.app.repository.VoteTypeRepository;

@Service
public class VoteService {
	
	@Autowired
	VoteRepository voteRepository;
	
	@Autowired
	PostRepository postRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	VoteTypeRepository voteTypeRepository;
	
	@Autowired
	VoteCountRepository voteCountRepository;
	
	//Remaing task in likePost -> to check if the user has disliked the post and if disliked then remove the dislike and then add the like
	
	public Integer likePost(String userId, String postId) {
		
		//here first we will find the like object which will match this user id and postid and then if we found that like ibject then we will update that 
		Boolean votePresentFlag = false;
		Boolean changeDislikeToLikeFlag = false;
		
		Optional<VoteCount> voteCount = voteCountRepository.findByPostId(Long.parseLong(postId));
		
		Optional<Vote> optionalVote;
		Vote vote = new Vote();
		Optional<Post> post;
		
		Optional<User> user =  userRepository.findById(Long.parseLong(userId));
		if(user.isPresent()) {
			
			post = postRepository.findById(Long.parseLong(postId));
			if(post.isPresent()) {
				
				//here if post is present then we check if that user has liked the post earlier or not
				optionalVote = voteRepository.findByPostIdAndUserId(Long.parseLong(postId), Long.parseLong(userId));
				if(optionalVote.isPresent()) {
//					votePresentFlag = true;//we are making this votePresentFlag true if the user has already liked this post
				
					//Here we will need to check the type of vote is it like or dislike 
					//if the vote is dislike then we will change it to like vote 
					//for this functionality we will have changeDislikeToLikeFlag = true
					
					if(optionalVote.get().getVoteType().getId()==2) {//if vote is like
						changeDislikeToLikeFlag = true;
					}else if(optionalVote.get().getVoteType().getId()==1) {//if vote is like
						//If the vote is like the we will make just delete the vote by doing the votePresentFlag = true
						votePresentFlag = true;
					}
					
				}
				
			}else {
				throw new PostNotFoundException("Post with id : "+postId+" not found");
			}
			
		}else {
			throw new UserNotFoundException("User with id : "+userId+" not found");
		}
		
		
		
		if(votePresentFlag) {
			
			//here we will remove the like from the like table
			
			voteRepository.delete(optionalVote.get());
			
			//here we will decrement the number of like from the table which keeps the record of total likes
			if(voteCount.isPresent()) {
				
				VoteCount voteCount2 = voteCount.get();
				voteCount2.setLikeCount(voteCount2.getLikeCount()-1);
				voteCountRepository.save(voteCount2);
				
			}else
			{
				VoteCount voteCount2 = new VoteCount();
				voteCount2.setLikeCount((long)1);
				voteCount2.setPost(post.get());
				voteCountRepository.save(voteCount2);
			}
			
			
		}else if(changeDislikeToLikeFlag) { //here vote will be present the vote type will be dislike so we will change it to like
			
			//here we are gonna change the voteType of the vote to like from dislike
			Long voteTypeId = (long) 1;
			Optional<VoteType> voteType = voteTypeRepository.findById(voteTypeId);
			
			vote = optionalVote.get();
			
			if(voteType.isPresent()) {
				vote.setVoteType(voteType.get());
			}else {
				throw new VoteTypeNotFoundException("Vote type with id :: "+voteTypeId+" not found");
			}
			
			//here we will decrease the number of like by 1 in the table which keeps the like record for that post and then increase the dislike count by 1
			if(voteCount.isPresent()) {
							
				VoteCount voteCount2 = voteCount.get();
				voteCount2.setDislikeCount(voteCount2.getDislikeCount()-1);
				try {
					
					voteCount2.setLikeCount(voteCount2.getLikeCount()+1); //here we can get exception if their is null value in like column in database table
					
				}catch(NullPointerException e) {
					
					voteCount2.setLikeCount((long)1);
				}
				
				voteCountRepository.save(voteCount2);
				
			}else
			{
				VoteCount voteCount2 = new VoteCount();
				voteCount2.setLikeCount((long)1);
				voteCount2.setPost(post.get());
				voteCountRepository.save(voteCount2);
			}
			
			voteRepository.save(vote);
			
			return 2;
			
		}
		else {
			
			//here we will store the like in the vote table with its voteType that will be like in this case
			
			vote = new Vote();
			vote.setUser(user.get());
			vote.setPost(post.get());
			
			Long voteTypeId = (long) 1;
			Optional<VoteType> voteType = voteTypeRepository.findById(voteTypeId);
			
			if(voteType.isPresent()) {
				vote.setVoteType(voteType.get());
			}else {
				throw new VoteTypeNotFoundException("voteType with id : "+voteTypeId+" not found");
			}
			
			//here we will increase the number of like by 1 in the table which keeps the like record for that post
			if(voteCount.isPresent()) {
							
				VoteCount voteCount2 = voteCount.get();
				voteCount2.setLikeCount(voteCount2.getLikeCount()+1);
				voteCountRepository.save(voteCount2);
				
			}else
			{
				VoteCount voteCount2 = new VoteCount();
				voteCount2.setLikeCount((long)1);
				voteCount2.setPost(post.get());
				voteCountRepository.save(voteCount2);
			}
			
			voteRepository.save(vote);
			
			return 0;
			
		}
		
//		if(!votePresentFlag) {
//			voteRepository.save(vote);
//			return 0;//we are returning this for liked
//		}
		
		return 1;//we are returning this if  like removed
		
		
		
	}
	
	
	public Integer dislikePost(String userId, String postId) {
		
		//here first we will find the like object which will match this user id and postid and then if we found that like object then we will update that 
		Boolean votePresentFlag = false;
		Boolean changeLikeToDislikeFlag = false;
		Optional<VoteCount> voteCount = voteCountRepository.findByPostId(Long.parseLong(postId));
		
		Optional<Vote> optionalVote; //made to check if the dislike already exists 
		Vote vote = new Vote(); //to update the vote
		Optional<Post> post; //to get the post
		
		Optional<User> user =  userRepository.findById(Long.parseLong(userId)); //get the user by id
		
		if(user.isPresent()) {
			
			post = postRepository.findById(Long.parseLong(postId));
			if(post.isPresent()) {
				
				//here if post is present then we check if that user has liked the post earlier or not
				optionalVote = voteRepository.findByPostIdAndUserId(Long.parseLong(postId), Long.parseLong(userId));
				if(optionalVote.isPresent()) {
//					votePresentFlag = true;//we are making this votePresentFlag true if the user has already liked this post
					
					//Here we will need to check the type of vote is it like or dislike 
					//if the vote is like then we will change it to dislike vote 
					//for this functionality we will have changeLikeToDislikeFlag = true
					
					if(optionalVote.get().getVoteType().getId()==1) {//if vote is like
						changeLikeToDislikeFlag = true;
					}else if(optionalVote.get().getVoteType().getId()==2) {//if vote is dislike
						//If the vote is dislike the we will make just delete the flag by by doing the votePresentFlag = true
						votePresentFlag = true;
					}
				}
				
			}else {
				throw new PostNotFoundException("Post with id : "+postId+" not found");
			}
			
		}else {
			throw new UserNotFoundException("User with id : "+userId+" not found");
		}
		
		//Writing the logic accoriding to flags
		if(votePresentFlag) {
			
			//here we will remove the dislike from the vote table
			voteRepository.delete(optionalVote.get());
			
			//here we will decrement the number of dislike from the voteCount table which keeps the record of total dislike
			if(voteCount.isPresent()) {
				VoteCount voteCount2 = voteCount.get();
				voteCount2.setDislikeCount(voteCount2.getDislikeCount()-1);
				voteCountRepository.save(voteCount2);
				
			}else
			{
				VoteCount voteCount2 = new VoteCount();
				voteCount2.setDislikeCount((long)1);
				voteCount2.setPost(post.get());
				voteCountRepository.save(voteCount2);
			}
			
//			voteRepository.save(vote);
			
			//If this gets executed then we are returning 1;
			
		}else if(changeLikeToDislikeFlag) { //here vote will be present the vote type will be like so we will change it to dislike
			
			//here we are gonna change the voteType of the vote to dislike from like
			Long voteTypeId = (long) 2;
			Optional<VoteType> voteType = voteTypeRepository.findById(voteTypeId);
			
			vote = optionalVote.get();
			
			if(voteType.isPresent()) {
				vote.setVoteType(voteType.get());
			}else {
				throw new VoteTypeNotFoundException("Vote type with id :: "+voteTypeId+" not found");
			}
			
			//here we will decrease the number of like by 1 in the table which keeps the like record for that post and then increase the dislike count by 1
			if(voteCount.isPresent()) {
							
				VoteCount voteCount2 = voteCount.get();
				voteCount2.setLikeCount(voteCount2.getLikeCount()-1);//here we will always be having the like count because we are executing this changeLikeToDislikeFlag if block because their is already like on this post by current userId and we just want to change it to dislike hence we will not get here that nullPointerException so no need to handle that exception as their will be no null value present in the like block
				try {
					
					voteCount2.setDislikeCount(voteCount2.getDislikeCount()+1);
					
				}catch(NullPointerException e) {
					
					voteCount2.setDislikeCount((long)1);
				}
				
				voteCountRepository.save(voteCount2);
				
			}else
			{
				VoteCount voteCount2 = new VoteCount();
				voteCount2.setDislikeCount((long)1);
				voteCount2.setPost(post.get());
				voteCountRepository.save(voteCount2);
			}
			
			voteRepository.save(vote);
			return 2;
			
		}else { //We will get here if their is no dislike by the user on this post in the vote table so here we will create a dislike and add 1 in the vote count table
			
			//here we will store the dislike in the vote table with its voteType that will be dislike in this case
			
			vote = new Vote();
			vote.setUser(user.get());
			vote.setPost(post.get());
			
			Long voteTypeId = (long) 2; //2 is for dislike 
			Optional<VoteType> voteType = voteTypeRepository.findById(voteTypeId); //will get the object for dislike voteType
			
			if(voteType.isPresent()) {
				vote.setVoteType(voteType.get());
			}else {
				throw new VoteTypeNotFoundException("voteType with id : "+voteTypeId+" not found");
			}
			
			//here we will increase the number of like by 1 in the table which keeps the like record for that post
			if(voteCount.isPresent()) {
							
				VoteCount voteCount2 = voteCount.get();
//				voteCount2.setLikeCount(voteCount2.getLikeCount()+1);
				try {
					voteCount2.setDislikeCount(voteCount2.getDislikeCount()+1);
				}catch(NullPointerException e) {
					
					voteCount2.setDislikeCount((long)1);
					
				}
				
				voteCountRepository.save(voteCount2);
				
			}else
			{
				VoteCount voteCount2 = new VoteCount();
//				voteCount2.setLikeCount((long)1)
				voteCount2.setDislikeCount((long)1);
				voteCount2.setPost(post.get());
				voteCountRepository.save(voteCount2);
			}
			
			voteRepository.save(vote);
			return 0;
		}
		
		
		return 1;
	}


	public Long getLikesCount(String postId) {
		
		Optional<VoteCount> voteCount = voteCountRepository.findByPostId(Long.parseLong(postId));
		
		if(voteCount.isPresent()) {
			return voteCount.get().getLikeCount();
		}else {
			throw new VoteCountNotFoundException("vote count with postid :: "+postId+" not found");
		}
		
//		return null;
		
	}
	
public Long getDislikeCount(String postId) {
		
		Optional<VoteCount> voteCount = voteCountRepository.findByPostId(Long.parseLong(postId));
		
		if(voteCount.isPresent()) {
			return voteCount.get().getDislikeCount();
		}else {
			throw new VoteCountNotFoundException("vote count with postid :: "+postId+" not found");
		}
		
//		return null;
		
	}


	public Long getIsVoted(String userId, String postId) {
		
		Optional<Vote> vote = voteRepository.findByPostIdAndUserId(Long.parseLong(postId), Long.parseLong(userId));
		if(vote.isPresent()) {
			Long result = vote.get().getVoteType().getId();
			return result;
		}else {
			return (long)0;
		}
		
	}

}
