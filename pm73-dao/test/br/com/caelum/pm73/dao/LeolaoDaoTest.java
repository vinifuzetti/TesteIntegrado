package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;

public class LeolaoDaoTest {
	private Session session;
	private LeilaoDao leilaoDao;
	private UsuarioDao usuarioDao;
	
	@Before
	public void initTest(){
		session = new CriadorDeSessao().getSession();
		leilaoDao = new LeilaoDao(session);
		usuarioDao = new UsuarioDao(session);
		
		session.beginTransaction();
	}
	
	@Test
	public void contaLeiloesNaoEncerrado(){
		Usuario vini = new Usuario("Vinicius", "vrfuzetti@gmail.com");
		
		Leilao ativo = new Leilao("TV", 1500.0, vini, false);
		Leilao encerrado = new Leilao("PS4", 1300.0, vini, false);
		encerrado.encerra();
		
		usuarioDao.salvar(vini);
		leilaoDao.salvar(ativo);
		leilaoDao.salvar(encerrado);
		
		long total = leilaoDao.total();
		
		assertEquals(1L, total);
		
	}
	
	@Test
	public void nenhumLeilaoNaoEncerrado(){
		Usuario vini = new Usuario("Vinicius", "vrfuzetti@gmail.com");
		
		Leilao leilao = new Leilao("TV", 1500.0, vini, false);
		leilao.encerra();
		
		usuarioDao.salvar(vini);
		leilaoDao.salvar(leilao);
		
		long total = leilaoDao.total();
		
		assertEquals(0L, total);
	}
	
	@Test
	public void leiloesNaoUsados(){
		Usuario vini = new Usuario("Vinicius", "vrfuzetti@gmail.com");
		
		Leilao usado = new Leilao("TV", 1500.0, vini, true);
		Leilao naoUsado = new Leilao("PS4", 1200.0, vini, false);
		
		usuarioDao.salvar(vini);
		leilaoDao.salvar(usado);
		leilaoDao.salvar(naoUsado);
		
		List<Leilao> listaLeilao = leilaoDao.novos();
		
		assertEquals(listaLeilao.get(0).getNome(), naoUsado.getNome());
	}
	
	@Test
	public void leiloesAntigos(){
		Usuario vini = new Usuario("Vinicius", "vrfuzetti@gmail.com");
		Calendar dataAtual = Calendar.getInstance();
		Calendar dataAntiga = Calendar.getInstance();
		Calendar dataSeteDias = Calendar.getInstance();
		dataAtual.set(2016, Calendar.JUNE, 28);
		dataAntiga.set(2016, Calendar.JUNE, 18);
		dataSeteDias.set(2016, Calendar.JUNE, 21);
		
		Leilao ps4 = new Leilao("PS4", 1500.0, vini, false);
		ps4.setDataAbertura(dataAntiga);
		Leilao mesa = new Leilao("Mesa", 500.0, vini, false);
		mesa.setDataAbertura(dataSeteDias);
		Leilao geladeira = new Leilao("Geladeira", 1500.0, vini, false);
		geladeira.setDataAbertura(dataAntiga);
		Leilao som = new Leilao("Som", 200.0, vini, false);
		som.setDataAbertura(dataAntiga);
		Leilao carro = new Leilao("Car", 51500.0, vini, false);
		carro.setDataAbertura(dataSeteDias);
		Leilao moto = new Leilao("Moto", 11200.0, vini, false);
		moto.setDataAbertura(dataAtual);
		
		usuarioDao.salvar(vini);
		leilaoDao.salvar(ps4);
		leilaoDao.salvar(mesa);
		leilaoDao.salvar(geladeira);
		leilaoDao.salvar(som);
		leilaoDao.salvar(carro);
		leilaoDao.salvar(moto);
		
		List<Leilao> listaLeilao = leilaoDao.antigos();
		
		assertEquals(5, listaLeilao.size());
	}
	
	@After
	public void fimTest(){
		session.getTransaction().rollback();
		session.close();
	}

}
