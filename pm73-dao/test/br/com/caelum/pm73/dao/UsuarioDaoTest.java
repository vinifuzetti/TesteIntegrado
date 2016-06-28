package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pm73.dominio.Usuario;

public class UsuarioDaoTest {
	private Session session;
	private UsuarioDao usuarioDao;
	
	@Before
	public void initTest(){
		session = new CriadorDeSessao().getSession();
		usuarioDao = new UsuarioDao(session);
	}
	@Test
	public void encontraPorNomeEEmail(){		
		Usuario vini = new Usuario("Vinicius", "vrfuzetti@gmail.com");
		usuarioDao.salvar(vini);
		
		Usuario usuarioDoBanco = usuarioDao.porNomeEEmail("Vinicius", "vrfuzetti@gmail.com");
		assertEquals("Vinicius", usuarioDoBanco.getNome());
		assertEquals("vrfuzetti@gmail.com", usuarioDoBanco.getEmail());
	}
	
	@Test
	public void retornoNull(){		
		Usuario usuarioDoBanco = usuarioDao.porNomeEEmail("Joao", "joao@gmail.com");
		assertNull(usuarioDoBanco);
	}
	
	@After
	public void fimTest(){
		session.close();
	}

}
