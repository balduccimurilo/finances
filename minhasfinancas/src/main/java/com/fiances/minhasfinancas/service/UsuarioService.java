package com.fiances.minhasfinancas.service;

import com.fiances.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar( String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
		
	
}
