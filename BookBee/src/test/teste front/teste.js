function alterarData(tipo) {
    var elementoData = (tipo === 'inicio') ? document.getElementById("dataInicio") : document.getElementById("dataFim");
    var botao = (tipo === 'inicio') ? document.getElementById("inicioButton") : document.getElementById("fimButton");
    
    // Criar um campo de entrada para a data
    var input = document.createElement("input");
    input.type = "text";
    input.value = elementoData.textContent;

    // Substituir o texto pelo campo de entrada
    botao.innerHTML = '';
    botao.appendChild(input);

    // Definir o foco no campo de entrada
    input.focus();

    // Definir um evento para capturar a nova data
    input.addEventListener("blur", function () {
        if (isValidDate(input.value)) {
            elementoData.textContent = input.value;
            botao.innerHTML = tipo === 'inicio' ? `<span id="dataInicio">${input.value}</span>` : `<span id="dataFim">${input.value}</span>`;
        } else {
            alert("Por favor, digite uma data válida no formato dd/mm/yyyy.");
            botao.innerHTML = tipo === 'inicio' ? `<span id="dataInicio">${elementoData.textContent}</span>` : `<span id="dataFim">${elementoData.textContent}</span>`;
        }
    });

    // Adicionar um evento para a tecla Enter
    input.addEventListener("keyup", function (event) {
        if (event.key === "Enter") {
            if (isValidDate(input.value)) {
                elementoData.textContent = input.value;
                botao.innerHTML = tipo === 'inicio' ? `Data de Início: <span id="dataInicio">${input.value}</span>` : `Data de Fim: <span id="dataFim">${input.value}</span>`;
            } else {
                alert("Por favor, digite uma data válida no formato dd/mm/yyyy.");
                botao.innerHTML = tipo === 'inicio' ? `Data de Início: <span id="dataInicio">${elementoData.textContent}</span>` : `Data de Fim: <span id="dataFim">${elementoData.textContent}</span>`;
            }
        }
    });

    // Função para validar a data
    function isValidDate(dateString) {
        var regex = /^\d{2}\/\d{2}\/\d{4}$/;
        if (!regex.test(dateString)) return false;
        var parts = dateString.split("/");
        var day = parseInt(parts[0], 10);
        var month = parseInt(parts[1], 10);
        var year = parseInt(parts[2], 10);
        if (year < 1000 || year > 3000 || month == 0 || month > 12) return false;
        var daysInMonth = new Date(year, month, 0).getDate();
        return day <= daysInMonth;
    }
}
