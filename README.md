
Biblioteca TCCs Software
Descrição: Sistema de gerenciamento de trabalhos acadêmicos, permitindo cadastrar, consultar, editar e excluir TCCs com interface gráfica e autenticação de usuários.

Tecnologias: Java, JavaFX, MySQL, JDBC.

Funcionalidades
Cadastro de TCCs (somente admin):

Nome do aluno, curso, ano, orientador, tema, resumo e arquivo.
Validação de campos e ano entre 1900 e 2100.
Consulta de TCCs:

Pesquisa por nome ou tema.
Exibição de todos os TCCs com detalhes (nome, curso, ano, orientador, tema, resumo, e caminho do arquivo).
Edição de TCCs (somente admin):

Atualização de informações existentes.
Exclusão de TCCs (somente admin):

Remoção de registros por nome.
Autenticação de Usuários:

Login diferenciado para administradores e usuários comuns.
Como Usar
Clone o Repositório:


git clone https://github.com/SEU_USUARIO/BibliotecaTCCs.git
Configure o Banco de Dados:

Crie um banco de dados chamado biblioteca no MySQL.
Configure a tabela tccs com os seguintes campos:
sql

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
Ajuste as credenciais de conexão no método connectToDatabase no código.
Compile e Execute:

Certifique-se de ter o JavaFX configurado no ambiente.
Compile e execute o programa:

javac TCCApp.java
java TCCApp
Estrutura do Projeto
TCCApp.java: Arquivo principal contendo todas as funcionalidades da aplicação.
Banco de Dados: MySQL usado para armazenar os registros dos TCCs.
Interface Gráfica: Desenvolvida em JavaFX.
Exemplo de Uso
Login: Insira admin como usuário e senha_admin como senha para acesso total.
Consulta: Pesquise TCCs por nome ou tema e visualize os detalhes.
Cadastro: Adicione novos TCCs preenchendo os campos obrigatórios.
Edição: Atualize informações de TCCs já cadastrados.
Exclusão: Remova registros de TCCs.
Dependências
Java 8 ou superior
MySQL
JDBC
JavaFX
