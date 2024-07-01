package com.springboot.springboot_jpa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.springboot.springboot_jpa.dao.PostRepository;
import com.springboot.springboot_jpa.dao.UserRepository;
import com.springboot.springboot_jpa.entity.Hashtag;
import com.springboot.springboot_jpa.entity.Post;
import com.springboot.springboot_jpa.entity.User;
import com.springboot.springboot_jpa.servicetypes.CreatePostRequest;
import com.springboot.springboot_jpa.servicetypes.GetPostDTO;
import com.springboot.springboot_jpa.servicetypes.PostsHashtagDTO;
import com.springboot.springboot_jpa.servicetypes.PostsUserDTO;
import com.springboot.springboot_jpa.servicetypes.UpdateUser;

@Service
public class PostService {
     @Autowired
     private PostRepository postRepository;

     public PostService(PostRepository postRepository) {
          this.postRepository = postRepository;
     }

     public Post createPost(CreatePostRequest newPost, User existingUser) {
          Post post = new Post(newPost.getPostId(), newPost.getContent(), existingUser,
                    newPost.getHashtags());
          Post savedPost = this.postRepository.save(post);
          return savedPost;
     }

     public GetPostDTO findPost(int postId) {
          Optional<Post> post = this.postRepository.findById(postId);
          if (post.isPresent()) {
               Post presentPost = post.get();
               return new GetPostDTO(presentPost.getPostId(), presentPost.getUser().getUserId(),
                         presentPost.getContent(), presentPost.getComments(), presentPost.getHashtags(),
                         presentPost.getPostedAt(), presentPost.getLastUpdatedAt(), presentPost.getShares(),
                         presentPost.getLikes());
          }
          return null;
     }

     public PostsUserDTO findPostsUser(int postId) {
          System.out.println("inside post service :" + this.postRepository.findPostsUser(postId));
          User user = this.postRepository.findPostsUser(postId);
          return new PostsUserDTO(user.getUserId(), user.getName(), user.getCity(), user.getStatus());
     }

     public Set<Hashtag> findPostsHashtags(int postId) {
          Set<Hashtag> postHashtags = this.postRepository.findPostsHashtags(postId);
          return postHashtags;
     }

     public List<PostsHashtagDTO> findMultiplePostsHashtags(List<Integer> postsIds) {
          Set<Hashtag> postHashtags = this.postRepository.findMultiplePostsHashtags(postsIds);
          System.out.println("inside findMultiplePostsHashtags service\n" + postHashtags);

          List<PostsHashtagDTO> postsHashtagDTOList = new ArrayList<>();
          for (Hashtag hashtag : postHashtags) {
               for (Post post : hashtag.getPosts()) {
                    PostsHashtagDTO postsHashtagDTO = new PostsHashtagDTO();
                    postsHashtagDTO.setHashtagId(hashtag.getHashtagId());
                    postsHashtagDTO.setHashtagName(hashtag.getHashtagName());
                    postsHashtagDTO.setPostId(post.getPostId());
                    postsHashtagDTOList.add(postsHashtagDTO);
               }
          }

          return postsHashtagDTOList;
     }

     public void deletePost(int postId) {
          this.postRepository.deleteById(postId);
     }

     // public User updateUser(int userId, UpdateUser postDataTobeUpdated) {
     // Optional<User> optionalUser = this.userRepository.findById(userId);
     // if (optionalUser.isPresent()) {
     // User existingUser = optionalUser.get();
     // if (userDataToBeUpdated.name != null) {
     // existingUser.setName(userDataToBeUpdated.name);
     // }
     // if (userDataToBeUpdated.city != null) {
     // existingUser.setCity(userDataToBeUpdated.city);
     // }
     // if (userDataToBeUpdated.status != null) {
     // existingUser.setName(userDataToBeUpdated.status);
     // }
     // if (userDataToBeUpdated.password != null) {
     // existingUser.setPassword(userDataToBeUpdated.password);
     // }
     // User updatedUser = userRepository.save(existingUser);
     // return updatedUser;
     // }
     // return null;
     // }

}