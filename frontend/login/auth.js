const URL_API_REGISTER = "http://localhost:8080/auth/register";

const URL_API_LOGIN = "http://localhost:8080/auth/login";

async function fazerCadastro() {
    const nomeRegister = document.getElementById("nome-register").value;
    const emailRegister = document.getElementById("email-register").value;
    const senhaRegister = document.getElementById("senha-register").value;

    const objectRegister = {
        nome: nomeRegister,
        email: emailRegister,
        senha: senhaRegister
    }

    try {
        
        const resposta = await fetch(`${URL_API_REGISTER}`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(objectRegister)
        })

        if (resposta.ok) {
            alert("Conta criada! Faça login.");
            alternarTelas();
        } else if (resposta.status === 409) {
            alert("Este e-mail já está cadastrado.");

        } else {
            alert("Erro ao criar conta");

            document.getElementById("nome-register").value = "";
            document.getElementById("email-register").value = "";
            document.getElementById("senha-register").value = "";
        }

    } catch (error) {
        console.error("Erro ao fazer cadastro", error);
    }
}

async function fazerLogin() {
    const emailLogin = document.getElementById("email-login").value;
    const senhaLogin = document.getElementById("senha-login").value;

    const objectLogin = {
        email: emailLogin,
        senha: senhaLogin
    }

    try {
        
        const resposta = await fetch(`${URL_API_LOGIN}`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(objectLogin)
        });

        if (resposta.ok) {
            const dados = await resposta.json();
            const token = dados.token;
            const usuario = dados.nomeUsuario;

            localStorage.setItem("token", token);
            localStorage.setItem("usuario", usuario);

            window.location.href = "/frontend/main/index.html";
            
        } else {
            alert("Email ou senha inválidos");

            document.getElementById("email-login").value = "";
            document.getElementById("senha-login").value = "";
        }

    } catch (error) {
        console.error("Erro ao fazer login", error);
    }
}

function alternarTelas() {
    // Pega os dois elementos
    const login = document.getElementById("card-login");
    const cadastro = document.getElementById("card-register");
    const inputs = document.querySelectorAll("input");

    // Limpa todos os inputs
    inputs.forEach(input => {
        input.value = "";
    })

    // Troca a classe 'hidden' (quem tem, perde. quem não tem, ganha)
    login.classList.toggle("hidden");
    cadastro.classList.toggle("hidden");
}