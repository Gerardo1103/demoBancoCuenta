package com.minsait.demo;

import com.minsait.demo.exception.DineroInsuficienteException;
import com.minsait.demo.models.Banco;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.repositories.BancoRepository;
import com.minsait.demo.repositories.CuentaRepository;
import com.minsait.demo.services.CuentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DemoApplicationTests {
	@Mock
	CuentaRepository cuentaRepository;
	@Mock
	BancoRepository bancoRepository;
	@InjectMocks
	CuentaServiceImpl service;
	@Test
	void testFindAll(){
		when(service.findAll()).thenReturn(Datos.CUENTAS);
		List<Cuenta> cuentas = service.findAll();
		assertFalse(cuentas.isEmpty());
		assertEquals(2,cuentas.size());
		assertTrue(cuentas.get(0).getId().equals(1L));
		assertTrue(cuentas.get(0).getPersona().equals("Gerardo"));
		assertTrue(cuentas.get(1).getId().equals(2L));
		assertTrue(cuentas.get(1).getPersona().equals("Canelo"));
	}
	@Test
	void testFindById(){
		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta1());
		Cuenta cuenta1 = service.findById(1l);
		Cuenta cuenta2 = service.findById(1l);
		assertSame(cuenta1,cuenta2);
		assertTrue(cuenta1.getId().equals(1l));
		assertEquals("Ricardo",cuenta1.getPersona());
	}

	@Test
	void testTransferencia() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta1());
		when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta2());
		when(bancoRepository.findById(1l)).thenReturn(Datos.crearBanco());
		//se revisa el primer saldo
		BigDecimal saldoCuenta1 = service.revisarSaldo(1L);
		BigDecimal saldoCuenta2 = service.revisarSaldo(2L);
		//se compara que tenga lo esperando
		assertEquals("1000",saldoCuenta1.toPlainString());
		assertEquals("5000000",saldoCuenta2.toPlainString());
		//se aplica la transferencia
		service.transferir(2l,1l,new BigDecimal(1000000),1l);
		//se revisan los saldos que si se transfirieron
		saldoCuenta1 = service.revisarSaldo(1L);
		saldoCuenta2 = service.revisarSaldo(2L);
		//pruebas
		assertEquals("1001000",saldoCuenta1.toPlainString());
		assertEquals("4000000",saldoCuenta2.toPlainString());

		//total transferencias
		int totalTransferencias = service.revisarTotalTransferencias(1L);
		assertEquals(1,totalTransferencias);


	}
	@Test
	void testTransferenciaException() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta1());
		when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta2());
		when(bancoRepository.findById(1l)).thenReturn(Datos.crearBanco());
		//se revisa el primer saldo
		BigDecimal saldoCuenta1 = service.revisarSaldo(1L);
		BigDecimal saldoCuenta2 = service.revisarSaldo(2L);
		//se compara que tenga lo esperando
		assertEquals("1000",saldoCuenta1.toPlainString());
		assertEquals("5000000",saldoCuenta2.toPlainString());
		//se aplica la transferencia

		Exception exception = assertThrows(DineroInsuficienteException.class,()->{
			service.transferir(2l,1l,new BigDecimal(6000000),1l);
		});
		assertEquals("Dinero insuficiente",exception.getMessage());
		//se revisan los saldos que si se transfirieron
		saldoCuenta1 = service.revisarSaldo(1L);
		saldoCuenta2 = service.revisarSaldo(2L);
		//pruebas
		assertEquals("1000",saldoCuenta1.toPlainString());
		assertEquals("5000000",saldoCuenta2.toPlainString());

		//total transferencias
		int totalTransferencias = service.revisarTotalTransferencias(1L);
		assertEquals(0,totalTransferencias);
	}
	//banco
	@Test
	void testBanco(){
		when(bancoRepository.findById(1l)).thenReturn(Datos.crearBanco());
		Optional<Banco> banco1 = bancoRepository.findById(1l);
		Optional<Banco> banco2 = bancoRepository.findById(1l);

		assertEquals(banco1,banco2);
		assertEquals(1l,banco1.get().getId());
		assertEquals("Banco Azteca",banco1.get().getNombre());

	}
	@Test
	void testSave(){
		Cuenta cuenta1 = new Cuenta(3L,"Javier",new BigDecimal("1000"));
		when(cuentaRepository.save(cuenta1)).thenReturn(cuenta1);
		when(cuentaRepository.findAll()).thenReturn(Datos.CUENTAS);
		long id = cuentaRepository.findAll().stream()
				.sorted(Comparator.comparing(Cuenta::getId).reversed())
				.mapToLong(value -> value.getId())
				.findFirst().getAsLong()+1;
		Cuenta cuenta2 = service.save(cuenta1);
		assertAll(
				() -> assertEquals(id,cuenta2.getId()),
				() -> assertEquals("Javier",cuenta2.getPersona()),
				() -> assertEquals("1000",cuenta2.getSaldo().toPlainString())
		);

	}
	@Test
	void testMain(){
		DemoApplication.main(new String[]{});
	}

}
