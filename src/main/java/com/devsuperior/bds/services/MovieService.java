package com.devsuperior.bds.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds.dto.MovieDTO;
import com.devsuperior.bds.entities.Genre;
import com.devsuperior.bds.entities.Movie;
import com.devsuperior.bds.repositories.GenreRepository;
import com.devsuperior.bds.repositories.MovieRepository;
import com.devsuperior.bds.services.exceptions.DataBaseException;
import com.devsuperior.bds.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {

	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Transactional(readOnly = true)
	public Page<MovieDTO> findAll(Pageable pageable){
		
		Page<Movie> entity = repository.findAll(pageable);
		
		return entity.map(x -> new MovieDTO(x, x.getGenre()));
	}
	
	@Transactional(readOnly = true)
	public MovieDTO findById(Long id) {

		Optional<Movie> obj = repository.findById(id);
		
		Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id not found"));
		
		return new MovieDTO(entity, entity.getGenre());
	}
	
	@Transactional(readOnly = true)
	public List<MovieDTO> queryMethod(String title) {
		
		List<Movie> entity = repository.findAllByTitleContainingIgnoreCase(title);
		
		return entity.stream().map(x -> new MovieDTO(x, x.getGenre())).collect(Collectors.toList());
	}
	
	@Transactional
	public MovieDTO insert(MovieDTO dto) {
		
		Movie entity = new Movie();
		copyDtoToEntity(entity, dto);
		repository.save(entity);
		
		return new MovieDTO(entity, entity.getGenre());
	}
	
	@Transactional
	public MovieDTO update(Long id, MovieDTO dto) {
		
		try {
		Movie entity = repository.getOne(id);
		copyDtoToEntity(entity, dto);
		repository.save(entity);
		return new MovieDTO(entity, entity.getGenre());
		
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found");
		}
	}
	
	public void copyDtoToEntity(Movie entity, MovieDTO dto) {
		
		Genre genre = genreRepository.getOne(dto.getGenre().getId());
		
		entity.setTitle(dto.getTitle());
		entity.setSubTitle(dto.getSubTitle());
		entity.setYear(dto.getYear());
		entity.setImgUrl(dto.getImgUrl());
		entity.setSynopsis(dto.getSynopsis());
		entity.setGenre(genre);
	}
	
	public void deleteById(Long id) {
		
		try {
		repository.deleteById(id);
	}catch(EmptyResultDataAccessException e) {
		throw new ResourceNotFoundException("Id not found");
	}catch(DataIntegrityViolationException e) {
		throw new DataBaseException("Integrity violation");
	}
	}
}
