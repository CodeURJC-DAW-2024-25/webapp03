package es.webapp03.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.webapp03.backend.model.Comment;
import es.webapp03.backend.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<Comment> findByCourseIdOrderByCreatedDateDesc(long id) {
        return commentRepository.findByCourseIdOrderByCreatedDateDesc(id);
    }

}
