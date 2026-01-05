package com.gabriel.simulador_financeiro_api.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j // Habilita os logs
@RequiredArgsConstructor // Injeção de dependência segura (substitui Autowired)
public class UsuarioService {

    // 'final' garante que nunca serão nulos e não podem ser trocados
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void editNome(String nomeNovo, Usuario usuario) {
        log.info("Usuario ID {} solicitou alteracao de nome para '{}'", usuario.getId(), nomeNovo);

        usuario.setNome(nomeNovo);
        repository.save(usuario);

        log.info("Nome alterado com sucesso para usuario ID {}", usuario.getId());
        
    }

    public void editSenha(String senhaAtual, String senhaNova, Usuario usuario) {
        log.info("Iniciando processo de troca de senha para usuario ID {}", usuario.getId());

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            log.warn("Falha na troca de senha: Senha atual incorreta. Usuario ID {}", usuario.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Senha atual incorreta");
        }

        String senhaHash = passwordEncoder.encode(senhaNova);
        usuario.setSenha(senhaHash);

        repository.save(usuario);
        log.info("Senha do usuario ID {} atualizada com sucesso", usuario.getId());
    }

    public void deleteUsuario(Usuario usuario, String senha) {
        log.info("SOLICITACAO CRITICA: Exclusao de conta para usuario ID {}", usuario.getId());

        Usuario usuarioDoBanco = repository.findById(usuario.getId())
            .orElseThrow(() -> {
            log.error("Erro grave: Tentativa de deletar usuario ID {} mas ele nao existe no banco", usuario.getId());
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
        });

        if (!passwordEncoder.matches(senha, usuarioDoBanco.getSenha())) {
            log.warn("Exclusao negada: Senha de confirmacao incorreta para usuario ID {}", usuario.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }

        repository.delete(usuarioDoBanco);
        log.info("CONTA EXCLUIDA: Usuario ID {} foi removido permanentemente", usuario.getId());
    }
}
