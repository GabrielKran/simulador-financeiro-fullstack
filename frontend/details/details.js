const params = new URLSearchParams(window.location.search);
const id = params.get("id");

const URL_API_PLANO = "https://simulador-financeiro-fullstack.onrender.com/planos-financeiros";

async function carregarPlanos(id) {

    if (!id) {
        alert("Nenhum plano selecionado.");
        window.location.href = "../index.html";
        return;
    }

    try {
        const resposta = await fetchAuth(`${URL_API_PLANO}/${id}`);
        
        console.log("Status da Resposta:", resposta.status); // <--- Quero ver isso
        
        if (!resposta.ok) {
            // Vamos ler o erro que o servidor devolveu (se devolveu algo)
            const textoErro = await resposta.text();
            console.error("Erro do Servidor:", textoErro);
            throw new Error(`Erro HTTP: ${resposta.status}`);
        }

        if (resposta === null) {
            alert("Acesso negado");
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
        console.error("Erro ao mostrar dados", error);
        alert("Erro de conexão");
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
                borderWidth: 2.5, // Linha um pouco mais grossa para destaque
                fill: true,
                tension: 0.4, 
                
                pointRadius: 0, // Tamanho da bolinha padrão é ZERO (invisível)
                pointHitRadius: 10, // Área detectável pelo mouse (maior para facilitar)
                pointHoverRadius: 6 // Quando passar o mouse, a bolinha aparece com tamanho 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: 'index', // Mostra o tooltip do ponto mais próximo do eixo X
                intersect: false, // Não precisa passar o mouse EXATAMENTE na linha
            },
            plugins: {
                legend: { display: false }, // Remove a legenda do topo (já tem título na tela)
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#fff',
                    bodyFont: { size: 14 },
                    callbacks: {
                        label: function(context) {
                            // Formatação BRL no tooltip
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
                    grid: { display: false }, // Remove grade vertical
                    ticks: {
                        maxTicksLimit: 10, // IMPORTANTE: Mostra no máximo 10 rótulos no eixo X (ex: Ano 1, Ano 5, Ano 10...)
                        maxRotation: 0 // Evita que o texto fique girado
                    }
                },
                y: {
                    beginAtZero: false,
                    grid: { color: '#f0f0f0' }, // Grade horizontal bem suave
                    ticks: {
                        callback: function(value) {
                            // Abrevia valores grandes (ex: 1.5M, 500k) para não ocupar espaço
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