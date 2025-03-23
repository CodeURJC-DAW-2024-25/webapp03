package es.webapp03.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.webapp03.backend.dto.CommentBasicDTO;
import es.webapp03.backend.dto.CommentDTO;
import es.webapp03.backend.dto.CommentMapper;
import es.webapp03.backend.model.Comment;
import es.webapp03.backend.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<Comment> findByCourseIdOrderByCreatedDateDesc(long id) {
        return commentRepository.findByCourseIdOrderByCreatedDateDesc(id);
    }

    public Page<Comment> findByCourseId(Long courseId, Pageable pageable) {
        return commentRepository.findByCourseId(courseId, pageable);
    }
    
    public List<CommentBasicDTO> findCommentsByCourseId(Long courseId) {
        List<Comment> comments = commentRepository.findByCourseIdOrderByCreatedDateDesc(courseId);
        return comments.stream().map(commentMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<CommentDTO> findById(Long id) {
        return commentRepository.findById(id).map(commentMapper::toCommentDTO);
    }
}
