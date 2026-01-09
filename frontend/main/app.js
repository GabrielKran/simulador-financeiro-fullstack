const URL_API_PLANO = "https://simulador-financeiro-fullstack.onrender.com/planos-financeiros";

carregarApp();
function carregarApp() {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "login/login.html";
        return;
    }
    carregarPlanos();
}


let planosMemoriaLocal = [];
let idEmEdicao = null;

async function carregarPlanos() {

    nomeUsuario();

    try {
        
        const resposta = await fetchAuth(URL_API_PLANO, {
            method: "GET"
        });

        if (resposta === null) return;

        if (resposta.ok) {

            const planos = await resposta.json();
            planosMemoriaLocal = planos;

            const divLista = document.getElementById("lista-planos");
            divLista.innerHTML = "";

            planos.forEach(plano => {
                divLista.innerHTML += 
                `
                <div onclick="setUrlId('${plano.id}')" class="card-plano">
                    <h3 id="nomePlano">${plano.nomePlano}</h3>
                    <p><strong>Meta: </strong>${formatarNumero(plano.metaValor)}</p>
                    <p><strong>Tempo Estimado: </strong>${calcTime(plano.mesesEstimados)}</p>

                    <div class="btns">
                        <button onclick="event.stopPropagation(); prepararEdicao('${plano.id}')" class="btn-edit">
                            Editar
                        </button>

                        <button onclick="event.stopPropagation(); deletePlano('${plano.id}')" class="btn-delete">
                            Deletar
                        </button>
                    </div>
                </div>
                `;
            });

        } else {
            Toast.show("Erro desconhecido ao carregar planos", "error")
        }

    } catch(error) {
        console.error("Erro na aplicação", error);
    }
}

function prepararEdicao(id) {
    const plano = planosMemoriaLocal.find(plano => plano.id === id);

    if (plano) {
        document.getElementById("nomePlano").value = plano.nomePlano;
        document.getElementById("metaValor").value = formatarNumero(plano.metaValor);
        document.getElementById("aporteMensal").value = formatarNumero(plano.aporteMensal);
        document.getElementById("taxaJuros").value = plano.taxaJurosAnual;

        idEmEdicao = id;
        document.querySelector(".btn-save").innerText = "Atualizar Plano";
    }

}

async function salvarPlano() {

    const nome = document.getElementById("nomePlano").value;

    // Converter para Float para garantir que vá como número
    const metaRS = document.getElementById("metaValor").value;
    const aporteRS = document.getElementById("aporteMensal").value;

    const meta = limparValorMoeda(metaRS);
    const aporte = limparValorMoeda(aporteRS);

    const juros = parseFloat(document.getElementById("taxaJuros").value);
    
    // Validação extra simples antes de enviar
    if (isNaN(meta) || isNaN(aporte) || isNaN(juros)) {
        Toast.show("Preencha os valores corretamente.", "warning");
        return;
    }

    const novoPlano = {
        nomePlano: nome,
        metaValor: meta,
        aporteMensal: aporte,
        taxaJurosAnual: juros,
    }

    let method = "POST";
    let url = URL_API_PLANO;

    if (idEmEdicao !== null) {
        method = "PUT";
        url = `${URL_API_PLANO}/${idEmEdicao}`;
    }

    try {
        
        const resposta = await fetchAuth(url, {
            method: method,
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(novoPlano)
        });

        if (resposta === null) return;

        if (resposta.ok) {
            Toast.show("Plano salvo com sucesso!", "success");
            
            limparInput();
            const planoResposta = await resposta.json();

            carregarPlanos();

        } else {
            Toast.show("Não foi possível salvar o plano.", "error");

        }
        
    } catch (error) {
        console.error("Erro ao salvar", error);
    }
}

function limparInput() {
    document.getElementById("nomePlano").value = "";
    document.getElementById("metaValor").value = "";
    document.getElementById("aporteMensal").value = "";
    document.getElementById("taxaJuros").value = "";

    idEmEdicao = null;
    document.querySelector(".btn-save").innerText = "Salvar Plano";
}

async function deletePlano(id) {

    const confirmou = await Modal.confirm("Excluir Plano", "Tem certeza? Essa ação não pode ser desfeita.");
    if (confirmou) {
        
        try {
            
            const resposta = await fetchAuth(`${URL_API_PLANO}/${id}`, {
                method: "DELETE"
            })

            if (resposta === null) return;

            if (resposta.ok) {
                Toast.show("Plano removido com sucesso!", "success");
                carregarPlanos();

            } else {
                Toast.show("Erro ao remover o plano.", "error");
            }

        } catch (error) {
            console.error("Erro na aplicação DELETE", error);
        }
    }
}

function deslogarUsuario() {
    localStorage.removeItem("token");
    localStorage.removeItem("usuario");
    window.location.href = "login/login.html";
}

function setUrlId(id) {
    window.location.href = `details/details.html?id=${id}`;
}

// CONFIGURAÇÂO DA INTERFACE DE ASIDE E CARDS

const overlay = document.getElementById("overlay");
const sidebar = document.getElementById("sidebar");
const btnUser = document.getElementById("btn-user");
const btnFecharSidebar = document.getElementById("btn-fechar");

