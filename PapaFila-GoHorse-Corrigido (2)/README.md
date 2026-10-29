# 👾 PapaFila — Seu lanche, sem fila!

Aplicativo Android demonstrativo para a cantina da CESAR School, desenvolvido para o Hackathon Relâmpago — Capítulo 5 de **Engenharia de Software Moderna**, de Marco Tulio Valente.

## O que funciona
- Cardápio por categorias com preços fictícios.
- Adicionar/remover produtos e calcular total do carrinho.
- Confirmar pedido sem cobrança real.
- Gerar código de confirmação para retirada.
- Acompanhar o preparo com contagem regressiva de 10 minutos.
- Simular pedido pronto imediatamente para facilitar a apresentação.
- Confirmar retirada.

**Atenção:** é uma prova de conceito local, sem servidor, banco de dados, pagamento real ou notificação push. O pedido e o cronômetro são mantidos na memória enquanto o processo do aplicativo estiver ativo; fechar o app pode perder o pedido. Os produtos e preços são demonstrativos.

## Como gerar o APK (sem instalar ferramentas)
1. Entre no GitHub e crie um repositório público chamado `PapaFila`.
2. Envie **o conteúdo desta pasta**, incluindo a pasta oculta `.github` (preserve a estrutura de diretórios; não envie apenas o ZIP).
3. Abra a aba **Actions** do repositório, escolha **Gerar APK PapaFila** e clique em **Run workflow**. O envio para a branch `main` também dispara o build automaticamente.
4. Quando a execução terminar, abra o resultado e baixe o artefato **PapaFila-APK**.
5. Extraia o ZIP baixado: o arquivo `app-debug.apk` é o instalador Android. Envie-o ao celular e permita a instalação de apps dessa origem, se solicitado.

O workflow usa o Gradle instalado na própria execução do GitHub Actions (não requer Gradle Wrapper no repositório). O fluxo usa Gradle 8.9, Android Gradle Plugin 8.7.3, Java 17 e Android SDK 35. Também pode ser compilado localmente com `gradle assembleDebug` em um ambiente Android configurado.

## Princípios do Capítulo 5 aplicados

| Princípio | Aplicação concreta |
|---|---|
| Responsabilidade Única (SRP) | `Cart` gerencia quantidades; `OrderService` confirma pedidos; `OrderTracker` atualiza estados e tempo. |
| Ocultamento de informação | O mapa interno do `Cart` e o estado mutável do `Order` são privados. |
| Coesão | As operações de cada serviço tratam de uma única responsabilidade. |
| Baixo acoplamento | A UI usa `CatalogRepository` em vez de depender da implementação concreta do catálogo. |
| Inversão de Dependências | A apresentação declara a dependência pela interface `CatalogRepository`; a implementação `DemoCatalogRepository` pode ser substituída. |
| Integridade conceitual | `Product`, `Cart`, `Order` e `OrderTracker` usam o mesmo vocabulário do domínio em todo o projeto. |
| Composição em vez de herança | A tela compõe serviços (`Cart`, `OrderService`, `OrderTracker`) sem criar subclasses para regras de negócio. |

## Organização
- `model/Product.java`: entidade de produto.
- `data/CatalogRepository.java`: abstração do catálogo.
- `data/DemoCatalogRepository.java`: catálogo demonstrativo.
- `service/Cart.java`: carrinho e totais.
- `service/Order.java`: pedido e estado.
- `service/OrderService.java`: confirmação e código.
- `service/OrderTracker.java`: tempo e progresso.
- `ui/MainActivity.java`: interface Android.

## Roteiro de vídeo (até 3 minutos)
0:00–0:30: mostrar a identidade PapaFila e o problema da fila no intervalo.
0:30–1:30: escolher coxinha e suco, adicionar ao carrinho, confirmar pedido e exibir código.
1:30–1:50: mostrar cronômetro e tocar em **Simular pedido pronto**; confirmar retirada.
1:50–2:50: abrir o código e explicar `Cart` (SRP), `CatalogRepository` (inversão de dependências), `OrderTracker` (coesão) e campos privados (ocultamento).
2:50–3:00: concluir com o benefício: pedir antes, retirar sem fila.

## Equipe — divisão proposta (confirmar contribuições reais)
- **Anamel Thaís:** liderança, desenvolvimento inicial e integração.
- **Maria Giovanna:** revisão de UI, identidade visual e componentes.
- **Everton Luan:** revisão da lógica do carrinho e pedidos.
- **Luan Marcos:** revisão de arquitetura e princípios de projeto.
- **Luann Flô:** validação do cronômetro, estados e retirada.
- **Mirella Albuquerque:** testes funcionais e qualidade.
- **Marina Mendes:** revisão da navegação e experiência do usuário.
- **Rayane Pontes:** revisão da mascote, cores e identidade visual.

## Verificação da Go-Horse
- Testes de regras de negócio em `tests/DomainTest.java` (executados antes do build no GitHub Actions).
- Mascote vetorial original em `app/src/main/res/drawable/monster.xml`.
- O APK **não foi compilado neste ambiente**; verifique o resultado verde na aba Actions e instale no Android antes de gravar o vídeo.
- Na demonstração, o botão **Simular pedido pronto** acelera a espera de 10 minutos; não é um aviso real da cantina.
- O projeto é propositalmente Go-Horse: a `MainActivity` ainda concentra a montagem de telas; pode ser refatorada na entrega 2 e comparada por métricas.
