package com.devsuperior.bds.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds.dto.ReviewDTO;
import com.devsuperior.bds.entities.Review;
import com.devsuperior.bds.repositories.ReviewRepository;
import com.devsuperior.bds.repositories.UserRepository;
import com.devsuperior.bds.services.exceptions.DataBaseException;
import com.devsuperior.bds.services.exceptions.ResourceNotFoundException;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional(readOnly = true)
	public List<ReviewDTO> findAll() {
		
		List<Review> entity = reviewRepository.findAll();
		
		return entity.stream().map(x -> new ReviewDTO(x, x.getUser().getId(), x.getMovie().getId())).collect(Collectors.toList());
		
	}
	
	@Transactional(readOnly = true)
	public ReviewDTO findById(Long id) {
		
		Optional<Review> obj = reviewRepository.findById(id);
		
		Review entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id not found"));
		
		return new ReviewDTO(entity);
		}
		
	@Transactional(readOnly = true)
	public List<ReviewDTO> queryMethod(String text){
		
		List<Review> entity = reviewRepository.findAllByTextContainingIgnoreCase(text);
		
		return entity.stream().map(x -> new ReviewDTO(x, x.getUser().getId(), x.getMovie().getId())).collect(Collectors.toList());
	}
	
	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {
		
		Review entity = new Review();
		copyDtoToEntity(entity, dto);
		reviewRepository.save(entity);
		return new ReviewDTO(entity);
	}
	
	@Transactional
	public ReviewDTO update(Long id, ReviewDTO dto) {
		
		try {
		Review entity = reviewRepository.getOne(id);
		copyDtoToEntity(entity, dto);
		reviewRepository.save(entity);
		return new ReviewDTO(entity);
	}catch(EntityNotFoundException e) {
		throw new ResourceNotFoundException("Id not found");
	}
}
	
	public void delete(Long id) {
		
		try {
		reviewRepository.deleteById(id);
	}catch(EntityNotFoundException e) {
		throw new ResourceNotFoundException("Id not found");
	}catch(DataIntegrityViolationException e) {
		throw new DataBaseException("Integrity violation");
	}
}
	
	public void copyDtoToEntity(Review review, ReviewDTO dto) {

		review.setText(dto.getText());
		dto.setUserId(review.getUser().getId());
		dto.setMovieId(review.getMovie().getId());
	}
}
