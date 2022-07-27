package com.augenet.os.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.augenet.os.domain.Cliente;
import com.augenet.os.domain.OS;
import com.augenet.os.domain.Tecnico;
import com.augenet.os.domain.enuns.Prioridade;
import com.augenet.os.domain.enuns.Status;
import com.augenet.os.dtos.OSDTO;
import com.augenet.os.repositories.OSRepository;
import com.augenet.os.services.exceptions.ObjectNotFoundException;

@Service
public class OSService {

	@Autowired
	private OSRepository repository;

	@Autowired
	private TecnicoService tecnicoService;

	@Autowired
	private ClienteService clienteService;

	public OS findById(Integer id) {
		Optional<OS> obj = repository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + OS.class.getName()));

	}

	public List<OS> findAll() {

		return repository.findAll();
	}

	public OS create(@Valid OSDTO obj) {
		return fromDTO(obj);
	}

	public OS update(@Valid OSDTO objDTO) {

		findById(objDTO.getId());

		return fromDTO(objDTO);
	}

	private OS fromDTO(OSDTO obj) {
		OS newObj = new OS();
		newObj.setId(obj.getId());
		newObj.setObservacoes(obj.getObservacoes());
		newObj.setPrioridade(Prioridade.toEnum(obj.getPrioridade()));
		newObj.setStatus(Status.toEnum(obj.getStatus()));

		Tecnico tec = tecnicoService.findById(obj.getTecnico());

		Cliente cli = clienteService.findById(obj.getCliente());

		newObj.setTecnico(tec);
		newObj.setCliente(cli);

		if (newObj.getStatus().getCodigo().equals(2)) {
			newObj.setDataFechamento(LocalDateTime.now());
		}

		return repository.save(newObj);
	}

}
