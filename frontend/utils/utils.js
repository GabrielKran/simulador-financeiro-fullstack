const LoadingSystem = {
    activeRequests: 0,
    timerSpinner: null,
    timerMessage1: null,
    timerMessage2: null,
    
    // Elementos do DOM (serão preenchidos no init)
    overlayEl: null,
    textEl: null,

    init() {
        // 1. Verifica se já não existe para não duplicar
        if (document.getElementById('global-loader')) return;

        // 2. Cria o HTML na memória
        const loaderDiv = document.createElement('div');
        loaderDiv.id = 'global-loader';
        loaderDiv.innerHTML = `
            <div class="loader-spinner"></div>
            <p class="loader-text">O servidor está demorando um pouco...</p>
        `;

        // 3. Injeta no corpo da página
        document.body.appendChild(loaderDiv);

        // 4. Salva as referências para usar no start/stop
        this.overlayEl = document.getElementById('global-loader');
        this.textEl = loaderDiv.querySelector('.loader-text');
    },

    start() {
        this.activeRequests++;

        if (!this.overlayEl) {
            this.init();
        }

        if (this.activeRequests === 1) {
            
            this.timerSpinner = setTimeout(() => {
                // Verificação dupla de segurança
                if (this.overlayEl) {
                    this.overlayEl.classList.add('visible');
                } else {
                    console.error("LoadingSystem: Erro crítico - Overlay não encontrado mesmo após init.");
                }
            }, 500);

            this.timerMessage1 = setTimeout(() => {
                if (this.textEl) {
                    this.textEl.innerHTML = "O servidor está demorando um pouco...";
                    this.textEl.classList.add('visible');
                }
            }, 5000);

            this.timerMessage2 = setTimeout(() => {
                if (this.textEl) {
                    this.textEl.innerHTML = "O servidor estava hibernando (Cold Start). Estamos ligando ele, isso pode levar até 1 minuto. Aguarde...";
                    this.textEl.classList.add('visible');
                }
            }, 15000);
        }
    },

    stop() {
        this.activeRequests--;
        if (this.activeRequests < 0) this.activeRequests = 0;

        if (this.activeRequests === 0) {

            clearTimeout(this.timerSpinner);
            clearTimeout(this.timerMessage1);
            clearTimeout(this.timerMessage2);

            if (this.overlayEl) this.overlayEl.classList.remove('visible');
            
            if (this.textEl) {
                this.textEl.classList.remove('visible');
                
                // Reseta o texto para o padrão (para a próxima vez que usar)
                // Espera 300ms (tempo da transição CSS) para trocar o texto invisível
                setTimeout(() => {
                    this.textEl.innerText = "O servidor está demorando um pouco...";
                }, 300);
            }
        }
    }
};

// Inicializa assim que o JS carregar
document.addEventListener("DOMContentLoaded", () => LoadingSystem.init());

async function fetchAuth(url, options = {}) {
    LoadingSystem.start();
    const token = localStorage.getItem("token");

    if (!options.headers) {
        options.headers = {};
    }
    
    if (token) {
        options.headers["Authorization"] = `Bearer ${token}`;
    }
    
    try {

        const resposta = await fetch(url, options);

        if ((resposta.status === 401 || resposta.status === 403) && !options.manualErrorHandling){
            localStorage.removeItem("token");
            localStorage.removeItem("usuario");

            window.location.href = "/login/login.html";

            return null;
        }

        return resposta;

    } finally {
        LoadingSystem.stop();
    }
    
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