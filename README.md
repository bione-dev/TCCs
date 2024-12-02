---

# Biblioteca TCCs Software

## Descrição
Sistema de gerenciamento de trabalhos acadêmicos (TCCs), com funcionalidades para cadastrar, consultar, editar e excluir registros de TCCs, além de uma interface gráfica e autenticação de usuários.

## Tecnologias Utilizadas
- **Java**: Linguagem principal para o desenvolvimento.
- **JavaFX**: Biblioteca para a criação da interface gráfica.
- **MySQL**: Banco de dados utilizado para armazenamento dos TCCs.
- **JDBC**: Conector para integrar a aplicação Java com o banco de dados MySQL.

## Funcionalidades
### 1. Cadastro de TCCs (Somente Administradores)
- **Campos**: Nome do aluno, curso, ano, orientador, tema, resumo, arquivo.
- **Validação**: Verificação dos campos obrigatórios e ano entre 1900 e 2100.

### 2. Consulta de TCCs
- **Pesquisa**: Por nome ou tema.
- **Exibição**: Lista de TCCs com detalhes completos (nome, curso, ano, orientador, tema, resumo e caminho do arquivo).

### 3. Edição de TCCs (Somente Administradores)
- **Funcionalidade**: Atualização de informações existentes dos TCCs cadastrados.

### 4. Exclusão de TCCs (Somente Administradores)
- **Remoção**: Exclusão de registros de TCCs por nome.

### 5. Autenticação de Usuários
- **Login**: Diferença de acesso entre administradores e usuários comuns.

## Como Usar

### 1. Clone o Repositório
```bash
git clone https://github.com/SEU_USUARIO/BibliotecaTCCs.git
```

### 2. Configure o Banco de Dados
1. Crie um banco de dados chamado `biblioteca` no MySQL.
2. Crie a tabela `tccs` com o seguinte SQL:
    ```sql
    CREATE TABLE tccs (
        id INT AUTO_INCREMENT PRIMARY KEY,
        aluno_nome VARCHAR(255) NOT NULL,
        curso VARCHAR(255) NOT NULL,
        ano INT NOT NULL,
        orientador VARCHAR(255) NOT NULL,
        arquivo_path VARCHAR(255),
        tema VARCHAR(255) NOT NULL,
        resumo TEXT NOT NULL
    );
    ```
3. Ajuste as credenciais de conexão no método `connectToDatabase` no código fonte.

### 3. Compile e Execute
1. Certifique-se de ter o JavaFX configurado no seu ambiente.
2. Compile e execute o programa com os seguintes comandos:
    ```bash
    javac TCCApp.java
    java TCCApp
    ```

## Estrutura do Projeto
- **TCCApp.java**: Arquivo principal contendo todas as funcionalidades da aplicação.
- **Banco de Dados**: MySQL usado para armazenar os registros dos TCCs.
- **Interface Gráfica**: Desenvolvida com JavaFX.

## Exemplo de Uso
1. **Login**: Use `admin` como usuário e `senha_admin` como senha para acesso total.
2. **Consulta**: Pesquise TCCs por nome ou tema e visualize os detalhes.
3. **Cadastro**: Adicione novos TCCs preenchendo os campos obrigatórios.
4. **Edição**: Atualize informações de TCCs já cadastrados.
5. **Exclusão**: Remova registros de TCCs.

## Dependências
- **Java 8 ou superior**
- **MySQL**
- **JDBC**
- **JavaFX**
