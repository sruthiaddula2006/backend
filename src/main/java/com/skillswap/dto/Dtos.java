package com.skillswap.dto;
import com.skillswap.model.User; import jakarta.validation.constraints.*; import java.time.Instant; import java.util.*;
public final class Dtos {
  private Dtos(){}
  public record RegisterReq(@NotBlank(message="Name is required.") String name, @NotBlank(message="Email is required.") @Email(message="Please enter a valid email.") String email,
    @NotBlank(message="Password is required.") @Size(min=6,message="Password must be at least 6 characters.") String password,
    @NotBlank(message="College/organization is required.") String college, @NotBlank(message="Location is required.") String location, String bio){}
  public record LoginReq(@NotBlank(message="Please enter your email.") String email, @NotBlank(message="Please enter your password.") String password){}
  public record ProfileReq(@NotBlank(message="Name is required.") String name, String college, String location, String bio, String avatar,
    Set<String> skillsKnown, @NotEmpty(message="Please select at least one skill you want to learn.") Set<String> skillsWanted){}
  public record SendRequestReq(@NotNull Long receiverId, @NotBlank(message="Please write a message.") String message){}
  public record SendMessageReq(@NotNull Long receiverId, @NotBlank(message="Message cannot be empty.") String text){}
  public record UserDto(Long id,String name,String email,String college,String location,String bio,String avatar,Set<String> skillsKnown,Set<String> skillsWanted){
    public static UserDto from(User u){ return new UserDto(u.getId(),u.getName(),u.getEmail(),u.getCollege(),u.getLocation(),u.getBio(),u.getAvatar(),new LinkedHashSet<>(u.getSkillsKnown()),new LinkedHashSet<>(u.getSkillsWanted())); } }
  public record AuthRes(String token, UserDto user){}
  public record MatchDto(UserDto user,int score,List<String> canTeach,List<String> canLearn,String reason){}
  public record RequestDto(Long id,Long senderId,String senderName,Long receiverId,String receiverName,Set<String> skillsOffered,Set<String> skillsRequested,String message,String status,Instant createdAt){}
  public record ExchangeDto(Long id,UserDto partner,Set<String> youTeach,Set<String> youLearn,String status,Instant createdAt){}
  public record MessageDto(Long id,Long senderId,Long receiverId,String text,Instant timestamp){}
  public record NotificationDto(Long id,String type,String message,boolean read,Instant createdAt){}
}
