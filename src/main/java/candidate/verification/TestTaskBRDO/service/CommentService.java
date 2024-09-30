package candidate.verification.TestTaskBRDO.service;

import candidate.verification.TestTaskBRDO.dto.Comment;
import candidate.verification.TestTaskBRDO.dto.CommentResponse;
import candidate.verification.TestTaskBRDO.model.User;
import candidate.verification.TestTaskBRDO.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        fetchAndProcessComments();
    }

    public void fetchAndProcessComments() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://dummyjson.com/comments";

        ResponseEntity<CommentResponse> response = restTemplate.getForEntity(url, CommentResponse.class);
        List<Comment> comments = Objects.requireNonNull(response.getBody()).getComments();

        List<User> users = comments.stream().map(comment -> {
            User user = new User();
            user.setBody(comment.getBody());
            user.setPostId(comment.getPostId());
            user.setUsername(capitalize(comment.getUser().getUsername()));
            user.setUpdatedAt(getCurrentDateTime());
            return user;
        }).collect(Collectors.toList());

        userRepository.saveAll(users);
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    private String capitalize(String username) {
        return username.substring(0, 1).toUpperCase() + username.substring(1);
    }
}

