const params = new URLSearchParams(window.location.search);
const id = params.get("id");

const URL_API_PLANO = "https://simulador-financeiro-fullstack.onrender.com/planos-financeiros";

async function carregarPlanos(id) {

    if (!id) {
        await Modal.alert("Erro de Navegação", "Nenhum plano foi selecionado.");
        window.location.href = "../index.html";
        return;
    }

    try {
        const resposta = await fetchAuth(`${URL_API_PLANO}/${id}`);
        
        console.log("Status da Resposta:", resposta.status); // <--- Quero ver isso
        
        if (!resposta.ok) {
            await Modal.alert("Plano não encontrado", "Este plano não existe ou você não tem permissão.");
            window.location.href = "../index.html";
            return;
        }

        if (resposta === null) {
            window.location.href = "../index.html";
            return;
        }

        const plano = await resposta.json();
        document.getElementById("tituloPlano").innerText = plano.nomePlano;
        document.getElementById("meta").innerText = formatarNumero(plano.metaValor);
        document.getElementById("aporte").innerText = formatarNumero(plano.aporteMensal);
        document.getElementById("juros").innerText = `${plano.taxaJurosAnual}%`;
        const anoMes = calcTime(plano.mesesEstimados);
        document.getElementById("tempoTexto").innerText = `${anoMes}`;
        gerarGrafico(plano.taxaJurosAnual, plano.mesesEstimados, plano.aporteMensal);

        
    } catch (error) {
        await Modal.alert("Erro de Conexão", "Não foi possível carregar os dados. Verifique sua internet.");
        window.location.href = "../index.html";
    }
}

let graficoInstance = null;

function gerarGrafico(taxaJurosAnual, mesesEstimados, aporteMensal) {

    let saldo = 0;
    const taxaMensal = (taxaJurosAnual / 100) / 12;
    const totalMeses = mesesEstimados;
    const aporte = aporteMensal;

    let labels = [];
    let dadosDinheiro = [];

    for (let i = 0; i < totalMeses; i++) {
        labels.push("Mês " + i);

        saldo += aporte;
        saldo = saldo + (saldo * taxaMensal);

        dadosDinheiro.push(saldo);
    }
    
    // --- CONFIGURAÇÃO DO CHART.JS (COMENTADA PARA ESTUDO) ---
    
    const ctx = document.getElementById('meuGrafico').getContext('2d');
    
    // 1. DETECÇÃO DE MOBILE (A Mágica)
    // Se a tela for menor que 600px, consideramos mobile
    const isMobile = window.innerWidth < 600;

    if (graficoInstance) {
        graficoInstance.destroy();
    }

    graficoInstance = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Evolução Patrimonial',
                data: dadosDinheiro,
                borderColor: '#007bff', 
                backgroundColor: 'rgba(0, 123, 255, 0.1)',
                borderWidth: 2.5,
                fill: true,
                tension: 0.4, 
                pointRadius: 0, 
                pointHitRadius: 10, 
                pointHoverRadius: 6 
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: 'index',
                intersect: false,
            },
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#fff',
                    bodyFont: { size: 14 },
                    callbacks: {
                        label: function(context) {
                            let valor = context.parsed.y;
                            return new Intl.NumberFormat('pt-BR', { 
                                style: 'currency', 
                                currency: 'BRL' 
                            }).format(valor);
                        }
                    }
                }
            },
            scales: {
                x: {
                    grid: { display: false },
                    ticks: {
                        // No mobile, mostra no máximo 4 legendas (Mês 0, Final e 2 no meio).
                        // No PC, mostra 10.
                        maxTicksLimit: isMobile ? 4 : 10, 
                        
                        maxRotation: 0,
                        
                        // Garante que o Chart.js pule labels se mesmo assim ficar apertado
                        autoSkip: true,
                        autoSkipPadding: 10,

                        // Reduz a fonte no mobile para caber melhor
                        font: {
                            size: isMobile ? 10 : 12
                        }
                    }
                },
                y: {
                    beginAtZero: false,
                    grid: { color: '#f0f0f0' },
                    ticks: {
                        // Reduz fonte do dinheiro no mobile também
                        font: {
                            size: isMobile ? 10 : 12
                        },
                        callback: function(value) {
                            if (value >= 1000000) return 'R$ ' + (value/1000000).toFixed(1) + 'M';
                            if (value >= 1000) return 'R$ ' + (value/1000).toFixed(0) + 'k';
                            return value;
                        }
                    }
                }
            }
        }
    });
    
}

carregarPlanos(id);