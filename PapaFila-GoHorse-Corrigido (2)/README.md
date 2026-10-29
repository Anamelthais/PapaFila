# 👾 PapaFila — Seu lanche, sem fila!

**Prova de Conceito Android | Hackathon Relâmpago #1 — CESAR School**

**Disciplina:** Fundamentos de Desenvolvimento de Software  
**Referência:** Engenharia de Software Moderna — Marco Tulio Valente, Capítulo 5

## 1. Visão Geral

O PapaFila é um aplicativo Android desenvolvido como prova de conceito (PoC) para otimizar o processo de pedidos na cantina da CESAR School.

O projeto surgiu da necessidade de reduzir o tempo gasto em filas durante os intervalos acadêmicos, permitindo que estudantes selecionem produtos antecipadamente, confirmem seus pedidos e acompanhem o preparo até o momento da retirada.

A proposta combina uma interface intuitiva com a aplicação de princípios de projeto de software, priorizando organização, modularidade e facilidade de manutenção.

## 2. Funcionalidades

- Visualização do cardápio de salgados, bebidas e combos.
- Seleção de produtos e gerenciamento do carrinho.
- Cálculo automático do valor total.
- Confirmação de pedidos.
- Geração de código de identificação para retirada.
- Acompanhamento do pedido com estimativa de preparo de 10 minutos.
- Atualização simulada do status para "Pronto para retirada".
- Confirmação da retirada do pedido.

**Escopo da aplicação:** o PapaFila é uma demonstração offline. Os produtos e preços são ilustrativos, não existem cobranças reais e os pedidos são mantidos temporariamente em memória.

## 3. Tecnologias Utilizadas

| Tecnologia | Aplicação |
|---|---|
| Java | Desenvolvimento das regras de negócio |
| Android SDK | Construção da aplicação Android |
| Gradle | Gerenciamento da compilação |
| Git e GitHub | Versionamento e hospedagem do código |
| GitHub Actions | Automação de testes e geração do APK |

A escolha do desenvolvimento Android nativo com Java permite separar as regras de negócio da interface e distribuir o aplicativo como um arquivo APK.

## 4. Arquitetura e Organização

O projeto utiliza uma estrutura modular, separando entidades, acesso a dados, serviços e apresentação.

### Model

- `Product.java`: representa os produtos disponíveis no cardápio.

### Data

- `CatalogRepository.java`: define o contrato para acesso aos produtos.
- `DemoCatalogRepository.java`: fornece os dados demonstrativos do cardápio.

### Service

- `Cart.java`: gerencia produtos, quantidades e valores.
- `Order.java`: representa os dados e estados de um pedido.
- `OrderService.java`: realiza a confirmação e gera o código de retirada.
- `OrderTracker.java`: controla o tempo estimado e as transições de estado.

### UI

- `MainActivity.java`: responsável pela interface Android e interação com o usuário.

Essa organização busca reduzir dependências desnecessárias e facilitar futuras alterações.

## 5. Princípios de Projeto Aplicados — Capítulo 5

### 5.1 Responsabilidade Única (SRP)

As classes possuem responsabilidades específicas.

- `Cart`: gerenciamento do carrinho.
- `OrderService`: confirmação dos pedidos.
- `OrderTracker`: acompanhamento do preparo.

Essa separação permite modificar regras específicas sem concentrar toda a lógica em uma única classe.

### 5.2 Ocultamento de Informação

Os detalhes internos do carrinho e os estados mutáveis dos pedidos são encapsulados, restringindo o acesso direto a informações que devem ser manipuladas por operações controladas.

### 5.3 Alta Coesão

As operações relacionadas são agrupadas em componentes com objetivos definidos.

Por exemplo, as operações de quantidade e totalização permanecem concentradas na classe `Cart`.

### 5.4 Baixo Acoplamento

A interface utiliza o contrato `CatalogRepository` para acessar os produtos, reduzindo a dependência direta de uma implementação específica do catálogo.

### 5.5 Inversão de Dependências

A interface `CatalogRepository` define uma abstração para acesso aos produtos.

A classe `DemoCatalogRepository` implementa esse contrato, permitindo que a origem dos dados seja substituída futuramente sem alterar o contrato utilizado pela aplicação.

### 5.6 Integridade Conceitual

O projeto utiliza uma nomenclatura consistente para representar os conceitos do domínio, como `Product`, `Cart`, `Order` e `OrderTracker`.

Essa padronização facilita a compreensão e a manutenção do sistema.

### 5.7 Composição em vez de Herança

A aplicação combina serviços especializados para implementar suas funcionalidades, evitando hierarquias de herança desnecessárias para as regras de negócio.

## 6. Como Executar o Projeto

### Compilação pelo GitHub Actions

1. Acessar o repositório público do PapaFila.
2. Abrir a aba **Actions**.
3. Selecionar o workflow **Gerar APK PapaFila**.
4. Executar **Run workflow**.
5. Após a execução bem-sucedida, baixar o artefato `PapaFila-APK`.
6. Extrair o arquivo `app-debug.apk` e instalá-lo em um dispositivo Android.

### Compilação local

Em um ambiente com Java 17, Android SDK 35 e Gradle 8.9 configurados, executar:

`gradle assembleDebug`

O APK será gerado em `app/build/outputs/apk/debug/`, caso a compilação seja concluída com sucesso.

**Validação:** a compilação e a instalação em dispositivo Android ainda precisam ser verificadas.

## 7. Equipe e Responsabilidades

| Integrante | Responsabilidade |
|---|---|
| Anamel Thaís | Coordenação, desenvolvimento inicial e integração |
| Maria Giovanna | Revisão da interface e identidade visual |
| Everton Luan | Revisão da lógica de carrinho e pedidos |
| Luan Marcos | Análise da arquitetura e princípios de projeto |
| Luann Flô | Validação do acompanhamento e retirada |
| Mirella Albuquerque | Planejamento e execução de testes funcionais |
| Marina Mendes | Avaliação da navegação e experiência do usuário |
| Rayane Pontes | Revisão da mascote e identidade visual |

A divisão apresenta as responsabilidades planejadas para a equipe, sujeitas à confirmação das contribuições realizadas.

## 8. Limitações e Evolução do Projeto

Por se tratar de uma versão Go-Horse, o PapaFila prioriza a implementação do fluxo principal de pedidos e a aplicação inicial dos princípios de projeto.

A classe `MainActivity` ainda concentra parte da construção das telas, representando uma oportunidade de refatoração.

Para a próxima versão, estão previstas melhorias na separação das responsabilidades, ampliação dos testes automatizados, aprimoramento da interface e comparação de métricas de qualidade de software.

## 9. Considerações Finais

O PapaFila demonstra como conceitos de Engenharia de Software podem ser aplicados à solução de um problema cotidiano do ambiente universitário.

Além de propor uma alternativa às filas da cantina, o projeto explora princípios fundamentais para o desenvolvimento de sistemas organizados, compreensíveis e preparados para evolução.
