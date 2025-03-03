package es.webapp03.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import es.webapp03.backend.repository.CommentRepository;

@Controller
public class CommentController {

	@Autowired
	private CommentRepository commentRepository;
    
    @PostMapping("/newcomment") //Revisar si este método hay que borrarlo o ponerlo dentro del metodo de cargar el curso
	public String newComment(Model model) {

		model.addAttribute("comment", commentRepository.findAll());

		return "course";
	}
}
