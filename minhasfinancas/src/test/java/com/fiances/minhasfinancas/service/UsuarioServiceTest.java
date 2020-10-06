package com.fiances.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fiances.minhasfinancas.exception.ErroAutenticacao;
import com.fiances.minhasfinancas.exception.RegraNegocioException;
import com.fiances.minhasfinancas.model.entity.Usuario;
import com.fiances.minhasfinancas.model.repository.UsuarioRepository;
import com.fiances.minhasfinancas.service.impl.UsuarioServiceImpl;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	UsuarioService service;
	
	@MockBean
	UsuarioRepository repository;
	
	@Before
	public void setUp() {
		service = new UsuarioServiceImpl(repository);
	}
	
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when( repository.findByEmail(email) ).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email, senha);
		
		//verificacao
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//acao
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha") );
		Assertions.assertThat(exception)
		.isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para email informado.");
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater( ) {
		//cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123") );
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida!");
		
	}
	
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenario
		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//acao
		service.validarEmail("email@email.com");		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		
		//acao
		service.validarEmail("email@email.com");
	}
}
