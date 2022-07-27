package com.augenet.os.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.augenet.os.domain.Cliente;
import com.augenet.os.domain.OS;
import com.augenet.os.domain.Tecnico;
import com.augenet.os.domain.enuns.Prioridade;
import com.augenet.os.domain.enuns.Status;
import com.augenet.os.repositories.ClienteRepository;
import com.augenet.os.repositories.OSRepository;
import com.augenet.os.repositories.TecnicoRepository;

@Service
public class DBService {

	@Autowired
	private TecnicoRepository tecnicoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private OSRepository osRepository;

	public void instaciaDB() {

		Tecnico t1 = new Tecnico(null, "Jose Allan", "909.229.301-63", "62 98888-8888");
		Tecnico t2 = new Tecnico(null, "Joao Lucas", "265.760.530-37", "62 97777-7777");
		Cliente c1 = new Cliente(null, "Gabriella", "659.908.660-89", "62 97777-4444");

		OS os1 = new OS(null, Prioridade.ALTA, "Teste create OS", Status.ANDAMENTO, t1, c1);

		t1.getList().add(os1);
		c1.getList().add(os1);

		tecnicoRepository.saveAll(Arrays.asList(t1,t2));
		clienteRepository.saveAll(Arrays.asList(c1));

		osRepository.saveAll(Arrays.asList(os1));
	}

}
