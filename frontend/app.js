const URL_API_PLANO = "http://localhost:8080/planos-financeiros";

async function carregarPlanos() {
    try {
        
        const resposta = await fetch(URL_API_PLANO);
        const planos = await resposta.json();

        const divLista = document.getElementById("lista-planos");
        divLista.innerHTML = "";

        planos.forEach(plano => {
            divLista.innerHTML += 
            `
            <div class="card-plano">
                <h3 id="nomePlano">${plano.nomePlano}</h3>
                <p><strong>Meta: </strong>${plano.metaValor}</p>
                <p><strong>Dono: </strong>${plano.usuario.nome}</p>

                <div class="btns">
                    <button class="btn-access">
                        Acessar
                    </button>
                    
                    <button onclick="deletePlano(${plano.id})" class="btn-delete">
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

async function criarPlano() {

    const nome = document.getElementById("nomePlano").value;
    const meta = document.getElementById("metaValor").value;
    const aporte = document.getElementById("aporteMensal").value;
    const juros = document.getElementById("taxaJuros").value;

    const novoPlano = {
        nomePlano: nome,
        metaValor: meta,
        aporteMensal: aporte,
        taxaJurosAnual: juros,

        usuario: {id: 1}
    }

    try {
        
        const resposta = await fetch(URL_API_PLANO, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(novoPlano)
        });

        if(resposta.ok) {
            alert("Plano adicionado com sucesso");
            carregarPlanos();

        } else {
            alert("Erro ao adicionar Plano");

        }
        
    } catch (error) {
        console.error("Erro na aplicação POST", error);
    }
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

carregarPlanos();