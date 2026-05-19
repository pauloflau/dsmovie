package com.devsuperior.movieflix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public ReviewDTO insert(ReviewDTO dto) {

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        User user = authService.authenticated();

        Review entity = new Review();
        entity.setText(dto.getText());
        entity.setMovie(movie);
        entity.setUser(user);

        entity = reviewRepository.save(entity);

        ReviewDTO response = new ReviewDTO();
        response.setId(entity.getId());
        response.setText(entity.getText());
        response.setMovieId(movie.getId());
        response.setUserId(user.getId());
        response.setUserName(user.getName());
        response.setUserEmail(user.getEmail());

        return response;
    }
}
