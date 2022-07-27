package com.augenet.os.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

import com.augenet.os.domain.Cliente;
import com.augenet.os.domain.Pessoa;
import com.augenet.os.dtos.ClienteDTO;
import com.augenet.os.repositories.ClienteRepository;
import com.augenet.os.repositories.PessoaRepository;
import com.augenet.os.services.exceptions.DataIntegratyViolationException;
import com.augenet.os.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private PessoaRepository pessoaRepository;

	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));

	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}

	public Cliente create(@Valid ClienteDTO objDTO) {

		if (findByCpf(objDTO) != null) {
			throw new DataAccessResourceFailureException("CPF já cadastrado na base de dados!");
		}

		return repository.save(new Cliente(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone()));
	}

	public Cliente update(Integer id, @Valid ClienteDTO objDTO) {

		Cliente oldObj = findById(id);

		if (findByCpf(objDTO) != null && findByCpf(objDTO).getId() != id) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}

		oldObj.setNome(objDTO.getNome());
		oldObj.setCpf(objDTO.getCpf());
		oldObj.setTelefone(objDTO.getTelefone());

		return repository.save(oldObj);
	}

	public void delete(Integer id) {

		Cliente obj = findById(id);

		if (obj.getList().size() > 0) {
			throw new DataIntegratyViolationException("Cliente possui Ordens de Serviço, não pode ser deletado!");
		}
		repository.deleteById(id);

	}

	public Pessoa findByCpf(ClienteDTO objDTO) {
		Pessoa obj = pessoaRepository.findByCpf(objDTO.getCpf());

		if (obj != null) {
			return obj;
		}

		return null;

	}
}
