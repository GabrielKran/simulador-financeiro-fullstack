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

// ==========================================
// MÁSCARAS E FORMATAÇÃO
// ==========================================

function formatarNumero(valor) {
    return new Intl.NumberFormat("pt-BR", {
        currency: "BRL",
        style: "currency"
    }).format(valor);
}

// 1. Aplica a máscara visual (R$) ENQUANTO o usuário digita
function mascaraMoeda(event) {
    const input = event.target;
    
    // Remove tudo que não for dígito (0-9)
    let valor = input.value.replace(/\D/g, ""); 
    
    if (valor === "") {
        input.value = "";
        return;
    }

    // Divide por 100 para considerar os centavos (Ex: 1500 vira 15.00)
    const numero = parseFloat(valor) / 100;

    input.value = formatarNumero(numero);
}

// 2. Limpa a formatação (tira o R$) para enviar ao Backend
function limparValorMoeda(valorFormatado) {
    if (!valorFormatado) return 0;
    
    // Remove tudo que não é dígito
    const apenasNumeros = valorFormatado.replace(/\D/g, "");
    
    // Divide por 100 para voltar a ser um número decimal puro
    return parseFloat(apenasNumeros) / 100;
}

// ==========================================
// SISTEMA DE NOTIFICAÇÕES (TOASTS)
// ==========================================
const Toast = {
    container: null,

    init() {
        if (!document.getElementById('toast-container')) {
            this.container = document.createElement('div');
            this.container.id = 'toast-container';
            document.body.appendChild(this.container);
        } else {
            this.container = document.getElementById('toast-container');
        }
    },

    // Chama assim: Toast.show("Salvo com sucesso!", "success")
    show(message, type = 'info') {
        this.init();

        const toast = document.createElement('div');
        toast.classList.add('toast', type);
        toast.innerText = message;

        // Adiciona ao container
        this.container.appendChild(toast);

        // Remove automaticamente após 4 segundos
        setTimeout(() => {
            toast.classList.add('hiding'); // Começa animação de saída
            toast.addEventListener('animationend', () => {
                toast.remove(); // Remove do HTML quando animação acabar
            });
        }, 4000);
    }
};

// ==========================================
// SISTEMA DE MODAL (CONFIRM & ALERT)
// ==========================================
const Modal = {
    // Para confirmar ações (Sim/Não)
    // Uso: if (await Modal.confirm("Tem certeza?", "Isso apaga tudo!")) { ... }
    confirm(titulo, mensagem) {
        return new Promise((resolve) => {
            const overlay = document.createElement('div');
            overlay.className = 'modal-overlay';
            
            overlay.innerHTML = `
                <div class="modal-box">
                    <h3 class="modal-title">${titulo}</h3>
                    <p class="modal-text">${mensagem}</p>
                    <div class="modal-actions">
                        <button class="btn-modal btn-cancel" id="modal-cancel">Cancelar</button>
                        <button class="btn-modal btn-confirm" id="modal-yes">Confirmar</button>
                    </div>
                </div>
            `;

            document.body.appendChild(overlay);

            // Eventos dos botões
            document.getElementById('modal-yes').onclick = () => {
                overlay.remove();
                resolve(true); // Retorna VERDADEIRO
            };

            document.getElementById('modal-cancel').onclick = () => {
                overlay.remove();
                resolve(false); // Retorna FALSO
            };
        });
    },

    // Para avisos bloqueantes (Sessão Expirada)
    // Uso: await Modal.alert("Sessão Expirada", "Faça login novamente.")
    alert(titulo, mensagem) {
        return new Promise((resolve) => {
            const overlay = document.createElement('div');
            overlay.className = 'modal-overlay';
            
            overlay.innerHTML = `
                <div class="modal-box">
                    <h3 class="modal-title">${titulo}</h3>
                    <p class="modal-text">${mensagem}</p>
                    <div class="modal-actions">
                        <button class="btn-modal btn-ok" id="modal-ok">Entendi</button>
                    </div>
                </div>
            `;

            document.body.appendChild(overlay);

            document.getElementById('modal-ok').onclick = () => {
                overlay.remove();
                resolve(true);
            };
        });
    }
};

// Inicializa assim que o JS carregar
document.addEventListener("DOMContentLoaded", () => {
    LoadingSystem.init();
    Toast.init();
});