import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TCCApp extends Application {
    private Connection connection;
    private boolean isAdmin = false;

    @Override
    public void start(Stage primaryStage) {
        connectToDatabase();
        
        VBox loginLayout = new VBox(10);
        Scene loginScene = new Scene(loginLayout, 300, 200);
        loginLayout.setStyle("-fx-background-color: #2b2b2b;");

        TextField userField = new TextField();
        userField.setPromptText("Usuário");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Senha");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
        loginButton.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passwordField.getText().trim();
            isAdmin = authenticateUser(username, password);
            
            if (!isAdmin && username.equals("user")) {
                showAlert("Informação", "Bem-vindo, usuário! Funcionalidades restritas a leitura.");
            }
            showMainInterface(primaryStage);
        });

        loginLayout.getChildren().addAll(userField, passwordField, loginButton);
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    private void showMainInterface(Stage primaryStage) {
        VBox layout = new VBox(10);
        Scene scene = new Scene(layout, 600, 500);
        layout.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white;");
        
        // Campos para cadastro de TCC (somente para admin)
        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome do Aluno");
        
        TextField cursoField = new TextField();
        cursoField.setPromptText("Curso");

        TextField anoField = new TextField();
        anoField.setPromptText("Ano");

        TextField orientadorField = new TextField();
        orientadorField.setPromptText("Orientador");

        TextField arquivoField = new TextField();
        arquivoField.setPromptText("Caminho do Arquivo");

        TextField temaField = new TextField();
        temaField.setPromptText("Tema do TCC");

        TextField resumoField = new TextField();
        resumoField.setPromptText("Resumo do TCC");

        Button cadastrarButton = new Button("Cadastrar TCC");
        cadastrarButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white;");
        cadastrarButton.setOnAction(e -> {
            String nome = nomeField.getText();
            String curso = cursoField.getText();
            String ano = anoField.getText();
            String orientador = orientadorField.getText();
            String arquivo = arquivoField.getText();
            String tema = temaField.getText();
            String resumo = resumoField.getText();

            if (nome.isEmpty() || curso.isEmpty() || ano.isEmpty() || orientador.isEmpty() || arquivo.isEmpty() || tema.isEmpty() || resumo.isEmpty()) {
                showAlert("Erro", "Todos os campos devem ser preenchidos.");
                return;
            }

            if (!validateYear(ano)) {
                showAlert("Erro", "O ano deve ser entre 1900 e 2100.");
                return;
            }

            cadastrarTCC(nome, curso, ano, orientador, arquivo, tema, resumo);
            showAlert("Sucesso", "TCC cadastrado com sucesso!");
            clearFields(nomeField, cursoField, anoField, orientadorField, arquivoField, temaField, resumoField);
        });

        Button consultarButton = new Button("Consultar TCCs");
        consultarButton.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white;");
        consultarButton.setOnAction(e -> {
            List<String> tccs = consultarTCCs("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Consulta TCCs");
            alert.setHeaderText(null);
            alert.setContentText(String.join("\n", tccs));
            alert.showAndWait();
        });

        TextField pesquisaNomeField = new TextField();
        pesquisaNomeField.setPromptText("Pesquisar por Nome");

        Button pesquisarNomeButton = new Button("Pesquisar por Nome");
        pesquisarNomeButton.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black;");
        pesquisarNomeButton.setOnAction(e -> {
            String nome = pesquisaNomeField.getText();
            if (nome.isEmpty()) {
                showAlert("Erro", "Preencha o campo de pesquisa por Nome.");
                return;
            }
            List<String> tccs = consultarTCCs(nome);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Consulta TCCs");
            alert.setHeaderText(null);
            alert.setContentText(String.join("\n", tccs));
            alert.showAndWait();
        });

        TextField pesquisaTemaField = new TextField();
        pesquisaTemaField.setPromptText("Pesquisar por Tema");

        Button pesquisarTemaButton = new Button("Pesquisar por Tema");
        pesquisarTemaButton.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black;");
        pesquisarTemaButton.setOnAction(e -> {
            String tema = pesquisaTemaField.getText();
            if (tema.isEmpty()) {
                showAlert("Erro", "Preencha o campo de pesquisa por Tema.");
                return;
            }
            List<String> tccs = consultarTCCs(tema);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Consulta TCCs");
            alert.setHeaderText(null);
            alert.setContentText(String.join("\n", tccs));
            alert.showAndWait();
        });
        
        Button editarButton = new Button("Editar TCC");
        editarButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white;");
        editarButton.setOnAction(e -> {
            String nome = pesquisaNomeField.getText();
            if (nome.isEmpty()) {
                showAlert("Erro", "Preencha o campo de pesquisa por Nome para editar.");
                return;
            }
            editarTCC(nome);
        });

        Button excluirButton = new Button("Excluir TCC");
        excluirButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        excluirButton.setOnAction(e -> {
            String nome = pesquisaNomeField.getText();
            if (nome.isEmpty()) {
                showAlert("Erro", "Preencha o campo de pesquisa por Nome para excluir.");
                return;
            }
            excluirTCC(nome);
            showAlert("Sucesso", "TCC excluído com sucesso!");
        });

        if (isAdmin) {
            layout.getChildren().addAll(nomeField, cursoField, anoField, orientadorField, arquivoField, temaField, resumoField, cadastrarButton, editarButton, excluirButton);
        }
        layout.getChildren().addAll(consultarButton, pesquisaNomeField, pesquisarNomeButton, pesquisaTemaField, pesquisarTemaButton);

        primaryStage.setScene(scene);
        primaryStage.setTitle("TCC App");
        primaryStage.show();
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "251216");
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    private boolean authenticateUser(String username, String password) {
        return username.equals("admin") && password.equals("senha_admin");
    }

    private void cadastrarTCC(String nome, String curso, String ano, String orientador, String arquivo, String tema, String resumo) {
        String query = "INSERT INTO tccs (aluno_nome, curso, ano, orientador, arquivo_path, tema, resumo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            statement.setString(2, curso);
            statement.setString(3, ano);
            statement.setString(4, orientador);
            statement.setString(5, arquivo);
            statement.setString(6, tema);
            statement.setString(7, resumo); // Insere o resumo no banco
            statement.executeUpdate();
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao cadastrar TCC: " + e.getMessage());
        }
    }

    private List<String> consultarTCCs(String filtro) {
        List<String> tccs = new ArrayList<>();
        String query = "SELECT aluno_nome, curso, ano, orientador, arquivo_path, tema, resumo FROM tccs WHERE aluno_nome LIKE ? OR tema LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + filtro + "%");
            statement.setString(2, "%" + filtro + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String alunoNome = resultSet.getString("aluno_nome");
                String curso = resultSet.getString("curso");
                String ano = resultSet.getString("ano");
                String orientador = resultSet.getString("orientador");
                String arquivo = resultSet.getString("arquivo_path");
                String tema = resultSet.getString("tema");
                String resumo = resultSet.getString("resumo"); // Recupera o resumo
                tccs.add(String.format("Nome: %s, Curso: %s, Ano: %s, Orientador: %s, Arquivo: %s, Tema: %s, Resumo: %s", 
                    alunoNome, curso, ano, orientador, arquivo, tema, resumo));
            }
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao consultar TCCs: " + e.getMessage());
        }
        return tccs;
    }

    private void editarTCC(String nome) {
        String query = "SELECT * FROM tccs WHERE aluno_nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Obtem todos os dados do TCC
                int id = resultSet.getInt("id"); // Supondo que você tenha um campo ID
                String novoNome = resultSet.getString("aluno_nome");
                String curso = resultSet.getString("curso");
                String ano = resultSet.getString("ano");
                String orientador = resultSet.getString("orientador");
                String arquivo = resultSet.getString("arquivo_path");
                String tema = resultSet.getString("tema");
                String resumo = resultSet.getString("resumo");

                // Campos para edição
                TextField nomeField = new TextField(novoNome);
                TextField cursoField = new TextField(curso);
                TextField anoField = new TextField(ano);
                TextField orientadorField = new TextField(orientador);
                TextField arquivoField = new TextField(arquivo);
                TextField temaField = new TextField(tema);
                TextField resumoField = new TextField(resumo);

                VBox editLayout = new VBox(10);
                editLayout.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white;");
                Scene editScene = new Scene(editLayout, 400, 400);
                editLayout.getChildren().addAll(
                    new Label("Nome do Aluno"), nomeField,
                    new Label("Curso"), cursoField,
                    new Label("Ano"), anoField,
                    new Label("Orientador"), orientadorField,
                    new Label("Caminho do Arquivo"), arquivoField,
                    new Label("Tema"), temaField,
                    new Label("Resumo"), resumoField
                );

                Button atualizarButton = new Button("Atualizar");
                atualizarButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
                atualizarButton.setOnAction(e -> {
                    atualizarTCC(id, nomeField.getText(), cursoField.getText(), anoField.getText(), orientadorField.getText(), arquivoField.getText(), temaField.getText(), resumoField.getText());
                    showAlert("Sucesso", "TCC atualizado com sucesso!");
                    editLayout.getScene().getWindow().hide(); // Fecha a janela após a atualização
                });
                editLayout.getChildren().add(atualizarButton);

                Stage editStage = new Stage();
                editStage.setScene(editScene);
                editStage.setTitle("Editar TCC");
                editStage.show();
            } else {
                showAlert("Erro", "TCC não encontrado.");
            }
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao buscar TCC: " + e.getMessage());
        }
    }

    private void atualizarTCC(int id, String novoNome, String curso, String ano, String orientador, String arquivo, String tema, String resumo) {
        String query = "UPDATE tccs SET aluno_nome = ?, curso = ?, ano = ?, orientador = ?, arquivo_path = ?, tema = ?, resumo = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, novoNome);
            statement.setString(2, curso);
            statement.setString(3, ano);
            statement.setString(4, orientador);
            statement.setString(5, arquivo);
            statement.setString(6, tema);
            statement.setString(7, resumo);
            statement.setInt(8, id); // Usar o ID do TCC para a atualização
            statement.executeUpdate();
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao atualizar TCC: " + e.getMessage());
        }
    }

    private void excluirTCC(String nome) {
        String query = "DELETE FROM tccs WHERE aluno_nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            statement.executeUpdate();
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao excluir TCC: " + e.getMessage());
        }
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private boolean validateYear(String ano) {
        try {
            int year = Integer.parseInt(ano);
            return year >= 1900 && year <= 2100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
