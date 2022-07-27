package com.augenet.os.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.augenet.os.domain.Pessoa;
import com.augenet.os.domain.Tecnico;
import com.augenet.os.dtos.TecnicoDTO;
import com.augenet.os.repositories.PessoaRepository;
import com.augenet.os.repositories.TecnicoRepository;
import com.augenet.os.services.exceptions.DataIntegratyViolationException;
import com.augenet.os.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {

	@Autowired
	private TecnicoRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	public Tecnico findById(Integer id) {

		Optional<Tecnico> obj = repository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Tecnico.class.getName()));

	}

	public List<Tecnico> findAll() {
		return repository.findAll();
	}

	public Tecnico create(TecnicoDTO objDTO) {

		if (findByCpf(objDTO) != null) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}
		return repository.save(new Tecnico(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone()));
	}

	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {

		Tecnico oldObj = findById(id);

		if (findByCpf(objDTO) != null && findByCpf(objDTO).getId() != id) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}
		
		oldObj.setNome(objDTO.getNome());
		oldObj.setCpf(objDTO.getCpf());
		oldObj.setTelefone(objDTO.getTelefone());

		return repository.save(oldObj);
	}
	
	public void delete(Integer id) {
		
		Tecnico obj = findById(id);
		
		if(obj.getList().size()>0) {
			throw new DataIntegratyViolationException("Técnico possui Ordens de Serviço, não pode ser deletado!");
		}
		repository.deleteById(id);
		
	}


	public Pessoa findByCpf(TecnicoDTO objDTO) {
		Pessoa obj = pessoaRepository.findByCpf(objDTO.getCpf());

		if (obj != null) {
			return obj;
		}

		return null;

	}


}
