const URL_API_PLANO = "http://localhost:8080/planos-financeiros";

let planosMemoriaLocal = [];
let idEmEdicao = null;

async function carregarPlanos() {
    try {
        
        const resposta = await fetch(URL_API_PLANO);
        const planos = await resposta.json();
        planosMemoriaLocal = planos;

        const divLista = document.getElementById("lista-planos");
        divLista.innerHTML = "";

        planos.forEach(plano => {
            divLista.innerHTML += 
            `
            <div onclick="setUrlId(${plano.id})" class="card-plano">
                <h3 id="nomePlano">${plano.nomePlano}</h3>
                <p><strong>Meta: </strong>${formatarNumero(plano.metaValor)}</p>
                <p><strong>Dono: </strong>${plano.usuario.nome}</p>

                <div class="btns">
                    <button onclick="event.stopPropagation(); prepararEdicao(${plano.id})" class="btn-edit">
                        Editar
                    </button>
                    
                    <button onclick="event.stopPropagation(); deletePlano(${plano.id})" class="btn-delete">
                        Deletar
                    </button>
                </div>
            </div>
            `;
        });
        
    } catch(error) {
        console.error("Erro na aplicação GET", error);
    }
}

function prepararEdicao(id) {
    const plano = planosMemoriaLocal.find(p => p.id === id);

    if (plano) {
        document.getElementById("nomePlano").value = plano.nomePlano;
        document.getElementById("metaValor").value = plano.metaValor;
        document.getElementById("aporteMensal").value = plano.aporteMensal;
        document.getElementById("taxaJuros").value = plano.taxaJurosAnual;

        idEmEdicao = id;
        document.querySelector(".btn-save").innerText = "Atualizar Plano";
    }

}

async function salvarPlano() {

    const nome = document.getElementById("nomePlano").value;
    const meta = document.getElementById("metaValor").value;
    const aporte = document.getElementById("aporteMensal").value;
    const juros = document.getElementById("taxaJuros").value;
    
    const novoPlano = {
        nomePlano: nome,
        metaValor: meta,
        aporteMensal: aporte,
        taxaJurosAnual: juros,

        //Para teste todos devem ser Joao
        usuario: {id: 1}
    }

    let method = "POST";
    let url = URL_API_PLANO;

    if (idEmEdicao !== null) {
        method = "PUT";
        url = `${URL_API_PLANO}/${idEmEdicao}`;
    }

    try {
        
        const resposta = await fetch(url, {
            method: method,
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(novoPlano)
        });

        if(resposta.ok) {
            alert("Plano salvo com sucesso");
            limparInput();
            carregarPlanos();

        } else {
            alert("Erro ao salvar Plano");

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
    if(confirm("Tem certeza que deseja apagar este Plano?")) {
        try {
            
            const resposta = await fetch(`${URL_API_PLANO}/${id}`, {
                method: "DELETE"
            })

            if (resposta.ok) {
                alert("Plano removido com sucesso");
                carregarPlanos();

            } else {
                alert("Erro ao remover Plano");
            }

        } catch (error) {
            console.error("Erro na aplicação DELETE", error);
        }
    }
}

function setUrlId(id) {
    window.location.href = `detalhes.html?id=${id}`;
}

function formatarNumero(valor) {
    return new Intl.NumberFormat("pt-BR", {
        currency: "BRL",
        style: "currency"
    }).format(valor);
}

carregarPlanos();