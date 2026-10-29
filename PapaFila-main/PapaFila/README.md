# PapaFila — Seu lanche, sem fila!

**Prova de conceito Android | Hackathon Relâmpago #1 — CESAR School | Engenharia de Software Moderna, capítulo 5**

## Visão geral

O PapaFila é um aplicativo demonstrativo de pedidos antecipados para a cantina universitária. A proposta é reduzir o tempo gasto na fila durante o intervalo: o estudante seleciona produtos, confirma um pedido e acompanha sua preparação até a retirada mediante um código.

## Funcionalidades

- Cardápio local de salgados, bebidas e combos, com valores ilustrativos.
- Carrinho com adição e remoção de itens e cálculo do total.
- Confirmação de pedido sem cobrança financeira.
- Código de confirmação associado ao pedido.
- Acompanhamento de preparo com estimativa inicial de 10 minutos.
- Alteração simulada do estado para **pronto para retirada** e confirmação de retirada.

**Escopo:** a aplicação é uma PoC offline. Não há autenticação, pagamentos reais, banco de dados, integração com a cantina ou notificações push. Os pedidos permanecem em memória e podem ser perdidos quando o processo é encerrado. O botão de antecipar o preparo existe exclusivamente para demonstração.

## Tecnologias e decisões

| Item | Escolha | Justificativa |
|---|---|---|
| Plataforma | Android nativo | Distribuição em APK para demonstração em celular |
| Linguagem | Java | Modelo de domínio e serviços separados da interface Android |
| Interface | Android SDK, com componentes criados em Java | Evita dependências externas para esta primeira versão |
| Dados | `DemoCatalogRepository` em memória | Simplifica a PoC sem impedir substituição futura |
| Build | Gradle 8.9, Android Gradle Plugin 8.7.3, Java 17, SDK 35 | Compilação reproduzível no GitHub Actions |
| Testes | `tests/DomainTest.java` | Verificação automatizada de regras do domínio |

## Estrutura do projeto

```text
.github/workflows/android-apk.yml   # Integração contínua e geração do APK
app/src/main/java/com/papafila/
  model/Product.java                 # Entidade de produto
  data/CatalogRepository.java       # Contrato de acesso ao catálogo
  data/DemoCatalogRepository.java   # Catálogo de demonstração
  service/Cart.java                  # Itens, quantidades e total
  service/Order.java                 # Dados e estado do pedido
  service/OrderService.java          # Confirmação e código de retirada
  service/OrderTracker.java          # Tempo restante e transições de estado
  ui/MainActivity.java               # Navegação e interface Android
app/src/main/res/drawable/monster.xml # Mascote vetorial
 tests/DomainTest.java               # Testes das regras de negócio
```

## Princípios de projeto — capítulo 5

| Princípio | Evidência no código | Benefício |
|---|---|---|
| Responsabilidade Única | `Cart`, `OrderService` e `OrderTracker` têm funções distintas | Facilita a manutenção isolada das regras |
| Ocultamento de informação | A estrutura interna de `Cart` e o estado mutável de `Order` são encapsulados | Reduz modificações indevidas |
| Coesão | Operações de carrinho e acompanhamento são agrupadas por finalidade | Melhora a compreensão de cada serviço |
| Baixo acoplamento | `MainActivity` utiliza o contrato `CatalogRepository` | Diminui a dependência de detalhes do catálogo |
| Inversão de Dependências | `CatalogRepository` abstrai `DemoCatalogRepository` | Permite substituir a fonte de dados |
| Integridade conceitual | Uso consistente de `Product`, `Cart`, `Order` e estados do pedido | Uniformiza os conceitos do domínio |
| Composição em vez de herança | `MainActivity` combina instâncias de serviços | Reutiliza comportamentos sem hierarquias artificiais |

**Limitação arquitetural da primeira versão:** `MainActivity` ainda concentra a montagem das telas. Essa decisão permite demonstrar o fluxo completo na Go-Horse e oferece um ponto concreto de refatoração para a segunda entrega. A aplicação dos princípios deve ser avaliada no código, não apenas nesta tabela.

## Execução e geração do APK

**Pelo GitHub Actions:** com os arquivos na raiz do repositório, acesse **Actions → Gerar APK PapaFila → Run workflow**. Após a conclusão bem-sucedida, baixe o artefato **PapaFila-APK** e extraia `app-debug.apk`. O workflow executa os testes de domínio antes da compilação.

**Localmente:** em ambiente com Java 17, Android SDK 35 e Gradle 8.9 configurados, execute `gradle assembleDebug` na raiz. O APK de debug será produzido em `app/build/outputs/apk/debug/` se a compilação concluir sem erros.

**Estado de validação:** a compilação Android e a instalação em dispositivo precisam ser confirmadas pelo resultado do workflow e por teste manual; a existência deste repositório não comprova que o APK foi compilado.

## Equipe e responsabilidades

| Integrante | Responsabilidade na organização do projeto |
|---|---|
| Anamel Thaís | Coordenação, implementação inicial e integração |
| Maria Giovanna | Revisão de interface e identidade visual |
| Everton Luan | Revisão do fluxo de carrinho e pedidos |
| Luan Marcos | Revisão de arquitetura e princípios de projeto |
| Luann Flô | Validação de cronômetro e retirada |
| Mirella Albuquerque | Planejamento e execução de testes funcionais |
| Marina Mendes | Avaliação de usabilidade e navegação |
| Rayane Pontes | Revisão do mascote e da linguagem visual |

A tabela registra a **distribuição de responsabilidades planejada**. As contribuições efetivamente concluídas devem ser confirmadas e atualizadas pela equipe antes da entrega.

## Limitações e evolução prevista

Esta versão prioriza um fluxo funcional de ponta a ponta e a separação inicial das regras de negócio. Na próxima iteração, a equipe poderá extrair responsabilidades de `MainActivity`, ampliar testes, melhorar a identidade visual e comparar métricas de qualidade do código entre as versões.
