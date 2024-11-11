# Exfiltration Client 
Uma ferramenta de exfiltração de dados. Tem como objetivo enviar dados através de protocolos conhecidos de rede, a fim de passar despercebido por sistemas de detecção.
Este é um projeto para colocar em prática meu aprendizado em uma linguagem orientada à objeto. Um server ainda deve ser desenvolvido para receber os dados exfiltrados.

## Modo de uso
```shell
  -p <protocolo>                   # Protocolo a ser utilizado na exfiltração.
                                   # Opções: `dns`, `http`
                                   
  -e <codificação>                 # Tipo de codificação a ser aplicado. 
                                   # Use `b` para base32 e `h` para hexadecimal.
                                   # Exemplo de combinação: `bhbbh`

  -u <URL>                         # Servidor de destino para a exfiltração.
                                   # Para DNS, utilize `exemplo.com`.
                                   # Para HTTP/HTTPS, utilize `http://exemplo.com` ou `https://exemplo.com`.

  -V                               # Exibe a versão do programa.

  -h                               # Exibe esta ajuda e encerra a execução.
```
## To-do list
- Implementar novos tipos de dados além de arquivos;
- Possivelmente converter para uma linguagem de programação mais rápida (C++, por exemplo).

## Funcionalidades
Está sendo desenvolvido:
- Codificação em base32, hexadecimal e múltiplas codificações (várias codificações feitas de forma sequencial);
- Envio dos dados via DNS e HTTP(S).
- No caso do envio via DNS, divisão da string codificada em chunks para serem enviados no subdomínio.

## Organização 
Decidi implementar Strategy Design Pattern para permitir maior escalabilidade da ferramenta (novas codificações e envios).

### Diagrama de classes UML
![exfiltration-client](https://github.com/user-attachments/assets/5064196d-8748-417e-b616-5f90cc846052)
