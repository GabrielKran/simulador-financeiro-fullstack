const URL_API_REGISTER = "https://simulador-financeiro-fullstack.onrender.com/auth/register";

const URL_API_LOGIN = "https://simulador-financeiro-fullstack.onrender.com/auth/login";

async function fazerCadastro() {
    LoadingSystem.start();

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
            Toast.show("Conta criada! Faça login para continuar.", "success");
            alternarTelas();
        } else if (resposta.status === 409) {
            Toast.show("Este e-mail já está em uso.", "error");

        } else {
            Toast.show("Erro ao criar conta. Tente novamente.", "error");

            document.getElementById("nome-register").value = "";
            document.getElementById("email-register").value = "";
            document.getElementById("senha-register").value = "";
        }

    } catch (error) {
        console.error("Erro ao fazer cadastro", error);

    } finally {
        LoadingSystem.stop();
    }
}

async function fazerLogin() {
    LoadingSystem.start();

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

            window.location.href = "../index.html";
            
        } else {
            Toast.show("E-mail ou senha inválidos.", "error");

            document.getElementById("email-login").value = "";
            document.getElementById("senha-login").value = "";
        }

    } catch (error) {
        console.error("Erro ao fazer login", error);

    } finally {
        LoadingSystem.stop();
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