const btnNome = document.getElementById("btn-abrir-nome");
const btnSenha = document.getElementById("btn-abrir-senha");
const btnDelete = document.getElementById("btn-abrir-delete");

const cardNome = document.getElementById("card-nome");
const cardSenha = document.getElementById("card-senha");
const cardDelete = document.getElementById("card-delete");

const btnFecharCardNome = document.getElementById("btn-fechar-nome");
const btnFecharCardSenha = document.getElementById("btn-fechar-senha");
const btnFecharCardDelete = document.getElementById("btn-fechar-delete");

const inputCards = document.querySelectorAll(".input-cards");

const nomeNovo = document.getElementById("novo-nome");
const senhaAtual = document.getElementById("senha-atual");
const senhaNova = document.getElementById("nova-senha");
const senhaAtualDelete = document.getElementById("senha-delete");
const senhaDelete = document.getElementById("senha-delete");

function fecharTudo() {
    sidebar.classList.remove("ativo");
    overlay.classList.remove("ativo");
    cardNome.classList.add("hidden");
    cardSenha.classList.add("hidden");
    cardDelete.classList.add("hidden");
    
    inputCards.forEach(input => {
        input.value = "";
    })
}

function toggleSidebar() {
    if (sidebar.classList.contains("ativo")) {
        fecharTudo()
    } else {
        sidebar.classList.add("ativo");
        overlay.classList.add("ativo");
    }
}

function abrirCard(cardParaAbrir) {
    sidebar.classList.remove("ativo");
    overlay.classList.add("ativo");
    cardParaAbrir.classList.remove("hidden");
}

btnUser.addEventListener("click", toggleSidebar);

overlay.addEventListener("click", fecharTudo);
btnNome.addEventListener("click", () => abrirCard(cardNome));
btnSenha.addEventListener("click", () => abrirCard(cardSenha));
btnDelete.addEventListener("click", () => abrirCard(cardDelete));
btnFecharCardNome.addEventListener("click", fecharTudo);
btnFecharCardSenha.addEventListener("click", fecharTudo);
btnFecharCardDelete.addEventListener("click", fecharTudo);
btnFecharSidebar.addEventListener("click", fecharTudo);

async function editarNomeUsuario(event) {
    event.preventDefault();
    const url = "https://simulador-financeiro-fullstack.onrender.com/usuarios/me/nome";

    const nomeEdicao = {
        nomeNovo: `${nomeNovo.value}`
    }

    try {
        const resposta = await fetchAuth(url, {
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(nomeEdicao)
        })

        if (resposta === null) return;

        if (resposta.ok) {
            Toast.show("Nome atualizado!", "success");
            localStorage.setItem("usuario", nomeEdicao.nomeNovo);
            nomeUsuario();
            fecharTudo();

        } else {
            Toast.show("Erro ao editar nome", "error");
        }

    } catch (error) {
        console.error("Erro na edição");
    }

}

async function editarSenhaUsuario(event) {
    event.preventDefault();
    const url = "https://simulador-financeiro-fullstack.onrender.com/usuarios/me/senha";

    if (senhaAtual.value === senhaNova.value) {
        Toast.show("As senhas devem ser diferentes.", "warning");
        senhaAtual.value = "";
        senhaNova.value = "";
        return;
    }

    const senhadicao = {
        senhaAtual: senhaAtual.value,
        senhaNova: senhaNova.value
    }

    try {
        const resposta = await fetchAuth(url, {
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(senhadicao),

            manualErrorHandling: true
        })


        if (resposta.ok) {
            Toast.show("Senha atualizada!", "success");
            fecharTudo();

        } else if (resposta.status === 401 || resposta.status === 403) {
            Toast.show("A senha atual está incorreta.", "error");
            senhaAtual.value = "";
            senhaNova.value = "";

            senhaAtual.focus();

        } else {
            Toast.show("Erro desconhecido ao editar senha", "error")
        }

    } catch (error) {
        console.error("Erro na edição");
    }
}

async function deletarUsuario(event) {
    event.preventDefault();
    const url = "https://simulador-financeiro-fullstack.onrender.com/usuarios/me";

    const senhaForDelete = {
        senha: senhaDelete.value
    }

    try {
        const resposta = await fetchAuth(url, {
            method: "DELETE",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(senhaForDelete),

            manualErrorHandling: true
        })

        if (resposta.ok) {
            Toast.show("Usuário apagado com sucesso!", "success")
            localStorage.removeItem("token");
            localStorage.removeItem("usuario");
            
            window.location.href = "login/login.html";
            
        } else if (resposta.status === 401 || resposta.status === 403) {
            Toast.show("A senha atual está incorreta.", "error")
            senhaDelete.value = "";

            senhaDelete.focus();

        } else {
           Toast.show("Erro desconhecido ao deletar usuário.", "error")
        }

    } catch (error) {
        console.error("Erro na edição");
    }
}

document.getElementById("form-nome").addEventListener("submit", editarNomeUsuario);

document.getElementById("form-senha").addEventListener("submit", editarSenhaUsuario);

document.getElementById("form-delete").addEventListener("submit", deletarUsuario);