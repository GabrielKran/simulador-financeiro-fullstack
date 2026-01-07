async function fetchAuth(url, options = {}) {
    const token = localStorage.getItem("token");

    if (!options.headers) {
        options.headers = {};
    }
    
    if (token) {
        options.headers["Authorization"] = `Bearer ${token}`;
    }
        
    const resposta = await fetch(url, options);

    if ((resposta.status === 401 || resposta.status === 403) && !options.manualErrorHandling){
        localStorage.removeItem("token");
        localStorage.removeItem("usuario");

        window.location.href = "/login/login.html";

        return null;
    }

    return resposta;
}

function nomeUsuario() {
    const usuario = localStorage.getItem("usuario");

    document.getElementById("usuarioLogado").innerText = `${usuario}`;
}

function calcTime(tempoMeses) {
    const mesesTotal = tempoMeses;
    const anos = Math.floor(mesesTotal / 12);
    const meses = mesesTotal % 12;

    let partesTexto = [];

    if (anos > 0) {
        const anosPalavra = anos === 1 ? "ano" : "anos";
        partesTexto.push(`${anos} ${anosPalavra}`);
    }

    if (meses > 0) {
        const mesespalavra = meses === 1 ? "mês" : "meses";
        partesTexto.push(`${meses} ${mesespalavra}`);
    }

    let textoFinal = partesTexto.join(" e ") || "Menos de um mês";

    return textoFinal;
}

function formatarNumero(valor) {
    return new Intl.NumberFormat("pt-BR", {
        currency: "BRL",
        style: "currency"
    }).format(valor);
}